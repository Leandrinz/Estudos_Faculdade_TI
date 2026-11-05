package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Dominio;

public class Carro extends Terrestre{
    private String placa;

    public Carro(int capacidade, int qtdrodas, String placa){
        super(capacidade, qtdrodas);
        this.placa = placa;
    }

    public Carro(int capacidade, String placa){
        this(capacidade, 4, placa);
    }

    @Override
    public void moverFrente(){
        System.out.println("O Carro está indo pra frente");
    }

    @Override
    public String toString() {
        return "Carro [placa=" + placa + ", capacidade=" + capacidade + ", rodas=" + qtdrodas + "]";
    }
}
