package com.lky.learn.QAdemo.corpus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CorpusReader {
    String url = null;
    Map<String, Map<String, String>> corpus;
    CorpusReader() throws Exception {
        setUrl("src/main/resources/NewQuestionAndAnswer_Engine.txt");
        initCorpus();

    }
    CorpusReader(String url){
        setUrl(url);
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {

        return url;
    }
    private void initCorpus() throws Exception {
        this.corpus = new TreeMap<String, Map<String, String>>();
        FileReader fileReader = new FileReader(getUrl());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Map<String, String> map = new TreeMap<String, String>();
        String line = bufferedReader.readLine();
        while (line!=null){
            map.clear();
            String[] linesplit = line.split("\t");
            map.put(linesplit[1], linesplit[2]);
            Map<String,String> temp=new TreeMap<String, String>();
            temp.putAll(map);
            corpus.put(linesplit[0],temp);
            line = bufferedReader.readLine();
        }
        //打印
        Set<Map.Entry<String, Map<String, String>>> corpusSet = corpus.entrySet();
        for (Iterator<Map.Entry<String, Map<String, String>>> it = corpusSet.iterator(); it.hasNext(); )
        {
            Map.Entry<String, Map<String, String>> me = it.next();
            System.out.printf("%s|%s\n", me.getKey(),me.getValue().toString());
        }

    }

    public static void main(String[] args) throws Exception {
        CorpusReader corpusReader = new CorpusReader();
    }


}
