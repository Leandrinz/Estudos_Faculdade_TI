package Semestre4.POO.Unidade1.Aula07.Prática.Ex_02.Test;

import Semestre4.POO.Unidade1.Aula07.Prática.Ex_02.Dominio.Igualdade;

public class IgualdadeTest01 {
    public static void main(String[] args) {
        if (Igualdade.ehIgual(2.5, 5.5)){
            System.out.println("Double's iguais");
        }
        else{
            System.out.println("Diferentes");
        }
        if (Igualdade.ehIgual(2, 4)){
            System.out.println("Inteiros iguais");
        }
        else{
            System.out.println("Diferentes");
        }
        if (Igualdade.ehIgual("Vasco", "Vasco")){
            System.out.println("Strings iguais");
        }
        else{
            System.out.println("Diferentes");
        }
    }
}
