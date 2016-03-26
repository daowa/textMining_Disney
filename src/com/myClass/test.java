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
import java.util.regex.Pattern;

import sun.misc.Regexp;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.data.Data_Segmentation;
import com.data.NLPIR;
import com.db.FileFunction;
import com.spreada.utils.chinese.ZHConverter;

public class test {

	public static void test() throws IOException{
		String temp = "太16-17阳5-6月亮星星sss2013-09-1813:32:00t[大笑]fc中Day3文英文sca";
		Pattern pattern = Pattern.compile("太阳");
//		temp = temp.replaceAll("[0-9]{4}-[0-9]{2}-[0-9]{4}:[0-9]{2}:[0-9]{2}", "");
		temp = temp.replaceAll("[0-9]{1,2}-[0-9]{1,2}", "");
		U.print(temp);
	}
	
}
