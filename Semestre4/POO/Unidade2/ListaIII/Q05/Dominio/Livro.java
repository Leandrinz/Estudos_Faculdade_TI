package Semestre4.POO.Unidade2.ListaIII.Q05.Dominio;

public class Livro {
    private String titulo;
    private String autor;
    private int numeroDePaginas;
    private int anoDaEdicao;

    public Livro(String titulo, String autor, int numeroDepaginas, int anoDaEdicao){
        this.titulo = titulo;
        this.autor = autor;
        this.numeroDePaginas = numeroDepaginas;
        this.anoDaEdicao = anoDaEdicao;
    }

    public String qualTitulo(){
        return titulo;
    }



    public String qualAutor(){
        return autor;
    }

    public String toString(){
        return "Título: " + titulo + " | Autor: " + autor + " | Número de Páginas: " + numeroDePaginas + " | Ano da edição: " + anoDaEdicao;
    }
}
