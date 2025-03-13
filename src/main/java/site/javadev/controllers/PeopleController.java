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

import java.util.List;

@Controller
@RequestMapping(value = "/people", produces = "text/html; charset=UTF-8")
@RequiredArgsConstructor
public class PeopleController {
    private final PersonService personService;

    @GetMapping
    public String getAllPeople(Model model) {
        try {
            List<Person> allPersons = personService.getAllPersons();
            model.addAttribute("keyAllPeoples", allPersons);
            return "people/view-with-all-people1";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных");
            return "people/error-view";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String giveToUserPageToCreateNewPerson(Model model) {
        model.addAttribute("keyOfNewPerson", new Person());
        return "people/view-to-create-new-person";
    }

    @GetMapping("/{id}/books")
    public String getBooksOnHand(@PathVariable("id") Long id, Model model) {
        Person person = personService.getPersonById(id);
        model.addAttribute("booksOnHand", person.getBooks());
        return "people/view-books-on-hand";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createPerson(@ModelAttribute("keyOfNewPerson") @Valid Person person,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/view-to-create-new-person";
        }
        try {
            personService.savePerson(person);
            return "redirect:/people";
        } catch (Exception e) {
            return "people/error-view";
        }
    }

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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") Long id,
                             @ModelAttribute("keyOfPersonToBeEdited") @Valid Person personFromForm,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/view-to-edit-person";
        }
        personFromForm.setId(id);
        personService.savePerson(personFromForm);
        return "redirect:/people";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deletePerson(@PathVariable("id") Long id) {
        personService.deletePerson(id);
        return "redirect:/people";
    }

    // Новый метод для списка удаленных пользователей
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deleted")
    public String getAllDeletedPeople(Model model) {
        try {
            List<Person> deletedPersons = personService.getAllDeletedPersons();
            model.addAttribute("keyAllDeletedPeoples", deletedPersons);
            return "people/view-with-deleted-people";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных удаленных пользователей");
            return "people/error-view";
        }
    }

    // Новый метод для восстановления пользователя
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/restore/{id}")
    public String restorePerson(@PathVariable("id") Long id) {
        personService.restorePerson(id);
        return "redirect:/people/deleted";
    }
}