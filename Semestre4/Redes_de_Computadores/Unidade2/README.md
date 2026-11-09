# 📘 Redes de Computadores — Apostila Intensiva de 2 Dias (Edição Atualizada)

> **Objetivo:** te preparar para a próxima avaliação cobrindo o **protocolo IP** (Lista de Exercícios 04, questão 2, itens **2(a) a 2(v)**): cabeçalho IPv4 em hexadecimal, fragmentação, interpretação de fragmentos, comparação IPv4 × IPv6, prioridade, checksum e transição IPv4/IPv6 (tradução e tunelamento).
> **Como usar:** leia na ordem, faça os exercícios **antes** de olhar o gabarito, e só avance quando acertar. Os gabaritos ficam no fim de cada módulo, em seção separada.
> **O que mudou em relação à apostila anterior:** o módulo de TCP/UDP foi **retirado** (você já estudou na 1ª unidade). Os módulos de cabeçalho, fragmentação e transição foram **mantidos e aprofundados**, e entraram os módulos de interpretação de fragmentos, campos que mudam nos roteadores e IPv4 × IPv6.

**Legenda**
- 🧠 **MEMORIZE** = precisa estar na cabeça no dia da prova.
- 🚚 **Analogia** = ideia intuitiva. Depois dela sempre vem a explicação técnica.
- ⚠️ **Cuidado** = erro comum que tira ponto.
- ✍️ **Na prova** = como o assunto aparece na avaliação.
- 🔍 **Como ler a questão** = como descobrir o que o enunciado está pedindo.
- 📎 **Complementar** = pode ignorar se faltar tempo.

---

# 🗂️ SUMÁRIO

0. [Diagnóstico: a prova anterior, a Lista 04 e o seu nível](#0-diagnóstico-a-prova-anterior-a-lista-04-e-o-seu-nível)
1. [Cronograma de 2 dias](#1-cronograma-de-2-dias)
2. [Módulo 1 — Datagrama e cabeçalho IPv4 (inclui leitura em hexadecimal)](#módulo-1--datagrama-e-cabeçalho-ipv4-leitura-em-hexadecimal)
3. [Módulo 2 — Fragmentação IPv4 (prioridade máxima)](#módulo-2--fragmentação-ipv4-prioridade-máxima)
4. [Módulo 3 — Interpretando fragmentos e campos que mudam nos roteadores](#módulo-3--interpretando-fragmentos-e-campos-que-mudam-nos-roteadores)
5. [Módulo 4 — IPv4 × IPv6: cabeçalhos, prioridade e checksum](#módulo-4--ipv4--ipv6-cabeçalhos-prioridade-e-checksum)
6. [Módulo 5 — Transição IPv4/IPv6: tradução e tunelamento](#módulo-5--transição-ipv4ipv6-tradução-e-tunelamento)
7. [Simulado final](#simulado-final)
8. [Gabarito do simulado](#gabarito-do-simulado)
9. [Folha de revisão relâmpago](#folha-de-revisão-relâmpago)
10. [Mapa de cobertura: itens 2(a) a 2(v)](#mapa-de-cobertura-itens-2a-a-2v)

---

# 0. Diagnóstico: a prova anterior, a Lista 04 e o seu nível

## 0.1 Como o professor cobrou na prova anterior (referência de estilo)

| Questão | Tema | Pontos | O que o professor quis | Vai cair de novo? |
|---|---|---|---|---|
| **1** | TCP e retenção de dados | 3,0 | Citar e explicar mecanismos | ❌ **Retirado desta apostila** (já estudado na 1ª unidade) |
| **2** | Fragmentação IPv4 no roteador de borda | 4,0 | **Todos os campos de todos os datagramas** que saem do roteador + **cálculos** + **justificativa** de cada campo que mudou | ✅ **Modelo mais importante**: foco total |
| **3** | Tradução e tunelamento IPv4→IPv6 | 3,0 | Explicar em linhas gerais, apontar problemas da tradução e como o tunelamento os resolveu | ✅ Mantido (item 2(v) da lista) |

**Padrão do professor (guarde isto):**
1. Dá os **números** (campos do cabeçalho, MTU, tamanho dos dados).
2. Exige **cálculos** mostrados (sem cálculo, a resposta é desconsiderada).
3. Exige **justificativa** de cada campo que mudou (ou não mudou).
4. Nas discursivas, pede **citar + explicar** (só citar vale metade).

## 0.2 O que a Lista 04 pede (questão 2, itens a–v)

| Grupo | Itens | Habilidade principal | Módulo |
|---|---|---|---|
| Ler um cabeçalho em hexadecimal | 2(a) | Converter hex, separar campos, concluir sobre fragmentação, dados, TTL, ID | 1 |
| HLEN, opções e comprimento | 2(b), 2(c), 2(d), 2(e), 2(f) | Relacionar HLEN × bytes de cabeçalho × opções × dados × comprimento total | 1 |
| Campo Protocol | 2(g) | Entender como o IP sabe a quem entregar o payload | 1 |
| Fragmentação (cálculos) | 2(h), 2(i), 2(j) | Quantidade de fragmentos, tamanhos, ID, flags, offsets; ID igual em todos | 2 |
| Interpretar bytes e offsets | 2(k), 2(p), 2(q) | Primeiro e último byte de dados a partir do offset | 3 |
| Classificar um datagrama | 2(l), 2(m), 2(n), 2(o) | Completo, primeiro, intermediário ou último fragmento | 3 |
| Campos que mudam nos roteadores | 2(r) | Obrigatórios × eventuais | 3 |
| IPv4 × IPv6 | 2(s), 2(t), 2(u) | Comparar campos, prioridade/precedência, fim do checksum | 4 |
| Transição | 2(v) | Tradução, tunelamento e qual é melhor | 5 |

## 0.3 Dados do exemplo-guia da prova anterior (Figura 1)

Usaremos esta figura como exemplo resolvido no Módulo 2, porque ela mostra o **estilo exato** do professor.

| Item da figura | Valor |
|---|---|
| **MTU** (roteador de borda) | **500 bytes** |
| Segmento da camada de transporte (payload) | **2.500 bytes** |
| Versão / HLEN / Tipo de serviço | 4 / 5 (**20 bytes**) / 0 |
| Comprimento do datagrama | **2.520** (= 20 + 2.500) |
| ID | **1.350** |
| Flags | **000** (DF = 0, MF = 0) |
| Deslocamento | **0** |
| TTL | **7** |
| Protocolo | **17** (= UDP) |
| Checksum | 0xFFFF |
| IP de origem / destino | 192.168.0.2 / 15.47.201.23 |

## 0.4 O que pode ser ignorado (por falta de tempo)

- **TCP/UDP como assunto** (só o necessário: o campo *Protocol* aponta para eles).
- **Endereçamento, máscaras, subnetting e notação IPv6**: você já domina (item 1 da lista) e não será revisado aqui.
- **ICMP** (item 3 da lista): fora de prioridade. Aparece só em notas de rodapé.
- Cálculo manual do checksum (📎 complementar; você precisa saber **que muda e por quê**).
- IPv6 além do necessário (cabeçalhos de extensão em detalhes, NDP, SLAAC).

## 0.5 Seu diagnóstico de lacunas

| Área | Situação | Prioridade |
|---|---|---|
| Endereçamento IPv4, máscaras, subnetting, notação IPv6 | ✅ Domina | Fora do plano |
| Hexadecimal aplicado ao cabeçalho, campos IPv4 | ❌ Do zero | **Altíssima** |
| Fragmentação IPv4 (cálculos, offsets, flags) | ❌ Dificuldade | **Máxima** |
| Interpretar fragmentos (primeiro/último byte, tipo) | ❌ Do zero | Alta |
| IPv4 × IPv6 (campos, prioridade, checksum) | ⚠️ Parcial (conhece só o formato dos endereços) | Alta |
| Tradução e tunelamento | ⚠️ Teórico, rápido de aprender | Média-alta |

> 💡 **Boa notícia:** o que você já sabe (binário, potências de 2, divisão em blocos, máscaras) é exatamente a base de que precisa. A conta de fragmentação é uma divisão com arredondamento. A dificuldade real é conhecer **as regras**, e elas cabem em uma página (a folha relâmpago no fim).

---

# 1. Cronograma de 2 dias

Considera ~6 a 7 horas líquidas de estudo por dia, **com pausas**. É um plano realista: se atrasar, corte apenas o que está marcado com 📎, e **nunca corte a prática de fragmentação nem a leitura de cabeçalho em hexadecimal**.

## 📅 Dia 1 — Ler cabeçalhos e dominar a fragmentação

| Bloco | Atividade | Duração |
|---|---|---|
| 1 | **Módulo 1**, 1.1 a 1.5: ideia de datagrama, hexadecimal, mapa do cabeçalho, campos | 1h15 |
| 2 | **Módulo 1**, 1.6 a 1.10: HLEN/opções, comprimento, TTL/ID, Protocol, **método de leitura em hex** e a resolução do 2(a) | 1h |
| Pausa | | 15 min |
| 3 | **Exercícios do Módulo 1** (faça sozinho, depois confira) | 1h |
| 4 | **Módulo 2**, 2.1 a 2.5: MTU, por que múltiplo de 8, algoritmo, flags | 1h15 |
| Pausa | | 15 min |
| 5 | **Módulo 2**, 2.6 a 2.8: exemplos resolvidos (**cubra a resolução e refaça sozinho**) | 1h |
| 6 | **Exercícios do Módulo 2** (Ex. 2.1 a 2.6) | 1h15 |
| Fechamento | Escrever **de cabeça**, em papel, o checklist de fragmentação e a tabela "campos de um cabeçalho em hex" | 20 min |

## 📅 Dia 2 — Interpretar, comparar, transição e simulado

| Bloco | Atividade | Duração |
|---|---|---|
| 1 | Refazer **de cabeça** 2 exercícios de fragmentação do Módulo 2 (um com ajuste de múltiplo de 8, um de fragmento de fragmento) | 40 min |
| 2 | **Módulo 3**: offset → bytes, classificação do datagrama, campos que mudam + exercícios | 1h15 |
| Pausa | | 15 min |
| 3 | **Módulo 4**: IPv4 × IPv6, prioridade, checksum + exercícios (e escrever sua versão da resposta 2(s)) | 1h15 |
| 4 | **Módulo 5**: tradução, tunelamento, qual é melhor + exercícios | 1h |
| Pausa | | 15 min |
| 5 | **Simulado final** em condições de prova (cronometrado: 1h30) | 1h30 |
| Fechamento | Correção do simulado + **folha de revisão relâmpago** (só reler) | 45 min |

> 🧠 **Regra de ouro:** errou um exercício? Volte à teoria **no ponto exato do erro**, depois refaça um **similar** antes de avançar.
> 🧠 **Regra de prova:** o dia anterior à prova termina na folha relâmpago, não em conteúdo novo.

---

# MÓDULO 1 — Datagrama e cabeçalho IPv4 (leitura em hexadecimal)

> **Cobre os itens:** 2(a), 2(b), 2(c), 2(d), 2(e), 2(f), 2(g).

## 1.1 A ideia básica: a encomenda

🚚 **Analogia.** Toda encomenda tem:
- **Etiqueta** (remetente, destinatário, prazo de validade, tipo de serviço, número de rastreio…) → o **cabeçalho**.
- **Conteúdo** (o produto) → a **carga útil**, o **payload**.

Na rede:
- **Datagrama IPv4** = **pacote da camada de rede (IP)** = **cabeçalho IP + payload**.
- **Payload** = o que o IP carrega: os **dados** que vieram da camada acima (a camada de transporte). Nesta apostila tratamos esse conteúdo como "dados" e não nos importamos com o que há dentro dele, a não ser pelo campo **Protocol**.
- **Cabeçalho** = informações de controle para os roteadores saberem **para onde** e **como** levar o pacote.

```
┌──────────────────────┬───────────────────────────────────────┐
│  CABEÇALHO IPv4      │            DADOS (payload)            │
│  (mín. 20 bytes)     │            (ex.: 2.500 bytes)         │
└──────────────────────┴───────────────────────────────────────┘
└────────────── COMPRIMENTO TOTAL = 2.520 bytes ───────────────┘
```

🧠 **Fórmula-base (use sempre):**

> **Comprimento total = Cabeçalho + Dados**
> **Dados = Comprimento total − Cabeçalho**
> **Cabeçalho (em bytes) = HLEN × 4**

**O que significa cada elemento:**
- *Comprimento total*: tamanho do datagrama **inteiro**, em bytes (campo do cabeçalho).
- *Cabeçalho*: bytes ocupados pela "etiqueta".
- *Dados*: bytes do conteúdo transportado.
- *HLEN*: campo que informa o tamanho do cabeçalho (explicado na seção 1.5).

**Exemplo de uso:** comprimento total 1.500 e cabeçalho 20 → dados = 1.500 − 20 = **1.480**.

## 1.2 Hexadecimal sem medo

A Lista 04 entrega o cabeçalho em **hexadecimal** (base 16). Como você já sabe binário, isso é fácil.

**Por que hexadecimal?** Porque **1 dígito hexa = exatamente 4 bits**, então um texto como `4500` é uma forma curta de escrever `0100 0101 0000 0000`. Engenheiros usam hexa para **abreviar binário**.

**Os 16 dígitos** 🧠

| Hexa | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | A | B | C | D | E | F |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| Decimal | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | **10** | **11** | **12** | **13** | **14** | **15** |
| Binário | 0000 | 0001 | 0010 | 0011 | 0100 | 0101 | 0110 | 0111 | 1000 | 1001 | 1010 | 1011 | 1100 | 1101 | 1110 | 1111 |

**Hexadecimal → decimal.** Cada posição vale uma potência de 16: da direita para a esquerda, **1, 16, 256, 4.096**.

> Exemplo 1: `0x54` = 5 × 16 + 4 × 1 = 80 + 4 = **84**.
> Exemplo 2: `0x0214` = 0 × 4.096 + 2 × 256 + 1 × 16 + 4 = 512 + 16 + 4 = **532**.
> Exemplo 3: `0xB4` = 11 × 16 + 4 = 176 + 4 = **180** (note: B = 11).
> Exemplo 4: `0x1F40` = 1 × 4.096 + 15 × 256 + 4 × 16 + 0 = 4.096 + 3.840 + 64 = **8.000**.

(O prefixo `0x` só indica "isto é hexadecimal". O valor é o mesmo sem ele.)

**Decimal → hexadecimal.** Divida por 16 e anote os restos, de trás para frente.
> Exemplo: 200 ÷ 16 = 12 resto 8 → 12 = C, resto 8 → **0xC8**.
> Exemplo: 500 ÷ 256 = 1 resto 244; 244 ÷ 16 = 15 resto 4 → 1, F, 4 → **0x01F4**.

**Um byte = 2 dígitos hexa.** Por isso um endereço IPv4 de 4 bytes vira 8 dígitos hexa: `C0A8 0002` → C0 = 192, A8 = 168, 00 = 0, 02 = 2 → **192.168.0.2**.

⚠️ **Cuidado.** Letras maiúsculas ou minúsculas são iguais (`B4` = `b4`). Não confunda `0x10` (que vale **16**) com o decimal 10.

## 1.3 O mapa do cabeçalho: 5 linhas de 32 bits

```
 0       4       8              16      19                     31
┌───────┬───────┬──────────────┬──────────────────────────────────┐
│Versão │ HLEN  │ Tipo Serviço │       Comprimento Total          │  ← linha 1
├───────┴───────┴──────────────┼───────┬──────────────────────────┤
│        Identificação (ID)    │ Flags │   Deslocamento (Offset)  │  ← linha 2
├──────────────┬───────────────┼───────┴──────────────────────────┤
│     TTL      │   Protocolo   │      Checksum do cabeçalho       │  ← linha 3
├──────────────┴───────────────┴──────────────────────────────────┤
│                       IP de Origem                              │  ← linha 4
├─────────────────────────────────────────────────────────────────┤
│                       IP de Destino                             │  ← linha 5
├─────────────────────────────────────────────────────────────────┤
│                   Opções (se houver) + Padding                  │  ← linhas extras
└─────────────────────────────────────────────────────────────────┘
```

Cada linha tem **32 bits = 4 bytes = 8 dígitos hexa = 2 "palavras" de 4 dígitos**. Sem opções são **5 linhas = 20 bytes = 10 palavras de 4 dígitos hexa**. Por isso o cabeçalho que a lista entrega tem **10 grupos de 4 dígitos**:

```
Palavra:   1     2     3     4     5     6     7     8     9     10
         4500  0054  0003  5850  2006  0000  7C4E  0302  B40E  0F02
         └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘
          linha 1      linha 2      linha 3      linha 4      linha 5
```

## 1.4 Tabela explicativa dos campos 🧠

> **Explicação de todas as siglas antes de usar:** **HLEN** (*Header Length*, tamanho do cabeçalho), **ID** (identificação), **DF** (*Don't Fragment*, não fragmentar), **MF** (*More Fragments*, mais fragmentos), **TTL** (*Time To Live*, tempo de vida), **MTU** (*Maximum Transmission Unit*, unidade máxima de transmissão).

| Campo | Tamanho | Onde está (palavra) | O que é / função |
|---|---|---|---|
| **Versão** | 4 bits | 1º dígito da palavra 1 | Versão do IP (4 ou 6) |
| **HLEN** | 4 bits | 2º dígito da palavra 1 | Tamanho do cabeçalho **em unidades de 4 bytes**. **Bytes = HLEN × 4** |
| **Tipo de serviço (TOS)** | 8 bits | últimos 2 dígitos da palavra 1 | Prioridade/qualidade desejada (ver Módulo 4) |
| **Comprimento total** | 16 bits | palavra 2 | Tamanho do datagrama **inteiro** (cabeçalho + dados), em bytes. Máximo 65.535 |
| **Identificação (ID)** | 16 bits | palavra 3 | "Número de série" do datagrama original; **todos os fragmentos o repetem** |
| **Flags** | 3 bits | 3 primeiros bits da palavra 4 | Reservado; **DF**; **MF** |
| **Deslocamento (*Fragment Offset*)** | 13 bits | demais 13 bits da palavra 4 | Posição do pedaço dentro do original, **em unidades de 8 bytes** |
| **TTL** | 8 bits | 1º byte (2 dígitos) da palavra 5 | Cada roteador subtrai 1; ao chegar a 0, o datagrama é descartado |
| **Protocol (Protocolo)** | 8 bits | 2º byte da palavra 5 | Quem recebe o payload: **6 = TCP, 17 = UDP, 1 = ICMP, 41 = IPv6** |
| **Checksum** | 16 bits | palavra 6 | Verificação de erros **só do cabeçalho** |
| **IP de origem** | 32 bits | palavras 7 e 8 | Quem enviou |
| **IP de destino** | 32 bits | palavras 9 e 10 | Para quem vai |
| **Opções** | 0 a 40 bytes | após a palavra 10 | Recursos raros; se houver, HLEN > 5 |

### Os 3 bits de flags e os 13 bits de deslocamento (a palavra 4)

A palavra 4 tem 16 bits: **3 de flags + 13 de deslocamento**. Para separá-los, converta a palavra para binário.

```
Bit:     0           1        2          3 ... 15
       ┌─────────┬────────┬────────┬─────────────────────────┐
       │Reservado│   DF   │   MF   │   Deslocamento (13 bits)│
       │ (sempre │ 1=não  │ 1=mais │                         │
       │   0)    │fragmentar│fragmentos│                       │
       └─────────┴────────┴────────┴─────────────────────────┘
```

> ⚠️ **Convenção dos bits de flags.** No padrão (RFC 791, e também no livro do Forouzan) a ordem é **[reservado][DF][MF]**: `000` = nenhuma flag; `010` = só DF; `001` = só MF. Na Figura 1 da prova anterior os bits apareciam rotulados como `0 M D` (2º bit = M, 3º bit = D); **confira nos seus slides**. Para nunca perder ponto: **escreva sempre por extenso** "MF = 1, DF = 0" e informe a ordem que usou. Nesta apostila, salvo aviso, usamos a **ordem padrão [0][DF][MF]**.

**Atalho para separar flags e deslocamento a partir do hexa.** O **primeiro dígito** da palavra 4 contém as 3 flags **mais o bit mais alto do deslocamento**:

| 1º dígito | Binário | Flags (0, DF, MF) | Bit alto do deslocamento |
|---|---|---|---|
| 0 | 0000 | 000 | 0 |
| 1 | 0001 | 000 | 1 (soma 4.096 ao deslocamento) |
| 2 | 0010 | 001 → **MF = 1** | 0 |
| 3 | 0011 | 001 → **MF = 1** | 1 (soma 4.096) |
| 4 | 0100 | 010 → **DF = 1** | 0 |
| 5 | 0101 | 010 → **DF = 1** | 1 (soma 4.096) |
| 6 | 0110 | 011 → DF = 1 e MF = 1 | 0 |
| 7 | 0111 | 011 → DF = 1 e MF = 1 | 1 (soma 4.096) |

> **Deslocamento** = (valor dos 12 bits finais = os 3 últimos dígitos) **+ 4.096 se o bit alto for 1**.
> Exemplo: `0x20B9` → 1º dígito 2 (flags 001, bit alto 0); deslocamento = 0x0B9 = 11 × 16 + 9 = **185**.
> Exemplo: `0x5850` → 1º dígito 5 (flags 010, bit alto 1); deslocamento = 4.096 + 0x850 = 4.096 + (8 × 256 + 5 × 16) = 4.096 + 2.128 = **6.224**.

## 1.5 HLEN, opções e tamanho do cabeçalho (itens 2(b), 2(c), 2(d))

**Por que existe o HLEN?** O cabeçalho pode ter **tamanho variável**: ele tem uma parte fixa de **20 bytes** e, às vezes, **opções** (recursos extras raros, como registrar a rota percorrida). O roteador precisa saber **onde o cabeçalho termina e onde começam os dados**. O HLEN diz isso.

**Por que "× 4"?** O HLEN tem **4 bits** (vai de 0 a 15). Se contasse em bytes, só chegaria a 15 bytes (nem caberia o cabeçalho fixo de 20). Então ele conta em **palavras de 4 bytes** (= as linhas de 32 bits do mapa). Assim: máximo = 15 × 4 = **60 bytes**.

🧠 **Relações para memorizar:**

| Relação | Fórmula | Exemplo |
|---|---|---|
| Cabeçalho em bytes | **HLEN × 4** | HLEN 5 → 20 bytes |
| Bytes de opções | **(HLEN × 4) − 20** | HLEN 7 → 28 − 20 = **8 bytes** |
| HLEN a partir das opções | **(20 + opções) ÷ 4** | opções 12 → 32 ÷ 4 = **8** |
| HLEN mínimo | **5** (cabeçalho sem opções) | |
| HLEN máximo | **15** (60 bytes; 40 de opções) | |

### Resolução do item 2(b): "HLEN = 7. Quantos bytes de opção?"
1. Cabeçalho = 7 × 4 = **28 bytes**. (O 4 vem de cada palavra ter 4 bytes.)
2. Parte fixa = **20 bytes** (sempre existe).
3. Opções = 28 − 20 = **8 bytes**.

### Resolução do item 2(c): "Opções de 20 bytes. HLEN em binário?"
1. Cabeçalho = 20 (fixo) + 20 (opções) = **40 bytes**.
2. HLEN = 40 ÷ 4 = **10**.
3. Em binário (4 bits): 10 = 8 + 2 = **1010**.

### Resolução do item 2(d): "HLEN pode ser menor que 5?"
**Não.** HLEN = 5 significa 20 bytes, que é a **parte fixa mínima**: ela **sempre** existe (versão, comprimento, ID, TTL, endereços etc.). HLEN menor que 5 (ex.: 4 → 16 bytes) descreveria um cabeçalho que não caberia nem os campos obrigatórios. Um roteador que receba HLEN < 5 considera o datagrama **inválido e o descarta**.

⚠️ **Cuidado:** o cabeçalho **sempre é múltiplo de 4 bytes**. Se as opções não somarem múltiplo de 4, o campo **padding** (enchimento de zeros) completa. Por isso nunca existe "cabeçalho de 22 bytes".

## 1.6 Comprimento total × dados (itens 2(e), 2(f))

### Resolução do item 2(e): "1.024 bytes de dados, sem opções. HLEN? Comprimento total?"
1. Sem opções → cabeçalho = 20 bytes → **HLEN = 20 ÷ 4 = 5**.
2. Comprimento total = cabeçalho + dados = 20 + 1.024 = **1.044**.

### Resolução do item 2(f): "Comprimento total 36, HLEN 5. Quantos bytes de dados?"
1. Cabeçalho = 5 × 4 = 20.
2. Dados = 36 − 20 = **16 bytes**.

🔍 **Como ler a questão.** Identifique **o que foi dado** e **o que se pede**: o trio (HLEN, comprimento total, dados) está sempre ligado por `total = HLEN × 4 + dados`. Dados 2 dos 3 e você acha o terceiro. Atenção: **HLEN é em palavras, comprimento total é em bytes**. Converta antes de somar.

## 1.7 TTL e ID

**TTL** (*Time To Live*): **contador de saltos**. 🚚 *Analogia:* uma carta que só pode passar por N agências; cada agência carimba e reduz o número; ao chegar a zero, a carta é jogada fora (para não ficar rodando para sempre se houver um loop de rotas).
- Cada roteador que encaminha o datagrama **subtrai 1**.
- Se o valor, depois de subtrair, chegar a **0**, o roteador **descarta** o datagrama.
- **Quantos roteadores a mais?** Valor do TTL. (*Detalhe técnico:* com TTL = N, o datagrama é encaminhado por N − 1 roteadores e **descartado no N-ésimo**, que o reduz a 0. A resposta esperada nas listas é "**N roteadores**", e você pode acrescentar essa nota.)

**ID** (identificação): 🚚 é o **número de rastreio da encomenda original**. A origem coloca um número em cada datagrama novo. Se o datagrama for **fragmentado**, **todos os fragmentos levam o mesmo ID**, e o destino usa isso para reunir os pedaços certos (detalhado no Módulo 2).

## 1.8 O campo Protocol (item 2(g))

**Problema:** quando um datagrama chega ao host de destino, o IP recebe uma "caixa" de dados. Mas o que há dentro dela? Um segmento do TCP? Do UDP? Uma mensagem ICMP? Quem deve receber?

🚚 **Analogia:** um prédio com vários departamentos (TCP, UDP, ICMP…). A portaria recebe uma encomenda e olha na etiqueta o **número do departamento**. Esse número é o campo **Protocol**.

**Resposta técnica:** a camada de rede lê o campo **Protocol** (8 bits) do cabeçalho IP e entrega o payload ao protocolo correspondente da camada acima. Esse processo é chamado de **demultiplexação** (separar os dados e mandar cada um ao destinatário certo).

| Valor (decimal) | Hexa | Protocolo que recebe o payload |
|---|---|---|
| 1 | 0x01 | ICMP |
| **6** | 0x06 | **TCP** |
| **17** | 0x11 | **UDP** |
| 41 | 0x29 | IPv6 encapsulado (usado em túneis, Módulo 5) |

**Resposta-modelo do item 2(g):** o host B examina o campo **Protocol** do cabeçalho IP do datagrama. Se o valor for **6**, entrega o payload ao TCP; se for **17**, ao UDP. É o campo Protocol que informa a qual protocolo da camada de transporte (ou outra) o payload pertence.

⚠️ **Cuidado:** o campo Protocol está **no cabeçalho IP** (camada de rede) e **não** no TCP/UDP. Não confunda com as **portas**, que ficam no cabeçalho do TCP/UDP e escolhem a *aplicação*. Resumindo: **Protocol → escolhe o protocolo (TCP ou UDP); porta → escolhe o programa**.

## 1.9 🎯 Método para ler um cabeçalho em hexadecimal (passo a passo)

Use **sempre** estes 6 passos:

1. **Separe em palavras de 4 dígitos** e numere de 1 a 10 (mais, se houver opções).
2. **Palavra 1** = `V H TT`: 1º dígito = versão; 2º = HLEN; 2 últimos = TOS.
3. **Palavra 2** = comprimento total (hexa → decimal). **Dados = total − HLEN × 4.**
4. **Palavra 3** = ID (hexa → decimal).
5. **Palavra 4** = flags + deslocamento (use a tabela do 1º dígito). Deslocamento ≠ 0 **ou** MF = 1 ⇒ **fragmentado**.
6. **Palavra 5** = `TT PP`: TTL (hexa → decimal) e Protocol. **Palavra 6** = checksum. **Palavras 7–8** = origem, **9–10** = destino (cada byte → decimal).

🔍 **Como ler a questão.** Perguntas típicas e onde olhar:

| Pergunta | Onde olhar | Conta |
|---|---|---|
| Está fragmentado? | Palavra 4 | Deslocamento ≠ 0 **ou** MF = 1 → sim |
| Tamanho dos dados? | Palavras 1 e 2 | total − HLEN × 4 |
| Quantos roteadores a mais? | Palavra 5 (1º byte) | valor do TTL |
| Número de identificação? | Palavra 3 | hexa → decimal |
| Qual protocolo no payload? | Palavra 5 (2º byte) | 06 = TCP, 11 = UDP |
| Primeiro/último byte dos dados? | Palavra 4 e dados | Módulo 3 |

## 1.10 🎯 Resolução completa do item 2(a)

**Cabeçalho:** `4500 0054 0003 5850 2006 0000 7C4E 0302 B40E 0F02`

### Passo 1: separar em palavras

| Palavra | Valor | Campo | Leitura |
|---|---|---|---|
| 1 | `4500` | Versão / HLEN / TOS | 4 / 5 / 00 → IPv4, cabeçalho de 5 × 4 = **20 bytes**, TOS 0 |
| 2 | `0054` | Comprimento total | 5 × 16 + 4 = **84** |
| 3 | `0003` | ID | **3** |
| 4 | `5850` | Flags + deslocamento | ver abaixo |
| 5 | `2006` | TTL / Protocol | TTL = 0x20 = 2 × 16 = **32**; Protocol = 0x06 = **TCP** |
| 6 | `0000` | Checksum | 0 (não usado neste exercício) |
| 7–8 | `7C4E 0302` | Origem | 0x7C = 124, 0x4E = 78, 3, 2 → **124.78.3.2** |
| 9–10 | `B40E 0F02` | Destino | 0xB4 = 180, 0x0E = 14, 15, 2 → **180.14.15.2** |

### Passo 2: a palavra 4 (`5850`)
1º dígito = 5 = `0101`: os 3 primeiros bits (flags) são `010`; o último bit (`1`) é o bit alto do deslocamento.
Em binário: `0101 1000 0101 0000` → flags `010` | deslocamento `1 1000 0101 0000`.
Deslocamento = 4.096 + 0x850 = 4.096 + 2.128 = **6.224**.

### Respostas (com justificativa)

**1. O pacote está fragmentado?** **Sim.** O campo **Deslocamento = 6.224**, diferente de zero. Um datagrama não fragmentado tem deslocamento 0; um valor diferente de zero indica que estes dados começam no meio do datagrama original, ou seja, este datagrama é um **fragmento**.

> 📌 *Sobre o tipo de fragmento (se a questão perguntar):* a leitura das flags `010` depende da ordem dos bits. Ordem padrão [0][DF][MF] → DF = 1, MF = 0 → **último fragmento** (porém DF = 1 junto com deslocamento ≠ 0 é estranho, pois um datagrama com DF = 1 não deveria ter sido fragmentado). Ordem [0][M][D] (como rotulada na figura da prova anterior) → MF = 1 → **fragmento intermediário**, o que é coerente (dados = 64, múltiplo de 8). A conclusão "está fragmentado" vale nas duas leituras. Se pedirem o tipo, **declare a convenção que usou**.

**2. Tamanho dos dados?** Comprimento total = 84. Cabeçalho = HLEN × 4 = 5 × 4 = 20. Dados = 84 − 20 = **64 bytes**.

**3. Por quantos roteadores a mais o pacote pode trafegar?** TTL = 0x20 = **32**. Cada roteador subtrai 1; o pacote pode passar por mais **32 roteadores** (no 32º o TTL chega a 0 e ele é descartado).

**4. Número de identificação?** Palavra 3 = 0x0003 = **3**.

**Extras que o professor pode pedir:** protocolo = 6 (**TCP**); origem 124.78.3.2; destino 180.14.15.2; primeiro byte dos dados = 6.224 × 8 = **49.792**; último = 49.792 + 64 − 1 = **49.855**.

## 1.11 Armadilhas do Módulo 1 ⚠️

1. **HLEN × 4:** esquecer de multiplicar por 4 (HLEN 5 não é "5 bytes").
2. **Comprimento total inclui o cabeçalho:** os dados são o total **menos** o cabeçalho.
3. **Hexa → decimal:** `0x0054` não é 54 (é 84). Os dígitos valem 16 e 256, não 10 e 100.
4. **Letras:** A = 10, B = 11, C = 12, D = 13, E = 14, F = 15.
5. **Palavra 4:** não leia `5850` como "deslocamento 5.850": os **3 primeiros bits** são flags.
6. **Palavra 5:** TTL é o **primeiro** byte e Protocol o **segundo** (`2006` → TTL 0x20, Protocol 0x06). Inverter dá TTL 6 e protocolo 32.
7. **Endereços IP:** cada **byte** vira um número decimal (`C0A8` → 192.168, **não** 49320).

## 1.12 Exercícios do Módulo 1 (progressivos)

**Ex. 1.1 (fácil — hexa).** Converta: (a) 0x2D para decimal; (b) 0xFF para decimal; (c) 0x0C80 para decimal; (d) 0x01F4 para decimal; (e) 200 para hexa; (f) 1.500 para hexa (4 dígitos).

**Ex. 1.2 (fácil — HLEN).** O HLEN de um datagrama é 9. Qual o tamanho do cabeçalho e quantos bytes de opções existem?

**Ex. 1.3 (fácil — HLEN).** Um datagrama tem 12 bytes de opções. Qual o HLEN, em decimal e em binário (4 bits)?

**Ex. 1.4 (conceitual).** (a) Qual o **maior** HLEN possível? Quantos bytes de opções isso permite? (b) Pode existir um cabeçalho IPv4 de 22 bytes? Justifique.

**Ex. 1.5 (fácil — comprimento).** Um datagrama carrega 600 bytes de dados e 8 bytes de opções. Qual o HLEN? Qual o comprimento total?

**Ex. 1.6 (fácil — comprimento).** Comprimento total 1.500 e HLEN 6. Quantos bytes de dados e quantos de opções?

**Ex. 1.7 (conceitual — TTL).** Um datagrama sai da origem com TTL = 64 e atravessa 9 roteadores até o destino. Qual o TTL ao chegar? E se um datagrama chegar a um roteador com TTL = 1, o que acontece?

**Ex. 1.8 (conceitual — Protocol).** (a) Quais protocolos correspondem aos valores 0x06, 0x11 e 0x01 do campo Protocol? (b) Por que o IP precisa desse campo? Use uma analogia.

**Ex. 1.9 (hexa — nível 1).** Interprete o cabeçalho:
`4500 00C8 1F40 4000 3A11 0000 C0A8 010A 0808 0808`
Informe: HLEN e tamanho do cabeçalho, comprimento total, tamanho dos dados, ID (decimal), flags (use a ordem padrão [0][DF][MF]), deslocamento, se é fragmento, TTL, protocolo transportado, IP de origem e de destino.

**Ex. 1.10 (hexa — nível 2, com opções).** Interprete:
`4600 0064 A3C1 0000 8006 0000 0A00 0005 AC10 0409 0101 0100`
Quantas palavras tem o cabeçalho? Qual o HLEN? Quantos bytes de opções? Quantos dados? ID? Fragmentado? TTL? Protocolo?

**Ex. 1.11 (hexa — nível 3, fragmento).** Interprete (ordem padrão [0][DF][MF]):
`4500 0214 0B2E 20B9 3F11 0000 C0A8 0007 C0A8 0108`
(a) Está fragmentado? Que tipo de datagrama é (completo, primeiro, intermediário, último)? (b) Tamanho dos dados. (c) Primeiro e último byte dos dados no datagrama original. (d) Quantos roteadores ainda pode atravessar? (e) ID e protocolo.

**Ex. 1.12 (construção — inverso do 1.9).** Monte, **em hexadecimal**, as duas primeiras palavras (Versão/HLEN/TOS e Comprimento total) de um datagrama IPv4 com HLEN = 6, TOS = 0 e 520 bytes de dados.

**Ex. 1.13 (discursiva, estilo prova).** O que é o campo Protocol e que problema ele resolve? Um host recebe dois datagramas, um com Protocol = 6 e outro com Protocol = 17. O que ele faz em cada caso?

---

## ✅ Gabarito comentado — Módulo 1

> Tente primeiro. Só depois confira.

**1.1** (a) 0x2D = 2 × 16 + 13 = **45**. (b) 0xFF = 15 × 16 + 15 = **255**. (c) 0x0C80 = 12 × 256 + 8 × 16 = 3.072 + 128 = **3.200**. (d) 0x01F4 = 256 + 15 × 16 + 4 = 256 + 240 + 4 = **500**. (e) 200 ÷ 16 = 12 resto 8 → C e 8 → **0xC8**. (f) 1.500 ÷ 256 = 5 resto 220; 220 ÷ 16 = 13 resto 12 → 5, D, C → **0x05DC**.

**1.2** Cabeçalho = 9 × 4 = **36 bytes**. Opções = 36 − 20 = **16 bytes**.

**1.3** Cabeçalho = 20 + 12 = 32 bytes. HLEN = 32 ÷ 4 = **8** = **1000** em binário.

**1.4** (a) O campo tem 4 bits: máximo `1111` = 15. Cabeçalho = 15 × 4 = 60 bytes; opções = 60 − 20 = **40 bytes**. (b) **Não.** 22 não é múltiplo de 4, e o HLEN só conta palavras inteiras de 4 bytes (o padding completaria até 24, HLEN = 6).

**1.5** Cabeçalho = 20 + 8 = 28 → **HLEN = 28 ÷ 4 = 7**. Comprimento total = 28 + 600 = **628**.

**1.6** Cabeçalho = 6 × 4 = 24. Dados = 1.500 − 24 = **1.476 bytes**. Opções = 24 − 20 = **4 bytes**.

**1.7** Cada roteador subtrai 1: 64 − 9 = **55**. Com TTL = 1 no roteador, ele subtrai 1 → 0 e **descarta** o datagrama (não o encaminha).

**1.8** (a) 0x06 = 6 → **TCP**; 0x11 = 1 × 16 + 1 = 17 → **UDP**; 0x01 = 1 → **ICMP**. (b) O IP entrega o payload ao protocolo certo da camada acima. Analogia: o número do departamento na etiqueta do prédio. Sem ele, a portaria (camada de rede) não saberia a quem entregar a encomenda.

**1.9**
- Palavra 1 `4500`: versão 4; **HLEN 5 → 20 bytes**; TOS 0.
- Palavra 2 `00C8`: 12 × 16 + 8 = **200** → dados = 200 − 20 = **180 bytes**.
- Palavra 3 `1F40`: 4.096 + 3.840 + 64 = **8.000** (ID).
- Palavra 4 `4000`: 1º dígito 4 = `0100` → flags `010` → **DF = 1, MF = 0**; deslocamento = 0 (bit alto 0, resto `000`).
- **Não é fragmento** (deslocamento 0 e MF 0): é um **datagrama completo**, e com DF = 1 o roteador não pode fragmentá-lo.
- Palavra 5 `3A11`: TTL = 0x3A = 3 × 16 + 10 = **58**; Protocol = 0x11 = **17 (UDP)**.
- Origem `C0A8 010A` = **192.168.1.10**; destino `0808 0808` = **8.8.8.8**.

**1.10**
- Palavras: `4600 0064 A3C1 0000 8006 0000 0A00 0005 AC10 0409 0101 0100` → **12 palavras** de 4 dígitos = 6 linhas de 32 bits = **24 bytes** (confere com HLEN).
- Palavra 1 `4600`: **HLEN = 6** → 24 bytes → opções = **4 bytes** (a última linha `0101 0100`).
- Comprimento total `0064` = 6 × 16 + 4 = **100** → dados = 100 − 24 = **76 bytes**.
- ID `A3C1` = 10 × 4.096 + 3 × 256 + 12 × 16 + 1 = 40.960 + 768 + 192 + 1 = **41.921**.
- Palavra 4 `0000`: flags 000, deslocamento 0 → **não fragmentado**.
- TTL = 0x80 = 8 × 16 = **128**; Protocol = 0x06 = **TCP**.
- Origem `0A00 0005` = 10.0.0.5; destino `AC10 0409` = 172.16.4.9.
- ⚠️ Lição: o cabeçalho tem **mais de 10 palavras** quando HLEN > 5; as palavras extras são opções, **não dados**.

**1.11**
- Palavra 4 `20B9`: 1º dígito 2 = `0010` → flags `001` → **DF = 0, MF = 1**; bit alto 0; deslocamento = 0x0B9 = 11 × 16 + 9 = **185**.
- (a) **Fragmentado**: MF = 1 e deslocamento ≠ 0 → **fragmento intermediário** (tem fragmentos antes, pois offset ≠ 0, e depois, pois MF = 1).
- (b) Total `0214` = 2 × 256 + 16 + 4 = 532; HLEN = 5 → 20; dados = **512 bytes**. (Confere: fragmento com MF = 1 tem dados múltiplo de 8: 512 = 64 × 8 ✅.)
- (c) Primeiro byte = 185 × 8 = **1.480**. Último = 1.480 + 512 − 1 = **1.991**.
- (d) TTL = 0x3F = 3 × 16 + 15 = **63** roteadores.
- (e) ID = `0B2E` = 11 × 256 + 2 × 16 + 14 = **2.862**; Protocol = 0x11 = **UDP**.

**1.12** Palavra 1: versão 4, HLEN 6, TOS 00 → **`4600`**. Cabeçalho = 24 bytes; total = 520 + 24 = 544. 544 = 2 × 256 + 32 = 2 × 256 + 2 × 16 → **`0220`**. Resultado: `4600 0220`.

**1.13** O campo Protocol (8 bits do cabeçalho IPv4) identifica **a qual protocolo da camada superior pertence o payload**. Resolve o problema de o IP do host destino não saber a quem entregar os dados (demultiplexação). Com Protocol = 6, o host entrega o payload ao **TCP**; com Protocol = 17, ao **UDP**.

---