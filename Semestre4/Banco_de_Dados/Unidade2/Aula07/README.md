# Normalização de Bases de Dados

## Utilidade

A normalização serve para **retirar redundância de dados**, evitando problemas como:

- **Anomalias de inserção** — não conseguir guardar um dado sem ter de repetir outros;
- **Anomalias de atualização** — ao mudar um dado, é preciso mudá-lo em vários sítios (e corre-se o risco de ficarem inconsistentes);
- **Anomalias de remoção** — ao apagar um registo, perde-se informação que não tinha nada a ver com o motivo da remoção.

---

## Exemplo de partida (tabela não normalizada — ÑN)

Imaginemos uma tabela que guarda projetos e, para cada projeto, a lista dos empregados alocados a ele. A informação dos empregados aparece **aninhada dentro** da linha do projeto (uma tabela dentro de outra tabela):

### Esquema textual

```
proj (CodProj, Tipo, Descr,
        (CodEmp, Nome, Cat, Sal, DataIni, TempoAl))
```

- **CodProj** — código do projeto (chave do projeto)
- **Tipo** — tipo de projeto (ex.: Software, Consultoria)
- **Descr** — descrição do projeto
- **CodEmp** — código do empregado alocado
- **Nome** — nome do empregado
- **Cat** — categoria profissional do empregado
- **Sal** — salário do empregado
- **DataIni** — data de início da alocação do empregado a esse projeto
- **TempoAl** — tempo (horas) alocado pelo empregado a esse projeto

### Exemplo de dados

| CodProj | Tipo         | Descr                | Empregados (tabela aninhada)                                                                 |
|---------|--------------|-----------------------|-----------------------------------------------------------------------------------------------|
| P1      | Software     | Sistema de Vendas     | (E1, João, Analista, 3000, 2023-01-10, 20) <br> (E2, Maria, Programador, 2500, 2023-03-01, 15) |
| P2      | Consultoria  | Auditoria Financeira  | (E1, João, Analista, 3000, 2023-01-10, 10) <br> (E3, Carlos, Gestor, 4000, 2022-11-05, 5)      |

Repara que, para cada linha de projeto, a coluna "Empregados" contém **várias linhas de outra tabela lá dentro**. É exatamente isto que as formas normais (1FN, 2FN, 3FN) vêm resolver, passo a passo.

---

## Formas normais

Regras a seguir para que a tabela seja considerada "bem projetada":

- **1FN** — elimina tabelas aninhadas / grupos repetitivos
- **2FN** — elimina dependências parciais
- **3FN** — elimina dependências transitivas

---

## Primeira Forma Normal (1FN)

### Como é

Uma tabela está na **1FN** quando:

- **não contém tabelas aninhadas** (nenhuma célula guarda uma "sub-tabela" ou uma lista de valores);
- cada célula guarda **um único valor atómico**;
- não há grupos de colunas que se repetem (ex.: `Emp1, Emp2, Emp3...`).

No nosso exemplo, a tabela `proj` **viola a 1FN**, porque a coluna dos empregados guarda, na verdade, várias linhas de outra entidade.

### Resolução

Há **dois métodos possíveis** para eliminar o aninhamento:

#### Método 1 — Tabela única (achatar / "flatten")

Junta-se tudo numa só tabela, repetindo os dados do projeto em cada linha de empregado:

**proj_1FN** (CodProj, Tipo, Descr, CodEmp, Nome, Cat, Sal, DataIni, TempoAl)
Chave primária: **(CodProj, CodEmp)**

| CodProj | Tipo        | Descr               | CodEmp | Nome   | Cat        | Sal  | DataIni    | TempoAl |
|---------|-------------|----------------------|--------|--------|------------|------|------------|---------|
| P1      | Software    | Sistema de Vendas    | E1     | João   | Analista   | 3000 | 2023-01-10 | 20      |
| P1      | Software    | Sistema de Vendas    | E2     | Maria  | Programador| 2500 | 2023-03-01 | 15      |
| P2      | Consultoria | Auditoria Financeira | E1     | João   | Analista   | 3000 | 2023-01-10 | 10      |
| P2      | Consultoria | Auditoria Financeira | E3     | Carlos | Gestor     | 4000 | 2022-11-05 | 5       |

✅ Já está na 1FN (sem tabelas aninhadas).
⚠️ Mas repara na redundância: `Tipo` e `Descr` de P1 repetem-se; `Nome`, `Cat` e `Sal` de João (E1) também se repetem. Isto é o que a 2FN e a 3FN vão resolver.

#### Método 2 — Decomposição em duas tabelas

Em vez de repetir tudo numa única tabela, separa-se logo a informação que "não depende" do par (projeto, empregado):

**PROJ** (CodProj, Tipo, Descr)

| CodProj | Tipo        | Descr                |
|---------|-------------|------------------------|
| P1      | Software    | Sistema de Vendas     |
| P2      | Consultoria | Auditoria Financeira  |

**ALOCACAO** (CodProj, CodEmp, Nome, Cat, Sal, DataIni, TempoAl)
Chave primária: **(CodProj, CodEmp)**

| CodProj | CodEmp | Nome   | Cat        | Sal  | DataIni    | TempoAl |
|---------|--------|--------|------------|------|------------|---------|
| P1      | E1     | João   | Analista   | 3000 | 2023-01-10 | 20      |
| P1      | E2     | Maria  | Programador| 2500 | 2023-03-01 | 15      |
| P2      | E1     | João   | Analista   | 3000 | 2023-01-10 | 10      |
| P2      | E3     | Carlos | Gestor     | 4000 | 2022-11-05 | 5       |

Ambos os métodos resolvem a 1FN. Na prática, **usa-se sempre a decomposição** (Método 2), porque já reduz alguma redundância e prepara o caminho para a 2FN.

### Dependências funcionais (exemplo)

Para avançar para a 2FN, é preciso identificar **quem depende de quem**. Na tabela `ALOCACAO` (chave = CodProj + CodEmp), temos:

| Dependência funcional            | Significado                                                     | Tipo               |
|-----------------------------------|------------------------------------------------------------------|--------------------|
| CodProj → Tipo, Descr             | O tipo e a descrição só dependem do projeto                     | Parcial (já saiu p/ PROJ) |
| CodEmp → Nome, Cat, Sal           | O nome, categoria e salário só dependem do empregado             | **Parcial** ⚠️     |
| (CodProj, CodEmp) → DataIni, TempoAl | Data de início e tempo alocado dependem do par projeto+empregado | Total ✅           |

Uma **dependência parcial** é quando um atributo não-chave depende apenas de **parte** da chave primária composta, e não da chave toda. É exatamente isto que a 2FN não permite.

---

## Segunda Forma Normal (2FN)

### Como é

Uma tabela está na **2FN** quando:

- está na **1FN**, **e**
- **não tem dependências parciais**, ou seja, todo atributo não-chave depende da **chave primária inteira**, e não apenas de uma parte dela.

> A 2FN só faz sentido analisar quando a chave primária é **composta** (mais do que uma coluna). Se a chave for simples (uma só coluna), a tabela já está automaticamente na 2FN.

Na tabela `ALOCACAO`, a chave é composta (CodProj + CodEmp). Como vimos acima, `Nome`, `Cat` e `Sal` dependem **só** de `CodEmp` — não precisam do `CodProj` para serem determinados. Logo, `ALOCACAO` **viola a 2FN**.

### Resolução

Retira-se para uma nova tabela tudo o que depende apenas de parte da chave:

**EMP** (CodEmp, Nome, Cat, Sal)

| CodEmp | Nome   | Cat        | Sal  |
|--------|--------|------------|------|
| E1     | João   | Analista   | 3000 |
| E2     | Maria  | Programador| 2500 |
| E3     | Carlos | Gestor     | 4000 |

**ALOCACAO** (CodProj, CodEmp, DataIni, TempoAl) — agora só com o que depende da chave toda

| CodProj | CodEmp | DataIni    | TempoAl |
|---------|--------|------------|---------|
| P1      | E1     | 2023-01-10 | 20      |
| P1      | E2     | 2023-03-01 | 15      |
| P2      | E1     | 2023-01-10 | 10      |
| P2      | E3     | 2022-11-05 | 5       |

✅ Agora sim: `DataIni` e `TempoAl` dependem realmente do **par** (CodProj, CodEmp) — faz sentido, porque a mesma pessoa (E1) pode ter datas e tempos de alocação diferentes consoante o projeto.

Esquema depois da 2FN:

```
PROJ      (CodProj, Tipo, Descr)
ALOCACAO  (CodProj, CodEmp, DataIni, TempoAl)
EMP       (CodEmp, Nome, Cat, Sal)
```

---

## Terceira Forma Normal (3FN)

### Como é

Uma tabela está na **3FN** quando:

- está na **2FN**, **e**
- **não tem dependências transitivas**, ou seja, nenhum atributo não-chave depende de **outro atributo não-chave** (só pode depender diretamente da chave primária).

> Dependência transitiva: A → B → C. Se **Chave → Cat** e **Cat → Sal**, então indiretamente **Chave → Sal** *através de* Cat — e isso é proibido na 3FN.

Vamos olhar de novo para a tabela `EMP`:

| CodEmp | Nome   | Cat        | Sal  |
|--------|--------|------------|------|
| E1     | João   | Analista   | 3000 |
| E2     | Maria  | Programador| 2500 |
| E3     | Carlos | Gestor     | 4000 |

Aqui, faz sentido pensar que o **salário depende da categoria profissional** (todo "Analista" ganha 3000, todo "Gestor" ganha 4000), e não diretamente do empregado. Ou seja:

```
CodEmp → Cat → Sal
```

Isto é uma **dependência transitiva**: `Sal` depende de `Cat`, que por sua vez depende de `CodEmp`. Logo, `EMP` **viola a 3FN** (o salário está "preso" à pessoa errada — devia estar preso à categoria).

### Resolução

Separa-se a categoria (e o que depende dela) para a sua própria tabela:

**CATEGORIA** (Cat, Sal)

| Cat         | Sal  |
|-------------|------|
| Analista    | 3000 |
| Programador | 2500 |
| Gestor      | 4000 |

**EMP** (CodEmp, Nome, Cat) — Cat passa a ser chave estrangeira para CATEGORIA

| CodEmp | Nome   | Cat        |
|--------|--------|------------|
| E1     | João   | Analista   |
| E2     | Maria  | Programador|
| E3     | Carlos | Gestor     |

✅ Agora, se o salário de um "Analista" mudar, altera-se **uma só linha** na tabela `CATEGORIA`, em vez de ter de procurar e corrigir todos os empregados dessa categoria um a um.

---

## Esquema final (após 1FN, 2FN e 3FN)

```
PROJ       (CodProj, Tipo, Descr)
ALOCACAO   (CodProj, CodEmp, DataIni, TempoAl)
EMP        (CodEmp, Nome, Cat)
CATEGORIA  (Cat, Sal)
```

---

## Resumo — o que verificar em cada forma normal

| Forma | O que verificar                                                             | O que fazer se falhar                                                                 |
|-------|-------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| **1FN** | Existem tabelas aninhadas, listas de valores numa célula, ou colunas repetidas (Emp1, Emp2...)? | Achatar numa tabela única **ou** decompor em tabelas separadas, com chave adequada.       |
| **2FN** | A chave primária é composta? Algum atributo não-chave depende só de **parte** da chave? | Retirar esse(s) atributo(s) para uma nova tabela, com essa parte da chave como chave dessa nova tabela. |
| **3FN** | Algum atributo não-chave depende de **outro atributo não-chave** (não diretamente da chave)? | Retirar esse(s) atributo(s) e o atributo do qual dependem para uma nova tabela.           |

**Regra prática, do início ao fim:** cada atributo não-chave deve depender **da chave, de toda a chave e de nada mais além da chave**.