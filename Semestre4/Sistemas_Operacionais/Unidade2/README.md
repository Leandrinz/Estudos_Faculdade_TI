# Sistemas Operacionais (PEX0134) — Respostas das Listas 07 a 12

**Prof.:** Vinícius Samuel Valério de Souza · **UFERSA – Campus Pau dos Ferros**

## Índice

- [Lista 07 – Espaços de Endereçamento](#lista-07--espaços-de-endereçamento)
- [Lista 08 – Troca de Memória (Swapping)](#lista-08--troca-de-memória-swapping)
- [Lista 09 – Paginação e Tabelas de Páginas](#lista-09--paginação-e-tabelas-de-páginas)
- [Lista 10 – Algoritmos de Substituição de Páginas](#lista-10--algoritmos-de-substituição-de-páginas)
- [Lista 11 – Questões de Projeto da Paginação](#lista-11--questões-de-projeto-da-paginação)
- [Lista 12 – Segmentação](#lista-12--segmentação)

---

# Lista 07 – Espaços de Endereçamento

## Questão 1 — Sistemas sem SO (endereços referenciam a memória física diretamente)

**a) Problema da proteção**
Como os programas acessam diretamente endereços físicos, nada impede que um processo leia ou escreva em áreas de outro processo (ou do próprio SO), seja por erro ou de forma maliciosa, corrompendo dados e código alheios.

**b) Problema da relocação**
Os programas são gerados com endereços absolutos fixos. Se forem carregados em posição diferente da prevista (por exemplo, porque outro programa já ocupa aquela área), seus endereços (variáveis, saltos, chamadas) passam a apontar para locais errados, possivelmente pertencentes a outro processo.

**c) Impacto na coexistência de dois ou mais processos**
Sem resolver esses problemas, não é possível manter vários processos na memória de forma segura: um pode destruir o outro (proteção) e não há como carregá-los em posições livres arbitrárias sem que seus endereços fiquem incorretos (relocação). Na prática, só um programa por vez poderia executar com segurança.

## Questão 2 — Registrador-base e registrador-limite

**a) Funcionamento**
Dois registradores de hardware guardam: a **base** (endereço físico onde o processo começa) e o **limite** (tamanho do espaço do processo). O programa gera endereços a partir de 0; a cada acesso, o hardware compara o endereço com o limite e, se for válido, soma a base para obter o endereço físico. O SO carrega os valores desses registradores a cada troca de processo.

**b) Por que resolve a proteção**
Todo acesso é verificado contra o limite. Se o endereço ultrapassar o espaço do processo, o hardware gera uma exceção (trap) e o acesso é bloqueado. Somente o SO (modo núcleo) pode alterar os registradores.

**c) Por que resolve a relocação**
O programa usa endereços relativos (começando em 0) e a soma da base ocorre dinamicamente, a cada acesso. Para mover o processo para outro local, basta alterar o valor da base, sem modificar o código.

## Questão 3 — Espaço de endereçamento

É o conjunto de endereços que um processo pode usar para referenciar a memória, ou seja, uma **abstração de memória privada** (memória virtual/lógica) de cada processo, indo de 0 até um valor máximo.

**Relação com o gerenciamento de processos:** cada processo ativo possui seu próprio espaço de endereçamento, independente dos demais. O SO mapeia cada espaço para uma região da memória física, o que permite que vários processos coexistam com isolamento (proteção) e flexibilidade de posicionamento (relocação).

---

# Lista 08 – Troca de Memória (*Swapping*)

## Questão 1 — Técnica de Swapping

**a) Como funciona**
O processo é carregado **inteiro** do disco para a memória principal, em uma área contígua, e executa. Quando é preciso liberar espaço (memória cheia, processo bloqueado ou fim de quantum), ele é copiado de volta para o disco (*swap out*) e outro processo é trazido (*swap in*).

**b) Lacunas**
São áreas livres da memória principal, deixadas entre processos quando estes saem ou quando têm tamanhos diferentes. Podem ser pequenas demais para comportar um novo processo, gerando fragmentação externa.

**c) Compactação como solução**
Consiste em mover todos os processos para uma das extremidades da memória, juntando as várias lacunas em uma única lacuna grande e contígua.

**d) Por que a compactação é inviável**
É muito custosa: exige copiar grandes quantidades de dados na memória, durante o que o sistema fica praticamente parado (consome tempo de CPU). Além disso, os processos movidos precisam ter seus registradores-base atualizados. Por isso raramente é usada.

**e) Processos que crescem/diminuem e a proteção**
O SO aloca uma **área extra de crescimento** junto ao processo quando ele é carregado. Os registradores base/limite garantem que o processo só acesse seu próprio espaço (o limite é atualizado quando ele cresce dentro da folga reservada), impedindo invasão do vizinho. Se o processo crescer além da folga e o vizinho estiver adjacente, ele é movido para uma lacuna maior ou enviado ao disco (*swap out*).

## Questão 2 — Mapeamento de lacunas

**a) Mapa de bits**

- **Memória principal:** dividida em *unidades de alocação* de tamanho fixo.
- **Mapa de bits:** 1 bit por unidade; **0 = livre**, **1 = ocupada**.
- **Relação:** o bit *i* do mapa corresponde à *i*-ésima unidade da memória.
- **Armazenar um processo:** se o processo precisa de *k* unidades, o SO percorre o mapa procurando *k* bits 0 consecutivos; ao achar, aloca o processo e marca esses bits como 1. Ao liberar, volta os bits para 0.

**b) Listas encadeadas**

- **Organização:** lista de segmentos da memória, em ordem de endereço. Cada nó contém: tipo (**P** = processo ou **L** = lacuna), endereço de início, tamanho e ponteiro para o próximo.
- **Relação:** cada nó representa um trecho contíguo da memória (ocupado ou livre).
- **Armazenar um processo:** percorrer a lista procurando uma lacuna (L) com tamanho suficiente; ao encontrar, o nó é dividido em um nó P (processo) e um nó L (sobra, se houver). Ao liberar um processo, o nó vira L e é fundido com lacunas vizinhas.

**c) Comparação**
As **listas encadeadas** costumam ser melhores. No mapa de bits, achar *k* zeros consecutivos é lento (varredura bit a bit), e há conflito na escolha do tamanho da unidade (unidade pequena = mapa grande; unidade grande = desperdício interno). Nas listas, o tamanho de cada lacuna já está registrado, a busca é mais rápida e a fusão de lacunas vizinhas é simples. A vantagem do mapa de bits é o tamanho fixo e a simplicidade.

**d) Duas listas (processos e lacunas) em vez de uma**

- **Melhorias:** a busca por lacunas é mais rápida (não percorre processos); a lista de lacunas pode ser ordenada por tamanho (acelera best fit); a própria lista pode ser guardada dentro das lacunas, sem gastar memória extra.
- **Pioras:** mais complexidade; a liberação de memória fica mais lenta (remover da lista de processos e inserir na de lacunas, possivelmente de forma ordenada), e a fusão de lacunas vizinhas fica mais difícil.

## Questão 3 — Algoritmos de encaixe

**a) Primeiro encaixe (*first fit*):** percorre a lista desde o início e usa a **primeira** lacuna grande o suficiente; a sobra vira uma nova lacuna.

**b) Próximo encaixe (*next fit*):** igual ao first fit, mas a busca começa **de onde parou** na última alocação (e não do início).

**c) Melhor encaixe (*best fit*):** percorre **toda** a lista e escolhe a **menor** lacuna que comporta o processo.

**d) Pior encaixe (*worst fit*):** percorre toda a lista e escolhe a **maior** lacuna disponível.

**e) Tamanho médio das lacunas geradas (sobras)**

| Algoritmo | Sobra média | Observação |
|---|---|---|
| Best fit | **Menores** | Gera muitas lacunas minúsculas e inúteis |
| First fit | Intermediária | — |
| Next fit | Intermediária | Semelhante ao first fit |
| Worst fit | **Maiores** | Sobra maior (mais aproveitável), mas consome as lacunas grandes |

**f) Tempo para encontrar a lacuna**

| Algoritmo | Tempo | Motivo |
|---|---|---|
| First fit | Rápido | Para na primeira lacuna adequada |
| Next fit | Rápido | Idem; evita reexaminar o início da lista |
| Best fit | **Lento** | Precisa varrer toda a lista |
| Worst fit | **Lento** | Precisa varrer toda a lista |

(Best e worst fit podem ser acelerados com lista de lacunas ordenada por tamanho.)

## Questão 4 — Swapping é eficiente hoje?

**Não.** Como o processo precisa estar **inteiro** na memória, processos grandes tornam a troca muito custosa (cópia de grandes volumes para/do disco, que é lento). Além disso, um processo maior que a memória principal simplesmente não consegue executar, e persiste o problema de fragmentação externa. Por isso utilizam-se técnicas de memória virtual (paginação/segmentação), que permitem manter apenas parte do processo na memória.

---

# Lista 09 – Paginação e Tabelas de Páginas

## Questão 1 — Swapping × memória virtual

No swapping, o processo precisa estar **inteiro** (contíguo) na memória para executar, e as trocas são de processos completos. Na memória virtual (paginação e segmentação), o processo executa com **apenas parte** dele na memória principal; o restante fica no disco e é trazido sob demanda, o que permite processos maiores que a memória física.

## Questão 2 — Princípios da paginação

**a) Espaço de endereçamento dos processos:** dividido em blocos de tamanho fixo chamados **páginas**, numeradas sequencialmente.

**b) Memória principal física:** dividida em blocos de tamanho fixo chamados **molduras de página** (*frames*).

**c) Relação página × moldura:** têm o **mesmo tamanho**. Cada moldura comporta o conteúdo de exatamente uma página. As páginas de um processo podem ocupar quaisquer molduras (não precisam ser contíguas), e a correspondência é registrada na tabela de páginas.

**d) Campos do endereço virtual**

- **Número da página** (bits mais significativos): identifica a página do espaço de endereçamento.
- **Deslocamento (*offset*)** (bits menos significativos): posição do byte dentro da página.

**e) Campos do endereço real**

- **Número da moldura** (bits mais significativos): identifica a moldura na memória física.
- **Deslocamento (*offset*):** posição do byte dentro da moldura (o mesmo valor do endereço virtual).

**f) Memória virtual de 256 bytes, real de 16 bytes, páginas de 8 bytes**

- Virtual: 256 = 2⁸ → **8 bits**. Real: 16 = 2⁴ → **4 bits**. Página: 8 = 2³ → **3 bits de offset**.
- **Endereço virtual (8 bits):** 5 bits de nº da página + 3 bits de offset (32 páginas).
- **Endereço real (4 bits):** 1 bit de nº da moldura + 3 bits de offset (2 molduras).
- *Motivo:* o offset é igual nos dois endereços porque página e moldura têm o mesmo tamanho; os bits restantes identificam a página (virtual) ou a moldura (real).

**g) Páginas de 4 bytes**

- Offset: 4 = 2² → **2 bits**.
- **Endereço virtual (8 bits):** 6 bits de nº da página + 2 de offset (64 páginas).
- **Endereço real (4 bits):** 2 bits de nº da moldura + 2 de offset (4 molduras).
- *O que mudou:* o offset diminuiu e, como o total de bits é fixo, aumentou o nº de bits do número da página/moldura (mais páginas e molduras, ou seja, tabela de páginas maior).

**h) Endereços das instruções**
Referem-se ao **espaço de endereçamento do processo (endereços virtuais)**, e não à memória física. Problemas possíveis ao acessar o conteúdo: a página pode **não estar na memória principal** (*falta de página*, quando precisa ser buscada no disco) ou o endereço pode ser **inválido/violar a proteção** (gera exceção).

## Questão 3 — Conversão de endereços virtuais em reais

**a) MMU (*Memory Management Unit*):** hardware que intercepta os endereços virtuais gerados pela CPU e os converte em endereços reais usando a tabela de páginas; também verifica presença e proteção da página.

**b) Tabela de páginas:** estrutura que mapeia cada número de página para o número da moldura correspondente. É a tabela consultada pela MMU para fazer a conversão.

**c) Linhas da tabela × páginas do processo:** cada linha (entrada) corresponde a **uma página** do processo e é indexada pelo número da página. Logo, o número de linhas é igual ao número de páginas do espaço de endereçamento.

**d) Campos de uma linha da tabela**

- **Número da moldura** onde a página está.
- **Bit de presença/ausência:** indica se a página está na memória (se 0, ocorre falta de página).
- **Bits de proteção:** permissões (leitura/escrita/execução).
- **Bit de modificação (*dirty*):** indica se a página foi alterada.
- **Bit de referência:** indica se a página foi acessada recentemente.

**e) Passos da conversão**

1. A CPU gera o endereço virtual.
2. A MMU separa o número da página e o offset.
3. O número da página é usado como índice na tabela de páginas.
4. Verifica-se o bit de presença: se for 0, ocorre **falta de página**. O SO traz a página do disco, atualiza a tabela e reinicia a instrução.
5. Verifica-se a proteção do acesso.
6. Obtém-se o número da moldura.
7. Concatena-se moldura + offset, formando o **endereço real**, que é enviado à memória.

**f) Tabelas × processos; troca de contexto**
**Cada processo tem sua própria tabela de páginas**, pois cada um possui seu espaço de endereçamento. Em uma troca de contexto, o SO faz a MMU passar a usar a tabela do processo que vai executar (carregando-a ou atualizando o registrador que aponta para ela), salvando o estado da anterior (como os bits R/M). Também é necessário invalidar o conteúdo em cache da tradução (TLB) do processo anterior.

---

# Lista 10 – Algoritmos de Substituição de Páginas

## Questão 1 — Falta de página

**a) O que é:** ocorre quando o processo referencia uma página que **não está na memória principal** (bit de presença = 0). A MMU gera uma exceção e o SO precisa buscar a página no disco.

**b) Problemas de desempenho:** o acesso ao disco é extremamente lento comparado à memória, e o processo fica bloqueado até a página chegar. Se não houver moldura livre, é preciso escolher uma página para remover e, se ela foi modificada, gravá-la no disco antes (mais um acesso lento). Há ainda custo de tratamento pelo SO e troca de contexto.

**c) Relação com a ultrapaginação (*thrashing*):** quando as faltas de página ocorrem com frequência excessiva, o sistema passa mais tempo trocando páginas entre disco e memória do que executando instruções úteis. Isso é a ultrapaginação.

## Questão 2 — Algoritmos de substituição

**a) O que é e importância:** é o algoritmo que escolhe qual página (vítima) será removida da memória quando ocorre falta de página e não há moldura livre. Escolher bem (remover páginas que não serão necessárias em breve) reduz as faltas futuras e evita a ultrapaginação.

**b) Ótimo:** remove a página que será referenciada **mais tarde no futuro** (ou nunca mais). É o que gera o **menor número possível de faltas**, mas é **impraticável**, pois exige conhecer a sequência futura de referências, o que o SO não tem. Serve apenas como referência de comparação.

**c) NRU (*Not Recently Used*):** usa os bits **R** (referenciada) e **M** (modificada); o bit R é zerado periodicamente por interrupção de relógio. As páginas são classificadas em:

- Classe 0: R=0, M=0
- Classe 1: R=0, M=1
- Classe 2: R=1, M=0
- Classe 3: R=1, M=1

Remove uma página **aleatória** da classe não vazia de menor número.
*Problemas:* a escolha dentro da classe é aleatória; a informação é grosseira (só indica se houve uso no último intervalo, sem considerar quando ou quantas vezes), podendo remover páginas importantes.

**d) FIFO:** mantém as páginas em fila por ordem de chegada e remove a **mais antiga**.
*Problemas:* ignora o uso da página; pode remover páginas muito usadas só por serem antigas; sofre da anomalia de Belady (mais molduras podem gerar mais faltas).

**e) Segunda chance:** é uma variação do FIFO. Examina a página mais antiga: se **R=0**, ela é removida; se **R=1**, zera-se R e a página vai para o final da fila (como se tivesse acabado de chegar), e o algoritmo examina a próxima.
*Problemas:* mover páginas na fila é ineficiente; se todas tiverem R=1, degenera em FIFO puro; considera apenas se houve referência, não a frequência.

**f) Relógio (*clock*):** igual à segunda chance, mas com as páginas em uma **lista circular** e um ponteiro (o "ponteiro do relógio") apontando para a mais antiga. Se R=0, a página é substituída; se R=1, zera-se R e o ponteiro avança. Relação: é a implementação eficiente da segunda chance (evita mover páginas na lista), que por sua vez é uma melhoria do FIFO.
*Problemas:* mesmos da segunda chance (só considera o bit R; se todas tiverem R=1, dá uma volta completa; ainda se aproxima do FIFO).

**g) LRU (*Least Recently Used*):** remove a página que está **há mais tempo sem ser usada**. Aproxima-se do ótimo porque se baseia na **localidade de referência**: páginas usadas recentemente tendem a ser usadas de novo em breve, e as não usadas há muito tempo tendem a continuar sem uso. Ou seja, usa o passado como estimativa do futuro (que é o que o ótimo usa de fato). A implementação exata é cara (exige atualização a cada referência).

**h) NFU (*Not Frequently Used*):** simula o LRU em software. Cada página tem um **contador**; a cada interrupção de relógio, o bit R é somado ao contador. Remove a página com **menor contador**.
*Problemas:* nunca "esquece" o passado: uma página muito usada no início mantém contador alto mesmo sem uso atual, enquanto páginas novas e úteis podem ser removidas. (A solução é o *aging*.)

## Questão 3 — Paginação por conjunto de trabalho

**a) Conjunto de trabalho:** conjunto de páginas que um processo está usando ativamente em um dado intervalo de tempo (por exemplo, nas últimas *k* referências), baseado na localidade de referência.

**b) Relação com molduras e desempenho:**

- Se o número de molduras disponíveis é **≥ tamanho do conjunto de trabalho**: poucas faltas de página e bom desempenho.
- Se é **menor**: o processo gera faltas constantes, levando à ultrapaginação e queda forte de desempenho.

---

# Lista 11 – Questões de Projeto da Paginação

## Questão 1 — Tamanho das páginas

| Aspecto | Páginas **pequenas** | Páginas **grandes** |
|---|---|---|
| **Espaço da tabela de páginas na MMU** | Mais páginas → tabela **maior** (mais espaço) | Menos páginas → tabela **menor** |
| **Fragmentação interna** | **Menor** (em média, só meia página da última página é desperdiçada) | **Maior** |
| **Faltas de página futuras** | **Mais** faltas, pois cada falta traz pouco conteúdo vizinho | **Menos** faltas (mais dados vizinhos já carregados), mas pode trazer dados inúteis que ocupam a moldura |
| **Transferência disco ↔ memória** | Transferências pequenas e flexíveis (mais dinâmicas), mas **numerosas**, e o tempo de busca no disco domina | **Poucas** transferências, melhor aproveitamento do tempo de busca, porém cada uma é maior e menos flexível |

**Conclusão:** não há tamanho ideal absoluto; busca-se um valor intermediário (tipicamente 4 KB) que equilibre tabela de páginas, fragmentação interna e custo de transferência.

---

# Lista 12 – Segmentação

## Questão 1 — Espaço de endereçamento único e estruturas dinâmicas

Em um espaço único e linear, as estruturas dinâmicas (pilhas, filas, listas, tabelas) crescem e diminuem dentro da mesma faixa de endereços e podem **colidir** umas com as outras. Pode faltar espaço para uma estrutura enquanto sobra para outra, obrigando a mover e reorganizar dados e endereços, o que é trabalhoso e ineficiente.

## Questão 2 — Princípios da segmentação

**a) Organização do espaço de endereçamento**

- **Quantidade de segmentos:** vários segmentos por espaço de endereçamento.
- **Tamanho:** variável; cada segmento tem tamanho diferente e pode mudar durante a execução.
- **Faixa de endereços:** cada segmento é um espaço linear independente, de 0 até o seu tamanho máximo.
- **Conteúdo:** cada segmento guarda uma entidade lógica (código, pilha, vetor, tabela, heap etc.).

**b) Como facilita as estruturas dinâmicas:** cada estrutura fica em seu próprio segmento e pode crescer ou diminuir **independentemente**, sem colidir com outras nem exigir reorganização dos endereços. Facilita também a proteção e o compartilhamento por segmento.

## Questão 3 — Formato dos endereços virtuais

**Formato:** o endereço é bidimensional, composto por **(número do segmento, deslocamento dentro do segmento)**.

**Dificuldades para o programador:** é preciso conhecer a divisão em segmentos e referenciá-los explicitamente (a divisão é visível ao programador/compilador), lidando com dois valores no endereço.

**Na paginação essas dificuldades não existem:** o endereço virtual é linear, um único espaço contínuo. A divisão em número da página e offset é feita pelo hardware (MMU) de forma transparente, apenas como separação de bits, sem relação com a estrutura lógica do programa.

## Questão 4 — Segmentação pura

**a) Fragmentação externa (*checkerboarding*):** como os segmentos têm tamanhos variáveis e são carregados/removidos ao longo do tempo, a memória fica com várias lacunas pequenas entre segmentos. A soma das lacunas pode ser suficiente para um novo segmento, mas nenhuma lacuna individual o comporta (desperdício).

**b) Compactação:** **sim, pode ser usada.** Os segmentos são movidos para uma extremidade da memória, juntando as lacunas em uma só (e atualizando a base de cada segmento na tabela de segmentos). **Inviável** pelo alto custo: copiar grandes volumes de dados consome muito tempo, durante o qual o sistema fica praticamente parado.

**c) Mapa de bits e listas encadeadas:** **sim, podem ser usados.**

- **Mapa de bits:** a memória é dividida em unidades de alocação, com 1 bit por unidade (0 = livre, 1 = ocupada); para um segmento de *k* unidades, procura-se *k* zeros consecutivos.
- **Listas encadeadas:** cada nó descreve um segmento ou lacuna (tipo, início, tamanho); percorre-se a lista procurando uma lacuna que comporte o segmento.

| Critério | Mapa de bits | Listas encadeadas |
|---|---|---|
| **Distinção entre segmentos vizinhos** | **Não distingue** (uma sequência de 1s não mostra onde um segmento termina e o outro começa) — desvantagem | **Distingue** (cada nó tem início e tamanho) — vantagem |
| **Tempo médio para achar lacuna** | **Lento** (varredura bit a bit atrás de *k* zeros seguidos) | **Mais rápido** (percorre nós, e cada lacuna já tem seu tamanho registrado) |

**d) Algoritmos de encaixe:** **sim, podem ser usados** para escolher a lacuna que receberá o segmento trazido do disco:

- **First fit:** primeira lacuna que comporta o segmento.
- **Next fit:** como o first fit, mas começa de onde parou a última busca.
- **Best fit:** menor lacuna que comporta o segmento.
- **Worst fit:** maior lacuna disponível.

| Algoritmo | Tempo para achar a lacuna | Tamanho médio das lacunas geradas |
|---|---|---|
| First fit | Rápido | Intermediário |
| Next fit | Rápido | Intermediário |
| Best fit | Lento (varre toda a lista) | **Menores** (muitas lacunas minúsculas inúteis) |
| Worst fit | Lento (varre toda a lista) | **Maiores** (mas consome as lacunas grandes) |

## Questão 5 — Segmentação com paginação

**a) Por que escolher segmentação com paginação em vez de paginação pura**
Combina as vantagens das duas técnicas:

- **Da segmentação:** espaço lógico organizado em segmentos, estruturas que crescem de forma independente, proteção e compartilhamento por segmento.
- **Da paginação:** elimina a fragmentação externa e a necessidade de compactação e de algoritmos de encaixe, pois todas as páginas têm tamanho fixo. Não é preciso carregar o segmento inteiro (só as páginas necessárias), e segmentos grandes são viáveis.

**b) Organização**

- **Espaço de endereçamento:** formado por vários segmentos, e cada segmento é dividido em páginas de tamanho fixo.
- **Memória física:** dividida em molduras do mesmo tamanho das páginas.
- **Tabelas:** existe uma **tabela de segmentos** e, para cada segmento, uma **tabela de páginas**.
- **Endereço virtual:** **(nº do segmento, nº da página, deslocamento)**.
- **Endereço real:** **(nº da moldura, deslocamento)**.
- **Conversão:** segmento → tabela de segmentos → tabela de páginas do segmento → moldura, e então concatena-se com o offset.

**c) Fragmentação**

- **Interna: ocorre.** A última página de cada segmento pode ficar parcialmente vazia (em média, meia página por segmento).
- **Externa: não ocorre.** As molduras têm tamanho fixo e igual ao das páginas, então qualquer moldura livre serve para qualquer página, sem lacunas inutilizáveis.