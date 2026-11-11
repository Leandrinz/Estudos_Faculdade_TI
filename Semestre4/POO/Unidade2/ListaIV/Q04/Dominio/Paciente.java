package Semestre4.POO.Unidade2.ListaIV.Q04.Dominio;

public class Paciente {
    private String RG;
    private int idade;

    public Paciente(String RG, int idade){
        this.RG = RG;
        this.idade = idade;
    }

    public String getRG(){
        return RG;
    }

    public boolean isPrioritario(){
        return idade > 60;
    }

    public String toString(){
        return "RG: " + RG + " | Idade: " + idade;
    }
}
