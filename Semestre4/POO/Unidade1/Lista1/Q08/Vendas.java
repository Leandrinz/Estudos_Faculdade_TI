package Semestre4.POO.Unidade1.Lista1.Q08;

import java.util.Scanner;

public class Vendas {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        int codigoSetor = 0;
        double valorOriginalDoProduto = 0.0;
        double novoValorDoProduto = 0.0;

        System.out.println("Digite o código do setor: ");
        codigoSetor = input.nextInt();

        if (codigoSetor != 111 && codigoSetor != 222){
            System.out.println("Setor Invalido");
        }
        else{
            System.out.println("Digite o valor Original do produto: ");
            valorOriginalDoProduto = input.nextDouble();

            if (codigoSetor == 111){
                if (valorOriginalDoProduto >= 500.0){
                    novoValorDoProduto = valorOriginalDoProduto * 0.90;
                }
                else{
                    novoValorDoProduto = valorOriginalDoProduto;
                }
            }
            else{
                if (valorOriginalDoProduto > 100.0){
                    novoValorDoProduto = valorOriginalDoProduto * 0.60;
                }
                else if (valorOriginalDoProduto >= 50 && valorOriginalDoProduto <= 100){
                    novoValorDoProduto = valorOriginalDoProduto * 0.80;
                }
                else{
                    novoValorDoProduto = valorOriginalDoProduto * 0.90;
                }
            }
            if (codigoSetor == 111){
                System.out.println("Setor: Eletros");
                System.out.println("Valor com Desconto: " + novoValorDoProduto);
            }
            else{
                System.out.println("Setor: Cama, mesa e banho");
                System.out.println("Valor com Desconto: " + novoValorDoProduto);
            }
        }
        input.close();
    }
}
