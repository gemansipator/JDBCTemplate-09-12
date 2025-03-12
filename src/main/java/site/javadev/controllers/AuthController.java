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

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response,
                        Model model) {
        try {
            // Аутентификация пользователя
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // Загрузка данных пользователя
            UserDetails userDetails = personDetailsService.loadUserByUsername(username);

            // Генерация JWT-токена
            String token = jwtTokenProvider.generateToken(userDetails.getUsername());

            // Сохранение токена в cookie
            Cookie jwtCookie = new Cookie("jwtToken", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3600); // 1 час
            response.addCookie(jwtCookie);

            return "redirect:/"; // Перенаправление на главную страницу
        } catch (Exception e) {
            model.addAttribute("error", "Неправильные имя или пароль");
            return "auth/login"; // Возврат на страницу логина с ошибкой
        }
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("person", new Person());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute("person") Person person,
                           BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        peopleService.savePerson(person);
        return "redirect:/auth/login";
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        model.addAttribute("users", peopleService.getAllUsers());
        return "auth/admin";
    }

    @PostMapping("/admin/assign-role")
    public String assignRole(@RequestParam("username") String username,
                             @RequestParam("role") String role,
                             Model model) {
        try {
            peopleService.changeUserRole(username, role);
            model.addAttribute("successMessage", "Роль успешно изменена!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        model.addAttribute("users", peopleService.getAllUsers());
        return "auth/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        return "redirect:/auth/login";
    }
}