package donatehub.utils;

import donatehub.domain.exceptions.BaseException;
import donatehub.domain.response.LogDataResponse;
import donatehub.domain.response.LogResponse;
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
    public List<LogResponse> getLogFileList(){
        File file = new File("logs");

        File[] files = file.listFiles();

        return Arrays.stream(files).map(f -> new LogResponse(f.getName())).toList();
    }

    public List<LogDataResponse> getDataOfLog(String fileName) {
        try {
            return Files.readAllLines(Path.of("logs/" + fileName)).stream().map(LogDataResponse::new).toList();
        } catch (IOException e) {
            throw new BaseException(
                    fileName + " nomli log file topilmadi",
                    HttpStatus.NOT_FOUND
            );
        }
    }
}
