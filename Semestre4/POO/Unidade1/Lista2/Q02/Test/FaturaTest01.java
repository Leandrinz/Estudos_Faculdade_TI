package Semestre4.POO.Unidade1.Lista2.Q02.Test;

import Semestre4.POO.Unidade1.Lista2.Q02.Dominio.Fatura;

public class FaturaTest01 {
    public static void main(String[] args) {
        Fatura fatura1 = new Fatura(01, 9, "Notebook", 4000);

        System.out.println("Fatura 1");
        System.out.println("Número :" + fatura1.getNumeroIdentificacao());
        System.out.println("Descrição: " + fatura1.getDescricao());
        System.out.println("Quantidade comprada: "+ fatura1.getQuantidadeComprada());
        System.out.println("Preço unitário: "+fatura1.getPrecoUnitario());
        System.out.println("Total: R$"+fatura1.calculaTotal());
    }
}
