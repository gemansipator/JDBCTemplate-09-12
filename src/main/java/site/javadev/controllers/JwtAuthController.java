package site.javadev.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import site.javadev.security.JwtTokenProvider;
import site.javadev.security.PersonDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PersonDetailsService personDetailsService;

    public JwtAuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PersonDetailsService personDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.personDetailsService = personDetailsService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestParam String username, @RequestParam String password) {
        // Аутентификация пользователя
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // Загрузка данных пользователя
        UserDetails userDetails = personDetailsService.loadUserByUsername(username);

        // Генерация JWT-токена
        String token = jwtTokenProvider.generateToken(userDetails);

        // Возвращаем токен в заголовке Authorization и в теле ответа
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(Collections.singletonMap("token", token));
    }
}