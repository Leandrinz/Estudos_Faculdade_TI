package Semestre4.POO.Unidade1.Lista3.Q07.Test;

import Semestre4.POO.Unidade1.Lista3.Q07.Dominio.TresAtributos;

public class TresAtributosTest01 {
    public static void main(String[] args) {
        TresAtributos<Integer> numeros = new TresAtributos<>(4, 4, 4);

        numeros.imprimeAtributos();
        numeros.quantidadeIguais();

        TresAtributos<String> numeros2 = new TresAtributos<>("Leandro", "Leandro", "Messi");

        numeros2.imprimeAtributos();
        numeros2.quantidadeIguais();

        TresAtributos<Double> numeros3 = new TresAtributos<>(3.0, 5.0, 2.0);

        numeros3.imprimeAtributos();
        numeros3.quantidadeIguais();
    }
}
