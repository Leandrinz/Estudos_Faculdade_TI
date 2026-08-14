# Aula 02 - 14/08/2026

## Comentários

*Comentário de uma linha*
```Java
// Exemplo
```

*Comentários de várias linhas*
```Java
/*
Isso é um comentário de
múltiplas linhas
 */
```

## Variáveis
- Não pode iniciar com números
- Não pode possuir caracteres especiais
- Não pode possuir espaços em branco

## Constantes:

```Java
final double ACELERACAO_GRAVIDADE = 9.78;
```

## Entrada e Saída de dados:

### Exibir texto

```Java
System.out.print();
System.out.println(); // Salta linha
System.out.printf(); // Igual o do C. Para saltar linha use %n
```

### Leitura de dados

```Java
import java.util.Scanner; // Importa

public class NomeDaClasse{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in); // Cria a conexão
        String str = input.next(); // Lê apenas uma palavra

        input.close(); // Fecha a conexão
    }
}
```

## Expressões Aritméticas

```terminal
Multiplicação - *
Divisão inteira - /
Resto - %
Adição - +
Subtração - -
Atribuição - =

Use parênteses () se quiser alterar isso
```

## Operadores lógicos

```Java
&& - E
|| - OU
! - Negação
```

## Estruturas Condicionais

```Java
if... else...

switch(número){
    case valor1:
        break;
    case valor2:
        break;
    ...
}
```