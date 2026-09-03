package Semestre4.POO.Unidade1.Aula05.Prática.Ex_03.Dominio;

public class EventoAcademico {
    public String nomeDoEvento;
    public String localDoEvento;
    public int numeroDeParticipantes;

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
