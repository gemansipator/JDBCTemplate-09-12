package site.javadev.controllers;

import site.javadev.model.Person;
import site.javadev.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/{id}/books") //на руках у текущего пользователя
    public String getBooksOnHand(@PathVariable("id") Long id, Model model) {
        Person person = personService.getPersonById(id);
        model.addAttribute("booksOnHand", person.getBooks());
        return "people/view-books-on-hand";
    }

    // Добавление нового человека POST
    @PreAuthorize("hasRole('ADMIN')") // Доступ только для администраторов
    @PostMapping
    public String createPerson(@ModelAttribute("keyOfNewPerson") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/view-to-create-new-person";
        }
        try {
            // Заполнение обязательных полей, если они не заданы
            if (person.getCreatedAt() == null) {
                person.setCreatedAt(LocalDateTime.now());
            }
            if (person.getCreatedPerson() == null) {
                person.setCreatedPerson("system");
            }
            if (person.getAge() == null) {
                person.setAge(0);
            }
            if (person.getEmail() == null) {
                person.setEmail("default@example.com");
            }
            if (person.getPhoneNumber() == null) {
                person.setPhoneNumber("+1234567890");
            }
            if (person.getPassword() == null) {
                person.setPassword("defaultPassword");
            }
            if (person.getRole() == null) {
                person.setRole("ROLE_USER");
            }

            personService.savePerson(person);
            return "redirect:/people";
        } catch (Exception e) {
            return "people/error-view";
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
            return "people/view-to-edit-person";
        }
        personFromForm.setId(id);

        // Заполнение обязательных полей, если они не заданы
        if (personFromForm.getCreatedAt() == null) {
            personFromForm.setCreatedAt(LocalDateTime.now());
        }
        if (personFromForm.getCreatedPerson() == null) {
            personFromForm.setCreatedPerson("system");
        }
        if (personFromForm.getAge() == null) {
            personFromForm.setAge(0);
        }
        if (personFromForm.getEmail() == null) {
            personFromForm.setEmail("default@example.com");
        }
        if (personFromForm.getPhoneNumber() == null) {
            personFromForm.setPhoneNumber("+1234567890");
        }
        if (personFromForm.getPassword() == null) {
            personFromForm.setPassword("defaultPassword");
        }
        if (personFromForm.getRole() == null) {
            personFromForm.setRole("ROLE_USER");
        }

        personService.updatePerson(personFromForm);
        return "redirect:/people";
    }

    // Удаление человека по ID DELETE
    @PreAuthorize("hasRole('ADMIN')") // Доступ только для администраторов
    @PostMapping("/delete/{id}")
    public String deletePerson(@PathVariable("id") Long id) {
        personService.deletePerson(id); // Удаляем человека
        return "redirect:/people"; // Перенаправление на список людей
    }
}
