# Sistemas Operacionais — Anotações para as Listas 01 a 06

> **Objetivo:** usar estas anotações como material de estudo para responder às listas de exercícios de **Processos, Threads, Escalonamento e Comunicação entre Processos**.
>
> **Organização:** cada lista corresponde a uma seção, na mesma ordem das atividades.
>
> **Observação importante:** foram disponibilizados aqui os arquivos das listas **01 a 05**. O arquivo da **Lista 06** não apareceu entre os anexos. Por isso, a seção 6 foi montada como um **complemento de estudo baseado no tema informado — “Processos e Threads: Problemas Clássicos de Comunicação Entre Processos” — e não como uma reprodução das questões de uma lista que não foi fornecida**.

---

# 1. Processos

## 1.1 O que é um processo?

Um **processo** é um programa em execução.

A diferença fundamental é:

- **Programa:** conjunto de instruções armazenado, algo passivo.
- **Processo:** programa que está sendo executado, acompanhado de todas as informações necessárias para controlar essa execução.

Portanto:

> **Programa = código/instruções.**
>
> **Processo = programa + estado da execução + recursos/informações necessários à execução.**

Um mesmo programa pode originar vários processos.

### Exemplo

Imagine um navegador instalado no computador:

- o arquivo do navegador é um **programa**;
- quando o sistema operacional inicia esse programa, ele cria um **processo**;
- esse processo possui estado, contador de programa, registradores, espaço de endereçamento etc.

---

## 1.2 Estados de um processo

O processo não fica o tempo inteiro usando a CPU. Ele passa por diferentes estados.

Os principais são:

### Novo (New)

O processo está sendo criado.

### Pronto (Ready)

O processo pode executar, mas está esperando a CPU.

### Executando (Running)

O processo está atualmente utilizando a CPU.

### Bloqueado / Esperando (Blocked/Waiting)

O processo não pode continuar naquele momento porque está esperando algum evento, normalmente uma operação de E/S.

### Terminado (Terminated)

A execução do processo terminou.

### Transições importantes

```text
                 admitido
    NOVO ----------------------> PRONTO
                                  |
                                  | escalonado
                                  v
                               EXECUTANDO
                              /     |      \
                   E/S espera/      |       \ término
                            v       |        v
                       BLOQUEADO    |     TERMINADO
                            |       |
                   E/S concluída    | preempção
                            |       |
                            +------>+
                               PRONTO
```

### Como explicar as transições

**Pronto → Executando**

O escalonador seleciona o processo para utilizar a CPU.

**Executando → Pronto**

O processo perde a CPU, por exemplo, por preempção ou porque o seu quantum terminou.

**Executando → Bloqueado**

O processo solicita uma operação que precisa esperar, como uma operação de E/S.

**Bloqueado → Pronto**

O evento esperado aconteceu, por exemplo, a E/S terminou.

**Executando → Terminado**

O processo finalizou sua execução.

---

# 1.3 Escalonador de processos

O **escalonador de processos** é a entidade responsável por decidir qual processo deverá utilizar a CPU.

Em um sistema multiprogramado existem vários processos, mas a CPU precisa decidir qual deles executar em determinado momento.

O escalonador permite realizar a **alternância de programas na CPU**.

### Ideia principal

```text
Processo A ─┐
Processo B ─┼──> Escalonador ──> CPU
Processo C ─┘
```

O escalonador escolhe um processo conforme uma determinada política/algoritmo de escalonamento.

### Por que ele é importante?

Porque uma escolha ruim pode:

- deixar a CPU ociosa;
- aumentar o tempo de espera;
- aumentar o tempo de resposta;
- prejudicar a interatividade;
- fazer processos demorarem muito para terminar;
- gerar injustiça entre processos/usuários.

---

# 1.4 Tabela de Processos

A **Tabela de Processos** é uma estrutura mantida pelo sistema operacional para armazenar informações necessárias sobre os processos existentes.

Cada processo possui uma entrada/registro nessa tabela.

Ela é essencial para que o SO consiga controlar vários processos e alternar a execução entre eles.

### Informações que podem estar associadas a um processo

- estado do processo;
- contador de programa;
- registradores;
- informações de escalonamento;
- informações de memória;
- informações de E/S;
- identificação do processo;
- relações com outros processos.

### Relação com a alternância da CPU

Quando um processo deixa a CPU, o sistema precisa preservar informações sobre seu estado.

Quando ele voltar a executar, essas informações são recuperadas.

```text
Processo A executando
        ↓
salva estado de A
        ↓
seleciona B
        ↓
carrega estado de B
        ↓
Processo B executando
```

Isso é parte fundamental do **chaveamento de contexto**.

---

# 1.5 Por que a Tabela de Processos é dinâmica?

A quantidade de processos existentes no sistema muda durante a execução.

Quando um novo processo é criado:

```text
novo processo → nova entrada na Tabela de Processos
```

Quando um processo termina:

```text
processo terminado → entrada pode ser liberada/removida
```

Portanto, a tabela precisa acompanhar a quantidade atual de processos.

### Aumenta quando:

- um processo novo é criado;
- um processo gera um processo-filho;
- alguma aplicação ou serviço inicia uma nova execução.

### Diminui quando:

- um processo termina;
- seus recursos e sua entrada deixam de ser necessários.

---

# 1.6 Interrupções, RSI e Arranjo de Interrupções

## Interrupção

Uma **interrupção** é um mecanismo que permite sinalizar ao processador que algum evento precisa ser tratado.

Ela é importante para que o SO possa reagir a eventos sem depender de um programa executar continuamente verificações.

---

## RSI — Rotina de Serviço de Interrupção

A **Rotina de Serviço de Interrupção (RSI)** é o código responsável por tratar uma determinada interrupção.

Em termos simples:

> **Interrupção acontece → CPU identifica o tipo → encontra a RSI correspondente → executa a RSI.**

---

## Arranjo de Interrupções

O **Arranjo de Interrupções** associa tipos/números de interrupção às respectivas rotinas de tratamento.

Uma forma simplificada:

```text
Número da interrupção
        ↓
Arranjo de Interrupções
        ↓
Endereço da RSI
        ↓
Execução da RSI
```

A partir do identificador/tipo da interrupção, o sistema encontra no arranjo a rotina correspondente.

---

# 1.7 RSI + Escalonador + Tabela de Processos

Esse é um ponto importante da Lista 01.

Uma interrupção pode fazer o sistema operacional assumir o controle da CPU.

Uma situação típica:

```text
CPU executa Processo A
        ↓
interrupção ocorre
        ↓
RSI é executada
        ↓
SO atualiza informações necessárias
        ↓
escalonador pode ser acionado
        ↓
novo processo escolhido
        ↓
estado do processo escolhido é restaurado
        ↓
CPU continua execução
```

A **Tabela de Processos** guarda as informações dos processos.

O **escalonador** decide quem deve executar.

A **RSI** permite que o sistema trate determinados eventos e, quando apropriado, participe do mecanismo que leva a uma nova decisão de escalonamento.

### Frase boa para prova

> A RSI trata a interrupção, podendo provocar ou permitir a execução de procedimentos do sistema operacional que atualizam o estado dos processos e acionam o escalonador; o escalonador utiliza as informações mantidas pelo SO, incluindo as associadas à Tabela de Processos, para decidir qual processo deve utilizar a CPU.

---

# 1.8 Sistemas multiprocessados

Em um sistema com várias CPUs/núcleos, mais de um processo pode executar simultaneamente.

Exemplo:

```text
CPU 1 → Processo A
CPU 2 → Processo B
CPU 3 → Processo C
CPU 4 → Processo D
```

Isso aumenta o paralelismo.

Porém, o escalonamento fica mais complicado.

### Problemas adicionais

O escalonador pode precisar considerar:

- distribuição dos processos entre CPUs;
- balanceamento de carga;
- sincronização;
- concorrência;
- afinidade de CPU;
- possibilidade de vários processos serem executados simultaneamente;
- acesso concorrente a estruturas compartilhadas.

### Ideia central

Em uma CPU:

> escolher **quem** executa.

Em várias CPUs:

> escolher **quem executa, onde executa e como distribuir a carga**.

---

# 1.9 Criação de processos

Um processo pode criar novos processos.

O processo que cria é chamado de **processo-pai** e o criado é chamado de **processo-filho**.

### Situações que podem levar à criação

Entre as situações típicas:

- inicialização do sistema;
- criação de processos para executar novos programas;
- solicitação explícita de uma aplicação;
- criação de processos-filhos;
- necessidade de realizar tarefas paralelamente;
- execução de determinados serviços/programas.

---

# 1.10 Espaço de endereçamento entre pai e filho

Quando um processo-filho é criado, existem diferentes abordagens para a relação entre os espaços de endereçamento.

A ideia que precisa ser compreendida é:

> **O filho pode possuir uma cópia do espaço do pai ou possuir um novo espaço associado à execução de outro programa, dependendo do mecanismo adotado.**

### Modelo de cópia

O processo-filho inicia com informações equivalentes às do pai.

```text
Pai
 └── espaço de endereçamento
             ↓
          criação
             ↓
Filho
 └── espaço inicialmente equivalente
```

Depois, pai e filho podem seguir execuções diferentes.

### Modelo de novo programa

O processo-filho pode ser criado e posteriormente carregar outro programa para executar.

---

# 1.11 Hierarquia pai-filho

Os processos podem formar uma estrutura hierárquica.

```text
        PAI
       /   \
     F1     F2
    /  \
  F3   F4
```

O processo que cria outro processo é o pai.

O processo criado é o filho.

Essa relação permite organizar processos em uma hierarquia.

---

# 1.12 Término de processos

Um processo pode terminar por diferentes motivos.

Situações importantes:

- conclusão normal do programa;
- término provocado pelo próprio processo;
- erro;
- condição excepcional;
- solicitação de outro processo/sistema;
- encerramento por decisão do sistema operacional.

### Para memorizar

```text
CRIAR
  ↓
NOVO
  ↓
PRONTO ↔ EXECUTANDO ↔ BLOQUEADO
  ↓
TERMINADO
```

---

# 2. Threads

## 2.1 O que é uma Thread?

Uma **Thread** é uma unidade de execução dentro de um processo.

Um processo pode possuir uma ou várias Threads.

```text
PROCESSO
├── Thread 1
├── Thread 2
└── Thread 3
```

As Threads do mesmo processo compartilham vários recursos, mas cada uma precisa manter informações próprias relacionadas à sua execução.

### Relação entre Processo e Thread

Uma forma útil de pensar:

> **Processo = ambiente/recursos da execução.**
>
> **Thread = fluxo/unidade de execução dentro desse ambiente.**

---

# 2.2 O que cada Thread precisa possuir individualmente?

Cada Thread precisa possuir seu próprio:

- **Contador de Programa (PC)**;
- **conjunto de registradores**;
- **pilha**;
- **estado**.

### Por quê?

Porque Threads diferentes podem estar executando partes diferentes do programa.

### Contador de Programa

Indica a próxima instrução que aquela Thread deverá executar.

Se duas Threads tivessem obrigatoriamente o mesmo PC, não poderiam manter fluxos independentes de execução.

### Registradores

Cada Thread precisa preservar seus valores de registradores para conseguir continuar sua execução corretamente.

### Pilha

A pilha armazena informações próprias da execução, como chamadas de funções, variáveis locais e contexto.

### Estado

Cada Thread pode estar:

- executando;
- pronta;
- bloqueada;
- etc.

Uma Thread pode estar bloqueada enquanto outra do mesmo processo continua executando.

---

# 2.3 Estados de uma Thread

Os estados são semelhantes aos dos processos:

```text
NOVO → PRONTO → EXECUTANDO → TERMINADO
              ↑      |
              |      ↓
              +-- BLOQUEADO
```

### Transições

**Pronto → Executando**

Thread selecionada pelo escalonador.

**Executando → Pronto**

Thread perde a CPU, por exemplo, por preempção.

**Executando → Bloqueado**

Thread realiza uma operação que precisa esperar.

**Bloqueado → Pronto**

O evento esperado aconteceu.

**Executando → Terminado**

A Thread termina.

---

# 2.4 O que as Threads compartilham?

Threads do mesmo processo compartilham informações e recursos como:

- espaço de endereçamento;
- código;
- dados globais;
- arquivos/recursos associados ao processo;
- outros recursos do processo.

Mas cada Thread possui seu próprio:

- contador de programa;
- registradores;
- pilha;
- estado.

### Resumo

| Compartilhado pelo processo | Próprio da Thread |
|---|---|
| Código | Contador de Programa |
| Dados | Registradores |
| Espaço de endereçamento | Pilha |
| Recursos do processo | Estado da Thread |

---

# 2.5 Vantagens das Threads

## 1. Criação mais barata

Criar uma Thread costuma ser menos custoso do que criar um processo completo.

## 2. Chaveamento mais barato

O contexto de Threads pode ser menor que o de processos.

## 3. Compartilhamento

Threads de um mesmo processo compartilham facilmente dados e recursos.

## 4. Paralelismo

Em sistemas com múltiplas CPUs/núcleos, Threads podem executar simultaneamente.

## 5. Responsividade

Uma Thread pode continuar atendendo uma tarefa enquanto outra está bloqueada.

---

# 2.6 Desvantagens das Threads

O compartilhamento também aumenta os riscos.

### Problemas

- condições de corrida;
- necessidade de sincronização;
- maior complexidade;
- uma falha grave no espaço compartilhado pode afetar outras Threads do processo;
- bugs de concorrência podem ser difíceis de reproduzir.

### Quando usar Threads?

São especialmente interessantes quando:

- tarefas pertencem ao mesmo programa;
- precisam compartilhar muitos dados;
- existe necessidade de concorrência/paralelismo;
- o custo de criar vários processos seria desnecessário.

### Quando usar processos?

Processos são interessantes quando:

- é desejável maior isolamento;
- as tarefas são mais independentes;
- segurança e separação de recursos são importantes;
- uma falha em uma execução não deve comprometer diretamente outra.

---

# 2.7 Threads no espaço do usuário

Nesse modelo, o gerenciamento das Threads é feito principalmente por uma biblioteca/runtime no espaço do usuário.

O sistema operacional pode enxergar o processo como uma unidade de execução, enquanto o **Runtime** administra as Threads daquele processo.

```text
                 Sistema Operacional
                         |
                      Processo
                         |
                  Runtime de Threads
                  /       |       \
              T1         T2        T3
```

### Papel do Runtime

O Runtime pode:

- criar Threads;
- terminar Threads;
- controlar estados;
- realizar chaveamentos;
- escolher qual Thread deve executar;
- implementar o algoritmo de escalonamento das Threads.

### Consequência importante

Diferentes processos podem utilizar diferentes runtimes/configurações.

Assim, um processo pode escolher um algoritmo de escalonamento para suas Threads e outro processo escolher outro.

---

# 2.8 Threads no núcleo

Nesse modelo, as Threads são conhecidas e gerenciadas pelo **Sistema Operacional**.

```text
Sistema Operacional
├── Processo A
│   ├── T1
│   └── T2
└── Processo B
    ├── T1
    └── T2
```

O próprio SO realiza o gerenciamento das Threads.

### Por que não há Runtime como no modelo anterior?

Porque o gerenciamento necessário das Threads é realizado pelo próprio núcleo/SO.

### Vantagem importante

O sistema operacional consegue enxergar e escalonar Threads individualmente.

Isso facilita o aproveitamento de múltiplas CPUs.

---

# 2.9 Espaço do usuário × núcleo

| Aspecto | Espaço do usuário | Núcleo |
|---|---|---|
| Quem gerencia Threads? | Runtime/biblioteca | Sistema Operacional |
| SO precisa conhecer cada Thread? | Não necessariamente | Sim |
| Criação/chaveamento | Geralmente mais barato | Geralmente mais caro |
| Sistema precisa suportar Threads? | Runtime pode oferecer suporte | SO precisa oferecer suporte |
| Chamada bloqueante | Pode bloquear o processo inteiro dependendo do mecanismo | SO pode bloquear a Thread |
| Escalonamento | Runtime controla as Threads do processo | SO controla as Threads do sistema |

### Pegadinha importante

No espaço do usuário, uma chamada bloqueante pode impedir a execução das demais Threads daquele processo caso o SO veja o processo inteiro como bloqueado.

No núcleo, o SO pode bloquear uma Thread e continuar executando outra.

---

# 2.10 Por que diferentes processos podem ter algoritmos diferentes no espaço do usuário?

Porque cada processo pode possuir seu próprio Runtime.

```text
Processo A → Runtime A → algoritmo X
Processo B → Runtime B → algoritmo Y
```

Já no modelo de Threads no núcleo, o escalonamento é realizado pelo SO.

Assim, a política de escalonamento das Threads fica sob controle do mecanismo global do sistema operacional.

---

# 2.11 Threads híbridas

O modelo híbrido combina características dos dois modelos.

A ideia é manter Threads de usuário associadas a entidades que o núcleo consegue escalonar.

```text
Threads de usuário
 T1 T2 T3 T4
    \ | | /
      ↓
Threads/entidades do núcleo
     K1 K2
      ↓
      SO
```

### Características

A abordagem híbrida busca combinar:

- flexibilidade do gerenciamento no usuário;
- capacidade do núcleo de escalonar entidades individualmente;
- possibilidade de aproveitar múltiplas CPUs;
- redução de alguns custos do gerenciamento totalmente no núcleo.

---

# 3. Escalonamento de Processos e Threads

## 3.1 O que é escalonamento?

Escalonamento é o processo de decidir **qual processo/Thread deve utilizar a CPU**.

O **escalonador** é quem toma a decisão.

O **algoritmo de escalonamento** define as regras utilizadas para tomar essa decisão.

### Não confundir

> **Escalonador = entidade que toma a decisão.**
>
> **Algoritmo = conjunto de regras utilizado para decidir.**

---

# 3.2 Por que o escalonador é importante?

O objetivo é utilizar a CPU de forma adequada, considerando características do sistema.

Critérios comuns:

- utilização da CPU;
- throughput;
- tempo de conclusão;
- tempo de espera;
- tempo de resposta;
- justiça;
- prioridade;
- quantidade de chaveamentos;
- comportamento interativo.

---

# 3.3 Processos limitados por CPU e por E/S

Os processos alternam entre:

```text
computação → E/S → computação → E/S → ...
```

### CPU-bound

Processo limitado por computação.

Possui intervalos longos de utilização da CPU.

```text
CPU ███████████████
E/S ██
CPU ███████████████
```

### I/O-bound

Processo limitado por E/S.

Passa bastante tempo esperando dispositivos de E/S.

```text
CPU ██
E/S ███████████
CPU ██
E/S ███████████
```

### Por que isso importa?

Um bom escalonador pode aproveitar a CPU enquanto um processo I/O-bound está bloqueado.

---

# 3.4 Quando pode ocorrer troca de processo?

Uma troca pode ocorrer, por exemplo:

1. processo termina;
2. processo bloqueia esperando E/S;
3. processo volta de uma espera;
4. ocorre preempção;
5. quantum termina;
6. chega um processo que pode ter prioridade;
7. ocorre uma interrupção relevante para o escalonamento.

O ponto importante é:

> Em cada evento, o escalonador pode decidir manter o processo atual ou selecionar outro.

---

# 3.5 Preempção

**Preempção** significa retirar a CPU de um processo que ainda poderia continuar executando.

Exemplo:

```text
A executando
     ↓
quantum terminou
     ↓
A → pronto
     ↓
B → executando
```

### Sem preempção

O processo normalmente mantém a CPU até:

- terminar;
- bloquear.

### Com preempção

O sistema pode retirar a CPU antes disso.

---

# 3.6 Sensibilidade ao relógio

Uma classificação importante dos algoritmos é observar se eles dependem das interrupções do relógio.

### Algoritmos não preemptivos

Não dependem de interrupções periódicas para retirar a CPU do processo.

O processo normalmente continua até:

- terminar;
- bloquear.

Exemplo:

- FCFS;
- tarefa mais curta primeiro, na forma clássica.

### Algoritmos preemptivos

Podem retirar a CPU durante a execução.

Exemplos:

- Round Robin;
- próximo de menor tempo restante;
- determinados escalonamentos por prioridade.

---

# 3.7 Sistemas em lote

Em sistemas em lote, os trabalhos são submetidos para execução sem exigir interação contínua do usuário.

O foco costuma estar em eficiência e tempo de conclusão.

Algoritmos destacados na lista:

- FCFS;
- tarefa mais curta primeiro;
- próximo de menor tempo restante.

---

# 3.8 FCFS — First Come First Served

Também chamado de **primeiro a chegar, primeiro a ser atendido**.

### Regra

O primeiro processo que chega é o primeiro a executar.

```text
Fila:
A → B → C → D
```

Se A está executando:

```text
A termina
 ↓
B executa
 ↓
C executa
 ↓
D executa
```

### Característica

Normalmente é simples de implementar.

### Problema clássico

Um processo longo pode ficar na frente de vários processos curtos.

Isso pode aumentar bastante o tempo médio de espera.

### Criação

Novo processo entra no final da fila.

### Término

Processo sai e o próximo da fila pode executar.

### Bloqueio

Se o processo bloquear, outro processo pronto pode utilizar a CPU.

Quando ele volta, retorna à fila conforme a política adotada.

---

# 3.9 Tarefa mais curta primeiro — Shortest Job First

Escolhe o processo com o menor tempo de execução estimado.

Exemplo:

```text
A = 40 ms
B = 10 ms
C = 20 ms
```

Se todos estão disponíveis:

```text
B → C → A
```

### Vantagem

Pode produzir um bom tempo médio de conclusão/espera quando os tempos são conhecidos.

### Desvantagem

É necessário conhecer ou estimar o tempo necessário para terminar os processos.

### Por que isso é mais fácil em sistemas em lote?

Porque os trabalhos são submetidos previamente e podem ter duração conhecida/estimada antes da execução.

---

# 3.10 Próximo de menor tempo restante — Shortest Remaining Time

É a versão preemptiva da ideia de escolher o menor trabalho.

O escalonador observa o **tempo restante**.

Exemplo:

```text
A restante = 30 ms
B restante = 8 ms
```

B pode receber a CPU.

Se surgir um processo ainda menor:

```text
A executando
     ↓
chega B com 5 ms restantes
     ↓
B preempta A
```

### Diferença essencial

- **Tarefa mais curta primeiro:** considera a duração do trabalho.
- **Menor tempo restante:** considera quanto ainda falta para terminar e pode preemptar.

---

# 3.11 Como resolver exercícios de escala de CPU

A Lista 03 apresenta tabelas com:

- processo;
- duração;
- instante de chegada/início;
- em alguns casos, prioridade.

Para resolver, faça sempre:

### Passo 1 — Monte a linha do tempo

```text
0     4     9     ...
|-----|-----|
```

### Passo 2 — Observe quem já chegou

Nunca escolha um processo antes do instante em que ele chega.

### Passo 3 — Aplique a regra do algoritmo

Exemplos:

- FCFS → menor instante de chegada;
- SJF → menor duração entre os disponíveis;
- SRTF → menor tempo restante entre os disponíveis;
- Round Robin → ordem da fila + quantum;
- Prioridade → maior prioridade entre os disponíveis.

### Passo 4 — Marque preempções

Sempre que a política permitir retirar a CPU, verifique se surgiu um processo que deve assumir.

### Passo 5 — Calcule o tempo de conclusão

O **tempo de conclusão** é o instante em que o processo termina.

### Passo 6 — Calcule o tempo médio solicitado

Se a questão pedir o tempo médio gasto para conclusão:

```text
média = soma dos tempos individuais / quantidade de processos
```

Quando a disciplina estiver tratando **turnaround**, use:

```text
Turnaround = término - chegada
```

Quando estiver tratando **tempo de espera**, use:

```text
Espera = turnaround - tempo efetivamente executado
```

**Atenção:** não confunda tempo de conclusão, turnaround e tempo de espera. Leia exatamente o que a questão está pedindo.

---

# 3.12 Sistemas interativos

Sistemas interativos precisam responder rapidamente às ações do usuário.

O tempo de resposta ganha grande importância.

Algoritmos destacados na lista:

- Round Robin;
- prioridades;
- filas múltiplas;
- garantido;
- loteria;
- fração justa.

---

# 3.13 Round Robin

O **Round Robin** utiliza um **quantum**.

Cada processo recebe a CPU durante no máximo aquele intervalo.

Exemplo com quantum de 5 ms:

```text
A | B | C | A | B | C | ...
  5   5   5
```

Se A não terminar em 5 ms, ele volta para a fila.

### Quantum pequeno

Vantagens:

- maior responsividade;
- processos recebem oportunidades mais rapidamente.

Desvantagens:

- muitos chaveamentos;
- maior custo de troca de contexto.

### Quantum grande

Vantagens:

- menos chaveamentos;
- menor custo de gerenciamento.

Desvantagens:

- pode ficar parecido com FCFS;
- pior resposta para processos interativos.

### Regra para prova

> **Quantum pequeno → mais responsividade, mais chaveamentos.**
>
> **Quantum grande → menos chaveamentos, menor responsividade.**

---

# 3.14 Escalonamento por prioridades

Cada processo possui uma prioridade.

O escalonador escolhe o processo de maior prioridade entre os disponíveis.

Na Lista 03:

> **quanto menor o número da prioridade, maior a prioridade.**

Assim:

```text
Prioridade 0 → maior
Prioridade 1
Prioridade 2
Prioridade 3 → menor
```

### Atenção

Não use automaticamente a convenção “número maior = prioridade maior”. A questão define a regra.

---

# 3.15 Filas múltiplas

Os processos são organizados em diferentes filas.

Exemplo:

```text
Fila de alta prioridade
    ↓
Fila de média prioridade
    ↓
Fila de baixa prioridade
```

Cada fila pode possuir regras diferentes.

### Problema

Um processo em uma fila de baixa prioridade pode ficar esperando por muito tempo.

Isso é chamado de **starvation**.

### Estratégias para reduzir esse problema

Uma estratégia importante é o **aging**:

> aumentar gradualmente a prioridade de um processo que espera há muito tempo.

Assim:

```text
espera longa → prioridade aumenta → processo finalmente executa
```

---

# 3.16 Escalonamento garantido

A ideia é oferecer uma participação de CPU que possa ser considerada garantida/proporcional ao número de processos ou usuários.

Se existem N processos com participação equivalente, cada um deve receber aproximadamente uma fração:

```text
1/N
```

A ideia central é justiça e previsibilidade.

---

# 3.17 Escalonamento por loteria

Cada processo recebe **bilhetes**.

O escalonador sorteia um bilhete.

Quem possuir o bilhete sorteado ganha a CPU.

Exemplo:

```text
A → 10 bilhetes
B → 30 bilhetes
C → 60 bilhetes
```

C possui maior probabilidade de ser escolhido.

### Ideia central

> Mais bilhetes → maior probabilidade de receber CPU.

Não significa necessariamente que o processo sempre executará mais em cada instante; significa maior probabilidade ao longo dos sorteios.

---

# 3.18 Escalonamento por fração justa

O objetivo é garantir justiça entre **usuários**, e não apenas entre processos.

Isso é importante porque um usuário pode possuir vários processos.

Exemplo:

```text
Usuário 1:
  A
  C

Usuário 2:
  B
  D
  E
```

Se simplesmente considerarmos cada processo igualmente, o usuário 2 poderia receber mais CPU porque possui mais processos.

Na **fração justa**, a distribuição considera o usuário.

### Na questão da Lista 03

- A e C → usuário 01;
- B, D e F → usuário 02.

A questão quer que você observe a diferença entre:

> justiça por processo

e

> justiça por usuário.

---

# 3.19 Comparação rápida dos algoritmos

| Algoritmo | Preempção | Ideia principal | Ponto forte | Problema |
|---|---|---|---|---|
| FCFS | Não, na forma clássica | Ordem de chegada | Simplicidade | Processo longo pode atrasar vários |
| Tarefa mais curta | Não, na forma clássica | Menor duração | Bom tempo médio em condições adequadas | Precisa conhecer duração |
| Menor tempo restante | Sim | Menor tempo restante | Favorece trabalhos curtos | Mais complexidade/chaveamentos |
| Round Robin | Sim | Quantum | Boa responsividade | Muitos chaveamentos se quantum for pequeno |
| Prioridades | Pode ser | Maior prioridade | Permite diferenciação | Starvation |
| Filas múltiplas | Pode ser | Filas diferentes | Flexibilidade | Baixa prioridade pode esperar muito |
| Garantido | — | Garantir participação | Justiça/previsibilidade | Mais complexo |
| Loteria | — | Sorteio de bilhetes | Flexibilidade | Resultado probabilístico |
| Fração justa | — | Justiça entre usuários | Evita vantagem por ter mais processos | Mais controle necessário |

---

# 3.20 Política × mecanismo de escalonamento

### Mecanismo

Define **como** algo é realizado.

### Política

Define **qual decisão** deve ser tomada.

Uma forma simples de memorizar:

> **Mecanismo = como fazer.**
>
> **Política = o que escolher/favorecer.**

Em sistemas com hierarquia de processos, essa separação permite que o mecanismo execute a decisão enquanto a política determine critérios como prioridade, justiça ou distribuição.

---

# 3.21 Escalonamento de Threads

A relação com a Lista 02 é direta.

### Threads no espaço do usuário

O Runtime pode escalonar Threads dentro de cada processo.

```text
SO
 ↓
Processo
 ↓
Runtime
 ↓
Threads
```

Logo, o algoritmo pode ser escolhido/implementado no nível do Runtime.

### Threads no núcleo

O SO conhece as Threads e pode escaloná-las diretamente.

```text
SO
├── T1
├── T2
├── T3
└── T4
```

---

# 4. Comunicação entre Processos com Espera Ociosa

## 4.1 O problema da concorrência

Quando dois processos/Threads executam concorrentemente e acessam dados compartilhados, a ordem das operações pode afetar o resultado.

Isso pode gerar uma **condição de corrida**.

---

# 4.2 Condição de corrida

Existe uma **condição de corrida** quando o resultado depende da ordem/intercalação em que processos concorrentes executam operações.

### Exemplo

Imagine:

```text
x = 5
```

Processo A:

```text
x = x + 1
```

Processo B:

```text
x = x + 1
```

Se os dois fizerem leitura/modificação/escrita de maneira inadequadamente intercalada, o resultado pode ser diferente do esperado.

O problema não é simplesmente “dois processos acessarem uma variável”.

O problema é o acesso concorrente a uma operação que precisa ser tratada de maneira coordenada.

---

# 4.3 Região crítica

A **região crítica** é a parte do programa em que ocorre acesso/manipulação de recursos compartilhados que não devem ser utilizados simultaneamente de maneira conflitante.

Exemplo:

```text
entrada

REGIÃO CRÍTICA
  acessar dado compartilhado
  modificar dado compartilhado

saída
```

---

# 4.4 Exclusão mútua

**Exclusão mútua** significa impedir que processos concorrentes entrem simultaneamente em uma região crítica incompatível.

Em termos simples:

```text
A entrou na região crítica
B quer entrar
     ↓
B precisa esperar
     ↓
A sai
     ↓
B pode entrar
```

### Objetivo

Evitar que operações concorrentes sobre dados compartilhados produzam resultados incorretos.

---

# 4.5 Quatro condições da solução correta

A Lista 04 destaca quatro condições.

## I. Exclusão mútua

No máximo um processo deve estar na região crítica por vez.

## II. Nada pode ser afirmado sobre velocidade ou quantidade de CPUs

A solução não deve depender de uma CPU específica ou de uma determinada relação de velocidade entre processos.

## III. Processo fora da região crítica não pode bloquear outro

Se um processo não está interessado no recurso compartilhado, ele não deve impedir outro processo de entrar na região crítica.

## IV. Nenhum processo deve esperar eternamente

A solução deve evitar espera infinita.

Isso está relacionado à **starvation**.

---

# 4.6 Desligamento de interrupções

Uma abordagem é desabilitar interrupções durante a região crítica.

Em uma máquina com uma única CPU:

```text
desabilita interrupções
      ↓
região crítica
      ↓
habilita interrupções
```

Se não houver interrupção capaz de causar uma troca de execução durante aquele período, outro processo não assume a CPU naquele intervalo.

### Por que isso pode funcionar em uma CPU?

Porque somente uma CPU está executando e as interrupções são um mecanismo importante para o SO recuperar o controle.

### Por que é problemático?

- compromete a segurança;
- um processo poderia impedir o tratamento de interrupções por tempo excessivo;
- não resolve adequadamente o problema em sistemas com várias CPUs.

### Em múltiplas CPUs

Desligar interrupções em uma CPU não impede outra CPU de executar outro processo.

---

# 4.7 Variáveis do tipo lock

Uma variável de trava pode indicar:

```text
0 → livre
1 → ocupada
```

Um processo verifica:

```text
se lock == 0:
    lock = 1
    entra
```

Depois:

```text
lock = 0
```

### Problema

A verificação e a alteração precisam ser indivisíveis.

Se dois processos fizerem:

```text
A lê lock = 0
B lê lock = 0
A coloca lock = 1
B coloca lock = 1
```

os dois podem acreditar que adquiriram a trava.

Isso viola a **exclusão mútua**.

Portanto, o lock simples, quando implementado com operações separadas, não garante corretamente a condição I.

---

# 4.8 Chaveamento obrigatório

Outra abordagem é forçar alternância entre processos.

Exemplo conceitual:

```text
vez = A

A executa
A sai
vez = B

B executa
B sai
vez = A
```

### Problema

Um processo que está fora da região crítica ainda pode impedir o outro.

Imagine:

```text
vez = A
```

A não quer entrar na região crítica.

B quer entrar.

Se a regra exige que seja a vez de A, B pode ficar esperando mesmo que A não esteja usando a região crítica.

Isso viola a condição:

> **Nenhum processo fora de sua região crítica pode bloquear outro processo.**

---

# 4.9 Solução de Peterson

A solução de Peterson é uma solução clássica para exclusão mútua entre dois processos.

Ela utiliza duas ideias:

- indicação de interesse;
- indicação de quem deve ter preferência em caso de conflito.

Conceitualmente:

```text
interessado[A] = true
vez = B

enquanto B estiver interessado e for a vez de B:
    esperar
```

O outro processo faz o equivalente.

### Ideia central

Se apenas um quer entrar:

> ele consegue entrar.

Se os dois querem:

> a variável de turno resolve o conflito.

### Problema citado na lista

A solução pode apresentar problema relacionado à **inversão de prioridades**.

Imagine:

```text
Processo de alta prioridade → quer região crítica
Processo de baixa prioridade → entra/segura condição necessária
```

O processo de alta prioridade pode acabar esperando por um processo de baixa prioridade.

Isso viola a exigência de não haver espera eterna em determinadas condições de prioridade e caracteriza o problema destacado pela lista.

---

# 4.10 TSL — Test and Set Lock

TSL é uma instrução especial de hardware que permite realizar de forma atômica uma operação de teste e alteração de uma trava.

A ideia é:

```text
TSL(lock)

testa o valor
e altera a trava
de maneira atômica
```

Assim, dois processos não conseguem executar uma sequência separada de “testar” e “marcar como ocupada” simultaneamente.

### Ideia

```text
lock livre?
   ↓
TSL verifica + ocupa atomicamente
   ↓
processo entra
```

### Problema

Pode haver **espera ocupada (busy waiting)**.

Um processo pode ficar continuamente verificando:

```text
enquanto lock ocupado:
    verificar novamente
```

Além disso, a lista destaca novamente o problema de **inversão de prioridades**.

---

# 4.11 Resumo da Lista 04

| Mecanismo | Ideia | Problema destacado |
|---|---|---|
| Desligar interrupções | Impedir interrupções durante região crítica | Segurança e múltiplas CPUs |
| Lock simples | Variável indica livre/ocupado | Não garante exclusão mútua se teste/alteração não forem atômicos |
| Chaveamento obrigatório | Alternância pré-determinada | Processo fora da região crítica pode bloquear outro |
| Peterson | Interesse + turno | Inversão de prioridades |
| TSL | Teste + alteração atômica | Inversão de prioridades / espera ocupada |

---

# 5. Comunicação entre Processos com Bloqueio

## 5.1 Por que abandonar a espera ociosa?

Na **espera ociosa**, um processo continua utilizando a CPU enquanto espera.

Exemplo:

```text
enquanto recurso ocupado:
    verificar
```

Isso é chamado de **busy waiting**.

### Problemas

- desperdiça CPU;
- aumenta consumo de processamento;
- pode prejudicar outros processos;
- pode ser ineficiente para esperas longas.

A comunicação com **bloqueio** permite que o processo seja colocado para dormir enquanto aguarda.

---

# 5.2 Sleep e Wakeup

## Sleep

A operação `sleep` faz o processo entrar em estado de espera/bloqueio.

Em vez de:

```text
esperar
esperar
esperar
esperar
```

o processo:

```text
sleep()
```

e deixa de disputar a CPU.

---

## Wakeup

`wakeup` acorda/libera um processo que estava esperando por determinado evento.

Conceitualmente:

```text
Processo A
   ↓
sleep
   ↓
B realiza evento necessário
   ↓
wakeup(A)
   ↓
A volta a poder executar
```

---

# 5.3 Problema de sleep/wakeup

O problema clássico é o **wakeup perdido**.

Imagine:

```text
1. Consumidor verifica que buffer está vazio.
2. Antes de executar sleep, produtor coloca item.
3. Produtor executa wakeup.
4. Consumidor ainda não estava efetivamente dormindo.
5. Consumidor executa sleep.
6. Não existe novo wakeup.
```

Resultado:

> O consumidor pode ficar dormindo mesmo depois de existir um item.

Essa é uma condição de corrida entre verificar a condição e dormir.

---

# 5.4 Produtor/Consumidor

Esse é um dos problemas centrais da comunicação entre processos.

Temos:

- **Produtor:** produz itens;
- **Consumidor:** consome itens;
- **buffer:** espaço intermediário.

```text
PRODUTOR
   ↓
[ BUFFER ]
   ↓
CONSUMIDOR
```

Com buffer de 100 posições:

- produtor não pode inserir quando o buffer está cheio;
- consumidor não pode retirar quando o buffer está vazio.

---

# 5.5 Semáforos

Um **semáforo** é um mecanismo de sincronização/comunicação utilizado para controlar o acesso e a espera entre processos.

Ele possui um valor associado e operações que devem ser realizadas de maneira **atômica**.

As operações são frequentemente apresentadas como:

- `wait` / `P` / `down`;
- `signal` / `V` / `up`.

---

# 5.6 Operação wait

A ideia é:

```text
wait(S)
```

O processo tenta adquirir/decrementar o semáforo.

Se o recurso/condição não estiver disponível, o processo pode ser bloqueado.

### Conceito

```text
wait(S)
   ↓
S disponível?
 ┌───────┴───────┐
sim             não
 ↓                ↓
continua        bloqueia
```

---

# 5.7 Operação signal

A ideia é:

```text
signal(S)
```

O processo libera/sinaliza o semáforo.

Se houver processos esperando, um deles pode ser desbloqueado conforme o mecanismo do SO.

---

# 5.8 Por que operações de semáforo precisam ser atômicas?

Porque, se `wait` ou `signal` pudessem ser interrompidos no meio da alteração do semáforo, dois processos poderiam observar/modificar o valor de maneira inconsistente.

A operação precisa ser tratada como indivisível.

> **Semáforo sem operações atômicas não fornece a sincronização esperada.**

---

# 5.9 Produtor/Consumidor com semáforos

Para um buffer de 100 posições, uma solução clássica usa três semáforos:

```text
empty = 100
full = 0
mutex = 1
```

### `empty`

Indica quantas posições do buffer estão livres.

Inicialmente:

```text
empty = 100
```

### `full`

Indica quantas posições possuem itens disponíveis.

Inicialmente:

```text
full = 0
```

### `mutex`

Controla a **exclusão mútua** durante o acesso à estrutura compartilhada.

Inicialmente:

```text
mutex = 1
```

---

# 5.10 Papel dos semáforos

### Produtor

Conceitualmente:

```text
wait(empty)
wait(mutex)

    coloca item no buffer

signal(mutex)
signal(full)
```

Interpretação:

1. espera existir espaço;
2. entra na região crítica;
3. insere item;
4. libera a região crítica;
5. informa que existe mais um item.

### Consumidor

```text
wait(full)
wait(mutex)

    retira item do buffer

signal(mutex)
signal(empty)
```

Interpretação:

1. espera existir item;
2. entra na região crítica;
3. retira item;
4. libera a região crítica;
5. informa que surgiu uma posição livre.

---

# 5.11 Qual semáforo faz o quê?

| Semáforo | Função |
|---|---|
| `mutex` | Exclusão mútua |
| `empty` | Sincronização relacionada a posições vazias |
| `full` | Sincronização relacionada a itens disponíveis |

### Frase para memorizar

> **mutex protege.**
>
> **empty controla espaço.**
>
> **full controla itens.**

---

# 5.12 Erro de ordem dos semáforos

A ordem das operações importa.

Se o produtor fizer algo como:

```text
wait(mutex)
wait(empty)
```

e não houver espaço, ele pode ficar bloqueado segurando `mutex`.

O consumidor pode precisar de `mutex` para liberar espaço.

Isso pode produzir **deadlock**.

Portanto, ao estudar os códigos da lista, observe:

1. qual semáforo é adquirido;
2. quando o processo pode bloquear;
3. quais recursos ele está segurando no momento do bloqueio;
4. quais outros processos precisam desses recursos para continuar.

---

# 5.13 Monitores

Um **monitor** é uma abstração de alto nível para comunicação/sincronização.

A ideia é agrupar:

- dados compartilhados;
- operações sobre esses dados;
- mecanismo de exclusão mútua;
- variáveis condicionais.

### Característica fundamental

A entrada no monitor fornece **exclusão mútua**.

Conceitualmente:

```text
Processo A ──┐
Processo B ──┼──> MONITOR
Processo C ──┘
```

Somente um processo pode estar executando dentro da parte protegida do monitor por vez, conforme o modelo utilizado.

---

# 5.14 Variáveis condicionais

Variáveis condicionais permitem que um processo dentro do monitor espere por determinada condição.

Operações clássicas:

- `wait`;
- `signal`.

### `wait`

O processo deixa de executar no monitor e espera pela condição.

### `signal`

Sinaliza que a condição pode ter sido satisfeita, permitindo que um processo esperando prossiga conforme a semântica do monitor.

---

# 5.15 Produtor/Consumidor com monitor

Podemos imaginar duas condições:

```text
not_full
not_empty
```

### `not_full`

Usada pelo produtor quando o buffer está cheio.

```text
se buffer cheio:
    wait(not_full)
```

Quando o consumidor remove um item:

```text
signal(not_full)
```

### `not_empty`

Usada pelo consumidor quando o buffer está vazio.

```text
se buffer vazio:
    wait(not_empty)
```

Quando o produtor coloca um item:

```text
signal(not_empty)
```

### Memorize

```text
buffer cheio
    ↓
produtor espera not_full

buffer vazio
    ↓
consumidor espera not_empty
```

---

# 5.16 Semáforos × Monitores

| Critério | Semáforos | Monitores |
|---|---|---|
| Facilidade de implementação | Mais baixo nível | Mais alto nível |
| Facilidade de evitar erros | Exige mais cuidado | Abstração facilita |
| Suporte por linguagens | Depende da linguagem | Pode ser integrado à linguagem |
| Exclusão mútua | Programador controla explicitamente | Estrutura do monitor fornece |
| Condições | Usa semáforos | Usa variáveis condicionais |
| Máquinas distintas | Semáforos locais não resolvem diretamente comunicação distribuída | Monitor tradicional também não implica comunicação entre máquinas |

### Regra para comparação

> **Semáforo oferece mais controle explícito.**
>
> **Monitor oferece abstração mais estruturada.**

---

# 5.17 Troca de mensagens

Outra abordagem é não depender diretamente de memória compartilhada.

Os processos trocam mensagens.

Operações conceituais:

```text
send(destino, mensagem)
receive(origem, mensagem)
```

### Exemplo

```text
Processo A
   |
   | send
   v
[ mensagem ]
   |
   v
Processo B
   |
   | receive
   v
```

A comunicação passa a ocorrer por mensagens.

---

# 5.18 Sincronização com troca de mensagens

A troca de mensagens também pode realizar sincronização.

Por exemplo, um processo pode ficar bloqueado esperando uma mensagem.

```text
receive()
   ↓
não chegou mensagem
   ↓
bloqueia
   ↓
mensagem chega
   ↓
processo pode continuar
```

Isso permite combinar:

- comunicação;
- sincronização.

---

# 5.19 Caixa Postal

A **Caixa Postal** funciona como um local intermediário para mensagens.

Conceitualmente:

```text
Processo A → Caixa Postal → Processo B
```

O produtor da mensagem não precisa necessariamente estar executando exatamente ao mesmo tempo que o consumidor.

### Vantagem

Maior flexibilidade temporal.

---

# 5.20 Rendezvous

No **Rendezvous**, os processos comunicantes precisam sincronizar a comunicação.

A ideia é semelhante a um encontro:

```text
A → espera B
B → espera A
     ↓
Rendezvous
     ↓
comunicação
```

### Comparação

| Caixa Postal | Rendezvous |
|---|---|
| Possui intermediário | Comunicação exige encontro/sincronização |
| Mais flexível | Mais sincronizado |
| Mensagem pode ficar armazenada | Processos precisam participar da comunicação |
| Desacopla mais os processos | Acopla mais a execução |

---

# 5.21 Barreiras

Uma **barreira** é um mecanismo de sincronização em que vários processos/Threads precisam chegar a determinado ponto antes que possam continuar.

Exemplo:

```text
A ───────────┐
B ────────┐  |
C ──────┐ |  |
         ↓ ↓  ↓
       BARREIRA
           ↓
      todos continuam
```

Se a barreira exige 3 processos:

```text
A chegou → espera
B chegou → espera
C chegou → libera todos
```

### Quando usar?

É especialmente útil em algoritmos divididos em fases.

Exemplo:

```text
Fase 1 → todos calculam
         ↓
      barreira
         ↓
Fase 2 → todos calculam
         ↓
      barreira
         ↓
Fase 3
```

---

# 6. Problemas Clássicos de Comunicação entre Processos

> **Atenção:** o arquivo específico da Lista 06 não foi anexado nesta conversa. Portanto, esta seção serve como complemento de estudo para o tema informado, e não deve ser tratada como transcrição das perguntas da Lista 06.

Os problemas clássicos são usados para testar se você entende:

- exclusão mútua;
- sincronização;
- bloqueio;
- semáforos;
- monitores;
- condições de corrida;
- deadlock;
- starvation;
- compartilhamento de recursos.

---

# 6.1 Produtor/Consumidor

É o problema já presente na Lista 05.

Temos:

```text
Produtor → Buffer → Consumidor
```

### Regras

O produtor:

- não pode inserir se o buffer estiver cheio.

O consumidor:

- não pode retirar se o buffer estiver vazio.

Além disso:

- acesso ao buffer precisa ser protegido;
- produtores/consumidores precisam ser sincronizados.

### Conceitos envolvidos

- exclusão mútua;
- semáforos;
- monitores;
- sleep/wakeup;
- troca de mensagens.

---

# 6.2 Leitores e Escritores

Existe um recurso compartilhado, como um arquivo ou banco de dados.

Temos:

- **leitores:** apenas leem;
- **escritores:** modificam.

### Regra principal

Vários leitores podem acessar simultaneamente:

```text
Leitor A ─┐
Leitor B ─┼──> recurso
Leitor C ─┘
```

Mas um escritor precisa de acesso exclusivo:

```text
Escritor
   ↓
recurso
```

Enquanto um escritor está modificando:

```text
nenhum leitor
nenhum outro escritor
```

deve acessar o recurso de maneira concorrente incompatível.

### Problema de starvation

Uma política pode favorecer leitores continuamente:

```text
R R R R R R R R ...
        ↓
       W
```

O escritor pode ficar esperando indefinidamente.

Também é possível criar políticas que favoreçam escritores, gerando o problema inverso.

### O que estudar

Ao resolver uma questão de leitores/escritores, procure identificar:

1. quem pode compartilhar;
2. quem precisa de exclusividade;
3. qual grupo pode sofrer starvation;
4. onde ocorre a região crítica;
5. qual mecanismo garante sincronização.

---

# 6.3 Jantar dos Filósofos

É um problema clássico de concorrência.

Imagine filósofos sentados ao redor de uma mesa.

Cada filósofo alterna entre:

```text
pensar → comer → pensar → ...
```

Para comer, precisa de dois recursos compartilhados, normalmente representados por garfos.

```text
       F
    P     P
   F       F
    P     P
       F
```

Cada filósofo precisa de dois garfos adjacentes.

---

# 6.4 Deadlock no jantar dos filósofos

Uma situação problemática:

```text
Todos pegam o garfo da esquerda
```

Então:

```text
P1 espera garfo direito
P2 espera garfo direito
P3 espera garfo direito
P4 espera garfo direito
P5 espera garfo direito
```

Ninguém consegue continuar.

Temos uma espera circular:

```text
P1 espera P2
P2 espera P3
P3 espera P4
P4 espera P5
P5 espera P1
```

Isso é um exemplo de **deadlock**.

---

# 6.5 Como pensar em soluções para filósofos?

Uma solução precisa impedir a situação problemática.

Estratégias clássicas incluem:

- limitar o número de filósofos que podem tentar pegar recursos simultaneamente;
- impor uma ordem global para aquisição dos recursos;
- fazer um filósofo pegar primeiro um recurso diferente dos demais;
- usar um mecanismo de monitor/semafóro que controle quando um filósofo pode comer.

### Ideia importante

O objetivo não é simplesmente “fazer funcionar”.

A solução precisa considerar:

- exclusão mútua;
- deadlock;
- starvation;
- progresso.

---

# 6.6 Barbeiro dorminhoco

Outro problema clássico.

Temos:

- um barbeiro;
- uma cadeira de atendimento;
- uma sala de espera com número limitado de cadeiras;
- clientes.

### Situações

Se não há clientes:

```text
barbeiro dorme
```

Se chega cliente e há espaço:

```text
cliente espera
```

Se barbeiro está livre:

```text
cliente é atendido
```

Se não há cadeira disponível:

```text
cliente vai embora
```

### Conceitos envolvidos

- sleep/wakeup;
- semáforos;
- sincronização;
- recursos limitados;
- exclusão mútua.

---

# 6.7 O que é deadlock?

**Deadlock** ocorre quando processos ficam presos esperando recursos/eventos que não permitem que o sistema avance.

Exemplo:

```text
A possui recurso 1 e espera recurso 2.
B possui recurso 2 e espera recurso 1.
```

```text
A ──possui──> R1
A ──espera──> R2

B ──possui──> R2
B ──espera──> R1
```

Ninguém consegue prosseguir.

---

# 6.8 Deadlock × starvation

Não confunda.

### Deadlock

Um conjunto de processos fica bloqueado de maneira circular.

```text
A espera B
B espera A
```

### Starvation

Um processo fica esperando indefinidamente porque outros continuam recebendo preferência.

```text
A espera
B executa
C executa
B executa
C executa
B executa
...
A nunca recebe oportunidade
```

### Memorize

> **Deadlock = bloqueio mútuo/circular.**
>
> **Starvation = espera indefinida por falta de oportunidade.**

---

# 6.9 Condição de corrida × Deadlock × Starvation

| Problema | O que acontece? |
|---|---|
| Condição de corrida | Resultado depende da ordem das execuções |
| Deadlock | Processos ficam presos esperando uns pelos outros |
| Starvation | Um processo espera indefinidamente enquanto outros continuam |
| Busy waiting | Processo fica consumindo CPU enquanto espera |

---

# 6.10 O que significa uma operação atômica?

Uma operação **atômica** deve ser tratada como indivisível do ponto de vista da concorrência.

Não existe uma interlevação inadequada no meio da operação.

Exemplo conceitual:

```text
TESTAR + ALTERAR
```

pode precisar ser uma única operação atômica.

Isso é fundamental em:

- TSL;
- operações de semáforo;
- mecanismos de lock;
- sincronização.

---

# 6.11 Como identificar o mecanismo adequado?

Quando encontrar uma questão de comunicação entre processos, faça estas perguntas:

### 1. Existe memória compartilhada?

Se sim, pense em:

- região crítica;
- exclusão mútua;
- locks;
- semáforos;
- monitores.

### 2. Existe espera?

Se sim, pergunte:

> O processo fica consumindo CPU ou é bloqueado?

- consumindo CPU → espera ociosa/busy waiting;
- bloqueado → comunicação com bloqueio.

### 3. Existe uma condição que precisa ser satisfeita?

Pense em:

- semáforo;
- variável condicional;
- sleep/wakeup;
- mensagens.

### 4. Existem recursos múltiplos?

Pense em:

- ordem de aquisição;
- deadlock;
- starvation.

### 5. A comunicação ocorre por mensagens?

Pense em:

- send;
- receive;
- caixa postal;
- rendezvous.

---

# 6.12 Mapa mental geral

```text
PROCESSOS E THREADS
│
├── PROCESSOS
│   ├── Programa × Processo
│   ├── Estados
│   ├── Tabela de Processos
│   ├── Escalonador
│   ├── Interrupções / RSI
│   ├── Criação
│   └── Término
│
├── THREADS
│   ├── Unidade de execução
│   ├── PC
│   ├── Registradores
│   ├── Pilha
│   ├── Estado
│   ├── Compartilhamento
│   ├── Usuário
│   ├── Núcleo
│   └── Híbrido
│
├── ESCALONAMENTO
│   ├── FCFS
│   ├── Tarefa mais curta
│   ├── Menor tempo restante
│   ├── Round Robin
│   ├── Prioridades
│   ├── Filas múltiplas
│   ├── Garantido
│   ├── Loteria
│   └── Fração justa
│
└── COMUNICAÇÃO
    ├── Região crítica
    ├── Condição de corrida
    ├── Exclusão mútua
    ├── Espera ociosa
    │   ├── Interrupções
    │   ├── Lock
    │   ├── Chaveamento
    │   ├── Peterson
    │   └── TSL
    │
    └── Bloqueio
        ├── Sleep / Wakeup
        ├── Semáforos
        ├── Monitores
        ├── Mensagens
        ├── Caixa Postal
        ├── Rendezvous
        └── Barreiras
```

---

# 7. Como estudar para responder as listas

## Lista 01 — Processos

Você precisa conseguir explicar sem consultar:

- diferença entre programa e processo;
- estados;
- transições;
- escalonador;
- Tabela de Processos;
- por que a tabela é dinâmica;
- RSI;
- Arranjo de Interrupções;
- relação RSI × escalonador × Tabela de Processos;
- sistemas multiprocessados;
- criação;
- processo-pai e filho;
- espaços de endereçamento;
- hierarquia;
- término.

### Pergunta-chave

> **“Como o SO consegue alternar entre vários processos?”**

Se você souber responder isso relacionando **Tabela de Processos + interrupção/RSI + escalonador + contexto**, domina boa parte da Lista 01.

---

# 8. Como estudar a Lista 02 — Threads

Domine estas comparações:

```text
Processo × Thread

Thread de usuário × Thread de núcleo

Usuário × Núcleo × Híbrido
```

Principalmente:

- o que é próprio da Thread;
- o que é compartilhado pelo processo;
- vantagens/desvantagens;
- Runtime;
- papel do SO;
- custo de criação;
- custo de chaveamento;
- bloqueio;
- múltiplos algoritmos de escalonamento.

---

# 9. Como estudar a Lista 03 — Escalonamento

Aqui existem dois tipos de preparação.

## Parte conceitual

Saiba explicar:

- escalonador;
- algoritmo;
- CPU-bound;
- I/O-bound;
- preempção;
- sistemas em lote;
- sistemas interativos;
- política;
- mecanismo;
- escalonamento de Threads.

## Parte de cálculo

Treine desenhar:

```text
0    4    9    14    20
|----|----|-----|-----|
```

e aplicar:

```text
FCFS
SJF
SRTF
Round Robin
Prioridades
Filas múltiplas
Fração justa
```

### Checklist para qualquer exercício numérico

- [ ] Liste processos.
- [ ] Anote chegada.
- [ ] Anote duração.
- [ ] Anote prioridade, se existir.
- [ ] Anote quantum, se existir.
- [ ] Veja quais processos estão disponíveis em cada instante.
- [ ] Aplique a regra do algoritmo.
- [ ] Marque cada troca.
- [ ] Encontre o instante de término de cada processo.
- [ ] Calcule a métrica solicitada.
- [ ] Faça a média.

---

# 10. Como estudar as Listas 04 e 05

A sequência lógica é:

```text
Problema
   ↓
Condição de corrida
   ↓
Região crítica
   ↓
Exclusão mútua
   ↓
Soluções com espera ociosa
   ↓
Problemas da espera ociosa
   ↓
Bloqueio
   ↓
Sleep/Wakeup
   ↓
Semáforos
   ↓
Monitores
   ↓
Troca de mensagens
   ↓
Barreiras
```

Se você entender essa evolução, as duas listas ficam muito mais fáceis.

---

# 11. Comparações que vale decorar

## Busy waiting × bloqueio

**Busy waiting:**

```text
espera → continua usando CPU
```

**Bloqueio:**

```text
espera → deixa de usar CPU
```

---

## Lock × Semáforo

**Lock:**

> normalmente representa ocupado/livre e é usado principalmente para exclusão mútua.

**Semáforo:**

> permite controlar recursos e sincronização, podendo fazer processos esperar/bloquear.

---

## Semáforo × Monitor

**Semáforo:**

> mais explícito e de nível mais baixo.

**Monitor:**

> abstração estruturada com exclusão mútua e condições.

---

## Caixa Postal × Rendezvous

**Caixa Postal:**

> usa intermediário e permite maior desacoplamento temporal.

**Rendezvous:**

> exige sincronização/encontro dos processos comunicantes.

---

## Processo × Thread

**Processo:**

> unidade mais isolada, com seu próprio ambiente de recursos.

**Thread:**

> fluxo de execução dentro do processo, compartilhando recursos com outras Threads do mesmo processo.

---

# 12. Checklist final antes da prova

## Processos

- [ ] Sei diferenciar programa e processo.
- [ ] Sei explicar todos os estados.
- [ ] Sei explicar a Tabela de Processos.
- [ ] Sei explicar por que ela é dinâmica.
- [ ] Sei explicar RSI e Arranjo de Interrupções.
- [ ] Sei relacionar RSI, escalonador e tabela.
- [ ] Sei explicar processo-pai e processo-filho.
- [ ] Sei explicar criação e término.

## Threads

- [ ] Sei explicar o que é uma Thread.
- [ ] Sei dizer o que cada Thread possui individualmente.
- [ ] Sei dizer o que as Threads compartilham.
- [ ] Sei comparar Threads e processos.
- [ ] Sei explicar Runtime.
- [ ] Sei comparar usuário, núcleo e híbrido.

## Escalonamento

- [ ] Sei diferenciar escalonador e algoritmo.
- [ ] Sei diferenciar CPU-bound e I/O-bound.
- [ ] Sei explicar preempção.
- [ ] Sei explicar FCFS.
- [ ] Sei explicar SJF.
- [ ] Sei explicar SRTF.
- [ ] Sei explicar Round Robin.
- [ ] Sei explicar prioridades.
- [ ] Sei explicar filas múltiplas.
- [ ] Sei explicar garantido.
- [ ] Sei explicar loteria.
- [ ] Sei explicar fração justa.
- [ ] Sei montar uma escala de CPU.
- [ ] Sei calcular a métrica pedida.

## Comunicação

- [ ] Sei explicar condição de corrida.
- [ ] Sei explicar região crítica.
- [ ] Sei explicar exclusão mútua.
- [ ] Sei explicar as quatro condições da Lista 04.
- [ ] Sei explicar desligamento de interrupções.
- [ ] Sei explicar lock.
- [ ] Sei explicar chaveamento obrigatório.
- [ ] Sei explicar Peterson.
- [ ] Sei explicar TSL.
- [ ] Sei explicar busy waiting.
- [ ] Sei explicar sleep/wakeup.
- [ ] Sei explicar semáforos.
- [ ] Sei explicar operações atômicas.
- [ ] Sei resolver mentalmente produtor/consumidor.
- [ ] Sei explicar monitores.
- [ ] Sei explicar variáveis condicionais.
- [ ] Sei explicar troca de mensagens.
- [ ] Sei diferenciar caixa postal e rendezvous.
- [ ] Sei explicar barreiras.
- [ ] Sei diferenciar condição de corrida, deadlock e starvation.

---

# 13. Resumo de uma página

Se você tiver pouquíssimo tempo, memorize isto:

### Processo

> Programa em execução + estado + informações/recursos necessários.

### Thread

> Unidade de execução dentro de um processo.

### Tabela de Processos

> Guarda informações necessárias para o SO controlar os processos.

### Escalonador

> Decide quem utiliza a CPU.

### RSI

> Rotina que trata uma interrupção.

### CPU-bound

> Muito tempo computando.

### I/O-bound

> Muito tempo esperando E/S.

### Preempção

> Retirar a CPU de um processo antes de ele terminar/bloquear.

### FCFS

> Primeiro que chega, primeiro que executa.

### SJF

> Menor duração primeiro.

### SRTF

> Menor tempo restante primeiro, com possibilidade de preempção.

### Round Robin

> Cada processo recebe um quantum.

### Prioridades

> Executa o processo de maior prioridade.

### Filas múltiplas

> Processos distribuídos em filas com regras diferentes.

### Loteria

> Mais bilhetes = maior chance.

### Fração justa

> Distribuição considerando usuários.

### Condição de corrida

> Resultado depende da ordem da execução concorrente.

### Região crítica

> Trecho que acessa recurso compartilhado de maneira que precisa de coordenação.

### Exclusão mútua

> Só um processo por vez na região crítica incompatível.

### Busy waiting

> Esperar consumindo CPU.

### Sleep

> Bloquear o processo enquanto espera.

### Wakeup

> Acordar/liberar processo que estava esperando.

### Semáforo

> Mecanismo de sincronização; operações precisam ser atômicas.

### Monitor

> Abstração de alto nível para dados compartilhados + exclusão mútua + condições.

### Mensagens

> Processos se comunicam por `send`/`receive`.

### Caixa Postal

> Mensagens passam por um intermediário.

### Rendezvous

> Processos sincronizam para realizar a comunicação.

### Barreira

> Todos precisam chegar a determinado ponto antes de continuar.

### Deadlock

> Processos presos em espera circular.

### Starvation

> Processo espera indefinidamente por falta de oportunidade.

### Regra de ouro

> **Em qualquer problema de comunicação entre processos, procure primeiro:**
>
> **1. Qual recurso é compartilhado?**
>
> **2. Qual é a região crítica?**
>
> **3. Quem pode executar simultaneamente?**
>
> **4. Quem precisa esperar?**
>
> **5. A espera consome CPU ou bloqueia?**
>
> **6. Existe risco de corrida, deadlock ou starvation?**

---

## Fonte das seções 1–5

As anotações das seções 1–5 foram organizadas a partir das listas fornecidas da disciplina **PEX0134 — Sistemas Operacionais**, cobrindo respectivamente:

1. **01 — Processos**
2. **02 — Threads**
3. **03 — Escalonamento de Processos e Threads**
4. **04 — Comunicação Entre Processos com Espera Ociosa**
5. **05 — Comunicação Entre Processos com Bloqueio**

A **Lista 06** não estava entre os arquivos anexados nesta conversa; sua seção foi incluída como complemento de estudo a partir do tema informado pelo usuário.
