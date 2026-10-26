package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_03.Dominio;

public class AutomovelDeLuxo extends AutomovelBasico{
    protected boolean arCondicionado;
    protected boolean direcaoHidraulica;

    public AutomovelDeLuxo(int ano, String modelo, String cor, boolean airbag, boolean radio, boolean arCondicionado, boolean direcaoHidraulica){
        super(ano, modelo, cor, airbag, radio);
        this.arCondicionado = arCondicionado;
        this.direcaoHidraulica = direcaoHidraulica;
    }

    @Override
    public double quantoCusta(){
        double custo = super.quantoCusta();
        if (direcaoHidraulica == true){
            custo += 5000;
        }
        if (arCondicionado == true){
            custo += 3990;
        }
        return custo;
    }

    @Override
    public String toString() {
        return "Modelo: " + modelo + ", Ano: " + ano + ", Cor: " + cor
             + ", Airbag: " + airbag + ", Rádio: " + radio
             + ", Ar: " + arCondicionado + ", Direção: " + direcaoHidraulica
             + ", Custo: R$ " + quantoCusta();
    }
}
