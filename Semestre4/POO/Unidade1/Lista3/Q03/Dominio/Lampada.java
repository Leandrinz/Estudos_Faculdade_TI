package Semestre4.POO.Unidade1.Lista3.Q03.Dominio;

import Semestre4.POO.Unidade1.Lista3.Q02.Dominio.Contador;

public class Lampada {
    public boolean estadoDaLampada;
    public Contador contador = new Contador();

    public void acende(){
        if (this.estadoDaLampada == false){
            this.estadoDaLampada = true;
            contador.incrementar();
            this.mostraEstado();
            System.out.print("Vezes que a lâmpada foi acesa: ");
            contador.imprimir();
        }
        else{
            this.mostraEstado();
        }
    }

    public void desliga(){
        if (this.estadoDaLampada == false){
            this.mostraEstado();
        }
        else{
            this.estadoDaLampada = false;
            this.mostraEstado();
        }
    }

    public void mostraEstado(){
        if (this.estadoDaLampada == true){
            System.out.println("A lâmpada está ligada!");
        }
        else{
            System.out.println("A lâmpada está desligada!");
        }
    }
}
