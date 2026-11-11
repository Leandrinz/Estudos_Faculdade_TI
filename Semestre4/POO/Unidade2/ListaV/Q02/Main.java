package Semestre4.POO.Unidade2.ListaV.Q02;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void verifica(String[] meses){
        Scanner input = new Scanner(System.in);
        int numero;
        boolean valorValido = false;
        while(!valorValido){
            try{
                System.out.println("Digite um valor de 1 a 12: ");
                numero = input.nextInt();
                System.out.println("Mês: " + meses[numero - 1]);
                valorValido = true;
            } catch (InputMismatchException e){
                System.out.println("Erro!!! Digite um valor inteiro!!!");
                input.next();
            } catch (IndexOutOfBoundsException e){
                System.out.println("Erro, digite um valor de 1 a 12");
            }
        }
    }
    public static void main(String[] args) {
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };

        verifica(meses);
    }
}
