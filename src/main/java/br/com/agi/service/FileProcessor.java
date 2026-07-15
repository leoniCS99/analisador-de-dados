package br.com.agi.service;

import br.com.agi.enums.RecordType;
import br.com.agi.model.Customer;
import br.com.agi.model.Report;
import br.com.agi.model.Sale;
import br.com.agi.model.SaleItem;
import br.com.agi.model.Salesman;
import br.com.agi.util.FileWriterUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
public class FileProcessor {
    private final ReportService reportService = new ReportService();

    public void process(Path file) {

        System.out.println(
                "Processando arquivo: " + file.getFileName()
        );

        List<Customer> customers = new ArrayList<>();
        List<Salesman> salesmen = new ArrayList<>();
        List<Sale> sales = new ArrayList<>();

        readFile(file, customers, salesmen, sales);

        generateReport(file, customers, salesmen, sales);

    }

    private void readFile(Path file, List<Customer> customers, List<Salesman> salesmen, List<Sale> sales) {

        try (BufferedReader reader = Files.newBufferedReader(file)) {

            String line;

            while ((line = reader.readLine()) != null) {

                processLineSafely(line, customers, salesmen, sales);

            }

        } catch (IOException e) {

            System.err.println("Erro ao processar arquivo: " + e.getMessage());

        }

    }

    private void processLineSafely(String line, List<Customer> customers, List<Salesman> salesmen, List<Sale> sales) {

        try {

            processLine(line, customers, salesmen, sales);

        } catch (Exception e) {

            System.err.printf("Linha inválida ignorada: %s%n",
                    line
            );

        }

    }

    private void processLine(String line, List<Customer> customers, List<Salesman> salesmen, List<Sale> sales) {

        String[] fields = line.split("ç");

        switch (RecordType.fromCode(fields[0])) {

            case SALESMAN:
                salesmen.add(new Salesman(
                        fields[1],
                        fields[2],
                        new BigDecimal(fields[3])
                ));
                break;

            case CUSTOMER:
                customers.add(new Customer(
                        fields[1],
                        fields[2],
                        fields[3]
                ));
                break;

            case SALE:
                sales.add(processSale(fields));
                break;
        }

    }

    private Sale processSale(String[] fields) {

        String saleId = fields[1];
        String salesmanName = fields[3];

        String itemsField = fields[2]
                .replace("[", "")
                .replace("]", "");

        List<SaleItem> items = new ArrayList<>();

        String[] saleItems = itemsField.split(",");

        for (String item : saleItems) {

            String[] values = item.split("-");

            items.add(new SaleItem(
                    Integer.parseInt(values[0]),
                    Integer.parseInt(values[1]),
                    new BigDecimal(values[2])
            ));

        }

        return new Sale(
                saleId,
                items,
                salesmanName
        );

    }

    private void generateReport(Path inputFile, List<Customer> customers, List<Salesman> salesmen, List<Sale> sales) {

        Report report = reportService.generate(
                customers,
                salesmen,
                sales
        );

        writeOutputFile(inputFile, report);

    }

    private void writeOutputFile(
            Path inputFile,
            Report report) {

        try {

            Path outputDirectory = inputFile
                    .getParent()
                    .getParent()
                    .resolve("out");

            Files.createDirectories(outputDirectory);

            String fileName = inputFile.getFileName().toString();

            fileName = fileName.replaceFirst("\\.dat$", "");

            Path outputFile = outputDirectory.resolve(
                    fileName + ".done.dat"
            );

            FileWriterUtil.write(
                    outputFile,
                    report
            );

            System.out.println("Arquivo gerado: " + outputFile.getFileName());

        } catch (IOException e) {

            System.err.println("Erro ao gerar relatório: " + e.getMessage());

        }

    }

}