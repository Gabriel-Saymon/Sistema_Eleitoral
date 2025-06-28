package br.ufes.Gabriel.eleicao.service;

import br.ufes.Gabriel.eleicao.model.Candidato;
import br.ufes.Gabriel.eleicao.model.Partido;
import java.text.NumberFormat;
import java.time.Period;
import java.util.*;

public class Relatorio {
   private Eleicao deputados;
    private List<Candidato> candidatosOrdenados;
    private List<Partido> partidosOrdenados;

    public Relatorio(Eleicao deputados) {
        this.deputados = deputados;
        // armazena uma lista ordenada de deputados por votos
        this.candidatosOrdenados = new ArrayList<>(deputados.getCandidatos().values());
        this.candidatosOrdenados.sort(Comparator.comparingInt(Candidato::getQtdVotos).reversed());
        // faz o mesmo com partidos ordenados por total de votos
        this.partidosOrdenados = new ArrayList<>(deputados.getPartidos());
        this.partidosOrdenados.sort(Comparator.comparingInt(Partido::getTotalVotos).reversed());
    }

    public void gerarRelatorios() {
        printNumeroVagas();
        printCandidatosMaisVotados();
        printVotacaoPorPartido();
        printPrimeiroUltimoPorPartido();
        printDistribuicaoFaixaEtaria();
        printDistribuicaoGenero();
        printTotalVotos();
    }

    public void printNumeroVagas() {
        System.out.println("Número de vagas: " + deputados.getQtdVagas() + "\n");
    }

    public void printCandidatosMaisVotados() {
        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setGroupingUsed(true);

        int numeroVagas = deputados.getNumeroCandidatosEleitos();
        List<Candidato> topN         = new ArrayList<>();
        List<Candidato> prejudicados = new ArrayList<>();
        List<Candidato> beneficiados = new ArrayList<>();

        // 2, 3, 4 e 5
        int contador = 1;
        for (Candidato c : candidatosOrdenados) {
            // Monta top-N
            if (contador <= numeroVagas) {
                topN.add(c);
                // Prejudicados: dentro do top-N mas não eleitos
                if (!c.isEleito()) {
                    prejudicados.add(c);
                }
            }
            // Beneficiados: eleitos mas fora do top-N
            else if (c.isEleito()) {
                beneficiados.add(c);
            }
            contador++;
        }

        // 2. Eleitos pelo proporcional
        int count = 1;
        System.out.println("Vereadores Eleitos:");
        for (Candidato c : candidatosOrdenados) {
            if (c.isEleito()) {
                System.out.print(count + " - ");
            if (c.getNrFederacao() != -1) System.out.print("*");
            System.out.println(c.getNmUrnaCandidato() + " (" + c.getSgPartido() + ", " + nf.format(c.getQtdVotos()) + " votos)");
                
            count++;
            }
        }
        System.out.println();

        // 3. Top-N mais votados
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        contador = 1;
        for (Candidato c : topN) {
            System.out.print(contador + " - ");
            if (c.getNrFederacao() != -1) System.out.print("*");
            System.out.println(c.getNmUrnaCandidato() + " (" + c.getSgPartido() + ", " + nf.format(c.getQtdVotos()) + " votos)");
                
            contador++;
        }
        System.out.println();

        // 4. Teriam sido eleitos se fosse majoritário
        System.out.println("\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");
        for (Candidato c : prejudicados) {
            System.out.print((topN.indexOf(c) + 1) + " - ");
            if (c.getNrFederacao() != -1) System.out.print("*");
            System.out.println(c.getNmUrnaCandidato() + " (" + c.getSgPartido() + ", " + nf.format(c.getQtdVotos()) + " votos)");

        }
        System.out.println();

        // 5. Eleitos que se beneficiaram do proporcional
        System.out.println("\nEleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");
        for (Candidato c : beneficiados) {
            System.out.print((topN.indexOf(c) + 1) + " - ");
            if (c.getNrFederacao() != -1) System.out.print("*");
            System.out.println(c.getNmUrnaCandidato() + " (" + c.getSgPartido() + ", " + nf.format(c.getQtdVotos()) + " votos)");

        }
        System.out.println();
    }

    public void printVotacaoPorPartido() {
    NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
    nf.setGroupingUsed(true);

    System.out.println("\n6. Votação por partido e número de candidatos eleitos:");
    int contador = 1;
    for (Partido p : partidosOrdenados) {
        // votos nominais e de legenda
        int votosNominais = p.getVotosNominais();
        int votosLegenda  = p.getVotosLegenda();
        int totalVotos    = votosNominais + votosLegenda;
        // conta quantos eleitos em cada partido
        long eleitos = p.getCandidatos()
                        .stream()
                        .filter(Candidato::isEleito)
                        .count();

        System.out.printf(
            "%2d - %s - %d, %s votos (%s nominais e %s de legenda), %d %s eleito%s%n",
            contador++,
            p.getSigla(),
            p.getNumero(),
            nf.format(totalVotos),
            nf.format(votosNominais),
            nf.format(votosLegenda),
            eleitos,
            eleitos > 1 ? "candidatos" : "candidato",
            eleitos > 1 ? "" : ""  // se quiser adaptar pluralização diferente
        );
    }
}



    public void printPrimeiroUltimoPorPartido() {
        // 1) Ordena cada lista interna de candidatos no partido com comparador explícito
        for (Partido p : partidosOrdenados) {
            p.getCandidatos().sort(
                Comparator.comparingInt(Candidato::getQtdVotos).reversed()
            );
        }

        // 2) Coleta o “primeiro” de cada partido (se houver candidato)
        List<Candidato> primeiros = new ArrayList<>();
        for (Partido p : partidosOrdenados) {
            List<Candidato> lista = p.getCandidatos();
            if (!lista.isEmpty()) {
                primeiros.add(lista.get(0));
            }
        }

        // 3) Ordena essa lista de primeiros pelo número de votos (decrescente)
        primeiros.sort(
            Comparator.comparingInt(Candidato::getQtdVotos).reversed()
        );

        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setGroupingUsed(true);

        System.out.println("Primeiro e último colocados de cada partido:");
        int contador = 1;
        for (Candidato c : primeiros) {
            // Imprime o primeiro colocado do partido
            System.out.printf(
                "%2d - %s - %d, %s (%d, %s votos) / ",contador++, c.getSgPartido(), c.getNrPartido(), c.getNmUrnaCandidato(), c.getNrCandidato(), nf.format(c.getQtdVotos())
            );

            // Busca o último do partido pelo número do partido
            Partido partido = deputados.getPartidoPorNumero(c.getNrPartido());
            List<Candidato> lista = partido.getCandidatos();
            Candidato ultimo = lista.get(lista.size() - 1);

            // Imprime o último colocado
            System.out.printf(
                "%s (%d, %s votos)%n",
                ultimo.getNmUrnaCandidato(),
                ultimo.getNrCandidato(),
                nf.format(ultimo.getQtdVotos())
            );
        }
    }

    public void printDistribuicaoFaixaEtaria() {
        // Vetores de contagem bruta e de porcentagem
        double[] contagem = new double[5];
        double[] porcentagem = new double[5];

        // Total de eleitos já calculado
        double totalEleitos = deputados.getNumeroCandidatosEleitos();

        // Conta quantos eleitos em cada faixa
        for (Candidato c : deputados.getCandidatos().values()) {
            if (!c.isEleito()) continue;

            int idade = Period.between(
                c.getDtNascimento(), 
                deputados.getDataEleicao()
            ).getYears();

            if      (idade < 30) contagem[0]++;
            else if (idade < 40) contagem[1]++;
            else if (idade < 50) contagem[2]++;
            else if (idade < 60) contagem[3]++;
            else                 contagem[4]++;
        }

        // Converte para porcentagem
        for (int i = 0; i < contagem.length; i++) {
            porcentagem[i] = (contagem[i] / totalEleitos) * 100.0;
        }

        // Formatação pt-BR com duas casas decimais
        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        // Impressão
        System.out.println("\nEleitos, por faixa etária (na data da eleição):");
        System.out.printf("      Idade < 30: %3d (%s%%)%n", (int)contagem[0], nf.format(porcentagem[0]));
        System.out.printf("30 <= Idade < 40: %3d (%s%%)%n", (int)contagem[1], nf.format(porcentagem[1]));
        System.out.printf("40 <= Idade < 50: %3d (%s%%)%n", (int)contagem[2], nf.format(porcentagem[2]));
        System.out.printf("50 <= Idade < 60: %3d (%s%%)%n", (int)contagem[3], nf.format(porcentagem[3]));
        System.out.printf("60 <= Idade     : %3d (%s%%)%n", (int)contagem[4], nf.format(porcentagem[4]));
    }


    public void printDistribuicaoGenero() {
        int totalEleitos = deputados.getNumeroCandidatosEleitos();
        int totalMulheres = 0;
        int totalHomens = 0;

        // Conta quantos eleitos são homens e quantos são mulheres
        for (Candidato c : deputados.getCandidatos().values()) {
            if (c.isEleito()) {
                if (c.getCdGenero() == 4) totalMulheres++;
                else if (c.getCdGenero() == 2) totalHomens++;
            }
        }

        // Calcula percentuais
        double percMulheres = 100.0 * totalMulheres / totalEleitos;
        double percHomens   = 100.0 * totalHomens   / totalEleitos;

        // Formatação pt-BR com duas casas decimais
        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        // Impressão
        System.out.println("\nEleitos, por gênero:");
        System.out.printf( "Feminino:  %d (%s%%)%n", totalMulheres, nf.format(percMulheres));
        System.out.printf("Masculino: %d (%s%%)%n", totalHomens, nf.format(percHomens));
    }

    public void printTotalVotos() {
        int totalVotosNominais = 0;
        int totalVotosLegenda  = 0;
        int totalVotosValidos;
        // Soma votos de legenda por partido
        for (Partido p : deputados.getPartidos()) {
            totalVotosLegenda += p.getVotosLegenda();
        }
        // Soma votos nominais por candidato
        for (Candidato c : deputados.getCandidatos().values()) {
            totalVotosNominais += c.getQtdVotos();
        }
        // Calcula total válido e percentuais
        totalVotosValidos = totalVotosNominais + totalVotosLegenda;
        double percNominal = 100.0 * totalVotosNominais / totalVotosValidos;
        double percLegenda = 100.0 * totalVotosLegenda  / totalVotosValidos;

        // Formatadores pt-BR
        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        NumberFormat nfPercent = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setGroupingUsed(true);
        nfPercent.setMinimumFractionDigits(2);
        nfPercent.setMaximumFractionDigits(2);

        // Impressão
        System.out.println("\nTotal de votos válidos:    " + nf.format(totalVotosValidos));
        System.out.println("Total de votos nominais:   " + nf.format(totalVotosNominais)
            + " (" + nfPercent.format(percNominal) + "%)");
        System.out.println("Total de votos de legenda: " + nf.format(totalVotosLegenda)
            + " (" + nfPercent.format(percLegenda) + "%)");
    }

    private int getIdade(Candidato c) {
        return Period.between(c.getDtNascimento(), deputados.getDataEleicao()).getYears();
    }
}
