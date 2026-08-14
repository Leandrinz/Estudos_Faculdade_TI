# Seção 4 — Comunicação Entre Processos com Espera Ociosa

> Base para a **Lista 04 — Comunicação Entre Processos com Espera Ociosa**
> Referência: Tanenbaum, *Sistemas Operacionais Modernos*, cap. 2.3.1 a 2.3.4

---

## 4.1 Conceitos fundamentais

### Condições de corrida (*race conditions*)

**Definição:** situação em que **dois ou mais processos (ou threads) acessam dados
compartilhados concorrentemente** e o **resultado final depende da ordem/velocidade
exata em que suas instruções são intercaladas** pelo escalonador.

Três elementos precisam coexistir:
1. **Recurso compartilhado** (memória, arquivo, variável, dispositivo);
2. **Acesso concorrente**, com pelo menos uma **escrita**;
3. **Ausência de sincronização** entre os acessos.

**Exemplo clássico — o spooler de impressão (Tanenbaum):**
Existe um diretório de spool com entradas numeradas e duas variáveis compartilhadas:
`out` (próximo arquivo a imprimir) e `in` (próxima posição livre). Suponha `in = 7`.

1. O processo **A** lê `in` e guarda `proximo_livre = 7`.
2. **Justo nesse instante** o escalonador preempta A e escalona **B**.
3. **B** lê `in` (ainda 7), grava seu arquivo na posição 7 e atualiza `in = 8`. B termina
   e sai satisfeito.
4. **A** volta a executar. Ele ainda tem `proximo_livre = 7` na sua variável local, então
   **sobrescreve a posição 7** com seu próprio arquivo — **apagando o de B** — e escreve
   `in = 8`.

**Resultado:** o arquivo de B desaparece silenciosamente. O sistema continua consistente
do ponto de vista estrutural; nenhum erro é reportado. B simplesmente nunca imprime.

**A característica mais perigosa:** o erro é **não determinístico e intermitente**. Se o
chaveamento tivesse ocorrido um pouco antes ou depois, tudo funcionaria. Rodar o programa
mil vezes pode dar certo mil vezes — e falhar na milésima primeira, em produção. São bugs
notoriamente difíceis de reproduzir e depurar.

### Regiões críticas

**Definição:** a **região crítica** (ou *seção crítica*) é o **trecho de código de um
processo em que ele acessa memória (ou recursos) compartilhada**, e onde, portanto,
condições de corrida podem ocorrer.

No exemplo do spooler, a região crítica de cada processo são as três instruções: ler `in`,
gravar o arquivo, atualizar `in`.

### Relação entre região crítica e condição de corrida

- A **região crítica é a *causa potencial***; a **condição de corrida é o *efeito
  indesejado***.
- Condições de corrida só ocorrem quando **dois processos estão simultaneamente dentro de
  suas regiões críticas** referentes ao **mesmo** recurso.
- Fora das regiões críticas, os processos podem executar em qualquer ordem, com qualquer
  intercalação, sem problema algum.
- **Conclusão:** para eliminar as condições de corrida **não é preciso proibir o
  paralelismo** — basta **garantir que nunca haja mais de um processo dentro da região
  crítica de um mesmo recurso ao mesmo tempo**. Esse é o objetivo de toda a disciplina de
  comunicação entre processos.

> ⚠️ Observação importante: regiões críticas **de recursos diferentes** podem ser
> executadas em paralelo sem problema. O que precisa ser serializado é o acesso ao **mesmo**
> recurso.

### Exclusão mútua (*mutual exclusion*)

**Definição:** propriedade que garante que, **enquanto um processo estiver executando sua
região crítica sobre um recurso, nenhum outro processo poderá entrar na região crítica
correspondente ao mesmo recurso**. O acesso é serializado.

**Por que é o conceito central:**
- É a **condição mínima e suficiente** para eliminar condições de corrida. Se apenas um
  processo por vez manipula o dado, não há intercalação possível de operações conflitantes.
- Permite tratar a região crítica como se fosse **atômica**: do ponto de vista dos outros
  processos, ela ou aconteceu inteira ou não aconteceu.
- Preserva os **invariantes** dos dados compartilhados: a estrutura é vista sempre
  consistente por quem está de fora.
- **Preserva o paralelismo onde ele é seguro:** só a região crítica é serializada; todo o
  resto do código continua concorrente. Por isso as regiões críticas devem ser **as
  menores possíveis**.
- É a base sobre a qual **todos** os mecanismos posteriores (travas, TSL, Peterson,
  semáforos, mutexes, monitores) são construídos — cada um é apenas uma forma diferente
  de implementá-la.

**Estrutura de qualquer solução:**
```c
while (TRUE) {
    entrar_regiao_critica();     /* protocolo de entrada */
    regiao_critica();            /* acesso ao recurso compartilhado */
    sair_regiao_critica();       /* protocolo de saída */
    regiao_nao_critica();        /* trabalho independente — paralelo livre */
}
```

---

## 4.2 As quatro condições para uma solução correta e eficiente

Uma boa solução de exclusão mútua deve satisfazer **todas** as quatro:

### (i) Exclusão mútua — *"Nunca dois processos simultaneamente dentro da mesma região crítica"*

É o requisito de **corretude**. Sem ele, a solução não resolve o problema que se propôs a
resolver: as condições de corrida continuam acontecendo. É a condição de **segurança
(safety)**: "nada de ruim acontece".

### (ii) Nada pode ser afirmado sobre a velocidade ou o número de CPUs

A solução **não pode depender de premissas sobre o tempo**: não pode supor que uma
instrução é mais rápida que outra, que o chaveamento ocorre em certos pontos, que há
apenas uma CPU, ou que as CPUs têm a mesma frequência.

**Por quê:**
- O escalonador é **imprevisível** e pode preemptar **entre quaisquer duas instruções de
  máquina** — inclusive no meio de um `count++`, que na verdade são três instruções (ler,
  incrementar, gravar).
- Interrupções, faltas de página e migração entre núcleos alteram o tempo relativo.
- **Multiprocessadores** executam instruções de processos distintos **realmente ao mesmo
  tempo** — uma solução que só funcione por acaso em CPU única falha imediatamente.
- Frequências variam (turbo boost, throttling térmico, hardware diferente).

**Consequência prática:** a corretude deve ser **estrutural** (garantida pela lógica do
algoritmo, para qualquer intercalação possível), nunca **temporal**. Se para justificar
que sua solução funciona você precisa dizer "mas é muito improvável que o chaveamento
ocorra exatamente aí", a solução está errada.

### (iii) Nenhum processo fora de sua região crítica pode bloquear outro processo

Um processo que está executando seu **código não crítico** (ou que já saiu da região
crítica, ou que ainda nem chegou nela, ou que travou/terminou fora dela) **não pode
impedir** que outro entre em sua região crítica.

**Por quê:**
- A região não crítica pode ser **arbitrariamente longa** (esperar entrada do usuário,
  fazer E/S de rede, computar por minutos) ou até **infinita**. Se um processo aí pudesse
  bloquear outro, o sistema pararia sem nenhuma razão.
- Um processo pode **terminar ou falhar** fora da região crítica; isso não pode travar os
  demais permanentemente.
- É a condição que garante **eficiência e disponibilidade**: se a região crítica está
  livre, quem quiser entrar deve conseguir entrar **imediatamente**, sem depender da
  boa vontade ou do progresso de terceiros. (Em teoria da concorrência isso corresponde à
  propriedade de **progresso**.)

### (iv) Nenhum processo deve esperar eternamente para entrar em sua região crítica

Todo processo que **queira** entrar deve entrar **em tempo finito** — não pode haver
**inanição (starvation)**.

**Por quê:**
- É a condição de **vivacidade (liveness)**: "algo de bom eventualmente acontece".
- Impede que processos "azarados" ou de baixa prioridade sejam sistematicamente
  ultrapassados por outros que continuam chegando.
- Sem ela, uma solução pode ser tecnicamente correta (não viola a exclusão mútua) e ainda
  assim ser **inutilizável**, porque alguns processos nunca progridem.
- Também é o que descarta soluções sujeitas a **deadlock** (todos travados esperando
  mutuamente) e a **livelock** (todos ativos, mas nenhum avançando).

> **Mnemônico:** (i) *correção*, (ii) *generalidade*, (iii) *progresso*, (iv) *ausência de
> inanição*. As soluções abaixo serão avaliadas exatamente contra essas quatro.

---

## 4.3 Mecanismo 1 — Desabilitação de interrupções

### Como funciona

```c
desabilitar_interrupcoes();   /* instrução privilegiada da CPU */
   /* região crítica */
habilitar_interrupcoes();
```

Logo ao entrar na região crítica, o processo **desabilita todas as interrupções** da CPU;
ao sair, as reabilita.

### Por que isso evita condições de corrida em sistemas de CPU única

A alternância entre processos **só acontece por meio de interrupções** — principalmente a
**interrupção de relógio**, que devolve o controle ao escalonador ao fim do quantum. Com as
interrupções desabilitadas:

- O relógio não interrompe → **o escalonador nunca é chamado** → não há chaveamento.
- Nenhum dispositivo de E/S gera interrupção → nenhuma RSI executa.
- Logo, o processo executa sua região crítica **do início ao fim sem ser interrompido**,
  de forma **efetivamente atômica**.
- Com **uma única CPU**, "não ser interrompido" equivale a "ser o único executando no
  sistema inteiro" → **exclusão mútua garantida**.

É a solução mais simples e absolutamente eficaz — **nessas condições específicas**.

### Por que não é adequada: segurança

1. **É uma instrução privilegiada.** Só pode ser executada em modo núcleo. Dar esse poder
   a processos de usuário é **imprudente**: bastaria um processo malicioso desabilitar as
   interrupções e **nunca mais reabilitá-las** para travar a máquina inteira,
   permanentemente — seria necessário reiniciar o computador.
2. **Um simples bug tem o mesmo efeito.** Um `return` antecipado, uma exceção, um laço
   infinito ou um travamento dentro da região crítica deixam as interrupções desligadas
   para sempre. Não há como o SO recuperar o controle — ele **é** o que foi desligado.
3. **O SO perde o controle do sistema.** Enquanto as interrupções estão off, o SO não
   consegue: contabilizar tempo de CPU, tratar E/S, atender ao usuário, aplicar políticas
   de escalonamento, ou matar o processo faltoso.
4. **Interrupções perdidas.** Interrupções que ocorram no período podem ser descartadas
   ou atrasadas, causando perda de dados de dispositivos e degradação de sistemas de tempo
   real e multimídia.

### Por que não é adequada: ineficácia em multiprocessadores

Este é o argumento **decisivo**:

- A instrução de desabilitar interrupções **afeta apenas a CPU que a executou**.
- Em uma máquina com **N CPUs**, as outras **N−1** continuam funcionando normalmente,
  recebendo interrupções e **executando processos**.
- Um desses processos pode perfeitamente entrar na **mesma região crítica** e acessar a
  **mesma memória compartilhada** ao mesmo tempo → **a exclusão mútua é violada** e a
  condição de corrida acontece exatamente como se nada tivesse sido feito.
- Fazer todas as CPUs desabilitarem interrupções exigiria uma comunicação entre elas
  (interrupção interprocessador) que é **cara, lenta e ela própria precisaria de
  sincronização** — o problema volta ao começo.
- Além disso, mesmo que funcionasse, **paralisar todas as CPUs** para proteger uma
  variável destruiria completamente o desempenho da máquina, que é a razão de existir do
  multiprocessador.

### Veredito

| Condição | Atende? |
|---|---|
| (i) Exclusão mútua | ✅ em CPU única / ❌ em multiprocessador |
| (ii) Independência de velocidade/nº de CPUs | ❌ **viola frontalmente** |
| (iii) Não bloquear de fora da RC | ✅ (mas bloqueia **todo o sistema** de dentro) |
| (iv) Sem espera eterna | ✅ (se o código for correto) |

**Uso legítimo:** o **próprio núcleo** usa essa técnica internamente, por **pouquíssimas
instruções**, para proteger estruturas como a lista de processos prontos. É uma técnica de
kernel, **não uma primitiva de usuário**.

---

## 4.4 Mecanismo 2 — Variáveis de trava (*lock variables*)

### Como funciona

Usa-se uma **variável compartilhada `lock`**, inicialmente **0** (região crítica livre).
1 significa ocupada.

```c
while (TRUE) {
    while (lock != 0) ;      /* espera ociosa: fica testando até liberar */
    lock = 1;                /* marca como ocupada */
    regiao_critica();
    lock = 0;                /* libera */
    regiao_nao_critica();
}
```

A ideia é intuitiva: "olho a placa na porta; se estiver 'livre', escrevo 'ocupado' e entro".

### Por que não atende à condição (i) — exclusão mútua

**O defeito é fatal: testar e alterar a variável são operações separadas**, e o
escalonador pode preemptar o processo **exatamente entre as duas**.

**Sequência de falha:**

| Instante | Processo A | Processo B | `lock` |
|---|---|---|---|
| t₁ | Lê `lock` → vê **0** (livre), sai do laço | — | 0 |
| t₂ | **PREEMPTADO** (antes de executar `lock = 1`) | — | 0 |
| t₃ | — | Lê `lock` → vê **0** (livre!), sai do laço | 0 |
| t₄ | — | Executa `lock = 1` | 1 |
| t₅ | — | **Entra na região crítica** | 1 |
| t₆ | Volta a executar; retoma **de onde parou**: `lock = 1` | (na RC) | 1 |
| t₇ | **Entra na região crítica** | (na RC) | 1 |

**Resultado:** A e B estão **simultaneamente dentro da região crítica**. A exclusão mútua
foi violada.

### A raiz do problema

Existe uma **janela de vulnerabilidade** entre o **teste** (`lock != 0`) e a **atribuição**
(`lock = 1`). Nesse intervalo, a informação lida por A tornou-se obsoleta sem que A
soubesse.

O problema é **exatamente o mesmo do spooler de impressão** — só que agora ele apareceu
**dentro do próprio mecanismo criado para resolvê-lo**. A variável `lock` é ela mesma um
recurso compartilhado com uma região crítica não protegida. É uma regressão infinita: para
proteger `lock` precisaríamos de outra trava, que precisaria de outra...

**A lição:** a exclusão mútua **não pode ser construída apenas com leituras e escritas
independentes em software**. É preciso que **testar e alterar** sejam uma **única operação
atômica e indivisível** — que é precisamente o que a instrução **TSL** oferece (seção 4.7),
ou o que a alternância estrita e a solução de Peterson conseguem por caminhos puramente
algorítmicos.

Note também que o problema **não** é resolvido invertendo a ordem, nem adicionando mais
testes: qualquer sequência de leituras e escritas separadas tem alguma intercalação ruim.

### Veredito

| Condição | Atende? |
|---|---|
| (i) Exclusão mútua | ❌ **VIOLA** |
| (ii) Independência de velocidade | ❌ (só "funciona" se a preempção não cair na janela) |
| (iii) Não bloquear de fora da RC | ✅ |
| (iv) Sem espera eterna | ✅ (mas irrelevante, pois já é incorreta) |

---

## 4.5 Mecanismo 3 — Alternância estrita / Chaveamento obrigatório

### Como funciona

Uma variável compartilhada **`turn`** indica **de quem é a vez** de entrar na região
crítica.

**Processo 0:**
```c
while (TRUE) {
    while (turn != 0) ;          /* espera ociosa até ser sua vez */
    regiao_critica();
    turn = 1;                    /* passa a vez para o processo 1 */
    regiao_nao_critica();
}
```

**Processo 1:**
```c
while (TRUE) {
    while (turn != 1) ;          /* espera ociosa até ser sua vez */
    regiao_critica();
    turn = 0;                    /* passa a vez para o processo 0 */
    regiao_nao_critica();
}
```

> Este é exatamente o código da **primeira figura da lista 04**.

**Por que a exclusão mútua é garantida:** `turn` tem um único valor por vez. Se `turn = 0`,
o processo 1 fica preso em seu laço; se `turn = 1`, o processo 0 fica preso. Só um consegue
passar. Além disso, `turn` é **escrita apenas por quem está saindo** da região crítica — não
há a janela teste-depois-escreve do mecanismo anterior, porque quem escreve não é quem
está testando.

**Espera ociosa (*busy waiting*):** o laço `while (turn != 0);` consome CPU integralmente
sem fazer trabalho útil. Uma trava implementada com espera ociosa é chamada de
***spin lock***.

### Por que não atende à condição (iii)

**A condição (iii) diz:** *nenhum processo fora de sua região crítica pode bloquear outro
processo*.

**Cenário de violação:**

1. Suponha `turn = 0`. O processo 0 entra na região crítica, executa e sai, fazendo
   `turn = 1`.
2. O processo 0 entra em sua **região não crítica**, que é **muito longa** (por exemplo,
   ele espera uma entrada do usuário, faz uma requisição de rede, ou simplesmente executa
   um trecho demorado).
3. Enquanto isso, o processo 1 entra na região crítica, sai rapidamente e faz `turn = 0`.
4. O processo 1 executa sua região não crítica, que é **muito curta**, e volta querendo
   entrar de novo na região crítica.
5. O processo 1 testa: `turn != 1` → **fica preso no laço**.
6. **Mas a região crítica está VAZIA!** Ninguém está usando o recurso. O processo 1 poderia
   entrar sem causar nenhum dano.
7. O processo 1 só será liberado quando o processo 0 **terminar sua região não crítica,
   entrar na região crítica, sair dela e fazer `turn = 1`**.

**Conclusão:** o processo 0, estando **fora** de sua região crítica (na região **não
crítica**), está **bloqueando** o processo 1. Isso viola diretamente a condição (iii).

**Agravante:** se o processo 0 **travar, entrar em laço infinito ou terminar** enquanto
está em sua região não crítica, o processo 1 fica bloqueado **para sempre** — mesmo com a
região crítica livre. Isso viola também a condição (iv).

### A raiz do problema

A alternância estrita impõe uma **ordem rígida e obrigatória**: 0, 1, 0, 1, 0, 1, ...
Nenhum processo pode entrar **duas vezes seguidas** na região crítica, mesmo que seja o
único interessado.

Ou seja, o algoritmo **acopla o direito de entrar ao progresso do outro processo**. Ele
confunde "garantir exclusão mútua" com "forçar revezamento". A exclusão mútua exige apenas
que **não haja dois dentro ao mesmo tempo** — não que eles se alternem.

Isso torna o desempenho refém do **processo mais lento**: a taxa de entradas na região
crítica é limitada pelo processo de região não crítica mais longa.

### Veredito

| Condição | Atende? |
|---|---|
| (i) Exclusão mútua | ✅ |
| (ii) Independência de velocidade | ✅ (a corretude é estrutural) |
| (iii) Não bloquear de fora da RC | ❌ **VIOLA** |
| (iv) Sem espera eterna | ❌ (se o outro processo travar fora da RC) |

Problemas adicionais: **espera ociosa** (desperdiça CPU) e **funciona apenas para 2
processos**.

---

## 4.6 Mecanismo 4 — Solução de Peterson (1981)

### O código (segunda figura da lista 04)

```c
#define FALSE 0
#define TRUE  1
#define N     2                    /* número de processos */

int turn;                          /* de quem é a vez? */
int interested[N];                 /* todos inicialmente 0 (FALSE) */

void enter_region(int process)     /* process vale 0 ou 1 */
{
    int other;                     /* número do outro processo */

    other = 1 - process;                    /* o oposto do processo */
    interested[process] = TRUE;             /* mostra que está interessado */
    turn = process;                         /* altera o valor de turn */
    while (turn == process && interested[other] == TRUE) ;  /* espera ociosa */
}

void leave_region(int process)     /* quem está saindo */
{
    interested[process] = FALSE;   /* indica a saída da região crítica */
}
```

### Como funciona

A solução combina **duas variáveis** que resolvem, cada uma, uma metade do problema:

- **`interested[i]`** — declaração de intenção: "eu quero entrar". Resolve o problema da
  alternância estrita: se o outro **não está interessado**, o laço termina imediatamente e
  eu entro, mesmo que seja a minha segunda vez seguida.
- **`turn`** — critério de desempate: usado **apenas** quando ambos querem entrar ao mesmo
  tempo.

**Passo a passo de `enter_region(i)`:**
1. `interested[i] = TRUE` — sinaliza a intenção;
2. `turn = i` — **cede a vez ao outro** (é o passo contraintuitivo e genial);
3. Espera enquanto `turn == i && interested[other] == TRUE`.

**Caso 1 — só um processo quer entrar.** O processo 0 chama `enter_region(0)`.
`interested[1]` é `FALSE`, então a condição do `while` é falsa imediatamente → **entra
sem esperar**. Pode fazer isso quantas vezes quiser em sequência. *(Resolve o defeito da
alternância estrita.)*

**Caso 2 — ambos querem entrar "ao mesmo tempo".** Ambos fazem `interested[i] = TRUE`.
Ambos escrevem em `turn`. Como `turn` é uma única variável, **a última escrita
sobrescreve a anterior** — digamos que o processo 1 escreve por último, deixando
`turn = 1`.
- Processo **1**: testa `turn == 1` (verdadeiro) `&& interested[0] == TRUE` (verdadeiro) →
  **fica esperando**.
- Processo **0**: testa `turn == 0` → **falso** (turn vale 1) → **entra na região crítica**.

**O truque central:** ao escrever `turn = i`, cada processo **abre mão da vez em favor do
outro**. Se os dois forem "educados", quem escreveu por **último** perde — e como só há
uma escrita final possível, **exatamente um** ganha. É impossível que ambos entrem
(exclusão mútua ✅) e impossível que ambos fiquem presos (sem deadlock ✅).

**Saída:** `leave_region(i)` apenas faz `interested[i] = FALSE`, o que libera o outro
processo do laço.

**Vantagens:** funciona **puramente em software**, sem instrução especial de hardware; não
requer alternância; satisfaz as condições (i), (ii) e (iii); é generalizável para N
processos (algoritmo da padaria de Lamport).

**Desvantagem intrínseca:** usa **espera ociosa** (*spin lock*) — o processo que espera
queima CPU em um laço sem trabalho útil.

### Por que não atende à condição (iv) — o problema da inversão de prioridades

> ⚠️ Este é o mesmo argumento cobrado para a instrução TSL (questão 6.b da lista). Vale
> para **qualquer** solução que use **espera ociosa**.

**Cenário:** um sistema com **escalonamento por prioridades** e **preempção**, em uma
**única CPU**, com dois processos:
- **H** — processo de **alta** prioridade;
- **L** — processo de **baixa** prioridade.

**Regra do escalonador:** sempre que H estiver **pronto**, ele executa. L só executa quando
H estiver bloqueado ou terminado.

**Sequência do impasse:**

1. **L** está executando (H está bloqueado, esperando E/S, por exemplo).
2. **L chama `enter_region(L)` e entra na região crítica.** `interested[L] = TRUE`.
3. **H fica pronto** (sua E/S terminou) e **preempta L imediatamente** — L é interrompido
   **dentro da região crítica**, sem ter chamado `leave_region`.
4. **H chama `enter_region(H)`.** Ele encontra `interested[L] == TRUE` e `turn == H` →
   entra no **laço de espera ociosa**.
5. **H fica girando no laço, consumindo 100% da CPU.**
6. **L precisaria executar** para sair da região crítica e fazer `interested[L] = FALSE`.
   Mas **L nunca é escalonado**, porque H tem prioridade maior **e está pronto** (girando
   no laço, ele nunca bloqueia — do ponto de vista do escalonador, está trabalhando).
7. **Impasse permanente:** H espera por L; L espera pela CPU que H monopoliza.

**Por que isso viola a condição (iv):** H **espera eternamente** para entrar em sua região
crítica. Não é um atraso — é um bloqueio permanente. Nenhum evento externo resolve.

**Por que é chamado de "inversão de prioridades":** na prática, o processo de **baixa**
prioridade (L) está impedindo o de **alta** prioridade (H) de progredir. A hierarquia de
prioridades foi **invertida** na prática.

**Observação crucial:** o problema **não é** um defeito lógico do algoritmo de Peterson. O
algoritmo é logicamente correto. O problema é a **interação entre espera ociosa e
escalonamento preemptivo por prioridades**: a espera ociosa mantém o processo no estado
**pronto/executando**, e o escalonador não tem como perceber que ele está "esperando" por
algo. Se H **bloqueasse** em vez de girar, L seria escalonado, sairia da RC, e tudo
funcionaria.

**Por isso este é o argumento definitivo para abandonar a espera ociosa** e adotar
primitivas com **bloqueio** — `sleep`/`wakeup`, semáforos, mutexes e monitores — que é
exatamente o assunto da Seção 5.

*(Soluções conhecidas para a inversão de prioridades: **herança de prioridade** — L herda
temporariamente a prioridade de H enquanto detém a trava — e **teto de prioridade**. O caso
real mais famoso foi o do rover **Mars Pathfinder**, em 1997, que reiniciava
repetidamente na superfície de Marte por causa desse problema.)*

### Veredito

| Condição | Atende? |
|---|---|
| (i) Exclusão mútua | ✅ |
| (ii) Independência de velocidade/nº de CPUs | ✅ |
| (iii) Não bloquear de fora da RC | ✅ |
| (iv) Sem espera eterna | ⚠️ Sim em condições normais; ❌ **com prioridades distintas (inversão de prioridades)** |

Desperdício adicional: **espera ociosa**.

---

## 4.7 Mecanismo 5 — Instrução TSL (*Test and Set Lock*)

### Como funciona

**TSL é uma instrução de máquina** (suporte de **hardware**), presente em praticamente
todos os processadores modernos:

```
TSL RX, LOCK
```

Ela executa, **como uma única operação atômica e indivisível**:
1. **Lê** o conteúdo da palavra de memória `LOCK` e o copia para o registrador `RX`;
2. **Escreve** um valor não zero (tipicamente 1) em `LOCK`.

**O que garante a atomicidade:** a CPU que executa TSL **bloqueia o barramento de memória**
durante toda a operação, impedindo que **qualquer outra CPU** acesse aquela palavra até o
término. Isso é fundamentalmente diferente de desabilitar interrupções — **funciona em
multiprocessadores**, porque o travamento é do barramento, não da CPU.

*(Instrução equivalente em algumas arquiteturas: **XCHG**, que troca atomicamente o
conteúdo de um registrador com o de uma posição de memória. Nas arquiteturas modernas,
`CMPXCHG` / *compare-and-swap*.)*

### Uso em assembly

```asm
enter_region:
    TSL REGISTER, LOCK      ; copia LOCK para o registrador e põe 1 em LOCK
    CMP REGISTER, #0        ; LOCK era zero (estava livre)?
    JNE enter_region        ; se não era zero, estava travado → tenta de novo
    RET                     ; retorna: entrou na região crítica

leave_region:
    MOVE LOCK, #0           ; armazena 0 em LOCK
    RET                     ; retorna
```

**Lógica:**
- Se `LOCK` valia **0** (livre): o registrador recebe 0 → o `CMP` dá igual → não pula →
  **retorna e o processo entra na RC**. E `LOCK` já ficou valendo 1 **na mesma operação
  atômica**, então mais ninguém entra.
- Se `LOCK` valia **1** (ocupada): o registrador recebe 1 → o `CMP` dá diferente → **pula
  de volta** e tenta novamente (espera ociosa).
- `leave_region` simplesmente grava 0 em `LOCK`.

### Por que TSL resolve o que a variável de trava não resolvia

O defeito da variável de trava era a **janela entre testar e escrever**. A instrução TSL
**elimina essa janela por construção**: ler e escrever são **uma única instrução
indivisível**. Não existe instante em que dois processos possam ver `LOCK == 0`
simultaneamente. É a exclusão mútua correta, **em multiprocessadores inclusive**.

É por isso que **todos os mutexes reais** são implementados sobre TSL/XCHG/CAS.

### Por que TSL não atende à condição (iv) — inversão de prioridades

**O argumento é idêntico ao da solução de Peterson**, porque a falha não está no
mecanismo de exclusão mútua, e sim na **espera ociosa** que ele usa (`JNE enter_region`
é um laço que gira consumindo CPU).

**Sequência:**

1. Processo **L** (baixa prioridade) executa `enter_region`, obtém a trava
   (`LOCK` passa de 0 para 1) e **entra na região crítica**.
2. Processo **H** (alta prioridade) fica **pronto** e **preempta L**, que fica parado
   **dentro** da região crítica, com `LOCK == 1`.
3. **H** executa `enter_region`: a instrução TSL retorna 1 (ocupada) → o `CMP` falha → ele
   **volta para `enter_region`** e fica girando indefinidamente.
4. Do ponto de vista do escalonador, **H está executando normalmente** — ele nunca bloqueia,
   nunca faz chamada de sistema, nunca cede a CPU. Está sempre no estado **pronto/em
   execução**.
5. Como H tem prioridade maior, **L jamais é escalonado**.
6. **L nunca executa `leave_region`**, logo `LOCK` nunca volta a 0.
7. **H espera eternamente.**

**Diagnóstico:** a espera ociosa faz com que o processo que espera **pareça produtivo** ao
escalonador. O escalonador, cumprindo corretamente a política de prioridades, dá 100% da
CPU justamente ao processo que **não pode progredir**, e nega CPU ao **único** processo
capaz de destravar a situação.

**Conclusão geral desta lista:** todas as soluções por **espera ociosa** (alternância
estrita, Peterson, TSL) compartilham dois defeitos:
1. **Desperdício de CPU** — ciclos gastos em laços improdutivos;
2. **Risco de inversão de prioridades** — violação da condição (iv) sob escalonamento
   por prioridades.

Isso motiva a mudança de paradigma para **primitivas com bloqueio**, tratadas na Seção 5.

### Veredito

| Condição | Atende? |
|---|---|
| (i) Exclusão mútua | ✅ (inclusive em multiprocessadores) |
| (ii) Independência de velocidade/nº de CPUs | ✅ |
| (iii) Não bloquear de fora da RC | ✅ |
| (iv) Sem espera eterna | ⚠️ Sim em condições normais; ❌ **com prioridades distintas** |

Desperdício adicional: **espera ociosa**. Requisito adicional: **suporte de hardware**.

---

## 4.8 Quadro-resumo geral

| Mecanismo | (i) Excl. mútua | (ii) Indep. velocidade/CPUs | (iii) Não bloqueia de fora | (iv) Sem espera eterna | Espera ociosa? | Nº de processos |
|---|---|---|---|---|---|---|
| **Desabilitar interrupções** | ✅ 1 CPU / ❌ N CPUs | ❌ | ✅ | ✅ | Não | N |
| **Variável de trava** | ❌ | ❌ | ✅ | ✅ | Sim | N |
| **Alternância estrita** | ✅ | ✅ | ❌ | ❌ | Sim | 2 |
| **Solução de Peterson** | ✅ | ✅ | ✅ | ⚠️ (inversão de prioridades) | Sim | 2 (generalizável) |
| **Instrução TSL** | ✅ | ✅ | ✅ | ⚠️ (inversão de prioridades) | Sim | N |

**Mapa mental da progressão:**
```
Desabilitar interrupções → inseguro e inútil em multiprocessador
        ↓
Variável de trava       → a janela teste-escreve quebra a exclusão mútua
        ↓
Alternância estrita     → corrige a exclusão mútua, mas força revezamento
        ↓
Peterson                → corrige o revezamento (software puro)
        ↓
TSL                     → torna atômico o teste-e-escreve (hardware); vale para N processos
        ↓
[TODAS ainda usam ESPERA OCIOSA → desperdício + inversão de prioridades]
        ↓
SEÇÃO 5: sleep/wakeup, semáforos, mutexes, monitores, troca de mensagens (BLOQUEIO)
```

---

## Checklist de revisão da Lista 04

- [ ] Sei definir condição de corrida e contar o exemplo do spooler passo a passo.
- [ ] Sei definir região crítica e explicar que ela é a causa e a corrida é o efeito.
- [ ] Sei definir exclusão mútua e dar 4 razões da sua importância.
- [ ] Sei explicar as 4 condições, uma por uma, com o **porquê** de cada uma.
- [ ] Sei explicar como desabilitar interrupções impede o chaveamento em CPU única.
- [ ] Sei os 4 argumentos de segurança e o argumento decisivo do multiprocessador.
- [ ] Sei desenhar a tabela de falha da variável de trava (violação da condição i).
- [ ] Sei escrever o código da alternância estrita e o cenário de violação da condição iii.
- [ ] Sei escrever o código de Peterson e explicar o papel de `interested[]` e `turn`.
- [ ] Sei explicar o truque do "cede a vez ao outro" e os dois casos (um / ambos querem).
- [ ] Sei escrever o pseudocódigo assembly da TSL e por que ela elimina a janela.
- [ ] Sei narrar a **inversão de prioridades** (H, L, 7 passos) e aplicá-la a Peterson e TSL.
- [ ] Sei preencher o quadro-resumo dos 5 mecanismos × 4 condições.
