-- Active: 1790621836472@@127.0.0.1@5432@postgres
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50)
);

INSERT INTO usuarios (nome) VALUES ('Marco');

SELECT * FROM usuarios;