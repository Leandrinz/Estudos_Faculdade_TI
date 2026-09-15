package Semestre4.POO.Unidade1.Lista1.Q1;

import java.util.Scanner;

public class Area {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int lado;

        System.out.println("Digite o lado do quadrado: ");
        lado = input.nextInt();

        System.out.println("Calculando a área...");
        System.out.println("Área de um quadrado com lado "+lado+" = "+lado*lado);

        input.close();
    }
}

