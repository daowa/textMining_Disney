package com.data;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Result;

import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;
import com.myClass.ValueComparator;
import com.mysql.fabric.xmlrpc.base.Array;
import com.sun.jna.Library;
import com.sun.jna.Native;


public class Data_Segmentation {
	
	//��ȡÿƪ�Ĵ�Ƶ���ܵĴ�Ƶ
	private static Map<String, Integer> mapAll;//�ܵĴ�Ƶͳ��
	public static void DianPing_WordFrequency() throws SQLException, IOException{
		NLPIR.NlpirInit();
		
		ResultSet rs = DBFunction.selectAllFromDianPing();
		List<String> wordCounts = new ArrayList<String>();
		mapAll = new HashMap<String, Integer>();//�ܵĴ�Ƶͳ��
		int timeCount = 0;
		
		while(rs.next()){
			U.print(timeCount++ + "");
			String content = rs.getString(MyStatic.KEY_Content);
			Map<String, Integer> map = getDianPingWordCount(content);
			wordCounts.add(map.toString());
		}
		
		ValueComparator bvc =  new ValueComparator(mapAll);  
        TreeMap<String, Integer> sorted_mapAll = new TreeMap<String, Integer>(bvc);
        sorted_mapAll.putAll(mapAll);
        
        U.print(sorted_mapAll.toString());
        
        //����ƪ������Ƶͳ��д��txt
        FileWriter fw = null; 
        fw = new FileWriter("E:\\work\\��ʿ��\\output\\��ƪ������Ƶͳ��.txt");   
        for (int i = 0; i < wordCounts.size(); i++) {
        	fw.write(wordCounts.get(i) + "\r\n");
        	fw.write(MyStatic.Others_Line + "\r\n");
        }   
        fw.close(); 
        
        //�����е�����Ƶͳ��д��txt
        FileWriter fwAll = null; 
        fwAll = new FileWriter("E:\\work\\��ʿ��\\output\\���е�����Ƶͳ��.txt");
        fwAll.write(sorted_mapAll.toString());
        fwAll.close(); 

		NLPIR.NlpirExit();
	}
	
	private static TreeMap<String, Integer> getDianPingWordCount(String content){
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
	
	
	
	
	
	
	
	
	
	
	//��ȡ�����ı��Ĵ������������Ƶ��tf-idf�����ԡ��ʳ���λ�õȣ�����д���м����ݿ⣨id��map{�ʣ�"������,������,������",��:"������,������,������"}
	public static void DianPing_WordFeature() throws UnsupportedEncodingException, SQLException{
		NLPIR.NlpirInit();
		
		ResultSet rs = DBFunction.selectAllFromDianPing();
		Map<String, Float> mapIDF = new HashMap<String, Float>();//��idf��map
		
		//��ȡidf(���ִʵ�ƪ��/��ƪ��)
		int documentCount = DBFunction.getDianPingCount();//��ȡ��ƪ��
		mapIDF = getIDF(rs, documentCount);//ͳ��idf
		
		rs.beforeFirst();
		//��ȡ����tf-idf��󲿷�����
		int c = 0;//������
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			Map<String, Vector<String>> map = getDianPingWordFeature(content, mapIDF);
			if(DBFunction.insertMiddle(id, map.toString()) > 0){
				U.print("�����" + ++c + "���ɹ�");
			}
		}
		
		NLPIR.NlpirExit();
	}
	
	private static Map<String, Float> getIDF(ResultSet rs, int documentCount) throws SQLException{
		Map<String, Float> mapIDF = new HashMap<String, Float>();//��idf��map
		
		int c = 0;//������
		while(rs.next()){
			
			U.print("�����" + ++c + "���μǵ�idf");
			
			String content = rs.getString(MyStatic.KEY_Content);
			String[] words = NLPIR.wordSegmentateWithoutCharacteristic(content);
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
			float idf = (float)Math.log(documentCount/entry.getValue());
			entry.setValue(idf);
		}
		U.print("idfͳ�����");
		
		return mapIDF;
	}
	
	private static Map<String, Vector<String>> getDianPingWordFeature(String content, Map<String, Float> mapIDF){
		String[] words = NLPIR.wordSegmentateWithCharacteristic(content);
		List<String> stopWords = NLPIR.getStopWords();
		Map<String, Vector<String>> map = new HashMap<String, Vector<String>>();//�ü�ֵ�Եķ�ʽ�� ��-������
		int firstPosition = 0;//��¼��ʱ�����˵ڼ����֣�ȥ��ͣ�ôʣ�
		
		//ͳ�Ƶ�ǰ�μ��г��ֵĴ�����ȥͣ�ôʣ�
		int allWordCount = 0;
		for(String nlpWord : words){
			String word = getWord(nlpWord);//��ȡԭ��
			if(!stopWords.contains(word)){
				//ͳ���ܴ�Ƶ
				allWordCount++;
			}
		}
		
		for(String nlpWord : words){
			String word = getWord(nlpWord);//��ȡԭ��
			String characteristic = getCharacteristic(nlpWord);//��ȡ����
			if(!stopWords.contains(word)){//ȥͣ�ô�
				
				Vector<String> v = map.get(word);
				if(v == null){//�ô�δ��ʼ��
					v = new Vector<String>(6);//��ʼ��vector
					v.add(MyStatic.Index_WordCount, "-1");
					v.add(MyStatic.Index_WordFrequency, "-1");
					v.add(MyStatic.Index_TFIDF, "-1");
					v.add(MyStatic.Index_WordCharacteristic, characteristic);//��ʼ��ʱֱ�����ô���
					v.add(MyStatic.Index_WordLength, word.length() + "");
					v.add(MyStatic.Index_Position_FirstWord, firstPosition + "");
				}
				//����ʳ�����
				int wordCount = (v.get(MyStatic.Index_WordCount) != "-1") ? Integer.parseInt(v.get(MyStatic.Index_WordCount))+1 : 1;
				v.set(MyStatic.Index_WordCount, wordCount + "");
				//�����Ƶ
				float wordFrequency = (float)wordCount/allWordCount;
				v.set(MyStatic.Index_WordFrequency, wordFrequency + "");
				map.put(word, v);
				//����tf-idf
				float idf = mapIDF.get(word) != null ? mapIDF.get(word) : 0;
				v.set(MyStatic.Index_TFIDF, wordFrequency * idf + "");
				
				firstPosition += word.length();//(ȥ��ͣ�ôʺ�)�ƶ�ǰ�����
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
