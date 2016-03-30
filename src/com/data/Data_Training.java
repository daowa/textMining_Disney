package com.data;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;

public class Data_Training {

	public static void humanIndexing() throws SQLException{
		
		ResultSet rs = DBFunction.getRandomDianPing(200, 106844, 142437);
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			U.print("=================  id:" + id + "  =================");
			U.print("原始本文:" + content);
			Map<String, Vector<String>> map = DBFunction.getDianPingStats(id);
			U.print("词统计:" + map.toString());
			
			if(DBFunction.DianPing_isIndexed(id)) continue;//如果已经标引过，则不用再标引s
			
			//输入标引的关键词
			int wordCount = 3;//限制关键词数
			while(true){
				U.print(">>请输入" + wordCount + "个词，以空格分隔(记得将光标手动定位到下一行，否则会出现异常)(输入pass跳过该条)");
				Scanner sc = new Scanner(System.in);
				String order = sc.nextLine();
				if(order.equals("pass"))//输入pass，跳过该条
					break;
				String[] keywords = order.split(" ");
				//检查输入格式
				if(keywords.length != wordCount){
					U.print("输入格式错误,可能不是" + wordCount + "个词");
					continue;//输入格式错误，则重新输入
				}
				else{
					//检查输入的词是否为分词结果中的词
					int passCount = 0;//全部通过才不需要重新输入
					for(String word : keywords){
						if(!map.containsKey(word))
							U.print(word + "并不在分词结果内");
						else 
							passCount++;
					}
					//如果输入的词满足条件了，则入库
					if(passCount == wordCount){
						if(DBFunction.insertTrainingSet(id, keywords) > 0){
							U.print("插入数据库成功");
							U.print("=================  done  =================");
						}
						break;
					}
					else
						continue;
				}
			}
		}
		
	}
	
	
	
	//将测试集中的内容输出到txt中，以供python使用
	public static void DataTraining2Txt() throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromTrainingSet();
		List<List<Double>> listX = new ArrayList<List<Double>>();//存所有词特征
		List<Integer> listY = new ArrayList<Integer>();//存是否是关键词
		List<String> listWord = new ArrayList<String>();//存关键词
		List<Integer> listID = new ArrayList<Integer>();//存测试集id
		List<String> listTFIDF = new ArrayList<String>();//存tfidf最高的n个词
		List<String> listFrequency = new ArrayList<String>();//存词频最高的n个词（如果同样高，则取现出现的）
		
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			listID.add(id);//存id
			
			String keywordsLine = rs.getString(MyStatic.KEY_Keyword);
			listWord.add(keywordsLine);//存关键词
			
			String[] keywords = keywordsLine.split(",");
			List<String> listKeys = new ArrayList<String>();
			listKeys = Arrays.asList(keywords);
			
			//获取每个词的特征
			String stats = DBFunction.getFeature(rs.getInt(MyStatic.KEY_ID_rawDianPing));
			Map<String, Vector<String>> map = U.string2Map(stats);
//			U.print(0 + map.toString());
			
			String topWords = getTopWords(map, 3, MyStatic.Index_TFIDF);
			listTFIDF.add(topWords);//存topTFIDF
			U.print(topWords);
//			U.print(1 + map.toString());
//			map = U.string2Map(stats);//好怪，map传参数过去就被改变了？！没有复制一个map的副本？！(再次实验发现没问题？！)
//			U.print(2 + map.toString());
			
			listFrequency.add(getTopWords(map, 3, MyStatic.Index_WordFrequency));//存top词频
//			U.print(3 + map.toString());
//			map = U.string2Map(stats);
//			U.print(4 + map.toString());
			
			for(String key : map.keySet()){
				//仅保留形容词之前的词
				if(U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)) > 7)
					continue;
				//存X
				List<Double> listx = new ArrayList<Double>();//存单个词特征
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_TFIDF)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_Position_FirstWord)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_WordLength)));
				listx.add((double)U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)));
				listX.add(listx);
				//存Y
				if(listKeys.contains(key))
					listY.add(1);
				else
					listY.add(0);
			}
		}
		
		FileFunction.writeTrainingSetX(listX);
		FileFunction.writeTrainingSetY(listY);
		FileFunction.writeTrainingSetWord(listWord);
		FileFunction.writeTrainingSetID(listID);
		FileFunction.writeTrainingSetTopTFIDF(listTFIDF);
		FileFunction.writeTrainingSetTopFrequency(listFrequency);
	}
	//获取最高频的n个词（tfidf或词频），便于对比分类器和tf-idf等方法的效果
	private static String getTopWords(Map<String, Vector<String>> map, int topN, int topWhat){
		//找出最高的topN个词
		//不管了直接扫描过去了
		String result = "";
		for(int i = 0; i < topN; i++){
			double maxWhat = 0;
			String maxKey = "";
			for(String key : map.keySet()){
				if(Double.parseDouble(map.get(key).get(topWhat)) > maxWhat){//仅大于才行，也就是说先出现的词有优势
					maxWhat = Double.parseDouble(map.get(key).get(topWhat));
					maxKey = key;
				}
			}
			map.remove(maxKey);
			result += maxKey + ",";
		}
		result = result.substring(0, result.length()-1);
		return result;
	}
	
	//将middle中的内容输出到txt中，以供python使用(注意确保特征字段和测试集的一致)
	public static void DataMiddle2Txt() throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromMiddle();
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String stats = rs.getString(MyStatic.KEY_Stats);
			Map<String, Vector<String>> map = U.string2Map(stats);
			
			List<List<Double>> listX = new ArrayList<List<Double>>();//存词特征
			List<String> listW = new ArrayList<String>();//存词名
			for(String key : map.keySet()){
				
				//仅保留形容词之前的词
				if(U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)) > 7)
					continue;
				
				//存X
				List<Double> listx = new ArrayList<Double>();//存单个词特征
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_TFIDF)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_Position_FirstWord)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_WordLength)));
				listx.add((double)U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)));
				listX.add(listx);
				
				//存词
				listW.add(key);
				
			}
			FileFunction.writeEveryMiddleFeature(listX, id);
			FileFunction.writeEveryMiddleWord(listW, id);
		}
		U.print("middle中的数值与词名已输出到txt");
	}
}
