package Semestre4.POO.Unidade2.ListaIV.Q01;

import java.util.LinkedList;
import java.util.ListIterator;

public class Main {
    public static void main(String[] args) {
        LinkedList<Character> listaOriginal = new LinkedList<>();
        for (Character c = 'A'; c <= 'J'; c++){
            listaOriginal.add(c);
        }

        LinkedList<Character> listaInversa = new LinkedList<>();

        ListIterator<Character> iterator = listaOriginal.listIterator(listaOriginal.size());

        while(iterator.hasPrevious()){
            listaInversa.add(iterator.previous());
        }

        System.out.println("Lista Original: " + listaOriginal);
        System.out.println("Lista Inversa: " + listaInversa);
    }
}
