package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.IOService;

import java.io.InputStreamReader;
import java.util.List;


@Repository("questionDao")
public class QuestionDaoSimple implements QuestionDao {

    private final Resource resource;
    private final IOService ioService;

    public QuestionDaoSimple(@Value("${questions.url}") Resource resource, IOService ioService) {
        this.resource = resource;
        this.ioService = ioService;
    }


    @Override
    public List<Question> getAllQuestions() {
        if(!resource.exists()) {
            ioService.printError("QuestionDaoSimple.getAllQuestions() error: questions resource not found.");
            return null;
        }

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());

            List<Question> data = new CsvToBeanBuilder<Question>(inputStreamReader).withType(Question.class).build().parse();
            inputStreamReader.close();

            return data;
        } catch(Exception e) {
            ioService.printError("QuestionDaoSimple.getAllQuestions() error while reading questions resource\n" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
