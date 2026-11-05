package Semestre4.POO.Unidade2.Aula10.Pratica.Ex_03.Dominio;

abstract class Veiculo {
    protected int capacidade;

    public Veiculo(int capacidade){
        setCapacidade(capacidade);
    }

    public int getCapacidade(){
        return this.capacidade;
    }

    public void setCapacidade(int capacidade){
        if (capacidade < 10){
            throw new IllegalArgumentException("Capacidade mínima é 10");
        }
        this.capacidade = capacidade;
    }

    public abstract void moverFrente();
}
