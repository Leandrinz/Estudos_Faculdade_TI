# Aula 03 - 20/08/2026

## Estruturas de Repetição ou Iteração

### Contadores
Variável especial usada em repetições

Ex:
```Java
int contador = 0;
```

### While
Bom para usar quando precisamos verificar a condição no **INÍCIO**

```Java
while (condição){
    // instrução a serem repetidas
}
```

### do-while:
Executa ao menos uma vez

```Java
do{
    // instruções a serem repetidas
}while(condição)
```

### for:

```Java
for (inicialização; verificação_de_condições; atualizações){
    // instruções a serem repetidas
}

Exemplo:

for (int i = 1; i < 11; i++){
    System.out.println(i); // Vai mostrar de 1 a 10
}
```

### break e continue

**break**
Utiliza em um laço de repetição, quando usado ele **SAI do laço**
```Java
break;
```

**continue**
Utiliza em um laço de repetição, quando usado ele **PULA PARA A PRÓXIMA ITERAÇÃO**

```Java
continue;
```

## Funções

```Java
public static [tipoDoRetorno][nomeDaFuncao]([lista de parâmetros]){
    ...
    return [valor de retorno]; // Lembre-se de ser do mesmo tipo da função
}
```

Se não vai retornar nada, o tipo é **void**
