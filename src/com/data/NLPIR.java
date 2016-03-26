package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.db.FileFunction;
import com.myClass.MyStatic;
import com.myClass.U;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class NLPIR {

	// ����ӿ�CLibrary���̳���com.sun.jna.Library
	public interface CLibrary extends Library {
			
			// ���岢��ʼ���ӿڵľ�̬���� ��һ�������������dll�ģ�ע��dll�ļ���·�������Ǿ���·��Ҳ���������·����ֻ��Ҫ��дdll���ļ��������ܼӺ�׺��
			CLibrary Instance = (CLibrary)Native.loadLibrary("E:\\JavaWorkspace\\textMining_Disney\\NLPIR", CLibrary.class);
			
			// ��ʼ����������
			public boolean NLPIR_Init(byte[] sDataPath, int encoding, byte[] sLicenceCode);
			
			//ִ�зִʺ�������
			public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
			
			//��ȡ�ؼ��ʺ�������
			public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
			
			public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
			
			public int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
			
			public int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
			
			public void NLPIR_SaveTheUsrDic();
			
			public String NLPIR_GetLastErrorMsg();
			
			
			//����´�
			
			public boolean NLPIR_NWI_Start();//�����´ʷ��ֹ���
			
			public boolean NLPIR_NWI_AddFile(String address); //����´�ѵ�����ļ����ɷ������
			
			public boolean NLPIR_NWI_Complete();//����ļ�����ѵ�����ݽ���
			
			public boolean NLPIR_NWI_Result2UserDict();//���´�ʶ�������뵽�û��ʵ���;
													   //��Ҫ������NLPIR_NWI_Complete()֮�󣬲���Ч
													   //�����Ҫ���´ʽ�����ñ��棬������ִ��NLPIR_SaveTheUsrDic
			
			public String NLPIR_NWI_GetResult();
			
			//���ַ����л�ȡ�´�,����´ʷ�Χ������ִ����Ч
		    public String NLPIR_GetNewWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
		    
		    public String NLPIR_GetFileNewWords(String sTextFile,int nMaxKeyLimit, boolean bWeightOut);


			
			//�˳���������
			public void NLPIR_Exit();
	}
	
	
	
	
	
	
	public static String transString(String aidString, String ori_encoding,
			String new_encoding) {
		try {
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void NlpirInit() throws UnsupportedEncodingException{
		String argu = "";
		
		String system_charset = "UTF-8";
		
		int charset_type = 1;
		
		if (!CLibrary.Instance.NLPIR_Init(argu.getBytes(system_charset), charset_type, "0".getBytes(system_charset))) {
			System.err.println("��ʼ��ʧ�ܣ�");
		}
	}
	
	public static void NlpirExit(){
		CLibrary.Instance.NLPIR_Exit();
	}
	
	//�ִʣ����غ��д��ԵĴ�����
	public static String[] wordSegmentateWithCharacteristic(String sInput){
		String nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 3);
		return nativeBytes.split(" ");
	}
	
	//�ִʣ����ز����д��ԵĴ�����
	public static String[] wordSegmentateWithoutCharacteristic(String sInput){
		String nativeBytes_justWord = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 0);
		return nativeBytes_justWord.split(" ");
	}
	
	//��ͣ�ôʶ����ڴ�
	public static List<String> getStopWords(){
		String stopWordsAddress = "E:\\work\\��ʿ��\\vocabulary\\stopWords.txt";
		List<String> stopWords = FileFunction.readTxt_StopWords(stopWordsAddress);
		return stopWords;
	}
	
	//����û��ʵ�
	public static void addUserDict(String word, String characteritic){
		CLibrary.Instance.NLPIR_AddUserWord(word + " " + characteritic);
		CLibrary.Instance.NLPIR_SaveTheUsrDic();
	}
	
	public static String getNewWord(String rawString) throws IOException{
		return CLibrary.Instance.NLPIR_GetNewWords(rawString, 50, false);
	}
	
}
