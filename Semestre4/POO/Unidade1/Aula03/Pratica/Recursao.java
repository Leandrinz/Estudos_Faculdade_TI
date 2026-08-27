package Semestre4.POO.Unidade1.Aula03.Pratica;

import java.util.Scanner;

public class Recursao {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int num;
        int fat;

        System.out.println("Informe o número: ");
        num = input.nextInt();
        fat = fatorial(num);
        System.out.println("Fatorial de "+num+" = "+fat);


        input.close();
    }

    public static int fatorial(int n){
        if (n == 0){
            return 1;
        }
        else{
            return n * fatorial(n-1);
        }
    }
}
