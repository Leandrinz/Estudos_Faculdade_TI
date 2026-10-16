import java.util.ArrayList;

public class Agenda {
    private ArrayList<EntradaEmAgenda> compromissos;

    public Agenda(){
        this.compromissos = new ArrayList<>();
    }

    public void adicionaCompromisso(EntradaEmAgenda compromisso){
        this.compromissos.add(compromisso);
    }

    public void listaDia(int dia, int mes, int ano){
        for (EntradaEmAgenda compromisso : this.compromissos){
            if (compromisso.ehNoDia(dia, mes, ano)){
                System.out.println(compromisso.toString());
            }
        }
    }
}
