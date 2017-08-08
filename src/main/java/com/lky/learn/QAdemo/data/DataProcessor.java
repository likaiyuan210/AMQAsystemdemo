package com.lky.learn.QAdemo.data;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by lky on 2017/8/8.
 */
public class DataProcessor {

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
}
