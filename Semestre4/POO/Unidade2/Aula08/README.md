# Arrays e ArrayLists

## O que é uma array
Uma array é uma estrutura que guarda **vários valores do mesmo tipo** em sequência na memória. Cada valor fica em uma posição, chamada de **índice**, e a contagem dos índices **começa em 0**.

> Arrays são objetos: ficam no heap e a variável guarda uma referência para eles.

## Como declarar

```java
tipo[] nomeDaReferencia;   // forma mais usada
tipo nomeDaReferencia[];   // forma alternativa (estilo C)
```

Exemplo:

```java
char[] letrasDoAlfabeto;
```

> Declarar não cria a array. Isso só cria uma referência. É preciso **inicializar** a array com `new` (ou com valores literais) antes de usá-la.

## Arrays unidimensionais

### Criando com new

```java
int[] posicoesDeMemoria = new int[1024];

int quantidadeNecessaria = 23324;
byte[] vetorNumerico = new byte[quantidadeNecessaria]; // o tamanho pode ser uma variável
```

### Valores padrão
Ao criar com `new`, todas as posições recebem um valor padrão:

| Tipo | Valor padrão |
|---|---|
| `int`, `byte`, `short`, `long` | `0` |
| `double`, `float` | `0.0` |
| `boolean` | `false` |
| `char` | `'\u0000'` (caractere nulo) |
| Objetos (`String`, `Funcionario`...) | `null` |

### Criando com valores iniciais

```java
int[] notas = {7, 8, 10, 6};
String[] nomes = {"Ana", "Bruno", "Carla"};
```

O tamanho é definido automaticamente pela quantidade de valores.

### Acessando a array

```java
int[] posicoesDeMemoria = new int[1024];

posicoesDeMemoria[0] = 50;                    // escrevendo no primeiro índice
System.out.println(posicoesDeMemoria[0]);     // primeiro índice
System.out.println(posicoesDeMemoria[1023]);  // último índice (tamanho - 1)
```

> Se o índice não existir (por exemplo, `posicoesDeMemoria[1024]`), o Java lança uma `ArrayIndexOutOfBoundsException` em tempo de execução.

> Não conseguimos redimensionar uma array já criada. Se precisar de mais espaço, é preciso criar uma nova array e copiar os valores (ou usar um `ArrayList`, visto mais abaixo).

### length
Para pegar o tamanho de uma array:

```java
array.length;   // atenção: sem parênteses, é um atributo
```

Note a diferença: em arrays é `length`, em `String` é `length()` e em `ArrayList` é `size()`.

> Muito usado em laços de repetição:

```java
int[] notas = {7, 8, 10, 6};

for (int i = 0; i < notas.length; i++) {
    System.out.println("Nota " + i + ": " + notas[i]);
}
```

## Arrays de instâncias de classe

Uma array de objetos guarda **referências**. Ao criá-la, todas as posições começam como `null`, e cada objeto precisa ser criado com `new` separadamente.

```java
Funcionario[] equipe = new Funcionario[5];   // 5 posições, todas null
equipe[2] = new Funcionario("Leandro", 874634, 23, 32, 1999, 43232);
```

> Acessar um método em uma posição ainda `null` causa `NullPointerException`.

## Passando array para métodos

Como arrays são objetos, o método recebe a **referência**. Por isso, alterações feitas dentro do método **afetam a array original**.

### Passando o array como parâmetro

```java
double[] array = new double[5];
modificaArray(array);
```

### Método recebendo array

```java
void modificaArray(double[] b) {
    b[0] = 10.5;   // altera a array original
}
```

## Arrays multidimensionais

São "arrays de arrays". A mais comum é a bidimensional (linhas e colunas).

```java
char[][] tabuleiro = new char[8][8];   // 8 linhas e 8 colunas
tabuleiro[5][4] = 'x';                 // linha 5, coluna 4
```

Com valores iniciais:

```java
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

## Arrays irregulares

São arrays em que cada linha pode ter uma **quantidade diferente de colunas**. Definimos só o número de linhas e depois criamos cada linha separadamente.

```java
int[][] b = new int[5][];   // 5 linhas, ainda sem colunas

b[0] = new int[2];   // linha 0 com 2 colunas
b[1] = new int[4];   // linha 1 com 4 colunas
```

## For aprimorado (for each)

Percorre todos os elementos da array sem precisar de índice.

```java
for (tipo elemento : nomeDaArray) {
    instruções;
}
```

Exemplo:

```java
int[] notas = {7, 8, 10, 6};

for (int nota : notas) {
    System.out.println(nota);
}
```

Leia como: "para cada `nota` em `notas`".

> Não podemos fazer modificações nos elementos com o for each. A variável `nota` é uma **cópia** do valor, então alterá-la não muda a array. Para modificar posições, use o `for` tradicional com índice.

## Lista de argumentos de comprimento variável (varargs)

Permite criar um método que recebe **uma quantidade qualquer de argumentos** do mesmo tipo, usando `...` após o tipo.

```java
double soma(double... numeros) {
    double total = 0;
    for (double n : numeros) {
        total += n;
    }
    return total;
}
```

Chamadas possíveis:

```java
soma();               // 0 argumentos
soma(5.0);            // 1 argumento
soma(1.5, 2.5, 3.0);  // vários argumentos
```

Dentro do método, `numeros` se comporta como uma **array comum** (`numeros.length`, for each, etc.).

> Regras: só pode haver **um** parâmetro varargs por método, e ele deve ser o **último** da lista de parâmetros.

## ArrayList (introdução)

Como vimos, uma array tem tamanho fixo. O `ArrayList` resolve isso: é uma lista que **cresce e diminui automaticamente**.

```java
import java.util.ArrayList;

ArrayList<String> nomes = new ArrayList<>();

nomes.add("Ana");                       // adiciona no final
nomes.add("Bruno");
System.out.println(nomes.get(0));       // acessa pelo índice -> Ana
System.out.println(nomes.size());       // quantidade de elementos -> 2
nomes.remove(0);                        // remove pelo índice
```

Diferenças em relação à array:

| | Array | ArrayList |
|---|---|---|
| Tamanho | Fixo | Dinâmico |
| Tamanho atual | `array.length` | `lista.size()` |
| Acesso | `array[i]` | `lista.get(i)` |
| Tipos primitivos | Aceita (`int[]`) | Não aceita direto, usa classes wrapper (`ArrayList<Integer>`) |

