# Aula 06 — 10/09/2026

## Escopo e Encapsulamento

### Escopo

- Alcance da variável (onde você consegue usar).
- Local (bloco) e Global.
- Você pode criar uma variável na última linha, que ela vai ser reconhecida por quem está em cima (mas não é recomendado).

### Encapsulamento

- Podemos proteger (esconder) métodos e atributos para que algumas classes não tenham acesso a eles.
- **"Pra que?"** → Impede que alguém venha e coloque coisas erradas.

### Modificadores de Acesso

Permitem a restrição do acesso.

```
modificadorDeAcesso tipo-ou-classe nome-do-campo;
```

**Tipos:**

| Modificador | Descrição |
|---|---|
| `public` | Todo mundo tem acesso |
| `private` | Só pode ser acessado dentro da classe |
| `package` ou `friendly` | Visível para todas as classes pertencentes ao mesmo pacote |
| `protected` | Apenas classes herdeiras têm acesso |

### Regras Básicas

- Todos os campos de uma classe devem ser `private`.
- Métodos que devem ser acessíveis/utilizados devem ser `public`.
- Métodos que permitam a manipulação controlada dos valores dos campos devem ser escritos e utilizar o modificador `public`.
- Métodos auxiliares podem ser declarados com o modificador `private`. Esses métodos poderão ser executados por outros métodos dentro da mesma classe.

### Métodos `get` e `set`

- **get** → Quando a outra classe quer *pegar* o valor da variável.
- **set** → Quando a outra classe quer *alterar* o valor da variável.

---

## Sobrecarga de Métodos

Escrever vários métodos com nomes **iguais**, mas com assinaturas diferentes (tipo e parâmetros).

`Soma.java`

```java
public class Soma {
    public int soma(int a, int b) {
        return a + b;
    }

    public float soma(float a, float b) {
        return a + b;
    }
}
```

**Benefícios:** Podemos criar objetos diferentes.

---

## Atributos e Métodos Estáticos

### Atributos Estáticos

- Compartilhados por todas as instâncias dessa classe.

**Declaração:**

```java
public static int qtd;
```

**Acesso** (através da própria classe):

```java
NomeDaClasse.atributoEstatico;
```

Iniciamos diretamente na classe, pois não precisamos de um objeto pra isso.

### Métodos Estáticos

Podem ser chamados sem existir objetos.

**Declaração:**

```java
public static void main(String[] args) { ... }
```

**Acesso:**

```java
NomeDaClasse.metodoEstatico();
```