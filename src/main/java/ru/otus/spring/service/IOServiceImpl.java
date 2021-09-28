package ru.otus.spring.service;

import javax.security.auth.kerberos.KerberosTicket;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class IOServiceImpl implements IOService {
    private final BufferedReader reader;
    private final PrintStream printStream;
    private final PrintStream errorStream;

    public IOServiceImpl() {
        printStream = System.out;
        errorStream = System.err;
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public BufferedReader getReader() {
        return reader;
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
        printStream.println(value);
    }

    @Override
    public void printError(String error) {
        errorStream.println(error);
    }
}
