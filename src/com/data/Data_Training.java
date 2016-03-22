package com.data;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;

public class Data_Training {

	public static void humanIndexing() throws SQLException{
		
		ResultSet rs = DBFunction.getRandomDianPing(50, 35656, 70921);
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
		List<Integer> listY = new ArrayList<Integer>();//存是否是关键词
		List<List<Double>> listX = new ArrayList<List<Double>>();//存所有词特征
		
		while(rs.next()){
			String[] keywords = rs.getString(MyStatic.KEY_Keyword).split(",");
			List<String> listKeys = new ArrayList<String>();
			listKeys = Arrays.asList(keywords);
			 
			String stats = DBFunction.getFeature(rs.getInt(MyStatic.KEY_ID_rawDianPing));
			Map<String, Vector<String>> map = U.string2Map(stats);
			for(String key : map.keySet()){
				
				//存X
				List<Double> listx = new ArrayList<Double>();//存单个词特征
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_TFIDF)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_Position_FirstWord)));
//				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_WordLength)));
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
	}
	//将middle中的内容输出到txt中，以供python使用(注意确保特征字段和测试集的一致)
	public static void DataMiddle2Txt() throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromMiddle();
		int count = 0;
		while(rs.next()){
			count ++;
			if(count > 100) break;
			
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String stats = rs.getString(MyStatic.KEY_Stats);
			Map<String, Vector<String>> map = U.string2Map(stats);
			
			List<List<Double>> listX = new ArrayList<List<Double>>();//存词特征
			List<String> listW = new ArrayList<String>();//存词名
			for(String key : map.keySet()){
				
				//存X
				List<Double> listx = new ArrayList<Double>();//存单个词特征
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_TFIDF)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_Position_FirstWord)));
//				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_WordLength)));
				listx.add((double)U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)));
				listX.add(listx);
				
				//存词
				listW.add(key);
				
			}
			FileFunction.writeEveryMiddleFeature(listX, id);
			FileFunction.writeEveryMiddleWord(listW, id);
		}
	}
}
