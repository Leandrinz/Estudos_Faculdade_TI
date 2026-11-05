package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Dominio;

abstract class Terrestre extends Veiculo{
    protected int qtdrodas;

    public Terrestre(int capacidade, int qtdrodas){
        super(capacidade);
        this.qtdrodas = qtdrodas;
    }
}
