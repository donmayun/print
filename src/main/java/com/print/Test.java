package com.print;

import java.io.UnsupportedEncodingException;

/**
 * description: 
 * @author don
 *
 */
public class Test {

	
	public static void main(String [] args) throws UnsupportedEncodingException{
		
		String source = "中文";
		System.out.println(source);

		String xmString = new String(source.getBytes("gbk"), "gbk");
		
		
		System.out.println(xmString);
		
		System.out.println(new String(xmString.getBytes("utf-8"), "gbk"));
		System.out.println(new String(new String(xmString.getBytes("utf-8"), "gbk").getBytes("gbk"), "gbk"));
	}
	
}
