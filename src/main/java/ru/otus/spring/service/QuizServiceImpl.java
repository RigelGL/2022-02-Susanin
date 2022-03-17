package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.config.QuizConfig;
import ru.otus.spring.dao.QuestionsReadingException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class QuizServiceImpl implements QuizService {
    private final IOService ioService;
    private final PersonService personService;
    private final QuestionService questionService;

    private final int minimumForTest;

    public QuizServiceImpl(
            IOService ioService,
            PersonService personService,
            QuestionService questionService,
            QuizConfig quizConfig
    ) {
        this.ioService = ioService;
        this.personService = personService;
        this.questionService = questionService;
        this.minimumForTest = quizConfig.getMinimumForTest();
    }


    private Person getPerson() {
        ioService.printLocalised("message.enter-your-name");

        String name = ioService.readNotBlankLine();

        return personService.getByName(name);
    }


    private List<Question> getShuffledQuestions() {
        try {
            List<Question> result = questionService.getAllQuestion();
            List<Question> shuffledQuestions = new ArrayList<>(result);
            Collections.shuffle(shuffledQuestions);
            return result;
        } catch(QuestionsReadingException e) {

            if(e.getType() == QuestionsReadingException.QuestionsReadingExceptionType.FileNotFound) {
                ioService.printLocalisedError("error.questions-file-not-found");
                ioService.printError(e.toString());
            }
            else {
                ioService.printError(e.getOriginal().toString());
            }
        }
        return new ArrayList<>();
    }


    private boolean askQuestion(Question question) {
        String shuffledAnswers = String.join(", ", question.getShuffledAnswers());
        ioService.printFormatted("%s %s", question.getQuestion(), shuffledAnswers);

        boolean oneOfAnswer;
        String answer;

        do {
            answer = ioService.readNotBlankLine().toLowerCase();
            String finalAnswer = answer;
            oneOfAnswer = question.getAnswers().stream().anyMatch(s -> s.toLowerCase().equals(finalAnswer));

            if(!oneOfAnswer) {
                ioService.printLocalised("message.incorrect-answer", shuffledAnswers);
            }

        } while(!oneOfAnswer);

        return answer.equals(question.getRightAnswer());
    }

    private int testUser(Person user, List<Question> questions) {
        ioService.printLocalised("message.hello", user.getName());

        int rightAnswers = 0;

        for(Question q : questions) {
            boolean isRightAnswer = askQuestion(q);

            if(isRightAnswer) {
                rightAnswers += 1;
            }
        }

        return rightAnswers;
    }


    private void printTestResult(Person user, int rightAnswers, int questionsCount) {
        ioService.printLocalised("message.result", user.getName(), rightAnswers, questionsCount);

        if(rightAnswers >= minimumForTest) {
            if(rightAnswers == questionsCount) {
                ioService.printLocalised("message.best-result");
            }
            else {
                ioService.printLocalised("message.success-result");
            }
        }
        else {
            ioService.printLocalised("message.failed-result", minimumForTest);
        }
    }


    @Override
    public void execute() {
        List<Question> questions = getShuffledQuestions();

        if(questions.size() == 0) {
            ioService.printLocalisedError("error.question-list-is-empty");
            return;
        }

        Person user = getPerson();

        int rightAnswers = testUser(user, questions);

        printTestResult(user, rightAnswers, questions.size());
    }
}
