package Semestre4.POO.Unidade1.Aula02.Pratica;
import java.util.Scanner;


public class Programa02 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Informe os valores de x1: ");
        int x1 = input.nextInt();
        System.out.print("Informe os valores de x2: ");
        int x2 = input.nextInt();

        System.out.print("Informe os valores de y1: ");
        int y1 = input.nextInt();
        System.out.print("Informe os valores de y2: ");
        int y2 = input.nextInt();

        double distancia = Math.sqrt((Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));

        System.out.println("Distância = "+distancia);
        input.close();
    }
}
