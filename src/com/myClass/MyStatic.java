package com.myClass;

public class MyStatic {

	public final static String DBName = "disney";
	
	public final static String TABLE_rawYouJi = "raw_youji";
	public final static String TABLE_rawDianPing = "raw_dianping";
	public final static String TABLE_rawWenDa = "raw_wenda";
	//放在待处理的表中数据
//	public final static String TABLE_rawYouJi = "clean_youji";
//	public final static String TABLE_rawDianPing = "clean_dianping";
//	public final static String TABLE_rawWenDa = "clean_wenda";
	
	//游记
	public final static String KEY_ID_rawYouJi = "idraw_youji";
	public final static String KEY_ID_rawDianPing = "idraw_dianping";
	public final static String KEY_ID_rawWenDa = "idraw_wenda";
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
	
}
