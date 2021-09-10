package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.Resource;
import ru.otus.spring.domain.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoSimple implements QuestionDao {

    private List<Question> data = new ArrayList<>();


    public QuestionDaoSimple(Resource resource) {

        if(!resource.exists()) {
            System.err.println("QuestionDaoSimple.<init> error: questions resource not found.");
            return;
        }

        try {
            data = new CsvToBeanBuilder<Question>(new FileReader(resource.getFile())).withType(Question.class).build().parse();
        } catch(Exception e) {
            System.err.println("QuestionDaoSimple.<init> error while reading questions resource\n" + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public List<Question> getAllQuestions() {
        return data;
    }
}
