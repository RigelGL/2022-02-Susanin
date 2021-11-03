package ru.otus.spring.service;

import java.io.BufferedReader;


public interface IOService {
    BufferedReader getReader();

    String readNotBlankLine();

    void println(String value);

    void printError(String error);
}
