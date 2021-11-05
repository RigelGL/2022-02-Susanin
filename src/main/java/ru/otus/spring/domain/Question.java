package ru.otus.spring.domain;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Question {

    @CsvBindByName(column = "question")
    private String question;

    @CsvBindAndSplitByName(column = "answers", splitOn = ";", elementType = String.class)
    private List<String> answers;

    private String rightAnswer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
        if(answers != null && answers.size() > 0) {
            rightAnswer = answers.get(0);
        }
        else {
            rightAnswer = "null";
        }
    }

    public List<String> getShuffledAnswers() {
        List<String> result = new ArrayList<>(answers);
        Collections.shuffle(result);
        return result;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    @Override
    public String toString() {
        return "Question {ask: " + getQuestion() + ", answers: " + getAnswers() + ", rightAnswer: " + getRightAnswer() + "}";
    }
}
