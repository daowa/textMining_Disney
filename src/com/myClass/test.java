package com.myClass;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.db.FileFunction;

public class test {

	public static void test() throws IOException{
		List<ArrayList<String>> words = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < 5; i++){
			ArrayList<String> word = new ArrayList<String>();
			word.add("ԭʼ");
			word.add("/f");
			word.add("4");
			words.add(word);
		}
		U.print(U.listlist2String(words));
		U.print(words.toString());
		
		FileWriter fwAll = null; 
        fwAll = new FileWriter("E:\\work\\��ʿ��\\output\\test.txt");
        fwAll.write(U.listlist2String(words));
        fwAll.close(); 
		U.print("�����txt���");
		
		String  address = "E:\\work\\��ʿ��\\output\\test.txt";
		List<ArrayList<String>> results = FileFunction.readTxt_Words(address);
		U.print("��txt��ȡ���");
		U.print("��ȡ����ǣ�" + results.toString());
		
		int i = Integer.parseInt(results.get(1).get(2));
		U.print(i + "");
		
		Vector<String> v = new Vector<String>();
		v.setSize(5);
		v.add(1, "1");
		U.print(v.toString());
	}
	
}
