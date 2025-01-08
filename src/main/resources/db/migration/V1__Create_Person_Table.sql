CREATE TABLE person (
                        id BIGSERIAL PRIMARY KEY,          -- Уникальный идентификатор человека
                        full_name VARCHAR(255) NOT NULL,   -- Полное имя
                        birth_year INT NOT NULL,           -- Год рождения
                        CONSTRAINT full_name_not_empty CHECK (full_name <> '') -- Проверка, чтобы имя не было пустым
);