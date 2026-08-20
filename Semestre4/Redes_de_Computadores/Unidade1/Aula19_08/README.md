# Introdução às Redes de Computadores — 19/08/2026

---

## 1. Comutação

![alt text](image.png)

### 1.1 O que é comutação?

**Comutação** (do inglês *switching*) é o processo pelo qual os dados são transferidos de uma origem até um destino através de uma rede formada por múltiplos nós intermediários (roteadores, switches, comutadores). Como não existe, em geral, um caminho físico dedicado permanente entre dois pontos, a rede precisa decidir, a cada trecho, **por onde** e **como** os dados vão trafegar até chegar ao destinatário. É essa decisão — e a forma como os recursos da rede (enlaces, buffers, largura de banda) são compartilhados entre várias comunicações simultâneas — que caracteriza a técnica de comutação usada.

### 1.2 Principais técnicas de comutação

**a) Comutação de circuitos**
Antes de qualquer dado ser enviado, é estabelecido um **caminho dedicado (circuito)** entre origem e destino, passando por todos os nós intermediários. Esse caminho reserva recursos (banda, canais) exclusivamente para aquela comunicação durante toda a sua duração.
- Vantagem: taxa de transmissão constante e garantida, sem disputa por recursos após o estabelecimento.
- Desvantagem: desperdício de recursos quando não há dados sendo enviados (o circuito fica "reservado" mesmo ocioso); exige tempo de estabelecimento do circuito antes da transmissão.
- Exemplo clássico: rede telefônica tradicional (comutação por circuitos analógicos/digitais).

**b) Comutação de pacotes**
Os dados são divididos em unidades menores chamadas **pacotes**, cada um com um cabeçalho contendo, entre outras informações, o endereço de destino. Os pacotes são enviados independentemente pela rede e os recursos (enlaces e roteadores) são **compartilhados** entre diversas comunicações, sem reserva prévia. Existem duas variantes principais:

- **Redes de circuito virtual**: um caminho lógico é definido no início da comunicação (todos os pacotes seguem sempre a mesma rota), mas sem reserva exclusiva de banda como no circuito físico.
- **Redes de datagrama**: cada pacote é tratado de forma independente e pode seguir rotas diferentes até o destino (detalhado na seção 2).

> **Comutação de pacotes** é a técnica mais usada nas redes atuais (incluindo a Internet), pois utiliza os recursos da rede de forma mais eficiente que a comutação de circuitos, já que a banda só é ocupada quando há dados de fato sendo transmitidos.

Dentre as técnicas de comutação de pacotes, a mais usada é a **rede de datagrama**, explicada em detalhes a seguir.

---

## 2. Redes de Datagrama

### 2.1 Como funciona

Em uma **rede de datagrama**, cada pacote (também chamado de *datagrama*) é tratado de forma **independente** pela rede — ou seja, não existe um caminho pré-definido para todos os pacotes de uma mesma comunicação. Cada pacote carrega em seu cabeçalho o **endereço de destino**, e cada roteador pelo qual ele passa decide, de forma independente, qual é o melhor próximo salto (próxima porta de saída) para encaminhá-lo.

### 2.2 O roteador e a tabela de roteamento

Um **roteador** possui várias **portas numeradas**, cada uma conectada a um enlace diferente da rede (podendo levar a outros roteadores ou diretamente a máquinas de destino). Para decidir por onde encaminhar cada pacote, o roteador consulta uma **tabela de roteamento**: uma tabela dinâmica que é constantemente atualizada (por protocolos de roteamento) para refletir mudanças na topologia da rede, congestionamentos ou falhas em enlaces.

A tabela de roteamento é organizada, de forma simplificada, em **duas colunas**:

| Endereço de destino | Porta de saída |
|:--------------------:|:---------------:|
| Máquina 2             | 3                |
| Máquina 3             | 1                |
| Máquina 3             | 3                |

> Observação: é possível existir mais de uma entrada para o mesmo destino (como no exemplo acima com a "Máquina 3"), representando **rotas alternativas**. Isso ocorre porque a tabela é dinâmica: dependendo do momento, do congestionamento da rede ou de falhas em um enlace, o roteador pode escolher uma porta diferente para alcançar o mesmo destino.

**Funcionamento resumido:**
1. O pacote chega a uma das portas de entrada do roteador.
2. O roteador lê o endereço de destino no cabeçalho do pacote.
3. O roteador consulta a tabela de roteamento para descobrir qual é a **melhor porta de saída** disponível naquele momento para aquele destino.
4. O pacote é encaminhado pela porta escolhida, seguindo para o próximo roteador (ou diretamente ao destinatário, se este estiver conectado a essa porta).

### 2.3 Por que a rede de datagrama é uma das mais escolhidas

A rede de datagrama é amplamente adotada (é o modelo usado na Internet, junto ao protocolo IP) porque a **rota é dinâmica**, isto é, pode mudar a qualquer momento, mesmo durante uma mesma comunicação. Isso traz vantagens importantes:

- **Tolerância a falhas**: se um enlace ou roteador falha, os pacotes seguintes podem ser automaticamente redirecionados por outra rota, sem que a comunicação inteira precise ser reiniciada.
- **Balanceamento de carga**: se uma rota está congestionada, os roteadores podem escolher caminhos alternativos, distribuindo melhor o tráfego pela rede.
- **Flexibilidade**: não é necessário estabelecer um caminho fixo antes de começar a enviar dados, como ocorre na comutação de circuitos ou no circuito virtual.

### 2.4 Consequência: pacotes podem não seguir a mesma rota

![alt text](image-1.png)

Como cada pacote é roteado de forma **independente** e as tabelas de roteamento mudam com o tempo, **pacotes de uma mesma mensagem podem chegar ao destino por caminhos diferentes**. Isso gera algumas consequências importantes que os protocolos de camadas superiores (como o TCP) precisam tratar:

- **Chegada fora de ordem**: como as rotas podem ter tamanhos e congestionamentos diferentes, um pacote enviado depois pode chegar antes de outro enviado antes. Por isso, cada pacote carrega um número de sequência, permitindo que o destinatário remonte a mensagem na ordem correta.
- **Atrasos variáveis (jitter)**: como os caminhos podem ser diferentes a cada momento, o tempo de chegada de cada pacote pode variar.
- **Perda de pacotes**: um pacote pode ser descartado no meio do caminho (por congestionamento, erro ou expiração do tempo de vida — TTL) e nunca chegar ao destino, exigindo mecanismos de retransmissão em camadas superiores.

---

## 3. Protocolos

Um **protocolo de rede** é um conjunto de regras e convenções que define **como** os dispositivos de uma rede devem se comunicar — incluindo formato das mensagens, ordem das trocas, como erros são tratados e como os dispositivos identificam uns aos outros. Sem um protocolo em comum, dois dispositivos não conseguem "entender" os dados trocados entre si, mesmo que estejam fisicamente conectados.

De forma geral, um protocolo especifica três aspectos: a **sintaxe** (formato/estrutura dos dados), a **semântica** (o significado de cada campo/mensagem) e a **temporização** (quando e em que ordem as mensagens devem ser enviadas).

### 3.1 Envio de dados

No envio, os dados gerados pela aplicação (ex.: uma mensagem, um arquivo, um vídeo) passam por um processo de **encapsulamento**: em cada camada de protocolo pela qual passam, é adicionado um cabeçalho (e, às vezes, um trailer) com informações de controle necessárias para aquela camada — como endereço de destino, número de sequência, checksum de erro, etc. Ao final, os dados são divididos em pacotes (segmentação) e entregues à camada física para serem transmitidos como sinais pelo meio de transmissão (cabo, fibra, rádio).

### 3.2 Recebimento de dados

No recebimento, ocorre o processo inverso: o **desencapsulamento**. Cada camada de protocolo no dispositivo receptor remove o cabeçalho correspondente adicionado no envio, verifica as informações de controle (por exemplo, se houve erro de transmissão usando o checksum, ou se o pacote está na ordem correta) e repassa o restante dos dados para a camada seguinte, até que a informação original chegue à aplicação de destino, remontada corretamente.

### 3.3 Encaminhamento nos roteadores

O **encaminhamento** (*forwarding*) é a tarefa realizada pelos roteadores intermediários: diferente do envio e do recebimento (que ocorrem nas extremidades da comunicação), o roteador não é o destino final dos dados — ele apenas recebe o pacote em uma porta, consulta sua tabela de roteamento e o repassa pela porta de saída adequada, sem alterar o conteúdo dos dados da aplicação (apenas informações de controle, como o TTL, costumam ser modificadas a cada salto). Esse processo se repete em cada roteador do caminho, até que o pacote alcance seu destino final.

---

## 4. Modelos de Referência

**Modelos de referência** são estruturas conceituais que organizam as funções de uma rede de computadores em **camadas**, onde cada camada é responsável por um conjunto específico de tarefas e se comunica apenas com as camadas imediatamente adjacentes (a camada de cima e a de baixo). Essa organização em camadas facilita o entendimento, o projeto e a padronização de protocolos, permitindo que diferentes tecnologias sejam desenvolvidas de forma independente, desde que respeitem a interface entre as camadas.

### 4.1 Modelo OSI

O **Modelo OSI** (*Open Systems Interconnection*), criado pela ISO, é um modelo de referência **teórico**, composto por **7 camadas**:

| Camada | Nome | Função principal |
|:---:|---|---|
| 7 | **Aplicação** | Interface direta com os programas do usuário (navegador, e-mail, etc.); define protocolos como HTTP, FTP, SMTP. |
| 6 | **Apresentação** | Tradução, formatação, criptografia e compressão dos dados entre sistemas diferentes. |
| 5 | **Sessão** | Estabelece, gerencia e encerra sessões de comunicação entre aplicações. |
| 4 | **Transporte** | Garante a entrega dos dados fim a fim (origem-destino), controle de fluxo e de erros; protocolos: TCP e UDP. |
| 3 | **Rede** | Responsável pelo **roteamento** e endereçamento lógico dos pacotes entre redes diferentes; protocolo: IP. |
| 2 | **Enlace** | Organiza os bits em quadros (*frames*), controle de acesso ao meio e detecção de erros no enlace físico direto. |
| 1 | **Física** | Transmissão dos bits como sinais elétricos, ópticos ou eletromagnéticos pelo meio físico. |

> A comutação e o roteamento estudados nas seções anteriores acontecem principalmente na **camada de Rede (3)**, onde os roteadores operam.

### 4.2 Modelo TCP/IP (complementar)

Na prática, a Internet utiliza o **modelo TCP/IP**, mais enxuto, com **4 camadas** (Aplicação, Transporte, Internet e Acesso à Rede), que mapeiam de forma aproximada às 7 camadas do modelo OSI, condensando as camadas de Aplicação/Apresentação/Sessão em uma única camada de Aplicação, e as camadas de Enlace/Física em uma única camada de Acesso à Rede. É esse modelo, mais simples e efetivamente implementado, que rege o funcionamento da Internet atual.