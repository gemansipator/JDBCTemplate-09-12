package site.javadev;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "111"; // ваш пароль
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(encodedPassword);
    }
}
