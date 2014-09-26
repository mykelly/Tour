package com.tour.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tour.R;
import com.tour.info.DataPlaceInfo;
import com.tour.util.PublicData;
import com.tour.view.GalleryimageFlow;
/**
 *
 * @author wl  “行程页面”
 *
 */
public class TourJourneyActivity extends NotTitleActivity {
	private Gallery gallery;
	GridView gridview;
	ListView journeyList;
	Button check_hotel;
	List<DataPlaceInfo> journeyItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tourjourneytab);
		journeyList = (ListView) findViewById(R.id.journeyListVeiw);
		journeyItem = new ArrayList<DataPlaceInfo>();
//		journeyItem = TourData.getJourneyData(RollListActivity.tour_Id);
//		journeyList.setAdapter(new journeyListAdtapter());
		gridview=(GridView)findViewById(R.id.journey_gv);
		gallery = (Gallery) findViewById(R.id.journey_gy); 
//		gallery.setLayoutParams(new LayoutParams(400, 300));
		gallery.setSpacing(5);// 间距
		if(PublicData.dataTourDateInfos.size()>0){
//			gridview.setAdapter(new journeyListAdtapter());
			gallery.setAdapter(new journeyListAdtapter());
		}
	}

	class journeyListAdtapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return PublicData.dataTourDateInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
//			LayoutInflater mInflater = LayoutInflater
//					.from(TourJourneyActivity.this);
//			convertView = mInflater.inflate(R.layout.tourjourneyelect, null);
//			TextView journeyTime = (TextView) convertView
//					.findViewById(R.id.journeytime);
//			TextView journeyMorning = (TextView) convertView
//					.findViewById(R.id.journeymorning);
//			TextView journeyNoon = (TextView) convertView
//					.findViewById(R.id.journeynoon);
//			TextView journeyNight = (TextView) convertView
//					.findViewById(R.id.journeynight);
//			TextView journeyHotel = (TextView) convertView
//					.findViewById(R.id.journeyhotel);
//			journeyTime.setText(" 2012-8-19");
//			journeyMorning.setText("早上:" + "到房间里进房间浪费");
//			journeyNoon.setText("中午:" + "惊动了房间垃圾粉");
//			journeyNight.setText("晚上:" + "惊动了封建时代拉克房间里");
//			journeyHotel.setText("交界附近时丁莱夫");
//			journeyHotel.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent(TourJourneyActivity.this,
//							TourHotelActivity.class);
//					startActivity(intent);
//				}
//			});		 
//			return convertView;
			
			ViewHold vh = new ViewHold();
			if (convertView == null) {
				convertView = LayoutInflater.from(TourJourneyActivity.this).inflate(
						R.layout.journeyitem, null);
				vh.tv_date = (TextView) convertView.findViewById(R.id.journeyitem_date);
				vh.tv_week = (TextView) convertView.findViewById(R.id.journeyitem_week);
				vh.tv_destination = (TextView) convertView.findViewById(R.id.journeyitem_destination);
				vh.tv_content = (TextView) convertView.findViewById(R.id.journeyitem_content);
				vh.tv_breakfast = (TextView) convertView.findViewById(R.id.journeyitem_breakfast);
				vh.tv_lunch = (TextView) convertView.findViewById(R.id.journeyitem_lunch);
				vh.tv_dinner = (TextView) convertView.findViewById(R.id.journeyitem_dinner);
				vh.tv_supper = (TextView) convertView.findViewById(R.id.journeyitem_supper);
				vh.tv_transport = (TextView) convertView.findViewById(R.id.journeyitem_transport);
				vh.tv_travel_agency = (TextView) convertView.findViewById(R.id.journeyitem_travel_agency);//地接社
				vh.tv_hotel = (TextView) convertView.findViewById(R.id.journeyitem_hotel);
				vh.tv_remark = (TextView) convertView.findViewById(R.id.journeyitem_remark);
				vh.relyt_hotel = (RelativeLayout) convertView.findViewById(R.id.journeyitem_hotel_relyt); 
//				convertView.setOnClickListener(new OnClickListener() {
					vh.relyt_hotel.setOnClickListener(new OnClickListener() {
				
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									int hotel_id=PublicData.dataTourDateInfos.get(position).getDateHotelId();
									Intent intent = new Intent(TourJourneyActivity.this,
//											TourHotelActivity.class);
											TourDetailsActivity.class);
									intent.putExtra("type", "hotel");
									intent.putExtra("id", hotel_id);
									intent.putExtra("position", position);
									startActivity(intent);
								}
							});		 
				convertView.setTag(vh);
			} else {
				vh = (ViewHold) convertView.getTag();
			}
			try {

				vh.tv_date.setText(PublicData.dataTourDateInfos.get(position).getTourDate());//日期
				vh.tv_week.setText(PublicData.dataTourDateInfos.get(position).getTourWeek());//星期
				vh.tv_destination.setText(PublicData.dataTourDateInfos.get(position).getDateDestination());
				vh.tv_content.setText(PublicData.dataTourDateInfos.get(position).getDateContent());
				vh.tv_breakfast.setText(PublicData.dataTourDateInfos.get(position).getDateBreakfast());
				vh.tv_lunch.setText(PublicData.dataTourDateInfos.get(position).getDateLunch());
				vh.tv_dinner.setText(PublicData.dataTourDateInfos.get(position).getDateDinner());
				vh.tv_supper.setText(PublicData.dataTourDateInfos.get(position).getDateSupper());
				vh.tv_transport.setText(PublicData.dataTourDateInfos.get(position).getDateTransport());
//				vh.tv_travel_agency.setText(PublicData.dataTourDateInfos.get(position).getDatePlace());//地接社
				vh.tv_hotel.setText(PublicData.dataTourDateInfos.get(position).getDateHotel());
				vh.tv_remark.setText(PublicData.dataTourDateInfos.get(position).getDateRemark());
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return convertView;
			
		}
		public class ViewHold {
			public	RelativeLayout relyt_hotel;
			public TextView tv_date,tv_week,tv_destination,tv_content,tv_breakfast,tv_lunch,tv_dinner,tv_supper,tv_transport,tv_travel_agency,tv_hotel,tv_remark;
		 
		}
	}

}
