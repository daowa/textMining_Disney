package com.data;

import java.io.File;
import java.util.List;

import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;

public class Data_PreProcessing {

	public static void readRawTextAndInsertDB(int category, String categoryAddress){
		
		File file = new File(categoryAddress);
		String[] webList = file.list();
		String[] cityList = {MyStatic.City_HongKong, MyStatic.City_California, MyStatic.City_Orlando, MyStatic.City_Paris, MyStatic.City_Tokyo};
			
//			for(int i = 0; i < webList.length; i++){
//				for(int j = 0; j < cityList.length; j++){
//					String fileAddress = categoryAddress + "\\" + webList[i] + "\\" + cityList[j];
//					List<List<String>> lists = FileFunction.readTxtInFile(fileAddress);
//					DBFunction.insertBatch(category, lists, webList[i], cityList[j]);
//				}
//			}
			
		for(int i = 0; i < webList.length; i++){
			for(int j = 0; j < cityList.length; j++){
				String fileAddress = categoryAddress + "\\" + webList[i] + "\\" + cityList[j];
				System.out.println(webList[i]);
				List<List<String>> lists = FileFunction.readTxtInFile(fileAddress);
				DBFunction.insertBatch(category, lists, webList[i], cityList[j]);
			}
		}
	}
	
}
