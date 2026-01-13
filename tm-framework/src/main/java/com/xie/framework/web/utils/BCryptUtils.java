package com.xie.framework.web.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtils {

    // 使用 BCryptPasswordEncoder 来加密密码
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 使用 BCrypt 算法加密密码
     *
     * @param plainTextPassword 明文密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String plainTextPassword) {
        // 使用 BCryptPasswordEncoder 进行加密
        return encoder.encode(plainTextPassword);
    }

    /**
     * 检查密码是否与加密后的密码匹配
     *
     * @param plainTextPassword 明文密码
     * @param encryptedPassword 加密后的密码
     * @return 如果匹配返回 true，否则返回 false
     */
    public static boolean checkPassword(String plainTextPassword, String encryptedPassword) {
        // 使用 BCrypt 的 passwordEncoder 来校验密码
        return encoder.matches(plainTextPassword, encryptedPassword);
    }


    public static void main(String[] args) {
        String plainTextPassword = "123456";

        // 使用 Spring Security 的 BCryptPasswordEncoder
        String encryptedPassword = encryptPassword(plainTextPassword);
        System.out.println("Encrypted Password (Spring): " + encryptedPassword);
        System.out.println("Password match (Spring): " + checkPassword(plainTextPassword, encryptedPassword));

    }
}
