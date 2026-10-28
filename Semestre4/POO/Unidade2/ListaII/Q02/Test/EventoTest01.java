package Semestre4.POO.Unidade2.ListaII.Q02.Test;

import Semestre4.POO.Unidade2.ListaII.Q02.Dominio.DataHora;
import Semestre4.POO.Unidade2.ListaII.Q02.Dominio.EventoDelegacao;
import Semestre4.POO.Unidade2.ListaII.Q02.Dominio.EventoHeranca;

public class EventoTest01 {
    public static void main(String[] args) {
        DataHora datahora1 = new DataHora(24, 07, 2006, 14, 35);
        EventoDelegacao ed = new EventoDelegacao("Nascimento",
        datahora1);
        System.out.println("--- Evento Delegação ---");
        System.out.println(ed);

        EventoHeranca eh = new EventoHeranca(24, 07, 2026, 14, 24, "Anivérsário de 20 Anos");
        System.out.println("--- Evento Herança ---");
        System.out.println(eh);

    }
}
