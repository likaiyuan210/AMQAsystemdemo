package com.lky.learn.QAdemo.main;

/**
 * Created by lky on 2017/8/7.
 *
 */
public class QAsystem {

    private static String getAnswer(String question){
        String answer = "There is no answer!";
        if(question!=null||question.equals("")){

            answer = "This is an answer!";
            return answer;
        }
        else
            return answer;
    }
    public static void main(String[] args){
        String question = "This is a question";
        System.out.println(QAsystem.getAnswer(question));

        System.out.println(QAsystem.getAnswer(question));
    }
}
