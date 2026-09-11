package Semestre4.POO.Unidade1.Aula07.Prática.Ex_01.Test;

import Semestre4.POO.Unidade1.Aula07.Prática.Ex_01.Dominio.ConversaoUnidades;

public class ConversaoUnidadesTest01 {
    public static void main(String[] args) {
        double polegadas = 3.5;
        double centimentros = ConversaoUnidades.polegadasParaCentimetros(polegadas);
        System.out.println("Polegadas "+ polegadas + " - "+ "Centimetros "+ centimentros);

        double pes = 4.0;
        centimentros = ConversaoUnidades.pesParaCentimetro(pes);
        System.out.println("Centimetros: "+centimentros);

        double milhas = 10;
        double kilometros = ConversaoUnidades.milhasParaKilometros(milhas);
        System.out.println("Kilometros: "+ kilometros);
    }
}
