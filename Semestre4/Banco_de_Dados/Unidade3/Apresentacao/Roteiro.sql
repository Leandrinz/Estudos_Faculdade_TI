CREATE TABLE Alunos (id_aluno INT PRIMARY KEY, nome VARCHAR(50));
CREATE TABLE Disciplinas (id_disciplina INT PRIMARY KEY, nome VARCHAR(50));
CREATE TABLE Matriculas (
  id_aluno INT REFERENCES Alunos(id_aluno),
  id_disciplina INT REFERENCES Disciplinas(id_disciplina)
);



INSERT INTO Alunos VALUES (1,'Ana'),(2,'Bruno'),(3,'Carla');
INSERT INTO Disciplinas VALUES (101,'Banco de Dados'),(102,'Algoritmos'),(103,'Redes');
INSERT INTO Matriculas VALUES (1,101),(1,102),(2,101),(3,103);



SELECT a.nome, m.id_disciplina
FROM Alunos a, Matriculas m;



SELECT a.nome, m.id_disciplina
FROM Alunos a, Matriculas m
WHERE a.id_aluno = m.id_aluno;



SELECT a.nome , d.nome
FROM Alunos a, Matriculas m, Disciplinas d
WHERE a.id_aluno = m.id_aluno
  AND d.id_disciplina = m.id_disciplina;

