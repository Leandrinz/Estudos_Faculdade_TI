package Semestre4.POO.Unidade2.ListaII.Q04.Dominio;

public class Governador extends Politico{
    private String estado;

    public Governador(String nome, int idade, String partidoPolitico, String estado){
        super(nome, idade, partidoPolitico);
        this.estado = estado;
    }

    @Override
    public String toString(){
        return super.toString() + " | Estado Governado: " + this.estado;
    }
}
