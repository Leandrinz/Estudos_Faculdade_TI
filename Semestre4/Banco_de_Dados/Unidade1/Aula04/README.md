# Aula 04 — Propriedades das Entidades (Modelo Entidade-Relacionamento)

---

## 1. Atributo

**Atributo** é uma característica, propriedade ou dado que descreve uma Entidade ou um Relacionamento.

No diagrama ER (notação de Heuser), a entidade é representada por um **retângulo**, e os atributos por **elipses (óvalos)** ligadas a ela por uma linha.

```
                    ┌──────┐
                 ┌─▶│ id_c │  (identificador)
   ┌─────────┐   │  └──────┘
   │ CLIENTE │───┤
   └─────────┘   │  ┌──────┐
                 ├─▶│ nome │
                 │  └──────┘
                 │  ┌──────┐
                 └─▶│ cpf  │
                    └──────┘
```

Exemplo: os atributos da entidade **CLIENTE** são `id_c`, `nome` e `cpf`.

### Tipos de atributo (conteúdo complementar — muito cobrado em prova)

| Tipo | Definição | Exemplo |
|---|---|---|
| **Simples/Atômico** | Não pode ser dividido | `cpf`, `nome` |
| **Composto** | É formado por outros atributos | `endereço` (rua, número, bairro, cidade) |
| **Monovalorado** | Possui um único valor por ocorrência | `data_nascimento` |
| **Multivalorado** | Pode ter vários valores para a mesma entidade (representado por elipse **dupla**) | `telefone` (uma pessoa pode ter vários números) |
| **Derivado** | Seu valor é calculado a partir de outro atributo (representado por elipse **tracejada**) | `idade`, derivada de `data_nascimento` |
| **Chave/Identificador** | Identifica unicamente cada ocorrência (nome **sublinhado** no diagrama) | `cpf`, `id_c` |

```
Multivalorado:        Derivado:              Identificador:
   ┌───────────┐        ┌ ─ ─ ─ ─ ┐             ┌───────┐
  ((telefone))         ╎  idade  ╎              │ __cpf__│
   └───────────┘        └ ─ ─ ─ ─ ┘             └───────┘
  (elipse dupla)      (elipse tracejada)      (texto sublinhado)
```

---

## 2. Cardinalidade de um atributo

A **cardinalidade de atributo** indica o **número mínimo e máximo de valores** que aquele atributo pode assumir para cada ocorrência da entidade. É escrita como **(mínimo, máximo)** ao lado do atributo.

| Cardinalidade | Significado | Exemplo |
|---|---|---|
| **(1,1)** | Obrigatório e único → **por padrão, é omitida do diagrama** | `cpf` — todo cliente tem exatamente 1 CPF |
| **(0,1)** | Opcional (pode não existir), mas se existir só há 1 valor → atributo desenhado com **linha pontilhada** | `email` — cliente pode não informar |
| **(1,n)** | Obrigatório, podendo ter vários valores | `telefone` — todo cliente tem ao menos 1 |
| **(0,n)** | Opcional e pode ter vários valores | `dependente` |

> **Regra prática:** se a cardinalidade mínima é **0**, o atributo é **opcional** (linha pontilhada até a elipse). Se a cardinalidade for **(1,1)**, ela nem aparece escrita no diagrama — é o padrão implícito.

```
CLIENTE ──────── nome        (1,1) → não escreve nada, é o padrão

CLIENTE - - - - (email)      (0,1) → linha pontilhada = opcional

CLIENTE ══════ ((telefone))  (1,n) → multivalorado obrigatório
```

---

## 3. Atributos compostos

Um **atributo composto** é aquele que pode ser decomposto em outros atributos menores (chamados de *subatributos*), pois cada parte tem significado próprio.

**Exemplo: Endereço**

```
                         ┌────────┐
                      ┌─▶│  rua   │
                      │  └────────┘
                      │  ┌────────┐
   ┌───────────┐      ├─▶│ bairro │
   │ endereço  │──────┤  └────────┘
   └───────────┘      │  ┌────────┐
                      ├─▶│ número │
                      │  └────────┘
                      │  ┌────────┐
                      └─▶│ cidade │
                         └────────┘
```

No banco de dados relacional, um atributo composto normalmente é **quebrado em colunas separadas** (rua, bairro, número, cidade) na hora da implementação, mas no modelo conceitual ele aparece agrupado, pois faz sentido tratá-lo como uma unidade lógica ("o endereço do cliente").

---

## 4. Atributo de relacionamento

Assim como as entidades, os **relacionamentos** também podem ter atributos próprios — dados que só fazem sentido **por causa da associação** entre duas entidades, e não pertencem a nenhuma delas isoladamente.

**Exemplo:** o relacionamento **COMPRA**, entre as entidades `CLIENTE` e `PRODUTO`:

```
   ┌─────────┐          ┌──────────┐          ┌─────────┐
   │ CLIENTE │───(N,M)──◇  COMPRA  ◇──(N,M)───│ PRODUTO │
   └─────────┘          └──────────┘          └─────────┘
                              │
                              ├──▶ (data_compra)
                              ├──▶ (quantidade)
                              └──▶ (valor_unitário)
```

- `data_compra`, `quantidade` e `valor_unitário` não são atributos de `CLIENTE` nem de `PRODUTO`.
- Eles só existem **no momento em que um cliente compra um produto específico**, ou seja, pertencem ao **relacionamento**.

Outro exemplo clássico: `ALUNO` cursa `DISCIPLINA` → o relacionamento **CURSA** tem o atributo `nota` e `frequência` (não fazem sentido isolados de "qual aluno" + "qual disciplina").

---

## 5. Identificador de entidade

O **identificador** (ou **chave**) é um atributo (ou conjunto de atributos) capaz de distinguir, de forma única, cada ocorrência de uma entidade. No diagrama, é representado com o **nome sublinhado**.

### Conceitos complementares importantes:

| Termo | Definição |
|---|---|
| **Superchave** | Qualquer conjunto de atributos que identifica uma ocorrência de forma única (pode ter atributos redundantes) |
| **Chave candidata** | Superchave "mínima" — nenhum atributo pode ser removido sem perder a unicidade |
| **Chave primária** | A chave candidata **escolhida** para ser o identificador oficial da entidade |
| **Chave composta** | Identificador formado por **mais de um atributo** juntos |
| **Identificador alternativo (secundário)** | Quando existe **mais de um** conjunto de atributos capaz de identificar a entidade |

### "Vai ter vezes que você cria mais de um identificador"

Isso acontece quando a entidade possui **mais de uma chave candidata**. Você escolhe uma como **chave primária** e as demais viram **chaves alternativas**.

**Exemplo:**

```
   ┌────────────┐
   │  PESSOA    │
   └────────────┘
        │
        ├──▶ __id_pessoa__   ← chave primária (escolhida)
        ├──▶ __cpf__         ← chave alternativa (também identifica sozinha)
        └──▶  nome
```

Tanto `id_pessoa` quanto `cpf` identificam uma pessoa sozinhos — por isso ambos são identificadores válidos, mas normalmente se escolhe o mais estável/curto (`id_pessoa`) como chave primária.

**Exemplo de chave composta:** na entidade `ITEM_PEDIDO`, nem `id_pedido` nem `id_produto` identificam sozinhos uma linha da tabela — só a combinação dos dois: `(id_pedido, id_produto)`.

---

## 6. Generalização / Especialização

São **hierarquias** criadas entre entidades em um diagrama ER, representando uma relação do tipo "é um" (*is-a*).

- **Generalização:** parte das entidades específicas para chegar a uma entidade mais genérica (de baixo para cima).
- **Especialização:** parte de uma entidade genérica e a divide em entidades mais específicas (de cima para baixo).

Representada por um **triângulo** ligando a superclasse às subclasses.

```
                     ┌─────────┐
                     │ PESSOA  │   (superclasse / entidade genérica)
                     └─────────┘
                          │
                          ▽          ← triângulo de generalização
                        ╱   ╲
                       ╱     ╲
               ┌────────┐  ┌────────────┐
               │ ALUNO  │  │FUNCIONÁRIO │  (subclasses / entidades específicas)
               └────────┘  └────────────┘
```

### Quando usar

Use generalização/especialização quando você perceber que **duas ou mais entidades têm atributos parecidos** (ex.: nome, CPF, data de nascimento) **e também se relacionam com as mesmas entidades** — nesse caso, vale a pena "puxar" o que é comum para uma entidade genérica (superclasse) e manter em cada subclasse apenas os atributos/relacionamentos que são exclusivos dela.

| | PESSOA (genérica) | ALUNO (específica) | FUNCIONÁRIO (específica) |
|---|---|---|---|
| Atributos comuns | nome, cpf, data_nasc | — | — |
| Atributos exclusivos | — | matrícula, curso | salário, cargo |

---

### 6.1 Generalização/Especialização Total x Parcial

A letra dentro do triângulo indica se **toda** ocorrência da superclasse precisa obrigatoriamente pertencer a alguma subclasse:

| Símbolo | Nome | Significado |
|---|---|---|
| **T** (ou triângulo sem letra, dependendo do autor) | **Total** | **Toda** ocorrência da entidade genérica **obrigatoriamente** é também uma ocorrência de alguma subclasse. Não pode existir uma PESSOA que não seja nem ALUNO nem FUNCIONÁRIO. |
| **P** | **Parcial** | **Nem toda** ocorrência da entidade genérica precisa se encaixar em uma subclasse. Pode existir uma PESSOA que não seja ALUNO nem FUNCIONÁRIO (ex.: um visitante cadastrado só como PESSOA). |

```
     PESSOA                          PESSOA
        │                               │
        ▽ P        ← parcial            ▽ T      ← total
      ╱   ╲                           ╱   ╲
  ALUNO  FUNCIONÁRIO              ALUNO  FUNCIONÁRIO

  Pode haver PESSOA que          Toda PESSOA tem que ser
  não seja aluno nem              obrigatoriamente aluno
  funcionário                     ou funcionário
```

> Resumindo a dúvida do "explique melhor doq isso": o **P** não é sobre os atributos, é sobre a **obrigatoriedade de participação**. Ele responde à pergunta: *"É possível ter uma instância da superclasse que não pertence a nenhuma subclasse?"* Se sim → **Parcial (P)**. Se não → **Total (T)**.

---

### 6.2 Exclusiva x Compartilhada (disjunção)

Além de Total/Parcial, o triângulo também indica se uma mesma ocorrência pode pertencer a **mais de uma subclasse ao mesmo tempo**:

| Tipo | Significado | Exemplo |
|---|---|---|
| **Exclusiva (disjunta)** | Uma ocorrência só pode estar em **uma** subclasse por vez, nunca em duas ao mesmo tempo | Um `VEÍCULO` só pode ser `CARRO` **ou** `MOTO`, nunca os dois |
| **Compartilhada (sobreposta)** | A mesma ocorrência **pode pertencer a mais de uma subclasse simultaneamente** | Uma `PESSOA` pode ser `ALUNO` **e** `FUNCIONÁRIO` ao mesmo tempo (ex.: monitor que também é aluno) |

```
Exclusiva:                       Compartilhada:

     VEÍCULO                          PESSOA
        │                                │
        ▽                                ▽
      ╱   ╲                            ╱   ╲
   CARRO   MOTO                    ALUNO   FUNCIONÁRIO

 (nunca é os dois               (pode ser os dois
  ao mesmo tempo)                 ao mesmo tempo)
```

### Combinando as duas classificações

Como Total/Parcial e Exclusiva/Compartilhada são **independentes**, existem 4 combinações possíveis:

| Combinação | Exemplo |
|---|---|
| **Total + Exclusiva** | Todo `FUNCIONÁRIO` é `CLT` **ou** `PJ`, nunca os dois, e não existe funcionário fora dessas duas categorias |
| **Total + Compartilhada** | Todo `PROFISSIONAL_SAÚDE` é `MÉDICO` e/ou `PROFESSOR` (de faculdade de medicina), e todos se encaixam em pelo menos uma |
| **Parcial + Exclusiva** | `VEÍCULO` pode ser `CARRO` ou `MOTO` (nunca os dois), mas pode haver veículo sem categoria definida ainda |
| **Parcial + Compartilhada** | `PESSOA` pode ser `ALUNO` e/ou `FUNCIONÁRIO`, e pode haver pessoa que seja só visitante (nenhuma das duas) |

---

## 7. Resumo das figuras no diagrama ER (notação de Heuser)

| Símbolo | Representa | Como desenhar |
|---|---|---|
| ▭ Retângulo | Entidade | Retângulo com o nome da entidade dentro |
| ▭▭ Retângulo duplo | Entidade fraca (depende de outra para existir) | Retângulo dentro de outro retângulo |
| ⬭ Elipse | Atributo simples | Elipse ligada à entidade por uma linha |
| ⬭⬭ Elipse dupla | Atributo multivalorado | Duas elipses concêntricas |
| ⬭ (tracejada) | Atributo derivado | Elipse com borda pontilhada |
| — — — (linha pontilhada) | Atributo opcional (cardinalidade mínima 0) | Linha tracejada da entidade até a elipse |
| Nome sublinhado | Atributo identificador (chave) | Texto do atributo sublinhado |
| ◇ Losango | Relacionamento | Losango entre duas ou mais entidades |
| Elipse ligada ao losango | Atributo de relacionamento | Elipse saindo do losango, não da entidade |
| △ Triângulo | Generalização/Especialização | Liga a superclasse (em cima) às subclasses (embaixo) |
| T / P dentro do triângulo | Total / Parcial | Indica se toda ocorrência da superclasse deve estar em alguma subclasse |
| (linhas do triângulo separadas ou "coladas") | Exclusiva / Compartilhada | Indica se uma ocorrência pode estar em mais de uma subclasse ao mesmo tempo |
| (mín, máx) | Cardinalidade | Escrita ao lado da linha de ligação (ex: (0,n), (1,1)) |

---

### 📌 Quadro-resumo rápido para revisão

- **Atributo** = característica de entidade/relacionamento
- **(0, x)** = opcional (pontilhado) | **(1,1)** = obrigatório único (omitido do diagrama)
- **Atributo composto** = tem subatributos (ex: endereço)
- **Atributo multivalorado** = elipse dupla (ex: telefone)
- **Atributo derivado** = elipse tracejada (ex: idade)
- **Atributo de relacionamento** = pertence à associação, não à entidade (ex: data_compra em COMPRA)
- **Identificador** = nome sublinhado; pode haver mais de um (chave alternativa)
- **Generalização/Especialização** = triângulo, hierarquia "é um"
- **T (Total)** = toda instância da superclasse deve virar subclasse
- **P (Parcial)** = pode haver instância da superclasse fora de qualquer subclasse
- **Exclusiva** = só pode estar em uma subclasse por vez
- **Compartilhada** = pode estar em mais de uma subclasse ao mesmo tempo