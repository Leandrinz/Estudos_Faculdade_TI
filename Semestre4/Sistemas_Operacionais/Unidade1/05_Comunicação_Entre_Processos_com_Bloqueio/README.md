# Seção 5 — Comunicação Entre Processos com Bloqueio

> Base para a **Lista 05 — Comunicação Entre Processos com Bloqueio**
> Referência: Tanenbaum, *Sistemas Operacionais Modernos*, cap. 2.3.4 a 2.3.8

---

## 5.1 Problemas das abordagens com espera ociosa (questão 1)

Todas as soluções da Seção 4 (alternância estrita, Peterson, TSL) fazem o processo que não
consegue entrar na região crítica **girar em um laço testando repetidamente** uma variável.
Isso é a **espera ociosa** (*busy waiting*) e traz os seguintes problemas:

### 1. Desperdício de tempo de CPU

O processo em espera consome **100% do seu quantum** executando um laço que não produz
nenhum trabalho útil. Esse tempo poderia ser usado por outro processo que **tem** trabalho
a fazer. Em sistemas com muita contenção, uma fração significativa da capacidade da máquina
é queimada em laços vazios.

### 2. Inversão de prioridades (o problema mais grave)

Em um sistema com escalonamento **preemptivo por prioridades** e **uma única CPU**:

1. O processo **L** (baixa prioridade) entra na região crítica.
2. O processo **H** (alta prioridade) fica pronto e **preempta L** dentro da RC.
3. **H** tenta entrar e começa a **girar em espera ociosa**.
4. Como H **nunca bloqueia**, o escalonador o considera sempre **pronto/executando** — e,
   sendo mais prioritário, **L nunca recebe a CPU**.
5. L nunca sai da região crítica; **H espera eternamente**. Impasse permanente.

O escalonador está funcionando corretamente e ainda assim o sistema trava — porque o
mecanismo de espera **esconde do SO** a informação de que aquele processo está,
na verdade, esperando.

### 3. Consumo de energia e desgaste

Especialmente crítico em dispositivos móveis e embarcados: o processador roda em plena
carga sem produzir nada, gastando bateria e gerando calor. Impede que a CPU entre em
estados de baixo consumo.

### 4. Contenção de barramento e degradação de cache

Em multiprocessadores, cada iteração de um laço TSL **trava o barramento de memória**.
Vários processos girando geram tráfego intenso, invalidação constante de linhas de cache
(*cache line bouncing*) e **degradam o desempenho de todas as CPUs**, inclusive das que
estão fazendo trabalho útil.

### 5. Não escala com o número de processos

Quanto mais processos disputam a região crítica, mais CPUs ficam girando simultaneamente,
e pior fica a contenção. O desempenho **piora** ao adicionar processadores.

### 6. Ausência de justiça / risco de inanição

Nada garante a ordem de entrada. Um processo azarado pode ser sistematicamente ultrapassado
por outros que chegam e conseguem a trava primeiro (**inanição**), pois não há fila.

### 7. Complexidade de programação

O programador tem de gerenciar manualmente a entrada e a saída da região crítica em todos
os pontos do código. Esquecer um `leave_region` (por um `return` antecipado, uma exceção ou
um desvio) trava o sistema permanentemente.

> **Conclusão que motiva toda a Seção 5:** em vez de o processo **esperar ativamente**, ele
> deve **avisar ao SO que não pode prosseguir e ser posto para dormir**, liberando a CPU.
> Quando a condição mudar, alguém o **acorda**. É a mudança de *espera ociosa* para
> *bloqueio*.

**Ressalva honesta (bom ponto para a prova):** a espera ociosa não é sempre ruim. Quando a
região crítica é **muito curta** (poucas instruções) e há **várias CPUs**, girar por alguns
ciclos pode custar menos que dois chaveamentos de contexto. Por isso o **núcleo** dos SOs
modernos usa **spin locks** internamente. O que é inadequado é usar espera ociosa em
**espaço de usuário** e em **regiões críticas longas**.

---

## 5.2 As primitivas `sleep` e `wakeup`

### O que cada uma faz (questão 2.a)

| Primitiva | Ações realizadas |
|---|---|
| **`sleep()`** | É uma **chamada de sistema** que faz o processo **bloquear a si mesmo**. O SO: (1) muda o estado do processo de *executando* para **bloqueado**; (2) salva o contexto na tabela de processos; (3) retira o processo da fila de prontos e o coloca na fila de bloqueados/espera; (4) **chama o escalonador** para dar a CPU a outro processo. O processo **não consome CPU** enquanto dorme. |
| **`wakeup(processo)`** | É uma **chamada de sistema** que **desperta outro processo**. O SO: (1) localiza o processo indicado na fila de bloqueados; (2) muda seu estado de **bloqueado** para **pronto**; (3) move-o para a fila de prontos. Ele **não vai direto para execução** — passa a concorrer normalmente pelo escalonador. Quem chama `wakeup` **continua executando**. |

**Ganho fundamental:** o par `sleep`/`wakeup` **elimina a espera ociosa**. Como o processo
bloqueado sai da fila de prontos, ele libera a CPU **e** informa ao escalonador seu estado
real — o que também elimina a inversão de prioridades causada por giro.

> Historicamente, `sleep`/`wakeup` foram propostos por Dijkstra. Uma variação comum é
> `sleep(condição)` / `wakeup(condição)` com um endereço de memória usado como rótulo para
> casar quem dorme com quem acorda.

---

## 5.3 Produtor/Consumidor com `sleep`/`wakeup` (questão 2.b — Figura 1)

### O código

**Produtor (Figura 1a):**
```c
#define N 100                          /* número de lugares no buffer */
int count = 0;                         /* número de itens no buffer */

void producer(void)
{
    int item;

    while (TRUE) {
        item = produce_item();         /* gera o próximo item */
        if (count == N) sleep();       /* se o buffer estiver cheio, vá dormir */
        insert_item(item);             /* ponha um item no buffer */
        count = count + 1;             /* incrementa o contador */
        if (count == 1) wakeup(consumer);   /* o buffer estava vazio? acorde o consumidor */
    }
}
```

**Consumidor (Figura 1b):**
```c
#define N 100
int count = 0;

void consumer(void)
{
    int item;

    while (TRUE) {
        if (count == 0) sleep();       /* se o buffer estiver vazio, vá dormir */
        item = remove_item();          /* retire um item do buffer */
        count = count - 1;             /* decremente o contador */
        if (count == N - 1) wakeup(producer);  /* o buffer estava cheio? acorde o produtor */
        consume_item(item);            /* consome o item */
    }
}
```

### Funcionamento pretendido

- O **buffer** tem `N = 100` posições. `count` guarda quantos itens há nele.
- O **produtor** gera itens e os insere. Se o buffer está **cheio** (`count == N`), ele
  não tem onde colocar → **dorme**.
- O **consumidor** retira itens. Se o buffer está **vazio** (`count == 0`), não há o que
  consumir → **dorme**.
- **Os despertares são condicionais e "de borda"**: o produtor só chama `wakeup(consumer)`
  quando `count` passa de 0 para 1 — pois é **exatamente nesse momento** que o consumidor
  pode estar dormindo por buffer vazio. Analogamente, o consumidor só chama
  `wakeup(producer)` quando `count` cai de N para N−1 — o único momento em que o produtor
  pode estar dormindo por buffer cheio.
- O `consume_item(item)` é feito **depois** de decrementar o contador, para que a região
  de manipulação do buffer seja a menor possível.

### Problema 1 — Condição de corrida sobre `count` (acesso irrestrito)

`count` é uma **variável compartilhada** manipulada **sem exclusão mútua** por ambos os
processos. As operações `count = count + 1` e `count = count - 1` **não são atômicas**: em
linguagem de máquina são três instruções (carregar, alterar, armazenar).

**Cenário de falha:** com `count == 5`:

| Instante | Produtor | Consumidor | `count` na memória |
|---|---|---|---|
| t₁ | Carrega `count` no registrador → 5 | — | 5 |
| t₂ | **preemptado** | — | 5 |
| t₃ | — | Carrega `count` → 5 | 5 |
| t₄ | — | Decrementa → 4; **armazena** | **4** |
| t₅ | — | **preemptado** | 4 |
| t₆ | Incrementa seu 5 → 6; **armazena** | — | **6** |

**Resultado:** um item foi inserido e um item foi removido — `count` deveria continuar 5,
mas vale **6**. O contador ficou **inconsistente com o conteúdo real do buffer**, o que a
longo prazo faz o produtor acreditar que o buffer está cheio quando não está (ou o
contrário), levando a bloqueios ou sobrescrita de dados.

### Problema 2 — **Sinal de despertar perdido** (*lost wakeup*) — o defeito clássico

Este é o problema que a questão pede. A raiz é que o **teste da condição e o `sleep` não
são atômicos**: o processo pode ser preemptado **entre** verificar `count == 0` e
efetivamente dormir.

**Sequência de falha (buffer vazio, `count == 0`):**

1. O **consumidor** executa e lê `count`. Vê `count == 0` → decide que vai dormir.
2. **Exatamente nesse ponto**, antes de executar `sleep()`, o escalonador **preempta o
   consumidor** e escalona o **produtor**.
3. O **produtor** produz um item, insere no buffer e faz `count = 1`.
4. O produtor testa: `count == 1`? Sim → executa **`wakeup(consumer)`**.
5. **Mas o consumidor ainda não está dormindo!** Ele está apenas *pronto*, prestes a
   dormir. O sinal de despertar é **enviado a um processo acordado** e, como `wakeup` não
   é armazenado em lugar nenhum, ele é **simplesmente perdido**.
6. O produtor continua produzindo e enchendo o buffer.
7. Em algum momento o consumidor volta a executar e retoma **exatamente de onde parou**:
   executa **`sleep()`** — dormindo, embora **haja itens no buffer**.
8. O produtor continua até encher o buffer (`count == N`), testa `count == N` e também
   **dorme**.
9. **Deadlock: ambos os processos dormem para sempre.** O consumidor espera um `wakeup`
   que já foi perdido; o produtor espera um `wakeup` que só o consumidor poderia enviar.

**Diagnóstico:** o sinal de despertar enviado a um processo **que ainda não está dormindo**
é perdido. O problema é **estrutural**: o par (testar condição, dormir) precisaria ser
**atômico**, mas não é.

**Cenário simétrico:** o produtor lê `count == N`, é preemptado antes de dormir, o
consumidor consome tudo e envia `wakeup(producer)` (perdido), e depois ambos dormem.

**Solução paliativa (e insuficiente):** adicionar um **bit de espera pelo despertar**
(*wakeup waiting bit*): se o `wakeup` chegar para um processo acordado, o bit é ligado; ao
tentar dormir com o bit ligado, o processo apenas desliga o bit e continua. Isso resolve
para **dois** processos, mas com três ou mais seria preciso um bit por processo — e depois
mais bits — a complexidade explode sem resolver o caso geral.

**A solução correta:** primitivas que façam **testar e bloquear atomicamente** →
**semáforos**.

---

## 5.4 Semáforos

### O que são (questão 3.a)

Um **semáforo** (Dijkstra, 1965) é uma **variável inteira não negativa**, mantida e
protegida pelo SO, usada para **contabilizar a quantidade de sinais de despertar
armazenados** — e, por extensão, a quantidade de instâncias disponíveis de um recurso.

Componentes:
- Um **valor inteiro** (`≥ 0`);
- Uma **fila de processos bloqueados** associada.

**A ideia-chave:** o semáforo **não perde sinais**. Diferentemente do `wakeup`, um sinal
enviado quando ninguém está esperando **fica armazenado no contador** e será consumido pelo
próximo processo que pedir. Isso resolve, por construção, o problema do despertar perdido.

**Interpretação do valor:**
- `s = 0`: não há sinal/recurso disponível → quem pedir bloqueia;
- `s = k > 0`: há **k** sinais/recursos disponíveis → os próximos k pedidos passam sem
  bloquear;
- Se houver processos na fila, o valor é 0 e a fila indica quantos esperam.

**Dois usos distintos (importante para a questão 3.d):**
- **Semáforo binário / mutex** (valor inicial 1): garante **exclusão mútua**;
- **Semáforo contador** (valor inicial N ou 0): implementa **sincronização** — impõe uma
  ordem entre eventos de processos diferentes.

### Operações (questão 3.b)

| Operação | Nomes | O que o **processo** faz | O que o **SO** faz |
|---|---|---|---|
| **DOWN** | `down()`, `P()`, `wait()` | Solicita/consome uma unidade do recurso. Pode **bloquear**. | Verifica se `s > 0`. **Se sim:** decrementa `s` e o processo **continua**. **Se não (`s == 0`):** muda o estado do processo para **bloqueado**, insere-o na **fila do semáforo**, remove-o da fila de prontos e **chama o escalonador**. |
| **UP** | `up()`, `V()`, `signal()` | Libera/produz uma unidade do recurso. **Nunca bloqueia.** | Verifica a fila do semáforo. **Se estiver vazia:** **incrementa `s`** (o sinal fica armazenado para o futuro). **Se houver processos esperando:** escolhe **um** deles, muda seu estado para **pronto**, move-o para a fila de prontos, e **`s` permanece 0** (o sinal foi consumido diretamente por quem esperava). Quem chamou `up` **continua executando**. |
| *(Inicialização)* | `init(s, valor)` | Define o valor inicial. | Cria a estrutura e a fila; feito **antes** de qualquer uso concorrente. |

**Detalhes que valem ponto:**
- `up` **nunca bloqueia** o processo que a executa — assimetria essencial em relação a
  `down`.
- Após um `up` que desbloqueia alguém, **o escalonador pode decidir continuar com quem
  chamou `up`** ou passar ao desbloqueado; isso depende da política e não afeta a corretude.
- Qual processo da fila é escolhido depende da implementação (FIFO evita inanição; escolha
  arbitrária não).

### Por que as operações precisam ser atômicas (questão 3.c)

Porque **`down` e `up` manipulam uma variável compartilhada e tomam decisões baseadas nela**
— exatamente o padrão que gera condições de corrida. Especificamente:

1. **`down` é internamente um "testar-e-alterar".** Ele testa se `s > 0` e, em caso
   afirmativo, decrementa. Se um processo for preemptado **entre o teste e o decremento**,
   dois processos podem ver `s == 1`, ambos decrementarem e **ambos entrarem na região
   crítica** — a exclusão mútua é destruída. É exatamente o defeito da variável de trava
   (Seção 4.4), agora dentro do semáforo.

2. **O par (testar condição, bloquear) precisa ser indivisível.** Se `down` testar `s == 0`
   e for preemptado **antes** de efetivamente bloquear, outro processo pode executar `up`,
   ver a fila vazia e apenas incrementar `s`. Quando o primeiro voltar, ele **bloqueia
   mesmo assim** — e o sinal já foi contabilizado no lugar errado. **É literalmente o
   problema do despertar perdido de volta.** Se os semáforos não fossem atômicos, não
   resolveriam nada.

3. **O próprio valor `s` pode ser corrompido.** `s = s + 1` e `s = s - 1` não são atômicos
   em linguagem de máquina; `up` e `down` concorrentes podem perder incrementos, deixando
   o contador inconsistente com o número real de recursos.

4. **A manipulação da fila de bloqueados** (inserir, remover) é uma estrutura de dados
   compartilhada; acessos concorrentes podem corrompê-la (perder um processo na fila =
   bloqueio permanente).

**Como a atomicidade é obtida na prática:** a operação sobre semáforos é uma **chamada de
sistema** e o **SO garante a indivisibilidade** — em CPU única, desabilitando brevemente as
interrupções durante as poucas instruções da operação; em multiprocessadores, usando uma
**instrução TSL/CAS** para proteger o semáforo.

> **Ponto conceitual importante para a prova:** a espera ociosa **não foi eliminada, foi
> deslocada**. O TSL usado dentro do semáforo gira por **poucas instruções** (o tempo de
> atualizar um inteiro e uma fila), em vez de girar por todo o tempo da região crítica do
> usuário — que pode ser milissegundos. É uma troca excelente.

---

## 5.5 Produtor/Consumidor com semáforos (questão 3.d — Figura 2)

### O código

**Produtor (Figura 2a):**
```c
#define N 100                     /* número de lugares no buffer */
typedef int semaphore;
semaphore mutex = 1;              /* controla o acesso à região crítica */
semaphore empty = N;              /* conta os lugares vazios no buffer */
semaphore full  = 0;              /* conta os lugares preenchidos no buffer */

void producer(void)
{
    int item;

    while (TRUE) {
        item = produce_item();    /* gera algo para pôr no buffer */
        down(&empty);             /* decrementa o contador de lugares vazios */
        down(&mutex);             /* entra na região crítica */
        insert_item(item);        /* põe o novo item no buffer */
        up(&mutex);               /* sai da região crítica */
        up(&full);                /* incrementa o contador de lugares preenchidos */
    }
}
```

**Consumidor (Figura 2b):**
```c
void consumer(void)
{
    int item;

    while (TRUE) {
        down(&full);              /* decrementa o contador de lugares preenchidos */
        down(&mutex);             /* entra na região crítica */
        item = remove_item();     /* retira o item do buffer */
        up(&mutex);               /* sai da região crítica */
        up(&empty);               /* incrementa o contador de lugares vazios */
        consume_item(item);       /* faz algo com o item */
    }
}
```

### Significado de cada semáforo

| Semáforo | Valor inicial | Tipo | Papel |
|---|---|---|---|
| **`mutex`** | 1 | Binário | **EXCLUSÃO MÚTUA.** Garante que produtor e consumidor **nunca manipulem o buffer ao mesmo tempo**. Como só há 1 "permissão", apenas um processo por vez fica entre `down(&mutex)` e `up(&mutex)`. |
| **`empty`** | N (=100) | Contador | **SINCRONIZAÇÃO.** Conta os **lugares vazios**. Impede o produtor de inserir em buffer cheio: quando `empty` chega a 0, `down(&empty)` **bloqueia o produtor**. |
| **`full`** | 0 | Contador | **SINCRONIZAÇÃO.** Conta os **lugares ocupados**. Impede o consumidor de retirar de buffer vazio: começando em 0, `down(&full)` **bloqueia o consumidor** até que haja algo. |

**Resposta direta à questão:** `mutex` faz **exclusão mútua**; `empty` e `full` fazem
**sincronização** entre processos.

> Note a elegância: `empty + full + (itens sendo manipulados) = N` sempre. Os dois
> semáforos são **complementares** — o que um decrementa, o outro incrementa.

### Funcionamento

**Produtor:** produz o item **fora** da região crítica (para minimizá-la). Depois:
1. `down(&empty)` — "reserva um lugar vazio". Se não houver (`empty == 0`, buffer cheio),
   **bloqueia** aqui.
2. `down(&mutex)` — pede acesso exclusivo ao buffer. Se o consumidor estiver dentro,
   **bloqueia**.
3. Insere o item (**região crítica**).
4. `up(&mutex)` — libera o buffer.
5. `up(&full)` — anuncia que há um item a mais. Se o consumidor estava bloqueado em
   `down(&full)`, ele é **desbloqueado**.

**Consumidor:** simétrico —
1. `down(&full)` — "reserva um item". Se não houver, **bloqueia**.
2. `down(&mutex)` — acesso exclusivo.
3. Remove o item (**região crítica**).
4. `up(&mutex)` — libera.
5. `up(&empty)` — anuncia um lugar vazio a mais, possivelmente desbloqueando o produtor.
6. Consome o item **fora** da região crítica.

**Como o despertar perdido é evitado:** se o produtor executa `up(&full)` quando **ninguém**
está esperando, o semáforo **incrementa e armazena** o sinal. Quando o consumidor chegar e
executar `down(&full)`, encontrará `full > 0` e **passará sem bloquear**. Nenhum sinal é
perdido — é a diferença fundamental para o `sleep`/`wakeup`.

### Condições de corrida nesta implementação: existem?

**Da forma como está escrito, NÃO.** A solução é **correta**: `mutex` protege a região
crítica e os semáforos contadores impedem overflow/underflow do buffer. Se a pergunta for
"existe alguma condição de corrida?", a resposta é **não, desde que as operações sobre os
semáforos sejam atômicas**.

**MAS — e aqui está o que o professor quer ver —** a corretude é **frágil**, porque depende
inteiramente da **ordem dos `down`**. **Se a ordem for invertida, há deadlock:**

```c
/* VERSÃO ERRADA — DEADLOCK */
void producer(void) {
    while (TRUE) {
        item = produce_item();
        down(&mutex);         /* ← trocado! entra na RC primeiro */
        down(&empty);         /* ← e SÓ DEPOIS verifica se há espaço */
        insert_item(item);
        up(&mutex);
        up(&full);
    }
}
```

**Cenário de deadlock:**
1. O buffer está **cheio** (`empty == 0`).
2. O produtor executa `down(&mutex)` → **entra na região crítica** (mutex vai a 0).
3. O produtor executa `down(&empty)` → `empty == 0` → **BLOQUEIA, ainda segurando o
   mutex**.
4. O consumidor chega e executa `down(&full)` → passa (há itens).
5. O consumidor executa `down(&mutex)` → mutex é 0 (o produtor o tem) → **BLOQUEIA**.
6. **Deadlock:** o produtor espera que o consumidor libere espaço; o consumidor espera que
   o produtor libere o mutex. Nenhum dos dois progride, para sempre.

**Regra de ouro que se extrai daí:** **nunca bloqueie em um semáforo de sincronização
enquanto segura o mutex.** Os `down` de sincronização (`empty`, `full`) devem vir **antes**
do `down(&mutex)`; os `up` podem vir em qualquer ordem depois do `up(&mutex)`.

**Outros erros comuns que geram corrida/deadlock** (bons para citar):
- Esquecer o `up(&mutex)` em algum caminho de saída → todos travam permanentemente.
- Fazer `insert_item` fora da proteção do mutex → **corrida sobre o buffer**.
- Trocar `up(&full)` por `up(&empty)` no produtor → o contador diverge do conteúdo real.
- Com **múltiplos** produtores e consumidores, a solução **continua correta**, pois o mutex
  serializa todos.

---

## 5.6 Monitores

### O que são (questão 4.a)

Um **monitor** (Hoare e Brinch Hansen, 1974–75) é uma **construção de sincronização de
alto nível oferecida pela linguagem de programação**: um **módulo/tipo abstrato** que
agrupa:
- **variáveis de dados compartilhadas**,
- **procedimentos** que operam sobre elas,
- **estruturas de dados** internas,

com uma propriedade fundamental garantida pelo **compilador**:

> **Apenas UM processo pode estar ativo dentro do monitor em um dado instante.**

**Características essenciais:**
- Os **dados internos** do monitor **só podem ser acessados pelos procedimentos do
  monitor** — processos externos não conseguem tocá-los diretamente (encapsulamento).
- O programador **não escreve nenhum código de exclusão mútua**: apenas declara o monitor,
  e o compilador cuida do resto. **É por isso que monitores são muito menos propensos a
  erro que semáforos.**
- Um monitor é uma **construção de linguagem**, não uma chamada de sistema.

### Como a exclusão mútua é implementada (questão 4.b)

**Quem faz:** o **compilador**, e não o programador.

**Mecanismo:** o compilador reconhece que os procedimentos daquele bloco são especiais e
**insere automaticamente** código nas fronteiras:
- Na **entrada** de todo procedimento do monitor: um `down` em um **semáforo binário
  (mutex) associado ao monitor**;
- Na **saída** de todo procedimento: um `up` nesse mesmo semáforo.

Assim, ao chamar um procedimento do monitor:
- Se **nenhum** processo estiver ativo lá dentro, o processo entra e prossegue.
- Se **algum** processo já estiver ativo, o processo chamador é **bloqueado** e colocado na
  **fila de entrada do monitor**, até que o monitor fique livre.

**A grande vantagem:** como o código de sincronização é **gerado automaticamente**, é
**impossível esquecer** um `up`, **impossível inverter** a ordem, **impossível** proteger
apenas metade das operações. Os erros mais comuns e mais catastróficos dos semáforos
**deixam de ser possíveis por construção**.

**Nota:** a implementação subjacente ainda usa semáforos ou mutex, mas isso fica **oculto**
do programador.

### Variáveis condicionais (questão 4.c)

**O problema que resolvem:** a exclusão mútua automática impede corridas, mas **não
resolve a sincronização**. Se o produtor entra no monitor e descobre que o buffer está
cheio, ele precisa **esperar** — mas, se simplesmente esperar dentro do monitor, ele
**bloqueia o monitor inteiro**, impedindo o consumidor de entrar para esvaziar o buffer.
**Deadlock.**

**Definição:** uma **variável condicional** (`condition`) é uma **variável declarada dentro
do monitor** que representa uma **condição lógica pela qual um processo pode precisar
esperar**. A ela está associada uma **fila de processos bloqueados**.

> ⚠️ Uma variável condicional **não é um contador e não tem valor**. Ela é **apenas uma
> fila de espera**. Isso é a diferença crucial em relação aos semáforos.

**Operações sobre uma variável condicional:**

| Operação | O que faz |
|---|---|
| **`wait(c)`** | O processo que a executa: (1) é **bloqueado** e inserido na **fila da variável condicional `c`**; (2) **libera a exclusão mútua do monitor** — este é o ponto essencial —, permitindo que **outro processo entre no monitor**. Quando for despertado, ele **reentra no monitor** (readquirindo a exclusão mútua) e continua a partir da instrução seguinte ao `wait`. |
| **`signal(c)`** | O processo que a executa **desperta um** dos processos bloqueados na fila de `c` (tipicamente o primeiro; se a fila estiver **vazia**, **o sinal é perdido e nada acontece** — comportamento intencional). Como não pode haver dois processos ativos no monitor simultaneamente, é preciso uma regra: **(a) Hoare** — o sinalizador é suspenso e o despertado executa imediatamente; **(b) Brinch Hansen** — o processo que sinaliza **deve executar `signal` como sua última instrução** e sair imediatamente do monitor. A regra de Brinch Hansen é mais simples e a mais adotada. *(Nas implementações reais, como em Java, usa-se a variante "signal and continue": o sinalizador continua e o despertado espera o monitor ficar livre — o que exige testar a condição com `while` em vez de `if`.)* |
| **`broadcast(c)` / `signalAll(c)`** | *(Extensão comum)* Desperta **todos** os processos da fila de `c`. Todos reentram no monitor um de cada vez e reavaliam a condição. |
| **`queue(c)` / `empty(c)`** | *(Extensão)* Verifica se **há** processos esperando na fila de `c` (booleano). |

**Diferença capital em relação a semáforos:**

| | Semáforo | Variável condicional |
|---|---|---|
| Tem valor/contador? | **Sim** | **Não** |
| Sinal sem esperadores | **É armazenado** (incrementa) | **É PERDIDO** |
| Consequência | Ordem `up`/`down` não importa para não perder sinal | **`signal` deve ser dado dentro do monitor, com a condição já verificada** |

É exatamente porque o `signal` pode ser perdido que a variável condicional **só é segura
dentro de um monitor** — a exclusão mútua garante que a condição testada não mude entre o
teste e o `wait`. Aqui, o **teste-e-bloqueio é atômico por construção**.

---

## 5.7 Produtor/Consumidor com monitores (questão 4.d — Figura 3)

### O código (em Pidgin Pascal, como na figura)

```pascal
monitor ProducerConsumer
    condition full, empty;
    integer count;

    procedure insert(item: integer);
    begin
        if count = N then wait(full);          { buffer cheio → espera }
        insert_item(item);
        count := count + 1;
        if count = 1 then signal(empty)        { estava vazio → acorda o consumidor }
    end;

    function remove: integer;
    begin
        if count = 0 then wait(empty);         { buffer vazio → espera }
        remove = remove_item;
        count := count - 1;
        if count = N - 1 then signal(full)     { estava cheio → acorda o produtor }
    end;

    count := 0;
end monitor;

procedure producer;
begin
    while true do
    begin
        item = produce_item;
        ProducerConsumer.insert(item)
    end
end;

procedure consumer;
begin
    while true do
    begin
        item = ProducerConsumer.remove;
        consume_item(item)
    end
end;
```

### Funcionamento

- O `monitor ProducerConsumer` encapsula o buffer e o contador `count`. Nem o produtor nem
  o consumidor tocam essas variáveis diretamente — **só através de `insert` e `remove`**.
- Ao chamar `ProducerConsumer.insert(item)`, o produtor tenta entrar no monitor. O
  **compilador** inseriu o código de exclusão mútua: se o consumidor estiver dentro, o
  produtor **espera na fila de entrada do monitor**.
- Dentro de `insert`: se `count = N` (cheio), o produtor executa **`wait(full)`** — ele
  bloqueia **e libera o monitor**, permitindo que o consumidor entre e consuma. Caso
  contrário, insere o item e incrementa `count`.
- Se `count = 1` (o buffer **estava vazio** e agora tem um item), executa
  **`signal(empty)`** para acordar um consumidor possivelmente bloqueado.
- `remove` é simétrico: espera em **`wait(empty)`** se estiver vazio; sinaliza
  **`signal(full)`** quando `count = N − 1` (o buffer **estava cheio** e agora tem espaço).
- Produtor e consumidor externos são triviais: apenas laços chamando os procedimentos.
  Toda a complexidade da sincronização está **dentro** do monitor.

### Significado das variáveis condicionais

| Variável condicional | Quem espera nela | Espera por quê | Quem sinaliza |
|---|---|---|---|
| **`full`** | O **produtor** | Espera que **deixe de estar cheio** (que apareça espaço livre) | O **consumidor**, quando `count = N − 1` |
| **`empty`** | O **consumidor** | Espera que **deixe de estar vazio** (que apareça um item) | O **produtor**, quando `count = 1` |

> ⚠️ **Cuidado com os nomes — eles enganam.** `full` é a condição *"buffer cheio"* (quem
> espera nela é quem foi barrado pelo buffer cheio, ou seja, o produtor). `empty` é a
> condição *"buffer vazio"*. É **oposto** da convenção usada nos semáforos da Figura 2, onde
> `full` contava itens e era o consumidor que fazia `down(&full)`. Muita gente erra isso.

**Como fazem a sincronização:** elas implementam a espera **condicional** — o processo só
bloqueia **se** a condição que impede seu progresso for verdadeira, e é acordado
**exatamente** quando o outro processo torna a condição falsa. Assim, produtor e consumidor
se **coordenam mutuamente** sem espera ociosa e sem se atropelarem no buffer.

### Existem condições de corrida?

**NÃO — e essa é a principal virtude dos monitores.** As razões:

1. **A exclusão mútua é automática e garantida pelo compilador.** É impossível que dois
   processos manipulem `count` ou o buffer simultaneamente. As corridas sobre `count` que
   apareciam no `sleep`/`wakeup` são **eliminadas por construção**.
2. **O despertar perdido é impossível.** No `sleep`/`wakeup`, o processo podia ser
   preemptado **entre** testar `count == 0` e executar `sleep()`. Aqui, o teste
   (`if count = 0`) e o `wait(empty)` acontecem **dentro do monitor**, com a exclusão mútua
   ativa. **Nenhum outro processo pode entrar no monitor e alterar `count` nesse intervalo.**
   O teste-e-bloqueio é efetivamente **atômico**.
3. **`wait` libera o monitor**, então nunca há deadlock por "esperar segurando a trava" —
   diferentemente do erro de ordem dos `down` nos semáforos.

**Ressalvas honestas (o que dizer se o professor insistir em "caso exista"):**

- **Dependência da disciplina de `signal`.** O código usa `if` em vez de `while` na
  verificação da condição. Isso só é seguro sob a semântica **Hoare/Brinch Hansen**, em que
  o processo despertado **retoma imediatamente** e a condição ainda vale. Com a semântica
  **"signal and continue"** (usada em Java, Pthreads e na maioria dos sistemas reais), entre
  o `signal` e a efetiva retomada do processo despertado **um terceiro processo pode entrar
  no monitor e mudar `count`** — e o despertado prosseguiria com uma premissa falsa. **Com
  múltiplos produtores/consumidores isso é uma condição de corrida real.** A correção é
  trocar `if` por **`while`**:
  ```pascal
  while count = N do wait(full);
  ```
- **Só funciona em máquinas de memória compartilhada.** Monitores não servem para sistemas
  distribuídos.
- **Depende de suporte da linguagem.** Em uma linguagem sem monitores, não há como o
  compilador gerar a exclusão mútua — e a garantia desaparece.
- Um erro do programador dentro de um procedimento do monitor (por exemplo, chamar
  `wait` na variável condicional errada) ainda causa deadlock — os monitores eliminam os
  erros **de sincronização estrutural**, não os erros **de lógica**.

---

## 5.8 Semáforos × Monitores (questão 5, primeira parte)

| Aspecto | **Semáforos** | **Monitores** |
|---|---|---|
| **Facilidade de implementação** | **Fáceis de implementar no SO** — são apenas um inteiro, uma fila e duas chamadas de sistema atômicas. Não exigem nada da linguagem. Mas **difíceis de usar corretamente**: cabe ao programador colocar cada `down`/`up` na posição certa. | **Difíceis de implementar** — exigem apoio do **compilador** e da linguagem, que precisa reconhecer a construção e gerar o código de exclusão mútua. Mas **muito fáceis de usar**: basta agrupar os dados e procedimentos; a sincronização é automática. |
| **Implementação livre de erros** | **Altamente propensos a erro.** São primitivas de baixo nível: (a) inverter a ordem dos `down` causa **deadlock**; (b) esquecer um `up` trava o sistema permanentemente; (c) usar o semáforo errado corrompe os dados; (d) um `down` a mais ou a menos desequilibra tudo. E esses erros são **intermitentes e quase impossíveis de reproduzir**, pois dependem de temporizações raras. | **Muito menos propensos a erro.** O compilador gera a exclusão mútua, tornando **impossível** esquecer, inverter ou omitir a proteção. Os dados ficam **encapsulados**, então nenhum código externo pode acessá-los sem passar pela sincronização. Restam apenas erros de lógica (uso da variável condicional errada, `if` em vez de `while`). |
| **Suporte pelas linguagens de programação e compiladores** | **Suporte praticamente universal.** Não exigem nada especial da linguagem — são chamadas de biblioteca/sistema. Disponíveis em C, C++, POSIX (`sem_wait`/`sem_post`), Windows, praticamente qualquer ambiente. | **Suporte limitado.** Poucas linguagens implementam monitores nativamente. Exemplos: **Java** (`synchronized` + `wait`/`notify`/`notifyAll`), **Concurrent Euclid**, **Ada** (com *rendezvous*), **C#** (`lock` + `Monitor`). **Não existem em C nem C++**, as linguagens dominantes em sistemas — o que limita muito seu alcance prático. |
| **Suporte a processos em máquinas distintas** | **Não suportam.** Um semáforo é uma variável na **memória compartilhada** do sistema; `up` e `down` precisam acessar essa memória. Em máquinas distintas, **não há memória comum** — só rede. | **Não suportam, pelo mesmo motivo.** As variáveis do monitor residem em memória compartilhada, e a exclusão mútua depende de um mutex local. Um monitor é ainda mais "amarrado" à máquina que um semáforo, pois é uma construção de linguagem em um único espaço de endereçamento. |
| **Nível de abstração** | Baixo nível (primitiva) | Alto nível (construção estruturada) |

**Conclusão para a prova:** *ambos* — semáforos e monitores — **falham no último critério**:
são inadequados para **sistemas distribuídos**, porque dependem de **memória
compartilhada**. É exatamente essa limitação que motiva o próximo mecanismo: **troca de
mensagens**.

---

## 5.9 Troca de mensagens (questão 5, itens sobre mensagens)

### As operações

| Operação | Descrição | O que o SO faz |
|---|---|---|
| **`send(destino, &mensagem)`** | O processo **envia** uma mensagem a um destinatário (processo, caixa postal ou porta). | Copia a mensagem do espaço do remetente para o buffer do núcleo/rede e a entrega ou enfileira. Pode **bloquear** o remetente se o buffer/caixa estiver **cheio**, ou se a semântica for síncrona (*rendezvous*), até que o destinatário receba. |
| **`receive(origem, &mensagem)`** | O processo **recebe** uma mensagem de uma origem (ou de qualquer origem). | Se houver mensagem disponível, copia-a para o espaço do receptor e retorna. Se **não houver**, tipicamente **bloqueia o processo** até que uma chegue (há também variantes não bloqueantes que retornam erro/vazio). |

**Variantes importantes:**
- **Bloqueante × não bloqueante** (síncrona × assíncrona);
- **Endereçamento direto** (nomear o processo) × **indireto** (caixa postal / porta);
- **Confirmação (`acknowledgement`)**: em redes não confiáveis, o receptor devolve um ACK e
  o remetente **retransmite** se ele não chegar em tempo hábil.

**Problemas específicos da troca de mensagens (bons pontos extras):**
- **Perda de mensagens** na rede → exige ACK e retransmissão.
- **Mensagens duplicadas** (quando o ACK se perde e há retransmissão) → exige **números de
  sequência** consecutivos para o receptor distinguir original de duplicata.
- **Nomeação de processos** de forma inequívoca entre máquinas (ex.:
  `processo@maquina.dominio`).
- **Autenticação**: como saber que se está falando com o servidor real e não com um
  impostor?
- **Desempenho**: copiar mensagens é mais lento que ler uma variável compartilhada, mesmo
  na mesma máquina.

### Produtor/Consumidor com troca de mensagens (Figura 4)

**Produtor (Figura 4a):**
```c
#define N 100                       /* número de lugares no buffer */

void producer(void)
{
    int item;
    message m;                      /* buffer de mensagem */

    while (TRUE) {
        item = produce_item();      /* gera algo para pôr no buffer */
        receive(consumer, &m);      /* espera que uma mensagem vazia chegue */
        build_message(&m, item);    /* constrói uma mensagem para enviar */
        send(consumer, &m);         /* envia o item ao consumidor */
    }
}
```

**Consumidor (Figura 4b):**
```c
void consumer(void)
{
    int item, i;
    message m;

    for (i = 0; i < N; i++) send(producer, &m);   /* envia N mensagens vazias */
    while (TRUE) {
        receive(producer, &m);      /* recebe uma mensagem contendo um item */
        item = extract_item(&m);    /* extrai o item da mensagem */
        send(producer, &m);         /* devolve uma resposta vazia */
        consume_item(item);         /* faz algo com o item */
    }
}
```

### Como a sincronização é feita — o mecanismo dos "N tokens"

Este é o ponto central da resposta:

1. **O consumidor "prepara o terreno"**: antes de qualquer coisa, envia **N mensagens
   vazias** ao produtor. Essas mensagens vazias funcionam como **"vales" / tokens /
   permissões de escrita**.
2. **O produtor só pode produzir se tiver um vale.** Cada iteração começa com
   `receive(consumer, &m)`: ele **bloqueia** até receber uma mensagem vazia. Ele então a
   preenche com o item e a devolve.
3. **O consumidor só consome se receber uma mensagem cheia.** Seu `receive(producer, &m)`
   **bloqueia** se não houver nenhuma. Ao consumir, ele **devolve a mensagem vazia** ao
   produtor, repondo o vale.

**O invariante que sustenta tudo:**
> O número **total** de mensagens em circulação no sistema é **constante e igual a N** —
> exatamente o tamanho do buffer. Elas apenas alternam entre "vazias" (em poder do
> produtor) e "cheias" (a caminho do consumidor).

**Consequências diretas:**
- **Buffer cheio:** se o consumidor for lento, todas as N mensagens estarão cheias,
  esperando por ele. O produtor não terá nenhuma mensagem vazia para receber → **bloqueia
  em `receive`**. Ele **não pode** produzir mais que N itens à frente. *(Equivalente ao
  `down(&empty)` dos semáforos.)*
- **Buffer vazio:** se o produtor for lento, o consumidor não terá mensagens cheias →
  **bloqueia em `receive`**. *(Equivalente ao `down(&full)`.)*
- **Exclusão mútua:** **não é necessária!** Não há **nenhuma variável compartilhada** — nem
  `count`, nem buffer comum, nem semáforo. Cada mensagem pertence a exatamente **um**
  processo por vez. **É por isso que este mecanismo funciona em máquinas distintas.**

**Papel do `N`:** define a "profundidade" do buffer, controlando o **acoplamento** entre
produtor e consumidor. Com `N = 1` o esquema vira um *rendezvous* estrito (produtor e
consumidor caminham em lockstep); com N grande, um pode adiantar-se bastante em relação
ao outro.

### Existem condições de corrida?

**NÃO, no sentido clássico** — e essa é a virtude do mecanismo: **não existe memória
compartilhada**, logo não existe região crítica sobre dados comuns, logo não existe
condição de corrida. `send` e `receive` são chamadas de sistema **atômicas** garantidas
pelo núcleo. Com **um** produtor e **um** consumidor, a solução está correta.

**Problemas que podem ocorrer (mencione-os para dar completude à resposta):**

1. **Perda de mensagens.** Se uma mensagem se perder na rede (ou o núcleo a descartar por
   falta de buffer), um **vale desaparece permanentemente**. O sistema continua funcionando,
   mas com capacidade reduzida (N−1). Perdendo todas, chega-se ao **deadlock**: ambos
   bloqueados em `receive`. Solução: ACK + timeout + retransmissão.
2. **Mensagens duplicadas.** Uma retransmissão indevida pode **criar um vale extra**,
   permitindo que o produtor exceda a capacidade do buffer. Solução: **números de
   sequência**.
3. **Perda de desempenho por cópia**: cada item é copiado do espaço do produtor para o
   núcleo e daí para o consumidor.
4. **Com múltiplos produtores/consumidores e endereçamento direto**, a solução **não
   funciona** como está: o `send(producer, &m)` nomeia um produtor específico. Seria
   preciso usar **caixas postais** (endereçamento indireto), e nesse caso o próprio núcleo
   serializa o acesso à caixa — sem corrida.
5. **Bug de inicialização:** se o consumidor enviasse **menos** de N mensagens, a
   capacidade efetiva seria menor; se enviasse mais, o buffer poderia estourar.

---

## 5.10 Caixa Postal × Rendezvous (questão 6)

### Caixa postal (*mailbox*)

**O que é:** uma estrutura de dados **intermediária**, mantida pelo núcleo, com **espaço
para armazenar um certo número de mensagens** (buffer). É uma forma de **endereçamento
indireto**: o `send` e o `receive` especificam a **caixa postal**, não o processo.

**Comportamento:**
- `send` deposita a mensagem na caixa. Se a caixa estiver **cheia**, o remetente é
  **suspenso** até liberar espaço.
- `receive` retira uma mensagem da caixa. Se estiver **vazia**, o receptor é **bloqueado**
  até que uma chegue.
- Remetente e destinatário **não precisam estar ativos ao mesmo tempo**. A comunicação é
  **assíncrona / desacoplada**.

> No exemplo do produtor/consumidor, cada um teria uma caixa: o produtor lê da sua caixa as
> mensagens vazias, e o consumidor lê da sua as cheias.

### Rendezvous (encontro)

**O que é:** a abordagem **sem armazenamento intermediário**. Se `send` for executado antes
de `receive`, o **remetente é bloqueado** até que o destinatário execute `receive`. Nesse
instante, a mensagem é copiada **diretamente** do remetente para o receptor.

**Comportamento:**
- Comunicação **síncrona**: a transferência só ocorre quando **ambos** estão prontos —
  eles se "encontram".
- Quem chegar primeiro **espera** pelo outro.
- Não há buffer, não há cópia intermediária no núcleo.

### Comparação

| Aspecto | **Caixa Postal** | **Rendezvous** |
|---|---|---|
| **Facilidade de implementação** | **Mais difícil.** Exige que o núcleo mantenha uma estrutura de dados persistente: alocar e gerenciar o buffer de mensagens, controlar quantas mensagens cabem, gerenciar as filas de remetentes e destinatários bloqueados, tratar caixa cheia/vazia, decidir a política de descarte, gerenciar criação/destruição de caixas e permissões. Consome **memória do núcleo** proporcional ao número de caixas e mensagens. | **Muito mais simples.** **Não há buffer para gerenciar** — nada é armazenado. O núcleo precisa apenas manter o processo que chegou primeiro bloqueado e, no encontro, **copiar a mensagem diretamente** de um espaço de endereçamento para o outro. Menos estruturas, menos memória, menos código, menos casos de erro. |
| **Flexibilidade em relação à execução dos processos comunicantes** | **Muito mais flexível.** Os processos são **desacoplados no tempo**: o remetente pode enviar e continuar trabalhando sem esperar que o destinatário esteja pronto — ou mesmo que ele exista naquele momento. Permite **rajadas** (o produtor adianta-se ao consumidor até encher a caixa), **absorve diferenças de velocidade** entre os processos e permite **vários remetentes e vários destinatários** compartilhando a mesma caixa. Reduz o número de bloqueios e melhora a vazão. | **Muito menos flexível.** Os processos ficam **fortemente acoplados**: caminham em *lockstep*, no ritmo do **mais lento**. Qualquer diferença de velocidade se traduz em **tempo de bloqueio** do mais rápido, desperdiçando paralelismo. Não há como o produtor adiantar trabalho. Em compensação, oferece **sincronização implícita e forte**: ao retornar do `send`, o remetente **sabe** que a mensagem foi recebida — não precisa de confirmação. |

**Resumo:** a caixa postal troca **simplicidade** por **flexibilidade**; o rendezvous troca
**flexibilidade** por **simplicidade e garantia de entrega**. O rendezvous é usado, por
exemplo, na linguagem **Ada** e no **MINIX**.

---

## 5.11 Barreiras (questão 7)

### O que são

Uma **barreira** é um mecanismo de sincronização destinado a **grupos de processos** (em
vez de pares). Ela funciona como um **ponto de encontro obrigatório**:

- A barreira é colocada **ao fim de uma fase** de computação.
- **Todo processo que chega à barreira é bloqueado** até que **TODOS** os processos do
  grupo tenham chegado.
- Quando o **último** processo chega, **todos são liberados simultaneamente** e podem
  iniciar a fase seguinte.

**Ilustração:**
```
Fase 1:   A ──────────►│           A ─────────►
          B ────────────────►│  →   B ─────────►
          C ───────►│         │     C ─────────►
          D ──────────────►│  │     D ─────────►
                    BARREIRA        Fase 2
        (A, C e B esperam D chegar)  (todos liberados juntos)
```

### Quando são utilizadas

Barreiras são usadas em **aplicações que progridem por fases**, nas quais **nenhum processo
pode iniciar a fase *n+1* antes que todos tenham concluído a fase *n***, porque a próxima
fase **depende dos resultados produzidos por todos** na fase anterior.

**Situações típicas:**

1. **Computação científica e cálculo numérico paralelo** — o exemplo canônico de
   Tanenbaum. Em métodos de **relaxamento** para resolver equações diferenciais parciais, a
   matriz é dividida entre os processos; cada um calcula sua parte de uma **iteração**.
   Antes de começar a iteração seguinte, cada processo precisa dos **valores de fronteira
   calculados pelos vizinhos** — portanto, **todos** precisam terminar a iteração atual.
2. **Simulações passo a passo** — previsão do tempo, dinâmica molecular, simulação de
   fluidos, modelos climáticos: o estado do instante *t+1* depende do estado completo em
   *t*.
3. **Processamento de imagens/vídeo em múltiplos passos** — cada filtro precisa da imagem
   inteira resultante do filtro anterior.
4. **Algoritmos paralelos por fases** — ordenação paralela, multiplicação de matrizes em
   blocos, algoritmos de grafos por níveis (BFS paralelo), MapReduce (todos os *mappers*
   antes de qualquer *reducer*).
5. **Inicialização coordenada** — garantir que todas as threads tenham concluído sua
   configuração antes que qualquer uma comece o trabalho real.
6. **Treinamento distribuído de modelos** — sincronizar gradientes ao fim de cada lote.

**Diferença essencial em relação aos outros mecanismos:** semáforos, monitores e mensagens
sincronizam **pares** de processos ou o acesso a um **recurso**; a barreira sincroniza o
**progresso temporal de um grupo inteiro**, forçando-o a avançar em conjunto.

**Custo:** todos ficam limitados pelo processo **mais lento** de cada fase — se a carga for
mal balanceada, muitos processos ficam ociosos esperando um retardatário.

---

## 5.12 Quadro-resumo dos mecanismos com bloqueio

| Mecanismo | Nível | Exclusão mútua | Sincronização | Máquinas distintas? | Propenso a erro? |
|---|---|---|---|---|---|
| **`sleep`/`wakeup`** | Chamada de sistema | Não fornece | Sim (mas **falha**: sinal perdido) | Não | Sim — **incorreto** |
| **Semáforos** | Chamada de sistema | Sim (binário/mutex) | Sim (contador) | Não | **Muito** |
| **Mutexes** | Biblioteca/SO | Sim | Não (só exclusão mútua) | Não | Médio |
| **Monitores** | Construção de linguagem | Sim (automática) | Sim (variáveis condicionais) | Não | Pouco |
| **Troca de mensagens** | Chamada de sistema | Não precisa (sem memória comum) | Sim (bloqueio em `send`/`receive`) | **SIM** | Médio (perda/duplicação) |
| **Barreiras** | Biblioteca | — | Sim (para **grupos**) | Sim (com implementação adequada) | Pouco |

---

## Checklist de revisão da Lista 05

- [ ] Sei citar **pelo menos 5** problemas da espera ociosa, com destaque para a inversão de prioridades.
- [ ] Sei descrever exatamente o que `sleep` e `wakeup` fazem (estados e filas).
- [ ] Sei narrar o **despertar perdido** passo a passo no código da Figura 1.
- [ ] Sei também apontar a corrida sobre `count` na Figura 1.
- [ ] Sei definir semáforo e explicar que ele **armazena** sinais (diferença para `wakeup`).
- [ ] Sei descrever `down` e `up` separando o que faz o **processo** e o que faz o **SO**.
- [ ] Sei dar **4 argumentos** para a atomicidade das operações sobre semáforos.
- [ ] Sei dizer que `mutex` = exclusão mútua e `empty`/`full` = sincronização (Figura 2).
- [ ] Sei explicar o **deadlock por inversão da ordem dos `down`**.
- [ ] Sei definir monitor e explicar que a exclusão mútua é gerada pelo **compilador**.
- [ ] Sei definir variável condicional e listar `wait`, `signal` (e `broadcast`/`queue`).
- [ ] Sei explicar que `wait` **libera o monitor** e que `signal` sem esperadores **se perde**.
- [ ] Sei o papel de `full` e `empty` na Figura 3 (atenção: nomes invertidos vs. semáforos!).
- [ ] Sei explicar por que monitores impedem o despertar perdido, e a ressalva do `if` × `while`.
- [ ] Sei preencher a tabela semáforos × monitores nos 4 aspectos pedidos.
- [ ] Sei descrever `send`/`receive` e o mecanismo dos **N tokens** da Figura 4.
- [ ] Sei que troca de mensagens dispensa exclusão mútua e por quê.
- [ ] Sei comparar caixa postal × rendezvous em facilidade e flexibilidade.
- [ ] Sei definir barreira e dar 3 exemplos de uso.
