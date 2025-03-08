package site.javadev.service;

import site.javadev.model.Person; // Импорт модели Person для работы с сущностью пользователя
import site.javadev.repositories.PersonRepository; // Импорт репозитория для работы с пользователями в базе данных
import org.springframework.stereotype.Service; // Аннотация для пометки класса как сервиса в контексте Spring

import java.util.List; // Импорт коллекции List для работы с множественными объектами

@Service // Аннотация для того, чтобы Spring управлял этим классом как сервисом
public class PersonService {

    private final PersonRepository personRepository; // Репозиторий для работы с пользователями

    // Конструктор, для инъекции зависимостей PersonRepository
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // Получение всех пользователей
    public List<Person> getAllPersons() {
        return personRepository.findAll(); // Возвращает все сущности Person из базы данных
    }

    // Получение пользователя по идентификатору
    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElse(null); // Ищет пользователя по id и возвращает его, если найден, иначе null
    }

    // Сохранение нового пользователя
    public Person savePerson(Person person) {
        return personRepository.save(person); // Сохраняет или обновляет пользователя в базе данных
    }

    // Обновление данных пользователя
    public void updatePerson(Person person) {
        personRepository.save(person); // Сохраняет изменения пользователя в базе данных
    }

    // Удаление пользователя по идентификатору
    public void deletePerson(Long id) {
        personRepository.deleteById(id); // Удаляет пользователя по id из базы данных
    }
}
