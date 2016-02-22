package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.myClass.MyStatic;

public class DBFunction {
	
	private static String dbDriver="com.mysql.jdbc.Driver";   
    private static String dbUrl="jdbc:mysql://localhost:3306/disney?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static String dbUser="root";  
    private static String dbPass="abcd@123";
	
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
            conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);//注意是三个参数  
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return conn;  
    }  
    
    //插入多条原始资料进入游记表
    public static void insertYouji_batch(List<List<String>> lists, String webFrom){
    	int count = 0;
    	for(int i = 0; i < lists.size(); i++){
    		count += insertYouji(lists.get(i), webFrom);
    	}
    	System.out.println("共插入" + count + "条记录");
    }
    
    //插入单条原始资料进入游记表
    public static int insertYouji(List<String> list, String webFrom){
    	Connection cnn=getConn();
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
        		MyStatic.KEY_WebFrom + ") values(?,?,?,?,?,?,?,?,?,?)";  
        try{  
            PreparedStatement preStmt =cnn.prepareStatement(sql);  
            preStmt.setString(1, list.get(0));  
            preStmt.setString(2, list.get(1));
            preStmt.setString(3, list.get(2));
            preStmt.setString(4, list.get(3));
            preStmt.setString(5, list.get(4));
            preStmt.setInt(6, list.get(5)=="" ? -1 : Integer.parseInt(list.get(5)));
            preStmt.setInt(7, list.get(6)=="" ? -1 : Integer.parseInt(list.get(7)));
            preStmt.setInt(8, list.get(7)=="" ? -1 : Integer.parseInt(list.get(8)));
            preStmt.setInt(9, list.get(8)=="" ? -1 : Integer.parseInt(list.get(9)));
            preStmt.setString(10, webFrom);
            i=preStmt.executeUpdate();  
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return i;//返回影响的行数，1为执行成功  
    }
	
//	public static void select(){
//		sql = "select *from raw_youji";//SQL语句  
//        db1 = new DBHelper(sql);//创建DBHelper对象  
//  
//        try {  
//            ret = db1.pst.executeQuery();//执行语句，得到结果集  
//            while (ret.next()) {  
//                String uid = ret.getString(1);  
//                String ufname = ret.getString(2);  
//                String ulname = ret.getString(3);  
//                String udate = ret.getString(4);  
//                System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
//            }//显示数据  
//            ret.close();  
//            db1.close();//关闭连接  
//        } catch (SQLException e) {  
//            e.printStackTrace();  
//        }
//	}

}
