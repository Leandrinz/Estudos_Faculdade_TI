package Semestre4.POO.Unidade2.ListaII.Q02.Dominio;

public class DataHora {
    private int dia;
    private int mes;
    private int ano;
    private int hora;
    private int minutos;

    public DataHora(int dia, int mes, int ano, int hora, int minutos){
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.minutos = minutos;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    @Override
    public String toString(){
        return dia + "/" + mes + "/" + ano + " - " + hora + ":" + minutos;
    }
}
