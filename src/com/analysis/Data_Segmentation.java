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
	
	//获取每篇的词频和总的词频
	private static Map<String, Integer> mapAll;//总的词频统计
	public static void WordFrequency(int category) throws SQLException, IOException{
		NLPIR.NlpirInit();
		
		ResultSet rs = DBFunction.selectAll(category);
		List<String> wordCounts = new ArrayList<String>();
		mapAll = new HashMap<String, Integer>();//总的词频统计
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
        
        //将单篇词频统计写入txt
        FileWriter fw = null; 
        String addressSingle = ""; //单篇文本词频统计
        String addressAll = ""; //所有文本词频统计
        if(category == MyStatic.Category_YouJi){
        	addressSingle = "E:\\work\\迪士尼\\output\\单篇词频统计_游记.txt";
        	addressAll = "E:\\work\\迪士尼\\output\\所有词频统计_游记.txt";
        }
        else if(category == MyStatic.Category_DianPing){
        	addressSingle = "E:\\work\\迪士尼\\output\\单篇词频统计_点评.txt";
        	addressAll = "E:\\work\\迪士尼\\output\\所有词频统计_点评.txt";
        }
        fw = new FileWriter(addressSingle);   
        for (int i = 0; i < wordCounts.size(); i++) {
        	fw.write(wordCounts.get(i) + "\r\n");
        	fw.write(MyStatic.Others_Line + "\r\n");
        }   
        fw.close(); 
        
        //将所有点评词频统计写入txt
        FileWriter fwAll = null; 
        fwAll = new FileWriter(addressAll);
        fwAll.write(sorted_mapAll.toString());
        fwAll.close(); 

		NLPIR.NlpirExit();
	}
	
	private static TreeMap<String, Integer> getWordCount(String content){
		String[] words = NLPIR.wordSegmentateWithoutCharacteristic(content);
		List<String> stopWords = NLPIR.getStopWords();
		Map<String, Integer> map = new HashMap<String, Integer>();//用键值对的方式存 词-词频
		
		for(String word : words){
			if(!stopWords.contains(word)){//去停用词
				int count = ((Integer)map.get(word) != null) ? (Integer)map.get(word)+1 : 1;
				map.put(word, count);
				//总的词频汇总
				int countAll = ((Integer)mapAll.get(word) != null) ? (Integer)mapAll.get(word)+1 : 1;
				mapAll.put(word, countAll);
			}
		}
		ValueComparator bvc =  new ValueComparator(map);  
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
        sorted_map.putAll(map);
        
		return sorted_map;
	}
	
	
	
	
	
	
	
	
	
	//获取文本的词与词特征（词频、tf-idf、词性、词长、位置等），并写入中间数据库（id、map{词："词特征,词特征,词特征",词:"词特征,词特征,词特征"}
	public static void WordFeature(int category) throws UnsupportedEncodingException, SQLException{
		NLPIR.NlpirInit();
		
		ResultSet rs = DBFunction.selectAll(category);
		Map<String, Float> mapIDF = new HashMap<String, Float>();//存idf的map
		
		//获取idf(出现词的篇数/总篇数)
		int documentCount = DBFunction.getRowsCount(category);//获取总篇数
		mapIDF = getIDF(rs, documentCount);//统计idf
		
		rs.beforeFirst();
		//获取除了tf-idf外大部分特征
		int c = 0;//计数器
		while(rs.next()){
			int id = -1;
			if(category == MyStatic.Category_YouJi)
				id = rs.getInt(MyStatic.KEY_ID_rawYouJi);
			else if(category == MyStatic.Category_DianPing)
				id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			Map<String, Vector<String>> map = getWordFeature(category, content, mapIDF);
			if(DBFunction.insertMiddle(category, id, JSON.toJSONString(map)) > 0){
				U.print("插入第" + ++c + "条成功");
			}
		}
		
		NLPIR.NlpirExit();
	}
	
	private static Map<String, Float> getIDF(ResultSet rs, int documentCount) throws SQLException{
		Map<String, Float> mapIDF = new HashMap<String, Float>();//存idf的map
		
		int c = 0;//计数器
		while(rs.next()){
			
			U.print("处理第" + ++c + "条记录的idf");
			
			String content = rs.getString(MyStatic.KEY_Content);
			U.print("content:" + content);
			String[] words = U.dereplication(NLPIR.wordSegmentateWithoutCharacteristic(content));
			List<String> stopWords = NLPIR.getStopWords();

			for(String word : words){
				if(!stopWords.contains(word)){
					//统计出现该词的篇数
					if(mapIDF.containsKey(word)){
						mapIDF.put(word, mapIDF.get(word) + 1);
					}
					else{
						mapIDF.put(word, (float)1);
					}
				}
			}
		}
		
		U.print("开始统计idf");
		//获取idf
		for (Entry<String, Float> entry : mapIDF.entrySet()) {
			//如果一个词越常见，那么分母就越大，逆文档频率就越小越接近0。log表示对得到的值取对数
			float idf = (float)Math.log(documentCount/(entry.getValue()));
			entry.setValue(idf);
		}
		U.print("idf统计完毕");
		
		return mapIDF;
	}
	
	private static Map<String, Vector<String>> getWordFeature(int category, String content, Map<String, Float> mapIDF){
		String[] words = NLPIR.wordSegmentateWithCharacteristic(content);
		List<String> stopWords = NLPIR.getStopWords();
		Map<String, Vector<String>> map = new HashMap<String, Vector<String>>();//用键值对的方式存 词-词特征
		int position = 0;//记录这时读到了第几个字（去除停用词后）
		
		//统计当前游记中出现的词数（去停用词）
		int allWordCount = 0;
		for(String nlpWord : words){
			String word = getWord(nlpWord);//获取原词
			if(!stopWords.contains(word)){
				//统计总词频
				allWordCount++;
			}
		}
		
		int wordsLength = words.length;//总词数，用于tf的归一化
		for(String nlpWord : words){
			String word = getWord(nlpWord);//获取原词
			String characteristic = getCharacteristic(nlpWord);//获取词性
			if(!stopWords.contains(word)){//去停用词
				
				Vector<String> v = map.get(word);
				if(v == null){//该词未初始化
					v = new Vector<String>(6);//初始化vector
					if (category == MyStatic.Category_YouJi){
						v.add(MyStatic.Index_WordCount, "-1");
						v.add(MyStatic.Index_WordFrequency, "-1");
						v.add(MyStatic.Index_TFIDF, "-1");
						v.add(MyStatic.Index_WordCharacteristic, characteristic);//初始化时直接设置词性
						v.add(MyStatic.Index_WordLength, word.length() + "");
						v.add(MyStatic.Index_Position_FirstWord, (float)(position)/(allWordCount) + "");//相对的位置
						v.add(MyStatic.Index_Position_LastWord, "0.0");//先暂时初始化为0.0
						v.add(MyStatic.Index_Position_Absolute, "0.0");//先暂时初始化为0.0
					}
					else if(category == MyStatic.Category_DianPing){
						v.add(MyStatic.Index_WordCount, "-1");
						v.add(MyStatic.Index_WordFrequency, "-1");
						v.add(MyStatic.Index_TFIDF, "-1");
						v.add(MyStatic.Index_WordCharacteristic, characteristic);//初始化时直接设置词性
						v.add(MyStatic.Index_WordLength, word.length() + "");
						v.add(MyStatic.Index_Position_FirstWord, (float)(position)/(allWordCount) + "");//相对的位置
						v.add(MyStatic.Index_Position_LastWord, "0.0");//先暂时初始化为0.0
						v.add(MyStatic.Index_Position_Absolute, "0.0");//先暂时初始化为0.0
					}
				}
				
				//计算词出现数
				int wordCount = (v.get(MyStatic.Index_WordCount) != "-1") ? Integer.parseInt(v.get(MyStatic.Index_WordCount))+1 : 1;
				v.set(MyStatic.Index_WordCount, wordCount + "");
				
				//计算词频,不除以总词数了，因为训练中是以词而不是以文本进行训练的，除以总词数会让短文本的tf上升
				float tf = (float)wordCount;
				v.set(MyStatic.Index_WordFrequency, tf + "");
				map.put(word, v);
				
				//计算tf-idf
				float idf = mapIDF.get(word) != null ? mapIDF.get(word) : 0;
				v.set(MyStatic.Index_TFIDF, tf * idf + "");
				
				//每次都更新lastPosition
				v.set(MyStatic.Index_Position_LastWord, (float)(position)/(allWordCount) + "");
				//每次都更新absolutePosition,取最小的距离
				v.set(MyStatic.Index_Position_Absolute, 
					Float.parseFloat(v.get(MyStatic.Index_Position_FirstWord)) < Float.parseFloat(v.get(MyStatic.Index_Position_Absolute)) ? 
					v.get(MyStatic.Index_Position_FirstWord) : v.get(MyStatic.Index_Position_Absolute));
				position += word.length();//(去除停用词后)移动前向距离
			}
		}
		return map;
	}
	
	//从带有词性的词中分离出原词
	private static String getWord(String nlpWord){
		Pattern p = Pattern.compile("/.*$");
		Matcher m = p.matcher(nlpWord);
		int start = 0;
		
		if(m.find()){
			start = m.start();
			return nlpWord.substring(0, start);
		}
		
		return "noFind";//未能匹配则返回"noFind"
		
	}
	//从带有词性的词中分离出词性
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
