package Semestre4.POO.Unidade2.ListaIII.Q03.Test;

import Semestre4.POO.Unidade2.ListaIII.Q03.Dominio.Circulo;
import Semestre4.POO.Unidade2.ListaIII.Q03.Dominio.FiguraGeometrica;
import Semestre4.POO.Unidade2.ListaIII.Q03.Dominio.Quadrado;
import Semestre4.POO.Unidade2.ListaIII.Q03.Dominio.Triangulo;

public class FigurasTest01 {
    public static void main(String[] args) {
    FiguraGeometrica c = new Circulo();
    FiguraGeometrica t = new Triangulo();
    FiguraGeometrica q = new Quadrado();

    c.descricao();
    t.descricao();
    q.descricao();
    }


}
