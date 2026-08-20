package Semestre4.POO.Unidade1.Aula03.Pratica;

public class Programa1 {
    public static void main(String[] args) {
        float kmh = 0.0F, mps, mph, pps;
        // 1km/h = 0.2778 metros / s

        System.out.println("Km/h\tm/s\tmph\tpps");
        while (kmh <= 50.0F){
            mps =  0.2778F * kmh;
            mph = 0.6214F * kmh;
            pps = 0.9113F * kmh;
            System.out.printf("%.2f\t%.2f\t%.2f\t%.2f\t\n", kmh, mps, mph, pps);
            kmh += 0.5F;
        }
    }
}
