package Semestre4.POO.Unidade2.ListaII.Q03.Dominio;

public class Livro {
    private String autor;
    private String titulo;
    private int anoLancamento;

    public Livro(String autor, String titulo, int anoLancamento){
        this.autor = autor;
        this.titulo = titulo;
        this.anoLancamento = anoLancamento;
    }

    @Override
    public String toString(){
        return "Autor: " + this.autor + " | " + "Título: " + this.titulo + " | " + "Ano de lançamento: " + this.anoLancamento;
    }
}
