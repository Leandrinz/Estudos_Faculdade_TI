# Reúso de Classes

> Anotações da aula de POO (Prof. Ítalo Assis, UFERSA)
> Base: slides "Reúso de classes" + explicações complementares
> Linguagem dos exemplos: **Java**

---

## 0. Mapa da aula (visão geral)

```
Reúso de classes
├── Introdução (por que reutilizar?)
├── Delegação (ou Composição)   →  relação "TEM UM"
└── Herança                     →  relação "É UM TIPO DE"
    ├── palavra-chave super
    ├── sobreposição de métodos
    ├── classe Object
    └── modificador protected
```

---

## 1. Introdução

### O que é reúso?

**Reúso** é aproveitar classes e métodos que já existem em vez de reescrever tudo do zero.

- Diminui a necessidade de escrever novos métodos e classes.
- Em POO, dá para criar uma classe nova **baseada em outra**: ela já nasce com o que a original tem e pode **acrescentar** comportamento próprio.

### O problema que motiva tudo

Imagine que existe a classe `RegistroAcademico` (modela um **graduando**). Agora precisamos modelar um **pós-graduando**, que tem:

- tudo que o graduando tem (atributos e métodos), **mais**
- `tituloTese` e `orientador`.

**Ideia ruim:** colocar `tituloTese` e `orientador` direto em `RegistroAcademico`.
Por quê? Todo graduando passaria a carregar campos que nunca usa → **desperdício de memória** (e bagunça no modelo).

**Solução orientada a objetos:** criar uma classe nova, `RegistroAcademicoPosGraduacao`, que reaproveita `RegistroAcademico` e só adiciona o que é novo.

> Importante: o código **não é copiado** da classe base para a nova. Ele é **reaproveitado** por um mecanismo da linguagem.

### Os dois mecanismos de reúso em Java

| Mecanismo | Como funciona | Relação |
|---|---|---|
| **Delegação (composição)** | A classe nova tem **uma instância** da classe base como **atributo** | "tem um" (*has-a*) |
| **Herança** | A classe nova **estende diretamente** a classe base | "é um tipo de" (*is-a*) |

---

## 2. Delegação ou Composição

### Conceito

Para criar uma classe nova a partir de outra **sem herança**, fazemos assim:

1. Colocamos **uma instância da classe base como atributo** da classe nova.
2. A classe nova passa a ser **composta** de: seus campos próprios + uma instância da classe base.
3. Para que os métodos da classe base possam ser usados, **escrevemos na classe nova métodos correspondentes que chamam os da classe base**.
   Isso é **delegar**: repassar a execução para o outro objeto.

### Dois nomes, duas ideias (mas andam juntas)

- **Composição** = a **estrutura** (um objeto *contém* outro).
- **Delegação** = o **comportamento** (um objeto *repassa* a tarefa ao outro).

### Exemplo do slide: `DataHora`

Uma data-e-hora **tem uma** `Data` e **tem uma** `Hora`. Não faz sentido dizer que `DataHora` "é uma" `Data`.

```java
public class DataHora {
    private Data estaData;   // composição: DataHora TEM uma Data
    private Hora estaHora;   // composição: DataHora TEM uma Hora

    DataHora(int hora, int minuto, int segundo, int dia, int mes, short ano) {
        estaData = new Data(dia, mes, ano);
        estaHora = new Hora(hora, minuto, segundo);
    }

    DataHora(int dia, int mes, short ano) {
        estaData = new Data(dia, mes, ano);
        estaHora = new Hora(0, 0, 0);     // meia-noite por padrão
    }

    public String toString() {
        return estaData + " " + estaHora; // delega: usa o toString de cada parte
    }
}
```

**Como ler esse código:**

- Os dois atributos `estaData` e `estaHora` são a **composição**.
- O construtor **cria** os objetos internos com `new`.
- O `toString` **delega** a formatação para `Data` e `Hora` (ao concatenar um objeto numa String, o Java chama o `toString` dele).

### Exemplo do slide (UML): `Funcionario` e `Data`

`Funcionario` tem o atributo `nasc: Data`. Para calcular a idade, ele **delega** para a classe `Data`:

```java
public int calcularIdade() {
    return nasc.calcularAnos(hoje);   // delegação: Data faz o cálculo
}
```

### Como montar uma delegação (receita)

```java
class Classe_Nova {
    private Classe_Base base;            // 1) atributo do tipo da classe base

    Classe_Nova(...) {
        base = new Classe_Base(...);     // 2) instancia no construtor
    }

    public Tipo metodo() {
        return base.metodo();            // 3) método que repassa a chamada
    }
}
```

---

## 3. Prática 1: `RegistroAcademicoPosGraduacao` por delegação

**Enunciado:** criar `RegistroAcademico` (atributos `nomeDoAluno`, `matricula`, `curso`, construtor e `toString`) e depois `RegistroAcademicoPosGraduacao`, que reutiliza a primeira **via delegação** e acrescenta `tituloTese` e `orientador`. Em uma classe executável, criar instâncias e imprimir com `toString`.

<details>
<summary><b>Ver solução</b> (tente fazer antes!)</summary>

```java
public class RegistroAcademico {
    private String nomeDoAluno;
    private String matricula;
    private String curso;

    public RegistroAcademico(String nomeDoAluno, String matricula, String curso) {
        this.nomeDoAluno = nomeDoAluno;
        this.matricula = matricula;
        this.curso = curso;
    }

    public String toString() {
        return nomeDoAluno + " (" + matricula + ") - " + curso;
    }
}
```

```java
public class RegistroAcademicoPosGraduacao {
    private RegistroAcademico registro;   // delegação: guarda um registro base
    private String tituloTese;
    private String orientador;

    public RegistroAcademicoPosGraduacao(String nome, String matricula, String curso,
                                         String tituloTese, String orientador) {
        this.registro = new RegistroAcademico(nome, matricula, curso);
        this.tituloTese = tituloTese;
        this.orientador = orientador;
    }

    public String toString() {
        return registro + " | Tese: " + tituloTese + " | Orientador: " + orientador;
    }
}
```

```java
public class Principal {
    public static void main(String[] args) {
        RegistroAcademicoPosGraduacao a = new RegistroAcademicoPosGraduacao(
            "Maria", "2024001", "Ciência da Computação",
            "Redes neurais em saúde", "Prof. João");
        RegistroAcademicoPosGraduacao b = new RegistroAcademicoPosGraduacao(
            "Carlos", "2024002", "Ciência da Computação",
            "Segurança em IoT", "Profa. Ana");

        System.out.println(a);
        System.out.println(b);
    }
}
```

**Observe:** `RegistroAcademicoPosGraduacao` **não** tem `getNome()`, por exemplo. Se quiséssemos, teríamos que escrever um método que repassa: `return registro.getNome();`. É o "custo" da delegação.

</details>

---

## 4. Herança

### Quando a delegação não é a mais natural

Delegação funciona bem para "tem um". Mas e quando a classe nova é **uma versão mais especializada** da base?

**Exemplo do slide:** `Pessoa`, `Funcionario` e `ChefeDeDepartamento`.

**Com delegação (desconfortável):**

```
ChefeDeDepartamento  →  tem um Funcionario  →  tem uma Pessoa
```

Seria preciso escrever vários métodos só repassando chamadas (`getNome`, `getIdentidade`, `getSalario`...). E, conceitualmente, um chefe **não "tem um" funcionário**, ele **é** um funcionário.

**Com herança (natural):**

```
Pessoa  ◄── Funcionario  ◄── ChefeDeDepartamento
         "é um tipo de"      "é um tipo de"
```

- Subindo na hierarquia: **Generalização** (mais geral).
- Descendo: **Especialização** (mais específico).

### O que é herança

- Cria uma classe **usando outra como base**.
- Na classe nova, descrevemos **apenas as diferenças** em relação à classe base.
- **Reutiliza campos e métodos** da base: instâncias da subclasse chamam métodos e acessam atributos herdados **como se fossem delas mesmas**.
- É o mecanismo mais apropriado para relações **"é-um-tipo-de"**.

### Vocabulário

| Termo | Significa | Exemplo |
|---|---|---|
| **Superclasse / classe base / classe pai** | A classe que está sendo estendida | `Funcionario` |
| **Subclasse / classe derivada / classe filha** | A classe que estende | `Programador` |
| **Classe ancestral** | Qualquer classe acima na hierarquia | `Pessoa` é ancestral de `ChefeDeDepartamento` |

### Sintaxe: `extends`

```java
class Programador extends Funcionario {
    private String linguagem;                       // só o que é novo

    public void informarLinguagem(String nome) {
        this.linguagem = nome;
    }
}
```

`Programador` **herda** `nome`, `nasc`, `salario`, `informarSalario()` e `calcularIdade()`. **Não precisa redeclarar nada disso.**

Então isto funciona:

```java
Programador p = new Programador();
p.informarSalario(5000);       // método herdado de Funcionario
p.informarLinguagem("Java");   // método próprio de Programador
```

### Teste mental para decidir: herança ou delegação?

Complete a frase:

- "**X é um tipo de Y**" faz sentido? → **Herança**
  (`Programador` é um tipo de `Funcionario` ✅)
- "**X tem um Y**" faz mais sentido? → **Delegação**
  (`DataHora` tem uma `Data` ✅; `DataHora` é um tipo de `Data` ❌)

---

## 5. A palavra-chave `super`

### Para que serve

- Refere-se à **classe ancestral imediata** (a superclasse direta).
- É parecida com `this`:
  - `this` → "o **meu** objeto"
  - `super` → "a parte **da minha superclasse**"
- Permite que a subclasse acesse **construtores e métodos** da superclasse → aumenta a reutilização de código.

### Uso 1: chamar o construtor da superclasse

```java
public class Ponto2D {
    private double x;
    private double y;

    public Ponto2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Ponto2D() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public String toString() {
        return "x=" + x + ", y=" + y;
    }
}

public class Ponto3D extends Ponto2D {
    private double z;

    public Ponto3D(double x, double y, double z) {
        super(x, y);          // chama o construtor Ponto2D(x, y)
        this.z = z;
    }

    public Ponto3D() {        // aqui o super() é IMPLÍCITO (chama Ponto2D())
        this.z = 0.0;
    }

    public String toString() {
        return super.toString() + ", z=" + z;   // reaproveita o toString da pai
    }
}
```

**Por que usar `super(x, y)`?** Os atributos `x` e `y` são `private` em `Ponto2D`, então `Ponto3D` **não consegue mexer neles diretamente**. Quem sabe inicializá-los é o construtor de `Ponto2D`. Então a subclasse "pede" a ele.

### Regras do `super(...)` em construtores (cai em prova!)

1. **Só pode ser chamado dentro de construtores** de subclasses.
2. **Precisa ser a primeira linha** do construtor.
3. O construtor de uma subclasse **sempre** chama o construtor da superclasse, **mesmo que você não escreva**. Se você não escrever, o Java insere automaticamente `super()` (sem argumentos).

> ⚠️ **Cuidado com a regra 3:** se a superclasse **não tiver** construtor sem argumentos (só tem construtores com parâmetros) e você não chamar `super(...)` explicitamente, **dá erro de compilação**. Nesse caso, você é obrigado a chamar `super(args)` na primeira linha.

**Ordem de execução ao criar `new Ponto3D(1, 2, 3)`:**

```
1. Ponto3D(...) começa
2. super(x, y) → executa Ponto2D(x, y)  (inicializa x e y)
3. volta para Ponto3D → this.z = z      (inicializa z)
```

A superclasse é sempre construída **primeiro** (faz sentido: a parte "mais geral" precisa existir antes da especializada).

### Uso 2: chamar método da superclasse

```java
super.nomeDoMetodo(par1, ..., parN)
```

No exemplo: `super.toString()` chama o `toString` de `Ponto2D`, e o `Ponto3D` só **completa** com `", z=" + z`.

---

## 6. Sobreposição de métodos (*override*)

- Declarar, na subclasse, um método com a **mesma assinatura** (mesmo nome + mesmos parâmetros) de um método da classe ancestral.
- Chama-se **sobreposição** (ou **superposição**). Em inglês: *override*.

**Por que sobrepor?** Porque métodos das classes herdeiras costumam fazer **tarefas adicionais** que o método da ancestral não faz. Ex.: `Ponto3D.toString()` precisa mostrar também o `z`.

### Padrão muito comum

```java
@Override
public String toString() {
    return super.toString() + ", z=" + z;   // o que a pai faz + o que é novo
}
```

> 💡 A anotação `@Override` é opcional, mas **boa prática**: se você errar a assinatura sem querer, o compilador avisa.

### ⚠️ Não confunda

| Termo | O que é |
|---|---|
| **Sobreposição** (*override*) | Mesma assinatura, em classes **diferentes** (pai e filha) |
| **Sobrecarga** (*overload*) | Mesmo nome, parâmetros **diferentes**, na mesma classe (ex.: os dois construtores de `Ponto2D`) |

---

## 7. Prática 2: `Pessoa` → `Funcionario` → `ChefeDeDepartamento`

**Enunciado resumido:**

- `Pessoa`: atributos `nome` e `identidade`; métodos `getNome`, `getIdentidade`, `toString`.
- `Funcionario` herda de `Pessoa`; acrescenta `salario` e `getSalario`.
- `ChefeDeDepartamento` herda de `Funcionario`; acrescenta `departamento` e `getDepartamento`.
- Em uma classe executável, criar uma instância de cada e imprimir com `toString`.

<details>
<summary><b>Ver solução</b> (tente fazer antes!)</summary>

```java
public class Pessoa {
    private String nome;
    private String identidade;

    public Pessoa(String nome, String identidade) {
        this.nome = nome;
        this.identidade = identidade;
    }

    public String getNome() { return nome; }
    public String getIdentidade() { return identidade; }

    public String toString() {
        return "Nome: " + nome + ", Identidade: " + identidade;
    }
}
```

```java
public class Funcionario extends Pessoa {
    private double salario;

    public Funcionario(String nome, String identidade, double salario) {
        super(nome, identidade);        // obrigatório: Pessoa não tem construtor vazio
        this.salario = salario;
    }

    public double getSalario() { return salario; }

    @Override
    public String toString() {
        return super.toString() + ", Salário: " + salario;
    }
}
```

```java
public class ChefeDeDepartamento extends Funcionario {
    private String departamento;

    public ChefeDeDepartamento(String nome, String identidade,
                               double salario, String departamento) {
        super(nome, identidade, salario);
        this.departamento = departamento;
    }

    public String getDepartamento() { return departamento; }

    @Override
    public String toString() {
        return super.toString() + ", Departamento: " + departamento;
    }
}
```

```java
public class Principal {
    public static void main(String[] args) {
        Pessoa p = new Pessoa("Ana", "123");
        Funcionario f = new Funcionario("Bruno", "456", 3500.0);
        ChefeDeDepartamento c = new ChefeDeDepartamento("Carla", "789", 8000.0, "TI");

        System.out.println(p);
        System.out.println(f);
        System.out.println(c);

        // Métodos herdados funcionando:
        System.out.println(c.getNome());          // vem de Pessoa
        System.out.println(c.getSalario());       // vem de Funcionario
        System.out.println(c.getDepartamento());  // próprio
    }
}
```

**Repare na "corrente" de `toString`:** `Chefe` chama `Funcionario`, que chama `Pessoa`. Cada nível acrescenta a sua parte.

</details>

---

## 8. A classe `Object`

- **Todas** as classes em Java herdam, direta ou indiretamente, de `java.lang.Object`.
- Se você não escreve `extends`, o Java assume `extends Object`.
- Por isso **toda classe** já tem os métodos de `Object`, mesmo que você não os tenha escrito.

**Métodos herdados** (o slide cita 11, alguns sobrecarregados):

`equals`, `hashCode`, `toString`, `wait`, `notify`, `notifyAll`, `getClass`, `finalize` e `clone`

### Conexão com o que já vimos

O `toString()` que sobrepomos nas classes vem **de `Object`**. Sem sobrepor, ele imprime algo como `Pessoa@1b6d3586` (nome da classe + código hash). Por isso sobrepomos: para imprimir algo útil.

```
Object
  └── Pessoa
        └── Funcionario
              └── ChefeDeDepartamento
```

---

## 9. Modificador de acesso `protected`

### Recapitulando os acessos

| Modificador | Quem acessa |
|---|---|
| `private` | Só a **própria classe** |
| `protected` | A própria classe + **classes herdeiras** (subclasses) |
| `public` | **Qualquer** classe |

O slide define: `protected` funciona como `private`, **exceto** que classes herdeiras também têm acesso ao campo ou método.

### Exemplo do problema

```java
class Pessoa {
    private String nome;
}

class Funcionario extends Pessoa {
    void mostrar() {
        System.out.println(nome);   // ❌ ERRO: nome é private em Pessoa
    }
}
```

Com `protected`:

```java
class Pessoa {
    protected String nome;
}

class Funcionario extends Pessoa {
    void mostrar() {
        System.out.println(nome);   // ✅ OK: subclasse acessa
    }
}
```

> 📌 **Detalhe extra (Java):** em Java, `protected` também permite acesso a classes **do mesmo pacote**, não só a subclasses. O slide simplifica dizendo "como private, mas herdeiras também acessam".

### Quando usar `protected`?

- Quando você **quer** que as subclasses mexam diretamente no atributo.
- ⚠️ Mas lembre: `protected` **enfraquece o encapsulamento** (as subclasses passam a depender dos detalhes internos). Muitas vezes é mais seguro manter `private` e oferecer **getters/setters** ou chamar `super`.

---

## 10. Prática 3: `Automovel` → `AutomovelBasico` → `AutomovelDeLuxo`

**Enunciado resumido:**

- `Automovel`: `ano`, `modelo`, `cor`; método `quantoCusta` (preço baseado no ano).
- `AutomovelBasico` herda de `Automovel`; acrescenta `airbag` e `radio`. Seu `quantoCusta` = custo como `Automovel` + custo dos acessórios básicos.
- `AutomovelDeLuxo` herda de `AutomovelBasico`; acrescenta `arCondicionado` e `direcaoHidraulica`. Seu `quantoCusta` = custo como `AutomovelBasico` + acessórios de luxo. Também tem `toString` com **todos** os atributos (inclusive herdados) e o custo.
- Em uma classe executável: criar um `AutomovelDeLuxo` e imprimir com `toString` e `quantoCusta`.
- As fórmulas de preço ficam a critério do aluno (as abaixo são inventadas).

<details>
<summary><b>Ver solução</b> (tente fazer antes!)</summary>

```java
public class Automovel {
    protected int ano;
    protected String modelo;
    protected String cor;

    public Automovel(int ano, String modelo, String cor) {
        this.ano = ano;
        this.modelo = modelo;
        this.cor = cor;
    }

    public double quantoCusta() {
        return 20000 + (ano - 2000) * 1000;     // quanto mais novo, mais caro
    }
}
```

```java
public class AutomovelBasico extends Automovel {
    protected boolean airbag;
    protected boolean radio;

    public AutomovelBasico(int ano, String modelo, String cor,
                           boolean airbag, boolean radio) {
        super(ano, modelo, cor);
        this.airbag = airbag;
        this.radio = radio;
    }

    @Override
    public double quantoCusta() {
        double custo = super.quantoCusta();     // custo como Automovel
        if (airbag) custo += 1500;
        if (radio)  custo += 800;
        return custo;
    }
}
```

```java
public class AutomovelDeLuxo extends AutomovelBasico {
    private boolean arCondicionado;
    private boolean direcaoHidraulica;

    public AutomovelDeLuxo(int ano, String modelo, String cor,
                           boolean airbag, boolean radio,
                           boolean arCondicionado, boolean direcaoHidraulica) {
        super(ano, modelo, cor, airbag, radio);
        this.arCondicionado = arCondicionado;
        this.direcaoHidraulica = direcaoHidraulica;
    }

    @Override
    public double quantoCusta() {
        double custo = super.quantoCusta();     // custo como AutomovelBasico
        if (arCondicionado)    custo += 3000;
        if (direcaoHidraulica) custo += 2000;
        return custo;
    }

    @Override
    public String toString() {
        return "Modelo: " + modelo + ", Ano: " + ano + ", Cor: " + cor
             + ", Airbag: " + airbag + ", Rádio: " + radio
             + ", Ar: " + arCondicionado + ", Direção: " + direcaoHidraulica
             + ", Custo: R$ " + quantoCusta();
    }
}
```

```java
public class Principal {
    public static void main(String[] args) {
        AutomovelDeLuxo carro = new AutomovelDeLuxo(
            2024, "Sedan X", "Preto", true, true, true, true);

        System.out.println(carro);
        System.out.println("Custo: " + carro.quantoCusta());
    }
}
```

**Por que `ano`, `modelo`, `cor`, `airbag` e `radio` são `protected`?** Porque o `toString` de `AutomovelDeLuxo` precisa **ler** esses campos herdados. Este é o uso clássico de `protected`.

**Cadeia de `quantoCusta`:**
`Luxo.quantoCusta()` → chama `Basico.quantoCusta()` → chama `Automovel.quantoCusta()`. Cada um soma sua parte.

</details>

---

## 11. Delegação × Herança (comparação final)

| | **Delegação (composição)** | **Herança** |
|---|---|---|
| Relação | "**tem um**" | "**é um tipo de**" |
| Como | Atributo do tipo da classe base | `extends` |
| Métodos da base | Você escreve métodos que **repassam** | Vêm **automaticamente** |
| Acoplamento | Mais fraco | Mais forte (filha depende da pai) |
| Encapsulamento | Preservado (expõe só o que quer) | Pode ser enfraquecido (`protected`) |
| Flexibilidade | Dá para trocar o objeto interno depois | Fixada na compilação |
| Quantas bases em Java | Quantas quiser | **Só uma** superclasse |
| Exemplo | `DataHora` tem `Data` e `Hora` | `Programador` é um `Funcionario` |

### Dica de ouro

> Use **herança** quando a relação "é um tipo de" for **verdadeira e estável**.
> No restante dos casos, prefira **composição + delegação**.

---

## 12. Resumo para revisão rápida

- **Reúso** = aproveitar classes existentes em vez de reescrever.
- Dois mecanismos em Java: **delegação/composição** ("tem um") e **herança** ("é um tipo de").
- **Delegação:** instância da classe base como atributo + métodos que repassam a chamada.
- **Herança:** `class Filha extends Pai`. Filha reaproveita campos e métodos da Pai e descreve só o que é diferente.
- **`super(...)`:** chama construtor da pai. **Só em construtor, primeira linha.** Se omitido, o Java chama `super()` sozinho.
- **`super.metodo()`:** chama o método da pai.
- **Sobreposição (*override*):** mesma assinatura na subclasse para acrescentar comportamento. (≠ sobrecarga)
- **`Object`:** raiz de todas as classes; de lá vêm `toString`, `equals`, `hashCode`...
- **`protected`:** como `private`, mas subclasses também acessam.

---

## 13. Perguntas para fixar

1. Qual a diferença entre composição e delegação?
2. Por que não é boa ideia colocar `tituloTese` e `orientador` direto em `RegistroAcademico`?
3. Como decidir entre herança e delegação? Dê um exemplo de cada.
4. O que acontece se uma subclasse não chama `super(...)` explicitamente?
5. Por que `super(...)` precisa ser a primeira linha do construtor?
6. Qual a diferença entre sobreposição e sobrecarga?
7. De qual classe todas as classes Java herdam? Cite 3 métodos que ela fornece.
8. Qual a diferença entre `private`, `protected` e `public`?
9. Por que, na Prática 3, os atributos de `Automovel` foram marcados como `protected`?

---

