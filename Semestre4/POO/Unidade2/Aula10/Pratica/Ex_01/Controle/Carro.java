public class Carro {
    private int capacidade;
    private int qtdrodas;
    private String placa;

    public Carro(int capacidade, int qtdrodas, String placa){
        this.capacidade = capacidade;
        this.qtdrodas = qtdrodas;
        this.placa = placa;
    }

    public void moverFrente(){
        System.out.println("O carro está movendo para frente");
    }
}
