package com.tour.ui;

import java.util.ArrayList;
import java.util.List;
import com.tour.R;
import com.tour.SQLite.DBTour;
import com.tour.adpater.CallNameAdpater;
import com.tour.info.CallNameInfo;
import com.tour.info.DataCustomerInfo;
import com.tour.util.FileUtil;
import com.tour.util.PublicData;
import com.tour.util.TTLog;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 点名
 */
public class CallNameActivity extends NotTitleActivity{
	private ImageButton  ib_back;
	Button bt_finish;
	private GridView gridView;
	private TextView mCount;
	private TextView mAbsentCount;
	private TextView mArriveCount;
	private int count;
	private int absentCount=0;
	private int arriveCount=0;
	static Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_name);
		creatUi();
		mContext=getApplicationContext();
	}
	private void creatUi(){
		
		ib_back=(ImageButton)findViewById(R.id.activity_call_name_back);
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		bt_finish=(Button)findViewById(R.id.activity_call_name_finish);
		bt_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent intent=new Intent(CallNameActivity.this,CallNameActivity.class);
//				startActivity(intent);
				finish();
				
			}
		});
		gridView=(GridView)findViewById(R.id.gv_activity_call_name);
		mCount=(TextView)findViewById(R.id.tv_activity_call_name_conunt);
		mAbsentCount=(TextView)findViewById(R.id.tv_activity_call_name_absent);
		mArriveCount=(TextView)findViewById(R.id.tv_activity_call_name_member_conunt);
//		List<DataCustomerInfo> list=FileUtil.getArrayList();
////		TTLog.s("list!=null==="+(list!=null));
//		if(list!=null){
//			PublicData.dataCustomerInfos=list;
//		}
		 
		count=PublicData.dataCustomerInfos!=null?PublicData.dataCustomerInfos.size():0;
		if(PublicData.dataCustomerInfos!=null){
			for(int i=0;i<PublicData.dataCustomerInfos.size();i++){
			 boolean isAbsent=PublicData.dataCustomerInfos.get(i).isAbsent();
			 if(isAbsent){
				 arriveCount=arriveCount+1;
			 }else{
				 absentCount=absentCount+1; 
			 }
			 
			}
		}
		mCount.setText(count+"人");
		mAbsentCount.setText(absentCount+"人");
		mArriveCount.setText(arriveCount+"人");
		gridView.setAdapter(new CallNameAdpater(this,mHandler));
	
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		FileUtil.saveArrayList(PublicData.dataCustomerInfos);
	}
  public Handler mHandler=new Handler(){
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		//缺席
		if(msg.what==0){
			arriveCount=arriveCount-1;
			absentCount=count-arriveCount;
			mAbsentCount.setText(absentCount+"人");
			mArriveCount.setText(arriveCount+"人");
		}else{
	    //已到
			arriveCount=arriveCount+1;
			absentCount=count-arriveCount;
			mAbsentCount.setText(absentCount+"人");
			mArriveCount.setText(arriveCount+"人");
		}
	}  
  };
  /*
   * 更新点名信息（顾客是否缺席）
   */
  public static void  callname(String id,Boolean isAbsent){
	  DBTour dbTour=new DBTour(mContext);
	  SQLiteDatabase database=dbTour.getWritableDatabase();
	    String ct_absent="" ;
		ContentValues values = new ContentValues();		
		if(isAbsent){
			ct_absent="1";
		}else{
			ct_absent="0";
		}	
	  values.put("ct_absent", ct_absent);		 			
	  database.update("data_customer", values, "CT_id="+id, null);
	  database.close();
  }
}
