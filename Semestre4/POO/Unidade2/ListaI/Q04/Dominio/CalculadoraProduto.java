package Semestre4.POO.Unidade2.ListaI.Q4.Dominio;

public class CalculadoraProduto {
    public static int produto(int ... numeros){
        if (numeros.length == 0){
            return 0;
        }

        int auxiliador = 1;

        for (int numero : numeros){
            auxiliador *= numero;
        }

        return auxiliador;
    }
}
