package site.javadev.Controllers;

import site.javadev.Model.Person;
import site.javadev.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // Обозначает, что этот класс является контроллером Spring MVC
@RequestMapping(value = "/people", produces = "text/html; charset=UTF-8") // Базовый URL для работы с людьми
@RequiredArgsConstructor // Автоматическое создание конструктора для final полей
public class PeopleController {

    private final PersonService personService; // Сервис для работы с данными о людях

    // Получение всех людей GET
    @GetMapping
    public String getAllPeople(Model model) {
        try {
            List<Person> allPersons = personService.getAllPersons(); // Получаем список всех людей
            model.addAttribute("keyAllPeoples", allPersons); // Передаем список в модель для отображения
            return "people/view-with-all-people1"; // Шаблон отображения всех людей
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных"); // Ошибка загрузки
            return "people/error-view"; // Шаблон отображения ошибок
        }
    }

    // Добавление нового человека GET
    @PreAuthorize("hasRole('ADMIN')") // Доступ только для администраторов
    @GetMapping("/create")
    public String giveToUserPageToCreateNewPerson(Model model) {
        model.addAttribute("keyOfNewPerson", new Person()); // Добавляем объект для заполнения формы
        return "people/view-to-create-new-person"; // Шаблон для добавления нового человека
    }

    // Добавление нового человека POST
    @PreAuthorize("hasRole('ADMIN')") // Доступ только для администраторов
    @PostMapping
    public String createPerson(@ModelAttribute("keyOfNewPerson") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/view-to-create-new-person"; // Вернуться к форме, если есть ошибки
        }
        try {
            personService.savePerson(person); // Сохраняем нового человека
            return "redirect:/people"; // Перенаправление на список людей
        } catch (Exception e) {
            return "people/error-view"; // Шаблон ошибки
        }
    }

    // Получение человека по ID GET
    @PreAuthorize("hasRole('ADMIN')") // Доступ только для администраторов
    @GetMapping("/{id}")
    public String getPersonById(@PathVariable("id") Long id, Model model) {
        Person personById = personService.getPersonById(id); // Получаем человека по ID
        if (personById == null) {
            model.addAttribute("errorMessage", "Человек не найден"); // Если человек не найден
            return "people/error-view"; // Шаблон ошибки
        }
        model.addAttribute("keyPersonById", personById); // Передаем человека в модель
        model.addAttribute("books", personById.getBooks()); // Добавляем книги, связанные с человеком
        return "people/view-with-person-by-id"; // Шаблон для отображения информации о человеке
    }

    // Редактирование человека по ID GET
    @PreAuthorize("hasRole('ADMIN')") // Доступ только для администраторов
    @GetMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") Long id, Model model) {
        Person personToBeEdited = personService.getPersonById(id); // Получаем данные человека для редактирования
        if (personToBeEdited == null) {
            model.addAttribute("errorMessage", "Человек не найден"); // Если человек не найден
            return "people/error-view"; // Шаблон ошибки
        }
        model.addAttribute("keyOfPersonToBeEdited", personToBeEdited); // Передаем человека в модель
        return "people/view-to-edit-person"; // Шаблон редактирования человека
    }

    // Редактирование человека по ID POST
    @PreAuthorize("hasRole('ADMIN')") // Доступ только для администраторов
    @PostMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") Long id,
                             @ModelAttribute("keyOfPersonToBeEdited") @Valid Person personFromForm,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/view-to-edit-person"; // Вернуться к форме, если есть ошибки
        }
        personFromForm.setId(id); // Устанавливаем ID для обновления
        personService.updatePerson(personFromForm); // Обновляем данные человека
        return "redirect:/people"; // Перенаправление на список людей
    }

    // Удаление человека по ID DELETE
    @PreAuthorize("hasRole('ADMIN')") // Доступ только для администраторов
    @PostMapping("/delete/{id}")
    public String deletePerson(@PathVariable("id") Long id) {
        personService.deletePerson(id); // Удаляем человека
        return "redirect:/people"; // Перенаправление на список людей
    }
}
