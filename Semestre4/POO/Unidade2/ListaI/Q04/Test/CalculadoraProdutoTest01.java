package Semestre4.POO.Unidade2.ListaI.Q4.Test;

import Semestre4.POO.Unidade2.ListaI.Q4.Dominio.CalculadoraProduto;

public class CalculadoraProdutoTest01 {
    public static void main(String[] args) {
        int p1 = CalculadoraProduto.produto(2, 5);
        System.out.println("Produto de (2, 5): " + p1);

        int p2 = CalculadoraProduto.produto(1, 2, 3, 4);
        System.out.println("Produto de (1, 2, 3, 4): " + p2);

        int p3 = CalculadoraProduto.produto(3, 3, 2, 1, 10);
        System.out.println("Produto de (3, 3, 2, 1, 10): " + p3);

        int p4 = CalculadoraProduto.produto(7);
        System.out.println("Produto de (7): " + p4);
    }
}
