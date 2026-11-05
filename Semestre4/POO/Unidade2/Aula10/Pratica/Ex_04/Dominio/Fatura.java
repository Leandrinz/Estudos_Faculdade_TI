package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_04.Dominio;

public class Fatura implements Pagavel {
    private double precoItem;
    private int qtdItem;

    public Fatura(double precoItem, int qtdItem){
        this.precoItem = precoItem;
        this.qtdItem = qtdItem;
    }

    @Override
    public double getValorPagamento(){
        return precoItem * qtdItem;
    }
}
