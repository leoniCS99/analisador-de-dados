package br.com.agi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Main class tests")
class MainTest {

    @Test
    @DisplayName("should have main method")
    void t1(){
       Main main = new Main();
       assertNotNull(main);
    }
}