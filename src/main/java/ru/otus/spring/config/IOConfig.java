package ru.otus.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.PrintStream;


@Configuration
public class IOConfig {

    @Bean
    InputStream inputStream() {
        return System.in;
    }

    @Bean
    PrintStream outputStream() {
        return System.out;
    }

    @Bean
    PrintStream errorStream() {
        return System.err;
    }

}
