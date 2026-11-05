package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03;

public abstract class Aereo extends Veiculo{
    protected String codANAC;

    public Aereo(int quantidade, String codANAC){
        super(quantidade);
        this.codANAC = codANAC;
    }
}
