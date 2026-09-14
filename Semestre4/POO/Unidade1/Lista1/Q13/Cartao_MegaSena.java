package Semestre4.POO.Unidade1.Lista1.Q13;

public class Cartao_MegaSena {
    public static void main(String[] args) {
        for (int i = 1; i <= 60; i++){
            if (i <= 9){
                    System.out.print("[0"+i+"] ");
                }
            else{
                System.out.print("["+i+"] ");
            }
            if (i % 10 == 0){
                System.out.println(" ");
            }
        }
    }
}
