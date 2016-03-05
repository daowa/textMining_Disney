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
		ResultSet rs = DBFunction.selectNotHongKongFromYouJi();
		try {
			int resetCount = 0;
			while(rs.next()){
				String title = rs.getString(MyStatic.KEY_Title);
				String content = rs.getString(MyStatic.KEY_Content);
				String city = rs.getString(MyStatic.KEY_City);
				String s = title + content;
				
				//���ڱ������ʾ���������ģ���Ϊƥ�䣬�����н�һ������ƥ��
				if(title.matches(".*" + city + ".*")) continue;
				//��Щ�б�־�Եĳ��п���ֱ�Ӵ���
				if(title.matches(".*���.*") || title.matches(".*����.*")){
					if(city.equals(MyStatic.City_HongKong))
						continue;
					else {
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_HongKong);
//						resetCount++;
//						U.print("��" + resetCount + "�����ó���");
						continue;
					}
				}
				if(title.matches(".*����.*") || title.matches(".*��ɼ�.*") || title.matches(".*LA.*")){
					if(city.equals(MyStatic.City_California))
						continue;
					else {
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_California);
//						resetCount++;
//						U.print("��" + resetCount + "�����ó���");
						continue;
					}
				}
				if((title.matches(".*�ձ�.*") || title.matches(".*�޺�.*") || title.matches(".*ӣ��.*") || title.matches(".*����.*"))){
					if(city.equals(MyStatic.City_Tokyo))
						continue;
					else {
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Tokyo);
//						resetCount++;
//						U.print("��" + resetCount + "�����ó���");
						continue;
					}
				}
				if(title.matches(".*����.*") || title.matches(".*����.*") || title.matches(".*ŷ��.*") || title.matches(".*(E|e)urope.*")){
					if(city.equals(MyStatic.City_Paris))
						continue;
					else {
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Paris);
//						resetCount++;
//						U.print("��" + resetCount + "�����ó���");
						continue;
					}
				}
				if(title.matches(".*������.*") || title.matches(".*�������.*") || title.matches(".*Florida.*")){
					if(city.equals(MyStatic.City_Orlando))
						continue;
					else {
//						DBFunction.updateYouJiCity(rs.getInt(MyStatic.KEY_ID_rawYouJi), MyStatic.TABLE_YouJi, MyStatic.City_Orlando);
//						resetCount++;
//						U.print("��" + resetCount + "�����ó���");
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
					U.print("���������ָ��:c-����;h-���;o-������;p-����;t-����,ֱ�ӻس���ʾnext");
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
//						U.print("��" + resetCount + "�����ó���");
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
