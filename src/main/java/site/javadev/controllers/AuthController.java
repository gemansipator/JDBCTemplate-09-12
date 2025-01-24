package site.javadev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Шаблон для страницы логина
    }

    @GetMapping("/")
    public String home() {
        return "home"; // Главная страница
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin"; // Страница для администратора
    }

    @GetMapping("/user")
    public String userPage() {
        return "user"; // Страница для обычного пользователя
    }
}
