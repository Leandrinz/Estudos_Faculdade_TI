package Semestre4.POO.Unidade2.ListaII.Q05.Dominio;

public class Assistente extends Funcionario{
    private String numeroMatricula;

    public Assistente(String nome, double salario, String numeroMatricula){
        super(nome, salario);
        this.numeroMatricula = numeroMatricula;
    }

    public String getNumeroMatricula(){
        return this.numeroMatricula;
    }

    public void setNumeroMatricula(String numeroMatricula){
        this.numeroMatricula = numeroMatricula;
    }

    @Override
    public String toString(){
        return super.toString() + " | Matrícula: " + this.numeroMatricula;
    }
}
