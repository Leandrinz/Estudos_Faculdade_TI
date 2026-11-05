package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_02;

public class Aviao extends Aereo{
    public String cadAviao;

    public Aviao(int capacidade, String cadANAC, String cadAviao){
        super(capacidade, cadANAC);
        this.cadAviao = cadAviao;
    }

    public void moverFrente(){
        System.out.println("O avião está indo para frente");
    }
}
