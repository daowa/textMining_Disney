package com.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
}
