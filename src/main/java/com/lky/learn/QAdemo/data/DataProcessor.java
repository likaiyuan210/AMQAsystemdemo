package com.lky.learn.QAdemo.data;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Created by lky on 2017/8/8.
 *
 */
public class DataProcessor {
    /**
     * 使用IK分词器进行中文分词
     * @param text
     * @return
     * @throws IOException
     */
    public static String segChinese(String text) throws IOException {
        //创建分词对象
        Analyzer analyzer = new IKAnalyzer(true);
        StringReader reader = new StringReader(text);
        //分词
        TokenStream ts = analyzer.tokenStream("", reader);
        CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
        StringBuffer buffer = new StringBuffer();
        ts.reset();
        while(ts.incrementToken()){
            buffer.append(term.toString()+" ");
        }
        String segText = buffer.toString();
        reader.close();
        return  segText;

    }

    /**
     * 计算问题列表的TFIDF值,
     * @param questions
     * @return
     * @throws Exception
     */
    public  static  Map<String,Map<String,Double>> computeTFIDF(ArrayList<String> questions) throws Exception {
        //从训练集和测试集建立属性词典
        Map<String,Map<String,Double>> storeMap= new TreeMap<String, Map<String, Double>>(); //存放<问题，<单词，tfidf值>>
        SortedMap<String,Double> TFPerDocMap = new TreeMap<String,Double>();
        Map<String,Map<String,Double>> fileMap=new TreeMap<String, Map<String, Double>>();

//        HashSet<String> dic = dataPreProcess.GetDic(".\\Dictionary_Engine_chinese.txt");//得到属性词典

        Map<String, Double> DFMap = computeDF(questions);


        /*计算训练集每篇文档中每个词的tfidf*/
        for(int i = 0; i < questions.size(); i++) {
            fileMap.clear();
            TFPerDocMap.clear();
            Double wordSumPerDoc = 0.0;//计算每篇文档的总词数
            String[] questionsplit=questions.get(i).split(" ");
            for(int j=0;j<questionsplit.length;j++){
//                if (dic.contains(questionsplit[j])) {                                    //必须是属性词典里面的词，去掉的词不考虑
                wordSumPerDoc++;
                if (TFPerDocMap.containsKey(questionsplit[j])) {
                    Double count = TFPerDocMap.get(questionsplit[j]);
                    TFPerDocMap.put(questionsplit[j], count + 1);
                } else {
                    TFPerDocMap.put(questionsplit[j], 1.0);
                }
//                }
            }
            //遍历一下当前文档的TFmap，除以文档的总词数换成词频,然后将词频乘以词的IDF，得到最终的特征权值，并且输出到文件
            //注意测试样例和训练样例写入的文件不同
            Double wordWeight;
            Set<Map.Entry<String, Double>> tempTF = TFPerDocMap.entrySet();
            for (Iterator<Map.Entry<String, Double>> mt = tempTF.iterator(); mt.hasNext(); ) {
                Map.Entry<String, Double> me = mt.next();
                wordWeight = Math.log(me.getValue() / wordSumPerDoc + 1) * Math.log((questions.size() + 1) / DFMap.get(me.getKey()));
                TFPerDocMap.put(me.getKey(), wordWeight);
            }
            TreeMap<String,Double> tempMap = new TreeMap<String,Double>();
            tempMap.putAll(TFPerDocMap);
            fileMap.put(questions.get(i), tempMap);
            storeMap.putAll(fileMap);
        }
        return storeMap;
    }


    /**计算IDF，即属性词典中每个词在多少个文档中出现过
     * @param questions 样本所在目录
     * @return 单词的IDFmap 格式为SortedMap<String,Double> 即<单词，包含该单词的文档数>
     * @throws IOException
     */
    private static Map<String,Double> computeDF(ArrayList<String> questions) throws IOException {
        // TODO Auto-generated method stub
        String word;
        /*统计出现在测试集的单词*/
        Map<String,Double> IDFPerWordMap = new TreeMap<String,Double>();
        Set<String> alreadyCountWord = new HashSet<String>();//记下当前已经遇到过的该文档中的词
        for(int i = 0; i <questions.size(); i++){
            alreadyCountWord.clear();
            String[] QuestionSplit=questions.get(i).split(" ");
            for(int j = 0; j <QuestionSplit.length; j++){
                if(!alreadyCountWord.contains(QuestionSplit[j])){
                    if(IDFPerWordMap.containsKey(QuestionSplit[j])){
                        IDFPerWordMap.put(QuestionSplit[j], IDFPerWordMap.get(QuestionSplit[j]) + 1.0);
                    }
                    else
                        IDFPerWordMap.put(QuestionSplit[j], 1.0);
                    alreadyCountWord.add(QuestionSplit[j]);
                }
            }
        }
        return IDFPerWordMap;
    }
}
