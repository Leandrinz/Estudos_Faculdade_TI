package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Dominio;


public class Helicoptero extends Aereo{
    public Helicoptero(int capacidade, String cadANAC){
        super(capacidade, cadANAC);
    }

    @Override
    public void moverFrente(){
        System.out.println("O helicóptero está inclinando e avançando");
    }


}
