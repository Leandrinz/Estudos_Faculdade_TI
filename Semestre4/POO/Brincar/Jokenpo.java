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
        int partidas = 0;

        while (partidas < 11) {
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
                partidas++;
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 0 && escolhaMaquina == 2){
                totalVitorias++;
                partidas++;
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 1 && escolhaMaquina == 0){
                totalVitorias++;
                partidas++;
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 1 && escolhaMaquina == 2){
                totalDerrotas++;
                partidas++;
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 2 && escolhaMaquina == 0){
                totalDerrotas++;
                partidas++;
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == 2 && escolhaMaquina == 1){
                totalVitorias++;
                partidas++;
                System.out.println("Escolha da máquina -> "+ escolha[escolhaMaquina]);
                System.out.println("Sua escolha -> "+escolha[escolhaJogador]);
            }
            else if (escolhaJogador == escolhaMaquina){
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
            System.out.println("Partidas "+partidas+"/"+11);
            System.out.println("Total de Vitórias: "+totalVitorias);
            System.out.println("Total de Derrotas: "+totalDerrotas);
        }
        if (totalVitorias > totalDerrotas){
            System.out.println(" ");
            System.out.println("===================================");
            System.out.println("VOCÊ VENCEU!!!!");
            System.out.println("Total de Vitórias: "+totalVitorias);
            System.out.println("Total de Derrotas: "+totalDerrotas);
            System.out.println("===================================");
        }
        else if (totalDerrotas > totalVitorias){
            System.out.println(" ");
            System.out.println("===================================");
            System.out.println("VOCÊ PERDEU!!!!");
            System.out.println("Total de Vitórias: "+totalVitorias);
            System.out.println("Total de Derrotas: "+totalDerrotas);
            System.out.println("===================================");
        }
        input.close();
    }
}
