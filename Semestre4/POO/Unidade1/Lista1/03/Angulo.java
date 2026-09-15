package Semestre4.POO.Unidade1.Lista1.Q3;

import java.util.Scanner;

public class Angulo {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        double graus;

        System.out.println("Digite o ângulo em graus: ");
        graus = input.nextDouble();

        double radianos = Math.toRadians(graus);

        double seno = Math.sin(radianos);
        double cosseno = Math.cos(radianos);
        double tangente = Math.tan(radianos);

        double cossecante = 1 / Math.sin(radianos);
        double secante = 1 / Math.cos(radianos);
        double cotangente = 1 / Math.tan(radianos);

        System.out.printf("Ângulo |Radianos |Seno |Cosseno |Tangente |Cossecante |Secante |Cotangente \n");
        System.out.printf("%.2f\t|%.2f\t|%.2f\t|%.2f\t|%.2f\t|%.2f\t|%.2f\t|%.2f", graus, radianos, seno, cosseno, tangente, cossecante, secante, cotangente);

        input.close();
    }


}
