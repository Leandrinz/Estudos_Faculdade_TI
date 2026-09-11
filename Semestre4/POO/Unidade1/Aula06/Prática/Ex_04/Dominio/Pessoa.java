package Semestre4.POO.Unidade1.Aula06.Prática.Ex_04.Dominio;

public class Pessoa {
    private String nome;
    private float altura;

    public Pessoa(String nome, float altura){
        this.nome = nome;
        this.altura = altura;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

}
