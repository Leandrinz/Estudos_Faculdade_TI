package Semestre4.POO.Unidade1.Lista2.Q02.Dominio;

public class Fatura {
    private int numeroIdentificacao;
    private String descricao;
    private int quantidadeComprada;
    private double precoUnitario;

    public Fatura(int numeroIdentificacao, int quantidadeComprada, String descricao, double precoUnitario){

        this.numeroIdentificacao = numeroIdentificacao;

        this.descricao = descricao;

        if (quantidadeComprada > 0){
            this.quantidadeComprada = quantidadeComprada;
        }
        else{
            this.quantidadeComprada = 0;
        }

        if (precoUnitario > 0.0){
            this.precoUnitario = precoUnitario;
        }
        else{
            this.precoUnitario = 0.0;
        }
    }

    public double calculaTotal(){
        return (this.quantidadeComprada * this.precoUnitario);
    }

    public int getNumeroIdentificacao(){
        return this.numeroIdentificacao;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public int getQuantidadeComprada(){
        return this.quantidadeComprada;
    }

    public double getPrecoUnitario(){
        return this.precoUnitario;
    }


}
