package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_02.Dominio;

public class ChefeDeDepartamento extends Funcionario{
    private String departamento;

    public ChefeDeDepartamento(String nome, String identidade, float salario, String departamento){
        super(nome, identidade, salario);
        this.departamento = departamento;
    }

    public String getDepartamento(){
        return departamento;
    }

    @Override
    public String toString(){
        return super.toString() + " | " + "Departamento: " + this.departamento;
    }
}
