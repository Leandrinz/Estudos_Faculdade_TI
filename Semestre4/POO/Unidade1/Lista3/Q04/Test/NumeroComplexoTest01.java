package Semestre4.POO.Unidade1.Lista3.Q04.Test;

import Semestre4.POO.Unidade1.Lista3.Q04.Dominio.NumeroComplexo;

public class NumeroComplexoTest01 {
    public static void main(String[] args) {
        NumeroComplexo numero1 = new NumeroComplexo();
        System.out.println(numero1.toString());

        NumeroComplexo numero2 = new NumeroComplexo(5, -3);
        System.out.println(numero2.toString());

        NumeroComplexo numero3 = new NumeroComplexo(4);
        System.out.println(numero3.toString());

    }
}
