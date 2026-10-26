package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_01.Dominio;

public class RegistroAcademico {
    private String nomeDoAluno;
    private String matricula;
    private String curso;

    public RegistroAcademico(String nomeDoAluno, String matricula, String curso){
        this.nomeDoAluno = nomeDoAluno;
        this.matricula = matricula;
        this.curso = curso;
    }

    public String toString(){
        return "Nome do aluno: " + nomeDoAluno + " / " + "Matrícula: " + matricula + " / " + " Curso: " + curso;
    }
}
