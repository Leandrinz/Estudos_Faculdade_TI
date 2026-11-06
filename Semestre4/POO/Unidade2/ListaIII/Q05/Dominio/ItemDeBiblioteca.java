package Semestre4.POO.Unidade2.ListaIII.Q05.Dominio;

public interface ItemDeBiblioteca {
    int maximoDeDiasParaEmprestimo = 14;

    boolean estaEmprestado();
    void empresta();
    void devolve();
    String localizacao();
    String descricao();
}
