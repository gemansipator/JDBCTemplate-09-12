-- Создание таблицы person с улучшениями
CREATE TABLE IF NOT EXISTS person (
                                      id BIGSERIAL PRIMARY KEY,
                                      full_name VARCHAR(255) NOT NULL,
                                      birth_year INT NOT NULL,
                                      CONSTRAINT full_name_not_empty CHECK (TRIM(full_name) <> ''),
                                      CONSTRAINT full_name_unique UNIQUE (full_name, birth_year)  -- Уникальность имени и года рождения
);

-- Вставка данных в таблицу person
INSERT INTO person (full_name, birth_year)
VALUES
    ('Николай Смирнов', 1990)
ON CONFLICT (full_name, birth_year) DO NOTHING;  -- Избегаем ошибок при дублировании данных

-- Создание таблицы books с улучшениями
CREATE TABLE IF NOT EXISTS books (
                                     id BIGSERIAL PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
                                     author VARCHAR(255) NOT NULL,
                                     year INT NOT NULL,
                                     owner BIGINT,
                                     CONSTRAINT title_not_empty CHECK (TRIM(title) <> ''),
                                     CONSTRAINT author_not_empty CHECK (TRIM(author) <> ''),
                                     CONSTRAINT fk_book_owner FOREIGN KEY (owner) REFERENCES person(id) ON DELETE SET NULL
);

-- Вставка данных в таблицу books с улучшениями
INSERT INTO books (title, author, year, owner)
VALUES
    ('Тайны Вселенной', 'Николай Смирнов', 2023, (SELECT id FROM person WHERE full_name = 'Николай Смирнов' AND birth_year = 1990))
ON CONFLICT DO NOTHING;  -- Избегаем ошибок при дублировании данных
