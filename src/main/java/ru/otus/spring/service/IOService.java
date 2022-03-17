package ru.otus.spring.service;

public interface IOService {

    String readNotBlankLine();

    void println(String value);

    void printFormatted(String string, Object... args);

    void printLocalised(String code, Object... args);

    void printError(String error);

    void printLocalisedError(String code, Object... args);
}
