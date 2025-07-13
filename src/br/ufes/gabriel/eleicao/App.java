package br.ufes.gabriel.eleicao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.ufes.gabriel.eleicao.service.Eleicao;
import br.ufes.gabriel.eleicao.service.Relatorio;
import br.ufes.gabriel.eleicao.util.Leitor;

public class App {
    public static void main(String[] args) throws Exception {
        // validacao de argumentos
        if (args.length != 4) {
            System.out.println("Uso: java -jar vereadores.jar <codigo_municipio> <arquivo_candidatos> <arquivo_votacao> <data_eleicao>");
            System.exit(1);
        }

        // leitura dos parametros
        String codigoMunicipio = args[0];
        String caminhoArquivoCandidatos = args[1];
        String caminhoArquivoVotos = args[2];

        // parse da data no formato dd/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataEleicao = LocalDate.parse(args[3], formatter);
        dataEleicao.format(formatter);

        try {
            Eleicao eleicao = new Eleicao(codigoMunicipio, dataEleicao);
            Leitor leitor = new Leitor(caminhoArquivoCandidatos);
            leitor.leituraCandidatos(eleicao);
            leitor.leituraVotos(eleicao, caminhoArquivoVotos);
            Relatorio rel = new Relatorio(eleicao);
            rel.gerarRelatorios();
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.exit(1);
        }

    }
}
