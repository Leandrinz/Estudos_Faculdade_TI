package Semestre4.POO.Unidade1.Lista1.Q12;

import java.util.Scanner;

public class Dia_Correspondente {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Entrada: ");
        int dia = input.nextInt();

        int resto = dia % 7;
        System.out.print("O dia " + dia + " será uma ");

        switch (resto){
            case 0:
                System.out.println("Sábado");
                break;
            case 1:
                System.out.println("Domingo");
                break;
            case 2:
                System.out.println("Segunda-Feira");
                break;
            case 3:
                System.out.println("Terça-Feira");
                break;
            case 4:
                System.out.println("Quarta-Feira");
                break;
            case 5:
                System.out.println("Quinta-Feira");
                break;
            case 6:
                System.out.println("Sexta-Feira");
                break;
        }
        input.close();
    }
}
