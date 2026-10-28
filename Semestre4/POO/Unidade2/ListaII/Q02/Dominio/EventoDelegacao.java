package Semestre4.POO.Unidade2.ListaII.Q02.Dominio;

public class EventoDelegacao {
    private String evento;
    private DataHora datahora;

    public EventoDelegacao(String evento, DataHora datahora){
        this.evento = evento;
        this.datahora = datahora;
    }

    public String toString(){
        return "Evento (Delegação): " + this.evento + " | Data/Hora: " + datahora.toString();
    }
}
