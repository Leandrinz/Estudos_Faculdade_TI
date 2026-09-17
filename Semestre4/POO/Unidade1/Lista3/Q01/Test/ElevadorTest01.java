package Semestre4.POO.Unidade1.Lista3.Q01.Test;

import Semestre4.POO.Unidade1.Lista3.Q01.Dominio.Elevador;

public class ElevadorTest01 {
    public static void main(String[] args) {
        Elevador elevador = new Elevador(60, 10);

        elevador.sai();
        elevador.entra();
        elevador.entra();
        elevador.sai();
        elevador.sobe();
        elevador.desce();
        elevador.desce();

    }
}
