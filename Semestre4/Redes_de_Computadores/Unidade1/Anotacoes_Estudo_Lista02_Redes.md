# Anotações — Camada de Transporte (UDP e TCP)
### Apoio para a Lista de Exercícios 02

> Essas notas seguem a ordem da lista. A ideia não é dar a "resposta pronta" de tudo, mas te dar o raciocínio e os conceitos para você mesmo montar as respostas — com alguns exemplos já resolvidos usando os números da própria lista.

---

## 1. UDP (User Datagram Protocol)

### 1.1 Cabeçalho UDP

O cabeçalho UDP é bem enxuto: só **8 bytes**, divididos em 4 campos de 2 bytes cada.

| Campo | Tamanho | O que significa |
|---|---|---|
| **Porta de origem** | 16 bits | Identifica o processo/aplicação que enviou o segmento (para a resposta saber pra onde voltar) |
| **Porta de destino** | 16 bits | Identifica o processo/aplicação de destino na máquina receptora |
| **Comprimento (Length)** | 16 bits | Tamanho total do segmento UDP (cabeçalho **+** dados), em bytes |
| **Checksum** | 16 bits | Verificação de erros — cobre cabeçalho + dados (e um "pseudo-cabeçalho" com IPs de origem/destino) |

Repare: **não existem** campos de número de sequência, ACK, janela, flags, etc. Isso é a essência do UDP — ele não estabelece conexão, não confirma recebimento, não reordena nem retransmite nada. É "melhor esforço" (best-effort), igual o IP por baixo dele.

### 1.2 Como ler um cabeçalho UDP em hexadecimal

Cada campo tem **2 bytes = 4 dígitos hexadecimais**. A ordem é sempre:

```
[porta origem 4 hex][porta destino 4 hex][comprimento 4 hex][checksum 4 hex]
```

**Exemplo resolvido** (mesmo formato do item b da questão 1):
Cabeçalho: `06 32 00 0D 00 1C E2 17`

- Porta origem = `0632` (hex) → converta para decimal
- Porta destino = `000D` (hex) → converta para decimal
- Comprimento = `001C` (hex) → converta para decimal → esse é o tamanho **total** do segmento (cabeçalho de 8 bytes + dados)
- Checksum = `E217` (hex) — não precisa converter, é só usado para validação, não carrega informação "legível"

💡 **Dica de conversão rápida hex→decimal:** cada dígito hex vale uma potência de 16.
`0x0632 = 0×16³ + 6×16² + 3×16¹ + 2×16⁰ = 0 + 1536 + 48 + 2 = 1586`

Para achar o **comprimento dos dados** (não do segmento todo), é só:
`dados = comprimento_total − 8` (os 8 bytes fixos do cabeçalho)

### 1.3 Tamanho máximo e mínimo do segmento UDP

- **Mínimo:** 8 bytes (só o cabeçalho, sem dados — é permitido enviar um datagrama vazio).
- **Máximo:** o campo *Length* tem 16 bits, então o maior valor representável é `2^16 − 1 = 65.535` bytes. Mas, na prática, o UDP está limitado pelo tamanho máximo de um datagrama IP, então o limite real de dados costuma ser um pouco menor (65.507 bytes, descontando cabeçalhos IP/UDP) — vale citar os dois raciocínios na resposta: o limite teórico do campo e o limite prático imposto pelo IP.

### 1.4 Por que o Checksum "caiu em desuso"?

Não é que ele desapareceu — em **IPv4** o checksum UDP é opcional (pode ser zero); em **IPv6** ele é obrigatório. A ideia por trás da pergunta é: por que muitas aplicações/pilhas modernas não dependem tanto dele?

- É uma verificação **fraca** (16 bits só detecta certos padrões de erro, não corrige nada).
- Aplicações modernas que precisam de robustez fazem sua **própria verificação de integridade** na camada de aplicação (ex: hashes, checksums mais fortes).
- Aplicações que usam UDP geralmente toleram perda/erro esporádico (streaming, VoIP, jogos) — não faz sentido pagar o custo de processamento do checksum para "descartar" o pacote de qualquer forma.
- Camada de enlace (Ethernet, por ex.) já faz sua própria checagem de erro nos quadros, então há redundância.

---

## 2. TCP (Transmission Control Protocol)

### 2.1 Cabeçalho TCP

Bem mais robusto que o UDP: **20 bytes fixos** (sem opções) + opções variáveis.

| Campo | Tamanho | Significado |
|---|---|---|
| Porta origem | 16 bits | Aplicação de origem |
| Porta destino | 16 bits | Aplicação de destino |
| Número de sequência | 32 bits | Posição, em bytes, do primeiro byte de dados deste segmento no fluxo |
| Número de confirmação (ACK) | 32 bits | Próximo byte que o receptor **espera** receber (confirma tudo até ele − 1) |
| HLEN (Data Offset) | 4 bits | Tamanho do cabeçalho, em **palavras de 32 bits** (não em bytes!) |
| Reservado | 6 bits | Não usado |
| Flags (URG, ACK, PSH, RST, SYN, FIN) | 6 bits | Bits de controle — cada um liga/desliga uma função do segmento |
| Janela de recepção (rwnd) | 16 bits | Quantos bytes o remetente deste segmento ainda pode receber (controle de fluxo) |
| Checksum | 16 bits | Verificação de erros (obrigatório no TCP) |
| Ponteiro urgente | 16 bits | Só válido se URG=1; indica onde termina o dado urgente |
| Opções | variável (0–40 bytes) | Ex: MSS, Window Scale, SACK, Timestamps |

**As 6 flags, resumidas:**

| Flag | Uso |
|---|---|
| **URG** | Indica que há dado urgente no segmento |
| **ACK** | Indica que o campo de confirmação é válido (quase sempre ligado, exceto no 1º SYN) |
| **PSH** | Pede para a aplicação de destino entregar os dados imediatamente ("empurrar"), sem esperar acumular no buffer |
| **RST** | Reseta a conexão (erro/recusa) |
| **SYN** | Usado para sincronizar números de sequência — abertura de conexão |
| **FIN** | Sinaliza que o remetente terminou de enviar dados — fecha a conexão |

### 2.2 Lendo um cabeçalho TCP em hexadecimal

O cabeçalho é dividido em **palavras de 32 bits (4 bytes = 8 hex)**. Estrutura das primeiras 5 palavras (20 bytes fixos):

```
Palavra 1: [porta origem 4 hex][porta destino 4 hex]
Palavra 2: [número de sequência - 8 hex]
Palavra 3: [número de confirmação - 8 hex]
Palavra 4: [HLEN(4bits)+Reserv(6bits)+Flags(6bits) - 4 hex][janela - 4 hex]
Palavra 5: [checksum - 4 hex][ponteiro urgente - 4 hex]
```

**Como quebrar a 4ª palavra** (a mais "chata"): ela tem 32 bits = HLEN(4) + Reservado(6) + Flags(6) + Janela(16).
Pegue os primeiros **4 bits** (primeiro dígito hex) → isso é o HLEN.
Os próximos 12 bits (Reservado+Flags) → converta para binário e leia as 6 flags nos últimos 6 bits.

**Exemplo de raciocínio** (aplicável ao item b, com `500207FF`):
- `5002` em binário: `0101 0000 0000 0010`
- Os 4 primeiros bits (`0101` = 5) → HLEN = 5 → 5 palavras de 32 bits = **20 bytes** (cabeçalho sem opções)
- Os 6 bits seguintes → reservado (ignorar)
- Os últimos 6 bits (`000010`) → flags na ordem URG-ACK-PSH-RST-SYN-FIN → aqui só o **SYN** está ligado
- `07FF` → converta para decimal → tamanho da janela anunciada

Esse mesmo método se aplica ao cabeçalho do item b da questão 2 — vale a pena você refazer a conta com os valores exatos dados (`05320017 00000001 00000000 500207FF 00000000`), separando palavra por palavra.

### 2.3 Sobre o campo HLEN (item d)

Lembre-se: **HLEN conta em palavras de 32 bits, não em bytes**. Então:

`tamanho do cabeçalho (bytes) = HLEN × 4`

Regras de coerência:
- O cabeçalho TCP **mínimo** é 20 bytes (sem opções) → HLEN mínimo = **5** (0101 em binário)
- O cabeçalho **máximo** é 60 bytes (20 fixos + até 40 de opções) → HLEN máximo = **15** (1111 em binário)
- Qualquer HLEN **fora do intervalo 5–15** é inconsistente (não existe cabeçalho menor que 20 bytes nem maior que 60)

Para achar bytes de opções: `opções = (HLEN × 4) − 20`

Aplique essa fórmula a cada um dos 6 valores binários dados (`0101; 1000; 0011; 1100; 0100; 0111`) — converta cada um para decimal primeiro, depois cheque se está entre 5 e 15.

### 2.4 Tamanho máximo/mínimo do cabeçalho TCP

Já adiantado acima:
- **Mínimo:** 20 bytes (sem opções)
- **Máximo:** 60 bytes (20 + 40 de opções, pois HLEN tem só 4 bits → máximo 15 palavras × 4 bytes)

### 2.5 Interpretando combinações de flags (item f)

Flags na ordem **URG-ACK-PSH-RST-SYN-FIN**:

| Valor | Decomposição | O que significa |
|---|---|---|
| `000000` | nenhuma flag ligada | Segmento "neutro" — só carrega dados, sem nenhuma sinalização especial (situação incomum isoladamente, pois normalmente ACK=1 após handshake) |
| `000001` | FIN=1 | Pedido de encerramento de conexão daquele lado |
| `010001` | ACK=1, FIN=1 | Confirmação de dados anteriores **e** pedido de encerramento no mesmo segmento (comum no fim de uma conexão) |

### 2.6 Dados urgentes vs. dados normais (item g)

- **Dados normais:** entregues à aplicação **em ordem**, na sequência em que chegam no buffer (respeitando número de sequência).
- **Dados urgentes:** o TCP sinaliza com a flag **URG=1** e usa o campo **ponteiro urgente** para indicar *onde termina* o trecho urgente dentro da área de dados daquele segmento. A aplicação é avisada para tratar esse trecho fora da ordem normal (é uma forma de "furar a fila", embora hoje em dia seja pouco usado na prática — TELNET é o exemplo clássico, ex: Ctrl+C).
- O TCP descobre que há dados urgentes verificando o bit **URG** no cabeçalho; o **início** do dado urgente é sempre o começo da área de dados do segmento, e o **fim** é indicado por `número de sequência do 1º byte + ponteiro urgente`.

### 2.7 Entrega tradicional vs. "empurrar" dados (PSH) — item h

- **Entrega tradicional:** o TCP pode **acumular** dados no buffer de recepção antes de entregar à aplicação (por eficiência), esperando um volume razoável ou um evento que force a entrega.
- **Com PUSH:** o remetente marca a flag **PSH=1**, dizendo ao TCP receptor "entregue esses dados à aplicação **imediatamente**, não espere acumular mais". Muito usado em aplicações interativas (chat, terminal remoto), onde cada tecla digitada precisa chegar rápido.
- O TCP identifica isso checando o bit **PSH** no cabeçalho recebido.

### 2.8 Por que o Checksum é "menos crítico" hoje (item i)

Raciocínio parecido com o do UDP: redes modernas (fibra, links confiáveis) têm taxa de erro muito baixa; a camada de enlace já verifica erros de quadro; e muitos ambientes usam checksums mais fortes em camadas superiores (TLS, por exemplo, garante integridade criptográfica). Mas, diferente do UDP, **no TCP o checksum é sempre calculado e verificado** — a "atenuação" aqui é mais sobre ele não ser a única/principal linha de defesa contra corrupção de dados.

### 2.9 Estabelecimento de conexão — 3-way handshake (item j)

Com ISN(A) = 10.000 e ISN(B) = 20.000, o diagrama temporal segue este roteiro (desenhe duas linhas verticais, A à esquerda e B à direita, tempo correndo para baixo):

1. **A → B:** `SYN=1, seq=10000` (A propõe seu número de sequência inicial)
2. **B → A:** `SYN=1, ACK=1, seq=20000, ack=10001` (B confirma o byte de A e propõe o seu próprio ISN)
3. **A → B:** `ACK=1, seq=10001, ack=20001` (A confirma o ISN de B — conexão estabelecida)

Note o padrão: **ack = seq_recebido + 1** (o SYN "consome" um número de sequência mesmo sem carregar dados).

### 2.10 Análise da Figura 1 (item k)

A figura mostra: A envia um segmento de dados (seq=100, 10 bytes) → B recebe e manda ACK=110. Um temporizador é iniciado quando A transmite.

- **Se o segmento verde (dados) se perder:** B nunca recebe os dados, então nunca envia o ACK. O temporizador de A expira (atinge RTO) e A **retransmite** o segmento.
- **Se o segmento vermelho (ACK) se perder:** B já recebeu os dados normalmente, mas A não sabe disso. O temporizador de A expira e A retransmite o segmento — B vai recebê-lo **duplicado**, mas o TCP em B detecta pelo número de sequência que já tinha esses dados e apenas reenvia o ACK (descarta o duplicado da entrega à aplicação).
- **Se o ACK chegar depois do RTO:** A já retransmitiu o segmento (achando que havia se perdido). Quando o ACK atrasado chega, ele é simplesmente tratado como confirmação (pode até confirmar a retransmissão). Isso gera uma retransmissão "desnecessária", mas não quebra a corretude do protocolo — só um pouco de desperdício de banda.

### 2.11 Um único temporizador para vários segmentos (item l)

O TCP não cria um timer por segmento — isso seria caro e complexo. Em vez disso:

- Existe **um único timer "mais antigo"** rodando por vez, associado ao segmento **enviado e ainda não confirmado há mais tempo**.
- Quando chega um ACK que confirma esse segmento mais antigo, o timer é **reiniciado** para o próximo segmento pendente mais antigo (se houver algum).
- Se o timer expira (RTO), o TCP retransmite o segmento **mais antigo ainda não confirmado** — não necessariamente todos os dez.

### 2.12 Análise da Figura 2 (item m)

Aqui o TCP usa **ACKs cumulativos**: um ACK confirma "recebi tudo até este byte", não segmento-a-segmento. Isso muda bastante a análise:

- **Perda apenas do 1º ACK (ack=110):** sem problema — o 2º ACK (ack=120) que chega depois já confirma tudo que o 1º confirmaria (é cumulativo). A não precisa retransmitir nada.
- **Perda apenas do 2º ACK (ack=120):** mesmo raciocínio — o 3º ACK (ack=130) cobre essa confirmação. Sem impacto.
- **Perda apenas do 3º ACK (ack=130):** este é o último; se ele se perder e nenhum outro ACK chegar depois, o timer de A vai expirar e A retransmite o segmento correspondente a partir do byte 120.
- **Perda apenas do 1º segmento de dados (seq=100):** B não recebe esses bytes, então **não pode confirmá-los** nem os segmentos seguintes de forma "avançada" — B continua reconhecendo apenas o que recebeu em ordem (ack ficaria "preso" em 100, esperando o segmento perdido). Isso ilustra que ACKs cumulativos exigem recebimento **em ordem**.
- **Perda apenas do 2º segmento (seq=110):** o 1º chega bem (B pode confirmar até 110), mas o 3º (seq=120) chega fora de ordem — B não pode avançar o ACK além de 110 até receber o que falta.
- **Perda apenas do 3º segmento (seq=120):** os dois primeiros chegam normalmente e são confirmados; o terceiro, se perdido, será notado pelo timeout (ou por ACKs duplicados, se houver mais segmentos depois).
- **1º segmento é o último a chegar (fora de ordem, mas todos chegam eventualmente):** B recebe 2º e 3º primeiro, mas só pode entregar à aplicação (e avançar o ACK "oficial") quando o 1º finalmente chega, pois o TCP entrega dados **em ordem** à aplicação, mesmo que já tenha recebido pedaços posteriores no buffer.

### 2.13 Retransmissão rápida (Fast Retransmit) — item n

Mecanismo que **não espera o timeout (RTO)** para agir:

1. Quando B recebe um segmento fora de ordem (um "buraco" na sequência), ele reenvia um **ACK duplicado** — repetindo a confirmação do último byte em ordem recebido.
2. Se o remetente A recebe **3 ACKs duplicados** (ou seja, 3 confirmações repetidas do mesmo número), ele interpreta isso como forte indício de que um segmento se perdeu.
3. A retransmite **imediatamente** o segmento faltante, **sem esperar o RTO expirar** — daí o nome "retransmissão rápida".

### 2.14 Por que esperar 3 ACKs duplicados, e não 1? (item o)

- Pacotes podem chegar **fora de ordem** na rede por motivos normais (rotas diferentes, filas), gerando 1 ou até 2 ACKs duplicados isolados sem que nada tenha se perdido de verdade.
- Esperar por **3 duplicados** dá mais confiança de que realmente houve perda (e não apenas um reordenamento passageiro), evitando retransmissões desnecessárias que desperdiçariam banda.

### 2.15 Encerramento de conexão (item p)

Diferente da abertura (3 passos), o encerramento é geralmente feito em **4 passos** porque cada lado precisa fechar seu próprio "lado" da comunicação (é bidirecional):

1. **A → B:** `FIN=1` (A não tem mais dados a enviar)
2. **B → A:** `ACK=1` (B confirma o FIN de A — mas B ainda pode continuar enviando dados para A, se quiser)
3. **B → A:** `FIN=1` (agora B também termina de enviar)
4. **A → B:** `ACK=1` (A confirma o FIN de B — conexão totalmente encerrada)

Depois do passo 4, A normalmente entra num estado de espera (**TIME_WAIT**) por um tempo, para garantir que o ACK final chegou e não confundir pacotes atrasados com uma nova conexão.

---

## 3. Controle de Fluxo e Controle de Congestionamento

> **Diferença-chave para não confundir:**
> - **Controle de fluxo (rwnd)** — protege o **receptor**: evita que o remetente envie mais dados do que o buffer do destino aguenta.
> - **Controle de congestionamento (cwnd)** — protege a **rede**: evita que o remetente sobrecarregue os roteadores/links no caminho.
>
> A quantidade real que o remetente pode enviar sem confirmação é sempre o **menor** entre os dois: `janela efetiva = min(cwnd, rwnd)`.

### 3.1 Inicialização e atualização do rwnd (item a)

- O `rwnd` é inicializado durante o **3-way handshake**, quando cada lado informa, no seu próprio segmento (SYN e SYN-ACK), o tamanho do seu buffer de recepção disponível naquele momento.
- Depois disso, o valor é **atualizado a cada segmento enviado**: todo segmento TCP carrega no cabeçalho o valor atual da janela de recepção daquele lado, refletindo o espaço livre no buffer **naquele instante** (que varia conforme a aplicação consome os dados do buffer).

### 3.2 Exemplo de janela de recepção (item b)

Dados: janela anterior de recepção = 10.000 bytes, ACK anterior = 22.001. Chega um segmento com **ack = 24.001** e **janela anunciada = 12.000**.

Raciocínio para o diagrama "antes/depois":
- **Antes:** o remetente podia enviar bytes de 22.001 até 22.001 + 10.000 − 1 = **32.000**
- Chegaram 2.000 novos bytes confirmados (de 22.001 até 24.000), então a "borda esquerda" da janela avança para 24.001
- **Depois:** a nova janela vai de 24.001 até 24.001 + 12.000 − 1 = **36.000**

Desenhe uma reta numerada representando os bytes do fluxo, com a janela antiga marcada (22.001–32.000) e a nova deslocada e maior (24.001–36.000).

### 3.3 Lendo o gráfico de cwnd (Figura 3) — item c

Padrão clássico para identificar as fases no gráfico:

- **Partida lenta (slow start):** cwnd cresce **exponencialmente** (dobra a cada RTT/rodada) — no gráfico, é o trecho com inclinação mais "íngreme e curva", tipicamente do início até a janela atingir o limiar (`ssthresh`).
- **Prevenção de congestionamento (congestion avoidance):** cwnd cresce **linearmente** (+1 por rodada) — trecho com reta mais "suave".
- **Após uma queda brusca pela metade (ex: 6ª, 22ª rodada):** geralmente indica que o TCP recebeu **3 ACKs duplicados** (perda detectada por retransmissão rápida) — o TCP corta cwnd pela metade e entra em congestion avoidance (comportamento do TCP Reno/New Reno).
- **Após uma queda para o valor mínimo (cwnd = 1)**, ex: possivelmente a 16ª rodada dependendo da leitura do gráfico: indica **estouro de temporizador (timeout)** — evento mais grave, o TCP volta ao início, refazendo slow start do zero.

Para responder com precisão os "picos" específicos (6ª, 16ª e 22ª rodada), observe no gráfico: se a queda é **para metade do valor anterior** → 3 ACKs duplicados; se cai **para 1** → timeout.

### 3.4 Por que só duplicar o RTO não basta (item d)

Duplicar o intervalo de timeout (backoff exponencial) ajuda a evitar retransmissões repetidas quando a rede já está congestionada, mas **não regula quanto dado é injetado na rede em primeiro lugar**. Sem uma janela de congestionamento:
- O TCP continuaria enviando na mesma taxa alta até detectar perda por timeout, agravando o congestionamento antes de reagir.
- Não haveria um mecanismo *proativo* de ajuste gradual da taxa de envio conforme as condições da rede mudam (melhoram ou pioram).
- O `cwnd` permite uma resposta mais fina e contínua (crescendo enquanto a rede aguenta, recuando rápido quando detecta perda), enquanto o backoff do timer só atua *depois* que o pior já aconteceu.

### 3.5 e 3.6 — Quantos bytes ainda podem ser enviados

Fórmula-chave:

```
janela efetiva = min(cwnd, rwnd) − bytes_em_trânsito_não_confirmados
```

**Item e:** cwnd = 2.000, rwnd = 6.000, bytes não confirmados = 2.000
→ `min(2000, 6000) = 2000`
→ `2000 − 2000 = 0` → o host **não pode enviar mais nada** até receber alguma confirmação.

**Item f:** cwnd = 8.000, rwnd = 5.000, bytes não confirmados = 2.000
→ `min(8000, 5000) = 5000`
→ `5000 − 2000 = 3000` → o host pode enviar mais **3.000 bytes**.

---

## 4. Relação entre UDP e TCP

### 4.1 Campos exclusivos do TCP (item a)

Compare as duas tabelas de cabeçalho lá em cima. O TCP tem, e o UDP não tem:
- Número de sequência / número de confirmação (não fazem sentido no UDP porque **não há conexão nem controle de entrega ordenada**)
- Flags de controle (SYN, FIN, ACK, etc.) — desnecessárias sem estabelecimento/encerramento de conexão
- Janela de recepção — não existe controle de fluxo no UDP
- HLEN e campo de opções — o UDP tem tamanho fixo e simples, sem necessidade de extensões
- Ponteiro urgente — conceito ligado ao fluxo ordenado de bytes do TCP, que o UDP (orientado a datagramas independentes) não possui

A ausência de todos esses campos reflete a filosofia do UDP: ser **simples, leve e sem estado (connectionless)**.

### 4.2 Controle sobre "quais dados são enviados" (item b)

No TCP, a aplicação escreve dados num fluxo (*stream*) contínuo de bytes; é o **TCP quem decide** como segmentar esses bytes em segmentos (pode juntar, dividir, reordenar a forma de empacotar). No UDP, cada `send()` da aplicação vira **exatamente um** datagrama — a aplicação tem controle total e explícito sobre o que vai em cada segmento, sem o TCP "reagrupando" ou "fatiando" por conta própria.

### 4.3 Controle sobre "quando enviar" (item c)

O TCP pode **atrasar o envio** de dados por conta de mecanismos internos como o algoritmo de Nagle (agrupa dados pequenos antes de enviar) ou controle de congestionamento/fluxo (segura dados se a janela estiver cheia). No UDP, a aplicação envia o datagrama **imediatamente**, no exato momento que decide — sem nenhum atraso artificial imposto pelo protocolo de transporte.

### 4.4 Confiabilidade sobre UDP (item d)

**Sim, é possível.** O UDP em si não oferece confiabilidade, mas nada impede que a **camada de aplicação** implemente seus próprios mecanismos por cima dele, como:
- Números de sequência próprios (definidos pela aplicação)
- ACKs e retransmissões implementados na aplicação
- Controle de janela/fluxo próprio

Exemplos reais: **QUIC** (usado no HTTP/3) e protocolos de streaming/tempo real que implementam confiabilidade seletiva sobre UDP, obtendo os benefícios de baixa latência do UDP com garantias de entrega adicionadas manualmente onde necessário.

---

## Resumo rápido — UDP vs TCP

| Aspecto | UDP | TCP |
|---|---|---|
| Conexão | Sem conexão | Orientado a conexão (handshake) |
| Confiabilidade | Não garante entrega | Garante entrega e ordem |
| Controle de fluxo | Não tem | Sim (rwnd) |
| Controle de congestionamento | Não tem | Sim (cwnd, slow start, congestion avoidance) |
| Cabeçalho | 8 bytes fixos | 20–60 bytes |
| Velocidade/overhead | Mais rápido, menos overhead | Mais lento, mais overhead |
| Uso típico | DNS, streaming, VoIP, jogos | HTTP, e-mail, transferência de arquivos |

---

**Bons estudos!** Se quiser, dá pra usar essas notas junto com o Kurose & Ross (capítulo 3), que é a referência principal da lista — os exemplos e a notação seguem bem de perto o que está no livro.
