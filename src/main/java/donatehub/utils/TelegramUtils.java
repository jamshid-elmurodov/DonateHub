package donatehub.utils;

import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class TelegramUtils {
    public static String createDataCheckString(Long userId, String firstName, String username, Long authDate) {
        return String.format(
                "auth_date=%d\nfirst_name=%s\nid=%d\nusername=%s",
                authDate, firstName, userId, username
        );
    }

//    public boolean verifyAuth(String data, String botToken, String hash){
//        try {
//            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//            sha256_HMAC.init(new SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(botToken.getBytes(StandardCharsets.UTF_8)),"SHA256"));
//            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes())).equals(hash);
//        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
//            return false;
//        }
//    }
}
