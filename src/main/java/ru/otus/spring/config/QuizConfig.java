package ru.otus.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "quiz")
@Component
public class QuizConfig {

    private Resource questions;
    private int minimumForTest;

    public Resource getQuestions() {
        return questions;
    }

    public void setQuestions(Resource questions) {
        this.questions = questions;
    }

    public int getMinimumForTest() {
        return minimumForTest;
    }

    public void setMinimumForTest(int minimumForTest) {
        this.minimumForTest = minimumForTest;
    }
}
