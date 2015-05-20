package com.sozolab.enelog;


import com.sozolab.enelog.R;
import com.sozolab.service.doPost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StartActivity extends Activity implements OnClickListener{
	private TextView title,url_link3;
	private Button start;
//	private Button setting;
	private Button logout;
	private Button exit;
	public static String mark;
	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.start_activity);  
        String tl = "EneLog";
        SpannableStringBuilder style=new SpannableStringBuilder(tl); 
        //SpannableStringBuilder Achieve CharSequence Interface 
        style.setSpan(new ForegroundColorSpan(Color.rgb(0, 153, 51)), 0, 1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ); 
        style.setSpan(new ForegroundColorSpan(Color.BLACK), 1, 3,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ); 
        style.setSpan(new ForegroundColorSpan(Color.rgb(255,0,51)), 3, 4,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ); 
        style.setSpan(new ForegroundColorSpan(Color.BLACK), 4, 5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
        title = (TextView)findViewById(R.id.title); 
        title.setText(style);
        
        url_link3 = (TextView)findViewById(R.id.url_link3); 
        url_link3.setMovementMethod(LinkMovementMethod.getInstance());
        Spanned text = Html.fromHtml("<a href=\"https://eneact.sozolab.jp/\">eneact.sozolab.jp</a>");
        url_link3.setText(text);
        
//        setting = (Button)findViewById(R.id.setting); 
//        setting.setOnClickListener(this);
        start = (Button)findViewById(R.id.start); 
        start.setOnClickListener(this);
        logout = (Button)findViewById(R.id.logout); 
        logout.setOnClickListener(this);
        exit = (Button)findViewById(R.id.exit); 
        exit.setOnClickListener(this);
	}
	    @Override 
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
	            dialog_exit(); 
	            return false; 
	        } 
	        return false; 
	    }
	    
	public void onClick(View v) {
		if (v.getId() == R.id.start) {
			start.setEnabled(false);
			Intent dbIntent = new Intent(StartActivity.this,
                    MainActivity.class);
            startActivity(dbIntent);
            StartActivity.this.finish();
		}
	/*	if (v.getId() == R.id.setting) {
			Intent dbIntent = new Intent(StartActivity.this,
                    SettingActivity.class);
            startActivity(dbIntent);
		}*/
		if (v.getId() == R.id.logout) {
			dialog(); 
		}
		if (v.getId() == R.id.exit) {
			dialog_exit(); 
		}
	}
	
	protected void dialog() { 
        AlertDialog.Builder builder = new Builder(StartActivity.this); 
        builder.setMessage("ログアウトしますか?"); 
        builder.setTitle("メッセージ"); 
        builder.setPositiveButton("はい", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                        
                        Editor editor = LoginActivity.prefs.edit();  
                        editor.putBoolean("Acc_ischecked",true);
                        editor.putBoolean("MAG_ischecked",false); 
                        editor.putBoolean("Ori_ischecked",false); 
                        editor.putBoolean("GYro_ischecked",true); 
                        editor.putBoolean("Light_ischecked",true); 
                        editor.putBoolean("Tem_ischecked",false);
                        editor.putBoolean("Hum_ischecked",false);
                        editor.commit(); 
                        doPost.logout();
                        LoginActivity.sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
                        LoginActivity.sp.edit().putInt("PAGE_COUNT", 1).commit(); 
                        LoginActivity.sp.edit().putString("postUrl", null).commit(); 
                        Intent dbIntent = new Intent(StartActivity.this,
                                LoginActivity.class);
                        startActivity(dbIntent);
                        StartActivity.this.finish();
                    } 
                }); 
        builder.setNegativeButton("いいえ", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
    } 
	
	protected void dialog_exit() { 
        AlertDialog.Builder builder = new Builder(StartActivity.this); 
        builder.setMessage("アプリをシャットダウンしますか？"); 
        builder.setTitle("メッセージ"); 
        builder.setPositiveButton("Yes", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                        StartActivity.this.finish();
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
	
}

