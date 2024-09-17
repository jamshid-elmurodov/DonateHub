package uz.mydonation.service.file;

import org.springframework.web.multipart.MultipartFile;
import uz.mydonation.domain.enums.FileType;

public interface FileService {
    String uploadFile(MultipartFile file, FileType fileType);
}
