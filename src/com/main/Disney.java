package com.main;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import com.data.Data_PreProcessing;
import com.data.Data_Segmentation;
import com.data.Data_Training;
import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;
import com.myClass.test;

public class Disney {

	private static String txtAddress = "F:\\����\\��ѧ\\������\\��ʿ������\\ԭʼ����\\�μ�\\���\\Я��\\Я��-�μ�-���35.txt";
	private static String fileAddress = "E:\\work\\��ʿ��\\ԭʼ�ı�����\\����\\�����\\����";
	private static String categoryAddress_youji = "E:\\work\\��ʿ��\\ԭʼ�ı�����\\�μ�";
	private static String categoryAddress_dianping = "E:\\work\\��ʿ��\\ԭʼ�ı�����\\����";
	private static String categoryAddress_wenda = "E:\\work\\��ʿ��\\ԭʼ�ı�����\\�ʴ�";
	
	public static void main(String[] args) throws SQLException, IOException{
		
//		��ƪ�������ݿ�
//		List<String> list = FileFunction.readTxt(txtAddress);
//		DBFunction.insertYouji(list, MyStatic.WebFrom_XieCheng);
		
//		�����ļ��в������ݿ�
//		List<List<String>> lists = FileFunction.readTxtInFile(fileAddress);
//		DBFunction.insertBatch(MyStatic.Category_DianPing, lists, MyStatic.WebFrom_MaFengWo, MyStatic.City_HongKong);
		
//		//�������ݿ�
//		Data_PreProcessing.readRawTextAndInsertDB(MyStatic.Category_YouJi, categoryAddress_youji);
//		Data_PreProcessing.readRawTextAndInsertDB(MyStatic.Category_DianPing, categoryAddress_dianping);
//		Data_PreProcessing.readRawTextAndInsertDB(MyStatic.Category_WenDa, categoryAddress_wenda);
		
		//Ԥ������
//		Data_PreProcessing.outputDianPingForFindNewWord();//��������ı���ͨ��nlpir����������´ʣ��˹�ɸѡ�����û��ʵ�
//		Data_PreProcessing.findNewWord();//�����´ʣ��˹�ɸѡ����ӵ��û��ʵ�
		Data_PreProcessing.getNewWordFrequency();
//		Data_PreProcessing.addUserDicFromTxt();//����û��ʵ�
		
		//�μ���������
//		Data_PreProcessing.youji_deleteNoDisney();//�������ʿ���޹ص��μ�
//		Data_PreProcessing.youji_resetCity();//�������Щ�μǵĳ��зŴ��ˣ�����+�˹����·���
		
		//������������
//		Data_PreProcessing.dianping_ZHConverter();//�������еķ�����תΪ������
		
		//�������ݴ���
//		Data_Segmentation.DianPing_WordFrequency();//��ȡÿƪ�Ĵ�Ƶ���ܵĴ�Ƶ
//		Data_Segmentation.DianPing_WordFeature();//�ִʡ��ֽ�ά���洢�������������м����ݿ�
		
		//�˹�����ѵ����
//		Data_Training.humanIndexing();
		
		//��ѵ���������txt���Ա���pythonʹ��
//		Data_Training.DataTraining2Txt();
//		Data_Training.DataMiddle2Txt();
		
//		test.test();
	}
	
}
