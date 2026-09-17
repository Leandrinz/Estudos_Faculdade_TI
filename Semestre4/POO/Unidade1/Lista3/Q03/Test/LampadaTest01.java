package Semestre4.POO.Unidade1.Lista3.Q03.Test;

import Semestre4.POO.Unidade1.Lista3.Q03.Dominio.Lampada;

public class LampadaTest01 {
    public static void main(String[] args) {
        Lampada lamp = new Lampada();

        System.out.println("=== Lâmp ===");
        lamp.acende();
        lamp.desliga();
        lamp.desliga();
        lamp.acende();
        lamp.acende();
    }
}
