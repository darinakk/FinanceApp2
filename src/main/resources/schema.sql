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

-- Skapar tabellen 'transactions' för att spara historik över alla händelser
CREATE TABLE transactions
(
    -- Unikt ID för varje enskild transaktion (1, 2, 3...)
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,

    -- Koppling till användaren: Håller koll på VEM som gjorde transaktionen
    -- Måste vara av samma typ (BIGINT) som id i tabellen 'users'
    user_id BIGINT,

    -- Beloppet för transaktionen (t.ex. 500.00 eller -200.00)
    -- DECIMAL(10,2) används för att undvika avrundningsfel med pengar
    amount  DECIMAL(10, 2),

    -- FOREIGN KEY (Främmande nyckel): En säkerhetsspärr i databasen
    -- Den garanterar att vi inte kan skapa en transaktion för ett user_id som inte finns.
    -- Den "länkar" ihop transactions-tabellen med users-tabellen.
    FOREIGN KEY (user_id) REFERENCES users (id)
);

INSERT INTO users (name, balance, role) VALUES ('Darin', 10000000.00,'USER');
INSERT INTO users (name, balance, role) VALUES ('William', 500.00, 'ADMIN');
INSERT INTO users (name,balance, role) VALUES('Mikael', 50000.00, 'USER');
INSERT INTO users (name,balance, role) VALUES ('Bosse', 4500.00, 'USER');




