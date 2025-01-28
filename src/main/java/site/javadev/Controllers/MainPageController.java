package site.javadev.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Указывает, что этот класс является контроллером Spring MVC
public class MainPageController {

    @GetMapping("/") // Маршрут для главной страницы (корень веб-приложения)
    public String showMainPage() {
        return "index"; // Возвращает имя представления "index". Thymeleaf будет искать файл "index.html" в папке templates
    }
}
