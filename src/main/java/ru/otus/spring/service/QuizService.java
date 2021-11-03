package ru.otus.spring.service;


public interface QuizService {

    void execute(PersonService personService, QuestionService questionService);
}
