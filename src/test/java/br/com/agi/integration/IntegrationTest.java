package br.com.agi.integration;

import br.com.agi.service.FileProcessor;
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

class IntegrationTest {
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Should process complete flow from input to output")
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

        String content = Files.readString(generatedFile);
        String[] lines = content.split("\n");

        assertEquals("Quantidade de clientes: 2", lines[0].trim());
        assertEquals("Quantidade de vendedores: 2", lines[1].trim());
        assertEquals("ID da venda mais cara: 10", lines[2].trim());
        assertEquals("Pior vendedor: Paulo", lines[3].trim());
    }

    @Test
    @DisplayName("Should handle file with only sales")
    void t2() throws IOException, URISyntaxException {

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
    @DisplayName("Should handle empty file")
    void t3() throws IOException, URISyntaxException {

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
}
