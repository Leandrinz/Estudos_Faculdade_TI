package Semestre4.POO.Unidade2.ListaII.Q05.Dominio;

public class Funcionario {
    private String nome;
    private double salario;

    public Funcionario(String nome, double salario){
        this.nome = nome;
        this.salario = salario;
    }

    public void aumentarSalario(double aumento){
        this.salario += aumento;
    }

    public double ganhoAnual(){
        return this.salario * 13;
    }

    @Override
    public String toString(){
        return "Nome: " + this.nome + " | Salário: " + this.salario + " | Ganho anual: " + this.ganhoAnual();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

}
