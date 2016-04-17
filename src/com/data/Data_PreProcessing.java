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
	
	//蚂蜂窝有些游记里面并没有到迪士尼
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
				if(!s.matches(".*迪.尼.*") && !s.matches(".*(d|D)isney.*") && !s.matches(".*米老鼠.*")
						 && !s.matches(".*唐老鸭.*") && !s.matches(".*米奇.*") && !s.matches(".*城堡.*")
						 && !s.matches(".*进园.*")  && !s.matches(".*入园.*")  && !s.matches(".*虫虫.*")
						 && !s.matches(".*fastpass.*") && !s.matches(".*乐园.*")){
					U.print("标题：" + title);
					U.print("正文：" + content);
					U.print("请输入操作指令,d表示删除，直接回车表示next");
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
	
	//蚂蜂窝有些游记的城市放错了
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
				
				//对于标题就显示出城市名的，认为匹配，不进行进一步正则匹配
				if(title.matches(".*" + city + ".*")) continue;
				//有些有标志性的城市可以直接处理
				if(title.matches(".*香港.*") || title.matches(".*澳门.*") || title.matches(".*港澳.*") || title.matches(".*(H|h)ong(K|k)ong.*")
						 || title.matches(".*HK.*")){
					if(city.equals(MyStatic.City_HongKong))
						continue;
					else {
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_HongKong);
						resetCount++;
						U.print("第" + resetCount + "个重置城市");
						continue;
					}
				}
				if(title.matches(".*加州.*") || title.matches(".*洛杉矶.*") || title.matches(".*LA.*")){
					if(city.equals(MyStatic.City_California))
						continue;
					else {
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_California);
						resetCount++;
						U.print("第" + resetCount + "个重置城市");
						continue;
					}
				}
				if(title.matches(".*日本.*") || title.matches(".*霓虹.*") || title.matches(".*樱花.*") || title.matches(".*东京.*")
						 || title.matches(".*(T|t)okyo.*") || title.matches(".*東京.*")){
					if(city.equals(MyStatic.City_Tokyo))
						continue;
					else {
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Tokyo);
						resetCount++;
						U.print("第" + resetCount + "个重置城市");
						continue;
					}
				}
				if(title.matches(".*法国.*") || title.matches(".*巴黎.*") || title.matches(".*欧洲.*") || title.matches(".*(E|e)urope.*")){
					if(city.equals(MyStatic.City_Paris))
						continue;
					else {
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Paris);
						resetCount++;
						U.print("第" + resetCount + "个重置城市");
						continue;
					}
				}
				if(title.matches(".*奥兰多.*") || title.matches(".*佛罗里达.*") || title.matches(".*(F|f)lorida.*") || title.matches(".*(O|o)rlando.*")){
					if(city.equals(MyStatic.City_Orlando))
						continue;
					else {
						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Orlando);
						resetCount++;
						U.print("第" + resetCount + "个重置城市");
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
					U.print("标题：" + title);
					U.print("正文：" + content);
					U.print("城市：" + city);
					U.print(">>请输入操作指令:c-加州;h-香港;o-奥兰多;p-巴黎;t-东京,d表示删除,直接回车表示next");
					
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
						U.print("第" + resetCount + "个重置城市");
					}
				}
			}
			U.print("重置" + resetCount);
			U.print("删除" + deleteCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//将点评中的繁体字转为简体字
	public static void dianping_ZHConverter() throws SQLException{
		ResultSet rs = DBFunction.selectAllFromDianPing();
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String rawContent = rs.getString(MyStatic.KEY_Content);
			String newContent = U.ZHConverter_TraToSim(rawContent);
			if(DBFunction.updateDianPingContent(id, newContent) > 0)
				U.print("id为" + id + "的点评修改成功");
		}
		U.print("已将点评中的繁体字转换为简体字");
	}
	//将点评中一些时间、表情等去除
	public static void dianping_deleteTimeAndOthers() throws SQLException{
		ResultSet rs = DBFunction.selectAllFromDianPing();
		while(rs.next()){
			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
			String content = rs.getString(MyStatic.KEY_Content);
			//去除形如2013-09-1813:32:00格式的时间
			content = content.replaceAll("[0-9]{4}-[0-9]{2}-[0-9]{4}:[0-9]{2}:[0-9]{2}", "");
			//去除形如Day3格式的日期
			content = content.replaceAll("Day[0-9]", "");
			//去除形如[大笑]的表情
			content = content.replaceAll("\\[[\u4E00-\u9FA5]{2}\\]", "");
			//去除形如5-24的数字格式（日期或地区）s
			content = content.replaceAll("[0-9]{1,2}-[0-9]{1,2}", "");
			//去除"阅读全部"等词
			content = content.replaceAll("阅读全部", "");
			//去除如下字母
			content = content.replaceAll("&#183;", "");
			//去除形如 评论(0) 的字母
			content = content.replaceAll("评论([0-9]*)", "");
			content = content.replaceAll("分享([0-9]*)", "");
			content = content.replaceAll("喜欢([0-9]*)", "");
			if(DBFunction.updateDianPingContent(id, content) > 0)
				U.print("id为" + id + "的点评修改成功");
		}
		U.print("已将点评中一些时间、表情等去除");
	}
	//同义词归并
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
					U.print("id为" + id + "的点评同义词归并成功");
			}
		}
		U.print("已归并同义词");
	}
	//对训练集中的词也进行同义词归并
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
					U.print("id为" + id + "的点评同义词归并成功");
			}
		}
		U.print("已归并同义词");
	}
	
	//输出点评文本，用nlpir的软件发现新词，然后人工筛选加入用户词典
	public static void outputDianPingForFindNewWord() throws SQLException, IOException{
		ResultSet rs = DBFunction.selectAllFromDianPing();
		String outputContent = "";//输出内容
		for(int i = 0; true; i++){
			if(!rs.next()) break;
			String content = rs.getString(MyStatic.KEY_Content) + "\r\n";
			outputContent += content;
			if(i % MyStatic.SIZE_DianPingSet == 0){
				FileWriter fw = new FileWriter("E:\\work\\迪士尼\\点评_用于发现新词\\" + i + ".txt");
				fw.write(outputContent);
				outputContent = "";
				U.print("输出点评集，文件名为:" + i + ".txt");
				fw.close();
			}
		}
	}
	
	//发现新词，需人工添加进用户词典
	public static void findNewWord() throws IOException{
		NLPIR.NlpirInit();
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < 35264 ; i+=MyStatic.SIZE_DianPingSet){
			String txtAddress = "E:\\work\\迪士尼\\点评_用于发现新词\\" + i + ".txt";
			String rawString = FileFunction.findNewWord_getDianPingSetString(txtAddress);
			String s = NLPIR.getNewWord(rawString);
			U.print(s);
			if(s != "" && !s.isEmpty())
				list.add(s);
		}
		FileFunction.findNewWord_outputNewWordRaw(list);
		U.print("新词发现处理完成");
		NLPIR.NlpirExit();
	}
	
	//读取添加的新词，统计词频并排序输出到txt
	public static void getNewWordFrequency() throws IOException{
		int[] size = {1, 10, 50, 100, 500, 1000, 5000};
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < size.length; i++){
			String txtAddress = "E:\\work\\迪士尼\\vocabulary\\raw_newword" + size[i] + ".txt";
			list.addAll(FileFunction.findNewWord_getRawNewWord(txtAddress));
		}
		//使用map存储新词与词频
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
		//将新词词频统计写入txt
		FileFunction.findNewWord_outputNewWordFrequency(sortedMap);
	}
	
	//读取用户词典并添加
	public static void addUserDicFromTxt() throws IOException{
		NLPIR.NlpirInit();
		File file = new File("E:\\work\\迪士尼\\vocabulary\\userdic.txt");
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
		U.print("添加用户词典完成");
	}
	
	
}
