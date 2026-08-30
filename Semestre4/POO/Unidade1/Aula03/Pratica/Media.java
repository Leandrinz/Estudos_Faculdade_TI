package Semestre4.POO.Unidade1.Aula03.Pratica;

import java.util.Scanner;

public class Media {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        double d1, d2, d3;

        System.out.println("Digite o número 1:");
        d1 = input.nextDouble();
        System.out.println("Digite o número 2:");
        d2 = input.nextDouble();
        System.out.println("Digite o número 3:");
        d3 = input.nextDouble();

        double media = media(d1, d2, d3);

        System.out.println("Média = "+media);
        input.close();
    }

    public static double media (double d1, double d2, double d3){
        return ((d1 + d2 + d3) / 3);
    }
}
