package Semestre4.POO.Unidade1.Lista1.Q19;

import java.util.Scanner;

public class Fatorial {
    public static void imprime(int numero, int resultadoFatorial){
        System.out.print("\t".repeat(numero) + numero + "! = " + resultadoFatorial);
        System.out.println(" ");
    }

    public static int fatorialRecursivo(int numero){
        if (numero == 0 || numero == 1){
            return 1;
        }
        else{
            return (numero * fatorialRecursivo(numero - 1));
        }
    }


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.println("Digite o número: ");
        int numero = input.nextInt();

        for (int i = 0; i <= numero; i++){
            int resultadoFatorial = fatorialRecursivo(i);
            imprime(i, resultadoFatorial);
        }

        input.close();
    }
}
