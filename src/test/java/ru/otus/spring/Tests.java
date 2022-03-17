package ru.otus.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.otus.spring.config.QuizConfig;
import ru.otus.spring.dao.PersonDao;
import ru.otus.spring.dao.PersonDaoSimple;
import ru.otus.spring.dao.QuestionDaoCSV;
import ru.otus.spring.dao.QuestionsReadingException;
import ru.otus.spring.domain.Person;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = Main.class)

public class Tests {

    @Test
    void questionDaoExceptionIfNoResource()  {
        QuizConfig quizConfig = new QuizConfig();
        quizConfig.setQuestions(new ClassPathResource("__invalid__for_test.csv"));

        QuestionDaoCSV questionDaoCSV = new QuestionDaoCSV(quizConfig);
        assertThrows(QuestionsReadingException.class, questionDaoCSV::getAllQuestions);
    }

    @Test
    void questionDaoExceptionIfInvalidFile()  {
        QuizConfig quizConfig = new QuizConfig();
        quizConfig.setQuestions(new ClassPathResource("questions-invalid.csv"));

        QuestionDaoCSV questionDaoCSV = new QuestionDaoCSV(quizConfig);
        assertThrows(QuestionsReadingException.class, questionDaoCSV::getAllQuestions);
    }

    @Test
    void personDaoSimpleFindByName() {
        PersonDao personDao = new PersonDaoSimple();
        Person person = personDao.findByName("name");
        assertThat(person).isNotNull();
    }
}
