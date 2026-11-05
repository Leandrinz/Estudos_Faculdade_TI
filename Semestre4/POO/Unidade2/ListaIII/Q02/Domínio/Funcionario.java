package Semestre4.POO.Unidade2.ListaIII.Q02.Domínio;

public class Funcionario {
    private String nome;
    private double valorHora;

    public Funcionario(String nome, double valorHora){
        this.nome = nome;
        this.valorHora = valorHora;
    }

    public Funcionario(String nome){
        this.nome = nome;
        this.valorHora = 2;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public double getValorHora(){
        return valorHora;
    }

    public double salario(double horasTrabalhadas){
        return valorHora * horasTrabalhadas;
    }

    public void exibirInformacoes(){
        System.out.println("Funcionário: " + nome);
        System.out.println("Valor por hora: R$ " + valorHora);
    }
}
