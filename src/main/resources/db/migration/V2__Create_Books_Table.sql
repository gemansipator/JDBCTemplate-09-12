CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       year INT NOT NULL,
                       owner BIGINT,
                       CONSTRAINT title_not_empty CHECK (title <> ''),
                       CONSTRAINT author_not_empty CHECK (author <> ''),
                       CONSTRAINT fk_book_owner FOREIGN KEY (owner) REFERENCES person(id) ON DELETE SET NULL
);
-- Вставка книги, принадлежащей только что добавленному человеку
INSERT INTO books (title, author, year, owner)
VALUES ('Тайны Вселенной', 'Николай Смирнов', 2023, (SELECT id FROM person WHERE full_name = 'Николай Смирнов' AND birth_year = 1990));