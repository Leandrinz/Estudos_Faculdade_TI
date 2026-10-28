package Semestre4.POO.Unidade2.ListaII.Q04.Test;

import Semestre4.POO.Unidade2.ListaII.Q04.Dominio.Governador;
import Semestre4.POO.Unidade2.ListaII.Q04.Dominio.Pessoa;
import Semestre4.POO.Unidade2.ListaII.Q04.Dominio.Politico;
import Semestre4.POO.Unidade2.ListaII.Q04.Dominio.Prefeito;

public class PoliticoTest01 {
    public static void main(String[] args) {
        Pessoa pessoa = new Pessoa("Leandro", 20);
        System.out.println("--- Pessoa ---");
        System.out.println(pessoa);

        Politico politico = new Politico("Lula", 80, "PT");
        System.out.println("--- Político ---");
        System.out.println(politico);

        Governador governador = new Governador("Cadu", 40, "PT", "Rio Grande do Norte");
        System.out.println("--- Governador ---");
        System.out.println(governador);

        Prefeito prefeito = new Prefeito("Mariana", 37, "PL", "Pau dos Ferros");
        System.out.println("--- Prefeita ---");
        System.out.println(prefeito);
    }
}
