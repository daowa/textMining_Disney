package com.analysis;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

import com.data.DBFunction;
import com.data.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;

public class Data_Training {

	public static void humanIndexing(int category, int mostWord) throws SQLException{
		ResultSet rs = DBFunction.getRandomContent(category, 200, 1, 6070);
		while(rs.next()){
			int id = (category == MyStatic.Category_YouJi) ? rs.getInt(MyStatic.KEY_ID_rawYouJi) : rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			U.print("=================  id:" + id + "  =================");
			U.print("原始本文:" + content);
			Map<String, Vector<String>> map = DBFunction.getStats(category, id);
//			U.print("词统计:" + map.toString());
			
			if(DBFunction.isIndexed(category, id)) continue;//如果已经标引过，则不用再标引
			
			//输入标引的关键词
			while(true){
				U.print(">>请输入至多" + mostWord + "个词，以空格分隔(记得将光标手动定位到下一行，否则会出现异常)(输入pass跳过该条)");
				Scanner sc = new Scanner(System.in);
				String order = sc.nextLine();
				if(order.equals("pass"))//输入pass，跳过该条
					break;
				String[] keywords = order.split(" ");
				//检查输入格式
				if(keywords.length > mostWord){
					U.print("输入格式错误,超过了" + mostWord + "个词");
					continue;//输入格式错误，则重新输入
				}
				else{
					//检查输入的词是否为分词结果中的词
					boolean pass = true;
					for(String word : keywords){
						if(!map.containsKey(word)){
							pass = false;
							U.print(word + "并不在分词结果内");
						}
					}
					if(!pass) continue;
					//如果输入的词满足条件了，则入库
					if(keywords.length <= mostWord){
						if(DBFunction.insertTrainingSet(category, id, keywords) > 0){
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
	public static void DataTraining2Txt(int category) throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromTrainingSet(category);
		List<List<Double>> listX = new ArrayList<List<Double>>();//存所有词特征
		List<Integer> listY = new ArrayList<Integer>();//存是否是关键词
		List<String> listWord = new ArrayList<String>();//存关键词
		List<Integer> listID = new ArrayList<Integer>();//存测试集id
		List<String> listTFIDF = new ArrayList<String>();//存tfidf最高的n个词
		List<String> listFrequency = new ArrayList<String>();//存词频最高的n个词（如果同样高，则取现出现的）
		
		while(rs.next()){
			int id = (category == MyStatic.Category_YouJi) ? rs.getInt(MyStatic.KEY_ID_rawYouJi) : rs.getInt(MyStatic.KEY_ID_rawDianPing);
			listID.add(id);//存id
			
			String keywordsLine = rs.getString(MyStatic.KEY_Keyword);
			listWord.add(keywordsLine);//存关键词
			
			String[] keywords = keywordsLine.split(",");
			List<String> listKeys = new ArrayList<String>();
			listKeys = Arrays.asList(keywords);
			U.print(listKeys.toString());
			
			//获取每个词的特征
			String stats = DBFunction.getFeature(category, id);
			Map<String, Vector<String>> map = U.string2Map(stats);
			
			String topWords = getTopWords(map, 5, MyStatic.Index_TFIDF);
			listTFIDF.add(topWords);//存topTFIDF
			map = U.string2Map(stats);//好怪，map传参数过去就被改变了？！没有复制一个map的副本？！(再次实验发现没问题？！)
			
			listFrequency.add(getTopWords(map, 5, MyStatic.Index_WordFrequency));//存top词频
			map = U.string2Map(stats);
			
			//第一遍遍历，仅收录关键词，并统计关键词数量
			int countKeys = 0;
			for(String key : map.keySet()){
				//仅保留形容词之前的词
				if(U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)) > 7) continue;
				//仅收录关键词
				if(!listKeys.contains(key)) continue;
				listX.add(getListx(category, map.get(key)));//存X
				listY.add(1);//存Y
				countKeys ++;
			}
			U.print(countKeys + "");
			
			//第二遍遍历，仅收录同数量的非关键词
			int mapSize = map.size();
			String[] temp = {"test", "test"};
			String[] s2 = map.keySet().toArray(temp);
			int randomCount = 0;//计算随机了几次，超过一定次数就跳出（可能出现非关键词<关键词数的情况，造成while无法跳出）
			while(countKeys > 0){
				if(randomCount++ > 10) break;
				Random r = new Random();
				int random = (int)r.nextInt(mapSize);
				//仅保留形容词之前的词
				if(U.wordCharacters2Int(map.get(s2[random]).get(MyStatic.Index_WordCharacteristic)) > 7)
					continue;
				//仅收录非关键词
				if(!listKeys.contains(s2[random])){
					listX.add(getListx(category, map.get(s2[random])));
					listY.add(0);
					countKeys --;
				}
			}
		}
		
		FileFunction.writeTrainingSetX(category, listX);
		FileFunction.writeTrainingSetY(category, listY);
		FileFunction.writeTrainingSetWord(category, listWord);
		FileFunction.writeTrainingSetID(category, listID);
		FileFunction.writeTrainingSetTopTFIDF(category, listTFIDF);
		FileFunction.writeTrainingSetTopFrequency(category, listFrequency);
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
	public static void DataMiddle2Txt(int category) throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromMiddle(category);
		while(rs.next()){
			int id = -1;
			if(category == MyStatic.Category_YouJi)
				id = rs.getInt(MyStatic.KEY_ID_rawYouJi);
			else if(category == MyStatic.Category_DianPing)
				id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String stats = rs.getString(MyStatic.KEY_Stats);
			Map<String, Vector<String>> map = U.string2Map(stats);
			
			List<List<Double>> listX = new ArrayList<List<Double>>();//存词特征
			List<String> listW = new ArrayList<String>();//存词名
			for(String key : map.keySet()){
				//仅保留形容词之前的词
				if(U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)) > 7)
					continue;
				//存X
				listX.add(getListx(category, map.get(key)));
				//存词
				listW.add(key);
				
			}
			FileFunction.writeEveryMiddleFeature(category, listX, id);
			FileFunction.writeEveryMiddleWord(category, listW, id);
		}
		U.print("middle中的数值与词名已输出到txt,类型为" + category);
	}
	
	//确定选用哪些特征
	private static List<Double> getListx(int category, Vector<String> vector){
		List<Double> listx = new ArrayList<Double>();//存单个词特征
		if(category == MyStatic.Category_YouJi){
//			listx.add(Double.parseDouble(vector.get(MyStatic.Index_WordCount)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_TFIDF)));
//			listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_FirstWord)));
//			listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_LastWord)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_Absolute)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_WordLength)));
			listx.add((double)U.wordCharacters2Int(vector.get(MyStatic.Index_WordCharacteristic)));
		}
		else if(category == MyStatic.Category_DianPing){
	//		listx.add(Double.parseDouble(vector.get(MyStatic.Index_WordCount)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_TFIDF)));
	//		listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_FirstWord)));
	//		listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_LastWord)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_Absolute)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_WordLength)));
			listx.add((double)U.wordCharacters2Int(vector.get(MyStatic.Index_WordCharacteristic)));
		}
		return listx;
	}
}
