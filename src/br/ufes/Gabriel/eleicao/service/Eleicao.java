package br.ufes.Gabriel.eleicao.service;

import br.ufes.Gabriel.eleicao.model.Candidato;
import br.ufes.Gabriel.eleicao.model.Partido;

import java.time.LocalDate;
import java.util.*;

/**
 * Classe que representa a eleição: armazena candidatos, partidos
 * e metadados como código do município e data da eleição.
 */
public class Eleicao {
    private final String codMunicipio;
    private final LocalDate dataEleicao;
    private int qtdVagas;
    private final Map<Integer, Candidato> candidatos;
    private final Map<Integer, Partido> partidosMap;
    private final List<Partido> partidosList;

    /**
     * Constrói a eleição para um município e data dados.
     *
     * @param codMunicipio  código de município (SG_UE)
     * @param dataEleicao   data da eleição como LocalDate
     */
    public Eleicao(String codMunicipio, LocalDate dataEleicao) {
        this.codMunicipio = codMunicipio;
        this.dataEleicao = dataEleicao;
        this.qtdVagas = 0;
        this.candidatos = new LinkedHashMap<>();
        this.partidosMap = new LinkedHashMap<>();
        this.partidosList = new ArrayList<>();
    }

    /**
     * Registra um candidato na eleição.
     * Incrementa o total de vagas caso o candidato seja eleito.
     *
     * @param cand candidato a ser adicionado
     */
    public void adicionaCandidato(Candidato cand) {
        candidatos.put(cand.getNrCandidato(), cand);
        if (cand.isEleito()) {
            qtdVagas++;
        }
    }

    /**
     * Adiciona um partido à lista de partidos.
     *
     * @param partido partido a ser registrado
     */
    public void adicionaPartido(Partido partido) {
        partidosMap.put(partido.getNumero(), partido);
        partidosList.add(partido);
    }

    /**
     * Recupera um partido pelo seu número.
     *
     * @param numero número do partido
     * @return Partido ou null se não existir
     */
    public Partido getPartidoPorNumero(int numero) {
        return partidosMap.get(numero);
    }

    /**
     * Imprime para conferência a posição e o nome de urna de cada candidato.
     */
    public void printConferenciaCandidatos() {
        System.out.println("Conferência de candidatos carregados:");
        List<Candidato> lista = new ArrayList<>(candidatos.values());
        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("Posição %2d → %s%n", i, lista.get(i).getNmUrnaCandidato());
        }
    }

    /**
     * Imprime para conferência o total de votos nominais por candidato e
     * votos de legenda por partido.
     */
    public void printConferenciaVotos() {
        System.out.println("Conferência de votos nominais por candidato:");
        List<Candidato> lista = new ArrayList<>(candidatos.values());
        for (int i = 0; i < lista.size(); i++) {
            Candidato c = lista.get(i);
            System.out.printf("#%2d %s (%d): %d votos nominais%n",
                i, c.getNmUrnaCandidato(), c.getNrCandidato(), c.getQtdVotos()
            );
        }
        System.out.println("\nConferência de votos de legenda por partido:");
        for (Partido p : partidosList) {
            System.out.printf("Partido %s (%d): %d votos de legenda%n",
                p.getSigla(), p.getNumero(), p.getVotosLegenda()
            );
        }
    }

    // --- getters para uso nos relatórios ---

    public String getCodMunicipio() {
        return codMunicipio;
    }

    public LocalDate getDataEleicao() {
        return dataEleicao;
    }

    public int getQtdVagas() {
        return qtdVagas;
    }

    public Map<Integer, Candidato> getCandidatos() {
        return Collections.unmodifiableMap(candidatos);
    }

    public List<Partido> getPartidos() {
        return Collections.unmodifiableList(partidosList);
    }
}
