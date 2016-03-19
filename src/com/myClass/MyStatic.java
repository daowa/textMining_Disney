package com.myClass;

public class MyStatic {

	public final static String DBName = "disney";
	
	//ԭʼ���ݱ�
//	public final static String TABLE_YouJi = "raw_youji";
//	public final static String TABLE_DianPing = "raw_dianping";
//	public final static String TABLE_WenDa = "raw_wenda";
	//�����������������
//	public final static String TABLE_YouJi = "clean_youji";
//	public final static String TABLE_DianPing = "clean_dianping";
//	public final static String TABLE_WenDa = "clean_wenda";
	//���Ա����Ի�����
	public final static String TABLE_YouJi = "test_youji";
	public final static String TABLE_DianPing = "test_dianping";
	public final static String TABLE_WenDa = "test_wenda";
	
	public final static String TABLE_Middle = "middle";
	public final static String TABLE_TrainingSet = "training_set";
	
	//�μ�
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
	
	//��������
	public final static String KEY_Star = "star";
	public final static String KEY_Scenery = "scenery";
	public final static String KEY_Interest = "interest";
	public final static String KEY_CostPerformance = "cost_performance";
	
	//�ʴ�����
	public final static String KEY_UpContent = "up_content";
	
	//�м����ݿ�
	public final static String KEY_Stats = "stats";
	
	//�ؼ������ݿ�
	public final static String KEY_Keyword = "keyword";
	
	public final static String WebFrom_MaFengWo = "�����";
	public final static String WebFrom_XieCheng = "Я��";
	public final static String WebFrom_BaiDuTravel = "�ٶ�����";
	public final static String WebFrom_MianBaoTravel = "�������";
	public final static String WebFrom_QiongYou = "������";
	public final static String WebFrom_QuNaEr = "ȥ�Ķ�";
	
	
	public final static String City_HongKong = "���";
	public final static String City_Tokyo = "����";
	public final static String City_Paris = "����";
	public final static String City_California = "����";
	public final static String City_Orlando = "������";
	
	public final static int Category_YouJi = 0;
	public final static int Category_DianPing = 1;
	public final static int Category_WenDa = 2;
	
	public final static int Index_WordCount = 0;//�ʳ�������
	public final static int Index_WordFrequency = 1;//��Ƶ
	public final static int Index_TFIDF = 2;//tf-idf
	public final static int Index_WordCharacteristic = 3;//����
	public final static int Index_WordLength = 4;//�ʳ�������ֱ��key.length�����ǲ�ͳһ���������ӿռ�洢��ͳһ�´��룩
	public final static int Index_Position_FirstWord = 5;//��һ�γ����ڵڼ����֣�ȥͣ�ôʣ�
	
	public final static String Others_Line = "����������������������������������������������������������������������������������������������������������������";
	
}
