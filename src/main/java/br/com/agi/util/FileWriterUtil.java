package br.com.agi.util;

import br.com.agi.model.Report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileWriterUtil {
    private FileWriterUtil() {
    }

    public static void write(Path outputFile, Report report) throws IOException {

        List<String> lines = List.of(
                "Quantidade de clientes: " + report.getCustomerCount(),
                "Quantidade de vendedores: " + report.getSalesmanCount(),
                "ID da venda mais cara: " + report.getMostExpensiveSaleId(),
                "Pior vendedor: " + report.getWorstSalesman()
        );

        Files.write(outputFile, lines);
    }

}
