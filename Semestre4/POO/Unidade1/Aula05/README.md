# Aula 05 - 03/09/2026

## Utilizando objetos:

```Java
identificador.metodo;
```

Exemplo:

```Java
calculadora.somaDoisNumeros();
```

`somaDoisNumeros` -> método

## Construtor:
Quando fazemos:

```Java
Classe objeto = new Classe();
```

Usamos o `new`, que é um construtor. PORÉM, ele não inicializa nada, apenas aloca nosso objeto na memória

### Criando um novo construtor
Dentro de uma classe, por exemplo `Anime` fazemos:

```Java
public class Anime {
    atributo1;
    atributo2;
    .
    .
    .
    atributoN;

    // Escrevemos nosso construtor
    public Anime(tipo parametro1, tipo parametro2, ..., tipo parametroN){
        this.atributo1 = parametro1;
        this.atributo2 = parametro2;
        this.atributo3 = parametro3;
        ...
        this.atributoN = parametroN
    }
}
```

> Observação: O nome do construtor deve ser IGUAL ao nome da CLASSE

### Chamando no executável:
Agora quando chamarmos, passamos os parâmetros dentro do construtor

```Java
Classe objeto = new Classe(parametro1, parametro2, ..., parametroN);
```

