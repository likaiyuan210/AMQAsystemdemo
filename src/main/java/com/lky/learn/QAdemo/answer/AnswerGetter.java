package com.lky.learn.QAdemo.answer;

import com.lky.learn.QAdemo.corpus.CorpusReader;
import com.lky.learn.QAdemo.data.DataProcessor;
import com.lky.learn.QAdemo.data.PreProcessor;

import java.util.*;

public class AnswerGetter {
    String question = null;
    private String answer = null;

    public AnswerGetter(String question){
        this.question = question;
    }

    public String getAnswer(CorpusReader corpusReader) throws Exception {
        ArrayList<String> questionList = corpusReader.getQuestionList();
        Map<String, Map<String, String>> corpusItem = new TreeMap<String, Map<String, String>>();
        //对问题分词
        question = PreProcessor.segment(question);
        //先进行模板匹配
        for(int i = 0; i < questionList.size(); i++){
            if(question.equals(questionList.get(i))){
                corpusItem.put(question, corpusReader.getCorpus().get(question));
                if(corpusItem!=null && !corpusItem.equals("")){
                    Set<Map.Entry<String, Map<String, String>>> es = corpusItem.entrySet();
                    for (Iterator<Map.Entry<String, Map<String, String>>> it = es.iterator(); it.hasNext(); ) {
                        Map.Entry<String, Map<String, String>> me = it.next();
                        Set<Map.Entry<String, String>> ansSet = me.getValue().entrySet();
                        for (Iterator<Map.Entry<String, String>> it2 = ansSet.iterator(); it2.hasNext(); ) {
                            Map.Entry<String, String> me2 = it2.next();
                            answer = "Similar Trouble：" + "\n" + me2.getKey() + "\n" +"Trouble Shooting：" +"\n"+ me2.getValue();
                            System.out.println( answer);
                        }
                    }
                }
                return this.answer;
            }
        }

        //进行文本相似度计算
        questionList.add(question);
        Map<String, Map<String, Double>> tfidf = DataProcessor.computeTFIDF(questionList);
        Map<String,Double> QuestionScore=new TreeMap<String, Double>();//问题与每一个已存故障的TFIDF值

        Set<Map.Entry<String, Map<String, Double>>> tfidfSet = tfidf.entrySet();
        for (Iterator<Map.Entry<String, Map<String, Double>>> it = tfidfSet.iterator(); it.hasNext(); ) {
            Map.Entry<String, Map<String, Double>> me = it.next();
            if (!me.getKey().equals(question)) {
                double score=computeSim(tfidf.get(question),me.getValue());
                QuestionScore.put(me.getKey(),score);
            }
        }

        String SimQuestion=Shell(QuestionScore);
        if(SimQuestion!=null && !SimQuestion.equals("")) {
            corpusItem.put(SimQuestion, corpusReader.getCorpus().get(SimQuestion));
        }
        //转换corpusItem-->String
        if(!corpusItem.equals("")){
            System.out.println(corpusItem);
            Set<Map.Entry<String, Map<String, String>>> es = corpusItem.entrySet();
            for (Map.Entry<String, Map<String, String>> me : es) {
                if(me.getKey().equals("抱歉，知识库中没有您要查找的排故方案！"))
                    return me.getKey();
                Set<Map.Entry<String, String>> ansSet = me.getValue().entrySet();
                for (Map.Entry<String, String> me2 : ansSet) {
                    answer = "Similar Trouble：" + "\n" + me2.getKey() + "\n" + "Trouble Shooting：" + "\n" + me2.getValue();
                    System.out.println(answer);
                }
            }
        }
        return answer;
    }
    public  double computeSim(Map<String, Double> giveQuestion, Map<String, Double> Questionset) {
        // TODO Auto-generated method stub
        double mul = 0, giveAbs = 0, setAbs = 0;
        Set<Map.Entry<String, Double>> giveQuestionSet = giveQuestion.entrySet();
        for(Iterator<Map.Entry<String, Double>> it = giveQuestionSet.iterator(); it.hasNext();){
            Map.Entry<String, Double> me = it.next();
            if(Questionset.containsKey(me.getKey())){
                mul += me.getValue()*Questionset.get(me.getKey());
            }
            giveAbs += me.getValue() * me.getValue();
        }
        giveAbs = Math.sqrt(giveAbs);

        Set<Map.Entry<String, Double>> QuestionsetSet = Questionset.entrySet();
        for(Iterator<Map.Entry<String, Double>> it = QuestionsetSet.iterator(); it.hasNext();){
            Map.Entry<String, Double> me = it.next();
            setAbs += me.getValue()*me.getValue();
        }
        setAbs = Math.sqrt(setAbs);
        return mul / (giveAbs * setAbs);
    }

    //对每个问题的分值进行排序,返回最大值对应的问题
    public  String Shell(Map<String,Double> QuestionScore) {
        double max=0;
        String Question = null;
        Set<Map.Entry<String, Double>> QuestionScoreSet = QuestionScore.entrySet();
        for(Iterator<Map.Entry<String, Double>> it = QuestionScoreSet.iterator(); it.hasNext();) {
            Map.Entry<String, Double> me = it.next();
            if(me.getValue()> max){
                max=me.getValue();
                Question=me.getKey();
            }
        }
        if(max<0.3)
            return "抱歉，知识库中没有您要查找的排故方案！";
        return Question;
    }

}
