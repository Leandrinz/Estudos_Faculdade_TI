# Aula 03 — Abordagem Entidade-Relacionamento (ER)

## 1. Entidade

Uma **entidade** é um conjunto de objetos da realidade modelada sobre os quais desejamos manter dados no banco de dados.

**Exemplos de entidades:** Pessoa, Cliente, Produto, Departamento, Empregado, Curso.

**Representação:** entidades são representadas por **retângulos** no diagrama ER.

![alt text](image.png)

**Convenção de nomenclatura:** o nome da entidade começa com letra maiúscula (ou é escrito totalmente em maiúsculas). Exemplo: `EMPREGADO` ou `Empregado`.

> *Leitura:* pense na entidade como um "molde" — ela descreve um tipo de coisa que o sistema precisa lembrar (por exemplo, "Cliente"), e não um cliente específico.

### 1.1 Propriedades das entidades

As entidades possuem três propriedades principais:

- **Relacionamento** — como a entidade se associa a outras entidades.
- **Atributos** — as características que descrevem a entidade (ex.: um Empregado tem nome, matrícula, salário).
- **Generalizações/especializações** — hierarquias entre entidades (ex.: "Funcionário" pode se especializar em "Gerente" e "Vendedor").

### 1.2 Ocorrência de entidade

Quando é necessário se referir a um exemplo concreto de uma entidade (um objeto real que ela representa), chamamos isso de **ocorrência de entidade**.

> *Leitura:* se a entidade é "Empregado", uma ocorrência seria "João Silva, matrícula 1023" — ou seja, um empregado específico e real.

---

## 2. Relacionamento

Um **relacionamento** é um conjunto de associações entre ocorrências de entidades.

**Representação:** losango (losângulo), com o nome do relacionamento normalmente escrito no **verbo no infinitivo** (ex.: Contratar, Vender, Alocar).

![alt text](image-1.png)

**Exemplo:**

```
[Proprietário] ----- Contactar ----- [Corretor]
```

Aqui, **Proprietário** e **Corretor** são entidades, e **Contactar** é o relacionamento entre elas (no original das anotações constava "Contatar" por engano — o correto é "Contactar").

> *Leitura:* o relacionamento descreve uma ação ou vínculo que conecta ocorrências de duas (ou mais) entidades — por exemplo, "o Proprietário X contacta o Corretor Y".

---

## 3. Autorrelacionamento

Nem sempre um relacionamento associa entidades diferentes. Quando uma entidade se relaciona **consigo mesma**, temos um **autorrelacionamento**.

**Exemplo clássico:** a entidade `EMPREGADO` pode se relacionar com ela mesma através do relacionamento `Supervisionar` (um empregado supervisiona outro empregado).

```
[Empregado] ----- Supervisionar ----- [Empregado]
```

### 3.1 Papel

Como as duas pontas do relacionamento são a mesma entidade, usamos **papéis (roles)** para diferenciar a função de cada ocorrência dentro do relacionamento.

**Exemplo de papel:**

![alt text](image-2.png)

```
[Empregado] --(supervisor)--- Supervisionar ---(supervisionado)--[Empregado]
```

> *Leitura:* o papel indica "quem faz o quê" dentro do relacionamento — um Empregado participa como **supervisor**, enquanto outro participa como **supervisionado**, mesmo sendo ambos da mesma entidade.

---

## 4. Cardinalidade

**Cardinalidade** é o número (mínimo e máximo) de ocorrências de uma entidade que podem estar associadas a **uma única** ocorrência da entidade em questão, através de um relacionamento.

### 4.1 Cardinalidade máxima

Indica o **maior número possível** de ocorrências associadas.

**Exemplo:**

Um departamento pode estar lotado com um número *n* de empregados.

```
[Departamento] ----- Lotar ----- [Empregado]
      (1)                            (n)
```

A cardinalidade máxima de **Empregado** em relação a Departamento é **n** (muitos).

> *Leitura:* isso significa que um único departamento pode ter vários empregados associados a ele — não há limite fixo definido além de "muitos".

### 4.2 Classificação dos Relacionamentos Binários

A cardinalidade máxima de um relacionamento binário (entre duas entidades) pode ser classificada em três tipos:

- **1:1 (um para um)**
- **1:n (um para muitos)**
- **n:n (muitos para muitos)**

#### Exemplo 1:1

```
Empregado (1) ------ Alocar ------ (1) Mesa
```

> *Leitura:* cada empregado é alocado a **exatamente uma** mesa, e cada mesa é ocupada por **exatamente um** empregado.

#### Exemplo 1:n

```
Aluno (n) ------ Matricular ------ (1) Curso
```

> *Leitura:* um curso pode ter **vários** alunos matriculados, mas cada aluno está matriculado em **um único** curso (nesse contexto específico do exemplo).

#### Exemplo n:n

```
Médico (n) ------ Consultar ------ (n) Paciente
```

> *Leitura:* um médico pode consultar **vários** pacientes, e um paciente pode ser consultado por **vários** médicos diferentes.

### 4.3 Relacionamento Ternário

É um tipo de relacionamento que estabelece uma associação entre **três entidades** simultaneamente (e não apenas duas, como nos relacionamentos binários).

**Exemplo:**

![alt text](image-3.png)

```
                [Fornecedor]
                     |
[Peça] ----- Fornecer ----- [Projeto]
```

> *Leitura:* o relacionamento **Fornecer** liga três entidades ao mesmo tempo — um Fornecedor fornece uma Peça para um Projeto específico. A informação só faz sentido completo quando as três entidades participam juntas da mesma ocorrência do relacionamento.

### 4.4 Cardinalidade Mínima

É o número **mínimo** de ocorrências de uma entidade associadas a uma ocorrência de outra entidade através de um relacionamento.

Duas cardinalidades mínimas são consideradas:

- **Mínima 0** → a participação é **opcional** (pode existir uma ocorrência da entidade sem estar associada a nenhuma ocorrência da outra).
- **Mínima 1** → a participação é **obrigatória** (toda ocorrência da entidade precisa estar associada a pelo menos uma ocorrência da outra).

**Exemplo:**

```
Empregado (0,1) ------- Alocar ------- (1,1) Mesa
```

> *Leitura:* um Empregado pode não ter mesa alocada ainda, mas se tiver, será alocado a **no máximo uma** (mínimo 0, máximo 1). Já toda Mesa **precisa** estar alocada a exatamente um Empregado (mínimo 1, máximo 1) — ou seja, não pode existir mesa sem empregado responsável.

---

## Resumo Visual da Notação

| Elemento | Representação | Exemplo |
|---|---|---|
| Entidade | Retângulo | `EMPREGADO` |
| Relacionamento | Losango, verbo no infinitivo | `Contactar` |
| Cardinalidade | Par (mín, máx) próximo à entidade | `(0,1)` |
| Autorrelacionamento | Entidade ligada a si mesma, com papéis | `Empregado --supervisor--> Empregado` |
| Relacionamento ternário | Losango ligado a 3 entidades | `Fornecedor–Peça–Projeto` |

---
