package Semestre4.POO.Unidade2.ListaII.Q01.Test;

import Semestre4.POO.Unidade2.ListaII.Q01.Dominio.Computador;
import Semestre4.POO.Unidade2.ListaII.Q01.Dominio.Equipamento;

public class TestaEquipamento {
    public static void main(String[] args) {
        Equipamento e = new Equipamento();
        e.setCodigo("12345");
        e.setPreco(200);

        System.out.println("--- Objeto equipamento ---");
        System.out.println(e.toString());

        System.out.println("--- Objeto Computador ---");

        Computador c = new Computador();
        c.setCodigo("54321");
        c.setPreco(5000);
        c.setHd(504);
        c.setMemoriaRam(64);
        System.out.println(c.toString());
    }
}
