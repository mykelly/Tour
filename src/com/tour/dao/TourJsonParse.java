package com.tour.dao;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import com.tour.SQLite.TourData;
public class TourJsonParse {/*
//HttpDown httpDown=new HttpDown();
public   void getNetDate(String data) throws Exception{
	JSONObject jsonObject=new JSONObject(data);
	String status=jsonObject.getString("statues");
	if("0".equals(status)){

	}else{
	JSONArray jsonArray=jsonObject.getJSONArray("tour");
	for(int i=0;i<jsonArray.length();i++){
		JSONObject item=jsonArray.getJSONObject(i);
		int tour_id=-1 ;
		tour_id=item.getInt("tour_id");
		String tour_title=null;
		tour_title=item.getString("tour_title");
		String tour_date=null;
		tour_date=item.getString("tour_date");
		int tour_no=-1;
		tour_no=item.getInt("tour_no");
		String tour_driver=null;
		tour_driver=item.getString("tour_driver");
		String tour_leader=null;
		tour_leader=item.getString("tour_leader");
		int tour_day=-1;
		tour_day=item.getInt("tour_day");
        int tour_type=-1;
        tour_type=item.getInt("tour_type");
        int  tour_ct_count=-1;
        tour_ct_count=item.getInt("tour_ct_count");
        int tour_flat=-1;
        tour_flat=item.getInt("tour_flat");
        String tour_update_time=null;
        tour_update_time=item.getString("tour_update_time");        
		JSONArray jsonArray2=item.getJSONArray("content");
		for(int j=0;j<jsonArray2.length();j++){
//			System.out.println("jsonArray3.length()"+jsonArray3.length());
			
			 * 行程
			 		
				JSONObject item3=jsonArray2.getJSONObject(j);
				String line=null;
				line=item3.getString("line");
				String hotel=null;
				hotel=item3.getString("hotel");

		}
			
			 *食物
			 
			JSONArray jsonArray3=item.getJSONArray("food");
			System.out.println("jsonArray3.length()"+jsonArray3.length());
			for(int m=0;m<jsonArray3.length();m++){
				JSONObject item3=jsonArray3.getJSONObject(m);
				int food_id=-1;
				food_id=item3.getInt("food_id");
				int food_tid=-1;
				food_tid=item3.getInt("food_tid");
				String food_title=null;
				food_title=item3.getString("food_title");
				String food_area=null;
				food_area=item3.getString("food_area");
				String food_content=null;
				food_content=item3.getString("food_content");
				String food_photo=null;
				food_photo=item3.getString("food_photo");
				String food_photo_dsc=null;
				food_photo_dsc=item3.getString("food_photo_dsc");
				String food_movie=null;
				food_movie=item3.getString("food_movie");
				TourData.addFoodData(food_id, food_tid, food_title, food_area, food_content, food_photo, food_photo_dsc, food_movie);
;
				try {

					httpDown.downLoadUrl("imageurl","http://shoumedia.com/Public/Upload/421/_thumb_120802103801ms8r.jpg");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			 * 旅游景点
			 
			JSONArray jsonArray4=item.getJSONArray("place");
//			System.out.println("jsonArray5.length()"+jsonArray4.length());
			for(int h=0;h<jsonArray4.length();h++){
				JSONObject item4=jsonArray4.getJSONObject(h);
				int place_id=-1;
				place_id=item4.getInt("place_id");
				int place_tid=-1;
				place_tid=item4.getInt("place_tid");
				String place_title=null;
				place_title=item4.getString("place_title");
				String place_area=null;
				place_area=item4.getString("food_area");
				String place_content=null;
				place_content=item4.getString("place_content");
				String place_photo=null;
				place_photo=item4.getString("place_photo");
				String place_photo_dsc=null;
				place_photo_dsc=item4.getString("place_photo_dsc");
				String place_movie=null;
				place_movie=item4.getString("place_movie");
				TourData.addJourneyData(place_id, place_tid, place_title, place_area, place_content, place_photo, place_photo_dsc, place_movie);
				try {
					httpDown.downLoadUrl("imageurl","http://shoumedia.com/Public/Upload/421/_thumb_120802103801ms8r.jpg");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				System.out.println("title"+title+"imageurl2"+imageurl2);
			}
			*//**
			 * 酒店
			 *//*
			JSONArray jsonArray5=item.getJSONArray("hotel");
			for(int n=0;n<jsonArray5.length();n++){
			JSONObject item5=jsonArray5.getJSONObject(n);
			int hotel_id=-1;
			hotel_id=item5.getInt("hotel_id");
			int hotel_tid=-1;
			hotel_tid=item5.getInt("hotel_tid");
			String hotel_title=null;
			hotel_title=item5.getString("hotel_title");
			String hotel_contact=null;
			hotel_contact=item5.getString("hotel_contact");
			int hotel_phone=-1;
			hotel_phone=item5.getInt("hotel_phone");
			int hotel_fax=-1;
			hotel_fax=item5.getInt("hotel_fax");
			String hotel_addr=null;
			hotel_addr=item5.getString("hotel_addr");
			String hotel_photo=null;
			hotel_photo=item5.getString("hotel_photo");
			TourData.addHotelData(hotel_id, hotel_tid, hotel_title, hotel_contact, hotel_phone, hotel_fax, hotel_addr, hotel_photo);
			}
			 TourData.addTourData(tour_id, tour_title, tour_no, tour_driver, tour_leader, tour_flat, tour_date, tour_day, tour_type, tour_ct_count, tour_update_time);
		}

        
}
}

*/}