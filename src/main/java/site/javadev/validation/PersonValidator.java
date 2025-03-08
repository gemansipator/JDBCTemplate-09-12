package site.javadev.validation;

import site.javadev.model.PersonSecurity;
import site.javadev.security.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// Аннотация @Component говорит Spring, что этот класс является компонентом, который будет автоматически обнаружен и зарегистрирован
// как bean в контексте приложения. Это позволяет использовать PersonValidator в других компонентах или сервисах через инъекцию зависимостей.
@Component
public class PersonValidator implements Validator {

    // Внедряем зависимость для загрузки пользователей по имени
    private final PersonDetailsService personDetailsService;

    // Конструктор для внедрения зависимости PersonDetailsService
    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService; // Присваиваем внедренную зависимость
    }

    // Основной метод валидации. Проверяет, существует ли уже пользователь с таким именем.
    @Override
    public void validate(Object target, Errors errors) {
        // Преобразуем target в объект PersonSecurity, который мы валидируем
        PersonSecurity personSecurity = (PersonSecurity) target;

        try {
            // Пытаемся загрузить пользователя по имени из репозитория
            personDetailsService.loadUserByUsername(personSecurity.getUsername());
        } catch (UsernameNotFoundException e) {
            // Если пользователь с таким именем не найден, это нормально, пропускаем ошибку
            return;
        }

        // Если пользователь найден, добавляем ошибку в объект errors, чтобы уведомить, что такое имя уже занято
        errors.rejectValue("username", "user.found.name",
                "User with name " + personSecurity.getUsername() + " already exists");
    }

    // Метод для проверки, поддерживает ли этот валидатор валидацию для заданного класса
    @Override
    public boolean supports(Class<?> clazz) {
        // Поддерживаем только валидацию для класса PersonSecurity
        return PersonSecurity.class.equals(clazz);
    }
}
/**Комментарии по шагам:
 Аннотация @Component:

 Эта аннотация используется для того, чтобы зарегистрировать класс как компонент
 Spring. Она позволяет автоматически инжектировать данный класс в другие компоненты
 через механизм зависимости Spring.
 Например, Spring будет автоматически создавать и управлять экземплярами
 PersonValidator в контексте приложения, позволяя использовать этот валидатор
 в других компонентах, таких как контроллеры или сервисы.
 Внедрение зависимости через конструктор (@Autowired):

 Мы внедряем зависимость PersonDetailsService, которая позволяет искать пользователей
 по имени. Это необходимая часть логики для валидации, чтобы проверить,
 существует ли уже пользователь с таким именем.
 @Autowired позволяет Spring автоматически передать экземпляр PersonDetailsService
 в конструктор.
 Метод validate(Object target, Errors errors):

 Шаг 1: Мы приводим target (объект, который необходимо валидировать) к типу
 PersonSecurity. Это объект, который передается в валидатор.
 Шаг 2: Пытаемся найти пользователя с таким именем, используя personDetailsService.
 loadUserByUsername(personSecurity.getUsername()).
 Если пользователь не найден, это означает, что имя еще свободно, и ошибок
 не возникает.
 Если пользователь найден, это значит, что имя уже занято, и мы добавляем ошибку
 в объект Errors. Это уведомит контроллер или другую часть приложения, что имя
 уже используется.
 Метод supports(Class<?> clazz):

 Этот метод проверяет, поддерживает ли валидатор проверку для класса PersonSecurity.
 Это необходимо, чтобы гарантировать, что валидатор будет работать только с объектами
 типа PersonSecurity.
 Возвращает true, если класс соответствует PersonSecurity.
 Зачем это нужно:
 @Component: Регистрация валидатора как бина в контексте Spring позволяет нам
 использовать его в других компонентах без необходимости вручную создавать
 экземпляры.
 Внедрение зависимостей: Конструктор с аннотацией @Autowired позволяет Spring
 автоматически передавать необходимые зависимости, такие как PersonDetailsService,
 в наш валидатор, чтобы тот мог выполнять свою работу.
 Валидация: Вся логика проверяет, не существует ли уже пользователя с таким именем,
 чтобы избежать создания нескольких пользователей с одинаковыми именами.*/