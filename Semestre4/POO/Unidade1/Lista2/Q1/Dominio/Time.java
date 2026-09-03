package Semestre4.POO.Unidade1.Lista2.Q1.Dominio;

import java.util.Scanner;

public class Time {
    Scanner input = new Scanner(System.in);

    public String nomeDoTime;
    public int numeroJogadores;
    public int numeroTitulos;
    public int numeroDePontos;

    public Time(String nomeDoTime){
        this.nomeDoTime = nomeDoTime;
    }

    public void imprime(){
        System.out.println("Nome do Time: "+ this.nomeDoTime);
        System.out.println("Número de Jogadores: "+ this.numeroJogadores);
        System.out.println("Número de Títulos: "+ this.numeroTitulos);
        System.out.println("Número de Pontos: "+ this.numeroDePontos);
    }

    public void inicializarDados(){
        System.out.println("Digite o número de Jogadores do "+ this.nomeDoTime);
        this.numeroJogadores = input.nextInt();
        System.out.println("Digite o número de títulos do "+ this.nomeDoTime);
        this.numeroTitulos = input.nextInt();
        System.out.println("Digite o número de Pontos do "+ this.nomeDoTime);
        this.numeroDePontos = input.nextInt();
    }
}
