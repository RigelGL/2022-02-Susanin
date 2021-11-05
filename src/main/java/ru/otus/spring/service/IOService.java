package ru.otus.spring.service;

public interface IOService {

    String readNotBlankLine();

    void println(String value);

    void printFormatted(String string, Object... args);

    void printError(String error);
}
