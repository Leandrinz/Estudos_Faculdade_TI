package Semestre4.POO.Unidade1.Aula06.Prática.Ex_06.Test;

import Semestre4.POO.Unidade1.Aula06.Prática.Ex_06.Dominio.CaixaBanco;

public class Principal {
    public static void main(String[] args) {
        System.out.println("Clientes atendidos: " + CaixaBanco.clientesAtendidos);
        CaixaBanco caixa1 = new CaixaBanco(1);
        CaixaBanco caixa2 = new CaixaBanco(2);
        CaixaBanco caixa3 = new CaixaBanco(3);
        CaixaBanco caixa4 = new CaixaBanco(4);
        CaixaBanco caixa5 = new CaixaBanco(5);

        caixa1.iniciaAtendimento();
        caixa2.iniciaAtendimento();
        caixa3.iniciaAtendimento();
        caixa4.iniciaAtendimento();
        caixa5.iniciaAtendimento();

        caixa2.iniciaAtendimento();
        caixa4.iniciaAtendimento();
        System.out.println("Clientes atendidos: " + CaixaBanco.clientesAtendidos);
    }
}
