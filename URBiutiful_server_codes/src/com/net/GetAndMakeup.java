package com.net;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

class GetAndMakeupThread implements Runnable{
    private Socket s;
    private InputStream bin;
    public GetAndMakeupThread(Socket s,InputStream bin) {
        this.s = s;
        this.bin=bin;
    }

    @Override
    public void run() {

        try {
        	File dir = new File("/root/clover/no_makeup/personal");
            if(!dir.exists()){
                dir.mkdir();
            }

            int count=1;            
            File file = new File(dir, "1.jpg");

            while(file.exists()&&file.length()>0)//1.png exists
            {
                file = new File(dir,(++count) + ".jpg"); 
            }

            String original_img="/root/clover/no_makeup/personal/"+count+".jpg";
            String makeup_img="/root/pure_if/简单相似人脸检索/new/result_"+count+".jpg";
            System.out.println(original_img);
            System.out.println(makeup_img);
            FileOutputStream fout = new FileOutputStream(file);  

            byte buf[] = new byte[1024];
            int len=0;
            while((len=bin.read(buf))>0)
            {
                String str = new String(buf);
                if(str.startsWith("imgEnd"))
                {
                    //System.out.println("str:" + str); 
                	break;
                } 
                else
                {               	
                    fout.write(buf, 0, len);
                }               
            }
            fout.close();
            System.out.println("finished receiving data");
                       
            PersonalMakeUp recommand=new PersonalMakeUp(original_img);
            recommand.personalMakeup();

            System.out.println("start sending personal makeup img");
        	DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
            FileInputStream fis = new FileInputStream(makeup_img);  
            //System.out.println("FileIutputStream initialized successfully");
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
            System.out.println("PersonalMakeupThread is over, socket closed");
        } catch (Exception e) {  
            e.printStackTrace();  
        }            
    }
}

