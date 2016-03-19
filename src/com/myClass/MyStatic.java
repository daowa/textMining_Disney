package com.myClass;

public class MyStatic {

	public final static String DBName = "disney";
	
	//原始数据表
//	public final static String TABLE_YouJi = "raw_youji";
//	public final static String TABLE_DianPing = "raw_dianping";
//	public final static String TABLE_WenDa = "raw_wenda";
	//待清理表（生产环境）
//	public final static String TABLE_YouJi = "clean_youji";
//	public final static String TABLE_DianPing = "clean_dianping";
//	public final static String TABLE_WenDa = "clean_wenda";
	//测试表（测试环境）
	public final static String TABLE_YouJi = "test_youji";
	public final static String TABLE_DianPing = "test_dianping";
	public final static String TABLE_WenDa = "test_wenda";
	
	public final static String TABLE_Middle = "middle";
	public final static String TABLE_TrainingSet = "training_set";
	
	//游记
	public final static String KEY_ID_rawYouJi = "idraw_youji";
	public final static String KEY_ID_rawDianPing = "idraw_dianping";
	public final static String KEY_ID_rawWenDa = "idraw_wenda";
	public final static String KEY_ID_id = "id";
	public final static String KEY_Title = "title";
	public final static String KEY_Content = "content";
	public final static String KEY_Author = "author";
	public final static String KEY_AuthroFrom = "authorfrom";
	public final static String KEY_Time = "time";
	public final static String KEY_Up = "up";
	public final static String KEY_Collect = "collect";
	public final static String KEY_Comment = "comment";
	public final static String KEY_Share = "share";
	public final static String KEY_WebFrom = "web_from";
	public final static String KEY_City = "city";
	
	//点评特有
	public final static String KEY_Star = "star";
	public final static String KEY_Scenery = "scenery";
	public final static String KEY_Interest = "interest";
	public final static String KEY_CostPerformance = "cost_performance";
	
	//问答特有
	public final static String KEY_UpContent = "up_content";
	
	//中间数据库
	public final static String KEY_Stats = "stats";
	
	//关键词数据库
	public final static String KEY_Keyword = "keyword";
	
	public final static String WebFrom_MaFengWo = "蚂蜂窝";
	public final static String WebFrom_XieCheng = "携程";
	public final static String WebFrom_BaiDuTravel = "百度旅游";
	public final static String WebFrom_MianBaoTravel = "面包旅行";
	public final static String WebFrom_QiongYou = "穷游网";
	public final static String WebFrom_QuNaEr = "去哪儿";
	
	
	public final static String City_HongKong = "香港";
	public final static String City_Tokyo = "东京";
	public final static String City_Paris = "巴黎";
	public final static String City_California = "加州";
	public final static String City_Orlando = "奥兰多";
	
	public final static int Category_YouJi = 0;
	public final static int Category_DianPing = 1;
	public final static int Category_WenDa = 2;
	
	public final static int Index_WordCount = 0;//词出现数量
	public final static int Index_WordFrequency = 1;//词频
	public final static int Index_TFIDF = 2;//tf-idf
	public final static int Index_WordCharacteristic = 3;//词性
	public final static int Index_WordLength = 4;//词长（可以直接key.length，但是不统一，所以增加空间存储量统一下代码）
	public final static int Index_Position_FirstWord = 5;//第一次出现在第几个字（去停用词）
	
	public final static String Others_Line = "————————————————————————————————————————————————————————";
	
}
