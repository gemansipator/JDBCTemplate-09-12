package site.javadev.controllers;

import site.javadev.model.PersonSecurity;
import site.javadev.service.PeopleService;
import site.javadev.validation.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller // Указывает, что класс является контроллером Spring MVC
@RequestMapping("/auth") // Указывает базовый путь для всех обработчиков в этом контроллере
@RequiredArgsConstructor // Автоматически генерирует конструктор для всех final полей
public class AuthController {

    private final PersonValidator personValidator; // Валидатор для проверки пользователя
    private final PeopleService peopleService; // Сервис для работы с данными пользователей

    @GetMapping("/login")
    public String login() {
        return "auth/login";
        // Возвращает шаблон страницы для входа
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("personSecurity", new PersonSecurity());
        // Добавляет в модель новый объект PersonSecurity для передачи в форму регистрации
        return "auth/registration";
        // Возвращает шаблон страницы регистрации
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute("personSecurity") PersonSecurity personSecurity,
                           BindingResult bindingResult) {
        personValidator.validate(personSecurity, bindingResult);
        // Выполняет валидацию объекта PersonSecurity (например, проверяет наличие пользователя в базе)

        if (bindingResult.hasErrors()) {
            return "auth/registration";
            // Если есть ошибки валидации, возвращает пользователя на страницу регистрации
        }

        peopleService.savePerson(personSecurity);
        // Сохраняет нового пользователя в базе данных

        return "redirect:/auth/login";
        // Перенаправляет пользователя на страницу входа
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        model.addAttribute("users", peopleService.getAllUsers());
        // Передает список всех пользователей в модель для отображения на странице администратора
        return "auth/admin";
        // Возвращает шаблон страницы администратора
    }

    @PostMapping("/admin/assign-role")
    public String assignRole(@RequestParam("username") String username,
                             @RequestParam("role") String role,
                             Model model) {
        try {
            peopleService.changeUserRole(username, role);
            // Изменяет роль пользователя в базе данных
            model.addAttribute("successMessage", "Роль успешно изменена!");
            // Добавляет сообщение об успешном изменении роли
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка: " + e.getMessage());
            // Добавляет сообщение об ошибке, если что-то пошло не так
        }
        model.addAttribute("users", peopleService.getAllUsers());
        // Обновляет список пользователей после изменения
        return "auth/admin";
        // Возвращает пользователя на страницу администратора
    }
}
