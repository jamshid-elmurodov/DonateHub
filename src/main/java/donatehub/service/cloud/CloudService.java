package donatehub.service.cloud;

import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.constants.FileType;

public interface CloudService {
    String uploadFile(MultipartFile file, FileType fileType);
}
