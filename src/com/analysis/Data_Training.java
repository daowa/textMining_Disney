package com.analysis;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

import com.data.DBFunction;
import com.data.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;

public class Data_Training {

	public static void humanIndexing(int category, int mostWord) throws SQLException{
		ResultSet rs = DBFunction.getRandomContent(category, 200, 1, 6070);
		while(rs.next()){
			int id = (category == MyStatic.Category_YouJi) ? rs.getInt(MyStatic.KEY_ID_rawYouJi) : rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			U.print("=================  id:" + id + "  =================");
			U.print("ԭʼ����:" + content);
			Map<String, Vector<String>> map = DBFunction.getStats(category, id);
//			U.print("��ͳ��:" + map.toString());
			
			if(DBFunction.isIndexed(category, id)) continue;//����Ѿ��������������ٱ���
			
			//��������Ĺؼ���
			while(true){
				U.print(">>����������" + mostWord + "���ʣ��Կո�ָ�(�ǵý�����ֶ���λ����һ�У����������쳣)(����pass��������)");
				Scanner sc = new Scanner(System.in);
				String order = sc.nextLine();
				if(order.equals("pass"))//����pass����������
					break;
				String[] keywords = order.split(" ");
				//��������ʽ
				if(keywords.length > mostWord){
					U.print("�����ʽ����,������" + mostWord + "����");
					continue;//�����ʽ��������������
				}
				else{
					//�������Ĵ��Ƿ�Ϊ�ִʽ���еĴ�
					boolean pass = true;
					for(String word : keywords){
						if(!map.containsKey(word)){
							pass = false;
							U.print(word + "�����ڷִʽ����");
						}
					}
					if(!pass) continue;
					//�������Ĵ����������ˣ������
					if(keywords.length <= mostWord){
						if(DBFunction.insertTrainingSet(category, id, keywords) > 0){
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
	public static void DataTraining2Txt(int category) throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromTrainingSet(category);
		List<List<Double>> listX = new ArrayList<List<Double>>();//�����д�����
		List<Integer> listY = new ArrayList<Integer>();//���Ƿ��ǹؼ���
		List<String> listWord = new ArrayList<String>();//��ؼ���
		List<Integer> listID = new ArrayList<Integer>();//����Լ�id
		List<String> listTFIDF = new ArrayList<String>();//��tfidf��ߵ�n����
		List<String> listFrequency = new ArrayList<String>();//���Ƶ��ߵ�n���ʣ����ͬ���ߣ���ȡ�ֳ��ֵģ�
		
		while(rs.next()){
			int id = (category == MyStatic.Category_YouJi) ? rs.getInt(MyStatic.KEY_ID_rawYouJi) : rs.getInt(MyStatic.KEY_ID_rawDianPing);
			listID.add(id);//��id
			
			String keywordsLine = rs.getString(MyStatic.KEY_Keyword);
			listWord.add(keywordsLine);//��ؼ���
			
			String[] keywords = keywordsLine.split(",");
			List<String> listKeys = new ArrayList<String>();
			listKeys = Arrays.asList(keywords);
			U.print(listKeys.toString());
			
			//��ȡÿ���ʵ�����
			String stats = DBFunction.getFeature(category, id);
			Map<String, Vector<String>> map = U.string2Map(stats);
			
			String topWords = getTopWords(map, 5, MyStatic.Index_TFIDF);
			listTFIDF.add(topWords);//��topTFIDF
			map = U.string2Map(stats);//�ù֣�map��������ȥ�ͱ��ı��ˣ���û�и���һ��map�ĸ�������(�ٴ�ʵ�鷢��û���⣿��)
			
			listFrequency.add(getTopWords(map, 5, MyStatic.Index_WordFrequency));//��top��Ƶ
			map = U.string2Map(stats);
			
			//��һ�����������¼�ؼ��ʣ���ͳ�ƹؼ�������
			int countKeys = 0;
			for(String key : map.keySet()){
				//���������ݴ�֮ǰ�Ĵ�
				if(U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)) > 7) continue;
				//����¼�ؼ���
				if(!listKeys.contains(key)) continue;
				listX.add(getListx(category, map.get(key)));//��X
				listY.add(1);//��Y
				countKeys ++;
			}
			U.print(countKeys + "");
			
			//�ڶ������������¼ͬ�����ķǹؼ���
			int mapSize = map.size();
			String[] temp = {"test", "test"};
			String[] s2 = map.keySet().toArray(temp);
			int randomCount = 0;//��������˼��Σ�����һ�����������������ܳ��ַǹؼ���<�ؼ���������������while�޷�������
			while(countKeys > 0){
				if(randomCount++ > 10) break;
				Random r = new Random();
				int random = (int)r.nextInt(mapSize);
				//���������ݴ�֮ǰ�Ĵ�
				if(U.wordCharacters2Int(map.get(s2[random]).get(MyStatic.Index_WordCharacteristic)) > 7)
					continue;
				//����¼�ǹؼ���
				if(!listKeys.contains(s2[random])){
					listX.add(getListx(category, map.get(s2[random])));
					listY.add(0);
					countKeys --;
				}
			}
		}
		
		FileFunction.writeTrainingSetX(category, listX);
		FileFunction.writeTrainingSetY(category, listY);
		FileFunction.writeTrainingSetWord(category, listWord);
		FileFunction.writeTrainingSetID(category, listID);
		FileFunction.writeTrainingSetTopTFIDF(category, listTFIDF);
		FileFunction.writeTrainingSetTopFrequency(category, listFrequency);
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
	public static void DataMiddle2Txt(int category) throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromMiddle(category);
		while(rs.next()){
			int id = -1;
			if(category == MyStatic.Category_YouJi)
				id = rs.getInt(MyStatic.KEY_ID_rawYouJi);
			else if(category == MyStatic.Category_DianPing)
				id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String stats = rs.getString(MyStatic.KEY_Stats);
			Map<String, Vector<String>> map = U.string2Map(stats);
			
			List<List<Double>> listX = new ArrayList<List<Double>>();//�������
			List<String> listW = new ArrayList<String>();//�����
			for(String key : map.keySet()){
				//���������ݴ�֮ǰ�Ĵ�
				if(U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)) > 7)
					continue;
				//��X
				listX.add(getListx(category, map.get(key)));
				//���
				listW.add(key);
				
			}
			FileFunction.writeEveryMiddleFeature(category, listX, id);
			FileFunction.writeEveryMiddleWord(category, listW, id);
		}
		U.print("middle�е���ֵ������������txt,����Ϊ" + category);
	}
	
	//ȷ��ѡ����Щ����
	private static List<Double> getListx(int category, Vector<String> vector){
		List<Double> listx = new ArrayList<Double>();//�浥��������
		if(category == MyStatic.Category_YouJi){
//			listx.add(Double.parseDouble(vector.get(MyStatic.Index_WordCount)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_TFIDF)));
//			listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_FirstWord)));
//			listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_LastWord)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_Absolute)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_WordLength)));
			listx.add((double)U.wordCharacters2Int(vector.get(MyStatic.Index_WordCharacteristic)));
		}
		else if(category == MyStatic.Category_DianPing){
	//		listx.add(Double.parseDouble(vector.get(MyStatic.Index_WordCount)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_TFIDF)));
	//		listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_FirstWord)));
	//		listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_LastWord)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_Absolute)));
			listx.add(Double.parseDouble(vector.get(MyStatic.Index_WordLength)));
			listx.add((double)U.wordCharacters2Int(vector.get(MyStatic.Index_WordCharacteristic)));
		}
		return listx;
	}
}
