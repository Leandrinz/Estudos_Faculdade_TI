# Seção 6 — Problemas Clássicos de Comunicação Entre Processos

> Base para a **Lista 06 — Problemas Clássicos de Comunicação Entre Processos**
> Referência: Tanenbaum, *Sistemas Operacionais Modernos*, cap. 2.5

---

## 6.1 O problema do Jantar dos Filósofos

### Descrição do problema (questão 1.a)

Formulado e resolvido por **Dijkstra em 1965**, é o problema clássico para modelar
situações em que processos disputam **acesso exclusivo a um número limitado de recursos**.

**O enunciado:**

- **Cinco filósofos** estão sentados ao redor de uma mesa circular.
- Cada filósofo tem à sua frente um **prato de espaguete**, tão escorregadio que é
  necessário **dois garfos** para comê-lo.
- Entre cada par de pratos há **um garfo** — portanto, há **exatamente cinco garfos** para
  cinco filósofos.
- Cada filósofo alterna entre duas atividades apenas: **pensar** e **comer**.
- Quando um filósofo fica com fome, ele tenta pegar o **garfo à sua esquerda** e o **garfo à
  sua direita**, **um de cada vez, em qualquer ordem**. Só come se conseguir **os dois**.
- Terminada a refeição, ele **devolve os dois garfos** e volta a pensar.

```
              F0
         g4        g0
      F4              F1
        g3        g1
           F3   g2   F2
```

**A pergunta central:** *É possível escrever um programa para cada filósofo que faça o que
se espera dele e nunca trave?*

**O mapeamento para sistemas operacionais:**

| Jantar dos filósofos | Sistema operacional |
|---|---|
| Filósofo | **Processo / thread** |
| Garfo | **Recurso compartilhado** (arquivo, dispositivo, registro de BD, trava) |
| Pensar | Executar código **não crítico** |
| Comer | Executar a **região crítica** |
| Pegar os dois garfos | Adquirir **múltiplos recursos** simultaneamente |
| Devolver os garfos | Liberar os recursos |

O problema modela, portanto, **qualquer situação em que um processo precise de mais de um
recurso ao mesmo tempo**.

### A solução ingênua e o deadlock (a situação que a questão pede)

**Tentativa óbvia:**
```c
void philosopher(int i)
{
    while (TRUE) {
        think();                   /* o filósofo está pensando */
        take_fork(i);              /* pega o garfo da ESQUERDA */
        take_fork((i+1) % N);      /* pega o garfo da DIREITA */
        eat();                     /* hummm, espaguete */
        put_fork(i);               /* devolve o garfo da esquerda */
        put_fork((i+1) % N);       /* devolve o garfo da direita */
    }
}
```
onde `take_fork` espera até o garfo estar disponível e o pega.

**🔴 SITUAÇÃO DE DEADLOCK:**

Suponha que **os cinco filósofos fiquem com fome ao mesmo tempo** e que todos executem
`take_fork(i)` **simultaneamente** (ou intercalados de modo que todos completem o primeiro
passo antes de qualquer um chegar ao segundo):

1. **Todos os cinco** pegam com sucesso o **garfo à sua esquerda** — há exatamente 5 garfos
   e 5 filósofos, então **todos conseguem**.
2. **Nenhum garfo sobra na mesa.**
3. **Todos os cinco** tentam então pegar o **garfo à sua direita** — mas o garfo à direita
   de cada um está na mão do vizinho.
4. **Todos os cinco bloqueiam, indefinidamente**, cada um segurando um garfo e esperando
   por outro que nunca será liberado.
5. **Nenhum filósofo jamais come. DEADLOCK.**

**Por que é um deadlock:** as quatro condições de Coffman estão presentes —
(1) **exclusão mútua** (um garfo é usado por um filósofo por vez);
(2) **posse e espera** (cada um segura um garfo enquanto espera outro);
(3) **não preempção** (não se pode tomar um garfo à força);
(4) **espera circular** (F0 espera F1, que espera F2, ..., que espera F0).

**🔴 SEGUNDA SITUAÇÃO — a "correção" que gera *starvation* / livelock:**

Uma tentativa de conserto: *"depois de pegar o garfo esquerdo, verificar se o direito está
disponível; se não estiver, devolver o esquerdo, esperar um tempo e tentar de novo"*.

Isso **não resolve**: se todos os filósofos começarem **exatamente ao mesmo tempo**, todos
pegam o garfo esquerdo, todos veem o direito ocupado, todos devolvem o esquerdo, todos
esperam o mesmo intervalo, todos tentam de novo... e o ciclo se repete **indefinidamente**.

- Todos os processos estão **executando** (não estão bloqueados) — por isso é chamado de
  **livelock**, e não de deadlock;
- Mas **nenhum progride** — é **inanição (starvation)**.

*(Usar um intervalo de espera **aleatório** torna a repetição improvável e funciona na
prática — é o que o Ethernet faz com o recuo exponencial —, mas **não é uma garantia**, e
em sistemas críticos como controle de usinas nucleares não seria aceitável.)*

**🔴 TERCEIRA SITUAÇÃO — o mutex único (correto, mas ruim):**

Proteger todo o ato de pegar os garfos com **um único semáforo binário**:
```c
down(&mutex);
take_fork(i); take_fork((i+1) % N);
eat();
put_fork(i); put_fork((i+1) % N);
up(&mutex);
```
Isso **elimina o deadlock**, mas **serializa completamente** o jantar: apenas **um**
filósofo come por vez, quando na verdade **dois poderiam comer simultaneamente** (por
exemplo, F0 e F2). O **paralelismo é destruído** — a solução é correta mas ineficiente.

**Soluções conhecidas (bom para citar):**
- **Assimetria:** filósofos de índice **par** pegam primeiro o garfo esquerdo; os de índice
  **ímpar**, o direito. Isso quebra a espera circular.
- **Limitar a 4 filósofos** simultaneamente com fome (com um semáforo contador inicializado
  em 4) — garante que ao menos um consiga os dois garfos.
- **Solução de Dijkstra com estados e semáforos privados** — a da Figura 2, analisada
  abaixo. É a que permite **máximo paralelismo**.

---

## 6.2 Solução do Jantar dos Filósofos com semáforos (questão 1.b — Figura 1)

### O código

```c
#define N            5              /* número de filósofos */
#define LEFT   (i+N-1)%N            /* número do vizinho à esquerda de i */
#define RIGHT  (i+1)%N              /* número do vizinho à direita de i */
#define THINKING     0              /* o filósofo está pensando */
#define HUNGRY       1              /* o filósofo está tentando pegar garfos */
#define EATING       2              /* o filósofo está comendo */

typedef int semaphore;
int state[N];                       /* vetor para controlar o estado de cada um */
semaphore mutex = 1;                /* exclusão mútua para as regiões críticas */
semaphore s[N];                     /* um semáforo por filósofo, iniciado em 0 */

void philosopher(int i)             /* i: número do filósofo, de 0 a N-1 */
{
    while (TRUE) {
        think();                    /* o filósofo está pensando */
        take_forks(i);              /* pega dois garfos ou bloqueia */
        eat();                      /* hummm, espaguete */
        put_forks(i);               /* devolve os dois garfos à mesa */
    }
}

void take_forks(int i)
{
    down(&mutex);                   /* entra na região crítica */
    state[i] = HUNGRY;              /* registra que o filósofo i está com fome */
    test(i);                        /* tenta pegar os dois garfos */
    up(&mutex);                     /* sai da região crítica */
    down(&s[i]);                    /* bloqueia se os garfos não foram pegos */
}

void put_forks(i)
{
    down(&mutex);                   /* entra na região crítica */
    state[i] = THINKING;            /* o filósofo acabou de comer */
    test(LEFT);                     /* vê se o vizinho da esquerda pode comer agora */
    test(RIGHT);                    /* vê se o vizinho da direita pode comer agora */
    up(&mutex);                     /* sai da região crítica */
}

void test(i)                        /* i: o número do filósofo, de 0 a N-1 */
{
    if (state[i] == HUNGRY && state[LEFT] != EATING && state[RIGHT] != EATING) {
        state[i] = EATING;
        up(&s[i]);
    }
}
```

### A ideia central da solução

O truque é **não pegar os garfos um a um**. Em vez disso, o filósofo:

1. Anuncia que está **com fome**;
2. **Testa** se pode comer — a condição é: *"estou com fome E nenhum dos meus dois vizinhos
   está comendo"*. Se ambos os vizinhos não estão comendo, **os dois garfos estão livres**
   (cada garfo é compartilhado com exatamente um vizinho);
3. Se puder, muda seu estado para **EATING** e **libera seu próprio semáforo**;
4. Se não puder, **bloqueia** no seu semáforo privado, esperando ser liberado por um vizinho.

**A aquisição dos dois garfos é feita de forma atômica**, protegida pelo `mutex` — ou o
filósofo pega **os dois**, ou **nenhum**. Isso quebra a condição de **posse-e-espera** de
Coffman e **elimina o deadlock por construção**.

### Papel de cada semáforo (o que a questão pede explicitamente)

| Semáforo | Valor inicial | Papel |
|---|---|---|
| **`mutex`** | **1** | **EXCLUSÃO MÚTUA.** Protege o vetor compartilhado `state[]`. Garante que os testes e as alterações de estado (em `take_forks`, `put_forks` e `test`) sejam **atômicos**. Sem ele, dois vizinhos poderiam testar simultaneamente, ambos concluir que podem comer, e **ambos pegar o mesmo garfo** — condição de corrida. É a região crítica da solução. |
| **`s[i]`** (vetor de N semáforos) | **0** (todos) | **SINCRONIZAÇÃO INDIVIDUAL — um "semáforo privado" por filósofo.** É o mecanismo de **bloqueio e despertar** de cada filósofo. Como começa em 0, o `down(&s[i])` **bloqueia** o filósofo *i* a menos que alguém já tenha executado `up(&s[i])`. Cada filósofo só bloqueia no **seu próprio** semáforo, e só é liberado quando **ele mesmo** (em `test(i)` dentro de `take_forks`) ou um **vizinho** (em `put_forks`) verifica que ele pode comer. |

> Note que **não há semáforo para os garfos**. Os garfos são representados **implicitamente**
> pelo vetor `state[]`: se nenhum vizinho está `EATING`, os dois garfos estão livres.

### Funcionamento detalhado

**`take_forks(i)` — o filósofo quer comer:**
1. `down(&mutex)` — entra na região crítica, obtendo acesso exclusivo a `state[]`.
2. `state[i] = HUNGRY` — registra publicamente que está com fome. **Isso é essencial:** é
   por esse registro que os vizinhos saberão, mais tarde, que devem testá-lo.
3. `test(i)` — tenta pegar os garfos. Se os dois vizinhos não estiverem comendo,
   `test` muda `state[i]` para `EATING` e executa `up(&s[i])`, deixando `s[i]` em 1.
4. `up(&mutex)` — sai da região crítica (importante: **libera o mutex ANTES de bloquear**,
   senão ninguém mais poderia entrar e o sistema travaria — é a mesma regra de ouro dos
   semáforos da Seção 5).
5. `down(&s[i])`:
   - Se `test(i)` **teve sucesso**, `s[i]` vale 1 → o `down` **passa direto** e o filósofo
     vai comer.
   - Se `test(i)` **falhou**, `s[i]` vale 0 → o filósofo **bloqueia aqui**, com
     `state[i] == HUNGRY`, aguardando que um vizinho o libere.

**`eat()`** — o filósofo come. Enquanto isso, `state[i] == EATING`, o que impede os dois
vizinhos de comerem.

**`put_forks(i)` — o filósofo terminou:**
1. `down(&mutex)` — acesso exclusivo a `state[]`.
2. `state[i] = THINKING` — anuncia que largou os garfos.
3. `test(LEFT)` — **verifica se o vizinho da esquerda agora pode comer**. Se ele estava
   `HUNGRY` e agora seus dois vizinhos estão livres, `test` o marca como `EATING` e executa
   `up(&s[LEFT])`, **desbloqueando-o**.
4. `test(RIGHT)` — o mesmo para o vizinho da direita.
5. `up(&mutex)` — sai da região crítica.

> **Ponto elegante:** o filósofo que termina de comer é quem tem a **responsabilidade de
> acordar os vizinhos**. É a mesma filosofia do produtor/consumidor: quem torna a condição
> verdadeira é quem sinaliza.

**`test(i)` — o coração da solução:**
```c
if (state[i] == HUNGRY && state[LEFT] != EATING && state[RIGHT] != EATING) {
    state[i] = EATING;
    up(&s[i]);
}
```
Três condições devem valer simultaneamente:
- `state[i] == HUNGRY` — o filósofo *i* **quer** comer (não adianta liberar quem está
  pensando);
- `state[LEFT] != EATING` — o vizinho da esquerda não está comendo → **o garfo esquerdo
  está livre**;
- `state[RIGHT] != EATING` — o vizinho da direita não está comendo → **o garfo direito está
  livre**.

Se as três valem, o filósofo **conquista os dois garfos de uma só vez** (`state[i] = EATING`)
e é liberado (`up(&s[i])`).

**⚠️ Atenção:** `test` é chamada **sempre de dentro de uma região crítica** protegida por
`mutex` (tanto em `take_forks` quanto em `put_forks`). Ela **não** faz `down`/`up` no mutex
— ela **presume** que o chamador já o detém. Chamá-la fora do mutex seria uma condição de
corrida.

### Por que não há deadlock

- Um filósofo **nunca segura um garfo enquanto espera pelo outro**. Ele muda para `EATING`
  (o que equivale a "peguei os dois") somente quando **ambos** estão disponíveis. Isso
  **elimina a condição de posse-e-espera** de Coffman, e sem ela não pode haver deadlock.
- O `mutex` é sempre **liberado antes** de qualquer bloqueio (`down(&s[i])` vem **depois**
  de `up(&mutex)`), então nenhum filósofo trava o sistema segurando o mutex.
- Todo filósofo que termina de comer **obrigatoriamente testa os dois vizinhos**, garantindo
  que quem estava esperando por ele seja liberado. Nenhum despertar se perde.

### Máximo paralelismo

A solução permite que **até `N/2` filósofos comam simultaneamente** (com N=5, dois
filósofos, por exemplo F0 e F2, ou F1 e F3). É o máximo teoricamente possível, já que
filósofos vizinhos nunca podem comer ao mesmo tempo. É por isso que esta solução é superior
à do mutex único.

### Ressalva: inanição ainda é possível

A solução **não garante ausência de starvation**. Um filósofo pode ser sistematicamente
"cercado": sempre que ele fica com fome, um dos vizinhos está comendo. Se os dois vizinhos
se alternarem indefinidamente, ele **nunca** conseguirá comer, mesmo sem deadlock. Soluções
para isso exigem alguma forma de **fila justa** ou **envelhecimento**. *(É um ótimo ponto
extra na resposta.)*

---

## 6.3 O problema dos Leitores e Escritores

### Descrição do problema (questão 2.a)

Formulado por **Courtois et al. (1971)**, modela o acesso concorrente a um **banco de dados**
(ou a qualquer estrutura de dados compartilhada) por dois tipos de processos com
necessidades diferentes:

- **Leitores (readers):** apenas **consultam** os dados, sem modificá-los.
- **Escritores (writers):** **modificam** os dados.

**As regras de acesso (o requisito do problema):**

| Situação | Permitido? | Por quê |
|---|---|---|
| **Vários leitores simultaneamente** | ✅ **SIM** | Leitura não altera nada; todos veem o mesmo estado consistente. Impedir isso seria um desperdício desnecessário de paralelismo. |
| **Um escritor sozinho** | ✅ SIM | É o acesso exclusivo necessário para modificar com segurança. |
| **Um escritor com qualquer leitor** | ❌ **NÃO** | O leitor poderia ler um estado **parcialmente modificado** (inconsistente). Ex.: uma transferência bancária que já debitou a conta A mas ainda não creditou a B — o leitor veria dinheiro desaparecer. |
| **Dois ou mais escritores simultaneamente** | ❌ **NÃO** | Condição de corrida clássica: as escritas se sobrepõem e os dados são corrompidos. |

**Formulação sintética:** *é permitido acesso concorrente para leitura, mas o acesso para
escrita deve ser exclusivo.* Isso é conhecido como **exclusão mútua de leitor-escritor**,
e é diferente da exclusão mútua simples (que serializaria também os leitores).

**Exemplo motivador:** um sistema de reservas de passagens aéreas com muitos processos
consultando voos e alguns processos efetuando reservas. Serializar todas as consultas
tornaria o sistema inutilizável — daí a necessidade de permitir leituras concorrentes.

**O desafio:** encontrar um mecanismo que permita esse paralelismo assimétrico **sem
corromper os dados e sem causar deadlock**.

### Variantes do problema (bom saber)

- **Prioridade para leitores** (a solução da Figura 2): um leitor que chega enquanto outros
  estão lendo **entra imediatamente**, mesmo que haja escritores esperando. Maximiza o
  paralelismo, mas pode causar **inanição dos escritores**.
- **Prioridade para escritores**: assim que um escritor chega, **nenhum novo leitor** é
  admitido; os leitores atuais terminam e o escritor entra. Evita inanição dos escritores,
  mas pode causar inanição dos leitores.
- **Solução justa**: quem chega primeiro é servido primeiro (com uma fila), evitando
  inanição dos dois lados ao custo de menos paralelismo.

---

## 6.4 Solução dos Leitores e Escritores com semáforos (questão 2.b — Figura 2)

### O código

```c
typedef int semaphore;
semaphore mutex = 1;               /* controla o acesso a 'rc' */
semaphore db    = 1;               /* controla o acesso ao banco de dados */
int rc = 0;                        /* nº de processos lendo ou querendo ler */

void reader(void)
{
    while (TRUE) {                 /* repete para sempre */
        down(&mutex);              /* obtém acesso exclusivo a 'rc' */
        rc = rc + 1;               /* agora há mais um leitor */
        if (rc == 1) down(&db);    /* se este é o primeiro leitor, trava o BD */
        up(&mutex);                /* libera o acesso exclusivo a 'rc' */
        read_data_base();          /* acesso aos dados */
        down(&mutex);              /* obtém acesso exclusivo a 'rc' */
        rc = rc - 1;               /* agora há um leitor a menos */
        if (rc == 0) up(&db);      /* se este é o último leitor, libera o BD */
        up(&mutex);                /* libera o acesso exclusivo a 'rc' */
        use_data_read();           /* região não crítica */
    }
}

void writer(void)
{
    while (TRUE) {                 /* repete para sempre */
        think_up_data();           /* região não crítica */
        down(&db);                 /* obtém acesso exclusivo ao BD */
        write_data_base();         /* atualiza os dados */
        up(&db);                   /* libera o acesso exclusivo */
    }
}
```

### Papel de cada semáforo e da variável `rc`

| Elemento | Valor inicial | Papel |
|---|---|---|
| **`db`** ("data base") | **1** | **EXCLUSÃO MÚTUA SOBRE O BANCO DE DADOS.** É a trava do recurso propriamente dito. Quem a detém tem acesso exclusivo aos dados. **Detalhe genial:** ela é adquirida **pelo PRIMEIRO leitor em nome de TODOS os leitores** e liberada **pelo ÚLTIMO**. Assim, o grupo inteiro de leitores conta como **um único detentor** da trava — é o que permite a leitura concorrente. Já cada escritor a adquire **individualmente**, obtendo acesso exclusivo. |
| **`mutex`** | **1** | **EXCLUSÃO MÚTUA SOBRE O CONTADOR `rc`.** Protege a região crítica onde `rc` é lido, incrementado/decrementado e testado. É indispensável porque `rc = rc + 1` **não é atômico** e porque o teste `if (rc == 1)` e a ação `down(&db)` precisam ser **indivisíveis**. |
| **`rc`** ("reader count") | **0** | **Contador de leitores** ativos (lendo ou prestes a ler). Não é semáforo — é uma variável compartilhada comum, protegida por `mutex`. É a informação que permite identificar quem é o **primeiro** e quem é o **último** leitor. |

### Funcionamento do leitor

**Entrada:**
1. `down(&mutex)` — acesso exclusivo ao contador.
2. `rc = rc + 1` — registra-se como leitor ativo.
3. `if (rc == 1) down(&db)` — **"eu sou o primeiro leitor?"** Se sim, ele **trava o banco
   de dados em nome de todo o grupo de leitores**. Isso impede que qualquer escritor entre
   enquanto houver pelo menos um leitor. Se `rc > 1`, **alguém já travou** — este leitor
   simplesmente **se junta ao grupo, sem tocar em `db`**. É exatamente aqui que a leitura
   concorrente acontece.
4. `up(&mutex)` — libera o contador para os outros leitores.
5. `read_data_base()` — **vários leitores executam esta linha simultaneamente**.

**Saída:**
6. `down(&mutex)` — acesso exclusivo ao contador.
7. `rc = rc - 1` — retira-se do grupo.
8. `if (rc == 0) up(&db)` — **"eu sou o último leitor?"** Se sim, **libera o banco de
   dados**, permitindo que um escritor (possivelmente bloqueado em `down(&db)`) entre.
9. `up(&mutex)` — libera o contador.
10. `use_data_read()` — processa o que leu, **fora** da região crítica.

### Funcionamento do escritor

O escritor é **trivial**:
1. `think_up_data()` — prepara os dados, fora da região crítica.
2. `down(&db)` — **pede acesso exclusivo ao banco**. Bloqueia se:
   - outro escritor estiver escrevendo, **ou**
   - houver **qualquer** leitor lendo (pois o primeiro leitor tomou `db`).
3. `write_data_base()` — modifica os dados, com garantia de exclusividade.
4. `up(&db)` — libera o banco.

Ele **não usa `mutex` nem `rc`** porque não precisa contar nada — ele sempre age sozinho.

### Por que `mutex` é indispensável (pergunta frequente)

**Sem `mutex`**, duas condições de corrida aparecem:

1. **Corrida sobre o contador.** `rc = rc + 1` são três instruções de máquina. Dois leitores
   entrando ao mesmo tempo poderiam ambos ler `rc == 0`, ambos gravar `rc == 1`. O contador
   ficaria errado: quando o primeiro saísse, faria `rc == 0` e **liberaria `db`**, permitindo
   que um escritor entrasse **enquanto o segundo leitor ainda está lendo**. Corrupção de dados.
2. **Corrida sobre o teste do primeiro leitor.** Dois leitores poderiam ambos ver `rc == 1`
   após seus incrementos (dependendo da intercalação) e **ambos executar `down(&db)`** — o
   segundo ficaria **bloqueado para sempre**, pois `db` só será liberado uma vez.

### Existe condição de corrida ou deadlock nesta solução?

**Correção: NÃO há condição de corrida.** A solução é correta:
- `mutex` protege o contador e o teste do primeiro/último leitor de forma atômica;
- `db` garante que escritores tenham acesso exclusivo e que nenhum escritor entre enquanto
  houver leitores;
- Não há deadlock: nenhum processo bloqueia em `db` **segurando** `mutex`, porque o
  `down(&db)` do primeiro leitor ocorre **dentro** do `mutex`, mas nesse instante ou `db`
  está livre (e ele passa) ou está com um escritor — que **não precisa de `mutex`** para
  terminar e liberar `db`. Como o escritor nunca pede `mutex`, **não há espera circular**.

> ⚠️ Curiosidade que vale ponto: o `down(&db)` do primeiro leitor **acontece com o `mutex`
> em mãos**, o que normalmente violaria a "regra de ouro" da Seção 5. Aqui é seguro
> **precisamente porque o escritor não usa `mutex`** — logo, quem detém `db` sempre
> consegue progredir e liberá-lo.

**⚠️ PROBLEMA REAL DA SOLUÇÃO: INANIÇÃO DOS ESCRITORES (*starvation*).**

Esta solução dá **prioridade absoluta aos leitores**:

1. O leitor L1 chega, faz `rc = 1`, executa `down(&db)` e começa a ler.
2. O escritor W chega e executa `down(&db)` → **bloqueia**, esperando.
3. O leitor L2 chega. Ele faz `down(&mutex)`, `rc = 2`. Como `rc != 1`, ele **não toca em
   `db`** — **entra direto para ler**, **passando na frente do escritor que já estava
   esperando**.
4. L1 termina, mas `rc` ainda é 1 (por causa de L2) → **`db` não é liberado**.
5. Chegam L3, L4, L5... Enquanto **houver pelo menos um leitor ativo continuamente**,
   `rc` **nunca chega a zero**.
6. **O escritor W nunca escreve.** Inanição permanente.

Em um sistema real com fluxo constante de leitores (o que é comum: consultas são muito mais
frequentes que atualizações), o escritor pode esperar **indefinidamente**.

**Solução para a inanição (Tanenbaum sugere):** quando um **escritor chega**, marcar que há
escritor esperando e **não admitir novos leitores** — colocá-los em uma fila **atrás** do
escritor. Assim, os leitores que já estão lendo terminam, `rc` chega a 0, `db` é liberado e
o escritor entra. O custo é **menos paralelismo** de leitura.

*(Implementação típica: acrescentar um terceiro semáforo, frequentemente chamado de
`turnstile` ou `wrt_pending`, que os leitores precisam atravessar na entrada e que o
escritor toma ao chegar.)*

---

## 6.5 Comparação dos dois problemas

| | **Jantar dos Filósofos** | **Leitores e Escritores** |
|---|---|---|
| **O que modela** | Processos que precisam de **múltiplos recursos simultaneamente** | Processos com **necessidades assimétricas** sobre **um** recurso |
| **Risco principal** | **Deadlock** (espera circular) | **Inanição** dos escritores |
| **Semáforo de exclusão mútua** | `mutex` protege `state[]` | `mutex` protege `rc` |
| **Semáforo do recurso** | `s[i]` — um **privado por processo** | `db` — **um único**, para o banco |
| **Como o paralelismo é obtido** | Filósofos **não vizinhos** comem juntos (até N/2) | **Todos os leitores** leem juntos (primeiro trava por todos) |
| **Estado compartilhado** | `state[N]` — estado de cada filósofo | `rc` — contagem de leitores |
| **Quem desperta quem** | Quem **sai** testa os **dois vizinhos** | Quem sai **último** libera `db` |
| **Sofre inanição?** | Sim (possível, se os vizinhos se alternarem) | **Sim, gravemente** (escritores) |

**Padrão comum às duas soluções (a lição geral da lista):**

1. Existe uma **variável de estado compartilhada** (`state[]` / `rc`) que descreve a
   situação do sistema.
2. Existe um **`mutex`** que torna **atômicas** a leitura, a alteração e o teste dessa
   variável.
3. Existe **um ou mais semáforos de bloqueio** (`s[i]` / `db`) sobre os quais os processos
   efetivamente esperam.
4. A regra de ouro é sempre respeitada: **libera-se o `mutex` antes de bloquear** — ou,
   quando não se libera (caso do primeiro leitor), garante-se que quem detém o recurso não
   precisa do `mutex` para progredir.
5. **Quem torna a condição verdadeira é quem sinaliza** — o filósofo que termina de comer
   testa os vizinhos; o último leitor libera o banco.

---

## Checklist de revisão da Lista 06

- [ ] Sei descrever o cenário do jantar dos filósofos (5 filósofos, 5 garfos, 2 por refeição).
- [ ] Sei fazer o mapeamento filósofo→processo, garfo→recurso, comer→região crítica.
- [ ] Sei narrar o **deadlock**: todos pegam o garfo esquerdo ao mesmo tempo.
- [ ] Sei citar as 4 condições de Coffman presentes nesse deadlock.
- [ ] Sei explicar por que "devolver o garfo e tentar de novo" gera **livelock/starvation**.
- [ ] Sei explicar por que o mutex único resolve mas destrói o paralelismo.
- [ ] Sei escrever/explicar `take_forks`, `put_forks` e `test` da solução de Dijkstra.
- [ ] Sei dizer que `mutex` protege `state[]` e que `s[i]` é o semáforo **privado** de bloqueio.
- [ ] Sei explicar por que `up(&mutex)` vem **antes** de `down(&s[i])`.
- [ ] Sei explicar por que a solução **elimina o deadlock** (quebra posse-e-espera).
- [ ] Sei que até **N/2** filósofos comem em paralelo, e que inanição ainda é possível.
- [ ] Sei enunciar as 4 regras de acesso do problema dos leitores e escritores.
- [ ] Sei dizer o papel de `db`, `mutex` e `rc` na solução da Figura 2.
- [ ] Sei explicar o truque do **primeiro leitor trava / último leitor libera**.
- [ ] Sei explicar por que `mutex` é indispensável (as 2 corridas que surgiriam sem ele).
- [ ] Sei narrar a **inanição dos escritores** passo a passo e propor a correção.
