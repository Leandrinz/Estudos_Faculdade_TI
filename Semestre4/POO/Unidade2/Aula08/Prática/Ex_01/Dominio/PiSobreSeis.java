package Semestre4.POO.Unidade2.Aula08.Prática.Ex_01.Dominio;

public class PiSobreSeis {
    public static void main(String[] args) {
        double[] termosDaSerie = new double[1000000];

        for (int i = 0; i < termosDaSerie.length; i++){
            termosDaSerie[i] = 1.0 / Math.pow(i+1, 2.0);
        }

        for (int n = 1; n < 1000000; n *= 10){
            calculaEMostraSomatoria(termosDaSerie, n);
        }

    }

    public static void calculaEMostraSomatoria(double[] vetor, int num){
        double soma = 0.0;
        for (int i = 0; i < num; i++){
            soma += vetor[i];
        }
        System.out.println("Soma dos " + num + " elementos iniciais: " + soma);
    }

}
