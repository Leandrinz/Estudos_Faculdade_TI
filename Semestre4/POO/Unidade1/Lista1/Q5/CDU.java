package Semestre4.POO.Unidade1.Lista1.Q5;

import java.util.Scanner;

public class CDU {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int numero;
        System.out.println("Digite o número: ");
        numero = input.nextInt();

        int centena = numero / 100;

        int dezena = (numero - (centena * 100)) / 10;

        int unidade = (numero - ((centena * 100) + (dezena * 10)));

        int novaCentena = unidade * 100;
        int novadezena = centena * 10;
        int novaUnidade = dezena;

        int numeroUCD = novaCentena + novadezena + novaUnidade;

        System.out.println("Número CDU = "+numero);
        System.out.println("Número UCD = "+numeroUCD);

        input.close();
    }
}
