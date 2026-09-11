# Aula 07 — 11/09/2026

## Fábrica de Instância

Em vez de usar construtores comuns, podemos usar um "construtor personalizado", o que chamamos de **fábrica de instâncias** (ou *factory method*). Nele podemos programar para só criar um objeto se as condições que colocarmos forem atendidas.

Para isso, geralmente:

- O construtor da classe é declarado como `private`, impedindo a criação direta com `new`.
- Um método `public static` é criado para controlar a criação do objeto, validando as condições antes de retorná-lo.

**Exemplo:**

```java
public class Usuario {
    private String nome;

    // Construtor privado
    private Usuario(String nome) {
        this.nome = nome;
    }

    // Fábrica de instância
    public static Usuario criar(String nome) {
        if (nome == null || nome.isEmpty()) {
            return null; // condição não atendida, não cria o objeto
        }
        return new Usuario(nome);
    }
}
```

```java
Usuario u = Usuario.criar("Maria"); // objeto criado normalmente
Usuario u2 = Usuario.criar("");     // retorna null, condição não atendida
```

---

## Classes e Métodos Genéricos

`Soma.java`

```java
public class Soma {
    public Integer soma(Integer a, Integer b) {
        Integer res = a + b;
        return res;
    }

    public Double soma(Double a, Double b) {
        Double res = a + b;
        return res;
    }
}
```

### Métodos Genéricos

- O retorno do método será do mesmo tipo dos parâmetros.
- Não pode usar tipos primitivos (`int`, `double`, `boolean`, etc.), apenas suas classes *wrapper* (`Integer`, `Double`, `Boolean`, etc.).
- O tipo genérico é declarado entre `<>` antes do retorno do método (ex.: `<T>`).

```java
public class Igualdade {
    public static <T> boolean ehIgual(T d1, T d2) {
        return d1.equals(d2);
    }
}
```

```java
boolean resultado = Igualdade.ehIgual(5, 5);           // funciona com Integer
boolean resultado2 = Igualdade.ehIgual("oi", "oi");     // funciona com String
```

### Classes Genéricas

- O tipo genérico é declarado entre `<>` junto ao nome da classe.
- Permite que a mesma classe trabalhe com diferentes tipos, sem precisar reescrever o código para cada um.

```java
import java.util.ArrayList;
import java.util.List;

public class Pilha<T> {
    private List<T> elementos = new ArrayList<>();

    public void push(T valor) {
        elementos.add(valor);
    }
}
```

```java
Pilha<Double> doubleStack = new Pilha<>();
doubleStack.push(10.5);

Pilha<String> stringStack = new Pilha<>();
stringStack.push("texto");
```

Isso nos dá uma boa oportunidade para **reutilização de software**, já que a mesma classe (`Pilha`) pode ser usada com diferentes tipos (`Double`, `String`, etc.) sem duplicar código.