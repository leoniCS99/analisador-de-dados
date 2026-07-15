package br.com.agi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class DirectoryWatcherTest {
    @Test
    @DisplayName("Should ignore files that are not dat")
    void t1() {

        ExecutorService executor = mock(ExecutorService.class);
        FileProcessor processor = mock(FileProcessor.class);

        DirectoryWatcher watcher = new DirectoryWatcher(
                executor,
                Path.of("input"),
                processor
        );

        @SuppressWarnings("unchecked")
        WatchEvent<Path> event = mock(WatchEvent.class);

        when(event.context()).thenReturn(Path.of("teste.txt"));

        watcher.processEvent(event);

        verifyNoInteractions(executor);
        verifyNoInteractions(processor);
    }

    @Test
    @DisplayName("Should submit dat file to executor")
    void t2() {

        ExecutorService executor = mock(ExecutorService.class);
        FileProcessor processor = mock(FileProcessor.class);

        DirectoryWatcher watcher = new DirectoryWatcher(
                executor,
                Path.of("input"),
                processor
        );

        @SuppressWarnings("unchecked")
        WatchEvent<Path> event = mock(WatchEvent.class);

        when(event.context()).thenReturn(Path.of("sales.dat"));

        watcher.processEvent(event);

        verify(executor).submit(any(Runnable.class));
    }

    @Test
    @DisplayName("Should process file successfully")
    void t3() {

        ExecutorService executor = mock(ExecutorService.class);
        FileProcessor processor = mock(FileProcessor.class);

        DirectoryWatcher watcher = new DirectoryWatcher(
                executor,
                Path.of("input"),
                processor
        );

        Path file = Path.of("sales.dat");

        watcher.processFile(file);

        verify(processor).process(file);
    }

    @Test
    @DisplayName("Should not propagate exception when processing file")
    void t4() {

        ExecutorService executor = mock(ExecutorService.class);
        FileProcessor processor = mock(FileProcessor.class);

        doThrow(new RuntimeException("Erro"))
                .when(processor)
                .process(any());

        DirectoryWatcher watcher = new DirectoryWatcher(
                executor,
                Path.of("input"),
                processor
        );

        assertDoesNotThrow(() ->
                watcher.processFile(Path.of("sales.dat"))
        );
    }

    @Test
    @DisplayName("Should shutdown executor")
    void t5() throws InterruptedException {

        ExecutorService executor = mock(ExecutorService.class);

        when(executor.awaitTermination(anyLong(), any()))
                .thenReturn(true);

        DirectoryWatcher watcher = new DirectoryWatcher(
                executor,
                Path.of("input"),
                mock(FileProcessor.class)
        );

        watcher.shutdown();

        verify(executor).shutdown();
        verify(executor).awaitTermination(anyLong(), any());
        verify(executor, never()).shutdownNow();
    }

    @Test
    @DisplayName("Should create default watcher")
    void t6() {

        DirectoryWatcher watcher = new DirectoryWatcher();

        assertNotNull(watcher);
    }

    @Test
    @DisplayName("Should execute submitted runnable")
    void t7() {

        ExecutorService executor = mock(ExecutorService.class);
        FileProcessor processor = mock(FileProcessor.class);

        DirectoryWatcher watcher = new DirectoryWatcher(
                executor,
                Path.of("input"),
                processor
        );

        @SuppressWarnings("unchecked")
        WatchEvent<Path> event = mock(WatchEvent.class);

        when(event.context()).thenReturn(Path.of("sales.dat"));

        watcher.processEvent(event);

        var captor = org.mockito.ArgumentCaptor.forClass(Runnable.class);

        verify(executor).submit(captor.capture());

        captor.getValue().run();

        verify(processor).process(any(Path.class));
    }

    @Test
    @DisplayName("Should force shutdown when executor does not terminate")
    void t8() throws InterruptedException {

        ExecutorService executor = mock(ExecutorService.class);

        when(executor.awaitTermination(anyLong(), any()))
                .thenReturn(false);

        DirectoryWatcher watcher = new DirectoryWatcher(
                executor,
                Path.of("input"),
                mock(FileProcessor.class)
        );

        watcher.shutdown();

        verify(executor).shutdown();
        verify(executor).shutdownNow();
    }

    @Test
    @DisplayName("Should shutdown executor when interrupted")
    void t9() throws InterruptedException {

        ExecutorService executor = mock(ExecutorService.class);

        when(executor.awaitTermination(anyLong(), any()))
                .thenThrow(new InterruptedException());

        DirectoryWatcher watcher = new DirectoryWatcher(
                executor,
                Path.of("input"),
                mock(FileProcessor.class)
        );

        watcher.shutdown();

        verify(executor).shutdown();
        verify(executor).shutdownNow();
    }
}