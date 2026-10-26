package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_01.Test;

import Semestre4.POO.Unidade2.Aula09.Pratica.Ex_01.Dominio.RegistroAcademicoPosGraduacao;

public class RegistroAcademicoTest01 {
    public static void main(String[] args) {
        RegistroAcademicoPosGraduacao registro1 = new RegistroAcademicoPosGraduacao("Leandro", "3232358339", "TI", "Frameworks de teste unitários", "Alysson");
        RegistroAcademicoPosGraduacao registro2 = new RegistroAcademicoPosGraduacao("Kamilly", "3432414", "Geografia", "Donde esta o planeta que habita mi amor", "Alucinações");

        System.out.println(registro1.toString());
        System.out.println(registro2.toString());
    }
}
