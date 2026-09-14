package Semestre4.POO.Unidade1.Lista1.Q15;

public class Numero_Perfeito {
    public static void main(String[] args) {
        int numeroCrescente = 1;
        for (int i = 1; i <= 4; numeroCrescente++){
            int contador = 0;
            for (int j = 1; j < numeroCrescente; j++){
                if (numeroCrescente == 1){
                    continue;
                }
                else{
                    if (numeroCrescente % j == 0){
                        contador += j;
                    }
                }
            }
            if (contador == numeroCrescente){
                System.out.println(i + " - " + contador);
                i++;
            }
        }
    }
}
