package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_04.Test;

import Semestre4.POO.Unidade2.Aula10.Pratica.Ex_04.Dominio.EmpregadoAssalariado;
import Semestre4.POO.Unidade2.Aula10.Pratica.Ex_04.Dominio.Fatura;
import Semestre4.POO.Unidade2.Aula10.Pratica.Ex_04.Dominio.Pagavel;

public class Main {
    public static void imprimirPagamento(Pagavel p){
        System.out.printf("Custo do pagamento: R$ %.2f%n", p.getValorPagamento());
    }
    public static void main(String [] args){
        Pagavel fatura = new Fatura(23.50, 4);
        Pagavel empregado = new EmpregadoAssalariado("Leandro", "7777777", 12000);

        imprimirPagamento(empregado);
        imprimirPagamento(fatura);
    }
}
