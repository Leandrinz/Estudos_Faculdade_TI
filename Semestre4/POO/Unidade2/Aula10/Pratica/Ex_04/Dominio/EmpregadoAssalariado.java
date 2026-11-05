package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_04.Dominio;

public class EmpregadoAssalariado extends Empregado{
    private double valorSalario;

    public EmpregadoAssalariado(String nome, String carteiraDeTrabalho, double valorSalario){
        super(nome, carteiraDeTrabalho);
        this.valorSalario = valorSalario;
    }

    @Override
    public double getValorPagamento(){
        return valorSalario;
    }
}
