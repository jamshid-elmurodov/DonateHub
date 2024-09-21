package uz.mydonation.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.mydonation.utils.TelegramLogAppender;

@Configuration
public class LogConfig {
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.chatId}")
    private String chatId;

    @Bean
    public TelegramLogAppender telegramLogAppender() {
        TelegramLogAppender telegramAppender = new TelegramLogAppender();
        telegramAppender.setBotToken(botToken);
        telegramAppender.setChatId(chatId);
        telegramAppender.start();

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        Logger customLogger = loggerContext.getLogger("CUSTOM_LOGGER");

        customLogger.addAppender(telegramAppender);
        customLogger.setLevel(Level.DEBUG);

        return telegramAppender;
    }
}
