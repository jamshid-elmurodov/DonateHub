package donatehub.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import donatehub.utils.TelegramLogAppender;

@Configuration
public class LogConfig {
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.chatId}")
    private String chatId;

    @Bean
    public Logger configureLogger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setContext(loggerContext);
        rollingFileAppender.setName("DAILY_FILE");
        rollingFileAppender.setFile("logs/current-0.log");

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();
        rollingFileAppender.setEncoder(encoder);

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setFileNamePattern("logs/%d{yyyy-MM-dd}.%i.log");
        rollingPolicy.setMaxFileSize(FileSize.valueOf("10MB"));
        rollingPolicy.setMaxHistory(30);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.start();
        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.start();

        TelegramLogAppender telegramAppender = new TelegramLogAppender();
        telegramAppender.setBotToken(botToken);
        telegramAppender.setChatId(chatId);
        telegramAppender.start();

        Logger customLogger = loggerContext.getLogger("CUSTOM_LOGGER");
        customLogger.addAppender(rollingFileAppender);
        customLogger.addAppender(telegramAppender);
        customLogger.setLevel(ch.qos.logback.classic.Level.DEBUG);

        return customLogger;
    }
}