package Semestre4.POO.Unidade1.Lista1.Q6;

import java.util.Scanner;

public class DHM {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int minutos;
        System.out.println("Digite o total de minutos");
        minutos = input.nextInt();

        int dias = minutos / 1440;

        int minutosPosdia = minutos - (1440 * dias);

        int horas = minutosPosdia / 60;

        int minutosFinais = minutosPosdia - (60 * horas);

        System.out.println(minutos+" minutos = "+dias+" dias, "+horas+" horas, "+minutosFinais+" minutos");

        input.close();
    }
}
