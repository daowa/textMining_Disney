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
			word.add("原始");
			word.add("/f");
			word.add("4");
			words.add(word);
		}
		U.print(U.listlist2String(words));
		U.print(words.toString());
		
		FileWriter fwAll = null; 
        fwAll = new FileWriter("E:\\work\\迪士尼\\output\\test.txt");
        fwAll.write(U.listlist2String(words));
        fwAll.close(); 
		U.print("输出到txt完成");
		
		String  address = "E:\\work\\迪士尼\\output\\test.txt";
		List<ArrayList<String>> results = FileFunction.readTxt_Words(address);
		U.print("从txt读取完成");
		U.print("读取结果是：" + results.toString());
		
		int i = Integer.parseInt(results.get(1).get(2));
		U.print(i + "");
		
		Vector<String> v = new Vector<String>();
		v.setSize(5);
		v.add(1, "1");
		U.print(v.toString());
	}
	
}
