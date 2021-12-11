package ru.otus.spring.dao;

public class QuestionsReadingException extends RuntimeException {

    private final QuestionsReadingExceptionType type;
    private final Exception original;

    public QuestionsReadingException(String s, QuestionsReadingExceptionType type, Exception original) {
        super(s);
        this.type = type;
        this.original = original;
    }

    public QuestionsReadingException(String s, QuestionsReadingExceptionType type) {
        this(s, type, null);
    }

    public QuestionsReadingExceptionType getType() {
        return type;
    }

    public Exception getOriginal() {
        return original;
    }

    public enum QuestionsReadingExceptionType {
        FileNotFound,
        ErrorWhileReading
    }
}
