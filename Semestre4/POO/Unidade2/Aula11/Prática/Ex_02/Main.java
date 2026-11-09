package Semestre4.POO.Unidade2.Aula11.Prática.Ex_02;

import java.util.Iterator;
import java.util.LinkedList;

public class Main {
    public static void removerComuns(LinkedList<String> lista1, LinkedList<String> lista2){
        Iterator<String> iterator = lista1.iterator();

        while(iterator.hasNext()){
            String cor = iterator.next();
            if (lista2.contains(cor)){
                iterator.remove();
            }
        }
    }

    public static void imprimir(LinkedList<String> lista){
        for (String cor : lista){
            System.out.print(cor + " | ");
        }
        System.out.println(" ");
    }
    public static void main(String[] args) {
        LinkedList<String> lista1 = new LinkedList<>();
        lista1.add("azul");
        lista1.add("vermelho");
        lista1.add("amarelo");
        lista1.add("verde");
        lista1.add("roxo");

        LinkedList<String> lista2 = new LinkedList<>();
        lista2.add("azul");
        lista2.add("amarelo");
        lista2.add("preto");

        System.out.println("======== ANTES ========");
        imprimir(lista1);
        removerComuns(lista1, lista2);

        System.out.println("======== DEPOIS =======");
        imprimir(lista1);

    }
}
