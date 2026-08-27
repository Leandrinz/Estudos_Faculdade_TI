package Semestre4.POO.Unidade1.Lista1.Q7;

import java.util.Scanner;

public class NumeroMenor {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int menor = 0;

        for (int i = 0; i <= 2; i++){
            System.out.println("Digite o "+(i + 1)+"º número: ");
            int temp;
            temp = input.nextInt();
            if (i == 0){
                menor = temp;
            }
            else{
                if (temp < menor){
                    menor = temp;
                }
            }
        }

        System.out.println("O menor número digitado foi: "+menor);

        input.close();
    }
}
