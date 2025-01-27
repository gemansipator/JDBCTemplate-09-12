package site.javadev.service;

import site.javadev.Model.PersonSecurity;
import site.javadev.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    // Сохранение нового пользователя с ролью "USER" по умолчанию
    public void savePerson(PersonSecurity personSecurity) {
        personSecurity.setPassword(passwordEncoder.encode(personSecurity.getPassword()));
        personSecurity.setRole("ROLE_USER");
        peopleRepository.save(personSecurity);
    }

    // Изменение роли пользователя (только для ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    public void changeUserRole(String username, String role) {
        PersonSecurity user = peopleRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));

        user.setRole(role);
        peopleRepository.save(user);
    }

    // Получение всех пользователей
    public List<PersonSecurity> getAllUsers() {
        return peopleRepository.findAll();
    }
}
