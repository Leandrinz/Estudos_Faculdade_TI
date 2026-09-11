package Semestre4.POO.Unidade1.Aula06.Prática.Ex_01.Dominio;

public class Triangulo {
    private float l1;
    private float l2;
    private float l3;
    private String desc;

    public void inicializaTriangulo(float lado1, float lado2, float lado3, String desc){
        this.l1 = lado1;
        this.l2 = lado2;
        this.l3 = lado3;
        this.desc = desc;
    }

    public float calculaPerimetro(){
        return this.l1 + this.l2 + this.l3;
    }
}
