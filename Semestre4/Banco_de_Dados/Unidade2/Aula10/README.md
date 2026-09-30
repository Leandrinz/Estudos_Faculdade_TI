#  Aula 10 — Introdução ao SQL (PostgreSQL)

##  Visão geral

O SQL é dividido em grupos de comandos. Nesta aula o foco é a **DDL**.

| Sigla | Nome | Serve para | Exemplos |
|-------|------|------------|----------|
| **DDL** | Data Definition Language | Definir/alterar a **estrutura** do banco | `create`, `drop`, `alter` |
| DML | Data Manipulation Language | Manipular os **dados** | `insert`, `update`, `delete` |
| DQL | Data Query Language | **Consultar** dados | `select` |

>  **Macete:** DDL mexe na **estrutura** (a "planta da casa"). DML mexe nos **dados** (a "mobília").

---

##  DDL — Data Definition Language

Principais comandos: **`create`**, **`drop`**, **`alter`**

| Comando | Ideia | Exemplo de uso |
|---------|-------|----------------|
| `create` | Criar | banco de dados, tabelas |
| `drop` | Apagar | tabelas, bancos |
| `alter` | Modificar | renomear, adicionar/remover atributos e restrições |

---

##  `create`

### `create database`
Cria um banco de dados.

```sql
create database <nome_do_banco>;
```

### `create table`
Cria uma tabela no banco, definindo **nome**, **atributos** (colunas), **tipos de dados** e **restrições de integridade**.

```sql
create table <tabela> (
    <atributo1> <tipo de dado> <restrição de integridade1>,
    <atributo2> <tipo de dado> <restrição de integridade2>,
                            ...
    <atributo n> <tipo de dado> <restrição de integridade m>
);
```

**Exemplo prático:**

```sql
create table aluno (
    matricula  integer      primary key,
    nome       varchar(100) not null,
    email      varchar(100) unique,
    idade      integer      check (idade >= 0),
    ativo      boolean      default true
);
```

>  Não esqueça da **vírgula** entre os atributos (menos no último) e do **`;`** no final.

---

###  Tipos de dados comuns no PostgreSQL

| Categoria | Tipo | Descrição | Exemplo de valor |
|-----------|------|-----------|------------------|
| **Inteiros** | `smallint` | Inteiro pequeno | `10` |
| | `integer` (ou `int`) | Inteiro padrão | `2500` |
| | `bigint` | Inteiro grande | `9000000000` |
| | `serial` | Inteiro com **autoincremento** | `1, 2, 3...` |
| **Decimais** | `numeric(p,s)` | Decimal exato (`p` dígitos no total, `s` após a vírgula) | `numeric(5,2)` → `123.45` |
| | `real` / `double precision` | Ponto flutuante (aproximado) | `3.14` |
| **Texto** | `char(n)` | Texto de tamanho **fixo** | `'SP'` |
| | `varchar(n)` | Texto de tamanho **variável** (até `n`) | `'Maria'` |
| | `text` | Texto sem limite | `'Uma descrição longa...'` |
| **Data/Hora** | `date` | Data | `'2025-03-10'` |
| | `time` | Hora | `'14:30:00'` |
| | `timestamp` | Data + hora | `'2025-03-10 14:30:00'` |
| **Lógico** | `boolean` | Verdadeiro/Falso | `true` / `false` |

>  Use `numeric` para **dinheiro** (não tem erro de arredondamento) e `varchar` para nomes.

---

###  Restrições de integridade

São regras que garantem que os dados da tabela façam sentido.

| Restrição | O que garante | Exemplo |
|-----------|---------------|---------|
| `primary key` | Identifica cada linha de forma **única**; não aceita nulo nem repetido | `matricula integer primary key` |
| `foreign key` | O valor **precisa existir** na tabela referenciada (liga duas tabelas) | `foreign key (id_curso) references curso` |
| `not null` | O valor **não pode ser nulo** (vazio) | `nome varchar(100) not null` |
| `unique` | Valores **não se repetem** na coluna (mas aceita nulo) | `email varchar(100) unique` |
| `check` | O valor deve **satisfazer uma condição** | `check (idade >= 0)` |
| `default` | Valor **automático** quando nenhum é informado | `ativo boolean default true` |

>  **Para lembrar:**
> - **PK** = "quem sou eu" (identidade)
> - **FK** = "a quem pertenço" (ligação)
> - **unique** = "ninguém repete"
> - **not null** = "obrigatório"

---

##  `drop table`

Apaga uma tabela (**estrutura + todos os dados**).

```sql
drop table <nome_da_tabela>;
```

>  **Cuidado:** não tem "desfazer". Se quiser evitar erro caso a tabela não exista:
> ```sql
> drop table if exists <nome_da_tabela>;
> ```

---

## `alter table`

Modifica uma tabela **que já existe**. A estrutura base é sempre:

```sql
alter table <nome da tabela>
<ação>;
```

###  Resumo rápido

| Quero... | Comando |
|----------|---------|
| Renomear a tabela | `rename to` |
| Renomear um atributo | `rename <atual> to <novo>` |
| Adicionar atributo | `add <atributo> <tipo>` |
| Remover atributo | `drop <atributo>` |
| Adicionar chave primária | `add primary key` |
| Adicionar chave estrangeira | `add foreign key` |
| Adicionar unicidade | `add unique` |
| Adicionar condição | `add check` |
| Mudar o tipo | `alter <atributo> type` |
| Tornar obrigatório | `alter <atributo> set not null` |
| Definir valor padrão | `alter <atributo> set default` |

---

###  Renomear

#### `rename to` — renomeia a **tabela**

```sql
alter table <nome atual da tabela>
rename to <nome novo da tabela>;
```

#### `rename` — renomeia um **atributo**

```sql
alter table <nome da tabela>
rename <nome atributo atual> to <nome atributo novo>;
```

---

###  Adicionar atributo

#### `add <atributo>`

```sql
alter table <nome da tabela>
add <nome atributo> <tipo do atributo>;
```

**Exemplo:**
```sql
alter table aluno
add telefone varchar(15);
```

---

###  Adicionar restrições

#### `add primary key` — define a chave primária

```sql
alter table <nome da tabela>
add primary key (atributo);
```

#### `add foreign key` — define a chave estrangeira

```sql
alter table <nome da tabela>
add foreign key (atributo) references <nome da tabela referenciada>
on update cascade on delete cascade;
```

>  **O que é o `cascade`?** É a propagação de mudanças:
> - `on update cascade` → se a chave na tabela referenciada **mudar**, o valor é atualizado aqui também.
> - `on delete cascade` → se a linha referenciada for **apagada**, as linhas que dependem dela também são apagadas.

#### `add unique` — impede valores repetidos

```sql
alter table <nome da tabela>
add unique (nome do atributo);
```

#### `add check` — impõe uma condição

```sql
alter table <nome tabela>
add check (nome atributo in (2,4,6));
```

 Adiciona a restrição de que o atributo só pode ser igual a **2, 4 ou 6**.

---

###  Modificar um atributo existente

#### `alter ... type` — muda o tipo de dado

```sql
alter table <nome da tabela>
alter <nome do atributo> type <novo tipo do atributo>;
```

#### `alter ... set not null` — torna o atributo obrigatório

```sql
alter table <nome da tabela>
alter <nome atributo> set not null;
```

#### `alter ... set default` — define um valor padrão

```sql
alter table <nome da tabela>
alter <nome atributo> set default <valor pra default>;
```

---

###  Remover atributo

#### `drop`

```sql
alter table <nome da tabela>
drop <nome do atributo>;
```

---

###  Várias alterações de uma vez

É possível fazer **mais de uma alteração** no mesmo `alter table`, separando as ações por **vírgula**:

```sql
alter table aluno
add telefone varchar(15),
alter nome set not null,
drop idade;
```

---

