# Transformação entre Modelos (Modelo ER → Modelo Relacional)

> Processo pelo qual um esquema conceitual (Modelo Entidade-Relacionamento) é convertido em um esquema lógico (Modelo Relacional), formado por tabelas, colunas, chaves primárias e chaves estrangeiras.

---

## 1. Implementação de Entidades

**Regra geral:** toda entidade do modelo ER dá origem a **uma tabela**. Cada atributo da entidade vira uma **coluna**, e o identificador (chave) da entidade vira a **chave primária (PK)** da tabela.

### Exemplo visual

Modelo ER:

```
┌───────────────────────┐
│        CLIENTE        │
├───────────────────────┤
│ *codigo                │
│  nome                  │
│  cpf                   │
│  telefone               │
└───────────────────────┘
```
(o `*` indica o atributo identificador/chave)

Tabela resultante:

| codigo | nome         | cpf         | telefone     |
|--------|--------------|-------------|--------------|
| 1      | Ana Souza    | 12345678900 | (84) 99999-1 |
| 2      | Bruno Lima   | 98765432100 | (84) 98888-2 |

**Atributos compostos** (ex.: endereço com rua, número, cidade) viram **várias colunas simples** na tabela (uma para cada subatributo).

**Atributos multivalorados** (ex.: cliente pode ter vários telefones) **não** viram uma coluna — eles se transformam em **uma tabela própria**, relacionada à tabela original por chave estrangeira (é tratado como se fosse um relacionamento 1:N).

```
CLIENTE (codigo PK, nome, cpf)
TELEFONE_CLIENTE (id PK, numero, codigo_cliente FK → CLIENTE.codigo)
```

---

## 2. Relacionamento Identificador (Entidade Fraca)

Uma **entidade fraca** é aquela que **não possui identificador próprio** — ela depende de outra entidade (a entidade forte/proprietária) para ser identificada. O relacionamento entre elas é chamado de **relacionamento identificador**.

### Exemplo visual

```
┌────────────────┐        1        N   ┌──────────────────┐
│   FUNCIONARIO   │──────possui────────│    DEPENDENTE     │
│ *matricula      │                     │ *nome (parcial)   │
│  nome           │                     │  data_nascimento  │
└────────────────┘                     └──────────────────┘
```
Aqui, `DEPENDENTE` não tem sentido sozinho: "nome" só identifica um dependente **dentro do contexto** de um funcionário específico (chave parcial).

### Regra de implementação

1. A entidade forte gera sua tabela normalmente, com sua PK.
2. A entidade fraca gera uma tabela cuja **chave primária é composta**: o identificador parcial da entidade fraca **+** a chave estrangeira que referencia a entidade forte.

Tabela resultante (DEPENDENTE):

| matricula_funcionario | nome    | data_nascimento |
|------------------------|---------|------------------|
| 10                      | Maria   | 2015-03-02       |
| 10                      | Pedro   | 2018-07-19       |
| 22                      | Maria   | 2020-01-10       |

> Note que "Maria" aparece duas vezes — mas cada uma é única *dentro do seu funcionário*, o que só funciona porque a chave é composta (matricula_funcionario + nome).

---

## 3. Implementação de Relacionamentos (entre entidades fortes)

Quando o relacionamento **não** é identificador, existem **três estratégias possíveis**, escolhidas de acordo com a **cardinalidade máxima** envolvida.

### 3.1 Tabela própria — usada quando **cardinalidade máxima N:N**

Quando os dois lados do relacionamento têm cardinalidade máxima **N** (muitos para muitos), o losango do relacionamento **se transforma em uma tabela própria**. Essa tabela contém, como chave primária composta, as chaves estrangeiras das duas entidades participantes, além de eventuais atributos do próprio relacionamento.

#### Exemplo visual

```
┌───────────┐   N        N   ┌───────────┐
│   ALUNO    │────matricula───│  DISCIPLINA │
│ *ra        │                │ *codigo     │
│  nome      │                │  nome       │
└───────────┘                └───────────┘
                 (data_matricula, nota)
```

Resultado: **3 tabelas** (uma para cada entidade + uma para o relacionamento — chamada, por exemplo, MATRICULA — cuja chave primária é composta pelas chaves de ALUNO e DISCIPLINA).

---

### 3.2 Adição de colunas (chave estrangeira) — usada quando **cardinalidade máxima 1** de um dos lados

Quando um dos lados do relacionamento tem cardinalidade máxima **1**, **não é necessário criar uma tabela nova**: basta **adicionar uma coluna de chave estrangeira na tabela que possui cardinalidade máxima igual a 1**, referenciando a tabela do outro lado.

> Regra prática: a FK sempre vai para o lado "1" (o lado que só pode se relacionar a **um** registro do outro lado).

#### Exemplo visual

```
┌───────────┐   1        N   ┌───────────┐
│ DEPARTAMENTO│────gerencia────│ FUNCIONARIO │
│ *codigo     │                │ *matricula   │
│  nome       │                │  nome        │
└───────────┘                └───────────┘
```
Cada funcionário trabalha em **1** departamento; um departamento tem **N** funcionários → a cardinalidade máxima 1 está do lado de DEPARTAMENTO (quando visto a partir de FUNCIONARIO). Ou seja, é o lado **N (FUNCIONARIO)** que recebe a FK, pois cada funcionário só aponta para **um único** departamento.

Tabela resultante (FUNCIONARIO):

| matricula | nome   | codigo_departamento |
|-----------|--------|----------------------|
| 1         | Carla  | 10                    |
| 2         | Diego  | 10                    |
| 3         | Elisa  | 20                    |

Resultado: **2 tabelas** — o relacionamento "desaparece" como tabela e vira apenas uma coluna.

---

### 3.3 Fusão de tabelas — usada quando **ambos os lados são (1,1)**

Quando a cardinalidade é **(1,1)** dos dois lados (ou seja, cada entidade se relaciona com **exatamente um** registro da outra, obrigatoriamente), pode-se **fundir as duas entidades em uma única tabela**, já que existe uma correspondência exata (um-para-um) entre elas.

#### Exemplo visual

```
┌───────────┐   1,1     1,1  ┌───────────┐
│  PESSOA    │────possui──────│ PASSAPORTE  │
│ *cpf        │                │ *numero      │
│  nome       │                │  validade    │
└───────────┘                └───────────┘
```

Tabela resultante (fundida):

| cpf         | nome   | numero_passaporte | validade_passaporte |
|-------------|--------|--------------------|-----------------------|
| 12345678900 | Ana    | BR1234567           | 2030-05-01             |

Resultado: **1 tabela única** representando as duas entidades e o relacionamento entre elas.

> ⚠️ Fundir só é recomendável quando a cardinalidade é *realmente* (1,1) dos dois lados. Se um dos lados for (0,1) — ou seja, o relacionamento é opcional — a fusão ainda é possível, mas gera muitos valores nulos na tabela (não é sempre a melhor escolha).

---

### 3.4 Resumo comparativo das três estratégias

| Estratégia            | Quando usar                              | Nº de tabelas geradas | Onde fica a informação do relacionamento |
|------------------------|-------------------------------------------|-------------------------|---------------------------------------------|
| Tabela própria          | Cardinalidade máxima **N:N**              | 3 (entidade + entidade + relacionamento) | Tabela própria com FKs compostas |
| Adição de colunas (FK)  | Cardinalidade máxima **1** de um dos lados | 2 (uma para cada entidade) | Coluna FK na tabela do lado "N" (ou do lado que aponta para o "1") |
| Fusão de tabelas        | Ambos os lados **(1,1)**                    | 1 (tabela única)         | Tudo junto na mesma tabela |

---

## 4. Implementação de Generalização/Especialização

Generalização/especialização representa uma hierarquia **superclasse → subclasses** (ex.: `PESSOA` generaliza `FUNCIONARIO` e `CLIENTE`). Existem **quatro estratégias clássicas** de implementação, e a escolha depende de dois critérios do modelo ER original:

- **Cobertura (totalidade):** *total* (toda ocorrência da superclasse deve pertencer a alguma subclasse) ou *parcial* (pode haver ocorrências da superclasse que não pertencem a nenhuma subclasse).
- **Disjunção (exclusividade):** *disjunta/exclusiva* (uma ocorrência pertence a no máximo uma subclasse) ou *sobreposta* (uma ocorrência pode pertencer a mais de uma subclasse simultaneamente).

### Modelo ER de exemplo (usado em todas as estratégias abaixo)

```
                ┌───────────────┐
                │     PESSOA     │
                │ *cpf            │
                │  nome           │
                └───────┬────────┘
                        │
                    (generalização)
                        │
            ┌───────────┴───────────┐
            │                        │
   ┌───────────────┐        ┌───────────────┐
   │  FUNCIONARIO    │        │    CLIENTE      │
   │  salario         │        │  limite_credito │
   └───────────────┘        └───────────────┘
```

---

### 4.1 Estratégia 1 — Uma tabela para toda a hierarquia (tabela única)

Todos os atributos (da superclasse e de **todas** as subclasses) são reunidos em **uma única tabela**, acrescida de um atributo discriminador que indica a qual subclasse cada linha pertence.

**Melhor quando:** a hierarquia é pequena, com poucos atributos específicos por subclasse.

Tabela resultante (PESSOA, com coluna `tipo` como discriminador: 'F' = Funcionário, 'C' = Cliente):

| cpf | nome | tipo | salario | limite_credito |
|-----|------|------|---------|------------------|
| 111 | Ana  | F    | 3500.00 | NULL             |
| 222 | Bruno | C   | NULL    | 2000.00          |
| 333 | Célia | F   | 4200.00 | NULL             |

⚠️ Gera colunas com muitos valores **NULL** quando as subclasses têm atributos bem diferentes entre si.

---

### 4.2 Estratégia 2 — Uma tabela para cada subclasse (atributos da superclasse duplicados)

Não se cria tabela para a superclasse. Os atributos da superclasse são **duplicados** em cada tabela de subclasse.

**Melhor quando:** a especialização é **total e disjunta** (toda pessoa é OU funcionário OU cliente, nunca as duas, e nunca nenhuma).

**FUNCIONARIO**

| cpf | nome | salario |
|-----|------|---------|
| 111 | Ana  | 3500.00 |
| 333 | Célia | 4200.00 |

**CLIENTE**

| cpf | nome  | limite_credito |
|-----|-------|------------------|
| 222 | Bruno | 2000.00          |

⚠️ Só funciona bem se a cobertura for **total** — senão, "pessoas puras" (nem funcionário nem cliente) ficam sem lugar para existir. E se for **sobreposta**, o `nome` fica duplicado em duas tabelas.

---

### 4.3 Estratégia 3 — Uma tabela para a superclasse + uma para cada subclasse (com FK)

Cria-se uma tabela para a superclasse (com todos os atributos comuns) e uma tabela **para cada subclasse**, contendo apenas os atributos específicos + uma **chave estrangeira que também é chave primária** (referenciando a superclasse).

**Melhor quando:** a especialização é **parcial** e/ou **sobreposta** (mais flexível — funciona em qualquer caso).

**PESSOA**

| cpf | nome  |
|-----|-------|
| 111 | Ana   |
| 222 | Bruno |
| 333 | Célia |
| 444 | Davi  |

**FUNCIONARIO**

| cpf | salario |
|-----|---------|
| 111 | 3500.00 |
| 333 | 4200.00 |

**CLIENTE**

| cpf | limite_credito |
|-----|------------------|
| 222 | 2000.00          |
| 111 | 1500.00          |

> Observe: `Ana (cpf 111)` aparece em **PESSOA**, **FUNCIONARIO** *e* **CLIENTE** — ela é funcionária e cliente ao mesmo tempo (sobreposição). `Davi (cpf 444)` está em **PESSOA** mas não é nem funcionário nem cliente (cobertura parcial). Essa estratégia é a que melhor representa **qualquer** combinação de total/parcial e disjunta/sobreposta.

---

### 4.4 Estratégia 4 — Ignorar a especialização / uma tabela por subclasse + atributos comuns em cada uma (variação da 2)

Alguns autores tratam como uma quarta variação a **junção parcial**: manter tabela da superclasse apenas quando ela tem muitos atributos próprios e vários relacionamentos, e duplicar atributos comuns leves nas subclasses. Na prática, a escolha entre as estratégias 1, 2 e 3 é o que realmente importa — a tabela abaixo resume quando usar cada uma.

### 4.5 Resumo comparativo — Generalização/Especialização

| Estratégia                                             | Total + Disjunta | Total + Sobreposta | Parcial + Disjunta | Parcial + Sobreposta |
|----------------------------------------------------------|:---:|:---:|:---:|:---:|
| 1. Tabela única (com discriminador)                       | ✅ | ⚠️ (muitos NULLs) | ⚠️ | ⚠️ |
| 2. Uma tabela por subclasse (sem tabela da superclasse)   | ✅ (ideal) | ❌ (duplica dados) | ❌ (perde "pessoas puras") | ❌ |
| 3. Superclasse + subclasses com FK                        | ✅ | ✅ (ideal) | ✅ (ideal) | ✅ (ideal) |

**Conclusão prática:** a **estratégia 3** (superclasse + subclasses ligadas por FK) é a mais usada no dia a dia, por ser a única que funciona corretamente em **todos** os casos de totalidade/disjunção — o custo é precisar reunir os dados completos de uma subclasse combinando as tabelas.

---

## 5. Quadro-resumo geral do capítulo

| Elemento do modelo ER              | Vira no modelo relacional                                  |
|--------------------------------------|----------------------------------------------------------------|
| Entidade                             | Tabela                                                          |
| Atributo simples                     | Coluna                                                          |
| Atributo composto                    | Várias colunas simples                                          |
| Atributo multivalorado               | Tabela própria (relacionamento 1:N com a entidade original)     |
| Identificador da entidade            | Chave primária (PK)                                             |
| Entidade fraca                       | Tabela com PK composta (id parcial + FK da entidade forte)      |
| Relacionamento N:N                   | Tabela própria com PK composta pelas FKs das entidades          |
| Relacionamento com cardinalidade máx. 1 | Coluna de FK adicionada à tabela do lado "1"                 |
| Relacionamento (1,1)–(1,1)            | Fusão em uma única tabela                                       |
| Generalização/Especialização          | Tabela única, tabelas separadas, ou superclasse+subclasses com FK (depende de total/parcial e disjunta/sobreposta) |