# Mini aula 02 - Funções de agregação

**Funções de agregação** pegam **várias linhas** e devolvem **um único valor** (uma soma, uma média, uma contagem...).

Nos exemplos usamos esta tabela `funcionarios`:

| id | nome   | cargo      | salario | cidade  | bonus |
|----|--------|------------|---------|---------|-------|
| 1  | Ana    | Analista   | 5000    | Recife  | 500   |
| 2  | Bruno  | Analista   | 4500    | Caruaru | NULL  |
| 3  | Carla  | Gerente    | 9000    | Recife  | 1000  |
| 4  | Diego  | Estagiário | 1800    | Olinda  | NULL  |
| 5  | Amanda | Analista   | 5200    | Caruaru | 300   |

---

## SUM

**Somatório**: soma todos os valores de uma coluna numérica.

```sql
SELECT SUM(salario) AS folha_total
FROM funcionarios;
-- Resultado: 25500
```

---

## COUNT

**Faz a contagem.** Tem três formas e cada uma conta uma coisa diferente:

| Forma | O que conta |
|-------|-------------|
| `COUNT(*)` | **todas as linhas** (inclusive com NULL) |
| `COUNT(coluna)` | linhas em que a coluna **não é NULL** |
| `COUNT(DISTINCT coluna)` | valores **diferentes** (sem repetir e sem NULL) |

```sql
SELECT
  COUNT(*)                AS total_linhas,     -- 5
  COUNT(bonus)            AS com_bonus,        -- 3 (Bruno e Diego são NULL)
  COUNT(DISTINCT cargo)   AS cargos_diferentes -- 3
FROM funcionarios;
```

---

## AVG

**Média** (*average*): soma dos valores dividida pela quantidade de valores.

```sql
SELECT AVG(salario) AS media_salarial
FROM funcionarios;
-- Resultado: 5100  (25500 / 5)
```

⚠️ `AVG` **ignora NULL**. Veja a diferença:

```sql
SELECT AVG(bonus) FROM funcionarios;
-- Resultado: 600  → (500 + 1000 + 300) / 3, e não / 5
```

Se quiser tratar NULL como zero, use `COALESCE`: `AVG(COALESCE(bonus, 0))` → 360.

---

## MIN e MAX

Retornam o **menor** e o **maior** valor de uma coluna.

```sql
SELECT
  MIN(salario) AS menor_salario,   -- 1800
  MAX(salario) AS maior_salario    -- 9000
FROM funcionarios;
```

Também funcionam com **datas** (mais antiga / mais recente) e **textos** (ordem alfabética).

---

> ⚠️ **Regra importante (faltava nas anotações):** todas essas funções **ignoram NULL**, exceto o `COUNT(*)`.
>
> Outro cuidado: sem `GROUP BY`, você **não pode misturar** uma coluna comum com uma função de agregação no `SELECT`.
> `SELECT nome, SUM(salario) FROM funcionarios;` ❌ → dá erro, pois o banco não sabe qual `nome` mostrar.

---

## GROUP BY

**Agrupa linhas** que têm o mesmo valor em uma coluna, e a função de agregação é calculada **dentro de cada grupo**.

Pense assim: "para **cada** cargo, me dê a média salarial".

```sql
SELECT cargo, COUNT(*) AS qtd, AVG(salario) AS media
FROM funcionarios
GROUP BY cargo;
```

| cargo      | qtd | media |
|------------|-----|-------|
| Analista   | 3   | 4900  |
| Gerente    | 1   | 9000  |
| Estagiário | 1   | 1800  |

**Regra de ouro:** toda coluna no `SELECT` que **não** está dentro de uma função de agregação **precisa estar no `GROUP BY`**.

Dá para agrupar por mais de uma coluna:

```sql
SELECT cidade, cargo, COUNT(*) AS qtd
FROM funcionarios
GROUP BY cidade, cargo;
```

---

## HAVING vs WHERE

Os dois **filtram**, mas em momentos diferentes:

| | `WHERE` | `HAVING` |
|---|---------|----------|
| Filtra | **linhas**, **antes** de agrupar | **grupos**, **depois** de agrupar |
| Pode usar `SUM`, `COUNT`, `AVG`...? | ❌ Não | ✅ Sim |

**Exemplo 1 - WHERE** (filtra antes de agrupar): média salarial por cargo, considerando só quem mora em Recife.

```sql
SELECT cargo, AVG(salario) AS media
FROM funcionarios
WHERE cidade = 'Recife'
GROUP BY cargo;
```

**Exemplo 2 - HAVING** (filtra depois de agrupar): só os cargos cuja média passa de 4000.

```sql
SELECT cargo, AVG(salario) AS media
FROM funcionarios
GROUP BY cargo
HAVING AVG(salario) > 4000;
-- Analista (4900) e Gerente (9000). Estagiário fica de fora.
```

**Exemplo 3 - os dois juntos:**

```sql
SELECT cidade, COUNT(*) AS qtd
FROM funcionarios
WHERE salario > 2000        -- 1º: tira quem ganha 2000 ou menos
GROUP BY cidade             -- 2º: agrupa por cidade
HAVING COUNT(*) >= 2        -- 3º: mantém só cidades com 2+ pessoas
ORDER BY qtd DESC;          -- 4º: ordena
```

❌ Isto dá erro: `WHERE AVG(salario) > 4000`. Condição sobre agregação vai sempre no `HAVING`.

---

## Bônus: ordem de execução

O SQL é **escrito** em uma ordem, mas **executado** em outra. Isso explica por que `WHERE` não enxerga agregações:

1. `FROM` → de onde vêm os dados
2. `WHERE` → filtra linhas
3. `GROUP BY` → forma os grupos
4. `HAVING` → filtra grupos
5. `SELECT` → calcula as colunas e agregações
6. `ORDER BY` → ordena
7. `LIMIT` → corta o resultado

---

## Resumo rápido

| Função | O que faz | Exemplo |
|--------|-----------|---------|
| `SUM` | soma | `SUM(salario)` |
| `COUNT` | conta | `COUNT(*)` |
| `AVG` | média | `AVG(salario)` |
| `MIN` / `MAX` | menor / maior | `MAX(salario)` |
| `GROUP BY` | agrupa linhas | `GROUP BY cargo` |
| `WHERE` | filtra linhas (antes) | `WHERE cidade = 'Recife'` |
| `HAVING` | filtra grupos (depois) | `HAVING COUNT(*) > 1` |