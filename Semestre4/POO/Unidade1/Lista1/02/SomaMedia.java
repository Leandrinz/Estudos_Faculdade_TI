package Semestre4.POO.Unidade1.Lista1.Q2;

import java.util.Scanner;

public class SomaMedia {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        double n1, n2, n3;

        System.out.println("Digite a nota 1: ");
        n1 = input.nextDouble();
        System.out.println("Digite a nota 2: ");
        n2 = input.nextDouble();
        System.out.println("Digite a nota 3: ");
        n3 = input.nextDouble();

        double media = calculaMedia(n1, n2, n3);

        System.out.println("Média: "+media);

        input.close();
    }

    public static double calculaMedia (double nota1, double nota2, double nota3){
        double soma = nota1 + nota2 + nota3;
        return (soma / 3);
    }
}
