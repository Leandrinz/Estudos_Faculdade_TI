package Semestre4.POO.Unidade1.Aula06.Prática.Ex_03.Dominio;

public class EventoAcademico {
    private String nomeDoEvento;
    private String localDoEvento;
    private int numeroDeParticipantes;

    public EventoAcademico(String n, String l, int numero){
        this.nomeDoEvento = n;
        this.localDoEvento = l;
        this.numeroDeParticipantes = numero;
    }

    public void mostraEvento(){
        System.out.println("Nome: "+this.nomeDoEvento);
        System.out.println("Local: "+this.localDoEvento);
        System.out.println("Número de participantes: "+ this.numeroDeParticipantes);
    }

}
