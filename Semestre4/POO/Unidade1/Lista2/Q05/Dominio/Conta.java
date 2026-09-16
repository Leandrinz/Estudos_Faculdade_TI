package Semestre4.POO.Unidade1.Lista2.Q05.Dominio;

public class Conta {
    public double saldo;
    public void sacar(double qtd){
        saldo -= qtd;
    }
    public void depositar(double qtd){
        saldo += qtd;
    }

}
