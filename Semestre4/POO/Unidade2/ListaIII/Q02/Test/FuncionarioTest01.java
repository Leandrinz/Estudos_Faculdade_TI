package Semestre4.POO.Unidade2.ListaIII.Q02.Test;

import Semestre4.POO.Unidade2.ListaIII.Q02.Domínio.Funcionario;

public class FuncionarioTest01 {
    public static void main(String[] args) {
        Funcionario f1 = new Funcionario("Leandro");
        f1.exibirInformacoes();
        System.out.println("Salário para 120 horas: " + f1.salario(120));

        System.out.println("============================");

        Funcionario f2 = new Funcionario("Colidio", 30);
        f2.exibirInformacoes();
        System.out.println("Salário para 120 horas: R$ " + f2.salario(120));
    }
}
