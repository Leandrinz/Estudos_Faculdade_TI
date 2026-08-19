# Anotações de Estudo — Lista de Exercícios 01
## Introdução às Redes de Computadores

> Baseado em Forouzan (*Comunicação de Dados e Redes de Computadores*) e Kurose & Ross (*Redes de Computadores e a Internet*), que são as referências indicadas na lista.

---

## 1. Componentes de um Sistema de Comunicação de Dados

Um sistema de comunicação de dados tem **5 componentes**:

1. **Mensagem** — é a informação (dado) a ser comunicada. Pode ser texto, número, imagem, áudio ou vídeo.
2. **Emissor (transmissor)** — o dispositivo que envia a mensagem. Pode ser um computador, telefone, câmera de vídeo, etc.
3. **Receptor** — o dispositivo que recebe a mensagem. Pode ser um computador, telefone, TV, etc.
4. **Meio de transmissão** — o "caminho físico" por onde a mensagem viaja do emissor ao receptor. Exemplos: cabo de par trançado, cabo coaxial, fibra óptica, ondas de rádio.
5. **Protocolo** — um conjunto de regras que governam a comunicação de dados. Representa um acordo entre os dispositivos que se comunicam. Sem um protocolo, dois dispositivos podem estar conectados, mas não se comunicar (ex.: uma pessoa falando francês não consegue ser entendida por outra que só fala japonês).

**Dica para a resposta:** monte uma tabelinha ou lista associando cada componente à sua função — é exatamente o que a questão 1 pede.

---

## 2. Modos de Transmissão (Simplex, Half-Duplex, Full-Duplex)

A direção do fluxo de dados entre dois dispositivos é chamada de **modo de transmissão** (ou modo de operação).

| Modo | Direção do fluxo | Uso simultâneo dos dois sentidos? | Exemplo |
|---|---|---|---|
| **Simplex** | Unidirecional — apenas um dispositivo transmite e o outro só recebe | Não. A capacidade do canal é usada em uma única direção | Teclado → CPU; monitor ← CPU; TV aberta |
| **Half-Duplex** | Bidirecional, mas não simultânea — cada estação transmite e recebe, mas não ao mesmo tempo | Não simultâneo (revezamento) | Walkie-talkie / rádio amador |
| **Full-Duplex** | Bidirecional e simultânea — ambos podem transmitir e receber ao mesmo tempo | Sim | Linha telefônica (chamada de voz); redes atuais com cabos separados ou multiplexação |

**Pontos-chave para justificar:**
- No **simplex**, toda a capacidade do link vai para uma direção só → desperdiça capacidade se precisar de resposta.
- No **half-duplex**, toda a capacidade é usada por vez, mas alternada — melhor que simplex, mas ainda gera atraso (tempo de troca de sentido).
- No **full-duplex** a capacidade do canal é dividida entre os dois sentidos (ou usa meios físicos separados), permitindo comunicação simultânea — é o mais eficiente, mas requer mais infraestrutura.

---

## 3. Conexão Multiponto x Conexão Ponto a Ponto

Tipo de conexão refere-se a como dois ou mais dispositivos se conectam a um link físico.

### Ponto a Ponto (Point-to-Point)
- Um link dedicado **entre exatamente dois dispositivos**.
- Toda a capacidade do link é reservada para esses dois dispositivos.
- **Vantagens:** maior segurança/privacidade (só os dois dispositivos "ouvem"); uso total da capacidade do link; simples de gerenciar.
- **Desvantagens:** caro em larga escala — precisa de um link dedicado para cada par de dispositivos que quiser se comunicar diretamente; pouco escalável.

### Multiponto (Multipoint / Multidrop)
- **Mais de dois dispositivos compartilham o mesmo link**.
- A capacidade do canal é compartilhada, seja espacialmente (todos usam ao mesmo tempo) ou temporalmente (usuários se revezam).
- **Vantagens:** mais econômico — um único meio físico atende vários dispositivos; melhor uso da infraestrutura.
- **Desvantagens:** menor privacidade/segurança (dados passam por/são visíveis a vários nós); pode gerar colisões e necessidade de controle de acesso ao meio (contenção); capacidade efetiva por dispositivo é menor.

---

## 4. Topologias Básicas de Rede

As **4 topologias físicas básicas** são: **Malha (Mesh)**, **Estrela (Star)**, **Barramento (Bus)** e **Anel (Ring)**.

Para cada uma, a lista pede análise em 5 critérios: **facilidade de instalação**, **robustez a falhas de enlace**, **robustez a falhas de nó**, **privacidade/segurança**, **eficácia da transmissão**.

### 4.1 Topologia em Malha (Mesh)
- Cada dispositivo tem um link **ponto a ponto dedicado** com **todos os outros** dispositivos (conexão dedicada de n(n-1)/2 links para n nós).
- **Instalação:** difícil e cara — quantidade de cabos e portas de I/O cresce muito com o número de nós.
- **Robustez a falha de enlace:** excelente — a falha de um link não interrompe o sistema, pois existem rotas alternativas.
- **Robustez a falha de nó:** excelente — a rede continua funcionando entre os demais nós.
- **Privacidade/segurança:** excelente — cada mensagem viaja por um link dedicado, só o destinatário pretendido a recebe.
- **Eficácia da transmissão:** excelente — não há problema de tráfego/compartilhamento pois cada link é exclusivo (embora o total de cabeamento seja alto).

### 4.2 Topologia em Estrela (Star)
- Cada dispositivo tem um link ponto a ponto dedicado apenas com um **controlador central (hub/switch)**.
- **Instalação:** fácil e barata comparada à malha — cada dispositivo precisa de apenas um link e uma porta de I/O.
- **Robustez a falha de enlace:** boa — a falha de um link afeta apenas o dispositivo conectado a ele, não a rede toda.
- **Robustez a falha de nó (do hub central):** **ponto fraco** — se o hub central falhar, toda a rede cai (ponto único de falha).
- **Privacidade/segurança:** boa — dados passam pelo hub, mas não por outros dispositivos periféricos.
- **Eficácia da transmissão:** boa, mas depende da capacidade do hub central — pode ser gargalo.

### 4.3 Topologia em Barramento (Bus)
- Todos os dispositivos são conectados a um **cabo central único (backbone)** por meio de cabos de derivação (drop lines) e conectores (taps).
- **Instalação:** fácil e é a que usa menos cabeamento entre as topologias.
- **Robustez a falha de enlace (do backbone):** **ponto fraco** — uma ruptura no cabo principal (backbone) pode derrubar toda a comunicação ou dividi-la em segmentos isolados.
- **Robustez a falha de nó:** boa — a falha de um dispositivo não afeta os demais (ele apenas sai da rede).
- **Privacidade/segurança:** ruim — o sinal passa por todo o cabo, todos os dispositivos "escutam" o tráfego (broadcast no meio físico).
- **Eficácia da transmissão:** limitada — degrada com o aumento de dispositivos e distância; sujeita a colisões (meio compartilhado) e atenuação do sinal.

### 4.4 Topologia em Anel (Ring)
- Cada dispositivo tem uma conexão ponto a ponto dedicada **apenas com os dois dispositivos vizinhos**, formando um anel fechado. Os dados circulam em uma direção, sendo repassados (repetidos) de nó em nó até o destino.
- **Instalação:** relativamente fácil (parecida com barramento, cabeamento moderado).
- **Robustez a falha de enlace/nó:** **ponto fraco** — a quebra de um único enlace ou a falha de um único dispositivo pode interromper toda a rede (a menos que se use anel duplo/redundante).
- **Privacidade/segurança:** ruim/moderada — os dados passam por todos os nós intermediários até chegar ao destino.
- **Eficácia da transmissão:** boa em cargas moderadas — usa protocolo de passagem de permissão (token), evitando colisões, mas o atraso cresce com o número de nós (cada um retransmite o sinal).

### Resumo comparativo rápido

| Critério | Malha | Estrela | Barramento | Anel |
|---|---|---|---|---|
| Facilidade de instalação | Ruim (muito cabo) | Boa | Ótima (pouco cabo) | Boa |
| Robustez a falha de enlace | Ótima | Boa (isola falha) | Ruim (backbone é crítico) | Ruim (rompe o anel) |
| Robustez a falha de nó | Ótima | Ruim (hub é crítico) | Boa | Ruim (a menos que redundante) |
| Privacidade/segurança | Ótima | Boa | Ruim | Ruim/moderada |
| Eficácia da transmissão | Ótima | Boa (gargalo no hub) | Degrada com carga | Boa (com token) |

---

## 5. Topologias Híbridas (para o exercício de desenho)

Uma **topologia híbrida** combina duas ou mais topologias básicas. Geralmente há um **backbone** (espinha dorsal) que interliga sub-redes menores, cada uma com sua própria topologia.

Estrutura pedida no exercício:

**a) Backbone em estrela + 3 redes em anel**
- Desenhe um hub/switch central (estrela).
- A partir desse hub central, três "ramos" saem, cada um levando a um anel independente de dispositivos.
- Ou seja: o backbone conecta 3 pontos, e cada um desses pontos é, na verdade, um nó de um anel diferente.

**b) Backbone em anel + 3 redes de barramento**
- Desenhe um anel central conectando 3 pontos (nós de backbone).
- Cada um desses 3 pontos do anel se conecta a um cabo de barramento próprio, com vários dispositivos pendurados nesse barramento.

**c) Backbone em barramento + 3 redes em estrela**
- Desenhe um cabo de barramento central.
- A partir de 3 pontos de derivação (taps) desse barramento, cada um se conecta a um hub central de uma topologia em estrela diferente.

> **Dica de desenho:** Use ferramentas como draw.io, PowerPoint ou até papel/caneta. O importante é deixar claro qual estrutura é o *backbone* (conecta as sub-redes entre si) e quais são as *sub-redes* penduradas nele.

---

## 6. LAN, MAN e WAN — fatores determinantes

Os fatores que determinam se um sistema de comunicação é uma LAN, MAN ou WAN são principalmente: **abrangência geográfica (distância)**, mas também **propriedade/gerência**, **velocidade de transmissão** e **finalidade/topologia**.

### LAN (Local Area Network)
- Abrange uma área geográfica pequena e limitada: um prédio, um campus, um escritório.
- Geralmente é de **propriedade privada** (de uma empresa/instituição).
- Alta velocidade de transmissão e baixa taxa de erros.
- Topologias comuns: estrela (mais atual), barramento, anel.

### MAN (Metropolitan Area Network)
- Abrange uma área maior que a LAN, tipicamente uma **cidade** (área metropolitana).
- Pode ser de propriedade de uma única entidade (empresa/provedor) ou pública, interligando várias LANs.
- Exemplo clássico: rede de TV a cabo de uma cidade, ou rede corporativa interligando filiais na mesma cidade.

### WAN (Wide Area Network)
- Abrange uma área geográfica **extensa**: pode ser um país, continente ou o mundo todo.
- Frequentemente interliga várias LANs e MANs através de longas distâncias, usando meios de transmissão como fibra óptica, satélite, links de operadoras de telecomunicações.
- Velocidade de transmissão geralmente menor que a LAN (devido à distância e complexidade da infraestrutura).
- Exemplo máximo de WAN: a própria **Internet**.

**Resumo:** o principal fator é a **distância/abrangência geográfica**, mas propriedade, velocidade e complexidade de gerenciamento também ajudam a diferenciar as três.

---

## 7. Comutação de Pacotes por Datagrama

### a) Como funciona
- Cada pacote é tratado de forma **independente** dos demais, mesmo que pertençam à mesma mensagem.
- Cada pacote (datagrama) carrega um **cabeçalho com o endereço de origem e destino completo**.
- Não existe uma rota pré-estabelecida (não há "conexão" prévia entre origem e destino); cada roteador decide o próximo salto (next hop) **no momento em que recebe o pacote**, com base em sua tabela de roteamento e no estado atual da rede.
- Por isso, pacotes de uma mesma mensagem podem seguir **rotas diferentes** e chegar em ordem diferente da que foram enviados.
- É o modelo usado pela camada de rede da Internet (**IP** — Internet Protocol é *connectionless*).

### b) A tabela de roteamento pode ter duas entradas com o mesmo endereço de destino?
- **Sim, pode.** Isso acontece porque a decisão de roteamento é tomada pacote a pacote, com base nas condições da rede **no momento** (congestionamento, disponibilidade de link, etc.).
- Assim, é possível existir mais de uma rota (mais de um "próximo salto") viável para o mesmo destino, e o roteador escolhe dinamicamente qual usar a cada novo pacote — isso permite, inclusive, balanceamento de carga entre rotas.
- Diferente da comutação de circuitos (onde a rota é fixa durante toda a "chamada") ou de circuito virtual (rota fixa para toda a mensagem/sessão).

### c) Aspectos que levam a entrega desordenada e/ou não garantida
- **Desordenada:** como cada pacote pode seguir uma rota diferente (com atrasos diferentes — tempos de propagação, filas em roteadores distintos), os pacotes podem **chegar fora de ordem** no destino.
- **Não garantida (perda de pacotes):** cada roteador trata os pacotes de forma independente e, em caso de congestionamento, buffer cheio ou erro, pode **descartar pacotes** sem aviso — não há mecanismo de confirmação/retransmissão nessa camada.
- Não há estabelecimento prévio de conexão nem reserva de recursos, então não há garantia de banda, de entrega ou de ordem — é um serviço do tipo **best-effort (melhor esforço)**.
- *(Observação de estudo: é justamente por isso que a Internet usa o TCP, na camada de transporte, para reordenar pacotes e garantir a entrega — o IP sozinho não garante nada disso.)*

### d) Por que é a técnica mais usada nas WANs / na Internet
- **Flexibilidade e resiliência:** como não depende de uma rota fixa, a rede se adapta dinamicamente a falhas de links/roteadores e a congestionamentos, redirecionando pacotes por rotas alternativas.
- **Uso eficiente dos recursos:** não reserva capacidade de forma dedicada (diferente da comutação de circuitos), permitindo que vários fluxos compartilhem os mesmos links — mais escalável e barato para redes muito grandes e heterogêneas como a Internet.
- **Simplicidade dos nós intermediários:** roteadores não precisam manter estado de conexão para cada fluxo (ao contrário de circuito virtual), o que simplifica a operação em uma rede de escala global com topologia complexa e não centralizada.
- **Robustez:** a ausência de uma rota fixa reduz o impacto de falhas pontuais — características essenciais para uma rede como a Internet, que interliga inúmeras redes autônomas e não confiáveis de forma uniforme.

---

## 8. Modelos de Referência OSI e TCP/IP

### a) Modelo OSI — 7 camadas

O modelo OSI (Open Systems Interconnection) organiza a comunicação em **7 camadas**, cada uma com responsabilidades bem definidas:

| # | Camada | Responsabilidade principal |
|---|---|---|
| 7 | **Aplicação** | Interface com o usuário/aplicações (ex.: e-mail, navegação web, transferência de arquivos); fornece os serviços de rede diretamente aos programas |
| 6 | **Apresentação** | Tradução, criptografia e compressão dos dados — garante que os dados sejam compreensíveis entre sistemas com representações diferentes (ex.: formatos de texto, criptografia, compressão) |
| 5 | **Sessão** | Estabelece, gerencia e encerra sessões de comunicação entre aplicações; sincronização e controle do diálogo |
| 4 | **Transporte** | Entrega fim a fim (origem-destino) da mensagem completa; controle de fluxo e de erros fim a fim; segmentação e remontagem |
| 3 | **Rede** | Entrega de pacotes da origem até o destino, possivelmente através de múltiplas redes (roteamento); endereçamento lógico (ex.: IP) |
| 2 | **Enlace de dados (Link)** | Entrega de quadros (frames) de um nó a outro **adjacente** no mesmo enlace; detecção/correção de erros no nível do enlace; controle de acesso ao meio |
| 1 | **Física** | Transmissão de bits "crus" pelo meio físico; trata de características elétricas, mecânicas e de sinalização (voltagem, tipo de cabo, taxa de bits, etc.) |

**Como as informações passam entre as camadas (encapsulamento):**
- No **envio**, cada camada recebe os dados da camada superior, adiciona seu próprio **cabeçalho** (e, na camada 2, também um *trailer*) com informações de controle, e passa para a camada inferior — esse processo é chamado de **encapsulamento**.
- Na **física**, tudo já virou um fluxo de bits que é efetivamente transmitido pelo meio.
- No **recebimento**, o processo é o inverso (**desencapsulamento**): cada camada remove o cabeçalho correspondente ao que sua camada par (peer) adicionou, e passa os dados restantes para a camada superior, até chegar à aplicação.
- Essa comunicação lógica entre camadas equivalentes em origem e destino é chamada de **comunicação par-a-par (peer-to-peer)**, mas fisicamente os dados sempre descem até a camada física, atravessam o meio, e sobem novamente do lado receptor.

### b) Modelo TCP/IP — camadas

O modelo TCP/IP (usado de fato na Internet) tem, na visão mais comum, **4 ou 5 camadas** (dependendo do autor — Forouzan costuma apresentar 5, similar ao OSI mas sem separar sessão/apresentação):

| Camada TCP/IP | Responsabilidade principal | Exemplos de protocolos |
|---|---|---|
| **Aplicação** | Fornece serviços diretamente aos processos de usuário/aplicações; já engloba as funções de apresentação e sessão do OSI | HTTP, FTP, SMTP, DNS |
| **Transporte** | Comunicação fim a fim entre processos (origem-destino); controle de fluxo, controle de erros, entrega confiável (TCP) ou não confiável (UDP) | TCP, UDP |
| **Rede (Internet)** | Roteamento e entrega de pacotes (datagramas) através de múltiplas redes; endereçamento lógico | IP, ICMP, ARP |
| **Enlace (Acesso à Rede/Link)** | Entrega de quadros entre nós adjacentes no mesmo enlace físico; encapsula pacotes IP em quadros apropriados ao meio físico | Ethernet, Wi-Fi (802.11) |
| **Física** | Transmissão de bits pelo meio físico (em algumas representações, é unida à camada de enlace) | Cabos, sinais, rádio |

**Fluxo de informação entre camadas:** segue a mesma lógica de **encapsulamento/desencapsulamento** do OSI — cada camada adiciona seu cabeçalho ao descer a pilha na origem, e cada camada remove seu cabeçalho correspondente ao subir a pilha no destino.

### c) Relação entre as camadas do OSI e do TCP/IP

| OSI (7 camadas) | TCP/IP (4-5 camadas) |
|---|---|
| Aplicação | **Aplicação** |
| Apresentação | **Aplicação** |
| Sessão | **Aplicação** |
| Transporte | **Transporte** |
| Rede | **Rede (Internet)** |
| Enlace de dados | **Enlace (Acesso à rede)** |
| Física | **Física** (às vezes unida à camada de enlace) |

**Ponto-chave para a resposta:** as três camadas superiores do OSI (Aplicação, Apresentação e Sessão) foram **condensadas em uma única camada de Aplicação** no modelo TCP/IP. As camadas de Transporte, Rede/Internet e Enlace têm correspondência praticamente direta entre os dois modelos. A camada Física é equivalente nos dois, embora em algumas representações do TCP/IP ela apareça fundida com a de Enlace.

---

## Checklist rápido de revisão

- [ ] Sei citar e explicar os 5 componentes de um sistema de comunicação de dados
- [ ] Sei diferenciar simplex / half-duplex / full-duplex com exemplos
- [ ] Sei as vantagens/desvantagens de conexão ponto a ponto x multiponto
- [ ] Sei desenhar e comparar malha, estrela, barramento e anel nos 5 critérios pedidos
- [ ] Consigo desenhar as 3 topologias híbridas pedidas
- [ ] Sei diferenciar LAN, MAN e WAN pelos fatores determinantes
- [ ] Entendo como funciona comutação por datagrama, por que a tabela pode ter rotas duplicadas, e por que a entrega pode ser desordenada/não garantida
- [ ] Sei justificar por que datagrama é dominante nas WANs/Internet
- [ ] Sei listar as 7 camadas do OSI e as camadas do TCP/IP, com responsabilidades
- [ ] Sei explicar encapsulamento/desencapsulamento entre camadas
- [ ] Sei fazer a correspondência OSI ↔ TCP/IP

---

**Referências da lista (para consulta completa):**
1. KUROSE, J. F.; ROSS, K. W. *Redes de computadores e a Internet: uma abordagem top-down*. 5ª ed. São Paulo: Pearson, 2010.
2. FOROUZAN, B. A. *Comunicação de dados e redes de computadores*. 4ª ed. Rio de Janeiro: McGraw-Hill, 2008.
