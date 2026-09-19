# ⏱️ Lista 03 — Escalonamento de Processos e Threads

**Sistemas Operacionais (PEX0134) · UFERSA · Prof. Vinícius Samuel**

> **Como estudar:** faça a primeira leitura pelas questões teóricas (1, 2, 5, 6), depois refaça os **cálculos** (3g, 4k, 4l) no papel sem olhar. Os blocos **🎯 Decore** trazem a frase para lembrar na hora da prova.

## 📌 Colinha de conceitos

| Termo | Significado |
|---|---|
| **Surto de CPU** | Intervalo em que o processo só computa |
| **Preemptivo** | O SO pode tirar a CPU do processo (interrupção de relógio, chegada de processo melhor) |
| **Não preemptivo** | O processo só larga a CPU ao terminar, bloquear ou ceder voluntariamente |
| **Quantum** | Fatia de tempo máxima que um processo usa a CPU de uma vez |
| **Chaveamento** | Troca de contexto entre processos (tem custo) |
| **Tempo de retorno** (turnaround) | `instante de término − instante de chegada` |
| **Tempo médio de conclusão** | Média dos tempos de retorno (é o que a lista pede nos cálculos) |
| **Inanição** (starvation) | Processo que nunca (ou quase nunca) recebe a CPU |

---

# 1) Escalonamento de processos

## a) O que é um escalonador de processos?

Parte do **núcleo do SO** que **decide qual processo pronto recebe a CPU** e por quanto tempo, sempre que há mais de um candidato.

## b) Importância para o desempenho

A CPU é o recurso mais disputado do sistema. Um bom escalonador:

- **Aumenta a utilização da CPU** (ela não fica ociosa);
- **Melhora vazão e tempo de resposta** (mais trabalho concluído, usuário atendido rápido);
- **Garante justiça** (ninguém fica sem CPU) e mantém **dispositivos de E/S ocupados**;
- **Evita chaveamentos desnecessários**, que são caros.

Um escalonador ruim gera CPU ociosa, sistema lento e processos em inanição.

## c) Tipos de processos (pelo comportamento)

| Tipo | Comportamento | O que o escalonador deve fazer |
|---|---|---|
| **Limitado por computação** (CPU-bound) | Surtos de CPU **longos**, pouca E/S | Não deixar que monopolize a CPU |
| **Limitado por E/S** (I/O-bound) | Surtos de CPU **curtos**, muito tempo bloqueado | Dar CPU **rápido** quando ficar pronto, para ele disparar a próxima E/S e manter o dispositivo ocupado |

> 🎯 **Decore:** *CPU-bound = surto longo; I/O-bound = surto curto. O I/O-bound deve ter preferência.*

## d) Quando ocorre a troca de processo e o que o escalonador decide

| Situação | Troca é obrigatória? | Decisões possíveis |
|---|---|---|
| **1. Criação** de processo | Não | Continuar com o pai ou rodar o filho (ou outro pronto) |
| **2. Término** do processo em execução | **Sim** | Escolher outro pronto (se não houver, roda o processo ocioso) |
| **3. Bloqueio** (E/S, semáforo...) | **Sim** | Escolher outro pronto; pode considerar a causa do bloqueio |
| **4. Interrupção de E/S** (processo bloqueado → pronto) | Não | Rodar o recém-pronto, manter o atual ou escolher outro |
| **5. Interrupção de relógio** (fim do quantum) | Não (só em preemptivo) | Manter o atual ou trocar |

> 🎯 **Decore:** *Término e bloqueio forçam a troca. As demais (criação, E/S, relógio) são decisões opcionais; nos não preemptivos o atual simplesmente continua.*

---

# 2) Algoritmos de escalonamento

## a) Relação entre algoritmo e escalonador

O **algoritmo** é a **regra** que define *qual* processo escolher. O **escalonador** é o **componente do SO que executa/implementa** essa regra (com o despachante realizando a troca de contexto).

## b) Critérios de classificação dos algoritmos

1. **Ambiente/tipo de sistema** alvo: lote, interativo ou tempo real;
2. **Sensibilidade às interrupções de relógio**: não preemptivo × preemptivo.

*(Outros critérios úteis: necessidade de conhecer previamente o tempo dos processos; uso de prioridades estáticas ou dinâmicas.)*

## c) Tipos, pela sensibilidade ao relógio

| Tipo | Descrição | Exemplos |
|---|---|---|
| **Não preemptivo** | Ignora o relógio. O processo roda até **terminar, bloquear ou ceder** voluntariamente | FCFS, tarefa mais curta primeiro |
| **Preemptivo** | Usa o relógio para **tirar a CPU** (fim do quantum ou chegada de processo mais urgente) | Circular, prioridades, filas múltiplas, próximo de menor tempo restante |

## d) Tipos de sistemas e o que o algoritmo deve buscar

| Sistema | Características | O que o algoritmo deve priorizar |
|---|---|---|
| **Em lote** | Sem usuários esperando; tarefas longas e previsíveis (folha de pagamento, relatórios) | **Vazão**, **tempo de retorno**, **utilização da CPU**. Não preemptivo (ou com quantum grande) basta, pois há menos chaveamento |
| **Interativo** | Usuários aguardando resposta (também vale para servidores) | **Tempo de resposta** e **proporcionalidade** (atender a expectativa do usuário). **Preempção é essencial** para impedir monopólio da CPU |
| **Tempo real** | Existem **prazos (deadlines)**; rígido (hard) ou suave (soft) | **Cumprir prazos** e **previsibilidade**. Preempção muitas vezes desnecessária, pois os processos são curtos e bloqueiam logo |

> 🎯 **Decore:** *Todos buscam **justiça**, **aplicação da política** e **equilíbrio** (manter todas as partes do sistema ocupadas). Lote: vazão/retorno. Interativo: resposta. Tempo real: prazo.*

---

# 3) Escalonamento em sistemas em lote

## a) Principais algoritmos

1. **FCFS** (First Come, First Served)
2. **Tarefa mais curta primeiro** (SJF)
3. **Próximo de menor tempo restante** (SRTN)

## b) FCFS

**Funcionamento:** não preemptivo. Fila FIFO por ordem de chegada; o primeiro roda até terminar ou bloquear.

| Situação | Comportamento |
|---|---|
| **Criação** | Vai para o **fim da fila** |
| **Término** | Próximo da **cabeça** da fila assume |
| **Bloqueio** | Próximo da fila assume; ao desbloquear, o processo vai para o **fim da fila** (como recém-chegado) |

✅ Simples e justo · ❌ **Efeito comboio**: um processo longo atrasa todos os curtos.

## c) Tarefa mais curta primeiro (SJF)

**Funcionamento:** não preemptivo. Entre os prontos, escolhe o de **menor tempo total de execução**. É ótimo para o tempo médio de retorno quando todos estão disponíveis ao mesmo tempo.

| Situação | Comportamento |
|---|---|
| **Criação** | Entra no conjunto de prontos; **não interrompe** o atual, mesmo sendo mais curto |
| **Término** | Escolhe o **mais curto** entre os prontos |
| **Bloqueio** | Escolhe o mais curto entre os prontos; ao desbloquear, volta a competir |

❌ Processos longos podem sofrer **inanição**.

## d) Próximo de menor tempo restante (SRTN)

**Funcionamento:** versão **preemptiva** do SJF. Roda sempre o processo com **menor tempo restante**.

| Situação | Comportamento |
|---|---|
| **Criação** | Se o novo tem tempo **menor que o restante do atual**, **preempta** |
| **Término** | Escolhe o de menor tempo restante entre os prontos |
| **Bloqueio** | Idem; ao desbloquear, é comparado pelo tempo restante |

✅ Ótimo em tempo médio de retorno · ❌ Mais chaveamentos, exige tempo restante atualizado, longos podem sofrer inanição.

## e) Por que precisar saber o tempo de antemão, e por que é fácil em lote?

- **Por que:** os dois algoritmos **ordenam/comparam processos pelo tempo**; sem esse valor não há como decidir quem é mais curto.
- **Por que é fácil em lote:** os trabalhos são **repetitivos e previsíveis** (rodam periodicamente, com histórico de execuções anteriores) e o **usuário/operador informa uma estimativa** ao submeter o job. Se o job ultrapassar o limite declarado, pode ser abortado, o que incentiva estimativas honestas.

## f) Comparação entre FCFS, SJF e SRTN

| Aspecto | FCFS | Tarefa mais curta | Menor tempo restante |
|---|---|---|---|
| **Implementação** | 🟢 Mais simples (fila FIFO) | 🟡 Média (escolher o menor; precisa dos tempos) | 🔴 Mais difícil (comparar a cada chegada, atualizar tempos restantes) |
| **CPU-bound × I/O-bound** | 🔴 Ruim: comboio, I/O-bound espera atrás do longo | 🟡 Favorece curtos, mas não interrompe o longo que já roda | 🟢 Melhor: I/O-bound que volta com pouco tempo restante toma a CPU |
| **Chegadas em instantes distintos** | 🔴 Ignora a duração; só a ordem de chegada importa | 🟡 Só decide quando a CPU é liberada; longo em execução atrasa os curtos que chegaram | 🟢 Reage **imediatamente** à chegada de um processo mais curto |
| **Preempção** | ❌ Não | ❌ Não | ✅ Sim |

## g) Escala e tempo médio de conclusão

| Processo | Duração | Chegada |
|---|---|---|
| A | 40 | 0 |
| B | 20 | 4 |
| C | 15 | 5 |
| D | 17 | 4 |
| E | 10 | 14 |

> **Convenções adotadas:** a coluna "Início da Execução" foi tratada como **instante de chegada**; onde B e D chegam juntos (4 ms), **B vem antes de D** (ordem da tabela); *retorno = término − chegada*.

### FCFS

```
A[0–40] → B[40–60] → D[60–77] → C[77–92] → E[92–102]
```

| | A | B | C | D | E |
|---|---|---|---|---|---|
| **Término** | 40 | 60 | 92 | 77 | 102 |
| **Retorno** | 40 | 56 | 87 | 73 | 88 |

**Média = (40+56+87+73+88) / 5 = 344 / 5 = 68,8 ms**

### Tarefa mais curta primeiro (SJF)

Não preemptivo: A começa sozinho em 0 e só sai em 40; aí entre B, C, D, E vence o mais curto.

```
A[0–40] → E[40–50] → C[50–65] → D[65–82] → B[82–102]
```

| | A | B | C | D | E |
|---|---|---|---|---|---|
| **Término** | 40 | 102 | 65 | 82 | 50 |
| **Retorno** | 40 | 98 | 60 | 78 | 36 |

**Média = (40+98+60+78+36) / 5 = 312 / 5 = 62,4 ms**

### Próximo de menor tempo restante (SRTN)

Pontos-chave da escala: em 4, D (17) preempta A (restam 36); em 5, C (15) preempta D (restam 16); em 14, E (10) **não** preempta C (restam 6).

```
A[0–4] → D[4–5] → C[5–20] → E[20–30] → D[30–46] → B[46–66] → A[66–102]
```

| | A | B | C | D | E |
|---|---|---|---|---|---|
| **Término** | 102 | 66 | 20 | 46 | 30 |
| **Retorno** | 102 | 62 | 15 | 42 | 16 |

**Média = (102+62+15+42+16) / 5 = 237 / 5 = 47,4 ms**

### 📊 Resumo

| Algoritmo | Média |
|---|---|
| FCFS | 68,8 ms |
| Tarefa mais curta | 62,4 ms |
| **Menor tempo restante** | **47,4 ms** ✅ melhor |

---

# 4) Escalonamento em sistemas interativos

## a) Principais algoritmos

Circular (*Round Robin*), por prioridades, por filas múltiplas, garantido, por loteria e por fração justa.

## b) Circular (Round Robin)

**Funcionamento:** preemptivo. Fila circular de prontos; cada processo recebe um **quantum**. Ao fim dele, é preemptado e vai para o **fim da fila**.

| Situação | Comportamento |
|---|---|
| **Criação** | Entra no **fim da fila** |
| **Término** | Próximo da fila assume, com quantum novo |
| **Bloqueio** | Próximo da fila assume; ao desbloquear, o processo vai para o **fim da fila** |

## c) Quantum pequeno × grande

| | ✅ Positivo | ❌ Negativo |
|---|---|---|
| **Pequeno** | Resposta **rápida**, boa interatividade | Muitos chaveamentos → **overhead** (ex.: quantum 4 ms + troca 1 ms = 20% de desperdício) |
| **Grande** | Pouco overhead, CPU bem aproveitada | Resposta **lenta**; se o quantum for maior que os surtos, degenera para **FCFS** |

> 🎯 **Decore:** *quantum é um equilíbrio: pequeno demais desperdiça CPU trocando; grande demais deixa o usuário esperando (regra prática: 20–50 ms).*

## d) Prioridades

**Funcionamento:** cada processo tem uma **prioridade**; roda o pronto de **maior prioridade**. Pode ser estática ou dinâmica; entre prioridades iguais costuma-se usar circular.

| Situação | Comportamento |
|---|---|
| **Criação** | Se a prioridade for **maior que a do atual**, preempta; senão, entra nos prontos |
| **Término** | Assume o pronto de maior prioridade |
| **Bloqueio** | Assume o pronto de maior prioridade; ao desbloquear, preempta se tiver prioridade maior que a do atual |

❌ **Inanição** de baixa prioridade (e inversão de prioridade). A prioridade pode ser reduzida a cada tick para o processo em execução (dinâmica).

## e) Filas múltiplas

**Funcionamento:** **uma fila por classe de prioridade**. Roda a fila **não vazia de maior prioridade**, e dentro dela usa **circular**. Variante (CTSS): classes de baixa prioridade têm **quantum maior**, e o processo que esgota o quantum é rebaixado.

| Situação | Comportamento |
|---|---|
| **Criação** | Entra na fila da sua classe (preempta se a classe for mais alta que a do atual) |
| **Término** | Assume o próximo da fila não vazia mais prioritária |
| **Bloqueio** | Idem; ao desbloquear, volta à sua fila (pode ser promovido, se for I/O-bound) |

## f) Como evitar que baixa prioridade demore demais

1. **Envelhecimento (aging):** aumentar a prioridade de quem espera há muito tempo;
2. **Rebaixar a prioridade** do processo em execução a cada quantum/tick (prioridade dinâmica);
3. **Quantum maior** nas filas baixas (rodam menos vezes, mas por mais tempo), ou **fatia garantida** de CPU para elas (ex.: 80% alta / 20% baixa);
4. **Promoção por tempo de espera** máximo.

## g) Garantido

**Funcionamento:** promete a cada um dos *n* processos **1/n da CPU**. Calcula a razão `tempo de CPU consumido / tempo de CPU a que tem direito` e **roda o de menor razão** (o mais prejudicado), até ultrapassar o próximo.

| Situação | Comportamento |
|---|---|
| **Criação** | Razão inicial ≈ 0 → é o mais prejudicado e ganha CPU até nivelar; a fatia dos demais diminui (1/n) |
| **Término** | *n* diminui, fatia dos demais aumenta; roda o de menor razão |
| **Bloqueio** | Bloqueado não consome CPU, mas o tempo de direito continua correndo → **razão cai** e, ao voltar, tem prioridade (compensação) |

## h) Loteria

**Funcionamento:** cada processo recebe **bilhetes**; a cada decisão sorteia-se um bilhete e o dono ganha a CPU por um quantum. **Mais bilhetes = mais chance**, portanto CPU proporcional.

| Situação | Comportamento |
|---|---|
| **Criação** | Recebe bilhetes e já participa do próximo sorteio |
| **Término** | Bilhetes removidos do sorteio |
| **Bloqueio** | Não concorre; bilhetes podem ser **emprestados** a processo cooperante (ex.: cliente empresta ao servidor) |

✅ Simples, resposta rápida a novos processos, sem inanição (probabilisticamente).

## i) Fração justa

**Funcionamento:** considera o **dono** do processo. A CPU é dividida **igualmente entre usuários** (ou por fração definida), independentemente de quantos processos cada um tem. *Ex.: u1 com 4 processos e u2 com 1 → 50% para cada usuário; o processo de u2 recebe 50%, cada um dos de u1 recebe 12,5%.*

| Situação | Comportamento |
|---|---|
| **Criação** | Novo processo **divide a fatia do dono**; não aumenta a fatia do usuário |
| **Término** | A fatia do usuário se divide entre menos processos (se era o último, a fatia é redistribuída aos outros usuários) |
| **Bloqueio** | O usuário mantém a fatia e a usa com seus outros processos; se todos bloqueiam, a CPU vai a outros usuários e ele acumula **crédito** |

## j) Comparação dos seis algoritmos

| Algoritmo | Simplicidade | Diferencia **processos** | Diferencia **usuários** | Nº de chaveamentos |
|---|---|---|---|---|
| **Circular** | 🟢 Muito simples | ❌ Não (todos iguais) | ❌ Não | 🔴 Muitos (≈ tempo/quantum) |
| **Prioridades** | 🟢 Simples | ✅ Sim (prioridade) | ❌ Não (a menos que a prioridade reflita o dono) | 🟢 Poucos (só ao bloquear/terminar/chegar mais prioritário) |
| **Filas múltiplas** | 🟡 Média (várias filas e regras) | ✅ Sim (classes, quanta) | ❌ Não | 🟡 Intermediário (quanta maiores nas classes baixas reduzem trocas) |
| **Garantido** | 🔴 Complexo (medir consumo, calcular razões) | 🟡 Não (fatia igual; pode ponderar) | 🟡 Parcial (se a garantia for por usuário) | 🔴 Muitos (reavalia razões constantemente) |
| **Loteria** | 🟢 Simples | ✅ Sim (nº de bilhetes) | 🟡 Parcial (bilhetes por usuário) | 🔴 Muitos (um sorteio por quantum) |
| **Fração justa** | 🔴 Complexo (contabilidade por usuário + política interna) | 🟡 Depende da política dentro do usuário | ✅ **Sim (é seu objetivo)** | 🔴 Muitos |

## k) Escala: circular, prioridades e filas múltiplas (quantum = 5 ms)

| Processo | Duração | Chegada | Prioridade |
|---|---|---|---|
| A | 40 | 0 | 3 |
| B | 20 | 4 | 1 |
| C | 15 | 5 | 2 |
| D | 17 | 4 | 1 |
| E | 10 | 14 | 0 |

*(menor número = maior prioridade)*

> **Convenções adotadas (diga-as na prova, se a resposta depender delas):**
> - Quem chega no mesmo instante em que um quantum termina entra na fila **antes** do processo preemptado;
> - **Prioridades:** preemptivo, estático; empate resolvido em **FCFS** (B antes de D; B mantém a vez dele);
> - **Filas múltiplas:** uma fila por prioridade, **circular com quantum 5** dentro da fila, e fila mais alta preempta as mais baixas (sem rebaixamento).

### Circular (q = 5)

```
A[0–5] B[5–10] D[10–15] C[15–20] A[20–25] B[25–30] E[30–35] D[35–40] C[40–45]
A[45–50] B[50–55] E[55–60] D[60–65] C[65–70] A[70–75] B[75–80] D[80–82] A[82–102]
```

| | A | B | C | D | E |
|---|---|---|---|---|---|
| **Término** | 102 | 80 | 70 | 82 | 60 |
| **Retorno** | 102 | 76 | 65 | 78 | 46 |

**Média = (102+76+65+78+46) / 5 = 367 / 5 = 73,4 ms**

### Prioridades (preemptivo, empate FCFS)

Em 4, B e D (prio 1) preemptam A (prio 3). Em 14, E (prio 0) preempta B, que ainda tinha 10 ms. Depois de E, B volta antes de D (empate por chegada).

```
A[0–4] → B[4–14] → E[14–24] → B[24–34] → D[34–51] → C[51–66] → A[66–102]
```

| | A | B | C | D | E |
|---|---|---|---|---|---|
| **Término** | 102 | 34 | 66 | 51 | 24 |
| **Retorno** | 102 | 30 | 61 | 47 | 10 |

**Média = (102+30+61+47+10) / 5 = 250 / 5 = 50,0 ms**

### Filas múltiplas (fila por prioridade + circular q = 5)

B e D (fila 1) se alternam em quanta de 5 ms; E (fila 0) passa na frente em 14; depois C (fila 2) e por último A (fila 3).

```
A[0–4] → B[4–9] D[9–14] → E[14–24] → B[24–29] D[29–34] B[34–39] D[39–44] B[44–49] D[49–51] → C[51–66] → A[66–102]
```

| | A | B | C | D | E |
|---|---|---|---|---|---|
| **Término** | 102 | 49 | 66 | 51 | 24 |
| **Retorno** | 102 | 45 | 61 | 47 | 10 |

**Média = (102+45+61+47+10) / 5 = 265 / 5 = 53,0 ms**

### 📊 Resumo

| Algoritmo | Média |
|---|---|
| Circular | 73,4 ms |
| **Prioridades** | **50,0 ms** ✅ melhor |
| Filas múltiplas | 53,0 ms |

> 💡 O circular tem a pior média de retorno (todos são "atrasados" pelos demais), mas é o que dá **melhor tempo de resposta** (todo mundo começa logo).

## l) Escala: fração justa

> **Convenções adotadas:**
> - A lista fala em "B, D e **F**", mas não existe F; assumi que é **E** (usuário 1: A, C; usuário 2: B, D, E);
> - Cada usuário tem direito a **50%**;
> - A cada quantum (5 ms) roda um processo do **usuário que consumiu menos CPU** até então; empate → quem foi atendido há mais tempo;
> - Dentro de cada usuário, os processos se alternam em **circular**.

```
A[0–5] B[5–10] C[10–15] D[15–20] A[20–25] B[25–30] C[30–35] E[35–40] A[40–45]
D[45–50] C[50–55] B[55–60] A[60–65] E[65–70] A[70–75] D[75–80] A[80–85] B[85–90]
A[90–95] D[95–97] A[97–102]
```

| | A | B | C | D | E |
|---|---|---|---|---|---|
| **Término** | 102 | 90 | 55 | 97 | 70 |
| **Retorno** | 102 | 86 | 50 | 93 | 56 |

**Média = (102+86+50+93+56) / 5 = 387 / 5 = 77,4 ms**

> 💡 Repare: enquanto os dois usuários têm processos prontos, **cada usuário recebe ~50% da CPU** (u1 tem 2 processos, u2 tem 3, então os processos de u2 recebem menos cada um). A média de retorno é a mais alta porque o objetivo aqui é **justiça entre usuários**, não desempenho.

---

# 5) Política × mecanismo de escalonamento

| | **Mecanismo** | **Política** |
|---|---|---|
| **Ideia** | **Como** escalonar: o algoritmo implementado no núcleo | **O que** priorizar: os **parâmetros** que orientam o algoritmo (prioridade, quantum, fração) |
| **Quem define** | O **núcleo** (fixo) | O **usuário/processo** (configurável) |

**Em sistemas com hierarquia de processos:** o **pai** conhece a importância de seus filhos (ex.: servidor com filhos que atendem tarefas de importância diferente). Por isso o núcleo oferece uma **chamada de sistema** para o pai definir a **prioridade dos filhos**. O escalonador do núcleo (mecanismo) apenas aplica esses valores (política do pai), montando a escala de CPU respeitando a hierarquia.

> 🎯 **Decore:** *mecanismo = núcleo (como); política = processo (o quê). Separar os dois deixa o escalonamento flexível sem mexer no núcleo.*

---

# 6) Escalonamento de threads com os algoritmos vistos

Todos os algoritmos (circular, prioridades, filas etc.) podem escalonar threads, mudando **quem** os executa:

| | **Threads no espaço do usuário** | **Threads no núcleo** |
|---|---|---|
| **Quem escalona** | Dois níveis: o **núcleo escalona processos** e o **runtime** de cada processo escalona as threads dele | Só o **escalonador do núcleo**, que escolhe **diretamente entre as threads** (de qualquer processo) |
| **Algoritmo** | **Cada processo pode ter o seu** (o runtime é código da aplicação) | **Um só** para todo o sistema |
| **Quantum** | É do **processo** e dividido entre suas threads | É de cada **thread** |
| **Preempção** | ❌ Sem interrupção de relógio para o runtime: circular puro **não funciona**, depende de `thread_yield` (cooperativo). Prioridades e FCFS funcionam | ✅ Relógio funciona; todos os algoritmos preemptivos funcionam |
| **Custo de troca** | Muito baixo (procedimento local) | Mais alto (chamada de sistema); trocar entre threads de **processos diferentes** é ainda mais caro (troca de espaço de endereçamento), então o núcleo pode **preferir threads do mesmo processo** |

> 🎯 **Decore:** *Usuário: dois níveis (SO escalona processos, runtime escalona threads) e algoritmo livre por processo, mas sem preempção. Núcleo: um nível, um algoritmo, com preempção.*

---

## ✅ Checklist final

- [ ] Sei os 5 momentos de troca e quais são obrigatórios (término e bloqueio)
- [ ] Sei o comportamento de cada algoritmo em **criação, término e bloqueio**
- [ ] Refaço a escala e as médias do 3g (**68,8 / 62,4 / 47,4**) sem olhar
- [ ] Refaço as do 4k (**73,4 / 50,0 / 53,0**) e do 4l (**77,4**) sem olhar
- [ ] Sei comparar os algoritmos pelos critérios do 3f e do 4j
- [ ] Sei explicar como os algoritmos escalonam threads no usuário × núcleo