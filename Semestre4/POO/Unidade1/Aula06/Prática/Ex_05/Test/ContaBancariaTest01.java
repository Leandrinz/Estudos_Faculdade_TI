package Semestre4.POO.Unidade1.Aula06.Prática.Ex_05.Test;

import Semestre4.POO.Unidade1.Aula06.Prática.Ex_05.Dominio.ContaBancaria;

public class ContaBancariaTest01 {
    public static void main(String[] args) {
        ContaBancaria conta1 = new ContaBancaria("Paulo", 123.4F, true);
        System.out.println(conta1);

        ContaBancaria conta2 = new ContaBancaria("Leandro");
        System.out.println(conta2);
    }
}
