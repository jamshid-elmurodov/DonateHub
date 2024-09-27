package donatehub.utils;

import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

@UtilityClass
public class TelegramUtils {
    public static String getDataCheckString(Long userId, String firstName, String username, Long authDate) {
        return String.format(
                "auth_date=%d\nfirst_name=%s\nid=%d\nusername=%s",
                authDate, firstName, userId, username
        );
    }

    public boolean verifyAuth(String dataCheckString, String botToken, String receivedHash) {
        String secretKey = sha256(botToken);

        String calculatedHash = hmacSHA256(dataCheckString, secretKey);

        return Objects.equals(receivedHash, calculatedHash);
    }

    private static String sha256(String originalString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
            return toHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String hmacSHA256(String data, String key) {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256HMAC.init(secretKey);

            byte[] hash = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return toHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA-256 hash hisoblashda xatolik yuz berdi", e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
