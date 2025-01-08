-- Создание таблицы books
CREATE TABLE book (
                      id BIGSERIAL PRIMARY KEY,                -- Уникальный идентификатор книги
                      title VARCHAR(255) NOT NULL,             -- Название книги
                      author VARCHAR(255) NOT NULL,            -- Автор книги
                      year INT NOT NULL,                       -- Год издания книги
                      owner BIGINT,                            -- Владелец книги (внешний ключ, может быть NULL)
                      CONSTRAINT title_not_empty CHECK (title <> ''),     -- Проверка, чтобы название не было пустым
                      CONSTRAINT author_not_empty CHECK (author <> ''),   -- Проверка, чтобы автор не был пустым
                      CONSTRAINT fk_book_owner FOREIGN KEY (owner) REFERENCES person (id) ON DELETE SET NULL -- Связь с таблицей person
);
