package Semestre4.POO.Unidade2.Aula11.Prática.Ex_05;

import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        TreeSet<String> cores = new TreeSet<>();

        cores.add("Verde");
        cores.add("Azul");
        cores.add("Amarelo");
        cores.add("Vermelho");
        cores.add("Azul");
        cores.add("Roxo");
        cores.add("Laranja");

        System.out.println("==== CONJUNTO ====");
        System.out.println(cores);

        String escolhida = "Azul";
        System.out.println("Antes de " + escolhida + ": " + cores.headSet(escolhida));

        System.out.println("Depois de  " + escolhida + ": " + cores.tailSet(escolhida));

        System.out.println("Primeiro: " + cores.first());

        System.out.println("Último: " + cores.last());

    }
}
