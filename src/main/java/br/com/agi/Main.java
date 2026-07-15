package br.com.agi;

import br.com.agi.service.DirectoryWatcher;

public class Main {
    public static void main(String[] args){
        System.out.println("Analisador de Dados iniciado.");

        new DirectoryWatcher().start();
    }
}