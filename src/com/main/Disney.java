package com.main;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import com.analysis.Data_Analysis;
import com.analysis.Data_PreProcessing;
import com.analysis.Data_Segmentation;
import com.analysis.Data_Training;
import com.data.DBFunction;
import com.data.FileFunction;
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
//		Data_PreProcessing.ZHConverter(MyStatic.Category_YouJi);//将游记中的繁体字转为简体字
//		Data_PreProcessing.deleteTimeAndOthers(MyStatic.Category_YouJi);//将游记中一些时间、表情等去除
//		Data_PreProcessing.synonym(MyStatic.Category_YouJi);//游记同义词归并
		
		//点评数据清理
//		Data_PreProcessing.ZHConverter(MyStatic.Category_DianPing);//将点评中的繁体字转为简体字
//		Data_PreProcessing.deleteTimeAndOthers(MyStatic.Category_DianPing);//将点评中一些时间、表情等去除
//		Data_PreProcessing.synonym(MyStatic.Category_DianPing);//点评同义词归并
		
		//游记数据处理
//		Data_Segmentation.WordFrequency(MyStatic.Category_YouJi);//获取每篇游记的词频和总的词频
//		Data_Segmentation.WordFeature(MyStatic.Category_YouJi);//将游记分词、粗降维、存储词特征、放入中间数据库
		
		//点评数据处理
//		Data_Segmentation.WordFrequency(MyStatic.Category_DianPing);//获取每篇点评的词频和总的词频
//		Data_Segmentation.WordFeature(MyStatic.Category_DianPing);//将点评分词、粗降维、存储词特征、放入中间数据库
		
		//人工标引训练集，第一个参数是游记还是点评，第二个参数是最多关键词数量
//		Data_Training.humanIndexing(MyStatic.Category_YouJi, 10);
//		Data_PreProcessing.trainingSet_synonym(MyStatic.Category_YouJi);//尽量别用，可能就找不到原来词的特征了；或者要求和同义词归并同时做
		
		//将训练集输出到txt，以便于python使用
		//更换特征的时候应该两个都进行更新
//		Data_Training.DataTraining2Txt(MyStatic.Category_YouJi);//输出游记的训练集
//		Data_Training.DataMiddle2Txt(MyStatic.Category_YouJi);//输出游记的middle
		
//		Data_Training.DataTraining2Txt(MyStatic.Category_DianPing);//输出点评的训练集
//		Data_Training.DataMiddle2Txt(MyStatic.Category_DianPing);//输出点评的middle
		
		
		//更多数据分析
//		Data_Analysis.outputKeyWordCounts();//输出关键词的词频统计等信息
//		Data_Analysis.outputGraphTxt(90);//输出关键词网络图（.net）
//		Data_Analysis.outputAddressKeywordCount();//输出各个地区迪士尼的关键词词频统计信息
//		Data_Analysis.outputMatrix(50);//输出关键词矩阵（文本-关键词），即每个id在哪些词上出现过
//		Data_Analysis.outputMatrix2();//输出关键词矩阵(关键词-文本），即每个词在哪些样本上出现过
//		Data_Analysis.outputMatrix_ShiWu();//输出关键词矩阵，事务类型（即id-关键词)
		
//		Data_Analysis.outputMonthKeywordCount();//输出各个月份迪士尼的关键词词频统计信息
//		Data_Analysis.outputMonthKeywordTFIDF();//输出各个月份关键词"TFIDF"
//		Data_Analysis.outputMonthKeywordVariance();//输出各个关键词在各个月份的方差（现在是 方差/平均)
//		Data_Analysis.outputMonthKeyword_JustTopVariance(200, 0.3);//将高方差词的每月词频数输出，第一个参数是topN，第二个参数是平均值的阈值
//		Data_Analysis.outputRadar();//输出雷达图所需要的数据（以聚类结果记）
//		Data_Analysis.outputRadar2();//输出雷达图所需要的数据（以出现词所属于的类记）
//		Data_Analysis.outputKeyowordCluster();//输出每个关键词对应于哪个类
		
		
//		test.test();
	}
	
}
