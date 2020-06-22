package com.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainSocket {
    
    public static void main(String[] args) {

        try {
        	
            ServerSocket server = new ServerSocket(8755);
            while(true){
            	System.out.println("socket run successfully, waiting for a client socket");
                Socket s = server.accept();  
                String ip = s.getInetAddress().getHostAddress();
                System.out.println("connected with:"+ip);

                    InputStream bin = s.getInputStream();
                    BufferedReader br=new BufferedReader(new InputStreamReader(bin));
                    String requestType=null;
                    requestType=br.readLine();
                   
                        if(requestType.startsWith("skinTest"))
                        {
                            System.out.println("skinTestThread Start"); 
                            new Thread(new GetAndSendResultThread(s,bin)).start(); 
                        }else if(requestType.startsWith("product")){
                        	System.out.println("productThread Start"); 
                        	String result=null;
                        	result=br.readLine();
                            new Thread(new GetAndSendCSVThread(s,result)).start(); 
                        }else if(requestType.startsWith("personalMakeup")){
                        	System.out.println("GetAndMakeupThread Start"); 
                        	new Thread(new GetAndMakeupThread(s,bin)).start();
                        }else if(requestType.startsWith("styleMakeup")){
                        	System.out.println("StyleMakeupThread Start"); 
                        	String style=null;
                        	style=br.readLine();
                        	new Thread(new StyleMakeupThread(s,bin,style)).start();
                        }
                    }   
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}