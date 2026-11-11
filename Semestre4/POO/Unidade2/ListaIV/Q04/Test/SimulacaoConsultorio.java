package Semestre4.POO.Unidade2.ListaIV.Q04.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import Semestre4.POO.Unidade2.ListaIV.Q04.Dominio.Paciente;

public class SimulacaoConsultorio {
    public static void main(String[] args) {
        Queue<Paciente> filaPrioritaria = new LinkedList<>();
        Queue<Paciente> filaComum = new LinkedList<>();
        Random random = new Random();

        int minutoAtual = 0;
        int pacienteAtendidosTotal = 0;
        int tempoRestanteConsulta = 0;
        Paciente pacienteEmAtendimento = null;

        System.out.println("====== Início do Dia de Atendimento no Consultório ===");

        while (pacienteAtendidosTotal < 20) {
            if (minutoAtual == 0 || minutoAtual % 4 == 0){
                String rg = String.format("%07d", random.nextInt(10000000));
                int idade = random.nextInt(95) + 1;

                Paciente novoPaciente = new Paciente(rg, idade);
                System.out.println("[Minuto " + minutoAtual + "] Chegou novo paciente -> " + novoPaciente);

                if (novoPaciente.isPrioritario()){
                    filaPrioritaria.add(novoPaciente);
                }
                else{
                    filaComum.add(novoPaciente);
                }
            }

                if (tempoRestanteConsulta > 0){
                    tempoRestanteConsulta--;
                    if (tempoRestanteConsulta == 0){
                        System.out.println("[Minuto " + minutoAtual + "] Fim da consulta do paciente RG: " + pacienteEmAtendimento.getRG());
                        pacienteAtendidosTotal++;
                        pacienteEmAtendimento = null;
                    }
                }

                if (tempoRestanteConsulta == 0){
                    if(!filaPrioritaria.isEmpty()){
                        pacienteEmAtendimento = filaPrioritaria.poll();
                        tempoRestanteConsulta = 5;
                        System.out.println("[Minuto " + minutoAtual + "] -> Médico chamou da fila prioritária: " + pacienteEmAtendimento);
                    }
                    else if(!filaComum.isEmpty()){
                        pacienteEmAtendimento = filaComum.poll();
                        tempoRestanteConsulta = 5;
                        System.out.println("[Minuto " + minutoAtual + "] -> Médico Chamou da fila comum: " + pacienteEmAtendimento);
                    }
                }
            minutoAtual++;
        }
        System.out.println("=== Fim do Dia: Os 20 Pacientes foram atendidos com Sucesso! ===");
    }
}
