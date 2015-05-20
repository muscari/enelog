package com.sozolab.enelog;


import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;

public class MeasureLabelTime extends Activity {
	private boolean buttonFlag;
	private String startTime=null;
	private String finishTime=null;
	private String labelName=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure_label_time);
		buttonFlag = false;
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.measure_label_time, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void setStartTime(){
		Time time = new Time("Asia/Tokyo");
		time.setToNow();
		startTime = time.year + "/" + (time.month+1) + "/" + time.monthDay + " " + time.hour + ":" + time.minute + ":" + time.second;
	}
	public String getStartTime(){
		return startTime;
	}
	
	public void setFinishTime(){
		Time time = new Time("Asia/Tokyo");
		time.setToNow();
		finishTime = time.year + "/" + (time.month+1) + "/" + time.monthDay + " " + time.hour + ":" + time.minute + ":" + time.second;
	}
	public String getFinishTime(){
		return finishTime;
	}
	public void setButtonFlag(){
		buttonFlag = !buttonFlag;
	}
	public boolean getButtonFlag(){
		return buttonFlag;
	}
	public void setLabel(String labelname){
		labelName = labelname;
	}
	public String getLabel(){
		return labelName;
	}
}
