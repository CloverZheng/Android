package com.net;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class MakeCSV {

	private String key="1224326";
	public MakeCSV(String keywords){
		this.key=keywords;
	}
	public void makeCSV() {
		// TODO Auto-generated method stub

		System.out.println("makeCSV run successfully");

        	try{
        		System.out.println("key is:"+key);
        		String[] arguments = new String[] {"python3","/root/clover/Eclipse project/TCP/product.py","--keywords",key};
        		//String[] arguments = new String[] {"pip3","install","lxml"};
        		Process p = Runtime.getRuntime().exec(arguments);
        		InputStreamReader ir = new InputStreamReader(p.getInputStream());
        		LineNumberReader input = new LineNumberReader(ir);
        		String str;
        		while((str=input.readLine())!=null){
        			System.out.println(str);
        		}
        	}catch(IOException e){
        		e.printStackTrace();
        	}   
	}

}
