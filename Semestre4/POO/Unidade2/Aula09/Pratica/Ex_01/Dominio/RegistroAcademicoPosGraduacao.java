package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_01.Dominio;

public class RegistroAcademicoPosGraduacao {
    private RegistroAcademico registro;
    private String tituloTese;
    private String orientador;

    public RegistroAcademicoPosGraduacao(String nome, String matricula, String curso, String tituloTese, String orientador){
        this.registro = new RegistroAcademico(nome, matricula, curso);
        this.tituloTese = tituloTese;
        this.orientador = orientador;
    }

    public String toString(){
        return registro + " / " + "Tese: " + tituloTese + " / " + "Orientador: " + orientador;
    }
}
