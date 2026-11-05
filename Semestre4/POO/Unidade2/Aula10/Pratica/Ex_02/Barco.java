package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_02;

public class Barco extends Aquatico{
    public String cadBarco;

    public Barco(int capacidade, double profundidade, String cadBarco){
        super(capacidade, profundidade);
        this.cadBarco = cadBarco;
    }

    public void moverFrente(){
        System.out.println("O Barco está movendo pra frente");
    }
}
