# 🧵 Threads — Lista II (Guia de Estudo)

> **Como usar:** leia na ordem no Dia 1, e no Dia 2 faça só as seções **🎯 Memorize** e os **🃏 Flashcards** do final. Se sobrar tempo, refaça de cabeça a tabela comparativa da Questão 2.

**Sumário rápido**
1. [Conceitos básicos de threads](#-questão-1--conceitos-básicos)
2. [Threads: usuário × núcleo × híbrido](#-questão-2--implementação-de-threads)
3. [🃏 Flashcards de revisão](#-flashcards-de-revisão)

---

# 📘 Questão 1 — Conceitos básicos

## a) O que é uma thread e qual a relação com processos?

**Thread = linha de execução independente dentro de um processo.**

| | **Processo** | **Thread** |
|---|---|---|
| Espaço de endereçamento | Próprio | **Compartilhado** com as threads do mesmo processo |
| Proteção mútua | **Sim** (podem ser de usuários hostis) | **Não** (e nem precisa: elas cooperam) |
| Papel | Unidade de **agrupamento de recursos** | Unidade de **execução** |

> 🎯 **Memorize:** *processo agrupa recursos, thread executa.* Threads vivem dentro do processo e dividem tudo dele.

---

## b) Por que cada thread precisa de sua própria pilha?

Porque cada thread executa **um trecho diferente do código, em um ponto diferente, de forma independente**. Cada uma tem suas próprias chamadas de procedimento, variáveis locais e endereços de retorno.

> 💡 Cada thread tem **PC, registradores, pilha e estado** próprios. O resto (código, dados globais, arquivos abertos) é compartilhado.

---

## c) Estados de uma thread

```
        escalonador escolhe
   ┌──────────────────────────┐
   ▼                          │
Em execução ──(espera evento)──► Bloqueada
   │  ▲                          │
   │  └── escalonado ──┐         │ evento ocorreu
   │                   │         ▼
   └──(preempção)────► Pronta ◄──┘
   │
   └──(termina)──► Concluída
```

| Estado | Significado |
|---|---|
| **Em execução** | Está usando a CPU |
| **Pronta** | Pode executar, aguarda o escalonador |
| **Bloqueada** | Espera um evento (E/S, semáforo, mutex, outra thread) |
| *Concluída* | Terminou sua função |

---

## d) Por que threads do mesmo processo compartilham recursos?

Três razões:

| Razão | Ideia central |
|---|---|
| **De projeto** | Threads **cooperam** para uma mesma aplicação. Ex.: processador de texto com thread de interface + thread de reformatação + thread de salvar; todas precisam enxergar **o mesmo documento**. |
| **De desempenho** | Memória compartilhada é a comunicação **mais barata** (basta escrever numa variável global). Criar e chavear threads é bem mais rápido porque **não troca o espaço de endereçamento** (sem troca de tabela de páginas, sem invalidar TLB/cache). |
| **Conceitual** | Recursos **pertencem ao processo** por definição; a thread só executa. Logo, os recursos são comuns a todas as suas threads. |

> 🎯 **Memorize:** *Cooperação + desempenho + definição.*

---

## e) Vantagens, desvantagens e quando usar

### ✅ Vantagens

| Vantagem | Por quê |
|---|---|
| Criação/término **muito mais rápidos** | ~10 a 100× mais rápido; não cria espaço de endereçamento nem tabela de páginas |
| Chaveamento **mais barato** | TLB e cache continuam válidos |
| Comunicação **trivial** | Memória compartilhada, sem chamada de sistema |
| Sobreposição **CPU × E/S** | Uma bloqueia em E/S, outra continua computando |
| **Paralelismo real** | Threads em núcleos diferentes aceleram **uma** aplicação |
| Modelo de programação **mais simples** | Decompõe aplicação concorrente em atividades quase sequenciais |

### ❌ Desvantagens

| Desvantagem | Por quê |
|---|---|
| **Sem proteção mútua** | Uma corrompe dados das outras; erro fatal em uma **derruba o processo todo** |
| **Condições de corrida** | Exige sincronização; fonte de deadlocks e bugs intermitentes |
| **Bibliotecas não reentrantes** | `errno`, `malloc`, funções com ponteiro estático |
| **Semântica confusa** | O que `fork()` faz? Copia todas as threads ou só a chamadora? Para quem vai o sinal? |

### 🔀 Threads ou processos?

| Use **threads** quando... | Use **processos** quando... |
|---|---|
| Atividades **cooperam** e compartilham dados | Atividades são **independentes** ou não confiáveis |
| Há muita E/S para sobrepor com CPU | Precisa de **isolamento/proteção** |
| Precisa manter a aplicação **responsiva** | Componentes têm **permissões/ciclos de vida** diferentes |
| Vai acelerar **uma aplicação** em multiprocessador | Componentes rodam em **máquinas diferentes** |
| *Ex.: editor de texto, servidor web, planilha* | *Ex.: shell, abas de navegador isoladas por segurança* |

> 🎯 **Regra de bolso:** *cooperam → threads. Desconfiam → processos.*

---

# 📗 Questão 2 — Implementação de threads

## A) Threads no **espaço do usuário**

**Ideia:** o núcleo **não sabe** que existem threads; vê só um processo com uma linha de execução.

- Uma biblioteca no espaço do usuário, o **runtime**, faz toda a gerência.
- Cada processo tem sua **tabela de threads própria** (PC, registradores, pilha, estado), mantida pelo runtime.

| Entidade | Papel |
|---|---|
| **Sistema Operacional** | Escalona **processos**; ignora as threads |
| **Runtime** | Cria, termina, bloqueia, sincroniza e **escalona as threads** |

**Como o runtime troca de thread** (`thread_yield`, `thread_exit` ou bloqueio):
1. Salva os registradores da thread atual **na tabela de threads**;
2. Escolhe a próxima thread pronta com o **algoritmo próprio**;
3. Restaura registradores, **SP e PC** da escolhida.

> ⚡ Só **procedimento local**: sem *trap*, sem chamada de sistema, sem mudança de modo, sem limpar cache → **ordem de grandeza mais rápido** que o núcleo.

---

## B) Threads no **núcleo**

**Ideia:** o núcleo conhece e gerencia as threads diretamente.

- **Não há runtime.**
- Tabela de threads **global**, no núcleo (além da tabela de processos).
- Toda operação (criar, terminar, esperar, sincronizar) é **chamada de sistema** (modo usuário → núcleo).
- O SO faz **tudo** e escalona **threads**, de qualquer processo.

> ❓ **Por que sem runtime?** O runtime só existe para *simular* no espaço do usuário o que o núcleo não oferece. Se o núcleo já faz tudo, a camada é **redundante**.

---

## C) Por que só o espaço do usuário permite algoritmos de escalonamento diferentes por processo? *(2.c)*

| | Quem escalona as threads | Consequência |
|---|---|---|
| **Usuário** | O **runtime** de cada processo (código da aplicação) | Cada processo pode ter **seu próprio algoritmo** (circular, prioridade, cooperativo...) |
| **Núcleo** | O **escalonador do SO** (único, código do kernel) | **Um algoritmo só** para todos; aplicações ajustam só parâmetros (prioridade, classe) |

> 🎯 **Memorize:** *runtime é da aplicação (troca à vontade); escalonador do núcleo é do SO (um só). Deixar aplicação injetar algoritmo no kernel = risco de segurança e estabilidade.*

---

## D) Comparação sistemática *(2.d)*

| Aspecto | 👤 **Espaço do usuário** | 🛡️ **Núcleo** |
|---|---|---|
| **SO sem suporte a threads** | ✅ **Possível** (só ligar a biblioteca; foi como threads surgiram) | ❌ **Impossível** (depende do núcleo) |
| **Custo** de criar/terminar/chavear/bloquear | 🟢 **Muito baixo**: procedimento local, sem trap | 🔴 **Alto**: chamada de sistema, troca de modo, salva contexto, pode limpar cache (ainda mais barato que processo) |
| **Chamada bloqueante** | 🔴 **Problema central**: o núcleo vê só o processo, então **bloqueia o processo inteiro** (todas as threads param, mesmo as prontas). Falta de página idem. | 🟢 **Natural**: bloqueia só **aquela thread**; o núcleo escalona outra |
| **Preempção** entre threads | 🔴 Difícil: sem interrupção de relógio para o runtime; depende de `thread_yield` voluntário (laço infinito trava o processo) | 🟢 Natural: interrupção de relógio |
| **Paralelismo real** (multiprocessador) | 🔴 Limitado (núcleo vê um só fluxo) | 🟢 Sim, threads em CPUs diferentes |
| **Recursos do núcleo** | 🟢 Nenhum por thread; escala para milhares | 🔴 Entrada na tabela + pilha de kernel por thread |

**Contornos (imperfeitos) para o bloqueio no usuário:**
- *Jacket/wrapper* em torno da chamada;
- `select` para testar se bloquearia antes de chamar;
- E/S não bloqueante.

> ⚠️ Todos exigem reescrever partes da biblioteca do sistema e nem sempre são possíveis.

> 🎯 **Memorize:** *Usuário = barato, mas cego (bloqueio derruba tudo). Núcleo = caro, mas esperto.*

---

## E) Implementação **híbrida** *(2.e)*

**Como funciona (modelo M:N):**
- O núcleo enxerga poucas **threads de núcleo**.
- Sobre cada uma, o runtime **multiplexa várias threads de usuário**.
- O programador define quantas de cada.
- *Variante:* **scheduler activation** — ao bloquear uma thread, o núcleo avisa o runtime com um **upcall**, que escalona outra thread de usuário.

```
   Threads de usuário:   U1 U2 U3 U4 U5 U6     ← runtime multiplexa (barato)
                          \ | /   \ | /
   Threads de núcleo:      K1       K2          ← núcleo escalona (paralelismo)
```

| Herda do **espaço do usuário** | Herda do **núcleo** |
|---|---|
| Runtime e tabela de threads no usuário | Núcleo conhece e escalona as threads de núcleo |
| Troca entre threads de usuário na **mesma** thread de núcleo é **barata** (procedimento local) | Operações nas threads de núcleo = **chamada de sistema** (mesmo custo) |
| **Algoritmo de escalonamento** definido pela aplicação | **Paralelismo real** (threads de núcleo em CPUs diferentes) |
| **Muitas** threads de usuário, baixo custo | **Bloqueio bem tratado**: se uma thread de núcleo bloqueia, as outras seguem executando as demais threads de usuário |

> 🎯 **Memorize:** *Híbrido = o melhor dos dois: barato + flexível (usuário) e paralelo + sem bloqueio total (núcleo).*

---

# 🃏 Flashcards de revisão

Cubra a resposta e teste-se.

| Pergunta | Resposta |
|---|---|
| Processo × thread em uma frase? | Processo agrupa recursos; thread é a unidade de execução |
| O que cada thread tem de próprio? | PC, registradores, pilha, estado |
| Por que threads não têm proteção mútua? | Cooperam; a proteção é entre processos |
| 3 razões para compartilhar recursos? | Projeto (cooperação), desempenho, conceitual (recursos são do processo) |
| Estados de uma thread? | Em execução, pronta, bloqueada (+ concluída) |
| Principal vantagem de threads sobre processos? | Criação/chaveamento/comunicação muito mais baratos |
| Principal risco de threads? | Sem isolamento + condições de corrida |
| Quando preferir processos? | Isolamento, código não confiável, máquinas distintas |
| Quem escalona threads no modelo usuário? | O runtime da aplicação |
| O núcleo enxerga threads no modelo usuário? | Não, só o processo |
| Onde fica a tabela de threads (usuário × núcleo)? | Usuário: no espaço do usuário, por processo. Núcleo: global, no kernel |
| Por que trocar threads no usuário é rápido? | Sem trap, sem chamada de sistema, sem limpar cache |
| Maior problema do modelo usuário? | Chamada bloqueante para o processo inteiro |
| Por que só o usuário permite algoritmos diferentes por processo? | O runtime é código da aplicação; o escalonador do núcleo é único |
| Threads de núcleo sem SO com suporte? | Impossível |
| O que é M:N? | Várias threads de usuário multiplexadas em poucas de núcleo |
| O que é scheduler activation? | Upcall do núcleo avisando o runtime sobre bloqueio |
| Trade-off resumido usuário × núcleo? | Usuário: barato, mas bloqueia tudo. Núcleo: caro, mas trata bloqueio e paraleliza |

---

