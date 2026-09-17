package Semestre4.POO.Unidade1.Lista3.Q01.Dominio;

public class Elevador {
    private int numeroAndarAtual;
    private int numeroAndaresTotal;
    private int capacidadeElevador;
    private int pessoasNoElevador;

    public Elevador(int capacidadeElevador, int numeroAndaresTotal){
        this.numeroAndarAtual = 0;
        this.numeroAndaresTotal = numeroAndaresTotal;
        this.capacidadeElevador = capacidadeElevador;
        this.pessoasNoElevador = 0;
    }

    public void entra(){
        if (this.pessoasNoElevador < capacidadeElevador){
            this.pessoasNoElevador += 1;
            System.out.println("Número de pessoas no elevador: "+ this.pessoasNoElevador);
        }
        else{
            System.out.println("Elevador lotado!!!");
        }
    }

    public void sai(){
        if (this.pessoasNoElevador > 0){
            this.pessoasNoElevador -= 1;
            System.out.println("Número de pessoas no elevador: "+ this.pessoasNoElevador);
        }
        else{
            System.out.println("Não há ninguém no elevador!!!");
        }
    }

    public void sobe(){
        if (this.numeroAndarAtual == this.numeroAndaresTotal){
            System.out.println("O elevador se encontra no último andar");
        }
        else{
            this.numeroAndarAtual += 1;
            System.out.println("Andar atual: " + this.numeroAndarAtual);
        }
    }

    public void desce(){
        if (this.numeroAndarAtual == 0){
            System.out.println("O elevador se encontra no térreo!");
        }
        else{
            this.numeroAndarAtual -= 1;
        }
    }
}
