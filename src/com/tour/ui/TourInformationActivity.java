package com.tour.ui;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import com.tour.R;
import com.tour.SQLite.DBTour;
import com.tour.SQLite.TourData;
import com.tour.adpater.TourMemberAdapter;
import com.tour.info.DataCustomerInfo;
import com.tour.info.TourDataInfo;
import com.tour.util.NetWorkStatus;
import com.tour.util.PublicData;
import com.tour.util.SaveDataClass;
import com.tour.util.TTLog;
import com.tour.util.TimeUtil;
import com.tour.view.WaitDialog;
/**
 * 
 * @author wl "团信息页面"
 *
 */
public class TourInformationActivity extends NotTitleActivity {
	TextView mTitle,mName,mNumber,mFlag,mGuide,mDriver,mDate,mType,mComment,tv_customer_total;
	Button bt_callname,bt_detail_tour,bt_detail_customer;
	GridView gridview;
	private NetworkConnectionReceiver networkReceiver = null;
	private WaitDialog dialog;// “等待”对话框
	public static Handler mHandler;
	TourDataInfo tourDataInfo;
	List<DataCustomerInfo> dataCustomerInfos;
//	public static List<String> clocklist=new ArrayList<String>();
	public static List<String> clocktaglist=new ArrayList<String>();
	public static List<String> clockUnixlist=new ArrayList<String>();
	public static List<String> clockhourlist=new ArrayList<String>();
	public static List<String> clockminlist=new ArrayList<String>();
	int tourId;
	private	String title="" ,number="",flag="",guide="",driver="",data="",type="",comment="",clock="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_tour_info);
		isHasNetWork();//检查联网情况
		init();
		dialog=new WaitDialog(TourInformationActivity.this, android.R.style.Theme_Translucent);
		dialog.show();
		Thread thread=new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//				 NetWorkStatus networkstatus=new NetWorkStatus();
				//				 PublicData.isNetWork=networkstatus.isNetWork(getApplicationContext());//检查联网情况
//				PublicData.isUpgrade=true;
				if(PublicData.isNetWork){
					if(PublicData.isUpgrade){
						PublicData.isUpgrade=false;					 
						DBTour dbTour=new DBTour(getApplicationContext());
						dbTour.onUpgrade(null, 2, 3);//导入数据
					}
				}else{
					SaveDataClass saveDataClass=new SaveDataClass();
					PublicData.tour_id=saveDataClass.getTourId(getApplicationContext());//获取团id
				}

				PublicData.tourDataInfo=TourData.queryTourInfobyId(TourInformationActivity.this,PublicData.tour_id);
				PublicData.dataTourDateInfos=TourData.getdataTourDate(TourInformationActivity.this);
				PublicData.dataCustomerInfos=TourData.queryCustomerbyId(TourInformationActivity.this,PublicData.tour_id);
				PublicData.tourScenic = TourData.queryPlacebyId(TourInformationActivity.this);
				PublicData.tourFood = TourData.getFoodData(TourInformationActivity.this);
				PublicData.moviephone=TourData.getMoveData(TourInformationActivity.this);

				//				 System.out.println("团信息PublicData.tourDataInfo="+PublicData.tourDataInfo);
				if(PublicData.tourDataInfo!=null&&!"".equals(PublicData.tourDataInfo)){
					mHandler.sendEmptyMessage(1);
				}else{
					if (dialog != null && dialog.isShowing())
						dialog.dismiss();
				}
			}
		}) ;
		thread.start();
//		clock="早上起床,1405686502|中午吃饭,1405626502|晚餐,1405679502";
//		if(clock!=null){
//			clock =clock + "|";
//			initDate();
//			TourData.insterAlarm(TourInformationActivity.this);
//		}
		mHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:

					break;

				case 1:
					if (dialog != null && dialog.isShowing())
						dialog.dismiss();
					if(clocktaglist!=null){
						clocktaglist.clear();
					}
					if(clockhourlist!=null){
						clockhourlist.clear();
					}
					if(clockminlist!=null){
						clockminlist.clear();
					}
					if(clockUnixlist!=null){
						clockUnixlist.clear();
					}
				    clockUnixlist=new ArrayList<String>();
					clocktaglist=new ArrayList<String>();
					clockhourlist=new ArrayList<String>();
					clockminlist=new ArrayList<String>();
					try {
						PublicData.tour_zip=PublicData.tourDataInfo.getTourZip();
						title=PublicData.tourDataInfo.getTourTitle();
						number=PublicData.tourDataInfo.getTourNo();
						flag=PublicData.tourDataInfo.getTourFlat();
						guide=PublicData.tourDataInfo.getTourGuide();
						driver=PublicData.tourDataInfo.getTourDriver();
						data=PublicData.tourDataInfo.getTourDate();
						type=PublicData.tourDataInfo.getTourType();
						comment=PublicData.tourDataInfo.getRemark(); 	
						clock=PublicData.tourDataInfo.getClock();//数据格式如：早上起床,6:30|中午吃饭,12:30|晚餐,18:00
						//						System.out.println("闹钟clock="+clock);
						/*if (clock != null) {
							String clocks =clock + "|";
							if (clocks != null&& clocks.indexOf("|") != -1) {
								String newclock = clocks.substring(0,clocks.indexOf("|"));
								clocklist.add(newclock);
								String clocktag = newclock.substring(0,newclock.indexOf(","));
								clocktaglist.add(clocktag);	

								String clockhour = newclock.substring(newclock.indexOf(",")+1,newclock.indexOf(":"));
								clockhourlist.add(clockhour);	
								String clockmin = newclock.substring(newclock.indexOf(":")+1,newclock.length());
								clockminlist.add(clockmin);	
								// 剩余URL
								String lastclock = clocks.substring(clocks.indexOf("|") + 1,clocks.length());
								// 直到不存在“|”
								while (lastclock.contains("|")) {
									if (lastclock.length() > 0) {
										newclock = lastclock.substring(0,lastclock.indexOf("|"));
										clocklist.add(newclock);
										clocktag = newclock.substring(0,newclock.indexOf(","));
										clocktaglist.add(clocktag);
										clockhour = newclock.substring(newclock.indexOf(",")+1,newclock.indexOf(":"));
										clockhourlist.add(clockhour);	
										clockmin = newclock.substring(newclock.indexOf(":")+1,newclock.length());
										clockminlist.add(clockmin);	
									}
									if (lastclock.length() > 0)
										lastclock = lastclock.substring(lastclock.indexOf("|") + 1,lastclock.length());
								}
							}
							//						System.out.println("闹钟clocklist="+clocklist);
													
						}*/
//						TTLog.s("clock========="+clock);
						if(clock!=null){
							clock =clock + "|";
							initDate();
						}
//						System.out.println("闹钟标签clocktaglist="+clocktaglist);
//						System.out.println("闹钟小时clockhourlist="+clockhourlist);
//						System.out.println("闹钟分钟clockminlist="+clockminlist);
						TourData.insterAlarm(TourInformationActivity.this);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					setdata();
					break;
				}
			}
		};
	}

	public void initDate(){

		if(clock!=null&&clock.length()>2){
			if(clock.indexOf("|")!=-1){
				String text=clock.substring(0,clock.indexOf("|"));
				if(text.indexOf(",")!=-1){
					String tag=text.substring(0,text.indexOf(","));
					clocktaglist.add(tag);
					String time=text.substring(text.indexOf(",")+1,text.length());
					clockUnixlist.add(time);
					time=TimeUtil.IntChangeDateSecond(time);
					String clockhour=time.substring(0,time.indexOf(":"));
					String	clockmin=time.substring(time.indexOf(":")+1,time.length());
					clockhourlist.add(clockhour);
					clockminlist.add(clockmin);
				}
//				System.out.println("text==="+text);
				if(clock.length()>clock.indexOf("|")+1){
					clock=clock.substring(clock.indexOf("|")+1,clock.length());
//					System.out.println("test==="+clock);
					initDate();	
				}

			}
		}
//		System.out.println("闹钟标签clocktaglist="+clocktaglist);
//		System.out.println("闹钟小时clockhourlist="+clockhourlist);
//		System.out.println("闹钟分钟clockminlist="+clockminlist);
	}

	private void init() {
		//		mTitle=(TextView)findViewById(R.id.tv_main_tab_top_title);
		//		mTitle.setText(getResources().getString(R.string.tour_info));
		mName = (TextView) findViewById(R.id.tv_tour_info_name);
		mNumber = (TextView) findViewById(R.id.tv_tour_info_number);
		mFlag = (TextView) findViewById(R.id.tv_tour_info_flag);
		mGuide= (TextView) findViewById(R.id.tv_tour_info_guide);
		mDriver = (TextView) findViewById(R.id.tv_tour_info_drive);
		mDate = (TextView) findViewById(R.id.tv_tour_info_fromdate);
		mType = (TextView) findViewById(R.id.tv_tour_info_type);
		mComment = (TextView) findViewById(R.id.tv_tour_info_comment);
		tv_customer_total= (TextView) findViewById(R.id.tv_tob_tour_info_number);
		bt_detail_tour= (Button) findViewById(R.id.tv_tob_tour_info_tourdetalil);
		bt_detail_tour.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(TourInformationActivity.this,DataDetailActivity.class);
				startActivity(intent);

			}
		});
		bt_detail_customer= (Button) findViewById(R.id.tv_tob_tour_info_customerdetalil);
		bt_detail_customer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(TourInformationActivity.this,CustomerListActivity.class);
				startActivity(intent);

			}
		});
		gridview=(GridView)findViewById(R.id.gv_tob_tour_info);
		bt_callname=(Button)findViewById(R.id.btn_tob_tour_info_callname);
		bt_callname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(TourInformationActivity.this,CallNameActivity.class);
				startActivity(intent);

			}
		});

	}

	private void setdata() {
		// TODO Auto-generated method stub
		mName.setText(title);
		mNumber.setText(number);
		mFlag.setText(flag);
		mGuide.setText(guide);
		mDriver.setText(driver);
		mDate.setText(data);
		mType.setText(type);
		mComment.setText(comment);
		tv_customer_total.setText(PublicData.dataCustomerInfos.size()+"人");
		gridview.setAdapter(new TourMemberAdapter(this,PublicData.dataCustomerInfos));
	}
	private void isHasNetWork() {
		// TODO Auto-generated method stub
		networkReceiver = new NetworkConnectionReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkReceiver, filter);
	}
	/**
	 * 实时判断网络状态
	 * 
	 * @author CWD
	 * 
	 */
	public class NetworkConnectionReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			// 获得网络连接服务
			ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			if (info != null && info.isAvailable() && info.isConnected()) {
				PublicData.isNetWork = true;
				//				System.out.println("网络已链接PublicData.isNetWork ="+PublicData.isNetWork);
			} else {
				PublicData.isNetWork = false;
				//                System.out.println("网络断开链接PublicData.isNetWork ="+PublicData.isNetWork);
			}

		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (null != networkReceiver) {
			unregisterReceiver(networkReceiver);// 取消网络监听
		}
	}
}
