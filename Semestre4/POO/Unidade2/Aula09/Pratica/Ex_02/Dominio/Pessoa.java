package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_02.Dominio;

public class Pessoa {
    private String nome;
    private String identidade;

    public Pessoa(String nome, String identidade){
        this.nome = nome;
        this.identidade = identidade;
    }

    public String getNome(){
        return nome;
    }

    public String getIdentidade(){
        return identidade;
    }

    public String toString(){
        return "Nome: " + this.nome + " | " + "Identidade: " + this.identidade;
    }
}
