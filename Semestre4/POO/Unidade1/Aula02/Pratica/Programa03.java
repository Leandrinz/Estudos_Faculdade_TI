package Semestre4.POO.Unidade1.Aula02.Pratica;

import java.util.Scanner;

public class Programa03 {
    public static void main(String[] args) {
        int ano, dia, mes;
        Scanner input = new Scanner(System.in);
        System.out.print("Informe o dia: ");
        dia = input.nextInt();
        System.out.print("Informe o mês: ");
        mes = input.nextInt();
        System.out.print("Informe o ano: ");
        ano = input.nextInt();

        if (mes == 1){
            System.out.println(dia+ " de Janeiro de "+ano);
        }
        else if (mes == 2){
            System.out.println(dia+ " de Fevereiro de "+ano);
        }
        else if (mes == 3){
            System.out.println(dia+ " de Março de "+ano);
        }
        else if (mes == 4){
            System.out.println(dia+ " de Abril de "+ano);
        }
        else if (mes == 5){
            System.out.println(dia+ " de Maio de "+ano);
        }
        else if (mes == 6){
            System.out.println(dia+ " de Junho de "+ano);
        }
        else if (mes == 7){
            System.out.println(dia+ " de Julho de "+ano);
        }
        else if (mes == 8){
            System.out.println(dia+ " de Agosto de "+ano);
        }
        else if (mes == 9){
            System.out.println(dia+ " de Setembro de "+ano);
        }
        else if (mes == 10){
            System.out.println(dia+ " de Outubro de "+ano);
        }
        else if (mes == 11){
            System.out.println(dia+ " de Novembro de "+ano);
        }
        else if (mes == 12){
            System.out.println(dia+ " de Dezembro de "+ano);
        }
        else{
            System.out.println("Mês inválido");
        }

        switch (mes) {
            case 1, 3, 5, 7, 8, 10, 12:
                System.out.println("O mês "+ mes + " possui 31 dias");
                break;
            case 2:
                System.out.println("O mês "+ mes + " possui 28 dias");
                break;
            case 4, 6, 9, 11:
                System.out.println("O mês "+ mes + " possui 31 dias");
            default:
                System.out.println("Mês inválido");
                break;
        }

        input.close();

    }
}
