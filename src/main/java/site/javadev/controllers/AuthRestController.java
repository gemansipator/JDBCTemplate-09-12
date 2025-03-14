package site.javadev.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.javadev.security.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/auth/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password,
                                        HttpServletResponse response) {
        // Аутентификация пользователя
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Генерация JWT токена
        String token = jwtTokenProvider.generateToken(authentication.getName());

        // Добавление токена в cookie
        Cookie jwtCookie = new Cookie("jwtToken", token);
        jwtCookie.setHttpOnly(true); // Защита от XSS
        jwtCookie.setPath("/"); // Доступность cookie для всего приложения
        jwtCookie.setMaxAge(24 * 60 * 60); // Время жизни cookie: 1 день
        response.addCookie(jwtCookie);

        return ResponseEntity.ok("Logged in successfully");
    }
}