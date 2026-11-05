package Semestre4.POO.Unidade2.ListaIII.Q04.Dominio;

import Semestre4.POO.Unidade2.ListaIII.Q03.Dominio.FiguraGeometrica;

public class Desenho {
    private FiguraGeometrica figuraGeometrica1;
    private double x1, y1;

    private FiguraGeometrica figuraGeometrica2;
    private double x2, y2;

    public Desenho(FiguraGeometrica figuraGeometrica1, double x1, double y1, FiguraGeometrica figuraGeometrica2, double x2, double y2){
        this.figuraGeometrica1 = figuraGeometrica1;
        this.x1 = x1;
        this.y1 = y1;

        this.figuraGeometrica2 = figuraGeometrica2;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void apresenta(){
        System.out.println("--- Informações do Desenho ---");

        System.out.println("Figura 1 - > Coordenada: (" + x1 + ", " + y1 + ") | ");
        figuraGeometrica1.descricao();

        System.out.println("Figura 2 - > Coordenada: (" + x2 + ", " + y2 + ") | ");
        figuraGeometrica2.descricao();

        System.out.println("--------------------------------");
    }

}
