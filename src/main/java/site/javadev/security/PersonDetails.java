package site.javadev.security;

import site.javadev.model.PersonSecurity; // Импорт модели PersonSecurity для работы с информацией пользователя
import org.springframework.security.core.GrantedAuthority; // Импорт интерфейса для представления прав доступа
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Импорт реализации прав доступа
import org.springframework.security.core.userdetails.UserDetails; // Импорт интерфейса для работы с деталями пользователя

import java.util.Collection; // Импорт коллекции для хранения авторизационных данных
import java.util.List; // Импорт списка для хранения прав доступа

public class PersonDetails implements UserDetails { // Реализация интерфейса UserDetails для работы с деталями пользователя

    private final PersonSecurity personSecurity; // Поле для хранения информации о пользователе

    // Конструктор для инициализации объекта PersonDetails
    public PersonDetails(PersonSecurity personSecurity) {
        this.personSecurity = personSecurity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // Возвращает права доступа пользователя
        String role = personSecurity.getRole(); // Извлекаем роль пользователя
        return List.of(new SimpleGrantedAuthority(role)); // Создаем и возвращаем список прав доступа
    }

    @Override
    public String getPassword() { // Возвращает пароль пользователя
        return personSecurity.getPassword();
    }

    @Override
    public String getUsername() { // Возвращает имя пользователя
        return personSecurity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { // Проверка, не истек ли срок действия аккаунта
        return true; // Здесь всегда возвращаем true, можно добавить логику для реальной проверки
    }

    @Override
    public boolean isAccountNonLocked() { // Проверка, заблокирован ли аккаунт
        return true; // Здесь всегда возвращаем true, можно добавить логику для реальной проверки
    }

    @Override
    public boolean isCredentialsNonExpired() { // Проверка, не истек ли срок действия учетных данных
        return true; // Здесь всегда возвращаем true, можно добавить логику для реальной проверки
    }

    @Override
    public boolean isEnabled() { // Проверка, активен ли аккаунт
        return true; // Здесь всегда возвращаем true, можно добавить логику для реальной проверки
    }
}
