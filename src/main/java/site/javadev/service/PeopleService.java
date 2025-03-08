package site.javadev.service;

import site.javadev.model.PersonSecurity; // Импорт модели PersonSecurity для работы с пользователями
import site.javadev.repositories.PeopleRepository; // Импорт репозитория PeopleRepository для работы с пользователями в базе данных
import lombok.RequiredArgsConstructor; // Импорт аннотации для автоматической генерации конструктора с зависимостями
import org.springframework.security.access.prepost.PreAuthorize; // Импорт аннотации для контроля доступа к методам
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Импорт исключения для обработки не найденных пользователей
import org.springframework.security.crypto.password.PasswordEncoder; // Импорт интерфейса для кодирования паролей
import org.springframework.stereotype.Service; // Импорт аннотации для пометки класса как сервиса

import java.util.List; // Импорт класса для работы с коллекциями

@Service // Аннотация, помечающая класс как сервис для работы с бизнес-логикой
@RequiredArgsConstructor // Аннотация, которая генерирует конструктор с необходимыми зависимостями
public class PeopleService {

    private final PeopleRepository peopleRepository; // Репозиторий для работы с пользователями
    private final PasswordEncoder passwordEncoder; // Интерфейс для кодирования паролей

    // Сохранение нового пользователя с ролью "USER" по умолчанию
    public void savePerson(PersonSecurity personSecurity) {
        personSecurity.setPassword(passwordEncoder.encode(personSecurity.getPassword())); // Кодируем пароль перед сохранением
        personSecurity.setRole("ROLE_USER"); // Устанавливаем роль пользователя по умолчанию
        peopleRepository.save(personSecurity); // Сохраняем пользователя в базе данных
    }

    // Изменение роли пользователя (только для ADMIN)
    @PreAuthorize("hasRole('ADMIN')") // Аннотация, ограничивающая доступ к методу только пользователям с ролью ADMIN
    public void changeUserRole(String username, String role) {
        PersonSecurity user = peopleRepository.findByUsername(username) // Ищем пользователя по имени
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден")); // Если пользователь не найден, выбрасываем исключение

        user.setRole(role); // Устанавливаем новую роль пользователю
        peopleRepository.save(user); // Сохраняем изменения в базе данных
    }

    // Получение всех пользователей
    public List<PersonSecurity> getAllUsers() {
        return peopleRepository.findAll(); // Возвращаем список всех пользователей из базы данных
    }
}
