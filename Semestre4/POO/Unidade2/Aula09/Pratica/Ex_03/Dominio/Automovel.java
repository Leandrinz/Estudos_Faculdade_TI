package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_03.Dominio;

public class Automovel {
    protected int ano;
    protected String modelo;
    protected String cor;

    public Automovel(int ano, String modelo, String cor){
        this.ano = ano;
        this.modelo = modelo;
        this.cor = cor;
    }

    public double quantoCusta(){
        return 20000 + (ano - 2000) * 1000;
    }
}
