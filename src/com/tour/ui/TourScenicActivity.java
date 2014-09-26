package com.tour.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.tour.R;
import com.tour.SQLite.DBTour;
import com.tour.SQLite.TourData;
import com.tour.adpater.ScenicGidAdapter;
import com.tour.info.DataPlaceInfo;
import com.tour.util.PublicData;
/**
 * 
 * @author wl "景点页面"
 *
 */
public class TourScenicActivity extends NotTitleActivity {
	GridView gridView;
	public static Handler mHandler;
	public static List<DataPlaceInfo> tourScenic = new ArrayList<DataPlaceInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tourscenictab);
		gridView = (GridView) findViewById(R.id.gridView);	
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				int id=PublicData.tourScenic.get(position).getPlaceId();
				Intent intent = new Intent(TourScenicActivity.this,
						TourDetailsActivity.class);
				intent.putExtra("type", "scenic");
				intent.putExtra("id", id);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
   /*   Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 System.out.println("取出景点");
//				if (tourScenic.size() != 0)
				tourScenic = TourData.queryPlacebyId(TourScenicActivity.this,PublicData.tour_id);
				System.out.println("tourScenic.size()="+tourScenic.size());
				if(tourScenic.size()>0){
				 mHandler.sendEmptyMessage(1);
				}
			}
		}) ;
		thread.start();*/
		mHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					
					break;

				case 1:
					gridView.setAdapter(new ScenicGidAdapter(TourScenicActivity.this,PublicData.tourScenic));
					break;
				}
			}
		};
//		System.out.println("设置景点数据PublicData.tourScenic.size()="+PublicData.tourScenic.size());
		if(PublicData.tourScenic.size()>0){
			 mHandler.sendEmptyMessage(1);
			}
	}

}
