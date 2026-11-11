package Semestre4.POO.Unidade2.Aula11.Prática.Ex_06;

import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        String texto = "O gato viu o rato. O rato fugiu do gato!";

        TreeMap<String, Integer> contagem = new TreeMap<>();

        String[] palavras = texto.toLowerCase().split("[^\\p{L}]+");

        for (String palavra : palavras){
            if (palavra.isEmpty()){
                continue;
            }
            if (contagem.containsKey(palavra)){
                contagem.put(palavra, contagem.get(palavra) + 1);
            }
            else{
                contagem.put(palavra, 1);
            }
        }

        for (String palavra : contagem.keySet()){
            System.out.println(palavra + ": " + contagem.get(palavra));
        }
    }
}
