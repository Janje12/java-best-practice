CREATE TYPE todo_state AS ENUM ('UNCOMPLETED', 'COMPLETED');

CREATE TABLE todo
(
    id          SERIAL UNIQUE PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    state       todo_state NOT NULL DEFAULT 'UNCOMPLETED',
    CONSTRAINT not_blank CHECK (length(title) > 0)
);