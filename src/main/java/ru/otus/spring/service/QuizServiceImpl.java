package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


@Service
public class QuizServiceImpl implements QuizService {

    private final IOService ioService;

    @Value("${user.region}")
    private Locale locale;

    @Value("${questions.minimum-for-test}")
    private int minimumForTest;

    public QuizServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void execute(PersonService personService, QuestionService questionService) {
        ioService.println("Enter your name:");

        String name = ioService.readNotBlankLine();

        Person user = personService.getByName(name);

        ioService.println(String.format(locale, "Hello, %s, complete the following phrases with one of the suggested options:", user.getName()));

        List<Question> questions = questionService.getAllQuestion();

        if(questions == null) {
            ioService.printError("There are no questions");
            return;
        }

        int rightAnswers = 0;

        List<Question> shuffledQuestions = new ArrayList<>(questions);
        Collections.shuffle(shuffledQuestions);

        for(Question q : shuffledQuestions) {
            String shuffledAnswers = String.join(", ", q.getShuffledAnswers());
            ioService.println(String.format(locale, "%s %s", q.getQuestion(), shuffledAnswers));

            boolean oneOfAnswer;
            String answer;

            do {
                answer = ioService.readNotBlankLine().toLowerCase();
                String finalAnswer = answer;
                oneOfAnswer = q.getAnswers().stream().anyMatch(s -> s.toLowerCase().equals(finalAnswer));

                if(!oneOfAnswer) {
                    ioService.println(String.format(locale, "Incorrect variant. Type one of %s", shuffledAnswers));
                }

            } while(!oneOfAnswer);

            if(answer.equals(q.getRightAnswer())) {
                rightAnswers += 1;
            }
        }

        ioService.println(String.format(locale, "Your result is: %d correct answers out of %d", rightAnswers, questions.size()));

        if(rightAnswers >= minimumForTest) {
            if(rightAnswers == questions.size()) {
                ioService.println("Very well! You passed the test for the highest score!");
            }else {
                ioService.println("You pass the test.");
            }
        } else {
            ioService.println("You did not pass the test!");
        }



    }
}
