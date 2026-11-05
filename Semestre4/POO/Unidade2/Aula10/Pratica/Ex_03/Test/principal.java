package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Test;

import Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Dominio.Aviao;
import Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Dominio.Barco;
import Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Dominio.Carro;
import Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Dominio.Controle;
import Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Dominio.Helicoptero;

public class principal {
    public static void main(String[] args) {
        Controle controle = new Controle();

        controle.controlar(new Aviao(100, "ANAC-123", "PR-ABC"));
        controle.controlar(new Barco(30, 5.5, "BC-001"));
        controle.controlar(new Carro(12, 4, "ABC-1234"));
        controle.controlar(new Helicoptero(15, "ANAC-777"));
        
    }
}