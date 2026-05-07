
CREATE TABLE users (        -- Skapar tabellen 'users' i databasen

-- Unikt ID för varje användare
    -- BIGINT = stort heltal
    -- PRIMARY KEY = unik identifierare
    -- AUTO_INCREMENT = ökar automatiskt (1,2,3...)
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
    -- Namn på användaren
    -- VARCHAR(255) = text upp till 255 tecken
                       name VARCHAR(255),
    -- Användarens saldo (pengar)
    -- DECIMAL(10,2) = max 10 siffror, 2 decimaler (t.ex. 12345.67)
    -- används för pengar för att undvika avrundningsfel
                       balance DECIMAL(10,2)
);
INSERT INTO users (name, balance) VALUES ('Darin', 10000000.00);
INSERT INTO users (name, balance) VALUES ('William', 500.00);
INSERT INTO users (name,balance) VALUES('Mikael', 50000.00);
INSERT INTO users (name,balance) VALUES ('Bosse', 4500.00);

