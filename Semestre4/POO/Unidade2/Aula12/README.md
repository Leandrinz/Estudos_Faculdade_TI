# Exceções em Java: material de estudo e prática

Material baseado nos slides "Exceções" (Prof. Ítalo Assis, UFERSA).

## Sumário

1. Exceções
2. A instrução try...catch
3. Hierarquia de exceção Java
4. Multi-catch
5. Bloco finally
6. Rastreamento de pilha
7. Exceções verificadas e não verificadas
8. Cláusula throws e a instrução throw
9. Exemplo completo: finally e throw
10. Exercícios práticos
11. Questões de revisão
12. Soluções e respostas

---

## 1. Exceções

Uma **exceção** indica um problema que ocorre quando um programa é executado.

Alguns problemas podem impedir que o programa continue executando. Exemplos:

- índice de array inválido;
- tipo de dado de entrada incorreto;
- divisão por zero.

Quando a JVM ou um método detecta um problema, ele **lança** uma exceção. Os métodos das suas próprias classes também podem lançar exceções.

O **tratamento de exceção** ajuda a criar programas **tolerantes a falhas**. Em muitos casos, ele permite que o programa continue a executar como se nenhum problema tivesse ocorrido.

### Ponto de partida

Considere um programa que usa um array para guardar as respostas de um aluno a um questionário de 10 perguntas, cada uma com alternativas de 0 a 5, e conta quantas vezes cada alternativa foi escolhida. O que acontece se o usuário digitar a alternativa 14 para uma das questões?

Se a contagem for feita com algo como `contagem[resposta]++` e o array tiver 6 posições (índices 0 a 5), o índice 14 não existe. A JVM lança uma `ArrayIndexOutOfBoundsException` e o programa é interrompido. Esse é o problema que o tratamento de exceção resolve.

---

## 2. A instrução try...catch

Para lidar com uma exceção, coloque o código que pode lançá-la dentro de uma instrução `try`.

- O **bloco try** contém o código que pode lançar uma exceção.
- O **bloco catch** contém o código que trata a exceção, caso ela ocorra.

```java
try {
    // código que pode lançar exceções
} catch (ExcecaoA a) {
    // código que trata a exceção ExcecaoA
}
```

### Vários blocos catch

Pode haver vários blocos `catch` para tratar diferentes tipos de exceção que podem ser lançadas no `try` correspondente.

```java
try {
    // código que pode lançar exceções
} catch (ExcecaoA a) {
    // código que trata a exceção ExcecaoA
} catch (ExcecaoB b) {
    // código que trata a exceção ExcecaoB
} catch (ExcecaoN n) {
    // código que trata a exceção ExcecaoN
}
```

---

## 3. Hierarquia de exceção Java

```
Throwable
├── Exception
│   ├── RuntimeException
│   │   ├── ClassCastException
│   │   ├── NullPointerException
│   │   ├── ArithmeticException
│   │   ├── IndexOutOfBoundsException
│   │   │   └── ArrayIndexOutOfBoundsException
│   │   └── NoSuchElementException
│   │       └── InputMismatchException
│   └── IOException
└── Error
    ├── AWTError
    ├── ThreadDeath
    └── VirtualMachineError
```

Pontos principais:

- Somente objetos `Throwable` podem ser utilizados com o mecanismo de tratamento de exceção.
- `Exception` e suas subclasses representam **situações excepcionais que podem ser capturadas pelo aplicativo**.
- `Error` e suas subclasses representam **situações anormais**. Geralmente os aplicativos não podem se recuperar de `Error`s.

Observe no diagrama que `Exception` e `Error` são ambas subclasses de `Throwable`.

---

## 4. Multi-catch

É comum que um bloco `try` seja seguido de vários blocos `catch` para tratar vários tipos de exceção. Se os corpos desses blocos `catch` forem **idênticos**, use o recurso **multi-catch**:

```java
catch (Tipo1 | Tipo2 | Tipo3 e)
```

Exemplo:

```java
try {
    // código que pode lançar exceções
} catch (InputMismatchException | ArithmeticException e) {
    System.err.println("Erro: " + e);
}
```

---

## 5. Bloco finally

O bloco `finally` é executado **independentemente de uma exceção ser lançada ou não**. Se estiver presente, ele é colocado depois do último bloco `catch`.

```java
try {
    // código que pode lançar exceções
} catch (ExcecaoA a) {
    // código que trata a exceção ExcecaoA
} catch (ExcecaoN n) {
    // código que trata a exceção ExcecaoN
} finally {
    // código que será executado independente de haver exceção
}
```

---

## 6. Rastreamento de pilha

O **rastreamento de pilha** (stack trace) é a informação exibida quando uma exceção é lançada. Ele inclui:

- o nome da exceção (por exemplo, `java.lang.ArithmeticException`) em uma mensagem descritiva que indica o problema ocorrido;
- a pilha de chamadas de método no momento em que a exceção ocorreu.

Ele ajuda a depurar o programa.

Exemplo:

```
Exception in thread "main" java.lang.ArithmeticException: / by zero
    at DivideByZeroNoExceptionHandling.quotient(DivideByZeroNoExceptionHandling.java:10)
    at DivideByZeroNoExceptionHandling.main(DivideByZeroNoExceptionHandling.java:22)
```

Como ler:

| Trecho | Significado |
|---|---|
| `Exception in thread "main"` | A exceção ocorreu na thread principal |
| `java.lang.ArithmeticException` | Nome da exceção lançada |
| `/ by zero` | Mensagem descritiva do problema |
| `at ...quotient(...java:10)` | O método onde a exceção ocorreu, na linha 10 |
| `at ...main(...java:22)` | O método que chamou `quotient`, na linha 22 |

A leitura é de cima para baixo: a primeira linha `at` é onde o problema aconteceu, e as seguintes mostram quem chamou quem até chegar ali.

---

## 7. Exceções verificadas e não verificadas

| Tipo | Quais são | Exigência do compilador |
|---|---|---|
| **Não verificadas** | Subclasses de `RuntimeException` | Nenhuma obrigação de capturar ou declarar |
| **Verificadas** | Classes herdeiras de `Exception`, mas **não** de `RuntimeException` | Devem ser capturadas ou declaradas em uma cláusula `throws` |

O compilador verifica cada chamada e cada declaração de método para determinar se ele lança uma exceção verificada. Se sim, o compilador checa se essa exceção é **capturada** ou **declarada em uma cláusula `throws`**.

Exemplos pelo diagrama:

- Não verificadas: `ArithmeticException`, `NullPointerException`, `ClassCastException`, `ArrayIndexOutOfBoundsException`, `InputMismatchException`.
- Verificada: `IOException`.

---

## 8. Cláusula throws e a instrução throw

### Cláusula throws

A cláusula `throws` especifica as exceções que o método pode lançar se ocorrerem problemas. Ela deve aparecer **após a lista de parâmetros** e **antes do corpo do método**.

```java
public static int divisao(int numerador, int denominador)
        throws InputMismatchException, ArithmeticException {
    return numerador / denominador;
}
```

Os chamadores do método `divisao` ficam assim informados de que o método pode lançar uma exceção.

### Como uma exceção pode ser lançada em um método

1. Pela chamada de um método que lança uma exceção.
2. Pela realização de uma operação que gera uma exceção.
3. Explicitamente, com a instrução `throw`:

```java
throw new Exception();
```

---

## 9. Exemplo completo: finally e throw

```java
public class UsoFinallyThrow {
    public static void main(String[] args) {
        try {
            lancaExcecao();
        } catch (Exception excecao) {
            System.err.println("Excecao tratada na main");
        }
        naoLancaExcecao();
    }

    public static void lancaExcecao() throws Exception {
        try {
            System.out.println("Metodo lancaExcecao");
            throw new Exception();
        } catch (Exception excecao) {
            System.err.println("Excecao tratada no metodo lancaExcecao");
            throw excecao;
        } finally {
            System.err.println("Finally executado em lancaExcecao");
        }
    }

    public static void naoLancaExcecao() {
        try {
            System.out.println("Metodo naoLancaExcecao");
        } catch (Exception exception) {
            System.err.println(exception);
        } finally {
            System.err.println("Finally executado em naoLancaExcecao");
        }
        System.out.println("Fim do metodo naoLancaExcecao");
    }
}
```

Antes de ver a resposta, tente prever a saída (veja a Questão de revisão 8). O trecho mais importante é o `lancaExcecao`: o `catch` trata a exceção e a relança com `throw excecao`, mas o `finally` ainda é executado antes de a exceção seguir para a `main`.

---

## 10. Exercícios práticos

### Exercício 1: Questionário

Escreva um programa que utilize um array para representar as respostas de um aluno a um questionário com 10 perguntas.

- Cada questão possui as alternativas de 0 a 5.
- O programa deve contar o total de ocorrências de cada uma das respostas.

Etapas:

1. **Sem tratamento de exceção.** Implemente e execute. Digite a alternativa 14 em uma das questões. O que acontece? Leia o rastreamento de pilha.
2. **Com tratamento de exceção.** Adicione `try...catch` para que o programa avise o usuário sobre a alternativa inválida e peça a resposta novamente, em vez de ser encerrado.

### Exercício 2: Divisão inteira

1. Escreva um método que receba um numerador e um denominador inteiros e retorne o resultado da divisão inteira.
2. No método `main`, leia do usuário dois inteiros e utilize o método criado para calcular a divisão.
3. **Sem tratamento de exceções**, execute e anote o que acontece quando:
   - o denominador é um inteiro diferente de zero;
   - o denominador é um inteiro igual a zero;
   - o denominador é uma palavra.
4. **Agora implemente o tratamento de exceções.** Capture as exceções correspondentes aos dois casos de erro e peça os valores novamente até que a divisão seja feita com sucesso.
5. Declare no método a cláusula `throws` com as exceções que ele pode lançar.
6. Acrescente um bloco `finally` e observe quando ele é executado.

### Exercício 3: Experimentos com a estrutura

1. No Exercício 2, reescreva os dois blocos `catch` usando **multi-catch**. Em que situação isso é possível?
2. Faça um método que lance explicitamente uma exceção com `throw new Exception()`. O compilador exigiu algo de você? Por quê?
3. Troque `Exception` por `ArithmeticException` no passo anterior. O compilador continua exigindo a mesma coisa? Por quê?
4. Digite o código da seção 9 e execute. Compare a saída com a sua previsão.

---

## 11. Questões de revisão

1. O que é uma exceção?
2. O que significa dizer que o tratamento de exceção ajuda a criar programas tolerantes a falhas?
3. Qual é a função do bloco `try`? E do bloco `catch`?
4. Qual a diferença de uso entre vários blocos `catch` e o multi-catch?
5. Quando o bloco `finally` é executado?
6. O que um rastreamento de pilha informa? Por que ele ajuda na depuração?
7. Qual a diferença entre exceções verificadas e não verificadas? Dê um exemplo de cada.
8. Qual é a saída do programa `UsoFinallyThrow`, na ordem em que as mensagens são geradas?
9. Onde a cláusula `throws` deve aparecer na declaração de um método?
10. Cite as três formas pelas quais uma exceção pode ser lançada em um método.
11. Qual a diferença entre `Exception` e `Error` na hierarquia? Um aplicativo geralmente consegue se recuperar de um `Error`?
12. Verdadeiro ou falso: `ArithmeticException` precisa obrigatoriamente ser declarada em uma cláusula `throws` ou capturada.

---

## 12. Soluções e respostas

Tente resolver tudo antes de consultar esta seção.

### Solução do Exercício 1

```java
import java.util.InputMismatchException;
import java.util.Scanner;

public class Questionario {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        int[] contagem = new int[6]; // alternativas 0 a 5
        int questao = 0;

        while (questao < 10) {
            try {
                System.out.print("Resposta da questao " + (questao + 1) + " (0 a 5): ");
                int resposta = entrada.nextInt();
                contagem[resposta]++;   // pode lançar ArrayIndexOutOfBoundsException
                questao++;              // só avança se não houve exceção
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Alternativa invalida. Use um valor de 0 a 5.");
            } catch (InputMismatchException e) {
                System.err.println("Digite um numero inteiro.");
                entrada.nextLine(); // descarta a entrada incorreta
            }
        }

        for (int i = 0; i < contagem.length; i++) {
            System.out.println("Alternativa " + i + ": " + contagem[i] + " ocorrencia(s)");
        }
    }
}
```

Observações:

- Sem o `try...catch`, a alternativa 14 lança `ArrayIndexOutOfBoundsException` e o programa é encerrado com o rastreamento de pilha.
- Com o tratamento, a mensagem é exibida e o contador `questao` não avança, então a mesma pergunta é repetida.
- Em caso de `InputMismatchException`, a entrada incorreta permanece no `Scanner`. O `entrada.nextLine()` a descarta para evitar um laço infinito.

### Solução do Exercício 2

```java
import java.util.InputMismatchException;
import java.util.Scanner;

public class Divisao {
    public static int divisao(int numerador, int denominador)
            throws ArithmeticException {
        return numerador / denominador;
    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        boolean continuar = true;

        do {
            try {
                System.out.print("Numerador: ");
                int numerador = entrada.nextInt();
                System.out.print("Denominador: ");
                int denominador = entrada.nextInt();

                int resultado = divisao(numerador, denominador);
                System.out.println("Resultado: " + resultado);
                continuar = false;
            } catch (InputMismatchException e) {
                System.err.println("Excecao: " + e);
                entrada.nextLine(); // descarta a entrada incorreta
                System.out.println("Digite apenas numeros inteiros.");
            } catch (ArithmeticException e) {
                System.err.println("Excecao: " + e);
                System.out.println("O denominador nao pode ser zero.");
            } finally {
                System.out.println("Fim da tentativa.");
            }
        } while (continuar);
    }
}
```

O que acontece **sem** tratamento:

| Denominador | Resultado |
|---|---|
| Inteiro diferente de zero | A divisão inteira é calculada normalmente |
| Zero | `ArithmeticException: / by zero`, com rastreamento de pilha, e o programa é encerrado |
| Uma palavra | `InputMismatchException` lançada por `nextInt()`, e o programa é encerrado |

Observação: o `finally` é executado a cada tentativa, tenha ocorrido exceção ou não.

### Solução do Exercício 3

1. Os dois blocos `catch` do Exercício 2 têm corpos **diferentes** (um descarta a entrada, o outro não). Por isso o multi-catch só é adequado se você unificar o tratamento, por exemplo:
   ```java
   } catch (InputMismatchException | ArithmeticException e) {
       System.err.println("Excecao: " + e);
       entrada.nextLine();
   }
   ```
   O multi-catch serve quando os corpos dos blocos `catch` são idênticos.
2. Sim. `Exception` é uma exceção verificada (não herda de `RuntimeException`), então o método deve capturá-la ou declará-la com `throws Exception`.
3. Não. `ArithmeticException` é subclasse de `RuntimeException`, portanto é não verificada e o compilador não exige captura nem `throws`.
4. Veja a resposta da Questão 8.

### Respostas das questões de revisão

1. Uma exceção indica um problema que ocorre quando o programa é executado, como índice de array inválido, tipo de entrada incorreto ou divisão por zero.
2. Significa que o programa pode tratar o problema e, em muitos casos, continuar executando como se nada tivesse ocorrido, em vez de ser encerrado.
3. O `try` contém o código que pode lançar uma exceção. O `catch` contém o código que trata a exceção se ela ocorrer.
4. Vários blocos `catch` permitem tratar cada tipo de exceção de forma diferente. O multi-catch (`catch (Tipo1 | Tipo2 e)`) é usado quando os corpos dos blocos `catch` seriam idênticos.
5. Sempre, independentemente de uma exceção ser lançada ou não. Ele é colocado depois do último bloco `catch`.
6. Informa o nome da exceção, uma mensagem descritiva do problema e a pilha de chamadas de método no momento em que a exceção ocorreu. Isso mostra onde o erro aconteceu e a sequência de chamadas que levou até ele.
7. Não verificadas são subclasses de `RuntimeException` (ex.: `ArithmeticException`). Verificadas herdam de `Exception`, mas não de `RuntimeException` (ex.: `IOException`), e o compilador exige que sejam capturadas ou declaradas em `throws`.
8. Ordem das mensagens geradas:
   ```
   Metodo lancaExcecao
   Excecao tratada no metodo lancaExcecao
   Finally executado em lancaExcecao
   Excecao tratada na main
   Metodo naoLancaExcecao
   Finally executado em naoLancaExcecao
   Fim do metodo naoLancaExcecao
   ```
   Parte das mensagens vai para a saída padrão (`System.out`) e parte para a saída de erro (`System.err`). Em alguns consoles, a ordem de exibição entre os dois fluxos pode variar, mas a ordem de execução é a mostrada acima.
9. Após a lista de parâmetros e antes do corpo do método.
10. (a) Pela chamada de um método que lança uma exceção; (b) pela realização de uma operação que gera uma exceção; (c) explicitamente, com `throw new Exception();`.
11. `Exception` e suas subclasses representam situações excepcionais que podem ser capturadas pelo aplicativo. `Error` e suas subclasses representam situações anormais, e geralmente os aplicativos não conseguem se recuperar delas.
12. Falso. `ArithmeticException` é uma subclasse de `RuntimeException`, ou seja, uma exceção não verificada. A captura ou declaração é opcional.

---

## Referência

DEITEL, Paul J; DEITEL, Harvey M. *Java: como programar*. 8. ed. São Paulo: Pearson Education do Brasil, 2010.