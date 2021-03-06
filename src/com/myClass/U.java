package com.myClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spreada.utils.chinese.ZHConverter;

public class U {
	
	//判断是否是数字
	public static boolean isNumeric(String str){
		if(str == "") return false;
		for(int i=str.length();--i>=0;){
		    int chr=str.charAt(i);
		    if(chr<48 || chr>57) return false;
		}
		return true;
	}
	
	//解析unicode码
	public static String decodeUnicode(String theString) {
		char aChar;      
	    int len = theString.length();      
	    String outBuffer = ""; 
	    for (int x = 0; x < len;) {      
	    	aChar = theString.charAt(x++);      
	    	if (aChar == '\\') {      
	    		aChar = theString.charAt(x++);      
	    		if (aChar == 'u') {      
	    			// Read the xxxx      
	    			int value = 0;      
	    			for (int i = 0; i < 4; i++) {      
	    				aChar = theString.charAt(x++);      
	    				switch (aChar) {      
	    				case '0':      
	    				case '1':    
	    				case '2':      
	    				case '3':      
	    				case '4':      
	    				case '5':      
	    				case '6':      
	    				case '7':      
	    				case '8':      
	    				case '9':      
	    					value = (value << 4) + aChar - '0';      
	    					break;      
	    				case 'a':      
	    				case 'b':      
	    				case 'c':      
	    				case 'd':      
	    				case 'e':      
	    				case 'f':      
	    					value = (value << 4) + 10 + aChar - 'a';      
	    					break;      
	    				case 'A':      
	    				case 'B':      
	    				case 'C':      
	    				case 'D':      
	    				case 'E':      
	    				case 'F':      
	    					value = (value << 4) + 10 + aChar - 'A';      
	    					break;      
	    				default:      
	    					throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");      
	    				}      
	    			}      
	    			outBuffer += (char)value;      
	    		} 
	    		else {      
	    			if (aChar == 't')      
	    				aChar = '\t';      
	    			else if (aChar == 'r')      
	    				aChar = '\r';      
	    			else if (aChar == 'n')      
	    				aChar = '\n';      
	    			else if (aChar == 'f')      
	    				aChar = '\f';      
	    			outBuffer += aChar;      
	    		}      
	    	} 
	    	else     
	    		outBuffer += aChar;     
	    }      
	    return outBuffer.toString();      
	}     
		
	public static void print(String s){
		System.out.println(s);
	}
	public static void print(String[] ss){
		String result = "";
		for(String s : ss){
			result += s;
			result += ",";
		}
		result = result.substring(0, result.length()-1);
		System.out.println(result);
	}
	
	public static String listlist2String(List<ArrayList<String>> listlist){
		String result = "";
		for(int i = 0; i < listlist.size(); i++){
			for(int j = 0; j < listlist.get(i).size(); j++){
				result += listlist.get(i).get(j);
				if(j != listlist.get(i).size() - 1)
					result += ",";
				else if(i != listlist.size()-1)
					result += ";";
			}
		}
		return result;
	}
	
	public static List<ArrayList<String>> string2ListList(String s){
		List<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
		String[] words = s.split(";");
		for(int i = 0; i < words.length; i++){
			String[] word = words[i].split(",");
			ArrayList<String> result = new ArrayList<String>();
			for(String item : word){
				result.add(item);
			}
			results.add(result);
		}
		return results;
	}
	
	//将string转换成Map<String, Vector<String>>
	public static Map<String, Vector<String>> string2Map(String s){
		Map<String, Object> mapIn = (Map<String, Object>)JSON.parse(s);
		Map<String, Vector<String>> mapOut = new HashMap<String, Vector<String>>();
		for(String key : mapIn.keySet()){
			List<String> list = (List<String>)mapIn.get(key);
			Vector<String> vector = new Vector<String>();
			//将list转化为vector
			for(String item : list)
				vector.add(item);
			mapOut.put(key, vector);
		}
		return mapOut;
	}
	
	//获取n个不相等的随机数字
	public static HashSet<Integer> getRandom(int n, int start, int end){//包含start，不包含end
		HashSet<Integer> hs = new HashSet<Integer>();
		while(true){
			int a = (int)(start + Math.random()*(end - start));
			hs.add(a);
		if(hs.size() == n)
			break;
		}
		return hs;
	}
	
	//将n个随机数变成(a,b,c,d,e)的形式，便于数据库查询
	public static String getRandom_String(HashSet<Integer> hs){
		String result = "(";
		Iterator<Integer> it = hs.iterator();
		while(it.hasNext()){
			int temp = it.next();
			result += (temp + ",");
		}
		result = result.substring(0, result.length()-1);//去除最后一个逗号
		result += ")";
		return result;
	}
	
	//将词性用数字表示
	public static int wordCharacters2Int(String ch){
		if(ch.matches("/n.*") || ch.matches("/vn.*"))//vn包括管理、回忆等词，记为名词
			return 1;
		else if(ch.matches("/a.*"))
			return 2;
		else if(ch.matches("/v.*"))
			return 3;
		else if(ch.matches("/nr.*"))
			return 4;
		else if(ch.matches("/ns.*"))
			return 5;
		else if(ch.matches("/t.*"))
			return 6;
		else if(ch.matches("/s.*"))
			return 7;
		else if(ch.matches("/f.*"))
			return 8;
		else if(ch.matches("/b.*"))
			return 9;
		else if(ch.matches("/z.*"))
			return 10;
		else if(ch.matches("/r.*"))
			return 11;
		else if(ch.matches("/m.*"))
			return 12;
		else if(ch.matches("/q.*"))
			return 13;
		else if(ch.matches("/d.*"))
			return 14;
		else if(ch.matches("/p.*"))
			return 15;
		else if(ch.matches("/c.*"))
			return 16;
		else if(ch.matches("/u.*"))
			return 17;
		else if(ch.matches("/e.*"))
			return 18;
		else if(ch.matches("/y.*"))
			return 19;
		else if(ch.matches("/o.*"))
			return 20;
		else if(ch.matches("/h.*"))
			return 21;
		else if(ch.matches("/k.*"))
			return 22;
		else if(ch.matches("/x.*"))
			return 23;
		else if(ch.matches("/w.*"))
			return 24;
		return 25;
	}
	
	//繁体字转简体字
	public static String ZHConverter_TraToSim(String tradStr) {
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
		String simplifiedStr = converter.convert(tradStr);
		return simplifiedStr;
	}
	//简体字转繁体字
	public static String ZHConverter_SimToTra(String simpStr) {
		ZHConverter converter = ZHConverter
				.getInstance(ZHConverter.TRADITIONAL);
		String traditionalStr = converter.convert(simpStr);
		return traditionalStr;
	}
	
	//去重
	public static String[] dereplication(String[] s){
		List<String> list = Arrays.asList(s);
		Set<String> set = new HashSet<String>(list);
		String[] result = (String[])set.toArray(new String[0]);
		return result;
	}
	
	//求和
	public static double MATH_getSum(List<Double> inputData) {
		if (inputData == null || inputData.size() == 0)
			return -1;
		int len = inputData.size();
		double sum = 0;
		for (int i = 0; i < len; i++) {
			sum = sum + inputData.get(i);
		}
		return sum;
	}
	//求平均数
	public static double MATH_getAverage(List<Double> inputData) {
		if (inputData == null || inputData.size() == 0)
			return -1;
		int len = inputData.size();
		double result = MATH_getSum(inputData) / len;;
		return result;
	}
	//求平方和
	public static double MATH_getSquareSum(List<Double> inputData) {
		if(inputData==null||inputData.size()==0)
		    return -1;
		int len=inputData.size();
		double sqrsum = 0.0;
		for (int i = 0; i <len; i++) {
			sqrsum = sqrsum + inputData.get(i) * inputData.get(i);
		}
		return sqrsum;
	}
	//求方差
	public static double MATH_getVariance(List<Double> inputData) {
		int count = inputData.size();
		double sqrsum = MATH_getSquareSum(inputData);
		double average = MATH_getAverage(inputData);
		double result = (sqrsum - count * average * average) / count;
		return result; 
	}
	//求标准差
	public static double MATH_getStandardDiviation(List<Double> inputData) {
		double result;
		//绝对值化很重要
		result = Math.sqrt(Math.abs(MATH_getVariance(inputData)));
		return result;
	}
	
}
