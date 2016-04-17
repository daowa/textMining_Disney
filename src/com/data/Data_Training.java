package com.data;

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

import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;

public class Data_Training {

	public static void humanIndexing(int version, int wordCount) throws SQLException{
		ResultSet rs = DBFunction.getRandomDianPing(200, 106844, 142437);
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			U.print("=================  id:" + id + "  =================");
			U.print("ԭʼ����:" + content);
			Map<String, Vector<String>> map = DBFunction.getDianPingStats(id);
			U.print("��ͳ��:" + map.toString());
			
			if(DBFunction.DianPing_isIndexed(version, id)) continue;//����Ѿ��������������ٱ���s
			
			//��������Ĺؼ���
			while(true){
				if(version == MyStatic.Version_HUMINDEX_1)
					U.print(">>������" + wordCount + "���ʣ��Կո�ָ�(�ǵý�����ֶ���λ����һ�У����������쳣)(����pass��������)");
				else
					U.print(">>����������" + wordCount + "���ʣ��Կո�ָ�(�ǵý�����ֶ���λ����һ�У����������쳣)(����pass��������)");
				Scanner sc = new Scanner(System.in);
				String order = sc.nextLine();
				if(order.equals("pass"))//����pass����������
					break;
				String[] keywords = order.split(" ");
				//��������ʽ
				if(version == MyStatic.Version_HUMINDEX_1 && keywords.length != wordCount){
					U.print("�����ʽ����,����" + wordCount + "����");
					continue;//�����ʽ��������������
				}
				else if(version == MyStatic.Version_HUMINDEX_2 && keywords.length > wordCount){
					U.print("�����ʽ����,������" + wordCount + "����");
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
					if(keywords.length <= wordCount){
						if(DBFunction.insertTrainingSet(version, id, keywords) > 0){
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
	public static void DataTraining2Txt(int version) throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromTrainingSet(version);
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
			U.print(listKeys.toString());
			
			//��ȡÿ���ʵ�����
			String stats = DBFunction.getFeature(rs.getInt(MyStatic.KEY_ID_rawDianPing));
			Map<String, Vector<String>> map = U.string2Map(stats);
			
			String topWords = getTopWords(map, 3, MyStatic.Index_TFIDF);
			listTFIDF.add(topWords);//��topTFIDF
			map = U.string2Map(stats);//�ù֣�map��������ȥ�ͱ��ı��ˣ���û�и���һ��map�ĸ�������(�ٴ�ʵ�鷢��û���⣿��)
			
			listFrequency.add(getTopWords(map, 3, MyStatic.Index_WordFrequency));//��top��Ƶ
			map = U.string2Map(stats);
			
			//��һ�����������¼�ؼ��ʣ���ͳ�ƹؼ�������
			int countKeys = 0;
			for(String key : map.keySet()){
				//���������ݴ�֮ǰ�Ĵ�
				if(U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)) > 7) continue;
				//����¼�ؼ���
				if(!listKeys.contains(key)) continue;
				listX.add(getListx(map.get(key)));//��X
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
					listX.add(getListx(map.get(s2[random])));
					listY.add(0);
					countKeys --;
				}
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
				listX.add(getListx(map.get(key)));
				//���
				listW.add(key);
				
			}
			FileFunction.writeEveryMiddleFeature(listX, id);
			FileFunction.writeEveryMiddleWord(listW, id);
		}
		U.print("middle�е���ֵ������������txt");
	}
	
	private static List<Double> getListx(Vector<String> vector){
		List<Double> listx = new ArrayList<Double>();//�浥��������
//		listx.add(Double.parseDouble(vector.get(MyStatic.Index_WordCount)));
		listx.add(Double.parseDouble(vector.get(MyStatic.Index_TFIDF)));
//		listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_FirstWord)));
//		listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_LastWord)));
		listx.add(Double.parseDouble(vector.get(MyStatic.Index_Position_Absolute)));
		listx.add(Double.parseDouble(vector.get(MyStatic.Index_WordLength)));
		listx.add((double)U.wordCharacters2Int(vector.get(MyStatic.Index_WordCharacteristic)));
		return listx;
	}
}
