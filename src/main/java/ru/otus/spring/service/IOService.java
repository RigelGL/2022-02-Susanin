package ru.otus.spring.service;

import java.io.BufferedReader;


public interface IOService {
    String readNotBlankLine();

    void println(String value);

    void printFormatted(String string, Object ... args);

    void printError(String error);
}
