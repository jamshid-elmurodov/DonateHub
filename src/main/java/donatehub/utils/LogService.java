package donatehub.utils;

import donatehub.domain.exception.BaseException;
import donatehub.domain.response.LogDataRes;
import donatehub.domain.response.LogRes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Component
public class LogService {
    public List<LogRes> getLogFileList(){
        File file = new File("logs");

        File[] files = file.listFiles();

        return Arrays.stream(files).map(f -> new LogRes(f.getName())).toList();
    }

    public List<LogDataRes> getDataOfLog(String fileName) {
        try {
            return Files.readAllLines(Path.of("logs/" + fileName)).stream().map(LogDataRes::new).toList();
        } catch (IOException e) {
            throw new BaseException(
                    fileName + " nomli log file topilmadi",
                    HttpStatus.NOT_FOUND
            );
        }
    }
}
