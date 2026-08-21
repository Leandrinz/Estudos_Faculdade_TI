# Introdução às Redes de Computadores
### Aula de 21/08/2026 — Modelo OSI, TCP/IP e Camada de Transporte

---

## 1. O que é o Modelo OSI?

O **Modelo OSI** (Open Systems Interconnection) é um modelo de referência que divide a comunicação em rede em **7 camadas**. Cada camada tem uma responsabilidade específica e só se comunica diretamente com a camada imediatamente acima e abaixo dela. A ideia é que cada camada não precise "saber" como as outras funcionam por dentro — apenas trocar informações através de interfaces bem definidas.

```
7. Aplicação      ← mais próxima do usuário
6. Apresentação
5. Sessão
4. Transporte
3. Rede
2. Enlace
1. Física          ← mais próxima do meio físico (cabo, rádio, fibra)
```

Um bom jeito de lembrar: **quanto mais alta a camada, mais próxima do usuário/aplicação; quanto mais baixa, mais próxima do hardware/sinal elétrico.**

---

## 2. As 7 Camadas, uma a uma

### 🔹 Camada 7 — Aplicação
Fornece suporte para o funcionamento das aplicações que o usuário usa diretamente (navegador, e-mail, streaming etc.). É aqui que ficam os protocolos que as aplicações usam para se comunicar, como **HTTP, FTP, SMTP, DNS**.

**Exemplo prático:** um servidor web recebe uma requisição HTTP e precisa responder com um arquivo HTML. Só que navegadores diferentes (Chrome, Firefox, Safari) podem renderizar esse HTML de formas diferentes. A camada de Aplicação é responsável por lidar com essas particularidades — é nela que fica definido *como* a aplicação cliente (o navegador) vai interpretar e exibir o conteúdo recebido, para que a experiência não fique ruim dependendo do cliente usado.

> 💡 Resumindo: a camada de Aplicação define **o que** está sendo comunicado, do ponto de vista da aplicação do usuário.

### 🔹 Camada 6 — Apresentação
Responsável por **traduzir/converter os dados** entre o formato usado pela aplicação e o formato usado na rede. Ela cuida de:
- Formatação de dados (ex: conversão de caracteres, codificação)
- Compressão de dados
- Criptografia/descriptografia (ex: TLS/SSL, que "prepara" os dados antes do envio)

> 💡 Pense nela como um "tradutor": garante que duas aplicações que usam representações diferentes de dados consigam se entender.

### 🔹 Camada 5 — Sessão
Responsável por **estabelecer, gerenciar e encerrar** sessões de comunicação entre duas aplicações. Isso inclui:
- Abrir a "conversa" entre cliente e servidor
- Manter essa conversa organizada (controle de diálogo)
- Encerrar a sessão de forma adequada quando a comunicação termina

> 💡 Exemplo: quando você faz login em um site e o site "lembra" que você está autenticado durante a navegação, há uma noção de sessão envolvida.

### 🔹 Camada 4 — Transporte
Realiza o transporte das mensagens da aplicação de origem até a aplicação de destino, garantindo (ou não) confiabilidade na entrega. É aqui que entram os protocolos **TCP** e **UDP**.

*(Veremos essa camada em detalhes na seção 4, já que foi o assunto principal da aula.)*

### 🔹 Camada 3 — Rede
Responsável por **encontrar o melhor caminho** (roteamento) para levar os pacotes de dados de um dispositivo a outro, possivelmente atravessando várias redes diferentes. Trabalha com **endereços lógicos** (endereços IP).

- Protocolo principal: **IP (Internet Protocol)**
- Dispositivo típico: **roteador**

> 💡 Pense nela como o "GPS" da rede: decide por qual caminho os dados devem seguir até o destino.

### 🔹 Camada 2 — Enlace (ou Link de Dados)
Responsável por transmitir dados entre dois dispositivos que estão **diretamente conectados** na mesma rede local, cuidando de:
- Endereçamento físico (endereço **MAC**)
- Detecção (e às vezes correção) de erros de transmissão
- Controle de acesso ao meio (evitar que dois dispositivos "falem" ao mesmo tempo no mesmo cabo/sinal)

- Protocolos/tecnologias: **Ethernet, Wi-Fi (802.11)**
- Dispositivo típico: **switch**

### 🔹 Camada 1 — Física
Responsável pela transmissão **efetiva dos bits** (0s e 1s) através do meio físico: cabos de cobre, fibra óptica, ondas de rádio, etc. Trata de aspectos como voltagem, frequência, modulação do sinal.

- Dispositivo típico: **cabos, hubs, antenas**

---

## 3. Modelo OSI vs. Modelo TCP/IP

Na prática, a internet **não usa o modelo OSI diretamente** — ela usa o modelo **TCP/IP**, que é mais simples e enxuto, com apenas 4 (ou 5, dependendo da fonte) camadas. O modelo OSI é usado principalmente como **ferramenta didática/conceitual** para entender e organizar o funcionamento das redes.

| Modelo OSI (7 camadas)         | Modelo TCP/IP (4 camadas) |
|---------------------------------|----------------------------|
| Aplicação                       | Aplicação                  |
| Apresentação                    | Aplicação                  |
| Sessão                          | Aplicação                  |
| Transporte                      | Transporte                 |
| Rede                            | Internet                   |
| Enlace                          | Acesso à Rede               |
| Física                          | Acesso à Rede               |

**Principais diferenças:**
- O TCP/IP **combina** as camadas de Aplicação, Apresentação e Sessão do OSI em uma única camada de Aplicação.
- O TCP/IP também **combina** as camadas de Enlace e Física em uma única camada de Acesso à Rede (às vezes dividida em duas: Enlace e Física, dependendo do autor).
- O modelo OSI é **teórico/de referência**; o TCP/IP é o modelo **realmente implementado** na internet.
- O TCP/IP foi criado antes do OSI ser formalizado, então na prática ele "venceu" como padrão de uso real.

> 💡 Resumo mental: **OSI = mapa detalhado para estudar; TCP/IP = o que realmente roda na internet.**

---

## 4. Camada de Transporte — Aprofundando

A camada de Transporte é responsável por pegar os dados vindos da camada de Aplicação (através das camadas de Apresentação e Sessão) e entregá-los à aplicação correspondente do outro lado da comunicação. Alguns pontos-chave:

- **Portas (ports):** identificam qual aplicação, no dispositivo de destino, deve receber os dados (ex: porta 80 para HTTP, porta 443 para HTTPS).
- **Segmentação:** os dados grandes da aplicação são divididos em pedaços menores (segmentos) para serem transmitidos.
- **Controle de entrega:** dependendo do protocolo usado, pode haver garantia (ou não) de que os dados cheguem completos, na ordem certa e sem erros.

### Os dois principais protocolos dessa camada:

**TCP (Transmission Control Protocol)**
- Orientado à conexão (é preciso "abrir conexão" antes de enviar dados — three-way handshake)
- Confiável: garante entrega, ordem correta e retransmite dados perdidos
- Possui controle de fluxo e controle de congestionamento
- Mais lento, porém mais seguro/confiável
- Usado em: navegação web (HTTP/HTTPS), e-mail, transferência de arquivos

**UDP (User Datagram Protocol)**
- Não orientado à conexão (envia direto, sem "combinar" antes)
- Não garante entrega, ordem ou ausência de erros
- Mais rápido e leve, com menos overhead
- Usado em: streaming de vídeo/áudio, jogos online, chamadas de voz (VoIP), DNS

> 💡 Pense assim: **TCP é como enviar uma carta registrada** (com confirmação de recebimento); **UDP é como jogar um bilhete pela janela** (rápido, mas sem garantia de que vai chegar).

---

## ✅ Pontos-chave para revisar antes da prova
- Saber a ordem das 7 camadas do OSI (de cima para baixo: Aplicação → Apresentação → Sessão → Transporte → Rede → Enlace → Física)
- Saber qual é a função principal de cada camada, com um exemplo
- Entender a diferença entre o modelo OSI (referência/didático) e o modelo TCP/IP (usado na prática)
- Saber comparar TCP x UDP: confiabilidade, velocidade, uso de portas, casos de uso típicos