package site.javadev.controllers;

import site.javadev.model.Person;
import site.javadev.security.JwtTokenProvider;
import site.javadev.service.PersonService;
import site.javadev.validation.PersonValidator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PersonValidator personValidator;
    private final PersonService personService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult,
                                      HttpServletResponse response) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        personService.savePerson(person);

        // Автоматический логин для сессии
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(person.getUsername(), person.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Генерация JWT и установка в cookie
        String jwt = jwtTokenProvider.generateToken(person.getUsername());
        Cookie jwtCookie = new Cookie("jwtToken", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3600); // 1 час
        response.addCookie(jwtCookie);

        return "redirect:/books";
    }
}