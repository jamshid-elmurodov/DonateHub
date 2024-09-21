package uz.mydonation.service.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");;

    private static final String VIDEOS_PATH = "/Users/jamshidelmurodov/Documents/java/MyDonation/src/main/resources/static/videos/";
    private static final String AUDIOS_PATH = "/Users/jamshidelmurodov/Documents/java/MyDonation/src/main/resources/static/audios/";
    private static final String IMAGES_PATH = "/Users/jamshidelmurodov/Documents/java/MyDonation/src/main/resources/static/images/";

    @Override
    public String uploadFile(MultipartFile file, FileType fileType) {
        log.info("Fayl yuklash boshlandi: Fayl turi - {}", fileType);

        String contentType = file.getContentType();

        if (contentType == null) {
            log.error("Faylning MIME turi aniqlanmadi");
            throw new BaseException(
                    "Faylning MIME turi aniqlanmadi",
                    HttpStatus.BAD_REQUEST
            );
        }

        String type = contentType.split("/")[0];
        log.info("Fayl turi aniqlandi: {}", type);

        switch (fileType) {
            case AUDIO -> {
                log.info("Audio fayl yuklanmoqda");
                checkType(fileType, type);
                return saveFile(AUDIOS_PATH, file);
            }
            case VIDEO -> {
                log.info("Video fayl yuklanmoqda");
                checkType(fileType, type);
                return saveFile(VIDEOS_PATH, file);
            }
            case IMAGE -> {
                log.info("Rasm fayl yuklanmoqda");
                checkType(fileType, type);
                return saveFile(IMAGES_PATH, file);
            }
            default -> {
                log.error("Fayl turi noto'g'ri: {}", fileType);
                throw new BaseException(
                        "Fayl saqlashda xatolik chiqdi",
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        }
    }

    private String saveFile(String path, MultipartFile file) {
        log.info("Fayl saqlanmoqda: Yo'l - {}", path);

        if (file.isEmpty()) {
            log.error("Bo'sh fayl yuklashga urinish");
            throw new BaseException(
                    "Fayl bo'sh yoki tanlanmagan",
                    HttpStatus.BAD_REQUEST
            );
        }

        String fileName = file.getOriginalFilename();
        log.info("Fayl nomi: {}", fileName);

        if (fileName == null || fileName.contains("..")) {
            log.error("Fayl nomi noto'g'ri: {}", fileName);
            throw new BaseException(
                    "Fayl nomi noto'g'ri",
                    HttpStatus.BAD_REQUEST
            );
        }

        Path filePath = Path.of(path, fileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            log.info("Fayl muvaffaqiyatli saqlandi: {}", filePath.toAbsolutePath());
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            log.error("Faylni saqlashda xatolik: {}", e.getMessage());
            throw new RuntimeException("Faylni saqlashda xatolik: " + e.getMessage(), e);
        }
    }

    private void checkType(FileType fileType, String type) {
        log.info("Fayl turini tekshirish: Kerakli tur - {}, Fayl turi - {}", fileType.getTypes(), type);

        if (!fileType.getTypes().contains(type)) {
            log.error("Fayl turi noto'g'ri: {}", type);
            throw new BaseException(
                    "Faqat " + fileType.getTypes() + " turlaridagi fayllar ruxsat etiladi",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}


