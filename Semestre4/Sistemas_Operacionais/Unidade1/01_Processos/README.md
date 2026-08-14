# Seção 1 — Processos

> Base para a **Lista 01 — Processos e Threads: Processos**
> Referência: Tanenbaum, *Sistemas Operacionais Modernos*, cap. 2.1

---

## 1.1 O modelo de processos

### O que é um processo

Um **processo** é um programa **em execução**, junto com todo o contexto necessário para
executá-lo. Formalmente, é uma abstração criada pelo SO que agrupa:

- o **código** (segmento de texto);
- os **dados** (variáveis globais, heap);
- a **pilha** (variáveis locais, endereços de retorno);
- os **valores dos registradores**, incluindo o **Contador de Programa (PC)** e o
  **Ponteiro de Pilha (SP)**;
- os **recursos alocados** (arquivos abertos, sinais pendentes, informações de
  contabilização, prioridade, estado etc.).

### Processo × Programa (a diferença que sempre cai na prova)

| Programa | Processo |
|---|---|
| Entidade **passiva** | Entidade **ativa** |
| Arquivo estático em disco | Existe na memória, em execução |
| É uma **receita** (algoritmo + dados de entrada) | É a **atividade de cozinhar** seguindo a receita |
| Não tem estado nem contador de programa | Tem estado, PC, pilha, registradores |
| Um programa pode originar **N** processos | Cada processo está ligado a **um** programa por vez |

> **Analogia clássica de Tanenbaum (a "analogia do bolo"):** a receita é o *programa*;
> o cozinheiro é a *CPU*; os ingredientes são os *dados de entrada*; a atividade do
> cozinheiro lendo a receita, pegando ingredientes e assando é o *processo*.
> Se o filho aparece picado por uma abelha, o cozinheiro **salva onde parou** (guarda o
> contexto), pega o manual de primeiros socorros (outro programa) e inicia **outro
> processo** de prioridade mais alta. Depois volta exatamente de onde parou.

**Frase-resumo para responder a questão 1.a:**
*Processo é um programa em execução com seu contexto associado; programa é apenas o
conjunto de instruções armazenado, uma entidade passiva. O mesmo programa executado
duas vezes gera dois processos distintos, com espaços de endereçamento e contextos
independentes.*

---

## 1.2 Estados de um processo

### Os três estados fundamentais

| Estado | Significado |
|---|---|
| **Em execução (running)** | Está de fato usando a CPU neste instante |
| **Pronto (ready)** | Pode executar, mas está temporariamente parado porque a CPU foi dada a outro processo |
| **Bloqueado (blocked / waiting)** | **Não pode** executar até que um evento externo ocorra (fim de E/S, chegada de dado, liberação de recurso) |

Estados adicionais que muitos SOs implementam: **novo** (sendo criado) e
**terminado/zumbi** (encerrado, aguardando o pai coletar o código de saída).

### As quatro transições e quando ocorrem

```
                     (1) bloqueio
        ┌──────────────────────────────────┐
        │                                  ▼
   ┌──────────┐  (2) preempção        ┌──────────┐
   │ EXECUÇÃO │ ──────────────────►   │ BLOQUEADO│
   │          │ ◄──────────────────   │          │
   └──────────┘  (3) escalonado       └──────────┘
        ▲                                  │
        │            ┌──────────┐          │
        └────────────│  PRONTO  │◄─────────┘
                     └──────────┘  (4) evento ocorreu
```

1. **Execução → Bloqueado**: o processo **por conta própria** pede algo que ainda não
   está disponível (ex.: `read()` de um arquivo, `scanf()` esperando o teclado). Ele
   descobre que não pode prosseguir e o SO o bloqueia. *Transição causada pelo próprio processo.*
2. **Execução → Pronto**: o **escalonador** decide que já deu tempo demais de CPU a esse
   processo (fim do *quantum*) ou um processo mais prioritário chegou. O processo
   continua apto a rodar — só perdeu a vez. *Transição causada pelo SO.*
3. **Pronto → Execução**: o escalonador escolheu esse processo para ocupar a CPU.
   *Transição causada pelo SO.*
4. **Bloqueado → Pronto**: o evento externo que ele esperava aconteceu (a E/S terminou,
   o dado chegou). Ele **não vai direto para execução** — vai para a fila de prontos e
   espera o escalonador escolhê-lo. *Transição causada por evento externo.*

> **Transição impossível:** Bloqueado → Execução (nunca é direta) e Pronto → Bloqueado
> (um processo parado não tem como pedir E/S). Isso costuma cair como pegadinha.

---

## 1.3 O escalonador de processos

### Definição

O **escalonador** é a parte do SO (dentro do nível mais baixo, abaixo de todos os
processos) responsável por **decidir qual processo pronto receberá a CPU** e por
**quanto tempo**. Ele implementa um ou mais **algoritmos de escalonamento**.

### Importância nos sistemas multiprogramados

Em **multiprogramação**, vários processos residem na memória simultaneamente, mas há
apenas uma CPU (ou poucas). A ilusão de paralelismo (**pseudoparalelismo**) só existe
porque a CPU alterna rapidamente entre eles. Quem faz essa alternância acontecer de
forma organizada é o escalonador:

- **Sem escalonador não há alternância**: um processo tomaria a CPU indefinidamente e o
  sistema voltaria a ser monoprogramado.
- Ele garante **justiça** (nenhum processo morre de fome) e **eficiência** (a CPU não
  fica ociosa enquanto processos esperam E/S).
- Ele decide o que fazer nos momentos-chave: criação, término, bloqueio e interrupção de
  relógio.
- Ele determina o **desempenho percebido** do sistema (tempo de resposta, vazão,
  tempo de retorno).

---

## 1.4 A tabela de processos (Process Table / PCB)

### Definição

A **tabela de processos** é uma estrutura de dados mantida pelo SO com **uma entrada por
processo existente**. Cada entrada é chamada de **Bloco de Controle de Processo (PCB)** e
guarda **tudo que é preciso para reiniciar o processo exatamente de onde ele parou**.

### O que fica em cada entrada

| Grupo | Campos típicos |
|---|---|
| **Gerência de processo** | PC, registradores, SP, PSW, estado, prioridade, PID, PID do pai, tempo de CPU usado, hora de início, parâmetros de escalonamento |
| **Gerência de memória** | Ponteiros para os segmentos de texto, dados e pilha; tabela de páginas |
| **Gerência de arquivos** | Diretório-raiz, diretório de trabalho, descritores de arquivos abertos, UID, GID |

### Importância para a alternância de programas

A alternância só é possível porque **o contexto do processo sobrevive fora da CPU**.
Quando um processo é retirado da CPU, seu contexto (registradores, PC, SP) é **salvo na
sua entrada da tabela de processos**; quando ele volta, o contexto é **restaurado** a
partir dali. Sem a tabela, a informação se perderia a cada chaveamento e o processo teria
de recomeçar do zero.

Além disso, a tabela é a **fonte de informação do escalonador** (estados, prioridades,
tempos), o que permite tomar decisões de escalonamento.

### Por que a tabela tem tamanho dinâmico

Porque **a quantidade de processos no sistema varia continuamente**. Alocar
estaticamente significaria desperdiçar memória (poucos processos) ou limitar
artificialmente o sistema (muitos processos).

- **Aumenta**: sempre que um novo processo é criado — inicialização do sistema, chamada
  de sistema de criação (`fork()`/`CreateProcess`) por um processo em execução, requisição
  interativa de um usuário (abrir um programa), início de uma tarefa em lote.
- **Diminui**: sempre que um processo termina — saída normal, saída por erro, erro fatal,
  ou morte por outro processo (`kill`).

> Na prática há um **limite máximo** (ex.: `/proc/sys/kernel/pid_max` no Linux), mas
> dentro desse teto a ocupação cresce e encolhe dinamicamente.

---

## 1.5 Interrupções: RSI e arranjo de interrupções

### Rotina de Serviço de Interrupção (RSI)

É o **trecho de código do SO que trata um tipo específico de interrupção**. Cada
dispositivo/evento (relógio, disco, teclado, placa de rede) tem sua própria RSI, que sabe
o que fazer quando aquele evento ocorre — por exemplo, ler o dado que o disco
disponibilizou e desbloquear o processo que estava esperando.

### Arranjo (vetor) de interrupções

É um **vetor em posições fixas e conhecidas da memória baixa** que armazena, em cada
posição, o **endereço da RSI correspondente**. Ou seja, é uma tabela de ponteiros
indexada pelo **número da interrupção**.

### Como a RSI é encontrada a partir do arranjo

1. O dispositivo gera a interrupção e coloca no barramento o seu **número de
   interrupção** (IRQ).
2. O hardware usa esse número como **índice** no arranjo de interrupções.
3. Lê nessa posição o **endereço da RSI**.
4. Carrega esse endereço no **Contador de Programa**, desviando a execução para a rotina.

Antes de desviar, o **hardware** empilha automaticamente o PC e o PSW do processo
interrompido.

### Fluxo completo: RSI ↔ Escalonador ↔ Tabela de processos

Esse é o "roteiro do chaveamento de contexto", e é a resposta da questão 1.g:

1. **Interrupção ocorre.** O hardware empilha PC e PSW do processo corrente e desvia para
   a RSI via arranjo de interrupções.
2. **Rotina em assembly salva o contexto**: os demais registradores são salvos e o
   ponteiro de pilha é trocado para uma pilha do kernel (a pilha do processo não é
   confiável para isso).
3. **O contexto salvo é gravado na entrada do processo na tabela de processos.** Aqui
   entra a tabela: o estado da CPU deixa de estar na CPU e passa a estar na memória, sob
   controle do SO.
4. **A RSI escrita em C executa o tratamento** propriamente dito (ex.: ler o dado do
   disco, marcar o processo que esperava aquele dado como **pronto** — atualizando de novo
   a tabela de processos).
5. **O escalonador é chamado** e decide qual processo executará em seguida — usando as
   informações (estado, prioridade, tempo de CPU) que estão **na tabela de processos**.
6. **Restauração**: o contexto do processo escolhido é lido da tabela de processos e
   carregado nos registradores; a rotina em assembly faz o retorno de interrupção e o
   processo escolhido volta a executar exatamente de onde havia parado.

> **Resumo em uma frase:** a **RSI** é o gatilho e o mecanismo de salvamento/restauração,
> a **tabela de processos** é o local onde o contexto é preservado, e o **escalonador** é
> quem decide para quem a CPU vai. Os três juntos implementam a alternância.

---

## 1.6 Sistemas multiprocessados

### Como dinamizam a execução

Com **N CPUs (ou núcleos)**, é possível haver **paralelismo real**: até N processos
executando **de fato ao mesmo tempo**, em vez do pseudoparalelismo da multiprogramação.
Efeitos:

- **Maior vazão (throughput)**: mais processos concluídos por unidade de tempo.
- **Menor tempo de espera**: a fila de prontos é drenada mais rápido.
- Processos limitados por CPU podem rodar em paralelo com processos limitados por E/S.
- **Threads** de um mesmo processo podem rodar simultaneamente em núcleos distintos,
  acelerando uma única aplicação.
- Maior **tolerância a falhas** e possibilidade de dedicar CPUs a tarefas específicas.

### Complicações que isso traz ao escalonador

1. **Decisão dupla**: não basta escolher *qual* processo — é preciso escolher também *em
   qual CPU* ele vai rodar.
2. **Afinidade de CPU e cache**: migrar um processo para outra CPU invalida o conteúdo do
   cache dessa CPU (*cache miss* em massa). O escalonador precisa tentar manter o processo
   na mesma CPU (afinidade), o que conflita com balanceamento.
3. **Balanceamento de carga**: precisa evitar que uma CPU fique sobrecarregada enquanto
   outra fica ociosa, sem migrar processos em excesso.
4. **Concorrência sobre as estruturas do SO**: a fila de prontos e a tabela de processos
   passam a ser acessadas por várias CPUs simultaneamente → é preciso **exclusão mútua**
   (travas/spinlocks), o que gera **condições de corrida** se malfeito e vira **gargalo**
   se a fila for única e global.
5. **Escalonamento em grupo (*gang scheduling*)**: threads que cooperam devem, de
   preferência, rodar ao mesmo tempo em CPUs diferentes; se uma roda e a outra não, a
   primeira fica esperando e desperdiça CPU.
6. **Sincronização e coerência de cache** entre CPUs adicionam custo a cada chaveamento.
7. **NUMA**: em máquinas onde o acesso à memória é não uniforme, colocar o processo na CPU
   "errada" o torna mais lento mesmo sem contenção.

---

## 1.7 Criação de processos

### Situações que provocam a criação (questão 2.a)

| Situação | Descrição |
|---|---|
| **1. Início do sistema** | Ao inicializar, o SO cria vários processos: os de **primeiro plano** (interagem com o usuário) e os de **segundo plano/daemons** (não associados a usuários — servidor de e-mail, de impressão, de páginas web, `cron`). |
| **2. Chamada de sistema por um processo em execução** | Um processo em execução cria outro via chamada de sistema (`fork()` no UNIX, `CreateProcess()` no Windows). Típico quando o trabalho pode ser decomposto: um processo busca dados na rede enquanto outro os processa. |
| **3. Requisição do usuário** | O usuário digita um comando, clica num ícone ou abre um programa — o interpretador de comandos/GUI cria o processo. (É, tecnicamente, um caso do item 2, mas disparado interativamente.) |
| **4. Início de uma tarefa em lote** | Em mainframes com processamento em lote, o SO retira a próxima tarefa da fila e cria o processo correspondente quando há recursos disponíveis. |

> Em todos os casos o mecanismo é o mesmo: **um processo já existente executa uma chamada
> de sistema de criação de processo**.

### Espaços de endereçamento entre pai e filho (questão 2.b)

Duas abordagens possíveis:

**(a) Espaços de endereçamento distintos (cópia — modelo UNIX)**
O filho recebe uma **cópia** da imagem do pai (código, dados e pilha). Após o `fork()`,
alterações feitas por um **não são visíveis** ao outro — são memórias separadas.

- *Vantagem:* isolamento e proteção; um não corrompe o outro.
- *Desvantagem:* copiar tudo é caro.
- *Otimização real:* **copy-on-write (COW)** — a memória é compartilhada em modo
  somente-leitura e só é duplicada a página que for efetivamente escrita.

**(b) Espaço de endereçamento compartilhado**
Pai e filho **compartilham** a mesma memória. Alterações de um são vistas imediatamente
pelo outro.

- *Vantagem:* comunicação trivial e barata; criação rápida.
- *Desvantagem:* **condições de corrida**, necessidade de exclusão mútua e perda de
  isolamento (um erro derruba os dois).
- Na prática, é isso que caracteriza **threads** em vez de processos.

> No Windows, o `CreateProcess()` já cria o processo filho **com outro programa carregado**
> desde o início — o espaço de endereçamento é diferente desde a criação, sem o passo
> `fork()` + `exec()` do UNIX.

### Hierarquia entre pai e filho (questão 2.c)

**(a) Com hierarquia (UNIX)**
Existe o conceito de **grupo de processos**: pai, filhos, netos etc. formam uma árvore.

- A raiz de tudo é o `init` (ou `systemd`), criado na inicialização.
- Um sinal enviado ao grupo atinge todos os membros (a menos que cada um decida tratá-lo).
- O pai pode esperar (`wait()`) e coletar o código de término do filho.
- Se o pai morre antes do filho, o **filho é "adotado"** pelo `init`, mantendo a árvore
  consistente.

**(b) Sem hierarquia (Windows)**
Não há conceito de hierarquia: todos os processos são **iguais**.

- O pai recebe um **handle** (identificador) do filho, que lhe dá controle sobre ele.
- Mas esse handle pode ser **passado adiante** para outro processo, quebrando qualquer
  relação de parentesco.
- Vantagem: mais flexível. Desvantagem: perde-se o agrupamento natural para operações
  coletivas (como enviar um sinal a toda uma árvore de processos).

---

## 1.8 Término de processos

### Situações que causam o término (questão 2.d)

| # | Situação | Voluntário? | Descrição |
|---|---|---|---|
| 1 | **Saída normal** | Voluntário | O processo terminou seu trabalho e executa a chamada de saída (`exit()` no UNIX, `ExitProcess()` no Windows). Ex.: o compilador terminou de compilar. |
| 2 | **Saída por erro** | Voluntário | O processo descobre um erro **tratável** e decide terminar. Ex.: `gcc arquivo.c` e o arquivo não existe; o compilador emite mensagem e sai com código de erro. Programas interativos costumam preferir pedir novo dado ao usuário em vez de sair. |
| 3 | **Erro fatal (involuntário)** | Involuntário | Um erro **causado pelo programa**, geralmente um bug: divisão por zero, referência inválida à memória (*segmentation fault*), execução de instrução ilegal. O SO mata o processo. |
| 4 | **Morto por outro processo** | Involuntário | Outro processo com **permissão adequada** executa uma chamada para matá-lo (`kill()` no UNIX, `TerminateProcess()` no Windows). O matador precisa ter autorização — normalmente ser do mesmo usuário ou o superusuário. |

> Em alguns SOs (VMS, por exemplo) matar o pai mata automaticamente todos os filhos. No
> UNIX **isso não acontece**: os filhos são adotados pelo `init`.

---

## Checklist de revisão da Lista 01

- [ ] Sei distinguir processo de programa com um exemplo concreto.
- [ ] Sei desenhar o diagrama de 3 estados com as 4 transições e dizer **quem causa** cada uma.
- [ ] Sei explicar por que Bloqueado → Execução não existe.
- [ ] Sei dizer o que o escalonador faz e por que sem ele não há multiprogramação.
- [ ] Sei listar os 3 grupos de campos da tabela de processos e explicar o papel dela no chaveamento.
- [ ] Sei justificar o tamanho dinâmico da tabela citando o que a faz crescer e encolher.
- [ ] Sei descrever o caminho: interrupção → arranjo → RSI → salvar contexto na tabela → escalonador → restaurar contexto.
- [ ] Sei citar 3 ganhos e 4 complicações dos sistemas multiprocessados.
- [ ] Sei as 4 causas de criação e as 4 causas de término de processos.
- [ ] Sei comparar espaço de endereçamento copiado × compartilhado e hierarquia UNIX × Windows.
