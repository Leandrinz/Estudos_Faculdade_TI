package Semestre4.POO.Unidade2.ListaII.Q02.Dominio;

public class EventoHeranca extends DataHora{
    private String evento;

    public EventoHeranca(int dia, int mes, int ano, int hora, int minutos, String evento){
        super(dia, mes, ano, hora, minutos);
        this.evento = evento;
    }

    @Override
    public String toString(){
        return "Evento (Herança): " + evento + " | Data/Hora: " + super.toString();
    }
}
