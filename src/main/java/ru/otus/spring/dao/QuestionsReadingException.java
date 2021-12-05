package ru.otus.spring.dao;

public class QuestionsReadingException extends RuntimeException {

    public enum QuestionsReadingExceptionType {
        FileNotFound,
        ErrorWhileReading
    }

    private final QuestionsReadingExceptionType type;

    public QuestionsReadingException(String s, QuestionsReadingExceptionType type) {
        super(s);
        this.type = type;
    }

    public QuestionsReadingExceptionType getType() {
        return type;
    }
}
