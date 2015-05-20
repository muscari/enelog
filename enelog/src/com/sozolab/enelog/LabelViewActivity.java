package com.sozolab.enelog;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;



public class LabelViewActivity extends Activity{
private static String[] category = {"未分類","身体の清潔","与薬","食事の世話","観察","患者の輸送","排泄の世話","安全の確保","安楽","自立の援助","呼吸循環管理","測定","検査","未分類","未分類","医師への報告","Ns間の申し継ぎ","家族との連絡","勤務管理","病床管理","環境管理","事務管理","管理業務","休憩の過ごし方"};
private static String[][] act ={{"アナムネ、退院指導"},{"清拭","かみ・ひげ・爪","口腔清潔","入浴・シャワー","寝具・寝衣交換","褥瘡予防"},{"配薬準備","薬の内服","点眼介助","点耳・点鼻介助","注腸・外用・経管","座薬の挿入","インスリン注射"},{"食事介助","配膳・下膳","飲水","経管栄養","哺乳","ミルク"},{"浮腫診察","症状観察・問診","聴診","打診","触診","生活反応","病室の巡視"},{"車いす介助","歩行介助","ベッド運搬"},{"尿の排泄介助","便の排泄介助","人工肛門ケア","オムツ交換","留置カーテルの管理","導尿","浣腸"},{"転倒・転落防止","抑制"},{"ギャジアップ","体位交換","氷枕・湯たんぽ","マッサージ","声かけ・傾聴"},{"リハビリ","カウンセリング","レクリエーション"},{"回診","輸液・シリンジポンプ","点滴","創部付け替え","皮筋注","輸血・静脈注射","IVHの管理","術前剃毛","洗浄・薬浴","穿刺の介助"},{"人工呼吸器","酸素吸入・テント","排痰促進・吸引","モニター観察","Aライン管理","保育器の調整"},{"呼吸","脈拍","体温","血圧","体重","胸囲","尿量"},{"検査前説明","採血","血糖測定","心電図","その他"},{"終末看護処理"},{"薬剤業務"},{"報告・連絡","指示受付"},{"申し送り・受け","カンファレンス","看護計画","看護師間の連絡","部長・師長連絡"},{"家族との連絡","訪問看護","電話訪問"},{"勤務表の作成","ワークシートの印刷","勤務実態の確認"},{"入院配置計画","転ベッド指示"},{"病室の環境整備","ME機器管理","棚卸","伝票記載提出","SSの環境整備"},{"電子カルテ記録","電子カルテ閲覧","看護必要度確認","入退院カルテ整理","健康診断"},{"院内会議","委員会","超勤等手当報告","日報作成","病棟会議","職員面接","看護学生の指導","インシデント・クレーム"},{"食事","会話","読書","携帯・スマホ","その他"}};
private String labelName;
private int nowButtonSizeWidth;
private ArrayList<MeasureLabelTime> mltList = new ArrayList<MeasureLabelTime>();
private ArrayList<Button> buttonList = new ArrayList<Button>();
private String labelFileName;
private WriteFile wf = new WriteFile();


@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_view);
		labelFileName = "testLabelFile";
	}
@Override
	public void onWindowFocusChanged(boolean hasFocus){
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
	
		//ディスプレイサイズの取得
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		int displaySizeWidth = disp.getWidth();
		int displaySizeHeight = disp.getHeight();
		
		//ScrollView scrollView = new ScrollView(this);
		ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView1);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		//ScrollViewにLinearLayoutを追加
		scrollView.addView(linearLayout);
		int buttonIdCounter = 0;
		//追加したいView
		//LinearLayoutにViewを追加
		final ArrayList<Integer> buttonIdList = new ArrayList<Integer>();
		for(int i = 0; i <category.length; i++){
			TextView tw = new TextView(this);
			tw.setText(category[i]);
			tw.setTextSize(20);
			tw.setBackgroundColor(Color.GREEN);
			//l1はカテゴリを表示するレイアウト
			LinearLayout l1 = new LinearLayout(this);
			l1.setOrientation(LinearLayout.VERTICAL);
			LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			linearLayout.addView(l1);
			l1.addView(tw,lp1);
			int totalButtonSizeWidth = 0;
			for(int j = 0; j < act[i].length; j++){
				RelativeLayout r1 = new RelativeLayout(this);
				RelativeLayout.LayoutParams rp =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				l1.addView(r1);
				Button btn = new Button(this);
				btn.setText(act[i][j]);
				btn.setId(buttonIdCounter);
				buttonIdCounter = buttonIdCounter + 1;
				rp.setMargins(10,10,10,10);
				btn.setOnClickListener(new OnClickListener(){
					MeasureLabelTime mlt = new MeasureLabelTime();
					public void onClick(View v){
						if(mlt.getButtonFlag()==false){
							//クリックされたViewをキャスト
							Button selectButton = (Button)v;
							labelName=(String) selectButton.getText();
							selectButton.setBackgroundColor(Color.RED);
							mlt.setLabel(labelName);
							mlt.setStartTime();
							mlt.setButtonFlag();
							mltList.add(mlt);
							buttonList.add(selectButton);
						}else{
							mlt.setFinishTime();
							mlt.setButtonFlag();
							Button selectButton = (Button)v;
							selectButton.setBackgroundColor(Color.LTGRAY);
							System.out.println(mlt.getLabel());
							System.out.println(mlt.getStartTime());
							System.out.println(mlt.getFinishTime());
							String message = mlt.getLabel() + "," + mlt.getStartTime() + "," + mlt.getFinishTime() + "\n";
							wf.writeFileInDevice(labelFileName,message);
							mltList.remove(mltList.indexOf(mlt));
							buttonList.remove(buttonList.indexOf(selectButton));
						}
						}
					});
				Paint p = new Paint();
				if(totalButtonSizeWidth <= (displaySizeWidth - totalButtonSizeWidth)){
					if(j == 0){
						System.out.println("first");
						r1.addView(btn,rp);
						p.setTextSize(btn.getTextSize());
						nowButtonSizeWidth = (int)p.measureText((String)btn.getText());
						totalButtonSizeWidth = totalButtonSizeWidth + nowButtonSizeWidth;
					}else{
						System.out.println("LEFT");
						rp.addRule(RelativeLayout.ALIGN_LEFT,j-1);
						r1.addView(btn,rp);
						p.setTextSize(btn.getTextSize());
						nowButtonSizeWidth = (int)p.measureText((String)btn.getText());
						totalButtonSizeWidth = totalButtonSizeWidth + nowButtonSizeWidth;
					}
				}else{
					System.out.println("top");
					rp.addRule(RelativeLayout.ALIGN_TOP,j-1);
					r1.addView(btn,rp);
					p.setTextSize(btn.getTextSize());
					nowButtonSizeWidth = (int)p.measureText((String)btn.getText());
					totalButtonSizeWidth = 0;
				}
			}
		}
		
		Button allStopButton = (Button)findViewById(R.id.allStopButton);
		allStopButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(buttonList.size()==0){
				}else{
				for(int k=0; k < buttonList.size(); k++){
					Button btn = buttonList.get(k);
					MeasureLabelTime mlt = mltList.get(k);
					mlt.setFinishTime();
					mlt.setButtonFlag();
					btn.setBackgroundColor(Color.LTGRAY);
					System.out.println(mlt.getLabel());
					System.out.println(mlt.getStartTime());
					System.out.println(mlt.getFinishTime());
					String message = mlt.getLabel() + "," + mlt.getStartTime() + "," + mlt.getFinishTime() + "\n";
					wf.writeFileInDevice(labelFileName,message);
				}

				buttonList.clear();
				mltList.clear();
			}
			}
		});	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.label_view, menu);
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
}
