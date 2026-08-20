package Semestre4.POO.Unidade1.Aula03.Pratica;

import java.util.Scanner;

public class Programa3 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int numero;
        System.out.println("Digite o número: ");
        numero = input.nextInt();

        for (int i = 0; i <= numero; i++){
            int contador = 1;
            for (int v = i; v >= 1; v--){
                contador *= v;
            }
            System.out.println("Fatorial ("+ i + ") = "+ contador);
        }
        input.close();
    }
}
