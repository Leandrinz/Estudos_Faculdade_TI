package Semestre4.POO.Unidade2.Aula11.Prática.Ex_04;

import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<Double> fila = new PriorityQueue<>();

        fila.offer(7.3);
        fila.offer(5.8);
        fila.offer(2.4);
        fila.offer(65.2);
        fila.offer(5.4);

        System.out.println("Tamanho inicial: " + fila.size());
        System.out.println("Próximo a sair(peek): " + fila.peek());

        System.out.println("Removendo:");
        while (!fila.isEmpty()){
            Double removido = fila.poll();
            System.out.println(removido);
        }
        System.out.println("Tamanho final: " + fila.size());
    }
}
