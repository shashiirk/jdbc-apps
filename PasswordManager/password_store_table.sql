DROP DATABASE IF EXISTS java_db;
CREATE DATABASE java_db;

CREATE TABLE password_store (
    id INT AUTO_INCREMENT NOT NULL,
    account_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    passcode VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (account_name, username)
);