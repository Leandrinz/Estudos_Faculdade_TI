package Semestre4.POO.Unidade1.Aula06.Prática.Ex_02.Dominio;

public class RegistroAcademico {
    private String nome;
    private String matricula;
    private int codigoCurso;
    private float percentualDeCobranca;

    public void inicializaRegistroAcademico(String n, String m, int c, float p){
        this.nome = n;
        this.matricula = m;
        this.codigoCurso = c;
        this.percentualDeCobranca = p;
    }

    public String getNome(){
        return this.nome;
    }

    public float calculaMensalidade(){
        return 100*codigoCurso*percentualDeCobranca;
    }

}
