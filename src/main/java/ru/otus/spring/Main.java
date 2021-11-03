package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.service.PersonService;
import ru.otus.spring.service.QuestionService;
import ru.otus.spring.service.QuizService;


public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        PersonService personService = context.getBean(PersonService.class);
        QuestionService questionService = context.getBean(QuestionService.class);

        QuizService quizService = context.getBean(QuizService.class);

        quizService.execute(personService, questionService);
    }
}
