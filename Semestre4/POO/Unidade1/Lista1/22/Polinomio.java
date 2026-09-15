package Semestre4.POO.Unidade1.Lista1.Q22;

import java.util.Scanner;

public class Polinomio {

    public static double calcularPolinomio(int n, double[] coeficientes, double x) {

        if (n == 0) {
            return coeficientes[0];
        }

        return x * calcularPolinomio(n - 1, coeficientes, x)
                + coeficientes[n];
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o grau do polinomio: ");
        int n = scanner.nextInt();

        double[] coeficientes = new double[n + 1];

        for (int i = 0; i <= n; i++) {
            System.out.print("Digite o coeficiente a" + i + ": ");
            coeficientes[i] = scanner.nextDouble();
        }

        System.out.print("Digite o valor de x: ");
        double x = scanner.nextDouble();

        double resultado = calcularPolinomio(n, coeficientes, x);

        System.out.println("Resultado: " + resultado);

        scanner.close();
    }
}

