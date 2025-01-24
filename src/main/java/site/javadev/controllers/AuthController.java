package site.javadev.controllers;


import site.javadev.model.PersonSecurity;
import site.javadev.service.PeopleService;
import site.javadev.validation.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.javadev.model.PersonSecurity;

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
    public String getAdminPage() {

        peopleService.doAdminSomething();

        return "auth/admin";
    }

}
