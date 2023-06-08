CREATE TABLE products
(
    id    serial,
    title varchar(100),
    price int
);

INSERT INTO products (title, price)
VALUES ('Bread', 40),
       ('Milk', 80);

CREATE TABLE users
(
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled  boolean      NOT NULL,
    PRIMARY KEY (username)
);

INSERT INTO users
VALUES ('user1', '{noop}123', true),
       ('user2', '{noop}123', true);
