package Semestre4.POO.Unidade2.ListaI.Q3.Dominio;

public class Cliente {
    private int id;
    private String nome;
    private int idade;
    private int telefone;

    public Cliente(int id, String nome, int idade, int telefone){
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.telefone = telefone;
    }

    public String toString(){
        return "Nome: " + this.nome + " / " + " ID: " + this.id + " / " + "Idade: " + this.idade + " / " + "Telefone: " + this.telefone;
    }
}
