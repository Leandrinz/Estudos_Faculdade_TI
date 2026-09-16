package Semestre4.POO.Unidade1.Lista2.Q05.Test;

import Semestre4.POO.Unidade1.Lista2.Q05.Dominio.Conta;

public class ContaTest01 {
    public static void main(String[] args) {
        Conta c1 = new Conta();
        c1.depositar(1000);
        Conta c2 = c1;
        c1.sacar(500);
        System.out.println(c1.saldo);

    }
}
