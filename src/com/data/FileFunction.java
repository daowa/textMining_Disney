package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.analysis.NLPIR;
import com.myClass.MyStatic;
import com.myClass.U;

public class FileFunction {

	public static List<String> readTxt(String txtAddress){
		List<String> list = new ArrayList<String>();
		File file = new File(txtAddress);
		BufferedReader reader = null;
//		System.out.println("start-readTxt:");
		try {
			reader = new BufferedReader(new FileReader(file));
			String temp = "";
			//以行为单位读取关键词
			for(int i = 0; i < 9; i++){//共9个字段,强制读取
				String s = reader.readLine();
//				if(s.isEmpty()){
//					System.out.println("!!!!!!!!!!");
//				}
				if(s == null){s = "";};
				temp = ((!s.isEmpty()) ? s : "");
				list.add(temp);
//				System.out.println("	line " + i + ": " + temp);s
			}
			reader.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(reader != null)
					reader.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static List<String> readTxt_StopWords(String txtAddress){
		List<String> list = new ArrayList<String>();
		File file = new File(txtAddress);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			//以行为单位读取关键词
			String s = "";
			while((s = reader.readLine()) != null){
				s = s.trim();
				if(!s.isEmpty() && s != "")
					list.add(s);
			}
			reader.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(reader != null)
					reader.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static List<String> readTxt_Keywords(String txtAddress) throws FileNotFoundException{
		List<String> list = new ArrayList<String>();
		File file = new File(txtAddress);
		FileInputStream in = new FileInputStream(file);  
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			//以行为单位读取关键词
			String s = "";
			while((s = reader.readLine()) != null){
				s = s.trim();
				if(!s.isEmpty() && s != "")
					list.add(s);
			}
			reader.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(reader != null)
					reader.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static List<ArrayList<String>> readTxt_Words(String txtAddress){
		List<ArrayList<String>> words = new ArrayList<ArrayList<String>>();
		File file = new File(txtAddress);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			//以行为单位读取关键词
			String s = reader.readLine();
			words = U.string2ListList(s);
			reader.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(reader != null)
					reader.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return words;
	}
	
	public static List<List<String>> readTxtInFile(String fileAddress){
		File file = new File(fileAddress);
		String[] fileList = file.list();
		String txtAddress = "";
		List<List<String>> lists = new ArrayList<List<String>>();
		for(int i = 0; i < fileList.length; i++){//将该目录下所有文章读入文本数组中
			txtAddress = fileAddress + "\\" + fileList[i];
			lists.add(readTxt(txtAddress));
		}
		System.out.println("读取文件夹完成，共读取" + lists.size() + "篇");
		return lists;
	}
	
	//发现新词-读入文件集合中的词
	public static String findNewWord_getDianPingSetString(String txtAddress) throws IOException{
		File file = new File(txtAddress);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String all = "";
		String one = "";
		while((one = reader.readLine()) != null){
			one.replace("\r", "");
			one.replace("\n", "");
			all += one.trim();
		}
		reader.close();
		return all;
	}
	//发现新词-输出新词到txt
	public static void findNewWord_outputNewWordRaw(List<String> list) throws IOException{
		U.print("开始输出新词");
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\vocabulary\\raw_newword.txt");
		for(int i = 0; i < list.size(); i++){
			U.print("输出第" + i + "行");
			fw.write(list.get(i));
			if(i != list.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("新词输出到 raw_newword.txt 完成");
	}
	//读入raw_newword,返回所有行的list
	public static List<String> findNewWord_getRawNewWord(String txtAddress) throws IOException{
		List<String> list = new ArrayList<String>();
		File file = new File(txtAddress);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String s = "";
		while((s = reader.readLine()) != null){
			list.add(s);
		}
		reader.close();
		U.print("读入地址为" + txtAddress + "的新词");
		return list;
	}
	//将新词的词频统计输出到txt
	public static void findNewWord_outputNewWordFrequency(TreeMap<String, Integer> map) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\vocabulary\\newword_frequency.txt");
		Set<Entry<String, Integer>> set = map.entrySet();
		for(Entry<String, Integer> i : set){
			fw.write(i.getKey() + "\t" + i.getValue());
			fw.write("\r\n");
		}
		fw.close();
		U.print("新词词频已输出到E:\\work\\迪士尼\\vocabulary\\newword_frequency.txt");
	}
	//读取同义词表
	public static List<List<String>> getSynonym() throws IOException{
		List<List<String>> synonym = new ArrayList<List<String>>();
		File file = new File("E:\\work\\迪士尼\\vocabulary\\synonym.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		while((line = reader.readLine()) != null){
			line = line.trim();
			String s[] = line.split(",");
			List<String> list = Arrays.asList(s);
			synonym.add(list);
		}
		return synonym;
	}
	
    //将训练集中的x输出到txt
	public static void writeTrainingSetX(int category, List<List<Double>> listX) throws IOException{
		String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\迪士尼\\output\\训练集_游记\\trainingX.txt" : "E:\\work\\迪士尼\\output\\训练集_点评\\trainingX.txt";
	    FileWriter fw = new FileWriter(address);   
	    for(int i = 0; i < listX.size(); i++) {
	    	for(int j = 0; j < listX.get(i).size(); j++){
	    		fw.write(listX.get(i).get(j) + "");
	    		if(j != listX.get(i).size()-1)
	    			fw.write(",");
	    	}
	    	if(i != listX.size()-1)
	    		fw.write("\r\n");
	    }   
	    fw.close(); 
	    U.print("训练集X输出完成");
	}
	
	//将训练集中的y输出到txt
	public static void writeTrainingSetY(int category, List<Integer> listY) throws IOException{
		String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\迪士尼\\output\\训练集_游记\\trainingY.txt" : "E:\\work\\迪士尼\\output\\训练集_点评\\trainingY.txt";
		FileWriter fw = new FileWriter(address);
		for(int i = 0; i < listY.size(); i++){
			fw.write(listY.get(i) + "");
			if(i != listY.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("训练集Y输出完成");
	}
	//将训练集中的y的值（词名）输出到txt
	public static void writeTrainingSetWord(int category, List<String> listWord) throws IOException{
		String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\迪士尼\\output\\训练集_游记\\trainingWord.txt" : "E:\\work\\迪士尼\\output\\训练集_点评\\trainingWord.txt";
		FileWriter fw = new FileWriter(address);
		for(int i = 0; i < listWord.size(); i++){
			fw.write(listWord.get(i) + "");
			if(i != listWord.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("训练集Word输出完成");
	}
	//将训练集的id输出到txt
	public static void writeTrainingSetID(int category, List<Integer> listID) throws IOException{
		String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\迪士尼\\output\\训练集_游记\\trainingID.txt" : "E:\\work\\迪士尼\\output\\训练集_点评\\trainingID.txt";
		FileWriter fw = new FileWriter(address);
		for(int i = 0; i < listID.size(); i++){
			fw.write(listID.get(i) + "");
			if(i != listID.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("训练集ID输出完成");
	}
	//将训练集TFIDF最高的几个词输出到txt
	public static void writeTrainingSetTopTFIDF(int category, List<String> list) throws IOException{
		String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\迪士尼\\output\\训练集_游记\\trainingTopTFIDF.txt" : "E:\\work\\迪士尼\\output\\训练集_点评\\trainingTopTFIDF.txt";
		FileWriter fw = new FileWriter(address);
		for(int i = 0; i < list.size(); i++){
			fw.write(list.get(i));
			if(i != list.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("训练集topTFIDF输出完成");
	}
	//将训练集词频最高的几个词输出到txt
		public static void writeTrainingSetTopFrequency(int category, List<String> list) throws IOException{
			String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\迪士尼\\output\\训练集_游记\\trainingTopFrequency.txt" : "E:\\work\\迪士尼\\output\\训练集_点评\\trainingTopFrequency.txt";
			FileWriter fw = new FileWriter(address);
			for(int i = 0; i < list.size(); i++){
				fw.write(list.get(i));
				if(i != list.size()-1)
					fw.write("\r\n");
			}
			fw.close();
			U.print("训练集topFrequency输出完成");
		}
	
	//输出middle的特征值
	public static void writeEveryMiddleFeature(int category, List<List<Double>> listX, int id) throws IOException{
		String address = "";
		if(category == MyStatic.Category_YouJi)
			address = "E:\\work\\迪士尼\\output\\middle\\游记_单篇特征值\\" + id + ".txt";
		else if(category == MyStatic.Category_DianPing)
			address = "E:\\work\\迪士尼\\output\\middle\\点评_单篇特征值\\" + id + ".txt";
	    FileWriter fw = new FileWriter(address);   
	    for(int i = 0; i < listX.size(); i++) {
	    	for(int j = 0; j < listX.get(i).size(); j++){
	    		fw.write(listX.get(i).get(j) + "");
	    		if(j != listX.get(i).size()-1)
	    			fw.write(",");
	    	}
	    	if(i != listX.size()-1)
	    		fw.write("\r\n");
	    }   
	    fw.close(); 
	    U.print("id为" + id + "的记录特征值输出完成");
	}
	//输出middle特征值所对应的词
	public static void writeEveryMiddleWord(int category, List<String> listW, int id) throws IOException{
		String address = "";
		if(category == MyStatic.Category_YouJi)
			address = "E:\\work\\迪士尼\\output\\middle\\游记_单篇特征值所对应的词\\" + id + ".txt";
		else if(category == MyStatic.Category_DianPing)
			address = "E:\\work\\迪士尼\\output\\middle\\点评_单篇特征值所对应的词\\" + id + ".txt";
		FileWriter fw = new FileWriter(address);   
	    for(int i = 0; i < listW.size(); i++) {
	    	fw.write(listW.get(i) + "");
	    	if(i != listW.size()-1)
	    		fw.write("\r\n");
	    }   
	    fw.close(); 
	    U.print("id为" + id + "的记录原词输出完成");
	}
	
	//将关键词的词频统计输出到txt
	public static void Keywords_outputKeywordsFrequency(TreeMap<String, Integer> map) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\keywords_frequency.txt");
		Set<Entry<String, Integer>> set = map.entrySet();
		for(Entry<String, Integer> i : set){
			fw.write(i.getKey() + "\t" + i.getValue());
			fw.write("\r\n");
		}
		fw.close();
		U.print("新词词频已输出到E:\\work\\迪士尼\\output\\keywords_frequency.txt");
	}
	
	//将各个地区迪士尼关键词的词频统计输出到txt
	public static void Keywords_outputAddressKeywordsFrequency(TreeMap<String, Integer> map, String city, int count) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\论文数据\\不同地区\\frequency_" + city + ".txt");
		Set<Entry<String, Integer>> set = map.entrySet();
		for(Entry<String, Integer> i : set){
			fw.write(i.getKey() + "\t" + (double)i.getValue()/count*100 + "%");
			fw.write("\r\n");
		}
		fw.close();
		U.print("E:\\work\\迪士尼\\output\\论文数据\\不同地区\\frequency_" + city + ".txt");
	}
	
	//将各个月份迪士尼关键词的词频统计输出到txt
	public static void Keywords_outputMonthKeywordsFrequency(TreeMap<String, Integer> map, int month, int count) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\论文数据\\不同月份\\frequency_" + month + ".txt");
		Set<Entry<String, Integer>> set = map.entrySet();
		for(Entry<String, Integer> i : set){
			fw.write(i.getKey() + "\t" + (double)i.getValue()/count*100 + "%");
			fw.write("\r\n");
		}
		fw.close();
		U.print("E:\\work\\迪士尼\\output\\论文数据\\不同月份\\frequency_" + month + ".txt");
	}
	
	//将各个月份迪士尼关键词的"TFIDF"输出到txt
	public static void Keywords_outputMonthKeywordsTFIDF(TreeMap<String, Double> sortedMap, int month) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\论文数据\\不同月份\\TFIDF_" + month + ".txt");
		Set<Entry<String, Double>> set = sortedMap.entrySet();
		for(Entry<String, Double> i : set){
			fw.write(i.getKey() + "\t" + (double)i.getValue()*100);
			fw.write("\r\n");
		}
		fw.close();
		U.print("E:\\work\\迪士尼\\output\\论文数据\\不同月份\\frequency_" + month + ".txt");
	}
	
	//以月份为分界，将各个关键词的方差和平均值输出，按照方差从大到小排列
	public static void Keywords_outputMonthKeywordsVarianceAndMeans(TreeMap<String, Double> mapVariance, Map<String, Double> mapMean) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\论文数据\\不同月份\\variance.txt");
		fw.write("关键词" + "\t" + "方差/平均值" + "\t" + "平均值");
		fw.write("\r\n");
		Set<Entry<String, Double>> set = mapVariance.entrySet();
		for(Entry<String, Double> i : set){
			fw.write(i.getKey() + "\t" + i.getValue() + "\t" + mapMean.get(i.getKey()));
			fw.write("\r\n");
		}
		fw.close();
		U.print("已输出到E:\\work\\迪士尼\\output\\论文数据\\不同月份\\variance.txt");
	}
	
	//将高方差（方差/平均值）关键词每月份的词频数输出
	public static void Keywords_outputJustTopVariance(List<String> output) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\迪士尼\\output\\论文数据\\不同月份\\justTopVariance.txt");
		fw.write("关键词" + "\t" + "1月" + "\t" + "2月" + "\t" + "3月" + "\t" + "4月" + "\t" + "5月" + "\t" + "6月"
				+ "\t" + "7月" + "\t" + "8月" + "\t" + "9月" + "\t" + "10月" + "\t" + "11月" + "\t" + "12月");
		fw.write("\r\n");
		for(int i = 0; i < output.size(); i++){
			fw.write(output.get(i));
			fw.write("\r\n");
		}
		fw.close();
		U.print("已输出到E:\\work\\迪士尼\\output\\论文数据\\不同月份\\justTopVariance.txt");
	}
}
