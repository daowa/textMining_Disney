package com.myClass;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import sun.org.mozilla.javascript.internal.json.JsonParser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.data.NLPIR;
import com.db.FileFunction;

public class test {

	public static void test() throws IOException{
//		String s = "虽然摆脱了问号和乱码的困扰，但这仍不是我们想要的结果";
		String s = "smallest Disneyland, ok if you have not visited other Disneyland in the world";
		NLPIR.NlpirInit();
		String[] words = NLPIR.wordSegmentateWithoutCharacteristic(s);
		for(int i = 0; i < words.length; i++){
			U.print(words[i]);
		}
		NLPIR.NlpirExit();
	}
	
}
