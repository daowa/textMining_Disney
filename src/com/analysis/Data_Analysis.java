package com.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.alibaba.fastjson.parser.Keywords;
import com.data.DBFunction;
import com.data.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;
import com.myClass.ValueComparator;
import com.myClass.ValueComparator_Double;

public class Data_Analysis {
	
	//统计出现的关键词数和词频
	public static void outputKeyWordCounts() throws IOException{
		//读取关键词
		List<String> list = FileFunction.readTxt_Keywords("E:\\work\\迪士尼\\output\\keywords.txt");//用readStopWords这个函数来读取
		Map<String, Integer> map = new HashMap<String, Integer>();
		int allCount = 0;
		U.print("开始读取并统计关键词");
		for(int i = 0; i < list.size(); i++){
			String line = list.get(i).trim();
			if(line.equals("") || line.isEmpty()) continue;
			String[] keywords = line.split(",");
			for(String keyword : keywords){
				if(keyword.equals("") || keyword.isEmpty()) continue;
				U.print(keyword);
				allCount ++;
				map.put(keyword, map.get(keyword) == null ? 1 : map.get(keyword)+1);
			}
		}
		U.print("开始排序关键词");
		ValueComparator bvc =  new ValueComparator(map);  
        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
        sortedMap.putAll(map);
        FileFunction.Keywords_outputKeywordsFrequency(sortedMap);
        U.print("共输出" + allCount + "个关键词");
        U.print("关键词输出完毕，共输出" + map.size() + "个词");
	}

	//输出用于绘网络图的txt
	public static void outputGraphTxt(double threshold) throws IOException{
		U.print("开始读取关键词-词频表");
		List<String> listKeywordsFrequency = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\keywords_frequency.txt");//用readStopWords这个函数来读取
		byte[][] matrix = new byte[10000][10000];
		//为每个词赋予id
		int id = 1;//从1开始计数
		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<Integer, String> mapID = new HashMap<Integer, String>();
		for(int i = 0; i < listKeywordsFrequency.size(); i++){
			String line = listKeywordsFrequency.get(i).trim();
			if(line == "" || line.isEmpty()) continue;
			String[] keywordsFrequency = line.split("\t");//形如"排队	2371"
			String keyword = keywordsFrequency[0];
			if(map.get(keyword) == null){
				map.put(keyword, id);
				mapID.put(id, keyword);
				id++;
			}
		}
		//读取关键词
		U.print("开始读取关键词");
		List<String> list = FileFunction.readTxt_Keywords("E:\\work\\迪士尼\\output\\keywords.txt");//用readStopWords这个函数来读取
		//将关系写入矩阵
		for(int i = 0; i < list.size(); i++){
			String line = list.get(i).trim();
			if(line.equals("") || line.isEmpty()) continue;
			String[] keywords = line.split(",");
			//关键词之间两两关系
			for(String keyword : keywords){
				if(keyword.equals("") || keyword.isEmpty()) continue;
				for(int j = 0; j < keywords.length; j ++){
					if(map.get(keyword) == null || map.get(keywords[j]) == null) continue;
					if(keyword.equals(keywords[j])) continue;//遇到自身不画线
					//绘制双向箭头
					matrix[map.get(keyword)][map.get(keywords[j])] += 1;
					matrix[map.get(keywords[j])][map.get(keyword)] += 1;
				}
			}
		}
		//统计哪些id符合条件
		List<Integer> idList = new ArrayList<Integer>();
		for(int i = 1; i <= mapID.size(); i++){
			for(int j = 1; j <= mapID.size(); j++){
				int weight = matrix[i][j];
				if(weight >= threshold && !idList.contains(i)){
					idList.add(i);
				}
			}
		}
		//写入txt
		U.print("开始写入关键词");
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\keywords_graph.net");
		fw.write("*Vertices " + idList.size());
		for(int i = 1; i <= idList.size(); i++){//这里的i是写入txt的新的id，idList.get(i-1)是原来矩阵中的id
			fw.write("\r\n");//为上一行补充换行，避免最后一行也换行了
			fw.write(i + " \"" + mapID.get(idList.get(i-1)) + "\"");
			U.print(i + " \"" + mapID.get(idList.get(i-1)) + "\"");
		}
		fw.write("\r\n");
		fw.write("*Edges");
		U.print("开始写入关键词之间的关系");
		for(int i = 1; i <= idList.size(); i++){
			for(int j = 1; j <= idList.size(); j++){
				int weight = matrix[idList.get(i-1)][idList.get(j-1)];
				if(weight >= threshold){
					fw.write("\r\n");//为上一行补充换行，避免最后一行也换行了
					fw.write(i + " " + j + " " + weight*1.2/threshold);
				}
			}
		}
		fw.close();
		U.print("关键词社会网络图写入完毕");
	}
	
	//输出用于关联分析和聚类的矩阵格式数据
	public static void outputMatrix(int threshold) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\keywords_matrix.txt");
		//将所有关键词读入,并写入txt的标题
		List<String> list_Keyword_Frequency = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\keywords_frequency.txt");
		fw.write("id");
		for(int i = 0; i < list_Keyword_Frequency.size(); i++){
			if(Integer.parseInt(list_Keyword_Frequency.get(i).split("\t")[1]) < threshold) continue;//只采用高于阈值的词，避免特征过多
			fw.write("," + list_Keyword_Frequency.get(i).split("\t")[0]);
		}
		fw.write("\r\n");
		//读入各个id的关键词，并写入txt
		List<String> list_ID_Keyword = DBFunction.getKeyword();
		for(int i = 0; i < list_ID_Keyword.size(); i++){
			String[] id_keywords = list_ID_Keyword.get(i).split("\t");
			if(id_keywords.length < 2) continue;//如果是content为空的情况，则不记录这一条
			U.print(id_keywords[0] + "\t" + id_keywords[1]);
			fw.write(id_keywords[0]);
			for(int j = 0; j < list_Keyword_Frequency.size(); j++){
				if(Integer.parseInt(list_Keyword_Frequency.get(j).split("\t")[1]) < threshold) continue;//只采用高于阈值的词，避免特征过多
				fw.write(",");
				String[] keywords = id_keywords[1].split(",");
				List<String> listKeywords = Arrays.asList(keywords);
				if(listKeywords.contains(list_Keyword_Frequency.get(j).split("\t")[0]))
					fw.write("1");
				else
					fw.write("0");
			}
			fw.write("\r\n");
		}
		fw.close();
		U.print("已输出到E:\\work\\迪士尼\\output\\keywords_matrix.txt");
	}
	//输出用于MDS分析的矩阵格式数据
	public static void outputMatrix2() throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\keywords_matrix2.txt");
		//将所有关键词读入,并写入txt的标题
		List<String> list_Keyword_Frequency = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\keywords_clusterTop.txt");
		fw.write("id");
		for(int i = 0; i < list_Keyword_Frequency.size(); i++){
			fw.write("," + list_Keyword_Frequency.get(i).split("\t")[0]);
		}
		fw.write("\r\n");
		//读入各个id的关键词，并写入txt
		List<String> list_ID_Keyword = DBFunction.getKeyword();
		for(int i = 0; i < list_ID_Keyword.size(); i++){
			String[] id_keywords = list_ID_Keyword.get(i).split("\t");
			if(id_keywords.length < 2) continue;//如果是content为空的情况，则不记录这一条
			U.print(id_keywords[0] + "\t" + id_keywords[1]);
			fw.write(id_keywords[0]);
			for(int j = 0; j < list_Keyword_Frequency.size(); j++){
				fw.write(",");
				String[] keywords = id_keywords[1].split(",");
				List<String> listKeywords = Arrays.asList(keywords);
				if(listKeywords.contains(list_Keyword_Frequency.get(j).split("\t")[0]))
					fw.write("1");
				else
					fw.write("0");
			}
			fw.write("\r\n");
		}
		fw.close();
		U.print("已输出到E:\\work\\迪士尼\\output\\keywords_matrix2.txt");
//		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\keywords_matrix2.txt");
//		//将所有文本读入,并将id写入txt的标题
//		List<String> list_ID_Keyword = DBFunction.getKeyword();
//		fw.write("words");
//		for(int i = 0; i < list_ID_Keyword.size(); i++){
//			fw.write("," + list_ID_Keyword.get(i).split("\t")[0]);
//		}
//		fw.write("\r\n");
//		//读入关键词，并统计每个id是否出现了该关键词
//		List<String> list_Keyword_Frequency = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\keywords_clusterTop.txt");
//		for(int i = 0; i < list_Keyword_Frequency.size(); i++){
//			fw.write(list_Keyword_Frequency.get(i));
//			for(int j = 0; j < list_ID_Keyword.size(); j++){
//				fw.write(",");
//				String[] id_keywords = list_ID_Keyword.get(j).split("\t");
//				if(id_keywords.length < 2){//如果是content为空的情况，则输出0
//					fw.write("0");
//					continue;
//				}
//				String[] keywords = id_keywords[1].split(",");
//				List<String> listKeywords = Arrays.asList(keywords);
//				if(listKeywords.contains(list_Keyword_Frequency.get(i)))
//					fw.write("1");
//				else
//					fw.write("0");
//			}
//			fw.write("\r\n");
//		}
//		fw.close();
//		U.print("已输出到E:\\work\\迪士尼\\output\\keywords_matrix2.txt");
	}
	
	//输出用于关联分析和聚类的矩阵格式数据_事务格式
	public static void outputMatrix_ShiWu() throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\keywords_matrix_shiwu.txt");
		fw.write("id" + "," + "keyword" + "\r\n");
		//读入各个id的关键词，并写入txt
		List<String> list_ID_Keyword = DBFunction.getKeyword();
		for(int i = 0; i < list_ID_Keyword.size(); i++){
			String[] id_keywords = list_ID_Keyword.get(i).split("\t");
			if(id_keywords.length < 2) continue;//如果是content为空的情况，则不记录这一条
			String[] keywords = id_keywords[1].split(",");
			for(int j = 0; j < keywords.length; j++){
				fw.write(id_keywords[0] + "," + keywords[j] + "\r\n");
			}
		}
		fw.close();
		U.print("已输出到E:\\work\\迪士尼\\output\\keywords_matrix_shiwu.txt");
	}
	
	//输出每个迪士尼的关键词，并且词频排序
	public static void outputAddressKeywordCount() throws IOException{
		//读取关键词
		String[] citys= {MyStatic.City_California, MyStatic.City_HongKong, MyStatic.City_Orlando, MyStatic.City_Paris, MyStatic.City_Tokyo};
		int[] cityCounts = {1109, 28542, 1188, 693, 3732};
		for(int iCity = 0; iCity < citys.length ;iCity++){
			String city = citys[iCity];
			List<String> list = DBFunction.getKeywordByAddress(city);
			Map<String, Integer> map = new HashMap<String, Integer>();
			int allCount = 0;
			U.print("开始读取并统计关键词");
			for(int i = 0; i < list.size(); i++){
				String line = list.get(i).trim();
				if(line.equals("") || line.isEmpty()) continue;
				String[] keywords = line.split(",");
				for(String keyword : keywords){
					if(keyword.equals("") || keyword.isEmpty()) continue;
					allCount ++;
					map.put(keyword, map.get(keyword) == null ? 1 : map.get(keyword)+1);
				}
			}
			U.print("开始排序关键词");
			ValueComparator bvc =  new ValueComparator(map);  
	        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
	        sortedMap.putAll(map);
	        FileFunction.Keywords_outputAddressKeywordsFrequency(sortedMap, city, cityCounts[iCity]);
	        U.print("共输出" + allCount + "个关键词");
	        U.print("关键词输出完毕，共输出" + city + map.size() + "个词");
	        U.print(MyStatic.Others_Line);
		}
	}
	
	//输出每个月份迪士尼的关键词，并且词频排序
	public static void outputMonthKeywordCount() throws IOException{
		//读取关键词
		for(int iMonth = 1; iMonth <= 12 ;iMonth++){
			List<String> list = DBFunction.getKeywordByMonth(iMonth);
			Map<String, Integer> map = new HashMap<String, Integer>();
			int allCount = 0;
			U.print("开始读取并统计关键词");
			for(int i = 0; i < list.size(); i++){
				String line = list.get(i).trim();
				if(line.equals("") || line.isEmpty()) continue;
				String[] keywords = line.split(",");
				for(String keyword : keywords){
					if(keyword.equals("") || keyword.isEmpty()) continue;
					allCount ++;
					map.put(keyword, map.get(keyword) == null ? 1 : map.get(keyword)+1);
				}
			}
			U.print("开始排序关键词");
			ValueComparator bvc =  new ValueComparator(map);  
	        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
	        sortedMap.putAll(map);
	        FileFunction.Keywords_outputMonthKeywordsFrequency(sortedMap, iMonth, list.size());
	        U.print("共输出" + allCount + "个关键词");
	        U.print("关键词输出完毕，共输出" + iMonth + "月" + map.size() + "个词");
	        U.print(MyStatic.Others_Line);
		}
	}
	
	//采用模仿TFIDF的方式，输出每个月“TFIDF”最高的几个词
	public static void outputMonthKeywordTFIDF() throws IOException{
		//将各月份关键词词频读入map
		Map<String, Double> mapMonth = new HashMap<String, Double>();
		List<List<String>> listlistMonthKeywords = new ArrayList<List<String>>();
		for(int month = 1; month <= 12; month++){
			List<String> list = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\论文数据\\不同月份\\frequency_" + month + ".txt");
			List<String> listKeywords = new ArrayList<String>();
			for(int i = 0; i < list.size(); i++){
				String[] s = list.get(i).trim().split("\t");
				double tf = Double.parseDouble(s[1].replace("%", ""));
				if(tf < 1) continue;//只选取词频高于1的词
				mapMonth.put(s[0] + month, Double.parseDouble(s[1].replace("%", "")));
				listKeywords.add(s[0]);
			}
			listlistMonthKeywords.add(listKeywords);
		}
		//将每个月的tfidf高频词输出
		for(int iMonth = 1; iMonth <= 12 ;iMonth++){
			Map<String, Double> map = new HashMap<String, Double>();
			List<String> listKeywords = listlistMonthKeywords.get(iMonth-1);
			for(int i = 0; i < listKeywords.size(); i++){
				String keyword = listKeywords.get(i);
				double countTF = 0;//将tf归一化
				double countMonth = 0;//计算这个词在各个月份文档中出现了几次
				for(int jMonth = 1; jMonth <= 12; jMonth++){
					if(mapMonth.get(keyword + jMonth) != null)
						countTF += mapMonth.get(keyword + jMonth);
						countMonth += 1;
				}
				map.put(keyword, mapMonth.get(keyword + iMonth)/countTF/countMonth);
			}
			U.print("开始排序关键词TFIDF");
			ValueComparator_Double bvc =  new ValueComparator_Double(map);  
	        TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(bvc);
	        sortedMap.putAll(map);
	        FileFunction.Keywords_outputMonthKeywordsTFIDF(sortedMap, iMonth);
	        U.print("关键词TFIDF输出完毕，共输出" + iMonth + "月" + map.size() + "个词");
	        U.print(MyStatic.Others_Line);
		}
	}
	
	//输出每个关键词按月份分的方差
	public static void outputMonthKeywordVariance() throws IOException{
		//将各月份关键词词频读入map
		Map<String, Double> map = new HashMap<String, Double>();
		for(int month = 1; month <= 12; month++){
			List<String> list = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\论文数据\\不同月份\\frequency_" + month + ".txt");
			for(int i = 0; i < list.size(); i++){
				String[] s = list.get(i).trim().split("\t");
				map.put(s[0] + month, Double.parseDouble(s[1].replace("%", "")));
			}
		}
		//读取所有的词,为每个词统计平均数和方差
		Map<String, Double> mapMean = new HashMap<String, Double>();
		Map<String, Double> mapVariance = new HashMap<String, Double>();
		List<String> list = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\keywords_frequency.txt");
		for(int i = 0; i < list.size(); i++){ //对于每个读取到的词，求方差，放入mapVariance中
			String[] s = list.get(i).trim().split("\t");
			List<Double> listData = new ArrayList<Double>();
			for(int month = 1; month <= 12; month++){
				listData.add(map.get(s[0] + month) != null ? map.get(s[0] + month) : 0);
			}
			mapMean.put(s[0], U.MATH_getAverage(listData));
			mapVariance.put(s[0], U.MATH_getVariance(listData)/U.MATH_getAverage(listData));
		}
		//按照方差排序
		ValueComparator_Double bvc =  new ValueComparator_Double(mapVariance);  
        TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(bvc);
        sortedMap.putAll(mapVariance);
        //输出到txt
        FileFunction.Keywords_outputMonthKeywordsVarianceAndMeans(sortedMap, mapMean);
	}
	
	public static void outputMonthKeyword_JustTopVariance(int topN, double threshold) throws IOException{
		//将各月份关键词词频读入map
		Map<String, Double> map = new HashMap<String, Double>();
		for(int month = 1; month <= 12; month++){
			List<String> list = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\论文数据\\不同月份\\frequency_" + month + ".txt");
			for(int i = 0; i < list.size(); i++){
				String[] s = list.get(i).trim().split("\t");
				map.put(s[0] + month, Double.parseDouble(s[1].replace("%", "")));
			}
		}
		//读取每个月高方差关键词的词数
		List<String> list = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\论文数据\\不同月份\\variance.txt");
		List<String> output = new ArrayList<String>();
		for(int i = 1; i < topN; i++){//不读取第一行,因为第一行是列名
			String[] s = list.get(i).trim().split("\t");
			if(Double.parseDouble(s[2]) < threshold) continue;
			String line = s[0] + "\t";
			for(int month = 1; month <= 12; month++){
				line += ((map.get(s[0] + month) != null ? map.get(s[0] + month) : 0) + "0000").substring(0, 4) + "\t";
			}
			output.add(line);
		}
		//输出到txt
		FileFunction.Keywords_outputJustTopVariance(output);
	}
	
	//输出雷达图所需要的数据（以聚类结果记）
	public static void outputRadar(){
		int[] hongkong = {0, 0, 0, 0, 0};
		int[] tokyo = {0, 0, 0, 0, 0};
		int[] paris = {0, 0, 0, 0, 0};
		int[] california = {0, 0, 0, 0, 0};
		int[] orlando = {0, 0, 0, 0, 0};
		List<String> list = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\cluster.txt");
		for(int i = 0; i < list.size(); i++){
			String[] s = list.get(i).split("\t");
			String city = DBFunction.getCity(Integer.parseInt(s[0]));
			int cluster = Integer.parseInt(s[1].substring(s[1].length()-1, s[1].length())) - 1;
			if(city.equals(MyStatic.City_HongKong))
				hongkong[cluster] ++;
			else if(city.equals(MyStatic.City_Tokyo))
				tokyo[cluster] ++;
			else if(city.equals(MyStatic.City_Paris))
				paris[cluster] ++;
			else if(city.equals(MyStatic.City_California))
				california[cluster] ++;
			else if(city.equals(MyStatic.City_Orlando))
				orlando[cluster] ++;
		}
		double allHongkong = countAll(hongkong);
		double allTokyo = countAll(tokyo);
		double allParis = countAll(paris);
		double allCalifornia = countAll(california);
		double allOrlando = countAll(orlando);
		
		U.print("游乐园\t童话情节\t全家总动员\t票务\t梦幻表演");
		U.print(MyStatic.City_HongKong + ":" + hongkong[1]/allHongkong + "\t" + hongkong[2]/allHongkong + "\t" + hongkong[0]/allHongkong + "\t" + hongkong[4]/allHongkong + "\t" + hongkong[3]/allHongkong);
		U.print(MyStatic.City_Tokyo + ":" + tokyo[1]/allTokyo + "\t" + tokyo[2]/allTokyo + "\t" + tokyo[0]/allTokyo + "\t" + tokyo[4]/allTokyo + "\t" + tokyo[3]/allTokyo);
		U.print(MyStatic.City_Paris + ":" + paris[1]/allParis + "\t" + paris[2]/allParis + "\t" + paris[0]/allParis + "\t" + paris[4]/allParis + "\t" + paris[3]/allParis);
		U.print(MyStatic.City_California + ":" + california[1]/allCalifornia + "\t" + california[2]/allCalifornia + "\t" + california[0]/allCalifornia + "\t" + california[4]/allCalifornia + "\t" + california[3]/allCalifornia);
		U.print(MyStatic.City_Orlando + ":" + orlando[1]/allOrlando + "\t" + orlando[2]/allOrlando + "\t" + orlando[0]/allOrlando + "\t" + orlando[4]/allOrlando + "\t" + orlando[3]/allOrlando);
	}
	//统计数组的和
	private static int countAll(int[] input){
		int all = 0;
		for(int i = 0; i < input.length; i++){
			all += input[i];
		}
		return all;
	}
	//输出雷达图所需要的数据（以出现词所属于的类记）
	public static void outputRadar2(){
		int[] hongkong = getCityWordsCluster(MyStatic.City_HongKong);
		int[] tokyo = getCityWordsCluster(MyStatic.City_Tokyo);
		int[] paris = getCityWordsCluster(MyStatic.City_Paris);
		int[] california = getCityWordsCluster(MyStatic.City_California);
		int[] orlando = getCityWordsCluster(MyStatic.City_Orlando);
		
		double allHongkong = countAll(hongkong);
		double allTokyo = countAll(tokyo);
		double allParis = countAll(paris);
		double allCalifornia = countAll(california);
		double allOrlando = countAll(orlando);
		
		U.print("游乐园\t童话情节\t全家总动员\t票务\t梦幻表演");
		U.print(MyStatic.City_HongKong + ":" + hongkong[1]/allHongkong + "\t" + hongkong[2]/allHongkong + "\t" + hongkong[0]/allHongkong + "\t" + hongkong[4]/allHongkong + "\t" + hongkong[3]/allHongkong);
		U.print(MyStatic.City_Tokyo + ":" + tokyo[1]/allTokyo + "\t" + tokyo[2]/allTokyo + "\t" + tokyo[0]/allTokyo + "\t" + tokyo[4]/allTokyo + "\t" + tokyo[3]/allTokyo);
		U.print(MyStatic.City_Paris + ":" + paris[1]/allParis + "\t" + paris[2]/allParis + "\t" + paris[0]/allParis + "\t" + paris[4]/allParis + "\t" + paris[3]/allParis);
		U.print(MyStatic.City_California + ":" + california[1]/allCalifornia + "\t" + california[2]/allCalifornia + "\t" + california[0]/allCalifornia + "\t" + california[4]/allCalifornia + "\t" + california[3]/allCalifornia);
		U.print(MyStatic.City_Orlando + ":" + orlando[1]/allOrlando + "\t" + orlando[2]/allOrlando + "\t" + orlando[0]/allOrlando + "\t" + orlando[4]/allOrlando + "\t" + orlando[3]/allOrlando);
	}
	private static int[] getCityWordsCluster(String city){
		int result[] = {0, 0, 0, 0, 0};
		//读入两个list
		List<String> list = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\cluster\\compare.txt");
		List<String> listWords = new ArrayList<>();
		Map<String, String> mapWordCluster = new HashMap<>();
		for(int i = 0; i < list.size(); i++){
			String[] ss = list.get(i).split("\t");
			listWords.add(ss[0]);
			mapWordCluster.put(ss[0], ss[1]);
		}
		//开始计算
		List<String> listKeywords = DBFunction.getKeywordByAddress(city);
		for(int i = 0; i < listKeywords.size(); i++){
			String line = listKeywords.get(i).trim();
			if(line.equals("") || line.isEmpty()) continue;
			String[] ss = line.split(",");
			for(String s : ss){
				if(listWords.contains(s)){
					if(mapWordCluster.get(s).equals("游乐园")){
						result[0] ++;
					}
					else if(mapWordCluster.get(s).equals("童话情节")){
						result[1] ++;
					}
					else if(mapWordCluster.get(s).equals("全家总动员")){
						result[2] ++;
					}
					else if(mapWordCluster.get(s).equals("票务")){
						result[3] ++;
					}
					else if(mapWordCluster.get(s).equals("梦幻表演")){
						result[4] ++;
					}
				}
			}
		}
		return result;
	}
	
	//输出每个关键词对应于哪个类
	public static void outputKeyowordCluster() throws IOException{
		List<String> list = FileFunction.readTxt_StopWords("E:\\work\\迪士尼\\output\\keywords_clusterTop.txt");
		Map<String, Double> mapYouLeYuan = getClusterTopKeywordsAndFrequency("E:\\work\\迪士尼\\output\\cluster\\游乐园.txt");
		Map<String, Double> mapTongHuaQingJie = getClusterTopKeywordsAndFrequency("E:\\work\\迪士尼\\output\\cluster\\童话情节.txt");
		Map<String, Double> mapQuanJiaZongDongYuan = getClusterTopKeywordsAndFrequency("E:\\work\\迪士尼\\output\\cluster\\全家总动员.txt");
		Map<String, Double> mapPiaoWu = getClusterTopKeywordsAndFrequency("E:\\work\\迪士尼\\output\\cluster\\票务.txt");
		Map<String, Double> mapMengHuanBiaoYan = getClusterTopKeywordsAndFrequency("E:\\work\\迪士尼\\output\\cluster\\梦幻表演.txt");
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\cluster\\compare.txt");
		for(int i = 0; i < list.size(); i++){
			String word = list.get(i);
			fw.write(word + "\t");
			//找出最大值
			double i0 = mapYouLeYuan.get(word) == null ? 0 : mapYouLeYuan.get(word);
			double i1 = mapTongHuaQingJie.get(word) == null ? 0 : mapTongHuaQingJie.get(word);
			double i2 = mapQuanJiaZongDongYuan.get(word) == null ? 0 : mapQuanJiaZongDongYuan.get(word);
			double i3 = mapPiaoWu.get(word) == null ? 0 : mapPiaoWu.get(word);
			double i4 = mapMengHuanBiaoYan.get(word) == null ? 0 : mapMengHuanBiaoYan.get(word);
			fw.write(getMaxFrequencyWordBelongTo(i0, i1, i2, i3, i4));
			if(i != list.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("done");
	}
	//获取每个类的top30关键词以及其在多少比例文本中出现
	private static Map<String, Double> getClusterTopKeywordsAndFrequency(String address){
		List<String> list = FileFunction.readTxt_StopWords(address);
		Map<String, Double> result = new HashMap<>();
		for(int i = 0; i < list.size(); i++){
			String[] s = list.get(i).split("\t");
			result.put(s[0], Double.parseDouble(s[1]));
		}
		return result;
	}
	private static String getMaxFrequencyWordBelongTo(double i0, double i1, double i2, double i3, double i4){
		double max = -1;
		String cluster = "";
		if(i0 > max){
			max = i0;
			cluster = "游乐园";
		}
		if(i1 > max){
			max = i1;
			cluster = "童话情节";
		}
		if(i2 > max){
			max = i2;
			cluster = "全家总动员";
		}
		if(i3 > max){
			max = i3;
			cluster = "票务";
		}
		if(i4 > max){
			max = i4;
			cluster = "梦幻表演";
		}
		return cluster;
	}
}
