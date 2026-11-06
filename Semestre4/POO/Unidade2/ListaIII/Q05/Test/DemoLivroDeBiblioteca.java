package Semestre4.POO.Unidade2.ListaIII.Q05.Test;

import Semestre4.POO.Unidade2.ListaIII.Q05.Dominio.LivroDeBiblioteca;

public class DemoLivroDeBiblioteca {
    public static void main(String[] args) {
        LivroDeBiblioteca livro = new LivroDeBiblioteca("História do Vasco", "Leandro", 777, 2026, false, "UFERSA - PDF");

        System.out.println("=== Estado Inicial ===");
        System.out.println(livro.toString());
        System.out.println("Descrição: " + livro.descricao());
        System.out.println("Localização: " + livro.localizacao());
        System.out.println("Está emprestado: " + livro.estaEmprestado());

        System.out.println("Emprestando livro...");
        livro.empresta();
        System.out.println("Está emprestado agora?" + livro.estaEmprestado());

        System.out.println("Realizando devolução...");
        livro.devolve();
        System.out.println("Está emprestado agora?" + livro.estaEmprestado());
    }
}