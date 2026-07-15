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

}