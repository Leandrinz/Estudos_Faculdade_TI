package Semestre4.POO.Unidade2.ListaII.Q04.Dominio;

public class Prefeito extends Politico{
    private String cidade;

    public Prefeito(String nome, int idade, String partidoPolitico, String cidade){
        super(nome, idade, partidoPolitico);
        this.cidade = cidade;
    }

    @Override
    public String toString(){
        return super.toString() + " | Cidade: " + this.cidade;
    }
}
