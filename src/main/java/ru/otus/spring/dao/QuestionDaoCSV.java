package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Question;

import java.io.InputStreamReader;
import java.util.List;


@Repository("questionDao")
public class QuestionDaoCSV implements QuestionDao {

    private final Resource resource;


    public QuestionDaoCSV(@Value("${questions.url}") Resource resource) {
        this.resource = resource;
    }


    @Override
    public List<Question> getAllQuestions() throws QuestionsReadingException {
        if(!resource.exists()) {
            throw new QuestionsReadingException("file not found: " + resource.getDescription(),
                    QuestionsReadingException.QuestionsReadingExceptionType.FileNotFound);
        }

        try(InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream())) {
            return new CsvToBeanBuilder<Question>(inputStreamReader).withType(Question.class).build().parse();
        } catch(Exception e) {
            throw new QuestionsReadingException("error while reading questions resource:\n\t" + e.toString(),
                    QuestionsReadingException.QuestionsReadingExceptionType.ErrorWhileReading, e);
        }
    }
}
