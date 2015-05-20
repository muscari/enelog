package com.sozolab.service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

import com.sozolab.domain.FormFile;

import android.util.Log;

/**
 * HTTPプロトコルでファイルをサーバーにアップロードする
 * 
 * @author Administrator
 *
 */
public class SocketHttpRequester {

    @SuppressWarnings("resource")
	public static boolean post(String path, Map<String, String> params, FormFile[] files) throws Exception{     
        final String BOUNDARY = "---------------------------7da2137580612"; //デバイダ
        final String endline = "--" + BOUNDARY + "--\r\n";//オーバー
        
        //Log.i("MainActivity",files+"");
        
        int fileDataLength = 0;
        for(FormFile uploadFile : files){//ファイルの長さ
            StringBuilder fileExplain = new StringBuilder();
             fileExplain.append("--");
             fileExplain.append(BOUNDARY);
             fileExplain.append("\r\n");
             fileExplain.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
             fileExplain.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
             fileExplain.append("\r\n");
             fileDataLength += fileExplain.length();
             //Log.i("MainActivity",uploadFile.getParameterName());
            if(uploadFile.getInStream()!=null){
                fileDataLength += uploadFile.getFile().length();
             }else{
                 fileDataLength += uploadFile.getData().length;
             }
        }
        
        //Log.i("MainActivity","1");
        
        StringBuilder textEntity = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            textEntity.append("--");
            textEntity.append(BOUNDARY);
            textEntity.append("\r\n");
            textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
            textEntity.append(entry.getValue());
            textEntity.append("\r\n");
        }
        
        //Log.i("MainActivity","2");
        
        //サーバ二送るデータの長さ
        int dataLength = textEntity.toString().getBytes().length + fileDataLength +  endline.getBytes().length;
        
        URL url = new URL(path);
        int port = url.getPort()==-1 ? 80 : url.getPort();
        //Log.i("MainActivity",InetAddress.getByName(url.getHost())+"---"+dataLength+""+path+"---");
        
        //Log.i("MainActivity",url.getPath()+"3");
        //Log.i("MainActivity",url.getHost()+"");
       
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port); 
        //Log.i("MainActivity","3");
        
        
        OutputStream outStream = socket.getOutputStream();
        
       
        String requestmethod = "POST "+ url.getPath()+" HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        
       
        
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY+ "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: "+ dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        String host = "Host: "+ url.getHost() +":"+ port +"\r\n";
        outStream.write(host.getBytes());
        outStream.write("\r\n".getBytes());
        //Log.i("MainActivity","=====================");
        //Log.i("MainActivity",textEntity.toString().getBytes()+"");
        outStream.write(textEntity.toString().getBytes());  
        //Log.i("MainActivity",textEntity.toString().getBytes()+"");
        //Log.i("MainActivity","=====================");
       
       ////////////////////////////////////
        for(FormFile uploadFile : files){
            StringBuilder fileEntity = new StringBuilder();
             fileEntity.append("--");
             fileEntity.append(BOUNDARY);
             fileEntity.append("\r\n");
             fileEntity.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
             fileEntity.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
             //Log.i("MainActivity",uploadFile.getParameterName()+"!!!!"+uploadFile.getFilname());
             outStream.write(fileEntity.toString().getBytes());
             //Log.i("MainActivity","=====================");
             //Log.i("MainActivity",fileEntity.toString().getBytes()+"");
             if(uploadFile.getInStream()!=null){
            	 //Log.i("MainActivity","$$$$$$$$$$$$$$$$$");
                 byte[] buffer = new byte[10000];
                 int len = 0;
                 while((len = uploadFile.getInStream().read(buffer, 0, 10000))!=-1){
                     outStream.write(buffer, 0, len);
                 }
                 uploadFile.getInStream().close();
             }else{
                 outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
             }
             
             //Log.i("MainActivity","=====================");
             //Log.i("MainActivity","fileEntity.toString().getBytes()"+fileEntity.toString().getBytes()+"");
             //Log.i("MainActivity","fileEntity.toString().getBytes()"+outStream+"");
             outStream.write("\r\n".getBytes());
        }
        
       // Log.i("MainActivity","4");
        //オーバー
        outStream.write(endline.getBytes());
        //Log.i("MainActivity","5");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if(reader.readLine().indexOf("200")==-1){//200だたら、成功　　　200じゃないなら、失败
            return false;
        }
        //Log.i("MainActivity","6");
        outStream.flush();
        outStream.close();
        //Log.i("MainActivity","7");
        reader.close();
        socket.close();
        return true;
        
    }
    
    public static boolean post(String path, Map<String, String> params, FormFile file) throws Exception{
       return post(path, params, new FormFile[]{file});
       
    }
}
