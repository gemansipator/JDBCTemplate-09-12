package site.javadev.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import site.javadev.security.JwtAuthenticationFilter;
import site.javadev.security.PersonDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Класс является конфигурацией Spring
@EnableWebSecurity // Включает веб-безопасность Spring Security
@EnableMethodSecurity // Включает использование аннотаций для безопасности на уровне методов
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PersonDetailsService personDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, PersonDetailsService personDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Отключаем CSRF, так как используем JWT
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/auth/login", "/auth/registration", "/auth/logout", "/error").permitAll()
                        // Эти URL доступны без авторизации
                        .requestMatchers("/books").hasAnyRole("USER", "ADMIN")
                        // Доступ к /books только для ролей USER и ADMIN
                        .requestMatchers("/books/**").hasRole("ADMIN")
                        // Доступ к /books/** только для роли ADMIN
                        .requestMatchers("/people").hasAnyRole("USER", "ADMIN")
                        // Доступ к /people только для ролей USER и ADMIN
                        .requestMatchers("/people/**").hasRole("ADMIN")
                        // Доступ к /people/** только для роли ADMIN
                        .requestMatchers("/auth/admin/**").hasRole("ADMIN")
                        // Доступ к /auth/admin/** только для роли ADMIN
                        .anyRequest().authenticated())
                // Остальные запросы требуют аутентификации
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Отключаем сессии

        // Добавляем JWT-фильтр перед UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // Создание PasswordEncoder для шифрования паролей
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(personDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}