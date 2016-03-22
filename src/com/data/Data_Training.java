package com.data;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;

public class Data_Training {

	public static void humanIndexing() throws SQLException{
		
		ResultSet rs = DBFunction.getRandomDianPing(50, 35656, 70921);
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			U.print("=================  id:" + id + "  =================");
			U.print("ԭʼ����:" + content);
			Map<String, Vector<String>> map = DBFunction.getDianPingStats(id);
			U.print("��ͳ��:" + map.toString());
			
			if(DBFunction.DianPing_isIndexed(id)) continue;//����Ѿ��������������ٱ���s
			
			//��������Ĺؼ���
			int wordCount = 3;//���ƹؼ�����
			while(true){
				U.print(">>������" + wordCount + "���ʣ��Կո�ָ�(�ǵý�����ֶ���λ����һ�У����������쳣)(����pass��������)");
				Scanner sc = new Scanner(System.in);
				String order = sc.nextLine();
				if(order.equals("pass"))//����pass����������
					break;
				String[] keywords = order.split(" ");
				//��������ʽ
				if(keywords.length != wordCount){
					U.print("�����ʽ����,���ܲ���" + wordCount + "����");
					continue;//�����ʽ��������������
				}
				else{
					//�������Ĵ��Ƿ�Ϊ�ִʽ���еĴ�
					int passCount = 0;//ȫ��ͨ���Ų���Ҫ��������
					for(String word : keywords){
						if(!map.containsKey(word))
							U.print(word + "�����ڷִʽ����");
						else 
							passCount++;
					}
					//�������Ĵ����������ˣ������
					if(passCount == wordCount){
						if(DBFunction.insertTrainingSet(id, keywords) > 0){
							U.print("�������ݿ�ɹ�");
							U.print("=================  done  =================");
						}
						break;
					}
					else
						continue;
				}
			}
		}
		
	}
	
	
	
	//�����Լ��е����������txt�У��Թ�pythonʹ��
	public static void DataTraining2Txt() throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromTrainingSet();
		List<Integer> listY = new ArrayList<Integer>();//���Ƿ��ǹؼ���
		List<List<Double>> listX = new ArrayList<List<Double>>();//�����д�����
		
		while(rs.next()){
			String[] keywords = rs.getString(MyStatic.KEY_Keyword).split(",");
			List<String> listKeys = new ArrayList<String>();
			listKeys = Arrays.asList(keywords);
			 
			String stats = DBFunction.getFeature(rs.getInt(MyStatic.KEY_ID_rawDianPing));
			Map<String, Vector<String>> map = U.string2Map(stats);
			for(String key : map.keySet()){
				
				//��X
				List<Double> listx = new ArrayList<Double>();//�浥��������
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_TFIDF)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_Position_FirstWord)));
//				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_WordLength)));
				listx.add((double)U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)));
				listX.add(listx);
				
				//��Y
				if(listKeys.contains(key))
					listY.add(1);
				else
					listY.add(0);
			}
		}
		
		FileFunction.writeTrainingSetX(listX);
		FileFunction.writeTrainingSetY(listY);
	}
	//��middle�е����������txt�У��Թ�pythonʹ��(ע��ȷ�������ֶκͲ��Լ���һ��)
	public static void DataMiddle2Txt() throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromMiddle();
		int count = 0;
		while(rs.next()){
			count ++;
			if(count > 100) break;
			
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String stats = rs.getString(MyStatic.KEY_Stats);
			Map<String, Vector<String>> map = U.string2Map(stats);
			
			List<List<Double>> listX = new ArrayList<List<Double>>();//�������
			List<String> listW = new ArrayList<String>();//�����
			for(String key : map.keySet()){
				
				//��X
				List<Double> listx = new ArrayList<Double>();//�浥��������
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_TFIDF)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_Position_FirstWord)));
//				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_WordLength)));
				listx.add((double)U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)));
				listX.add(listx);
				
				//���
				listW.add(key);
				
			}
			FileFunction.writeEveryMiddleFeature(listX, id);
			FileFunction.writeEveryMiddleWord(listW, id);
		}
	}
}
