package Semestre4.POO.Unidade2.ListaII.Q03.Dominio;

public class LivroBiblioteca extends Livro{
    private boolean emprestado;
    private String codigo;

    public LivroBiblioteca(String autor, String titulo, int anoLancamento, boolean emprestado, String codigo){
        super(autor, titulo, anoLancamento);
        this.emprestado = emprestado;
        this.codigo = codigo;
    }

    @Override
    public String toString(){
        String statusEmprestimo = emprestado ? "Sim" : "Não";
        return super.toString() + " | Emprestado: " + statusEmprestimo + " | Código: " + this.codigo;
    }
}
