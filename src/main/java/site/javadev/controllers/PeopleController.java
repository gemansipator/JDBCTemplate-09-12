package site.javadev.controllers;

import site.javadev.model.Person;
import site.javadev.service.PeopleService; // Исправлено на PeopleService
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(value = "/people", produces = "text/html; charset=UTF-8")
@RequiredArgsConstructor
public class PeopleController {

    private final PeopleService personService; // Исправлено на PeopleService

    // Получение всех людей GET
    @GetMapping
    public String getAllPeople(Model model) {
        try {
            List<Person> allPersons = personService.getAllReaders(); // Исправлено на getAllReaders
            model.addAttribute("keyAllPeoples", allPersons);
            return "people/view-with-all-people1";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных");
            return "people/error-view";
        }
    }

    // Добавление нового человека GET
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String giveToUserPageToCreateNewPerson(Model model) {
        model.addAttribute("keyOfNewPerson", new Person());
        return "people/view-to-create-new-person";
    }

    @GetMapping("/{id}/books")
    public String getBooksOnHand(@PathVariable("id") Long id, Model model) {
        Person person = personService.getPersonById(id); // Предполагается, что такой метод есть в PeopleService
        model.addAttribute("booksOnHand", person.getBooks());
        return "people/view-books-on-hand";
    }

    // Добавление нового человека POST
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createPerson(@ModelAttribute("keyOfNewPerson") @Valid Person person,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/view-to-create-new-person";
        }
        try {
            // Заполнение обязательных полей
            if (person.getCreatedAt() == null) {
                person.setCreatedAt(LocalDateTime.now());
            }
            if (person.getCreatedPerson() == null) {
                person.setCreatedPerson("system"); // Можно заменить на имя текущего админа
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

            personService.savePerson(person);
            return "redirect:/people";
        } catch (Exception e) {
            return "people/error-view";
        }
    }

    // Получение человека по ID GET
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public String getPersonById(@PathVariable("id") Long id, Model model) {
        Person personById = personService.getPersonById(id);
        if (personById == null) {
            model.addAttribute("errorMessage", "Человек не найден");
            return "people/error-view";
        }
        model.addAttribute("keyPersonById", personById);
        model.addAttribute("books", personById.getBooks());
        return "people/view-with-person-by-id";
    }

    // Редактирование человека по ID GET
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") Long id, Model model) {
        Person personToBeEdited = personService.getPersonById(id);
        if (personToBeEdited == null) {
            model.addAttribute("errorMessage", "Человек не найден");
            return "people/error-view";
        }
        model.addAttribute("keyOfPersonToBeEdited", personToBeEdited);
        return "people/view-to-edit-person";
    }

    // Редактирование человека по ID POST
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") Long id,
                             @ModelAttribute("keyOfPersonToBeEdited") @Valid Person personFromForm,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/view-to-edit-person";
        }
        personFromForm.setId(id);

        // Заполнение обязательных полей
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

        personService.savePerson(personFromForm); // Используем savePerson вместо updatePerson
        return "redirect:/people";
    }

    // Удаление человека по ID POST
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deletePerson(@PathVariable("id") Long id) {
        personService.deletePerson(id); // Предполагается, что такой метод есть
        return "redirect:/people";
    }
}