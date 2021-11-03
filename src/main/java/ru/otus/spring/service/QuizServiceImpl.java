package ru.otus.spring.service;

import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;

import java.util.List;

public class QuizServiceImpl implements QuizService {

    private final IOService ioService;

    public QuizServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void execute(PersonService personService, QuestionService questionService) {
        ioService.println("Enter your name:");

        String name = ioService.readNotBlankLine();

        Person user = personService.getByName(name);

        ioService.println("Hello, " + user.getName() + ", complete the following phrases with one of the suggested options:");

        List<Question> questions = questionService.getAllQuestion();

        if(questions == null) {
            ioService.printError("There are no questions");
            return;
        }

        for(Question q : questions) {
            ioService.println(q.getQuestion() + "    " + String.join(", ", q.getShuffledAnswers()));
        }


    }
}
