#  Aula 11 — Introdução à SQL

> **Tema:** manipulação de dados com a **DML** (*Data Manipulation Language*)

---

##  Sumário

1. [DML: visão geral](#-dml-visão-geral)
2. [Operadores](#-operadores)
3. [INSERT](#-insert)
4. [UPDATE](#-update)
5. [DELETE](#-delete)
6. [Resumo rápido](#-resumo-rápido)

---

##  DML: visão geral

A DML reúne os comandos usados para **manipular os dados** dentro das tabelas:

| Comando  | O que faz                      |
|----------|--------------------------------|
| `INSERT` | Insere novas tuplas (linhas)   |
| `UPDATE` | Altera tuplas existentes       |
| `DELETE` | Remove tuplas existentes       |

---

##  Operadores

###  Operadores aritméticos

| Operador | Operação          | Exemplo        |
|:--------:|-------------------|----------------|
| `+`      | Adição            | `orcamento + 1000` |
| `-`      | Subtração         | `orcamento - 1000` |
| `*`      | Multiplicação     | `orcamento * 1.1`  |
| `/`      | Divisão           | `orcamento / 2`    |
| `%`      | Resto da divisão (módulo) | `10 % 3` → `1` |

###  Operadores relacionais

| Operador       | Significado        | Exemplo                |
|:--------------:|--------------------|------------------------|
| `=`            | Igual a            | `predio = 'DCE-A'`     |
| `<>` ou `!=`   | Diferente de       | `predio <> 'DCE-A'`    |
| `<`            | Menor que          | `orcamento < 500000`   |
| `>`            | Maior que          | `orcamento > 500000`   |
| `<=`           | Menor ou igual a   | `orcamento <= 500000`  |
| `>=`           | Maior ou igual a   | `orcamento >= 500000`  |

###  Operadores lógicos e auxiliares *(bônus)*

Muito usados junto com o `WHERE`:

| Operador     | Uso                                          |
|--------------|----------------------------------------------|
| `AND`        | Todas as condições devem ser verdadeiras     |
| `OR`         | Pelo menos uma condição deve ser verdadeira  |
| `NOT`        | Nega uma condição                            |
| `BETWEEN`    | Valor dentro de um intervalo (inclusivo)     |
| `IN`         | Valor dentro de uma lista                    |
| `LIKE`       | Busca por padrão (`%` = qualquer sequência)  |
| `IS NULL`    | Verifica se o valor é nulo                   |

---

##  INSERT

Permite **inserir uma ou mais tuplas** em uma tabela.

### Sintaxe

```sql
INSERT INTO <tabela> (<atributo1>, <atributo2>, ..., <atributoN>)
VALUES (<valor1>, <valor2>, ..., <valorN>),
       (<valor1>, <valor2>, ..., <valorN>),
       ...
       (<valor1>, <valor2>, ..., <valorN>);
```

### Exemplo

```sql
INSERT INTO departamento (nome_dep, predio, orcamento)
VALUES ('Ciências Exatas', 'DCE-A', 2000000);
```

### Omitindo a lista de atributos

É possível **omitir os nomes das colunas**, mas somente se você informar valores para **todas** as colunas, **na mesma ordem** em que foram definidas na tabela.

```sql
INSERT INTO departamento
VALUES ('Ciências Exatas', 'DCE-A', 2000000);
```

>  **Cuidado:** se a estrutura da tabela mudar (nova coluna, ordem diferente), esse comando pode quebrar ou inserir dados no lugar errado. Listar as colunas é a prática mais segura.

### Inserindo várias linhas de uma vez

```sql
INSERT INTO departamento (nome_dep, predio, orcamento)
VALUES ('Biologia',   'DCB-B', 1500000),
       ('Computação', 'DCC-C', 2500000),
       ('Física',     'DCF-D', 1800000);
```

---

##  UPDATE

Permite **alterar os valores** de atributos de tuplas que já existem.

### Sintaxe

```sql
UPDATE <tabela>
SET <atributo1> = <valor1>,
    <atributo2> = <valor2>
WHERE <condição>;
```

### Exemplo

```sql
UPDATE departamento
SET orcamento = 2500000
WHERE nome_dep = 'Ciências Exatas';
```

Também é possível usar **operadores aritméticos** no `SET`:

```sql
-- Aumenta em 10% o orçamento de todos os departamentos do prédio DCE-A
UPDATE departamento
SET orcamento = orcamento * 1.10
WHERE predio = 'DCE-A';
```

###  WHERE

A cláusula `WHERE` define **quais tuplas serão afetadas**, usando operadores relacionais e lógicos.

```sql
WHERE orcamento > 1000000 AND predio = 'DCE-A'
```

>  **Atenção:** sem o `WHERE`, o `UPDATE` altera **TODAS** as tuplas da tabela!

```sql
--  Isso altera o orçamento de TODOS os departamentos
UPDATE departamento
SET orcamento = 0;
```

---

##  DELETE

Permite **remover tuplas** de uma tabela.

### Sintaxe

```sql
DELETE FROM <tabela>
WHERE <condição>;
```

### Exemplo

```sql
DELETE FROM departamento
WHERE nome_dep = 'Ciências Exatas';
```

Com condição relacional:

```sql
DELETE FROM departamento
WHERE orcamento < 500000;
```

>  **Atenção:** sem o `WHERE`, o `DELETE` remove **TODAS** as tuplas da tabela (a tabela continua existindo, mas vazia).

```sql
--  Apaga todas as linhas de departamento
DELETE FROM departamento;
```

 **Dica de segurança:** antes de rodar um `UPDATE` ou `DELETE`, teste a condição com um `SELECT`:

```sql
SELECT * FROM departamento WHERE orcamento < 500000;
```

---

##  Resumo rápido

| Comando  | Estrutura básica                                      | Precisa de `WHERE`? |
|----------|-------------------------------------------------------|:-------------------:|
| `INSERT` | `INSERT INTO t (cols) VALUES (vals);`                 | Não                 |
| `UPDATE` | `UPDATE t SET col = val WHERE cond;`                  | **Sim** (recomendado) |
| `DELETE` | `DELETE FROM t WHERE cond;`                           | **Sim** (recomendado) |