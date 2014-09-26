package com.tour.ui;

import com.tour.R;
import com.tour.SQLite.DBTour;
import com.tour.deskclock.DeskClockMainActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class TourTabActivity extends TabActivity {
	 long exitTime = 0;
	 ImageButton ib_exit;
	 TextView mTitle;
	TabHost tabHost;
	TabWidget tabWidget;
	private String name[] = { "团信息", "行程安排", "景点", "餐厅", "视频","闹钟" };
	// default
	private int myMenuRes[] = { R.drawable.tab_tour_default, R.drawable.tab_journey_default,
			R.drawable.tab_scenic_default, R.drawable.tab_cate_default, R.drawable.tab_vedio_default ,R.drawable.tab_clock_default};

	// on hover
	private int myMenuResHover[] = { R.drawable.tab_tour_focus, R.drawable.tab_journey_focus,
			R.drawable.tab_scenic_focus, R.drawable.tab_cate_focus, R.drawable.tab_vedio_focus,R.drawable.tab_clock_focus };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub、
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tourtab);
		super.onCreate(savedInstanceState);
		ib_exit=(ImageButton)findViewById(R.id.tourtab_top_exit);
		ib_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mTitle=(TextView)findViewById(R.id.tourtab_top_title);

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if("0".equals(tabId)){
					mTitle.setText(name[0]);
				}else if("1".equals(tabId)){
					mTitle.setText(name[1]);
				}else if("2".equals(tabId)){
					mTitle.setText(name[2]);
				}else if("3".equals(tabId)){
					mTitle.setText(name[3]);
				}else if("4".equals(tabId)){
					mTitle.setText(name[4]);
				}else if("5".equals(tabId)){
					mTitle.setText(name[5]);
				}
				// tabId值为要切换到的tab页的索引位置
				for (int i = 0; i < tabWidget.getChildCount(); i++) {
					if (tabId.equals(i + "")) {
						ImageView iv = (ImageView) tabWidget.getChildAt(i)
								.findViewById(R.id.tab_image);
						iv.setBackgroundResource(myMenuResHover[i]);
						TextView tv = (TextView) tabWidget.getChildAt(i)
								.findViewById(R.id.tab_menu_name);
						tv.setTextColor(Color.WHITE);
//						tabWidget.getChildAt(i).setBackgroundResource(
//								R.drawable.bg_toolbar_sel);
					} else {
						ImageView iv = (ImageView) tabWidget.getChildAt(i)
								.findViewById(R.id.tab_image);
						iv.setBackgroundResource(myMenuRes[i]);
						TextView tv = (TextView) tabWidget.getChildAt(i)
								.findViewById(R.id.tab_menu_name);
						tv.setTextColor(Color.parseColor("#b0aba9"));
//						tabWidget.getChildAt(i).setBackgroundColor(
//								Color.argb(0, 255, 255, 255));
					}
				}

			}
		});
		init();
//		Thread thread=new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					DBTour dbTour=new DBTour(getApplicationContext());
//					dbTour.onUpgrade(null, 2, 3);//导入数据
//				}
//			}) ;
//			thread.start();
	}

	private void init() {
		setIndicator(myMenuRes[0], name[0], 0, new Intent(this,
				TourInformationActivity.class));
		setIndicator(myMenuRes[1], name[1], 1, new Intent(this,
				TourJourneyActivity.class));
		setIndicator(myMenuRes[2], name[2], 2, new Intent(this,
				TourScenicActivity.class));
		setIndicator(myMenuRes[3], name[3], 3, new Intent(this,
				TourFoodActivity.class));
		setIndicator(myMenuRes[4], name[4], 4, new Intent(this,
				TourDownVideoActivity.class));
		setIndicator(myMenuRes[5], name[5], 5, new Intent(this,
				DeskClockMainActivity.class));
	}

	private void setIndicator(int icon, String name, int tabId, Intent intent) {
		View localView = LayoutInflater.from(this.tabHost.getContext())
				.inflate(R.layout.tour_space, null);
		((ImageView) localView.findViewById(R.id.tab_image))
				.setBackgroundResource(icon);

		TextView tv = ((TextView) localView.findViewById(R.id.tab_menu_name));
		tv.setText(name);

		String str = String.valueOf(tabId);
		// System.out.println(str);
		TabHost.TabSpec localTabSpec = tabHost.newTabSpec(str)
				.setIndicator(localView).setContent(intent);
		tabHost.addTab(localTabSpec);
	}
	// 返回键监听退出
		@Override
	    public boolean dispatchKeyEvent(KeyEvent event) {
	       
	        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
	        		&& event.getAction() == KeyEvent.ACTION_DOWN ) {
	        	if((System.currentTimeMillis()-exitTime) > 1000){
					Toast.makeText(getApplicationContext(), "再按一次退出软件",500).show();
					exitTime = System.currentTimeMillis();
					 return true;
					} else {
						finish();
					 return true;
					} 	
	        }
	        return super.dispatchKeyEvent(event);   /** 按下其它键，调用父类方法，进行默认操作 */  
	        }
}