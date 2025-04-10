package com.rrtyui.weatherappv2.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {
    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean isCorrectPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
