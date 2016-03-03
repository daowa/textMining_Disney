package com.myClass;

public class U {
	
	public static boolean isNumeric(String str){
		if(str == "") return false;
		for(int i=str.length();--i>=0;){
		    int chr=str.charAt(i);
		    if(chr<48 || chr>57) return false;
		}
		return true;
	}
	
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
}
