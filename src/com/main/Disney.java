package com.main;

import java.io.File;
import java.util.List;

import com.data.Data_PreProcessing;
import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;

public class Disney {

	private static String txtAddress = "F:\\资料\\大学\\大四下\\迪士尼论文\\原始资料\\游记\\香港\\携程\\携程-游记-香港35.txt";
	private static String fileAddress = "E:\\work\\迪士尼\\原始文本数据\\点评\\蚂蜂窝\\加州";
	private static String categoryAddress_youji = "E:\\work\\迪士尼\\原始文本数据\\游记";
	private static String categoryAddress_dianping = "E:\\work\\迪士尼\\原始文本数据\\点评";
	private static String categoryAddress_wenda = "E:\\work\\迪士尼\\原始文本数据\\问答";
	
	public static void main(String[] args){
		
//		List<String> list = FileFunction.readTxt(txtAddress);
//		DBFunction.insertYouji(list, MyStatic.WebFrom_XieCheng);
		
//		List<List<String>> lists = FileFunction.readTxtInFile(fileAddress);
//		DBFunction.insertBatch(MyStatic.Category_DianPing, lists, MyStatic.WebFrom_MaFengWo, MyStatic.City_HongKong);
		
//		Data_PreProcessing.readRawTextAndInsertDB(MyStatic.Category_YouJi, categoryAddress_youji);
//		Data_PreProcessing.readRawTextAndInsertDB(MyStatic.Category_DianPing, categoryAddress_dianping);
//		Data_PreProcessing.readRawTextAndInsertDB(MyStatic.Category_WenDa, categoryAddress_wenda);
		
	}
	
}
