# Seção 3 — Escalonamento de Processos e Threads

> Base para a **Lista 03 — Processos e Threads: Escalonamento**
> Referência: Tanenbaum, *Sistemas Operacionais Modernos*, cap. 2.4

---

## 3.1 O escalonador de processos

### Definição

O **escalonador** é o componente do SO que **decide qual, dentre os processos prontos,
receberá a CPU** e por quanto tempo. Ele aplica um **algoritmo de escalonamento** para
tomar essa decisão. É invocado sempre que a CPU precisa ser reatribuída.

### Importância para o desempenho

- **A CPU é o recurso mais disputado.** Escolher mal significa CPU ociosa (todos os
  processos escolhidos bloqueiam em E/S) ou dispositivos ociosos (só rodam processos de
  CPU).
- **Custo do chaveamento.** Cada troca de processo custa: mudança de modo, salvamento e
  restauração de contexto, troca de mapa de memória e — o mais caro — **invalidação de
  cache e TLB**. Um escalonador que chaveia demais gasta mais tempo trocando que
  trabalhando.
- **Tempo de resposta.** Em sistemas interativos, o escalonador define se o clique do
  usuário responde em 50 ms ou em 3 s.
- **Justiça e ausência de inanição.** Precisa garantir que nenhum processo espere
  indefinidamente.
- **Cumprimento de prazos.** Em sistemas de tempo real, uma decisão errada pode significar
  perda de um deadline com consequências físicas.
- **Vazão (throughput) e uso da CPU.** Determina quantas tarefas o sistema conclui por
  hora.

> Regra prática de Tanenbaum: quando um sistema tem mais processos prontos do que CPUs,
> **a escolha importa**. Quando não tem, o escalonador é trivial.

---

## 3.2 Comportamento dos processos: os dois tipos (questão 1.c)

Processos alternam **surtos (bursts) de computação** com **períodos de espera por E/S**.
Conforme a proporção entre eles, classificam-se em:

### Processos limitados por CPU (*CPU-bound* / limitados por computação)

- **Surtos de CPU longos**, esperas por E/S raras e curtas.
- Passam a maior parte do tempo **computando**.
- Exemplos: compilação de projeto grande, renderização, cálculo numérico, compressão de
  vídeo, treinamento de modelos.

### Processos limitados por E/S (*I/O-bound*)

- **Surtos de CPU curtos e frequentes**, seguidos de longas esperas por E/S.
- Passam a maior parte do tempo **bloqueados**.
- Exemplos: editor de texto, shell interativo, servidor de banco de dados lendo disco,
  navegador baixando arquivos.
- **Ponto-chave:** o que caracteriza esse tipo **não é a duração da E/S**, mas sim os
  **surtos de CPU curtos** — ele não usa muito a CPU, então pede E/S rapidamente.

### Consequências para o escalonador

- Como as CPUs ficam mais rápidas mais depressa que os discos, **cada vez mais processos
  são limitados por E/S**.
- **Boa política:** dar **prioridade aos processos limitados por E/S**. Eles usam a CPU
  por pouco tempo, disparam sua E/S e liberam a CPU. Assim, o dispositivo de E/S fica
  ocupado em paralelo com um processo CPU-bound rodando — **maximiza o uso simultâneo de
  CPU e dispositivos**.
- Se um processo I/O-bound tiver de esperar muito pela CPU, ele demorará a disparar sua
  E/S, deixando o **disco ocioso** — desperdício duplo.

---

## 3.3 Quando ocorre a troca de processos (questão 1.d)

| # | Situação | O que o escalonador pode decidir |
|---|---|---|
| **1** | **Criação de um novo processo** | Executar o **pai** ou o **filho**. Ambos estão prontos; é uma decisão legítima de política. (No UNIX o filho normalmente vai para a fila de prontos.) |
| **2** | **Término de um processo** | O processo que terminou não pode mais rodar → **é obrigatório escolher outro** processo pronto. Se não houver nenhum, roda-se o processo ocioso (*idle*). |
| **3** | **Bloqueio de um processo** (E/S, semáforo, espera por filho) | O processo bloqueado não pode continuar → **é obrigatório escolher outro**. O escalonador pode levar em conta *por que* ele bloqueou (ex.: quem bloqueou esperando outro processo curto pode ser priorizado depois). |
| **4** | **Interrupção de E/S** | Um processo que estava bloqueado ficou **pronto**. O escalonador decide entre: (a) continuar com o processo corrente, (b) executar o que acabou de ser desbloqueado (preempção), ou (c) escolher um terceiro. |
| **5** | **Interrupção de relógio (*clock*)** | Só existe em algoritmos **preemptivos**. Ao fim de cada tique/*quantum*, o escalonador pode: manter o processo (se o quantum não acabou) ou **retomar a CPU** e escolher outro. |

> **Regra que separa os dois grandes grupos:** nas situações **2 e 3** a troca é
> **inevitável**; nas situações **1, 4 e 5** a troca é **opcional** — e é exatamente aí
> que se distingue um algoritmo **preemptivo** de um **não preemptivo**.

---

## 3.4 Algoritmos de escalonamento: classificação

### Relação com o escalonador (questão 2.a)

O **escalonador** é o **mecanismo** — o código do SO que efetivamente retira um processo
da CPU e coloca outro. O **algoritmo de escalonamento** é a **política** — a regra que
diz *quem* deve ser escolhido. O escalonador **implementa/executa** o algoritmo; trocar
o algoritmo muda o comportamento do sistema sem mudar o mecanismo.

### Critérios de classificação (questão 2.b)

1. **Sensibilidade à interrupção de relógio** → preemptivo × não preemptivo.
2. **Tipo de ambiente/sistema** → lote, interativo, tempo real.
3. **Objetivo otimizado** → vazão, tempo de retorno, tempo de resposta, uso de CPU,
   previsibilidade, justiça.
4. **Uso ou não de prioridades** (e se são estáticas ou dinâmicas).
5. **Necessidade de conhecimento prévio** do tempo de execução.
6. **Número de filas** (fila única × múltiplas filas).

### Preemptivo × não preemptivo (questão 2.c)

| | **Não preemptivo** | **Preemptivo** |
|---|---|---|
| **Definição** | Escolhe um processo e o deixa executar **até ele bloquear ou terminar voluntariamente**. Ignora a interrupção de relógio para fins de troca. | Escolhe um processo e o deixa executar por **no máximo um tempo fixo (quantum)**. Se ao fim do quantum ele ainda estiver executando, é **suspenso à força** e outro é escolhido. |
| **Requisito de hardware** | Nenhum especial | Exige **interrupção de relógio** no fim do intervalo |
| **Vantagens** | Simples; menos chaveamentos; menos sobrecarga; sem condições de corrida por preempção no meio de uma seção crítica do kernel | Garante tempo de resposta; impede monopolização da CPU; justo; essencial para interatividade |
| **Desvantagens** | Um processo longo (ou em laço infinito) trava o sistema; péssimo tempo de resposta | Mais chaveamentos → mais sobrecarga; exige sincronização cuidadosa |
| **Onde se usa** | Sistemas em lote | Sistemas interativos, tempo real, servidores |

> Cuidado com a formulação: mesmo num algoritmo não preemptivo, o SO **recebe** a
> interrupção de relógio (para contabilizar tempo). O que ele **não faz** é usá-la para
> tirar a CPU do processo.

### Tipos de sistema e o que os escalonadores devem otimizar (questão 2.d)

#### (a) Sistemas em lote (*batch*)

- **Características:** sem usuários esperando interativamente; tarefas submetidas a uma
  fila e executadas quando há recursos; tempos de execução normalmente **conhecidos**;
  ainda usados para folha de pagamento, processamento de sinistros, fechamentos contábeis.
- **Metas do escalonador:**
  - **Vazão (throughput)**: maximizar tarefas concluídas por hora.
  - **Tempo de retorno (turnaround)**: minimizar o tempo médio entre submissão e término.
  - **Utilização da CPU**: mantê-la sempre ocupada.
- Como não há usuário aguardando resposta, **algoritmos não preemptivos** (ou preemptivos
  com quantum longo) são aceitáveis e reduzem a sobrecarga de chaveamento.

#### (b) Sistemas interativos

- **Características:** usuários (ou clientes) esperando resposta em tempo real
  perceptível; desktops, servidores web, celulares; comportamento imprevisível; é
  essencial impedir que um processo monopolize a CPU.
- **Metas do escalonador:**
  - **Tempo de resposta**: responder rapidamente às requisições — é a meta dominante.
  - **Proporcionalidade**: atender às **expectativas** do usuário (ele tolera 45 s para
    conectar à internet, mas não 45 s para fechar uma janela — mesmo que ambas as
    operações sejam objetivamente similares).
- **Exige preempção**, com quantum relativamente pequeno.

#### (c) Sistemas de tempo real

- **Características:** processos executam para controlar o mundo físico e há **prazos
  (deadlines) rígidos**; ex.: controle de vôo, marca-passo, multimídia, sistemas
  industriais. Muitas vezes os processos são **conhecidos previamente** e de curta duração.
- **Metas do escalonador:**
  - **Cumprir prazos**: evitar perda de dados / falha do sistema.
  - **Previsibilidade**: evitar degradação de qualidade em sistemas multimídia.
- Frequentemente **não precisam ser preemptivos**, porque os processos são bem-comportados
  e cedem a CPU rapidamente por conta própria.

**Metas comuns a todos os sistemas:** justiça, aplicação da política definida,
balanceamento (manter todas as partes do sistema ocupadas).

---

## 3.5 Escalonamento em sistemas em lote

Algoritmos principais: **FCFS**, **Tarefa Mais Curta Primeiro (SJF)** e **Próximo de
Menor Tempo Restante (SRTN)**.

### (a) FCFS — *First Come, First Served* (Primeiro a Chegar, Primeiro a Ser Servido)

**Funcionamento:** fila única **FIFO**. A CPU é dada ao processo que está há mais tempo
esperando; ele executa **até bloquear ou terminar** (é **não preemptivo**).

**Comportamento em situações excepcionais:**
- **Criação:** o novo processo é inserido **no fim da fila** de prontos.
- **Término:** a CPU é entregue ao **primeiro da fila**.
- **Bloqueio:** o processo sai da CPU e vai para a lista de bloqueados; a CPU vai para o
  primeiro da fila de prontos. **Quando o processo desbloqueia, ele volta para o FIM da
  fila** — como se fosse um processo recém-chegado. Esse detalhe é o que penaliza
  gravemente os processos I/O-bound.

**Vantagem:** é o algoritmo mais simples possível — uma lista encadeada.
**Desvantagem clássica (efeito comboio):** um processo CPU-bound longo à frente faz todos
os I/O-bound esperarem. Se um processo limitado por CPU roda 1 s por vez e vários
processos limitados por E/S precisam de 1 ms de CPU cada para ler um bloco do disco, cada
um deles esperará 1 s por leitura. O disco fica ocioso o tempo todo — desempenho péssimo.

### (b) SJF — *Shortest Job First* (Tarefa Mais Curta Primeiro)

**Funcionamento:** entre os processos **disponíveis no momento da escolha**, executa
primeiro o de **menor tempo total de execução**. É **não preemptivo**: uma vez iniciado,
o processo roda até terminar (ou bloquear).

**Comportamento em situações excepcionais:**
- **Criação:** o novo processo entra na fila, mas **não interrompe** quem está rodando —
  mesmo que seja mais curto. Só será considerado na próxima escolha.
- **Término:** escolhe-se o **mais curto** dentre os prontos naquele instante.
- **Bloqueio:** a CPU é reatribuída ao mais curto dos prontos; ao desbloquear, o processo
  volta a concorrer com seu **tempo restante/total** conforme a implementação.

**Propriedade importante:** o SJF é **ótimo para o tempo médio de retorno** — mas **apenas
quando todas as tarefas estão disponíveis simultaneamente**. Se as tarefas chegam ao longo
do tempo, o SJF pode não ser ótimo.

**Desvantagem:** **inanição (starvation)** de tarefas longas se tarefas curtas continuarem
chegando; e exige conhecer o tempo de execução previamente.

### (c) SRTN — *Shortest Remaining Time Next* (Próximo de Menor Tempo Restante)

**Funcionamento:** versão **preemptiva** do SJF. O escalonador sempre executa o processo
com o **menor tempo restante de execução**.

**Comportamento em situações excepcionais:**
- **Criação:** ao chegar um novo processo, seu tempo total é comparado ao **tempo restante**
  do processo em execução. Se for **menor**, o processo corrente é **preemptado
  imediatamente**. Esse é o traço distintivo do algoritmo.
- **Término:** escolhe-se o de menor tempo restante entre os prontos.
- **Bloqueio:** libera a CPU para o de menor tempo restante; ao desbloquear, volta a
  concorrer com seu tempo restante e **pode preemptar** quem estiver rodando.

**Vantagem:** ótimo tempo de retorno para tarefas curtas; lida bem com chegadas em
instantes distintos.
**Desvantagem:** inanição de tarefas longas é ainda mais severa; exige conhecimento prévio
do tempo; mais chaveamentos.

### (d) Por que é preciso conhecer o tempo previamente (questão 3.e)

**Por que a premissa é necessária:** tanto o SJF quanto o SRTN **definem a ordem de
execução comparando durações**. Sem saber quanto cada processo vai durar, não há como
identificar "o mais curto" nem "o de menor tempo restante" — o critério de decisão do
algoritmo simplesmente não existe. Não é um detalhe de implementação: é a **entrada
obrigatória** do algoritmo.

**Por que o tempo é facilmente obtido em sistemas em lote:**
- As tarefas em lote são, na maioria, **repetitivas e previsíveis**: a mesma folha de
  pagamento, o mesmo fechamento contábil, o mesmo processamento de sinistros roda toda
  semana/mês com volume semelhante.
- Existe **histórico** de execuções anteriores para basear a estimativa (técnica do
  **envelhecimento**: estimativa nova = média ponderada entre a estimativa anterior e o
  tempo real observado, tipicamente `T₁ = aT₀ + (1−a)T`, com `a = ½`).
- O **usuário/operador informa** o tempo estimado na submissão da tarefa (era exigido nas
  linguagens de controle de jobs), e há incentivo para acertar: subestimar faz a tarefa ser
  abortada, superestimar a atrasa.
- Não há interatividade — nenhuma decisão depende do usuário no meio da execução, o que
  torna a duração determinística.

> Em sistemas **interativos** nada disso vale: não se sabe quando o usuário vai clicar,
> nem o que vai digitar. Por isso SJF/SRTN não são usados lá.

### (e) Comparação dos três (questão 3.f)

| Aspecto | **FCFS** | **SJF** | **SRTN** |
|---|---|---|---|
| **Facilidade de implementação** | **Máxima.** Uma fila FIFO simples. Nenhuma informação extra. | **Média.** Precisa ordenar por duração e **conhecer as durações** (estimativas, histórico). | **Menor.** Precisa das durações, manter tempos restantes atualizados e **testar preempção a cada chegada**. |
| **Alocação eficiente da CPU com processos CPU-bound e I/O-bound misturados** | **Péssima.** Efeito comboio: um CPU-bound longo à frente bloqueia todos os I/O-bound, que perdem a vez e ainda voltam ao fim da fila ao desbloquear → dispositivos de E/S ociosos. | **Melhor que FCFS**, pois os I/O-bound (surtos curtos) tendem a ser escolhidos primeiro. Mas, sendo **não preemptivo**, se um CPU-bound longo já começou, todos esperam até ele terminar. | **A melhor das três.** Processos I/O-bound, com surtos curtíssimos, **preemptam** imediatamente o CPU-bound, disparam sua E/S e liberam a CPU → CPU e dispositivos ocupados em paralelo. |
| **Alocação eficiente com processos que iniciam em instantes distintos** | Funciona "naturalmente" (ordena por chegada), mas a eficiência é ruim: a ordem de chegada não tem relação com a duração. | **Ruim.** O SJF só é ótimo se **todas** as tarefas estiverem presentes no início. Uma tarefa curta que chega logo depois do início de uma longa espera até o fim dela. | **Ótima.** Foi projetado exatamente para isso: cada chegada dispara uma reavaliação e a tarefa curta que chega depois passa imediatamente à frente. |
| **Possibilidade de preempção** | **Não** (não preemptivo) | **Não** (não preemptivo) | **Sim** (preemptivo, por chegada — não por relógio) |
| **Risco de inanição** | Nenhum (todos avançam na fila) | Alto para tarefas longas | Muito alto para tarefas longas |
| **Tempo médio de retorno** | O pior | Ótimo **se** todos chegarem juntos | Geralmente o melhor com chegadas escalonadas |

---

## 3.6 EXERCÍCIO RESOLVIDO — Sistemas em Lote (questão 3.g)

**Dados:**

| Processo | Duração | Início (chegada) |
|---|---|---|
| A | 40 ms | 0 ms |
| B | 20 ms | 4 ms |
| C | 15 ms | 5 ms |
| D | 17 ms | 4 ms |
| E | 10 ms | 14 ms |

Trabalho total = 40+20+15+17+10 = **102 ms**. Como a CPU nunca fica ociosa (sempre há
alguém pronto a partir de t=0), **o último processo sempre termina em t = 102 ms** nos
três algoritmos. Isso é uma ótima verificação de sanidade.

> **Convenção de desempate adotada:** quando B e D chegam juntos em t=4, usa-se a ordem da
> tabela (B antes de D). Declare a convenção na prova — o professor aceita desde que seja
> coerente.
>
> **Tempo de retorno (turnaround)** = instante de término − instante de chegada.

---

### (a) FCFS

Ordem de chegada: A(0) → B(4) → D(4) → C(5) → E(14). Não há preempção.

**Escala da CPU:**
```
 0        40      60       77      92     102
 |───A────|───B───|───D────|───C───|──E───|
```

| Processo | Início | Término | Chegada | Turnaround |
|---|---|---|---|---|
| A | 0 | 40 | 0 | **40 ms** |
| B | 40 | 60 | 4 | **56 ms** |
| D | 60 | 77 | 4 | **73 ms** |
| C | 77 | 92 | 5 | **87 ms** |
| E | 92 | 102 | 14 | **88 ms** |

**Tempo médio de retorno = (40+56+73+87+88)/5 = 344/5 = 68,8 ms**

---

### (b) SJF — Tarefa Mais Curta Primeiro (não preemptivo)

- t=0: apenas **A** está disponível → A executa **até terminar** (não preemptivo), de 0 a 40.
- t=40: disponíveis B(20), C(15), D(17), E(10) → o mais curto é **E(10)**: 40→50.
- t=50: restam B(20), C(15), D(17) → **C(15)**: 50→65.
- t=65: restam B(20), D(17) → **D(17)**: 65→82.
- t=82: resta **B(20)**: 82→102.

**Escala da CPU:**
```
 0             40     50      65       82        102
 |──────A──────|──E───|───C───|───D────|────B─────|
```

| Processo | Término | Chegada | Turnaround |
|---|---|---|---|
| A | 40 | 0 | **40 ms** |
| E | 50 | 14 | **36 ms** |
| C | 65 | 5 | **60 ms** |
| D | 82 | 4 | **78 ms** |
| B | 102 | 4 | **98 ms** |

**Tempo médio de retorno = (40+36+60+78+98)/5 = 312/5 = 62,4 ms**

---

### (c) SRTN — Próximo de Menor Tempo Restante (preemptivo)

Rastreando os tempos restantes a cada chegada:

- **t=0:** A chega (rest. 40) → A executa.
- **t=4:** chegam B(20) e D(17). A tem 36 restantes. Menor = **D(17)** → **preempta A**.
- **t=5:** chega C(15). D tem 16 restantes. 15 < 16 → **preempta D**, C executa.
- **t=14:** chega E(10). C tem 15−9 = **6** restantes. 6 < 10 → **C continua**.
- **t=20:** C **termina**. Restantes: A=36, B=20, D=16, E=10 → **E** executa.
- **t=30:** E **termina**. Restantes: A=36, B=20, D=16 → **D** executa.
- **t=46:** D **termina** (16 ms). Restantes: A=36, B=20 → **B** executa.
- **t=66:** B **termina**. Resta **A** (36) → 66 a 102.

**Escala da CPU:**
```
 0   4   5           20      30        46           66              102
 |─A─|─D─|─────C─────|───E───|────D────|─────B──────|───────A────────|
```

| Processo | Término | Chegada | Turnaround |
|---|---|---|---|
| A | 102 | 0 | **102 ms** |
| B | 66 | 4 | **62 ms** |
| C | 20 | 5 | **15 ms** |
| D | 46 | 4 | **42 ms** |
| E | 30 | 14 | **16 ms** |

**Tempo médio de retorno = (102+62+15+42+16)/5 = 237/5 = 47,4 ms**

---

### Comparativo final (lote)

| Algoritmo | Tempo médio de retorno |
|---|---|
| FCFS | 68,8 ms |
| SJF | 62,4 ms |
| **SRTN** | **47,4 ms** ← melhor |

**Comentário para a prova:** o SRTN vence porque a preempção por chegada permite que as
tarefas curtas (C e E) sejam concluídas cedo, retirando-as da média. O preço é que A, a
tarefa mais longa, é adiada ao máximo (turnaround de 102 ms — o pior possível), o que
ilustra o risco de **inanição** desses algoritmos.

---

## 3.7 Escalonamento em sistemas interativos

Algoritmos: **circular (Round Robin)**, **por prioridades**, **filas múltiplas**,
**garantido**, **por loteria** e **por fração justa**. (Tanenbaum cita ainda o
*shortest process next* e o escalonamento em dois níveis.)

### (a) Escalonamento circular (*Round Robin*)

**Funcionamento:** cada processo recebe um intervalo fixo de tempo, o **quantum**. Existe
uma **fila circular** de prontos. O processo executa por até um quantum; se ainda não
terminou quando o quantum expira, é **preemptado** e colocado **no fim da fila**. A CPU
vai para o próximo da fila.

**Situações excepcionais:**
- **Criação:** o novo processo é inserido **no fim da fila** de prontos.
- **Término:** o processo é removido da fila e a CPU vai imediatamente para o próximo,
  **sem esperar o quantum acabar**.
- **Bloqueio:** o processo sai da fila de prontos **antes de esgotar seu quantum**; a CPU
  passa ao próximo. Ao desbloquear, ele entra **no fim da fila** de prontos, recebendo um
  quantum inteiro novo. (É por isso que processos I/O-bound são tratados razoavelmente bem
  aqui, ao contrário do FCFS.)

**Vantagens:** justo (todos recebem fatias iguais), simples, sem inanição, bom tempo de
resposta.
**Desvantagem:** trata todos igualmente, mesmo quando não deveria.

### (b) Escolha do quantum: pequeno × grande

O ponto central é o **custo do chaveamento de contexto** (salvar/restaurar registradores e
mapa de memória, invalidar cache e TLB) — tipicamente ~1 ms.

**Quantum pequeno (ex.: 4 ms para 1 ms de chaveamento):**
- ✅ **Excelente tempo de resposta** — cada processo é atendido rapidamente; ótima
  sensação de interatividade.
- ✅ Justiça de granularidade fina.
- ❌ **Sobrecarga altíssima**: com quantum de 4 ms e chaveamento de 1 ms, **20% da CPU é
  desperdiçada** apenas trocando de processo.
- ❌ Pior aproveitamento de cache (cada processo mal aquece o cache e já sai).

**Quantum grande (ex.: 100 ms):**
- ✅ **Baixa sobrecarga**: com 1 ms de chaveamento, apenas ~1% da CPU é desperdiçada.
- ✅ Melhor uso de cache/TLB; melhor vazão para processos CPU-bound.
- ❌ **Tempo de resposta ruim**: com 50 processos prontos, o último espera até 5 s para
  começar — inaceitável em sistema interativo.
- ❌ Tende ao comportamento do FCFS quando o quantum supera o surto médio de CPU.

**Conclusão:** o quantum deve ser **maior que o surto médio de CPU dos processos
interativos** (para que eles terminem seu surto e bloqueiem por conta própria, sem
preempção) e **muito maior que o tempo de chaveamento**. Na prática, algo entre **20 ms e
50 ms** é um bom compromisso.

### (c) Escalonamento por prioridades

**Funcionamento:** cada processo recebe uma **prioridade**; executa sempre o processo
**pronto de maior prioridade**. É a resposta à limitação do Round Robin (nem todos os
processos são igualmente importantes).

Para evitar monopolização pelos processos de alta prioridade, o escalonador tipicamente:
- **Reduz a prioridade do processo em execução** a cada tique de relógio (prioridade
  dinâmica). Quando cai abaixo da do segundo colocado, há troca.
- Ou atribui um **quantum máximo** a cada processo; ao esgotá-lo, o próximo de maior
  prioridade assume.

**Situações excepcionais:**
- **Criação:** o novo processo recebe uma prioridade (herdada, atribuída pelo usuário via
  `nice`, ou calculada). Se for **maior** que a do processo em execução, em geral **o
  preempta** (versão preemptiva); na versão não preemptiva, apenas aguarda na fila.
- **Término:** escolhe-se o processo pronto de **maior prioridade**.
- **Bloqueio:** a CPU vai para o de maior prioridade entre os prontos. Ao desbloquear, o
  processo compete de novo por prioridade — e em muitos sistemas **recebe um aumento de
  prioridade** por ter feito E/S (fórmula `1/f`, onde `f` é a fração do quantum que ele
  usou: quem usa 1/50 do quantum recebe prioridade 50). Isso favorece automaticamente os
  processos I/O-bound.

**Prioridades estáticas × dinâmicas:**
- **Estáticas**: atribuídas externamente (usuário, classe de serviço, importância
  administrativa). Ex.: no UNIX, `nice`.
- **Dinâmicas**: ajustadas pelo SO conforme o comportamento observado (envelhecimento,
  bônus por E/S).

**Desvantagem principal:** **inanição** dos processos de baixa prioridade. Solução:
**envelhecimento (aging)** — aumentar gradualmente a prioridade de quem espera há muito
tempo.

**Implementação usual:** agrupar processos em **classes de prioridade** e usar **Round
Robin dentro de cada classe**.

### (d) Escalonamento por múltiplas filas

**Funcionamento:** o sistema mantém **várias filas**, uma por **classe de prioridade**.
Executa-se sempre a fila **não vazia de maior prioridade**, com Round Robin dentro dela.

A versão clássica (CTSS) resolve o problema do chaveamento excessivo dando **quanta
maiores às classes mais baixas**:
- Classe de prioridade mais alta: **1 quantum**
- Classe seguinte: **2 quanta**
- Classe seguinte: **4 quanta**
- Classe seguinte: **8 quanta**, e assim por diante (potências de 2).

**Regra de rebaixamento:** quando um processo **esgota todos os quanta** de sua classe, ele
é **rebaixado uma classe** — recebendo, em compensação, o dobro de tempo na próxima vez.
Assim, um processo que precisa de 100 quanta roda: 1 vez com 1 quantum, depois 2, 4, 8,
16, 32, 64 — apenas **7 chaveamentos** em vez de 100.

**Situações excepcionais:**
- **Criação:** o processo entra na fila de **maior prioridade** (assume-se que pode ser
  interativo/curto) ou na fila correspondente à sua prioridade inicial.
- **Término:** remove-se da fila; a CPU vai para o primeiro da fila não vazia de maior
  prioridade.
- **Bloqueio:** o processo sai antes de esgotar seus quanta → **não é rebaixado** (afinal,
  comportou-se como interativo). Ao desbloquear, volta à sua fila — e, em muitas
  implementações, é **promovido** à fila de maior prioridade, o que favorece os I/O-bound.
- **Preempção:** a chegada de um processo em fila de prioridade mais alta preempta quem
  está executando em fila mais baixa.

**(e) Estratégias contra a demora dos processos de baixa prioridade (questão 4.f)**

1. **Envelhecimento (aging):** aumentar a prioridade (promover de fila) de processos que
   esperam há muito tempo sem receber CPU.
2. **Promoção por comportamento interativo:** todo processo que bloqueia por E/S ou pelo
   teclado é promovido à fila mais alta. No CTSS, digitar ENTER promovia o processo à
   classe mais alta — presumindo que ele estava prestes a virar interativo.
   *(Fraqueza histórica:* usuários descobriram isso e passaram a apertar ENTER
   aleatoriamente para ganhar prioridade.)
3. **Reservar uma fração da CPU** para as filas baixas (ex.: a cada N escalonamentos, um é
   obrigatoriamente da fila mais baixa).
4. **Redefinir prioridades periodicamente** (recalcular todas as prioridades a cada
   intervalo, zerando vantagens acumuladas).
5. **Limitar o tempo total** que um processo pode permanecer na fila mais alta.

### (f) Escalonamento garantido

**Funcionamento:** o SO **faz uma promessa quantitativa** ao usuário e a cumpre.
A promessa típica: com **n** processos/usuários, cada um receberá **1/n da CPU**.

Mecanismo:
1. O SO **contabiliza** quanto de CPU cada processo já consumiu desde a criação.
2. Calcula quanto ele **teria direito**: (tempo desde a criação) / n.
3. Calcula a **razão** = tempo consumido / tempo devido.
4. Executa o processo com a **menor razão** — o mais "prejudicado" — até que ele
   ultrapasse o segundo colocado.

*Interpretação:* razão 0,5 = recebeu metade do que merecia (executa já); razão 2,0 =
recebeu o dobro (espera).

**Situações excepcionais:**
- **Criação:** o valor de **n muda**, então **todos os direitos são recalculados**. O novo
  processo tem consumo zero → razão 0 → **é escolhido imediatamente**.
- **Término:** n diminui; os demais passam a ter direito a uma fatia maior; o de menor
  razão assume a CPU.
- **Bloqueio:** enquanto bloqueado, o processo **não consome CPU**, mas o **tempo continua
  passando** — logo seu "devido" cresce e sua razão **cai**. Ao desbloquear, ele estará com
  razão baixa e tenderá a ser escolhido rapidamente. **Isso favorece naturalmente os
  processos I/O-bound.**

**Vantagem:** justiça mensurável e verificável.
**Desvantagem:** exige contabilização precisa e contínua; implementação relativamente
complexa.

### (g) Escalonamento por loteria

**Funcionamento:** distribuem-se **bilhetes de loteria** aos processos, cada bilhete dando
direito a recursos do sistema. Sempre que é preciso escalonar, **sorteia-se um bilhete ao
acaso** e o processo dono dele ganha a CPU pelo quantum.

- Processos mais importantes recebem **mais bilhetes** → maior probabilidade.
- Com 100 bilhetes ao todo e 20 bilhetes para um processo, ele obtém **~20% da CPU** a
  longo prazo — estatisticamente, o mesmo resultado do escalonamento garantido, com
  implementação **muito mais simples**.
- Processos **cooperativos podem trocar bilhetes** entre si (ex.: um cliente que envia
  mensagem a um servidor e bloqueia pode passar **todos os seus bilhetes** ao servidor,
  para que ele responda depressa — depois devolve).
- **Altamente responsivo:** um processo recém-criado com bilhetes já pode ser sorteado na
  próxima decisão.

**Situações excepcionais:**
- **Criação:** o processo recebe seu lote de bilhetes e passa a concorrer **imediatamente**
  nos sorteios seguintes.
- **Término:** os bilhetes do processo são **devolvidos/retirados** do sorteio,
  redistribuindo automaticamente a probabilidade entre os restantes.
- **Bloqueio:** os bilhetes do processo bloqueado são **excluídos do sorteio** (senão
  haveria sorteios "vazios"); ao desbloquear, voltam ao bolo. Alternativamente, ele pode
  **transferir seus bilhetes** ao processo que o está atendendo.

**Vantagens:** simples, altamente responsivo, **sem inanição** (todo processo com pelo
menos 1 bilhete tem probabilidade não nula), permite dividir recursos de forma
proporcional e trivialmente ajustável.
**Desvantagem:** garantia apenas **probabilística** — a curto prazo pode haver desvios.

### (h) Escalonamento por fração justa (*fair share*)

**Funcionamento:** o recurso é dividido entre **usuários (ou grupos)**, não entre
processos. Cada usuário recebe uma **fração da CPU** e essa fração é dividida entre seus
processos. Assim, um usuário **não ganha mais CPU só por criar mais processos**.

*Exemplo (Tanenbaum):* usuário 1 com processos A, B, C, D e usuário 2 apenas com E,
50% para cada usuário → escala `A E B E C E D E A E B E ...`. Se o usuário 1 tivesse
direito a 2× o tempo do usuário 2: `A B E C D E A B E C D E ...`.

**Situações excepcionais:**
- **Criação:** se for de um **usuário já ativo**, a fração desse usuário passa a ser
  dividida por mais um processo — **os processos existentes desse usuário perdem tempo, os
  dos outros usuários não são afetados**. Se for de um **usuário novo**, as frações de
  todos os usuários são recalculadas.
- **Término:** a fatia do processo é redistribuída **dentro do mesmo usuário**; se era o
  último processo do usuário, a fração dele é redistribuída aos demais usuários.
- **Bloqueio:** o usuário não perde sua fatia — ela é passada a **outro processo do mesmo
  usuário**. Se o usuário não tiver outro processo pronto, sua fatia é cedida (naquele
  momento) aos outros usuários, para não desperdiçar CPU.

**Vantagem:** justiça entre **pessoas**, não entre processos — essencial em servidores
multiusuário e ambientes compartilhados.
**Desvantagem:** implementação mais complexa (dois níveis de contabilização) e pode deixar
CPU ociosa se aplicada rigidamente demais.

---

### (i) Comparação dos algoritmos interativos (questão 4.j)

| Aspecto | **Circular (RR)** | **Prioridades** | **Filas múltiplas** | **Garantido** | **Loteria** | **Fração justa** |
|---|---|---|---|---|---|---|
| **Simplicidade de implementação** | **A mais simples**: uma fila circular e um temporizador. | Simples-média: fila ordenada; complica com prioridades dinâmicas e aging. | **Média-alta**: várias filas, regras de promoção/rebaixamento, quanta variáveis. | **Alta complexidade**: contabilizar consumo, calcular direito e razão continuamente. | **Baixa complexidade**: sortear um número aleatório; mais simples que o garantido para o mesmo efeito. | **Alta**: contabilização em dois níveis (usuário e processo). |
| **Diferenciação entre processos** | **Nenhuma** — todos recebem quanta iguais. | **Sim**, explicitamente — é o propósito do algoritmo. | **Sim**, por classes, e ainda **adapta-se dinamicamente** ao comportamento observado. | **Não** por importância — todos recebem 1/n. Diferencia apenas por *quem está atrasado*. | **Sim**, de forma fina e proporcional: basta variar o número de bilhetes. | Sim **entre usuários**; dentro do usuário, geralmente igualitário. |
| **Diferenciação entre usuários distintos** | **Não.** Um usuário com 10 processos recebe 10× mais CPU que um com 1. | **Indiretamente**, atribuindo prioridades por usuário — mas quem criar mais processos ainda leva vantagem. | **Indiretamente**, pelo mesmo motivo. | Parcialmente — a promessa é feita por processo (ou por usuário, se assim definida). | **Sim**, se os bilhetes forem distribuídos **por usuário** e depois repartidos entre seus processos. | **Sim — é exatamente o objetivo do algoritmo.** É o único que garante isso por construção. |
| **Quantidade de chaveamentos até concluir um processo** | **Alta** — proporcional a (tempo total / quantum); ignora quantos chaveamentos isso gera. | **Média** — processos de alta prioridade concluem com poucos chaveamentos; os de baixa, com muitos (ou nunca). | **A menor para processos longos** — os quanta dobram a cada rebaixamento, reduzindo os chaveamentos de *n* para *log₂ n*. | Média-alta — a razão muda continuamente, provocando trocas frequentes. | **Alta e variável** — o sorteio é a cada quantum e pode alternar muito; nada impede sequências ruins. | Alta — alterna deliberadamente entre usuários, o que força chaveamentos frequentes. |
| **Risco de inanição** | Nenhum | **Alto** (sem aging) | Médio (mitigado por promoção/aging) | Nenhum (o atrasado é sempre priorizado) | Nenhum (probabilidade não nula) | Nenhum dentro da fatia do usuário |

---

## 3.8 EXERCÍCIO RESOLVIDO — Sistemas Interativos (questões 4.k e 4.l)

**Dados:**

| Processo | Duração | Início | Prioridade |
|---|---|---|---|
| A | 40 ms | 0 ms | 3 |
| B | 20 ms | 4 ms | 1 |
| C | 15 ms | 5 ms | 2 |
| D | 17 ms | 4 ms | 1 |
| E | 10 ms | 14 ms | 0 |

**Quantum = 5 ms. Menor número = maior prioridade.** Total = 102 ms (último término
sempre em t = 102).

> **Convenções adotadas (declare-as na prova):**
> - Quando um processo chega **exatamente** no instante em que outro é preemptado, o
>   **recém-chegado entra na fila antes** do preemptado.
> - Empates de chegada e de prioridade seguem a ordem da tabela (B antes de D).
> - No escalonamento por prioridades, a chegada de processo mais prioritário **preempta**.

---

### (a) Circular (Round Robin), quantum = 5 ms

Acompanhamento da fila de prontos:

| Intervalo | Executa | Restante | Fila após |
|---|---|---|---|
| 0–5 | A | 35 | B, D, C, A |
| 5–10 | B | 15 | D, C, A, B |
| 10–15 | D | 12 | C, A, B, E, D |
| 15–20 | C | 10 | A, B, E, D, C |
| 20–25 | A | 30 | B, E, D, C, A |
| 25–30 | B | 10 | E, D, C, A, B |
| 30–35 | E | 5 | D, C, A, B, E |
| 35–40 | D | 7 | C, A, B, E, D |
| 40–45 | C | 5 | A, B, E, D, C |
| 45–50 | A | 25 | B, E, D, C, A |
| 50–55 | B | 5 | E, D, C, A, B |
| 55–60 | **E termina** | 0 | D, C, A, B |
| 60–65 | D | 2 | C, A, B, D |
| 65–70 | **C termina** | 0 | A, B, D |
| 70–75 | A | 20 | B, D, A |
| 75–80 | **B termina** | 0 | D, A |
| 80–82 | **D termina** (usa só 2 ms) | 0 | A |
| 82–102 | **A termina** (sozinha) | 0 | — |

**Escala resumida:**
```
A B D C A B E D C A B E D C A B D A
0 5 10 15 20 25 30 35 40 45 50 55 60 65 70 75 80 82 ... 102
```

| Processo | Término | Chegada | Turnaround |
|---|---|---|---|
| A | 102 | 0 | **102 ms** |
| B | 80 | 4 | **76 ms** |
| C | 70 | 5 | **65 ms** |
| D | 82 | 4 | **78 ms** |
| E | 60 | 14 | **46 ms** |

**Tempo médio de retorno = (102+76+65+78+46)/5 = 367/5 = 73,4 ms**

---

### (b) Por prioridades (preemptivo)

- **t=0:** só A (prio 3) está pronto → A executa.
- **t=4:** chegam B(1) e D(1), mais prioritários que A(3) → **preemptam A** (A tem 36
  restantes). Empate B/D → **B** executa.
- **t=5:** chega C(2). Prioridade 2 < prioridade 1 de B → **B continua**.
- **t=14:** chega **E(0)**, a maior prioridade de todas → **preempta B** (B rodou 10 ms,
  restam 10).
- **t=14–24:** E executa e **termina em 24**.
- **t=24:** prontos: B(1, rest. 10), D(1, 17), C(2, 15), A(3, 36). Maior prioridade: classe
  1 → B (chegou antes de D) → executa 24–34, **termina em 34**.
- **t=34:** classe 1 → **D** (17 ms) → executa 34–51, **termina em 51**.
- **t=51:** classe 2 → **C** (15 ms) → executa 51–66, **termina em 66**.
- **t=66:** classe 3 → **A** (36 restantes) → executa 66–102, **termina em 102**.

**Escala da CPU:**
```
 0   4        14        24       34         51        66            102
 |─A─|────B───|────E────|───B────|────D─────|────C────|──────A───────|
```

| Processo | Término | Chegada | Turnaround |
|---|---|---|---|
| A | 102 | 0 | **102 ms** |
| B | 34 | 4 | **30 ms** |
| C | 66 | 5 | **61 ms** |
| D | 51 | 4 | **47 ms** |
| E | 24 | 14 | **10 ms** |

**Tempo médio de retorno = (102+30+61+47+10)/5 = 250/5 = 50,0 ms**

---

### (c) Filas múltiplas

**Modelo adotado (CTSS clássico):** quatro classes, com quantum-base de 5 ms e alocação
dobrando a cada classe inferior. O processo entra na fila correspondente à sua prioridade.

| Classe | Alocação | Processos iniciais |
|---|---|---|
| 0 (mais alta) | 1 quantum = 5 ms | E |
| 1 | 2 quanta = 10 ms | B, D |
| 2 | 4 quanta = 20 ms | C |
| 3 (mais baixa) | 8 quanta = 40 ms | A |

Regra: executa sempre a fila não vazia de maior prioridade; quem **esgota sua alocação
inteira** é **rebaixado** uma classe; quem chega em classe mais alta **preempta**.

- **t=0–4:** só A (classe 3) → executa. Restam 36. (Foi **preemptado**, não esgotou a
  alocação → permanece na classe 3.)
- **t=4:** chegam B e D (classe 1) → preemptam A. **B** executa.
- **t=5:** chega C (classe 2) — inferior → B continua.
- **t=4–14:** B usa **os 10 ms inteiros** de sua alocação → **rebaixado para a classe 2**,
  com 10 ms restantes. Nesse mesmo instante chega **E (classe 0)**.
- **t=14–19:** **E** (classe 0, alocação 5 ms) executa, esgota a alocação → **rebaixado
  para a classe 1**, com 5 ms restantes.
- **t=19–29:** classe 1 → **D** (esperando desde t=4, à frente de E que acabou de ser
  rebaixado). Usa os 10 ms inteiros → **rebaixado para a classe 2**, restam 7 ms.
- **t=29–34:** classe 1 → **E** (5 restantes) executa e **termina em 34**.
- **t=34–49:** classe 2. Ordem na fila: C (lá desde t=5), B (rebaixado em 14), D
  (rebaixado em 29). **C** executa 15 ms (< 20 de alocação) e **termina em 49**.
- **t=49–59:** classe 2 → **B** (10 restantes) executa e **termina em 59**.
- **t=59–66:** classe 2 → **D** (7 restantes) executa e **termina em 66**.
- **t=66–102:** classe 3 → **A** (36 restantes) executa e **termina em 102**.

**Escala da CPU:**
```
 0   4      14   19      29    34      49      59     66            102
 |─A─|──B───|─E──|───D───|──E──|───C───|───B───|──D───|──────A───────|
```

| Processo | Término | Chegada | Turnaround |
|---|---|---|---|
| A | 102 | 0 | **102 ms** |
| B | 59 | 4 | **55 ms** |
| C | 49 | 5 | **44 ms** |
| D | 66 | 4 | **62 ms** |
| E | 34 | 14 | **20 ms** |

**Tempo médio de retorno = (102+55+44+62+20)/5 = 283/5 = 56,6 ms**

> ⚠️ Se o professor usar outra convenção (por exemplo, Round Robin puro de 5 ms dentro de
> cada fila, sem quanta dobrados, ou rebaixamento a cada quantum consumido), os números
> mudam. **Escreva a convenção adotada no início da resposta** — o raciocínio é o que vale.

---

### (d) Fração justa — questão 4.l

**Usuário 01:** A e C. **Usuário 02:** B, D e E. *(O enunciado diz "B, D e F", mas F não
existe na tabela — trata-se de E.)*
**Cada usuário recebe 50% da CPU**, alternando quanta de 5 ms; dentro de cada usuário,
Round Robin.

| Intervalo | Usuário | Processo | Restante |
|---|---|---|---|
| 0–5 | **U1** | A | 35 (U2 ainda não tem processos em t=0) |
| 5–10 | U2 | B | 15 |
| 10–15 | U1 | C | 10 |
| 15–20 | U2 | D | 12 |
| 20–25 | U1 | A | 30 |
| 25–30 | U2 | E | 5 |
| 30–35 | U1 | C | 5 |
| 35–40 | U2 | B | 10 |
| 40–45 | U1 | A | 25 |
| 45–50 | U2 | D | 7 |
| 50–55 | U1 | **C termina (55)** | 0 |
| 55–60 | U2 | **E termina (60)** | 0 |
| 60–65 | U1 | A | 20 |
| 65–70 | U2 | B | 5 |
| 70–75 | U1 | A | 15 |
| 75–80 | U2 | D | 2 |
| 80–85 | U1 | A | 10 |
| 85–90 | U2 | **B termina (90)** | 0 |
| 90–95 | U1 | A | 5 |
| 95–97 | U2 | **D termina (97)** — usa só 2 ms | 0 |
| 97–102 | U1 | **A termina (102)** — U2 vazio | 0 |

| Processo | Usuário | Término | Chegada | Turnaround |
|---|---|---|---|---|
| A | U1 | 102 | 0 | **102 ms** |
| B | U2 | 90 | 4 | **86 ms** |
| C | U1 | 55 | 5 | **50 ms** |
| D | U2 | 97 | 4 | **93 ms** |
| E | U2 | 60 | 14 | **46 ms** |

**Tempo médio de retorno = (102+86+50+93+46)/5 = 377/5 = 75,4 ms**

**Observação para a resposta:** o usuário 01, tendo apenas 2 processos, vê cada um deles
receber **25% da CPU**; o usuário 02, com 3 processos, vê cada um receber ~**16,7%**.
É exatamente o efeito desejado: **criar mais processos não dá mais CPU ao usuário**.

---

### Quadro comparativo geral (interativos)

| Algoritmo | Tempo médio de retorno |
|---|---|
| Circular (RR, q=5 ms) | 73,4 ms |
| **Por prioridades** | **50,0 ms** ← melhor aqui |
| Filas múltiplas | 56,6 ms |
| Fração justa | 75,4 ms |

**Leitura dos resultados:** o algoritmo por prioridades vence porque a distribuição de
prioridades dada coincide com "processos curtos têm prioridade alta" (E, o mais curto, tem
prioridade 0). Round Robin e fração justa, por tratarem todos igualmente e chavear muito,
elevam o tempo médio — mas oferecem, em troca, **melhor tempo de resposta** e **justiça**,
que são as métricas que realmente importam em sistemas interativos.

---

## 3.9 Política × Mecanismo de escalonamento (questão 5)

| | **Mecanismo** | **Política** |
|---|---|---|
| **O que é** | *Como* fazer — a máquina que executa a decisão | *O que* decidir — a regra que define quem é escolhido |
| **Onde fica** | No **núcleo** do SO (o escalonador propriamente dito) | Pode ser definida **fora do núcleo**, por processos de usuário |
| **Exemplo** | O código que salva o contexto, consulta parâmetros de prioridade e chaveia | "O processo P deve ter prioridade 5", "filhos deste processo devem ser atendidos nesta ordem" |

**A ideia central: separar política de mecanismo.** O núcleo implementa o mecanismo e
oferece uma **chamada de sistema** que permite a um processo **definir os parâmetros de
escalonamento (prioridades) de seus filhos**. O algoritmo é parametrizado pelo núcleo, mas
os **parâmetros são preenchidos por processos de usuário**.

**Uso em sistemas com hierarquia de processos:**
- Só o **processo-pai** conhece a semântica da aplicação: ele sabe quais dos seus filhos
  são críticos e quais podem esperar. O núcleo **não tem** essa informação.
- *Exemplo de Tanenbaum:* um processo de banco de dados cria filhos para tratar
  requisições. Só o pai sabe qual requisição é urgente e qual não é.
- Assim, o pai **atribui prioridades aos filhos** via chamada de sistema; o núcleo,
  usando seu mecanismo (por exemplo, escalonamento por prioridades), apenas **obedece** a
  esses valores.
- Isso permite **escalonamento em dois níveis**: o núcleo distribui CPU entre os processos
  de topo; cada pai redistribui internamente a fatia que recebeu entre seus filhos,
  conforme sua própria política.
- **Benefício:** um único mecanismo no núcleo atende a infinitas políticas de aplicação,
  sem que seja preciso recompilar o kernel para cada caso.

---

## 3.10 Escalonamento de threads (questão 6)

Os mesmos algoritmos são usados, mas **o efeito é diferente** conforme o modelo de
implementação.

### Threads no espaço do usuário

- O **núcleo escalona processos**, sem saber que há threads. Quando dá um quantum ao
  processo, **o runtime** decide qual thread do processo usará esse tempo.
- O runtime pode aplicar **qualquer algoritmo** (RR, prioridades, garantido, loteria,
  cooperativo...), e **cada processo pode usar um diferente**.
- **Não há preempção por relógio entre threads** do mesmo processo (o runtime não recebe
  a interrupção de relógio). Uma thread só sai da CPU se ceder voluntariamente
  (`thread_yield`), bloquear ou terminar. Logo, algoritmos **cooperativos**, com quantum
  ilimitado, funcionam bem aqui.
- Se uma thread esgotar o quantum **do processo**, o núcleo escalona **outro processo**; ao
  voltar, o runtime retoma de onde parou.
- **Vantagem específica:** chaveamento entre threads é baratíssimo → algoritmos com muitos
  chaveamentos (RR com quantum pequeno) são viáveis sem penalidade.
- **Limitação:** o núcleo pode dar 1/n da CPU ao processo, e as threads têm de dividir
  essa fatia entre si.

### Threads no núcleo

- O núcleo escalona **threads diretamente**, ignorando a qual processo pertencem (ou
  levando isso em conta apenas parcialmente).
- Os algoritmos são aplicados **sobre a fila global de threads**: RR, prioridades, filas
  múltiplas etc. funcionam exatamente como descritos, com threads no lugar de processos.
- **Há preempção por relógio** entre threads, inclusive do mesmo processo.
- **Um único algoritmo** vale para todo o sistema.
- **Efeito colateral relevante:** um processo com 50 threads tende a receber muito mais CPU
  que um processo com 1 thread — a menos que se use **fração justa** para corrigir isso.
- **Custo:** o chaveamento é uma chamada de sistema. O núcleo pode otimizar preferindo,
  ao escolher a próxima thread, **uma thread do mesmo processo** (evita trocar o espaço de
  endereçamento e invalidar cache/TLB).
- **Ganho específico:** se uma thread bloqueia, o núcleo escalona **outra thread do mesmo
  processo** — algo impossível no modelo de espaço de usuário.

---

## Checklist de revisão da Lista 03

- [ ] Sei definir escalonador e citar 5 razões de sua importância para o desempenho.
- [ ] Sei diferenciar CPU-bound de I/O-bound e explicar por que priorizar I/O-bound.
- [ ] Sei as 5 situações de troca e quais são obrigatórias × opcionais.
- [ ] Sei diferenciar política (algoritmo) de mecanismo (escalonador).
- [ ] Sei os 6 critérios de classificação e a diferença preemptivo × não preemptivo.
- [ ] Sei as metas de escalonamento de lote, interativos e tempo real.
- [ ] Sei descrever FCFS, SJF e SRTN com criação/término/bloqueio.
- [ ] Sei justificar a premissa do tempo conhecido e por que ela vale em lote.
- [ ] Sei preencher a tabela comparativa dos 3 algoritmos de lote (4 aspectos).
- [ ] **Refiz o exercício de lote e cheguei a 68,8 / 62,4 / 47,4 ms.**
- [ ] Sei descrever RR, prioridades, filas múltiplas, garantido, loteria e fração justa.
- [ ] Sei discutir quantum pequeno × grande com o argumento da % de sobrecarga.
- [ ] Sei as 5 estratégias contra a demora de processos de baixa prioridade.
- [ ] Sei preencher a tabela comparativa dos 6 algoritmos interativos (4 aspectos).
- [ ] **Refiz o exercício interativo e cheguei a 73,4 / 50,0 / 56,6 / 75,4 ms.**
- [ ] Sei explicar escalonamento em dois níveis com hierarquia de processos.
- [ ] Sei aplicar os algoritmos a threads nos dois modelos de implementação.
