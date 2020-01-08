package br.com.hbsis.ExportImport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class ImportCSV {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportCSV.class);

    public String[][] leitorCSV(MultipartFile importCategoria) {
        String[][] CSV = new String[10000][11];

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importCategoria.getInputStream()))) {

            leitor.readLine();
            String linhaDoArquivo;

            for (int qtdLinha = 0; (linhaDoArquivo = leitor.readLine()) != null; qtdLinha++) {

                String[] colunas = linhaDoArquivo.split(";");

                int contColunm = 0;
                for (String coluna : colunas) {
                    CSV[qtdLinha][contColunm] = coluna;
                    contColunm++;
                }
            }
        } catch (IOException e) {
            LOGGER.debug("error import: ", e);
        }
        return CSV;
    }
}
