package site.javadev.config;

import site.javadev.security.PersonDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration // Класс является конфигурацией Spring
@EnableWebSecurity // Включает веб-безопасность Spring Security
@EnableMethodSecurity // Включает использование аннотаций для безопасности на уровне методов
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PersonDetailsService personDetailsService) throws Exception {
        http
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
                .userDetailsService(personDetailsService)
                // Указание кастомного UserDetailsService
                .formLogin().loginPage("/auth/login")
                // Указание страницы для формы входа
                .loginProcessingUrl("/process_login")
                // URL для обработки формы входа
                .defaultSuccessUrl("/", true)
                // Перенаправление после успешного входа
                .failureUrl("/auth/login?error")
                // Перенаправление при ошибке входа
                .and()
                .logout()
                .logoutUrl("/logout")
                // URL для выхода
                .logoutSuccessUrl("/auth/login");
        // Перенаправление после выхода

        return http.build();
        // Возвращаем настроенную цепочку фильтров безопасности
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // Создание PasswordEncoder для шифрования паролей
    }
}
