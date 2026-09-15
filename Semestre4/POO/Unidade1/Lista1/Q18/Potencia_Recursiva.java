package Semestre4.POO.Unidade1.Lista1.Q18;

import java.util.Scanner;

public class Potencia_Recursiva {
    public static int potencia(int base, int expoente){
        if (expoente == 1){
            return base;
        }
        else{
            return potencia(base * base, expoente - 1);
        }
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Digite o número: ");
        int numeroDesejado = input.nextInt();
        System.out.println("Digite o expoente de "+ numeroDesejado);
        int expoente = input.nextInt();
        while (true) {
            if (expoente < 1){
                System.out.println("O expoente deve ser igual ou maior que 1!!!");
                System.out.println("Digite o expoente de "+ numeroDesejado);
                expoente = input.nextInt();
            }
            else{
                break;
            }
        }
        int resultado = potencia(numeroDesejado, expoente);
        System.out.println(numeroDesejado + "^"+expoente+ "= "+ resultado);
        
        input.close();
    }
}
