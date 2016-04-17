package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.db.FileFunction;
import com.myClass.U;
import com.myClass.ValueComparator;

public class Data_Analysis {
	
	//统计出现的关键词数和词频
	public static void outputKeyWordCounts() throws IOException{
		//读取关键词
		List<String> list = FileFunction.readTxt_Keywords("E:\\work\\迪士尼\\output\\keywords.txt");//用readStopWords这个函数来读取
		Map<String, Integer> map = new HashMap<String, Integer>();
		U.print("开始读取并统计关键词");
		for(int i = 0; i < list.size(); i++){
			String line = list.get(i).trim();
			if(line.equals("") || line.isEmpty()) continue;
			String[] keywords = line.split(",");
			for(String keyword : keywords){
				if(keyword.equals("") || keyword.isEmpty()) continue;
				U.print(keyword);
				map.put(keyword, map.get(keyword) == null ? 1 : map.get(keyword)+1);
			}
		}
		U.print("开始排序关键词");
		ValueComparator bvc =  new ValueComparator(map);  
        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
        sortedMap.putAll(map);
        FileFunction.Keywords_outputKeywordsFrequency(sortedMap);
        U.print("关键词输出完毕，共输出" + map.size() + "个词");
	}

	public static void outputGraphTxt(int threashold) throws IOException{
		U.print("开始读取关键词-词频表");
		List<String> listKeywordsFrequency = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\keywords_frequency.txt");//用readStopWords这个函数来读取
		//为每个词赋予id
		int id = 1;//从1开始计数
		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<Integer, String> mapID = new HashMap<Integer, String>();
		for(int i = 0; i < listKeywordsFrequency.size(); i++){
			String line = listKeywordsFrequency.get(i).trim();
			if(line == "" || line.isEmpty()) continue;
			String[] keywordsFrequency = line.split("\t");//形如"排队	2371"
			if(Integer.parseInt(keywordsFrequency[1]) < threashold) break;
			String keyword = keywordsFrequency[0];
			if(map.get(keyword) == null){
				map.put(keyword, id);
				mapID.put(id, keyword);
				U.print(keyword + "　" + id);
				id++;
			}
		}
		//读取关键词
		U.print("开始读取关键词");
		List<String> list = FileFunction.readTxt_Keywords("E:\\work\\迪士尼\\output\\keywords.txt");//用readStopWords这个函数来读取
		U.print("开始写入关键词");
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\keywords_graph.net");
		fw.write("*Vertices " + map.size());
		for(int i = 0; i < map.size(); i++){
			fw.write("\r\n");//为上一行补充换行，避免最后一行也换行了
			fw.write((i+1) + " \"" + mapID.get(i+1) + "\"");
			U.print("写入词:" + map.get(mapID.get(i+1)));
		}
		fw.write("\r\n");
		fw.write("*Edges");
		U.print("开始写入关键词之间的关系");
		for(int i = 0; i < list.size(); i++){
			String line = list.get(i).trim();
			if(line.equals("") || line.isEmpty()) continue;
			String[] keywords = line.split(",");
			//关键词之间两两关系
			for(String keyword : keywords){
				if(keyword.equals("") || keyword.isEmpty()) continue;
				for(int j = 0; j < keywords.length; j ++){
					if(map.get(keyword) == null || map.get(keywords[j]) == null) continue;//只要其中一个小于阈值，则不输出
					if(keyword.equals(keywords[j])) continue;//遇到自身不画线
					fw.write("\r\n");//为上一行补充换行，避免最后一行也换行了
					fw.write(map.get(keyword) + " " + map.get(keywords[j]));
					U.print(map.get(keyword) + " " + map.get(keywords[j]));
				}
			}
		}
		fw.close();
		U.print("关键词社会网络图写入完毕");
	}
	
}
