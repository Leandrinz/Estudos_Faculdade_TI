package Semestre4.POO.Unidade2.ListaII.Q01.Dominio;

public class Equipamento {
    private String codigo;
    private double preco;

    public Equipamento(){}

    public Equipamento(String codigo, double preco){
        this.codigo = codigo;
        this.preco = preco;
    }

    public String getCodigo(){
        return codigo;
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
    }

    public double getPreco(){
        return preco;
    }

    public void setPreco(double preco){
        this.preco = preco;
    }

    @Override
    public String toString(){
        return "Código: " + codigo + " | " + "Preço: " + preco;
    }
}
