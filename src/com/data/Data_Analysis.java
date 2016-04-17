package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.db.FileFunction;
import com.myClass.U;
import com.myClass.ValueComparator;

public class Data_Analysis {
	
	//ͳ�Ƴ��ֵĹؼ������ʹ�Ƶ
	public static void outputKeyWordCounts() throws IOException{
		//��ȡ�ؼ���
		List<String> list = FileFunction.readTxt_Keywords("E:\\work\\��ʿ��\\output\\keywords.txt");//��readStopWords�����������ȡ
		Map<String, Integer> map = new HashMap<String, Integer>();
		U.print("��ʼ��ȡ��ͳ�ƹؼ���");
		for(int i = 0; i < list.size(); i++){
			String line = list.get(i).trim();
			if(line.equals("") || line.isEmpty()) continue;
			String[] keywords = line.split(",");
			for(String keyword : keywords){
				if(keyword.equals("") || keyword.isEmpty()) continue;
				U.print(keyword);
				map.put(keyword, map.get(keyword) == null ? 1 : map.get(keyword)+1);
			}
		}
		U.print("��ʼ����ؼ���");
		ValueComparator bvc =  new ValueComparator(map);  
        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
        sortedMap.putAll(map);
        FileFunction.Keywords_outputKeywordsFrequency(sortedMap);
        U.print("�ؼ��������ϣ������" + map.size() + "����");
	}

	public static void outputGraphTxt(int threashold) throws IOException{
		U.print("��ʼ��ȡ�ؼ���-��Ƶ��");
		List<String> listKeywordsFrequency = FileFunction.readTxt_StopWords("E:\\work\\��ʿ��\\output\\keywords_frequency.txt");//��readStopWords�����������ȡ
		//Ϊÿ���ʸ���id
		int id = 1;//��1��ʼ����
		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<Integer, String> mapID = new HashMap<Integer, String>();
		for(int i = 0; i < listKeywordsFrequency.size(); i++){
			String line = listKeywordsFrequency.get(i).trim();
			if(line == "" || line.isEmpty()) continue;
			String[] keywordsFrequency = line.split("\t");//����"�Ŷ�	2371"
			if(Integer.parseInt(keywordsFrequency[1]) < threashold) break;
			String keyword = keywordsFrequency[0];
			if(map.get(keyword) == null){
				map.put(keyword, id);
				mapID.put(id, keyword);
				U.print(keyword + "��" + id);
				id++;
			}
		}
		//��ȡ�ؼ���
		U.print("��ʼ��ȡ�ؼ���");
		List<String> list = FileFunction.readTxt_Keywords("E:\\work\\��ʿ��\\output\\keywords.txt");//��readStopWords�����������ȡ
		U.print("��ʼд��ؼ���");
		FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\output\\keywords_graph.net");
		fw.write("*Vertices " + map.size());
		for(int i = 0; i < map.size(); i++){
			fw.write("\r\n");//Ϊ��һ�в��任�У��������һ��Ҳ������
			fw.write((i+1) + " \"" + mapID.get(i+1) + "\"");
			U.print("д���:" + map.get(mapID.get(i+1)));
		}
		fw.write("\r\n");
		fw.write("*Edges");
		U.print("��ʼд��ؼ���֮��Ĺ�ϵ");
		for(int i = 0; i < list.size(); i++){
			String line = list.get(i).trim();
			if(line.equals("") || line.isEmpty()) continue;
			String[] keywords = line.split(",");
			//�ؼ���֮��������ϵ
			for(String keyword : keywords){
				if(keyword.equals("") || keyword.isEmpty()) continue;
				for(int j = 0; j < keywords.length; j ++){
					if(map.get(keyword) == null || map.get(keywords[j]) == null) continue;//ֻҪ����һ��С����ֵ�������
					if(keyword.equals(keywords[j])) continue;//������������
					fw.write("\r\n");//Ϊ��һ�в��任�У��������һ��Ҳ������
					fw.write(map.get(keyword) + " " + map.get(keywords[j]));
					U.print(map.get(keyword) + " " + map.get(keywords[j]));
				}
			}
		}
		fw.close();
		U.print("�ؼ����������ͼд�����");
	}
	
}
