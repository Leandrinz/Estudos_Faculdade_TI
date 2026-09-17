package Semestre4.POO.Unidade1.Lista3.Q07.Dominio;

public class TresAtributos <T>{
    private T atributo1;
    private T atributo2;
    private T atributo3;

    public TresAtributos(T atributo1, T atributo2, T atributo3){
        this.atributo1 = atributo1;
        this.atributo2 = atributo2;
        this.atributo3 = atributo3;
    }

    public void quantidadeIguais(){

        if (atributo1 == atributo2 && atributo1 == atributo3){
            System.out.println("3 iguais");
        }
        else if ((atributo1 == atributo2 || atributo1 == atributo3) && (atributo1 != atributo2 || atributo1 != atributo3)){
            System.out.println("2 iguais");
        }
        else{
            System.out.println("Nenhum igual");;
        }
    }

    public void imprimeAtributos(){
        System.out.println("Atributo 1: " + atributo1);
        System.out.println("Atributo 2: " + atributo2);
        System.out.println("Atributo 3: " + atributo3);
    }
}
