package com.rrtyui.weatherappv2.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {
    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean isCorrectPassword(String source, String target) {
        String encodePassword = encodePassword(source);
        return !BCrypt.checkpw(encodePassword, target);
    }
}
