package Semestre4.POO.Unidade1.Lista3.Q05.Test;

import Semestre4.POO.Unidade1.Lista3.Q05.Dominio.MaiorValor;

public class MaiorValorTest01 {
    public static void main(String[] args) {
        System.out.println("Maior de dois int: "
                + MaiorValor.maior(10, 20));

        System.out.println("Maior de dois double: "
                + MaiorValor.maior(5.5, 3.2));

        // Testando três valores
        System.out.println("Maior de três int: "
                + MaiorValor.maior(10, 30, 20));

        System.out.println("Maior de três double: "
                + MaiorValor.maior(2.5, 8.7, 4.1));

        // Testando quatro valores
        System.out.println("Maior de quatro int: "
                + MaiorValor.maior(10, 40, 20, 30));

        System.out.println("Maior de quatro double: "
                + MaiorValor.maior(1.2, 9.8, 3.5, 7.6));

        // Testando cinco valores
        System.out.println("Maior de cinco int: "
                + MaiorValor.maior(10, 50, 20, 40, 30));

        System.out.println("Maior de cinco double: "
                + MaiorValor.maior(2.3, 8.9, 4.5, 1.2, 6.7));
    }
}
