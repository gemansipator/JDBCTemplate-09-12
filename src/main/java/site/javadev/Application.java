package site.javadev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Аннотация @SpringBootApplication обозначает, что это основной класс приложения Spring Boot.
// Она включает в себя несколько других аннотаций:
// - @Configuration — этот класс является источником конфигурации для Spring.
// - @EnableAutoConfiguration — включает автоконфигурацию Spring.
// - @ComponentScan — позволяет Spring автоматически искать компоненты в пакете и его подкаталогах, например, сервисы, репозитории и контроллеры.
@SpringBootApplication
public class Application {

    // Основной метод запуска приложения.
    public static void main(String[] args) {
        // SpringApplication.run() запускает приложение Spring Boot, создавая и настраивая контекст приложения.
        // Это метод, который находит ваш @SpringBootApplication класс и запускает встроенный сервер.
        SpringApplication.run(Application.class, args);
    }

}
