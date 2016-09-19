package com.analysis;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale.Category;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.alibaba.fastjson.JSON;
import com.data.DBFunction;
import com.myClass.MyStatic;
import com.myClass.U;
import com.myClass.ValueComparator;


public class Data_Segmentation {
	
	//��ȡÿƪ�Ĵ�Ƶ���ܵĴ�Ƶ
	private static Map<String, Integer> mapAll;//�ܵĴ�Ƶͳ��
	public static void WordFrequency(int category) throws SQLException, IOException{
		NLPIR.NlpirInit();
		
		ResultSet rs = DBFunction.selectAll(category);
		List<String> wordCounts = new ArrayList<String>();
		mapAll = new HashMap<String, Integer>();//�ܵĴ�Ƶͳ��
		int timeCount = 0;
		
		while(rs.next()){
			U.print(timeCount++ + "");
			String content = rs.getString(MyStatic.KEY_Content);
			Map<String, Integer> map = getWordCount(content);
			wordCounts.add(map.toString());
		}
		
		ValueComparator bvc =  new ValueComparator(mapAll);  
        TreeMap<String, Integer> sorted_mapAll = new TreeMap<String, Integer>(bvc);
        sorted_mapAll.putAll(mapAll);
        
        U.print(sorted_mapAll.toString());
        
        //����ƪ��Ƶͳ��д��txt
        FileWriter fw = null; 
        String addressSingle = ""; //��ƪ�ı���Ƶͳ��
        String addressAll = ""; //�����ı���Ƶͳ��
        if(category == MyStatic.Category_YouJi){
        	addressSingle = "E:\\work\\��ʿ��\\output\\��ƪ��Ƶͳ��_�μ�.txt";
        	addressAll = "E:\\work\\��ʿ��\\output\\���д�Ƶͳ��_�μ�.txt";
        }
        else if(category == MyStatic.Category_DianPing){
        	addressSingle = "E:\\work\\��ʿ��\\output\\��ƪ��Ƶͳ��_����.txt";
        	addressAll = "E:\\work\\��ʿ��\\output\\���д�Ƶͳ��_����.txt";
        }
        fw = new FileWriter(addressSingle);   
        for (int i = 0; i < wordCounts.size(); i++) {
        	fw.write(wordCounts.get(i) + "\r\n");
        	fw.write(MyStatic.Others_Line + "\r\n");
        }   
        fw.close(); 
        
        //�����е�����Ƶͳ��д��txt
        FileWriter fwAll = null; 
        fwAll = new FileWriter(addressAll);
        fwAll.write(sorted_mapAll.toString());
        fwAll.close(); 

		NLPIR.NlpirExit();
	}
	
	private static TreeMap<String, Integer> getWordCount(String content){
		String[] words = NLPIR.wordSegmentateWithoutCharacteristic(content);
		List<String> stopWords = NLPIR.getStopWords();
		Map<String, Integer> map = new HashMap<String, Integer>();//�ü�ֵ�Եķ�ʽ�� ��-��Ƶ
		
		for(String word : words){
			if(!stopWords.contains(word)){//ȥͣ�ô�
				int count = ((Integer)map.get(word) != null) ? (Integer)map.get(word)+1 : 1;
				map.put(word, count);
				//�ܵĴ�Ƶ����
				int countAll = ((Integer)mapAll.get(word) != null) ? (Integer)mapAll.get(word)+1 : 1;
				mapAll.put(word, countAll);
			}
		}
		ValueComparator bvc =  new ValueComparator(map);  
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
        sorted_map.putAll(map);
        
		return sorted_map;
	}
	
	
	
	
	
	
	
	
	
	//��ȡ�ı��Ĵ������������Ƶ��tf-idf�����ԡ��ʳ���λ�õȣ�����д���м����ݿ⣨id��map{�ʣ�"������,������,������",��:"������,������,������"}
	public static void WordFeature(int category) throws UnsupportedEncodingException, SQLException{
		NLPIR.NlpirInit();
		
		ResultSet rs = DBFunction.selectAll(category);
		Map<String, Float> mapIDF = new HashMap<String, Float>();//��idf��map
		
		//��ȡidf(���ִʵ�ƪ��/��ƪ��)
		int documentCount = DBFunction.getRowsCount(category);//��ȡ��ƪ��
		mapIDF = getIDF(rs, documentCount);//ͳ��idf
		
		rs.beforeFirst();
		//��ȡ����tf-idf��󲿷�����
		int c = 0;//������
		while(rs.next()){
			int id = -1;
			if(category == MyStatic.Category_YouJi)
				id = rs.getInt(MyStatic.KEY_ID_rawYouJi);
			else if(category == MyStatic.Category_DianPing)
				id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			Map<String, Vector<String>> map = getWordFeature(category, content, mapIDF);
			if(DBFunction.insertMiddle(category, id, JSON.toJSONString(map)) > 0){
				U.print("�����" + ++c + "���ɹ�");
			}
		}
		
		NLPIR.NlpirExit();
	}
	
	private static Map<String, Float> getIDF(ResultSet rs, int documentCount) throws SQLException{
		Map<String, Float> mapIDF = new HashMap<String, Float>();//��idf��map
		
		int c = 0;//������
		while(rs.next()){
			
			U.print("�����" + ++c + "����¼��idf");
			
			String content = rs.getString(MyStatic.KEY_Content);
			U.print("content:" + content);
			String[] words = U.dereplication(NLPIR.wordSegmentateWithoutCharacteristic(content));
			List<String> stopWords = NLPIR.getStopWords();

			for(String word : words){
				if(!stopWords.contains(word)){
					//ͳ�Ƴ��ָôʵ�ƪ��
					if(mapIDF.containsKey(word)){
						mapIDF.put(word, mapIDF.get(word) + 1);
					}
					else{
						mapIDF.put(word, (float)1);
					}
				}
			}
		}
		
		U.print("��ʼͳ��idf");
		//��ȡidf
		for (Entry<String, Float> entry : mapIDF.entrySet()) {
			//���һ����Խ��������ô��ĸ��Խ�����ĵ�Ƶ�ʾ�ԽСԽ�ӽ�0��log��ʾ�Եõ���ֵȡ����
			float idf = (float)Math.log(documentCount/(entry.getValue()));
			entry.setValue(idf);
		}
		U.print("idfͳ�����");
		
		return mapIDF;
	}
	
	private static Map<String, Vector<String>> getWordFeature(int category, String content, Map<String, Float> mapIDF){
		String[] words = NLPIR.wordSegmentateWithCharacteristic(content);
		List<String> stopWords = NLPIR.getStopWords();
		Map<String, Vector<String>> map = new HashMap<String, Vector<String>>();//�ü�ֵ�Եķ�ʽ�� ��-������
		int position = 0;//��¼��ʱ�����˵ڼ����֣�ȥ��ͣ�ôʺ�
		
		//ͳ�Ƶ�ǰ�μ��г��ֵĴ�����ȥͣ�ôʣ�
		int allWordCount = 0;
		for(String nlpWord : words){
			String word = getWord(nlpWord);//��ȡԭ��
			if(!stopWords.contains(word)){
				//ͳ���ܴ�Ƶ
				allWordCount++;
			}
		}
		
		int wordsLength = words.length;//�ܴ���������tf�Ĺ�һ��
		for(String nlpWord : words){
			String word = getWord(nlpWord);//��ȡԭ��
			String characteristic = getCharacteristic(nlpWord);//��ȡ����
			if(!stopWords.contains(word)){//ȥͣ�ô�
				
				Vector<String> v = map.get(word);
				if(v == null){//�ô�δ��ʼ��
					v = new Vector<String>(6);//��ʼ��vector
					if (category == MyStatic.Category_YouJi){
						v.add(MyStatic.Index_WordCount, "-1");
						v.add(MyStatic.Index_WordFrequency, "-1");
						v.add(MyStatic.Index_TFIDF, "-1");
						v.add(MyStatic.Index_WordCharacteristic, characteristic);//��ʼ��ʱֱ�����ô���
						v.add(MyStatic.Index_WordLength, word.length() + "");
						v.add(MyStatic.Index_Position_FirstWord, (float)(position)/(allWordCount) + "");//��Ե�λ��
						v.add(MyStatic.Index_Position_LastWord, "0.0");//����ʱ��ʼ��Ϊ0.0
						v.add(MyStatic.Index_Position_Absolute, "0.0");//����ʱ��ʼ��Ϊ0.0
					}
					else if(category == MyStatic.Category_DianPing){
						v.add(MyStatic.Index_WordCount, "-1");
						v.add(MyStatic.Index_WordFrequency, "-1");
						v.add(MyStatic.Index_TFIDF, "-1");
						v.add(MyStatic.Index_WordCharacteristic, characteristic);//��ʼ��ʱֱ�����ô���
						v.add(MyStatic.Index_WordLength, word.length() + "");
						v.add(MyStatic.Index_Position_FirstWord, (float)(position)/(allWordCount) + "");//��Ե�λ��
						v.add(MyStatic.Index_Position_LastWord, "0.0");//����ʱ��ʼ��Ϊ0.0
						v.add(MyStatic.Index_Position_Absolute, "0.0");//����ʱ��ʼ��Ϊ0.0
					}
				}
				
				//����ʳ�����
				int wordCount = (v.get(MyStatic.Index_WordCount) != "-1") ? Integer.parseInt(v.get(MyStatic.Index_WordCount))+1 : 1;
				v.set(MyStatic.Index_WordCount, wordCount + "");
				
				//�����Ƶ,�������ܴ����ˣ���Ϊѵ�������Դʶ��������ı�����ѵ���ģ������ܴ������ö��ı���tf����
				float tf = (float)wordCount;
				v.set(MyStatic.Index_WordFrequency, tf + "");
				map.put(word, v);
				
				//����tf-idf
				float idf = mapIDF.get(word) != null ? mapIDF.get(word) : 0;
				v.set(MyStatic.Index_TFIDF, tf * idf + "");
				
				//ÿ�ζ�����lastPosition
				v.set(MyStatic.Index_Position_LastWord, (float)(position)/(allWordCount) + "");
				//ÿ�ζ�����absolutePosition,ȡ��С�ľ���
				v.set(MyStatic.Index_Position_Absolute, 
					Float.parseFloat(v.get(MyStatic.Index_Position_FirstWord)) < Float.parseFloat(v.get(MyStatic.Index_Position_Absolute)) ? 
					v.get(MyStatic.Index_Position_FirstWord) : v.get(MyStatic.Index_Position_Absolute));
				position += word.length();//(ȥ��ͣ�ôʺ�)�ƶ�ǰ�����
			}
		}
		return map;
	}
	
	//�Ӵ��д��ԵĴ��з����ԭ��
	private static String getWord(String nlpWord){
		Pattern p = Pattern.compile("/.*$");
		Matcher m = p.matcher(nlpWord);
		int start = 0;
		
		if(m.find()){
			start = m.start();
			return nlpWord.substring(0, start);
		}
		
		return "noFind";//δ��ƥ���򷵻�"noFind"
		
	}
	//�Ӵ��д��ԵĴ��з��������
	private static String getCharacteristic(String nlpWord){
		Pattern p = Pattern.compile("/.*$");
		Matcher m = p.matcher(nlpWord);
		int start = 0;
		
		if(m.find()){
			start = m.start();
			return nlpWord.substring(start, nlpWord.length());
		}
		
		return "/noFind";
	}
	
	
}
