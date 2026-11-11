package Semestre4.POO.Unidade2.ListaV.Q01;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static int obterIntValido(){
        Scanner input = new Scanner(System.in);
        int numero = 0;
        boolean entradaValida = false;

        while(!entradaValida){
            try{
                System.out.println("Digite um número inteiro: ");
                numero = input.nextInt();
                entradaValida = true;
            } catch(InputMismatchException e){
                System.out.println("Erro: Valor inválido! Por favor, digite um número inteiro!!!");
                input.next();
            }
        }
        return numero;
    }
    public static void main(String[] args) {
        System.out.println("==== Obtendo primeiro operando ====");
        int numero1 = obterIntValido();
        System.out.println("==== Obtendo segundo operando ==== ");
        int numero2 = obterIntValido();

        int soma = numero1 + numero2;

        System.out.println("====== Soma ======");
        System.out.println("Resultado: " + numero1 + " + " + numero2 + " = " + soma);
    }
}
