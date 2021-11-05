package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Question;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;


@Repository("questionDao")
public class QuestionDaoCSV implements QuestionDao {

    private final Resource resource;


    public QuestionDaoCSV(@Value("${questions.url}") Resource resource) {
        this.resource = resource;
    }


    @Override
    public List<Question> getAllQuestions() throws Exception {
        if(!resource.exists())
            throw new FileNotFoundException(resource.getDescription());

        try(InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream())) {
            return new CsvToBeanBuilder<Question>(inputStreamReader).withType(Question.class).build().parse();
        } catch(Throwable e) {
            throw new RuntimeException("QuestionDaoSimple.getAllQuestions() error while reading questions resource:\n\t" + e.toString());
        }
    }
}
