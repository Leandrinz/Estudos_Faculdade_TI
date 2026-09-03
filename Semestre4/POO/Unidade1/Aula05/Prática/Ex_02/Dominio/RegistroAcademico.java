package Semestre4.POO.Unidade1.Aula05.Prática.Ex_02.Dominio;

public class RegistroAcademico {
    public String nome;
    public String matricula;
    int codigoCurso;
    float percentualDeCobranca;

    public void inicializaRegistroAcademico(String n, String m, int c, float p){
        this.nome = n;
        this.matricula = m;
        this.codigoCurso = c;
        this.percentualDeCobranca = p;
    }

    public float calculaMensalidade(){
        return 100*codigoCurso*percentualDeCobranca;
    }

}
