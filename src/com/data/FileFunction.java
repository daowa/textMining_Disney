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
			//����Ϊ��λ��ȡ�ؼ���
			for(int i = 0; i < 9; i++){//��9���ֶ�,ǿ�ƶ�ȡ
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
			//����Ϊ��λ��ȡ�ؼ���
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
			//����Ϊ��λ��ȡ�ؼ���
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
			//����Ϊ��λ��ȡ�ؼ���
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
		for(int i = 0; i < fileList.length; i++){//����Ŀ¼���������¶����ı�������
			txtAddress = fileAddress + "\\" + fileList[i];
			lists.add(readTxt(txtAddress));
		}
		System.out.println("��ȡ�ļ�����ɣ�����ȡ" + lists.size() + "ƪ");
		return lists;
	}
	
	//�����´�-�����ļ������еĴ�
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
	//�����´�-����´ʵ�txt
	public static void findNewWord_outputNewWordRaw(List<String> list) throws IOException{
		U.print("��ʼ����´�");
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\vocabulary\\raw_newword.txt");
		for(int i = 0; i < list.size(); i++){
			U.print("�����" + i + "��");
			fw.write(list.get(i));
			if(i != list.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("�´������ raw_newword.txt ���");
	}
	//����raw_newword,���������е�list
	public static List<String> findNewWord_getRawNewWord(String txtAddress) throws IOException{
		List<String> list = new ArrayList<String>();
		File file = new File(txtAddress);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String s = "";
		while((s = reader.readLine()) != null){
			list.add(s);
		}
		reader.close();
		U.print("�����ַΪ" + txtAddress + "���´�");
		return list;
	}
	//���´ʵĴ�Ƶͳ�������txt
	public static void findNewWord_outputNewWordFrequency(TreeMap<String, Integer> map) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\vocabulary\\newword_frequency.txt");
		Set<Entry<String, Integer>> set = map.entrySet();
		for(Entry<String, Integer> i : set){
			fw.write(i.getKey() + "\t" + i.getValue());
			fw.write("\r\n");
		}
		fw.close();
		U.print("�´ʴ�Ƶ�������E:\\work\\��ʿ��\\vocabulary\\newword_frequency.txt");
	}
	//��ȡͬ��ʱ�
	public static List<List<String>> getSynonym() throws IOException{
		List<List<String>> synonym = new ArrayList<List<String>>();
		File file = new File("E:\\work\\��ʿ��\\vocabulary\\synonym.txt");
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
	
    //��ѵ�����е�x�����txt
	public static void writeTrainingSetX(int category, List<List<Double>> listX) throws IOException{
		String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\��ʿ��\\output\\ѵ����_�μ�\\trainingX.txt" : "E:\\work\\��ʿ��\\output\\ѵ����_����\\trainingX.txt";
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
	    U.print("ѵ����X������");
	}
	
	//��ѵ�����е�y�����txt
	public static void writeTrainingSetY(int category, List<Integer> listY) throws IOException{
		String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\��ʿ��\\output\\ѵ����_�μ�\\trainingY.txt" : "E:\\work\\��ʿ��\\output\\ѵ����_����\\trainingY.txt";
		FileWriter fw = new FileWriter(address);
		for(int i = 0; i < listY.size(); i++){
			fw.write(listY.get(i) + "");
			if(i != listY.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("ѵ����Y������");
	}
	//��ѵ�����е�y��ֵ�������������txt
	public static void writeTrainingSetWord(int category, List<String> listWord) throws IOException{
		String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\��ʿ��\\output\\ѵ����_�μ�\\trainingWord.txt" : "E:\\work\\��ʿ��\\output\\ѵ����_����\\trainingWord.txt";
		FileWriter fw = new FileWriter(address);
		for(int i = 0; i < listWord.size(); i++){
			fw.write(listWord.get(i) + "");
			if(i != listWord.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("ѵ����Word������");
	}
	//��ѵ������id�����txt
	public static void writeTrainingSetID(int category, List<Integer> listID) throws IOException{
		String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\��ʿ��\\output\\ѵ����_�μ�\\trainingID.txt" : "E:\\work\\��ʿ��\\output\\ѵ����_����\\trainingID.txt";
		FileWriter fw = new FileWriter(address);
		for(int i = 0; i < listID.size(); i++){
			fw.write(listID.get(i) + "");
			if(i != listID.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("ѵ����ID������");
	}
	//��ѵ����TFIDF��ߵļ����������txt
	public static void writeTrainingSetTopTFIDF(int category, List<String> list) throws IOException{
		String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\��ʿ��\\output\\ѵ����_�μ�\\trainingTopTFIDF.txt" : "E:\\work\\��ʿ��\\output\\ѵ����_����\\trainingTopTFIDF.txt";
		FileWriter fw = new FileWriter(address);
		for(int i = 0; i < list.size(); i++){
			fw.write(list.get(i));
			if(i != list.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("ѵ����topTFIDF������");
	}
	//��ѵ������Ƶ��ߵļ����������txt
		public static void writeTrainingSetTopFrequency(int category, List<String> list) throws IOException{
			String address = (category == MyStatic.Category_YouJi) ? "E:\\work\\��ʿ��\\output\\ѵ����_�μ�\\trainingTopFrequency.txt" : "E:\\work\\��ʿ��\\output\\ѵ����_����\\trainingTopFrequency.txt";
			FileWriter fw = new FileWriter(address);
			for(int i = 0; i < list.size(); i++){
				fw.write(list.get(i));
				if(i != list.size()-1)
					fw.write("\r\n");
			}
			fw.close();
			U.print("ѵ����topFrequency������");
		}
	
	//���middle������ֵ
	public static void writeEveryMiddleFeature(int category, List<List<Double>> listX, int id) throws IOException{
		String address = "";
		if(category == MyStatic.Category_YouJi)
			address = "E:\\work\\��ʿ��\\output\\middle\\�μ�_��ƪ����ֵ\\" + id + ".txt";
		else if(category == MyStatic.Category_DianPing)
			address = "E:\\work\\��ʿ��\\output\\middle\\����_��ƪ����ֵ\\" + id + ".txt";
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
	    U.print("idΪ" + id + "�ļ�¼����ֵ������");
	}
	//���middle����ֵ����Ӧ�Ĵ�
	public static void writeEveryMiddleWord(int category, List<String> listW, int id) throws IOException{
		String address = "";
		if(category == MyStatic.Category_YouJi)
			address = "E:\\work\\��ʿ��\\output\\middle\\�μ�_��ƪ����ֵ����Ӧ�Ĵ�\\" + id + ".txt";
		else if(category == MyStatic.Category_DianPing)
			address = "E:\\work\\��ʿ��\\output\\middle\\����_��ƪ����ֵ����Ӧ�Ĵ�\\" + id + ".txt";
		FileWriter fw = new FileWriter(address);   
	    for(int i = 0; i < listW.size(); i++) {
	    	fw.write(listW.get(i) + "");
	    	if(i != listW.size()-1)
	    		fw.write("\r\n");
	    }   
	    fw.close(); 
	    U.print("idΪ" + id + "�ļ�¼ԭ��������");
	}
	
	//���ؼ��ʵĴ�Ƶͳ�������txt
	public static void Keywords_outputKeywordsFrequency(TreeMap<String, Integer> map) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\keywords_frequency.txt");
		Set<Entry<String, Integer>> set = map.entrySet();
		for(Entry<String, Integer> i : set){
			fw.write(i.getKey() + "\t" + i.getValue());
			fw.write("\r\n");
		}
		fw.close();
		U.print("�´ʴ�Ƶ�������E:\\work\\��ʿ��\\output\\keywords_frequency.txt");
	}
	
	//������������ʿ��ؼ��ʵĴ�Ƶͳ�������txt
	public static void Keywords_outputAddressKeywordsFrequency(TreeMap<String, Integer> map, String city, int count) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\��������\\��ͬ����\\frequency_" + city + ".txt");
		Set<Entry<String, Integer>> set = map.entrySet();
		for(Entry<String, Integer> i : set){
			fw.write(i.getKey() + "\t" + (double)i.getValue()/count*100 + "%");
			fw.write("\r\n");
		}
		fw.close();
		U.print("E:\\work\\��ʿ��\\output\\��������\\��ͬ����\\frequency_" + city + ".txt");
	}
	
	//�������·ݵ�ʿ��ؼ��ʵĴ�Ƶͳ�������txt
	public static void Keywords_outputMonthKeywordsFrequency(TreeMap<String, Integer> map, int month, int count) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\frequency_" + month + ".txt");
		Set<Entry<String, Integer>> set = map.entrySet();
		for(Entry<String, Integer> i : set){
			fw.write(i.getKey() + "\t" + (double)i.getValue()/count*100 + "%");
			fw.write("\r\n");
		}
		fw.close();
		U.print("E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\frequency_" + month + ".txt");
	}
	
	//�������·ݵ�ʿ��ؼ��ʵ�"TFIDF"�����txt
	public static void Keywords_outputMonthKeywordsTFIDF(TreeMap<String, Double> sortedMap, int month) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\TFIDF_" + month + ".txt");
		Set<Entry<String, Double>> set = sortedMap.entrySet();
		for(Entry<String, Double> i : set){
			fw.write(i.getKey() + "\t" + (double)i.getValue()*100);
			fw.write("\r\n");
		}
		fw.close();
		U.print("E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\frequency_" + month + ".txt");
	}
	
	//���·�Ϊ�ֽ磬�������ؼ��ʵķ����ƽ��ֵ��������շ���Ӵ�С����
	public static void Keywords_outputMonthKeywordsVarianceAndMeans(TreeMap<String, Double> mapVariance, Map<String, Double> mapMean) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\variance.txt");
		fw.write("�ؼ���" + "\t" + "����/ƽ��ֵ" + "\t" + "ƽ��ֵ");
		fw.write("\r\n");
		Set<Entry<String, Double>> set = mapVariance.entrySet();
		for(Entry<String, Double> i : set){
			fw.write(i.getKey() + "\t" + i.getValue() + "\t" + mapMean.get(i.getKey()));
			fw.write("\r\n");
		}
		fw.close();
		U.print("�������E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\variance.txt");
	}
	
	//���߷������/ƽ��ֵ���ؼ���ÿ�·ݵĴ�Ƶ�����
	public static void Keywords_outputJustTopVariance(List<String> output) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\justTopVariance.txt");
		fw.write("�ؼ���" + "\t" + "1��" + "\t" + "2��" + "\t" + "3��" + "\t" + "4��" + "\t" + "5��" + "\t" + "6��"
				+ "\t" + "7��" + "\t" + "8��" + "\t" + "9��" + "\t" + "10��" + "\t" + "11��" + "\t" + "12��");
		fw.write("\r\n");
		for(int i = 0; i < output.size(); i++){
			fw.write(output.get(i));
			fw.write("\r\n");
		}
		fw.close();
		U.print("�������E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\justTopVariance.txt");
	}
}
