package Semestre4.POO.Unidade1.Lista1.Q14;

import java.util.Scanner;

public class Fibonacci {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int antecessor1 = 0;
        int antecessor2 = 1;

        System.out.println("Digite o n-ésimo valor da sequência de Fibonacci: ");
        int nesimoValor = input.nextInt();

        for (int i = 1; i <= nesimoValor; i++){
            int proximo = antecessor1 + antecessor2;
            antecessor1 = antecessor2;
            antecessor2 = proximo;
        }

        System.out.println("O número na posição "+ nesimoValor + " é "+ antecessor1);

        input.close();
    }
}
