# Arrays e ArrayLists em Java

## 1. Array

Estrutura que guarda **vários valores do mesmo tipo** em sequência. Cada posição tem um **índice**, e a contagem **começa em 0**. Arrays são objetos: ficam no heap e a variável guarda uma referência.

### Declaração e criação

```java
int[] notas;                 // declara (só cria a referência)
notas = new int[4];          // cria a array com 4 posições

int[] a = new int[1024];     // declaração + criação
int n = 10;
byte[] b = new byte[n];      // o tamanho pode ser uma variável

int[] c = {7, 8, 10, 6};     // valores iniciais (tamanho = 4)
String[] nomes = {"Ana", "Bruno", "Carla"};
```

Forma alternativa de declarar (estilo C): `int notas[];`

### Valores padrão (ao criar com `new`)

| Tipo | Padrão |
|---|---|
| `int`, `byte`, `short`, `long` | `0` |
| `double`, `float` | `0.0` |
| `boolean` | `false` |
| `char` | `'\u0000'` |
| Objetos (`String`, etc.) | `null` |

### Acesso e tamanho

```java
int[] v = new int[5];
v[0] = 50;                        // escreve no primeiro índice
System.out.println(v[v.length - 1]); // último índice = tamanho - 1
v[5] = 1;                         // ArrayIndexOutOfBoundsException
```

- O tamanho **não pode ser alterado** depois de criada. Para "aumentar", crie outra array e copie (ver `Arrays.copyOf`) ou use `ArrayList`.
- Cuidado com a diferença entre os três:

| Tipo | Tamanho |
|---|---|
| Array | `array.length` (atributo, sem parênteses) |
| `String` | `texto.length()` |
| `ArrayList` | `lista.size()` |

### Percorrendo

```java
int[] notas = {7, 8, 10, 6};

for (int i = 0; i < notas.length; i++) {
    System.out.println("Nota " + i + ": " + notas[i]);
}
```

### For aprimorado (for each)

Percorre todos os elementos sem usar índice. Lê-se: "para cada `nota` em `notas`".

```java
for (int nota : notas) {
    System.out.println(nota);
}
```

Limitação: `nota` é uma **cópia** do valor. Alterá-la não muda a array. Para modificar posições, use o `for` com índice.

```java
for (int i = 0; i < notas.length; i++) {
    notas[i] = notas[i] + 1;   // modifica de verdade
}
```

---

## 2. Array de objetos

Guarda **referências**. Ao criar, todas as posições são `null`; cada objeto precisa de seu próprio `new`.

```java
Funcionario[] equipe = new Funcionario[5];        // 5 posições null
equipe[2] = new Funcionario("Leandro", 874634, 23, 32, 1999, 43232);

equipe[0].getNome();   // NullPointerException (posição 0 ainda é null)
```

---

## 3. Arrays e métodos

O método recebe a **referência** da array, então alterações dentro dele **afetam a array original**.

```java
void modificaArray(double[] b) {
    b[0] = 10.5;
}

double[] array = new double[5];
modificaArray(array);
System.out.println(array[0]);   // 10.5
```

### Varargs (argumentos de comprimento variável)

Permite receber qualquer quantidade de argumentos do mesmo tipo, com `...` após o tipo. Dentro do método, o parâmetro é uma array comum.

```java
double soma(double... numeros) {
    double total = 0;
    for (double n : numeros) total += n;
    return total;
}

soma();               // 0.0
soma(5.0);            // 5.0
soma(1.5, 2.5, 3.0);  // 7.0
```

Regras: apenas **um** varargs por método, e ele deve ser o **último** parâmetro.

---

## 4. Arrays multidimensionais

São "arrays de arrays". A mais comum é a bidimensional (linhas e colunas).

```java
char[][] tabuleiro = new char[8][8];   // 8 linhas, 8 colunas
tabuleiro[5][4] = 'x';                 // linha 5, coluna 4

int[][] matriz = {
    {1, 2, 3},
    {4, 5, 6}
};
```

Percorrendo com dois laços:

```java
for (int i = 0; i < matriz.length; i++) {          // linhas
    for (int j = 0; j < matriz[i].length; j++) {   // colunas da linha i
        System.out.print(matriz[i][j] + " ");
    }
    System.out.println();
}
```

### Arrays irregulares

Cada linha pode ter um número diferente de colunas. Define-se só o número de linhas e cria-se cada linha depois.

```java
int[][] b = new int[3][];   // 3 linhas, sem colunas ainda
b[0] = new int[2];
b[1] = new int[4];
b[2] = new int[1];
```

---

## 5. Classe `Arrays` (`java.util.Arrays`)

Métodos estáticos utilitários para manipular arrays.

```java
import java.util.Arrays;
```

| Método | O que faz |
|---|---|
| `sort` | Ordena em ordem crescente |
| `binarySearch` | Procura um valor e retorna o índice onde está |
| `equals` | Compara o conteúdo de duas arrays |
| `fill` | Preenche a array com um valor |
| `toString` | Converte a array em texto para impressão |
| `copyOf` | Cria uma cópia, com novo tamanho |

### sort

```java
int[] v = {5, 2, 9, 1};
Arrays.sort(v);
System.out.println(Arrays.toString(v));   // [1, 2, 5, 9]
```

### binarySearch

A array **precisa estar ordenada**. Retorna o índice se encontrar; se não encontrar, retorna um número negativo.

```java
int[] v = {1, 2, 5, 9};
Arrays.binarySearch(v, 5);   // 2
Arrays.binarySearch(v, 7);   // negativo (não encontrado)
```

### equals

`==` compara referências; `Arrays.equals` compara o **conteúdo**.

```java
int[] x = {1, 2, 3};
int[] y = {1, 2, 3};
x == y;                  // false (objetos diferentes)
Arrays.equals(x, y);     // true
```

### fill

```java
int[] v = new int[4];
Arrays.fill(v, 7);
System.out.println(Arrays.toString(v));   // [7, 7, 7, 7]
```

### copyOf (redimensionando)

```java
int[] v = {1, 2, 3};
int[] maior = Arrays.copyOf(v, 5);
System.out.println(Arrays.toString(maior));   // [1, 2, 3, 0, 0]
```

Dica: para imprimir arrays bidimensionais, use `Arrays.deepToString(matriz)`.

---

## 6. ArrayList

Lista de **tamanho dinâmico**: cresce e diminui automaticamente. Faz parte de `java.util`.

```java
import java.util.ArrayList;

ArrayList<String> itens = new ArrayList<>();
```

O tipo entre `< >` é obrigatório. Tipos primitivos usam classes wrapper: `ArrayList<Integer>`, `ArrayList<Double>`, `ArrayList<Boolean>`.

### Métodos principais

| Método | O que faz |
|---|---|
| `add(e)` | Adiciona ao final |
| `add(i, e)` | Insere na posição `i` |
| `get(i)` | Retorna o elemento da posição `i` |
| `size()` | Quantidade de elementos |
| `contains(e)` | `true` se o elemento existe na lista |
| `indexOf(e)` | Índice da primeira ocorrência (`-1` se não existir) |
| `remove(i)` / `remove(e)` | Remove por índice ou por objeto |
| `clear()` | Remove todos os elementos |

### Exemplo completo

```java
ArrayList<String> itens = new ArrayList<>();

itens.add("Ana");
itens.add("Bruno");
itens.add("Carla");
itens.add(1, "Daniel");                  // [Ana, Daniel, Bruno, Carla]

System.out.println(itens.get(0));        // Ana
System.out.println(itens.size());        // 4
System.out.println(itens.contains("Bruno")); // true
System.out.println(itens.indexOf("Carla"));  // 3
System.out.println(itens.indexOf("Zé"));     // -1

itens.remove(0);                         // remove por índice -> [Daniel, Bruno, Carla]
itens.remove("Bruno");                   // remove por objeto -> [Daniel, Carla]

itens.clear();
System.out.println(itens.size());        // 0
```

### Percorrendo

```java
for (int i = 0; i < itens.size(); i++) {
    System.out.println(itens.get(i));
}

for (String item : itens) {
    System.out.println(item);
}
```

### Atenção: `remove` em `ArrayList<Integer>`

```java
ArrayList<Integer> nums = new ArrayList<>();
nums.add(10);
nums.add(20);
nums.add(30);

nums.remove(1);                    // remove o ÍNDICE 1 -> [10, 30]
nums.remove(Integer.valueOf(10));  // remove o VALOR 10 -> [30]
```

---

## 7. Array x ArrayList

| | Array | ArrayList |
|---|---|---|
| Tamanho | Fixo | Dinâmico |
| Tamanho atual | `array.length` | `lista.size()` |
| Acesso | `array[i]` | `lista.get(i)` |
| Atribuição | `array[i] = x` | `lista.set(i, x)` |
| Primitivos | Aceita (`int[]`) | Só wrappers (`ArrayList<Integer>`) |

