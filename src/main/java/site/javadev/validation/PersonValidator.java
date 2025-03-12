package site.javadev.validation;

import site.javadev.model.PersonSecurity;
import site.javadev.security.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonSecurity.class.equals(clazz); // Поддерживаем только PersonSecurity
    }


    @Override
    public void validate(Object target, Errors errors) {
        PersonSecurity personSecurity = (PersonSecurity) target; // Приводим к PersonSecurity

        try {
            personDetailsService.loadUserByUsername(personSecurity.getUsername());
            // Если пользователь найден, значит имя уже занято
            errors.rejectValue("username", "user.exists", "Пользователь с таким именем уже существует");
        } catch (UsernameNotFoundException e) {
            // Пользователь не найден — это нормально при регистрации
            return;
        }
    }
}