import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Leitor {

    public void leituraCandidatos(Eleicao deputados,String caminhoArquivoCandidatos, String caminhoArquivoVotos) {
        int cargo;
        String[] colunas;
        Candidato pessoa;
        Partido grupo;

    
        try (BufferedReader br = Files.newBufferedReader(Paths.get(caminhoArquivoCandidatos), StandardCharsets.UTF_8)) {
            String linha = br.readLine();

        } catch (IOException e) {
            System.out.println("Erro de I/O");
            System.exit(1);
        }
    }
}
