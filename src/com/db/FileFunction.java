package com.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	public static void findNewWord_outputNewWord2Txt(List<String> list) throws IOException{
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
