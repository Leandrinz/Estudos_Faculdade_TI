# Álgebra Relacional (parte 2): Junções, Projeção Generalizada, Agregação e Agrupamento

> Material de estudo baseado na **Aula 09** e na **Lista de Exercícios IV** (Banco de Dados, UFERSA, Profa. Laysa Mabel).
> Referência: Silberschatz, Korth, Sudarshan. *Sistema de Banco de Dados*, 3ª ed., Cap. 6.

---

## 0. Antes de começar: como ler uma expressão

Uma expressão de álgebra relacional é lida **de dentro para fora**: o que está entre parênteses é calculado primeiro, e o resultado vira a entrada da operação de fora.

```
π nome, nome_func ( departamento ⋈ id_gerente = id_func funcionario )
│                  └──────────── passo 1: junção ─────────────────┘
└── passo 2: projeção sobre o resultado do passo 1
```

**Símbolos usados** (a aula usa `|X|` para a junção, o restante é a notação padrão):

| Operação | Símbolo padrão | Como aparece na aula |
|---|---|---|
| Projeção | π | Π |
| Seleção | σ | σ |
| Renomeação | ρ | ρ |
| Produto cartesiano | × | × |
| Junção | ⋈ | `\|X\|` |
| Junção natural | ⋈ (sem condição) | `\|X\|` (sem condição) |
| Junção externa à esquerda | ⟕ | `]X\|` |
| Junção externa à direita | ⟖ | `\|X[` |
| Junção externa total | ⟗ | `]X[` |
| Agregação / agrupamento | 𝒢 | 𝒢 (letra G caligráfica) |

**Truque para lembrar as junções externas:** o colchete `]` ou `[` fica do lado da relação que **tem todas as suas tuplas preservadas**.
`]X|` → preserva a esquerda. `|X[` → preserva a direita. `]X[` → preserva as duas.

---

## 1. Junção (θ-join)

### O que é
Junção = **produto cartesiano + seleção**. Combina duas relações e mantém só as combinações de tuplas que satisfazem um **predicado de junção**.

```
R ⋈ <predicado> S   =   σ <predicado> ( R × S )
```

O resultado tem **todos os atributos de R e de S**.

### Exemplo guiado: Consulta 1
**"Recupere o nome de cada departamento e de seus respectivos gerentes."**

**Tabelas:**

`funcionario`

| id_func | nome_func |
|---|---|
| 1010 | João |
| 1011 | Cláudia |
| 1012 | Valentina |
| 1013 | Marcelo |
| 1014 | Adriana |

`departamento`

| id | nome | id_gerente | dt_inicio |
|---|---|---|---|
| 1 | Informática | 1013 | 23/03/2007 |
| 3 | Recursos Humanos | 1011 | 05/10/2010 |
| 4 | Financeiro | 1014 | 02/06/2000 |

**Expressão:**

```
π nome, nome_func ( departamento ⋈ id_gerente = id_func funcionario )
```

**Passo 1 (o que a junção "faz por baixo dos panos"):**
o produto cartesiano gera 3 × 5 = **15 combinações**. Só sobrevivem as que satisfazem `id_gerente = id_func`:

| id | nome | id_gerente | dt_inicio | id_func | nome_func |
|---|---|---|---|---|---|
| 1 | Informática | 1013 | 23/03/2007 | 1013 | Marcelo |
| 3 | Recursos Humanos | 1011 | 05/10/2010 | 1011 | Cláudia |
| 4 | Financeiro | 1014 | 02/06/2000 | 1014 | Adriana |

**Passo 2 (projeção em `nome, nome_func`):**

| nome | nome_func |
|---|---|
| Informática | Marcelo |
| Recursos Humanos | Cláudia |
| Financeiro | Adriana |

### Observações importantes (caem em prova)
1. Tuplas em que o atributo de junção é **nulo**, ou em que o predicado é **falso**, **não aparecem** no resultado.
2. Se **nenhuma** combinação satisfaz o predicado, o resultado é uma **relação vazia** (sem tuplas).

> Repare: João (1010) e Valentina (1012) **sumiram** do resultado porque não são gerentes de nenhum departamento. Guarde essa ideia, ela é exatamente o que a junção externa vai resolver.

---

## 2. Junção Natural

### O que é
É uma junção em que o predicado é **automático**: compara **todos os atributos que têm o mesmo nome** nas duas relações e exige igualdade. Além disso, o atributo em comum **aparece uma só vez** no resultado.

```
R ⋈ S
```

### Exemplo guiado: Consulta 2
**"Liste o nome de todos os clientes que são devedores e a cidade onde moram."**

`cliente`

| id_cli | nome | cidade | ag | conta |
|---|---|---|---|---|
| 1 | Jonas | São Paulo | 456 | 1111 |
| 2 | Silvio | Guarulhos | 734 | 2222 |
| 3 | Henrique | Santos | 578 | 3333 |
| 4 | Carlos | Campinas | 456 | 4444 |
| 5 | Paulo | Santos | 987 | 5555 |

`devedor`

| id_emp | id_cli |
|---|---|
| 1 | 1 |
| 3 | 2 |
| 1 | 2 |
| 6 | 4 |

`cliente` e `devedor` têm **um único atributo com o mesmo nome: `id_cli`**. Então a junção natural compara `cliente.id_cli = devedor.id_cli`.

**Expressão:**

```
π nome, cidade ( cliente ⋈ devedor )
```

**Passo 1: junção natural** (`id_cli` aparece uma vez só):

| id_cli | nome | cidade | ag | conta | id_emp |
|---|---|---|---|---|---|
| 1 | Jonas | São Paulo | 456 | 1111 | 1 |
| 2 | Silvio | Guarulhos | 734 | 2222 | 3 |
| 2 | Silvio | Guarulhos | 734 | 2222 | 1 |
| 4 | Carlos | Campinas | 456 | 4444 | 6 |

Henrique (3) e Paulo (5) não estão em `devedor`, então ficam de fora.

**Passo 2: projeção em `nome, cidade`.**
Silvio aparece 2 vezes no passo 1, mas o resultado final tem **uma linha só**. Isso acontece porque relações são **conjuntos**: a projeção elimina duplicatas.

| nome | cidade |
|---|---|
| Jonas | São Paulo |
| Silvio | Guarulhos |
| Carlos | Campinas |

### ⚠️ Armadilha clássica
Na lista de exercícios, `empregado` e `departamento` têm um atributo `nome` cada. Se você escrever `empregado ⋈ departamento`, a junção natural vai exigir **também** `empregado.nome = departamento.nome`, o que quase nunca dá certo e retorna vazio (ou lixo). Quando as relações compartilham nomes de atributos que **não** deveriam ser comparados, **use a junção com predicado explícito** (ou renomeie com ρ antes).

---

## 3. Junção Externa (Outer Join)

### A ideia
A junção comum **perde tuplas** sem correspondência. A junção externa **preserva** essas tuplas, preenchendo com **`nulo`** os atributos da outra relação.

Existem 3 formas:

| Forma | Preserva |
|---|---|
| Esquerda `R ]X│ S` | todas as tuplas de **R** |
| Direita `R │X[ S` | todas as tuplas de **S** |
| Total `R ]X[ S` | todas as tuplas de **R e S** |

### 3.1 Junção Externa à Esquerda

**Consulta 3:** *"Liste o nome de todos os funcionários e o nome do departamento quando o funcionário for um gerente."*

Usa as mesmas tabelas `funcionario` e `departamento` da seção 1.
"Todos os funcionários" → `funcionario` é a relação **da esquerda**, que deve ser totalmente preservada.

```
π nome_func, nome ( funcionario ]X| id_func = id_gerente departamento )
```

**Passo 1: junção externa à esquerda:**

| id_func | nome_func | id | nome | id_gerente | dt_inicio |
|---|---|---|---|---|---|
| 1010 | João | **nulo** | **nulo** | **nulo** | **nulo** |
| 1011 | Cláudia | 3 | Recursos Humanos | 1011 | 05/10/2010 |
| 1012 | Valentina | **nulo** | **nulo** | **nulo** | **nulo** |
| 1013 | Marcelo | 1 | Informática | 1013 | 23/03/2007 |
| 1014 | Adriana | 4 | Financeiro | 1014 | 02/06/2000 |

João e Valentina agora **aparecem**, com nulos do lado do departamento.

**Passo 2: projeção em `nome_func, nome`:**

| nome_func | nome |
|---|---|
| João | nulo |
| Cláudia | Recursos Humanos |
| Valentina | nulo |
| Marcelo | Informática |
| Adriana | Financeiro |

**Comparação com a Consulta 1:** lá, com junção comum, João e Valentina sumiram. Aqui, com a externa à esquerda, todos os 5 funcionários aparecem.

### 3.2 Junção Externa à Direita

**Consulta 4:** *"Liste o identificador dos clientes devedores e todos os possíveis valores de empréstimos."*
"Todos os empréstimos" → `emprestimo` fica à **direita**.

`emprestimo`

| id | valor |
|---|---|
| 1 | 1.000,00 |
| 2 | 2.000,00 |
| 3 | 1.500,00 |
| 4 | 500,00 |

`devedor` (nesta versão do exemplo)

| id_emp | id_cli |
|---|---|
| 1 | 1 |
| 3 | 2 |
| 1 | 2 |
| 2 | 4 |

```
π id_cli, valor ( devedor |X[ id_emp = id emprestimo )
```

**Passo 1: junção externa à direita:**

| id_emp | id_cli | id | valor |
|---|---|---|---|
| 1 | 1 | 1 | 1.000,00 |
| 3 | 2 | 3 | 1.500,00 |
| 1 | 2 | 1 | 1.000,00 |
| 2 | 4 | 2 | 2.000,00 |
| **nulo** | **nulo** | 4 | 500,00 |

O empréstimo 4 (R$ 500,00) **ninguém deve**, mas ele aparece mesmo assim, com nulos do lado de `devedor`.

**Passo 2: projeção em `id_cli, valor`:**

| id_cli | valor |
|---|---|
| 1 | 1.000,00 |
| 2 | 1.500,00 |
| 2 | 1.000,00 |
| 4 | 2.000,00 |
| nulo | 500,00 |

### 3.3 Junção Externa Total

**Consulta 5:** *"Liste o nome de todas as disciplinas, juntamente com o nome dos professores que as ministram. Deve-se incluir o nome de todos os professores, mesmo que estes não tenham ministrado disciplina."*
Aqui **as duas** relações têm elementos "sem par" → junção total.

`disciplina`

| id_d | nome_dis | id_prof |
|---|---|---|
| 1 | Contabilidade Básica | 2 |
| 2 | Economia | 2 |
| 3 | Direito do Trabalho | 1 |
| 4 | Pesquisa Operacional | nulo |

`professor`

| id_p | nome_prof |
|---|---|
| 1 | Jonas |
| 2 | Carla |
| 3 | Paulo |

```
π nome_dis, nome_prof ( disciplina ]X[ id_prof = id_p professor )
```

**Passo 1: junção externa total:**

| id_d | nome_dis | id_prof | id_p | nome_prof |
|---|---|---|---|---|
| 1 | Contabilidade Básica | 2 | 2 | Carla |
| 2 | Economia | 2 | 2 | Carla |
| 3 | Direito do Trabalho | 1 | 1 | Jonas |
| 4 | Pesquisa Operacional | nulo | nulo | nulo |
| nulo | nulo | nulo | 3 | Paulo |

- *Pesquisa Operacional* não tem professor → preservada pela **esquerda**.
- *Paulo* não dá aula → preservado pela **direita**.

**Passo 2: projeção:**

| nome_dis | nome_prof |
|---|---|
| Contabilidade Básica | Carla |
| Economia | Carla |
| Direito do Trabalho | Jonas |
| Pesquisa Operacional | nulo |
| nulo | Paulo |

### Resumo visual das junções externas

| Tem correspondência? | Junção comum | Esquerda | Direita | Total |
|---|---|---|---|---|
| Sim (ambos os lados) | ✅ | ✅ | ✅ | ✅ |
| Só existe na esquerda | ❌ | ✅ (nulos à direita) | ❌ | ✅ |
| Só existe na direita | ❌ | ❌ | ✅ (nulos à esquerda) | ✅ |

**Como decidir qual usar:** olhe o enunciado e pergunte *"de qual relação eu não posso perder nenhuma tupla?"*

- "todos os funcionários…" → externa à esquerda (funcionário à esquerda)
- "todos os possíveis empréstimos…" → externa à direita (empréstimo à direita)
- "todas as disciplinas **e** todos os professores…" → externa total

---

## 4. Projeção Generalizada

### O que é
Estende a projeção comum: dentro da lista de projeção, você pode usar **expressões aritméticas** (e funções de string) envolvendo atributos e constantes.

```
π F1, F2, ..., Fn (E)
```
onde cada `Fi` é um atributo **ou uma expressão** sobre atributos de `E`.

### Exemplo guiado: Consulta 6
**"Liste o identificador, o nome e o salário líquido do funcionário."**

`funcionario`

| id | nome | sal_bruto | deducao |
|---|---|---|---|
| 1010 | João | 4.000,00 | 900,00 |
| 1011 | Cláudia | 1.900,00 | 427,50 |
| 1012 | Valentina | 2.300,00 | 517,50 |

O salário líquido não existe como coluna, mas é `sal_bruto - deducao`:

```
r ← π id, nome, sal_bruto - deducao ( funcionario )
```

Esse `←` significa **atribuição**: guardamos o resultado numa relação temporária `r`. A coluna calculada fica sem nome bom, então usamos **renomeação (ρ)**:

```
ρ id, nome, sal_liquido ( r )
```

**Cálculo:**
- João: 4.000,00 − 900,00 = **3.100,00**
- Cláudia: 1.900,00 − 427,50 = **1.472,50**
- Valentina: 2.300,00 − 517,50 = **1.782,50**

**Resultado final:**

| id | nome | sal_liquido |
|---|---|---|
| 1010 | João | 3.100,00 |
| 1011 | Cláudia | 1.472,50 |
| 1012 | Valentina | 1.782,50 |

**Mais exemplos para fixar (mesma tabela):**

- Salário com aumento de 10%: `π nome, sal_bruto * 1.10 ( funcionario )`
- Total de descontos anual: `π nome, deducao * 12 ( funcionario )`

---

## 5. Função Agregada

### O que é
Uma função que recebe **um atributo**, olha para o **conjunto de valores** dele e devolve **um único valor**.

**Principais funções:** `max`, `min`, `sum`, `avg`, `count`.

```
𝒢 <função agregada> ( R )
```

### Exemplo guiado: Consulta 7
**"Recupere a média salarial de todos os funcionários."**

`funcionario`

| id_func | nome_func | sexo | id_dep | id_superv | salario |
|---|---|---|---|---|---|
| 1010 | João | M | 3 | --- | 6.500,00 |
| 1011 | Cláudia | F | 1 | 1010 | 3.000,00 |
| 1012 | Valentina | F | 1 | 1011 | 2.000,00 |
| 1013 | Marcelo | M | 2 | 1012 | 1.620,00 |
| 1014 | Adriana | F | 3 | 1010 | 3.340,00 |

```
𝒢 avg(salario) ( funcionario )
```

**Cálculo:** (6.500 + 3.000 + 2.000 + 1.620 + 3.340) / 5 = 16.460 / 5 = **3.292,00**

| avg |
|---|
| 3.292,00 |

**Outras agregações sobre a mesma tabela (treine calculando):**

| Expressão | Resultado |
|---|---|
| `𝒢 max(salario) (funcionario)` | 6.500,00 |
| `𝒢 min(salario) (funcionario)` | 1.620,00 |
| `𝒢 sum(salario) (funcionario)` | 16.460,00 |
| `𝒢 count(id_func) (funcionario)` | 5 |

Sem agrupamento, a agregação **colapsa a relação inteira em uma única linha**.

---

## 6. Agrupamento

### O que é
Divide as tuplas em **grupos** (pelos valores de um ou mais atributos) e aplica as funções agregadas **em cada grupo**.

```
<atributos de agrupamento>  𝒢  <funções agregadas> ( R )
```

- O que vem **à esquerda** do 𝒢: por quais atributos agrupar.
- O que vem **à direita** do 𝒢: o que calcular em cada grupo.
- Se **não** houver nada à esquerda, é a agregação simples da seção 5 (um grupo só, com todas as tuplas).

### Exemplo guiado: Consulta 8
**"Recupere o identificador de cada departamento, o número de funcionários que trabalham em cada departamento e o salário médio pago em cada departamento."**

Usando a mesma tabela `funcionario` da seção 5:

```
id_dep 𝒢 count(*), avg(salario) ( funcionario )
```

**Passo a passo: formando os grupos por `id_dep`:**

| id_dep | Funcionários no grupo | salários |
|---|---|---|
| 1 | Cláudia, Valentina | 3.000 e 2.000 |
| 2 | Marcelo | 1.620 |
| 3 | João, Adriana | 6.500 e 3.340 |

**Aplicando as funções em cada grupo:**
- Grupo 1: count = 2, avg = (3.000 + 2.000)/2 = **2.500,00**
- Grupo 2: count = 1, avg = **1.620,00**
- Grupo 3: count = 2, avg = (6.500 + 3.340)/2 = 9.840/2 = **4.920,00**

**Resultado final:**

| id_dep | count | avg |
|---|---|---|
| 1 | 2 | 2.500,00 |
| 2 | 1 | 1.620,00 |
| 3 | 2 | 4.920,00 |

### Mais um exemplo: agrupar por `sexo`
**"Quantos funcionários e qual o maior salário de cada sexo?"**

```
sexo 𝒢 count(*), max(salario) ( funcionario )
```

| sexo | count | max |
|---|---|---|
| M | 2 | 6.500,00 |
| F | 3 | 3.340,00 |

(M: João e Marcelo → max entre 6.500 e 1.620. F: Cláudia, Valentina, Adriana → max entre 3.000, 2.000 e 3.340.)

### Agrupar por mais de um atributo
`id_dep, sexo 𝒢 count(*) ( funcionario )` gera um grupo para cada **combinação** (id_dep, sexo):

| id_dep | sexo | count |
|---|---|---|
| 1 | F | 2 |
| 2 | M | 1 |
| 3 | M | 1 |
| 3 | F | 1 |

---

## 7. Cola: álgebra relacional × SQL

Para conectar com o que você provavelmente verá em SQL depois:

| Álgebra | SQL |
|---|---|
| `π a, b (R)` | `SELECT DISTINCT a, b FROM R` |
| `σ a = 5 (R)` | `SELECT * FROM R WHERE a = 5` |
| `R ⋈ R.x = S.y S` | `SELECT * FROM R JOIN S ON R.x = S.y` |
| `R ⋈ S` (natural) | `SELECT * FROM R NATURAL JOIN S` |
| `R ]X\| S` | `... R LEFT OUTER JOIN S ON ...` |
| `R \|X[ S` | `... R RIGHT OUTER JOIN S ON ...` |
| `R ]X[ S` | `... R FULL OUTER JOIN S ON ...` |
| `𝒢 avg(sal) (R)` | `SELECT AVG(sal) FROM R` |
| `dep 𝒢 count(*), avg(sal) (R)` | `SELECT dep, COUNT(*), AVG(sal) FROM R GROUP BY dep` |

---

## 8. Resolução comentada da Lista de Exercícios IV

**Esquema** (o que está antes do "referencia" são as chaves estrangeiras):

```
empregado    (id_emp, nome, dt_nasc, sexo, salario, id_depto)
                       id_depto  → departamento
departamento (id_dept, nome, id_gerente, dt_ini_gerencia)
                       id_gerente → empregado
projeto      (id_proj, nome, id_depto)
                       id_depto  → departamento
trabalha_em  (id_emp, id_proj, horas)
                       id_emp → empregado,  id_proj → projeto
dependente   (id_dep, id_emp, nome, sexo, dt_nasc, parentesco)
                       id_emp → empregado
```

> ⚠️ **Repare nos nomes:** a chave de `departamento` é `id_dept`, mas a FK em `empregado` e `projeto` chama-se `id_depto`. Os nomes **diferem**, então uma junção natural entre eles **não** funciona. Sempre use predicado explícito: `id_depto = id_dept`.
>
> ⚠️ `nome` existe em `empregado`, `departamento`, `projeto` e `dependente`. Quando juntar, **qualifique** (`empregado.nome`) ou renomeie com ρ.

### a) Nome dos empregados que trabalham no departamento 5

**Raciocínio:** `id_depto` já está em `empregado`, não precisa de junção. Selecione as linhas e projete o nome.

```
π nome ( σ id_depto = 5 ( empregado ) )
```

Ordem: **σ primeiro** (filtra linhas), **π depois** (escolhe colunas).

### b) Nome dos empregados que trabalham no departamento de Pesquisa

**Raciocínio:** o enunciado dá o *nome* do departamento, mas em `empregado` só temos o *id*. Logo precisamos de `departamento`: filtrar por `nome = 'Pesquisa'` e juntar com `empregado` por `id_depto = id_dept`.

```
π empregado.nome (
    empregado ⋈ empregado.id_depto = departamento.id_dept
    ( σ nome = 'Pesquisa' ( departamento ) )
)
```

**Versão mais limpa, evitando a ambiguidade de `nome` com ρ:**

```
PESQ ← ρ id_dept, nome_dep, id_gerente, dt_ini_gerencia
       ( σ nome = 'Pesquisa' ( departamento ) )

π nome ( empregado ⋈ id_depto = id_dept PESQ )
```

Como `PESQ` não tem mais atributo chamado `nome`, o `π nome` no final refere-se sem ambiguidade ao nome do empregado.

### c) Média salarial dos empregados de cada departamento

**Raciocínio:** "de cada departamento" → **agrupamento** por `id_depto`; "média salarial" → `avg(salario)`.

```
id_depto 𝒢 avg(salario) ( empregado )
```

*Mini-exemplo:* se `empregado` tiver (id_depto, salario) = (1, 3000), (1, 2000), (2, 1620), o resultado será (1, 2500) e (2, 1620).

### d) Nome dos gerentes que tenham dependentes

**Raciocínio:** precisamos de empregados que sejam **gerentes** (aparecem em `departamento.id_gerente`) **e** tenham **dependentes** (aparecem em `dependente.id_emp`). São duas junções encadeadas a partir de `empregado`.

Para evitar conflito de nomes (`nome` aparece nas três relações), reduzimos `departamento` e `dependente` apenas ao que interessa:

```
G ← π id_gerente ( departamento )
D ← ρ id_emp_dep ( π id_emp ( dependente ) )

π nome ( ( empregado ⋈ id_emp = id_gerente G ) ⋈ id_emp = id_emp_dep D )
```

Leitura:
1. `G` = ids de todos os gerentes.
2. `D` = ids de todos os empregados que têm ao menos um dependente (renomeado para não colidir com `id_emp` de `empregado`).
3. `empregado ⋈ G` fica só com gerentes; depois `⋈ D` fica só com os que também têm dependente.
4. Projeta o nome.

*Mini-exemplo:* se Marcelo é gerente e tem 2 dependentes, ele aparece **duas vezes** no resultado intermediário (uma por dependente), mas o `π nome` final elimina a duplicata e ele sai **uma vez só**.

### e) Média de horas trabalhadas em cada projeto

**Raciocínio:** `trabalha_em` já tem `id_proj` e `horas`. Agrupe por projeto e calcule a média.

```
id_proj 𝒢 avg(horas) ( trabalha_em )
```

**Variante (se pedirem o nome do projeto):** junção natural entre `projeto` e `trabalha_em`. O único atributo em comum é `id_proj`, então aqui a junção natural é segura.

```
id_proj, nome 𝒢 avg(horas) ( projeto ⋈ trabalha_em )
```

*Mini-exemplo:* `trabalha_em` = (10, P1, 20), (11, P1, 40), (10, P2, 10) → resultado: (P1, 30), (P2, 10).

### f) Quantidade de empregados que trabalham em cada projeto

**Raciocínio:** agrupar por `id_proj` e **contar** as linhas de `trabalha_em` (cada linha é um empregado alocado no projeto).

```
id_proj 𝒢 count(id_emp) ( trabalha_em )
```

Equivalente com `count(*)`. Com o mini-exemplo anterior: (P1, 2), (P2, 1).

---

## 9. Erros comuns (revise antes da prova)

1. **Usar junção natural quando os nomes das chaves são diferentes** (`id_depto` × `id_dept`) → retorna vazio. Use predicado explícito.
2. **Usar junção natural quando há atributos homônimos "extras"** (como `nome`) → ela também os compara. Renomeie com ρ ou use predicado explícito.
3. **Esquecer que a junção comum perde tuplas.** Se o enunciado diz "todos os X, *mesmo que* ...", precisa de junção **externa**.
4. **Escolher o lado errado da externa.** Pergunte: "qual relação não pode perder tuplas?"
5. **Trocar a ordem σ/π.** Se você projeta antes de selecionar, pode eliminar o atributo que a seleção precisa.
6. **Achar que a projeção mantém duplicatas.** Em álgebra relacional pura, resultado é conjunto: duplicatas somem.
7. **Confundir 𝒢 com e sem atributos à esquerda.** `𝒢 avg(sal)(R)` = uma linha para a tabela toda. `dep 𝒢 avg(sal)(R)` = uma linha **por departamento**.
8. **Esquecer a renomeação** de colunas calculadas na projeção generalizada (`sal_bruto - deducao` → `sal_liquido`).

---

## 10. Exercícios extras para praticar

Use as tabelas da aula. Tente escrever a expressão e o resultado antes de olhar as respostas.

**A)** Usando `funcionario` e `departamento` (seção 1): liste o nome de todos os **departamentos** e o nome do gerente, incluindo departamentos sem gerente.
**B)** Usando `funcionario` (seção 5): recupere o salário total pago em cada departamento.
**C)** Usando `funcionario` (seção 4): liste o nome e o salário de cada funcionário com um aumento de 5%.
**D)** Usando `cliente` e `devedor`: liste o nome de **todos** os clientes e o id do empréstimo que devem (nulo se não devem nada).
**E)** Usando `funcionario` (seção 5): quantos funcionários cada supervisor supervisiona diretamente?

<details>
<summary><b>Respostas</b></summary>

**A)** Departamentos à esquerda para preservá-los:
`π nome, nome_func ( departamento ]X| id_gerente = id_func funcionario )`
*(Com os dados da aula, todos têm gerente, então o resultado é igual ao da Consulta 1, mas a expressão está correta para o caso geral. Atenção: `nome` é de departamento e `nome_func` é do gerente.)*

**B)** `id_dep 𝒢 sum(salario) ( funcionario )`
Resultado: (1, 5.000,00), (2, 1.620,00), (3, 9.840,00).

**C)** `π nome_func, salario * 1.05 ( funcionario )`
Ex.: João 6.500 → 6.825,00; Cláudia 3.000 → 3.150,00.

**D)** Clientes à esquerda:
`π nome, id_emp ( cliente ]X| cliente.id_cli = devedor.id_cli devedor )`
Resultado: (Jonas, 1), (Silvio, 3), (Silvio, 1), (Henrique, nulo), (Carlos, 6), (Paulo, nulo).

**E)** `id_superv 𝒢 count(*) ( funcionario )`
Resultado: (1010, 2), (1011, 1), (1012, 1). João (`id_superv` = `---`, nulo) forma seu próprio grupo com count = 1, dependendo do tratamento de nulos.

</details>

---

## 11. Resumo de bolso

| Preciso de… | Use | Forma |
|---|---|---|
| Combinar tabelas com condição | Junção | `R ⋈ cond S` |
| Combinar por atributos de mesmo nome | Junção natural | `R ⋈ S` |
| Manter tudo da esquerda | Externa à esquerda | `R ]X\| cond S` |
| Manter tudo da direita | Externa à direita | `R \|X[ cond S` |
| Manter tudo dos dois lados | Externa total | `R ]X[ cond S` |
| Calcular colunas (a − b, a × 1.1…) | Projeção generalizada | `π a, b - c (R)` |
| Um valor sobre a tabela toda | Função agregada | `𝒢 avg(x) (R)` |
| Um valor **por grupo** | Agrupamento | `g 𝒢 avg(x) (R)` |
| Dar nome novo a colunas/relação | Renomeação | `ρ novo (R)` |