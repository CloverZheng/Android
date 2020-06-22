package com.net;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class PersonalMakeUp {
	private String file_name="/root/clover/receives/Clovertest.jpg"; 
	
	public PersonalMakeUp(String filename){
		this.file_name=filename;
	}

	public void personalMakeup(){
		System.out.println("personalMakeUp run successfully");

    	try{
    		//String[] arguments = new String[] {"pip3","uninstall","zipp"};
    		String[] arguments = new String[] {"python3","/root/pure_if/简单相似人脸检索/Face_recognition2.py","--no_makeup",file_name};
    		//String[] arguments = new String[] {"pip3","install","tensorflow"};
    		Process p = Runtime.getRuntime().exec(arguments);
    		//InputStreamReader ir = new InputStreamReader(p.getInputStream());
    		InputStreamReader ir = new InputStreamReader(p.getErrorStream());
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
