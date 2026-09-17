package Semestre4.POO.Unidade1.Lista3.Q06.Test;

import Semestre4.POO.Unidade1.Lista3.Q06.Dominio.RegistroAcademico;

public class RegistroAcademicoTest01 {
    public static void main(String[] args) {
        RegistroAcademico aluno1 =
                new RegistroAcademico("Leandro", 1, 0.5f);

        RegistroAcademico aluno2 =
                new RegistroAcademico("Victor", 2, 0.7f);

        RegistroAcademico aluno3 =
                new RegistroAcademico("Heloíze", 3, 0.8f);

        System.out.println("Aluno: " + aluno1.getNome());
        System.out.println("Matrícula: " + aluno1.getMatricula());

        System.out.println();

        System.out.println("Aluno: " + aluno2.getNome());
        System.out.println("Matrícula: " + aluno2.getMatricula());

        System.out.println();

        System.out.println("Aluno: " + aluno3.getNome());
        System.out.println("Matrícula: " + aluno3.getMatricula());
    }
}
