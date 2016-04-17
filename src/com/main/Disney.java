package com.main;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import com.data.Data_Analysis;
import com.data.Data_PreProcessing;
import com.data.Data_Segmentation;
import com.data.Data_Training;
import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;
import com.myClass.test;

public class Disney {

	private static String txtAddress = "F:\\资料\\大学\\大四下\\迪士尼论文\\原始资料\\游记\\香港\\携程\\携程-游记-香港35.txt";
	private static String fileAddress = "E:\\work\\迪士尼\\原始文本数据\\点评\\蚂蜂窝\\加州";
	private static String categoryAddress_youji = "E:\\work\\迪士尼\\原始文本数据\\游记";
	private static String categoryAddress_dianping = "E:\\work\\迪士尼\\原始文本数据\\点评";
	private static String categoryAddress_wenda = "E:\\work\\迪士尼\\原始文本数据\\问答";
	
	public static void main(String[] args) throws SQLException, IOException{
		
//		单篇插入数据库
//		List<String> list = FileFunction.readTxt(txtAddress);
//		DBFunction.insertYouji(list, MyStatic.WebFrom_XieCheng);
		
//		整个文件夹插入数据库
//		List<List<String>> lists = FileFunction.readTxtInFile(fileAddress);
//		DBFunction.insertBatch(MyStatic.Category_DianPing, lists, MyStatic.WebFrom_MaFengWo, MyStatic.City_HongKong);
		
//		//插入数据库
//		Data_PreProcessing.readRawTextAndInsertDB(MyStatic.Category_YouJi, categoryAddress_youji);
//		Data_PreProcessing.readRawTextAndInsertDB(MyStatic.Category_DianPing, categoryAddress_dianping);
//		Data_PreProcessing.readRawTextAndInsertDB(MyStatic.Category_WenDa, categoryAddress_wenda);
		
		//预处理工作
//		Data_PreProcessing.outputDianPingForFindNewWord();//输出点评文本，通过nlpir的软件发现新词，人工筛选加入用户词典
//		Data_PreProcessing.findNewWord();//发现新词，人工筛选并添加到用户词典
//		Data_PreProcessing.getNewWordFrequency();//将发现的新词按照词频排序导出到txt
//		Data_PreProcessing.addUserDicFromTxt();//添加用户词典
		
		//游记数据清理
//		Data_PreProcessing.youji_deleteNoDisney();//清理与迪士尼无关的游记
//		Data_PreProcessing.youji_resetCity();//蚂蜂窝有些游记的城市放错了，规则+人工重新放置
		
		//点评数据清理
//		Data_PreProcessing.dianping_ZHConverter();//将点评中的繁体字转为简体字
//		Data_PreProcessing.dianping_deleteTimeAndOthers();//将点评中一些时间、表情等去除
//		Data_PreProcessing.dianping_synonym();//同义词归并
		
		//点评数据处理
//		Data_Segmentation.DianPing_WordFrequency();//获取每篇的词频和总的词频
//		Data_Segmentation.DianPing_WordFeature();//分词、粗降维、存储词特征、放入中间数据库
		
		//人工标引训练集
//		Data_Training.humanIndexing(MyStatic.Version_HUMINDEX_2, 5);
//		Data_PreProcessing.trainingSet_synonym(MyStatic.Version_HUMINDEX_2);//尽量别用，可能就找不到原来词的特征了；或者要求和同义词归并同时做
		
		//将训练集输出到txt，以便于python使用
		//更换特征的时候应该两个都进行更新
//		Data_Training.DataTraining2Txt(MyStatic.Version_HUMINDEX_2);
//		Data_Training.DataMiddle2Txt();
		
//		Data_Analysis.outputKeyWordCounts();//输出关键词的词频统计等信息
		Data_Analysis.outputGraphTxt(150);//输出关键词网络图（.net）
		
//		test.test();
	}
	
}
