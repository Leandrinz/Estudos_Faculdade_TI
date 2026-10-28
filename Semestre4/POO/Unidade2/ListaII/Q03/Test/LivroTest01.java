package Semestre4.POO.Unidade2.ListaII.Q03.Test;

import Semestre4.POO.Unidade2.ListaII.Q03.Dominio.Livro;
import Semestre4.POO.Unidade2.ListaII.Q03.Dominio.LivroBiblioteca;
import Semestre4.POO.Unidade2.ListaII.Q03.Dominio.LivroLivraria;

public class LivroTest01 {
    public static void main(String[] args) {
        Livro l = new Livro("Deus", "Bíblia", 0);
        System.out.println("--- Livro ---");
        System.out.println(l);

        LivroLivraria ll = new LivroLivraria("Leandro", "Vasco", 2026, 700, 70);
        System.out.println("--- Livro livraria ---");
        System.out.println(ll);

        LivroBiblioteca lb = new LivroBiblioteca("Jesus", "A Salvação", 33, true, "777");
        System.out.println("--- Livro Biblioteca");
        System.out.println(lb);
    }
}
