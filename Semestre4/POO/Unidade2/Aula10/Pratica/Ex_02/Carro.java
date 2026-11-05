package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_02;

public class Carro extends Terrestre {
    public String placa;

    public Carro(int capacidade, int qtdRodas, String placa){
        super(capacidade, qtdRodas);
        this.placa = placa;
    }

    public void moverFrente(){
        System.out.println("O carro está sendo movido pra frente");
    }
}
