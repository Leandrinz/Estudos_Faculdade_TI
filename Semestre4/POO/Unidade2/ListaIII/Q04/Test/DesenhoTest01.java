package Semestre4.POO.Unidade2.ListaIII.Q04.Test;

import Semestre4.POO.Unidade2.ListaIII.Q03.Dominio.Circulo;
import Semestre4.POO.Unidade2.ListaIII.Q03.Dominio.Quadrado;
import Semestre4.POO.Unidade2.ListaIII.Q03.Dominio.Triangulo;
import Semestre4.POO.Unidade2.ListaIII.Q04.Dominio.Desenho;

public class DesenhoTest01 {
    public static void main(String[] args) {
        Desenho desenho1 = new Desenho(new Quadrado(), 2, 3, new Circulo(), 1, 6);

        Desenho desenho2 = new Desenho(new Triangulo(), 2.3, 9.2, new Circulo(), 2, 10);

        desenho1.apresenta();
        desenho2.apresenta();
    }
}
