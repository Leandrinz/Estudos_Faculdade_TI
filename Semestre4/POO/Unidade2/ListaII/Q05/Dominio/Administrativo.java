package Semestre4.POO.Unidade2.ListaII.Q05.Dominio;

public class Administrativo extends Assistente{
    private String turno;
    private double adicionalNoturno;

    public Administrativo(String nome, double salario, String numeroMatricula, String turno, double adicionalNoturno){
        super(nome, salario, numeroMatricula);
        this.turno = turno;
        this.adicionalNoturno = adicionalNoturno;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public double getAdicionalNoturno() {
        return adicionalNoturno;
    }

    public void setAdicionalNoturno(double adicionalNoturno) {
        this.adicionalNoturno = adicionalNoturno;
    }

    @Override
    public double ganhoAnual(){
        double ganho12meses;
        if (turno.equalsIgnoreCase("noite")){
            ganho12meses = (getSalario() + adicionalNoturno) * 12;
        }
        else {
            ganho12meses = getSalario() * 12;
        }

        double decimoTerceiro = getSalario();

        return ganho12meses + decimoTerceiro;
    }

    @Override
    public String toString(){
        return super.toString() + " | Turno: " + this.turno + " | Adicional Noturno: " + this.adicionalNoturno;
    }
}
