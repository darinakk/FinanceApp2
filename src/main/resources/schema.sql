DROP TABLE IF EXISTS transactions; -- Ta bort denna först pga foreign keys
DROP TABLE IF EXISTS users;
CREATE TABLE users
(                                           -- Skapar tabellen 'users' i databasen

-- Unikt ID för varje användare
    -- BIGINT = stort heltal
    -- PRIMARY KEY = unik identifierare
    -- AUTO_INCREMENT = ökar automatiskt (1,2,3...)
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    -- Namn på användaren
    -- VARCHAR(255) = text upp till 255 tecken
    name    VARCHAR(255),
    -- Användarens saldo (pengar)
    -- DECIMAL(10,2) = max 10 siffror, 2 decimaler (t.ex. 12345.67)
    -- används för pengar för att undvika avrundningsfel
    balance DECIMAL(10, 2),

    role    ENUM ('USER', 'ADMIN') NOT NULL -- kolla vad enu

);

CREATE TABLE transactions
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    amount  DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

INSERT INTO users (name, balance, role) VALUES ('Darin', 10000000.00,'USER');
INSERT INTO users (name, balance, role) VALUES ('William', 500.00, 'ADMIN');
INSERT INTO users (name,balance, role) VALUES('Mikael', 50000.00, 'USER');
INSERT INTO users (name,balance, role) VALUES ('Bosse', 4500.00, 'USER');




