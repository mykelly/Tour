package com.tour.ui;

import java.util.ArrayList;
import java.util.List;

import com.tour.R;
import com.tour.adpater.CustomerAdapter;
import com.tour.adpater.TourMemberAdapter;
import com.tour.util.PublicData;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class DataDetailActivity extends Activity {
	private ImageButton  ib_back;
	private TextView mTitle,mName,mNumber,mFlag,mDriver,mDate,mType,mComment,
	tv_content,tv_ptype,tv_place,tv_driver_phone,tv_leader,tv_leader_phone,tv_guide,tv_guide_phone,tv_count,tv_count_list,tv_day;
	private	String title="" ,number="",flag="",leader="",leader_phone="",driver="",driver_phone="",data="",day="",
			type="",ptype="",comment="",content="",place="",guide="",guide_phone="",count_list="";
	int count=0;
	private	List<String> countlist = new ArrayList<String>();
	/**
	 * @param wl “团详细信息”
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tourdata_detail);
         init();
         setdata();
 	}
	private void init() {
		// TODO Auto-generated method stub
		ib_back=(ImageButton)findViewById(R.id.customer_detail_back);
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mName = (TextView) findViewById(R.id.tourdata_detail_name);
		mNumber = (TextView) findViewById(R.id.tourdata_detail_number);
		mFlag = (TextView) findViewById(R.id.tourdata_detail_flag);
		mDriver = (TextView) findViewById(R.id.tourdata_detail_drive);
		mDate = (TextView) findViewById(R.id.tourdata_detail_fromdate);
		mType = (TextView) findViewById(R.id.tourdata_detail_type);
		mComment = (TextView) findViewById(R.id.tourdata_detail_comment);
		tv_content= (TextView) findViewById(R.id.tourdata_detail_content);
		tv_ptype= (TextView) findViewById(R.id.tourdata_detail_ptype);
		tv_place= (TextView) findViewById(R.id.tourdata_detail_place);
		tv_driver_phone= (TextView) findViewById(R.id.tourdata_detail_drivephone);
		tv_leader= (TextView) findViewById(R.id.tourdata_detail_leader);
		tv_leader_phone= (TextView) findViewById(R.id.tourdata_detail_leaderphone);
		tv_guide= (TextView) findViewById(R.id.tourdata_detail_guide);
		tv_guide_phone= (TextView) findViewById(R.id.tourdata_detail_guidephone);
		tv_count= (TextView) findViewById(R.id.tourdata_detail_count);
		tv_count_list= (TextView) findViewById(R.id.tourdata_detail_countlist);
		tv_day= (TextView) findViewById(R.id.tourdata_detail_day);
	}
	private void setdata() {
		// TODO Auto-generated method stub
		try {
			title=PublicData.tourDataInfo.getTourTitle();
			number=PublicData.tourDataInfo.getTourNo();
			content=PublicData.tourDataInfo.getTourLineContent();
			flag=PublicData.tourDataInfo.getTourFlat();
			leader=PublicData.tourDataInfo.getTourLeader();
			leader_phone=PublicData.tourDataInfo.getTourLeaderPhone();
			driver=PublicData.tourDataInfo.getTourDriver();
			driver_phone=PublicData.tourDataInfo.getTourDriverPhone();
			guide=PublicData.tourDataInfo.getTourGuide();
			guide_phone=PublicData.tourDataInfo.getTourGuidePhone();
			data=PublicData.tourDataInfo.getTourDate();
			day=PublicData.tourDataInfo.getTourDay();
			type=PublicData.tourDataInfo.getTourType();
			ptype=PublicData.tourDataInfo.getTourPtype();
			place=PublicData.tourDataInfo.getTourPlace();
			count=PublicData.tourDataInfo.getTourCtCount();
			count_list=PublicData.tourDataInfo.getTourCtCountList();
			comment=PublicData.tourDataInfo.getRemark();	
			//count_list数据格式:0,0,0,0,0   对应    小童,成人,長者,會員,免費　 
			if (!"".equals(count_list)) {
				String counts =count_list + ",";
				if (counts != null&& counts.indexOf(",") != -1) {
					String newUrl = counts.substring(0,
							counts.indexOf(","));
					countlist.add(newUrl);
					// 剩余URL
					String remainUrl = counts.substring(
							counts.indexOf(",") + 1,
							counts.length());
					// 直到不存在“|”
					while (remainUrl.contains(",")) {
						if (remainUrl.length() > 0) {
							newUrl = remainUrl.substring(0,
									remainUrl.indexOf(","));
							countlist.add(newUrl);
						}
						if (remainUrl.length() > 0)
							remainUrl = remainUrl.substring(
									remainUrl.indexOf(",") + 1,
									remainUrl.length());
					}
				}

			}
			if( countlist.size()>=5){
				count_list="儿童"+countlist.get(0)+",成人"+countlist.get(1)+",長者"+countlist.get(2)+",會員"+countlist.get(3)+",免費"+countlist.get(4);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		mName.setText(title);
		mNumber.setText(number);
		mFlag.setText(flag);
		tv_leader.setText(leader);
		mDriver.setText(driver);
		mDate.setText(data);
		mType.setText(type);
		mComment.setText(comment);
		tv_content.setText(content);
		tv_ptype.setText(ptype);
		tv_place.setText(place);
		tv_driver_phone.setText(driver_phone);
		tv_leader_phone.setText(leader_phone);
		tv_guide.setText(guide);
		tv_guide_phone.setText(guide_phone);
		tv_count.setText(count+"");
		tv_count_list.setText(count_list);
		tv_day.setText(day);
	}
}
