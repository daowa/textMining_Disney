package com.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.data.NLPIR;
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
	public static void writeTrainingSetX(List<List<Double>> listX) throws IOException{
	    FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\trainingX.txt");   
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
	public static void writeTrainingSetY(List<Integer> listY) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\trainingY.txt");
		for(int i = 0; i < listY.size(); i++){
			fw.write(listY.get(i) + "");
			if(i != listY.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("ѵ����Y������");
	}
	//��ѵ�����е�y��ֵ�������������txt
	public static void writeTrainingSetWord(List<String> listWord) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\trainingWord.txt");
		for(int i = 0; i < listWord.size(); i++){
			fw.write(listWord.get(i) + "");
			if(i != listWord.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("ѵ����Word������");
	}
	//��ѵ������id�����txt
	public static void writeTrainingSetID(List<Integer> listID) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\trainingID.txt");
		for(int i = 0; i < listID.size(); i++){
			fw.write(listID.get(i) + "");
			if(i != listID.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("ѵ����ID������");
	}
	//��ѵ����TFIDF��ߵļ����������txt
	public static void writeTrainingSetTopTFIDF(List<String> list) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\trainingTopTFIDF.txt");
		for(int i = 0; i < list.size(); i++){
			fw.write(list.get(i));
			if(i != list.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("ѵ����topTFIDF������");
	}
	//��ѵ������Ƶ��ߵļ����������txt
		public static void writeTrainingSetTopFrequency(List<String> list) throws IOException{
			FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\trainingTopFrequency.txt");
			for(int i = 0; i < list.size(); i++){
				fw.write(list.get(i));
				if(i != list.size()-1)
					fw.write("\r\n");
			}
			fw.close();
			U.print("ѵ����topFrequency������");
		}
	
	//���middle������ֵ
	public static void writeEveryMiddleFeature(List<List<Double>> listX, int id) throws IOException{
	    FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\��ƪ����ֵ\\" + id + ".txt");   
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
	public static void writeEveryMiddleWord(List<String> listW, int id) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\��ƪ����ֵ����Ӧ�Ĵ�\\" + id + ".txt");   
	    for(int i = 0; i < listW.size(); i++) {
	    	fw.write(listW.get(i) + "");
	    	if(i != listW.size()-1)
	    		fw.write("\r\n");
	    }   
	    fw.close(); 
	    U.print("idΪ" + id + "�ļ�¼ԭ��������");
	}
}
