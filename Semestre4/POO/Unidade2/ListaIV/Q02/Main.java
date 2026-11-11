package Semestre4.POO.Unidade2.ListaIV.Q02;

import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        String texto = "HELLO THERE";

        Map<Character, Integer> mapa = new TreeMap<>();
        texto = texto.toUpperCase();

        for (int i = 0; i < texto.length(); i++){
            char c = texto.charAt(i);

            if (Character.isLetter(c)){
                mapa.put(c, mapa.getOrDefault(c, 0) + 1);
            }
        }

        System.out.println("Resultado para String: "+ texto);
        for (Map.Entry<Character, Integer> entry : mapa.entrySet()){
            System.out.println("Letra: " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
