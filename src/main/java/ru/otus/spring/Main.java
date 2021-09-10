package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.PersonService;
import ru.otus.spring.service.QuestionService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        PersonService personService = context.getBean(PersonService.class);
        QuestionService questionService = context.getBean(QuestionService.class);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Your name:");
        String name = null;

        while(name == null || name.isBlank()) {
            try {
                name = reader.readLine();
            } catch(Exception ignored) {
            }
        }

        Person user = personService.getByName(name);
        System.out.println("Hello, " + user.getName() + ", complete the following phrases with one of the suggested options:");

        List<Question> questions = questionService.getAllQuestion();

        for(Question q : questions) {
            System.out.println(q.getAsk() + "    " + String.join(", ", q.getShuffledAnswers()));
        }

    }
}
