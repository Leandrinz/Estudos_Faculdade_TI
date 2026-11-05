public class Aviao {
    private int capacidade;
    private double profundidade;
    private String cadBarco;

    public Aviao(int capacidade, double profundidade, String cadBarco){
        this.capacidade = capacidade;
        this.profundidade = profundidade;
        this.cadBarco = cadBarco;
    }

    public void moverFrente(){
        System.out.println("O barco está navegando para frente");
    }
}
