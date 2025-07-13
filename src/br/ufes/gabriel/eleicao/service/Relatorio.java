package br.ufes.gabriel.eleicao.service;

import java.text.NumberFormat;
import java.time.Period;
import java.util.*;

import br.ufes.gabriel.eleicao.model.Candidato;
import br.ufes.gabriel.eleicao.model.Partido;

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
        this.partidosOrdenados.sort(Comparator.comparingInt(Partido::getTotalVotos).reversed().thenComparingInt(Partido::getNumero));
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

    /**
     * Imprime o numero de vagas (quantidade de eleitos).
     * 1
     */
    private void printNumeroVagas() {
        System.out.printf("Número de vagas: %d%n%n", deputados.getNumeroCandidatosEleitos());
    }

    /**
     * Imprime candidatos eleitos, top-N nominais, prejudicados e beneficiados.
     * 2, 3, 4 e 5
     */
    public void printCandidatosMaisVotados() {
        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setGroupingUsed(true);

        int numeroVagas = deputados.getNumeroCandidatosEleitos();
        List<Candidato> topN         = new ArrayList<>();
        List<Candidato> prejudicados = new ArrayList<>();
        List<Candidato> beneficiados = new ArrayList<>();

        //Cria ranking completo e reordena:
        List<Candidato> ranking = new ArrayList<>(candidatosOrdenados);
        ranking.sort(
            Comparator.comparingInt(Candidato::getQtdVotos).reversed()
                    // desempate: candidato mais velho primeiro
                    .thenComparing(c ->
                        Period.between(c.getDtNascimento(), deputados.getDataEleicao()).getYears(),
                        Comparator.reverseOrder()
                    )
        );

        //Separa top-N, prejudicados e beneficiados
        int idx = 1;
        for (Candidato c : ranking) {
            if (idx <= numeroVagas) {
                topN.add(c);
                if (!c.isEleito()) {
                    prejudicados.add(c);
                }
            } else if (c.isEleito()) {
                beneficiados.add(c);
            }
            idx++;
        }

        //Vereadores eleitos
        System.out.println("Vereadores eleitos:");
        int count = 1;
        for (Candidato c : ranking) {
            if (!c.isEleito()) continue;
            String prefix = c.getNrFederacao() != -1 ? "*" : "";
            System.out.printf(
                "%2d - %s%s (%s, %s votos)%n",
                count++,
                prefix,
                c.getNmUrnaCandidato(),
                c.getSgPartido(),
                nf.format(c.getQtdVotos())
            );
        }
        System.out.println();

        //Top-N mais votados
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        int ord = 1;
        for (Candidato c : topN) {
            String prefix = c.getNrFederacao() != -1 ? "*" : "";
            System.out.printf(
                "%2d - %s%s (%s, %s votos)%n",
                ord++,
                prefix,
                c.getNmUrnaCandidato(),
                c.getSgPartido(),
                nf.format(c.getQtdVotos())
            );
        }
        System.out.println();

        //Prejudicados
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        System.out.println("(com sua posição no ranking de mais votados)");
        for (Candidato c : prejudicados) {
            int pos = ranking.indexOf(c) + 1;
            String prefix = c.getNrFederacao() != -1 ? "*" : "";
            System.out.printf(
                "%2d - %s%s (%s, %s votos)%n",
                pos,
                prefix,
                c.getNmUrnaCandidato(),
                c.getSgPartido(),
                nf.format(c.getQtdVotos())
            );
        }

        //Beneficiados
        System.out.println("\nEleitos, que se beneficiaram do sistema proporcional:");
        System.out.println("(com sua posição no ranking de mais votados)");
        for (Candidato c : beneficiados) {
            int pos = ranking.indexOf(c) + 1;
            String prefix = c.getNrFederacao() != -1 ? "*" : "";
            System.out.printf(
                "%2d - %s%s (%s, %s votos)%n",
                pos,
                prefix,
                c.getNmUrnaCandidato(),
                c.getSgPartido(),
                nf.format(c.getQtdVotos())
            );
        }
        System.out.println();
    }

    /**
    * Imprime a votação dos partidos e quantos candidatos cada um teve eleitos.
    */
    public void printVotacaoPorPartido() {
        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setGroupingUsed(true);

        System.out.println("Votação dos partidos e número de candidatos eleitos:");
        int contador = 1;

        for (Partido p : partidosOrdenados) {
            int nominais = p.getVotosNominais();
            int legenda  = p.getVotosLegenda();
            int total    = nominais + legenda;

            // conta eleitos
            int eleitos = 0;
            for (Candidato c : p.getCandidatos()) {
                if (c.isEleito()) eleitos++;
            }

            // escolhe termo para total de votos
            String termoTotal;
            if (total <= 1) {
                termoTotal = "voto";
            } else {
                termoTotal = "votos";
            }

            // escolhe termo para nominais
            String termoNominais;
            if (nominais <= 1) {
                termoNominais = "nominal";
            } else {
                termoNominais = "nominais";
            }

            // escolhe termo para legenda (mantém 'de legenda' invariável, só pluraliza 'voto')
            String termoLegenda;
            if (legenda <= 1) {
                termoLegenda = "de legenda";
            } else {
                termoLegenda = "de legenda";
            }

            // escolhe termo para candidatos eleitos
            String textoEleitos;
            if (eleitos <= 1) {
                textoEleitos = eleitos + " candidato eleito";
            } else {
                textoEleitos = eleitos + " candidatos eleitos";
            }

            // imprime tudo com um único printf
            System.out.printf(
                "%2d - %s - %d, %s %s (%s %s e %s %s), %s%n",
                contador,
                p.getSigla(),
                p.getNumero(),
                nf.format(total), 
                termoTotal,
                nf.format(nominais),termoNominais,
                nf.format(legenda), termoLegenda,
                textoEleitos
            );
            contador++;
        }
        System.out.println();
    }

    /**
     * Imprime o primeiro e ultimo colocados de cada partido.
     */
    public void printPrimeiroUltimoPorPartido() {
        // 1) Ordena cada lista interna de candidatos no partido
        for (Partido p : partidosOrdenados) {
            p.getCandidatos().sort(
                Comparator.comparingInt(Candidato::getQtdVotos).reversed()
                        .thenComparing(c ->
                            Period.between(c.getDtNascimento(), deputados.getDataEleicao()).getYears(),
                            Comparator.reverseOrder()
                        )
            );
        }

        // 2) Coleta o “primeiro” de cada partido que tenha ao menos 2 candidatos
        List<Candidato> primeiros = new ArrayList<>();
        for (Partido p : partidosOrdenados) {
            List<Candidato> lista = p.getCandidatos();
            if (lista.size() >= 2) {
                primeiros.add(lista.get(0));
            }
        }

        // 3) Ordena esses primeiros pelo número de votos, desempate por partido e idade
        primeiros.sort(
            Comparator.comparingInt(Candidato::getQtdVotos).reversed()
                    .thenComparingInt(Candidato::getNrPartido)
                    .thenComparing(c ->
                        Period.between(c.getDtNascimento(), deputados.getDataEleicao()).getYears(),
                        Comparator.reverseOrder()
                    )
        );

        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setGroupingUsed(true);

        System.out.println("\nPrimeiro e último colocados de cada partido:");
        int contador = 1;
        for (Candidato primeiro : primeiros) {
            Partido partido = deputados.getPartidoPorNumero(primeiro.getNrPartido());
            List<Candidato> lista = partido.getCandidatos();
            Candidato ultimo = lista.get(lista.size() - 1);

            // singular/plural para o primeiro
            int vp = primeiro.getQtdVotos();
            String lblP = (vp <= 1 ? "voto" : "votos");

            // singular/plural para o último
            int vu = ultimo.getQtdVotos();
            String lblU = (vu <= 1 ? "voto" : "votos");

            // impressão única
            System.out.printf(
                "%2d - %s - %d, %s (%d, %s %s) / %s (%d, %s %s)%n",
                contador++,
                primeiro.getSgPartido(),
                primeiro.getNrPartido(),
                primeiro.getNmUrnaCandidato(),
                primeiro.getNrCandidato(),
                nf.format(vp),
                lblP,
                ultimo.getNmUrnaCandidato(),
                ultimo.getNrCandidato(),
                nf.format(vu),
                lblU
            );
        }
    }


    /**
     * Imprime distribuicao de eleitos por faixa etaria.
     */
    public void printDistribuicaoFaixaEtaria() {
        //Vetores de contagem bruta e de porcentagem
        double[] contagem = new double[5];
        double[] porcentagem = new double[5];

        //Total de eleitos já calculado
        double totalEleitos = deputados.getNumeroCandidatosEleitos();

        //Conta quantos eleitos em cada faixa
        for (Candidato c : deputados.getCandidatos().values()) {
            if (!c.isEleito()) continue;

            int idade = Period.between(
                c.getDtNascimento(), 
                deputados.getDataEleicao()
            ).getYears();

            if (idade < 30) contagem[0]++;
            else if (idade < 40) contagem[1]++;
            else if (idade < 50) contagem[2]++;
            else if (idade < 60) contagem[3]++;
            else contagem[4]++;
        }

        for (int i = 0; i < contagem.length; i++) {
            porcentagem[i] = (contagem[i] / totalEleitos) * 100.0;
        }

        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        System.out.println("\nEleitos, por faixa etária (na data da eleição):");
        System.out.printf("      Idade < 30: %3d (%s%%)%n", (int)contagem[0], nf.format(porcentagem[0]));
        System.out.printf("30 <= Idade < 40: %3d (%s%%)%n", (int)contagem[1], nf.format(porcentagem[1]));
        System.out.printf("40 <= Idade < 50: %3d (%s%%)%n", (int)contagem[2], nf.format(porcentagem[2]));
        System.out.printf("50 <= Idade < 60: %3d (%s%%)%n", (int)contagem[3], nf.format(porcentagem[3]));
        System.out.printf("60 <= Idade     : %3d (%s%%)%n", (int)contagem[4], nf.format(porcentagem[4]));
    }

    /**
     * Imprime distribuicao de eleitos por genero.
     */
    public void printDistribuicaoGenero() {
        int totalEleitos = deputados.getNumeroCandidatosEleitos();
        int totalMulheres = 0;
        int totalHomens = 0;

        for (Candidato c : deputados.getCandidatos().values()) {
            if (c.isEleito()) {
                if (c.getCdGenero() == 4) totalMulheres++;
                else if (c.getCdGenero() == 2) totalHomens++;
            }
        }

        double percMulheres = 100.0 * totalMulheres / totalEleitos;
        double percHomens   = 100.0 * totalHomens   / totalEleitos;

        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        System.out.println("\nEleitos, por gênero:");
        System.out.printf( "Feminino:  %d (%s%%)%n", totalMulheres, nf.format(percMulheres));
        System.out.printf("Masculino: %d (%s%%)%n", totalHomens, nf.format(percHomens));
    }

    /**
     * Imprime total de votos validos, nominais e de legenda.
     */
    public void printTotalVotos() {
        int totalVotosNominais = 0;
        int totalVotosLegenda  = 0;
        int totalVotosValidos;
        
        for (Partido p : deputados.getPartidos()) {
            totalVotosLegenda += p.getVotosLegenda();
        }
        
        for (Candidato c : deputados.getCandidatos().values()) {
            totalVotosNominais += c.getQtdVotos();
        }
        
        totalVotosValidos = totalVotosNominais + totalVotosLegenda;
        double percNominal = 100.0 * totalVotosNominais / totalVotosValidos;
        double percLegenda = 100.0 * totalVotosLegenda  / totalVotosValidos;

        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        NumberFormat nfPercent = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
        nf.setGroupingUsed(true);
        nfPercent.setMinimumFractionDigits(2);
        nfPercent.setMaximumFractionDigits(2);

        System.out.println("\nTotal de votos válidos:    " + nf.format(totalVotosValidos));
        System.out.println("Total de votos nominais:   " + nf.format(totalVotosNominais)
            + " (" + nfPercent.format(percNominal) + "%)");
        System.out.println("Total de votos de legenda: " + nf.format(totalVotosLegenda)
            + " (" + nfPercent.format(percLegenda) + "%)");
    }
}
