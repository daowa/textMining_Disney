package com.data;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;

public class Data_Training {

	public static void humanIndexing() throws SQLException{
		
		ResultSet rs = DBFunction.getRandomDianPing(200, 106844, 142437);
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
		List<List<Double>> listX = new ArrayList<List<Double>>();//�����д�����
		List<Integer> listY = new ArrayList<Integer>();//���Ƿ��ǹؼ���
		List<String> listWord = new ArrayList<String>();//��ؼ���
		List<Integer> listID = new ArrayList<Integer>();//����Լ�id
		List<String> listTFIDF = new ArrayList<String>();//��tfidf��ߵ�n����
		List<String> listFrequency = new ArrayList<String>();//���Ƶ��ߵ�n���ʣ����ͬ���ߣ���ȡ�ֳ��ֵģ�
		
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			listID.add(id);//��id
			
			String keywordsLine = rs.getString(MyStatic.KEY_Keyword);
			listWord.add(keywordsLine);//��ؼ���
			
			String[] keywords = keywordsLine.split(",");
			List<String> listKeys = new ArrayList<String>();
			listKeys = Arrays.asList(keywords);
			
			//��ȡÿ���ʵ�����
			String stats = DBFunction.getFeature(rs.getInt(MyStatic.KEY_ID_rawDianPing));
			Map<String, Vector<String>> map = U.string2Map(stats);
//			U.print(0 + map.toString());
			
			String topWords = getTopWords(map, 3, MyStatic.Index_TFIDF);
			listTFIDF.add(topWords);//��topTFIDF
			U.print(topWords);
//			U.print(1 + map.toString());
//			map = U.string2Map(stats);//�ù֣�map��������ȥ�ͱ��ı��ˣ���û�и���һ��map�ĸ�������(�ٴ�ʵ�鷢��û���⣿��)
//			U.print(2 + map.toString());
			
			listFrequency.add(getTopWords(map, 3, MyStatic.Index_WordFrequency));//��top��Ƶ
//			U.print(3 + map.toString());
//			map = U.string2Map(stats);
//			U.print(4 + map.toString());
			
			for(String key : map.keySet()){
				//���������ݴ�֮ǰ�Ĵ�
				if(U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)) > 7)
					continue;
				//��X
				List<Double> listx = new ArrayList<Double>();//�浥��������
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_TFIDF)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_Position_FirstWord)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_WordLength)));
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
		FileFunction.writeTrainingSetWord(listWord);
		FileFunction.writeTrainingSetID(listID);
		FileFunction.writeTrainingSetTopTFIDF(listTFIDF);
		FileFunction.writeTrainingSetTopFrequency(listFrequency);
	}
	//��ȡ���Ƶ��n���ʣ�tfidf���Ƶ�������ڶԱȷ�������tf-idf�ȷ�����Ч��
	private static String getTopWords(Map<String, Vector<String>> map, int topN, int topWhat){
		//�ҳ���ߵ�topN����
		//������ֱ��ɨ���ȥ��
		String result = "";
		for(int i = 0; i < topN; i++){
			double maxWhat = 0;
			String maxKey = "";
			for(String key : map.keySet()){
				if(Double.parseDouble(map.get(key).get(topWhat)) > maxWhat){//�����ڲ��У�Ҳ����˵�ȳ��ֵĴ�������
					maxWhat = Double.parseDouble(map.get(key).get(topWhat));
					maxKey = key;
				}
			}
			map.remove(maxKey);
			result += maxKey + ",";
		}
		result = result.substring(0, result.length()-1);
		return result;
	}
	
	//��middle�е����������txt�У��Թ�pythonʹ��(ע��ȷ�������ֶκͲ��Լ���һ��)
	public static void DataMiddle2Txt() throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromMiddle();
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String stats = rs.getString(MyStatic.KEY_Stats);
			Map<String, Vector<String>> map = U.string2Map(stats);
			
			List<List<Double>> listX = new ArrayList<List<Double>>();//�������
			List<String> listW = new ArrayList<String>();//�����
			for(String key : map.keySet()){
				
				//���������ݴ�֮ǰ�Ĵ�
				if(U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)) > 7)
					continue;
				
				//��X
				List<Double> listx = new ArrayList<Double>();//�浥��������
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_TFIDF)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_Position_FirstWord)));
				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_WordLength)));
				listx.add((double)U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)));
				listX.add(listx);
				
				//���
				listW.add(key);
				
			}
			FileFunction.writeEveryMiddleFeature(listX, id);
			FileFunction.writeEveryMiddleWord(listW, id);
		}
		U.print("middle�е���ֵ������������txt");
	}
}
