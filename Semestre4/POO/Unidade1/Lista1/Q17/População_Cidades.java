package Semestre4.POO.Unidade1.Lista1.Q17;

public class População_Cidades {
    public static void main(String[] args) {
        double populacaoCidadeA = 7000;
        double populacaoCidadeB = 20000;

        int quantidadeDeAnos = 0;

        while (true){
            double crescimentoPorAnoCidadeA = populacaoCidadeA * 0.035;
            double crescimentoPorAnoCidadeB = populacaoCidadeB * 0.01;
            if (populacaoCidadeA < populacaoCidadeB){
                populacaoCidadeA += crescimentoPorAnoCidadeA;
                populacaoCidadeB += crescimentoPorAnoCidadeB;
                quantidadeDeAnos++;
            }
            else{
                break;
            }
        }
        System.out.println("Quantidade de anos necessária: "+ quantidadeDeAnos);
    }
}
