package com.lky.learn.QAdemo.answer;

public class AnswerGetter {
    String question = null;
    private String answer = null;

    AnswerGetter(String question){
        this.question = question;
    }

    public String getAnswer(){
        return this.answer;
    }

}
