package com.sozolab.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.sozolab.enelog.LoginActivity;
import com.sozolab.enelog.MainActivity;



public class doPost {
//clientを
	
public static HttpClient client = HttpClientHelper.getHttpClient();
public static String info;

//login
public boolean login(String user,String pass) {

	HttpPost mPost = new HttpPost(LoginActivity.sp.getString("login_url", MainActivity.def_login_url)+"/login");
	//HttpPost mPost = new HttpPost("http://eneact_test.sozolab.jp"+"/login");
	//System.out.println("login_url:"+LoginActivity.sp.getString("login_url", MainActivity.def_login_url)+"/login");
	
	List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
	pairs.add(new BasicNameValuePair("login", user));
	pairs.add(new BasicNameValuePair("password", pass));
	//System.out.println("111");
	try {
	mPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
	} catch (UnsupportedEncodingException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}
	//System.out.println("222");
	try {
		//postの方法で
	HttpResponse responsePost = client.execute(mPost);
	//System.out.println("333");
	int resPost = responsePost.getStatusLine().getStatusCode();
	//System.out.println("LoginActivity"+resPost+"=================");
	//System.out.println("444");
	try {
		//System.out.println("resPost:"+resPost);
	if (resPost == 200) {
	HttpEntity entity = responsePost.getEntity();
	return true;
	} 
	else{
		return false;
		}
	}catch (Exception e) {
			//System.out.println("message1:"+e); 
		return false;
		}
	}catch (ClientProtocolException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	//System.out.println("message2:"+e);
	} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	//System.out.println("message3:"+e);
	}
	return false;
}

//dopost
public String doPost(String url,String filenameKey,String filename,
		String typeKey,String type,String versionkey, String version,String postKey,String data){
	 try {
		     HttpPost method = new HttpPost(url);
		     // リクエストパラメータの設定
		     List<NameValuePair> params = new ArrayList<NameValuePair>();
		     params.add(new BasicNameValuePair(filenameKey,filename));
		     params.add(new BasicNameValuePair(typeKey, type));
		     params.add(new BasicNameValuePair(versionkey,version));
		     params.add(new BasicNameValuePair(postKey, data));
		     method.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
		     
		     try{
			     HttpResponse response = client.execute(method);
			     int status = response.getStatusLine().getStatusCode();
			     try{
				     if(status == 200){	  
				       //successを取る
				       HttpEntity entity = response.getEntity();
				       if(entity != null){
				       info = EntityUtils.toString(entity);
				       //System.out.println("<info========"+info+"========info>");
				       }
				     }else{
				       //System.out.println("サーバーに接続できません。。。"); 
				     }
			     }catch(Exception e){
			     }
		     }catch(ClientProtocolException e){
		        	// TODO Auto-generated catch block
		        	e.printStackTrace();
		     }catch(IOException e){
		     	// TODO Auto-generated catch block
		     	e.printStackTrace();
		     }
		     
		     //Log.v("Status",url);
		     return "info:" + info;
     }catch (Exception e) {
        return "Error:" + e.getMessage();
     }
}


//logout
public static void logout(){
	HttpGet mGet = new HttpGet(LoginActivity.sp.getString("login_url", MainActivity.def_login_url)+"/logout");
		try {
			//postの方法で
		HttpResponse responseGet = client.execute(mGet);
		int resGet = responseGet.getStatusLine().getStatusCode();
		//System.out.println("LoginActivity"+resGet+"=================");
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
}


}