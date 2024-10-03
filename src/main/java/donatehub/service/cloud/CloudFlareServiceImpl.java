package donatehub.service.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.constants.FileType;
import donatehub.domain.exceptions.BaseException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

@Service
public class CloudFlareServiceImpl implements CloudService {
    private final Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");

    private static final String BUCKET_NAME = "donatehub";
    private static final String ACCOUNT_ID = "9162406f34609f0eb2bda06470aa0ae6";
    private static final String ACCESS_KEY = "749edf39504a482763af09c7403b5588";
    private static final String SECRET_KEY = "d091c7871b2a9f26e988b07d6a35bdab68f0a1c8c13124f0d8262ee06510212c";
    private static final String CLOUDFLARE_R2_ENDPOINT = "https://" + ACCOUNT_ID + ".r2.cloudflarestorage.com";

    private final AwsBasicCredentials awsCreds = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);

    private final S3Client s3Client = S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create(CLOUDFLARE_R2_ENDPOINT))
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build();

    private void uploadFile(MultipartFile file, String fileName) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new BaseException("Faylni yuklashda xatolik yuz berdi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getFileUrl(String fileName) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        URL url = s3Client.utilities().getUrl(getUrlRequest);
        return url.toString();
    }

    @Override
    public String uploadFile(MultipartFile file, FileType fileType) {
        log.info("Uploading file: {}", file.getOriginalFilename());

        String extension = getFileExtension(file.getOriginalFilename());
        checkExtension(extension, fileType);

        String fileName = UUID.randomUUID() + "_" + fileType + "." + extension;
        uploadFile(file, fileName);

        return getFileUrl(fileName);
    }

    private void checkExtension(String extension, FileType fileType) {
        if (!fileType.getTypes().contains(extension)) {
            throw new BaseException(
                    fileType.name() + " uchun faqat " + fileType.getTypes() + " mumkin",
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE
            );
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}