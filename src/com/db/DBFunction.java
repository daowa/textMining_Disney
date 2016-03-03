package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.myClass.MyStatic;
import com.myClass.U;

public class DBFunction {
	
	private static String dbDriver="com.mysql.jdbc.Driver";   
    private static String dbUrl="jdbc:mysql://localhost:3306/disney?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static String dbUser="root";  
    private static String dbPass="abcd@123";
    
    private static Connection cnn=getConn();
	
    private static Connection getConn()  
    {  
        Connection conn=null;  
        try  
        {  
            Class.forName(dbDriver);  
        }  
        catch (ClassNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        try  
        {  
            conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);//ע������������  
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return conn;  
    }  
    
    //�������ԭʼ����
    public static void insertBatch(int category, List<List<String>> lists, String webFrom, String city){
    	int count = 0;
    	
    	if(category == MyStatic.Category_YouJi){
	    	for(int i = 0; i < lists.size(); i++){
	    		count += insertYouji(lists.get(i), webFrom, city);
	    	}
    	}
    	else if(category == MyStatic.Category_DianPing){
    		for(int i = 0; i < lists.size(); i++){
	    		count += insertDianPing(lists.get(i), webFrom, city);
	    	}
    	}
    	else if(category == MyStatic.Category_WenDa){
    		for(int i = 0; i < lists.size(); i++){
	    		count += insertWenDa(lists.get(i), webFrom, city);
	    	}
    	}
    	
    	System.out.println("������" + count + "����¼,����" + webFrom);
    }
    
    //���뵥��ԭʼ���Ͻ��� �μǱ�
    public static int insertYouji(List<String> list, String webFrom, String city){
    	int i = 0;
        String sql="insert into " + MyStatic.TABLE_rawYouJi + "(" + 
        		MyStatic.KEY_Title + "," + 
        		MyStatic.KEY_Content + "," + 
        		MyStatic.KEY_Author + "," + 
        		MyStatic.KEY_AuthroFrom + "," + 
        		MyStatic.KEY_Time + "," + 
        		MyStatic.KEY_Up + "," + 
        		MyStatic.KEY_Collect + "," + 
        		MyStatic.KEY_Comment + "," + 
        		MyStatic.KEY_Share + "," + 
        		MyStatic.KEY_WebFrom + "," + 
        		MyStatic.KEY_City + ") values(?,?,?,?,?,?,?,?,?,?,?)";  
        try{  
            PreparedStatement preStmt =cnn.prepareStatement(sql);  
            preStmt.setString(1, list.get(0));  
            preStmt.setString(2, list.get(1));
            preStmt.setString(3, list.get(2));
            preStmt.setString(4, list.get(3));
            preStmt.setString(5, list.get(4)!="" ? list.get(4) : "1970/01/01");
            
            preStmt.setInt(6, U.isNumeric(list.get(5)) ? Integer.parseInt(list.get(5)) : -1);
            preStmt.setInt(7, U.isNumeric(list.get(6)) ? Integer.parseInt(list.get(6)) : -1);
            preStmt.setInt(8, U.isNumeric(list.get(7)) ? Integer.parseInt(list.get(7)) : -1);
            preStmt.setInt(9, U.isNumeric(list.get(8)) ? Integer.parseInt(list.get(8)) : -1);
            preStmt.setString(10, webFrom);
            preStmt.setString(11, city);
            i=preStmt.executeUpdate();  
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return i;//����Ӱ���������1Ϊִ�гɹ�  
    }
    
    //���뵥��ԭʼ���Ͻ��� ������
    public static int insertDianPing(List<String> list, String webFrom, String city){
    	int i = 0;
        String sql="insert into " + MyStatic.TABLE_rawDianPing + "(" + 
        		MyStatic.KEY_Content + "," + 
        		MyStatic.KEY_Author + "," + 
        		MyStatic.KEY_Time + "," + 
        		MyStatic.KEY_Star + "," +
        		MyStatic.KEY_Up + "," + 
        		MyStatic.KEY_Scenery + "," + 
        		MyStatic.KEY_Interest + "," + 
        		MyStatic.KEY_CostPerformance + "," + 
        		MyStatic.KEY_WebFrom + "," + 
        		MyStatic.KEY_City + ") values(?,?,?,?,?,?,?,?,?,?)";  
        try{  
            PreparedStatement preStmt =cnn.prepareStatement(sql);  
            preStmt.setString(1, (webFrom == MyStatic.WebFrom_MaFengWo || webFrom == MyStatic.WebFrom_QiongYou) ? U.decodeUnicode(list.get(0)) : list.get(0));  
            preStmt.setString(2, (webFrom == MyStatic.WebFrom_MaFengWo || webFrom == MyStatic.WebFrom_QiongYou) ? U.decodeUnicode(list.get(1)) : list.get(1));
            preStmt.setString(3, list.get(2)!="" ? list.get(2) : "1970/01/01");
            
            preStmt.setInt(4, U.isNumeric(list.get(3)) ? Integer.parseInt(list.get(3)) : -1);
            preStmt.setInt(5, U.isNumeric(list.get(4)) ? Integer.parseInt(list.get(4)) : -1);
            preStmt.setInt(6, U.isNumeric(list.get(5)) ? Integer.parseInt(list.get(5)) : -1);
            preStmt.setInt(7, U.isNumeric(list.get(6)) ? Integer.parseInt(list.get(6)) : -1);
            preStmt.setInt(8, U.isNumeric(list.get(7)) ? Integer.parseInt(list.get(7)) : -1);
            preStmt.setString(9, webFrom);
            preStmt.setString(10, city);
            i=preStmt.executeUpdate();  
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return i;//����Ӱ���������1Ϊִ�гɹ�  
    }
    
    //���뵥��ԭʼ���Ͻ��� �ʴ��
    public static int insertWenDa(List<String> list, String webFrom, String city){
    	int i = 0;
        String sql="insert into " + MyStatic.TABLE_rawWenDa + "(" + 
        		MyStatic.KEY_Title + "," + 
        		MyStatic.KEY_Content + "," + 
        		MyStatic.KEY_Author + "," + 
        		MyStatic.KEY_Time + "," + 
        		MyStatic.KEY_Up + "," + 
        		MyStatic.KEY_UpContent + "," + 
        		MyStatic.KEY_WebFrom + "," + 
        		MyStatic.KEY_City + ") values(?,?,?,?,?,?,?,?)";  
        try{  
            PreparedStatement preStmt =cnn.prepareStatement(sql);  
            preStmt.setString(1, list.get(0));  
            preStmt.setString(2, list.get(1));
            preStmt.setString(3, list.get(2));
            
            //����20Сʱǰ����һ��ʱ����Ϊ��2016/03/01��
            String time = list.get(3);
            if(time == "") time = "1970/01/01";
            if(time.matches("\\d+Сʱǰ")) time = "2016/03/01";
            preStmt.setString(4, time);
            
            preStmt.setInt(5, U.isNumeric(list.get(4)) ? Integer.parseInt(list.get(4)) : -1);
            preStmt.setString(6, list.get(5));
            preStmt.setString(7, webFrom);
            preStmt.setString(8, city);
            i=preStmt.executeUpdate();  
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return i;//����Ӱ���������1Ϊִ�гɹ�  
    }
	
//	public static void select(){
//		sql = "select *from raw_youji";//SQL���  
//        db1 = new DBHelper(sql);//����DBHelper����  
//  
//        try {  
//            ret = db1.pst.executeQuery();//ִ����䣬�õ������  
//            while (ret.next()) {  
//                String uid = ret.getString(1);  
//                String ufname = ret.getString(2);  
//                String ulname = ret.getString(3);  
//                String udate = ret.getString(4);  
//                System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
//            }//��ʾ����  
//            ret.close();  
//            db1.close();//�ر�����  
//        } catch (SQLException e) {  
//            e.printStackTrace();  
//        }
//	}

}
