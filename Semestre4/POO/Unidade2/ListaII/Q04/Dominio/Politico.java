package Semestre4.POO.Unidade2.ListaII.Q04.Dominio;

public class Politico extends Pessoa{
    private String partidoPolitico;

    public Politico(String nome, int idade, String partidoPolitico){
        super(nome, idade);
        this.partidoPolitico = partidoPolitico;
    }

    @Override
    public String toString(){
        return super.toString() + " | Partido Político: " + this.partidoPolitico;
    }
}
