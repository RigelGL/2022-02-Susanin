package ru.otus.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.service.PersonService;
import ru.otus.spring.service.QuestionService;
import ru.otus.spring.service.QuizService;

import java.util.Locale;


@Configuration
@ComponentScan(basePackages = "ru.otus")
@PropertySource("classpath:application.properties")
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        QuizService quizService = context.getBean(QuizService.class);

        quizService.execute();
    }
}
