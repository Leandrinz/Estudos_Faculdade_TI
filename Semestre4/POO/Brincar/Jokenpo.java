package Semestre4.POO.Brincar;

import java.util.Random;
import java.util.Scanner;

public class Jokenpo {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Random random = new Random();
        String escolha[] = {"Pedra", "Papel", "Tesoura"};

        int escolhaMaquina;
        int escolhaJogador;
        int totalVitorias = 0;
        int totalDerrotas = 0;

        while (true) {
            System.out.println("==========================");
            System.out.println("JOKENPO");
            System.out.println("==========================");

            escolhaMaquina = random.nextInt(3);

            for (int i = 0; i < 3; i++){
                System.out.printf("%d - %s\n", i+1, escolha[i]);
            }
            System.out.println("4 - Sair");
            escolhaJogador = (input.nextInt() - 1);

            if (escolhaJogador == 0 && escolhaMaquina == 1){
                totalDerrotas++;
                System.out.println("Vitória da máquina!!!");
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 0 && escolhaMaquina == 2){
                totalVitorias++;
                System.out.println("Vitória do Jogador!!!");
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 1 && escolhaMaquina == 0){
                totalVitorias++;
                System.out.println("Vitória do Jogador!!!");
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 1 && escolhaMaquina == 2){
                totalDerrotas++;
                System.out.println("Vitória da máquina!!!");
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 2 && escolhaMaquina == 0){
                totalDerrotas++;
                System.out.println("Vitória da máquina!!!");
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 2 && escolhaMaquina == 1){
                totalVitorias++;
                System.out.println("Vitória do Jogador!!!");
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == escolhaMaquina){
                System.out.println("Empate!!!");
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 3){
                System.out.println("Finalizando  o programa...");
                break;
            }
            else{
                System.out.println("Escolha inválida!!!");
            }
            System.out.println("Total de Vitórias: "+totalVitorias);
            System.out.println("Total de Derrotas: "+totalDerrotas);

        }
        input.close();
    }
}
