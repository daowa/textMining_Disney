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
//		Data_PreProcessing.getNewWordFrequency();//�����ֵ��´ʰ��մ�Ƶ���򵼳���txt
//		Data_PreProcessing.addUserDicFromTxt();//����û��ʵ�
		
		//�μ���������
//		Data_PreProcessing.youji_deleteNoDisney();//�������ʿ���޹ص��μ�
//		Data_PreProcessing.youji_resetCity();//�������Щ�μǵĳ��зŴ��ˣ�����+�˹����·���
//		Data_PreProcessing.ZHConverter(MyStatic.Category_YouJi);//���μ��еķ�����תΪ������
//		Data_PreProcessing.deleteTimeAndOthers(MyStatic.Category_YouJi);//���μ���һЩʱ�䡢�����ȥ��
//		Data_PreProcessing.synonym(MyStatic.Category_YouJi);//�μ�ͬ��ʹ鲢
		
		//������������
//		Data_PreProcessing.ZHConverter(MyStatic.Category_DianPing);//�������еķ�����תΪ������
//		Data_PreProcessing.deleteTimeAndOthers(MyStatic.Category_DianPing);//��������һЩʱ�䡢�����ȥ��
//		Data_PreProcessing.synonym(MyStatic.Category_DianPing);//����ͬ��ʹ鲢
		
		//�μ����ݴ���
//		Data_Segmentation.WordFrequency(MyStatic.Category_YouJi);//��ȡÿƪ�μǵĴ�Ƶ���ܵĴ�Ƶ
//		Data_Segmentation.WordFeature(MyStatic.Category_YouJi);//���μǷִʡ��ֽ�ά���洢�������������м����ݿ�
		
		//�������ݴ���
//		Data_Segmentation.WordFrequency(MyStatic.Category_DianPing);//��ȡÿƪ�����Ĵ�Ƶ���ܵĴ�Ƶ
//		Data_Segmentation.WordFeature(MyStatic.Category_DianPing);//�������ִʡ��ֽ�ά���洢�������������м����ݿ�
		
		//�˹�����ѵ��������һ���������μǻ��ǵ������ڶ������������ؼ�������
//		Data_Training.humanIndexing(MyStatic.Category_YouJi, 10);
//		Data_PreProcessing.trainingSet_synonym(MyStatic.Category_YouJi);//�������ã����ܾ��Ҳ���ԭ���ʵ������ˣ�����Ҫ���ͬ��ʹ鲢ͬʱ��
		
		//��ѵ���������txt���Ա���pythonʹ��
		//����������ʱ��Ӧ�����������и���
//		Data_Training.DataTraining2Txt(MyStatic.Category_YouJi);//����μǵ�ѵ����
//		Data_Training.DataMiddle2Txt(MyStatic.Category_YouJi);//����μǵ�middle
		
//		Data_Training.DataTraining2Txt(MyStatic.Category_DianPing);//���������ѵ����
//		Data_Training.DataMiddle2Txt(MyStatic.Category_DianPing);//���������middle
		
		
		//�������ݷ���
//		Data_Analysis.outputKeyWordCounts();//����ؼ��ʵĴ�Ƶͳ�Ƶ���Ϣ
//		Data_Analysis.outputGraphTxt(90);//����ؼ�������ͼ��.net��
//		Data_Analysis.outputAddressKeywordCount();//�������������ʿ��Ĺؼ��ʴ�Ƶͳ����Ϣ
//		Data_Analysis.outputMatrix(50);//����ؼ��ʾ����ı�-�ؼ��ʣ�����ÿ��id����Щ���ϳ��ֹ�
//		Data_Analysis.outputMatrix2();//����ؼ��ʾ���(�ؼ���-�ı�������ÿ��������Щ�����ϳ��ֹ�
//		Data_Analysis.outputMatrix_ShiWu();//����ؼ��ʾ����������ͣ���id-�ؼ���)
		
//		Data_Analysis.outputMonthKeywordCount();//��������·ݵ�ʿ��Ĺؼ��ʴ�Ƶͳ����Ϣ
//		Data_Analysis.outputMonthKeywordTFIDF();//��������·ݹؼ���"TFIDF"
//		Data_Analysis.outputMonthKeywordVariance();//��������ؼ����ڸ����·ݵķ�������� ����/ƽ��)
//		Data_Analysis.outputMonthKeyword_JustTopVariance(200, 0.3);//���߷���ʵ�ÿ�´�Ƶ���������һ��������topN���ڶ���������ƽ��ֵ����ֵ
//		Data_Analysis.outputRadar();//����״�ͼ����Ҫ�����ݣ��Ծ������ǣ�
//		Data_Analysis.outputRadar2();//����״�ͼ����Ҫ�����ݣ��Գ��ִ������ڵ���ǣ�
//		Data_Analysis.outputKeyowordCluster();//���ÿ���ؼ��ʶ�Ӧ���ĸ���
		
		
//		test.test();
	}
	
}
