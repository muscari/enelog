package com.sozolab.enelog;


import com.sozolab.enelog.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity  {
	private TextView note,text_login_url,text_upload_url ;
	private CheckBox Acc,MAG, Ori, GYro,Light,Pressure,Tem,Proximity,Hum;
	private Button OK;
	 EditText login_url,upload_url;
	
	public static boolean checked1,checked2,checked3,checked4,checked5,checked6,checked7,checked8;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		
		note = (TextView)this.findViewById(R.id.note);
		text_login_url = (TextView)this.findViewById(R.id.text_login_url );
		text_upload_url = (TextView)this.findViewById(R.id.text_upload_url);
		Acc = (CheckBox)this.findViewById(R.id.Acc);
		MAG = (CheckBox)this.findViewById(R.id.MAG);
		Ori = (CheckBox)this.findViewById(R.id.Ori);
		GYro = (CheckBox)this.findViewById(R.id.GYro);
		Light = (CheckBox)this.findViewById(R.id.Light);
		Tem = (CheckBox)this.findViewById(R.id.Tem);
		Hum = (CheckBox)this.findViewById(R.id.Hum);
		/*Pressure = (CheckBox)this.findViewById(R.id.Pressure);
		Tem = (CheckBox)this.findViewById(R.id.Tem);
		Proximity = (CheckBox)this.findViewById(R.id.Proximity);*/
		login_url = (EditText)this.findViewById(R.id.login_url);
		upload_url = (EditText)this.findViewById(R.id.upload_url);
		OK = (Button)this.findViewById(R.id.OK);
		
	    
		Acc.setChecked(LoginActivity.prefs.getBoolean("Acc_ischecked" ,true));
		if(Acc.isChecked()){
			LoginActivity.prefs.edit().putBoolean("Acc_ischecked" ,true).commit();
		}else{
			LoginActivity.prefs.edit().putBoolean("Acc_ischecked" ,false).commit();
			}
		MAG.setChecked(LoginActivity.prefs.getBoolean("MAG_ischecked" ,false));
		if(MAG.isChecked()){
			LoginActivity.prefs.edit().putBoolean("MAG_ischecked" ,true).commit();
		}else{
			LoginActivity.prefs.edit().putBoolean("MAG_ischecked" ,false).commit();
			}
		Ori.setChecked(LoginActivity.prefs.getBoolean("Ori_ischecked" ,false));
		if(Ori.isChecked()){
			LoginActivity.prefs.edit().putBoolean("Ori_ischecked" ,true).commit();
		}else{
			LoginActivity.prefs.edit().putBoolean("Ori_ischecked" ,false).commit();
			}
		GYro.setChecked(LoginActivity.prefs.getBoolean("GYro_ischecked" ,true));
		if(GYro.isChecked()){
			LoginActivity.prefs.edit().putBoolean("GYro_ischecked" ,true).commit();
		}else{
			LoginActivity.prefs.edit().putBoolean("GYro_ischecked" ,false).commit();
			}
		Light.setChecked(LoginActivity.prefs.getBoolean("Light_ischecked" ,true));
		if(Light.isChecked()){
			LoginActivity.prefs.edit().putBoolean("Light_ischecked" ,true).commit();
		}else{
			LoginActivity.prefs.edit().putBoolean("Light_ischecked" ,false).commit();
			}
		
		Tem.setChecked(LoginActivity.prefs.getBoolean("Tem_ischecked" ,false));
		if(Tem.isChecked()){
			LoginActivity.prefs.edit().putBoolean("Tem_ischecked" ,true).commit();
		}else{
			LoginActivity.prefs.edit().putBoolean("Tem_ischecked" ,false).commit();
			}
		
		Hum.setChecked(LoginActivity.prefs.getBoolean("Hum_ischecked" ,false));
		if(Hum.isChecked()){
			LoginActivity.prefs.edit().putBoolean("Hum_ischecked" ,true).commit();
		}else{
			LoginActivity.prefs.edit().putBoolean("Hum_ischecked" ,false).commit();
			}

		/*Pressure.setChecked(prefs.getBoolean("Pressure_ischecked" ,false));
		if(Pressure.isChecked()){
			checked6 = true;
		}
		Tem.setChecked(prefs.getBoolean("Tem_ischecked" ,false));
		if(Tem.isChecked()){
			checked7 = true;
		}
		Proximity.setChecked(prefs.getBoolean("Proximity_ischecked" ,false));
		if(Proximity.isChecked()){
			checked8 = true;
		}*/
		
		
		Acc.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean checked1) {
				// TODO Auto-generated method stub
				if(Acc.isChecked()){
					checked1 = true;
					
				}else{
					checked1 = false;
				}
				Editor editor = getSharedPreferences("syllabus", 0).edit();
				editor.putBoolean("Acc_ischecked", checked1);
				editor.commit();
			}
		});
		
		MAG.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean checked2) {
				// TODO Auto-generated method stub
				if(MAG.isChecked()){
					checked2 = true;

				}else{
					checked2 = false;
				}
				Editor editor = getSharedPreferences("syllabus", 0).edit();
			 	editor.putBoolean("MAG_ischecked", checked2);
			 	editor.commit();
			}
		});
		
		Ori.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean checked3) {
				// TODO Auto-generated method stub
				if(Ori.isChecked()){
					checked3 = true;
					
				}else{
					checked3 = false;
				}
				Editor editor = getSharedPreferences("syllabus", 0).edit();
			 	editor.putBoolean("Ori_ischecked", checked3);
			 	editor.commit();
			}
		});
		
		GYro.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean checked4) {
				// TODO Auto-generated method stub
				if(GYro.isChecked()){
					checked4 = true;

				}else{
					checked4 = false;
				}
				Editor editor = getSharedPreferences("syllabus", 0).edit();
			 	editor.putBoolean("GYro_ischecked", checked4);
			 	editor.commit();
			}
		});
		
		Light.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean checked5) {
				// TODO Auto-generated method stub
				if(Light.isChecked()){
					checked5 = true;

				}else{
					checked5 = false;
				}
				Editor editor = getSharedPreferences("syllabus", 0).edit();
			 	editor.putBoolean("Light_ischecked", checked5);
			 	editor.commit();
			}
		});
		
		Tem.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean checked6) {
				// TODO Auto-generated method stub
				if(Tem.isChecked()){
					checked6 = true;

				}else{
					checked6 = false;
				}
				Editor editor = getSharedPreferences("syllabus", 0).edit();
			 	editor.putBoolean("Tem_ischecked", checked6);
			 	editor.commit();
			}
		});
		Hum.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean checked7) {
				// TODO Auto-generated method stub
				if(Hum.isChecked()){
					checked7 = true;

				}else{
					checked7 = false;
				}
				Editor editor = getSharedPreferences("syllabus", 0).edit();
			 	editor.putBoolean("Hum_ischecked", checked7);
			 	editor.commit();
			}
		});
		
		login_url.setText(LoginActivity.sp.getString("login_url", MainActivity.def_login_url));
		upload_url.setText(LoginActivity.sp.getString("upload_url", MainActivity.def_upload_url));
		/*Pressure.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(Pressure.isChecked()){
					checked6 = true;

				}else{
					checked6 = false;
				}
				Editor editor = getSharedPreferences("syllabus", 0).edit();
			 	editor.putBoolean("Pressure_ischecked", isChecked);
			 	editor.commit();
			}
		});
		
		Tem.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(Tem.isChecked()){
					checked7 = true;

				}else{
					checked7 = false;
				}
				Editor editor = getSharedPreferences("syllabus", 0).edit();
			 	editor.putBoolean("Tem_ischecked", isChecked);
			 	editor.commit();
			}
		});
		
		
		
		Proximity.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(Proximity.isChecked()){
					checked8 = true;

				}else{
					checked8 = false;
				}
				Editor editor = getSharedPreferences("syllabus", 0).edit();
			 	editor.putBoolean("Proximity_ischecked", isChecked);
			 	editor.commit();
			}
		});
		*/
		
		OK.setOnClickListener(new OnClickListener(){
			public void onClick (View v){
				//login_url 
				try{
				if(("").equals(login_url.getText().toString())){
					System.out.println("!#####!");
				LoginActivity.sp.edit().putString("login_url", MainActivity.def_login_url).commit();
				}else {
					System.out.println("!!!");
					LoginActivity.sp.edit().putString("login_url", login_url.getText().toString()).commit();	
					}
				}catch(Exception e){
				}
				//upload_url
				try{
				if(("").equals(upload_url.getText().toString())){
					System.out.println("!#####!");
				LoginActivity.sp.edit().putString("upload_url", MainActivity.def_upload_url).commit();
				}else {
					System.out.println("!!!");
					LoginActivity.sp.edit().putString("upload_url", upload_url.getText().toString()).commit();	
					}
				}catch(Exception e){
				}
				
				//System.out.println("login_url:"+login_url.getText().toString());
				//System.out.println("upload_url:"+upload_url.getText().toString());
				new AlertDialog.Builder(SettingActivity.this)
				.setTitle("設定")
				.setMessage("設定完了しました！")
				.setPositiveButton("OK",new android.content.DialogInterface.OnClickListener(){
					public void onClick(DialogInterface arg0, int arg1){
						arg0.dismiss();
						Intent startIntent = new Intent(SettingActivity.this,
			                    LoginActivity.class);
						 startActivity(startIntent);
						 SettingActivity.this.finish();
					}
				})
				.show();
			}
		});
	}
}
