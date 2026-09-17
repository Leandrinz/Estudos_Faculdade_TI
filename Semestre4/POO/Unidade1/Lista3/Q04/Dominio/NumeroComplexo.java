package Semestre4.POO.Unidade1.Lista3.Q04.Dominio;

public class NumeroComplexo {
    private double parteReal;
    private double parteImaginaria;

    public NumeroComplexo(double parteReal, double parteImaginaria){
        this.parteImaginaria = parteImaginaria;
        this.parteReal = parteReal;
    }

    public NumeroComplexo(double parteReal){
        this.parteReal = parteReal;
        this.parteImaginaria = 0;
    }

    public NumeroComplexo(){
        this.parteImaginaria = 0;
        this.parteReal = 0;
    }

    public String toString(){
        if (this.parteImaginaria >= 0){
            return parteReal + " + " + parteImaginaria + "i";
        }
        else{
            return parteReal + " - " + (parteImaginaria * -1) + "i";
        }
    }
}
