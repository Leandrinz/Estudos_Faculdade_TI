package Semestre4.POO.Unidade1.Aula05.Prática.Ex_01.Test;

import Semestre4.POO.Unidade1.Aula05.Prática.Ex_01.Dominio.Triangulo;

public class TrianguloTest01 {
    public static void main(String[] args){
        Triangulo t1, t2, t3;
        t1 = new Triangulo();
        t1.inicializaTriangulo(3F, 4F, 5F, "triângulo retângulo");
        t2 = new Triangulo();
        t1.inicializaTriangulo(3F, 4F, 5F, "triângulo retângulo");
        t3 = t1;
        System.out.println(t1 == t2);
        System.out.println(t1 == t3);
    }
}
