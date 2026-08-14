# Seção 2 — Threads

> Base para a **Lista 02 — Processos e Threads: Threads**
> Referência: Tanenbaum, *Sistemas Operacionais Modernos*, cap. 2.2

---

## 2.1 O modelo de threads clássico

### O que são threads

Uma **thread** (ou *processo leve*) é uma **linha de execução independente dentro de um
processo**. Ela é a unidade que o escalonador realmente coloca na CPU.

A ideia central é **separar dois conceitos que o modelo de processos misturava**:

| Conceito | Quem representa |
|---|---|
| **Agrupamento de recursos** (memória, arquivos abertos, sinais, filhos) | O **processo** |
| **Linha de execução** (PC, registradores, pilha, estado) | A **thread** |

O processo passa a ser o "contêiner" de recursos; as threads são as entidades que
executam dentro dele. Todo processo tem **pelo menos uma** thread. Quando há mais de uma,
fala-se em **multithreading**.

**Relação entre threads e processos:**
- Threads existem **dentro** de um processo e compartilham seus recursos.
- Threads do mesmo processo **não são protegidas umas das outras** — e nem deveriam ser,
  pois cooperam. Processos distintos, ao contrário, são de usuários possivelmente hostis e
  precisam de proteção mútua.
- Threads compartilham o **mesmo espaço de endereçamento**; processos, não.

### O que é privado × o que é compartilhado

| Por **thread** (privado) | Por **processo** (compartilhado entre todas as threads) |
|---|---|
| Contador de programa (PC) | Espaço de endereçamento (código, dados globais, heap) |
| Conjunto de registradores | Arquivos abertos |
| Pilha | Processos-filhos |
| Estado (executando/pronto/bloqueado) | Sinais e tratadores de sinais |
| — | Alarmes pendentes |
| — | Informações de contabilização |

### Por que cada thread precisa de PC, registradores, pilha e estado próprios (questão 1.b)

Porque **cada thread executa um trecho diferente do código, em um ponto diferente, e de
forma independente das demais**:

- **Contador de programa próprio**: cada thread está executando uma instrução diferente.
  Se o PC fosse compartilhado, ao chavear de uma thread para outra o SO não saberia onde
  cada uma parou — todas seriam forçadas ao mesmo ponto de execução.
- **Conjunto de registradores próprio**: os registradores guardam as **variáveis de
  trabalho do momento**. Ao ser retirada da CPU, a thread precisa que esses valores sejam
  salvos e restaurados intactos, senão seus cálculos são corrompidos pela outra thread.
- **Pilha própria**: a pilha guarda o **histórico de chamadas de função** — um quadro por
  procedimento chamado e ainda não retornado, com parâmetros, variáveis locais e endereço
  de retorno. Como cada thread chama procedimentos diferentes, em ordem diferente, cada
  uma tem uma **história de execução distinta** e portanto precisa da sua própria pilha.
- **Estado próprio**: uma thread pode estar bloqueada esperando E/S enquanto outra está
  executando e outra está pronta. O estado é uma propriedade da linha de execução, não do
  contêiner de recursos.

> **Resumo:** esses quatro itens formam o **contexto de execução**. Sem eles, as threads
> não seriam linhas de execução independentes — seriam a mesma linha.

### Estados de uma thread (questão 1.c)

São **os mesmos** do modelo de processos, com as **mesmas transições**:

| Estado | Significado |
|---|---|
| **Em execução** | Ocupa a CPU |
| **Pronta** | Apta a executar, aguardando escalonamento |
| **Bloqueada** | Aguardando um evento (E/S, outra thread, semáforo/mutex) |
| *(Concluída)* | Terminou sua função |

**Transições:**
1. **Execução → Bloqueada**: a thread executa uma chamada bloqueante (E/S,
   `thread_join`, espera em condição/semáforo) ou aguarda um recurso.
2. **Execução → Pronta**: preempção — fim do *quantum*, chegada de thread mais
   prioritária, ou a própria thread cede voluntariamente a CPU (`thread_yield`).
3. **Pronta → Execução**: o escalonador (do núcleo ou o *runtime*) a escolheu.
4. **Bloqueada → Pronta**: o evento aguardado ocorreu (E/S concluída, `signal` recebido,
   semáforo liberado).

> **Detalhe importante:** `thread_yield` não existe para processos. Como não há
> interrupção de relógio no escalonamento de threads em espaço de usuário, é essencial que
> as threads **cedam a CPU voluntariamente** para que as irmãs progridam.

### Por que threads do mesmo processo compartilham informações (questão 1.d)

Três razões que se reforçam:

1. **Razão de projeto — elas cooperam.** Threads são criadas para executar **partes de uma
   mesma aplicação**. Um processador de texto com uma thread para interagir com o
   usuário, outra para reformatar o documento e outra para salvar em disco só faz sentido
   se **todas enxergarem o mesmo documento na memória**. Se cada uma tivesse memória
   própria, seria preciso comunicação interprocessos cara para trocar o texto.
2. **Razão de desempenho.** Compartilhar memória é a forma mais barata de comunicação:
   basta escrever em uma variável global. Criar, terminar e chavear threads é ordens de
   grandeza mais rápido que fazer o mesmo com processos exatamente porque **não é preciso
   trocar o espaço de endereçamento** (não há troca de tabela de páginas nem invalidação
   de TLB/cache).
3. **Razão conceitual.** O processo é a unidade de **agrupamento de recursos**; a thread
   é a unidade de **execução**. Os recursos pertencem ao processo por definição, logo são
   comuns a todas as threads dele.

> **Consequência que liga com as listas 4, 5 e 6:** justamente por compartilharem memória,
> threads estão sujeitas a **condições de corrida**, e por isso precisam de mecanismos de
> **exclusão mútua** (mutexes, semáforos, monitores).

### Vantagens e desvantagens: threads × processos (questão 1.e)

**Vantagens das threads**

| Vantagem | Explicação |
|---|---|
| **Criação/término muito mais rápidos** | Tipicamente 10 a 100× mais rápido que criar processo — não é preciso criar espaço de endereçamento, tabela de páginas, copiar descritores etc. |
| **Chaveamento mais barato** | Não há troca de espaço de endereçamento; TLB e cache continuam válidos. |
| **Comunicação trivial e barata** | Memória compartilhada; não requer chamadas de sistema nem cópias. |
| **Sobreposição de CPU e E/S** | Enquanto uma thread bloqueia em E/S, outra continua computando dentro da mesma aplicação. |
| **Paralelismo real em multiprocessadores** | Threads podem rodar simultaneamente em núcleos diferentes, acelerando **uma única** aplicação. |
| **Simplificação do modelo de programação** | Permite decompor uma aplicação naturalmente concorrente em atividades quase sequenciais. |

**Desvantagens das threads**

| Desvantagem | Explicação |
|---|---|
| **Ausência de proteção mútua** | Uma thread pode corromper os dados das outras; um erro fatal em uma derruba o processo inteiro. |
| **Condições de corrida** | O compartilhamento exige sincronização explícita, fonte comum de bugs difíceis (deadlocks, corrupções intermitentes). |
| **Problemas com bibliotecas não reentrantes** | Variáveis globais como `errno`, `malloc`, funções que retornam ponteiros estáticos. |
| **Semântica confusa de chamadas herdadas** | O que `fork()` deve fazer? Copiar todas as threads? Só a que chamou? Como distribuir sinais? |
| **Menor robustez** | Todas as threads partilham o destino do processo. |

**Quando usar cada um**

- **Use threads** quando: as atividades **cooperam intensamente e compartilham dados**;
  há muita E/S a sobrepor com computação; a aplicação precisa continuar responsiva;
  criação/destruição de linhas de execução é frequente; deseja-se acelerar **uma
  aplicação** em multiprocessador. *Exemplos:* processador de texto, servidor web,
  navegador (aba renderizando enquanto outra baixa), planilha recalculando em segundo plano.
- **Use processos** quando: as atividades são **independentes** ou vêm de fontes não
  confiáveis; é preciso **isolamento e proteção** (uma falha não pode derrubar o resto);
  o sistema é distribuído em máquinas distintas; os componentes têm ciclos de vida e
  permissões diferentes. *Exemplos:* shell executando comandos do usuário; navegadores
  modernos que isolam cada aba/plugin em processo próprio por segurança; serviços de
  usuários distintos.

---

## 2.2 Implementação de threads

Há três esquemas: **espaço do usuário**, **núcleo** e **híbrido**.

### (A) Threads no espaço do usuário

**Como funciona:**
- O **núcleo não sabe** que existem threads. Para ele, o processo é uma linha única de
  execução, como no modelo tradicional.
- Toda a gerência é feita por uma biblioteca em nível de usuário chamada **runtime
  (sistema de suporte de execução)**, ligada à aplicação.
- Cada processo tem sua **própria tabela de threads**, mantida **no espaço do usuário**,
  gerenciada pelo runtime. Ela guarda, por thread, PC, registradores, ponteiro de pilha,
  estado — os mesmos campos da tabela de processos do núcleo, mas para threads.

**Papel de cada entidade:**

| Entidade | Papel |
|---|---|
| **Sistema Operacional** | Escalona **processos**. Salva/restaura o contexto do processo na tabela de processos. Ignora completamente a existência de threads. |
| **Runtime** | Cria, termina, bloqueia, sincroniza e **escalona as threads** do processo. Mantém a tabela de threads. |

**Como o runtime escalona threads do mesmo processo:**
Quando uma thread executa `thread_yield`, `thread_exit` ou uma operação que a bloqueia,
ela chama um **procedimento local do runtime** (não uma chamada de sistema). O runtime:
1. Salva os registradores da thread corrente **na tabela de threads** (basta salvar em
   variáveis locais — não há mudança de modo, nem chamada de sistema, nem
   descarregamento de cache);
2. Escolhe a próxima thread pronta pelo seu algoritmo próprio;
3. Recarrega os registradores dessa thread e altera o ponteiro de pilha e o PC.

Tudo isso são **poucas instruções de máquina**, sem *trap* para o núcleo — daí ser
tipicamente **uma ordem de grandeza mais rápido** que o chaveamento pelo núcleo.

### (B) Threads no núcleo

**Como funciona:**
- **Não existe runtime.** O núcleo conhece e gerencia as threads diretamente.
- O núcleo mantém uma **tabela de threads global**, com uma entrada por thread do sistema
  (além da tabela de processos).
- Toda operação sobre threads (criar, terminar, esperar, sincronizar) é uma **chamada de
  sistema**, que causa mudança de modo usuário→núcleo.

**Papel do SO:** faz **tudo** — cria, termina, bloqueia, escalona threads e processos,
salva e restaura contextos. Escalona **threads**, não processos: pode escolher qualquer
thread do sistema, de qualquer processo.

**Por que não existe runtime aqui:** o runtime só existe para *simular*, no espaço do
usuário, funções que o núcleo não oferece. Se o núcleo já implementa nativamente toda a
gerência de threads, a camada intermediária se torna redundante — a aplicação chama o
núcleo diretamente.

### (C) Por que só o modo usuário permite algoritmos de escalonamento distintos por processo (questão 2.c)

- No **espaço do usuário**, o escalonador de threads é **o runtime de cada processo** —
  ou seja, **código da própria aplicação**. Cada processo pode ser ligado a um runtime
  diferente e, portanto, usar um algoritmo diferente (circular, por prioridades,
  cooperativo, específico do domínio). Um servidor web pode usar um algoritmo e um
  compilador outro, simultaneamente, na mesma máquina.
- No **núcleo**, o escalonador de threads é **o escalonador do SO** — uma única
  implementação, compartilhada por todos os processos do sistema. Ele não pode ser
  substituído por aplicação, pois é código do kernel. O máximo que se oferece são
  parâmetros (prioridade, classe/política de escalonamento), mas o **algoritmo em si** é
  um só. Permitir que cada aplicação injetasse seu próprio algoritmo no núcleo criaria
  problemas graves de segurança e estabilidade.

### (D) Comparação sistemática (questão 2.d)

| Aspecto | Espaço do usuário | Núcleo |
|---|---|---|
| **Uso em SOs que não suportam threads** | **Possível.** Basta ligar a biblioteca de runtime à aplicação; o SO nem precisa saber. Foi assim que threads surgiram historicamente. | **Impossível.** Depende de suporte nativo do núcleo — se o SO não implementa, não há threads. |
| **Custo de criação, término, chaveamento e bloqueio** | **Muito baixo.** Tudo é chamada de procedimento local: salvar registradores em variáveis, trocar SP e PC. Sem *trap*, sem mudança de modo, sem invalidar cache. Ordens de grandeza mais rápido. | **Alto.** Toda operação é uma **chamada de sistema**: mudança de modo usuário↔núcleo, salvamento de contexto completo, possível descarregamento de cache. Mesmo sendo mais barato que criar processos, é bem mais caro que no espaço do usuário. |
| **Tratamento de chamadas bloqueantes dentro de uma thread** | **Difícil — é o problema central deste esquema.** Como o núcleo vê apenas o processo, uma chamada bloqueante feita por **uma** thread bloqueia **o processo inteiro**, parando todas as outras threads mesmo que estivessem prontas. Contornos possíveis, todos imperfeitos: (i) *jacket/wrapper* em torno das chamadas; (ii) chamada `select` para testar antes se a operação bloquearia; (iii) E/S não bloqueante. Todos exigem reescrever partes da biblioteca do sistema e nem sempre são possíveis. **Faltas de página** têm o mesmo problema. | **Fácil e natural.** O núcleo sabe que aquilo que bloqueou foi **uma thread**, não o processo. Ele marca só aquela thread como bloqueada e escalona **outra thread do mesmo processo** (ou de outro). Nenhum truque é necessário. |
| **Preempção entre threads do mesmo processo** | Difícil: não há interrupção de relógio para o runtime. Depende de as threads cederem a CPU voluntariamente (`thread_yield`) — uma thread em laço infinito monopoliza o processo. | Natural: o núcleo usa a interrupção de relógio e preempta threads normalmente. |
| **Paralelismo real em multiprocessador** | Limitado: como o núcleo vê um só fluxo, as threads de um processo tendem a ficar em uma CPU. | Sim: threads do mesmo processo podem rodar em núcleos diferentes ao mesmo tempo. |
| **Consumo de recursos do núcleo** | Nenhum por thread. Escalável para milhares de threads. | Cada thread consome uma entrada na tabela do núcleo + pilha de kernel. Menos escalável. |

### (E) Implementação híbrida (questão 2.e)

**Como funciona:**
- O núcleo enxerga e escalona um **número reduzido de threads de núcleo**.
- Sobre cada thread de núcleo, o runtime **multiplexa várias threads de usuário**
  (modelo *many-to-few*, ou *M:N*).
- O programador decide quantas threads de núcleo usar e quantas threads de usuário
  multiplexar em cada uma.
- Uma variante é a **ativação do escalonador (*scheduler activation*)**: o núcleo, ao
  bloquear uma thread, avisa o runtime por meio de um *upcall*, que então escalona outra
  thread de usuário no lugar — combinando a fácil detecção do bloqueio (núcleo) com o
  chaveamento barato (usuário).

**Semelhanças com o modelo de espaço do usuário:**
- Mantém o **runtime** e a **tabela de threads em espaço do usuário**.
- O chaveamento entre threads de usuário multiplexadas na **mesma** thread de núcleo é
  feito por procedimento local — **barato**, sem chamada de sistema.
- Preserva a **flexibilidade de algoritmos de escalonamento** definidos pela aplicação
  para as threads de usuário.
- Permite criar um **número muito grande** de threads de usuário com pouco custo.

**Semelhanças com o modelo de núcleo:**
- O núcleo **conhece** threads (as de núcleo) e as escalona diretamente.
- Toda operação sobre as **threads de núcleo** é chamada de sistema, com o mesmo custo.
- Permite **paralelismo real**: threads de núcleo distintas podem rodar em CPUs
  diferentes simultaneamente.
- **Bloqueio é bem tratado**: se uma thread de usuário bloqueia e leva junto sua thread de
  núcleo, as **outras threads de núcleo** continuam executando as demais threads de
  usuário — o processo inteiro não para.

**Resumo:** o híbrido busca o **melhor dos dois mundos** — o baixo custo e a flexibilidade
do modelo de usuário, com o paralelismo real e o bom tratamento de bloqueio do modelo de
núcleo. O preço é a **complexidade de implementação**, o que fez vários SOs abandonarem
o M:N em favor do modelo 1:1 (núcleo puro).

---

## Checklist de revisão da Lista 02

- [ ] Sei definir thread e explicar a separação "processo = recursos / thread = execução".
- [ ] Sei preencher a tabela do que é privado por thread × compartilhado por processo.
- [ ] Sei justificar **um a um** por que PC, registradores, pilha e estado são privados.
- [ ] Sei desenhar os estados de uma thread e as 4 transições, citando `thread_yield`.
- [ ] Sei dar as 3 razões (projeto, desempenho, conceitual) do compartilhamento.
- [ ] Sei listar 5 vantagens e 4 desvantagens de threads e dizer quando usar cada modelo.
- [ ] Sei descrever runtime + tabela de threads no usuário, e a tabela global no núcleo.
- [ ] Sei explicar por que não há runtime no modelo de núcleo.
- [ ] Sei o argumento "o runtime é código da aplicação, o escalonador do kernel é único".
- [ ] Sei a tabela comparativa dos 3 aspectos cobrados (suporte do SO, custo, bloqueio).
- [ ] Sei explicar o modelo híbrido e citar 3 semelhanças com cada um dos outros dois.
