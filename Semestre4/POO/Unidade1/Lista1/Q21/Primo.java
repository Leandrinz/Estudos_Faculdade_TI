package Semestre4.POO.Unidade1.Lista1.Q21;

import java.util.Scanner;

public class Primo {
    public static boolean ehPrimoRecursivo(int numero, int restante, int divisores){
        if (restante == 0){
            if (divisores == 2){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if (numero % restante == 0){
                divisores++;
            }
            return ehPrimoRecursivo(numero, restante - 1, divisores);
        }
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Digite o número e verifique se é primo: ");

        int numero = input.nextInt();
        boolean ehPrimo = ehPrimoRecursivo(numero, numero, 0);

        if (ehPrimo == true){
            System.out.println(numero + " é primo");
        }
        else{
            System.out.println(numero + " não é primo");
        }
        input.close();
    }
}
