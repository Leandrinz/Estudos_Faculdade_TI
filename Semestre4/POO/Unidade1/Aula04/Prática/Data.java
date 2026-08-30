package Semestre4.POO.Unidade1.Aula04.Prática;

public class Data {
    int dia;
    int mes;
    int ano;

    public void inicializaData(int d, int m, int a){
        dia = d;
        mes = m;
        ano = a;
        if (!dataEhValida()){
            dia = 1;
            mes = 1;
            ano = 1971;
        }
    }

    public boolean dataEhValida(){
        return dia >= 1 && dia <= 30 && mes >= 1 && mes <= 12 && ano >= 1;
    }

    public void mostraData(){
        System.out.println("Data: " +dia+ "/ " +mes+ "/" +ano);
    }
}
