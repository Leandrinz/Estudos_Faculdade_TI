package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_04.Dominio;

public abstract class Empregado implements Pagavel{
    private String nome;
    private String carteiraDeTrabalho;

    public Empregado(String nome, String carteiraDeTrabalho){
        this.nome = nome;
        this.carteiraDeTrabalho = carteiraDeTrabalho;
    }

    public String getNome(){
        return nome;
    }

    public String getCarteiraDeTrabalho(){
        return carteiraDeTrabalho;
    }
}
