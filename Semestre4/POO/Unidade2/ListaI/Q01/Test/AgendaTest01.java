public class AgendaTest01 {
    public static void main(String[] args) {
        Agenda agenda = new Agenda();
        EntradaEmAgenda compromisso1 = new EntradaEmAgenda("21:30", 17, 10, 2026, "Vasco x São Paulo");
        EntradaEmAgenda compromisso2 = new EntradaEmAgenda("19:00", 17, 10, 2026, "Missa");

        agenda.adicionaCompromisso(compromisso1);
        agenda.adicionaCompromisso(compromisso2);

        agenda.listaDia(17, 10, 2026);

    }
}
