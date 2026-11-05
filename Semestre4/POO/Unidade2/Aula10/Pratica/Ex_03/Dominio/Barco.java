package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Dominio;

public class Barco extends Aquatico{
    public String cadBarco;

    public Barco(int capacidade, double profundidade, String cadBarco){
        super(capacidade, profundidade);
        this.cadBarco = cadBarco;
    }

    public void moverFrente(){
        System.out.println("O Barco está indo pra frente");
    }
}
