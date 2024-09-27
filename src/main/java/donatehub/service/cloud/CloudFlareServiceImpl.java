package donatehub.service.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.enums.FileType;
import donatehub.domain.exception.BaseException;
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
import java.util.Objects;
import java.util.UUID;

@Service
public class CloudFlareServiceImpl implements CloudService {
    private final Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");;

    private static final String BUCKET_NAME = "donatehub";
    private static final String CLOUDFLARE_ACCOUNT_ID = "9162406f34609f0eb2bda06470aa0ae6";
    private static final String ACCESS_KEY = "749edf39504a482763af09c7403b5588";
    private static final String SECRET_KEY = "d091c7871b2a9f26e988b07d6a35bdab68f0a1c8c13124f0d8262ee06510212c";
    private static final String CLOUDFLARE_R2_ENDPOINT = "https://" + CLOUDFLARE_ACCOUNT_ID + ".r2.cloudflarestorage.com";

    private void uploadFile(S3Client s3Client, MultipartFile file, String fileName) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Faylni yuklashda xatolik yuz berdi", e);
        }
    }

    private String getFileUrl(S3Client s3Client, String fileName) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        URL url = s3Client.utilities().getUrl(getUrlRequest);
        return url.toString();
    }

    @Override
    public String uploadFile(MultipartFile file, FileType fileType) {
        log.info("Fayl yuklash boshlandi");
        log.info(file.getOriginalFilename());

        String extension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        checkExtension(extension, fileType);

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);

        S3Client s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(CLOUDFLARE_R2_ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        String fileName = UUID.randomUUID() + "_" + fileType + "." + extension;

        uploadFile(s3Client, file, fileName);

        return getFileUrl(s3Client, fileName);
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