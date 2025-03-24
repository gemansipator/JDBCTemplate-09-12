package site.javadev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Указывает, что этот класс является контроллером Spring MVC
public class MainPageController {

    @GetMapping("/")
    public String showMainPage() {
        return "index";
    }
}
