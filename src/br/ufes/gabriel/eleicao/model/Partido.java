package br.ufes.gabriel.eleicao.model;

import java.util.ArrayList;
import java.util.List;

public class Partido {
    private int numero;
    private String sigla;
    private List<Candidato> candidatos = new ArrayList<>();
    private int votosLegenda;

    public Partido(int numero, String sigla) {
        this.numero = numero;
        this.sigla = sigla;
        this.votosLegenda = 0;
    }

    public int getNumero() {
        return numero;
    }

    public String getSigla() {
        return sigla;
    }

    public List<Candidato> getCandidatos() {
        return candidatos;
    }

    public int getVotosLegenda() {
        return votosLegenda;
    }

    public void addCandidato(Candidato c) {
        candidatos.add(c);
    }

    public void addVotosLegenda(int qtd) {
        votosLegenda += qtd;
    }

    public int getVotosNominais() {
        // Garante que cada Candidato tem o metodo getQtdVotos()
        return candidatos.stream().mapToInt(Candidato::getQtdVotos).sum();
    }

    public int getTotalVotos() {
        return getVotosNominais() + votosLegenda;
    }

    public void ordenaCandidatos(){
        candidatos.sort(null);
    }
}
