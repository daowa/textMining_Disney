package com.myClass;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import com.data.Data_Segmentation;
import com.data.NLPIR;
import com.db.FileFunction;
import com.spreada.utils.chinese.ZHConverter;

public class test {

	public static void test() throws IOException{
		NLPIR.NlpirInit();
		U.print(NLPIR.wordSegmentateWithCharacteristic("飞越太空山的二维码"));
		NLPIR.NlpirExit();
	}
	
}
