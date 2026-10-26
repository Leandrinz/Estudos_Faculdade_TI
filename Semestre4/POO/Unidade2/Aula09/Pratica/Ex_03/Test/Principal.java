package Semestre4.POO.Unidade2.Aula09.Pratica.Ex_03.Test;

import Semestre4.POO.Unidade2.Aula09.Pratica.Ex_03.Dominio.AutomovelDeLuxo;

public class Principal {
    public static void main(String[] args) {
        AutomovelDeLuxo carro = new AutomovelDeLuxo(
            2024, "Sedan X", "Preto", true, true, true, true);

        System.out.println(carro);
        System.out.println("Custo: " + carro.quantoCusta());
    }
}
