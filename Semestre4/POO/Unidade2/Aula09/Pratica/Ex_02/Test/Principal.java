package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_02.Test;

import Semestre4.POO.Unidade2.Aula09.Pratica.Ex_02.Dominio.ChefeDeDepartamento;
import Semestre4.POO.Unidade2.Aula09.Pratica.Ex_02.Dominio.Funcionario;
import Semestre4.POO.Unidade2.Aula09.Pratica.Ex_02.Dominio.Pessoa;

public class Principal {
    public static void main(String[] args) {
        Pessoa p = new Pessoa("Leandro", "23532389");
        Funcionario f = new Funcionario("Hinestroza", "7777777777", 1000000);
        ChefeDeDepartamento c = new ChefeDeDepartamento("Pedrinho", "101010101010", 1000000, "Presidência");

        System.out.println(p);
        System.out.println(f);
        System.out.println(c);

        System.out.println(c.getNome());
        System.out.println(c.getSalario());
        System.out.println(c.getDepartamento());
    }
}
