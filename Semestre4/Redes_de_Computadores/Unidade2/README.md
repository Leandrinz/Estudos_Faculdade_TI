# 📘 Camada de Rede — Guia de Estudo + Lista de Exercícios Resolvida

**Disciplina:** Redes de Computadores (UFERSA) · **Assunto:** Camada de Rede (IPv4, IPv6, NAT, Fragmentação, ICMP)

> **Como usar este guia:** cada bloco tem **(1) a teoria necessária**, **(2) as respostas da lista** já explicadas e **(3) macetes**. Como a prova não repete as questões da lista, **foque em entender o método**, não em decorar o resultado. No final há um *resumão* e um *treino extra* com números diferentes.

---

## 0. Ferramentas de cálculo (aprenda isto primeiro!)

### Potências de 2
| 2⁰ | 2¹ | 2² | 2³ | 2⁴ | 2⁵ | 2⁶ | 2⁷ | 2⁸ | 2⁹ | 2¹⁰ |
|---|---|---|---|---|---|---|---|---|---|---|
| 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128 | 256 | 512 | 1024 |

### Prefixo `/n` ↔ máscara ↔ tamanho do bloco (no último octeto)
| Prefixo | Máscara (último octeto) | Tamanho do bloco | Hosts úteis |
|---|---|---|---|
| /24 | 255.255.255.**0** | 256 | 254 |
| /25 | 255.255.255.**128** | 128 | 126 |
| /26 | 255.255.255.**192** | 64 | 62 |
| /27 | 255.255.255.**224** | 32 | 30 |
| /28 | 255.255.255.**240** | 16 | 14 |
| /29 | 255.255.255.**248** | 8 | 6 |
| /30 | 255.255.255.**252** | 4 | 2 |

### Fórmulas-chave
- **Nº de endereços do bloco** = 2^(32 − n)  (n = prefixo)
- **Hosts utilizáveis** = 2^(32 − n) − 2  (tira o endereço de rede e o de broadcast)
- **Endereço de rede** = IP **AND** máscara (zera os bits de host)
- **Broadcast** = rede com **todos os bits de host = 1** (= rede + tamanho do bloco − 1)
- **Tamanho do bloco** (no octeto "quebrado") = 256 − valor da máscara nesse octeto

> 💡 **Macete do "tamanho do bloco":** ache o octeto onde a máscara não é 255 nem 0. Calcule `256 − máscara`. A rede é o **maior múltiplo desse valor que seja ≤ ao octeto do IP**. O broadcast é o próximo múltiplo − 1.

---

# 🌐 QUESTÃO 1 — Endereçamento IP

## Teoria base: o que é um endereço IPv4?
É o "endereço postal" de uma **interface de rede** (não do equipamento inteiro). Tem **32 bits** e é dividido em duas partes:

```
|------- netid / prefixo (rede) -------|---- hostid (máquina) ----|
```
Todos os hosts da mesma sub-rede compartilham o **netid**; o **hostid** diferencia cada interface.

---

### 1a) Como um IPv4 é representado em notação decimal pontuada?

Os 32 bits são divididos em **4 grupos de 8 bits (octetos/bytes)**; cada octeto é convertido para decimal (0 a 255) e separado por **pontos**.

```
11000000 . 10101000 . 00000001 . 00000001
   192   .   168    .    1     .    1      →  192.168.1.1
```
Conversão rápida de um octeto: pesos `128 64 32 16 8 4 2 1`. Ex.: `11000000 = 128 + 64 = 192`.

---

### 1b) Máscara, endereço de rede e broadcast

| Item | O que é |
|---|---|
| **Máscara** | Número de 32 bits com **1s na parte de rede** e **0s na parte de host**. Diz quantos bits identificam a rede. Ex.: `255.255.255.0` = `/24`. |
| **Endereço de rede** | **Primeiro** endereço do bloco (bits de host todos **0**). **Identifica a sub-rede** em si; não pode ser atribuído a um host. Obtém-se por `IP AND máscara`. |
| **Endereço de broadcast** | **Último** endereço do bloco (bits de host todos **1**). Um pacote enviado a ele é entregue a **todos os hosts da sub-rede**. Também não é atribuído a host. |

Exemplo, bloco `192.168.1.0/24`: rede = `192.168.1.0`, broadcast = `192.168.1.255`, hosts = `.1` a `.254`.

---

### 1c) Classes do endereçamento classe cheia (classful) e como identificá-las

Antes do CIDR, a Internet dividia o espaço de endereços em **5 classes de blocos de tamanho fixo**. A classe é identificada pelos **primeiros bits** do endereço (ou, na prática, pelo **valor do 1º octeto**):

| Classe | Bits iniciais | 1º octeto | Máscara padrão | Rede / Host | Hosts por rede | Uso |
|---|---|---|---|---|---|---|
| **A** | `0` | 0 – 127 | 255.0.0.0 (/8) | 8 / 24 bits | 16.777.214 | Redes enormes |
| **B** | `10` | 128 – 191 | 255.255.0.0 (/16) | 16 / 16 bits | 65.534 | Redes médias/grandes |
| **C** | `110` | 192 – 223 | 255.255.255.0 (/24) | 24 / 8 bits | 254 | Redes pequenas |
| **D** | `1110` | 224 – 239 | — | — | — | **Multicast** |
| **E** | `1111` | 240 – 255 | — | — | — | Reservada (experimental) |

> 💡 **Como descobrir a classe:** olhe o 1º octeto. **0–127 → A · 128–191 → B · 192–223 → C · 224–239 → D · 240–255 → E.**
> Obs.: `127.x.x.x` é **loopback** (teste local) e `0.0.0.0` é endereço especial.

---

### 1d) Classe cheia × CIDR: como funcionam e onde há mais desperdício

**Classe cheia:** a organização só pode receber um bloco de **tamanho fixo** (A, B ou C). Se precisa de 300 hosts, a classe C (254) não basta → recebe uma **classe B (65.534 endereços)** e **desperdiça ~65.234**.

**CIDR (*Classless Inter-Domain Routing*):** **abole as classes**. O bloco pode ter **qualquer tamanho que seja potência de 2**, e o limite entre rede e host é indicado pelo prefixo: `a.b.c.d/n`. Para 300 hosts → `/23` (512 endereços, 510 úteis) → desperdício de só ~210.

**Regras de um bloco CIDR:**
1. Endereços **contíguos**;
2. Quantidade de endereços = **potência de 2**;
3. O **primeiro endereço** deve ser **divisível pelo tamanho do bloco**.

✅ **Resposta:** o **maior desperdício ocorre na classe cheia**, porque os blocos têm só três tamanhos possíveis (/8, /16, /24), muito distantes das necessidades reais. No CIDR, a granularidade é fina (/n qualquer), então o bloco se ajusta melhor à demanda. (CIDR também permite **agregação de rotas**, reduzindo as tabelas dos roteadores.)

---

### 1e) Classe, netid e hostid (classe cheia)

Método: 1º octeto → classe → quantos octetos são rede (A=1, B=2, C=3); o resto é host.

| Endereço | 1º octeto | Classe | netid | hostid |
|---|---|---|---|---|
| 238.34.2.1 | 238 (224–239) | **D** | *não possui* (multicast) | *não possui* |
| 129.14.6.8 | 129 (128–191) | **B** | 129.14 | 6.8 |
| 114.34.2.8 | 114 (0–127) | **A** | 114 | 34.2.8 |
| 132.56.8.6 | 132 (128–191) | **B** | 132.56 | 8.6 |
| 208.34.54.12 | 208 (192–223) | **C** | 208.34.54 | 12 |

> A classe D é de **multicast**: identifica um *grupo* de hosts, então não há divisão netid/hostid.

---

### 1f) Host `25.34.12.56/16` — rede e broadcast

- `/16` → máscara `255.255.0.0` → **os 2 primeiros octetos são rede**, os 2 últimos são host.
- **Rede** (zera host): `25.34.0.0`
- **Broadcast** (host tudo 1 = 255): `25.34.255.255`

**Resposta:** rede = **25.34.0.0** · broadcast = **25.34.255.255**.

---

### 1g) Host `182.44.82.16/26` — rede e broadcast

- `/26` → máscara `255.255.255.192`. O octeto "quebrado" é o 4º: bloco = 256 − 192 = **64**.
- Sub-redes possíveis no 4º octeto: `0, 64, 128, 192`. O valor **16** está entre 0 e 63 → rede começa em **0**.
- Em binário: `00010000 AND 11000000 = 00000000`. Broadcast: 6 bits de host em 1 → `00111111 = 63`.

**Resposta:** rede = **182.44.82.0** · broadcast = **182.44.82.63** (hosts úteis: .1 a .62).

---

### 1h) Bloco `211.17.180.0/24` dividido em 8 sub-redes de tamanho fixo

**Raciocínio:** 8 sub-redes = 2³ → precisamos **emprestar 3 bits** da parte de host. Novo prefixo: 24 + 3 = **/27**.

- **Máscara:** `255.255.255.224`
- **Endereços por sub-rede:** 2^(32−27) = 2⁵ = **32** (30 úteis)
- Sub-redes começam em múltiplos de 32: `.0, .32, .64, .96, .128, .160, .192, .224`

| Sub-rede | Primeiro endereço | Último endereço |
|---|---|---|
| **1ª** | 211.17.180.**0** | 211.17.180.**31** |
| **8ª (última)** | 211.17.180.**224** | 211.17.180.**255** |

> 💡 *Receita:* sub-redes desejadas = 2^k → soma **k** ao prefixo. Tamanho de cada uma = tamanho original ÷ nº de sub-redes (256 ÷ 8 = 32).

---

### 1i) ISP com `120.60.4.0/22` → 10 organizações com 80 endereços cada

**Passo 1 — Tamanho do bloco original:** /22 → 2¹⁰ = **1024 endereços** (de `120.60.4.0` até `120.60.7.255`).

**Passo 2 — Problema:** em CIDR, um bloco único tem tamanho **potência de 2**. 80 não é potência de 2; o menor bloco que comporta 80 é **128 (/25)**. Só que 10 × 128 = **1280 > 1024** → **não cabe**: só 8 organizações (8 × 128 = 1024) seriam atendidas com /25.

**Passo 3 — Solução que cabe:** como 80 = 64 + 16, cada organização recebe **dois sub-blocos: um /26 (64) + um /28 (16)** = exatamente 80 endereços. Total usado: 10 × 80 = **800**.

| Organização | Bloco /26 (64) | Bloco /28 (16) |
|---|---|---|
| 1 | 120.60.4.0/26 | 120.60.6.128/28 |
| 2 | 120.60.4.64/26 | 120.60.6.144/28 |
| 3 | 120.60.4.128/26 | 120.60.6.160/28 |
| 4 | 120.60.4.192/26 | 120.60.6.176/28 |
| 5 | 120.60.5.0/26 | 120.60.6.192/28 |
| 6 | 120.60.5.64/26 | 120.60.6.208/28 |
| 7 | 120.60.5.128/26 | 120.60.6.224/28 |
| 8 | 120.60.5.192/26 | 120.60.6.240/28 |
| 9 | 120.60.6.0/26 | 120.60.7.0/28 |
| 10 | 120.60.6.64/26 | 120.60.7.16/28 |

**Passo 4 — Sobra:** 1024 − 800 = **224 endereços** disponíveis (`120.60.7.32` até `120.60.7.255`).

> ⚠️ **Confirme com o professor** qual resposta ele espera aqui: se ele só quer a conta "1024 − 10×80 = **224**", é o mesmo resultado acima. Se ele esperar um único prefixo por organização, a conclusão correta é que **/25 não comporta 10 organizações**. Em prova, o método é: **arredondar a demanda para a potência de 2 acima → achar o prefixo → alocar do maior bloco para o menor, sempre alinhado**.

---

### 1j) Passo a passo do NAT e como ele ameniza o esgotamento de endereços

**Ideia:** a rede local usa endereços **privados** (`10.0.0.0/8`, `172.16.0.0/12`, `192.168.0.0/16`), que não são roteados na Internet. O roteador NAT tem **1 endereço público** e faz a "tradução" entre os dois mundos, usando uma **tabela de tradução (IP:porta)** — esta variante é chamada **NAPT/PAT**.

**Passo a passo (ex.: PC `192.168.0.10` acessa um servidor, NAT público = `200.10.20.30`):**

1. O host `192.168.0.10:3345` envia um datagrama ao servidor.
2. O roteador NAT **substitui o IP de origem** pelo público (`200.10.20.30`) e **a porta de origem** por uma nova, única (ex.: `5001`).
3. Ele **registra na tabela**: `192.168.0.10:3345 ⇄ 200.10.20.30:5001` e **recalcula os checksums** (IP e TCP/UDP).
4. O servidor responde para `200.10.20.30:5001`.
5. O NAT **consulta a tabela**, troca o destino de volta para `192.168.0.10:3345` e encaminha ao host.
6. A entrada é **removida** após fim da conexão/timeout.

**Como ajuda no esgotamento:** **centenas de hosts compartilham UM único IP público** (diferenciados pelas portas — ~65 mil portas, ou seja, dezenas de milhares de conexões simultâneas). Os mesmos blocos privados são **reutilizados** em milhões de redes. Resultado: **o consumo de endereços públicos cai drasticamente**, adiando o fim do IPv4.

---

### 1k) Os três principais aspectos contra o NAT

1. **Viola a arquitetura em camadas:** o roteador (camada 3) mexe em **números de porta** (camada 4). Portas deveriam endereçar *processos*, não *hosts*; e roteadores deveriam processar apenas até a camada de rede.
2. **Viola o argumento fim a fim / transparência:** hosts externos **não conseguem iniciar conexões** com hosts atrás do NAT (servidores, P2P, VoIP) sem *port forwarding* ou técnicas de travessia (NAT traversal). Também quebra protocolos que **embutem IPs no payload** (FTP, SIP) e complica o IPsec.
3. **É um "remendo" que adia o problema real:** a solução definitiva para a falta de endereços é o **IPv6**; o NAT prolonga a vida do IPv4 e atrasa a migração, além de adicionar complexidade e ponto de falha.

---

### 1l) Tamanho do IPv6 e representação amigável

- Um endereço IPv6 tem **128 bits** (2¹²⁸ endereços).
- Notação **hexadecimal com dois-pontos**: **8 grupos de 16 bits** (4 dígitos hex) separados por `:`.
  Ex.: `2340:1ABC:119A:A000:0000:0000:0000:0000`
- **Abreviação** (duas regras):
  1. **Zeros à esquerda** de cada grupo podem ser omitidos (`00AA → AA`; `0000 → 0`).
  2. **Uma única sequência** de grupos zerados consecutivos pode ser trocada por `::` (**só pode aparecer 1 vez** no endereço; se houver duas sequências, abrevia a **maior**).

---

### 1m) Forma abreviada

| Original | Abreviado |
|---|---|
| 2340:1ABC:119A:A000:0000:0000:0000:0000 | **2340:1ABC:119A:A000::** |
| 0000:00AA:0000:0000:0000:0000:119A:A231 | **0:AA::119A:A231** |
| 2340:0000:0000:0000:0000:119A:A001:0000 | **2340::119A:A001:0** |
| 0000:0000:0000:2340:0000:0000:0000:0000 | **0:0:0:2340::** |

> Atenção nas duas últimas: na 3ª, a sequência de 4 zeros vira `::` e o último `0000` vira só `0`. Na 4ª há duas sequências de zeros (3 grupos no início e 4 no fim) → `::` fica com a **maior** (a do fim).

---

### 1n) Forma original (expandida)

**Método:** conte os grupos escritos explicitamente; o `::` completa o que falta até **8 grupos**. Depois complete cada grupo com zeros à esquerda até 4 dígitos.

| Abreviado | Grupos explícitos | `::` vale | Expandido |
|---|---|---|---|
| `0::0` | 2 | 6 grupos | 0000:0000:0000:0000:0000:0000:0000:0000 |
| `0:AA::0` | 3 | 5 grupos | 0000:00AA:0000:0000:0000:0000:0000:0000 |
| `0:1234::3` | 3 | 5 grupos | 0000:1234:0000:0000:0000:0000:0000:0003 |
| `123::1:2` | 3 | 5 grupos | 0123:0000:0000:0000:0000:0000:0001:0002 |

---

### 1o) Roteadores possuem endereços IP? Quantos?

**Sim.** O endereço IP está associado à **interface de rede**, não ao equipamento. Como o roteador **liga várias redes**, ele tem **uma interface (e um IP) para cada rede a que está conectado**. Logo, **o nº de endereços = nº de interfaces** (no mínimo 2, senão não seria roteador).

---

# 📦 QUESTÃO 2 — Protocolo IP (IPv4, fragmentação, IPv6)

## Teoria base 1: o cabeçalho IPv4

O datagrama IPv4 = **cabeçalho (20 a 60 bytes) + dados**.

| Campo | Bits | Para que serve |
|---|---|---|
| **Versão** | 4 | `4` para IPv4 |
| **HLEN** (IHL) | 4 | Tamanho do cabeçalho **em palavras de 4 bytes**. Mín. **5** (=20 B), máx. **15** (=60 B) |
| **ToS / DS** | 8 | Tipo de serviço / prioridade (QoS) |
| **Comprimento total** | 16 | Tamanho **cabeçalho + dados**, em bytes (máx. 65.535) |
| **Identificação (ID)** | 16 | Identifica o datagrama original; **todos os fragmentos têm o mesmo ID** |
| **Flags** | 3 | `reservado` · **DF** (*Don't Fragment*) · **MF** (*More Fragments*, o "bit M") |
| **Deslocamento (offset)** | 13 | Posição dos dados do fragmento no original, **em unidades de 8 bytes** |
| **TTL** | 8 | Nº máximo de saltos; **cada roteador decrementa 1**; ao chegar em 0 o pacote é descartado |
| **Protocolo** | 8 | Quem recebe a carga: **1=ICMP · 6=TCP · 17=UDP** |
| **Checksum** | 16 | Verificação **só do cabeçalho** |
| **IP origem / destino** | 32 + 32 | Endereços |
| **Opções** | 0–40 B | Raramente usado (padding até múltiplo de 4) |

### Fórmulas que você vai usar o tempo todo
```
Tamanho do cabeçalho (bytes) = HLEN × 4
Bytes de dados               = Comprimento total − (HLEN × 4)
Bytes de opções              = (HLEN × 4) − 20
Byte inicial dos dados       = Deslocamento × 8
```

## Teoria base 2: fragmentação

- Cada enlace tem uma **MTU** (maior quadro que ele aceita). Se o datagrama é maior que a MTU, o roteador **fragmenta**.
- Só os **dados** são divididos; o **cabeçalho é copiado** em cada fragmento (com ID igual).
- Os dados de cada fragmento (exceto o último) devem ser **múltiplos de 8 bytes**, pois o offset conta de 8 em 8.
- **MF = 1** em todos os fragmentos, **menos no último** (MF = 0).
- A **remontagem só ocorre no destino final** (não nos roteadores).

**Receita para fragmentar:**
1. Dados totais = tamanho do datagrama − 20.
2. Dados máx. por fragmento = maior múltiplo de 8 que seja ≤ (MTU − 20).
3. Nº de fragmentos = ⌈dados totais ÷ dados por fragmento⌉ (o último leva o resto).
4. Offset de cada fragmento = (bytes de dados antes dele) ÷ 8.

**Classificação pelo par (Offset, M):**

| Offset | M | Significado |
|---|---|---|
| 0 | 0 | **Datagrama completo** (não fragmentado) |
| 0 | 1 | **1º fragmento** |
| > 0 | 1 | **Fragmento intermediário** |
| > 0 | 0 | **Último fragmento** |

---

### 2a) Datagrama com cabeçalho `4500 0054 0003 5850 2006 0000 7C4E 0302 B40E 0F02`

Cada 4 dígitos hex = 2 bytes. Decodificando:

| Hex | Campo | Valor |
|---|---|---|
| `4` | Versão | IPv4 |
| `5` | HLEN | 5 → cabeçalho = 5×4 = **20 bytes** |
| `00` | ToS | 0 |
| `0054` | Comprimento total | 0x54 = **84 bytes** |
| `0003` | **Identificação** | **3** |
| `5850` | Flags + Offset | `0101 1000 0101 0000` → flags `010` (DF=1, MF=0); offset = `1 1000 0101 0000`₂ = **6224** |
| `20` | **TTL** | 0x20 = **32** |
| `06` | Protocolo | 6 = TCP |
| `0000` | Checksum | 0 |
| `7C4E0302` | Origem | 124.78.3.2 |
| `B40E0F02` | Destino | 180.14.15.2 |

- **Está fragmentado?** **Sim.** O **offset (6224) é diferente de zero** e **M = 0** → é o **último fragmento** de um datagrama fragmentado.
  *(Observação: a flag DF=1 é incoerente com um pacote fragmentado, pois DF proíbe fragmentar. Se o professor perguntar, cite os dois fatos: pela regra offset≠0 e M=0 é o último fragmento, mas DF=1 é uma peculiaridade desse exemplo.)*
- **Tamanho dos dados:** 84 − (5×4) = 84 − 20 = **64 bytes**.
- **Quantos roteadores ainda?** TTL = 0x20 = **32** → pode atravessar até **32 roteadores/saltos** (cada um decrementa 1; ao zerar, é descartado).
- **Identificação:** 0x0003 = **3**.

> 💡 *Macete de conversão hex→decimal:* `0x54 = 5×16 + 4 = 84`; `0x20 = 2×16 = 32`.

---

### 2b) HLEN = 7 → quantos bytes de opção?

Cabeçalho = 7 × 4 = **28 bytes**. Parte fixa = 20 bytes. Opções = 28 − 20 = **8 bytes**.

### 2c) Opções = 20 bytes → HLEN em binário?

Cabeçalho = 20 (fixo) + 20 (opções) = **40 bytes**. HLEN = 40 ÷ 4 = **10** = **`1010`** em binário (4 bits).

### 2d) HLEN pode ser menor que 5?

**Não.** O cabeçalho tem no mínimo **20 bytes** (campos fixos). Como HLEN = bytes ÷ 4, o mínimo é 20 ÷ 4 = **5**. Valores < 5 indicariam cabeçalho menor que o necessário (pacote inválido).

### 2e) 1.024 bytes de dados, sem opções: HLEN e comprimento total?

- **HLEN = 5** (cabeçalho de 20 bytes).
- **Comprimento total** = 20 + 1024 = **1044 bytes**.

### 2f) Comprimento total = 36 e HLEN = 5: quantos bytes de dados?

Cabeçalho = 5×4 = 20. Dados = 36 − 20 = **16 bytes**.

### 2g) Como a camada de rede de B sabe se entrega a TCP ou UDP?

Pelo campo **Protocolo** (8 bits) do cabeçalho IP: **6 = TCP**, **17 = UDP** (e 1 = ICMP). A origem preenche esse campo; o destino o lê e entrega a carga ao protocolo certo (isso é a **demultiplexação**).

---

### 2h) Datagrama de 2.400 bytes, MTU = 500, ID = 422

*Premissa (padrão do Kurose): os 2.400 bytes **incluem** o cabeçalho de 20 bytes.*

1. **Dados totais** = 2400 − 20 = **2380 bytes**.
2. **Dados por fragmento** = MTU − cabeçalho = 500 − 20 = 480 (e 480 ÷ 8 = 60 ✔ múltiplo de 8).
3. **Nº de fragmentos** = 2380 ÷ 480 = 4,96 → **5 fragmentos** (4 × 480 = 1920; o último leva 2380 − 1920 = **460**).

| Fragmento | Dados | Comprimento total | ID | MF | Offset |
|---|---|---|---|---|---|
| 1 | 480 | 500 | 422 | **1** | **0** |
| 2 | 480 | 500 | 422 | **1** | 480÷8 = **60** |
| 3 | 480 | 500 | 422 | **1** | 960÷8 = **120** |
| 4 | 480 | 500 | 422 | **1** | 1440÷8 = **180** |
| 5 | 460 | 480 | 422 | **0** | 1920÷8 = **240** |

*Justificativas:* **ID igual** (mesmo datagrama original) · **MF = 1** exceto no último · **DF = 0** (senão não poderia fragmentar) · **offset** = bytes anteriores ÷ 8.

### 2i) Mesmo problema com MTU = 700

- Dados totais = 2380. Dados por fragmento = 700 − 20 = **680** (680 ÷ 8 = 85 ✔).
- Nº de fragmentos = 2380 ÷ 680 = 3,5 → **4 fragmentos** (3 × 680 = 2040; último = 2380 − 2040 = **340**).

| Fragmento | Dados | Comprimento total | ID | MF | Offset |
|---|---|---|---|---|---|
| 1 | 680 | 700 | 422 | **1** | **0** |
| 2 | 680 | 700 | 422 | **1** | **85** |
| 3 | 680 | 700 | 422 | **1** | **170** |
| 4 | 340 | 360 | 422 | **0** | **255** |

*Conferência:* 2040 ÷ 8 = 255 ✔ · 680+680+680+340 = 2380 ✔.

---

### 2j) 100 datagramas oriundos de fragmentação; o primeiro tem ID 1024. Qual o ID do último?

**1024.** Todos os fragmentos de um mesmo datagrama original **carregam o mesmo ID** — é ele que permite ao destino saber quais fragmentos pertencem juntos na remontagem. (Quem os distingue entre si é o *offset*.)

### 2k) Fragmento com offset = 120: primeiro e último byte dos dados?

- **Primeiro byte** = 120 × 8 = **960**.
- **Último byte** = 960 + (nº de bytes de dados do fragmento) − 1, sendo bytes de dados = comprimento total − HLEN×4.
  Sem o comprimento total, não dá para fechar o número; o que se determina é o **primeiro byte (960)** e que o último é **960 + dados − 1**.
  *(Ex.: se o fragmento tem 400 bytes de dados → vai do byte 960 ao 1359.)*

---

### 2l) Offset = 0 e M = 0
**Datagrama completo (não fragmentado).** Começa no byte 0 (offset 0) e **não há mais fragmentos** depois (M=0): ele é o datagrama inteiro.

### 2m) Offset = 0 e M = 1
**Fragmentado — 1º fragmento.** Começa no byte 0 (é o início do original) e **ainda há mais fragmentos** (M=1).

### 2n) Offset = 180 e M = 1
**Fragmentado — fragmento intermediário.** Offset ≠ 0 (há dados antes dele) e M = 1 (há dados depois).

### 2o) Offset = 180 e M = 0
**Fragmentado — último fragmento.** Offset ≠ 0 (há dados antes) e M = 0 (não há mais depois).

### 2p) Offset = 100: quantos bytes foram enviados antes?
100 × 8 = **800 bytes** de dados vieram antes dele.

### 2q) M = 0, HLEN = 5, comprimento total = 200, offset = 200

- Dados = 200 − (5×4) = 200 − 20 = **180 bytes**.
- **Primeiro byte** = 200 × 8 = **1600**.
- **Último byte** = 1600 + 180 − 1 = **1779**.
- Como M = 0 e offset ≠ 0, é o **último fragmento**.

---

### 2r) Campos do cabeçalho IPv4 que mudam de roteador em roteador

**Mudam SEMPRE (obrigatório):**
- **TTL** — cada roteador decrementa em 1.
- **Checksum** — como o cabeçalho mudou (o TTL), o checksum **precisa ser recalculado** em cada salto.

**Mudam ÀS VEZES (nem sempre):**
- **Comprimento total, Flags (MF) e Deslocamento** — só se o roteador **fragmentar** o datagrama (enlace de MTU menor). (O **ID não muda** na fragmentação.)
- **Opções** (e consequentemente **HLEN** e comprimento total) — algumas opções (ex.: *record route*, *timestamp*) são alteradas pelos roteadores.
- **ToS/DS** — pode ser remarcado/reclassificado por política de QoS.
- **Endereços** — só se houver **NAT** no caminho.

**Nunca mudam:** Versão, ID, Protocolo (e origem/destino, salvo NAT).

---

### 2s) IPv4 × IPv6: diferenças nos cabeçalhos

O cabeçalho IPv6 é **fixo de 40 bytes** (simplificado, para processamento mais rápido nos roteadores).

| IPv4 | IPv6 | O que mudou |
|---|---|---|
| Versão | Versão | igual (valor 6) |
| HLEN | — | **Removido**: cabeçalho de tamanho fixo (40 B) |
| ToS | **Classe de tráfego** | Renomeado (prioridade/QoS) |
| — | **Rótulo de fluxo (20 bits)** | **Novo**: identifica fluxos de pacotes (tratamento especial/QoS) |
| Comprimento total | **Comprimento da carga útil** | Agora conta **só os dados** (+ cabeçalhos de extensão), sem o cabeçalho base |
| ID, Flags, Offset | — | **Removidos** do cabeçalho base: a fragmentação foi para um **cabeçalho de extensão** e **só a origem fragmenta** |
| TTL | **Limite de saltos (Hop Limit)** | Renomeado; mesma função |
| Protocolo | **Próximo cabeçalho** | Renomeado; indica o tipo do que vem a seguir (TCP, UDP, ou cabeçalho de extensão) |
| Checksum | — | **Removido** (ver 2u) |
| Opções | **Cabeçalhos de extensão** | Opções saíram do cabeçalho base e viraram extensões encadeadas por "Próximo cabeçalho" |
| Origem/Destino (32 b) | Origem/Destino (**128 b**) | Endereços maiores |

> 💡 **Resumão:** *removidos* = HLEN, ID, Flags, Offset, Checksum, Opções · *renomeados* = ToS→Classe de tráfego, Compr. total→Compr. da carga, TTL→Hop Limit, Protocolo→Próximo cabeçalho · *novo* = Rótulo de fluxo.

---

### 2t) Prioridade dos pacotes: IPv4 × IPv6

- **IPv4:** usa o campo **ToS (8 bits)**. Originalmente: 3 bits de *precedência* + bits de tipo de serviço (atraso, vazão, confiabilidade, custo). Hoje é reinterpretado pelo **DiffServ**: **6 bits DSCP** (classe de serviço) + **2 bits ECN** (notificação de congestionamento).
- **IPv6:** usa o campo **Classe de tráfego (8 bits)**, com o mesmo propósito/estrutura do DiffServ, **mais** o **Rótulo de fluxo (20 bits)**, que marca pacotes de um mesmo fluxo (ex.: vídeo em tempo real) para que os roteadores os tratem de forma uniforme/diferenciada.
  *(No Forouzan, o IPv6 é descrito com um campo **Prioridade de 4 bits**: valores **0–7** para tráfego com controle de congestionamento — o que pode ser atrasado — e **8–15** para tráfego sem controle, como áudio/vídeo em tempo real. Vale citar se o professor seguir esse livro.)*

---

### 2u) Por que o checksum foi eliminado do IPv6?

1. **Desempenho:** como o TTL/Hop Limit muda a cada roteador, o checksum precisaria ser **recalculado em todos os saltos** — custo desnecessário.
2. **Redundância:** a detecção de erros já é feita **na camada de enlace** (CRC do Ethernet etc.) e **na camada de transporte** (TCP e UDP têm checksum; no IPv6 o checksum do UDP passou a ser **obrigatório**, e cobre um pseudo-cabeçalho com os IPs).
3. Resultado: **roteadores mais rápidos** e cabeçalho mais simples.

---

### 2v) Transição IPv4 → IPv6: tradução × tunelamento

Como não dá para migrar a Internet inteira de uma vez, as duas versões **coexistirão por muito tempo**.

**Tradução (*header translation*, ex.: NAT64):**
Um *gateway* **converte o cabeçalho IPv6 em IPv4** (e vice-versa). Usada quando **o remetente usa IPv6 mas o destinatário só entende IPv4** (ou o inverso). O gateway mapeia campos (Hop Limit↔TTL, Próximo cabeçalho↔Protocolo etc.) e endereços (existem endereços IPv6 "mapeados" de IPv4).

**Tunelamento (*tunneling*):**
Quando **dois hosts IPv6 precisam se comunicar atravessando uma região IPv4**, o roteador de entrada **encapsula o pacote IPv6 inteiro dentro de um pacote IPv4** (campo Protocolo = 41). Na saída do "túnel", o outro roteador **desencapsula** e entrega o IPv6 original.

*(Existe ainda a **pilha dupla / dual stack**: o host roda IPv4 e IPv6 ao mesmo tempo.)*

**Qual é melhor?**
O **tunelamento** é melhor **quando ambas as pontas são IPv6**, porque **preserva o pacote IPv6 intacto** (nenhuma informação/recurso é perdido). A **tradução** é **com perdas** (campos exclusivos do IPv6, como o rótulo de fluxo, não têm equivalente no IPv4) e é mais complexa. Porém, a tradução é a **única opção quando uma das pontas só fala IPv4**. Ou seja: são **complementares**, mas o tunelamento é preferível sempre que possível.

---

# 🧠 RESUMÃO PARA REVISÃO (leia no dia anterior)

### Receitas de cálculo
| Pergunta | Como resolver |
|---|---|
| Rede/broadcast dado IP/n | Ache o octeto "quebrado" → bloco = 256 − máscara → rede = múltiplo ≤ IP → broadcast = próximo múltiplo − 1 |
| Quantos hosts úteis? | 2^(32−n) − 2 |
| Dividir em N sub-redes iguais | k = log₂N → novo prefixo = n + k |
| Prefixo p/ H hosts | menor *h* com 2^h − 2 ≥ H → prefixo = 32 − h |
| Classe do IP | 1º octeto: 0–127 A · 128–191 B · 192–223 C · 224–239 D · 240–255 E |
| Bytes de dados | comprimento total − HLEN×4 |
| Bytes de opção | HLEN×4 − 20 |
| Fragmentar | dados = total − 20 → dados/frag = múlt. de 8 ≤ MTU−20 → offset = bytes antes ÷ 8 |
| Hex → campos IPv4 | `4 5 00 | total | ID | flags+offset | TTL protocolo | checksum | origem | destino` |
| Abreviar IPv6 | tirar zeros à esquerda + `::` na **maior** sequência de grupos 0 (só 1 vez) |

### Decore
- **IPv4 = 32 bits · IPv6 = 128 bits (8 grupos de 16 bits)**
- **Protocolo:** 1 ICMP · 6 TCP · 17 UDP · **IPv6 em IPv4 (túnel): 41**
- **HLEN:** 5–15 (20–60 bytes) · **Offset** em unidades de **8 bytes** · **MTU Ethernet** = 1500
- **Fragmentos:** mesmo ID · MF=1 exceto o último · remontagem **só no destino**
- **TTL/Hop Limit** cai 1 a cada roteador → **checksum IPv4 muda a cada salto**
- **IPv6:** sem HLEN, sem checksum, sem fragmentação nos roteadores, com Rótulo de fluxo e cabeçalhos de extensão
- **Privados:** 10.0.0.0/8 · 172.16.0.0/12 · 192.168.0.0/16

### ⚠️ Pegadinhas comuns de prova
1. **Esquecer −2** nos hosts úteis (rede e broadcast não são atribuíveis).
2. **Esquecer que o offset é em unidades de 8 bytes** (multiplique/divida por 8).
3. **Confundir comprimento total** (cabeçalho + dados) **com tamanho dos dados**.
4. Datagrama "de 2400 bytes" **já inclui** o cabeçalho de 20 bytes → dados = 2380.
5. Dados dos fragmentos **não-finais** devem ser **múltiplo de 8**. Se (MTU − 20) não for múltiplo de 8, **arredonde para baixo** (ex.: MTU 1500 → 1480 ✔; MTU 1000 → 980 não serve, usa-se **976**).
6. Usar **`::` duas vezes** no IPv6 (proibido).
7. **Classe D e E** não têm netid/hostid.

---

# 🏋️ TREINO EXTRA (números diferentes, mesmo método)

**T1.** Rede e broadcast de `172.16.45.200/20`.
> `/20` → máscara `255.255.240.0`; octeto quebrado = 3º; bloco = 256−240 = **16**. 45 → múltiplo de 16 ≤ 45 = **32**. **Rede 172.16.32.0 · Broadcast 172.16.47.255** · hosts úteis = 2¹² − 2 = 4094.

**T2.** Rede e broadcast de `10.1.1.130/25`.
> Bloco = 128. 130 ≥ 128 → **Rede 10.1.1.128 · Broadcast 10.1.1.255** · 126 hosts úteis.

**T3.** Que prefixo atende 500 hosts?
> 2⁹ − 2 = 510 ≥ 500 → h = 9 → **/23**.

**T4.** Fragmente um datagrama de 4020 bytes (MTU 1500, ID 77).
> Dados = 4000. Por fragmento = 1480 (múltiplo de 8 ✔). 4000 ÷ 1480 → **3 fragmentos**: 1480, 1480, 1040.
> Offsets: **0, 185, 370** (2960÷8 = 370) · MF: **1, 1, 0** · Comprimentos totais: 1500, 1500, 1060 · ID = 77 em todos.

**T5.** HLEN = 9 e comprimento total = 300. Opções? Dados?
> Cabeçalho = 36 B → opções = 16 B · dados = 300 − 36 = **264 B**.

**T6.** Abrevie `2001:0DB8:0000:0000:0008:0800:200C:417A`.
> **2001:DB8::8:800:200C:417A**

**T7.** Dividir `192.168.10.0/24` em 4 sub-redes.
> 4 = 2² → **/26** · máscara 255.255.255.192 · 64 endereços cada: `.0/26`, `.64/26`, `.128/26`, `.192/26`.

**T8.** Pacote com offset = 50, M = 1, total = 1500, HLEN = 5.
> Intermediário · dados = 1480 · primeiro byte = 400 · último byte = 400 + 1480 − 1 = **1879**.

---

🍀 **Plano sugerido para a semana:** (dias 1–2) Seção 0 + Questão 1 até refazer **sem olhar** as contas de rede/broadcast/sub-redes · (dias 3–4) cabeçalho IPv4 + fragmentação + hex · (dia 5) IPv6, NAT, transição e ICMP (teoria) · (dia 6) Treino Extra + resumão · (dia 7) revisão leve.

**Boa prova! 🚀**