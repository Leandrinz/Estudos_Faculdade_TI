package Semestre4.POO.Unidade1.Aula03.Pratica;

import java.util.Scanner;
import java.time.*;

public class Programa2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        YearMonth atual = YearMonth.now();

        String nome;
        String numero;
        String codigo;
        int mes;
        int ano;

        int anoAtual = atual.getYear();
        int mesAtual = atual.getMonthValue();
        System.out.println();

        System.out.print("Nome:");
        nome = input.nextLine();
        System.out.print("Numero: ");
        numero = input.nextLine();
        System.out.print("Codigo: ");
        codigo = input.nextLine();

        boolean vencido;

        do{

            System.out.print("Mês: ");
            mes = input.nextInt();
            System.out.print("Ano: ");
            ano = input.nextInt();
            vencido = (ano < anoAtual || (ano == anoAtual && mes < mesAtual));
            if (vencido == true){
                System.out.println("Dados inválidos, cartão vencido!!!");
            }
            else{
                System.out.println("Cartão Validado!!!");
            }
        }while(vencido);


        input.close();
    }

}
