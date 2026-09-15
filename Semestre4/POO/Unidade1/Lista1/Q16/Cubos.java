package Semestre4.POO.Unidade1.Lista1.Q16;

import java.util.Scanner;

public class Cubos {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int numeroDesejado;
        System.out.println("Digite um número: ");
        numeroDesejado = input.nextInt();
        boolean achado = false;

        for (int i = 1; i <= numeroDesejado; i++){
            for (int j = 1; j <= numeroDesejado; j++){
                for (int l = 1; l <= numeroDesejado; l++){
                    int soma = (i * i * i) + (j * j * j) + (l * l * l);
                    if (soma == numeroDesejado && achado == false){
                        System.out.println(numeroDesejado + " = " + i + "^3 " + "+ " + j + "^3 +" + l + "^3");
                        achado = true;
                    }
                }
            }
        }
        if (achado == false){
            System.out.println("Infelizmente não achamos uma combinação compatível!!!");
        }
        input.close();
    }
}
