package br.ufes.Gabriel.eleicao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Leitor {
    public static List<String[]> parse(Path path, Charset charset, String delimiter) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path, charset)) {
            String line;
            br.readLine(); // pula cabeçalho
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(delimiter);
                for (int i = 0; i < cols.length; i++) {
                    cols[i] = cols[i].replaceAll("^\"|\"$", "");
                }
                records.add(cols);
            }
        }
        return records;
    }
}