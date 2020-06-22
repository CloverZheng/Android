package com.net;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

class GetAndSendResultThread implements Runnable{
    private Socket s;
    public InputStream bin;
    public GetAndSendResultThread(Socket s,InputStream bin) {
        this.s = s;
        this.bin=bin;
    }

    @Override
    public void run() {
    	
        try {
        	File dir = new File("/root/clover/no_makeup/skinTest");

            if(!dir.exists()){
                dir.mkdir();
            }

            int count=1;
            File file = new File(dir, "1.jpg");
            while(file.exists()&&file.length()>0)//1.png exists
            {
                file = new File(dir,(++count) + ".jpg"); 
            }

            FileOutputStream fout = new FileOutputStream(file);  

            byte buf[] = new byte[1024];
            int len=0;
            while((len=bin.read(buf))>0)
            {
                String str = new String(buf);
                if(str.startsWith("imgEnd"))
                { 
                	System.out.println("finished receiving data");
                	break;
                } 
                else
                {               	
                    fout.write(buf, 0, len);
                }               
            }
            fout.close();
            String original_img="/root/clover/no_makeup/skinTest/"+count+".jpg";
            String result_txt="/root/HF/code/userskin_result.txt";
            System.out.println(original_img);
            System.out.println(result_txt);
     
            
           
        	String[] arguments = new String[] {"python3","/root/HF/code/skintestmain.py","--no_makeup",original_img};
    		//String[] arguments = new String[] {"pip3","install","tensorflow"};
    		Process p = Runtime.getRuntime().exec(arguments);
    		//InputStreamReader ir = new InputStreamReader(p.getInputStream());
    		InputStreamReader ir = new InputStreamReader(p.getErrorStream());
    		LineNumberReader input = new LineNumberReader(ir);
    		String str;
    		while((str=input.readLine())!=null){
    			System.out.println(str);
    		}
   
    		System.out.println("start send data");
            
        	DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
        	System.out.println("DataOutputStream initialized successfully");
            FileInputStream fis = new FileInputStream(result_txt);       
            int size = fis.available();            
            System.out.println("size = "+size);
            byte[] data = new byte[size];  
            fis.read(data);  
            System.out.println("file loaded");
            dos.writeInt(size);  
            //System.out.println("DataOutputStream is ready");
            dos.write(data);  
            //System.out.println("DataOutputStream is ready");
            dos.flush();  
            System.out.println("Send successfully");
            dos.close();  
            fis.close();  
            s.close(); 
            System.out.println("Send result socket closed");
        }catch (IOException e) {  
            e.printStackTrace();  
        }            
    }
}