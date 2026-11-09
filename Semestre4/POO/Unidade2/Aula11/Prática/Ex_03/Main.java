package Semestre4.POO.Unidade2.Aula11.Prática.Ex_03;

import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Stack<Number> pilha = new Stack<>();
        pilha.push(10);
        System.out.println("Push(10) -> " + pilha);

        pilha.push(3.14);
        System.out.println("Push(3.14) -> " + pilha);

        pilha.push(9.5f);
        System.out.println("Push(9.5f) -> " + pilha);

        pilha.push(1000L);
        System.out.println("Push(1000L -> " + pilha);

        while (!pilha.isEmpty()){
            Number removido = pilha.pop();
            System.out.println("pop() = " + removido + " -> " + pilha);
        }
    }
}
