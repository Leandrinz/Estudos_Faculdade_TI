# Aula 08 - Álgebra Relacional

> Para todos os exemplos desta aula, vamos usar duas relações (tabelas) fixas:

**Aluno**

| matricula | nome     | idade | curso_id |
|-----------|----------|-------|----------|
| 1         | Ana      | 20    | 10       |
| 2         | Bruno    | 22    | 10       |
| 3         | Carla    | 19    | 20       |
| 4         | Diego    | 25    | 30       |

**Curso**

| curso_id | nome_curso   |
|----------|--------------|
| 10       | Computação   |
| 20       | Matemática   |
| 30       | Física       |

---

## Nomenclatura

A álgebra relacional trabalha em cima do modelo relacional, então usamos os seguintes termos:

- **Relação**: é a "tabela" em si. Ex: `Aluno` é uma relação.
- **Tupla**: é uma "linha" da tabela. Ex: `(1, Ana, 20, 10)` é uma tupla de `Aluno`.
- **Atributo**: é uma "coluna" da tabela. Ex: `nome`, `idade` são atributos de `Aluno`.
- **Grau**: número de atributos (colunas) de uma relação. `Aluno` tem grau 4.
- **Cardinalidade**: número de tuplas (linhas) de uma relação. `Aluno` tem cardinalidade 4.
- **Domínio**: conjunto de valores possíveis que um atributo pode assumir (ex: domínio de `idade` são números inteiros positivos).

Cada operação da álgebra relacional recebe uma ou mais relações e **devolve outra relação** (é por isso que dá pra combinar/encadear operações).

---

## Seleção (σ)

Recupera **linhas** (horizontal) que satisfazem um predicado (condição).

```
σ<predicado>(<relação>)
```

**Exemplo:** alunos com idade maior que 20

```
σ idade > 20 (Aluno)
```

Resultado:

| matricula | nome  | idade | curso_id |
|-----------|-------|-------|----------|
| 2         | Bruno | 22    | 10       |
| 4         | Diego | 25    | 30       |

**Exemplo com mais de uma condição** (usando `E` / `AND` e `OU` / `OR`):

```
σ idade > 18 AND curso_id = 10 (Aluno)
```

Resultado: alunos com idade > 18 **e** que fazem o curso 10 → Ana e Bruno.

---

## Projeção (π)

Recupera **colunas** (vertical). Remove atributos que não interessam.

```
π<atributo1, atributo2, ...>(<relação>)
```

**Exemplo:** só o nome e a idade dos alunos

```
π nome, idade (Aluno)
```

Resultado:

| nome  | idade |
|-------|-------|
| Ana   | 20    |
| Bruno | 22    |
| Carla | 19    |
| Diego | 25    |

> Obs: projeção remove duplicatas automaticamente, já que o resultado precisa ser uma relação (conjunto), e conjuntos não têm elementos repetidos.

### Combinando seleção + projeção

Dá pra encadear as operações, aplicando uma em cima do resultado da outra.

**Exemplo:** nome dos alunos com mais de 20 anos

```
π nome (σ idade > 20 (Aluno))
```

Resultado: `Bruno`, `Diego`.

---

## Atribuição (←)

Serve para guardar o resultado de uma expressão em uma "variável" (uma relação temporária), facilitando escrever expressões grandes por partes.

```
Resultado ← σ idade > 20 (Aluno)
```

**Exemplo:** dividir uma consulta em passos

```
Maiores ← σ idade > 20 (Aluno)
Final   ← π nome, curso_id (Maiores)
```

É equivalente a escrever tudo em uma linha só, mas fica mais legível.

---

## Renomeação (ρ)

Renomeia uma relação (ou seus atributos). Útil quando precisamos referenciar a mesma relação duas vezes (ex: em produto cartesiano/junção) ou só para deixar o resultado com nome mais claro.

```
ρ<novo_nome>(<relação>)
```

**Exemplo:** renomear a relação `Aluno` para `A`

```
ρ A (Aluno)
```

**Exemplo renomeando também os atributos:**

```
ρ A(id, nome_aluno, idade, curso) (Aluno)
```

Isso cria uma cópia de `Aluno` chamada `A`, com as colunas renomeadas para `id`, `nome_aluno`, `idade`, `curso`.

---

## União (∪)

Junta as tuplas de duas relações, **sem repetir** tuplas iguais. As duas relações precisam ser **compatíveis** (mesmo número de atributos e mesmos domínios).

```
Relação1 ∪ Relação2
```

**Exemplo:** matrícula dos alunos que fazem Computação **ou** que têm mais de 20 anos

```
π matricula (σ curso_id = 10 (Aluno)) ∪ π matricula (σ idade > 20 (Aluno))
```

- Primeiro conjunto (curso 10): `{1, 2}`
- Segundo conjunto (idade > 20): `{2, 4}`
- União: `{1, 2, 4}`

---

## Interseção (∩)

Retorna apenas as tuplas que aparecem **nas duas** relações. Também exige relações compatíveis.

```
Relação1 ∩ Relação2
```

**Exemplo:** matrícula dos alunos que fazem Computação **e** têm mais de 20 anos

```
π matricula (σ curso_id = 10 (Aluno)) ∩ π matricula (σ idade > 20 (Aluno))
```

- Primeiro conjunto: `{1, 2}`
- Segundo conjunto: `{2, 4}`
- Interseção: `{2}` → apenas Bruno.

---

## Diferença (−)

*(bônus, geralmente vista junto com união/interseção)*: retorna as tuplas que estão na primeira relação e **não** estão na segunda.

```
Relação1 − Relação2
```

**Exemplo:** alunos do curso 10 que **não** têm mais de 20 anos

```
π matricula (σ curso_id = 10 (Aluno)) − π matricula (σ idade > 20 (Aluno))
```

- Primeiro conjunto: `{1, 2}`
- Segundo conjunto: `{2, 4}`
- Diferença: `{1}` → apenas Ana.

---

## Produto Cartesiano (×)

Combina **toda tupla** de uma relação com **toda tupla** de outra relação. O grau do resultado é a soma dos graus das duas relações, e a cardinalidade é o produto das cardinalidades.

```
Relação1 × Relação2
```

**Exemplo:**

```
Aluno × Curso
```

Como `Aluno` tem 4 tuplas e `Curso` tem 3 tuplas, o resultado terá **4 × 3 = 12 tuplas**, cada uma combinando um aluno com um curso (mesmo que o curso não seja o dele de verdade). Por exemplo, algumas linhas seriam:

| matricula | nome | idade | curso_id (Aluno) | curso_id (Curso) | nome_curso  |
|-----------|------|-------|-------------------|--------------------|-------------|
| 1         | Ana  | 20    | 10                 | 10                 | Computação  |
| 1         | Ana  | 20    | 10                 | 20                 | Matemática  |
| 1         | Ana  | 20    | 10                 | 30                 | Física      |
| ...       | ...  | ...   | ...                | ...                | ...         |

> Note que o produto cartesiano puro **não filtra** as combinações que fazem sentido. Por isso, na prática, ele quase sempre é usado com uma **seleção** logo em seguida, para filtrar só as combinações válidas:

```
σ Aluno.curso_id = Curso.curso_id (Aluno × Curso)
```

Isso é, na prática, o que dá origem à operação de **Junção (Join, ⋈)** — que combina produto cartesiano + seleção em um único passo, mas costuma ser vista em uma aula separada.