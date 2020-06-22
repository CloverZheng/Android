package com.net;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

class GetAndSendCSVThread implements Runnable{
    private Socket s;
    public String result;
    public GetAndSendCSVThread(Socket s,String result) {
        this.s = s;
        this.result=result;
    }

    @Override
    public void run() {

        try {

        	//BufferedReader br=new BufferedReader(new InputStreamReader(bin));
            
            //4.读取用户输入信息
            
                MakeCSV test=new MakeCSV(result);
                test.makeCSV();    
            
            System.out.println("start sending data"); 
            
        	DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
        	//System.out.println("DataOutputStream initialized successfully");
            FileInputStream fis = new FileInputStream("/root/clover/Eclipse project/TCP/skinProduct.csv");  
            //.out.println("FileIutputStream initialized successfully");
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
        }catch (IOException e) {  
            e.printStackTrace();  
        }            
    }
}