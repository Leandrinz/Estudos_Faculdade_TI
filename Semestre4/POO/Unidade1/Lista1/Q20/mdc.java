package Semestre4.POO.Unidade1.Lista1.Q20;

import java.util.Scanner;

public class mdc {
    public static int mdcRecursivo(int n, int m){
            if (n > m){
                return mdcRecursivo(m, n);
            }
            else if (n == 0){
                return m;
            }
            else{
                return mdcRecursivo(n, m%n);
            }
        }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Digite um número: ");
        int numero = input.nextInt();

        System.out.println("Digite um divisor para " + numero);
        int divisor = input.nextInt();

        int maiorDivisorComum = mdcRecursivo(numero, divisor);
        System.out.println("O maior mdc entre " + numero + " e " + divisor + " é " + maiorDivisorComum);

        input.close();
    }
}
