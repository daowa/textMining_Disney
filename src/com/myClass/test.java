package com.myClass;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Pattern;

import sun.misc.Regexp;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.data.Data_Segmentation;
import com.data.NLPIR;
import com.db.DBFunction;
import com.db.FileFunction;
import com.spreada.utils.chinese.ZHConverter;

public class test {

	public static void test() throws IOException, SQLException{
//		ResultSet rs = DBFunction.selectAllFromTrainingSet();
//		List<List<Double>> listX = new ArrayList<List<Double>>();//�����д�����
//		List<String> listTFIDF = new ArrayList<String>();
//		
//		int count = 0;
//		while(rs.next()){
//			count ++;
//			if(count > 2) break;
//			
//			int id = rs.getInt(MyStatic.KEY_ID_rawDianPing);
//			
//			String stats = DBFunction.getFeature(rs.getInt(MyStatic.KEY_ID_rawDianPing));
//			Map<String, Vector<String>> map = U.string2Map(stats);
//			
//			U.print(map.toString());
//			String topTFIDF = getTopWords(map, 3, MyStatic.Index_WordCount);
//			U.print(topTFIDF);
			
//			for(String key : map.keySet()){
//				//��X
//				List<Double> listx = new ArrayList<Double>();//�浥��������
//				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_TFIDF)));
//				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_Position_FirstWord)));
////				listx.add(Double.parseDouble(map.get(key).get(MyStatic.Index_WordLength)));
//				listx.add((double)U.wordCharacters2Int(map.get(key).get(MyStatic.Index_WordCharacteristic)));
//				listX.add(listx);
//			}
//		}
	}
	
}
