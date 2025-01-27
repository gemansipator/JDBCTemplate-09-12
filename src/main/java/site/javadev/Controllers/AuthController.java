package site.javadev.Controllers;

import site.javadev.Model.PersonSecurity;
import site.javadev.service.PeopleService;
import site.javadev.validation.PersonValidator;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("personSecurity", new PersonSecurity());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute("personSecurity") PersonSecurity personSecurity,
                           BindingResult bindingResult) {

        personValidator.validate(personSecurity, bindingResult); // проверим есть ли такой пользователь в базе уже

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        peopleService.savePerson(personSecurity);

        return "redirect:/auth/login";
    }


    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        model.addAttribute("users", peopleService.getAllUsers()); // Передаем список пользователей для отображения
        return "auth/admin";
    }

    @PostMapping("/admin/assign-role")
    public String assignRole(@RequestParam("username") String username,
                             @RequestParam("role") String role,
                             Model model) {
        try {
            peopleService.changeUserRole(username, role); // Изменяем роль пользователя
            model.addAttribute("successMessage", "Роль успешно изменена!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        model.addAttribute("users", peopleService.getAllUsers());
        return "auth/admin"; // Возвращаемся на страницу администратора
    }

}
