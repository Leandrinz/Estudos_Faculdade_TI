package Semestre4.POO.Unidade1.Aula07.Prática.Ex_03.Test;

import Semestre4.POO.Unidade1.Aula07.Prática.Ex_03.Dominio.Tupla;

public class TuplaTest01 {
    public static void main(String[] args) {
        Tupla<Integer,Double> tupla1 = new Tupla<>(4, 2.5);
        System.out.println(tupla1);

        Tupla<String,Double> tupla2 = new Tupla<>("Leandro", 2.5);
        System.out.println(tupla2);
    }
}
