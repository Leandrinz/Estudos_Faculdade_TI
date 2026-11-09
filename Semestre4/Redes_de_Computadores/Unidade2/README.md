# 📘 Redes de Computadores — Apostila Intensiva de 2 Dias

> **Objetivo:** te preparar para a 2ª Avaliação (TCP/UDP, Fragmentação IPv4 e Transição IPv4/IPv6) partindo do seu nível atual.
> **Como usar:** leia na ordem, faça os exercícios **antes** de olhar o gabarito, e só avance quando acertar. Os gabaritos ficam no fim de cada módulo, em seção separada.

**Legenda**
- 🧠 **MEMORIZE** = precisa estar na cabeça no dia da prova.
- 🚚 **Analogia** = ideia intuitiva. Depois dela sempre vem a explicação técnica.
- ⚠️ **Cuidado** = erro comum que tira ponto.
- ✍️ **Na prova** = como o assunto aparece na avaliação.
- 📎 **Complementar** = pode ignorar se faltar tempo.

---

# 🗂️ SUMÁRIO

0. [Diagnóstico da prova e do seu nível](#0-diagnóstico-da-prova-e-do-seu-nível)
1. [Cronograma de 2 dias](#1-cronograma-de-2-dias)
2. [Módulo 1 — TCP e UDP](#módulo-1--tcp-e-udp)
3. [Módulo 2 — Datagrama e cabeçalho IPv4](#módulo-2--datagrama-e-cabeçalho-ipv4)
4. [Módulo 3 — Fragmentação IPv4 (prioridade máxima)](#módulo-3--fragmentação-ipv4-prioridade-máxima)
5. [Módulo 4 — Transição IPv4/IPv6](#módulo-4--transição-ipv4ipv6-tradução-e-tunelamento)
6. [Simulado final](#simulado-final)
7. [Gabarito do simulado](#gabarito-do-simulado)
8. [Folha de revisão relâmpago](#folha-de-revisão-relâmpago)

---

# 0. Diagnóstico da prova e do seu nível

## 0.1 Análise questão por questão

| Questão | Tema | Pontos | O que o professor quer | Tipo de resposta |
|---|---|---|---|---|
| **1** | TCP × UDP: por que o TCP **retém** dados na camada de transporte | 3,0 | **Citar** os aspectos do TCP que causam retenção **e explicar** por que cada um causa isso | Discursiva (verbo: *cite* + *explique*) |
| **2** | Fragmentação IPv4 no roteador de borda | 4,0 | **Todos os campos de todos os datagramas** que saem do roteador + **cálculos** + **justificativa** de cada campo que mudou | Cálculo + justificativa |
| **3** | Tradução e tunelamento IPv4→IPv6 | 3,0 | Explicar **em linhas gerais** como cada um funciona, **quais problemas** a tradução tem e **como o tunelamento os resolveu** | Discursiva (3 partes) |

## 0.2 Dados da Figura 1 (Questão 2) — leia com atenção

| Item da figura | Valor |
|---|---|
| Máquinas na LAN 01 (rede privada) | 192.168.0.2, 192.168.0.3, 192.168.0.4 |
| Interface do roteador na LAN | 192.168.0.5 |
| Interface do roteador para a Internet | 201.15.0.10 |
| **MTU** (do roteador de borda) | **500 bytes** |
| Segmento da camada de transporte | **2.500 bytes** |
| Versão | 4 |
| HLEN | 5 → cabeçalho de **20 bytes** |
| Tipo de serviço | 0 |
| Comprimento do datagrama | **2.520 bytes** (= 20 + 2.500) |
| ID | **1.350** |
| Flags | **000** (DF=0, MF=0) |
| Deslocamento | **0** |
| TTL | **7** |
| Protocolo | **17** (= UDP) |
| Checksum | 0xFFFF (todos os bits em 1) |
| IP de origem | 192.168.0.2 |
| IP de destino | 15.47.201.23 |

**Quadro "Flags" da figura:** 1º bit = `0` (não usado); 2º bit = `M`; 3º bit = `D`.

> ⚠️ **Atenção à ordem das flags.** No padrão oficial (RFC 791) a ordem é: 1º bit reservado (0), 2º bit **DF**, 3º bit **MF**. A figura da sua prova rotula 2º = M e 3º = D (confira na sua folha). **Na prova, siga a figura do professor** e, para não ter erro, **escreva também por extenso** "MF = 1, DF = 0". Assim a correção entende independente da ordem dos bits.

## 0.3 Conhecimentos exigidos

| Questão | Conceitos indispensáveis | Cálculos |
|---|---|---|
| 1 | ACK, retransmissão, buffer de envio/recepção, números de sequência, controle de fluxo (janela), controle de congestionamento | Nenhum |
| 2 | MTU, cabeçalho × payload, DF/MF, deslocamento, TTL, checksum, ID | Subtrações, divisão, múltiplos de 8, divisão por 8 |
| 3 | Diferenças IPv4/IPv6, tradução, tunelamento (encapsulamento) | Nenhum |

## 0.4 O que pode ser ignorado (por falta de tempo)

- Detalhes de cabeçalho TCP/UDP além do básico (portas, janelas em números).
- Algoritmos de congestionamento em detalhe (CUBIC, BBR).
- IPv6 além do necessário para a Questão 3 (extensões, NDP, SLAAC).
- Detalhes de NAT (só o que aparece como nota).
- Cálculo manual do checksum (**é complementar**; você só precisa saber que ele é **recalculado**).

## 0.5 Seu diagnóstico de lacunas

| Área | Situação | Prioridade |
|---|---|---|
| Endereçamento IPv4, máscaras, sub-redes, IPv6 (notação) | ✅ Você já domina. **Não vai cair na prova** | Só revisar se sobrar tempo |
| TCP/UDP, ACK, buffers | ❌ Do zero | Alta (3 pts, mas exige texto) |
| Cabeçalho IPv4, MTU, fragmentação | ❌ Do zero | **Máxima (4 pts)** |
| Tradução e tunelamento | ❌ Do zero | Alta (3 pts), mas é teórico e rápido de aprender |

> 💡 Boa notícia: o que você já sabe (binário, divisão em blocos, potências de 2) ajuda **muito** na fragmentação. A conta de fragmentação é simples. A dificuldade é conhecer **as regras**, não a matemática.

---

# 1. Cronograma de 2 dias

Considerando ~7 a 8 horas líquidas de estudo por dia. Se você tiver menos tempo, **corte as partes marcadas com 📎** e **nunca corte a prática de fragmentação**.

## 📅 Dia 1 — Construir a base e dominar a fragmentação

| Horário sugerido | Atividade | Duração |
|---|---|---|
| Bloco 1 | **Módulo 2** (datagrama e cabeçalho IPv4): ler + tabela de campos | 1h15 |
| Bloco 2 | **Módulo 3**, partes 3.1 a 3.6 (teoria da fragmentação) | 1h30 |
| Pausa | | 15 min |
| Bloco 3 | **Módulo 3**, 3.7: refazer **sozinho** a Questão 2 (sem olhar a resolução) e comparar | 1h |
| Bloco 4 | **Exercícios do Módulo 3** (Ex. 1 a 5), com gabarito só depois | 1h30 |
| Bloco 5 | **Módulo 1**, partes 1.1 a 1.4 (transporte, UDP, TCP básico, ACK) | 1h15 |
| Fechamento | Revisão rápida: tabela "o que muda e o que não muda" + checklist de fragmentação (de cabeça, em papel) | 30 min |

## 📅 Dia 2 — Discursivas, revisão e simulado

| Horário sugerido | Atividade | Duração |
|---|---|---|
| Bloco 1 | **Módulo 1**, 1.5 a 1.9 (buffers, fluxo, congestionamento) e **resposta-modelo da Questão 1** | 1h15 |
| Bloco 2 | **Exercícios do Módulo 1** | 45 min |
| Bloco 3 | **Módulo 4** completo (IPv6, tradução, tunelamento) | 1h30 |
| Pausa | | 15 min |
| Bloco 4 | **Exercícios do Módulo 4** + escrever sua versão da resposta da Questão 3 | 45 min |
| Bloco 5 | Refazer **de cabeça** 2 exercícios de fragmentação (um com cabeçalho de 20 bytes, um de fragmento de fragmento) | 45 min |
| Bloco 6 | **Simulado final** em condições de prova (cronometrado: 1h30) | 1h30 |
| Fechamento | Correção do simulado + folha de revisão relâmpago | 45 min |

**Regra de ouro:** errou um exercício? Volte à teoria do ponto exato do erro, depois refaça um **similar** antes de avançar.

---

# MÓDULO 1 — TCP e UDP

## 1.1 Antes da técnica: a ideia de "camadas"

🚚 **Analogia.** Imagine que você manda um presente por correio.
- Você (a **aplicação**) quer apenas que o presente chegue.
- Quem embala, etiqueta e decide *como* o pacote vai (rápido e sem garantia, ou devagar com rastreio e confirmação) é o **setor de transporte**.
- Quem escolhe estradas e cidades é o **setor de rotas** (a camada de rede, IP).

Na rede é igual. Os dados passam por **camadas**:

| Camada | Pergunta que ela responde | Exemplo |
|---|---|---|
| Aplicação | O que eu quero enviar? | Navegador, vídeo, jogo |
| **Transporte** | **Como** o dado vai de **programa a programa**? Com garantia ou sem? | **TCP, UDP** |
| Rede | Por onde (por quais roteadores) chega à outra máquina? | IP |
| Enlace/Física | Como passa no cabo/Wi-Fi? | Ethernet, Wi-Fi |

**Camada de transporte** = camada que leva os dados **de um programa (processo) em uma máquina até um programa em outra máquina**. Para saber *qual programa*, usa **portas** (ex.: 80 web, 53 DNS). O dado dessa camada se chama **segmento** (no TCP) ou **datagrama de usuário** (no UDP; a prova chama de "segmento" também).

✍️ **Na prova:** a Figura 1 mostra "Segmento: 2.500 bytes". Esse segmento é o que vira **payload (carga)** do datagrama IP.

## 1.2 UDP — o "mandar e esquecer"

**UDP** = *User Datagram Protocol*.

🚚 **Analogia:** jogar uma carta na caixa de correio **sem rastreio, sem aviso de recebimento**. Rápido, simples, mas você não sabe se chegou.

Características técnicas 🧠:
- **Sem conexão:** não há "conversa prévia"; a aplicação envia e pronto.
- **Sem confirmação (sem ACK)**, **sem retransmissão**, **sem ordenação**.
- **Sem controle de fluxo nem de congestionamento.**
- Cabeçalho pequeno (**8 bytes**).
- **Não guarda os dados:** o que a aplicação entrega, o UDP empacota e **manda imediatamente**.

Por isso serve para: voz/vídeo ao vivo, jogos online, DNS. Nesses casos, **chegar rápido importa mais que chegar perfeito**.

✍️ **Na prova (Questão 1):** a aplicação escolheu UDP justamente porque o UDP **não retém** pacotes.

## 1.3 TCP — a "entrega com rastreio"

**TCP** = *Transmission Control Protocol*.

🚚 **Analogia:** carta registrada com **aviso de recebimento**. Antes de enviar, você combina com o destinatário; cada carta é numerada; se ele não confirmar, você reenvia; e você não despeja mais cartas do que ele consegue guardar.

Características técnicas 🧠:
- **Orientado a conexão:** antes de enviar dados faz o **handshake de 3 vias** (SYN → SYN+ACK → ACK).
- **Confiável:** garante que os dados chegam **completos, sem duplicata e em ordem**.
- Usa **números de sequência**, **ACKs**, **retransmissão**, **controle de fluxo**, **controle de congestionamento**.
- Usa **buffers** (memória temporária) no emissor e no receptor.
- Cabeçalho maior (mínimo **20 bytes**).

## 1.4 Tabela comparativa TCP × UDP 🧠

| Característica | TCP | UDP |
|---|---|---|
| Conexão | Sim (handshake) | Não |
| Confiabilidade | Sim | Não |
| ACK / retransmissão | Sim | Não |
| Ordem garantida | Sim (números de sequência) | Não |
| Controle de fluxo | Sim (janela de recepção) | Não |
| Controle de congestionamento | Sim | Não |
| Retém dados na camada de transporte | **Sim** | **Não (quase nada)** |
| Velocidade/latência | Mais lento, mais atraso | Mais rápido, menos atraso |
| Cabeçalho | ≥ 20 bytes | 8 bytes |
| Uso típico | Web, e-mail, arquivos | Voz, vídeo ao vivo, jogos, DNS |

## 1.5 Buffer: o conceito mais importante da Questão 1

**Buffer** = **área de memória temporária** onde dados ficam **guardados à espera** de algo acontecer.

🚚 **Analogia:** a **fila de uma padaria**. O balconista só atende uma pessoa por vez; quem chegou espera na fila. A fila é o buffer.

No TCP existem **dois buffers por conexão**:

| Buffer | Onde | O que guarda | Até quando |
|---|---|---|---|
| **Buffer de envio** | Emissor | Dados que a aplicação mandou **e** dados já enviados **mas ainda não confirmados (sem ACK)** | Até receber o ACK (para poder **retransmitir**) |
| **Buffer de recepção** | Receptor | Dados que chegaram **fora de ordem** ou que a aplicação **ainda não leu** | Até reordenar/completar ou até a aplicação ler |

## 1.6 ACK, números de sequência e retransmissão

**Número de sequência (SEQ):** cada **byte** enviado pelo TCP tem um número. O cabeçalho TCP leva o número do **primeiro byte** do segmento. Isso permite ao receptor **ordenar** e **detectar falta**.

**ACK (*Acknowledgment*, confirmação):** o receptor responde "recebi tudo **até aqui**; o próximo byte que espero é o número X". O ACK carrega o **próximo byte esperado**.

**Exemplo:** o emissor envia um segmento com SEQ = 1000 e 500 bytes de dados (bytes 1000 a 1499). O receptor responde **ACK = 1500** ("espero o byte 1500").

**Retransmissão:** o emissor, ao enviar, **liga um temporizador** (*timeout*). Se o ACK não chega a tempo (ou chegam 3 ACKs duplicados), ele **reenvia** o segmento.

> 🔑 **Consequência decisiva:** para poder **reenviar**, o emissor precisa **manter uma cópia** dos dados enviados **até o ACK chegar**. Isso é **retenção** (no buffer de envio). **Esse é o ponto central da Questão 1.**

## 1.7 Ordenação e buffer de recepção

A rede IP pode entregar pacotes **fora de ordem** (caminhos diferentes). Exemplo: chegam os bytes 0–499, depois 1000–1499 (o 500–999 ainda não chegou).
O TCP **guarda** o 1000–1499 no buffer de recepção, **não entrega à aplicação** (ela precisa receber em ordem) e **espera** o trecho faltante. Quando ele chega, o TCP entrega tudo em ordem. Mais uma **retenção**.

## 1.8 Controle de fluxo

**Problema:** o emissor é rápido e o receptor é lento. O buffer de recepção enche.

🚚 **Analogia:** você jogando água num copo. Se despejar rápido demais, transborda. O receptor avisa "me mande no máximo X".

**Como funciona:** em cada ACK o receptor informa a **janela de recepção (*rwnd*)**: quantos bytes ainda cabem no buffer dele. O emissor **não pode ter mais bytes "em trânsito" sem ACK que o valor da janela**. Se a janela é **0**, o emissor **para** e **mantém os dados esperando** no buffer de envio.

## 1.9 Controle de congestionamento

**Problema:** não é o receptor que está sobrecarregado, é a **rede** (roteadores com filas cheias).

🚚 **Analogia:** trânsito engarrafado. Se todos acelerarem, piora.

**Como funciona:** o emissor mantém uma **janela de congestionamento (*cwnd*)** que estima quanto a rede aguenta.
- Começa pequena (**partida lenta / *slow start***) e cresce.
- Ao detectar perda (timeout, ACKs duplicados), **reduz** a janela.
- A quantidade permitida em trânsito é **min(rwnd, cwnd)**.

Resultado: mesmo que a aplicação tenha muitos dados, o TCP **segura parte deles no buffer de envio** até a janela permitir. Mais **retenção**.

## 1.10 Outras causas de retenção/atraso 📎

- **Handshake de 3 vias:** a aplicação só começa a enviar dados após estabelecer a conexão (atraso no início, não exatamente retenção de pacotes).
- **Algoritmo de Nagle:** o TCP pode **juntar pequenos pedaços de dados** e esperar mais dados (ou um ACK) antes de mandar, para não enviar segmentos minúsculos.

## 1.11 ✍️ Como responder à Questão 1 (resposta-modelo)

> **Enunciado resumido:** a aplicação quer enviar pacotes o mais rápido possível, escolheu UDP porque o UDP não retém pacotes como o TCP. Cite quais aspectos do TCP o fazem reter pacotes na camada de transporte e explique por quê.

**Resposta-modelo (nota máxima):**

O UDP é um protocolo sem conexão, sem confirmação e sem controle de fluxo ou de congestionamento. Assim, ele entrega o dado à camada de rede imediatamente. O TCP, por outro lado, oferece transmissão confiável e, por isso, possui mecanismos que podem reter os dados na camada de transporte:

1. **Confirmação (ACK) e retransmissão:** o TCP precisa guardar, no **buffer de envio**, uma cópia de cada segmento enviado até receber o ACK correspondente, pois, se o ACK não chegar (timeout), ele terá que retransmitir. Os dados ficam retidos até serem confirmados.

2. **Ordenação por números de sequência (buffer de recepção):** como os segmentos podem chegar fora de ordem, o TCP armazena no **buffer de recepção** os segmentos que chegaram adiantados, e só os entrega à aplicação em ordem, após receber os trechos faltantes.

3. **Controle de fluxo:** o receptor informa, via janela de recepção, quanto espaço livre tem no seu buffer. Se o receptor é lento ou a janela está cheia ou zerada, o emissor precisa **segurar** os dados no seu buffer até haver espaço, evitando estouro no receptor.

4. **Controle de congestionamento:** o TCP limita a quantidade de dados em trânsito por uma janela de congestionamento (começando pequena na partida lenta e diminuindo com perdas). Dados excedentes ficam **retidos** na máquina de origem até a rede suportar o envio.

5. *(Opcional)* **Algoritmo de Nagle e estabelecimento de conexão (handshake)**, que também atrasam a saída dos dados.

Como o UDP não possui nenhum desses mecanismos, os pacotes saem da máquina o mais cedo possível, o que atende ao interesse da aplicação, ao custo de não haver garantia de entrega, ordem ou controle de carga.

> 🧠 **Estrutura para memorizar:** **ACK/retransmissão → fluxo → congestionamento → ordenação.** Para cada um: **nome + por que retém**. A prova pede *citar* **e** *explicar*; só citar vale metade.

## 1.12 Exercícios do Módulo 1 (progressivos)

**Ex. 1.1 (fácil).** Complete: o UDP é ______ conexão e ______ confiabilidade; o TCP é ______ conexão e ______ confiabilidade.

**Ex. 1.2 (fácil).** Qual protocolo você usaria para uma chamada de vídeo ao vivo? Por quê?

**Ex. 1.3 (cálculo simples).** Um emissor TCP envia um segmento com SEQ = 4000 e 600 bytes de dados. Qual o ACK correto de volta, se tudo chegou bem?

**Ex. 1.4 (intermediário).** Chegaram ao receptor os bytes 0–999 e 2000–2999. Qual ACK o receptor envia? O que ele faz com os bytes 2000–2999?

**Ex. 1.5 (conceitual).** Por que o emissor TCP **não pode apagar** um segmento do buffer de envio logo depois de enviá-lo?

**Ex. 1.6 (conceitual).** Diferencie **controle de fluxo** e **controle de congestionamento** (quem protege quem? qual janela usa?).

**Ex. 1.7 (discursiva, estilo prova).** Uma aplicação de jogo online escolheu UDP. Explique, com pelo menos três aspectos do TCP, por que o TCP poderia atrasar o envio dos dados.

---

## ✅ Gabarito comentado — Módulo 1

> Tente primeiro. Só depois confira.

**1.1** UDP: **sem** conexão, **sem** confiabilidade. TCP: **com** conexão, **com** confiabilidade.

**1.2** **UDP**. Em vídeo ao vivo, pacote atrasado é inútil (o momento já passou), então prefere-se **perder um pedaço** a **esperar retransmissão**. O TCP atrasaria o vídeo com retransmissões e esperas.

**1.3** O segmento cobre os bytes 4000 a 4599. Próximo byte esperado: 4000 + 600 = **4600**. **ACK = 4600**.
*Erro comum:* responder 4601 ou 4599. O ACK é o **próximo byte esperado**, não o último recebido.

**1.4** O receptor tem os bytes até 999. O próximo esperado é **1000**, então **ACK = 1000**. Os bytes 2000–2999 ficam **guardados no buffer de recepção**, sem serem entregues à aplicação, até que o trecho 1000–1999 chegue (provavelmente após retransmissão).

**1.5** Porque ele só sabe que o dado chegou quando recebe o **ACK**. Se o ACK não vier (timeout), ele terá de **retransmitir** o segmento e, para isso, precisa ter a cópia. Só após o ACK ele libera o espaço do buffer.

**1.6**

| | Controle de fluxo | Controle de congestionamento |
|---|---|---|
| Protege | O **receptor** (buffer dele) | A **rede** (roteadores) |
| Quem decide | Receptor informa a **rwnd** | Emissor estima a **cwnd** |
| Gatilho | Buffer do receptor enchendo | Perdas/atrasos na rede |

Na prática o emissor respeita **min(rwnd, cwnd)**.

**1.7** (Modelo) O TCP poderia atrasar: (i) pelo **handshake** antes de enviar; (ii) pela **espera de ACK**: guarda os dados no buffer de envio e retransmite perdas, causando atrasos; (iii) pela **ordenação**: dados que chegaram após uma perda ficam no buffer de recepção esperando o segmento faltante (bloqueio de cabeça de fila); (iv) pelo **controle de fluxo/congestionamento**, que limita o que pode ser enviado de uma vez. O UDP envia imediatamente, aceitando perdas, o que é preferível em jogos.

---

# MÓDULO 2 — Datagrama e cabeçalho IPv4

## 2.1 A ideia básica: a encomenda

🚚 **Analogia.** Toda encomenda tem:
- **Etiqueta** (remetente, destinatário, tipo de serviço, prazo de validade, número de rastreio…) → o **cabeçalho**.
- **Conteúdo** (o produto) → a **carga útil**, o **payload**.

Na rede:
- **Datagrama IPv4** = **pacote da camada de rede (IP)** = **cabeçalho IP + payload**.
- **Payload** = o que o IP carrega: o **segmento** do TCP/UDP.
- **Cabeçalho** = informações de controle para os roteadores saberem **para onde** e **como** levar.

```
┌──────────────────────┬───────────────────────────────────────┐
│  CABEÇALHO IPv4      │            PAYLOAD                    │
│  (mín. 20 bytes)     │  (segmento TCP/UDP: 2.500 bytes)      │
└──────────────────────┴───────────────────────────────────────┘
└────────────── COMPRIMENTO TOTAL = 2.520 bytes ───────────────┘
```

🧠 **Fórmula-base (use sempre):**

> **Comprimento total = Cabeçalho + Payload**
> **Payload = Comprimento total − Cabeçalho**

## 2.2 O que é MTU

**MTU** = *Maximum Transmission Unit* (Unidade Máxima de Transmissão) = **maior tamanho de datagrama IP (cabeçalho + payload) que um enlace aceita enviar de uma só vez.**

🚚 **Analogia:** o **limite de carga de um caminhão**. Se a encomenda pesa mais que o caminhão aguenta, você precisa dividir em várias.

⚠️ **O MTU inclui o cabeçalho IP.** MTU 500 significa **datagrama inteiro ≤ 500 bytes**, e **não** "500 bytes de dados".
⚠️ O MTU **não** inclui o cabeçalho do quadro de enlace (Ethernet) — não se preocupe com isso na prova.

**O que acontece se o datagrama é maior que o MTU?**
- Se **DF = 0** (pode fragmentar): o roteador **fragmenta**.
- Se **DF = 1** (não fragmentar): o roteador **descarta** o datagrama e avisa a origem com uma mensagem **ICMP** ("fragmentação necessária"). 📎

## 2.3 Estrutura do cabeçalho IPv4

```
 0       4       8              16      19                     31
┌───────┬───────┬──────────────┬──────────────────────────────────┐
│Versão │ HLEN  │ Tipo Serviço │       Comprimento Total          │
├───────┴───────┴──────────────┼───────┬──────────────────────────┤
│        Identificação (ID)    │ Flags │   Deslocamento (Offset)  │
├──────────────┬───────────────┼───────┴──────────────────────────┤
│     TTL      │   Protocolo   │      Checksum do cabeçalho       │
├──────────────┴───────────────┴──────────────────────────────────┤
│                       IP de Origem                              │
├─────────────────────────────────────────────────────────────────┤
│                       IP de Destino                             │
├─────────────────────────────────────────────────────────────────┤
│                   Opções (se houver) + Padding                  │
└─────────────────────────────────────────────────────────────────┘
```

Cada linha tem **32 bits = 4 bytes**. Sem opções, são **5 linhas = 20 bytes**. É daí que vem **HLEN = 5**.

## 2.4 Tabela explicativa dos campos 🧠

| Campo | Tamanho | O que é / função | Na prova (valor) |
|---|---|---|---|
| **Versão** | 4 bits | Versão do IP (4 ou 6) | 4 |
| **HLEN / IHL** | 4 bits | *Header Length*: tamanho do cabeçalho **em palavras de 4 bytes**. **Bytes = HLEN × 4** | 5 → **20 bytes** |
| **Tipo de serviço** | 8 bits | Prioridade/qualidade desejada (DSCP/ECN) | 0 |
| **Comprimento total** | 16 bits | Tamanho do datagrama **inteiro** (cabeçalho + payload), em bytes. Máx. 65.535 | 2.520 |
| **Identificação (ID)** | 16 bits | "Número de série" do datagrama original; **todos os fragmentos o repetem** para o destino saber que pertencem juntos | 1.350 |
| **Flags** | 3 bits | Bit reservado (0); **DF** (*Don't Fragment*): 1 = proibido fragmentar; **MF** (*More Fragments*): 1 = ainda vem mais fragmento depois deste | 000 |
| **Deslocamento (*Fragment Offset*)** | 13 bits | **Posição** do payload do fragmento dentro do payload original, **em unidades de 8 bytes** | 0 |
| **TTL** | 8 bits | *Time To Live*: **cada roteador subtrai 1**; se chegar a 0, o datagrama é descartado (evita loops eternos) | 7 |
| **Protocolo** | 8 bits | Qual protocolo está no payload: **6 = TCP, 17 = UDP, 1 = ICMP** | 17 (UDP) |
| **Checksum** | 16 bits | Verificação de erros **só do cabeçalho**; **recalculado a cada roteador** (porque o TTL muda) | 0xFFFF (fictício) |
| **IP de origem** | 32 bits | Quem enviou | 192.168.0.2 |
| **IP de destino** | 32 bits | Quem deve receber | 15.47.201.23 |
| **Opções** | 0–40 bytes | Recursos raros. Se houver, HLEN > 5 | — |

> 🧠 **Memorize:** HLEN × 4 = bytes de cabeçalho. Protocolo: **6 = TCP, 17 = UDP**.

### Por que o deslocamento é "em unidades de 8 bytes"?

O campo tem **13 bits**, logo vai de 0 a 8.191. Se contasse em bytes, só alcançaria 8.191 bytes, mas o datagrama pode ter até 65.535. Contando em **blocos de 8 bytes**: 8.191 × 8 ≈ 65.528, que cobre o máximo.

**Consequência (decisiva para a prova):** o payload de cada fragmento (exceto o último) **tem de ser múltiplo de 8 bytes**, caso contrário o próximo deslocamento não seria um número inteiro.

## 2.5 O que **muda** e o que **não muda** na fragmentação 🧠

| Campo | Muda nos fragmentos? | Por quê |
|---|---|---|
| Versão | ❌ Não | Continua sendo IPv4 |
| HLEN | ❌ Não* | Cada fragmento leva um cabeçalho do mesmo tamanho (*sem opções*) |
| Tipo de serviço | ❌ Não | Mesma qualidade de serviço |
| **Comprimento total** | ✅ **Sim** | Cada fragmento tem seu próprio tamanho |
| **ID** | ❌ **Não** | É a "chave" para o destino remontar o datagrama |
| **Flags (MF)** | ✅ **Sim** | MF = 1 em todos menos no último (MF = 0) |
| **Deslocamento** | ✅ **Sim** | Cada fragmento começa em uma posição diferente |
| **TTL** | ✅ **Sim (−1)** | O roteador que encaminha decrementa 1 (vale para todos os fragmentos) |
| Protocolo | ❌ Não | Mesmo protocolo no payload |
| **Checksum** | ✅ **Sim** | Cabeçalho mudou, precisa recalcular **em cada fragmento** |
| IP de origem | ❌ Não* | O roteador não altera quem enviou |
| IP de destino | ❌ Não | Mesmo destino |

\* Considerando só fragmentação. Se houver NAT (fora do escopo da questão), o IP de origem mudaria.

## 2.6 Exercícios do Módulo 2

**Ex. 2.1 (fácil).** Qual o tamanho do cabeçalho em bytes se HLEN = 5? E se HLEN = 8?

**Ex. 2.2 (fácil).** Um datagrama tem comprimento total 1.500 e HLEN = 5. Qual o tamanho do payload?

**Ex. 2.3 (fácil).** Que valor do campo **Protocolo** indica TCP? E UDP?

**Ex. 2.4 (conceitual).** Por que o ID é igual em todos os fragmentos?

**Ex. 2.5 (conceitual).** Por que o checksum precisa ser recalculado mesmo se o datagrama **não** for fragmentado, apenas encaminhado por um roteador?

**Ex. 2.6 (intermediário).** Um datagrama de comprimento total 3.028 bytes tem HLEN = 7. Qual o tamanho do payload? E quanto seria o campo "Comprimento total" se o payload fosse o mesmo, mas HLEN fosse 5?

**Ex. 2.7 (conceitual).** Um roteador recebe um datagrama com **DF = 1** maior que o MTU do enlace de saída. O que ele faz?

---

## ✅ Gabarito comentado — Módulo 2

**2.1** Bytes = HLEN × 4. HLEN = 5 → 5 × 4 = **20 bytes**. HLEN = 8 → 8 × 4 = **32 bytes** (20 do básico + 12 de opções).

**2.2** Payload = total − cabeçalho = 1.500 − 20 = **1.480 bytes**.

**2.3** **TCP = 6; UDP = 17.**

**2.4** Porque, no destino, o ID (junto com origem, destino e protocolo) identifica **a qual datagrama original** cada fragmento pertence. Fragmentos com o mesmo ID são reunidos na remontagem. Se o ID mudasse, o destino não conseguiria reagrupá-los.

**2.5** Porque o roteador **diminui o TTL em 1**, alterando o conteúdo do cabeçalho. Como o checksum é calculado sobre o cabeçalho, o valor antigo deixa de ser válido e precisa ser recalculado **a cada salto**.

**2.6** Cabeçalho = 7 × 4 = 28 bytes. Payload = 3.028 − 28 = **3.000 bytes**. Com HLEN = 5 → cabeçalho 20; total = 3.000 + 20 = **3.020 bytes**.

**2.7** **Descarta** o datagrama (não pode fragmentar) e envia à origem uma mensagem **ICMP** "destino inalcançável — fragmentação necessária e DF ligado".

---

# MÓDULO 3 — Fragmentação IPv4 (prioridade máxima)

## 3.1 Por que fragmentar?

🚚 **Analogia.** Você precisa enviar um sofá de 2,5 m. O caminhão menor do trecho final só carrega 0,5 m de carga por viagem. Solução: **desmontar** o sofá em pedaços, mandar em várias viagens, **remontar** no destino.

**Técnico:** redes diferentes têm MTUs diferentes (Ethernet ≈ 1.500; outros menores). Se um datagrama maior que o MTU do **próximo enlace** precisa passar, o roteador o **divide em fragmentos**, cada um com **cabeçalho próprio** e cabendo no MTU.

**Quem fragmenta?** O roteador (ou a origem). **Quem remonta?** **Somente o destino final** (os roteadores intermediários não juntam).

## 3.2 A regra de ouro: separar cabeçalho e payload

Cada fragmento é um **datagrama completo**: **novo cabeçalho + pedaço do payload**.

```
Original:  [ CAB 20 ][ ───────────── PAYLOAD 2.500 ───────────── ]

Fragmentos (MTU 500):
F1: [ CAB 20 ][ 480 ]
F2: [ CAB 20 ][ 480 ]
F3: [ CAB 20 ][ 480 ]
F4: [ CAB 20 ][ 480 ]
F5: [ CAB 20 ][ 480 ]
F6: [ CAB 20 ][ 100 ]
```

⚠️ **Só o payload é dividido.** O cabeçalho é **duplicado** em cada fragmento (com ajustes). Por isso a soma dos comprimentos totais dos fragmentos é **maior** que o original.

## 3.3 O algoritmo — passo a passo 🧠

**Passo 1 — Payload total.**
`Payload = Comprimento total − (HLEN × 4)`

**Passo 2 — Máximo de payload que cabe em um fragmento.**
`Máx. bruto = MTU − cabeçalho`

**Passo 3 — Ajustar para múltiplo de 8** (descendo, nunca subindo).
`Máx. por fragmento = maior múltiplo de 8 ≤ Máx. bruto`
*(Dica: divida por 8, ignore a parte decimal, multiplique por 8.)*

**Passo 4 — Quantidade de fragmentos.**
`N = ⌈ Payload ÷ Máx. por fragmento ⌉` (arredonda **para cima**)

**Passo 5 — Tamanho do payload de cada fragmento.**
Os **N−1 primeiros** levam o Máx. por fragmento; o **último** leva o **resto**:
`Último = Payload − (N−1) × Máx.`

**Passo 6 — Comprimento total de cada fragmento.**
`Total = payload do fragmento + cabeçalho`

**Passo 7 — Deslocamento (offset).**
`Offset = (bytes de payload que vieram ANTES) ÷ 8`
Ou seja, o offset do fragmento seguinte = offset anterior + (payload do anterior ÷ 8).

**Passo 8 — Flags.**
**MF = 1** em todos exceto o último (**MF = 0**). **DF = 0** (precisa ser 0 para poder fragmentar).

**Passo 9 — Demais campos.**
ID igual; **TTL = TTL original − 1**; **checksum recalculado**; origem, destino, protocolo, versão, HLEN e TOS inalterados.

### Checklist de prova (copie para o papel)

```
1) Payload = Total − Cab
2) Máx = MTU − Cab → ajustar p/ múltiplo de 8
3) N = ⌈Payload ÷ Máx⌉
4) Payloads: Máx, Máx, ..., Resto
5) Total_i = Payload_i + Cab
6) Offset_i = soma dos payloads anteriores ÷ 8
7) MF = 1 (todos) exceto último = 0
8) ID igual | TTL − 1 | Checksum recalculado
```

## 3.4 Por que tem que ser múltiplo de 8?

O offset é contado em **blocos de 8 bytes**. Se o primeiro fragmento levasse, por exemplo, 485 bytes, o segundo começaria no byte 485, que **não é divisível por 8** (485 ÷ 8 = 60,6), e o campo offset (inteiro) não consegue expressar isso. Por isso se corta para o múltiplo de 8 anterior.

> Exemplo: MTU = 500, cab = 20 → bruto = 480 → 480 ÷ 8 = 60 (exato) ✅ já é múltiplo.
> Exemplo: MTU = 1.000, cab = 20 → bruto = 980 → 980 ÷ 8 = 122,5 → usa 122 × 8 = **976** ✅.

## 3.5 Flags DF e MF em detalhe

| Flag | Nome | Valor 1 significa | Valor 0 significa |
|---|---|---|---|
| **DF** | *Don't Fragment* | Proibido fragmentar | Pode fragmentar |
| **MF** | *More Fragments* | **Há mais fragmentos** depois deste | **Este é o último** (ou o datagrama não foi fragmentado) |

**Em binário (padrão RFC 791):** bits `[reservado][DF][MF]`
- Não fragmentado: `000`
- Fragmento intermediário (DF=0, MF=1): `001`
- Último fragmento: `000`

**Na figura da sua prova** a ordem rotulada é `[0][M][D]`, portanto fragmento intermediário seria `010`. Para não errar: **escreva sempre "MF = 1" por extenso ao lado**.

> ⚠️ O **último fragmento tem MF = 0**, mas **offset ≠ 0**. É assim que o destino o distingue de um datagrama não fragmentado (que tem MF = 0 **e** offset = 0).

## 3.5.1 TTL e checksum durante a fragmentação

- **TTL:** o roteador que está encaminhando decrementa **1**, uma vez. Todos os fragmentos saem com **TTL original − 1**. Aqui: 7 − 1 = **6** (**não** é 7 − 6).
- **Checksum:** é o **complemento de 1 da soma de todas as palavras de 16 bits do cabeçalho** (com o campo checksum zerado durante o cálculo). Como cada fragmento tem comprimento, flags/offset, TTL diferentes, **cada um tem seu checksum**, todos diferentes de 0xFFFF.
  Na prova, o professor deu 0xFFFF só como valor simbólico. O que ele cobra é saber que o checksum **muda** e **por quê**.

## 3.6 Remontagem no destino

O **destino final** reúne os fragmentos que têm **mesmo ID + mesma origem + mesmo destino + mesmo protocolo**:
1. Posiciona cada payload na posição **offset × 8**.
2. Sabe que **acabou** quando recebeu o fragmento com **MF = 0** e **não há buracos**.
3. Tamanho original do payload = **offset do último × 8 + payload do último**.
4. Se algum fragmento se perder, o datagrama inteiro é descartado depois de um temporizador (o IP não retransmite).

**Exemplo com os dados da prova:** último fragmento tem offset 300 e payload 100 → 300 × 8 + 100 = **2.500 bytes** ✅ (bate com o payload original).

## 3.7 🎯 Resolução completa da Questão 2 da sua prova

### Dados
- Datagrama recebido: **total 2.520**, HLEN = 5 (**cabeçalho 20**), ID = 1.350, flags = 000 (DF = 0 → **pode** fragmentar), offset = 0, TTL = 7, protocolo 17, origem 192.168.0.2, destino 15.47.201.23.
- MTU do roteador de borda = **500 bytes**.

### Passo 1 — Payload
Payload = 2.520 − 20 = **2.500 bytes** (confere com o segmento da figura).

### Passo 2 — Existe necessidade de fragmentar?
2.520 > 500 e DF = 0 → **sim, fragmenta**.

### Passo 3 — Máximo de payload por fragmento
Bruto = 500 − 20 = 480.
480 ÷ 8 = 60 (exato) → **Máx. = 480 bytes** (já é múltiplo de 8).

### Passo 4 — Quantidade de fragmentos
2.500 ÷ 480 = 5,208… → arredonda para cima → **N = 6 fragmentos**.

### Passo 5 — Payload de cada fragmento
- Fragmentos 1 a 5: **480** cada (5 × 480 = 2.400).
- Fragmento 6: 2.500 − 2.400 = **100**.

### Passo 6 — Comprimento total
- Fragmentos 1 a 5: 480 + 20 = **500**.
- Fragmento 6: 100 + 20 = **120**.

### Passo 7 — Deslocamento (÷ 8)
| Fragmento | Bytes antes | Offset |
|---|---|---|
| 1 | 0 | 0 ÷ 8 = **0** |
| 2 | 480 | 480 ÷ 8 = **60** |
| 3 | 960 | 960 ÷ 8 = **120** |
| 4 | 1.440 | 1.440 ÷ 8 = **180** |
| 5 | 1.920 | 1.920 ÷ 8 = **240** |
| 6 | 2.400 | 2.400 ÷ 8 = **300** |

### Passo 8 — Flags
Fragmentos 1 a 5: **MF = 1** (há mais), DF = 0. Fragmento 6: **MF = 0**, DF = 0.

### Passo 9 — TTL
7 − 1 = **6** em todos.

### Passo 10 — Checksum
Recalculado em cada fragmento (valores de exemplo calculados abaixo).

### 📋 Tabela final (todos os campos de todos os datagramas que saem)

| Campo | F1 | F2 | F3 | F4 | F5 | F6 |
|---|---|---|---|---|---|---|
| Versão | 4 | 4 | 4 | 4 | 4 | 4 |
| HLEN | 5 | 5 | 5 | 5 | 5 | 5 |
| Tipo de serviço | 0 | 0 | 0 | 0 | 0 | 0 |
| **Comprimento total** | **500** | **500** | **500** | **500** | **500** | **120** |
| ID | 1.350 | 1.350 | 1.350 | 1.350 | 1.350 | 1.350 |
| **Flags (MF; DF)** | **MF=1; DF=0** | MF=1; DF=0 | MF=1; DF=0 | MF=1; DF=0 | MF=1; DF=0 | **MF=0; DF=0** |
| Flags (bits da figura `0 M D`) | 010 | 010 | 010 | 010 | 010 | 000 |
| **Deslocamento** | **0** | **60** | **120** | **180** | **240** | **300** |
| **TTL** | **6** | **6** | **6** | **6** | **6** | **6** |
| Protocolo | 17 | 17 | 17 | 17 | 17 | 17 |
| **Checksum** (recalculado) | 0xF4C2 | 0xF486 | 0xF44A | 0xF40E | 0xF3D2 | 0x1513 |
| IP de origem | 192.168.0.2 | idem | idem | idem | idem | idem |
| IP de destino | 15.47.201.23 | idem | idem | idem | idem | idem |
| *(payload em bytes)* | 480 | 480 | 480 | 480 | 480 | 100 |

> Os checksums acima foram calculados de fato com os valores de cada cabeçalho, usando a codificação padrão (MF no 3º bit). Na prova, **não é necessário calcular o valor numérico**, a não ser que o professor peça. O essencial é escrever "**recalculado**" e justificar. Se quiser, coloque "Checksum ≠ 0xFFFF, recalculado".

### Demonstração do cálculo do checksum (F1) 📎

Palavras de 16 bits do cabeçalho (checksum = 0000):

| Palavra | Valor |
|---|---|
| Versão+HLEN+TOS | 0x4500 |
| Comprimento total (500) | 0x01F4 |
| ID (1.350) | 0x0546 |
| Flags+Offset (MF=1, offset 0) | 0x2000 |
| TTL(6)+Protocolo(17) | 0x0611 |
| Checksum | 0x0000 |
| Origem 192.168 | 0xC0A8 |
| Origem 0.2 | 0x0002 |
| Destino 15.47 | 0x0F2F |
| Destino 201.23 | 0xC917 |

Soma = 0x20B3B → "dobra" o excesso (o 2 que passou de 16 bits) somando-o ao resto: 0x0B3B + 0x2 = **0x0B3D**.
**Complemento de 1** (inverte os bits) = 0xFFFF − 0x0B3D = **0xF4C2** ✅.

### 📝 Justificativas dos campos que mudaram (o que o professor exige!)

Escreva algo como:

- **Comprimento total:** muda porque cada fragmento é um datagrama independente, com cabeçalho de 20 bytes + sua parte do payload. Como o MTU é 500, cada um dos cinco primeiros leva 480 bytes de dados (maior múltiplo de 8 que cabe em 500 − 20), totalizando 500; o último leva os 100 bytes restantes (2.500 − 5 × 480), totalizando 120.
- **Flags:** o bit MF indica se há mais fragmentos. Fica 1 nos fragmentos 1 a 5, pois ainda existem fragmentos seguintes, e 0 no último, que encerra o datagrama. DF continua 0 (foi isso que permitiu a fragmentação).
- **Deslocamento:** indica a posição do payload do fragmento no payload original, em unidades de 8 bytes. Fragmento 1 = 0; cada fragmento seguinte soma 480 ÷ 8 = 60: 0, 60, 120, 180, 240 e 300 (2.400 ÷ 8).
- **TTL:** o roteador de borda decrementa 1 ao encaminhar; 7 − 1 = 6, igual em todos os fragmentos.
- **Checksum:** o cabeçalho de cada fragmento mudou (comprimento, flags, deslocamento, TTL), então o checksum deve ser recalculado em cada um deles; por isso difere do 0xFFFF original e entre fragmentos.
- **Não mudaram:** versão, HLEN, tipo de serviço, protocolo e endereços de origem e destino (inalterados pela fragmentação). O **ID** também não muda: é o mesmo (1.350) para o destino identificar que todos os fragmentos pertencem ao mesmo datagrama e remontá-lo.

### ✅ Verificações finais (faça sempre!)
1. Soma dos payloads: 5 × 480 + 100 = **2.500** ✅
2. Offset do último × 8 + payload do último: 300 × 8 + 100 = **2.500** ✅
3. Cada fragmento ≤ MTU: 500 ≤ 500 ✅; 120 ≤ 500 ✅
4. Todos os payloads (exceto o último) múltiplos de 8: 480 ÷ 8 = 60 ✅

### 💡 Observações interessantes
- **Protocolo 17 = UDP:** o segmento de 2.500 bytes inclui o cabeçalho UDP de 8 bytes. Esse cabeçalho UDP ficará **só no fragmento 1** (é parte do payload IP). Os demais fragmentos carregam apenas dados "soltos". Só depois da remontagem o UDP é lido.
- **NAT (📎 complementar):** a origem é um IP privado (192.168.0.2). Na vida real, ao sair para a Internet, o roteador de borda faria NAT e trocaria a origem por 201.15.0.10. A questão pede só o processo de **fragmentação** e diz que a justificativa deve ser dada aos campos que mudam **por causa dele**. Mantenha a origem 192.168.0.2 e, se quiser, escreva uma linha dizendo que "a eventual tradução NAT está fora do escopo desta questão".

## 3.8 Exercícios progressivos de fragmentação

> Em todos: **cabeçalho de 20 bytes (HLEN = 5)** e **DF = 0**, salvo indicação contrária. Mostre: payload, máximo por fragmento, N, comprimento total, flags (MF), offset.

**Ex. 3.1 (nível 1 — warm-up).** Datagrama de comprimento total **1.220**, ID = 77, TTL = 10. MTU = **620**.

**Ex. 3.2 (nível 2 — último fragmento menor).** Datagrama de comprimento total **4.020**, ID = 900, TTL = 64. MTU = **1.500**.

**Ex. 3.3 (nível 3 — MTU que exige ajuste para múltiplo de 8).** Datagrama de comprimento total **3.020**, ID = 5, TTL = 30. MTU = **1.000**.

**Ex. 3.4 (nível 4 — cabeçalho com opções).** Datagrama com **HLEN = 7**, comprimento total **1.428**, MTU = **580**. (Considere que as opções são copiadas para todos os fragmentos.)

**Ex. 3.5 (nível 5 — "fragmento de fragmento").** Um fragmento chega a um novo roteador com: total = **500**, ID = 321, offset = **185**, **MF = 1**, TTL = 5. O próximo enlace tem MTU = **300**. Fragmente-o de novo.

**Ex. 3.6 (conceitual).** Um datagrama de 1.200 bytes com DF = 1 encontra um enlace de MTU 800. O que ocorre?

**Ex. 3.7 (remontagem).** O destino recebeu fragmentos do mesmo datagrama com: (offset 0, payload 1.480, MF 1), (offset 185, payload 1.480, MF 1), (offset 370, payload 1.040, MF 0). Qual era o tamanho do payload original? E o comprimento total do datagrama original (HLEN = 5)?

**Ex. 3.8 (erro comum — detecte).** Um aluno resolveu: "MTU = 500, payload = 2.500 → 2.500 ÷ 500 = 5 fragmentos". Aponte **todos** os erros.

---

## ✅ Gabarito comentado — Módulo 3

**3.1**
- Payload = 1.220 − 20 = **1.200**.
- Bruto = 620 − 20 = 600 → 600 ÷ 8 = 75 (exato) → **Máx. = 600**.
- N = 1.200 ÷ 600 = 2 (exato) → **2 fragmentos**.

| Frag | Payload | Total | Offset | MF |
|---|---|---|---|---|
| 1 | 600 | 620 | 0 | 1 |
| 2 | 600 | 620 | 600 ÷ 8 = **75** | 0 |

TTL = 9 nos dois; ID = 77 nos dois; checksum recalculado.
*Ponto de atenção:* quando a divisão é exata, **não** sobra "resto menor": o último também tem o tamanho máximo.

**3.2**
- Payload = 4.020 − 20 = **4.000**.
- Bruto = 1.480 → ÷ 8 = 185 (exato) → **Máx. = 1.480**.
- N = 4.000 ÷ 1.480 = 2,70 → **3 fragmentos**.
- Último = 4.000 − 2 × 1.480 = 4.000 − 2.960 = **1.040**.

| Frag | Payload | Total | Offset | MF |
|---|---|---|---|---|
| 1 | 1.480 | 1.500 | 0 | 1 |
| 2 | 1.480 | 1.500 | 185 | 1 |
| 3 | 1.040 | 1.060 | 370 | 0 |

Offsets: 0; 1.480 ÷ 8 = 185; 2.960 ÷ 8 = 370. TTL = 63. Verificação: 370 × 8 + 1.040 = 4.000 ✅.

**3.3**
- Payload = 3.020 − 20 = **3.000**.
- Bruto = 1.000 − 20 = 980 → 980 ÷ 8 = 122,5 → 122 × 8 = **976** (Máx.).
- N = 3.000 ÷ 976 = 3,07 → **4 fragmentos** (um 4º com poucos bytes!).
- Último = 3.000 − 3 × 976 = 3.000 − 2.928 = **72**.

| Frag | Payload | Total | Offset | MF |
|---|---|---|---|---|
| 1 | 976 | 996 | 0 | 1 |
| 2 | 976 | 996 | 122 | 1 |
| 3 | 976 | 996 | 244 | 1 |
| 4 | 72 | 92 | 366 | 0 |

TTL = 29. Verificação: 366 × 8 + 72 = 2.928 + 72 = 3.000 ✅.
*Erro comum:* usar 980 (não é múltiplo de 8) e obter offsets quebrados (122,5).

**3.4**
- Cabeçalho = 7 × 4 = **28**. Payload = 1.428 − 28 = **1.400**.
- Bruto = 580 − 28 = 552 → 552 ÷ 8 = 69 (exato) → **Máx. = 552**.
- N = 1.400 ÷ 552 = 2,54 → **3 fragmentos**. Último = 1.400 − 2 × 552 = 1.400 − 1.104 = **296**.

| Frag | Payload | Total | Offset | MF |
|---|---|---|---|---|
| 1 | 552 | 580 | 0 | 1 |
| 2 | 552 | 580 | 69 | 1 |
| 3 | 296 | 324 | 138 | 0 |

Verificação: 138 × 8 = 1.104; + 296 = 1.400 ✅. O **HLEN** continua 7 em todos (porque as opções foram copiadas).
*Lição:* o cabeçalho **não** é sempre 20; use HLEN × 4.

**3.5**
- Payload do fragmento = 500 − 20 = **480**.
- Bruto = 300 − 20 = 280 → 280 ÷ 8 = 35 (exato) → Máx. = **280**.
- N = 480 ÷ 280 = 1,71 → **2 sub-fragmentos**. Segundo: 480 − 280 = **200**.

| Sub-frag | Payload | Total | Offset | MF |
|---|---|---|---|---|
| A | 280 | 300 | **185** (herda o offset do fragmento!) | **1** |
| B | 200 | 220 | 185 + 280 ÷ 8 = 185 + 35 = **220** | **1** (o original tinha MF = 1: ainda há mais fragmentos depois) |

TTL = 4; ID = 321 (igual). Verificação: B termina em 220 × 8 + 200 = 1.960 = (185 × 8 = 1.480) + 480 ✅.
*Armadilha:* o offset começa no do fragmento recebido (**não** em 0), e o **MF do último sub-fragmento só é 0 se o fragmento original também tinha MF = 0**.

**3.6** O roteador **não pode fragmentar** (DF = 1). **Descarta** o datagrama e envia à origem uma mensagem **ICMP** "fragmentação necessária" (informando o MTU). A origem deve então reenviar com datagramas menores.

**3.7**
- Payload original = último offset × 8 + payload do último = 370 × 8 + 1.040 = 2.960 + 1.040 = **4.000 bytes**.
- Total original = 4.000 + 20 = **4.020 bytes**.

**3.8** Erros:
1. **Esqueceu o cabeçalho:** cada fragmento carrega 20 bytes de cabeçalho, então só cabem **480** de dados, não 500.
2. **Dividiu pelo MTU em vez do máximo de payload:** o correto é 2.500 ÷ 480.
3. **Não arredondou para cima:** 5,2 → **6** fragmentos.
4. Não verificou **múltiplo de 8**.
5. Esqueceu de calcular o último fragmento (100 bytes), **offsets**, flags, TTL e checksum.

---

# MÓDULO 4 — Transição IPv4/IPv6: tradução e tunelamento

## 4.1 Por que surgiu o IPv6?

🚚 **Analogia:** o IPv4 é como um sistema de CEP/telefone que **acabou os números**. O IPv6 é o novo sistema, com números de sobra.

**Motivo principal:** **esgotamento de endereços IPv4**. IPv4 tem 32 bits (~4,3 bilhões de endereços). IPv6 tem **128 bits** (~3,4 × 10³⁸).

## 4.2 Diferenças fundamentais IPv4 × IPv6 🧠

| Aspecto | IPv4 | IPv6 |
|---|---|---|
| Tamanho do endereço | 32 bits | **128 bits** |
| Notação | Decimal com pontos (192.168.0.2) | Hexadecimal com `:` (2001:db8::1) — você já domina |
| Cabeçalho | Variável (20+ bytes), com opções | **Fixo de 40 bytes**, mais simples |
| Checksum no cabeçalho | Sim | **Não** (removido) |
| Fragmentação | Roteadores **e** origem | **Só a origem** (roteadores não fragmentam) |
| Campos | Ver módulo 2 | Versão, Classe de tráfego, Rótulo de fluxo, Tamanho do payload, Próximo cabeçalho, Limite de saltos, Origem, Destino |
| TTL | TTL | **Hop Limit** (Limite de saltos) |
| Protocolo | Campo Protocolo | **Próximo Cabeçalho** |

**Ponto central:** os cabeçalhos são **diferentes e incompatíveis**: um nó só-IPv4 **não entende** um pacote IPv6, e vice-versa.

## 4.3 O problema da coexistência

A Internet não pode ser "desligada" e trocada de uma vez. Por muito tempo haverá:
- **Ilhas IPv6** cercadas de rede IPv4;
- **Hosts IPv4** querendo falar com **hosts IPv6**.

Dois grandes problemas:
1. **Pacotes IPv6 atravessando uma rede IPv4** (roteadores que só entendem IPv4 descartariam os pacotes IPv6).
2. **Comunicação entre um host só-IPv4 e um host só-IPv6.**

Duas técnicas principais: **tradução** e **tunelamento**.

## 4.4 Tradução (*translation*)

🚚 **Analogia:** um **intérprete** entre uma pessoa que só fala português (IPv4) e outra que só fala inglês (IPv6). Ele ouve uma frase, **traduz** e repete na outra língua.

**Como funciona:** um **tradutor** (um roteador/gateway com função de tradução, como o NAT64 ou o antigo NAT-PT) **recebe** um pacote IPv4, **converte o cabeçalho IPv4 em cabeçalho IPv6** (ou o inverso), e **encaminha**. O tradutor também precisa **mapear endereços** entre as duas famílias.

```
 Host IPv4 ──[pacote IPv4]──► [TRADUTOR] ──[pacote IPv6]──► Host IPv6
                               converte cabeçalho
                               e endereços
 Host IPv4 ◄─[pacote IPv4]── [TRADUTOR] ◄─[pacote IPv6]── Host IPv6
```

**Passos do tradutor (em linhas gerais):**
1. Recebe o pacote em um formato.
2. **Reescreve o cabeçalho** campo a campo para o outro formato (ex.: TTL ↔ limite de saltos; Protocolo ↔ Próximo cabeçalho).
3. **Converte endereços** (ex.: endereço IPv4 embutido em um prefixo IPv6; ou mapeamento dinâmico por tabela).
4. Reenvia.

### Problemas da tradução 🧠 (a prova pergunta!)

| # | Problema | Por que ocorre |
|---|---|---|
| 1 | **Campos sem equivalente** | Alguns campos existem num cabeçalho e não no outro (ex.: checksum IPv4, flags, ID/offset de fragmentação, rótulo de fluxo IPv6) → **perda de informação** ou conversão imperfeita |
| 2 | **Endereços incompatíveis (32 × 128 bits)** | Um endereço IPv6 não cabe em 32 bits: exige **mapeamento/tabela** e o tradutor mantém **estado** |
| 3 | **Fragmentação diferente** | IPv4 permite fragmentação por roteadores; IPv6 não → tratamento complexo |
| 4 | **Checksums de transporte (TCP/UDP)** | Calculados com um **pseudo-cabeçalho que inclui os IPs**; ao trocar endereços, o checksum de TCP/UDP **também precisa ser recalculado** |
| 5 | **Protocolos de aplicação que carregam IPs no conteúdo** | (ex.: FTP) Os endereços dentro dos dados **não são traduzidos**; exige tratadores especiais (ALGs) |
| 6 | **Gargalo e ponto único de falha** | Todo o tráfego passa pelo tradutor; ele guarda estado e **perde a comunicação fim a fim** |
| 7 | **Perde-se o "ponta a ponta"** | A origem não é mais vista como é; dificulta segurança fim a fim (ex.: IPsec) |

## 4.5 Tunelamento (*tunneling*) e encapsulamento

🚚 **Analogia:** você precisa enviar uma encomenda que só pode viajar por uma estrada que aceita apenas "caixas padrão azuis". Você coloca a encomenda **inteira, intacta, dentro de uma caixa azul**, envia pela estrada e, no final, **tira a caixa azul**. O conteúdo original nem foi mexido.

**Encapsulamento** = colocar um pacote **inteiro (cabeçalho + payload) como payload de outro pacote**, com novo cabeçalho por fora.

**Tunelamento** = usar encapsulamento para **atravessar uma rede** que não entende o pacote original. O "túnel" é o caminho virtual entre dois pontos extremos.

**Caso típico:** pacotes **IPv6** atravessando uma rede **IPv4**.

```
  Rede IPv6 A              REDE IPv4              Rede IPv6 B
  (Host A)                                         (Host B)
     │                                                │
     │  ┌──────────┐                    ┌──────────┐  │
     └─►│ Roteador │══ T Ú N E L ══════►│ Roteador │──┘
        │ de entrada│  (dentro do IPv4) │ de saída │
        └──────────┘                    └──────────┘

Pacote original (IPv6):          [ Cab IPv6 ][ Dados ]
Dentro do túnel (encapsulado):   [ Cab IPv4 ][ Cab IPv6 ][ Dados ]
                                  ↑ novo, endereça os extremos do túnel
                                  (campo Protocolo = 41, "IPv6 dentro de IPv4")
Na saída do túnel:               [ Cab IPv6 ][ Dados ]   (desencapsulado)
```

**Passo a passo:**
1. O host A (IPv6) envia o pacote ao **roteador de entrada** do túnel (dual stack: entende IPv4 e IPv6).
2. O roteador de entrada **encapsula**: coloca o pacote **IPv6 inteiro** dentro de um datagrama **IPv4**, cujo **destino é o roteador de saída** do túnel.
3. Os roteadores IPv4 no meio do caminho veem **apenas um datagrama IPv4 comum** e o encaminham normalmente.
4. O roteador de saída **desencapsula** (remove o cabeçalho IPv4) e entrega o **pacote IPv6 original, intacto**.

🧠 **Detalhe técnico:** no campo **Protocolo** do cabeçalho IPv4 externo coloca-se **41** (identifica "payload é IPv6").
📎 Em escala maior, o tunelamento também precisa considerar o **MTU** (o cabeçalho IPv4 extra de 20 bytes reduz o espaço útil).

## 4.6 Como o tunelamento resolve os problemas da tradução

| Problema da tradução | Como o tunelamento resolve |
|---|---|
| Campos sem equivalente / perda de informação | O pacote IPv6 vai **inteiro e inalterado** dentro do payload → **nada é convertido ou perdido** |
| Endereços de tamanhos diferentes | **Não há conversão de endereços**: os endereços IPv6 originais seguem intactos; só os extremos do túnel usam IPv4 |
| Checksums de TCP/UDP quebrados | Como os endereços IPv6 não mudam, os checksums de transporte **permanecem válidos** |
| Aplicações com IP no conteúdo | O conteúdo e os endereços internos **não são alterados** |
| Perda do fim a fim | O pacote original chega **idêntico** ao destino (preserva comunicação fim a fim e segurança) |
| Estado complexo no tradutor | Basta **encapsular/desencapsular** nos extremos (sem tabelas de mapeamento de endereços) |

## 4.7 Tradução × Tunelamento 🧠

| Critério | Tradução | Tunelamento |
|---|---|---|
| Ideia | **Converte** o pacote de um formato para o outro | **Embrulha** o pacote original dentro de outro |
| Pacote original | **Modificado** (cabeçalho reescrito) | **Preservado integralmente** |
| Permite IPv4 ↔ IPv6 (hosts de versões diferentes) conversarem? | **Sim**, é sua principal vantagem | **Não diretamente**: ambos os lados precisam falar a mesma versão; só **atravessa** uma rede de outra versão |
| Uso típico | Host só-IPv4 ↔ host só-IPv6 | **Ilhas IPv6** atravessando a Internet IPv4 |
| Vantagens | Interconecta hosts de versões diferentes | Sem perda de informação; simples; mantém ponta a ponta |
| Limitações | Perda de campos, estado, quebra do fim a fim, complexidade, gargalo | **Overhead** de 20 bytes extras (MTU/fragmentação); exige roteadores **dual stack** nos extremos; não resolve host IPv4 ↔ IPv6 |

## 4.8 ✍️ Resposta-modelo da Questão 3

> **Enunciado resumido:** considerando tradução e tunelamento na transição IPv4→IPv6, explique em linhas gerais como cada um funciona e informe quais problemas existem na tradução e como foram resolvidos no tunelamento.

**Resposta-modelo (nota máxima):**

O IPv4 e o IPv6 têm cabeçalhos e endereços incompatíveis (32 × 128 bits), e a transição entre eles será gradual, o que exige mecanismos de coexistência.

**Tradução.** Um dispositivo tradutor (gateway/roteador) recebe um pacote de uma versão, converte o cabeçalho para o formato da outra (por exemplo, TTL ↔ limite de saltos, Protocolo ↔ Próximo cabeçalho), mapeia os endereços e encaminha o pacote. Assim, um host somente IPv4 consegue se comunicar com outro somente IPv6.

**Problemas da tradução:** (i) alguns campos de um cabeçalho não possuem equivalente no outro (checksum, flags e campos de fragmentação, rótulo de fluxo), gerando perda de informação; (ii) a diferença de tamanho dos endereços exige mapeamento e manutenção de estado no tradutor; (iii) os checksums de TCP/UDP dependem dos endereços IP e precisam ser recalculados; (iv) aplicações que embutem endereços IP nos dados deixam de funcionar sem tratamento especial; (v) o tradutor vira gargalo/ponto único de falha e quebra a comunicação fim a fim.

**Tunelamento.** Consiste em **encapsular** o pacote IPv6 inteiro como payload de um datagrama IPv4, cujo destino é o roteador de saída do túnel. Os roteadores IPv4 intermediários só encaminham o datagrama IPv4 comum. No roteador de saída o pacote é **desencapsulado**, restaurando o pacote IPv6 original.

**Como o tunelamento resolveu os problemas:** como o pacote original não é convertido, mas apenas encapsulado, não há perda de campos nem necessidade de mapear endereços; os checksums e as aplicações continuam válidos; e a comunicação fim a fim é preservada. Como limitação, há overhead do cabeçalho adicional e o túnel só conecta redes de mesma versão através de uma rede de outra versão, não permitindo, por si só, a comunicação direta entre hosts IPv4 e IPv6.

## 4.9 Exercícios do Módulo 4

**Ex. 4.1 (fácil).** Por que surgiu o IPv6?

**Ex. 4.2 (fácil).** Cite três diferenças entre IPv4 e IPv6.

**Ex. 4.3 (conceitual).** Explique, com suas palavras, por que um roteador só-IPv4 não consegue encaminhar um pacote IPv6.

**Ex. 4.4 (conceitual).** Em um túnel IPv6 sobre IPv4, qual é o endereço de destino do cabeçalho IPv4 externo? E o do cabeçalho IPv6 interno?

**Ex. 4.5 (comparação).** Complete: na tradução o pacote original é __________; no tunelamento é __________.

**Ex. 4.6 (intermediário).** Dê **quatro** problemas da tradução e a explicação de cada.

**Ex. 4.7 (aplicação).** Duas filiais de uma empresa têm redes IPv6 e estão ligadas por uma Internet só-IPv4. Qual técnica usar? E se um computador só-IPv4 precisar acessar um servidor só-IPv6?

**Ex. 4.8 (discursiva, estilo prova).** Escreva em 10 a 12 linhas a resposta completa para: "explique como funciona o tunelamento e como ele resolve os problemas da tradução".

---

## ✅ Gabarito comentado — Módulo 4

**4.1** Esgotamento dos endereços IPv4 (32 bits). O IPv6 usa 128 bits, oferecendo uma quantidade praticamente ilimitada de endereços (além de cabeçalho simplificado e outras melhorias).

**4.2** Qualquer três: endereço 32 × 128 bits; cabeçalho variável × fixo de 40 bytes; checksum presente × removido; fragmentação feita por roteadores × só pela origem; TTL × limite de saltos; notação decimal × hexadecimal.

**4.3** O roteador IPv4 espera encontrar o cabeçalho no **formato IPv4** (versão 4, campos nas posições do IPv4). Ao receber um pacote em formato IPv6, não reconhece a estrutura e o **descarta**.

**4.4** Cabeçalho IPv4 externo: destino = **IPv4 do roteador de saída do túnel**. Cabeçalho IPv6 interno: destino = **IPv6 do host final**.

**4.5** Tradução: **modificado/convertido (cabeçalho reescrito)**. Tunelamento: **preservado integralmente (encapsulado)**.

**4.6** Ver tabela da seção 4.4 (campos sem equivalente; endereços de tamanhos diferentes; checksum de TCP/UDP; aplicações com IP nos dados; gargalo/ponto único; perda de fim a fim).

**4.7** Ilhas IPv6 sobre Internet IPv4 → **tunelamento** (IPv6 dentro de IPv4). Host só-IPv4 ↔ servidor só-IPv6 → **tradução** (o tunelamento não converte versões).

**4.8** Use como base a seção 4.8: (1) pacote IPv6 vai ao roteador de entrada; (2) é encapsulado em IPv4; (3) cruza a rede IPv4 como datagrama comum; (4) o roteador de saída desencapsula; (5) pacote original intacto → sem perda de campos, sem conversão de endereços, checksums válidos, ponta a ponta preservada.

---

# SIMULADO FINAL

> ⏱️ **Tempo:** 1h30. **Sem consulta** à apostila. Responda por escrito, como na prova. Só depois veja o gabarito.
> **Valor total:** 10 pontos.

**Nome:** ______________________  **Matrícula:** __________

---

**1. (Valor: 3,0 pontos)** Uma aplicação de videoconferência será executada em uma máquina e deseja que seus dados saiam da máquina o mais rápido possível, com o menor atraso. Ao escolher o protocolo da camada de transporte, a aplicação optou pelo UDP, pois, diferentemente do TCP, ele não mantém os dados retidos na camada de transporte. Nesses termos, **cite** quais aspectos do TCP fazem com que ele possa reter dados na camada de transporte e **explique** por que cada um desses aspectos pode causar tal retenção.

---

**2. (Valor: 4,0 pontos)** Um roteador de borda recebe um datagrama IPv4 com os campos de cabeçalho abaixo. O **MTU do enlace de saída é 820 bytes**. O datagrama carrega um segmento de **3.000 bytes**. Nesses termos, **informe todos os campos de todos os datagramas** que sairão do roteador de borda em razão do processo de fragmentação.

| Campo | Valor |
|---|---|
| Versão | 4 |
| HLEN | 5 |
| Tipo de Serviço | 0 |
| Comprimento do Datagrama | 3.020 |
| ID | 4.021 |
| Flags | 000 (DF = 0, MF = 0) |
| Deslocamento | 0 |
| TTL | 12 |
| Protocolo | 6 |
| Checksum | 0xFFFF (todos os bits em 1) |
| IP de Origem | 10.0.0.7 |
| IP de Destino | 172.16.4.9 |

> **Observação:** quero que o discente **informe os cálculos** do processo de fragmentação, bem como **apresente as devidas justificativas corretas** para os campos que sofreram mudança em relação ao datagrama original (para cada campo que tiver que mudar, informe o motivo). **Respostas não acompanhadas desses cálculos e justificativas serão desconsideradas.**

---

**3. (Valor: 3,0 pontos)** Considerando os processos de **tradução** e de **tunelamento** referentes à transição do IPv4 para o IPv6, explique em linhas gerais como cada um deles funciona e informe quais problemas existem no processo de tradução e como esses problemas foram resolvidos na técnica de tunelamento.

---

# GABARITO DO SIMULADO

> ⛔ **Só abra depois de fazer o simulado!** Atribua nota a você mesmo conforme os critérios.

## Questão 1 (3,0 pontos)

**Critérios:**
- **0,75** — ACK + retransmissão + buffer de envio (guarda cópia até confirmar).
- **0,75** — Números de sequência/ordenação + buffer de recepção (guarda fora de ordem).
- **0,75** — Controle de fluxo (janela de recepção; receptor lento/janela zero).
- **0,75** — Controle de congestionamento (janela de congestionamento; slow start).
- *(Pontos só são dados se houver **explicação**, não apenas o nome.)*

**Resposta de referência:** igual à da seção 1.11, adaptada à videoconferência, concluindo que o UDP envia os pacotes imediatamente e que, em vídeo ao vivo, a perda de alguns pacotes é preferível ao atraso causado por retransmissão e espera.

## Questão 2 (4,0 pontos)

**Cálculos:**
- Payload = 3.020 − 20 = **3.000**.
- Bruto = 820 − 20 = 800 → 800 ÷ 8 = 100 (exato) → **Máx. = 800**.
- N = 3.000 ÷ 800 = 3,75 → **4 fragmentos**.
- Payloads: 800, 800, 800 e 3.000 − 2.400 = **600**.
- Totais: 820, 820, 820 e 600 + 20 = **620**.
- Offsets: 0; 800 ÷ 8 = **100**; 1.600 ÷ 8 = **200**; 2.400 ÷ 8 = **300**.
- TTL: 12 − 1 = **11**.

| Campo | F1 | F2 | F3 | F4 |
|---|---|---|---|---|
| Versão | 4 | 4 | 4 | 4 |
| HLEN | 5 | 5 | 5 | 5 |
| Tipo de Serviço | 0 | 0 | 0 | 0 |
| **Comprimento total** | **820** | **820** | **820** | **620** |
| ID | 4.021 | 4.021 | 4.021 | 4.021 |
| **Flags** | **MF=1, DF=0** | MF=1, DF=0 | MF=1, DF=0 | **MF=0, DF=0** |
| **Deslocamento** | **0** | **100** | **200** | **300** |
| **TTL** | **11** | **11** | **11** | **11** |
| Protocolo | 6 | 6 | 6 | 6 |
| **Checksum** | recalculado (≠ 0xFFFF) | recalculado | recalculado | recalculado |
| Origem | 10.0.0.7 | idem | idem | idem |
| Destino | 172.16.4.9 | idem | idem | idem |

**Verificações:** 3 × 800 + 600 = 3.000 ✅; 300 × 8 + 600 = 3.000 ✅.

**Critérios de nota:**
- **1,5** — Cálculos corretos (payload, máximo múltiplo de 8, N, tamanhos).
- **1,0** — Offsets, flags, TTL corretos.
- **1,0** — Justificativas de **todos** os campos que mudaram (comprimento, flags, offset, TTL, checksum) e dos que não mudaram (ID etc.).
- **0,5** — Tabela completa com todos os campos de todos os fragmentos.

## Questão 3 (3,0 pontos)

**Critérios:**
- **0,75** — Tradução: converte cabeçalhos/endereços entre IPv4 e IPv6 em um tradutor.
- **0,75** — Tunelamento: encapsula o pacote IPv6 no payload de um IPv4; desencapsula na saída.
- **0,75** — Problemas da tradução (pelo menos 3 bem explicados: campos sem equivalente, endereços, checksums, aplicações, perda fim a fim).
- **0,75** — Como o tunelamento resolve (pacote preservado → sem conversão, sem perda, checksums válidos, fim a fim mantido).

**Resposta de referência:** seção 4.8.

## Como interpretar sua nota

| Nota | O que fazer |
|---|---|
| 9 – 10 | Pronto. Revise só a folha relâmpago |
| 7 – 8,9 | Refaça a parte em que perdeu pontos (use os exercícios do módulo correspondente) |
| Abaixo de 7 | Releia o módulo da questão com pior nota e refaça **um exercício novo** antes de encerrar o dia |

---

# FOLHA DE REVISÃO RELÂMPAGO

### Fragmentação (4 pts)
```
Payload  = Total − (HLEN×4)
Máx/frag = ⌊(MTU − Cab) ÷ 8⌋ × 8
N        = ⌈Payload ÷ Máx⌉
Último   = Payload − (N−1)×Máx
Total_i  = Payload_i + Cab
Offset_i = (payload acumulado antes) ÷ 8
MF = 1 exceto no último | DF = 0
ID igual | TTL − 1 | Checksum recalculado
Muda: Comprimento, Flags(MF), Offset, TTL, Checksum
Não muda: Versão, HLEN, TOS, ID, Protocolo, Origem, Destino
Verificação: offset_último × 8 + payload_último = Payload original
```

### Valores para lembrar
**Protocolo: 6 = TCP · 17 = UDP · 1 = ICMP · 41 = IPv6 encapsulado.** HLEN 5 = 20 bytes. UDP = cabeçalho 8 bytes.

### TCP × UDP (3 pts)
**Retenção no TCP:** ACK/retransmissão (buffer de envio) · números de sequência/ordenação (buffer de recepção) · controle de fluxo (janela do receptor) · controle de congestionamento (janela de congestionamento). **UDP:** nada disso, sai imediatamente.

### Tradução × Tunelamento (3 pts)
- **Tradução:** converte cabeçalhos e endereços. Problemas: campos sem equivalente, endereços 32/128 bits, checksums, aplicações com IP nos dados, gargalo, perda de fim a fim.
- **Tunelamento:** encapsula IPv6 dentro de IPv4 e desencapsula na saída. Resolve: pacote intacto, sem conversão, ponta a ponta preservada. Limitação: overhead; não conecta host IPv4 a host IPv6.

### Dicas para o dia da prova
1. Em fragmentação, **mostre todos os cálculos** (professor desconsidera respostas sem eles).
2. Para cada campo que mudou, **escreva o motivo** em uma frase.
3. Na Questão 1 e na 3, **cite e explique**: nome do mecanismo + por que ele retém/resolve.
4. Confira a figura: **MTU = 500**, **segmento 2.500**, **TTL = 7**, **ID = 1.350**.

---

