package donatehub.service.file;

import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.enums.FileType;

public interface FileService {
    String uploadFile(MultipartFile file, FileType fileType);
}
