# Lista 02 — Camada de Transporte: UDP e TCP (respostas para estudo)

---

# PARTE 1 — UDP

## 1.a) Campos do cabeçalho UDP (8 bytes fixos)

```
|  Porta de origem (16)  |  Porta de destino (16) |
|     Comprimento (16)   |    Checksum (16)       |
```

| Campo | Significado |
|---|---|
| **Porta de origem** | Identifica o processo/aplicação que enviou. |
| **Porta de destino** | Identifica o processo/aplicação que deve receber. |
| **Comprimento** | Tamanho **total** do datagrama (cabeçalho + dados), em bytes. |
| **Checksum** | Detecção de erros no cabeçalho + dados (+ pseudo-cabeçalho IP). |

## 1.b) Cabeçalho UDP: `06 32 00 0D 00 1C E2 17`

| Pergunta | Resposta | Justificativa |
|---|---|---|
| Porta de origem | **1586** | 1º campo (2 bytes): `0632`₁₆ = 6·256 + 50 = 1586 |
| Porta de destino | **13** | 2º campo: `000D`₁₆ = 13 (serviço *daytime*) |
| Comprimento dos **dados** | **20 bytes** | 3º campo: `001C`₁₆ = 28 (total). Dados = 28 − 8 (cabeçalho) = **20** |

(O 4º campo `E217` é o checksum.)

## 1.c) Tamanho máximo e mínimo de um segmento UDP
- **Mínimo: 8 bytes** → só o cabeçalho, sem dados.
- **Máximo: 65.535 bytes** → o campo Comprimento tem 16 bits (2¹⁶ − 1). Na prática, descontando o cabeçalho IP (20 bytes), sobram **65.507 bytes** de dados.

## 1.d) Por que o Checksum não tem sido usado?
A justificativa esperada: **as camadas inferiores já fazem detecção de erros** (CRC no enlace, checksum do cabeçalho IP) e calcular o checksum **custa processamento**; para aplicações que toleram perdas (voz, vídeo) isso é dispensável.
> ⚠️ Na prática: no **IPv4 o checksum UDP é opcional** (valor 0 = não usado), mas no **IPv6 é obrigatório**. Se o professor falou "não usado", responda com a justificativa acima.

---

# PARTE 2 — TCP: cabeçalho

## 2.a) Campos do cabeçalho TCP (20 a 60 bytes)

```
| Porta origem (16)        | Porta destino (16)       |
| Número de sequência (32)                            |
| Número de confirmação / ACK (32)                    |
| HLEN(4)|Reservado(6)|Flags(6)| Janela de recepção (16)|
| Checksum (16)            | Ponteiro urgente (16)    |
| Opções (0–40 bytes)                                 |
```

| Campo | Significado |
|---|---|
| **Porta origem / destino** | Processos de origem e destino. |
| **Nº de sequência** | Número do **1º byte de dados** do segmento no fluxo (SYN/FIN consomem 1 número). |
| **Nº de confirmação** | Próximo byte que o receptor **espera** receber (ACK cumulativo). Válido se ACK=1. |
| **HLEN** | Tamanho do cabeçalho em **palavras de 4 bytes** (valor × 4 = bytes). |
| **Reservado** | 6 bits para uso futuro. |
| **Flags** (URG, ACK, PSH, RST, SYN, FIN) | Controle (ver 2.e). |
| **Janela de recepção (rwnd)** | Bytes que o receptor ainda aceita (**controle de fluxo**). |
| **Checksum** | Detecção de erros (cabeçalho + dados + pseudo-cabeçalho). |
| **Ponteiro urgente** | Indica o fim dos dados urgentes (válido se URG=1). |
| **Opções** | Extras (MSS, window scale, timestamps...). |

## 2.b) Cabeçalho TCP: `05320017 00000001 00000000 500207FF 00000000`

| Pergunta | Resposta | Justificativa |
|---|---|---|
| Porta origem | **1330** | `0532`₁₆ = 5·256 + 50 = 1330 |
| Porta destino | **23** (Telnet) | `0017`₁₆ = 23 |
| Nº de sequência | **1** | `00000001` |
| Nº de confirmação | **0** | `00000000` (faz sentido: ACK não está ativo) |
| Tamanho do cabeçalho | **20 bytes** | 1º dígito de `5002` = 5 → HLEN = 5 × 4 = 20 |
| Flags ativos | **SYN** | `5002`: 0101 · 000000 · **000010** → só o bit SYN (abertura de conexão) |
| Janela de recepção | **2047 bytes** | `07FF`₁₆ = 2047 |

Restante: `0000` checksum (não calculado no exemplo) e `0000` ponteiro urgente.

**Ordem dos flags:** `URG | ACK | PSH | RST | SYN | FIN`

## 2.c) Portas no sentido contrário
A→B: origem = *x*, destino = *y*. **B→A: origem = *y*, destino = *x*** (as portas se invertem).

---

# PARTE 3 — HLEN e flags

## 3.a) HLEN de seis segmentos

| HLEN | Decimal | Cabeçalho (×4) | Coerente? | Bytes de opções |
|---|---|---|---|---|
| 0101 | 5 | **20 B** | ✅ | 0 |
| 1000 | 8 | **32 B** | ✅ | 12 |
| 0011 | 3 | **12 B** | ❌ (< 20) | — |
| 1100 | 12 | **48 B** | ✅ | 28 |
| 0100 | 4 | **16 B** | ❌ (< 20) | — |
| 0111 | 7 | **28 B** | ✅ | 8 |

- **Incoerentes:** `0011` e `0100` (menos que o mínimo de 20 bytes/HLEN 5).
- **Opções** = cabeçalho − 20.

## 3.b) Tamanho máximo e mínimo do cabeçalho TCP
- **Mínimo: 20 bytes** (HLEN = 5, sem opções: campos fixos).
- **Máximo: 60 bytes** (HLEN tem 4 bits → máx 15 × 4 = 60; sobram 40 bytes de opções).

## 3.c) Flags: 000000, 000001, 010001

| Flags | Ativos | O que dizer |
|---|---|---|
| `000000` | nenhum | **Anormal/inválido**: segmento "nulo" (usado em varreduras de portas). Todo segmento após o SYN inicial deveria ter ACK=1. |
| `000001` | **FIN** | Pede encerramento da conexão, mas **sem ACK** → incomum (normalmente FIN vem com ACK). |
| `010001` | **ACK + FIN** | Segmento **normal de encerramento**: fecha o lado do emissor e confirma dados recebidos. |

---

# PARTE 4 — Urgente e Push

## 4.a) Dados urgentes × não urgentes
- **Não urgentes:** vão para o buffer do receptor, **em ordem**, e são entregues à aplicação **quando ela lê** (ou o buffer enche).
- **Urgentes:** são entregues à aplicação **imediatamente**, **fora da ordem normal** (a aplicação é avisada), sem esperar os dados anteriores no buffer.
- **Como o TCP descobre?** Flag **URG = 1**.
- **Onde começam/terminam?** Começam no **1º byte da área de dados** do segmento. O **ponteiro urgente** é um deslocamento somado ao nº de sequência: **fim = nº de sequência + ponteiro urgente**.

## 4.b) Entrega tradicional × "empurrar" (PUSH)
- **Tradicional:** o TCP emissor **espera acumular** dados para montar segmentos maiores, e o TCP receptor **espera** o buffer encher ou a aplicação pedir os dados.
- **Com PUSH:** o emissor **envia imediatamente**, sem esperar acumular, e o receptor **entrega logo** à aplicação, sem esperar encher o buffer (ex.: Telnet/SSH, digitação interativa).
- **Como o TCP descobre?** Flag **PSH = 1**.

## 4.c) Por que o Checksum do TCP não é usado nas versões mais novas?
Mesma linha do UDP: as camadas inferiores já detectam erros (CRC, checksum IP) e o cálculo consome CPU.
> ⚠️ Na prática, o checksum do TCP é **obrigatório** (IPv4 e IPv6). Use a justificativa acima se a prova seguir o enunciado.

---

# PARTE 5 — Estabelecimento de conexão (3-way handshake)

ISN de A = 10.000; ISN de B = 20.000.

```
     A (ISN=10000)                       B (ISN=20000)
        │── SYN, seq=10000 ─────────────────▶│
        │                                     │
        │◀─ SYN+ACK, seq=20000, ack=10001 ────│
        │                                     │
        │── ACK, seq=10001, ack=20001 ───────▶│
        │        conexão estabelecida         │
```

**Regra:** o SYN consome 1 número de sequência, por isso ack = ISN + 1.

---

# PARTE 6 — Retransmissão e confiabilidade

## Figura 1 (A envia seq=100, 10 bytes; B responde ack=110)

| Situação | O que acontece |
|---|---|
| **Segmento de dados perdido** | B não recebe → não envia ACK → o temporizador de A chega ao **RTO** → A **retransmite** o segmento (seq=100). |
| **ACK perdido** | A não recebe o ACK → estoura o **RTO** → A **retransmite** o segmento. B recebe **duplicado**, **descarta** e **reenvia o ACK 110**. |
| **ACK chega depois do RTO** | A já **retransmitiu** ao estourar o RTO. B recebe o **duplicado**, descarta e reenvia o ACK 110. A recebe dois ACKs iguais e ignora o duplicado (a retransmissão foi desnecessária). |

## Um único temporizador para 10 segmentos
- **Por que só um?** Custo/complexidade: 10 temporizadores seriam pesados; o TCP usa **ACK cumulativo**, então basta controlar o **segmento não confirmado mais antigo**.
- **Gerenciamento:** o temporizador está associado ao **mais antigo não confirmado**. Quando chega um ACK que confirma dados novos, o temporizador é **reiniciado** para o próximo segmento não confirmado (ou **parado** se não sobrou nenhum).
- **No RTO:** retransmite **apenas o segmento não confirmado mais antigo** (menor nº de sequência) e reinicia o temporizador (com RTO dobrado). Os demais só são reenviados se continuarem sem confirmação.

## Figura 2 (A envia segs 100, 110, 120 de 10 bytes; ACKs = 110, 120, 130, cumulativos)

| Caso | Resultado |
|---|---|
| **Só o 1º ACK perdido** | O ACK 120 (cumulativo) confirma tudo até 119 → **nada é retransmitido** (se chegar antes do RTO). |
| **Só o 2º ACK perdido** | O ACK 130 confirma tudo → **nada é retransmitido**. |
| **Só o 3º ACK perdido** | A fica sem confirmar o seg. 120 → **RTO** → A retransmite o **seg. 120**; B descarta o duplicado e reenvia **ACK 130**. |
| **Só o 1º seg. de dados perdido** | B recebe 110 e 120 fora de ordem: **guarda no buffer** e envia **ACKs duplicados (ack=100)** (2, menos de 3). **RTO** → A retransmite o seg. 100 → B envia **ACK 130**. |
| **Só o 2º seg. de dados perdido** | B: ACK 110 (do seg. 100) e depois ACK 110 duplicado (seg. 120 fora de ordem, guardado). **RTO** → A retransmite o seg. 110 → B envia **ACK 130**. |
| **Só o 3º seg. de dados perdido** | B envia ACK 110 e ACK 120; falta o 130. **RTO** → A retransmite o seg. 120 → B envia **ACK 130**. |
| **1º seg. chega por último** | B recebe 110 e 120 primeiro (fora de ordem → 2 ACKs duplicados com ack=100), e quando chega o 100 envia **ACK 130** (cumulativo). Sem retransmissão se chegar antes do RTO. |

## Retransmissão rápida (Fast Retransmit)
- Se o receptor recebe um segmento **fora de ordem**, ele reenvia o ACK do último byte em ordem (**ACK duplicado**).
- Quando o emissor recebe **3 ACKs duplicados** (4 ACKs iguais no total), ele **retransmite o segmento perdido imediatamente**, **sem esperar o RTO**.

## Por que esperar 3 ACKs duplicados (e não 1)?
Porque **1 ou 2 ACKs duplicados podem ser causados só por reordenação** de pacotes na rede (nenhum foi perdido). Esperar 3 reduz **retransmissões desnecessárias**.

## Encerramento de conexão (A fecha, depois B fecha)
Usando os números da Figura 2 (A enviou até o byte 129; B está com seq=29):

```
     A                                      B
     │── FIN, seq=130, ack=30 ─────────────▶│
     │◀─ ACK, seq=29, ack=131 ──────────────│   (B pode ainda enviar dados)
     │                                      │
     │◀─ FIN, seq=29, ack=131 ──────────────│   (B decide fechar)
     │── ACK, seq=131, ack=30 ─────────────▶│
     │  (A espera em TIME_WAIT ≈ 2×MSL)     │
     │            conexão encerrada         │
```
O FIN consome 1 número de sequência. Em geral o 2º e o 3º segmentos podem ser combinados em **FIN+ACK**.

---

# PARTE 7 — Controle de fluxo e de congestionamento

## a) Variável rwnd
- **Inicializada** no **estabelecimento da conexão** (handshake), com o tamanho do buffer de recepção.
- **Atualizada** a cada segmento enviado: `rwnd = RcvBuffer − (LastByteRcvd − LastByteRead)`. O valor vai no campo **Janela** dos segmentos de ACK para o emissor.

## b) Janela antes e depois (rwnd = 10.000; ack anterior = 22.001; novo ack = 24.001; nova janela = 12.000)

```
ANTES   :  janela = [22.001 ........................ 32.000]   (10.000 bytes)
                     ▲ início                        ▲ fim

DEPOIS  :                  janela = [24.001 ........................ 36.000]   (12.000 bytes)
                                     ▲ início (avançou 2.000)          ▲ fim (avançou 4.000)
```
- 2.000 bytes (22.001–24.000) foram **confirmados** (saem da janela pela esquerda).
- A borda direita foi de 32.000 para 36.000 (janela **aumentou** 2.000 bytes).

## c) Figura 3 (evolução do cwnd)

| Pergunta | Resposta | Justificativa |
|---|---|---|
| **Partida lenta** | Rodadas **1–6** e **23–26** | cwnd cresce **exponencialmente** (1, 2, 4, 8, 16, 32); reinicia em 1 após timeout na rodada 22. |
| **Prevenção de congestionamento** | Rodadas **6–16** e **17–22** | cwnd cresce **linearmente** (+1 por rodada). |
| **Após a 6ª rodada** | cwnd atingiu o **limiar (ssthresh = 32)** → passa da partida lenta para a prevenção de congestionamento | Crescimento muda de exponencial para linear. |
| **Após a 16ª rodada** | **3 ACKs duplicados** (perda detectada) | cwnd cai **pela metade** (42 → ~21), ssthresh = 21, e segue em crescimento linear (recuperação rápida, TCP Reno). |
| **Após a 22ª rodada** | **Timeout (RTO)** | cwnd volta a **1**, ssthresh = metade (≈13) e recomeça a **partida lenta**. |

## d) Por que janela de congestionamento além de dobrar o RTO?
- Dobrar o RTO só **reage depois da perda** e apenas reduz a frequência de **retransmissões**; não limita quanto de dado **novo** é enviado.
- O controle por janela (**cwnd**) **limita continuamente** a quantidade de dados em trânsito e ajusta a taxa conforme a rede (**sondagem de banda**), evitando o congestionamento **antes** de ele se agravar.

## e) Quantos bytes ainda podem ser enviados?
**Fórmula:** `janela efetiva = min(cwnd, rwnd)`; **pode enviar = janela efetiva − bytes em trânsito (não confirmados)**

| cwnd | rwnd | Em trânsito | min | **Pode enviar** |
|---|---|---|---|---|
| 2.000 | 6.000 | 2.000 | 2.000 | 2.000 − 2.000 = **0 bytes** |
| 8.000 | 5.000 | 2.000 | 5.000 | 5.000 − 2.000 = **3.000 bytes** |

---

# PARTE 8 — UDP × TCP

## a) Campos do TCP que **não existem** no UDP e por quê

| Campo TCP | Por que o UDP não tem |
|---|---|
| **Nº de sequência / Nº de confirmação** | UDP **não é confiável** (sem ordenação, ACK ou retransmissão). |
| **HLEN / Opções** | UDP tem cabeçalho **fixo de 8 bytes**. |
| **Flags** (SYN, FIN, RST, ACK, PSH, URG) | UDP **não tem conexão** (sem abrir/fechar), nem urgentes/push. |
| **Janela de recepção** | UDP **não tem controle de fluxo**. |
| **Ponteiro urgente** | UDP não tem conceito de dado urgente. |
| **Reservado** | Consequência de não ter os demais campos de controle. |

## b) Por que o UDP dá mais controle sobre **quais dados** vão no segmento?
O UDP **preserva os limites da mensagem**: cada dado entregue pela aplicação vira **exatamente um datagrama**. Já o TCP é um **fluxo de bytes**: pode dividir, juntar ou reagrupar os dados em segmentos como quiser.

## c) Por que o UDP dá mais controle sobre **quando** os dados são enviados?
O UDP **envia imediatamente**, sem controle de congestionamento, sem controle de fluxo e sem conexão. O TCP pode **atrasar o envio** (janela cwnd/rwnd, acumular dados, aguardar ACKs).

## d) É possível ter transferência confiável sobre UDP?
**Sim.** A confiabilidade é implementada na **camada de aplicação**: números de sequência, ACKs, temporizadores e retransmissões escritos na própria aplicação (ex.: TFTP, QUIC).

---

# ⭐ EXTRA — TCP: **Piggyback** e **Transferência de dados** (questão da prova)

## Piggybacking (carona)

**Definição:** enviar o **ACK dentro de um segmento de dados** que vai no sentido contrário, em vez de enviar um segmento só de ACK.

**Como funciona:**
- Em comunicação **bidirecional**, quando B recebe dados de A, ele **espera um pouco** (ACK atrasado, tipicamente até ~500 ms) e, se tiver dados para A, envia **dados + ACK no mesmo segmento** (flag ACK = 1, com o nº de confirmação preenchido).
- Se não houver dados para enviar a tempo, envia só o ACK.

**Vantagem:** **menos segmentos, menos overhead** e menos tráfego na rede.

**Exemplo:**
```
 A                                              B
 │── seq=100, ack=30, ACK, 10 B dados ─────────▶│
 │                                              │
 │◀─ seq=30, ack=110, ACK, 20 B dados ──────────│  ← PIGGYBACK: dados de B + ACK do que A enviou
 │                                              │
 │── seq=110, ack=50, ACK  ────────────────────▶│  ← ACK dos 20 B de B
```
(Nesse exemplo, **B → A** carrega dados **e** confirma os bytes 100–109 de A com ack=110.)

## Transferência de dados no TCP

**Ideias-chave:**
- Dados são um **fluxo de bytes**, divididos em segmentos.
- **Nº de sequência** = número do **1º byte** do segmento.
- **Próximo seq** = seq atual + nº de bytes de dados (SYN/FIN contam 1).
- **Nº de confirmação** = **próximo byte esperado** (ACK **cumulativo**).
- **Flags úteis:** ACK (confirma), PSH (entrega imediata), URG (urgente).
- **Confiabilidade:** temporizador de RTO, retransmissão, retransmissão rápida (3 ACKs duplicados) e buffer de recepção para segmentos fora de ordem.

**Exemplo de transferência (do emissor A para o receptor B):**
```
 A                                          B
 │── seq=100, 10 B ─────────────────────────▶│
 │── seq=110, 10 B ─────────────────────────▶│
 │── seq=120, 10 B ─────────────────────────▶│
 │◀─ ACK, ack=110 ────────────────────────────│  (confirma bytes até 109)
 │◀─ ACK, ack=120 ────────────────────────────│
 │◀─ ACK, ack=130 ────────────────────────────│  (confirma bytes até 129)
```

**Regras de ACK do receptor (resumo):**
1. Tem dados para enviar? → **piggyback** (dados + ACK).
2. Chegou segmento **em ordem**? → pode **esperar até ~500 ms** por dados para carona; se chegarem **2 segmentos em ordem** seguidos, envia **ACK imediato**.
3. Chegou segmento **fora de ordem** (lacuna)? → envia **ACK duplicado imediatamente** (com o próximo byte esperado).
4. Chegou o segmento **que preenche a lacuna**? → **ACK imediato** (cumulativo).
5. Chegou segmento **duplicado**? → descarta e envia **ACK imediato**.

---

### 🔑 Cola rápida (revise na véspera)
- **UDP:** cabeçalho **8 B** (origem, destino, comprimento, checksum). Dados = comprimento − 8.
- **TCP:** cabeçalho **20–60 B**. HLEN × 4. Flags: **URG ACK PSH RST SYN FIN**.
- Hex → decimal: `0532` = 1330 · `0017` = 23 · `07FF` = 2047.
- **Handshake:** SYN → SYN+ACK → ACK (ack = ISN+1). **Encerramento:** FIN → ACK → FIN → ACK.
- **RTO estourou** → retransmite o **mais antigo não confirmado**. **3 ACKs duplicados** → **retransmissão rápida**.
- **Janela efetiva = min(cwnd, rwnd)**; pode enviar = janela − em trânsito.
- **cwnd:** partida lenta (exponencial) → limiar → prevenção (linear). **3 dup ACKs:** cwnd/2. **Timeout:** cwnd = 1.
- **Piggyback** = ACK + dados no mesmo segmento (economiza segmentos).