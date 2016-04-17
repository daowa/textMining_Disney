package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import com.data.NLPIR.CLibrary;
import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;
import com.myClass.ValueComparator;

public class Data_PreProcessing {

	public static void readRawTextAndInsertDB(int category, String categoryAddress){
		
		File file = new File(categoryAddress);
		String[] webList = file.list();
		String[] cityList = {MyStatic.City_HongKong, MyStatic.City_California, MyStatic.City_Orlando, MyStatic.City_Paris, MyStatic.City_Tokyo};
			
		for(int i = 0; i < webList.length; i++){
			for(int j = 0; j < cityList.length; j++){
				String fileAddress = categoryAddress + "\\" + webList[i] + "\\" + cityList[j];
				System.out.println(webList[i]);
				List<List<String>> lists = FileFunction.readTxtInFile(fileAddress);
				DBFunction.insertBatch(category, lists, webList[i], cityList[j]);
			}
		}
	}
	
	//�������Щ�μ����沢û�е���ʿ��
	public static void youji_deleteNoDisney(){
		ResultSet rs = DBFunction.selectAllFromYouJi();
		try {
			while(rs.next()){
				String title = rs.getString(MyStatic.KEY_Title);
				String content = rs.getString(MyStatic.KEY_Content);
				if(content == ""){
					DBFunction.delete(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi);
					continue;
				}
					
				String s = title + content;
				if(!s.matches(".*��.��.*") && !s.matches(".*(d|D)isney.*") && !s.matches(".*������.*")
						 && !s.matches(".*����Ѽ.*") && !s.matches(".*����.*") && !s.matches(".*�Ǳ�.*")
						 && !s.matches(".*��԰.*")  && !s.matches(".*��԰.*")  && !s.matches(".*���.*")
						 && !s.matches(".*fastpass.*") && !s.matches(".*��԰.*")){
					U.print("���⣺" + title);
					U.print("���ģ�" + content);
					U.print("���������ָ��,d��ʾɾ����ֱ�ӻس���ʾnext");
					Scanner sc = new Scanner(System.in);
					String order = sc.nextLine();
					if(order.equals("d")){
						DBFunction.delete(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//�������Щ�μǵĳ��зŴ���
	public static void youji_resetCity(){
		ResultSet rs = DBFunction.selectAllFromYouJi();
		try {
			int resetCount = 0;
			int deleteCount = 0;
			while(rs.next()){
				String title = rs.getString(MyStatic.KEY_Title);
				String content = rs.getString(MyStatic.KEY_Content);
				String city = rs.getString(MyStatic.KEY_City);
				String s = title + content;
				
				//���ڱ������ʾ���������ģ���Ϊƥ�䣬�����н�һ������ƥ��
				if(title.matches(".*" + city + ".*")) continue;
				//��Щ�б�־�Եĳ��п���ֱ�Ӵ���
				if(title.matches(".*���.*") || title.matches(".*����.*") || title.matches(".*�۰�.*") || title.matches(".*(H|h)ong(K|k)ong.*")
						 || title.matches(".*HK.*")){
					if(city.equals(MyStatic.City_HongKong))
						continue;
					else {
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_HongKong);
						resetCount++;
						U.print("��" + resetCount + "�����ó���");
						continue;
					}
				}
				if(title.matches(".*����.*") || title.matches(".*��ɼ�.*") || title.matches(".*LA.*")){
					if(city.equals(MyStatic.City_California))
						continue;
					else {
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_California);
						resetCount++;
						U.print("��" + resetCount + "�����ó���");
						continue;
					}
				}
				if(title.matches(".*�ձ�.*") || title.matches(".*�޺�.*") || title.matches(".*ӣ��.*") || title.matches(".*����.*")
						 || title.matches(".*(T|t)okyo.*") || title.matches(".*�|��.*")){
					if(city.equals(MyStatic.City_Tokyo))
						continue;
					else {
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Tokyo);
						resetCount++;
						U.print("��" + resetCount + "�����ó���");
						continue;
					}
				}
				if(title.matches(".*����.*") || title.matches(".*����.*") || title.matches(".*ŷ��.*") || title.matches(".*(E|e)urope.*")){
					if(city.equals(MyStatic.City_Paris))
						continue;
					else {
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Paris);
						resetCount++;
						U.print("��" + resetCount + "�����ó���");
						continue;
					}
				}
				if(title.matches(".*������.*") || title.matches(".*�������.*") || title.matches(".*(F|f)lorida.*") || title.matches(".*(O|o)rlando.*")){
					if(city.equals(MyStatic.City_Orlando))
						continue;
					else {
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Orlando);
						resetCount++;
						U.print("��" + resetCount + "�����ó���");
						continue;
					}
				}
				
				int addressCount = 0;
				String[] cityList = {MyStatic.City_California, MyStatic.City_HongKong, MyStatic.City_Orlando, MyStatic.City_Paris, MyStatic.City_Tokyo};
				for(int i = 0; i < 5; i++){
					if(s.matches(".*" + cityList[i] + ".*")){
						addressCount ++;
					}
				}
				
				if(addressCount >= 2){
					U.print(MyStatic.Others_Line);
					U.print("���⣺" + title);
					U.print("���ģ�" + content);
					U.print("���У�" + city);
					U.print(">>���������ָ��:c-����;h-���;o-������;p-����;t-����,d��ʾɾ��,ֱ�ӻس���ʾnext");
					
					String newCity = "";
					Scanner sc = new Scanner(System.in);
					String order = sc.nextLine();
					
					if(order.equals("d")){
						deleteCount ++;
						DBFunction.delete(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi);
						continue;
					}
					
					if(order.equals("c"))
						newCity = MyStatic.City_California;
					else if(order.equals("h"))
						newCity = MyStatic.City_HongKong;
					else if(order.equals("o"))
						newCity = MyStatic.City_Orlando;
					else if(order.equals("p"))
						newCity = MyStatic.City_Paris;
					else if(order.equals("t"))
						newCity = MyStatic.City_Tokyo;
					
					if(!newCity.equals("")){
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, newCity);
						resetCount ++;
						U.print("��" + resetCount + "�����ó���");
					}
				}
			}
			U.print("����" + resetCount);
			U.print("ɾ��" + deleteCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//�������еķ�����תΪ������
	public static void dianping_ZHConverter() throws SQLException{
		ResultSet rs = DBFunction.selectAllFromDianPing();
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String rawContent = rs.getString(MyStatic.KEY_Content);
			String newContent = U.ZHConverter_TraToSim(rawContent);
			if(DBFunction.updateDianPingContent(id, newContent) > 0)
				U.print("idΪ" + id + "�ĵ����޸ĳɹ�");
		}
		U.print("�ѽ������еķ�����ת��Ϊ������");
	}
	//��������һЩʱ�䡢�����ȥ��
	public static void dianping_deleteTimeAndOthers() throws SQLException{
		ResultSet rs = DBFunction.selectAllFromDianPing();
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			//ȥ������2013-09-1813:32:00��ʽ��ʱ��
			content = content.replaceAll("[0-9]{4}-[0-9]{2}-[0-9]{4}:[0-9]{2}:[0-9]{2}", "");
			//ȥ������Day3��ʽ������
			content = content.replaceAll("Day[0-9]", "");
			//ȥ������[��Ц]�ı���
			content = content.replaceAll("\\[[\u4E00-\u9FA5]{2}\\]", "");
			//ȥ������5-24�����ָ�ʽ�����ڻ������s
			content = content.replaceAll("[0-9]{1,2}-[0-9]{1,2}", "");
			//ȥ��"�Ķ�ȫ��"�ȴ�
			content = content.replaceAll("�Ķ�ȫ��", "");
			//ȥ��������ĸ
			content = content.replaceAll("&#183;", "");
			//ȥ������ ����(0) ����ĸ
			content = content.replaceAll("����([0-9]*)", "");
			content = content.replaceAll("����([0-9]*)", "");
			content = content.replaceAll("ϲ��([0-9]*)", "");
			if(DBFunction.updateDianPingContent(id, content) > 0)
				U.print("idΪ" + id + "�ĵ����޸ĳɹ�");
		}
		U.print("�ѽ�������һЩʱ�䡢�����ȥ��");
	}
	//ͬ��ʹ鲢
	public static void dianping_synonym() throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromDianPing();
		List<List<String>> synonym = FileFunction.getSynonym();
		while(rs.next()){
			boolean needSynonmy = false;
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			for(List<String> list : synonym){
				for(int i = 0; i < list.size(); i++){
					if(i!=0 && content.contains(list.get(i))){
						needSynonmy = true;
						content = content.replaceAll(list.get(i), list.get(0));
						U.print(list.get(i));
					}
				}
			}
			if(needSynonmy){
				if(DBFunction.updateDianPingContent(id, content) > 0)
					U.print("idΪ" + id + "�ĵ���ͬ��ʹ鲢�ɹ�");
			}
		}
		U.print("�ѹ鲢ͬ���");
	}
	//��ѵ�����еĴ�Ҳ����ͬ��ʹ鲢
	public static void trainingSet_synonym(int version) throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromTrainingSet(version);
		List<List<String>> synonym = FileFunction.getSynonym();
		while(rs.next()){
			boolean needSynonmy = false;
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Keyword);
			for(List<String> list : synonym){
				for(int i = 0; i < list.size(); i++){
					if(i!=0 && content.contains(list.get(i))){
						needSynonmy = true;
						content = content.replaceAll(list.get(i), list.get(0));
						U.print(list.get(i));
					}
				}
			}
			if(needSynonmy){
				if(DBFunction.updateTrainingSetContent(version, id, content) > 0)
					U.print("idΪ" + id + "�ĵ���ͬ��ʹ鲢�ɹ�");
			}
		}
		U.print("�ѹ鲢ͬ���");
	}
	
	//��������ı�����nlpir����������´ʣ�Ȼ���˹�ɸѡ�����û��ʵ�
	public static void outputDianPingForFindNewWord() throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromDianPing();
		String outputContent = "";//�������
		for(int i = 0; true; i++){
			if(!rs.next()) break;
			String content = rs.getString(MyStatic.KEY_Content) + "\r\n";
			outputContent += content;
			if(i % MyStatic.SIZE_DianPingSet == 0){
				FileWriter fw = new FileWriter("E:\\work\\��ʿ��\\����_���ڷ����´�\\" + i + ".txt");
				fw.write(outputContent);
				outputContent = "";
				U.print("������������ļ���Ϊ:" + i + ".txt");
				fw.close();
			}
		}
	}
	
	//�����´ʣ����˹���ӽ��û��ʵ�
	public static void findNewWord() throws IOException{
		NLPIR.NlpirInit();
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < 35264 ; i+=MyStatic.SIZE_DianPingSet){
			String txtAddress = "E:\\work\\��ʿ��\\����_���ڷ����´�\\" + i + ".txt";
			String rawString = FileFunction.findNewWord_getDianPingSetString(txtAddress);
			String s = NLPIR.getNewWord(rawString);
			U.print(s);
			if(s != "" && !s.isEmpty())
				list.add(s);
		}
		FileFunction.findNewWord_outputNewWordRaw(list);
		U.print("�´ʷ��ִ������");
		NLPIR.NlpirExit();
	}
	
	//��ȡ��ӵ��´ʣ�ͳ�ƴ�Ƶ�����������txt
	public static void getNewWordFrequency() throws IOException{
		int[] size = {1, 10, 50, 100, 500, 1000, 5000};
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < size.length; i++){
			String txtAddress = "E:\\work\\��ʿ��\\vocabulary\\raw_newword" + size[i] + ".txt";
			list.addAll(FileFunction.findNewWord_getRawNewWord(txtAddress));
		}
		//ʹ��map�洢�´����Ƶ
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(int i = 0; i < list.size(); i++){
			String line = list.get(i);
			String[] words = line.substring(0, line.length() - 1).split("#");
			for(String word : words){
				int count = ((Integer)map.get(word) != null) ? (Integer)map.get(word)+1 : 1;
				map.put(word, count);
			}
		}
		ValueComparator vc = new ValueComparator(map);
		TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(vc);
		sortedMap.putAll(map);
		Set<Entry<String, Integer>> set = sortedMap.entrySet();
		//���´ʴ�Ƶͳ��д��txt
		FileFunction.findNewWord_outputNewWordFrequency(sortedMap);
	}
	
	//��ȡ�û��ʵ䲢���
	public static void addUserDicFromTxt() throws IOException{
		NLPIR.NlpirInit();
		File file = new File("E:\\work\\��ʿ��\\vocabulary\\userdic.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		while((line = reader.readLine()) != null){
			line = line.trim();
			String s[] = line.split("\t");
			NLPIR.addUserDict(s[0], s[1]);
			U.print(line);
		}
		CLibrary.Instance.NLPIR_SaveTheUsrDic();
		NLPIR.NlpirExit();
		U.print("����û��ʵ����");
	}
	
	
}
