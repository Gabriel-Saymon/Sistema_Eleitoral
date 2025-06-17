import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        // impressao para conferencia
        System.out.println("Codigo do municipio: " + codigoMunicipio);
        System.out.println("Arquivo de candidatos: " + caminhoArquivoCandidatos);
        System.out.println("Arquivo de votacaoo: " + caminhoArquivoVotos);
        System.out.println("Data da eleicao: " + dataEleicao.format(formatter));
    }
}
