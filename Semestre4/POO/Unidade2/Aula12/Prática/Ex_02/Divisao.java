package Semestre4.POO.Unidade2.Aula12.Prática.Ex_02;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Divisao {
    public static void main(String[] args) {
        Scanner ent = new Scanner(System.in);
        int n, d;

        try{
            System.out.println("Numerador: ");
            n = ent.nextInt();

            System.out.println("Denominador: ");
            d = ent.nextInt();

            int resp = divisao(n, d);
            System.out.println("Resposta: " + resp);
        } catch (ArithmeticException e){
            System.out.println("Divisão por zero não é possível");
        } catch (InputMismatchException e){
            System.out.println("Digite um valor inteiro");
        } finally {
            ent.close();
        }
    }

    public static int divisao(int num, int den){
        return num / den;
    }
}
