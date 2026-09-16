package Semestre4.POO.Unidade1.Lista2.Q03.Test;

import Semestre4.POO.Unidade1.Lista2.Q03.Dominio.Alunos;

public class AlunosTest01 {
    public static void main(String[] args) {
        Alunos aluno1 = new Alunos("2025013783", "Leandro", 6, 6, 8);

        System.out.println("Aluno 1");
        System.out.println("Matrícula: " + aluno1.getMatricula());
        System.out.println("Nome: " + aluno1.getNome());
        System.out.println("Nota 1: " + aluno1.getP1());
        System.out.println("Nota 2: " + aluno1.getP2());
        System.out.println("Nota do trabalho: " + aluno1.getT());
        System.out.println("Média parcial: " + aluno1.media());
        System.out.println("Prova final: " + aluno1.provaFinal());
    }
}
