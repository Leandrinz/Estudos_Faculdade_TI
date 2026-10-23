package Semestre4.POO.Unidade2.ListaI.Q3.Test;

import java.util.ArrayList;
import java.util.Scanner;

import Semestre4.POO.Unidade2.ListaI.Q3.Dominio.Cliente;

public class ClienteTest01 {
    public static void main(String[] args) {
    int id;
    String nome;
    int idade;
    int telefone;
    ArrayList<Cliente> clientes = new ArrayList<>();
    Scanner input = new Scanner(System.in);

    System.out.println("Olá, bem vindo ao cadastro de clientes");

    while(true){
        System.out.println("Digite o id do cliente: ");
        id = input.nextInt();
        if (id < 0){
            break;
        }

        input.nextLine(); // Limpa Buffer pra evitar bug

        System.out.println("Digite o nome do cliente: ");
        nome = input.nextLine();
        System.out.println("Digite a idade do cliente: ");
        idade = input.nextInt();
        System.out.println("Digite o telefone do cliente: ");
        telefone = input.nextInt();

        input.nextLine();

        Cliente cliente = new Cliente(id, nome, idade, telefone);
        clientes.add(cliente);
    }
    System.out.println("=======================");
    System.out.println("Imprimindo os dados dos clientes");
    System.out.println("=======================");
    for (Cliente cliente : clientes){
        System.out.println(cliente.toString());
    }
    input.close();
}
}
