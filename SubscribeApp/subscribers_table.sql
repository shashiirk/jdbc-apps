DROP DATABASE IF EXISTS java_db;
CREATE DATABASE java_db;

CREATE TABLE subscribers (
    id INT AUTO_INCREMENT NOT NULL,
    email VARCHAR(30) NOT NULL,
    date_joined DATE NOT NULL DEFAULT (CURRENT_DATE),
    PRIMARY KEY (id),
    UNIQUE (email)
);