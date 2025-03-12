package site.javadev.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.javadev.security.JwtAuthenticationFilter;
import site.javadev.security.PersonDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
                .csrf().disable()
                // Убираем STATELESS, используем сессии по умолчанию
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/auth/login", "/auth/registration", "/auth/logout").permitAll()
                        .requestMatchers("/books").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/books/**").hasRole("ADMIN")
                        .requestMatchers("/people").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/people/**").hasRole("ADMIN")
                        .requestMatchers("/auth/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // Настраиваем страницу логина
                .formLogin(form -> form
                        .loginPage("/auth/login") // Страница логина
                        .permitAll()              // Доступна всем
                        .defaultSuccessUrl("/", true) // Куда перенаправить после успешного логина
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
                )
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/auth/login"); // Редирект вместо 403
                })
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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