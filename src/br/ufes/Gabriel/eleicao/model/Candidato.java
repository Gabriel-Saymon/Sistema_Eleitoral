package br.ufes.Gabriel.eleicao.model;

import java.time.LocalDate;

public class Candidato {
    private String sgUe;              // Codigo do municipio
    private int cdCargo;              // 13 = vereador
    private int nrCandidato;          // Numero do candidato
    private String nmCandidato;       // Nome do Candidato 
    private String nmUrnaCandidato;   // Nome na urna
    private int nrPartido;            // Numero do partido
    private String sgPartido;         // Sigla do partido
    private int nrFederacao;          // Numero da federação (-1 = isolado)
    private LocalDate dtNascimento;   // Data de nascimento
    private int cdGenero;             // 2 = masculino, 4 = feminino
    private int cdSitTotTurno;        // 2 ou 3 = eleito; -1 = invalido

    private int qtdVotos;             // Votos nominais

    // Formata datas no padrao dia/mes/ano (ex: 06/10/2024)
    //private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Candidato(String sgUe, int cdCargo, int nrCandidato, String nmCandidato, String nmUrnaCandidato, int nrPartido, String sgPartido, int nrFederacao,
                     LocalDate dtNascimento,
                     int cdGenero,
                     int cdSitTotTurno) {
        this.sgUe = sgUe;
        this.cdCargo = cdCargo;
        this.nrCandidato = nrCandidato;
        this.nmCandidato = nmCandidato;       
        this.nmUrnaCandidato = nmUrnaCandidato;
        this.nrPartido = nrPartido;
        this.sgPartido = sgPartido;
        this.nrFederacao = nrFederacao;
        this.dtNascimento = dtNascimento;
        this.cdGenero = cdGenero;
        this.cdSitTotTurno = cdSitTotTurno;
    }
   
    public String getNmCandidato() {
        return this.nmCandidato;
    }
    public String getSgUe() { 
        return sgUe; 
    }
    public int getCdCargo() { 
        return cdCargo; 
    }
    public int getNrCandidato() { 
        return nrCandidato; 
    }
    public String getNmUrnaCandidato() { 
        return nmUrnaCandidato; 
    }
    public int getNrPartido() { 
        return nrPartido; 
    }
    public String getSgPartido() { 
        return sgPartido; 
    }
    public int getNrFederacao() { 
        return nrFederacao; 
    }
    public LocalDate getDtNascimento() { 
        return dtNascimento; 
    }
    public int getCdGenero() { 
        return cdGenero; 
    }
    public int getCdSitTotTurno() { 
        return cdSitTotTurno; 
    }


    // Calcula a idade com base na data da eleição
    public int getIdade(LocalDate dataEleicao) {
        return java.time.Period.between(dtNascimento, dataEleicao).getYears();
    }

    // Verifica se foi eleito (situação 2 ou 3) 
    public boolean isEleito() {
        return cdSitTotTurno == 2 || cdSitTotTurno == 3;
    }

    public int getQtdVotos() {
        return this.qtdVotos;
    }

    public void addVotos(int qtd) {
        this.qtdVotos += qtd;
    }

}