DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(255)           NOT NULL,
    password VARCHAR(255)           NOT NULL, -- BCrypt-hash, aldrig klartext
    balance  DECIMAL(15, 2)         DEFAULT 0 NOT NULL,
    role     ENUM ('USER', 'ADMIN') NOT NULL,
    UNIQUE (name),
    CHECK (balance >= 0)
);

CREATE TABLE transactions (
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id    BIGINT         NOT NULL,
    type       VARCHAR(20)    NOT NULL,
    amount     DECIMAL(15, 2) NOT NULL,
    created_at DATETIME       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Demoanvändarna skapas av DemoDataLoader så att lösenorden kan hashas med BCrypt
