import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for password hashing using SHA-256.
 */
public class SecurityUtils {

    /**
     * Hashes the given password using SHA-256 algorithm.
     *
     * @param password the plain text password
     * @return hashed password as a hexadecimal string, or null if error occurs
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) return null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm not found: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes array of bytes
     * @return hex string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
