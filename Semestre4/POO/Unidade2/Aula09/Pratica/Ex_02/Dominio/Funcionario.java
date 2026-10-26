package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_02.Dominio;

public class Funcionario extends Pessoa{
    private float salario;

    public Funcionario(String nome, String identidade, float salario){
        super(nome, identidade);
        this.salario = salario;
    }

    public float getSalario(){
        return salario;
    }

    @Override
    public String toString(){
        return super.toString() + " | " + "Salário: " + this.salario;
    }
}
