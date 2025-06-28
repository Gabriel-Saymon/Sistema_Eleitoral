package br.ufes.Gabriel.eleicao.util;

import br.ufes.Gabriel.eleicao.service.Eleicao;
import br.ufes.Gabriel.eleicao.model.Candidato;
import br.ufes.Gabriel.eleicao.model.Partido;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe responsável pela leitura dos arquivos CSV de candidatos e de votos
 * e registro dos dados na instância de Eleicao.
 */
public class Leitor {
    private BufferedReader brCandidatos;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Inicializa o leitor para o arquivo de candidatos.
     *
     * @param caminhoArquivoCandidatos CSV de candidatos (ISO-8859-1, ";" como separador)
     */
    public Leitor(String caminhoArquivoCandidatos) {
        try {
            InputStream is = new FileInputStream(caminhoArquivoCandidatos);
            InputStreamReader isr = new InputStreamReader(is, "ISO-8859-1");
            this.brCandidatos = new BufferedReader(isr);
        } catch (IOException e) {
            System.err.println("Erro ao abrir candidatos: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Lê e processa o CSV de candidatos, filtrando por município, cargo=13 e situação válida,
     * registrando em Eleicao e agrupando no respectivo Partido.
     */
    public void leituraCandidatos(Eleicao eleicao) {
        try {
            String linha = brCandidatos.readLine(); // cabeçalho
            while ((linha = brCandidatos.readLine()) != null) {
                String[] cols = linha.split(";");
                for (int i = 0; i < cols.length; i++) cols[i] = cols[i].replace("\"", "");

                String sgUe = cols[11];
                int cdCargo = Integer.parseInt(cols[13]);
                int cdSit = Integer.parseInt(cols[48]);
                if (!sgUe.equals(eleicao.getCodMunicipio()) || cdCargo != 13 || cdSit == -1) continue;

                int nrCandidato = Integer.parseInt(cols[16]);
                String nmCandidato = cols[17];
                String nmUrna = cols[18];
                int nrPartido = Integer.parseInt(cols[25]);
                String sgPartido = cols[26];
                int nrFederacao = Integer.parseInt(cols[28]);
                LocalDate dtNasc = LocalDate.parse(cols[36], formatter);
                int cdGenero = Integer.parseInt(cols[38]);

                Candidato cand = new Candidato(sgUe, cdCargo, nrCandidato, nmCandidato, nmUrna, nrPartido, sgPartido, nrFederacao, dtNasc, cdGenero, cdSit);

                eleicao.adicionaCandidato(cand);

                Partido partido = eleicao.getPartidoPorNumero(nrPartido);
                if (partido == null) {
                    partido = new Partido(nrPartido, sgPartido);
                    eleicao.adicionaPartido(partido);
                }
                partido.addCandidato(cand);
            }
        } catch (IOException e) {
            System.err.println("Erro de I/O ao ler candidatos: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Lê e processa o CSV de votos, filtrando por município e cargo=13,
     * ignorando votos 95–98, e distribuindo votos nominais aos candidatos
     * e votos de legenda ao partido correspondente.
     *
     * @param eleicao             instância que receberá os votos
     * @param caminhoArquivoVotos caminho do CSV de votação (ISO-8859-1, ";" como separador)
     */
    public void leituraVotos(Eleicao eleicao, String caminhoArquivoVotos) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(caminhoArquivoVotos),
                StandardCharsets.ISO_8859_1
                ))) {

            // pula cabeçalho
            String linha = br.readLine();

            while ((linha = br.readLine()) != null) {
                String[] cols = linha.split(";");
                // remove aspas
                for (int i = 0; i < cols.length; i++) {
                    cols[i] = cols[i].replace("\"", "");
                }

                // usa SG_UE (indice 11) ou CD_MUNICIPIO (13) — aqui vamos com SG_UE
                String sgUe     = cols[11];           
                int cdCargo     = Integer.parseInt(cols[17]);  // CD_CARGO = 17
                if (!sgUe.equals(eleicao.getCodMunicipio()) || cdCargo != 13) {
                    continue;
                }

                int nrVotavel   = Integer.parseInt(cols[19]);  // NR_VOTAVEL = 19
                int qtVotos     = Integer.parseInt(cols[21]);  // QT_VOTOS = 21

                // ignora votos especiais 95–98
                if (nrVotavel >= 95 && nrVotavel <= 98) {
                    continue;
                }

                // nominal = 5 dígitos
                if (String.valueOf(nrVotavel).length() == 5) {
                    Candidato cand = eleicao.getCandidatos().get(nrVotavel);
                    if (cand != null) {
                        cand.addVotos(qtVotos);
                    }
                } else {
                    Partido p = eleicao.getPartidoPorNumero(nrVotavel);
                    if (p != null) {
                        p.addVotosLegenda(qtVotos);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro de I/O ao ler votos: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
