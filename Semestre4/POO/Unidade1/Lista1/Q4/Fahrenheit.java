package Semestre4.POO.Unidade1.Lista1.Q4;

import java.util.Scanner;

public class Fahrenheit {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        double celsius;
        System.out.println("Digite a temperatura em Celsius: ");
        celsius = input.nextDouble();

        double fahrenheit = celsius * 1.8 + 32;

        System.out.println("Fahrenheit: "+fahrenheit);


        input.close();
    }
}
