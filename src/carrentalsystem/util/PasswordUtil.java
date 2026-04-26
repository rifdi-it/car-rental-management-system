package carrentalsystem.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(12));
    }
    public static boolean check(String plain, String hashed) {
        if (hashed == null) return false;
        return BCrypt.checkpw(plain, hashed);
    }
}
