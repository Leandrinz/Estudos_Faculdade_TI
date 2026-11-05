package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_02;

public class Terrestre extends Veiculo{
    protected int qtdRodas;

    public Terrestre(int capacidade, int qtdRodas){
        super(capacidade);
        this.qtdRodas = qtdRodas;
    }
}
