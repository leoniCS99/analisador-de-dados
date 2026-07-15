package br.com.agi.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class DirectoryWatcher {
    private final ExecutorService executor;
    private final Path inputDirectory;
    private final FileProcessor fileProcessor;

    private volatile boolean running = true;

    public DirectoryWatcher() {
        this(
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                ),
                Paths.get(
                        System.getProperty("user.home"),
                        "data",
                        "in"
                ),
                new FileProcessor()
        );

        Runtime.getRuntime().addShutdownHook(
                new Thread(this::shutdown)
        );
    }

    DirectoryWatcher(
            ExecutorService executor,
            Path inputDirectory,
            FileProcessor fileProcessor) {

        this.executor = executor;
        this.inputDirectory = inputDirectory;
        this.fileProcessor = fileProcessor;
    }

    public void start() {

        try {

            Files.createDirectories(inputDirectory);

            WatchService watchService =
                    FileSystems.getDefault().newWatchService();

            inputDirectory.register(
                    watchService,
                    ENTRY_CREATE
            );

            System.out.println(
                    "Monitorando pasta: " + inputDirectory
            );

            monitorDirectory(watchService);

        } catch (IOException e) {

            throw new RuntimeException("Erro ao monitorar diretório.", e);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            System.err.println("Monitoramento interrompido.");
        }
    }

    private void monitorDirectory(WatchService watchService) throws InterruptedException {

        while (running) {

            WatchKey key = watchService.take();

            for (WatchEvent<?> event : key.pollEvents()) {

                processEvent(event);

            }

            if (!key.reset()) {
                break;
            }
        }
    }

    void processEvent(WatchEvent<?> event) {

        Path fileName = (Path) event.context();

        if (!fileName.toString().endsWith(".dat")) {
            return;
        }

        Path file = inputDirectory.resolve(fileName);

        System.out.println("Novo arquivo encontrado: " + file.getFileName());

        executor.submit(() -> processFile(file));
    }

    void processFile(Path file) {

        try {

            fileProcessor.process(file);

        } catch (Exception e) {

            System.err.println("Erro ao processar arquivo: " + file.getFileName());
            e.printStackTrace();

        }
    }

    void shutdown() {

        running = false;
        executor.shutdown();

        try {

            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {

                executor.shutdownNow();

            }

        } catch (InterruptedException e) {

            executor.shutdownNow();

            Thread.currentThread().interrupt();
        }
    }

}