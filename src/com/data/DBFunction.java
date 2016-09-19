package com.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
        return i;//����Ӱ���������1Ϊִ�гɹ�  
    }
    
    //���뵥��ԭʼ���Ͻ��� ������
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
        return i;//����Ӱ���������1Ϊִ�гɹ�  
    }
    
    //���뵥��ԭʼ���Ͻ��� �ʴ��
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
    
    public static ResultSet selectAll(int category){
    	String sql = "";
    	if(category == MyStatic.Category_YouJi)
    		sql = "select * from " + MyStatic.TABLE_YouJi;
    	else if(category == MyStatic.Category_DianPing)
    		sql = "select * from " + MyStatic.TABLE_DianPing;
    	else if(category == MyStatic.Category_WenDa)
    		sql = "select * from " + MyStatic.TABLE_WenDa;
		try {
			Statement stmt = cnn.createStatement(); 
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return null;  
    }
    
    //���ݿ��й��ж�������¼
    public static int getRowsCount(int category){
    	String sql = "";
    	if(category == MyStatic.Category_YouJi)
    		sql = "select count(*) as rowCount from " + MyStatic.TABLE_YouJi;
    	else if(category == MyStatic.Category_DianPing)
    		sql = "select count(*) as rowCount from " + MyStatic.TABLE_DianPing;
    	else if(category == MyStatic.Category_WenDa)
    		sql = "select count(*) as rowCount from " + MyStatic.TABLE_WenDa;
    	int count = 0;
    	try{
    		Statement stmt = cnn.createStatement();//����������������е�ָ������ƶ�
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("rowCount");
    	} catch (Exception e) {
			e.printStackTrace();
		}  
    	return count;
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
    
    //ѡ�����۵��μǣ������������μ��д�ֵĿ���
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
            	U.print("ɾ���ɹ�: id=" + id);
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
    }  
    
    public static void updateYouJiCity(int id, String table, String newCity){
        String sql="update " + MyStatic.TABLE_YouJi + " set " + MyStatic.KEY_City + "=? where " + MyStatic.KEY_ID_rawYouJi + " =?";//ע��Ҫ��where���� 

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
        	U.print("��id" + id + "�ĳ����޸�Ϊ" + newCity);
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
    		Statement stmt = cnn.createStatement();//����������������е�ָ������ƶ�
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("rowCount");
    	} catch (Exception e) {
			e.printStackTrace();
		}  
    	return count;
    }
    
    public static int updateContent(int category, int id, String content){
    	String sql = "";
    	if(category == MyStatic.Category_DianPing)
    		sql = "update " + MyStatic.TABLE_DianPing + " set " + MyStatic.KEY_Content + " =? where " + MyStatic.KEY_ID_rawDianPing + " =?";
    	else if(category == MyStatic.Category_YouJi)
    		sql = "update " + MyStatic.TABLE_YouJi + " set " + MyStatic.KEY_Content + " =? where " + MyStatic.KEY_ID_rawYouJi + " =?";
    	int count = 0;	
    	try{
    		PreparedStatement ps = cnn.prepareStatement(sql);
    		ps.setString(1, content);
    		ps.setInt(2, id);
    		count = ps.executeUpdate();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return count;
    }
    
    //�����м����ݿ�
    public static int insertMiddle(int category, int id, String stats) throws SQLException{
    	int i = 0;
    	String sql = "";
    	if(category == MyStatic.Category_YouJi)
    		sql = "insert into " + MyStatic.TABLE_Middle_YouJi + "(" + MyStatic.KEY_ID_rawYouJi + "," + MyStatic.KEY_Stats + ") value(?,?)";
    	else if(category == MyStatic.Category_DianPing)
    		sql = "insert into " + MyStatic.TABLE_Middle_DianPing + "(" + MyStatic.KEY_ID_rawDianPing + "," + MyStatic.KEY_Stats + ") value(?,?)";
    	PreparedStatement ps = cnn.prepareStatement(sql);
    	ps.setInt(1, id);
    	ps.setString(2, stats);
    	i = ps.executeUpdate();
    	return i;
    }
    
    
    //�����ȡn�������������˹�����
    //��һ����������������
    //�ڶ�����������ʼid
    //����������������id+1
    public static ResultSet getRandomContent(int category, int n, int startID, int endID) throws SQLException{
    	String table = (category == MyStatic.Category_YouJi) ? MyStatic.TABLE_YouJi : MyStatic.TABLE_DianPing;
    	String keyID = (category == MyStatic.Category_YouJi) ? MyStatic.KEY_ID_rawYouJi : MyStatic.KEY_ID_rawDianPing;
    	String sql = "select * from " + table + " where " + keyID + " in "
    			+ U.getRandom_String(U.getRandom(n, startID, endID)) + ";";
    	Statement stmt = cnn.createStatement();
    	ResultSet rs = stmt.executeQuery(sql);
    	return rs;
    }
    //ѡ��ô������ĵ���֮ǰ��û�б�����
    public static boolean isIndexed(int category, int id) throws SQLException{
    	String table = (category == MyStatic.Category_YouJi) ? MyStatic.TABLE_TrainingSet_YouJi : MyStatic.TABLE_TrainingSet_DianPing;
    	String keyID = (category == MyStatic.Category_YouJi) ? MyStatic.KEY_ID_rawYouJi : MyStatic.KEY_ID_rawDianPing;
    	String sql = "select * from " + table + " where " + keyID + " =?";
    	PreparedStatement ps = cnn.prepareStatement(sql);
    	ps.setInt(1, id);
    	ResultSet rs = ps.executeQuery();
    	if(rs.next())//�����id������ԭ���У���˵��������
    		return true;
    	return false;
    }
    
    //����id���м����ݿ��ȡ�ü�¼�Ĵ�Ƶ��tf-idf�����Ե���������
    public static Map<String, Vector<String>> getStats(int category, int id) throws SQLException{
    	String table = (category == MyStatic.Category_YouJi) ? MyStatic.TABLE_Middle_YouJi : MyStatic.TABLE_Middle_DianPing;
    	String keyID = (category == MyStatic.Category_YouJi) ? MyStatic.KEY_ID_rawYouJi : MyStatic.KEY_ID_rawDianPing;
    	String sql = "select * from " + table + " where " + keyID + " = " + id;
    	Statement stmt = cnn.createStatement();
    	ResultSet rs = stmt.executeQuery(sql);
    	rs.next();
    	String stats = rs.getString(MyStatic.KEY_Stats);
    	Map<String, Vector<String>> map = U.string2Map(stats);
    	return map;
    }
    
    //�������Ĺؼ���д�����ݿ�
    public static int insertTrainingSet(int category, int id, String[] keywords) throws SQLException{
    	int i = 0;
    	String table = (category == MyStatic.Category_YouJi) ? MyStatic.TABLE_TrainingSet_YouJi : MyStatic.TABLE_TrainingSet_DianPing;
    	String keyID = (category == MyStatic.Category_YouJi) ? MyStatic.KEY_ID_rawYouJi : MyStatic.KEY_ID_rawDianPing;
    	String sql = "insert into " + table + "(" + keyID + "," + MyStatic.KEY_Keyword + ") value(?,?)";
    	String s_keywords = "";//��keywords��listת���� a,b,c ����ʽ�������´γ����ʱ��ĵ���
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
    
    //����ѵ����������
    public static int updateTrainingSetContent(int category, int id, String content){
    	String table = (category == MyStatic.Category_YouJi) ? MyStatic.TABLE_TrainingSet_YouJi : MyStatic.TABLE_TrainingSet_DianPing;
    	String keyID = (category == MyStatic.Category_YouJi) ? MyStatic.KEY_ID_rawYouJi : MyStatic.KEY_ID_rawDianPing;
    	String sql = "update " + table + " set " + MyStatic.KEY_Keyword + " =? where " + keyID + " =?";
    	int count = 0;
    	try{
    		PreparedStatement ps = cnn.prepareStatement(sql);
    		ps.setString(1, content);
    		ps.setInt(2, id);
    		count = ps.executeUpdate();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return count;
    }

    //��ȡ�����Ĳ��Լ������е����м�¼
    public static ResultSet selectAllFromTrainingSet(int category){
    	String table = (category == MyStatic.Category_YouJi) ? MyStatic.TABLE_TrainingSet_YouJi : MyStatic.TABLE_TrainingSet_DianPing;
    	String sql = "select * from " + table;  
		try {
			Statement stmt = cnn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return null;  
    }
    
    //��ȡmiddle�е����м�¼
    public static ResultSet selectAllFromMiddle(int category){
    	String sql = "";
    	if(category == MyStatic.Category_YouJi)
    		sql = "select * from " + MyStatic.TABLE_Middle_YouJi;
    	else if(category == MyStatic.Category_DianPing)
    		sql = "select * from " + MyStatic.TABLE_Middle_DianPing;
    	try {
			Statement stmt = cnn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return null;  
    }
    
    //����id��middle���л�ȡ����ֵ
    public static String getFeature(int category, int id){
    	String table = (category == MyStatic.Category_YouJi) ? MyStatic.TABLE_Middle_YouJi : MyStatic.TABLE_Middle_DianPing;
    	String keyID = (category == MyStatic.Category_YouJi) ? MyStatic.KEY_ID_rawYouJi : MyStatic.KEY_ID_rawDianPing;
    	String sql = "select * from " + table + " where " + keyID + " = " + id;
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
    
    //��ȡ���йؼ���
    public static List<String> getKeyword(){
    	String sql = "SELECT * FROM disney.keywords";
    	List<String> result = new ArrayList<String>();
    	try {
    		Statement stmt = cnn.createStatement();
    		ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			result.add(rs.getString(MyStatic.KEY_ID_rawDianPing) + "\t" + rs.getString(MyStatic.KEY_Keyword));
    		}
    		
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		return result;
    }
    
    //���ݵ�����keywords���ж�ȡ�ؼ���
    public static List<String> getKeywordByAddress(String city){
    	String sql = "SELECT * FROM disney.clean_dianping inner join disney.keywords on disney.clean_dianping.idraw_dianping = disney.keywords.idraw_dianping where city = \""
    			+ city + "\";";
    	List<String> result = new ArrayList<String>();
    	try {
    		Statement stmt = cnn.createStatement();
    		ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			result.add(rs.getString(MyStatic.KEY_Keyword));
    		}
    		
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		return result;
    }
    
    //�����·ݴ�keywords���ж�ȡ�ؼ���
    public static List<String> getKeywordByMonth(int month){
    	String sql = "SELECT * FROM disney.clean_dianping inner join disney.keywords on disney.clean_dianping.idraw_dianping = disney.keywords.idraw_dianping where month(time) = "
    			+ month + ";";
    	List<String> result = new ArrayList<String>();
    	try {
    		Statement stmt = cnn.createStatement();
    		ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			result.add(rs.getString(MyStatic.KEY_Keyword));
    		}
    		
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		return result;
    }
    
    //����id��ȡ����
    public static String getCity(int id){
    	String sql = "Select * From disney.clean_dianping where idraw_dianping = " + id;
    	String result = "";
    	try {
    		Statement stmt = cnn.createStatement();
    		ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			result = rs.getString(MyStatic.KEY_City);
    		}
    		
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		return result;
    }
}
