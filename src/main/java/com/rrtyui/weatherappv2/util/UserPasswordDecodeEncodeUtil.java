package com.rrtyui.weatherappv2.util;

import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import org.mindrot.jbcrypt.BCrypt;

public class UserPasswordDecodeEncodeUtil {
    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public static boolean isCorrectPassword (String source, String target) {
        return !BCrypt.checkpw(source, target);
    }
}
