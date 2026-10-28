package Semestre4.POO.Unidade2.ListaII.Q04.Dominio;

public class Pessoa {
    private String nome;
    private int idade;

    public Pessoa(String nome, int idade){
        this.nome = nome;
        this.idade = idade;
    }

    @Override
    public String toString(){
        return "Nome: " + this.nome + " | Idade: " + this.idade;
    }
}
