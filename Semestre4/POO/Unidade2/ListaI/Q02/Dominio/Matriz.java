package Semestre4.POO.Unidade2.ListaI.Q2.Dominio;

public class Matriz {
    private float[][] dados;

    public Matriz(float a, float b, float c, float d){
        dados = new float[2][2];

        this.dados[0][0] = a;
        this.dados[0][1] = b;
        this.dados[1][0] = c;
        this.dados[1][1] = d;
    }

    public float calculaDeterminante(){
        return (dados[0][0] * dados[1][1]) - dados[0][1] * dados[1][0];
    }

    public void imprimeMatriz(){
        for (int i = 0; i <= 1; i++){
            for (int j = 0; j <= 1; j++){
                System.out.print(dados[i][j] + " ");
            }
            System.out.println(" ");
        }
    }
}
