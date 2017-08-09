package com.lky.learn.QAdemo.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by lky on 2017/8/8.
 */
public class PreProcessor {
    /**
     * 获取知识库问题清单
     * @param corpus
     * @return
     */
    public static ArrayList<String> getQuestionsList(Map<String, Map<String, String>> corpus){
        ArrayList<String> questionList = new ArrayList<String>();
        Set<Map.Entry<String, Map<String, String>>> corpusSet = corpus.entrySet();
        for (Iterator<Map.Entry<String, Map<String, String>>> it = corpusSet.iterator(); it.hasNext(); )
        {
            Map.Entry<String, Map<String, String>> me = it.next();
           questionList.add(me.getKey());
        }
        return questionList;
    }

    /**
     * 中文分词
     * @param text
     * @return
     * @throws IOException
     */
    public static String segment(String text) throws IOException {
        StringBuffer str = new StringBuffer();
        String seg = DataProcessor.segChinese(text);
        String[] temp = seg.split(" ");

        for(int i = 0; i < temp.length; i++){
            str.append(temp[i] + " ");
        }
        return String.valueOf(str).trim();
    }
}
