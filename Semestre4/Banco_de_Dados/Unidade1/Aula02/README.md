# Aula 02 - Modelos de Dados

## Importância dos Modelos de Dados
Os modelos de dados podem facilitar a iteração entre o projetista, o programador e o usuário final

## Blocos básicos de construção de modelos de dados
- Entidade
- Atributo
- Relacionamento
- Restrição

### Entidade:
É algo (uma pessoa, um local, um objeto, um evento) sobre o qual sejam coletados e armazenados dados

Exemplo: Alunos

### Atributo
É uma característica de uma **entidade**

### Relacionamento
Descreve uma associação entre entidades

#### Tipos
- Um para muitos (1:n) // Aluno com várias disciplinas ou Disciplina com vários alunos
- Muitos para muitos (n:n)
- Um para um (1:1)

### Restrição
É uma limitação imposta aos dados


## Regra de negócio
É uma descrição breve, precisa e sem ambiguidades de uma política

Exemplos:
  - 1. Um aluno pode se matricular em muitas disciplinas
  - 2. Uma disciplina é ofertada para muitos alunos

  - 1. Um professor pode ministrar pode muitas disciplinar
  - 2. Uma disciplina será ministrada exclusivamente por um professor

- Regras gerais:
  - Um substantivo será traduzido como uma entidade
  - Um verbo será traduzido como um relacionamento

# Estudo de Caso
Verificar o PDF "Estudo de Caso"

Questão 1:
"Um mesmo doador pode, portanto, estar relacionado a várias bolsas de
sangue"

"Cada bolsa de sangue é proveniente de um único doador"

"Paciente pode receber várias bolsas de sangue"

"Quando uma bolsa é utilizada, ela é destinada a um
único paciente."

Questão 2:

Entidades:
- Doador
- Paciente
- Bolsas de sangue

Relacionamentos:
- Doador/Bolsa de sangue = 1:N
- Paciente/Bolsa de sangue = 1:N