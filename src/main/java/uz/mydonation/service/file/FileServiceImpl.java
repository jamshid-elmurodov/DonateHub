package uz.mydonation.service.file;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.mydonation.domain.enums.FileType;
import uz.mydonation.domain.exception.BaseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {
    private static final String VIDEOS_PATH = "/Users/jamshidelmurodov/Documents/java/MyDonation/src/main/resources/static/videos/";
    private static final String AUDIOS_PATH = "/Users/jamshidelmurodov/Documents/java/MyDonation/src/main/resources/static/audios/";
    private static final String IMAGES_PATH = "/Users/jamshidelmurodov/Documents/java/MyDonation/src/main/resources/static/images/";

    @Override
    public String uploadFile(MultipartFile file, FileType fileType) {
        String contentType = file.getContentType();

        if (contentType == null) {
            throw new BaseException(
                    "Faylning MIME turi aniqlanmadi",
                    HttpStatus.BAD_REQUEST
            );
        }

        String type = contentType.split("/")[0];

        switch (fileType) {
            case AUDIO -> {
                checkType(fileType, type);
                return saveFile(AUDIOS_PATH, file);
            }
            case VIDEO -> {
                checkType(fileType, type);
                return saveFile(VIDEOS_PATH, file);
            }
            case IMAGE -> {
                checkType(fileType, type);
                return saveFile(IMAGES_PATH, file);
            }
        }

        throw new BaseException(
                "Fayl saqlashda xatolik chiqdi",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private String saveFile(String path, MultipartFile file) {
        if (file.isEmpty()) {
            throw new BaseException(
                    "Fayl bo'sh yoki tanlanmagan",
                    HttpStatus.BAD_REQUEST
            );
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.contains("..")) {
            throw new BaseException(
                    "Fayl nomi noto'g'ri",
                    HttpStatus.BAD_REQUEST
            );
        }

        Path filePath = Path.of(path, fileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Faylni saqlashda xatolik: " + e.getMessage(), e);
        }
    }

    private void checkType(FileType fileType, String type) {
        if (!fileType.getTypes().contains(type)) {
            throw new BaseException(
                    "Faqat " + fileType.getTypes() + " turlaridagi fayllar ruxsat etiladi",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}

