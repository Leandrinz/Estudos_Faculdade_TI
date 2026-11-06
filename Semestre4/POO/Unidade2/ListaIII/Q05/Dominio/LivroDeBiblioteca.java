package Semestre4.POO.Unidade2.ListaIII.Q05.Dominio;

public class LivroDeBiblioteca extends Livro implements ItemDeBiblioteca {
    private boolean emprestado;
    private String localizacaoFisica;

    public LivroDeBiblioteca(String titulo, String autor, int numeroDePaginas, int anoDaEdicao, boolean emprestado, String localizacaoFisica){
        super(titulo, autor, numeroDePaginas, anoDaEdicao);
        this.emprestado = emprestado;
        this.localizacaoFisica = localizacaoFisica;
    }

    @Override
    public boolean estaEmprestado(){
        return this.emprestado;
    }

    @Override
    public void empresta(){
        this.emprestado = true;
    }

    @Override
    public void devolve(){
        this.emprestado = false;
    }

    @Override
    public String localizacao(){
        return this.localizacaoFisica;
    }

    @Override
    public String descricao(){
        return "Livro de Biblioteca: " + qualTitulo() + " (Autor: " + qualAutor() + ")";
    }

    public String toString(){
        String situacaoEmprestado;
        if (emprestado == true){
            situacaoEmprestado = "Sim";
        }
        else{
            situacaoEmprestado = "Não";
        }
        return super.toString() + " | Emprestado: " + situacaoEmprestado + " | Localizacao Fisica: " + localizacaoFisica;
    }
}
