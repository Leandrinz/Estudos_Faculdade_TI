package Semestre4.POO.Unidade2.ListaI.Q5;

import java.util.Random;

public class SimulacaoDados {
    public static void main(String[] args) {
        Random randomNumbers = new Random();

        int[] frequencias = new int[13];

        long totalLancamentos = 36_000_000L;

        for (long i = 0; i < totalLancamentos; i++){
            int dado1 = 1 + randomNumbers.nextInt(6);

            int dado2 = 1 + randomNumbers.nextInt(6);

            int soma = dado1 + dado2;
            frequencias[soma]++;
        }

        System.out.println("Soma\tFrequência\tPorcentagem");
        System.out.println("------------------------------");

        for (int soma = 2; soma <= 12; soma++){
            double percentagem = (frequencias[soma] * 100.0) / totalLancamentos;
            System.out.printf("%d\t%d\t\t%.2f%%\n", soma, frequencias[soma], percentagem);
        }
    }
}
