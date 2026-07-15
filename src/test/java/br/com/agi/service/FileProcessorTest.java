package br.com.agi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileProcessorTest {
    @TempDir
    Path tempDir;
    @Test
    @DisplayName("Should process input file and generate output report")
    void t1() throws IOException, URISyntaxException {

        Path inputDirectory = tempDir.resolve("in");
        Path outputDirectory = tempDir.resolve("out");

        Files.createDirectories(inputDirectory);
        Files.createDirectories(outputDirectory);

        Path stubInput = Path.of(
                Objects.requireNonNull(
                        getClass().getResource("/stub/sales.dat")
                ).toURI()
        );

        Path inputFile = inputDirectory.resolve("sales.dat");

        Files.copy(stubInput, inputFile);

        new FileProcessor().process(inputFile);

        Path generatedFile = outputDirectory.resolve("sales.done.dat");

        assertTrue(Files.exists(generatedFile));

        Path expectedFile = Path.of(
                Objects.requireNonNull(
                        getClass().getResource("/stub/sales.done.dat")
                ).toURI()
        );

        String expected = Files.readString(expectedFile);
        String actual = Files.readString(generatedFile);

        assertEquals(expected, actual);

    }
    @Test
    @DisplayName("Should ignore malformed lines and continue processing")
    void t2() throws IOException, URISyntaxException {

        Path inputDirectory = tempDir.resolve("in");
        Path outputDirectory = tempDir.resolve("out");

        Files.createDirectories(inputDirectory);
        Files.createDirectories(outputDirectory);

        Path stubInput = Path.of(
                Objects.requireNonNull(
                        getClass().getResource("/stub/malformed.dat")
                ).toURI()
        );

        Path inputFile = inputDirectory.resolve("malformed.dat");
        Files.copy(stubInput, inputFile);

        new FileProcessor().process(inputFile);

        Path generatedFile = outputDirectory.resolve("malformed.done.dat");
        assertTrue(Files.exists(generatedFile));

        String content = Files.readString(generatedFile);
        String[] lines = content.split("\n");

        assertEquals("Quantidade de clientes: 1", lines[0].trim());
        assertEquals("Quantidade de vendedores: 1", lines[1].trim());
        assertEquals("ID da venda mais cara: 10", lines[2].trim());
        assertEquals("Pior vendedor: Pedro", lines[3].trim());
    }
    @Test
    @DisplayName("Should handle lines with insufficient fields")
    void t3() throws IOException, URISyntaxException {

        Path inputDirectory = tempDir.resolve("in");
        Path outputDirectory = tempDir.resolve("out");

        Files.createDirectories(inputDirectory);
        Files.createDirectories(outputDirectory);

        Path stubInput = Path.of(
                Objects.requireNonNull(
                        getClass().getResource("/stub/insufficient_fields.dat")
                ).toURI()
        );

        Path inputFile = inputDirectory.resolve("insufficient_fields.dat");
        Files.copy(stubInput, inputFile);

        new FileProcessor().process(inputFile);

        Path generatedFile = outputDirectory.resolve("insufficient_fields.done.dat");
        assertTrue(Files.exists(generatedFile));

        String content = Files.readString(generatedFile);
        String[] lines = content.split("\n");

        assertEquals("Quantidade de clientes: 0", lines[0].trim());
        assertEquals("Quantidade de vendedores: 1", lines[1].trim());
        assertEquals("ID da venda mais cara: N/A", lines[2].trim());
        assertEquals("Pior vendedor: N/A", lines[3].trim());
    }
    @Test
    @DisplayName("Should handle invalid number formats")
    void t4() throws IOException, URISyntaxException {

        Path inputDirectory = tempDir.resolve("in");
        Path outputDirectory = tempDir.resolve("out");

        Files.createDirectories(inputDirectory);
        Files.createDirectories(outputDirectory);

        Path stubInput = Path.of(
                Objects.requireNonNull(
                        getClass().getResource("/stub/invalid_salary.dat")
                ).toURI()
        );

        Path inputFile = inputDirectory.resolve("invalid_salary.dat");
        Files.copy(stubInput, inputFile);

        new FileProcessor().process(inputFile);

        Path generatedFile = outputDirectory.resolve("invalid_salary.done.dat");
        assertTrue(Files.exists(generatedFile));

        String content = Files.readString(generatedFile);
        String[] lines = content.split("\n");

        assertEquals("Quantidade de clientes: 0", lines[0].trim());
        assertEquals("Quantidade de vendedores: 1", lines[1].trim());
    }
    @Test
    @DisplayName("Should handle empty file")
    void t5() throws IOException, URISyntaxException {

        Path inputDirectory = tempDir.resolve("in");
        Path outputDirectory = tempDir.resolve("out");

        Files.createDirectories(inputDirectory);
        Files.createDirectories(outputDirectory);

        Path stubInput = Path.of(
                Objects.requireNonNull(
                        getClass().getResource("/stub/empty.dat")
                ).toURI()
        );

        Path inputFile = inputDirectory.resolve("empty.dat");
        Files.copy(stubInput, inputFile);

        new FileProcessor().process(inputFile);

        Path generatedFile = outputDirectory.resolve("empty.done.dat");
        assertTrue(Files.exists(generatedFile));

        String content = Files.readString(generatedFile);
        String[] lines = content.split("\n");

        assertEquals("Quantidade de clientes: 0", lines[0].trim());
        assertEquals("Quantidade de vendedores: 0", lines[1].trim());
        assertEquals("ID da venda mais cara: N/A", lines[2].trim());
        assertEquals("Pior vendedor: N/A", lines[3].trim());
    }
    @Test
    @DisplayName("Should handle only valid sales")
    void t6() throws IOException, URISyntaxException {

        Path inputDirectory = tempDir.resolve("in");
        Path outputDirectory = tempDir.resolve("out");

        Files.createDirectories(inputDirectory);
        Files.createDirectories(outputDirectory);

        Path stubInput = Path.of(
                Objects.requireNonNull(
                        getClass().getResource("/stub/only_sales.dat")
                ).toURI()
        );

        Path inputFile = inputDirectory.resolve("only_sales.dat");
        Files.copy(stubInput, inputFile);

        new FileProcessor().process(inputFile);

        Path generatedFile = outputDirectory.resolve("only_sales.done.dat");
        assertTrue(Files.exists(generatedFile));

        String content = Files.readString(generatedFile);
        String[] lines = content.split("\n");

        assertEquals("Quantidade de clientes: 0", lines[0].trim());
        assertEquals("Quantidade de vendedores: 0", lines[1].trim());
        assertEquals("ID da venda mais cara: 10", lines[2].trim());
        assertEquals("Pior vendedor: Paulo", lines[3].trim());
    }
    @Test
    @DisplayName("Should handle file with multiple sales for same salesman")
    void t7() throws IOException, URISyntaxException {

        Path inputDirectory = tempDir.resolve("in");
        Path outputDirectory = tempDir.resolve("out");

        Files.createDirectories(inputDirectory);
        Files.createDirectories(outputDirectory);

        String testData = """
            001ç1234567891234çPedroç50000
            003ç10ç[1-10-100]çPedro
            003ç11ç[1-5-50]çPedro
            """;

        Path inputFile = inputDirectory.resolve("multiple_sales.dat");
        Files.writeString(inputFile, testData);

        new FileProcessor().process(inputFile);

        Path generatedFile = outputDirectory.resolve("multiple_sales.done.dat");
        assertTrue(Files.exists(generatedFile));

        String content = Files.readString(generatedFile);
        String[] lines = content.split("\n");

        assertEquals("Quantidade de clientes: 0", lines[0].trim());
        assertEquals("Quantidade de vendedores: 1", lines[1].trim());
        assertEquals("ID da venda mais cara: 10", lines[2].trim());
        assertEquals("Pior vendedor: Pedro", lines[3].trim());
    }
    @Test
    @DisplayName("Should handle file with decimal prices")
    void t8() throws IOException {

        Path inputDirectory = tempDir.resolve("in");
        Path outputDirectory = tempDir.resolve("out");

        Files.createDirectories(inputDirectory);
        Files.createDirectories(outputDirectory);

        String testData = """
            001ç1234567891234çPedroç50000.50
            003ç10ç[1-10-100.99]çPedro
            """;

        Path inputFile = inputDirectory.resolve("decimal_prices.dat");
        Files.writeString(inputFile, testData);

        new FileProcessor().process(inputFile);

        Path generatedFile = outputDirectory.resolve("decimal_prices.done.dat");
        assertTrue(Files.exists(generatedFile));

        String content = Files.readString(generatedFile);
        String[] lines = content.split("\n");

        assertEquals("Quantidade de clientes: 0", lines[0].trim());
        assertEquals("Quantidade de vendedores: 1", lines[1].trim());
    }
}