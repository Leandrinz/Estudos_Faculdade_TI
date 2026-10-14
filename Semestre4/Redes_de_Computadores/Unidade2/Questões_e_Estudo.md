# Camada de Rede — Anotações + Respostas da Lista

**Disciplina:** Redes de Computadores — UFERSA, Campus Pau dos Ferros
**Referências da lista:** Kurose & Ross (5ª ed.) e Forouzan (4ª ed.)

---

## Como usar este material

- Cada seção **ensina o conteúdo** (teoria, macetes, exemplos). Dentro dela, os blocos marcados com **✅ RESPOSTA — Questão X** são a resposta da lista, já contextualizada.
- Se o professor trocar os números na prova, o que importa é o **método** (sempre explicado antes da resposta).
- **Numeração:** o arquivo `.docx` perdeu a numeração ao ser lido, então numerei pela ordem em que as questões aparecem: **Questão 1 (itens a–o)** = endereçamento, NAT e IPv6; **Questão 2 (itens a–v)** = protocolo IP; **Questão 3 (itens a–e)** = ICMP. Isso bate com a referência "item h" que a própria lista faz.
- Há **dois pontos em que o enunciado é ambíguo** (Q1-i e Q2-a). Marquei com ⚠️. Vale confirmar com o professor.

### Índice rápido: onde está cada questão

| Questão | Assunto | Seção |
|---|---|---|
| 1a | Notação decimal pontuada | 1.1 |
| 1b | Máscara, endereço de rede, broadcast | 1.2 |
| 1c | Classes de endereço | 1.3 |
| 1d | Classe cheia vs. CIDR | 1.4 |
| 1e | Classe, netid, hostid | 1.3 |
| 1f, 1g | Rede e broadcast a partir de IP/prefixo | 1.5 |
| 1h | 8 sub-redes de tamanho fixo | 1.6 |
| 1i | ISP distribuindo blocos (VLSM) | 1.7 |
| 1j, 1k | NAT | 2 |
| 1l, 1m, 1n | IPv6: tamanho, abreviação, expansão | 3 |
| 1o | Roteadores têm IP? | 3.5 |
| 2a–2f | Cabeçalho IPv4 (HLEN, comprimento total, hexadecimal) | 4 |
| 2g | Como o IP sabe se entrega a TCP ou UDP | 4.3 |
| 2h–2q | Fragmentação | 5 |
| 2r | Campos que mudam a cada roteador | 4.4 |
| 2s, 2t, 2u | IPv4 vs. IPv6 (campos, prioridade, checksum) | 6 |
| 2v | Transição IPv4 → IPv6 | 6.4 |
| 3a–3e | ICMP | 7 |

---

## 0. Visão geral da camada de rede

**Função:** levar pacotes (chamados de **datagramas**) do host de origem até o host de destino, passando por vários enlaces e roteadores. Hosts e roteadores têm camada de rede.

**Duas funções principais:**

| Função | O que é | Escopo |
|---|---|---|
| **Encaminhamento (forwarding)** | Quando um pacote chega a uma interface do roteador, ele é jogado para a interface de saída certa, consultando a **tabela de encaminhamento** | Local, por pacote (nanossegundos) |
| **Roteamento (routing)** | Determina o **caminho** fim a fim e **monta as tabelas** (RIP, OSPF, BGP...) | Global, em rede inteira |

**Serviço do IP: melhor esforço (*best effort*).** Não garante entrega, ordem, atraso nem ausência de erros no payload. Cada datagrama é independente (sem conexão). Quem dá confiabilidade, se precisar, é o TCP.

**Componentes da camada de rede na Internet:**

1. **IP**: endereçamento, formato do datagrama, fragmentação, tratamento de pacotes.
2. **ICMP**: relatório de erros e diagnóstico (ping, traceroute).
3. **Protocolos de roteamento**: preenchem as tabelas.
4. **Técnicas auxiliares**: NAT, IPv6 e transição.

**Encapsulamento:** `Dados da aplicação → Segmento TCP/UDP → Datagrama IP → Quadro de enlace`.

---

## 1. Endereçamento IPv4

### 1.1 Notação decimal pontuada

Um endereço IPv4 tem **32 bits**. Para humanos, ele é dividido em **4 octetos (bytes)**, cada um escrito em decimal (0–255) e separado por pontos.

```
10101100 00010000 00001010 00000001   (binário, 32 bits)
   172  .  16    .   10   .    1      (decimal pontuada)
```

Total de endereços possíveis: 2³² ≈ **4,29 bilhões**. Esse número se mostrou pequeno demais, o que motivou CIDR, NAT e IPv6.

> ### ✅ RESPOSTA — Questão 1a
> Um endereço IPv4 (32 bits) é escrito em **notação decimal pontuada** dividindo-o em **quatro grupos de 8 bits (octetos)**, convertendo cada grupo para decimal (0 a 255) e separando-os por pontos. Exemplo: `10101100.00010000.00001010.00000001` → `172.16.10.1`.

**Conversão rápida binário ↔ decimal:** pesos de um octeto = `128 64 32 16 8 4 2 1`. Exemplo: `11000000` = 128 + 64 = **192**.

---

### 1.2 Máscara, endereço de rede e endereço de broadcast

Um endereço IP tem duas partes: **quem é a rede** (prefixo) e **quem é o host** dentro dela. A **máscara** diz onde está a divisão.

| Conceito | O que é | Como obter |
|---|---|---|
| **Máscara de sub-rede** | 32 bits: **1s** para a parte de rede, **0s** para a parte de host. Ex.: `255.255.255.0` = `/24` | Dada pelo prefixo `/n` (n bits em 1) |
| **Endereço de rede** | **Primeiro** endereço do bloco (todos os bits de host = **0**). Identifica a sub-rede. **Não** pode ser atribuído a um host | `IP AND máscara` |
| **Endereço de broadcast** | **Último** endereço do bloco (todos os bits de host = **1**). Pacotes enviados a ele chegam a **todos** os hosts da sub-rede. **Não** pode ser atribuído a um host | `rede OR (NOT máscara)` |
| **Endereços utilizáveis** | Todos entre os dois acima | **2^h − 2** (h = bits de host) |

**Exemplo:** `192.168.10.0/24` → máscara `255.255.255.0`; rede `192.168.10.0`; broadcast `192.168.10.255`; hosts de `.1` a `.254` (254 hosts).

> ### ✅ RESPOSTA — Questão 1b
> - **Máscara:** sequência de 32 bits com 1s na parte de rede/sub-rede e 0s na parte de host. Serve para separar o prefixo (identificação da rede) do sufixo (identificação do host).
> - **Endereço de rede:** o primeiro endereço do bloco (bits de host todos 0); identifica a sub-rede como um todo e não é atribuído a nenhuma interface.
> - **Endereço de broadcast:** o último endereço do bloco (bits de host todos 1); é o endereço usado para falar com todos os hosts da sub-rede de uma vez, e também não é atribuído a nenhuma interface.

**Endereços especiais para saber:**

| Endereço | Significado |
|---|---|
| `0.0.0.0` | "Este host" / rota padrão |
| `255.255.255.255` | Broadcast limitado (a própria rede local, nunca atravessa roteador) |
| `127.0.0.0/8` (ex.: `127.0.0.1`) | **Loopback**: o próprio host |
| `10.0.0.0/8`, `172.16.0.0/12` (172.16–172.31), `192.168.0.0/16` | **Privados** (RFC 1918), não roteáveis na Internet; usados com NAT |
| `169.254.0.0/16` | Link-local (auto-configuração quando não há DHCP) |

---

### 1.3 Endereçamento por classes (classe cheia / *classful*)

No modelo original, o tamanho da parte de rede era **fixo**, definido pelos **primeiros bits** do endereço. A divisão é entre **netid** (identifica a rede, igual para todas as interfaces da sub-rede) e **hostid** (identifica a interface dentro da rede).

| Classe | Bits iniciais | 1º octeto | Netid / Hostid | Máscara padrão | Nº de redes | Hosts por rede |
|---|---|---|---|---|---|---|
| **A** | `0` | 0–127 (úteis 1–126) | 8 / 24 | `255.0.0.0` (/8) | 126 | 16.777.214 |
| **B** | `10` | 128–191 | 16 / 16 | `255.255.0.0` (/16) | 16.384 | 65.534 |
| **C** | `110` | 192–223 | 24 / 8 | `255.255.255.0` (/24) | 2.097.152 | 254 |
| **D** | `1110` | 224–239 | — (**multicast**) | — | — | — |
| **E** | `1111` | 240–255 | — (**reservada/experimental**) | — | — | — |

**Macete:** olhe só o **primeiro octeto** → `1–126 = A`, `128–191 = B`, `192–223 = C`, `224–239 = D`, `240–255 = E`. (O 127 é loopback, não é rede de classe A utilizável.)

> ### ✅ RESPOSTA — Questão 1c
> Existem **5 classes: A, B, C, D e E**. A classe de um endereço é identificada pelos **bits mais significativos** do primeiro octeto (ou, equivalentemente, pelo valor decimal dele):
> - **A**: começa com `0` → 1º octeto de 0 a 127;
> - **B**: começa com `10` → 128 a 191;
> - **C**: começa com `110` → 192 a 223;
> - **D**: começa com `1110` → 224 a 239 (multicast);
> - **E**: começa com `1111` → 240 a 255 (reservada).
>
> Só A, B e C são usadas para endereçar hosts (unicast).

> ### ✅ RESPOSTA — Questão 1e
> Método: identificar a classe pelo 1º octeto → isso define quantos octetos são netid (A=1, B=2, C=3); o restante é hostid.
>
> | Endereço | 1º octeto | Classe | Netid | Hostid |
> |---|---|---|---|---|
> | `238.34.2.1` | 238 (224–239) | **D** (multicast) | **não possui** | **não possui** |
> | `129.14.6.8` | 129 (128–191) | **B** | `129.14` | `6.8` |
> | `114.34.2.8` | 114 (1–126) | **A** | `114` | `34.2.8` |
> | `132.56.8.6` | 132 (128–191) | **B** | `132.56` | `8.6` |
> | `208.34.54.12` | 208 (192–223) | **C** | `208.34.54` | `12` |
>
> **Justificativa do 238.34.2.1:** classe D é usada para **multicast**; o endereço inteiro identifica um *grupo*, não uma rede + host, portanto não há divisão netid/hostid.

---

### 1.4 Classe cheia × CIDR (atribuição de blocos e desperdício)

**Classe cheia:** a organização só pode receber blocos de **três tamanhos fixos**: classe A (~16,7 milhões de endereços), B (65.536) ou C (256). Não existe tamanho intermediário.

- Precisa de 300 endereços? A classe C (256) não basta → recebe uma **B (65.536)** e desperdiça ~65.000.
- Consequência: **grande desperdício** e esgotamento acelerado.

**CIDR (*Classless Inter-Domain Routing*):** o tamanho da parte de rede é **qualquer** valor de 0 a 32, escrito como `a.b.c.d/n`. O bloco tem **2^(32−n)** endereços.

**Regras de um bloco CIDR:**
1. Os endereços são **contíguos**.
2. O número de endereços é **potência de 2**.
3. O **primeiro endereço** é divisível pelo tamanho do bloco.

- Precisa de 300 endereços? → `/23` (512 endereços, 510 úteis) → desperdício de ~210.
- Vantagem extra: **agregação de rotas** (vários blocos contíguos viram um prefixo só, mantendo as tabelas de roteamento pequenas), com roteamento pelo **prefixo mais longo** (*longest prefix match*).

> ### ✅ RESPOSTA — Questão 1d
> - **Classe cheia:** a rede recebe um bloco de **tamanho fixo** determinado pela classe (A, B ou C); a divisão rede/host é sempre nos limites de 8, 16 ou 24 bits. A organização escolhe a menor classe que comporte suas necessidades.
> - **CIDR:** a divisão rede/host é **arbitrária** (`/n`), então o bloco atribuído é a menor potência de 2 que comporte o número de hosts necessários.
> - **Maior desperdício: na classe cheia**, porque os tamanhos são muito discretos e muito distantes entre si (256, 65.536, 16,7 milhões). Uma organização que precisa de, por exemplo, 300 endereços receberia um bloco classe B de 65.536 (desperdício de ~65 mil), enquanto no CIDR receberia um `/23` com 512 (desperdício de ~200).

---

### 1.5 Como achar endereço de rede e broadcast (método universal)

**Método rápido (octeto "interessante")**

1. Converta `/n` em máscara; o **octeto interessante** é o primeiro que não é 255 nem 0.
2. **Tamanho do bloco** = `256 − valor da máscara nesse octeto`.
3. **Rede** = maior múltiplo do tamanho do bloco que seja ≤ valor do octeto do IP (octetos anteriores se mantêm, posteriores viram 0).
4. **Broadcast** = rede + tamanho do bloco − 1 nesse octeto (octetos posteriores viram 255).

**Tabela de máscaras (decore):**

| Prefixo | Máscara | Endereços no bloco | Hosts úteis |
|---|---|---|---|
| /24 | 255.255.255.0 | 256 | 254 |
| /25 | 255.255.255.128 | 128 | 126 |
| /26 | 255.255.255.192 | 64 | 62 |
| /27 | 255.255.255.224 | 32 | 30 |
| /28 | 255.255.255.240 | 16 | 14 |
| /29 | 255.255.255.248 | 8 | 6 |
| /30 | 255.255.255.252 | 4 | 2 |
| /31 | 255.255.255.254 | 2 | (uso ponto a ponto) |
| /32 | 255.255.255.255 | 1 | host único |
| /23 | 255.255.254.0 | 512 | 510 |
| /22 | 255.255.252.0 | 1.024 | 1.022 |
| /21 | 255.255.248.0 | 2.048 | 2.046 |
| /20 | 255.255.240.0 | 4.096 | 4.094 |
| /16 | 255.255.0.0 | 65.536 | 65.534 |
| /8 | 255.0.0.0 | 16.777.216 | 16.777.214 |

> ### ✅ RESPOSTA — Questão 1f: `25.34.12.56/16`
> - Prefixo /16 → máscara `255.255.0.0`: os **2 primeiros octetos** (`25.34`) são rede e os 2 últimos são host.
> - **Endereço de rede** (host tudo 0): **`25.34.0.0`**
> - **Endereço de broadcast** (host tudo 1): **`25.34.255.255`**
> - Faixa utilizável: `25.34.0.1` a `25.34.255.254` (65.534 hosts).
>
> *Observação:* o endereço seria classe A (25), mas o `/16` (CIDR) **sobrepõe** a classe, pois o prefixo informado é quem manda.

> ### ✅ RESPOSTA — Questão 1g: `182.44.82.16/26`
> - /26 → máscara `255.255.255.192`. Octeto interessante: o **4º** (192). Tamanho do bloco = 256 − 192 = **64**. As sub-redes começam em 0, 64, 128, 192.
> - O 4º octeto do IP é 16 → está no bloco que começa em **0** (0–63).
> - Em binário: `16 = 00010000`; máscara `11000000`; AND → `00000000 = 0`.
> - **Endereço de rede: `182.44.82.0`**
> - **Endereço de broadcast: `182.44.82.63`** (host tudo 1: `00111111 = 63`)
> - Hosts úteis: `182.44.82.1` a `182.44.82.62` (62 hosts).

---

### 1.6 Sub-redes de tamanho fixo (*subnetting*)

**Ideia:** pegar um bloco e "emprestar" bits da parte de host para criar mais sub-redes, todas do **mesmo tamanho**.

**Passos:**
1. Número de sub-redes desejado: `N` → bits emprestados `b` com **2^b ≥ N**.
2. Novo prefixo = prefixo original + `b`.
3. Cada sub-rede tem `2^(32 − novo prefixo)` endereços.
4. Sub-redes consecutivas: some o tamanho do bloco ao endereço inicial.

> ### ✅ RESPOSTA — Questão 1h: `211.17.180.0/24` → 8 sub-redes
> - 8 sub-redes → 2^b ≥ 8 → **b = 3 bits** emprestados.
> - Novo prefixo: 24 + 3 = **/27** → **máscara `255.255.255.224`**.
> - Endereços por sub-rede: 2^(32−27) = 2⁵ = **32 endereços** (30 utilizáveis para hosts).
>
> | Sub-rede | Primeiro endereço (rede) | Último endereço (broadcast) |
> |---|---|---|
> | 1ª | **211.17.180.0/27** | **211.17.180.31** |
> | 2ª | 211.17.180.32/27 | 211.17.180.63 |
> | 3ª | 211.17.180.64/27 | 211.17.180.95 |
> | 4ª | 211.17.180.96/27 | 211.17.180.127 |
> | 5ª | 211.17.180.128/27 | 211.17.180.159 |
> | 6ª | 211.17.180.160/27 | 211.17.180.191 |
> | 7ª | 211.17.180.192/27 | 211.17.180.223 |
> | 8ª (última) | 211.17.180.224/27 | **211.17.180.255** |
>
> - **Primeira sub-rede:** primeiro endereço `211.17.180.0`, último `211.17.180.31`.
> - **Última sub-rede:** primeiro endereço `211.17.180.224`, último `211.17.180.255`.
> - (Se pedirem só os endereços *utilizáveis*: 1ª = `.1` a `.30`; 8ª = `.225` a `.254`.)

---

### 1.7 Blocos de tamanhos diferentes (VLSM) — questão do ISP

**VLSM** (*Variable Length Subnet Mask*): dividir um bloco em sub-blocos de **tamanhos diferentes**, conforme a demanda de cada cliente.

**Regras de ouro:**
- Cada sub-bloco é potência de 2 → quantidade pedida é **arredondada para cima** até a próxima potência de 2.
- Aloque do **maior para o menor** (evita desalinhamento).
- Cada sub-bloco deve começar num endereço **divisível pelo seu tamanho**.
- Os blocos não podem se sobrepor.

**O bloco recebido:** `120.60.4.0/22` → 2^(32−22) = **1.024 endereços**, indo de `120.60.4.0` a `120.60.7.255`.

> ### ✅ RESPOSTA — Questão 1i ⚠️ (enunciado com um problema; leia as duas leituras)
>
> **O problema:** 80 **não** é potência de 2. Se cada organização recebesse **um único** bloco CIDR, o menor bloco que comporta 80 endereços seria **128 = `/25`**. Mas 10 × 128 = **1.280 > 1.024**, então **não cabe** no bloco `/22` do ISP.
>
> #### Leitura A — cada organização recebe exatamente 80 endereços (dois sub-blocos: 64 + 16)
> 80 = 64 + 16 → um `/26` (64) + um `/28` (16) por organização. Total usado: 10 × 80 = **800** endereços.
>
> | Org. | Sub-bloco /26 (64) | Sub-bloco /28 (16) |
> |---|---|---|
> | 1 | 120.60.4.0/26 | 120.60.6.128/28 |
> | 2 | 120.60.4.64/26 | 120.60.6.144/28 |
> | 3 | 120.60.4.128/26 | 120.60.6.160/28 |
> | 4 | 120.60.4.192/26 | 120.60.6.176/28 |
> | 5 | 120.60.5.0/26 | 120.60.6.192/28 |
> | 6 | 120.60.5.64/26 | 120.60.6.208/28 |
> | 7 | 120.60.5.128/26 | 120.60.6.224/28 |
> | 8 | 120.60.5.192/26 | 120.60.6.240/28 |
> | 9 | 120.60.6.0/26 | 120.60.7.0/28 |
> | 10 | 120.60.6.64/26 | 120.60.7.16/28 |
>
> **Endereços ainda disponíveis: 1.024 − 800 = 224**, de `120.60.7.32` a `120.60.7.255` (equivalem aos blocos `120.60.7.32/27` + `120.60.7.64/26` + `120.60.7.128/25`).
>
> #### Leitura B — um único bloco CIDR por organização (/25 = 128 endereços)
> Cada organização receberia um `/25` (128 endereços, dos quais 80 são usados e 48 desperdiçados). Só cabem **8** organizações:
> `120.60.4.0/25`, `120.60.4.128/25`, `120.60.5.0/25`, `120.60.5.128/25`, `120.60.6.0/25`, `120.60.6.128/25`, `120.60.7.0/25`, `120.60.7.128/25`.
> Sobram **0** endereços e **2 organizações ficam sem bloco**: o ISP precisaria de um bloco maior (por exemplo, `/21`).
>
> **Para a prova:** apresente a Leitura A como resposta principal (sobram **224**) e cite a observação da Leitura B mostrando que entendeu a restrição de potência de 2. Se o professor tiver gabarito, confirme qual das duas leituras ele adotou.

---

## 2. NAT (*Network Address Translation*)

### 2.1 Por que existe

Os endereços IPv4 estão esgotados. A ideia do NAT: uma rede doméstica ou corporativa usa endereços **privados** (10.0.0.0/8, 172.16.0.0/12, 192.168.0.0/16), que só têm significado **dentro** da rede local, e **compartilha um (ou poucos) endereço(s) público(s)** para falar com a Internet. O **roteador NAT** faz a tradução na borda.

### 2.2 Funcionamento passo a passo (NAT com tradução de portas, NAPT/PAT)

**Exemplo (Kurose):** host `10.0.0.1` (porta 3345) acessa um servidor Web `128.119.40.186:80`. O roteador NAT tem IP público `138.76.29.7`.

1. **Saída:** o host envia o datagrama com origem `10.0.0.1:3345` e destino `128.119.40.186:80`.
2. **Tradução na saída:** o roteador NAT escolhe uma **nova porta de origem** (ex.: 5001) e **substitui** o IP e a porta de origem por `138.76.29.7:5001`. Recalcula os checksums do IP e do TCP/UDP.
3. **Tabela de tradução:** o NAT registra a correspondência:
   `138.76.29.7:5001 ⇄ 10.0.0.1:3345`
4. **Internet:** o servidor vê a requisição como vinda de `138.76.29.7:5001` e responde para esse endereço e porta.
5. **Entrada:** a resposta chega ao NAT com destino `138.76.29.7:5001`. O NAT **consulta a tabela** e reescreve o destino para `10.0.0.1:3345`, e encaminha para dentro da rede local.
6. **Remoção:** a entrada é removida da tabela quando a conexão termina ou expira por inatividade (timeout).

> ### ✅ RESPOSTA — Questão 1j
> O NAT funciona assim: (1) o host interno envia o pacote com seu IP privado e porta; (2) o roteador NAT substitui o IP de origem pelo seu IP público e a porta de origem por uma porta nova e única; (3) registra essa correspondência na **tabela de tradução NAT**; (4) o servidor responde ao IP público/porta nova; (5) ao receber a resposta, o NAT consulta a tabela, restaura o IP privado e a porta originais no destino e encaminha o pacote ao host interno.
>
> **Como alivia o esgotamento:** vários hosts internos (centenas ou milhares) **compartilham um único IP público**, distinguidos pelo número de **porta** (campo de 16 bits → dezenas de milhares de conexões simultâneas por IP). Além disso, os **endereços privados podem ser reutilizados** em qualquer número de redes diferentes. Resultado: cada organização/residência consome apenas um (ou poucos) endereço(s) público(s) em vez de um por dispositivo.

*Variações de NAT (para saber):* **estático** (um IP privado ↔ um IP público fixo), **dinâmico** (pool de IPs públicos), **PAT/NAPT** (um IP público compartilhado via portas, o mais comum).

### 2.3 Por que a comunidade de redes não gosta do NAT

> ### ✅ RESPOSTA — Questão 1k (os três principais aspectos)
> 1. **Mistura de camadas / uso indevido das portas:** números de porta servem para identificar **processos** (camada de transporte), mas o NAT os usa para identificar **hosts** na tradução.
> 2. **Roteadores deveriam trabalhar só até a camada 3:** o NAT precisa olhar e **alterar cabeçalhos da camada de transporte** (portas, checksum TCP/UDP) e até dados da aplicação (ex.: FTP, SIP), violando o modelo em camadas.
> 3. **Viola o argumento fim a fim:** os hosts atrás do NAT não têm endereço globalmente alcançável; ninguém de fora consegue iniciar conexão com eles. Isso **atrapalha servidores, aplicações P2P** e protocolos como IPsec, exigindo gambiarras (*NAT traversal*, redirecionamento de porta, UPnP).
>
> *Complemento:* a "solução correta" para a falta de endereços seria o **IPv6**; o NAT é um remendo que adia a migração.

---

## 3. IPv6: endereçamento

### 3.1 Tamanho e notação

- **128 bits** (4× o tamanho do IPv4) → 2¹²⁸ ≈ 3,4 × 10³⁸ endereços.
- Notação **hexadecimal com dois-pontos**: 8 grupos de 16 bits, cada um com 4 dígitos hexadecimais.
  `2340:1ABC:119A:A000:0000:0000:0000:0000`

**Regras de abreviação:**
1. **Zeros à esquerda** de cada grupo podem ser omitidos (`00AA` → `AA`; `0000` → `0`).
2. **Uma sequência de grupos consecutivos de zeros** pode ser trocada por `::`, mas **apenas UMA vez** no endereço (senão fica ambíguo). Se houver mais de uma sequência, comprime-se a **mais longa**.
3. Para expandir: conte os grupos explícitos; o `::` preenche com zeros o que falta para completar **8 grupos**.

> ### ✅ RESPOSTA — Questão 1l
> Um endereço IPv6 tem **128 bits**. Para ficar mais amigável, usa-se a **notação hexadecimal com dois-pontos** (8 grupos de 4 dígitos hexa), podendo ser **abreviada** omitindo zeros à esquerda de cada grupo e substituindo uma sequência de grupos zerados consecutivos por `::` (uma única vez por endereço).

### 3.2 Abreviações

> ### ✅ RESPOSTA — Questão 1m
>
> | Original | Abreviado |
> |---|---|
> | `2340:1ABC:119A:A000:0000:0000:0000:0000` | **`2340:1ABC:119A:A000::`** |
> | `0000:00AA:0000:0000:0000:0000:119A:A231` | **`0:AA::119A:A231`** |
> | `2340:0000:0000:0000:0000:119A:A001:0000` | **`2340::119A:A001:0`** |
> | `0000:0000:0000:2340:0000:0000:0000:0000` | **`0:0:0:2340::`** |
>
> **Justificativas:**
> - 1º: os 4 últimos grupos são zeros → `::` no fim.
> - 2º: o `00AA` perde os zeros à esquerda (`AA`); o primeiro `0000` vira `0`; os 4 grupos zerados do meio viram `::`.
> - 3º: há uma sequência de 4 zeros no meio (vira `::`) e um `0000` isolado no fim, que só vira `0` (não se usa `::` duas vezes).
> - 4º: há duas sequências de zeros (3 grupos no início e 4 no fim). Comprime-se a **mais longa** (a do final) e os três primeiros ficam `0:0:0`. (`::2340:0:0:0:0` também é válido, mas a forma recomendada comprime a maior sequência.)

### 3.3 Expansões

> ### ✅ RESPOSTA — Questão 1n
>
> | Abreviado | Expandido (8 grupos de 4 dígitos) | Raciocínio |
> |---|---|---|
> | `0::0` | **`0000:0000:0000:0000:0000:0000:0000:0000`** | 2 grupos explícitos, `::` completa com 6 grupos de zeros (é o endereço "não especificado" `::`) |
> | `0:AA::0` | **`0000:00AA:0000:0000:0000:0000:0000:0000`** | 3 explícitos (`0`, `AA`, `0`), `::` = 5 grupos de zeros |
> | `0:1234::3` | **`0000:1234:0000:0000:0000:0000:0000:0003`** | 3 explícitos (`0`, `1234`, `3`), `::` = 5 grupos de zeros |
> | `123::1:2` | **`0123:0000:0000:0000:0000:0000:0001:0002`** | 3 explícitos (`123`, `1`, `2`), `::` = 5 grupos de zeros; zeros à esquerda recompostos (`123` → `0123`) |

### 3.4 Tipos de endereço IPv6 (para saber)

| Tipo | O que é | Observação |
|---|---|---|
| **Unicast** | Identifica **uma** interface | Global (`2000::/3`), link-local (`fe80::/10`), local único (`fc00::/7`) |
| **Multicast** | Identifica um **grupo** de interfaces | Prefixo `ff00::/8` |
| **Anycast** | Atribuído a várias interfaces; o pacote vai à **mais próxima** | Mesmo formato do unicast |

- **Não existe broadcast no IPv6** (foi substituído por multicast).
- `::1` = loopback; `::` = endereço não especificado.

### 3.5 Roteadores têm endereço IP?

> ### ✅ RESPOSTA — Questão 1o
> **Sim.** Um roteador possui **um endereço IP para cada interface (cada enlace/rede a que está conectado)**. Como a função dele é interligar redes, ele tem **no mínimo dois** endereços (um em cada rede diferente), e na prática tantos quantas forem suas interfaces. Cada interface fica numa sub-rede diferente.

---

## 4. O protocolo IP (IPv4): cabeçalho

### 4.1 Formato do cabeçalho IPv4

```
 0       4       8              16      19                      31
 +-------+-------+---------------+-------+-----------------------+
 | Vers  | HLEN  | Tipo serviço  |        Comprimento total      |
 +-------+-------+---------------+-------+-----------------------+
 |        Identificação          | Flags |  Deslocamento (offset)|
 +---------------+---------------+-------+-----------------------+
 |  TTL          |  Protocolo    |   Checksum do cabeçalho       |
 +---------------+---------------+-------------------------------+
 |                 Endereço IP de origem (32 bits)               |
 +---------------------------------------------------------------+
 |                 Endereço IP de destino (32 bits)              |
 +---------------------------------------------------------------+
 |                  Opções (0 a 40 bytes) + padding              |
 +---------------------------------------------------------------+
```

| Campo | Bits | Função |
|---|---|---|
| **Versão** | 4 | `4` para IPv4 |
| **HLEN** (IHL) | 4 | Tamanho do cabeçalho em **palavras de 4 bytes** → bytes = HLEN × 4. **Mínimo 5** (20 bytes) e máximo 15 (60 bytes) |
| **Tipo de serviço** (ToS / DiffServ) | 8 | Prioridade/qualidade de serviço (ver 6.2) |
| **Comprimento total** | 16 | Cabeçalho + dados, **em bytes**. Máximo 65.535 |
| **Identificação** | 16 | Identifica o datagrama original; **todos os fragmentos de um datagrama carregam o mesmo ID** |
| **Flags** | 3 | `0 | DF | MF`: DF = *Don't Fragment*; MF = *More Fragments* (1 = há mais fragmentos depois deste) |
| **Deslocamento (offset)** | 13 | Posição dos dados do fragmento no datagrama original, em unidades de **8 bytes** |
| **TTL** | 8 | Tempo de vida: **decrementado em 1 a cada roteador**; chegou a 0 → descartado (e ICMP "tempo excedido") |
| **Protocolo** | 8 | Indica a que protocolo da camada superior entregar os dados: `6`=TCP, `17`=UDP, `1`=ICMP |
| **Checksum do cabeçalho** | 16 | Detecção de erro **só no cabeçalho** |
| **Origem / Destino** | 32+32 | Endereços IP |
| **Opções** | 0–40 B | Raras (registro de rota, timestamp, roteamento pela origem...) |

**Relações fundamentais (decore):**

```
Tamanho do cabeçalho (bytes) = HLEN × 4
Bytes de opções              = (HLEN × 4) − 20
Tamanho dos dados            = Comprimento total − (HLEN × 4)
Comprimento total            = Cabeçalho + Dados
```

### 4.2 Questões sobre HLEN e comprimento total

> ### ✅ RESPOSTA — Questão 2b (HLEN = 7)
> Cabeçalho = 7 × 4 = **28 bytes**. O cabeçalho básico (sem opções) tem 20 bytes, então as opções ocupam 28 − 20 = **8 bytes**.

> ### ✅ RESPOSTA — Questão 2c (Opção = 20 bytes)
> Cabeçalho = 20 (básico) + 20 (opções) = **40 bytes**. HLEN = 40 ÷ 4 = **10**, que em binário (4 bits) é **`1010`**.

> ### ✅ RESPOSTA — Questão 2d (HLEN pode ser menor que 5?)
> **Não.** O cabeçalho IPv4 tem **no mínimo 20 bytes** (campos obrigatórios), e HLEN mede em palavras de 4 bytes: 20 ÷ 4 = 5. Valores 0–4 significariam cabeçalho menor que o mínimo, o que é inválido (o datagrama seria considerado malformado e descartado).

> ### ✅ RESPOSTA — Questão 2e (1.024 bytes de dados, sem opções)
> - **HLEN = 5** (cabeçalho de 20 bytes, sem opções).
> - **Comprimento total = 20 + 1.024 = 1.044 bytes.**

> ### ✅ RESPOSTA — Questão 2f (comprimento total 36, HLEN 5)
> Cabeçalho = 5 × 4 = 20 bytes. Dados = 36 − 20 = **16 bytes**.

### 4.3 Como a camada de rede sabe se entrega ao TCP ou ao UDP?

O campo **Protocolo** do cabeçalho IP funciona como a "cola" entre a camada de rede e a de transporte, assim como o número de **porta** é a cola entre transporte e aplicação.

| Valor do campo Protocolo | Protocolo |
|---|---|
| 1 | ICMP |
| 6 | TCP |
| 17 | UDP |
| 41 | IPv6 encapsulado em IPv4 (tunelamento) |
| 89 | OSPF |

> ### ✅ RESPOSTA — Questão 2g
> Pelo campo **Protocolo** do cabeçalho IPv4. Quando A envia um segmento TCP encapsulado em um datagrama IP, o campo Protocolo é preenchido com **6** (TCP). Ao receber o datagrama, a camada de rede de B lê esse valor e entrega a carga útil ao **TCP** (se fosse 17, entregaria ao UDP). *(No IPv6, o campo equivalente é o **Next Header**.)*

### 4.4 Decodificando um cabeçalho em hexadecimal

**Técnica:** cada 4 dígitos hexa = 16 bits (2 bytes). Um cabeçalho de 20 bytes tem 10 palavras de 4 dígitos.

> ### ✅ RESPOSTA — Questão 2a
> Cabeçalho: `4500 0054 0003 5850 2006 0000 7C4E 0302 B40E 0F02`
>
> | Palavra | Valor | Interpretação |
> |---|---|---|
> | `4500` | `4` / `5` / `00` | Versão = 4 (IPv4); **HLEN = 5** (20 bytes); ToS = 0 |
> | `0054` | 0x0054 | **Comprimento total = 84 bytes** |
> | `0003` | 0x0003 | **Identificação = 3** |
> | `5850` | `0101 1000 0101 0000` | Flags = `010` → reservado 0, **DF = 1, MF = 0**; **Deslocamento = 0x1850 = 6.224** |
> | `2006` | `20` / `06` | **TTL = 0x20 = 32**; **Protocolo = 6 (TCP)** |
> | `0000` | — | Checksum = 0 (campo zerado no exemplo) |
> | `7C4E 0302` | — | Origem = **124.78.3.2** |
> | `B40E 0F02` | — | Destino = **180.14.15.2** |
>
> **Está fragmentado? (⚠️ ler a observação)** Regra: um datagrama é fragmento se **MF = 1 ou Deslocamento ≠ 0**. Aqui o deslocamento é **6.224 ≠ 0** e MF = 0, então, tecnicamente, é um **fragmento, o último** (os dados começam no byte 6.224 × 8 = 49.792 do datagrama original).
> **Observação:** DF = 1 ("não fragmentar") é contraditório com ser um fragmento; o cabeçalho do exercício tem essa inconsistência. Se na aula o professor usou "DF = 1 ⇒ não fragmentado", ele pode esperar essa resposta. Na prova, escreva a justificativa mostrando os dois campos (DF=1 e offset≠0) para garantir.
>
> **Tamanho dos dados:** comprimento total − cabeçalho = 84 − (5 × 4) = 84 − 20 = **64 bytes**.
>
> **Quantos roteadores a mais:** o **TTL = 32**; cada roteador decrementa 1 e descarta o pacote se o valor chegar a 0. Logo, o pacote ainda pode passar por **32 roteadores** (estritamente, o 32º o descartaria ao zerar o TTL, mas a resposta usual é 32).
>
> **Número de identificação:** **3**.

### 4.5 Campos que mudam de roteador em roteador

> ### ✅ RESPOSTA — Questão 2r
> **Mudam obrigatoriamente em todo roteador:**
> - **TTL**: decrementado em 1 a cada salto.
> - **Checksum do cabeçalho**: como o TTL mudou, o checksum precisa ser **recalculado** em todo roteador.
>
> **Nem sempre mudam (só em certas situações):**
> - **Comprimento total, Identificação, Flags (MF) e Deslocamento:** mudam quando o roteador **fragmenta** o datagrama porque o MTU do próximo enlace é menor.
> - **HLEN e Opções:** algumas opções (registro de rota, timestamp, roteamento pela origem) são alteradas pelos roteadores; as opções copiadas para os fragmentos também influenciam o HLEN.
> - **Tipo de serviço / DSCP:** o roteador pode remarcá-lo por política de QoS.
>
> **Não mudam no caminho:** versão, protocolo, endereço de origem e de destino (exceto se houver NAT, que não é função do roteador comum).

---

## 5. Fragmentação do IPv4

### 5.1 Conceitos

- Cada enlace tem um **MTU** (*Maximum Transmission Unit*): tamanho máximo do campo de dados do quadro (Ethernet: 1.500 bytes). Se o datagrama é maior que o MTU do próximo enlace, o roteador o **fragmenta** em datagramas menores.
- A **remontagem** é feita **somente no destino final**, não nos roteadores intermediários.
- **Cada fragmento é um datagrama IP completo**, com seu cabeçalho (copiado do original, com campos ajustados).

**Regras de ouro:**

1. O **MTU inclui o cabeçalho**: dados por fragmento ≤ MTU − 20 (sem opções).
2. O deslocamento é em **unidades de 8 bytes**; portanto os dados de **todos os fragmentos, exceto o último, devem ser múltiplos de 8 bytes**.
3. **ID** = igual em todos os fragmentos (é o do datagrama original).
4. **MF = 1** em todos, **menos o último** (MF = 0).
5. **Deslocamento** = (bytes de dados que vieram antes) ÷ 8.
6. **Comprimento total** de cada fragmento = dados do fragmento + cabeçalho.
7. Se **DF = 1** e o datagrama não couber, o roteador **descarta** e envia ICMP "fragmentação necessária" (tipo 3, código 4), base do *Path MTU Discovery*.
8. Perdeu **um** fragmento → o destino não consegue remontar → o datagrama inteiro é perdido.

**Passos para resolver:**
1. Dados do datagrama = tamanho total − 20.
2. Máximo de dados por fragmento = maior múltiplo de 8 que seja ≤ MTU − 20.
3. Nº de fragmentos = ⌈dados ÷ máximo⌉.
4. Calcule o offset e o MF de cada um.

### 5.2 Fragmentação: MTU = 500 bytes (item h)

> ### ✅ RESPOSTA — Questão 2h (datagrama de 2.400 bytes, MTU = 500, ID = 422)
> - Cabeçalho = 20 bytes → **dados = 2.400 − 20 = 2.380 bytes**.
> - Máximo de dados por fragmento = 500 − 20 = 480 (e 480 ÷ 8 = 60 → é múltiplo de 8 ✔).
> - 2.380 ÷ 480 = 4,96 → **5 fragmentos** (4 com 480 bytes de dados + 1 com 460 bytes: 4 × 480 + 460 = 2.380 ✔).
>
> | Fragmento | Dados | Comp. total | ID | MF (flag) | Deslocamento | Justificativa do offset |
> |---|---|---|---|---|---|---|
> | 1 | 480 | 500 | 422 | 1 | **0** | começa no byte 0 |
> | 2 | 480 | 500 | 422 | 1 | **60** | 480 ÷ 8 |
> | 3 | 480 | 500 | 422 | 1 | **120** | 960 ÷ 8 |
> | 4 | 480 | 500 | 422 | 1 | **180** | 1.440 ÷ 8 |
> | 5 | 460 | 480 | 422 | **0** | **240** | 1.920 ÷ 8 (último fragmento) |
>
> **Justificativas:** o ID é o mesmo em todos (permite ao destino saber que pertencem ao mesmo datagrama); MF = 1 em todos exceto no último, que sinaliza "não vem mais nada depois"; o deslocamento indica a posição (em blocos de 8 bytes) dos dados do fragmento dentro dos dados originais.

### 5.3 Fragmentação: MTU = 700 bytes (item i)

> ### ✅ RESPOSTA — Questão 2i (datagrama de 2.400 bytes, MTU = 700, ID = 422)
> - Dados = 2.400 − 20 = **2.380 bytes**.
> - Máximo de dados por fragmento = 700 − 20 = 680 (680 ÷ 8 = 85 ✔).
> - 2.380 ÷ 680 = 3,5 → **4 fragmentos** (3 × 680 + 340 = 2.380 ✔).
>
> | Fragmento | Dados | Comp. total | ID | MF | Deslocamento |
> |---|---|---|---|---|---|
> | 1 | 680 | 700 | 422 | 1 | **0** |
> | 2 | 680 | 700 | 422 | 1 | **85** (680 ÷ 8) |
> | 3 | 680 | 700 | 422 | 1 | **170** (1.360 ÷ 8) |
> | 4 | 340 | 360 | 422 | **0** | **255** (2.040 ÷ 8) |

### 5.4 ID dos fragmentos

> ### ✅ RESPOSTA — Questão 2j
> **1024.** Todos os fragmentos de um mesmo datagrama original **carregam o mesmo número de identificação**, pois é por ele (junto com origem/destino/protocolo) que o destino sabe quais fragmentos pertencem ao mesmo datagrama durante a remontagem. Como os 100 datagramas são fragmentos resultantes da fragmentação, o último também tem ID = **1024**.

### 5.5 Offset e bytes de dados

**Fórmulas:**
```
Primeiro byte de dados do fragmento = Deslocamento × 8
Último byte de dados                = primeiro byte + (nº de bytes de dados do fragmento) − 1
Nº de bytes de dados                = Comprimento total − HLEN × 4
Bytes enviados antes do fragmento   = Deslocamento × 8
```

> ### ✅ RESPOSTA — Questão 2k (Deslocamento = 120)
> - **Primeiro byte** = 120 × 8 = **960**.
> - **Último byte** = 960 + (dados do fragmento) − 1, onde dados do fragmento = **comprimento total − (HLEN × 4)** (ambos lidos do cabeçalho).
> - *Exemplo:* se comprimento total = 1.500 e HLEN = 5 → dados = 1.480 → último byte = 960 + 1.480 − 1 = **2.439**.
> - *Dica:* se MF = 1, os dados do fragmento são múltiplo de 8, e o próximo fragmento terá offset = (último byte + 1) ÷ 8.

> ### ✅ RESPOSTA — Questão 2p (Deslocamento = 100)
> A origem já havia enviado **100 × 8 = 800 bytes** de dados antes dos dados deste fragmento (o deslocamento é medido em blocos de 8 bytes).

> ### ✅ RESPOSTA — Questão 2q (M = 0, HLEN = 5, comp. total = 200, deslocamento = 200)
> - Dados = 200 − (5 × 4) = 200 − 20 = **180 bytes**.
> - **Primeiro byte** = 200 × 8 = **1.600**.
> - **Último byte** = 1.600 + 180 − 1 = **1.779**.
> - Como M = 0 e o deslocamento ≠ 0, é o **último fragmento**, coerente com o resultado (o datagrama original tinha 1.780 bytes de dados).

### 5.6 Como classificar um datagrama pelo offset e M

| Deslocamento | Bit M (MF) | Situação |
|---|---|---|
| **0** | **0** | **Datagrama completo** (não fragmentado) |
| **0** | **1** | Fragmentado: **primeiro fragmento** |
| **> 0** | **1** | Fragmentado: **fragmento intermediário** |
| **> 0** | **0** | Fragmentado: **último fragmento** |

**Raciocínio:** offset 0 → os dados começam no início, então é o primeiro pedaço (ou o datagrama todo). M = 1 → ainda vem mais coisa depois. Logo, *offset 0 + M 0* = nada antes e nada depois = completo.

> ### ✅ RESPOSTA — Questão 2l (Offset = 0, M = 0)
> **Datagrama completo (não fragmentado).** Offset 0 → não há dados antes dele; M = 0 → não há dados depois. Ele carrega todos os dados do datagrama original.

> ### ✅ RESPOSTA — Questão 2m (Offset = 0, M = 1)
> **Fragmentado — é o primeiro fragmento.** Offset 0 → está no início dos dados; M = 1 → ainda vêm mais fragmentos.

> ### ✅ RESPOSTA — Questão 2n (Offset = 180, M = 1)
> **Fragmentado — é um fragmento intermediário.** Offset ≠ 0 → há fragmentos antes (não é o primeiro); M = 1 → há fragmentos depois (não é o último).

> ### ✅ RESPOSTA — Questão 2o (Offset = 180, M = 0)
> **Fragmentado — é o último fragmento.** Offset ≠ 0 → há dados antes; M = 0 → não há dados depois.

---

## 6. IPv6 vs. IPv4: cabeçalho, prioridade, checksum e transição

### 6.1 Cabeçalho IPv6 e comparação com o IPv4

**Cabeçalho base IPv6: 40 bytes, tamanho fixo.**

| Campo IPv6 | Bits | Função |
|---|---|---|
| Versão | 4 | `6` |
| Classe de tráfego (*Traffic Class*) | 8 | Prioridade/DiffServ (equivale ao ToS) |
| Rótulo de fluxo (*Flow Label*) | 20 | Identifica pacotes de um mesmo **fluxo** que merecem tratamento especial (ex.: tempo real) |
| Comprimento do payload | 16 | Tamanho dos dados **após** o cabeçalho base de 40 bytes |
| Próximo cabeçalho (*Next Header*) | 8 | Tipo do próximo cabeçalho (extensão ou TCP/UDP/ICMPv6) |
| Limite de saltos (*Hop Limit*) | 8 | Equivale ao TTL |
| Origem | 128 | |
| Destino | 128 | |

> ### ✅ RESPOSTA — Questão 2s
>
> | IPv4 | IPv6 | O que mudou |
> |---|---|---|
> | Versão | Versão | Mantido (valor 6) |
> | HLEN | — | **Eliminado**: cabeçalho base tem tamanho fixo (40 bytes) |
> | Tipo de serviço | Classe de tráfego | **Renomeado**, mesma função (QoS) |
> | — | **Rótulo de fluxo** | **Novo**: identificação de fluxos para tratamento especial |
> | Comprimento total | Comprimento do payload | Agora **não inclui** o cabeçalho base |
> | Identificação, Flags, Deslocamento | — | **Eliminados** do cabeçalho base: roteadores **não fragmentam** em IPv6; só a origem fragmenta (via *cabeçalho de extensão* de fragmentação). Se for grande demais, o roteador descarta e envia ICMPv6 "Pacote Muito Grande" |
> | TTL | Limite de saltos | **Renomeado**, mesma função |
> | Protocolo | Próximo cabeçalho | **Renomeado e generalizado** (aponta também para cabeçalhos de extensão) |
> | Checksum do cabeçalho | — | **Eliminado** (ver 2u) |
> | Origem / Destino (32 bits) | Origem / Destino (**128 bits**) | Endereços 4× maiores |
> | Opções | — | **Removidas** do cabeçalho base; passam a ser **cabeçalhos de extensão** (salto-a-salto, roteamento, fragmentação, destino, autenticação AH, ESP) |
>
> **Resumo:** o cabeçalho IPv6 é **mais simples e de tamanho fixo** (menos campos processados por roteador → encaminhamento mais rápido), os endereços são maiores e as funcionalidades menos usadas foram movidas para cabeçalhos de extensão opcionais.

### 6.2 Prioridade/precedência: IPv4 × IPv6

> ### ✅ RESPOSTA — Questão 2t
> **IPv4:** usa o campo **Tipo de Serviço (ToS, 8 bits)**. Na definição original: 3 bits de **precedência** (8 níveis de prioridade) + bits que pedem mínimo atraso, máxima vazão, máxima confiabilidade e mínimo custo. Hoje esse campo foi redefinido como **DSCP (6 bits, DiffServ) + ECN (2 bits)**, que classificam o pacote em classes de serviço.
>
> **IPv6:** usa o campo **Classe de Tráfego (8 bits, DiffServ)**, equivalente ao ToS/DSCP, e adiciona o **Rótulo de Fluxo (20 bits)**, que permite à origem marcar sequências de pacotes de um mesmo fluxo (ex.: voz e vídeo) para receberem tratamento especial uniforme dos roteadores. *(Na descrição do Forouzan (4ª ed.), o IPv6 tinha um campo **Prioridade de 4 bits**, e ele divide os valores em duas faixas: **0–7 para tráfego com controle de congestionamento** (ex.: 1 = dados de fundo/notícias, 2 = e-mail, 4 = transferência de arquivos tipo FTP, 6 = interativo/Telnet, 7 = controle/roteamento) e **8–15 para tráfego sem controle de congestionamento** (tempo real, onde perder pacote é preferível a atrasar, e quanto maior o valor, mais prioritário).)*

### 6.3 Por que o IPv6 não tem checksum

> ### ✅ RESPOSTA — Questão 2u
> O checksum foi eliminado por **eficiência e redundância**:
> 1. **Custo:** o campo Hop Limit muda em **todo roteador**, então o checksum teria de ser **recalculado a cada salto**, consumindo processamento e atrasando o encaminhamento.
> 2. **Redundância:** erros já são detectados em outras camadas: o **enlace** (CRC dos quadros) e o **transporte** (TCP e UDP têm checksum próprio; no IPv6 o checksum do UDP passou a ser **obrigatório**, e esses checksums cobrem um pseudo-cabeçalho com os endereços IP).
> 3. O IP já é serviço de melhor esforço e não garante integridade de qualquer forma, então manter o checksum não traz benefício proporcional ao custo.

### 6.4 Transição IPv4 → IPv6

Não dá para trocar a Internet toda de uma vez (não existe um "dia D"), então IPv4 e IPv6 coexistem por muito tempo. Técnicas: **pilha dupla** (dual stack), **tunelamento** e **tradução**.

**Tunelamento (*tunneling*)**
- Usado quando dois nós IPv6 precisam se comunicar passando por uma região de rede que só entende IPv4.
- O pacote IPv6 inteiro é **encapsulado como payload de um datagrama IPv4** (campo Protocolo = 41). Na entrada do túnel (roteador de borda) ocorre o encapsulamento; na saída do túnel, o desencapsulamento restaura o pacote IPv6 original.
- O pacote IPv6 **não é modificado** (nenhuma informação perdida).

**Tradução de cabeçalho (*header translation*)**
- Usada quando a **maior parte da Internet já é IPv6**, mas ainda há um nó que só fala **IPv4**, ou o contrário: a origem fala IPv6 e o destino **não entende IPv6**.
- Um **tradutor** converte o cabeçalho IPv6 em IPv4 (e vice-versa), mapeando endereços e campos equivalentes (ex.: Hop Limit ↔ TTL, Next Header ↔ Protocolo, recalcula o checksum). Campos sem correspondente (como Rótulo de Fluxo) são **perdidos**.

> ### ✅ RESPOSTA — Questão 2v
> - **Tunelamento:** o pacote IPv6 é **encapsulado dentro de um pacote IPv4** ao atravessar uma rede que só suporta IPv4; na saída, é desencapsulado e segue como IPv6.
> - **Tradução:** um dispositivo tradutor **converte o cabeçalho** IPv6 em IPv4 (e vice-versa) para permitir comunicação entre um nó IPv6 e um nó que só entende IPv4.
>
> **Qual é melhor?** Depende do cenário, mas, quando ambos os extremos usam IPv6, o **tunelamento é melhor**: preserva integralmente o pacote IPv6 (sem perda de campos como o rótulo de fluxo e os cabeçalhos de extensão) e não exige conversão de campos. A **tradução** só é indispensável quando um dos extremos é **apenas IPv4** e o outro **apenas IPv6** (porque o túnel não resolve isso), mas ela perde informação e é mais complexa. Por isso, a tradução é o último recurso e o tunelamento, a preferência.

---

## 7. ICMP (*Internet Control Message Protocol*)

### 7.1 Teoria

O IP é *best effort* e não tem mecanismo próprio de informar erros ou testar a rede. O **ICMP** serve para isso:

1. **Relatar erros** (mensagens de erro).
2. **Consultas** e diagnóstico (mensagens de consulta, como o `ping`).

**Particularidades:**
- Mensagens ICMP são **encapsuladas em datagramas IP** (campo Protocolo = **1**), mas o ICMP é considerado parte da camada de rede (fica "acima" do IP, só logicamente).
- Mensagens de erro são sempre enviadas **à origem** do datagrama problemático (o roteador só conhece o endereço de origem do pacote, e não os roteadores anteriores no caminho).
- ICMP **informa**, mas não **corrige** o problema.

**Formato:** Tipo (8 bits) + Código (8 bits) + Checksum (16 bits) + 4 bytes (dependem do tipo) = **cabeçalho de 8 bytes**; depois vêm os dados.

**Tipos importantes:**

| Tipo | Código | Mensagem | Quando |
|---|---|---|---|
| 8 / 0 | 0 | **Echo request / Echo reply** | `ping` |
| 3 | 0 / 1 / 2 / 3 / 4 | **Destino inalcançável** (rede / host / protocolo / **porta** / fragmentação necessária com DF=1) | Roteador ou host não consegue entregar |
| 11 | 0 / 1 | **Tempo excedido** (TTL chegou a 0 / tempo de remontagem esgotado) | Base do `traceroute` |
| 12 | — | **Problema de parâmetro** (cabeçalho inválido) | |
| 5 | — | **Redirecionamento** | Há uma rota melhor |
| 4 | 0 | Source quench (**obsoleto**) | Antigo controle de congestionamento |

**Ferramentas:**
- **ping:** envia *echo request* (8) e espera *echo reply* (0); mede RTT e alcançabilidade.
- **traceroute:** manda pacotes com TTL = 1, 2, 3... Cada roteador que zera o TTL devolve "tempo excedido", revelando o caminho; o destino final responde com "porta inalcançável" (UDP) ou echo reply.

**Quando o ICMP NÃO gera mensagem de erro:**
- Em resposta a **outra mensagem de erro ICMP** (evita laço infinito de erros).
- Para **fragmentos que não sejam o primeiro**.
- Para datagramas com destino **multicast ou broadcast**.
- Para endereços especiais (0.0.0.0, 127.x.x.x).

### 7.2 Respostas

> ### ✅ RESPOSTA — Questão 3a
> Olhando o campo **Protocolo** do cabeçalho IPv4: se o valor for **1**, o datagrama transporta uma mensagem **ICMP**.

> ### ✅ RESPOSTA — Questão 3b
> Incluir o **cabeçalho IP original + os 8 primeiros bytes dos dados** serve para que a origem consiga **identificar qual datagrama/aplicação causou o erro**:
> - O **cabeçalho IP** traz os endereços de origem e destino, o protocolo, o ID etc.; permite identificar de qual datagrama se trata.
> - Os **primeiros 8 bytes dos dados** correspondem ao início do cabeçalho da camada de transporte, que contém os **números de porta de origem e destino** (no UDP, é o cabeçalho inteiro; no TCP, as portas e o número de sequência). Com eles, o host sabe **qual processo/socket/conexão** deve ser notificado do erro.

> ### ✅ RESPOSTA — Questão 3c
> - **Tamanho mínimo:** **8 bytes** (apenas o cabeçalho ICMP: tipo, código, checksum e os 4 bytes restantes; sem dados, como pode ocorrer numa mensagem de consulta).
> - **Tamanho máximo:** o ICMP viaja num datagrama IPv4, cujo comprimento total é limitado a **65.535 bytes** (campo de 16 bits). Descontando o cabeçalho IP mínimo (20 bytes), o ICMP pode ter no máximo **65.515 bytes** (8 de cabeçalho + 65.507 de dados). Com um cabeçalho IP de 60 bytes (opções), seria 65.475.
> - *Observação:* as **mensagens de erro** são bem menores na prática: 8 (cabeçalho ICMP) + 20 a 60 (cabeçalho IP original) + 8 (primeiros bytes dos dados) = **36 a 76 bytes**. Já as mensagens de consulta (ex.: echo do `ping`) podem carregar dados adicionais.

> ### ✅ RESPOSTA — Questão 3d
> - **Tamanho mínimo do pacote IPv4 que transporta ICMP:** 20 (cabeçalho IP mínimo) + 8 (ICMP mínimo) = **28 bytes**.
> - **Tamanho máximo:** **65.535 bytes** (limite do campo Comprimento total do IPv4, que inclui o cabeçalho).

> ### ✅ RESPOSTA — Questão 3e
> Duas explicações possíveis:
> 1. **O datagrama foi descartado por congestionamento:** o buffer de um roteador estava cheio e o descartou. O IP é *best effort*, e roteadores **não são obrigados a avisar** a origem sobre descartes por congestionamento (o *source quench* está obsoleto).
> 2. **O datagrama foi descartado e a mensagem ICMP de erro gerada se perdeu no caminho de volta** (a própria mensagem ICMP é um datagrama IP e também pode ser perdida ou descartada).
>
> Outras explicações válidas: o descarte aconteceu em uma situação em que o ICMP **não gera erro** (por exemplo, o datagrama perdido era ele mesmo uma mensagem de erro ICMP, ou era um fragmento que não o primeiro, ou tinha destino multicast/broadcast); um **firewall** bloqueou/descartou o pacote silenciosamente e não retorna ICMP; o cabeçalho chegou **corrompido** (checksum inválido) e foi descartado sem informar a origem confiável (já que os endereços poderiam estar errados).

---

## 8. Complementos: pontos relacionados que podem aparecer na prova

*(Estes tópicos não estão na lista, mas completam a "Camada de Rede". Confirme com o professor se entram.)*

### 8.1 Encaminhamento e prefixo mais longo (liga com CIDR)

O roteador compara o endereço de destino com os prefixos da tabela de encaminhamento e escolhe a entrada que casa com o **prefixo mais longo** (maior `/n`).

| Prefixo | Interface |
|---|---|
| 200.23.16.0/21 | 0 |
| 200.23.24.0/24 | 1 |
| 0.0.0.0/0 (padrão) | 2 |

- O `/21` cobre `200.23.16.0` a `200.23.23.255`; o `/24` cobre `200.23.24.0` a `200.23.24.255`; o `0.0.0.0/0` casa com tudo.
- Destino `200.23.18.5` → casa com `/21` e com o padrão → vence o mais longo (`/21`) → **interface 0**.
- Destino `200.23.24.8` → casa com `/24` e com o padrão → vence `/24` → **interface 1**.
- Destino `8.8.8.8` → só casa com o padrão → **interface 2**.

### 8.2 Agregação de rotas (sumarização)

Blocos contíguos viram um prefixo só. Ex.: `200.1.0.0/24`, `200.1.1.0/24`, `200.1.2.0/24`, `200.1.3.0/24` → **`200.1.0.0/22`**. (Condição: contíguos, mesmo tamanho e o primeiro alinhado ao tamanho total.)

### 8.3 DHCP

Protocolo da camada de aplicação que **atribui automaticamente** IP, máscara, gateway e DNS a um host que entra na rede. Passos: *Discover → Offer → Request → Ack* (concessão de endereço por tempo limitado, *lease*).

### 8.4 Roteamento (visão geral)

- **Estado de enlace** (ex.: **OSPF**): cada roteador conhece a topologia inteira e calcula caminhos mínimos (Dijkstra).
- **Vetor de distâncias** (ex.: **RIP**): cada roteador conhece só seus vizinhos e troca tabelas de distância (Bellman-Ford).
- **Intra-AS:** RIP, OSPF. **Inter-AS:** **BGP** (política entre sistemas autônomos).

---

## 9. Resumo-relâmpago para revisar antes da prova

**Fórmulas:**
- Endereços no bloco = `2^(32 − n)`; hosts úteis = `2^(32 − n) − 2`.
- Sub-redes: `2^b ≥ N`; novo prefixo = antigo + `b`.
- Rede = IP AND máscara; broadcast = rede OR (NOT máscara).
- Tamanho do bloco (octeto interessante) = `256 − máscara`.
- Cabeçalho (bytes) = HLEN × 4; dados = comprimento total − HLEN × 4; opções = HLEN × 4 − 20.
- Fragmentação: dados/frag = maior múltiplo de 8 ≤ MTU − 20; offset = bytes anteriores ÷ 8; MF = 0 só no último; mesmo ID em todos.
- Primeiro byte = offset × 8; último = primeiro + dados − 1.

**Classes:** A `0` (1–126), B `10` (128–191), C `110` (192–223), D `1110` (224–239), E `1111` (240–255).

**Privados:** `10/8`, `172.16/12`, `192.168/16`.

**Protocolos (campo Protocolo):** ICMP=1, TCP=6, UDP=17, IPv6-em-IPv4=41.

**IPv6:** 128 bits; `::` só uma vez; cabeçalho 40 bytes fixo; sem checksum, sem HLEN, sem fragmentação por roteadores; ToS→Traffic Class, TTL→Hop Limit, Protocolo→Next Header; novo: Flow Label.

**Tabela offset/M:** `0/0` completo · `0/1` primeiro · `>0/1` intermediário · `>0/0` último.

**ICMP:** Protocolo=1; erro = cabeçalho IP original + 8 bytes de dados; mínimo 8 bytes; não gera erro sobre erro ICMP, nem para fragmentos não-iniciais, nem para multicast/broadcast.

**NAT:** tabela (IP privado, porta) ⇄ (IP público, porta nova); críticas: portas p/ hosts, roteador mexendo na camada 4, viola fim a fim (P2P/servidores); a solução real é IPv6.