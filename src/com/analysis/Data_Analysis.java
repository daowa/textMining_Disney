package com.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.alibaba.fastjson.parser.Keywords;
import com.data.DBFunction;
import com.data.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;
import com.myClass.ValueComparator;
import com.myClass.ValueComparator_Double;

public class Data_Analysis {
	
	//ͳ�Ƴ��ֵĹؼ������ʹ�Ƶ
	public static void outputKeyWordCounts() throws IOException{
		//��ȡ�ؼ���
		List<String> list = FileFunction.readTxt_Keywords("E:\\work\\��ʿ��\\output\\keywords.txt");//��readStopWords�����������ȡ
		Map<String, Integer> map = new HashMap<String, Integer>();
		int allCount = 0;
		U.print("��ʼ��ȡ��ͳ�ƹؼ���");
		for(int i = 0; i < list.size(); i++){
			String line = list.get(i).trim();
			if(line.equals("") || line.isEmpty()) continue;
			String[] keywords = line.split(",");
			for(String keyword : keywords){
				if(keyword.equals("") || keyword.isEmpty()) continue;
				U.print(keyword);
				allCount ++;
				map.put(keyword, map.get(keyword) == null ? 1 : map.get(keyword)+1);
			}
		}
		U.print("��ʼ����ؼ���");
		ValueComparator bvc =  new ValueComparator(map);  
        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
        sortedMap.putAll(map);
        FileFunction.Keywords_outputKeywordsFrequency(sortedMap);
        U.print("�����" + allCount + "���ؼ���");
        U.print("�ؼ��������ϣ������" + map.size() + "����");
	}

	//������ڻ�����ͼ��txt
	public static void outputGraphTxt(double threshold) throws IOException{
		U.print("��ʼ��ȡ�ؼ���-��Ƶ��");
		List<String> listKeywordsFrequency = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\keywords_frequency.txt");//��readStopWords�����������ȡ
		byte[][] matrix = new byte[10000][10000];
		//Ϊÿ���ʸ���id
		int id = 1;//��1��ʼ����
		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<Integer, String> mapID = new HashMap<Integer, String>();
		for(int i = 0; i < listKeywordsFrequency.size(); i++){
			String line = listKeywordsFrequency.get(i).trim();
			if(line == "" || line.isEmpty()) continue;
			String[] keywordsFrequency = line.split("\t");//����"�Ŷ�	2371"
			String keyword = keywordsFrequency[0];
			if(map.get(keyword) == null){
				map.put(keyword, id);
				mapID.put(id, keyword);
				id++;
			}
		}
		//��ȡ�ؼ���
		U.print("��ʼ��ȡ�ؼ���");
		List<String> list = FileFunction.readTxt_Keywords("E:\\work\\��ʿ��\\output\\keywords.txt");//��readStopWords�����������ȡ
		//����ϵд�����
		for(int i = 0; i < list.size(); i++){
			String line = list.get(i).trim();
			if(line.equals("") || line.isEmpty()) continue;
			String[] keywords = line.split(",");
			//�ؼ���֮��������ϵ
			for(String keyword : keywords){
				if(keyword.equals("") || keyword.isEmpty()) continue;
				for(int j = 0; j < keywords.length; j ++){
					if(map.get(keyword) == null || map.get(keywords[j]) == null) continue;
					if(keyword.equals(keywords[j])) continue;//������������
					//����˫���ͷ
					matrix[map.get(keyword)][map.get(keywords[j])] += 1;
					matrix[map.get(keywords[j])][map.get(keyword)] += 1;
				}
			}
		}
		//ͳ����Щid��������
		List<Integer> idList = new ArrayList<Integer>();
		for(int i = 1; i <= mapID.size(); i++){
			for(int j = 1; j <= mapID.size(); j++){
				int weight = matrix[i][j];
				if(weight >= threshold && !idList.contains(i)){
					idList.add(i);
				}
			}
		}
		//д��txt
		U.print("��ʼд��ؼ���");
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\keywords_graph.net");
		fw.write("*Vertices " + idList.size());
		for(int i = 1; i <= idList.size(); i++){//�����i��д��txt���µ�id��idList.get(i-1)��ԭ�������е�id
			fw.write("\r\n");//Ϊ��һ�в��任�У��������һ��Ҳ������
			fw.write(i + " \"" + mapID.get(idList.get(i-1)) + "\"");
			U.print(i + " \"" + mapID.get(idList.get(i-1)) + "\"");
		}
		fw.write("\r\n");
		fw.write("*Edges");
		U.print("��ʼд��ؼ���֮��Ĺ�ϵ");
		for(int i = 1; i <= idList.size(); i++){
			for(int j = 1; j <= idList.size(); j++){
				int weight = matrix[idList.get(i-1)][idList.get(j-1)];
				if(weight >= threshold){
					fw.write("\r\n");//Ϊ��һ�в��任�У��������һ��Ҳ������
					fw.write(i + " " + j + " " + weight*1.2/threshold);
				}
			}
		}
		fw.close();
		U.print("�ؼ����������ͼд�����");
	}
	
	//������ڹ��������;���ľ����ʽ����
	public static void outputMatrix(int threshold) throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\keywords_matrix.txt");
		//�����йؼ��ʶ���,��д��txt�ı���
		List<String> list_Keyword_Frequency = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\keywords_frequency.txt");
		fw.write("id");
		for(int i = 0; i < list_Keyword_Frequency.size(); i++){
			if(Integer.parseInt(list_Keyword_Frequency.get(i).split("\t")[1]) < threshold) continue;//ֻ���ø�����ֵ�Ĵʣ�������������
			fw.write("," + list_Keyword_Frequency.get(i).split("\t")[0]);
		}
		fw.write("\r\n");
		//�������id�Ĺؼ��ʣ���д��txt
		List<String> list_ID_Keyword = DBFunction.getKeyword();
		for(int i = 0; i < list_ID_Keyword.size(); i++){
			String[] id_keywords = list_ID_Keyword.get(i).split("\t");
			if(id_keywords.length < 2) continue;//�����contentΪ�յ�������򲻼�¼��һ��
			U.print(id_keywords[0] + "\t" + id_keywords[1]);
			fw.write(id_keywords[0]);
			for(int j = 0; j < list_Keyword_Frequency.size(); j++){
				if(Integer.parseInt(list_Keyword_Frequency.get(j).split("\t")[1]) < threshold) continue;//ֻ���ø�����ֵ�Ĵʣ�������������
				fw.write(",");
				String[] keywords = id_keywords[1].split(",");
				List<String> listKeywords = Arrays.asList(keywords);
				if(listKeywords.contains(list_Keyword_Frequency.get(j).split("\t")[0]))
					fw.write("1");
				else
					fw.write("0");
			}
			fw.write("\r\n");
		}
		fw.close();
		U.print("�������E:\\work\\��ʿ��\\output\\keywords_matrix.txt");
	}
	//�������MDS�����ľ����ʽ����
	public static void outputMatrix2() throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\keywords_matrix2.txt");
		//�����йؼ��ʶ���,��д��txt�ı���
		List<String> list_Keyword_Frequency = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\keywords_clusterTop.txt");
		fw.write("id");
		for(int i = 0; i < list_Keyword_Frequency.size(); i++){
			fw.write("," + list_Keyword_Frequency.get(i).split("\t")[0]);
		}
		fw.write("\r\n");
		//�������id�Ĺؼ��ʣ���д��txt
		List<String> list_ID_Keyword = DBFunction.getKeyword();
		for(int i = 0; i < list_ID_Keyword.size(); i++){
			String[] id_keywords = list_ID_Keyword.get(i).split("\t");
			if(id_keywords.length < 2) continue;//�����contentΪ�յ�������򲻼�¼��һ��
			U.print(id_keywords[0] + "\t" + id_keywords[1]);
			fw.write(id_keywords[0]);
			for(int j = 0; j < list_Keyword_Frequency.size(); j++){
				fw.write(",");
				String[] keywords = id_keywords[1].split(",");
				List<String> listKeywords = Arrays.asList(keywords);
				if(listKeywords.contains(list_Keyword_Frequency.get(j).split("\t")[0]))
					fw.write("1");
				else
					fw.write("0");
			}
			fw.write("\r\n");
		}
		fw.close();
		U.print("�������E:\\work\\��ʿ��\\output\\keywords_matrix2.txt");
//		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\keywords_matrix2.txt");
//		//�������ı�����,����idд��txt�ı���
//		List<String> list_ID_Keyword = DBFunction.getKeyword();
//		fw.write("words");
//		for(int i = 0; i < list_ID_Keyword.size(); i++){
//			fw.write("," + list_ID_Keyword.get(i).split("\t")[0]);
//		}
//		fw.write("\r\n");
//		//����ؼ��ʣ���ͳ��ÿ��id�Ƿ�����˸ùؼ���
//		List<String> list_Keyword_Frequency = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\keywords_clusterTop.txt");
//		for(int i = 0; i < list_Keyword_Frequency.size(); i++){
//			fw.write(list_Keyword_Frequency.get(i));
//			for(int j = 0; j < list_ID_Keyword.size(); j++){
//				fw.write(",");
//				String[] id_keywords = list_ID_Keyword.get(j).split("\t");
//				if(id_keywords.length < 2){//�����contentΪ�յ�����������0
//					fw.write("0");
//					continue;
//				}
//				String[] keywords = id_keywords[1].split(",");
//				List<String> listKeywords = Arrays.asList(keywords);
//				if(listKeywords.contains(list_Keyword_Frequency.get(i)))
//					fw.write("1");
//				else
//					fw.write("0");
//			}
//			fw.write("\r\n");
//		}
//		fw.close();
//		U.print("�������E:\\work\\��ʿ��\\output\\keywords_matrix2.txt");
	}
	
	//������ڹ��������;���ľ����ʽ����_�����ʽ
	public static void outputMatrix_ShiWu() throws IOException{
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\keywords_matrix_shiwu.txt");
		fw.write("id" + "," + "keyword" + "\r\n");
		//�������id�Ĺؼ��ʣ���д��txt
		List<String> list_ID_Keyword = DBFunction.getKeyword();
		for(int i = 0; i < list_ID_Keyword.size(); i++){
			String[] id_keywords = list_ID_Keyword.get(i).split("\t");
			if(id_keywords.length < 2) continue;//�����contentΪ�յ�������򲻼�¼��һ��
			String[] keywords = id_keywords[1].split(",");
			for(int j = 0; j < keywords.length; j++){
				fw.write(id_keywords[0] + "," + keywords[j] + "\r\n");
			}
		}
		fw.close();
		U.print("�������E:\\work\\��ʿ��\\output\\keywords_matrix_shiwu.txt");
	}
	
	//���ÿ����ʿ��Ĺؼ��ʣ����Ҵ�Ƶ����
	public static void outputAddressKeywordCount() throws IOException{
		//��ȡ�ؼ���
		String[] citys= {MyStatic.City_California, MyStatic.City_HongKong, MyStatic.City_Orlando, MyStatic.City_Paris, MyStatic.City_Tokyo};
		int[] cityCounts = {1109, 28542, 1188, 693, 3732};
		for(int iCity = 0; iCity < citys.length ;iCity++){
			String city = citys[iCity];
			List<String> list = DBFunction.getKeywordByAddress(city);
			Map<String, Integer> map = new HashMap<String, Integer>();
			int allCount = 0;
			U.print("��ʼ��ȡ��ͳ�ƹؼ���");
			for(int i = 0; i < list.size(); i++){
				String line = list.get(i).trim();
				if(line.equals("") || line.isEmpty()) continue;
				String[] keywords = line.split(",");
				for(String keyword : keywords){
					if(keyword.equals("") || keyword.isEmpty()) continue;
					allCount ++;
					map.put(keyword, map.get(keyword) == null ? 1 : map.get(keyword)+1);
				}
			}
			U.print("��ʼ����ؼ���");
			ValueComparator bvc =  new ValueComparator(map);  
	        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
	        sortedMap.putAll(map);
	        FileFunction.Keywords_outputAddressKeywordsFrequency(sortedMap, city, cityCounts[iCity]);
	        U.print("�����" + allCount + "���ؼ���");
	        U.print("�ؼ��������ϣ������" + city + map.size() + "����");
	        U.print(MyStatic.Others_Line);
		}
	}
	
	//���ÿ���·ݵ�ʿ��Ĺؼ��ʣ����Ҵ�Ƶ����
	public static void outputMonthKeywordCount() throws IOException{
		//��ȡ�ؼ���
		for(int iMonth = 1; iMonth <= 12 ;iMonth++){
			List<String> list = DBFunction.getKeywordByMonth(iMonth);
			Map<String, Integer> map = new HashMap<String, Integer>();
			int allCount = 0;
			U.print("��ʼ��ȡ��ͳ�ƹؼ���");
			for(int i = 0; i < list.size(); i++){
				String line = list.get(i).trim();
				if(line.equals("") || line.isEmpty()) continue;
				String[] keywords = line.split(",");
				for(String keyword : keywords){
					if(keyword.equals("") || keyword.isEmpty()) continue;
					allCount ++;
					map.put(keyword, map.get(keyword) == null ? 1 : map.get(keyword)+1);
				}
			}
			U.print("��ʼ����ؼ���");
			ValueComparator bvc =  new ValueComparator(map);  
	        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
	        sortedMap.putAll(map);
	        FileFunction.Keywords_outputMonthKeywordsFrequency(sortedMap, iMonth, list.size());
	        U.print("�����" + allCount + "���ؼ���");
	        U.print("�ؼ��������ϣ������" + iMonth + "��" + map.size() + "����");
	        U.print(MyStatic.Others_Line);
		}
	}
	
	//����ģ��TFIDF�ķ�ʽ�����ÿ���¡�TFIDF����ߵļ�����
	public static void outputMonthKeywordTFIDF() throws IOException{
		//�����·ݹؼ��ʴ�Ƶ����map
		Map<String, Double> mapMonth = new HashMap<String, Double>();
		List<List<String>> listlistMonthKeywords = new ArrayList<List<String>>();
		for(int month = 1; month <= 12; month++){
			List<String> list = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\frequency_" + month + ".txt");
			List<String> listKeywords = new ArrayList<String>();
			for(int i = 0; i < list.size(); i++){
				String[] s = list.get(i).trim().split("\t");
				double tf = Double.parseDouble(s[1].replace("%", ""));
				if(tf < 1) continue;//ֻѡȡ��Ƶ����1�Ĵ�
				mapMonth.put(s[0] + month, Double.parseDouble(s[1].replace("%", "")));
				listKeywords.add(s[0]);
			}
			listlistMonthKeywords.add(listKeywords);
		}
		//��ÿ���µ�tfidf��Ƶ�����
		for(int iMonth = 1; iMonth <= 12 ;iMonth++){
			Map<String, Double> map = new HashMap<String, Double>();
			List<String> listKeywords = listlistMonthKeywords.get(iMonth-1);
			for(int i = 0; i < listKeywords.size(); i++){
				String keyword = listKeywords.get(i);
				double countTF = 0;//��tf��һ��
				double countMonth = 0;//����������ڸ����·��ĵ��г����˼���
				for(int jMonth = 1; jMonth <= 12; jMonth++){
					if(mapMonth.get(keyword + jMonth) != null)
						countTF += mapMonth.get(keyword + jMonth);
						countMonth += 1;
				}
				map.put(keyword, mapMonth.get(keyword + iMonth)/countTF/countMonth);
			}
			U.print("��ʼ����ؼ���TFIDF");
			ValueComparator_Double bvc =  new ValueComparator_Double(map);  
	        TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(bvc);
	        sortedMap.putAll(map);
	        FileFunction.Keywords_outputMonthKeywordsTFIDF(sortedMap, iMonth);
	        U.print("�ؼ���TFIDF�����ϣ������" + iMonth + "��" + map.size() + "����");
	        U.print(MyStatic.Others_Line);
		}
	}
	
	//���ÿ���ؼ��ʰ��·ݷֵķ���
	public static void outputMonthKeywordVariance() throws IOException{
		//�����·ݹؼ��ʴ�Ƶ����map
		Map<String, Double> map = new HashMap<String, Double>();
		for(int month = 1; month <= 12; month++){
			List<String> list = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\frequency_" + month + ".txt");
			for(int i = 0; i < list.size(); i++){
				String[] s = list.get(i).trim().split("\t");
				map.put(s[0] + month, Double.parseDouble(s[1].replace("%", "")));
			}
		}
		//��ȡ���еĴ�,Ϊÿ����ͳ��ƽ�����ͷ���
		Map<String, Double> mapMean = new HashMap<String, Double>();
		Map<String, Double> mapVariance = new HashMap<String, Double>();
		List<String> list = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\keywords_frequency.txt");
		for(int i = 0; i < list.size(); i++){ //����ÿ����ȡ���Ĵʣ��󷽲����mapVariance��
			String[] s = list.get(i).trim().split("\t");
			List<Double> listData = new ArrayList<Double>();
			for(int month = 1; month <= 12; month++){
				listData.add(map.get(s[0] + month) != null ? map.get(s[0] + month) : 0);
			}
			mapMean.put(s[0], U.MATH_getAverage(listData));
			mapVariance.put(s[0], U.MATH_getVariance(listData)/U.MATH_getAverage(listData));
		}
		//���շ�������
		ValueComparator_Double bvc =  new ValueComparator_Double(mapVariance);  
        TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(bvc);
        sortedMap.putAll(mapVariance);
        //�����txt
        FileFunction.Keywords_outputMonthKeywordsVarianceAndMeans(sortedMap, mapMean);
	}
	
	public static void outputMonthKeyword_JustTopVariance(int topN, double threshold) throws IOException{
		//�����·ݹؼ��ʴ�Ƶ����map
		Map<String, Double> map = new HashMap<String, Double>();
		for(int month = 1; month <= 12; month++){
			List<String> list = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\frequency_" + month + ".txt");
			for(int i = 0; i < list.size(); i++){
				String[] s = list.get(i).trim().split("\t");
				map.put(s[0] + month, Double.parseDouble(s[1].replace("%", "")));
			}
		}
		//��ȡÿ���¸߷���ؼ��ʵĴ���
		List<String> list = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\��������\\��ͬ�·�\\variance.txt");
		List<String> output = new ArrayList<String>();
		for(int i = 1; i < topN; i++){//����ȡ��һ��,��Ϊ��һ��������
			String[] s = list.get(i).trim().split("\t");
			if(Double.parseDouble(s[2]) < threshold) continue;
			String line = s[0] + "\t";
			for(int month = 1; month <= 12; month++){
				line += ((map.get(s[0] + month) != null ? map.get(s[0] + month) : 0) + "0000").substring(0, 4) + "\t";
			}
			output.add(line);
		}
		//�����txt
		FileFunction.Keywords_outputJustTopVariance(output);
	}
	
	//����״�ͼ����Ҫ�����ݣ��Ծ������ǣ�
	public static void outputRadar(){
		int[] hongkong = {0, 0, 0, 0, 0};
		int[] tokyo = {0, 0, 0, 0, 0};
		int[] paris = {0, 0, 0, 0, 0};
		int[] california = {0, 0, 0, 0, 0};
		int[] orlando = {0, 0, 0, 0, 0};
		List<String> list = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\cluster.txt");
		for(int i = 0; i < list.size(); i++){
			String[] s = list.get(i).split("\t");
			String city = DBFunction.getCity(Integer.parseInt(s[0]));
			int cluster = Integer.parseInt(s[1].substring(s[1].length()-1, s[1].length())) - 1;
			if(city.equals(MyStatic.City_HongKong))
				hongkong[cluster] ++;
			else if(city.equals(MyStatic.City_Tokyo))
				tokyo[cluster] ++;
			else if(city.equals(MyStatic.City_Paris))
				paris[cluster] ++;
			else if(city.equals(MyStatic.City_California))
				california[cluster] ++;
			else if(city.equals(MyStatic.City_Orlando))
				orlando[cluster] ++;
		}
		double allHongkong = countAll(hongkong);
		double allTokyo = countAll(tokyo);
		double allParis = countAll(paris);
		double allCalifornia = countAll(california);
		double allOrlando = countAll(orlando);
		
		U.print("����԰\tͯ�����\tȫ���ܶ�Ա\tƱ��\t�λñ���");
		U.print(MyStatic.City_HongKong + ":" + hongkong[1]/allHongkong + "\t" + hongkong[2]/allHongkong + "\t" + hongkong[0]/allHongkong + "\t" + hongkong[4]/allHongkong + "\t" + hongkong[3]/allHongkong);
		U.print(MyStatic.City_Tokyo + ":" + tokyo[1]/allTokyo + "\t" + tokyo[2]/allTokyo + "\t" + tokyo[0]/allTokyo + "\t" + tokyo[4]/allTokyo + "\t" + tokyo[3]/allTokyo);
		U.print(MyStatic.City_Paris + ":" + paris[1]/allParis + "\t" + paris[2]/allParis + "\t" + paris[0]/allParis + "\t" + paris[4]/allParis + "\t" + paris[3]/allParis);
		U.print(MyStatic.City_California + ":" + california[1]/allCalifornia + "\t" + california[2]/allCalifornia + "\t" + california[0]/allCalifornia + "\t" + california[4]/allCalifornia + "\t" + california[3]/allCalifornia);
		U.print(MyStatic.City_Orlando + ":" + orlando[1]/allOrlando + "\t" + orlando[2]/allOrlando + "\t" + orlando[0]/allOrlando + "\t" + orlando[4]/allOrlando + "\t" + orlando[3]/allOrlando);
	}
	//ͳ������ĺ�
	private static int countAll(int[] input){
		int all = 0;
		for(int i = 0; i < input.length; i++){
			all += input[i];
		}
		return all;
	}
	//����״�ͼ����Ҫ�����ݣ��Գ��ִ������ڵ���ǣ�
	public static void outputRadar2(){
		int[] hongkong = getCityWordsCluster(MyStatic.City_HongKong);
		int[] tokyo = getCityWordsCluster(MyStatic.City_Tokyo);
		int[] paris = getCityWordsCluster(MyStatic.City_Paris);
		int[] california = getCityWordsCluster(MyStatic.City_California);
		int[] orlando = getCityWordsCluster(MyStatic.City_Orlando);
		
		double allHongkong = countAll(hongkong);
		double allTokyo = countAll(tokyo);
		double allParis = countAll(paris);
		double allCalifornia = countAll(california);
		double allOrlando = countAll(orlando);
		
		U.print("����԰\tͯ�����\tȫ���ܶ�Ա\tƱ��\t�λñ���");
		U.print(MyStatic.City_HongKong + ":" + hongkong[1]/allHongkong + "\t" + hongkong[2]/allHongkong + "\t" + hongkong[0]/allHongkong + "\t" + hongkong[4]/allHongkong + "\t" + hongkong[3]/allHongkong);
		U.print(MyStatic.City_Tokyo + ":" + tokyo[1]/allTokyo + "\t" + tokyo[2]/allTokyo + "\t" + tokyo[0]/allTokyo + "\t" + tokyo[4]/allTokyo + "\t" + tokyo[3]/allTokyo);
		U.print(MyStatic.City_Paris + ":" + paris[1]/allParis + "\t" + paris[2]/allParis + "\t" + paris[0]/allParis + "\t" + paris[4]/allParis + "\t" + paris[3]/allParis);
		U.print(MyStatic.City_California + ":" + california[1]/allCalifornia + "\t" + california[2]/allCalifornia + "\t" + california[0]/allCalifornia + "\t" + california[4]/allCalifornia + "\t" + california[3]/allCalifornia);
		U.print(MyStatic.City_Orlando + ":" + orlando[1]/allOrlando + "\t" + orlando[2]/allOrlando + "\t" + orlando[0]/allOrlando + "\t" + orlando[4]/allOrlando + "\t" + orlando[3]/allOrlando);
	}
	private static int[] getCityWordsCluster(String city){
		int result[] = {0, 0, 0, 0, 0};
		//��������list
		List<String> list = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\cluster\\compare.txt");
		List<String> listWords = new ArrayList<>();
		Map<String, String> mapWordCluster = new HashMap<>();
		for(int i = 0; i < list.size(); i++){
			String[] ss = list.get(i).split("\t");
			listWords.add(ss[0]);
			mapWordCluster.put(ss[0], ss[1]);
		}
		//��ʼ����
		List<String> listKeywords = DBFunction.getKeywordByAddress(city);
		for(int i = 0; i < listKeywords.size(); i++){
			String line = listKeywords.get(i).trim();
			if(line.equals("") || line.isEmpty()) continue;
			String[] ss = line.split(",");
			for(String s : ss){
				if(listWords.contains(s)){
					if(mapWordCluster.get(s).equals("����԰")){
						result[0] ++;
					}
					else if(mapWordCluster.get(s).equals("ͯ�����")){
						result[1] ++;
					}
					else if(mapWordCluster.get(s).equals("ȫ���ܶ�Ա")){
						result[2] ++;
					}
					else if(mapWordCluster.get(s).equals("Ʊ��")){
						result[3] ++;
					}
					else if(mapWordCluster.get(s).equals("�λñ���")){
						result[4] ++;
					}
				}
			}
		}
		return result;
	}
	
	//���ÿ���ؼ��ʶ�Ӧ���ĸ���
	public static void outputKeyowordCluster() throws IOException{
		List<String> list = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\keywords_clusterTop.txt");
		Map<String, Double> mapYouLeYuan = getClusterTopKeywordsAndFrequency("E:\\work\\��ʿ��\\output\\cluster\\����԰.txt");
		Map<String, Double> mapTongHuaQingJie = getClusterTopKeywordsAndFrequency("E:\\work\\��ʿ��\\output\\cluster\\ͯ�����.txt");
		Map<String, Double> mapQuanJiaZongDongYuan = getClusterTopKeywordsAndFrequency("E:\\work\\��ʿ��\\output\\cluster\\ȫ���ܶ�Ա.txt");
		Map<String, Double> mapPiaoWu = getClusterTopKeywordsAndFrequency("E:\\work\\��ʿ��\\output\\cluster\\Ʊ��.txt");
		Map<String, Double> mapMengHuanBiaoYan = getClusterTopKeywordsAndFrequency("E:\\work\\��ʿ��\\output\\cluster\\�λñ���.txt");
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\cluster\\compare.txt");
		for(int i = 0; i < list.size(); i++){
			String word = list.get(i);
			fw.write(word + "\t");
			//�ҳ����ֵ
			double i0 = mapYouLeYuan.get(word) == null ? 0 : mapYouLeYuan.get(word);
			double i1 = mapTongHuaQingJie.get(word) == null ? 0 : mapTongHuaQingJie.get(word);
			double i2 = mapQuanJiaZongDongYuan.get(word) == null ? 0 : mapQuanJiaZongDongYuan.get(word);
			double i3 = mapPiaoWu.get(word) == null ? 0 : mapPiaoWu.get(word);
			double i4 = mapMengHuanBiaoYan.get(word) == null ? 0 : mapMengHuanBiaoYan.get(word);
			fw.write(getMaxFrequencyWordBelongTo(i0, i1, i2, i3, i4));
			if(i != list.size()-1)
				fw.write("\r\n");
		}
		fw.close();
		U.print("done");
	}
	//��ȡÿ�����top30�ؼ����Լ����ڶ��ٱ����ı��г���
	private static Map<String, Double> getClusterTopKeywordsAndFrequency(String address){
		List<String> list = FileFunction.readTxt_StopWords(address);
		Map<String, Double> result = new HashMap<>();
		for(int i = 0; i < list.size(); i++){
			String[] s = list.get(i).split("\t");
			result.put(s[0], Double.parseDouble(s[1]));
		}
		return result;
	}
	private static String getMaxFrequencyWordBelongTo(double i0, double i1, double i2, double i3, double i4){
		double max = -1;
		String cluster = "";
		if(i0 > max){
			max = i0;
			cluster = "����԰";
		}
		if(i1 > max){
			max = i1;
			cluster = "ͯ�����";
		}
		if(i2 > max){
			max = i2;
			cluster = "ȫ���ܶ�Ա";
		}
		if(i3 > max){
			max = i3;
			cluster = "Ʊ��";
		}
		if(i4 > max){
			max = i4;
			cluster = "�λñ���";
		}
		return cluster;
	}
}
