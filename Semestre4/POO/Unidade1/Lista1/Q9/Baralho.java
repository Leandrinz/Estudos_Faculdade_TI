package Semestre4.POO.Unidade1.Lista1.Q9;

import java.util.Scanner;

public class Baralho {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        System.out.println("Digite o Valor da Carta: ");
        int valorDaCarta = input.nextInt();
        System.out.println("Digite o naipe da carta:\n 1 - Ouros\n2 - Paus\n3 - Copas\n4 - Espadas");
        int naipeDaCarta = input.nextInt();

        switch (valorDaCarta) {
            case 1:
                System.out.print("Ás de ");
                break;
            case 2:
                System.out.print("Dois de ");
                break;
            case 3:
                System.out.print("Três de ");
                break;
            case 4:
                System.out.print("Quatro de ");
                break;
            case 5:
                System.out.print("Cinco de ");
                break;
            case 6:
                System.out.print("Seis de ");
                break;
            case 7:
                System.out.print("Sete de ");
                break;
            case 8:
                System.out.print("Oito de ");
                break;
            case 9:
                System.out.print("Nove de ");
                break;
            case 10:
                System.out.print("Dez de ");
                break;
            case 11:
                System.out.print("Valete de ");
                break;
            case 12:
                System.out.print("Dama de ");
                break;
            case 13:
                System.out.print("Rei de ");
                break;
            default:
                break;
        }

        switch(naipeDaCarta){
            case 1:
                System.out.print("Ouros");
                break;
            case 2:
                System.out.print("Paus");
                break;
            case 3:
                System.out.print("Copas");
                break;
            case 4:
                System.out.print("Espadas");
                break;
        }
        input.close();
    }
}
