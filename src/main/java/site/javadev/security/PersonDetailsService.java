package site.javadev.security;

import site.javadev.model.PersonSecurity; // Импорт модели PersonSecurity для работы с данными пользователя
import site.javadev.repositories.PeopleRepository; // Импорт репозитория для работы с базой данных пользователей
import lombok.RequiredArgsConstructor; // Импорт аннотации для автоматической генерации конструктора
import org.springframework.security.core.userdetails.UserDetails; // Импорт интерфейса для работы с деталями пользователя
import org.springframework.security.core.userdetails.UserDetailsService; // Импорт интерфейса для предоставления пользовательских данных
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Импорт исключения для обработки ошибки, когда пользователь не найден
import org.springframework.stereotype.Service; // Импорт аннотации для определения сервиса

import java.util.Optional; // Импорт Optional для безопасной работы с возможными null-значениями

@Service // Обозначение класса как сервис для внедрения зависимостей
@RequiredArgsConstructor // Генерация конструктора с обязательными параметрами (репозиторий)
public class PersonDetailsService implements UserDetailsService { // Реализация интерфейса UserDetailsService для загрузки данных пользователя

    private final PeopleRepository peopleRepository; // Репозиторий для работы с пользователями в базе данных

    @Override
    public UserDetails loadUserByUsername(String username) { // Метод для загрузки пользователя по имени
        Optional<PersonSecurity> person = peopleRepository.findByUsername(username); // Поиск пользователя в базе данных

        if (person.isEmpty()) { // Если пользователь не найден, выбрасываем исключение
            throw new UsernameNotFoundException("Repository has not found user with username: " + username);
        }

        return new PersonDetails(person.get()); // Если пользователь найден, возвращаем объект PersonDetails
    }
}
