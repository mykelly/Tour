package com.tour.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.down.sdk.util.DownCallBack;
import com.tour.R;
import com.tour.adpater.DownVideoAdpater;
import com.tour.util.PublicData;
import com.tour.util.TTLog;

public class TourDownVideoActivity extends Activity implements DownCallBack{
	ListView downVideoList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tourdownvideo);
		downVideoList = (ListView) findViewById(R.id.downvideo);
		if(PublicData.moviephone.size()>0){
		downVideoList.setAdapter(new DownVideoAdpater(this,this));
		}
	}
 
	@Override
	public void getApkDataBack(String apkData, String flag) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getApkProgress(String id, int progress, int maxProgress) {
		// TODO Auto-generated method stub
		TTLog.s(progress+"===id==="+id);
		
		if(progress==100){
			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("id", id);
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			TTLog.s("下载完成。。。。");
			Bundle bundle = msg.getData();
			String id = bundle.getString("id");
			Button btn = (Button) downVideoList.findViewWithTag(id);	
			btn.setText("已下载");
		};
	};
}
