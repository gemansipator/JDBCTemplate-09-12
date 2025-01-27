package site.javadev.Config;

import site.javadev.security.PersonDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig  {





    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PersonDetailsService personDetailsService) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                    .requestMatchers("/auth/login", "/auth/registration", "/auth/logout", "/error").permitAll()
                    .requestMatchers("/books").hasAnyRole("USER", "ADMIN") // Для простоты только USER и ADMIN
                    .requestMatchers("/books/**").hasRole("ADMIN")
                    .requestMatchers("/people").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/people/**").hasRole("ADMIN")
                    .requestMatchers("/auth/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated())
                .userDetailsService(personDetailsService)
                .formLogin().loginPage("/auth/login") // Используем форму для входа
                .loginProcessingUrl("/process_login") // Используем для обработки формы входа
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login");

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
