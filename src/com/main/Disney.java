package com.main;

import java.util.List;

import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;

public class Disney {

	private static String txtAddress = "F:\\����\\��ѧ\\������\\��ʿ������\\ԭʼ����\\�μ�\\���\\Я��\\Я��-�μ�-���35.txt";
	private static String fileAddress = "F:\\����\\��ѧ\\������\\��ʿ������\\ԭʼ����\\�μ�\\���\\Я��";
	public static void main(String[] args){
		
//		List<String> list = FileFunction.readTxt(txtAddress);
//		DBFunction.insertYouji(list, MyStatic.WebFrom_XieCheng);
		
		List<List<String>> lists = FileFunction.readTxtInFile(fileAddress);
		DBFunction.insertYouji_batch(lists, MyStatic.WebFrom_XieCheng);

	}
	
}
