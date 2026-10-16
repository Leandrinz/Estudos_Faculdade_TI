package Semestre4.POO.Unidade2.ListaI.Q2.Test;

import Semestre4.POO.Unidade2.ListaI.Q2.Dominio.Matriz;

public class MatrizTest01 {
    public static void main(String[] args) {
        Matriz matriz = new Matriz(7.0f, 5.0f, 2.0f, 4.3f);

        System.out.println("Imprimindo a matriz");
        matriz.imprimeMatriz();

        System.out.println("Calculando determinante:");
        System.out.println(matriz.calculaDeterminante());
    }
}
