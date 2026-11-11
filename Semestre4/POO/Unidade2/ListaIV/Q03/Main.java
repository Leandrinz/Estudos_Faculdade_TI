package Semestre4.POO.Unidade2.ListaIV.Q03;

import java.util.Stack;

public class Main {
    public static boolean estaBalanceada(String texto){
        Stack<Character> pilha = new Stack<>();
        for (int i = 0; i < texto.length(); i++){
            char c = texto.charAt(i);

            if (c == '('){
                pilha.push('(');
            }
            else if (c == ')'){
                if (pilha.isEmpty()){
                    return false;
                }
                pilha.pop();
            }
        }
        return pilha.isEmpty();
    }
    public static void main(String[] args) {
        String[] exemplosValidos = {
            "()()()()",
            "(())()",
            "()()"
        };

        String[] exemplosInvalidos = {
            "(((((",
            ")))))",
            "()()())"
        };

        System.out.println("--- TESTANDO VÁLIDOS ---");
        for (String s : exemplosValidos){
            System.out.println("Exemplo: " + s + " -> Balanceada? " + estaBalanceada(s));
        }

        System.out.println("--- TESTANDO INVÁLIDOS ---");
        for (String s : exemplosInvalidos){
            System.out.println("Exemplo: " + s + " -> Balanceada? " + estaBalanceada(s));
        }
    }
}
