# Aula 04 — 28/08/2026

# Criação de Classes

---

## Paradigmas de Programação

É a forma como a solução para um determinado problema é desenvolvida.

---

## Benefícios do POO (Programação Orientada a Objetos)

- **Modularidade**
  - Uma vez criado, um objeto pode ser passado por todo o sistema.

- **Encapsulamento**
  - Detalhes de implementação ficam ocultos externamente ao objeto.

- **Reuso**
  - Podemos reutilizar um objeto em outros programas.

- **Manutenibilidade**
  - A manutenção é realizada em pontos específicos do programa (objetos).

---

## Classes, Métodos e Atributos

| Conceito     | Exemplo / Definição                          |
|--------------|-----------------------------------------------|
| **Classes**  | Pessoa, Imóvel, Produto                        |
| **Métodos**  | Ações realizadas por essa classe               |
| **Atributos**| Dados armazenados pela classe                  |

### Declaração de Classe

```java
class NomeDaClasse {
    // aqui contém os dados e operações
}
```

### Atributos

```java
public class Pessoa {
    int idade;    // Atributo
    String nome;  // Atributo
}
```

### Métodos

O método é como uma função, mas está associado a uma **classe**.

Métodos podem receber parâmetros.

#### Declaração

```java
tipoOuClasseDeRetorno nomeDoMetodo(listaDeArgumentos) {
    // corpo do método
}
```

### Classe Executável

Para ser executável, uma classe deve ter um método `main`:

```java
public static void main(String[] args) {
    // ponto de entrada do programa
}
```

---

## Objetos e Construtores

### Objetos

- Materialização da classe.
- São a própria instância.
- Sempre possuem **todos** os atributos da classe.

#### Referência

É a nossa variável de referência que aponta para o objeto.

### Criação de Referências

Para criar uma referência, declaramos o tipo (a classe) seguido do nome da variável:

```java
Pessoa p1;
```

Nesse momento, `p1` ainda não aponta para nenhum objeto — ela existe apenas como uma referência vazia (`null`).

### Operador `new`

Aloca memória para o novo objeto.

```java
new Pessoa();
```

### Criar o Objeto

Para criar o objeto e associá-lo a uma referência, combinamos os dois passos anteriores:

```java
Pessoa p1 = new Pessoa();
```

Aqui, `p1` passa a apontar para um objeto do tipo `Pessoa` recém-criado na memória.

---

## Valores Padrão de um Campo Não Inicializado

Quando um atributo (campo) de uma classe não é inicializado explicitamente, ele recebe um valor padrão de acordo com seu tipo:

| Tipo                  | Valor padrão   |
|-----------------------|----------------|
| `boolean`              | `false`        |
| `char`                 | `'\u0000'`     |
| `byte`                 | `0`            |
| `short`                | `0`            |
| `int`                  | `0`            |
| `long`                 | `0L`           |
| `float`                | `0.0f`         |
| `double`               | `0.0d`         |
| Tipos de referência (String, objetos, arrays) | `null` |