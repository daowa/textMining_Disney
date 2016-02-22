package com.main;

import java.util.List;

import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;

public class Disney {

	private static String txtAddress = "F:\\资料\\大学\\大四下\\迪士尼论文\\原始资料\\游记\\香港\\携程\\携程-游记-香港35.txt";
	private static String fileAddress = "F:\\资料\\大学\\大四下\\迪士尼论文\\原始资料\\游记\\香港\\携程";
	public static void main(String[] args){
		
//		List<String> list = FileFunction.readTxt(txtAddress);
//		DBFunction.insertYouji(list, MyStatic.WebFrom_XieCheng);
		
		List<List<String>> lists = FileFunction.readTxtInFile(fileAddress);
		DBFunction.insertYouji_batch(lists, MyStatic.WebFrom_XieCheng);

	}
	
}
