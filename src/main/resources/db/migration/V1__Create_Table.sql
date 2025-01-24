-- Создание таблицы person с улучшениями
CREATE TABLE IF NOT EXISTS person (
                                      id BIGSERIAL PRIMARY KEY,
                                      full_name VARCHAR(255) NOT NULL,
                                      birth_year INT NOT NULL,
                                      CONSTRAINT full_name_not_empty CHECK (TRIM(full_name) <> ''),
                                      CONSTRAINT full_name_unique UNIQUE (full_name, birth_year)
);

-- Логируем создание таблицы person
DO $$
    BEGIN
        RAISE NOTICE 'Таблица person успешно создана или уже существует.';
    END $$;

-- Вставка данных в таблицу person
INSERT INTO person (full_name, birth_year)
VALUES
    ('Николай Смирнов', 1990)
ON CONFLICT (full_name, birth_year) DO NOTHING; -- Избегаем ошибок при дублировании данных

-- Логируем вставку данных в таблицу person
DO $$
    BEGIN
        RAISE NOTICE 'Данные для Николая Смирнова успешно вставлены или уже существуют.';
    END $$;

-- Создание таблицы users для хранения данных пользователей
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(255) UNIQUE NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     roles TEXT[] NOT NULL,  -- Массив ролей (текстовые значения)
                                     CONSTRAINT role_check CHECK (roles @> ARRAY['ROLE_USER'] OR roles @> ARRAY['ROLE_ADMIN']) -- Проверка, что хотя бы одна из ролей есть
);

-- Логируем создание таблицы users
DO $$
    BEGIN
        RAISE NOTICE 'Таблица users успешно создана или уже существует.';
    END $$;

-- Вставка пользователей с ролями
INSERT INTO users (username, password, roles)
VALUES
    ('admin', '$2a$10$yD8Qm2J9K8kdM7f5cm1tM.g8e0Z/.T1VtXgN5VzFZf3ro7SmZo3Jq', ARRAY['ROLE_ADMIN']), -- Пример пароля: admin123
    ('user', '$2a$10$yD8Qm2J9K8kdM7f5cm1tM.g8e0Z/.T1VtXgN5VzFZf3ro7SmZo3Jq', ARRAY['ROLE_USER'])    -- Пример пароля: user123
ON CONFLICT (username) DO NOTHING; -- Избегаем ошибок при дублировании данных

-- Логируем вставку пользователей
DO $$
    BEGIN
        RAISE NOTICE 'Пользователи успешно вставлены или уже существуют.';
    END $$;

-- Создание таблицы books с улучшениями
CREATE TABLE IF NOT EXISTS books (
                                     id BIGSERIAL PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
                                     author VARCHAR(255) NOT NULL,
                                     year INT NOT NULL,
                                     owner BIGINT,
                                     user_id BIGINT, -- Добавление ссылки на таблицу users
                                     CONSTRAINT title_not_empty CHECK (TRIM(title) <> ''),
                                     CONSTRAINT author_not_empty CHECK (TRIM(author) <> ''),
                                     CONSTRAINT fk_book_owner FOREIGN KEY (owner) REFERENCES person(id) ON DELETE SET NULL,
                                     CONSTRAINT fk_book_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE -- Добавление связи с таблицей users
);

-- Логируем создание таблицы books
DO $$
    BEGIN
        RAISE NOTICE 'Таблица books успешно создана или уже существует.';
    END $$;

-- Вставка данных в таблицу books с проверками
DO $$
    BEGIN
        -- Проверка существования владельца книги в таблице person
        IF NOT EXISTS (SELECT 1 FROM person WHERE full_name = 'Николай Смирнов' AND birth_year = 1990) THEN
            RAISE NOTICE 'Запись для владельца "Николай Смирнов" не найдена в таблице person.';
        ELSE
            RAISE NOTICE 'Запись для владельца "Николай Смирнов" найдена в таблице person.';
        END IF;

        -- Проверка существования пользователя в таблице users
        IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin') THEN
            RAISE NOTICE 'Пользователь "admin" не найден в таблице users.';
        ELSE
            RAISE NOTICE 'Пользователь "admin" найден в таблице users.';
        END IF;
    END $$;

-- Вставка книги в таблицу books
INSERT INTO books (title, author, year, owner, user_id)
VALUES
    ('Тайны Вселенной', 'Николай Смирнов', 2023,
     (SELECT id FROM person WHERE full_name = 'Николай Смирнов' AND birth_year = 1990),
     (SELECT id FROM users WHERE username = 'admin'))
ON CONFLICT DO NOTHING; -- Избегаем ошибок при дублировании данных

-- Логируем вставку данных в таблицу books
DO $$
    BEGIN
        RAISE NOTICE 'Книга "Тайны Вселенной" успешно вставлена в таблицу books.';
    END $$;
