package com.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Socket;

class StyleMakeupThread implements Runnable{
    private Socket s;
    private InputStream bin;
    String style;
    public StyleMakeupThread(Socket s,InputStream bin,String style) {
        this.s = s;
        this.bin=bin;
        this.style=style;
    }

    @Override
    public void run() {
        try {
        	File dir = new File("/root/clover/no_makeup/style/"+style);
            if(!dir.exists()){
                dir.mkdir();
            }
            int count=1;            
            byte buf[] = new byte[1024];
            int len=0;

            File file = new File(dir, "1.jpg");
            while(file.exists()&&file.length()>0)
            {
                file = new File(dir,(++count) + ".jpg"); 
            }

            FileOutputStream fout = new FileOutputStream(file);  

            while((len=bin.read(buf))>0)
            {
                String str = new String(buf);
                if(str.startsWith("imgEnd"))
                {
                    System.out.println("str:" + str); 
                	break;
                } 
                else
                {               	
                    fout.write(buf, 0, len);
                }               
            }
            fout.close();
            System.out.println("finished receiving data");
            String original_img="/root/clover/no_makeup/style/"+style+"/"+count+".jpg";
            String style_makeup_img="/root/pure_if/简单相似人脸检索/style_new/"+style+"/result_"+count+".jpg";
            System.out.println(original_img);
            System.out.println(style_makeup_img);
            String[] arguments = new String[] {"python3","/root/pure_if/简单相似人脸检索/Face_recognition5.py","--no_makeup",original_img,"--img_data","/root/pure_if/"+style};
    		//String[] arguments = new String[] {"pip3","install","tensorflow"};
    		Process p = Runtime.getRuntime().exec(arguments);
    		InputStreamReader ir = new InputStreamReader(p.getInputStream());
    		//InputStreamReader ir = new InputStreamReader(p.getErrorStream());
    		LineNumberReader input = new LineNumberReader(ir);
    		String str;
    		while((str=input.readLine())!=null){
    			System.out.println(str);
    		}

            System.out.println("start sending style makeup img");
        	DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
        	//System.out.println("DataOutputStream initialized successfully");
            FileInputStream fis = new FileInputStream(style_makeup_img);  
            System.out.println("FileIutputStream initialized successfully");
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
            System.out.println("StyleMakeupThread is over, socket closed");
        } catch (IOException e) {  
            e.printStackTrace();  
        }            
    }
}
