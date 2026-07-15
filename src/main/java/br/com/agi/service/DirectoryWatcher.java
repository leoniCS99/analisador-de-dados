package br.com.agi.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class DirectoryWatcher {
    private final ExecutorService executor;
    private volatile boolean running = true;
    private final Path inputDirectory;

    public DirectoryWatcher() {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.inputDirectory = Paths.get(
                System.getProperty("user.home"),
                "data", "in"
        );
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
    }

    public void start() {

        try {

            Files.createDirectories(inputDirectory);

            WatchService watchService = FileSystems.getDefault().newWatchService();

            inputDirectory.register(watchService, ENTRY_CREATE);

            System.out.println("Monitorando pasta: " + inputDirectory);

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

    private void processEvent(WatchEvent<?> event) {

        Path fileName = (Path) event.context();

        if (!fileName.toString().endsWith(".dat")) {
            return;
        }

        Path file = inputDirectory.resolve(fileName);

        System.out.println("Novo arquivo encontrado: " + file.getFileName());

        executor.submit(() -> processFile(file));
    }
    private void processFile(Path file) {

        try {

            new FileProcessor().process(file);

        } catch (Exception e) {

            System.err.println("Erro ao processar arquivo: " + file.getFileName());
            e.printStackTrace();

        }
    }
    private void shutdown(){
        running = false;
        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)){
                executor.shutdownNow();
            }
        }catch (InterruptedException e){
            executor.shutdown();
            Thread.currentThread().interrupt();
        }
    }

}