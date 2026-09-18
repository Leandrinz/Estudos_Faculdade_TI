# 📚 Lista 1 — Processos e Threads: Processos

---

## Questão 1

### 🅰️ O que é um processo?

> **Definição:** Um processo é um **programa em execução**, junto com todo o contexto necessário para executá-lo.

| Programa | Processo |
|---|---|
| Entidade **passiva** | Entidade **ativa** |
| Arquivo estático em disco | Existe na memória, em execução |
| É uma **receita** (algoritmo + dados de entrada) | É a **atividade de cozinhar** seguindo a receita |
| Não tem estado nem contador de programa | Tem estado, PC, pilha, registradores |
| Um programa pode originar **N** processos | Cada processo está ligado a **um** programa por vez |

---

### 🅱️ Estados de um processo

**Três estados possíveis:**

🟢 **Em execução** (*running*) — 🟡 **Pronto** (*ready*) — 🔴 **Bloqueado** (*blocked*)

**Situações de mudança de estado:**
- Criação
- Escalonamento
- Espera
- Fim do I/O
- Interrupção
- Finalização

---

### 🅲️ O escalonador

> **Definição:** O **escalonador** é parte do SO (no nível mais baixo, abaixo de todos os processos) responsável por **decidir qual processo pronto receberá a CPU** e **por quanto tempo**. Ele implementa um ou mais **algoritmos de escalonamento**.

**Por que é importante em sistemas multiprogramados:**

| Aspecto | Explicação |
|---|---|
| 🔁 Alternância | Sem escalonador, um processo tomaria a CPU indefinidamente e o sistema voltaria a ser monoprogramado |
| ⚖️ Justiça e eficiência | Garante que nenhum processo morra de fome e que a CPU não fique ociosa enquanto processos esperam E/S |
| 📈 Desempenho | Determina o desempenho percebido do sistema (tempo de resposta, vazão, tempo de retorno) |

---

### 🅳️ Tabela de processos

> **Definição:** Estrutura de dados mantida pelo SO com **uma entrada por processo existente**. Cada entrada é chamada de **Bloco de Controle de Processo (PCB)** e guarda tudo que é necessário para reiniciar o processo exatamente de onde ele parou.

**Como funciona a alternância:**

1. A alternância só é possível porque o **contexto fica salvo fora da CPU**
2. Quando um processo é retirado da CPU → seu contexto (registradores, PC, SP) é salvo na sua entrada da tabela de processos
3. Quando ele volta → esse contexto é **restaurado** a partir dali

⚠️ Sem a tabela, a informação se perderia e o processo teria de recomeçar do zero.

---

### 🅴️ Por que a tabela de processos é dinâmica?

Porque a **quantidade de processos no sistema varia continuamente**, e um tamanho fixo poderia gerar:
- Desperdício de memória, **ou**
- Limitação artificial do sistema

| ⬆️ Aumenta quando... | ⬇️ Diminui quando... |
|---|---|
| Um novo processo é criado | Um processo termina |
| Inicialização do sistema | Saída normal |
| Requisição interativa de um usuário (abrir um programa) | Saída por erro |
| Início de uma tarefa em lote | Erro fatal |
| | Morte por outro processo (`kill`) |

---

### 🅵️ RSI e vetor de interrupções

| Conceito | Definição |
|---|---|
| **RSI** | Trecho de código do SO que trata um **tipo específico** de interrupção |
| **Vetor de interrupções** | Tabela de ponteiros indexada pelo **número da interrupção** |

**Fluxo de tratamento:**

```
1. Dispositivo gera a interrupção → coloca no barramento seu número de interrupção
2. Hardware usa esse número como índice no vetor de interrupções
3. Lê nessa posição o endereço da RSI
4. Carrega esse endereço no contador de programa (PC) → desvia a execução para a rotina
```

---

### 🅖️ Ciclo completo de tratamento de interrupção

| # | Etapa | O que acontece |
|---|---|---|
| 1 | **Interrupção ocorre** | O hardware empilha PC e PSW do processo corrente e desvia para a RSI via vetor de interrupções |
| 2 | **Salvamento do contexto (assembly)** | Os demais registradores são salvos e o ponteiro de pilha é trocado para uma pilha do kernel |
| 3 | **Gravação na tabela de processos** | O contexto salvo é gravado na entrada do processo na tabela de processos |
| 4 | **Tratamento (C)** | A RSI escrita em C executa o tratamento (ex: ler o dado do disco) |
| 5 | **Escalonamento** | O escalonador é chamado e decide qual processo executará em seguida |
| 6 | **Restauração** | O contexto do processo escolhido é lido da tabela e carregado nos registradores; a rotina em assembly faz o retorno de interrupção e o processo volta a executar exatamente de onde parou |

---

### 🅷️ Múltiplas CPUs (paralelismo real)

**Vantagens de ter N CPUs/núcleos:**

- ✅ Maior vazão: mais processos concluídos por unidade de tempo
- ✅ Menor tempo de espera
- ✅ Processos limitados por CPU podem rodar em paralelo com processos limitados por E/S
- ✅ Threads de um mesmo processo podem rodar simultaneamente em núcleos distintos, acelerando uma única aplicação
- ✅ Maior tolerância a falhas e possibilidade de dedicar CPUs a tarefas específicas

**Desafios para o escalonador multiprocessado:**

| # | Desafio | Descrição |
|---|---|---|
| 1 | **Decisão dupla** | — |
| 2 | **Afinidade de CPU e cache** | Migrar um processo para outra CPU invalida o conteúdo do cache dessa CPU (cache miss em massa). O escalonador precisa tentar manter o processo na mesma CPU (afinidade), o que conflita com balanceamento |
| 3 | **Balanceamento de carga** | Precisa evitar que uma CPU fique sobrecarregada enquanto outra fica ociosa, sem migrar processos em excesso |
| 4 | **Concorrência sobre as estruturas do SO** | — |
| 5 | **Escalonamento em grupo (gang scheduling)** | Threads que cooperam devem, de preferência, rodar ao mesmo tempo em CPUs diferentes; se uma roda e a outra não, a primeira fica esperando e desperdiça CPU |
| 6 | **Sincronização e coerência de cache** | Adicionam custo a cada chaveamento entre CPUs |
| 7 | **NUMA** | Em máquinas onde o acesso à memória é não uniforme, colocar o processo na CPU "errada" o torna mais lento mesmo sem contenção |

---
---

## Questão 2

### 🅰️ Situações de criação de processos

| # | Situação | Descrição |
|---|---|---|
| 1 | **Início do sistema** | Ao inicializar, o SO cria vários processos: os de **primeiro plano** (interagem com o usuário) e os de **segundo plano/daemons** (não associados a usuários — servidor de e-mail, de impressão, de páginas web, `cron`) |
| 2 | **Chamada de sistema por um processo em execução** | Um processo em execução cria outro via chamada de sistema (`fork()` no UNIX, `CreateProcess()` no Windows). Típico quando o trabalho pode ser decomposto: um processo busca dados na rede enquanto outro os processa |
| 3 | **Requisição do usuário** | O usuário digita um comando, clica num ícone ou abre um programa — o interpretador de comandos/GUI cria o processo. (Tecnicamente é um caso do item 2, mas disparado interativamente) |
| 4 | **Início de uma tarefa em lote** | Em mainframes com processamento em lote, o SO retira a próxima tarefa da fila e cria o processo correspondente quando há recursos disponíveis |

---

### 🅱️ Modelos de espaço de endereçamento

#### (a) Espaços de endereçamento distintos — cópia (modelo UNIX)

O filho recebe uma **cópia** da imagem do pai (código, dados e pilha). Após o `fork()`, alterações feitas por um **não são visíveis** ao outro — são memórias separadas.

| ✅ Vantagem | ❌ Desvantagem |
|---|---|
| Isolamento e proteção; um não corrompe o outro | Copiar tudo é caro |

💡 **Otimização real:** *copy-on-write* (COW) — a memória é compartilhada em modo somente-leitura, e só é duplicada a página que for efetivamente escrita.

#### (b) Espaço de endereçamento compartilhado

Pai e filho **compartilham a mesma memória**. Alterações de um são vistas imediatamente pelo outro.

| ✅ Vantagem | ❌ Desvantagem |
|---|---|
| Comunicação trivial e barata; criação rápida | Condições de corrida, necessidade de exclusão mútua e perda de isolamento (um erro derruba os dois) |

📌 Na prática, é isso que caracteriza **threads** em vez de processos.

---

### 🅲️ Hierarquia de processos

#### (a) Com hierarquia — UNIX

Existe o conceito de **grupo de processos**: pai, filhos, netos etc. formam uma **árvore**.

- A raiz de tudo é o `init` (ou `systemd`), criado na inicialização
- Um sinal enviado ao grupo atinge todos os membros (a menos que cada um decida tratá-lo)
- O pai pode esperar (`wait()`) e coletar o código de término do filho
- Se o pai morre antes do filho, o filho é **"adotado"** pelo `init`, mantendo a árvore consistente

#### (b) Sem hierarquia — Windows

**Não há** conceito de hierarquia: todos os processos são iguais.

- O pai recebe um **handle** (identificador) do filho, que lhe dá controle sobre ele
- Esse handle pode ser passado adiante para outro processo, **quebrando** qualquer relação de parentesco

| ✅ Vantagem | ❌ Desvantagem |
|---|---|
| Mais flexível | Perde-se o agrupamento natural para operações coletivas (como enviar um sinal a toda uma árvore de processos) |

---

### 🅳️ Formas de término de um processo

| # | Situação | Voluntário? | Descrição |
|---|---|:---:|---|
| 1 | **Saída normal** | ✅ Voluntário | O processo terminou seu trabalho e executa a chamada de saída (`exit()` no UNIX, `ExitProcess()` no Windows). Ex.: o compilador terminou de compilar |
| 2 | **Saída por erro** | ✅ Voluntário | O processo descobre um erro **tratável** e decide terminar. Ex.: `gcc arquivo.c` e o arquivo não existe; o compilador emite mensagem e sai com código de erro. Programas interativos costumam preferir pedir novo dado ao usuário em vez de sair |
| 3 | **Erro fatal (involuntário)** | ❌ Involuntário | Um erro **causado pelo programa**, geralmente um bug: divisão por zero, referência inválida à memória (*segmentation fault*), execução de instrução ilegal. O SO mata o processo |
| 4 | **Morto por outro processo** | ❌ Involuntário | Outro processo com **permissão adequada** executa uma chamada para matá-lo (`kill()` no UNIX, `TerminateProcess()` no Windows). O matador precisa ter autorização — normalmente ser do mesmo usuário ou o superusuário |