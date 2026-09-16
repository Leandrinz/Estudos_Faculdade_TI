package Semestre4.POO.Unidade1.Lista2.Q03.Dominio;

public class Alunos {
    private String matricula;
    private String nome;
    private double P1;
    private double P2;
    private double T;

    public double getP2(){
        return this.P2;
    }

    public double getT(){
        return this.T;
    }

    public double getP1(){
        return this.P1;
    }

    public String getMatricula(){
        return this.matricula;
    }

    public String getNome(){
        return this.nome;
    }

    public Alunos(String matricula, String nome, double P1, double P2, double T){
        this.matricula = matricula;
        this.nome = nome;
        this.P1 = P1;
        this.P2 = P2;
        this.T = T;
    }

    public double media(){
        return ((2.5 * this.P1 + 2.5 * this.P2 + 2 * this.T) / 7);
    }

    public double provaFinal(){
        if (media() < 3.0 || media() >= 7.0){
            return 0;
        }
        else{
            double EF = 7.0 - media();
            double MF = ((media() * 6 +  EF * 4) / 10);
            if (MF >= 5.0){
                System.out.println("Aprovado");
            }
            else{
                System.out.println("Reprovado");
            }
            return MF;
        }


    }
}
