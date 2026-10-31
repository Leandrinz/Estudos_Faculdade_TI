# Coleções Genéricas em Java: Anotações de Estudo

Material base: slides "Coleções genéricas" (Prof. Ítalo Assis, UFERSA).

Como usar estas anotações:

1. Leia a teoria de cada seção.
2. Tente resolver cada exercício no espaço "Seu espaço" **antes** de olhar a solução.
3. Compare com a solução e entenda as diferenças.

---

## Sumário

1. [Visão geral e o que é uma coleção](#1-visão-geral-e-o-que-é-uma-coleção)
2. [Classes empacotadoras, autoboxing e auto-unboxing](#2-classes-empacotadoras-autoboxing-e-auto-unboxing)
3. [Interface Collection e classe Collections](#3-interface-collection-e-classe-collections)
4. [Listas (List, ArrayList, LinkedList, Vector)](#4-listas)
5. [Pilhas (Stack)](#5-pilhas)
6. [Filas (Queue e PriorityQueue)](#6-filas)
7. [Conjuntos (Set, HashSet, TreeSet)](#7-conjuntos)
8. [Mapas (Map, HashMap, TreeMap)](#8-mapas)
9. [Resumo comparativo final](#9-resumo-comparativo-final)

---

## 1. Visão geral e o que é uma coleção

### Ideia central

Você já conhece o `ArrayList`: uma estrutura de dados do tipo array que **cresce e diminui sozinha** e guarda referências a objetos de um tipo que você define na criação.

```java
ArrayList<String> nomes = new ArrayList<>();
```

O `ArrayList` é só uma das várias estruturas prontas do **framework de coleções** (*Collections Framework*) do Java. Esse framework oferece estruturas genéricas já implementadas, para que você não precise criar listas, pilhas, filas etc. do zero.

### O que é uma coleção?

Uma **coleção** é um objeto que armazena **referências a outros objetos**.

Todas as classes e interfaces do framework ficam no pacote `java.util`.

```java
import java.util.*; // ou importar uma a uma
```

### As interfaces principais

| Interface    | O que representa                                                                 |
|--------------|-----------------------------------------------------------------------------------|
| `Collection` | A interface-raiz da hierarquia. `Set`, `Queue` e `List` derivam dela.             |
| `Set`        | Coleção **sem duplicatas**.                                                       |
| `List`       | Coleção **ordenada** (indexada) que **pode ter duplicatas**.                      |
| `Queue`      | Em geral, "primeiro a entrar, primeiro a sair" (FIFO), modelando uma fila.        |
| `Map`        | Associa **chaves a valores**, sem chaves duplicadas. **Não deriva de `Collection`.** |

### Esquema da hierarquia

```
                 Collection
          /          |          \
        List        Set        Queue
       /  |  \     /    \         |
 ArrayList |  Vector HashSet  TreeSet  PriorityQueue
      LinkedList

 Map  (separado, NÃO é filho de Collection)
  |-- HashMap / Hashtable
  |-- TreeMap
```

### Pontos de atenção

- `Map` **não** é uma `Collection`. Isso cai muito em prova.
- "Ordenada" em `List` significa que os elementos têm **posição (índice)**, e não que estão em ordem alfabética ou numérica.

---

## 2. Classes empacotadoras, autoboxing e auto-unboxing

### O problema

Coleções genéricas **não aceitam tipos primitivos**. Isto não compila:

```java
ArrayList<int> lista = new ArrayList<>();      // ERRO
```

Isso acontece porque tipos primitivos (`int`, `double`...) não são objetos e não têm métodos.

### A solução: classes empacotadoras (wrapper classes)

Cada tipo primitivo tem uma classe correspondente que "embrulha" o valor dentro de um objeto:

| Primitivo | Classe empacotadora |
|-----------|---------------------|
| `boolean` | `Boolean`           |
| `byte`    | `Byte`              |
| `char`    | `Character`         |
| `double`  | `Double`            |
| `float`   | `Float`             |
| `int`     | `Integer`           |
| `long`    | `Long`              |
| `short`   | `Short`             |

```java
ArrayList<Integer> lista = new ArrayList<>();   // OK
```

Essas classes também guardam **métodos úteis** relacionados ao tipo. Exemplo: `Integer.parseInt("42")` converte uma `String` em `int`.

### Autoboxing e auto-unboxing

O Java faz a conversão entre primitivo e objeto **automaticamente**:

- **Autoboxing**: primitivo → objeto empacotador
- **Auto-unboxing**: objeto empacotador → primitivo

```java
Integer[] integerArray = new Integer[5];
integerArray[0] = 10;            // autoboxing: int 10 vira Integer
int value = integerArray[0];     // auto-unboxing: Integer vira int
```

### Dica de estudo

Por causa do autoboxing, você escreve `lista.add(10)` normalmente, mesmo a lista sendo de `Integer`. Por baixo dos panos o Java faz `lista.add(Integer.valueOf(10))`.

### Cuidado: `null` no unboxing

Se um `Integer` for `null` e o Java tentar fazer o unboxing, ocorre `NullPointerException`.

```java
Integer x = null;
int y = x; // NullPointerException
```

---

## 3. Interface Collection e classe Collections

Atenção ao nome: são **duas coisas diferentes**.

| Nome          | O que é                | Para que serve                                                       |
|---------------|------------------------|-----------------------------------------------------------------------|
| `Collection`  | **interface**          | Define operações comuns a quase todas as coleções.                    |
| `Collections` | **classe utilitária**  | Traz métodos `static` para pesquisar, classificar e manipular coleções. |

### 3.1 Interface `Collection`

Operações que valem para a coleção inteira:

- adicionar elementos (`add`)
- limpar (`clear`)
- comparar objetos
- saber o tamanho (`size`)
- saber se está vazia (`isEmpty`)
- converter em array (`toArray`)
- obter um **`Iterator`** (`iterator`)

### 3.2 O `Iterator`

O `Iterator` permite **percorrer** a coleção e, o mais importante, **remover elementos durante a iteração** de forma segura.

Métodos principais:

| Método      | O que faz                                          |
|-------------|----------------------------------------------------|
| `hasNext()` | Retorna `true` se ainda há próximo elemento.       |
| `next()`    | Retorna o próximo elemento e avança.               |
| `remove()`  | Remove o **último** elemento retornado por `next()`. |

#### Exemplo dos slides, explicado

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        ArrayList<Double> notas = new ArrayList<>(Arrays.asList(5.5, 6.5, 7.5, 8.5));
        Iterator<Double> iterator = notas.iterator();

        imprimeCollection(notas);                 // 5.5 6.5 7.5 8.5

        while (iterator.hasNext()) {
            if (iterator.next() < 7.0) {          // auto-unboxing do Double
                iterator.remove();                // remove o que next() acabou de devolver
            }
        }

        imprimeCollection(notas);                 // 7.5 8.5
    }

    // Aceita QUALQUER Collection<Double> (ArrayList, LinkedList, HashSet...)
    private static void imprimeCollection(Collection<Double> collec) {
        for (Double elemento : collec) {
            System.out.print(elemento + " ");
        }
        System.out.println();
    }
}
```

O que observar:

1. `Arrays.asList(...)` cria uma lista a partir dos valores. Os `double` viram `Double` por autoboxing.
2. O parâmetro de `imprimeCollection` é do tipo **`Collection`** (a interface). Por isso o método funciona com qualquer coleção. Isso é **programar para a interface**.
3. `iterator.remove()` é a forma correta de remover durante um percurso.

#### Por que não usar `lista.remove()` dentro de um `for-each`?

```java
for (Double n : notas) {
    if (n < 7.0) notas.remove(n);   // ConcurrentModificationException
}
```

Modificar a coleção diretamente enquanto ela é percorrida causa `ConcurrentModificationException`. O `Iterator.remove()` evita esse problema.

### 3.3 Classe `Collections` (métodos estáticos)

| Método       | O que faz                                                          | Aplica-se a |
|--------------|---------------------------------------------------------------------|-------------|
| `sort`       | Classifica (ordena) os elementos                                    | `List`      |
| `binarySearch` | Localiza um objeto usando **pesquisa binária**                    | `List`      |
| `reverse`    | Inverte a ordem dos elementos                                       | `List`      |
| `shuffle`    | Embaralha os elementos aleatoriamente                               | `List`      |
| `fill`       | Faz todos os elementos referirem-se a um objeto especificado        | `List`      |
| `copy`       | Copia referências de uma lista para outra                           | `List`      |
| `min`        | Retorna o menor elemento                                            | `Collection`|
| `max`        | Retorna o maior elemento                                            | `Collection`|
| `addAll`     | Acrescenta todos os elementos de um array a uma coleção             | `Collection`|
| `frequency`  | Conta quantos elementos são iguais ao elemento especificado         | `Collection`|
| `disjoint`   | Informa se duas coleções **não têm nenhum elemento em comum**       | `Collection`|

#### Exemplos rápidos

```java
List<Integer> nums = new ArrayList<>(Arrays.asList(5, 2, 9, 2, 7));

Collections.sort(nums);                          // [2, 2, 5, 7, 9]
Collections.reverse(nums);                       // [9, 7, 5, 2, 2]
Collections.shuffle(nums);                       // ordem aleatória
int menor = Collections.min(nums);               // 2
int maior = Collections.max(nums);               // 9
int vezes = Collections.frequency(nums, 2);      // 2
boolean semComum = Collections.disjoint(nums, List.of(100, 200)); // true
```

### 3.4 Pesquisa binária (`binarySearch`): pontos importantes

- A lista **precisa estar ordenada** antes. Se não estiver, o resultado é imprevisível.
- Se encontra o elemento, retorna o **índice** dele.
- Se **não** encontra, retorna um **número negativo**: `-(posição de inserção) - 1`.

Por isso, o teste correto para "encontrou" é `resultado >= 0`.

```java
List<String> cores = Arrays.asList("amarelo", "azul", "verde");
Collections.binarySearch(cores, "azul");     // 1
Collections.binarySearch(cores, "preto");    // negativo (não existe)
```

---

### Exercício 1: Busca em ArrayList ordenada

**Enunciado (slide 11):**

- Crie um método que recebe uma `ArrayList` de `String` ordenada e uma `String` alvo, e informa se a `String` alvo está na `ArrayList` e em qual posição.
- No método principal:
  - Crie uma `ArrayList` com nomes de cores **não ordenados** alfabeticamente e a imprima.
  - Ordene e imprima novamente.
  - Teste o método criado para diferentes cores.



#### Solução comentada

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    // Recebe uma lista JÁ ORDENADA e a String alvo
    public static void buscarCor(ArrayList<String> listaOrdenada, String alvo) {
        int posicao = Collections.binarySearch(listaOrdenada, alvo);

        if (posicao >= 0) {
            System.out.println("A cor \"" + alvo + "\" esta na posicao " + posicao);
        } else {
            System.out.println("A cor \"" + alvo + "\" NAO esta na lista.");
        }
    }

    public static void main(String[] args) {
        // Lista fora de ordem alfabetica
        ArrayList<String> cores = new ArrayList<>(
            Arrays.asList("vermelho", "azul", "verde", "amarelo", "roxo", "laranja")
        );

        System.out.println("Antes de ordenar: " + cores);

        Collections.sort(cores);   // ordena em ordem alfabetica

        System.out.println("Depois de ordenar: " + cores);

        // Testes
        buscarCor(cores, "azul");      // existe
        buscarCor(cores, "vermelho");  // existe (ultima posicao)
        buscarCor(cores, "preto");     // nao existe
        buscarCor(cores, "amarelo");   // existe (primeira posicao)
    }
}
```

**Saída esperada:**

```
Antes de ordenar: [vermelho, azul, verde, amarelo, roxo, laranja]
Depois de ordenar: [amarelo, azul, laranja, roxo, verde, vermelho]
A cor "azul" esta na posicao 1
A cor "vermelho" esta na posicao 5
A cor "preto" NAO esta na lista.
A cor "amarelo" esta na posicao 0
```

**Pontos para fixar:**

- Sem o `Collections.sort`, o `binarySearch` poderia dar resultados errados.
- `posicao >= 0` significa "encontrou". Não teste `== -1`, pois o valor negativo varia.
- Mantive tudo em minúsculas porque a comparação de `String` diferencia maiúsculas de minúsculas (`"Azul"` vem **antes** de `"amarelo"` na ordem natural, pois letras maiúsculas têm código menor).

---

## 4. Listas

### Características de uma `List`

- É uma `Collection` **indexada**: cada elemento tem um índice.
- **Aceita elementos duplicados**.
- O índice do primeiro elemento é **zero**.

### O que a interface `List` oferece

- Manipular elementos por **índice** (`get(i)`, `set(i, x)`, `add(i, x)`, `remove(i)`)
- Manipular um **intervalo** de elementos (`subList(de, ate)`)
- **Procurar** elementos (`indexOf`, `contains`)
- Obter um **`ListIterator`** (iterador que anda para frente **e para trás**)

### Implementações da interface `List`

`ArrayList`, `LinkedList` e `Vector`.

### 4.1 `ArrayList` e `Vector`

- São implementações baseadas em **arrays redimensionáveis**.
- Têm comportamento **praticamente idêntico**.
- **Inserir no meio** é uma operação **ineficiente**: os elementos seguintes precisam ser deslocados uma posição.
- **Acessar por índice é muito rápido**, porque é só calcular a posição no array.

Sobre o `Vector`:

- Existe desde o **Java 1.0**, antes mesmo do framework de coleções.
- Possui alguns métodos que **não fazem parte** da interface `List`.

### 4.2 `LinkedList`

- Implementada como **lista encadeada**: cada elemento aponta para o próximo (e para o anterior).
- **Inserir ou remover no meio é eficiente**: basta ajustar os "ponteiros".
- **Pular para um elemento específico é menos eficiente** que no `ArrayList`: precisa percorrer a lista até chegar lá.

### Comparativo

| Operação                       | `ArrayList` / `Vector` | `LinkedList` |
|--------------------------------|------------------------|--------------|
| Acessar por índice (`get(i)`)  | Rápido                 | Lento        |
| Inserir/remover no meio        | Lento                  | Rápido       |
| Inserir/remover no fim         | Rápido                 | Rápido       |

### Regra prática de escolha

- Muitas **leituras por posição** → `ArrayList`
- Muitas **inserções/remoções no meio** → `LinkedList`

### Exemplo de uso

```java
List<String> frutas = new ArrayList<>();
frutas.add("maca");
frutas.add("banana");
frutas.add("maca");          // duplicata permitida
frutas.add(1, "uva");        // insere na posicao 1
System.out.println(frutas);  // [maca, uva, banana, maca]
System.out.println(frutas.get(2));       // banana
System.out.println(frutas.indexOf("maca")); // 0
```

Dica: declare a variável com o tipo da **interface** (`List<String>`) e instancie a classe concreta (`new ArrayList<>()`). Assim você troca a implementação sem mexer no resto do código.

---

### Exercício 2: Remover de uma LinkedList os elementos de outra

**Enunciado (slide 14):**

- Crie um método que recebe duas `LinkedList` de `String` e, utilizando um `Iterator`, remove da **primeira** os elementos que aparecem na **segunda**.
- No método principal:
  - Crie duas `LinkedList` de `String`, cada uma com uma lista de cores.
  - Use o método para remover da primeira as cores que aparecem na segunda.
  - Imprima a primeira lista **antes e depois** das exclusões.





#### Solução comentada

```java
import java.util.Iterator;
import java.util.LinkedList;

public class Main {

    public static void removerComuns(LinkedList<String> primeira, LinkedList<String> segunda) {
        Iterator<String> it = primeira.iterator();

        while (it.hasNext()) {
            String cor = it.next();          // pega o proximo elemento da primeira
            if (segunda.contains(cor)) {     // aparece na segunda?
                it.remove();                 // remove da primeira (via iterator)
            }
        }
    }

    public static void main(String[] args) {
        LinkedList<String> primeira = new LinkedList<>();
        primeira.add("vermelho");
        primeira.add("azul");
        primeira.add("verde");
        primeira.add("amarelo");
        primeira.add("roxo");

        LinkedList<String> segunda = new LinkedList<>();
        segunda.add("azul");
        segunda.add("amarelo");
        segunda.add("preto");

        System.out.println("Antes:  " + primeira);

        removerComuns(primeira, segunda);

        System.out.println("Depois: " + primeira);
    }
}
```

**Saída esperada:**

```
Antes:  [vermelho, azul, verde, amarelo, roxo]
Depois: [vermelho, verde, roxo]
```

**Pontos para fixar:**

- Chamamos `it.next()` **uma vez** por volta do laço e guardamos em variável. Se chamássemos `next()` duas vezes, pularíamos elementos.
- `it.remove()` só pode ser chamado **depois** de um `next()`.
- `"preto"` não existia na primeira lista, então não houve problema: só são removidos os que aparecem em **ambas**.
- Alternativa sem iterator: `primeira.removeAll(segunda)`. O exercício exige o `Iterator` para você praticar o conceito.

---

## 5. Pilhas

### Conceito

Uma **pilha** segue a regra **LIFO** (*Last In, First Out*): o **último** a entrar é o **primeiro** a sair. Pense em uma pilha de pratos: você coloca e retira sempre pelo topo.

```
   push(x)  -->  [ topo ]  -->  pop()
                 [  ...  ]
                 [ base  ]
```

### A classe `Stack`

- `Stack` **estende `Vector`** para implementar a estrutura de pilha.

| Método      | O que faz                                                          |
|-------------|--------------------------------------------------------------------|
| `push(x)`   | Adiciona `x` no **topo** da pilha.                                 |
| `pop()`     | **Remove** e retorna o elemento do topo.                           |
| `peek()`    | Retorna o elemento do topo **sem remover**.                        |
| `isEmpty()` | Herdado de `Vector`: informa se a pilha está vazia.                |

### Exemplo

```java
Stack<String> pilha = new Stack<>();
pilha.push("A");
pilha.push("B");
pilha.push("C");
System.out.println(pilha);        // [A, B, C]  (C esta no topo)
System.out.println(pilha.peek()); // C  (nao remove)
System.out.println(pilha.pop());  // C  (remove)
System.out.println(pilha);        // [A, B]
```

### Cuidados

- Chamar `pop()` ou `peek()` em uma pilha **vazia** lança `EmptyStackException`. Use `isEmpty()` antes.
- Ao imprimir uma `Stack`, o **último** da lista é o **topo**.

### Observação extra (além dos slides)

Como `Stack` herda de `Vector`, ela carrega métodos que não fazem sentido para uma pilha (como `get(i)`). Em código novo, a documentação do Java recomenda usar `Deque` (por exemplo `ArrayDeque`) no lugar de `Stack`. Para a disciplina, siga o que o professor ensina: `Stack`.

---

### Exercício 3: Pilha de números de tipos variados

**Enunciado (slide 16):**

- Crie um programa com uma pilha que armazena elementos de **qualquer tipo numérico**.
- Adicione elementos de tipos numéricos variados.
- Remova os elementos um a um até esvaziar a pilha.
- Após cada alteração, imprima a pilha.

#### Dica de raciocínio

Qual é a classe pai de `Integer`, `Double`, `Float`, `Long` etc.? É a classe **`Number`**. Uma `Stack<Number>` aceita qualquer um deles.


#### Solução comentada

```java
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Stack<Number> pilha = new Stack<>();

        // Adicionando (autoboxing em cada literal)
        pilha.push(10);        // Integer
        System.out.println("push(10)     -> " + pilha);

        pilha.push(3.14);      // Double
        System.out.println("push(3.14)   -> " + pilha);

        pilha.push(2.5f);      // Float
        System.out.println("push(2.5f)   -> " + pilha);

        pilha.push(100L);      // Long
        System.out.println("push(100L)   -> " + pilha);

        // Removendo ate esvaziar
        while (!pilha.isEmpty()) {
            Number removido = pilha.pop();
            System.out.println("pop() = " + removido + " -> " + pilha);
        }
    }
}
```

**Saída esperada:**

```
push(10)     -> [10]
push(3.14)   -> [10, 3.14]
push(2.5f)   -> [10, 3.14, 2.5]
push(100L)   -> [10, 3.14, 2.5, 100]
pop() = 100 -> [10, 3.14, 2.5]
pop() = 2.5 -> [10, 3.14]
pop() = 3.14 -> [10]
pop() = 10 -> []
```

**Pontos para fixar:**

- Os elementos saem na ordem **inversa** da entrada (LIFO).
- `Stack<Number>` funciona graças ao **polimorfismo**: `Integer`, `Double` etc. são `Number`.
- O laço `while (!pilha.isEmpty())` é o padrão para esvaziar uma pilha com segurança.
- Ao retirar um `Number`, só podemos usar os métodos de `Number` (como `intValue()`, `doubleValue()`), a menos que façamos um cast.

---

## 6. Filas

### Conceito

Uma **fila** segue, em geral, a regra **FIFO** (*First In, First Out*): o primeiro a entrar é o primeiro a sair, como uma fila de banco.

```
 Fim  [ E ][ D ][ C ][ B ][ A ]  Inicio
 (entrada)                        (saida)
```

### A interface `Queue`

- `Queue` **estende `Collection`**.
- Acrescenta operações de **inserção, remoção e inspeção** próprias de fila.

| Método    | O que faz                                                        |
|-----------|------------------------------------------------------------------|
| `offer(x)`| **Insere** `x` na fila.                                          |
| `poll()`  | **Remove** e retorna o elemento do início (ou `null` se vazia).  |
| `peek()`  | Retorna o elemento do início **sem remover** (ou `null` se vazia).|
| `clear()` | Remove todos os elementos.                                       |
| `size()`  | Retorna a quantidade de elementos.                               |

### `PriorityQueue` (fila de prioridades)

- Implementa `Queue`.
- **Não** segue FIFO puro: ordena os elementos pela **ordem natural**.
- O elemento de **maior prioridade** é o de **menor valor**. Ele é o **primeiro a ser removido**.

Ou seja, em uma `PriorityQueue<Integer>` com `5, 1, 8, 3`, o `poll()` retorna `1`, depois `3`, depois `5`, depois `8`.

### Exemplo

```java
Queue<Integer> fila = new PriorityQueue<>();
fila.offer(5);
fila.offer(1);
fila.offer(8);
fila.offer(3);

System.out.println(fila.peek()); // 1  (menor valor = maior prioridade)
System.out.println(fila.poll()); // 1
System.out.println(fila.poll()); // 3
System.out.println(fila.size()); // 2
```

### Cuidado importante

Ao **imprimir** uma `PriorityQueue` com `System.out.println(fila)`, a ordem exibida **não é necessariamente a ordem de remoção**. Internamente ela usa uma estrutura de **heap**, que garante apenas que o menor elemento fica na frente. A ordem correta só aparece quando você vai usando `poll()`.

### Observação extra

`LinkedList` também implementa `Queue`. É a forma mais comum de ter uma fila FIFO "comum" em Java.

---

### Exercício 4: Fila de prioridades de Doubles

**Enunciado (slide 18):**

- Crie um programa que contém uma fila de prioridades de `Double`.
- Adicione elementos a esta fila.
- Remova e apresente os elementos um a um até zerar a fila.

#### Seu espaço

```java
// Escreva aqui a sua solução:



```

#### Solução comentada

```java
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<Double> fila = new PriorityQueue<>();

        // Insercao fora de ordem
        fila.offer(7.5);
        fila.offer(2.3);
        fila.offer(9.1);
        fila.offer(4.8);
        fila.offer(1.2);

        System.out.println("Tamanho inicial: " + fila.size());
        System.out.println("Proximo a sair (peek): " + fila.peek());

        System.out.println("Removendo:");
        while (!fila.isEmpty()) {
            Double removido = fila.poll();
            System.out.println(removido);
        }

        System.out.println("Tamanho final: " + fila.size());
    }
}
```

**Saída esperada:**

```
Tamanho inicial: 5
Proximo a sair (peek): 1.2
Removendo:
1.2
2.3
4.8
7.5
9.1
Tamanho final: 0
```

**Pontos para fixar:**

- Mesmo inserindo fora de ordem, a remoção sai **do menor para o maior**.
- `poll()` remove; `peek()` apenas consulta.
- Como `isEmpty()` vem de `Collection`, ele funciona na fila também.

---

## 7. Conjuntos

### Características de um `Set`

- É uma `Collection` **sem elementos duplicados**.
- Em geral **não ordenado** (não há índice nem posição garantida).
- Tentar adicionar um elemento que já existe **não altera** o conjunto: `add` retorna `false` e nada acontece.

### Principais implementações

| Classe    | Como armazena         | Características                                      |
|-----------|-----------------------|------------------------------------------------------|
| `HashSet` | **Tabela de hash**    | Muito rápido; **sem ordem** garantida.               |
| `TreeSet` | **Árvore**            | Mantém os elementos **ordenados** (ordem natural).   |

### `SortedSet`

- `SortedSet` é uma **interface** cujos elementos são mantidos **ordenados**.
- A classe **`TreeSet` implementa `SortedSet`**.

Métodos úteis:

| Método          | O que retorna                                                                  |
|-----------------|--------------------------------------------------------------------------------|
| `headSet(x)`    | Elementos **menores** que `x` (`x` **não** entra).                             |
| `tailSet(x)`    | Elementos **maiores ou iguais** a `x` (`x` **entra**, se existir).             |
| `first()`       | O **primeiro** (menor) elemento.                                               |
| `last()`        | O **último** (maior) elemento.                                                 |

### Exemplo

```java
TreeSet<Integer> nums = new TreeSet<>();
nums.add(30);
nums.add(10);
nums.add(20);
nums.add(10);                       // duplicata: ignorada

System.out.println(nums);           // [10, 20, 30]  (ordenado, sem repetir)
System.out.println(nums.headSet(20)); // [10]
System.out.println(nums.tailSet(20)); // [20, 30]
System.out.println(nums.first());   // 10
System.out.println(nums.last());    // 30
```

### Dica de memória

- `head` = "cabeça" = o que vem **antes** (exclui o valor dado).
- `tail` = "cauda" = o que vem **a partir** do valor (inclui o valor dado).

### Observação

Para o `TreeSet` ordenar objetos próprios, a classe deles deve implementar `Comparable` (ou você passa um `Comparator`). Para `String`, `Integer` etc., a ordem natural já está pronta.

---

### Exercício 5: Conjunto de cores com TreeSet

**Enunciado (slide 20):**

- Escreva um programa que crie e inicialize um conjunto de cores utilizando um `TreeSet`.
  - Experimente inserir a **mesma cor mais de uma vez**.
- Imprima o conjunto criado.
- Escolha uma cor e, considerando a ordem alfabética, apresente:
  - uma lista com as cores que vêm **antes** da cor selecionada
  - uma lista das cores **a partir** da cor selecionada
  - o **primeiro e o último** elementos do conjunto


#### Solução comentada

```java
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        TreeSet<String> cores = new TreeSet<>();

        cores.add("Verde");
        cores.add("Azul");
        cores.add("Amarelo");
        cores.add("Vermelho");
        cores.add("Azul");        // repetida: sera ignorada
        cores.add("Roxo");
        cores.add("Laranja");

        System.out.println("Conjunto: " + cores);

        String escolhida = "Roxo";
        System.out.println("Cor escolhida: " + escolhida);

        System.out.println("Antes de " + escolhida + ": " + cores.headSet(escolhida));
        System.out.println("A partir de " + escolhida + ": " + cores.tailSet(escolhida));

        System.out.println("Primeiro: " + cores.first());
        System.out.println("Ultimo: " + cores.last());
    }
}
```

**Saída esperada:**

```
Conjunto: [Amarelo, Azul, Laranja, Roxo, Verde, Vermelho]
Cor escolhida: Roxo
Antes de Roxo: [Amarelo, Azul, Laranja]
A partir de Roxo: [Roxo, Verde, Vermelho]
Primeiro: Amarelo
Ultimo: Vermelho
```

**Pontos para fixar:**

- O `"Azul"` inserido duas vezes aparece **uma só vez**: é um `Set`.
- A saída já vem em ordem alfabética porque é um `TreeSet`. Se fosse `HashSet`, a ordem seria imprevisível e `headSet`/`tailSet` nem existiriam.
- `headSet("Roxo")` **não** inclui `"Roxo"`; `tailSet("Roxo")` **inclui**.
- Começar todas as cores com maiúscula evita problemas de comparação entre maiúsculas e minúsculas.

---

## 8. Mapas

### Conceito

Um `Map` **associa chaves a valores**, como um dicionário ou uma agenda: você busca pelo **nome** (chave) e obtém o **telefone** (valor).

```
 CHAVE      VALOR
 "Jan"  ->  327.2
 "Fev"  ->  368.2
 "Mar"  ->  197.6
```

### Regras

- As **chaves são únicas**: não pode haver duas chaves iguais.
- Os **valores podem se repetir**.
- Se você usar `put` com uma chave que já existe, o valor antigo é **substituído**.
- Lembre: `Map` **não** deriva de `Collection`.

### Principais implementações

| Classe / Interface | Como armazena      | Características                                         |
|--------------------|--------------------|---------------------------------------------------------|
| `HashMap`          | Tabela de hash     | Rápido; chaves **sem ordem** garantida.                 |
| `Hashtable`        | Tabela de hash     | Versão mais antiga (Java 1.0).                          |
| `SortedMap`        | (interface)        | Mantém as **chaves ordenadas**.                         |
| `TreeMap`          | Árvore             | Implementa `SortedMap`: chaves **ordenadas**.           |

### Principais métodos

| Método                 | O que faz                                                          |
|------------------------|--------------------------------------------------------------------|
| `put(chave, valor)`    | Insere uma entrada no mapa (ou substitui o valor da chave existente). |
| `get(chave)`           | Retorna o valor associado à chave (ou `null` se não existir).      |
| `containsKey(chave)`   | `true` se a chave existe no mapa; `false` caso contrário.          |
| `keySet()`             | Retorna um **conjunto** (`Set`) com todas as chaves.               |

Outros úteis (além dos slides): `size()`, `remove(chave)`, `values()`, `containsValue(v)`, `getOrDefault(chave, padrao)`.

### Exemplo

```java
Map<String, Integer> idades = new TreeMap<>();
idades.put("Carlos", 30);
idades.put("Ana", 25);
idades.put("Bruno", 30);     // valor repetido: permitido
idades.put("Ana", 26);       // chave repetida: substitui 25 por 26

System.out.println(idades);                       // {Ana=26, Bruno=30, Carlos=30}
System.out.println(idades.get("Bruno"));          // 30
System.out.println(idades.containsKey("Diana"));  // false

for (String nome : idades.keySet()) {
    System.out.println(nome + " -> " + idades.get(nome));
}
```

### Padrão clássico: contador de ocorrências

Este é o padrão do próximo exercício:

```
para cada item:
    se a chave JA existe no mapa:
        valor = valor antigo + 1
    senao:
        valor = 1
```

Em código:

```java
if (mapa.containsKey(item)) {
    mapa.put(item, mapa.get(item) + 1);
} else {
    mapa.put(item, 1);
}
```

Perceba o **autoboxing e unboxing** em ação: `mapa.get(item) + 1` faz unboxing de `Integer` para `int`, e o `put` faz boxing do resultado de volta.

### Observação extra

`HashMap` aceita chave e valor `null`; `Hashtable` **não** aceita. `Hashtable` também é sincronizada (segura para múltiplas threads), por isso é mais lenta. Em código novo, o comum é usar `HashMap`.

---

### Exercício 6: Contar palavras com TreeMap

**Enunciado (slide 23):**

- Escreva um programa que utiliza um `TreeMap` para **contar e exibir** o número de ocorrências de cada palavra em uma `String`.
  - Considere como **iguais** palavras que se diferenciam apenas por letras maiúsculas/minúsculas.
  - Exiba as palavras em **ordem alfabética**.

#### Dicas de raciocínio

1. Converta o texto para minúsculas (`toLowerCase()`) para tratar `"O"` e `"o"` como iguais.
2. Separe o texto em palavras (`split`).
3. Para cada palavra, aplique o padrão contador.
4. Como é `TreeMap`, percorrer o `keySet()` já entrega as chaves em ordem alfabética.


#### Solução comentada

```java
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        String texto = "O gato viu o rato. O rato fugiu do gato!";

        TreeMap<String, Integer> contagem = new TreeMap<>();

        // 1) tudo minusculo; 2) separa por qualquer coisa que nao seja letra
        String[] palavras = texto.toLowerCase().split("[^\\p{L}]+");

        for (String palavra : palavras) {
            if (palavra.isEmpty()) {
                continue;                 // ignora pedacos vazios
            }

            if (contagem.containsKey(palavra)) {
                contagem.put(palavra, contagem.get(palavra) + 1);
            } else {
                contagem.put(palavra, 1);
            }
        }

        // TreeMap: keySet() ja vem em ordem alfabetica
        for (String palavra : contagem.keySet()) {
            System.out.println(palavra + ": " + contagem.get(palavra));
        }
    }
}
```

**Saída esperada:**

```
do: 1
fugiu: 1
gato: 2
o: 3
rato: 2
viu: 1
```

**Pontos para fixar:**

- `toLowerCase()` garante que `"O"` e `"o"` contem como a mesma palavra.
- A expressão `"[^\\p{L}]+"` significa "um ou mais caracteres que **não** sejam letras". Isso separa por espaços e pontuação (e funciona com acentos, como em "não" ou "você").
- O `if (palavra.isEmpty())` evita contar uma "palavra vazia", que pode surgir se o texto começar com pontuação.
- Se você usasse `HashMap`, a ordem de saída não seria alfabética. A escolha do `TreeMap` é o que resolve o requisito "ordem alfabética".
- Versão mais curta, com `getOrDefault`:

```java
contagem.put(palavra, contagem.getOrDefault(palavra, 0) + 1);
```

---

## 9. Resumo comparativo final

### Qual estrutura usar?

| Estrutura        | Ordem                      | Duplicatas | Acesso por índice | Característica marcante                          |
|------------------|----------------------------|------------|-------------------|--------------------------------------------------|
| `ArrayList`      | Ordem de inserção          | Sim        | Sim (rápido)      | Array redimensionável                            |
| `Vector`         | Ordem de inserção          | Sim        | Sim               | Parecido com `ArrayList`, mais antigo            |
| `LinkedList`     | Ordem de inserção          | Sim        | Sim (lento)       | Inserção/remoção no meio eficientes              |
| `Stack`          | LIFO                       | Sim        | (herdado)         | `push`, `pop`, `peek`                            |
| `PriorityQueue`  | Por prioridade (menor 1o)  | Sim        | Não               | `offer`, `poll`, `peek`                          |
| `HashSet`        | Sem ordem                  | **Não**    | Não               | Tabela de hash                                   |
| `TreeSet`        | **Ordenado**               | **Não**    | Não               | `headSet`, `tailSet`, `first`, `last`            |
| `HashMap`        | Chaves sem ordem           | Valores sim, chaves **não** | Não | Chave -> valor, tabela de hash           |
| `TreeMap`        | Chaves **ordenadas**       | Valores sim, chaves **não** | Não | Chave -> valor, árvore                   |

### Frases-chave para memorizar

- `Collection` é interface; `Collections` é classe com métodos estáticos.
- `Map` **não** herda de `Collection`.
- Coleções genéricas só guardam **objetos**; tipos primitivos usam **classes empacotadoras**.
- **Autoboxing**: primitivo -> objeto. **Auto-unboxing**: objeto -> primitivo.
- `Iterator.remove()` é a forma segura de remover durante a iteração.
- `binarySearch` exige lista **ordenada** e retorna negativo quando não encontra.
- `Stack` = LIFO; `Queue` = FIFO; `PriorityQueue` = menor valor sai primeiro.
- `Set` não aceita duplicatas; `TreeSet` mantém tudo ordenado.
- `headSet` exclui o valor; `tailSet` inclui o valor.
- Em `Map`, chaves são únicas; valores podem repetir.

### Perguntas para se testar (responda sem olhar)

1. Qual a diferença entre `Collection` e `Collections`?
2. Por que `ArrayList<int>` não compila e como resolver?
3. O que acontece ao chamar `put` com uma chave que já existe no `Map`?
4. Quando `LinkedList` é melhor que `ArrayList`? E quando é pior?
5. Por que o `for-each` com `lista.remove()` dá erro e o `Iterator` não?
6. Em uma `PriorityQueue<Integer>` com 4, 9, 1, 6, qual a ordem de saída com `poll()`?
7. Qual a diferença entre `headSet(x)` e `tailSet(x)`?
8. Por que `PriorityQueue` impressa com `println` pode parecer fora de ordem?
9. Qual implementação de `Map` você usaria para listar as chaves em ordem alfabética?
10. O que `Collections.binarySearch` retorna quando o elemento não existe?

### Respostas das perguntas

1. `Collection` é a interface-raiz das coleções; `Collections` é uma classe utilitária com métodos estáticos (`sort`, `max`, `reverse`...).
2. Coleções genéricas não aceitam primitivos. Usa-se a classe empacotadora: `ArrayList<Integer>`.
3. O valor antigo é substituído pelo novo (chaves são únicas).
4. É melhor para inserir/remover no meio; é pior para acessar um elemento por índice.
5. Alterar a coleção diretamente durante a iteração gera `ConcurrentModificationException`; o `Iterator.remove()` é a forma segura.
6. `1, 4, 6, 9`.
7. `headSet(x)` retorna os elementos menores que `x` (exclui `x`); `tailSet(x)` retorna os maiores ou iguais a `x` (inclui `x`).
8. Porque internamente é um heap, que só garante o menor elemento na frente; a ordem completa aparece apenas com `poll()`.
9. `TreeMap`.
10. Um número negativo, igual a `-(posição de inserção) - 1`.

---

