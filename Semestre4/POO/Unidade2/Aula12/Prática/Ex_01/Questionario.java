package Semestre4.POO.Unidade2.Aula12.Prática.Ex_01;

public class Questionario {
    public static void main(String[] args) {
        int resp[] = {0, 2, 3, 5, 1, 14, 2, 0, 3, 3};
        int cont[] = new int[6];
        for (int i = 0; i < cont.length; i++){
            cont[i] = 0;
        }
        for (int r : resp){
            try{
                cont[r]++;
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println(r + "???? Digita de 0 a 5 irmão, tá tirando é?");
            }
        }
        for (int i = 0; i < cont.length; i++){
            System.out.println(i + " : " + cont[i]);
        }
    }
}

