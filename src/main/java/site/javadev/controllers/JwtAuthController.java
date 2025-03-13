package site.javadev.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import site.javadev.security.JwtTokenProvider;
import site.javadev.security.PersonDetailsService;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PersonDetailsService personDetailsService;

    public JwtAuthController(AuthenticationManager authenticationManager,
                             JwtTokenProvider jwtTokenProvider,
                             PersonDetailsService personDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.personDetailsService = personDetailsService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestParam String username,
                                      @RequestParam String password,
                                      HttpServletResponse response) {
        // Аутентификация пользователя
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // Загрузка данных пользователя
        UserDetails userDetails = personDetailsService.loadUserByUsername(username);

        // Генерация JWT-токена
        String token = jwtTokenProvider.generateToken(userDetails.getUsername());

        // Сохраняем токен в cookie
        Cookie jwtCookie = new Cookie("jwtToken", token);
        jwtCookie.setHttpOnly(true); // Защищаем cookie от JavaScript
        jwtCookie.setPath("/"); // Доступно для всего приложения
        jwtCookie.setMaxAge(3600); // Время жизни cookie (1 час, как в настройках JWT)
        response.addCookie(jwtCookie);

        // Перенаправляем на главную страницу
        return ResponseEntity.ok().header("Location", "/").body(Collections.singletonMap("message", "Logged in"));
    }
}