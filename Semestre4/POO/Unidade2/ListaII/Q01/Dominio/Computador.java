package Semestre4.POO.Unidade2.ListaII.Q01.Dominio;

public class Computador extends Equipamento{
    private int memoriaRam;
    private int hd;

    public Computador(){}

    public Computador(String codigo, double preco, int memoriaRam, int hd){
        super(codigo, preco);
        this.memoriaRam = memoriaRam;
        this.hd = hd;
    }

    public int getMemoriaRam() {
        return memoriaRam;
    }


    public void setMemoriaRam(int memoriaRam) {
        this.memoriaRam = memoriaRam;
    }

    public int getHd() {
        return hd;
    }

    public void setHd(int hd) {
        this.hd = hd;
    }

    @Override
    public String toString(){
        return super.toString() + " | " + "Memória RAM: " + this.memoriaRam + " | " + "HD: " + this.hd;
    }
}
