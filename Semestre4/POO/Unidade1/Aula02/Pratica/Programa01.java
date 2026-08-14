package Semestre4.POO.Unidade1.Aula02.Pratica;
import java.util.Scanner;

public class Programa01 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String nome = input.nextLine();
        System.out.println("Bem-Vindo(a), "+nome);

        input.close();
    }
}
