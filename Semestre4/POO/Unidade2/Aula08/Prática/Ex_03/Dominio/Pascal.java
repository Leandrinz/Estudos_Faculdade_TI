package Semestre4.POO.Unidade2.Aula08.Prática.Ex_03.Dominio;

import java.util.Scanner;

public class Pascal {
     public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        System.out.println("Linhas: ");
        int linhas = entrada.nextInt();

        int tri[][] = new int[linhas][];

        for (int l = 0; l < tri.length; l++) {

            tri[l] = new int[l + 2];

            tri[l][0] = 1;
            tri[l][l + 1] = 1;

            for (int c = 1; c < tri[l].length - 1; c++) {
                tri[l][c] = tri[l - 1][c - 1] + tri[l - 1][c];
            }
        }

        for (int l = 0; l < tri.length; l++) {

            for (int c = 0; c < tri[l].length; c++) {
                System.out.print(tri[l][c] + " ");
            }

            System.out.println();
        }

        entrada.close();
    }
}
