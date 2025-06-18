package br.ufes.Gabriel.eleicao.model;

public class Voto {
    private int numeroVotavel;
    private int quantidade;
    private boolean nominal;

    public Voto(int numeroVotavel, int quantidade, boolean nominal) {
        this.numeroVotavel = numeroVotavel;
        this.quantidade = quantidade;
        this.nominal = nominal;
    }

    public int getNumeroVotavel() {
        return numeroVotavel;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public boolean isNominal() {
        return nominal;
    }
}
