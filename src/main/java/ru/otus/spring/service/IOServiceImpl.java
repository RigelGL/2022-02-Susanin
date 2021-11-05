package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Locale;


@Service
public class IOServiceImpl implements IOService {

    private final BufferedReader reader;
    private final PrintStream outputStream;
    private final PrintStream errorStream;

    private Locale locale;

    public IOServiceImpl(InputStream inputStream, PrintStream outputStream, PrintStream errorStream, @Value("${user.region}") Locale locale) {
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        this.locale = locale;
    }

    @Override
    public String readNotBlankLine() {
        String result = null;

        while(result == null || result.isBlank()) {
            try {
                result = reader.readLine();
            } catch(Exception ignored) {
            }
        }

        return result;
    }

    @Override
    public void println(String value) {
        outputStream.println(value);
    }

    @Override
    public void printFormatted(String string, Object... args) {
        outputStream.println(String.format(locale, string, args));
    }

    @Override
    public void printError(String error) {
        errorStream.println(error);
    }
}
