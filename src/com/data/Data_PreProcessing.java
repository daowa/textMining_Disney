package com.data;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.db.DBFunction;
import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;

public class Data_PreProcessing {

	public static void readRawTextAndInsertDB(int category, String categoryAddress){
		
		File file = new File(categoryAddress);
		String[] webList = file.list();
		String[] cityList = {MyStatic.City_HongKong, MyStatic.City_California, MyStatic.City_Orlando, MyStatic.City_Paris, MyStatic.City_Tokyo};
			
//			for(int i = 0; i < webList.length; i++){
//				for(int j = 0; j < cityList.length; j++){
//					String fileAddress = categoryAddress + "\\" + webList[i] + "\\" + cityList[j];
//					List<List<String>> lists = FileFunction.readTxtInFile(fileAddress);
//					DBFunction.insertBatch(category, lists, webList[i], cityList[j]);
//				}
//			}
			
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
		ResultSet rs = DBFunction.selectNotHongKongFromYouJi();
		try {
			int resetCount = 0;
			while(rs.next()){
				String title = rs.getString(MyStatic.KEY_Title);
				String content = rs.getString(MyStatic.KEY_Content);
				String city = rs.getString(MyStatic.KEY_City);
				String s = title + content;
				
				//对于标题就显示出城市名的，认为匹配，不进行进一步正则匹配
				if(title.matches(".*" + city + ".*")) continue;
				//有些有标志性的城市可以直接处理
				if(title.matches(".*香港.*") || title.matches(".*澳门.*")){
					if(city.equals(MyStatic.City_HongKong))
						continue;
					else {
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_HongKong);
//						resetCount++;
//						U.print("第" + resetCount + "个重置城市");
						continue;
					}
				}
				if(title.matches(".*加州.*") || title.matches(".*洛杉矶.*") || title.matches(".*LA.*")){
					if(city.equals(MyStatic.City_California))
						continue;
					else {
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_California);
//						resetCount++;
//						U.print("第" + resetCount + "个重置城市");
						continue;
					}
				}
				if((title.matches(".*日本.*") || title.matches(".*霓虹.*") || title.matches(".*樱花.*") || title.matches(".*东京.*"))){
					if(city.equals(MyStatic.City_Tokyo))
						continue;
					else {
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Tokyo);
//						resetCount++;
//						U.print("第" + resetCount + "个重置城市");
						continue;
					}
				}
				if(title.matches(".*法国.*") || title.matches(".*巴黎.*") || title.matches(".*欧洲.*") || title.matches(".*(E|e)urope.*")){
					if(city.equals(MyStatic.City_Paris))
						continue;
					else {
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Paris);
//						resetCount++;
//						U.print("第" + resetCount + "个重置城市");
						continue;
					}
				}
				if(title.matches(".*奥兰多.*") || title.matches(".*佛罗里达.*") || title.matches(".*Florida.*")){
					if(city.equals(MyStatic.City_Orlando))
						continue;
					else {
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Orlando);
//						resetCount++;
//						U.print("第" + resetCount + "个重置城市");
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
					U.print("请输入操作指令:c-加州;h-香港;o-奥兰多;p-巴黎;t-东京,直接回车表示next");
//					
					Scanner sc = new Scanner(System.in);
					String order = sc.nextLine();
					String newCity = "";
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
//					
//					if(!newCity.equals("")){
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, newCity);
//						resetCount++;
//						U.print("第" + resetCount + "个重置城市");
//					}
					resetCount ++;
				}
			}
			U.print(resetCount + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
