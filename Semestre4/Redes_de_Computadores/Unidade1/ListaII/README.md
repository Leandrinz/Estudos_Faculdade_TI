# Lista 02 — Camada de Transporte (UDP e TCP)
### Versão completa, na ordem da lista, com respostas simples

> As questões estão numeradas **na ordem em que aparecem na lista**. Cada uma tem **Resposta curta** (o que escrever na prova) e, quando ajuda, um **Macete** (como lembrar).

## 📅 Plano de 3 dias
| Dia | Estudar | Foco |
|---|---|---|
| **1** | Q1 a Q7 | Cabeçalhos UDP/TCP e as contas de hexadecimal |
| **2** | Q8 a Q14 + Extra (piggyback) | Handshake, retransmissão, ACKs, encerramento |
| **3** | Q15 a Q19 + cola final | Janelas (rwnd/cwnd), gráfico do cwnd, UDP × TCP |

## ✅ Checklist de questões
| Q | Tema | Q | Tema |
|---|---|---|---|
| 1 | UDP (campos, hex, tamanho, checksum) | 11 | Figura 2 (7 cenários) |
| 2 | TCP (campos, hex, portas) | 12 | Retransmissão rápida |
| 3 | HLEN e tamanho do cabeçalho | 13 | Por que 3 ACKs duplicados |
| 4 | Flags 000000 / 000001 / 010001 | 14 | Encerramento de conexão |
| 5 | Dados urgentes | 15 | rwnd, janela, gráfico cwnd |
| 6 | Push | 16 | RTO dobrado × janela |
| 7 | Checksum do TCP | 17 | cwnd 2.000 / rwnd 6.000 |
| 8 | Handshake (ISN 10.000 e 20.000) | 18 | cwnd 8.000 / rwnd 5.000 |
| 9 | Figura 1 (3 cenários) | 19 | UDP × TCP (a, b, c, d) |
| 10 | Temporizador único | ⭐ | Piggyback / transferência de dados |

---

# Q1 — UDP

## Q1.a) Campos do cabeçalho UDP (**8 bytes fixos**)
```
| Porta origem (16 bits) | Porta destino (16 bits) |
| Comprimento (16 bits)  | Checksum (16 bits)      |
```
| Campo | Para que serve |
|---|---|
| **Porta origem** | Qual processo enviou |
| **Porta destino** | Qual processo deve receber |
| **Comprimento** | Tamanho total (cabeçalho + dados), em bytes |
| **Checksum** | Detecta erros (cabeçalho + dados) |

**Macete:** 4 campos × 2 bytes = 8 bytes.

## Q1.b) Cabeçalho `06 32 00 0D 00 1C E2 17`
Separe em 4 pedaços de 2 bytes: `0632` | `000D` | `001C` | `E217`

| Pergunta | Resposta | Conta |
|---|---|---|
| Porta de origem | **1586** | `0632` = 6×256 + 3×16 + 2 = 1536 + 48 + 2 |
| Porta de destino | **13** | `000D` = 13 (D = 13) |
| Comprimento dos **dados** | **20 bytes** | `001C` = 28 (C = 12; 16 + 12) → dados = 28 − 8 = **20** |

**Cuidado:** o campo Comprimento inclui o cabeçalho, então **subtraia 8**.

## Q1.c) Tamanho máximo e mínimo de um segmento UDP
- **Mínimo = 8 bytes** (só o cabeçalho, sem dados).
- **Máximo = 65.535 bytes**, porque o campo Comprimento tem 16 bits (2¹⁶ − 1). Só de dados, sobram 65.527 (65.535 − 8).

## Q1.d) Por que o Checksum não tem sido usado?
As camadas de baixo (enlace e IP) **já detectam erros**, e calcular o checksum **gasta processamento**. Para aplicações que toleram perda (voz, vídeo), não compensa.
> ⚠️ Na prática, o checksum UDP é opcional no IPv4 e obrigatório no IPv6. Na prova, responda com o argumento acima.

---

# Q2 — TCP

## Q2.a) Campos do cabeçalho TCP (**20 a 60 bytes**)
```
| Porta origem (16)          | Porta destino (16)        |
| Número de sequência (32)                                |
| Número de confirmação (32)                              |
| HLEN(4) | Reservado(6) | Flags(6) | Janela (16)         |
| Checksum (16)              | Ponteiro urgente (16)     |
| Opções (0 a 40 bytes)                                   |
```
| Campo | Para que serve |
|---|---|
| **Porta origem / destino** | Processos nas duas pontas |
| **Nº de sequência** | Número do **1º byte de dados** do segmento |
| **Nº de confirmação** | **Próximo byte que espero receber** (vale se ACK = 1) |
| **HLEN** | Tamanho do cabeçalho **em palavras de 4 bytes** |
| **Reservado** | 6 bits sem uso (futuro) |
| **Flags** | URG, ACK, PSH, RST, SYN, FIN (controle) |
| **Janela (rwnd)** | Quantos bytes o receptor ainda aceita (controle de fluxo) |
| **Checksum** | Detecção de erros |
| **Ponteiro urgente** | Onde terminam os dados urgentes (vale se URG = 1) |
| **Opções** | Extras (MSS, escala de janela...) |

**Macete das flags (nesta ordem):** **U**rgente, **A**CK, **P**ush, **R**eset, **S**YN, **F**IN → "**U A P R S F**".

## Q2.b) Cabeçalho `05320017 00000001 00000000 500207FF 00000000`
Cada bloco = 4 bytes:

| Bloco | Significado |
|---|---|
| `0532` `0017` | porta origem, porta destino |
| `00000001` | nº de sequência |
| `00000000` | nº de confirmação |
| `5` `002` `07FF` | HLEN, flags, janela |
| `0000` `0000` | checksum, ponteiro urgente |

| Pergunta | Resposta | Justificativa |
|---|---|---|
| Porta origem | **1330** | `0532` = 5×256 + 3×16 + 2 = 1280 + 48 + 2 |
| Porta destino | **23** | `0017` = 1×16 + 7 (Telnet) |
| Nº de sequência | **1** | `00000001` |
| Nº de confirmação | **0** | `00000000` (ACK não está ativo) |
| Comprimento do cabeçalho | **20 bytes** | HLEN = 5 → 5 × 4 = 20 |
| Flags ativos | **SYN** | `002` em binário: 0000 0000 **0010**. Os 6 últimos bits são `000010`, ou seja, só o SYN |
| Janela de recepção | **2047 bytes** | `07FF` = 7×256 + 255 = 1792 + 255 |

## Q2.c) Portas no sentido contrário
A → B: origem = *x*, destino = *y*.
**B → A: origem = *y*, destino = *x*.** As portas se **invertem**.

---

# Q3 — HLEN: `0101; 1000; 0011; 1100; 0100; 0111`

**Regra:** tamanho do cabeçalho = **HLEN × 4**. O mínimo válido é 20 (HLEN = 5).

## Q3.a, b, c) Tamanho, coerência e opções
| HLEN | Decimal | Cabeçalho | Coerente? | Bytes de opções (cabeçalho − 20) |
|---|---|---|---|---|
| 0101 | 5 | **20 B** | ✅ | 0 |
| 1000 | 8 | **32 B** | ✅ | 12 |
| 0011 | 3 | **12 B** | ❌ (< 20) | — |
| 1100 | 12 | **48 B** | ✅ | 28 |
| 0100 | 4 | **16 B** | ❌ (< 20) | — |
| 0111 | 7 | **28 B** | ✅ | 8 |

- **Incoerentes:** `0011` e `0100` (o cabeçalho ficaria menor que os 20 bytes dos campos fixos).

## Q3.d) Tamanho máximo e mínimo do cabeçalho TCP
- **Mínimo = 20 bytes** (HLEN = 5, sem opções).
- **Máximo = 60 bytes** (HLEN tem 4 bits → máximo 15 × 4 = 60, sobrando 40 bytes de opções).

---

# Q4 — Flags: `000000; 000001; 010001`
Ordem: **URG ACK PSH RST SYN FIN**

| Flags | Ativos | O que dizer |
|---|---|---|
| `000000` | nenhum | **Segmento anormal/inválido** (nulo). Depois do 1º SYN, todo segmento deveria ter ACK. |
| `000001` | **FIN** | Pede o **encerramento** da conexão, mas **sem ACK** → incomum. |
| `010001` | **ACK + FIN** | Segmento **normal de encerramento**: fecha o lado dele e confirma dados recebidos. |

---

# Q5 — Dados urgentes × não urgentes

**Diferença na entrega:**
- **Não urgentes:** ficam no buffer, **em ordem**, até a aplicação ler.
- **Urgentes:** são entregues **na hora**, **fora de ordem**, sem esperar os dados que estão na frente no buffer.

**Como o TCP descobre que tem dado urgente?** Flag **URG = 1**.

**Onde os dados urgentes começam e terminam?**
- **Começam** no 1º byte da área de dados do segmento.
- **Terminam** em: **nº de sequência + ponteiro urgente**.

---

# Q6 — Push (empurrar)

- **Tradicional:** o emissor **junta dados** antes de enviar, e o receptor **espera** o buffer encher (ou a aplicação pedir).
- **Com Push:** o emissor **envia na hora** e o receptor **entrega à aplicação na hora**, sem esperar encher.
- **Como o TCP descobre?** Flag **PSH = 1**.

**Exemplo:** digitar num terminal remoto (SSH/Telnet): cada tecla precisa ir e voltar rápido.

---

# Q7 — Por que o Checksum do TCP não é usado nas versões novas?
Mesma resposta do UDP: enlace e IP **já detectam erros** e o cálculo **custa processamento**.
> ⚠️ Na prática o checksum TCP é obrigatório. Na prova, use o argumento acima.

---

# Q8 — Handshake (ISN de A = 10.000, ISN de B = 20.000)

```
     A                                     B
     │── SYN, seq=10000 ────────────────▶│
     │◀─ SYN+ACK, seq=20000, ack=10001 ───│
     │── ACK, seq=10001, ack=20001 ──────▶│
     │        conexão estabelecida        │
```
**Macete:** o SYN "gasta" 1 número de sequência, então **ack = ISN do outro + 1**. São 3 segmentos: **SYN → SYN+ACK → ACK**.

---

# Q9 — Figura 1 (A envia seq=100 com 10 bytes; B responde ack=110)

| Situação | O que acontece |
|---|---|
| **Dado (verde) perdido** | B não recebe e não responde. O temporizador de A chega ao **RTO** → A **retransmite** o segmento (seq=100). |
| **ACK (vermelho) perdido** | A não recebe o ACK → **RTO** → A **retransmite**. B recebe **duplicado**, **descarta** e **reenvia o ACK 110**. |
| **ACK chega depois do RTO** | A já retransmitiu ao estourar o RTO. B recebe o duplicado, descarta e reenvia o ACK 110. A vê ACKs repetidos e ignora o extra. **A retransmissão foi desnecessária.** |

**Macete:** perdeu algo (dado ou ACK) → **RTO** → **retransmite**.

---

# Q10 — Por que UM temporizador para 10 segmentos?

- **Por que só um?** Controlar 10 temporizadores seria caro. Como o TCP usa **ACK cumulativo**, basta vigiar o **segmento mais antigo ainda não confirmado**.
- **Como o temporizador é gerenciado:**
  - Chegou ACK que confirma dado novo → **reinicia** o temporizador (para o próximo não confirmado).
  - Nada mais pendente → **para** o temporizador.
- **Quando atinge o RTO:** retransmite **apenas o segmento não confirmado mais antigo** (o de menor nº de sequência) e reinicia o temporizador.

---

# Q11 — Figura 2 (A envia 3 segmentos: seq 100, 110, 120, de 10 bytes cada)
ACKs normais: **110, 120, 130** (cumulativos).

## ACKs perdidos
| Caso | O que acontece |
|---|---|
| **Só o 1º ACK perdido** | O 2º ACK (120) confirma tudo até 119. **Nada é retransmitido** (se chegar antes do RTO). |
| **Só o 2º ACK perdido** | O 3º ACK (130) confirma tudo. **Nada é retransmitido.** |
| **Só o 3º ACK perdido** | A fica sem confirmação do segmento 120 → **RTO** → retransmite o **120**. B descarta o duplicado e reenvia **ACK 130**. |

**Macete:** ACK é **cumulativo**. Se um ACK **posterior** chega, ele cobre os perdidos. Só o **último** ACK perdido causa retransmissão.

## Dados perdidos
| Caso | O que acontece |
|---|---|
| **Só o 1º segmento perdido** | B recebe 110 e 120 **fora de ordem**: guarda no buffer e manda **ACK 100 duplicado** (2 vezes, menos de 3). **RTO** → A retransmite o **100** → B manda **ACK 130**. |
| **Só o 2º segmento perdido** | B: ACK 110 (do 100), depois **ACK 110 duplicado** (120 chegou fora de ordem). **RTO** → A retransmite o **110** → B manda **ACK 130**. |
| **Só o 3º segmento perdido** | B manda ACK 110 e ACK 120. Falta o 130. **RTO** → A retransmite o **120** → B manda **ACK 130**. |
| **1º segmento chega por último** | B recebe 110 e 120 antes (fora de ordem → ACKs duplicados de 100). Quando o 100 chega, B manda **ACK 130**. Sem retransmissão se chegar antes do RTO. |

**Macete:** perdeu um segmento → o receptor guarda os seguintes, manda **ACK duplicado** do que falta → após o RTO o emissor **reenvia só o que faltou** → o receptor manda um **ACK cumulativo** grande.

---

# Q12 — Retransmissão rápida (Fast Retransmit)
1. O receptor recebe um segmento **fora de ordem** (tem uma lacuna).
2. Ele reenvia o ACK do último byte em ordem → **ACK duplicado**.
3. O emissor recebe **3 ACKs duplicados** e **retransmite o segmento perdido na hora**, **sem esperar o RTO**.

---

# Q13 — Por que esperar **3** ACKs duplicados e não só 1?
Porque 1 ou 2 ACKs duplicados podem ser só **reordenação de pacotes na rede** (o segmento não se perdeu, chegou fora de ordem). Esperar 3 evita **retransmissões desnecessárias**.

---

# Q14 — Encerramento (A fecha, depois B fecha)
Usando os números da Figura 2 (A já enviou até o byte 129 → próximo seq de A = 130; B está com seq = 29):

```
     A                                     B
     │── FIN, seq=130, ack=30 ───────────▶│
     │◀─ ACK, seq=29, ack=131 ────────────│   (lado A → B fechado)
     │                                    │
     │◀─ FIN, seq=29, ack=131 ────────────│   (agora B quer fechar)
     │── ACK, seq=131, ack=30 ───────────▶│
     │   (A espera um tempo: TIME_WAIT)   │
     │          conexão encerrada         │
```
**Macete:** cada lado faz **FIN → ACK**. O FIN gasta 1 número de sequência (ack = seq + 1). Em geral o 2º e o 3º segmentos podem ser juntados em **FIN+ACK**.

---

# Q15 — Controle de fluxo e congestionamento

## Q15.a) Quando a rwnd é inicializada e como é atualizada?
- **Inicializada:** no **estabelecimento da conexão** (handshake), com o tamanho do buffer de recepção.
- **Atualizada:** a cada segmento, pelo receptor: **rwnd = tamanho do buffer − dados que já chegaram e ainda não foram lidos pela aplicação**. O valor vai no campo **Janela** dos ACKs.

## Q15.b) Janela antes e depois
(janela 10.000; ack anterior 22.001; novo ack 24.001; nova janela 12.000)
```
ANTES : [22.001 ─────────────────────── 32.000]      (10.000 bytes)

DEPOIS:            [24.001 ─────────────────────────── 36.000]   (12.000 bytes)
```
- Os bytes **22.001 a 24.000** (2.000 bytes) foram **confirmados**: saem pela **esquerda**.
- A janela **aumentou** para 12.000: a borda **direita** foi de 32.000 para 36.000.

## Q15.c) Figura 3 (gráfico do cwnd)

Como ler: sobe **dobrando** (1, 2, 4, 8, 16, 32) = partida lenta. Sobe **de 1 em 1** = prevenção de congestionamento.

| Pergunta | Resposta | Justificativa |
|---|---|---|
| **Partida lenta** | Rodadas **1 a 6** e **23 a 26** | cwnd cresce **exponencialmente** (1, 2, 4, 8, 16, 32) |
| **Prevenção de congestionamento** | Rodadas **6 a 16** e **17 a 22** | cwnd cresce **linearmente** (+1 por rodada) |
| **Após a 6ª rodada** | cwnd chegou ao **limiar (ssthresh = 32)** | Muda de partida lenta (exponencial) para prevenção (linear) |
| **Após a 16ª rodada** | **3 ACKs duplicados** | cwnd cai **pela metade** (42 → ~21), ssthresh = 21, e continua **linear** |
| **Após a 22ª rodada** | **Timeout (RTO)** | cwnd volta a **1**, ssthresh = metade (≈13) e recomeça a **partida lenta** |

**Macete:** **3 ACKs duplicados** = problema leve → cwnd cai **pela metade**. **Timeout** = problema grave → cwnd **volta a 1**.

---

# Q16 — Por que janela de congestionamento se já existe o RTO dobrado?
- Dobrar o RTO só **reage depois** da perda e só **espaça as retransmissões**.
- A janela **cwnd** **limita o tanto de dado novo** em trânsito o tempo todo e **ajusta a taxa** conforme a rede, **evitando** que o congestionamento piore.

---

# Q17 e Q18 — Quantos bytes ainda podem ser enviados?

**Fórmula:**
```
janela efetiva = min(cwnd, rwnd)
pode enviar    = janela efetiva − bytes já enviados e não confirmados
```
| | cwnd | rwnd | Em trânsito | min | **Pode enviar** |
|---|---|---|---|---|---|
| **Q17** | 2.000 | 6.000 | 2.000 | 2.000 | 2.000 − 2.000 = **0 bytes** |
| **Q18** | 8.000 | 5.000 | 2.000 | 5.000 | 5.000 − 2.000 = **3.000 bytes** |

---

# Q19 — UDP × TCP

## Q19.a) Campos do TCP que não existem no UDP, e por quê
| Campo do TCP | Por que o UDP não tem |
|---|---|
| **Nº de sequência e de confirmação** | UDP **não é confiável** (sem ordem, ACK ou retransmissão) |
| **HLEN e Opções** | O cabeçalho UDP é **fixo (8 bytes)** |
| **Flags** (SYN, FIN, ACK, RST, PSH, URG) | UDP **não tem conexão** e não tem urgente/push |
| **Janela** | UDP **não tem controle de fluxo** |
| **Ponteiro urgente** | UDP não trata dados urgentes |
| **Reservado** | Sobra dos campos de controle que o UDP não usa |

## Q19.b) UDP dá mais controle sobre **quais dados** vão no segmento
O UDP **respeita os limites da mensagem**: cada envio da aplicação vira **um datagrama**. O TCP é um **fluxo de bytes**: ele decide sozinho como cortar e juntar os dados em segmentos.

## Q19.c) UDP dá mais controle sobre **quando** os dados são enviados
O UDP **envia na hora**: não tem controle de congestionamento, de fluxo nem conexão. O TCP pode **segurar** os dados (cwnd, rwnd, esperar ACKs, juntar dados).

## Q19.d) Dá para ter transferência confiável sobre UDP?
**Sim.** A **aplicação** implementa a confiabilidade: nº de sequência, ACK, temporizador e retransmissão (ex.: TFTP, QUIC).

---

# ⭐ EXTRA — TCP: **Piggyback** e **Transferência de dados** (questão da prova)

## Piggyback ("carona")
**O que é:** mandar o **ACK dentro de um segmento de dados** que já vai no sentido contrário, em vez de mandar um segmento só de ACK.

**Como funciona:**
1. B recebe dados de A.
2. B **espera um pouquinho** (ACK atrasado, cerca de até 500 ms).
3. Se B tiver dados para A, manda **dados + ACK no mesmo segmento** (ACK = 1, com o nº de confirmação preenchido).
4. Se não tiver, manda só o ACK.

**Vantagem:** **menos segmentos e menos overhead** na rede.

```
 A                                             B
 │── seq=100, ack=30, ACK, 10 B de dados ─────▶│
 │◀─ seq=30, ack=110, ACK, 20 B de dados ──────│  ← PIGGYBACK: dados de B + ACK dos bytes 100–109
 │── seq=110, ack=50, ACK ────────────────────▶│  ← ACK dos 20 B de B
```

## Transferência de dados no TCP
**Ideias-chave:**
- Os dados são um **fluxo de bytes** cortado em segmentos.
- **Nº de sequência** = número do **1º byte** do segmento.
- **Próximo seq** = seq atual + bytes de dados.
- **Nº de confirmação** = **próximo byte esperado** (ACK **cumulativo**).
- **Confiabilidade:** temporizador (RTO), retransmissão, retransmissão rápida (3 ACKs duplicados) e buffer para segmentos fora de ordem.
- **Flags úteis:** ACK (confirma), PSH (entrega imediata), URG (urgente).

```
 A                                          B
 │── seq=100, 10 B ─────────────────────────▶│
 │── seq=110, 10 B ─────────────────────────▶│
 │── seq=120, 10 B ─────────────────────────▶│
 │◀─ ACK, ack=110 ────────────────────────────│   (confirma até o byte 109)
 │◀─ ACK, ack=120 ────────────────────────────│
 │◀─ ACK, ack=130 ────────────────────────────│   (confirma até o byte 129)
```

**Regras de ACK do receptor (resumo):**
| Situação | O que o receptor faz |
|---|---|
| Tem dados para enviar | **Piggyback**: dados + ACK juntos |
| Chegou segmento **em ordem** | Pode esperar até ~500 ms; se chegarem **2 segmentos em ordem**, manda ACK na hora |
| Chegou segmento **fora de ordem** (lacuna) | **ACK duplicado imediato** (repete o próximo byte esperado) |
| Chegou o segmento que **preenche a lacuna** | **ACK imediato** e cumulativo |
| Chegou segmento **duplicado** | **Descarta** e manda ACK imediato |

---

# 🔑 COLA FINAL (leia na véspera e antes da prova)

**Cabeçalhos**
- **UDP** = 8 bytes: origem, destino, comprimento, checksum. Dados = comprimento − 8.
- **TCP** = 20 a 60 bytes. Tamanho = HLEN × 4. Opções = tamanho − 20.
- Flags: **U A P R S F**.
- Hex → decimal: `0532` = 1330 · `0017` = 23 · `07FF` = 2047 · `0632` = 1586 · `001C` = 28.

**Conexão**
- Abertura: **SYN → SYN+ACK → ACK** (ack = ISN + 1).
- Fechamento: **FIN → ACK → FIN → ACK**.

**Confiabilidade**
- Perdeu dado ou ACK → **RTO** → retransmite o **mais antigo não confirmado**.
- ACK é **cumulativo**: ACK posterior cobre os perdidos.
- Fora de ordem → **ACK duplicado**. **3 ACKs duplicados** → **retransmissão rápida**.
- **Piggyback** = ACK + dados no mesmo segmento.

**Janelas**
- **pode enviar = min(cwnd, rwnd) − em trânsito**.
- cwnd: partida lenta (dobra) → limiar → prevenção (+1).
- **3 ACKs duplicados** → cwnd/2. **Timeout** → cwnd = 1.

**UDP × TCP**
- UDP: sem conexão, sem ACK, sem controle de fluxo/congestionamento, cabeçalho de 8 bytes, envia na hora, respeita limites da mensagem.
- Confiabilidade sobre UDP: só feita pela **aplicação**.