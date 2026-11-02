# Aula 10 – Classes Abstratas, Polimorfismo e Interfaces

> Anotações de Programação Orientada a Objetos (POO). Os exemplos estão em **Java**, mas os conceitos valem para qualquer linguagem orientada a objetos (em C#, Python, C++ só muda a sintaxe).

---

## Sumário

1. [Revisão rápida: herança](#1-revisão-rápida-herança)
2. [Classes abstratas](#2-classes-abstratas)
3. [Polimorfismo](#3-polimorfismo)
4. [Interfaces](#4-interfaces)
5. [Classe abstrata × Interface](#5-classe-abstrata--interface)
6. [Juntando tudo](#6-juntando-tudo-um-exemplo-completo)
7. [Erros comuns](#7-erros-comuns)
8. [Resumo para revisão](#8-resumo-para-revisão)
9. [Desafios práticos](#9-desafios-práticos)

---

## 1. Revisão rápida: herança

**Herança** permite que uma classe (**subclasse/filha**) reaproveite atributos e métodos de outra (**superclasse/mãe**), usando `extends`.

```java
class Animal {
    String nome;

    void dormir() {
        System.out.println(nome + " está dormindo...");
    }
}

class Cachorro extends Animal {
    void latir() {
        System.out.println("Au au!");
    }
}
```

`Cachorro` ganha `nome` e `dormir()` de graça. A relação é do tipo **"é um"**: um Cachorro *é um* Animal.

**O problema:** e se alguém escrever `new Animal()`? O que seria um "animal genérico"? Não existe um animal que seja só "animal", ele sempre é um cachorro, um gato, um pássaro... É aí que entram as **classes abstratas**.

### 🛠️ Prática dos slides: Motivação – 1ª tentativa (uma classe para cada entidade)

**Contexto:** você é engenheiro de uma empresa especializada em **controlar veículos remotamente**. A empresa quer um sistema capaz de controlar diferentes tipos de veículos (barcos, carros, aviões...). Vamos dividir o problema em dois: **modelagem dos veículos** e **modelagem do controle**.

**Questão:** implemente as três classes abaixo, **cada uma independente das outras** (sem herança):

| BARCO | CARRO | AVIAO |
|---|---|---|
| capacidade | capacidade | capacidade |
| profundidade | qtdRodas | cadANAC |
| cadBarco | placa | cadAviao |
| `moverFrente()` | `moverFrente()` | `moverFrente()` |

Depois responda: **se a empresa determinar que todos os veículos devem ter capacidade mínima igual a 10, o que precisaria ser mudado?**

<details>
<summary>👁️ Ver resposta</summary>

```java
class Barco {
    private int capacidade;
    private double profundidade;
    private String cadBarco;

    public Barco(int capacidade, double profundidade, String cadBarco) {
        this.capacidade = capacidade;
        this.profundidade = profundidade;
        this.cadBarco = cadBarco;
    }

    public void moverFrente() {
        System.out.println("O barco está navegando para frente");
    }
}

class Carro {
    private int capacidade;
    private int qtdRodas;
    private String placa;

    public Carro(int capacidade, int qtdRodas, String placa) {
        this.capacidade = capacidade;
        this.qtdRodas = qtdRodas;
        this.placa = placa;
    }

    public void moverFrente() {
        System.out.println("O carro está andando para frente");
    }
}

class Aviao {
    private int capacidade;
    private String cadANAC;
    private String cadAviao;

    public Aviao(int capacidade, String cadANAC, String cadAviao) {
        this.capacidade = capacidade;
        this.cadANAC = cadANAC;
        this.cadAviao = cadAviao;
    }

    public void moverFrente() {
        System.out.println("O avião está taxiando para frente");
    }
}
```

**E a capacidade mínima = 10?** Seria preciso **validar o atributo `capacidade` nos construtores e nos métodos `set` de CADA classe** (Barco, Carro, Aviao), repetindo o mesmo código três vezes. E se surgir um quarto veículo, repete de novo.

**Conclusão:** a **não utilização de herança reduz o reuso de código**. Dá para melhorar!

</details>

### 🛠️ Prática dos slides: Motivação – 2ª tentativa (hierarquia de classes)

**Questão:** reformule o projeto usando **hierarquia de classes**, conforme o slide:

```
                      VEICULO (capacidade)
          ┌───────────────┼───────────────┐
      AQUATICO         TERRESTRE         AEREO
   (profundidade)      (qtdRodas)       (cadANAC)
     ┌────┴────┐       ┌────┴────┐      ┌────┴────┐
   BARCO     ...     CARRO     ...    AVIAO     ...
  (cadBarco)         (placa)          (cadAviao)
 moverFrente()      moverFrente()    moverFrente()
```

Quanto mais ao topo, mais **abstrato**; quanto mais embaixo, mais **concreto**. Coloque a validação de capacidade mínima (10) **em um único lugar**.

<details>
<summary>👁️ Ver resposta</summary>

```java
class Veiculo {
    protected int capacidade;

    public Veiculo(int capacidade) {
        setCapacidade(capacidade);
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        if (capacidade < 10) {
            throw new IllegalArgumentException("Capacidade mínima é 10");
        }
        this.capacidade = capacidade;
    }
}

class Aquatico extends Veiculo {
    protected double profundidade;

    public Aquatico(int capacidade, double profundidade) {
        super(capacidade);
        this.profundidade = profundidade;
    }
}

class Terrestre extends Veiculo {
    protected int qtdRodas;

    public Terrestre(int capacidade, int qtdRodas) {
        super(capacidade);
        this.qtdRodas = qtdRodas;
    }
}

class Aereo extends Veiculo {
    protected String cadANAC;

    public Aereo(int capacidade, String cadANAC) {
        super(capacidade);
        this.cadANAC = cadANAC;
    }
}

class Barco extends Aquatico {
    private String cadBarco;

    public Barco(int capacidade, double profundidade, String cadBarco) {
        super(capacidade, profundidade);
        this.cadBarco = cadBarco;
    }

    public void moverFrente() {
        System.out.println("O barco está navegando para frente");
    }
}

class Carro extends Terrestre {
    private String placa;

    public Carro(int capacidade, int qtdRodas, String placa) {
        super(capacidade, qtdRodas);
        this.placa = placa;
    }

    public void moverFrente() {
        System.out.println("O carro está andando para frente");
    }
}

class Aviao extends Aereo {
    private String cadAviao;

    public Aviao(int capacidade, String cadANAC, String cadAviao) {
        super(capacidade, cadANAC);
        this.cadAviao = cadAviao;
    }

    public void moverFrente() {
        System.out.println("O avião está taxiando para frente");
    }
}
```

Agora a regra "capacidade mínima 10" está **só em `Veiculo`** e vale para todos os descendentes (inclusive os que ainda vão ser criados).

**Mas ainda há um problema:** nada impede `new Veiculo(50)`, e `Veiculo` não declara `moverFrente()`. Um veículo "genérico" não existe... Isso nos leva às **classes abstratas**.

</details>

---

## 2. Classes abstratas

### 2.1 O que é?

Uma **classe abstrata** é uma classe **incompleta de propósito**. Ela serve de **molde/modelo** para outras classes, mas **não pode ser instanciada** (você não pode criar objetos dela diretamente).

```java
abstract class Animal { ... }

Animal a = new Animal();   // ERRO de compilação!
```

### 2.2 Métodos abstratos

Dentro de uma classe abstrata podemos declarar **métodos abstratos**: métodos que têm **apenas a assinatura, sem corpo**. Eles dizem *"toda filha minha PRECISA implementar isso, mas eu não sei como"*.

```java
abstract class Animal {
    String nome;

    // método abstrato: sem corpo, termina com ponto e vírgula
    abstract void emitirSom();

    // método concreto: tem corpo normal
    void dormir() {
        System.out.println(nome + " está dormindo...");
    }
}
```

> **Analogia:** pense numa **planta de casa** do arquiteto. Ela define que "a casa tem uma porta", mas não é uma casa de verdade (você não mora na planta!). Cada casa construída (subclasse) decide o material e o estilo da porta.

### 2.3 Regras importantes

| Regra | Explicação |
|---|---|
| Não pode ser instanciada | `new ClasseAbstrata()` dá erro |
| Pode ter métodos abstratos **e** concretos | Mistura de "contrato" + "código pronto" |
| Pode ter atributos e construtores | O construtor é chamado pelas filhas via `super(...)` |
| Se tem **pelo menos um** método abstrato, a classe **precisa** ser `abstract` | Obrigatório |
| A subclasse **concreta** deve implementar **todos** os métodos abstratos | Senão ela também precisa ser `abstract` |
| Método abstrato não pode ser `private`, `final` ou `static` | Porque precisa ser sobrescrito |
| Uma classe abstrata **pode** ter zero métodos abstratos | Só para impedir instanciação |

### 2.4 Exemplo completo: Formas geométricas

```java
abstract class Forma {
    private String cor;

    public Forma(String cor) {          // construtor existe, mesmo sendo abstrata
        this.cor = cor;
    }

    public String getCor() {
        return cor;
    }

    // Toda forma TEM área, mas o cálculo depende de qual forma é
    public abstract double calcularArea();
    public abstract double calcularPerimetro();

    // Método concreto: reaproveitado por todas as filhas
    public void exibirInfo() {
        System.out.println("Forma de cor " + cor);
        System.out.println("Área: " + calcularArea());
        System.out.println("Perímetro: " + calcularPerimetro());
    }
}
```

```java
class Circulo extends Forma {
    private double raio;

    public Circulo(String cor, double raio) {
        super(cor);                     // chama o construtor da classe abstrata
        this.raio = raio;
    }

    @Override
    public double calcularArea() {
        return Math.PI * raio * raio;
    }

    @Override
    public double calcularPerimetro() {
        return 2 * Math.PI * raio;
    }
}

class Retangulo extends Forma {
    private double base;
    private double altura;

    public Retangulo(String cor, double base, double altura) {
        super(cor);
        this.base = base;
        this.altura = altura;
    }

    @Override
    public double calcularArea() {
        return base * altura;
    }

    @Override
    public double calcularPerimetro() {
        return 2 * (base + altura);
    }
}
```

**Repare em duas coisas:**

1. `exibirInfo()` está escrito **uma vez só** na classe `Forma`, mas funciona para qualquer filha. Ele chama `calcularArea()` sem saber qual implementação será usada. Isso é **reuso de código**.
2. Se você esquecer de implementar `calcularPerimetro()` em `Retangulo`, o compilador **reclama**. A classe abstrata **obriga** as filhas a cumprirem o contrato.

### 2.5 Quando usar classe abstrata?

Use quando:
- Existe um **conceito genérico** que não faz sentido instanciar sozinho (Forma, Animal, Funcionário, Veículo).
- Você quer **compartilhar código e atributos** entre as filhas **e** forçar que elas implementem certos métodos.

### 🛠️ Prática dos slides: reformular o projeto de veículos com classes abstratas

**Questão:** retome a hierarquia da prática anterior (`Veiculo` → `Aquatico`/`Terrestre`/`Aereo` → `Barco`/`Carro`/`Aviao`) e **reformule-a usando classes abstratas**:

1. Quais classes devem ser abstratas? Por quê?
2. Declare `moverFrente()` como **método abstrato** em `Veiculo`.
3. Teste: o que acontece ao escrever `new Veiculo(50)`? E ao esquecer `moverFrente()` em `Carro`?

<details>
<summary>👁️ Ver resposta</summary>

**1.** `Veiculo`, `Aquatico`, `Terrestre` e `Aereo` são **conceitos genéricos** (não existe um "veículo qualquer"), então viram `abstract`. Só `Barco`, `Carro` e `Aviao` (as classes concretas) podem ser instanciadas.

**2.** Só o que muda em relação à versão anterior:

```java
abstract class Veiculo {
    protected int capacidade;

    public Veiculo(int capacidade) {
        setCapacidade(capacidade);
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        if (capacidade < 10) {
            throw new IllegalArgumentException("Capacidade mínima é 10");
        }
        this.capacidade = capacidade;
    }

    // método abstrato: toda classe concreta é obrigada a implementar
    public abstract void moverFrente();
}

abstract class Aquatico extends Veiculo {
    protected double profundidade;

    public Aquatico(int capacidade, double profundidade) {
        super(capacidade);
        this.profundidade = profundidade;
    }
}

abstract class Terrestre extends Veiculo {
    protected int qtdRodas;

    public Terrestre(int capacidade, int qtdRodas) {
        super(capacidade);
        this.qtdRodas = qtdRodas;
    }
}

abstract class Aereo extends Veiculo {
    protected String cadANAC;

    public Aereo(int capacidade, String cadANAC) {
        super(capacidade);
        this.cadANAC = cadANAC;
    }
}
```

`Aquatico`, `Terrestre` e `Aereo` **não implementam** `moverFrente()`, e tudo bem: como também são `abstract`, passam a obrigação para as filhas. As classes concretas ficam iguais, mas agora com `@Override`:

```java
class Carro extends Terrestre {
    private String placa;

    public Carro(int capacidade, int qtdRodas, String placa) {
        super(capacidade, qtdRodas);
        this.placa = placa;
    }

    @Override
    public void moverFrente() {
        System.out.println("O carro está andando para frente");
    }
}
// Barco e Aviao seguem o mesmo padrão (@Override em moverFrente())
```

**3.** Testes:

```java
Veiculo v = new Veiculo(50);   // ERRO de compilação: Veiculo é abstrata
```

Se `Carro` não implementar `moverFrente()`, o compilador também reclama (a menos que `Carro` seja declarada `abstract`).

</details>

---

## 3. Polimorfismo

**Poli** = muitas, **morfismo** = formas. Polimorfismo é a capacidade de **um mesmo comando se comportar de formas diferentes dependendo do objeto real** que o executa.

> **Analogia:** o botão "Play" do controle remoto. O gesto é o mesmo (apertar Play), mas na TV ele roda um filme, no som toca música, no videogame inicia o jogo. Mesma mensagem, comportamentos diferentes.

### 3.1 Os dois ingredientes

Para o polimorfismo funcionar em Java você precisa de:

1. **Herança** (ou implementação de interface)
2. **Sobrescrita de métodos** (`@Override`)

E então você pode usar uma **variável do tipo da superclasse** para guardar um **objeto de qualquer subclasse**. Isso se chama **upcasting** (e é automático).

```java
Forma f1 = new Circulo("Vermelho", 2.0);       // OK: Círculo É UMA Forma
Forma f2 = new Retangulo("Azul", 3.0, 4.0);    // OK: Retângulo É UMA Forma
```

### 🛠️ Prática dos slides: Modelagem do controle – 1ª tentativa (um método para cada classe concreta)

**Questão:** agora vamos modelar o **controle remoto**. Crie a classe `Controle` com **um método `controlar()` para cada classe concreta** (`Aviao`, `Barco` e `Carro`), que chama o `moverFrente()` do veículo recebido. Depois responda: **qual o problema dessa abordagem quando a empresa passar a controlar um novo tipo de veículo (ex.: `Helicoptero`)?**

<details>
<summary>👁️ Ver resposta</summary>

```java
class Controle {
    public void controlar(Aviao a) {
        a.moverFrente();
    }

    public void controlar(Barco b) {
        b.moverFrente();
    }

    public void controlar(Carro c) {
        c.moverFrente();
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        Controle controle = new Controle();

        controle.controlar(new Aviao(100, "ANAC-123", "PR-ABC"));
        controle.controlar(new Barco(30, 5.5, "BC-001"));
        controle.controlar(new Carro(12, 4, "ABC-1234"));
    }
}
```

**O problema:** a cada novo tipo de veículo (`Helicoptero`, `Moto`, `Submarino`...) é preciso **abrir a classe `Controle` e escrever mais um método `controlar()`**. O código fica repetitivo e **aberto para modificação**, e não para extensão. A solução é o **polimorfismo**, que vem a seguir.

</details>

### 3.2 O poder do polimorfismo na prática

```java
public class Main {
    public static void main(String[] args) {
        // Um único array guardando tipos DIFERENTES de forma
        Forma[] formas = {
            new Circulo("Vermelho", 2.0),
            new Retangulo("Azul", 3.0, 4.0),
            new Circulo("Verde", 1.5)
        };

        // Um único laço funciona para todas
        for (Forma f : formas) {
            f.exibirInfo();     // cada objeto usa o SEU calcularArea()
            System.out.println("-----");
        }
    }
}
```

O compilador vê `Forma f`, mas **em tempo de execução** o Java descobre se aquele objeto é um `Circulo` ou `Retangulo` e chama o método certo. Isso se chama **ligação dinâmica** (*dynamic binding* ou *late binding*).

**Vantagem enorme:** se amanhã você criar a classe `Triangulo extends Forma`, o laço acima **continua funcionando sem nenhuma alteração**. Código aberto para extensão, fechado para modificação.

### 3.3 Polimorfismo em parâmetros

```java
public static void imprimirArea(Forma f) {
    System.out.println("A área é: " + f.calcularArea());
}

imprimirArea(new Circulo("Preto", 5));
imprimirArea(new Retangulo("Branco", 2, 6));
```

Um método que recebe `Forma` aceita **qualquer filha** de Forma.

### 🛠️ Prática dos slides: Polimorfismo de inclusão (Controle com `Veiculo`)

**Contexto:** *polimorfismo de inclusão* é a **substituição da classe mãe por seus descendentes**. A assinatura do método recebe um parâmetro da classe mãe (`Veiculo`) e, na chamada, podemos passar instâncias das classes filhas (`Aviao`, `Barco`, `Carro`).

**Questão:** reescreva a classe `Controle` com **um único método** `controlar(Veiculo v)`. Teste passando um `Aviao`, um `Barco` e um `Carro`. Em seguida, crie a classe `Helicoptero` (filha de `Aereo`) e verifique se `Controle` precisa ser alterada.

<details>
<summary>👁️ Ver resposta</summary>

```java
class Controle {
    // aceita QUALQUER descendente de Veiculo
    public void controlar(Veiculo v) {
        v.moverFrente();    // cada objeto executa a implementação mais próxima na hierarquia
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        Controle controle = new Controle();

        controle.controlar(new Aviao(100, "ANAC-123", "PR-ABC"));   // "O avião está taxiando..."
        controle.controlar(new Barco(30, 5.5, "BC-001"));           // "O barco está navegando..."
        controle.controlar(new Carro(12, 4, "ABC-1234"));           // "O carro está andando..."
    }
}
```

Novo veículo, **sem mexer em `Controle`**:

```java
class Helicoptero extends Aereo {
    public Helicoptero(int capacidade, String cadANAC) {
        super(capacidade, cadANAC);
    }

    @Override
    public void moverFrente() {
        System.out.println("O helicóptero está inclinando e avançando");
    }
}

// na main:
controle.controlar(new Helicoptero(15, "ANAC-777"));   // funciona sem nenhuma alteração em Controle!
```

**Por que funciona?** O parâmetro é do tipo `Veiculo` (upcasting automático), e, como `moverFrente()` é abstrato em `Veiculo`, a **ligação dinâmica** escolhe em tempo de execução a implementação do objeto real.

</details>

### 3.4 Sem polimorfismo (o jeito ruim)

```java
// Péssimo: precisa de um if para cada tipo novo
if (obj instanceof Circulo) { ... }
else if (obj instanceof Retangulo) { ... }
else if (obj instanceof Triangulo) { ... }
```

Com polimorfismo, tudo isso vira **uma linha**: `forma.calcularArea()`.

### 3.5 Downcasting e `instanceof`

O contrário do upcasting. Se você tem uma variável do tipo `Forma` e precisa acessar algo **exclusivo** da filha, precisa fazer **cast explícito**:

```java
Forma f = new Circulo("Vermelho", 2.0);

// f.getRaio();   // ERRO: Forma não conhece getRaio()

if (f instanceof Circulo) {
    Circulo c = (Circulo) f;      // downcasting
    System.out.println(c.getRaio());
}
```

> Sempre teste com `instanceof` antes do downcast, senão pode estourar `ClassCastException`.

### 3.6 Sobrecarga × Sobrescrita (cai muito em prova!)

|  | **Sobrescrita** (*Override*) | **Sobrecarga** (*Overload*) |
|---|---|---|
| O que é | Filha **redefine** método da mãe | Mesma classe, **mesmo nome**, parâmetros diferentes |
| Assinatura | **Igual** | **Diferente** (tipo, quantidade ou ordem dos parâmetros) |
| Resolvida em | **Execução** (polimorfismo dinâmico) | **Compilação** (polimorfismo estático) |
| Exemplo | `calcularArea()` em Círculo | `somar(int a, int b)` e `somar(double a, double b)` |

```java
class Calculadora {
    int somar(int a, int b)             { return a + b; }
    double somar(double a, double b)    { return a + b; }       // sobrecarga
    int somar(int a, int b, int c)      { return a + b + c; }   // sobrecarga
}
```

### 🛠️ Prática dos slides: Polimorfismo de sobrecarga (segundo construtor em `Carro`)

**Contexto:** ocorre quando definimos **múltiplas funções com o mesmo nome**, porém com **tipos/quantidades diferentes de parâmetros**. É uma técnica muito comum em **construtores**.

**Questão:** crie um **segundo construtor** para a classe `Carro`, que receba apenas a `capacidade` e a `placa` (assumindo 4 rodas por padrão). Instancie um `Carro` com cada construtor.

<details>
<summary>👁️ Ver resposta</summary>

```java
class Carro extends Terrestre {
    private String placa;

    // Construtor 1: completo
    public Carro(int capacidade, int qtdRodas, String placa) {
        super(capacidade, qtdRodas);
        this.placa = placa;
    }

    // Construtor 2: sobrecarga (menos parâmetros, 4 rodas por padrão)
    public Carro(int capacidade, String placa) {
        this(capacidade, 4, placa);     // reaproveita o construtor 1
    }

    @Override
    public void moverFrente() {
        System.out.println("O carro está andando para frente");
    }
}
```

```java
Carro c1 = new Carro(5, 6, "ABC-1234");   // usa o construtor 1
Carro c2 = new Carro(5, "XYZ-9876");      // usa o construtor 2 (4 rodas)
```

O compilador escolhe qual construtor chamar **em tempo de compilação**, olhando a quantidade e os tipos dos argumentos. O `this(...)` evita repetir código entre os construtores.

</details>

### 🛠️ Prática dos slides: Polimorfismo de sobreposição (`toString` em `Carro`)

**Contexto:** ocorre quando **redefinimos um método da classe mãe em uma classe filha**; o método da filha se **sobrepõe**. Todas as classes em Java herdam de `Object`, e `Object` já possui uma implementação do método `toString()`.

**Questão:** imprima um objeto do tipo `Carro` **sem** sobrescrever `toString()` e depois **com** a sobrescrita. Compare as saídas.

<details>
<summary>👁️ Ver resposta</summary>

**Sem sobrescrever `toString()` em `Carro`:**

```java
Carro c = new Carro(5, 4, "ABC-1234");
System.out.println(c);
// Saída (algo como): Carro@1b6d3586
```

Usa a implementação herdada de `Object`: nome da classe + `@` + código hash (inútil para humanos).

**Sobrescrevendo:**

```java
class Carro extends Terrestre {
    // ... (restante da classe)

    @Override
    public String toString() {
        return "Carro [placa=" + placa + ", capacidade=" + capacidade + ", rodas=" + qtdRodas + "]";
    }
}
```

```java
System.out.println(c);
// Saída: Carro [placa=ABC-1234, capacidade=5, rodas=4]
```

O `println` chama `toString()` automaticamente, e, como o objeto real é um `Carro`, a versão **sobreposta** é a executada. É a mesma ideia de `calcularArea()` em `Circulo`.

</details>

---

## 4. Interfaces

### 4.1 O que é?

Uma **interface** é um **contrato puro**: ela define **O QUE** uma classe deve fazer, sem dizer **COMO**. Quem assina o contrato (usa `implements`) é obrigado a implementar todos os métodos.

```java
interface Voador {
    void voar();          // implicitamente public e abstract
}
```

```java
class Passaro implements Voador {
    @Override
    public void voar() {
        System.out.println("Batendo as asas...");
    }
}

class Aviao implements Voador {
    @Override
    public void voar() {
        System.out.println("Ligando os motores e decolando...");
    }
}
```

Repare: **Pássaro e Avião não têm nada em comum** na hierarquia (não faz sentido um herdar do outro), mas ambos **sabem voar**. A interface descreve uma **capacidade**.

> **Analogia:** uma **tomada**. A tomada define o formato do plugue (o contrato). Não importa se você vai plugar um ventilador, um carregador ou uma TV: se o plugue encaixa, funciona. A tomada não se importa com *o que* você pluga, só que siga o padrão.

### 4.2 Relação "é um" × "sabe fazer"

- **Herança/classe abstrata** → relação **"é um"** (Cachorro **é um** Animal)
- **Interface** → relação **"sabe fazer" / "pode ser tratado como"** (Pássaro **sabe** Voar; Pato **pode** Nadar)

Por isso muitas interfaces terminam em *-ável* / *-or*: `Comparable`, `Runnable`, `Voador`, `Imprimivel`.

### 4.3 Múltipla implementação

Em Java uma classe só pode **herdar de UMA** classe (`extends`), mas pode **implementar VÁRIAS** interfaces:

```java
interface Voador { void voar(); }
interface Nadador { void nadar(); }
interface Corredor { void correr(); }

class Animal { }

class Pato extends Animal implements Voador, Nadador, Corredor {
    @Override public void voar()   { System.out.println("Pato voando"); }
    @Override public void nadar()  { System.out.println("Pato nadando"); }
    @Override public void correr() { System.out.println("Pato correndo"); }
}
```

Isso resolve o famoso "problema do diamante" que a herança múltipla de classes causaria.

### 4.4 Regras das interfaces

| Regra | Explicação |
|---|---|
| Métodos são `public abstract` por padrão | Não precisa escrever |
| Atributos são `public static final` (constantes) | Têm que ser inicializados |
| Não pode ser instanciada | `new Voador()` dá erro |
| Não tem construtor | Não guarda estado |
| Classe usa `implements` | Interface pode usar `extends` para herdar de outras interfaces |
| Classe pode implementar várias | `implements A, B, C` |

```java
interface Config {
    int MAX_TENTATIVAS = 3;     // é public static final automaticamente
}
```

### 4.5 Métodos `default` e `static` (Java 8+)

Antes do Java 8, interfaces só tinham métodos abstratos. Hoje podem ter **métodos com corpo**:

```java
interface Veiculo {
    void acelerar();                          // abstrato

    default void buzinar() {                  // com implementação padrão
        System.out.println("Beep beep!");
    }

    static void mostrarRegras() {             // método estático da interface
        System.out.println("Respeite as leis de trânsito");
    }
}
```

- `default`: a classe **pode** usar como está ou **sobrescrever**. Serve para adicionar métodos novos a uma interface sem quebrar as classes que já a implementam.
- `static`: chamado direto pela interface: `Veiculo.mostrarRegras()`.

### 4.6 Polimorfismo com interfaces

Funciona **exatamente igual** ao polimorfismo com classes abstratas: a variável pode ter o tipo da interface.

```java
Voador v1 = new Passaro();
Voador v2 = new Aviao();

v1.voar();   // Batendo as asas...
v2.voar();   // Ligando os motores e decolando...

// Lista de qualquer coisa que voa
List<Voador> voadores = List.of(new Passaro(), new Aviao(), new Pato());
for (Voador v : voadores) {
    v.voar();
}
```

### 4.7 Exemplo clássico: `Comparable`

Você já usa interfaces sem perceber! A API do Java tem a interface `Comparable<T>`, que permite ordenar objetos:

```java
class Aluno implements Comparable<Aluno> {
    String nome;
    double nota;

    Aluno(String nome, double nota) {
        this.nome = nome;
        this.nota = nota;
    }

    @Override
    public int compareTo(Aluno outro) {
        return Double.compare(this.nota, outro.nota);
    }
}

// Agora Collections.sort(listaDeAlunos) funciona!
```

O `sort` não sabe nada sobre `Aluno`. Ele só sabe que todo `Comparable` tem `compareTo`. Isso é polimorfismo + interface trabalhando juntos.

### 🛠️ Prática dos slides: Pagamentos de funcionários e faturas (`Pagavel`)

**Contexto:** uma interface é muito usada quando **classes sem relação hierárquica** precisam compartilhar métodos comuns, permitindo que objetos de classes não relacionadas sejam **processados polimorficamente**. Uma `Fatura` e um `Empregado` não têm nada em comum, mas ambos podem ser **pagos**.

**Questão:** construa um aplicativo que determina os pagamentos para funcionários e também faturas, segundo o modelo:

```
              <<interface>>
                 Pagavel
                ▲        ▲
                │        │
             Fatura   Empregado (abstrata)
                          ▲
                          │
                 EmpregadoAssalariado
```

- `Pagavel` deve conter um método para **obter o valor do pagamento**;
- `Fatura` deve conter atributos para o **preço do item** e a **quantidade do item**;
- `Empregado` deve ser uma **classe abstrata** com os atributos **nome** e **carteira de trabalho**;
- `EmpregadoAssalariado` deve ter um atributo para o **valor do seu salário**;
- `Fatura` e `EmpregadoAssalariado` são **classes concretas**;
- Na classe executável, escreva um **método que recebe um objeto `Pagavel`** e imprime o custo do pagamento.

<details>
<summary>👁️ Ver resposta</summary>

```java
// Interface: o contrato
interface Pagavel {
    double getValorPagamento();
}

// Concreta 1: implementa a interface diretamente
class Fatura implements Pagavel {
    private double precoItem;
    private int quantidade;

    public Fatura(double precoItem, int quantidade) {
        this.precoItem = precoItem;
        this.quantidade = quantidade;
    }

    @Override
    public double getValorPagamento() {
        return precoItem * quantidade;
    }
}

// Abstrata: implementa a interface, mas deixa o método para as filhas
abstract class Empregado implements Pagavel {
    private String nome;
    private String carteiraTrabalho;

    public Empregado(String nome, String carteiraTrabalho) {
        this.nome = nome;
        this.carteiraTrabalho = carteiraTrabalho;
    }

    public String getNome() {
        return nome;
    }

    public String getCarteiraTrabalho() {
        return carteiraTrabalho;
    }

    // getValorPagamento() continua sem implementação: Empregado é abstrata
}

// Concreta 2: herda de Empregado e cumpre o contrato de Pagavel
class EmpregadoAssalariado extends Empregado {
    private double salario;

    public EmpregadoAssalariado(String nome, String carteiraTrabalho, double salario) {
        super(nome, carteiraTrabalho);
        this.salario = salario;
    }

    @Override
    public double getValorPagamento() {
        return salario;
    }
}
```

```java
public class Main {
    // Recebe QUALQUER coisa que seja Pagavel
    public static void imprimirPagamento(Pagavel p) {
        System.out.printf("Custo do pagamento: R$ %.2f%n", p.getValorPagamento());
    }

    public static void main(String[] args) {
        Pagavel fatura = new Fatura(25.50, 4);
        Pagavel empregado = new EmpregadoAssalariado("Ana", "12345", 3200.00);

        imprimirPagamento(fatura);       // Custo do pagamento: R$ 102,00
        imprimirPagamento(empregado);    // Custo do pagamento: R$ 3200,00
    }
}
```

**O que esse exercício mostra:**
- `Fatura` e `Empregado` **não têm relação de herança**, mas ambos "sabem ser pagos" (`Pagavel`).
- `imprimirPagamento(Pagavel p)` trata os dois **polimorficamente**, sem saber (nem se importar) com o tipo real.
- `Empregado` é abstrata e pode **implementar uma interface sem implementar seus métodos**, deixando essa tarefa para as filhas concretas.

</details>

---

## 5. Classe abstrata × Interface

| Característica | Classe Abstrata | Interface |
|---|---|---|
| Palavra-chave | `abstract class` / `extends` | `interface` / `implements` |
| Pode ter atributos de instância | Sim | Não (só constantes) |
| Pode ter construtor | Sim | Não |
| Métodos concretos | Sim | Só `default` e `static` |
| Métodos abstratos | Sim (opcional) | Sim (padrão) |
| Herança/implementação múltipla | **Não**, só 1 classe | **Sim**, várias interfaces |
| Relação | **"é um"** | **"sabe fazer"** |
| Modificadores de acesso | Qualquer um | Métodos são `public` |
| Quando usar | Famílias de classes parecidas, com código/estado em comum | Capacidades que classes **não relacionadas** podem ter |

### Como decidir?

> - As classes são **parentes** e compartilham **código e atributos**? → **Classe abstrata**
> - As classes **não têm relação** mas precisam cumprir o **mesmo contrato**? → **Interface**
> - Precisa de **vários contratos** ao mesmo tempo? → **Interfaces**
> - Na dúvida, **comece com interface** (é mais flexível), e use classe abstrata quando precisar de estado compartilhado.

Os dois podem (e devem!) ser **combinados**, como no próximo exemplo.

---

## 6. Juntando tudo: um exemplo completo

**Cenário:** sistema de uma empresa com funcionários, e alguns deles podem receber bônus.

```java
// Interface: uma capacidade
interface Bonificavel {
    double calcularBonus();
}

// Classe abstrata: o conceito genérico
abstract class Funcionario {
    protected String nome;
    protected double salarioBase;

    public Funcionario(String nome, double salarioBase) {
        this.nome = nome;
        this.salarioBase = salarioBase;
    }

    // Cada tipo de funcionário calcula o salário de um jeito
    public abstract double calcularSalario();

    public void exibirContracheque() {
        System.out.printf("%s recebe R$ %.2f%n", nome, calcularSalario());
    }
}

// Concreta 1: só herda
class Estagiario extends Funcionario {
    public Estagiario(String nome) {
        super(nome, 1500.0);
    }

    @Override
    public double calcularSalario() {
        return salarioBase;     // estagiário não recebe extras
    }
}

// Concreta 2: herda E implementa interface
class Gerente extends Funcionario implements Bonificavel {
    private int tamanhoEquipe;

    public Gerente(String nome, double salarioBase, int tamanhoEquipe) {
        super(nome, salarioBase);
        this.tamanhoEquipe = tamanhoEquipe;
    }

    @Override
    public double calcularBonus() {
        return tamanhoEquipe * 200.0;
    }

    @Override
    public double calcularSalario() {
        return salarioBase + calcularBonus();
    }
}

// Concreta 3: herda E implementa interface
class Vendedor extends Funcionario implements Bonificavel {
    private double totalVendas;

    public Vendedor(String nome, double salarioBase, double totalVendas) {
        super(nome, salarioBase);
        this.totalVendas = totalVendas;
    }

    @Override
    public double calcularBonus() {
        return totalVendas * 0.05;      // 5% de comissão
    }

    @Override
    public double calcularSalario() {
        return salarioBase + calcularBonus();
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        List<Funcionario> equipe = List.of(
            new Estagiario("Ana"),
            new Gerente("Bruno", 8000, 5),
            new Vendedor("Carla", 3000, 20000)
        );

        // Polimorfismo com a classe abstrata
        for (Funcionario f : equipe) {
            f.exibirContracheque();

            // Polimorfismo com a interface
            if (f instanceof Bonificavel b) {
                System.out.println("   -> Bônus incluso: R$ " + b.calcularBonus());
            }
        }
    }
}
```

**Saída esperada:**
```
Ana recebe R$ 1500.00
Bruno recebe R$ 9000.00
   -> Bônus incluso: R$ 1000.0
Carla recebe R$ 4000.00
   -> Bônus incluso: R$ 1000.0
```

**O que esse exemplo mostra:**
- `Funcionario` (abstrata): define o conceito e o código compartilhado (`exibirContracheque`).
- `Bonificavel` (interface): capacidade que só **alguns** funcionários têm.
- Polimorfismo: o laço trata todos como `Funcionario`, e cada um calcula o salário do seu jeito.

---

## 7. Erros comuns

| Erro | Por quê acontece | Como corrigir |
|---|---|---|
| `new ClasseAbstrata()` | Classe abstrata não pode ser instanciada | Instancie uma **filha concreta** |
| Esquecer de implementar método abstrato | Contrato não cumprido | Implemente com `@Override` ou torne a filha `abstract` |
| Método da interface sem `public` na implementação | Sobrescrever não pode reduzir a visibilidade | Coloque `public` |
| Tentar chamar método exclusivo da filha com variável da mãe | O compilador só enxerga o tipo da **variável** | Faça downcast (com `instanceof`) |
| `ClassCastException` | Downcast para o tipo errado | Teste com `instanceof` antes |
| Confundir sobrecarga com sobrescrita | Nomes parecidos | Lembre: **sobre-escrita** = reescrever a da mãe |
| Usar `abstract` e `final` juntos | Contraditório (abstrato precisa ser sobrescrito, final proíbe) | Escolha um |
| Criar interface com atributos de instância | Atributos de interface são constantes | Use classe abstrata se precisa de estado |

---

## 8. Resumo para revisão

- **Classe abstrata**: modelo incompleto, **não instancia**; mistura métodos abstratos (contrato) e concretos (código pronto); tem atributos e construtor; relação **"é um"**; só herda de **uma**.
- **Método abstrato**: só assinatura, sem corpo; **filhas concretas são obrigadas a implementar**.
- **Polimorfismo**: mesmo método, comportamentos diferentes conforme o objeto **real**; usa **upcasting** + **sobrescrita** + **ligação dinâmica**.
- **Interface**: contrato 100% de "o quê", relação **"sabe fazer"**; uma classe pode implementar **várias**; métodos `public abstract` por padrão; atributos são constantes; pode ter `default` e `static`.
- **Sobrecarga** (mesma classe, parâmetros diferentes, compilação) ≠ **Sobrescrita** (filha redefine método da mãe, execução).
- **Boa prática:** programe **voltado para a abstração** (use `Forma f`, `Voador v`, `List<...>`), não para a classe concreta. Seu código fica flexível e fácil de estender.

---

## 9. Desafios práticos

### Desafio 1 (nível fácil): Zoológico sonoro

Crie uma **classe abstrata** `Animal` com:
- atributo `nome` e construtor;
- método abstrato `emitirSom()`;
- método concreto `apresentar()` que imprime `"Eu sou o <nome> e faço: "` seguido do som (chamando `emitirSom()`).

Crie as classes `Cachorro`, `Gato` e `Vaca` que estendem `Animal`.

Na `main`, crie um **array de `Animal`** com um de cada e use um `for` para chamar `apresentar()` em todos.

**Bônus:** adicione uma nova classe `Pato` **sem modificar** o laço da `main`. Percebeu como o polimorfismo facilita?

---

### Desafio 2 (nível médio): Sistema de pagamentos

Crie uma **interface** `Pagavel` com o método `boolean processarPagamento(double valor)`.

Implemente três classes **sem relação de herança entre si**:
- `CartaoCredito` (atributos: número, limite; só aprova se `valor <= limite`, e então reduz o limite);
- `Pix` (atributo: saldo; aprova se tiver saldo, e então debita);
- `Boleto` (sempre aprova, mas imprime "Boleto gerado, aguardando pagamento").

Crie uma classe `Checkout` com o método:
```java
public void finalizarCompra(Pagavel forma, double valor)
```
que chama `processarPagamento` e imprime se a compra foi aprovada ou recusada.

Teste passando os três tipos de pagamento para o **mesmo método** `finalizarCompra`.

**Bônus:** adicione um método `default` na interface chamado `exibirComprovante()` que imprime uma mensagem padrão. Sobrescreva-o apenas em `Pix`.

---

### Desafio 3 (nível difícil): Biblioteca multimídia

Monte um sistema que **combina** classe abstrata e interface:

1. Classe abstrata `ItemBiblioteca` com atributos `titulo` e `anoPublicacao`, o método abstrato `calcularMultaPorDia()` e um método concreto `calcularMulta(int diasAtraso)` que usa `calcularMultaPorDia()` (isso é polimorfismo dentro da classe mãe!).
2. Interface `Reproduzivel` com os métodos `play()` e `pause()`.
3. Interface `Emprestavel` com os métodos `emprestar()` e `devolver()`.
4. Classes concretas:
   - `Livro`: é `ItemBiblioteca` e `Emprestavel` (multa de R$ 1,00/dia);
   - `DVD`: é `ItemBiblioteca`, `Emprestavel` **e** `Reproduzivel` (multa de R$ 3,00/dia);
   - `Revista`: é `ItemBiblioteca`, mas **não** é emprestável, só consulta local (multa 0).
5. Na `main`, crie uma `List<ItemBiblioteca>` com vários itens e, usando `instanceof`:
   - percorra a lista e imprima a multa de 5 dias de atraso de cada item;
   - dê `play()` apenas nos itens que são `Reproduzivel`;
   - tente `emprestar()` apenas nos que são `Emprestavel`.

**Reflexão final:** por que `Reproduzivel` e `Emprestavel` foram feitas como **interfaces** e não como classes abstratas? (Dica: pense em quantas classes `DVD` poderia herdar.)

---