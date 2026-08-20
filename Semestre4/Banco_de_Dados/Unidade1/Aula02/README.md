# Aula 02 – Modelos de Dados

## Importância dos Modelos de Dados

Os modelos de dados podem facilitar a interação entre o projetista, o programador e o usuário final.

## Blocos Básicos de Construção de Modelos de Dados

- Entidade
- Atributo
- Relacionamento
- Restrição

### Entidade

É algo (uma pessoa, um local, um objeto, um evento) sobre o qual sejam coletados e armazenados dados.

**Exemplo:** Alunos

### Atributo

É uma característica de uma **entidade**.

### Relacionamento

Descreve uma associação entre entidades.

**Tipos:**

| Tipo | Descrição | Exemplo |
|---|---|---|
| Um para muitos (1:N) | — | Aluno com várias disciplinas ou Disciplina com vários alunos |
| Muitos para muitos (N:N) | — | — |
| Um para um (1:1) | — | — |

### Restrição

É uma limitação imposta aos dados.

## Regra de Negócio

É uma descrição breve, precisa e sem ambiguidades de uma política.

**Exemplos:**

1. Um aluno pode se matricular em muitas disciplinas.
2. Uma disciplina é ofertada para muitos alunos.


1. Um professor pode ministrar muitas disciplinas.
2. Uma disciplina será ministrada exclusivamente por um professor.

**Regras gerais:**

- Um substantivo será traduzido como uma **entidade**.
- Um verbo será traduzido como um **relacionamento**.

---

## Estudo de Caso

> Verificar o PDF "Estudo de Caso"

### Questão 1

- "Um mesmo doador pode, portanto, estar relacionado a várias bolsas de sangue."
- "Cada bolsa de sangue é proveniente de um único doador."
- "Paciente pode receber várias bolsas de sangue."
- "Quando uma bolsa é utilizada, ela é destinada a um único paciente."

### Questão 2

**Entidades:**

- Doador
- Paciente
- Bolsa de Sangue

**Relacionamentos:**

- Doador / Bolsa de Sangue → 1:N
- Paciente / Bolsa de Sangue → 1:N

**Resposta:**

**RN1:** Um **doador** pode *doar* muitas **bolsas de sangue**.
**RN2:** Uma **bolsa de sangue** pode ser doada por exclusivamente um **doador**.

→ Cardinalidade: **1:N**

**RN3:** Um **paciente** pode *receber* várias **bolsas de sangue**.
**RN4:** Uma **bolsa de sangue** pode ser recebida por exclusivamente um **paciente**.

→ Cardinalidade: **1:N**