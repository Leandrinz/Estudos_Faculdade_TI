package Semestre4.POO.Unidade2.ListaII.Q03.Dominio;

public class LivroLivraria extends Livro{
    private double preco;
    private int quantidadeEmEstoque;

    public LivroLivraria(String autor, String titulo, int anoLancamento, double preco, int quantidadeEmEstoque){
        super(autor, titulo, anoLancamento);
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    @Override
    public String toString(){
        return super.toString() + " | Preço: " + this.preco + "| Quantidade em estoque: " + this.quantidadeEmEstoque;
    }
}
