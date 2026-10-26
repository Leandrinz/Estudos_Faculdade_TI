package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_03.Dominio;

public class AutomovelBasico extends Automovel {
    protected boolean airbag;
    protected boolean radio;

    public AutomovelBasico(int ano, String modelo, String cor, boolean airbag, boolean radio){
        super(ano, modelo, cor);
        this.airbag = airbag;
        this.radio = radio;
    }

    @Override
    public double quantoCusta(){
        double custo = super.quantoCusta();
        if (radio == true){
            custo += 2000;
        }
        if (airbag == true){
            custo += 3000;
        }
        return custo;
    }
}
