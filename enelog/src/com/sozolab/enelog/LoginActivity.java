package com.sozolab.enelog;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.net.CookieStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sozolab.enelog.R;
import com.sozolab.service.UserService;
import com.sozolab.service.doPost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {
	private TextView net,title,url_link1;
    EditText username;
	EditText password;
	ImageButton login;
	Button register;
	private CheckBox rem_pw, auto_login; 
	public static SharedPreferences sp,page; 
	LinearLayout l;
	public static HashMap<String, Object> map; 
	private HashMap<String, String> session = new HashMap<String,String>(); 
	private boolean  flag3; 
	public MessageDigest md = null;
	public static String user;
	public static String pass;
	public static doPost a =new doPost();
	String cookies_old;
	public static  CookieStore _cookieStore2 = null;
	public static SharedPreferences prefs;
	public static String login_Url = null;
	public static String upload_Url = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites().detectNetwork()
		.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
		.build());
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.login_activity);
		prefs= getSharedPreferences("syllabus", 0);
		findViews();
		
		if(sp.getString("page", "not_auto")!=null){
			//自動ログイン
		          //rem_pw.setChecked(true);  
		          username.setText(sp.getString("USER_NAME", ""));  
		          password.setText(sp.getString("PASSWORD", ""));  
		          //自動的にログインする状態をチェックする 
		          //System.out.println(sp.getBoolean("AUTO_ISCHECK", false));
		          if(sp.getBoolean("AUTO_ISCHECK", false))  
		          {  
		                 //自動的にログインを設置する  
		                 auto_login.setChecked(true);  
		                 user = username.getText().toString();
		 				 pass=password.getText().toString();
		                 //System.out.println("user,pass"+user+"and"+pass);
		                 try{
		                 if(a.login(user,pass)){
		                	 //System.out.println("user,pass"+user+"and"+pass);
		                	//画面遷移 
		                	 Intent intent = new Intent(LoginActivity.this,MainActivity.class);  
				                LoginActivity.this.startActivity(intent); 
				                LoginActivity.sp.edit().putInt("PAGE_COUNT", 2).commit(); 
				                LoginActivity.this.finish();
		 				}else{
		 					//Log.i("TAG","error");
		 					Toast.makeText(LoginActivity.this, "入力を確認してください", Toast.LENGTH_LONG).show();
		 				}}catch(Exception e){
		 					Toast.makeText(LoginActivity.this, "入力を確認してください", Toast.LENGTH_LONG).show();
		 				}
		                 
		          }
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
            dialog(); 
            return false; 
        } 
        return false; 
    }

	protected void dialog() { 
        AlertDialog.Builder builder = new Builder(LoginActivity.this); 
        builder.setMessage("アプリをシャットダウンしますか？"); 
        builder.setTitle("メッセージ"); 
        builder.setPositiveButton("Yes", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                        LoginActivity.this.finish();
                    } 
                }); 
        builder.setNegativeButton("No", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
    } 
	
	private void findViews() {
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE); 
		String tl = "EneLog";
        SpannableStringBuilder style=new SpannableStringBuilder(tl); 
        //SpannableStringBuilder Achieve CharSequence Interface  
        style.setSpan(new ForegroundColorSpan(Color.rgb(0, 153, 51)), 0, 1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ); 
        style.setSpan(new ForegroundColorSpan(Color.BLACK), 1, 3,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ); 
        style.setSpan(new ForegroundColorSpan(Color.rgb(255,0,51)), 3, 4,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ); 
        style.setSpan(new ForegroundColorSpan(Color.BLACK), 4, 5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
        title = (TextView)findViewById(R.id.title); 
        title.setText(style);
		
        //url_link
        url_link1 = (TextView)findViewById(R.id.url_link1); 
        url_link1.setMovementMethod(LinkMovementMethod.getInstance());
        Spanned text = Html.fromHtml("<a href=\"https://eneact.sozolab.jp/\">eneact.sozolab.jp</a>");
        url_link1.setText(text);
        
		username=(EditText) findViewById(R.id.username);
		password=(EditText) findViewById(R.id.password);
//		login=(Button) findViewById(R.id.login);
		login= (ImageButton)findViewById(R.id.login);
//		register=(Button) findViewById(R.id.register);
		password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	    //backgroundLayoutをタッチ
		l = (LinearLayout) findViewById(R.id.backgroundLayout); 
//	    rem_pw = (CheckBox) findViewById(R.id.rem);  
        auto_login = (CheckBox) findViewById(R.id.auto); 
	    if(checkNetworkAvailable()){
//	    	net = (TextView) findViewById(R.id.net);
//	    	net.setText("is online中...");
	    }else{
//	    	net = (TextView) findViewById(R.id.net);
//	    	net.setText("is not online中...");
	    }
	    //長押す
	    login.setOnLongClickListener(new OnLongClickListener(){
	    	public boolean onLongClick(View v) {
	    		
	    		Intent intent = new Intent(LoginActivity.this,
	                    SettingActivity.class);
	            startActivity(intent);	
	            LoginActivity.this.finish();
	            return false;
	            
	    	}
	    });
	    //押す
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				login.setEnabled(false);
				user = username.getText().toString();
				pass=password.getText().toString();
				//login
				try{
				if( a.login(user,pass)){
					Editor editor = sp.edit();  
                      editor.putString("USER_NAME", user);  
                      editor.putString("PASSWORD",pass); 
                      editor.putString("page", "auto");
                      editor.putInt("PAGE_COUNT", 2); 
//                    editor.putString("COOKIE",cookie.getValue()); 
                      editor.commit();  
				//画面を遷移する
					Intent intent = new Intent(LoginActivity.this,
		                    MainActivity.class);
		            startActivity(intent);
		            //System.out.println("login_url"+LoginActivity.sp.getString("login_url",""));
		            LoginActivity.this.finish();
				}else{
					//Log.i("TAG","error");
					Toast.makeText(LoginActivity.this, "入力を確認してください", Toast.LENGTH_LONG).show();
					login.setEnabled(true);
				}
				}	catch (Exception e) {
					// TODO Auto-generated catch block
					
					Toast.makeText(LoginActivity.this, "入力を確認してください", Toast.LENGTH_LONG).show();
					} 
			}
		});
	
		l.setOnClickListener(new OnClickListener() {   
	        @Override   
	        public void onClick(View v) {   
	        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))   
	        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),                                
	        		InputMethodManager.HIDE_NOT_ALWAYS);   
	                        }   
	                }); 
		
      //自動的にログイン  
        auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (auto_login.isChecked()) {  
                    //System.out.println("AUTO_ISCHECK選択した");  
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();  
  
                } else {  
                    //System.out.println("AUTO_ISCHECK選択してない");  
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
                }  
            }  
        });  
	}
	
	//networkをチェックする
    public boolean checkNetworkAvailable() {  
        boolean isNetworkAvailable = false;  
        android.net.ConnectivityManager connManager = (android.net.ConnectivityManager)getApplicationContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE);  
        if(connManager.getActiveNetworkInfo() != null){  
            isNetworkAvailable = connManager.getActiveNetworkInfo().isAvailable();  
        }  
        return isNetworkAvailable;  
    }
}

