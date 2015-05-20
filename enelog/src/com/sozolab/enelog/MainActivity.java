package com.sozolab.enelog;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sozolab.domain.FormFile;
import com.sozolab.enelog.R;
import com.sozolab.service.GetVersion;
import com.sozolab.service.SocketHttpRequester;
import com.sozolab.service.doPost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.view.WindowManager.LayoutParams;
public class MainActivity extends Activity implements SensorEventListener, OnClickListener{
	
	public static boolean doWrite = true;
	private TextView Acc, MAG,Ori, GYro,Light,Pressure,Tem,Hum,Proximity,WRSta,title,url_link2;
	private Button stop, label, dataView;
	private SensorManager sm1,sm2,sm3,sm4,sm5,sm6,sm7,sm8;
	private float lowX = 0, lowY = 0, lowZ = 0;
	private final float FILTERING_VALAUE = 0.1f;
	//private File file1,file2,file3,file4,file5,file6,file7,file8;
	private Handler handler1,handler2,handler3,handler4,handler5,handler6,handler7,handler_removecallbacks;
	
	private static final String TAG="MainActivity";
	
	/*StringBuilder sb2 = new StringBuilder();
	StringBuilder sb3 = new StringBuilder();
	StringBuilder sb4 = new StringBuilder();
	StringBuilder sb5 = new StringBuilder();
	StringBuilder upload_sb1 = new StringBuilder();
	StringBuilder upload_sb2 = new StringBuilder();
	StringBuilder upload_sb3 = new StringBuilder();
	StringBuilder upload_sb4 = new StringBuilder();
	StringBuilder upload_sb5 = new StringBuilder();*/
	StringBuilder sbtest = new StringBuilder();
	
	int lineIndex=0;
	String f1,f2,f3,f4,f5,f6,f7,upload1,upload2,upload3,upload4,upload5,upload6,upload7;
	private File file1,file2,file3,file4,file5,file6,file7,uploadfile1,uploadfile2,uploadfile3,uploadfile4,uploadfile5,uploadfile6,uploadfile7;
	int abc =0;
	String array1,array2,array3,array4,array5,array6,array7;
	public static String def_login_url= "https://eneact.sozolab.jp";
	public static String def_upload_url= "https://eneact.sozolab.jp";
	
	
	
	
	
/*private static class MyHandler extends Handler{
	private final WeakReference<MainActivity> mActivity;
	public MyHandler(MainActivity activity){
		mActivity = new WeakReference<MainActivity>(activity);
	}
	public void handleMessage(Message msg){
		MainActivity activity = mActivity.get();
		if(activity!=null){}
	}
}

private MyHandler handler1 = new MyHandler(this);
private MyHandler handler = new MyHandler(this);	*/
	
	
	public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.main_activity);
        
        doWrite = true;
        //値を初始化
        upload1 = LoginActivity.sp.getString("upload_1", "upload1.csv");
        f1 = LoginActivity.sp.getString("f1", "start1.csv");
        upload2 = LoginActivity.sp.getString("upload_2", "upload2.csv");
        f2 = LoginActivity.sp.getString("f2", "start2.csv");
        upload3 = LoginActivity.sp.getString("upload_3", "upload3.csv");
        f3 = LoginActivity.sp.getString("f3", "start3.csv");
        upload4 = LoginActivity.sp.getString("upload_4", "upload4.csv");
        f4 = LoginActivity.sp.getString("f4", "start4.csv");
        upload5 = LoginActivity.sp.getString("upload_5", "upload5.csv");
        f5 = LoginActivity.sp.getString("f5", "start5.csv");
        upload6 = LoginActivity.sp.getString("upload_6", "upload6.csv");
        f6 = LoginActivity.sp.getString("f6", "start6.csv");
        upload7 = LoginActivity.sp.getString("upload_7", "upload7.csv");
        f7 = LoginActivity.sp.getString("f7", "start7.csv");
        
        //title->enelog
        String tl = "EneLog";
        SpannableStringBuilder style=new SpannableStringBuilder(tl); 
        //SpannableStringBuilder Achieve CharSequence Interface  
        style.setSpan(new ForegroundColorSpan(Color.rgb(0, 153, 51)), 0, 1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ); 
        style.setSpan(new ForegroundColorSpan(Color.BLACK), 1, 3,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ); 
        style.setSpan(new ForegroundColorSpan(Color.rgb(255,0,51)), 3, 4,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ); 
        style.setSpan(new ForegroundColorSpan(Color.BLACK), 4, 5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
        title = (TextView)findViewById(R.id.title); 
        title.setText(style);
		WRSta = (TextView)findViewById(R.id.Station);
		stop = (Button)findViewById(R.id.stop);
		//ラベル付けへの画面遷移
		label = (Button)findViewById(R.id.label);
		label.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                // Sub 画面を起動
	                Intent intent = new Intent();
	                intent.setClassName("com.sozolab.enelog", "com.sozolab.enelog.LabelViewActivity");
	                startActivity(intent);
	            }
	        });
		
		dataView = (Button)findViewById(R.id.dataView);
		dataView.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				Intent intent2 = new Intent();
				intent2.setClassName("com.sozolab.enelog","com.sozolab.enelog.DataView");
				startActivity(intent2);
			}
		});
		
		url_link2 = (TextView)findViewById(R.id.url_link2); 
        url_link2.setMovementMethod(LinkMovementMethod.getInstance());
        Spanned text = Html.fromHtml("<a href=\"https://eneact.sozolab.jp/\">eneact.sozolab.jp</a>");
        url_link2.setText(text);
        
		
		if(LoginActivity.prefs.getBoolean("Acc_ischecked" ,true)){
			 Acc = (TextView)this.findViewById(R.id.Acc);
			sm1 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
			sm1.registerListener(this, 
	        		sm1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        		SensorManager.SENSOR_DELAY_NORMAL);
			 //スレッド動かす
			handler1=new Handler();
	        handler1.post(runnable1);
			abc++;
		}
		if(LoginActivity.prefs.getBoolean("MAG_ischecked" ,false)){
			 MAG = (TextView)this.findViewById(R.id.MAG);
			sm2 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
			sm2.registerListener(this, 
					sm2.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
	        		SensorManager.SENSOR_DELAY_NORMAL);
			handler2=new Handler();
	        handler2.post(runnable2);
			abc++;
		}
		if(LoginActivity.prefs.getBoolean("Ori_ischecked" ,false)){
			Ori = (TextView)this.findViewById(R.id.Ori);
			sm3 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
			sm3.registerListener(this, 
	        		sm3.getDefaultSensor(Sensor.TYPE_ORIENTATION),
	        		SensorManager.SENSOR_DELAY_NORMAL);
			handler3=new Handler();
	        handler3.post(runnable3);
			abc++;
		}
		if(LoginActivity.prefs.getBoolean("GYro_ischecked" ,true)){
			GYro = (TextView)this.findViewById(R.id.GYro);
			sm4 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
			sm4.registerListener(this, 
	        		sm4.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
	        		SensorManager.SENSOR_DELAY_NORMAL);
			handler4=new Handler();
	        handler4.post(runnable4);
			abc++;
		}
		if(LoginActivity.prefs.getBoolean("Light_ischecked" ,true)){
			Light = (TextView)this.findViewById(R.id.Light);
			sm5 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
			sm5.registerListener(this, 
	        		sm5.getDefaultSensor(Sensor.TYPE_LIGHT),
	        		SensorManager.SENSOR_DELAY_NORMAL);
			handler5=new Handler();
	        handler5.post(runnable5);
			abc++;
		}
		if(LoginActivity.prefs.getBoolean("Tem_ischecked" ,false)){
			Tem = (TextView)this.findViewById(R.id.Tem);
			sm6 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
			sm6.registerListener(this, 
	        		sm6.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
	        		SensorManager.SENSOR_DELAY_UI);
			handler6=new Handler();
	        handler6.post(runnable6);
			abc++;
			//////////////////////////////////System.out.println("Tem"+SensorManager.SENSOR_DELAY_UI);
		}
		if(LoginActivity.prefs.getBoolean("Hum_ischecked" ,false)){
			Hum = (TextView)this.findViewById(R.id.Hum);
			sm7 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
			sm7.registerListener(this, 
	        		sm7.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
	        		SensorManager.SENSOR_DELAY_UI);
			handler7=new Handler();
	        handler7.post(runnable7);
			abc++;
			////////////////////////////////////System.out.println("Hum"+(int)1e6);
		}

		/*if(SettingActivity.checked6){
			Pressure = (TextView)this.findViewById(R.id.Pressure);
			sm6 = (SensorManager)get//systemService(Context.SENSOR_SERVICE);
			sm6.registerListener(this, 
	        		sm6.getDefaultSensor(Sensor.TYPE_PRESSURE),
	        		SensorManager.SENSOR_DELAY_NORMAL);
			abc++;
		}
		if(SettingActivity.checked7){
			Tem = (TextView)this.findViewById(R.id.Tem);
			sm7 = (SensorManager)get//systemService(Context.SENSOR_SERVICE);
			sm7.registerListener(this, 
	        		sm7.getDefaultSensor(Sensor.TYPE_TEMPERATURE),
	        		SensorManager.SENSOR_DELAY_NORMAL);
		}
		if(SettingActivity.checked8){
			Proximity = (TextView)this.findViewById(R.id.Proximity);
			sm8 = (SensorManager)get//systemService(Context.SENSOR_SERVICE);
			sm8.registerListener(this, 
	        		sm8.getDefaultSensor(Sensor.TYPE_PROXIMITY),
	        		SensorManager.SENSOR_DELAY_NORMAL);
			abc++;
		}*/
		
		stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this); 
       // //////////////////////////////////System.out.println(LoginActivity.sp.getInt("PAGE_COUNT", 1));
        int page_count=LoginActivity.sp.getInt("PAGE_COUNT", 1);
        if(page_count==2){
        	dialog_return();
        }
	}
	//dialog
	 protected void dialog() { 
	        AlertDialog.Builder builder = new Builder(MainActivity.this); 
	        builder.setMessage("計測停止しますか"); 
	        builder.setTitle("メッセージ"); 
	        builder.setPositiveButton("OK", 
	                new android.content.DialogInterface.OnClickListener() { 
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        dialog.dismiss(); 
	                        doWrite = false;
	                        finish();
	                        Intent startIntent = new Intent(MainActivity.this,
				                    StartActivity.class);
							 startActivity(startIntent);
							 try{
								 //スッレドを止まる
								 handler_removecallbacks=new Handler();
							     handler_removecallbacks.post(runnable_removecallbacks);
							     try{
								//f1を削除する
						    		file1.delete();
						    		LoginActivity.sp.edit().putString("f1", null).commit(); 
						    		//////////////////////////////////System.out.println("file1を削除した");
							     }catch(Exception e){}
							     try{
								//f2を削除する
						    		file2.delete();
						    		LoginActivity.sp.edit().putString("f2", null).commit(); 
						    		//////////////////////////////////System.out.println("file2を削除した");
							     }catch(Exception e){}
							     try{
								//f3を削除する
						    		file3.delete();
						    		LoginActivity.sp.edit().putString("f3", null).commit(); 
						    		//////////////////////////////////System.out.println("file3を削除した");
							     }catch(Exception e){}
							     try{
								//f4を削除する
						    		file4.delete();
						    		LoginActivity.sp.edit().putString("f4", null).commit(); 
						    		//////////////////////////////////System.out.println("file4を削除した");
							     }catch(Exception e){}
							     try{
								//f5を削除する
						    		file5.delete();
						    		LoginActivity.sp.edit().putString("f5", null).commit(); 
						    		//////////////////////////////////System.out.println("file5を削除した");
							     }catch(Exception e){}
							     try{
										//f6を削除する
								    		file6.delete();
								    		LoginActivity.sp.edit().putString("f6", null).commit(); 
								    		//////////////////////////////////System.out.println("file6を削除した");
									     }catch(Exception e){}
							     try{
										//f7を削除する
								    		file7.delete();
								    		LoginActivity.sp.edit().putString("f7", null).commit(); 
								    		//////////////////////////////////System.out.println("file7を削除した");
									     }catch(Exception e){}
							     MainActivity.this.finish();
							 }catch(Exception e){
							 }
	                    } 
	                }); 
	        builder.setNegativeButton("Cancel", 
	                new android.content.DialogInterface.OnClickListener() { 
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        dialog.dismiss(); 
	                    } 
	                }); 
	        builder.create().show(); 
	    } 
	 //バックグラウンドで処理をする
	 protected void dialog_return() { 
	        AlertDialog.Builder builder = new Builder(MainActivity.this); 
	        builder.setMessage("計測開始しました。この後画面が閉じられますが、引き続き計測が行われます。"); 
	        builder.setTitle("メッセージ"); 
	        builder.setPositiveButton("OK", 
	                new android.content.DialogInterface.OnClickListener() { 
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        dialog.dismiss(); 
	                        
	                        Intent intent = new Intent(); 
	                        intent.setAction(Intent.ACTION_MAIN);
	                        intent.addCategory(Intent.CATEGORY_HOME);	        	  
	                        //startActivity(intent);	                    
	                    } 
	                }); 
	        builder.create().show(); 
	    } 
	    @Override 
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
	            dialog(); 
	            return false; 
	        } 
	        return false; 
	    }
	
    public void onPause(){
    	super.onPause();
    }
    public void onDestorye(){
    	try{
    	handler1.removeCallbacks(runnable1);
    	handler2.removeCallbacks(runnable2);
    	handler3.removeCallbacks(runnable3);
    	handler4.removeCallbacks(runnable4);
    	handler5.removeCallbacks(runnable5);
    	handler6.removeCallbacks(runnable6);
    	handler7.removeCallbacks(runnable7);
    	}catch(Exception e){
    	}
    }
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
    public void onClick(View v) {
		if (v.getId() == R.id.stop) {
	       dialog();
		}
    }
    public void onSensorChanged(SensorEvent event) {
    	String message = new String();
		Calendar Cld = Calendar.getInstance();
		int yy = Cld.get(Calendar.YEAR);
		int mh = Cld.get(Calendar.MONTH)+1;
		int dd = Cld.get(Calendar.DATE);
		int hh = Cld.get(Calendar.HOUR_OF_DAY);
		int mm = Cld.get(Calendar.MINUTE);
		int ss = Cld.get(Calendar.SECOND);
		int mi = Cld.get(Calendar.MILLISECOND);
	    DecimalFormat df = new DecimalFormat("#,##0.000");
		DecimalFormat df1 = new DecimalFormat("#,##000");
	if(doWrite){
		switch (event.sensor.getType()){
			case Sensor.TYPE_ACCELEROMETER :
				//f1
				file1 = new File(getFilesDir(), f1);
				////////////////////////////////////System.out.println("file1#####:"+file1);
				if  (!file1.exists()  && !file1.isDirectory())      
				{       
				    ////////////////////////////////////System.out.println("//file1不存在");  
				    //new
				     LoginActivity.sp.edit().putString("f1", "acc"+"_"+yy+"-"+mh+"-"+dd+"T"+hh+":"+mm+":"+ss+":"+mi+"09:00"+".csv").commit(); 
				     f1 = LoginActivity.sp.getString("f1", "start1.csv");
				}
			//if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER  ) {
				float X1 = event.values[0];
				float Y1 = event.values[1];
				float Z1 = event.values[2];
		/*		//Low-Pass Filter
		        lowX = X1 * FILTERING_VALAUE + lowX * (1.0f - FILTERING_VALAUE);
		        lowY = Y1 * FILTERING_VALAUE + lowY * (1.0f - FILTERING_VALAUE);
		        lowZ = Z1 * FILTERING_VALAUE + lowZ * (1.0f - FILTERING_VALAUE);        
		        //High-pass filter
		        float highX  = X1 -  lowX;
		        float highY  = Y1 -  lowY;
		        float highZ  = Z1 -  lowZ;*/
				message = yy + "-"; 
				message += mh + "-";
				message += dd + "T";
				message += hh + ":";
				message += mm + ":";
				message += ss + ":";
				message += df1.format(mi) + "+";
				message += "09:00,";
				message += df.format(X1) + ",";
				message += df.format(Y1) + ",";
				message += df.format(Z1) +"\n";
					Acc.setText("Acc: "+message + "\n");
					writeFile(f1, message);
			break;
			//	if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			case 	Sensor.TYPE_MAGNETIC_FIELD :
				file2 = new File(getFilesDir(), f2);
				////////////////////////////////////System.out.println("file1#####:"+file1);
				if  (!file2.exists()  && !file2.isDirectory())      
				{       
				    ////////////////////////////////////System.out.println("//file2不存在");  
				  //new
				     LoginActivity.sp.edit().putString("f2", "mag"+"_"+yy+"-"+mh+"-"+dd+"T"+hh+":"+mm+":"+ss+":"+mi+"09:00"+".csv").commit(); 
				     f2 = LoginActivity.sp.getString("f2", "start2.csv");
				}
				float X2 = event.values[0];
				float Y2 = event.values[1];
				float Z2 = event.values[2];
				message = yy + "-"; 
				message += mh + "-";
				message += dd + "T";
				message += hh + ":";
				message += mm + ":";
				message += ss + ":";
				message += df1.format(mi) + "+";
				message += "09:00,";
				message += df.format(X2) + ",";
				message += df.format(Y2) + ",";
				message += df.format(Z2)+"\n";      
					MAG.setText("MAG: "+message + "\n");
					writeFile(f2, message);
			break;
			//	if(event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			case 		Sensor.TYPE_ORIENTATION:
				file3 = new File(getFilesDir(), f3);
				////////////////////////////////////System.out.println("file1#####:"+file1);
				if  (!file3.exists()  && !file3.isDirectory())      
				{       
				    ////////////////////////////////////System.out.println("//file3不存在");  
				    //new
				     LoginActivity.sp.edit().putString("f3", "ori"+"_"+yy+"-"+mh+"-"+dd+"T"+hh+":"+mm+":"+ss+":"+mi+"09:00"+".csv").commit(); 
				     f3 = LoginActivity.sp.getString("f3", "start3.csv");
				}
				float X3 = event.values[0];
				float Y3 = event.values[1];
				float Z3 = event.values[2];
				message = yy + "-"; 
				message += mh + "-";
				message += dd + "T";
				message += hh + ":";
				message += mm + ":";
				message += ss + ":";
				message += df1.format(mi) + "+";
				message += "09:00,";
				message += df.format(X3) + ",";
				message += df.format(Y3) + ",";
				message += df.format(Z3)+"\n";      
					Ori.setText("Ori: "+message + "\n");
					writeFile(f3, message);
			break;
			//	if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			case	Sensor.TYPE_GYROSCOPE:
				file4 = new File(getFilesDir(), f4);
				////////////////////////////////////System.out.println("file1#####:"+file1);
				if  (!file4.exists()  && !file4.isDirectory())      
				{       
				    ////////////////////////////////////System.out.println("//file4不存在");  
				    //new
				     LoginActivity.sp.edit().putString("f4", "gyro"+"_"+yy+"-"+mh+"-"+dd+"T"+hh+":"+mm+":"+ss+":"+mi+"09:00"+".csv").commit(); 
				     f4 = LoginActivity.sp.getString("f4", "start4.csv");
				}
				float X4 = event.values[0];
				float Y4 = event.values[1];
				float Z4 = event.values[2];
				message = yy + "-"; 
				message += mh + "-";
				message += dd + "T";
				message += hh + ":";
				message += mm + ":";
				message += ss + ":";
				message += df1.format(mi) + "+";
				message += "09:00,";
				message += df.format(X4) + ",";
				message += df.format(Y4) + ",";
				message += df.format(Z4)+"\n";      
					GYro.setText("GYro: "+message + "\n");
					writeFile(f4, message);
			break;
			//	if(event.sensor.getType() == Sensor.TYPE_LIGHT) {
			case Sensor.TYPE_LIGHT:
				file5 = new File(getFilesDir(), f5);
				////////////////////////////////////System.out.println("file1#####:"+file1);
				if  (!file5.exists()  && !file5.isDirectory())      
				{       
				    ////////////////////////////////////System.out.println("//file5不存在");  
				    //new
				     LoginActivity.sp.edit().putString("f5", "light"+"_"+yy+"-"+mh+"-"+dd+"T"+hh+":"+mm+":"+ss+":"+mi+"09:00"+".csv").commit(); 
				     f5 = LoginActivity.sp.getString("f5", "start5.csv");
				}
				float X5 = event.values[0];
				float Y5 = event.values[1];
				float Z5 = event.values[2];
				message = yy + "-"; 
				message += mh + "-";
				message += dd + "T";
				message += hh + ":";
				message += mm + ":";
				message += ss + ":";
				message += df1.format(mi) + "+";
				message += "09:00,";
				message += df.format(X5) + ",";
				message += df.format(Y5) + ",";
				message += df.format(Z5)+"\n";      
					Light.setText("Light: "+message + "\n");
					writeFile(f5, message);
			break;
			
			case Sensor.TYPE_AMBIENT_TEMPERATURE:
				file6 = new File(getFilesDir(), f6);
				////////////////////////////////////System.out.println("file1#####:"+file1);
				if  (!file6.exists()  && !file6.isDirectory())      
				{       
					
//				    //////////////////////////////////System.out.println("//不存在"+!file6.exists()+!file6.isDirectory());  
				    //new
				     LoginActivity.sp.edit().putString("f6", "Tem"+"_"+yy+"-"+mh+"-"+dd+"T"+hh+":"+mm+":"+ss+":"+mi+"09:00"+".csv").commit(); 
				     f6 = LoginActivity.sp.getString("f6", "start6.csv");
				}
				
				float X6 = event.values[0];
				float Y6 = event.values[1];
				float Z6 = event.values[2];
				message = yy + "-"; 
				message += mh + "-";
				message += dd + "T";
				message += hh + ":";
				message += mm + ":";
				message += ss + ":";
				message += df1.format(mi) + "+";
				message += "09:00,";
				message += df.format(X6) + ",";
				message += df.format(Y6) + ",";
				message += df.format(Z6)+"\n";      
					Tem.setText("Tem: "+message + "\n");
					writeFile(f6, message);
					//////////////////////////////////System.out.println(message+"\n");
			break;
			
			case Sensor.TYPE_RELATIVE_HUMIDITY:
				file7 = new File(getFilesDir(), f7);
				////////////////////////////////////System.out.println("file1#####:"+file1);
				if  (!file7.exists()  && !file7.isDirectory())      
				{       
					
//				    //////////////////////////////////System.out.println("//不存在"+!file7.exists()+!file5.isDirectory());  
				    //new
				     LoginActivity.sp.edit().putString("f7", "Hum"+"_"+yy+"-"+mh+"-"+dd+"T"+hh+":"+mm+":"+ss+":"+mi+"09:00"+".csv").commit(); 
				     f7 = LoginActivity.sp.getString("f7", "start7.csv");
				}
				
				float X7 = event.values[0];
				float Y7 = event.values[1];
				float Z7 = event.values[2];
				message = yy + "-"; 
				message += mh + "-";
				message += dd + "T";
				message += hh + ":";
				message += mm + ":";
				message += ss + ":";
				message += df1.format(mi) + "+";
				message += "09:00,";
				message += df.format(X7) + ",";
				message += df.format(Y7) + ",";
				message += df.format(Z7)+"\n";      
					Hum.setText("Hum: "+message + "\n");
					writeFile(f7, message);
					////////////////////////////////////System.out.println(message);
			break;

			
			
			
			
			
			
			
	//	else if(event.sensor.getType() == Sensor.TYPE_PRESSURE) {
/*		case 	Sensor.TYPE_PRESSURE:
			Log.i("MainActivity",event.sensor.getType()+"6");
			float X6 = event.values[0];
			float Y6 = event.values[1];
			float Z6 = event.values[2];
			message = yy + "_"; 
			message += mh + "_";
			message += dd + "_";
			message += hh + "_";
			message += mm + "_";
			message += ss + "_";
			message += df1.format(mi) + ",";
			message += df.format(X6) + ",";
			message += df.format(Y6) + ",";
			message += df.format(Z6);      
			Pressure.setText(message + "\n");
			writeFile(f6, message);
		break;
	//	if(event.sensor.getType() == Sensor.TYPE_TEMPERATURE) {
		case		Sensor.TYPE_TEMPERATURE:
			float X7 = event.values[0];
			float Y7 = event.values[1];
			float Z7 = event.values[2];
			message = yy + "_"; 
			message += mh + "_";
			message += dd + "_";
			message += hh + "_";
			message += mm + "_";
			message += ss + "_";
			message += df1.format(mi) + ",";
			message += df.format(X7) + ",";
			message += df.format(Y7) + ",";
			message += df.format(Z7);      
			Tem.setText(message + "\n");
			writeFile(f7, message);
		break;
	//	if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
		case		Sensor.TYPE_PROXIMITY:
			float X8 = event.values[0];
			float Y8 = event.values[1];
			float Z8 = event.values[2];
			message = yy + "_"; 
			message += mh + "_";
			message += dd + "_";
			message += hh + "_";
			message += mm + "_";
			message += ss + "_";
			message += df1.format(mi) + ",";
			message += df.format(X8) + ",";
			message += df.format(Y8) + ",";
			message += df.format(Z8);      
			Proximity.setText(message + "\n");
			writeFile(f8, message);
		break;*/
		}
		////////////////////////////////////System.out.println(f1);
	}
   }
    public void writeFile(String fileName,String message) {
        try {
        	FileOutputStream fout = openFileOutput(fileName, Context.MODE_APPEND);
        	byte [] bytes = message.getBytes();
        	
        	fout.write(bytes);
        	fout.close();
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
    public String read(String file, String array){
    	try{
    	FileInputStream fis = openFileInput(file);
    	BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
    	String s;
    	StringBuilder sb1 = new StringBuilder();
    	while((s = reader.readLine())!=null){
    		sb1.append(s+"\n");
    	//	//////////////////////////////////System.out.println("upload1.sb1:"+s);
    	}
        array = new String(sb1);
        sb1.delete(0, sb1.length());
//        //////////////////////////////////System.out.println("array:"+array);
    	reader.close();
    	fis.close();
    	}catch(Exception e) {
        	e.printStackTrace();
        }
		return array;
    	}
    	 public void copyFile(String fileName,String fileName2){
    	    	try{
    	    	FileInputStream fis = openFileInput(fileName);
    	    	BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
    	    	String s;
    	    	String mess;
    	    	while((s = reader.readLine())!=null){
    	    		//sは正しい
    	    		mess = new String(sbtest.append(s));
    	    		////////////////////////////////////System.out.println("mess:"+mess);
    	    		//sbtestは正しい
    	    		writeFile(fileName2,mess+"\n");
    	    		//upload1ファイルは正しい
    	    		sbtest.delete(0,sbtest.length());
    	    	}
    	    	mess = null;
    	reader.close();
    	fis.close();
    	}catch(Exception e) {
        	e.printStackTrace();
        }
    }
    	 
     Runnable runnable1=new Runnable() {
        public void run() {
        	////////////////////////////////////System.out.println("11111");
    	// 通常バックグランドをここに記述します
        	//postDelayed説明、大事です！
        	 handler1.postDelayed(runnable1, 60000);
        	 ////////////////////////////////////System.out.println("22222");
    	try {
    		////////////////////////////////////System.out.println("33333");
    		doPost post = new doPost();
    		uploadfile1 = new File(getFilesDir(), upload1);
    		if(!uploadfile1.exists()  && !uploadfile1.isDirectory()){
                //upload1はBのこと
    			////////////////////////////////////System.out.println("uploadfile1不存在、upload1.csvを作成します");
    		    //new
    		    upload1 = LoginActivity.sp.getString("upload_1", "upload1.csv");
//    			upload1 = "upload1.csv";
                }
    		//new
    		
    		////////////////////////////////////System.out.println("f1の名前は:"+f1);
    		//f1はAのこと
    		//コピー
    	  copyFile(f1,upload1);
    	  ////////////////////////////////////System.out.println("f1からupload1にコピーしました");
    	    //f1を削除する
    		file1.delete();
    		////////////////////////////////////System.out.println("file1を削除した");
    		//new
    		LoginActivity.sp.edit().putString("f1", null).commit(); 
    		
    		////////////////////////////////////System.out.println("f1を削除した");
    		array1 = read(upload1,array1);
    		////////////////////////////////////System.out.println("upload1:"+upload1);
            //空に設定する
    		try{
            if(checkNetworkAvailable()){
            	//////////////////////////////////System.out.println("88");
            	
            		try{
            			post.doPost(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload","filename",f1,"type","acc","version",getVersionName(),"data",array1);
            			//post.doPost("192.168.200.24:80/","filename",f1,"type","acc","version",getVersionName(),"data",array1);
            		}catch(Exception e){
            			////////////////////////////////////System.out.println("error");
            		}
            //	//////////////////////////////////System.out.println(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload");
            	
            
            	try{
            	//レスポンス message, infoを確認
            	if(doPost.info.equals("success")){
            		//////////////////////////////////System.out.println("アップロード OK!!! array1とuploadfile1を削除する");
         			array1 = null;
         			uploadfile1.delete();
         			//new
         			LoginActivity.sp.edit().putString("upload_1", null).commit(); 
         		}else{
         			////////////////////////////////////System.out.println("アップロード NOT YET!!! array1とuploadfile1を保留します");
         		}
            	}catch(Exception e){
            	}
            }
    		}catch(Exception e){
        		
        	}
            	
            
            
			////////////////////////////////////System.out.println("10");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
    };
    
    Runnable runnable2=new Runnable() {
        public void run() {
        	////////////////////////////////////System.out.println("11111");
    	// 通常バックグランドをここに記述します
        	//postDelayed説明、大事です！
        	 handler2.postDelayed(runnable2, 60000);
        	 ////////////////////////////////////System.out.println("22222");
    	try {
    		////////////////////////////////////System.out.println("33333");
    		doPost post = new doPost();
    		uploadfile2 = new File(getFilesDir(), upload2);
    		if(!uploadfile2.exists()  && !uploadfile2.isDirectory()){
                //upload2はBのこと
    			////////////////////////////////////System.out.println("uploadfile2不存在、upload2.csvを作成します");
    		    //new
    		    upload2 = LoginActivity.sp.getString("upload_2", "upload2.csv");
    		    
//    			upload2 = "upload2.csv";
                }
    		//new
    		
    		////////////////////////////////////System.out.println("f2の名前は:"+f2);
    		//f2はAのこと
    		//コピー
    	  copyFile(f2,upload2);
    	  ////////////////////////////////////System.out.println("f2からupload2にコピーしました");
    	    //f2を削除する
    		file2.delete();
    		////////////////////////////////////System.out.println("file2を削除した");
    		//new
    		LoginActivity.sp.edit().putString("f2", null).commit(); 
    		
    		////////////////////////////////////System.out.println("f2を削除した");
    		array2 = read(upload2,array2);
    		////////////////////////////////////System.out.println("upload2:"+upload2);
            //空に設定する
    		try{
            if(checkNetworkAvailable()){
            	////////////////////////////////////System.out.println("88");
            	
            		try{
            			post.doPost(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload","filename",f2,"type","mag","version",getVersionName(),"data",array1);
            			//post.doPost("192.268.200.24:80/","filename",f2,"type","mag","version",getVersionName(),"data",array2);
            		}catch(Exception e){
            			////////////////////////////////////System.out.println("error");
            		}
            	////////////////////////////////////System.out.println(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload");
            	
            
            	try{
                	//レスポンス message, infoを確認
                	if(doPost.info.equals("success")){
                		////////////////////////////////////System.out.println("アップロード OK!!! array2とuploadfile2を削除する");
             			array2 = null;
             			uploadfile2.delete();
             			//new
             			LoginActivity.sp.edit().putString("upload_2", null).commit(); 
             		}else{
             			////////////////////////////////////System.out.println("アップロード NOT YET!!! array2とuploadfile2を保留します");
             		}
            	}catch(Exception e){
            		
            	}
            }
    		}catch(Exception e){
        		
        	}
            	
            
            
			//////////////////////////////////System.out.println("10");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
    };
    
    Runnable runnable3=new Runnable() {
        public void run() {
        	////////////////////////////////////System.out.println("11111");
    	// 通常バックグランドをここに記述します
        	//postDelayed説明、大事です！
        	 handler3.postDelayed(runnable3, 60000);
        	 ////////////////////////////////////System.out.println("22222");
    	try {
    		////////////////////////////////////System.out.println("33333");
    		doPost post = new doPost();
    		uploadfile3 = new File(getFilesDir(), upload3);
    		if(!uploadfile3.exists()  && !uploadfile3.isDirectory()){
                //upload3はBのこと
    			////////////////////////////////////System.out.println("uploadfile3不存在、upload3.csvを作成します");
    		    //new
    		    upload3 = LoginActivity.sp.getString("upload_3", "upload3.csv");
    		    
//    			upload3 = "upload3.csv";
                }
    		//new
    		
    		////////////////////////////////////System.out.println("f3の名前は:"+f3);
    		//f3はAのこと
    		//コピー
    	  copyFile(f3,upload3);
    	  ////////////////////////////////////System.out.println("f3からupload3にコピーしました");
    	    //f3を削除する
    		file3.delete();
    		////////////////////////////////////System.out.println("file3を削除した");
    		//new
    		LoginActivity.sp.edit().putString("f3", null).commit(); 
    		
    		////////////////////////////////////System.out.println("f3を削除した");
    		array3 = read(upload3,array3);
    		////////////////////////////////////System.out.println("upload3:"+upload3);
            //空に設定する
    		try{
            if(checkNetworkAvailable()){
            	////////////////////////////////////System.out.println("88");
            	
            		try{
            			post.doPost(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload","filename",f3,"type","ori","version",getVersionName(),"data",array3);
            			//post.doPost("192.168.200.24:80/","filename",f3,"type","ori","version",getVersionName(),"data",array3);
            		}catch(Exception e){
            			//////////////////////////////////System.out.println("error");
            		}
            	//////////////////////////////////System.out.println(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload");
            	
            
            	try{
                	//レスポンス message, infoを確認
                	if(doPost.info.equals("success")){
                		//////////////////////////////////System.out.println("アップロード OK!!! array3とuploadfile3を削除する");
             			array3 = null;
             			uploadfile3.delete();
             			//new
             			LoginActivity.sp.edit().putString("upload_3", null).commit(); 
             		}else{
             			//////////////////////////////////System.out.println("アップロード NOT YET!!! array3とuploadfile3を保留します");
             		}
            	}catch(Exception e){
            		
            	}
            }
    		}catch(Exception e){
        		
        	}
            	
            
            
			//////////////////////////////////System.out.println("10");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
    };
    
    Runnable runnable4=new Runnable() {
        public void run() {
        	//////////////////////////////////System.out.println("11111");
    	// 通常バックグランドをここに記述します
        	//postDelayed説明、大事です！
        	 handler4.postDelayed(runnable4, 60000);
        	 //////////////////////////////////System.out.println("22222");
    	try {
    		//////////////////////////////////System.out.println("33333");
    		doPost post = new doPost();
    		uploadfile4 = new File(getFilesDir(), upload4);
    		if(!uploadfile4.exists()  && !uploadfile4.isDirectory()){
                //upload4はBのこと
    			//////////////////////////////////System.out.println("uploadfile4不存在、upload4.csvを作成します");
    		    //new
    		    upload4 = LoginActivity.sp.getString("upload_4", "upload4.csv");
    		    
//    			upload4 = "upload4.csv";
                }
    		//new
    		
    		//////////////////////////////////System.out.println("f4の名前は:"+f4);
    		//f4はAのこと
    		//コピー
    	  copyFile(f4,upload4);
    	  //////////////////////////////////System.out.println("f4からupload4にコピーしました");
    	    //f4を削除する
    		file4.delete();
    		//////////////////////////////////System.out.println("file4を削除した");
    		//new
    		LoginActivity.sp.edit().putString("f4", null).commit(); 
    		
    		//////////////////////////////////System.out.println("f4を削除した");
    		array4 = read(upload4,array4);
    		////////////////////////////////////System.out.println("upload4:"+upload4);
            //空に設定する
    		try{
            if(checkNetworkAvailable()){
            	//////////////////////////////////System.out.println("88");
            	
            		try{
            			post.doPost(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload","filename",f4,"type","gyro","version",getVersionName(),"data",array4);
            			//post.doPost("192.168.200.24:80/","filename",f4,"type","gyro","version",getVersionName(),"data",array4);
            		}catch(Exception e){
            			//////////////////////////////////System.out.println("error");
            		}
            	//////////////////////////////////System.out.println(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload");
            	
            
            	
            	try{
                	//レスポンス message, infoを確認
                	if(doPost.info.equals("success")){
                		//////////////////////////////////System.out.println("アップロード OK!!! array4とuploadfile4を削除する");
             			array4 = null;
             			uploadfile4.delete();
             			//new
             			LoginActivity.sp.edit().putString("upload_4", null).commit(); 
             		}else{
             			//////////////////////////////////System.out.println("アップロード NOT YET!!! array4とuploadfile4を保留します");
             		}
            	}catch(Exception e){
            		
            	}
            }
    		}catch(Exception e){
        		
        	}
            	
            
            
			//////////////////////////////////System.out.println("10");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
    };
    
    Runnable runnable5=new Runnable() {
        public void run() {
        	//////////////////////////////////System.out.println("11111");
    	// 通常バックグランドをここに記述します
        	//postDelayed説明、大事です！
        	 handler5.postDelayed(runnable5, 60000);
        	 //////////////////////////////////System.out.println("22222");
    	try {
    		//////////////////////////////////System.out.println("33333");
    		doPost post = new doPost();
    		uploadfile5 = new File(getFilesDir(), upload5);
    		if(!uploadfile5.exists()  && !uploadfile5.isDirectory()){
                //upload5はBのこと
    			//////////////////////////////////System.out.println("uploadfile5不存在、upload5.csvを作成します");
    		    //new
    		    upload5 = LoginActivity.sp.getString("upload_5", "upload5.csv");
    		    
//    			upload5 = "upload5.csv";
                }
    		//new
    		
    		//////////////////////////////////System.out.println("f5の名前は:"+f5);
    		//f5はAのこと
    		//コピー
    	  copyFile(f5,upload5);
    	  //////////////////////////////////System.out.println("f5からupload5にコピーしました");
    	    //f5を削除する
    		file5.delete();
    		//////////////////////////////////System.out.println("file5を削除した");
    		//new
    		LoginActivity.sp.edit().putString("f5", null).commit(); 
    		
    		//////////////////////////////////System.out.println("f5を削除した");
    		array5 = read(upload5,array5);
    		////////////////////////////////////System.out.println("upload5:"+upload5);
            //空に設定する
    		try{
            if(checkNetworkAvailable()){
            	//////////////////////////////////System.out.println("88");
            	
            		try{
            			post.doPost(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload","filename",f5,"type","light","version",getVersionName(),"data",array5);
            			//post.doPost("192.168.200.24:80/","filename",f5,"type","light","version",getVersionName(),"data",array5);
            		}catch(Exception e){
            			//////////////////////////////////System.out.println("error");
            		}
            	//////////////////////////////////System.out.println(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload");
            	
            
            	try{
                	//レスポンス message, infoを確認
                	if(doPost.info.equals("success")){
                		//////////////////////////////////System.out.println("アップロード OK!!! array5とuploadfile5を削除する");
             			array5 = null;
             			uploadfile5.delete();
             			//new
             			LoginActivity.sp.edit().putString("upload_5", null).commit(); 
             		}else{
             			//////////////////////////////////System.out.println("アップロード NOT YET!!! array5とuploadfile5を保留します");
             		}
            	}catch(Exception e){
            		
            	}
            }
    		}catch(Exception e){
        		
        	}
            	
            
            
			//////////////////////////////////System.out.println("10");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
    };
    
    Runnable runnable6=new Runnable() {
        public void run() {
        	//////////////////////////////////System.out.println("11111");
    	// 通常バックグランドをここに記述します
        	//postDelayed説明、大事です！
        	 handler6.postDelayed(runnable6, 60000);
        	 //////////////////////////////////System.out.println("22222");
    	try {
    		//////////////////////////////////System.out.println("33333");
    		doPost post = new doPost();
    		uploadfile6 = new File(getFilesDir(), upload6);
    		if(!uploadfile6.exists()  && !uploadfile6.isDirectory()){
                //upload6はBのこと
    			//////////////////////////////////System.out.println("uploadfile6不存在、upload6.csvを作成します");
    		    //new
    		    upload6 = LoginActivity.sp.getString("upload_6", "upload6.csv");
//    			upload6 = "upload6.csv";
                }
    		//new
    		
    		//////////////////////////////////System.out.println("f6の名前は:"+f6);
    		//f6はAのこと
    		//コピー
    	  copyFile(f6,upload6);
    	  //////////////////////////////////System.out.println("f6からupload6にコピーしました");
    	    //f6を削除する
    		file6.delete();
    		//////////////////////////////////System.out.println("file6を削除した");
    		//new
    		LoginActivity.sp.edit().putString("f6", null).commit(); 
    		
    		//////////////////////////////////System.out.println("f6を削除した");
    		array6 = read(upload6,array6);
    		////////////////////////////////////System.out.println("upload6:"+upload6);
            //空に設定する
    		try{
            if(checkNetworkAvailable()){
            	//////////////////////////////////System.out.println("88");
            	
            		try{
            			post.doPost(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload","filename",f6,"type","Tem","version",getVersionName(),"data",array6);
            			//post.doPost("192.168.200.24:80/","filename",f6,"type","acc","version",getVersionName(),"data",array6);
            		}catch(Exception e){
            			//////////////////////////////////System.out.println("error");
            		}
            	//////////////////////////////////System.out.println(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload");
            	
            
            	try{
                	//レスポンス message, infoを確認
                	if(doPost.info.equals("success")){
                		//////////////////////////////////System.out.println("アップロード OK!!! array6とuploadfile6を削除する");
             			array6 = null;
             			uploadfile6.delete();
             			//new
             			LoginActivity.sp.edit().putString("upload_6", null).commit(); 
             		}else{
             			//////////////////////////////////System.out.println("アップロード NOT YET!!! array6とuploadfile6を保留します");
             		}
            	}catch(Exception e){
            		
            	}
            }
    		}catch(Exception e){
        		
        	}
            	
            
            
			//////////////////////////////////System.out.println("10");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
    };
    
    Runnable runnable7=new Runnable() {
        public void run() {
        	//////////////////////////////////System.out.println("11111");
    	// 通常バックグランドをここに記述します
        	//postDelayed説明、大事です！
        	 handler7.postDelayed(runnable7, 60000);
        	 //////////////////////////////////System.out.println("22222");
    	try {
    		//////////////////////////////////System.out.println("33333");
    		doPost post = new doPost();
    		uploadfile7 = new File(getFilesDir(), upload7);
    		if(!uploadfile7.exists()  && !uploadfile7.isDirectory()){
                //upload7はBのこと
    			//////////////////////////////////System.out.println("uploadfile7不存在、upload7.csvを作成します");
    		    //new
    		    upload7 = LoginActivity.sp.getString("upload_7", "upload7.csv");
//    			upload7 = "upload7.csv";
                }
    		//new
    		
    		//////////////////////////////////System.out.println("f7の名前は:"+f7);
    		//f7はAのこと
    		//コピー
    	  copyFile(f7,upload7);
    	  //////////////////////////////////System.out.println("f7からupload7にコピーしました");
    	    //f7を削除する
    		file7.delete();
    		//////////////////////////////////System.out.println("file7を削除した");
    		//new
    		LoginActivity.sp.edit().putString("f7", null).commit(); 
    		
    		//////////////////////////////////System.out.println("f7を削除した");
    		array7 = read(upload7,array7);
    		////////////////////////////////////System.out.println("upload7:"+upload7);
            //空に設定する
    		try{
            if(checkNetworkAvailable()){
            	//////////////////////////////////System.out.println("88");
            	
            		try{
            			post.doPost(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload","filename",f7,"type","Hum","version",getVersionName(),"data",array7);
            			//post.doPost("192.168.200.24:80/","filename",f7,"type","acc","version",getVersionName(),"data",array7);
            		}catch(Exception e){
            			//////////////////////////////////System.out.println("error");
            		}
            	//////////////////////////////////System.out.println(LoginActivity.sp.getString("upload_url", def_upload_url)+"/sensors/0/upload");
            	
            
            	try{
                	//レスポンス message, infoを確認
                	if(doPost.info.equals("success")){
                		//////////////////////////////////System.out.println("アップロード OK!!! array7とuploadfile7を削除する");
             			array7 = null;
             			uploadfile7.delete();
             			//new
             			LoginActivity.sp.edit().putString("upload_7", null).commit(); 
             		}else{
             			//////////////////////////////////System.out.println("アップロード NOT YET!!! array7とuploadfile7を保留します");
             		}
            	}catch(Exception e){
            		
            	}
            }
    		}catch(Exception e){
        		
        	}
            	
            
            
			//////////////////////////////////System.out.println("10");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
    };
    
    
    Runnable runnable_removecallbacks = new Runnable(){
    	public void run(){
    		try{
			 handler1.removeCallbacks(runnable1);
			 //////////////////////////////////System.out.println("スッレドを停止しました1");
    		}catch(Exception e){
    		}
    		try{
			 handler2.removeCallbacks(runnable2);
			 //////////////////////////////////System.out.println("スッレドを停止しました2");
    		}catch(Exception e){
			}
    		try{
			 handler3.removeCallbacks(runnable3);
			 //////////////////////////////////System.out.println("スッレドを停止しました3");
    		}catch(Exception e){
			}
    		try{
			 handler4.removeCallbacks(runnable4);
			 //////////////////////////////////System.out.println("スッレドを停止しました4");
    		}catch(Exception e){
    		}
			try{
			 handler5.removeCallbacks(runnable5);
			 //////////////////////////////////System.out.println("スッレドを停止しました5");
    		}catch(Exception e){
    		}
			try{
			 handler6.removeCallbacks(runnable6);
			 //////////////////////////////////System.out.println("スッレドを停止しました6");
    		}catch(Exception e){
    		}
			try{
			 handler7.removeCallbacks(runnable7);
			 //////////////////////////////////System.out.println("スッレドを停止しました7");
    		}catch(Exception e){
    		}
    	}
    };
    
  /*  Runnable runnable5=new Runnable() {
        public void run() {
    	// 通常バックグランドをここに記述します
        	//postDelayed説明、大事です！
        	 handler5.postDelayed(runnable5, 60000);
        	 try {
         		doPost post = new doPost();
         		uploadfile5 = new File(getFilesDir(), upload5);
         		if(!uploadfile5.exists()  && !uploadfile5.isDirectory()){
                     //upload5はBのこと
         			//////////////////////////////////System.out.println("upload5.csvを作成します");
         		    //new
         		    upload5 = LoginActivity.sp.getString("upload_5", "upload5.csv");
         		    
//         			upload5 = "upload5.csv";
                     }
         		//new
         		
         		//////////////////////////////////System.out.println("f5:"+f5);
         		//f5はAのこと
         		//コピー
         	  copyFile(f5,upload5);
         	    //f5を削除する
         		file5.delete();
         		//new
         		LoginActivity.sp.edit().putString("f5", null).commit(); 
         		//////////////////////////////////System.out.println("5");
         		array5 = read(upload5,array5);
         		//////////////////////////////////System.out.println("upload5:"+upload5);
//         		//////////////////////////////////System.out.println("array5:"+array5);
                 //////////////////////////////////System.out.println("7");
                 //空に設定する
                 if(checkNetworkAvailable()){
                 	//////////////////////////////////System.out.println("88");
                 	if(("").equals(LoginActivity.sp.getString("upload_url", "https://eneact.sozolab.jp/sensors/0/upload"))){
                 		try{
                 			post.doPost("https://eneact.sozolab.jp/sensors/0/upload","filename",f5,"type","light","version",getVersionName(),"data",array5);
                 		}catch(Exception e){
                 			//////////////////////////////////System.out.println("error");
                 		}
                 		//////////////////////////////////System.out.println("https://eneact.sozolab.jp/sensors/0/upload");
                 	}else{
                 		try{
                 			post.doPost(LoginActivity.sp.getString("upload_url", "https://eneact.sozolab.jp/sensors/0/upload"),"filename",f5,"type","light","version",getVersionName(),"data",array5);
                 		}catch(Exception e){
                 			//////////////////////////////////System.out.println("error");
                 		}
                 	//////////////////////////////////System.out.println(LoginActivity.sp.getString("upload_url", "https://eneact.sozolab.jp/sensors/0/upload"));
                 	}
                 	//レスポンス200を確認
                 	//////////////////////////////////System.out.println("status:"+doPost.status);
                 	if(doPost.status==200){
             			array5 = null;
             			uploadfile5.delete();
             			//new
             			LoginActivity.sp.edit().putString("upload_5", null).commit(); 
             		}
                 }
     			//////////////////////////////////System.out.println("10");
     		} catch (Exception e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
        }
    };*/
    
    private String getVersionName() throws Exception  
    {  
            // packagemanager
            PackageManager packageManager = getPackageManager();  
            // getPackageName()，  
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);  
            String version = packInfo.versionName;  
            return version;  
    }  
    public boolean checkNetworkAvailable() {  
        boolean isNetworkAvailable = false;  
        android.net.ConnectivityManager connManager = (android.net.ConnectivityManager)getApplicationContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE);  
        if(connManager.getActiveNetworkInfo() != null){  
            isNetworkAvailable = connManager.getActiveNetworkInfo().isAvailable();  
        }  
        return isNetworkAvailable;  
    }
}
   
