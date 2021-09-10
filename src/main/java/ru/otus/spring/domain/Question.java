package ru.otus.spring.domain;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Question {
    @CsvBindByName(column = "ask")
    private String ask;
    @CsvBindAndSplitByName(column = "answers", splitOn = ";", elementType = String.class)
    private List<String> answers;


    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<String> getShuffledAnswers() {
        List<String> result = new ArrayList<>(answers);
        Collections.shuffle(result);
        return result;
    }

    public String getRightAnswer() {
        return answers != null && answers.size() > 0 ? answers.get(0) : "";
    }

    @Override
    public String toString() {
        return "Question {ask: " + getAsk() + ", answers: " +  getAnswers() + ", rightAnswer: " + getRightAnswer() + "}";
    }
}
