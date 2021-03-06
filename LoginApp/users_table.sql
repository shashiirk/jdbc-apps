DROP DATABASE IF EXISTS java_db;
CREATE DATABASE java_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (email)
);