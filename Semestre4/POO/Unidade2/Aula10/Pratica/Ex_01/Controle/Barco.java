public class Barco {
    public int capacidade;
    public String cadANAC;
    public String cadAviao;

    public Barco(int capacidade, String cadANAC, String cadAviao){
        this.capacidade = capacidade;
        this.cadANAC = cadANAC;
        this.cadAviao = cadAviao;
    }

    public void moverFrente(){
        System.out.println("O avião está indo pra frente");
    }
}
