# Aula 05 — Abordagem Relacional

## O que é

O **modelo relacional** é o modelo de dados utilizado pelos SGBDs (Sistemas Gerenciadores de Banco de Dados) do tipo relacional. Nele, os dados são organizados em **tabelas**, que se relacionam entre si por meio de chaves.

---

## Conceitos Básicos

### Tabela (ou Relação)
É uma estrutura organizada em:
- **Colunas** (atributos) — representam as características dos dados
- **Linhas** (tuplas) — representam os registros/ocorrências

### Campo
É cada valor armazenado na interseção de uma linha com uma coluna (uma célula).

Os valores de campo de uma tabela devem ser:
- **Atômicos** — não podem ser divididos em subvalores (não podem ser compostos por outros campos)
- **Monovalorados** — devem conter apenas um único valor (não é permitido mais de um valor no mesmo campo)

> *Exemplo:* o campo "Telefone" não deveria armazenar dois números separados por vírgula — isso violaria a atomicidade.

### Linha (ou Tupla)
Refere-se a um objeto, indivíduo ou entidade do mundo real representado na tabela (ex: um cliente, um produto, um pedido).

---

## Chaves

Chave é o conceito básico utilizado para **identificar linhas** de forma única dentro de uma tabela.

### Chave Primária (Primary Key — PK)
É uma coluna, ou uma combinação de colunas, cujos valores **distinguem uma linha das demais** dentro da mesma tabela. Não pode conter valores nulos nem se repetir.

> *Exemplo:* `CPF` em uma tabela de Clientes.

### Chave Estrangeira (Foreign Key — FK)
É uma coluna, ou uma combinação de colunas, cujos valores aparecem **necessariamente** como chave primária em outra tabela (ou na mesma tabela, no caso de autorrelacionamento). É o mecanismo que implementa os relacionamentos entre tabelas.

**Restrições associadas:**
- **Inclusão de uma linha com chave estrangeira:** só é permitida se o valor informado já existir como chave primária na tabela referenciada.
- **Alteração/exclusão do valor da chave primária referenciada:** pode ser restrita, propagada em cascata (`CASCADE`) ou definida como nula (`SET NULL`), dependendo da regra definida no banco.

> *Exemplo:* `id_cliente` na tabela Pedidos, que referencia `id_cliente` (PK) na tabela Clientes.

### Chave Alternativa (Candidate Key)
É qualquer outra coluna (ou combinação de colunas) que **também poderia ter sido escolhida como chave primária**, pois também identifica unicamente cada linha, mas não foi a selecionada.

> *Exemplo:* em uma tabela de Funcionários, tanto o `CPF` quanto a `Matrícula` identificam unicamente um funcionário. Se `Matrícula` for escolhida como chave primária, o `CPF` passa a ser uma chave alternativa.

---

## Domínio da Coluna

### O que é
Refere-se ao **conjunto de valores válidos** (numérico, alfanumérico, data, etc.) que os campos de determinada coluna podem assumir.

> *Exemplo:* o domínio da coluna "Idade" pode ser definido como números inteiros positivos entre 0 e 120.

### Valor Vazio (Nulo)
Quando uma tabela é definida, é necessário especificar se os campos de cada coluna podem ou não ficar vazios (aceitar valor `NULL`).

### Coluna Obrigatória
É a coluna em que **não são permitidos** campos vazios — ou seja, todo registro precisa ter um valor preenchido nela (restrição `NOT NULL`).

### Restrição de Integridade
São regras que precisam ser obrigatoriamente respeitadas pelos dados armazenados. O próprio SGBD é responsável por garantir seu cumprimento (ex: integridade referencial, unicidade de chave primária, domínio válido).

---

## Notações para Esquemas de BDs Relacionais

### 1. Esquema Textual (mais simples)
Representa a tabela e suas colunas em formato de texto, geralmente com a chave primária sublinhada ou destacada.

```
Cliente (id_cliente, nome, email, telefone)
Pedido (id_pedido, data, id_cliente*)
```
*(o `*` ou sublinhado indica chave estrangeira/primária)*

### 2. Esquema Diagramático
Representa as tabelas visualmente, geralmente em retângulos, com as colunas listadas dentro e linhas conectando as chaves estrangeiras às primárias correspondentes (Diagrama Entidade-Relacionamento ou Diagrama de Esquema Relacional).

```
┌─────────────────┐        ┌──────────────────────┐
│     Cliente      │        │        Pedido         │
├─────────────────┤        ├──────────────────────┤
│ PK id_cliente    │◄───────│ PK id_pedido           │
│    nome          │        │    data                │
│    email         │        │ FK id_cliente          │
│    telefone      │        │                        │
└─────────────────┘        └──────────────────────┘
```

---

## Resumo Rápido

| Conceito | Definição em uma frase |
|---|---|
| Tabela | Estrutura de linhas e colunas |
| Campo | Valor atômico e monovalorado em uma célula |
| Linha | Um objeto/indivíduo representado |
| Chave Primária | Identifica unicamente cada linha |
| Chave Estrangeira | Referencia a chave primária de outra tabela |
| Chave Alternativa | Candidata a PK que não foi escolhida |
| Domínio | Conjunto de valores válidos de uma coluna |