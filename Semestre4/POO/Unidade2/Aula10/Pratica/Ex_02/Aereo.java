package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_02;

public class Aereo extends Veiculo{
    protected String cadANAC;

    public Aereo(int capacidade, String cadANAC){
        super(capacidade);
        this.cadANAC = cadANAC;
    }
}
