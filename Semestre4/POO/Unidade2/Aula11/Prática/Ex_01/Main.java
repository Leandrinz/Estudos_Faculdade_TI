package Semestre4.POO.Unidade2.Aula11.Prática.Ex_01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class Main {
    public static void verificaString(Collection<String> listinha, String nome){
        boolean achou = false;
        for (String nomeLista : listinha){
            if (nomeLista == nome){
                achou = true;
                break;
            }
        }
        if (achou == true){
            System.out.println(nome + " Está na lista!!!");
        }
        else{
            System.out.println(nome + " não está na lista!!!");
        }
    }

    public static void imprimir(Collection<String> lista){
        for (String nome : lista){
            System.out.print(nome + " | ");
        }
        System.out.println(" ");
    }
    public static void main(String[] args) {
        ArrayList<String> lista1 = new ArrayList<>(Arrays.asList("Laranja", "Azul", "Vermelho", "Amarelo", "Roxo", "Preto", "Branco"));

        ArrayList<String> lista2 = new ArrayList<>(Arrays.asList("Verde", "Marrom", "Vermelho", "Cinza", "Roxo", "Preto", "Rosa"));

        System.out.println("============= Lista 1 ==============");
        imprimir(lista1);
        lista1.sort(Comparator.naturalOrder());
        imprimir(lista1);
        verificaString(lista1, "Azul");

        System.out.println("============= Lista 2 ==============");
        imprimir(lista2);
        lista2.sort(Comparator.naturalOrder());
        imprimir(lista2);
        verificaString(lista2, "Roxo");
    }
}
