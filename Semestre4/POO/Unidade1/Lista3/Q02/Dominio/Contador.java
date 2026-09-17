package Semestre4.POO.Unidade1.Lista3.Q02.Dominio;

public class Contador {
    private int cont;

    private void imprimir(){
        System.out.println("Contador: " + this.cont);
    }

    public void zerar(){
        this.cont = 0;
        imprimir();
    }

    public void incrementar(){
        this.cont++;
        imprimir();
    }




}
