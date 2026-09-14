package Semestre4.POO.Unidade1.Lista1.Q11;
import java.util.Scanner;
public class Pontos_Cartesianos {
    public static void main(){
        Scanner input = new Scanner(System.in);
        System.out.println("Digite o x do Ponto 1: ");
        int xPonto1 = input.nextInt();
        System.out.println("Digite o y do Ponto 1: ");
        int yPonto1 = input.nextInt();
        System.out.println("Digite o x do ponto 2: ");
        int xPonto2 = input.nextInt();
        System.out.println("Digite o y do Ponto 2: ");
        int yPonto2 = input.nextInt();

        if (yPonto2 > yPonto1){
            System.out.print("Ponto 2 está acima ");
        }
        else if (yPonto2 < yPonto1){
            System.out.print("Ponto 2 está abaixo ");
        }
        else{}

        if (xPonto2 > xPonto1){
            System.out.print(" e a direita ");
        }
        else if (xPonto2 < xPonto1){
            System.out.print("e a esquerda ");
        }
        else{}

        if (xPonto1 != xPonto2 || yPonto1 != yPonto2){
            System.out.print("Do ponto 1");
        }
        else{}
        input.close();
    }
}
