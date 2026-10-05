# Lista de Exercícios V — SQL (DDL)

**Componente:** Banco de Dados

## Esquema

```
cliente (id_cli, nome, cpf, num_conta, telefone, cidade)
carro   (id_car, chassi, modelo, cor, ano, preco)
aluguel (id_alu, id_cli, id_car, data_ent, data_sai, total)
```

> Os comandos abaixo usam sintaxe compatível com PostgreSQL. Diferenças para MySQL são indicadas nos comentários.

---

## 1.1. Criação das tabelas

As chaves estrangeiras de `aluguel` são definidas depois, nos itens **c** e **d**.

```sql
CREATE TABLE cliente (
    id_cli     INT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    cpf        CHAR(11) NOT NULL UNIQUE,
    num_conta  VARCHAR(20),
    telefone   VARCHAR(20),
    cidade     VARCHAR(50)
);

CREATE TABLE carro (
    id_car  INT PRIMARY KEY,
    chassi  VARCHAR(17) NOT NULL UNIQUE,
    modelo  VARCHAR(50),
    cor     VARCHAR(30),
    ano     INT,
    preco   DECIMAL(10,2)
);

CREATE TABLE aluguel (
    id_alu    INT PRIMARY KEY,
    id_cli    INT,
    id_car    INT,
    data_ent  DATE,
    data_sai  DATE,
    total     DECIMAL(10,2)
);
```

## 1.2. Operações

### a. Adicionar `agencia`, `rua`, `numero`, `bairro` e `estado` em `cliente`

```sql
ALTER TABLE cliente
    ADD COLUMN agencia VARCHAR(10),
    ADD COLUMN rua     VARCHAR(100),
    ADD COLUMN numero  VARCHAR(10),
    ADD COLUMN bairro  VARCHAR(50),
    ADD COLUMN estado  CHAR(2);
```

### b. Renomear `num_conta` para `conta` em `cliente`

```sql
ALTER TABLE cliente RENAME COLUMN num_conta TO conta;
```

### c. `id_cli` de `aluguel` como chave estrangeira de `cliente`

```sql
ALTER TABLE aluguel
    ADD CONSTRAINT fk_aluguel_cliente
    FOREIGN KEY (id_cli) REFERENCES cliente (id_cli);
```

### d. `id_car` de `aluguel` como chave estrangeira de `carro`

```sql
ALTER TABLE aluguel
    ADD CONSTRAINT fk_aluguel_carro
    FOREIGN KEY (id_car) REFERENCES carro (id_car);
```

### e. Apagar `conta` e `agencia` de `cliente`

```sql
ALTER TABLE cliente
    DROP COLUMN conta,
    DROP COLUMN agencia;
```

### f. Renomear `preco` para `valor` em `carro`

```sql
ALTER TABLE carro RENAME COLUMN preco TO valor;
```

### g. Valor padrão `0` para `valor` em `carro`

```sql
ALTER TABLE carro ALTER COLUMN valor SET DEFAULT 0;
-- MySQL: ALTER TABLE carro ALTER COLUMN valor SET DEFAULT 0;  (mesma sintaxe)
--    ou: ALTER TABLE carro MODIFY valor DECIMAL(10,2) DEFAULT 0;
```