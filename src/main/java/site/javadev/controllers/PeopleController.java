package site.javadev.controllers;

import site.javadev.dao.PersonDao;
import site.javadev.model.Person;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Data
@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDao personDao;

    @Autowired
    public PeopleController(PersonDao personDao) {
        this.personDao = personDao;
    }

    //получение всех людей GET
    @GetMapping
    public String getAllPeople(Model model, HttpServletResponse response) {
        try {
            List<Person> allPeoples = personDao.getAllPeoples();
            model.addAttribute("keyAllPeoples", allPeoples);
            return "people/view-with-all-people1";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных");
            return "people/error-view";
        }
    }

    //добавление нового человека GET
    @GetMapping("/create")
    public String giveToUserPageToCreateNewPerson(Model model) {

        model.addAttribute("keyOfNewPerson", new Person());

        return "people/view-to-create-new-person";
    }

    //добавление нового человека POST
    @PostMapping
    public String createPerson(@ModelAttribute("keyOfNewPerson") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/view-to-create-new-person";
        }
        try {
            personDao.savePerson(person);
            return "redirect:/people";
        } catch (Exception e) {
            return "people/error-view";
        }
    }

    //получение человека по id GET
    @GetMapping("/{id}")
    public String getPersonById(@PathVariable("id") long id, Model model) {

        Person personById = personDao.getPersonById(id);
        model.addAttribute("keyPersonById", personById);
        model.addAttribute("books", personById.getBooks());
        return "people/view-with-person-by-id";
    }

    //редактирование человека по id GET
    @GetMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") long id, Model model) {
        Person personToBeEdited = personDao.getPersonById(id);
        model.addAttribute("keyOfPersonToBeEdited", personToBeEdited);
        return "people/view-to-edit-person";
    }

    //редактирование человека по id POST
    @PostMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") long id,
                             @ModelAttribute("keyOfPersonToBeEdited") @Valid Person personFromForm,

                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/view-to-edit-person";
        }
        personDao.updatePerson(personFromForm);
        return "redirect:/people";
    }

    //удаление человека по id DELETE
    @PostMapping("/delete/{id}")
    public String deletePerson(@PathVariable("id") long id) {
        personDao.deletePerson(id);
        return "redirect:/people";
    }
}
