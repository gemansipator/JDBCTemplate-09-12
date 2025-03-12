package site.javadev.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import site.javadev.model.Person;
import site.javadev.security.JwtTokenProvider;
import site.javadev.security.PersonDetailsService;
import site.javadev.service.PeopleService;
import site.javadev.validation.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PersonDetailsService personDetailsService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("person", new Person());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute("person") Person person,
                           BindingResult bindingResult) {
        System.out.println("Registering person: " + person);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println("Validation errors: " + bindingResult.getAllErrors());
            return "auth/registration";
        }
        peopleService.savePerson(person);
        System.out.println("Redirecting to /auth/login");
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response,
                        Model model) {
        System.out.println("Attempting login for: " + username);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = personDetailsService.loadUserByUsername(username);
            System.out.println("User found: " + userDetails.getUsername() + ", authorities: " + userDetails.getAuthorities());
            String token = jwtTokenProvider.generateToken(userDetails.getUsername());
            Cookie jwtCookie = new Cookie("jwtToken", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3600);
            response.addCookie(jwtCookie);
            System.out.println("Login successful, token: " + token);
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            model.addAttribute("error", "Неправильные имя или пароль");
            return "auth/login";
        }
    }
}