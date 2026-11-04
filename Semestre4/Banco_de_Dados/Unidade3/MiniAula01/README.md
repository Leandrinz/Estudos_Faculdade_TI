# Mini aula 01 - Cláusulas da linguagem SQL

Nos exemplos usamos esta tabela `funcionarios`:

| id | nome    | cargo      | salario | cidade   | gerente_id |
|----|---------|------------|---------|----------|------------|
| 1  | Ana     | Analista   | 5000    | Recife   | NULL       |
| 2  | Bruno   | Analista   | 4500    | Caruaru  | 1          |
| 3  | Carla   | Gerente    | 9000    | Recife   | NULL       |
| 4  | Diego   | Estagiário | 1800    | Olinda   | 3          |
| 5  | Amanda  | Analista   | 5200    | Caruaru  | 3          |

---

## SELECT | FROM | WHERE

São o "esqueleto" de quase toda consulta.

- **SELECT** → escolhe **quais colunas** você quer ver.
- **SELECT \*** → traz **todas** as colunas.
- **FROM** → diz **de qual tabela** os dados vêm.
- **WHERE** → **filtra as linhas** (só ficam as que atendem à condição).

```sql
-- Só nome e cargo
SELECT nome, cargo
FROM funcionarios;

-- Todas as colunas
SELECT *
FROM funcionarios;

-- Só quem ganha mais de 5000
SELECT nome, salario
FROM funcionarios
WHERE salario > 5000;
```

> 💡 **Dica:** em projetos reais, evite `SELECT *`. Peça só as colunas de que precisa: fica mais rápido e mais claro.

### Operadores úteis no WHERE (faltava nas anotações)

| Operador | Significado       | Exemplo                          |
|----------|-------------------|----------------------------------|
| `=`      | igual             | `cidade = 'Recife'`              |
| `<>` ou `!=` | diferente     | `cargo <> 'Gerente'`             |
| `>` `<` `>=` `<=` | comparações | `salario >= 5000`          |
| `AND`    | as duas condições | `cidade = 'Recife' AND salario > 5000` |
| `OR`     | uma das condições | `cidade = 'Recife' OR cidade = 'Caruaru'` |
| `NOT`    | nega a condição   | `NOT cidade = 'Recife'`          |

---

## AS

Dá um **apelido temporário** a uma coluna, tabela ou resultado de cálculo. O nome só vale durante aquela consulta; **não altera nada no banco**.

```sql
-- Apelido de coluna
SELECT nome AS funcionario, salario * 12 AS salario_anual
FROM funcionarios;

-- Apelido de tabela (muito usado em consultas maiores)
SELECT f.nome, f.cargo
FROM funcionarios AS f;
```

Resultado do primeiro exemplo: as colunas aparecem como `funcionario` e `salario_anual`.

---

## LIMIT e OFFSET

- **LIMIT** → limita **quantas linhas** são retornadas.
- **OFFSET** → **pula** X linhas antes de começar a contar o LIMIT.

```sql
-- Só 3 linhas
SELECT nome FROM funcionarios
LIMIT 3;

-- Pula as 2 primeiras e mostra as próximas 3
SELECT nome FROM funcionarios
LIMIT 3 OFFSET 2;
```

⚠️ **Sem `ORDER BY`, o banco não garante a ordem das linhas.** Então "as 3 primeiras" podem ser diferentes a cada execução. Sempre combine `LIMIT` com `ORDER BY`:

```sql
-- Os 3 maiores salários
SELECT nome, salario
FROM funcionarios
ORDER BY salario DESC
LIMIT 3;
```

> 💡 `LIMIT` + `OFFSET` é a base da **paginação** (página 1, página 2...).
> Em SQL Server usa-se `TOP`, e no Oracle `FETCH FIRST`, mas a ideia é a mesma.

---

## DISTINCT

**Remove linhas duplicadas** do resultado.

```sql
-- Sem DISTINCT: Analista, Analista, Gerente, Estagiário, Analista
SELECT cargo FROM funcionarios;

-- Com DISTINCT: Analista, Gerente, Estagiário
SELECT DISTINCT cargo FROM funcionarios;
```

Útil para descobrir "quais valores diferentes existem" em uma coluna (cargos, cidades, status...).

---

## ORDER BY

**Ordena o resultado final** da consulta.

- `ASC` → crescente (é o padrão)
- `DESC` → decrescente

```sql
-- Do menor para o maior salário
SELECT nome, salario FROM funcionarios
ORDER BY salario ASC;

-- Do maior para o menor
SELECT nome, salario FROM funcionarios
ORDER BY salario DESC;

-- Por cargo (A-Z) e, dentro do cargo, maior salário primeiro
SELECT nome, cargo, salario FROM funcionarios
ORDER BY cargo ASC, salario DESC;
```

---

## LIKE | NOT LIKE

Busca textos por um **padrão**. Usa dois curingas:

- `%` → qualquer quantidade de caracteres (inclusive nenhum)
- `_` → exatamente **um** caractere

### LIKE

```sql
-- Nomes que começam com "A"
SELECT nome FROM funcionarios
WHERE nome LIKE 'A%';          -- Ana, Amanda

-- Nomes que terminam com "o"
WHERE nome LIKE '%o';          -- Bruno, Diego

-- Nomes que contêm "ar"
WHERE nome LIKE '%ar%';        -- Carla

-- Nomes com 3 letras, começando com "A" e terminando com "a"
WHERE nome LIKE 'A_a';         -- Ana
```

### NOT LIKE

**Exclui** o que combina com o padrão.

```sql
-- Todo mundo que NÃO começa com "A"
SELECT nome FROM funcionarios
WHERE nome NOT LIKE 'A%';      -- Bruno, Carla, Diego
```

---

## BETWEEN | NOT BETWEEN

- **BETWEEN** → retorna o que está **dentro de um intervalo** (os extremos **são incluídos**).
- **NOT BETWEEN** → retorna o que está **fora** do intervalo.

```sql
-- Salário entre 4500 e 5200 (inclusive)
SELECT nome, salario FROM funcionarios
WHERE salario BETWEEN 4500 AND 5200;   -- Ana, Bruno, Amanda

-- Salário fora desse intervalo
SELECT nome, salario FROM funcionarios
WHERE salario NOT BETWEEN 4500 AND 5200;   -- Carla, Diego
```

Também funciona com datas: `WHERE data BETWEEN '2026-01-01' AND '2026-03-31'`.

---

## IN | NOT IN

- **IN** → verifica se o valor está **dentro de uma lista**. É um jeito curto de escrever vários `OR`.
- **NOT IN** → o contrário: valor **fora** da lista.

```sql
-- Em vez de: cidade = 'Recife' OR cidade = 'Caruaru'
SELECT nome, cidade FROM funcionarios
WHERE cidade IN ('Recife', 'Caruaru');

-- Quem NÃO está nessas cidades
SELECT nome, cidade FROM funcionarios
WHERE cidade NOT IN ('Recife', 'Caruaru');   -- Diego
```

> ⚠️ **Cuidado:** se a lista do `NOT IN` contiver um `NULL`, a consulta pode não retornar nada. Isso acontece com mais frequência quando o `NOT IN` usa uma subconsulta.

---

## IS NULL | IS NOT NULL

`NULL` significa **"sem valor / desconhecido"**. Não é zero nem texto vazio.

- **IS NULL** → mostra os registros em que o campo **é nulo**.
- **IS NOT NULL** → mostra os registros em que o campo **não é nulo**.

```sql
-- Quem não tem gerente
SELECT nome FROM funcionarios
WHERE gerente_id IS NULL;        -- Ana, Carla

-- Quem tem gerente
SELECT nome FROM funcionarios
WHERE gerente_id IS NOT NULL;    -- Bruno, Diego, Amanda
```

❌ **Erro comum:** `WHERE gerente_id = NULL` **não funciona**. Para NULL, use sempre `IS NULL`.

---

## Bônus: ordem de escrita de uma consulta

As cláusulas precisam seguir esta ordem:

```sql
SELECT DISTINCT cargo, salario
FROM funcionarios
WHERE cidade = 'Recife'
ORDER BY salario DESC
LIMIT 5 OFFSET 0;
```

`SELECT → FROM → WHERE → ORDER BY → LIMIT/OFFSET`

---

## Resumo rápido

| Cláusula | Para que serve |
|----------|----------------|
| `SELECT` | escolher colunas |
| `FROM` | escolher a tabela |
| `WHERE` | filtrar linhas |
| `AS` | apelido temporário |
| `LIMIT / OFFSET` | limitar / pular linhas |
| `DISTINCT` | remover duplicados |
| `ORDER BY` | ordenar |
| `LIKE / NOT LIKE` | buscar por padrão de texto |
| `BETWEEN / NOT BETWEEN` | dentro / fora de um intervalo |
| `IN / NOT IN` | dentro / fora de uma lista |
| `IS NULL / IS NOT NULL` | é / não é nulo |