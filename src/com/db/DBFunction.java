package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
            conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);//注意是三个参数  
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return conn;  
    }  
    
    //插入多条原始资料
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
    	
    	System.out.println("共插入" + count + "条记录,来自" + webFrom);
    }
    
    //插入单条原始资料进入 游记表
    public static int insertYouji(List<String> list, String webFrom, String city){
    	int i = 0;
        String sql="insert into " + MyStatic.TABLE_YouJi + "(" + 
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
        return i;//返回影响的行数，1为执行成功  
    }
    
    //插入单条原始资料进入 点评表
    public static int insertDianPing(List<String> list, String webFrom, String city){
    	int i = 0;
        String sql="insert into " + MyStatic.TABLE_DianPing + "(" + 
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
//            (webFrom == MyStatic.WebFrom_MaFengWo || webFrom == MyStatic.WebFrom_QiongYou) ? U.decodeUnicode(list.get(0)) : list.get(0)
            preStmt.setString(1, U.decodeUnicode(list.get(0)));  
            preStmt.setString(2, U.decodeUnicode(list.get(1)));
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
        return i;//返回影响的行数，1为执行成功  
    }
    
    //插入单条原始资料进入 问答表
    public static int insertWenDa(List<String> list, String webFrom, String city){
    	int i = 0;
        String sql="insert into " + MyStatic.TABLE_WenDa + "(" + 
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
            
            //将“20小时前”这一类时间置为“2016/03/01”
            String time = list.get(3);
            if(time == "") time = "1970/01/01";
            if(time.matches("\\d+小时前")) time = "2016/03/01";
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
        return i;//返回影响的行数，1为执行成功  
    }
	
    public static ResultSet selectAllFromYouJi(){
    	String sql = "select * from " + MyStatic.TABLE_YouJi;  
		try {
			Statement stmt = cnn.createStatement(); 
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return null;  
    }
    
    //选择非香港的游记，其它地区的游记有错分的可能
    public static ResultSet selectNotHongKongFromYouJi(){
    	String sql = "select * from " + MyStatic.TABLE_YouJi + " where " + MyStatic.KEY_City + " != \"" + MyStatic.City_HongKong + "\"";
		try {
			Statement stmt = cnn.createStatement(); 
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return null;  
    }
    
    public static void delete(int id, String table){
    	
    	String idRaw = "";
    	if(table == MyStatic.TABLE_YouJi) idRaw = MyStatic.KEY_ID_rawYouJi;
    	else if(table == MyStatic.TABLE_DianPing) idRaw = MyStatic.KEY_ID_rawDianPing;
    	else if(table == MyStatic.TABLE_WenDa) idRaw = MyStatic.KEY_ID_rawWenDa;
    	String sql = "delete from " + table + " where " + idRaw + " = " + id;
    	
        try  
        {  
            Statement stmt = cnn.createStatement();  
            int i = stmt.executeUpdate(sql);
            if(i > 0)
            	U.print("删除成功: id=" + id);
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
    }  
    
    public static void updateYouJiCity(int id, String table, String newCity){
        String sql="update " + MyStatic.TABLE_YouJi + " set " + MyStatic.KEY_City + "=? where " + MyStatic.KEY_ID_rawYouJi + " =?";//注意要有where条件 

        int i=0;
        try{  
            PreparedStatement preStmt =cnn.prepareStatement(sql);
            preStmt.setString(1, newCity);
            preStmt.setInt(2, id);
            i=preStmt.executeUpdate();
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }
        if(i > 0)
        	U.print("将id" + id + "的城市修改为" + newCity);
    }
    
    
    public static ResultSet selectAllFromDianPing(){
    	String sql = "select * from " + MyStatic.TABLE_DianPing;  
		try {
			Statement stmt = cnn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return null;  
    }
    
    public static int getDianPingCount(){
    	String sql = "select count(*) as rowCount from " + MyStatic.TABLE_DianPing;
    	int count = 0;
    	try{
    		Statement stmt = cnn.createStatement();//两个参数来结果集中的指针可以移动
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("rowCount");
    	} catch (Exception e) {
			e.printStackTrace();
		}  
    	return count;
    }
    
    //插入中间数据库
    public static int insertMiddle(int id, String stats) throws SQLException{
    	int i = 0;
    	String sql = "insert into " + MyStatic.TABLE_Middle + "(" + MyStatic.KEY_ID_rawDianPing + "," + MyStatic.KEY_Stats + ") value(?,?)";
    	PreparedStatement ps = cnn.prepareStatement(sql);
    	ps.setInt(1, id);
    	ps.setString(2, stats);
    	i = ps.executeUpdate();
    	return i;
    }
    
    //随机获取n条点评，进行人工标引
    //第一个参数：几条点评
    //第二个参数：起始id
    //第三个参数：结束id+1
    public static ResultSet getRandomDianPing(int n, int startID, int endID) throws SQLException{
    	String sql = "select * from " + MyStatic.TABLE_DianPing + " where " + MyStatic.KEY_ID_rawDianPing + " in "
    			+ U.getRandom_String(U.getRandom(n, startID, endID)) + ";";
    	Statement stmt = cnn.createStatement();
    	ResultSet rs = stmt.executeQuery(sql);
    	return rs;
    }
    
    //根据id从中间数据库获取该记录的词频、tf-idf、词性等特征数据
    public static Map<String, Vector<String>> getDianPingStats(int id) throws SQLException{
    	String sql = "select * from " + MyStatic.TABLE_Middle + " where " + MyStatic.KEY_ID_rawDianPing + " = " + id;
    	Statement stmt = cnn.createStatement();
    	ResultSet rs = stmt.executeQuery(sql);
    	rs.next();
    	String stats = rs.getString(MyStatic.KEY_Stats);
    	Map<String, Vector<String>> map = U.string2Map(stats);
    	return map;
    }
    
    //将标引的关键词写入数据库
    public static int insertTrainingSet(int id, String[] keywords) throws SQLException{
    	int i = 0;
    	String sql = "insert into " + MyStatic.TABLE_TrainingSet + "(" + MyStatic.KEY_ID_rawDianPing + "," + MyStatic.KEY_Keyword + ") value(?,?)";
    	String s_keywords = "";//将keywords的list转化成 a,b,c 的形式，便于下次出库的时候的调用
    	for(String word : keywords)
    		s_keywords += word + ",";
    	s_keywords = s_keywords.substring(0, s_keywords.length()-1);
    	U.print(s_keywords);
    	PreparedStatement ps = cnn.prepareStatement(sql);
    	ps.setInt(1, id);
    	ps.setString(2, s_keywords);
    	i = ps.executeUpdate();
    	return i;
    }

    //获取标引的测试集数据中的所有记录
    public static ResultSet selectAllFromTrainingSet(){
    	String sql = "select * from " + MyStatic.TABLE_TrainingSet;  
		try {
			Statement stmt = cnn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return null;  
    }
    
    //根据id从middle表中获取特征值
    public static String getFeature(int id){
    	String sql = "select * from " + MyStatic.TABLE_Middle + " where " + MyStatic.KEY_ID_rawDianPing + " = " + id;
    	String result = "";
    	try {
    		Statement stmt = cnn.createStatement();
    		ResultSet rs = stmt.executeQuery(sql);
    		if(rs.next())
    			result = rs.getString(MyStatic.KEY_Stats);
    		else
    			result = "nothing";
    		
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		return result;
     }
}
