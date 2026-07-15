package br.com.agi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DirectoryWatcherTest {
    @Test
    @DisplayName("Should create executor service with available processors")
    void t1() {
        DirectoryWatcher watcher = new DirectoryWatcher();
        assertNotNull(watcher);
    }
}