package Semestre4.POO.Unidade1.Aula06.Prática.Ex_05.Dominio;

public class ContaBancaria {
    private String nome;
    private float saldo;
    private boolean especial;

    public ContaBancaria(String nome, float saldo, boolean especial){
        this.nome = nome;
        this.saldo = saldo;
        this.especial = especial;
    }

    public ContaBancaria(String nome){
        this.nome = nome;
        this.saldo = 0.0F;
        this.especial = false;
    }

    public String toString(){
        String res = "Correntista: " + nome;
        res += "\n Saldo: " + saldo;
        res += "\n Especial: " + especial;
        return res;
    }
}
