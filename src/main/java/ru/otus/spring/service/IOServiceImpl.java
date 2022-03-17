package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.LocalisationConfig;

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

    private final Locale locale;
    private final MessageSource msg;

    public IOServiceImpl(@Value("#{T(java.lang.System).in}") InputStream inputStream,
                         @Value("#{T(java.lang.System).out}") PrintStream outputStream,
                         @Value("#{T(java.lang.System).err}") PrintStream errorStream,
                         LocalisationConfig localisationConfig,
                         MessageSource msg) {
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.locale = localisationConfig.getLocale();
        this.msg = msg;
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
        outputStream.printf(locale, (string) + "%n", args);
    }

    @Override
    public void printLocalised(String code, Object... args) {
        println(msg.getMessage(code, args, locale));
    }

    @Override
    public void printError(String error) {
        errorStream.println(error);
    }

    @Override
    public void printLocalisedError(String code, Object... args) {
        printError(msg.getMessage(code, args, locale));
    }
}
