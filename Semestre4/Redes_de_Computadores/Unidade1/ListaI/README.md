# Lista 01 — Introdução às Redes de Computadores (respostas para estudo)

---

## 1. Cinco componentes de um sistema de comunicação de dados

| Componente | Significado |
|---|---|
| **Mensagem** | A informação a ser comunicada (texto, números, imagem, áudio, vídeo). |
| **Emissor** | Dispositivo que envia a mensagem (computador, celular, câmera). |
| **Receptor** | Dispositivo que recebe a mensagem. |
| **Meio de transmissão** | Caminho físico por onde a mensagem viaja (cabo, fibra, ondas de rádio). |
| **Protocolo** | Conjunto de regras que emissor e receptor seguem para se entenderem. Sem protocolo, os dispositivos podem estar conectados, mas não se comunicam. |

---

## 2. Simplex × Half-duplex × Full-duplex

| Modo | Como funciona | Exemplo |
|---|---|---|
| **Simplex** | Só **um sentido**. Um lado sempre envia, o outro sempre recebe. | Teclado → computador, TV, rádio |
| **Half-duplex** | **Dois sentidos, mas não ao mesmo tempo** (um de cada vez). | Walkie-talkie |
| **Full-duplex** | **Dois sentidos ao mesmo tempo.** | Telefone, Ethernet moderna |

---

## 3. Ponto a ponto × Multiponto

**Ponto a ponto:** link **dedicado** entre dois dispositivos.
- ✅ Capacidade total só para os dois, mais privacidade/segurança, fácil isolar falhas.
- ❌ Caro: precisa de muitos enlaces/cabos para interligar vários dispositivos.

**Multiponto:** vários dispositivos **compartilham** o mesmo enlace.
- ✅ Mais barato, menos cabeamento, fácil adicionar dispositivos.
- ❌ Capacidade dividida entre todos, menos privacidade, precisa de controle de acesso ao meio (colisões), falha no meio afeta todos.

---

## 4. As quatro topologias básicas

**Malha (mesh), Estrela, Barramento (bus) e Anel (ring).**

| Critério | **Malha** | **Estrela** | **Barramento** | **Anel** |
|---|---|---|---|---|
| **Instalação** | Difícil e cara: n(n−1)/2 enlaces, n−1 portas por nó | Fácil: 1 enlace por nó até o hub/switch | Fácil e barata: 1 cabo só | Relativamente fácil: cada nó liga só a vizinhos |
| **Falha em enlace** | Muito robusta (há rotas alternativas) | Só isola o nó daquele enlace | **Ruim:** cabo principal rompido derruba a rede toda | **Ruim:** rompe o anel (a menos que seja anel duplo) |
| **Falha em nó** | Robusta | Nó comum: sem problema. **Hub/switch central: derruba tudo** | Robusta (nó não afeta o cabo) | **Ruim:** nó parado quebra o anel (a menos que haja bypass/anel duplo) |
| **Privacidade / segurança** | Alta (enlaces dedicados) | Boa com switch; baixa com hub (broadcast) | Baixa (todos "ouvem" tudo) | Baixa (quadro passa por todos os nós) |
| **Eficácia das transmissões** | Alta (sem disputa), mas custo alto | Boa (com switch) | Baixa com muita carga (colisões, meio compartilhado) | Boa/previsível (token), mas atraso cresce com nº de nós |

---

## 5. Topologias híbridas (desenhos)

**a) Backbone em estrela + 3 redes em anel**
```
   ┌──●──●──┐                      ┌──●──●──┐
   ●  anel  ●───┐              ┌───●  anel  ●
   └──●──●──┘   │              │   └──●──●──┘
                [  HUB / SWITCH  ]   ← backbone em estrela
                       │
                  ┌──●──●──┐
                  ●  anel  ●
                  └──●──●──┘
```

**b) Backbone em anel + 3 redes em barramento**
```
     ══●══●══ (barramento 1)
          │
       [ nó ]────────[ nó ]══●══●══ (barramento 2)
          │   backbone   │
          └─── [ nó ] ───┘        ← 3 nós formam o ANEL
                 │
            ══●══●══ (barramento 3)
```

**c) Backbone em barramento + 3 redes em estrela**
```
   [HUB1]     [HUB2]     [HUB3]
   /  |  \    /  |  \    /  |  \      ← 3 estrelas
   ●  ●  ●    ●  ●  ●    ●  ●  ●
     │           │          │
 ════╧═══════════╧══════════╧════════  ← backbone em barramento
```

---

## 6. O que determina se é LAN, MAN ou WAN?

Principalmente a **abrangência geográfica**, além de propriedade, tecnologia e velocidade:

| | Abrangência | Propriedade / características |
|---|---|---|
| **LAN** | Prédio, casa, campus (até poucos km) | Privada, alta velocidade, baixo atraso (Ethernet, Wi-Fi) |
| **MAN** | Cidade / região metropolitana (dezenas de km) | Pode ser privada ou de uma operadora |
| **WAN** | País, continente, mundo | Usa infraestrutura de operadoras (públicas/alugadas), usa comutação, velocidade/atraso maiores |

Fatores: **tamanho (alcance)**, **propriedade/administração**, **tecnologia e meio de transmissão**, **taxa de transmissão**, **número de hosts**.

---

## 7. Comutação de pacotes por datagramas

### a) Como funciona?
- A mensagem é **dividida em pacotes** (datagramas).
- Cada pacote leva o **endereço de destino** no cabeçalho e é tratado **de forma independente**.
- Cada roteador usa a **tabela de roteamento** para escolher o próximo salto (*store-and-forward*).
- **Não há conexão** nem reserva de recursos antes de enviar.

### b) A tabela pode ter duas entradas com o mesmo destino?
**Não** (regra geral). Cada destino deve ter **uma única** interface/próximo salto de saída. Duas entradas causariam ambiguidade: o roteador não saberia por onde encaminhar.

### c) O que causa entrega desordenada e/ou não garantida?
- Pacotes independentes podem seguir **rotas diferentes** com atrasos diferentes → **chegam fora de ordem**.
- A tabela de roteamento pode **mudar** no meio da transmissão.
- **Congestionamento** → filas dos roteadores enchem e pacotes são **descartados**.
- A rede é *best effort*: **não há confirmação nem retransmissão** na camada de rede.

### d) Por que é a mais usada nas WANs/Internet?
- **Sem estabelecimento de conexão** (menos atraso para mensagens curtas).
- Roteadores **não guardam estado por conexão** → escala melhor.
- **Robusta:** desvia de enlaces/roteadores com falha dinamicamente.
- **Uso eficiente dos enlaces** (multiplexação estatística).
- Simples e funciona sobre redes heterogêneas; a confiabilidade fica nas pontas (transporte).

---

## 8. Modelos de referência

### a) Modelo OSI — 7 camadas

| # | Camada | Responsabilidade |
|---|---|---|
| 7 | **Aplicação** | Serviços ao usuário (HTTP, SMTP, FTP) |
| 6 | **Apresentação** | Tradução de formatos, criptografia, compressão |
| 5 | **Sessão** | Controle de diálogo, sincronização (pontos de verificação) |
| 4 | **Transporte** | Entrega **processo a processo** (portas), segmentação, confiabilidade, controle de fluxo e congestionamento |
| 3 | **Rede** | Entrega **host a host** entre redes, endereçamento lógico (IP), roteamento |
| 2 | **Enlace** | Entrega **nó a nó** (mesma rede), quadros, endereço físico (MAC), detecção de erros |
| 1 | **Física** | Transmissão de **bits** no meio (sinais, cabos, taxas) |

**Como a informação passa entre camadas:**
- **No envio:** os dados descem da aplicação até a física. Cada camada **adiciona seu cabeçalho** (**encapsulamento**; o enlace também adiciona um *trailer*).
- **Na recepção:** sobem da física até a aplicação. Cada camada **remove o cabeçalho** correspondente (**desencapsulamento**) e entrega os dados para a de cima.
- Cada camada conversa logicamente com a **camada par** do outro lado, mas usa os serviços da camada de baixo.

### b) Modelo TCP/IP

| Camada | Responsabilidade | Unidade de dados |
|---|---|---|
| **Aplicação** | Protocolos das aplicações (HTTP, DNS, SMTP) | Mensagem |
| **Transporte** | TCP/UDP: processo a processo, portas | Segmento (TCP) / Datagrama (UDP) |
| **Rede (Internet)** | IP, roteamento, endereçamento lógico | Datagrama/Pacote |
| **Enlace** | Entrega nó a nó, MAC, detecção de erros | Quadro |
| **Física** | Transmissão de bits | Bits |

> Algumas versões (original) usam **4 camadas**: Aplicação, Transporte, Internet e **Interface de rede** (= Enlace + Física). Kurose e Forouzan usam **5**.

Passagem entre camadas: **mesma ideia** — encapsulamento (descendo) e desencapsulamento (subindo).

### c) Relação OSI × TCP/IP

| OSI | TCP/IP (5 camadas) | TCP/IP (4 camadas) |
|---|---|---|
| Aplicação | | |
| Apresentação | **Aplicação** | **Aplicação** |
| Sessão | | |
| Transporte | **Transporte** | **Transporte** |
| Rede | **Rede** | **Internet** |
| Enlace | **Enlace** | **Interface de rede** |
| Física | **Física** | |

---

### 🔑 Cola rápida (revise na véspera)
- 5 componentes: **mensagem, emissor, receptor, meio, protocolo**.
- Simplex (1 sentido) / Half (alternado) / Full (simultâneo).
- Topologias: **malha, estrela, barramento, anel**. Ponto fraco: estrela → hub; barramento → cabo; anel → qualquer nó/enlace.
- LAN/MAN/WAN → **abrangência geográfica** (+ propriedade e tecnologia).
- Datagrama: **sem conexão, pacotes independentes, pode desordenar/perder, tabela com 1 entrada por destino**.
- OSI: **F-E-R-T-S-A-A** (Física, Enlace, Rede, Transporte, Sessão, Apresentação, Aplicação).
- Encapsula descendo, desencapsula subindo.