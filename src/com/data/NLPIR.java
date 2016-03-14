package com.data;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.db.FileFunction;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class NLPIR {

	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
			
			// 定义并初始化接口的静态变量 这一个语句是来加载dll的，注意dll文件的路径可以是绝对路径也可以是相对路径，只需要填写dll的文件名，不能加后缀。
			CLibrary Instance = (CLibrary)Native.loadLibrary("E:\\JavaWorkspace\\textMining_Disney\\NLPIR", CLibrary.class);
			
			// 初始化函数声明
			public boolean NLPIR_Init(byte[] sDataPath, int encoding, byte[] sLicenceCode);
			
			//执行分词函数声明
			public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
			
			//提取关键词函数声明
			public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
			
			public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
			
			public int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
			
			public int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
			
			public String NLPIR_GetLastErrorMsg();
			
			//退出函数声明
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
		
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		
		int charset_type = 1;
		// int charset_type = 0;
		
		// 调用printf打印信息
		if (!CLibrary.Instance.NLPIR_Init(argu.getBytes(system_charset), charset_type, "0".getBytes(system_charset))) {
			System.err.println("初始化失败！");
		}
	}
	
	public static void NlpirExit(){
		CLibrary.Instance.NLPIR_Exit();
	}
	
	//分词，返回含有词性的词数组
	public static String[] wordSegmentateWithCharacteristic(String sInput){
		String nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 3);
		return nativeBytes.split(" ");
	}
	
	//分词，返回不含有词性的词数组
	public static String[] wordSegmentateWithoutCharacteristic(String sInput){
		String nativeBytes_justWord = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 0);
		return nativeBytes_justWord.split(" ");
	}
	
	//将停用词读入内存
	public static List<String> getStopWords(){
		String stopWordsAddress = "E:\\work\\迪士尼\\vocabulary\\stopWords.txt";
		List<String> stopWords = FileFunction.readTxt_StopWords(stopWordsAddress);
		return stopWords;
	}
	
}
