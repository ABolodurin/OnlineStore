ALTER TABLE users
    DROP COLUMN enabled;

CREATE TABLE authorities
(
    id        serial PRIMARY KEY,
    authority varchar(32)
);

CREATE TABLE users_authorities
(
    id           bigserial PRIMARY KEY,
    authority_id integer     NOT NULL,
    username     varchar(50) NOT NULL,
    CONSTRAINT users_authorities_unq UNIQUE (authority_id, username),

    CONSTRAINT users_authorities_fk_users FOREIGN KEY (username)
        REFERENCES users (username),
    CONSTRAINT users_authorities_fk_authorities FOREIGN KEY (authority_id)
        REFERENCES authorities (id)
);

INSERT INTO authorities (authority)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');

INSERT INTO users_authorities (authority_id, username)
VALUES (1, 'user1'),
       (2, 'user1'),
       (2, 'user2');

UPDATE users
SET password = '$2a$12$Wu2StpAYxJ8qmZrrZD68Q.LZbUa13GR/Y0rmIk1dHJLbTZlnsEECm' /* admin */
WHERE password = '{noop}123';
