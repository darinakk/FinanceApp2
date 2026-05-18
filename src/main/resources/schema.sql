DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL, -- Ny kolumn!
                       balance DECIMAL(10, 2),
                       role ENUM ('USER', 'ADMIN') NOT NULL
);

CREATE TABLE transactions (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT,
                              type VARCHAR(20),
                              amount DECIMAL(10, 2),
                              timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Lösenordet är '123' för alla för att göra det enkelt att testa
INSERT INTO users (name, password, balance, role) VALUES ('Darin', '123', 100.00, 'USER');
INSERT INTO users (name, password, balance, role) VALUES ('William', '123', 500.00, 'ADMIN');
INSERT INTO users (name, password, balance, role) VALUES ('Mikael', '123', 50000.00, 'USER');




