package Semestre4.POO.Unidade1.Aula05.Prática.Ex_02.Test;

import Semestre4.POO.Unidade1.Aula05.Prática.Ex_02.Dominio.RegistroAcademico;

public class TestRegistroAcademico01 {
    public static void main(String[] args){
        RegistroAcademico michael = new RegistroAcademico();
        michael.inicializaRegistroAcademico("Michael", "025010324", 2, 0.6F);

        float calculaMensalidade = michael.calculaMensalidade();
        System.out.println("A mensalidade de "+ michael.nome + " é "+ calculaMensalidade);

        RegistroAcademico roberto = new RegistroAcademico();
        float mensalidade2 = roberto.calculaMensalidade();
        System.out.println("A mensalidade de "+ roberto.nome + " é "+ mensalidade2);

    }
}
