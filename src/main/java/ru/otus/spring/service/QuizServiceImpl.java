package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class QuizServiceImpl implements QuizService {

    private final IOService ioService;
    private final PersonService personService;
    private final QuestionService questionService;

    private int minimumForTest;


    public QuizServiceImpl(IOService ioService, PersonService personService, QuestionService questionService,
                           @Value("${questions.minimum-for-test}") int minimumForTest
    ) {
        this.ioService = ioService;
        this.personService = personService;
        this.questionService = questionService;
        this.minimumForTest = minimumForTest;
    }


    private Person getUser() {
        ioService.println("Enter your name:");

        String name = ioService.readNotBlankLine();

        return personService.getByName(name);
    }


    private List<Question> getShuffledQuestions() {
        try {
            List<Question> result = questionService.getAllQuestion();
            List<Question> shuffledQuestions = new ArrayList<>(result);
            Collections.shuffle(shuffledQuestions);
            return result;
        } catch(Exception e) {

            if(e instanceof FileNotFoundException) {
                ioService.printError("Questions file not found");
                ioService.printError(e.toString());
            }
            else {
                ioService.printError(e.toString());
            }
        }
        return new ArrayList<>();
    }


    private int testUser(Person user, List<Question> questions) {
        ioService.printFormatted("Hello, %s, complete the following phrases with one of the suggested options:", user.getName());

        int rightAnswers = 0;

        for(Question q : questions) {
            String shuffledAnswers = String.join(", ", q.getShuffledAnswers());
            ioService.printFormatted("%s %s", q.getQuestion(), shuffledAnswers);

            boolean oneOfAnswer;
            String answer;

            do {
                answer = ioService.readNotBlankLine().toLowerCase();
                String finalAnswer = answer;
                oneOfAnswer = q.getAnswers().stream().anyMatch(s -> s.toLowerCase().equals(finalAnswer));

                if(!oneOfAnswer) {
                    ioService.printFormatted("Incorrect variant. Type one of %s", shuffledAnswers);
                }

            } while(!oneOfAnswer);

            if(answer.equals(q.getRightAnswer())) {
                rightAnswers += 1;
            }
        }

        return rightAnswers;
    }


    private void printTestResult(Person user, int rightAnswers, int questionsCount) {
        ioService.printFormatted("%s, your result is: %d correct answers out of %d", user.getName(), rightAnswers, questionsCount);

        if(rightAnswers >= minimumForTest) {
            if(rightAnswers == questionsCount) {
                ioService.println("Very well! You passed the test for the highest score!");
            }
            else {
                ioService.println("You pass the test.");
            }
        }
        else {
            ioService.printFormatted("You did not pass the test! Нou need to answer at least %d questions correctly", minimumForTest);
        }
    }


    @Override
    public void execute() {
        List<Question> questions = getShuffledQuestions();

        if(questions.size() == 0) {
            return;
        }

        Person user = getUser();

        int rightAnswers = testUser(user, questions);

        printTestResult(user, rightAnswers, questions.size());
    }
}
