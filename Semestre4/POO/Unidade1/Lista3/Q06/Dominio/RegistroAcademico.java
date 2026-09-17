package Semestre4.POO.Unidade1.Lista3.Q06.Dominio;

public class RegistroAcademico {
    private static int numeroDeMatriculas = 0;
    private String nome;
    private String matricula;
    private int codigoCurso;
    private float percentualDeCobranca;

    public RegistroAcademico(String n, int c, float p) {

        numeroDeMatriculas++;

        this.nome = n;
        this.matricula = "MAT" + numeroDeMatriculas;
        this.codigoCurso = c;
        this.percentualDeCobranca = p;
    }

    public String getNome() {
        return this.nome;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public float calculaMensalidade() {
        return 100 * codigoCurso * percentualDeCobranca;
    }
}
