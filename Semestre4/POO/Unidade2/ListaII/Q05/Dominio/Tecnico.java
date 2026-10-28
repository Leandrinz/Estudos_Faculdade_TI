package Semestre4.POO.Unidade2.ListaII.Q05.Dominio;

public class Tecnico extends Assistente{
    private double bonusSalarial;

    public Tecnico(String nome, double salario, String numeroMatricula, double bonusSalarial){
        super(nome, salario, numeroMatricula);
        this.bonusSalarial = bonusSalarial;
    }

    public double getBonusSalarial() {
        return bonusSalarial;
    }

    public void setBonusSalarial(double bonusSalarial) {
        this.bonusSalarial = bonusSalarial;
    }

    @Override
    public double ganhoAnual(){
        return (getSalario() + bonusSalarial) * 13;
    }

    @Override
    public String toString(){
        return super.toString() + " | Bônus Salarial: " + this.bonusSalarial;
    }

}
