package com.tour.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tour.R;
import com.tour.SQLite.TourData;
import com.tour.info.DataHotelInfo;
import com.tour.util.PublicData;
import com.tour.view.GalleryFlow;
import com.tour.view.GalleryimageFlow;

/**
 * 
 * @author ly 酒店详情
 */

public class TourHotelActivity extends Activity {
	private GalleryimageFlow gallery;
	private ImageButton ib_back,videobutton;
	private	TextView hotel_title; // 酒店名
	private	TextView hotel_contact; // 联系人
	private	TextView hotel_phone; // 联系电话
	private	TextView hotel_fax; // 传真
	private	TextView hotel_addr; // 地址
	private	TextView hotel_level; // 酒店级别
	public static Handler mHandler;
	private	List<DataHotelInfo> tourHotel = new ArrayList<DataHotelInfo>();
	private	List<String> ImageUrls = new ArrayList<String>();
	private	List<String> imageurl = new ArrayList<String>();
	private	int hotel_id=0;
	private	int position=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tourhoteltab);
		
		   hotel_id=getIntent().getIntExtra("hotel_id", 0);
		   position=getIntent().getIntExtra("position", 0);
//		   System.out.println("hotel_id="+hotel_id+"    postion="+position);
		   Thread thread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
		  PublicData.dataHotelData=TourData.getHotelData(TourHotelActivity.this, hotel_id);
		// tourHotel=TourData.getHotelData(hotel_id);
		   if (PublicData.dataHotelData != null&&PublicData.dataHotelData.size()>0) {
//			   System.out.println("PublicData.dataHotelData.size()="+PublicData.dataHotelData.size());
		   mHandler.sendEmptyMessage(1);
		   }
				}
				}) ;
			thread.start();
		   init();
		 
		 mHandler=new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					switch (msg.what) {
					case 0:
						
						break;

					case 1:						 
						setdata();
						break;
					}
				}
			};
	}

	

	private void init() {
		ib_back=(ImageButton)findViewById(R.id.tourhoteltab_back);
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		hotel_title = (TextView) findViewById(R.id.hotel_title);
		hotel_contact = (TextView) findViewById(R.id.hotel_contact);
		hotel_phone = (TextView) findViewById(R.id.hotel_phone);
		hotel_fax = (TextView) findViewById(R.id.hotel_fax);
		hotel_addr = (TextView) findViewById(R.id.hotel_addr);
		hotel_level= (TextView) findViewById(R.id.hotel_level);
		gallery = (GalleryimageFlow) findViewById(R.id.gy);// 使用Gallery显示商品详情图
		gallery.setSpacing(5);// 图片间距
		gallery.setSelection(4);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800p）
//		System.out.println("screenWidth" + screenWidth + "screenHeight"+ screenHeight);
		gallery.setMinimumHeight(185);
		gallery.setMinimumWidth(screenWidth * 100 / 100);

		
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TourHotelActivity.this,TourPictureDecribleActivity.class);
				startActivity(intent);
			}
		});
	}
	private void setdata() {
		// TODO Auto-generated method stub
		
		 hotel_title.setText(PublicData.dataHotelData.get(position).getHotelTitle());
		 hotel_contact.setText("联系人："+PublicData.dataHotelData.get(position).getHotelContact());
		 hotel_phone.setText("电话号码："+PublicData.dataHotelData.get(position).getHotelPhone());
		 hotel_fax.setText("传真号码："+PublicData.dataHotelData.get(position).getHotelFax());
		 hotel_addr.setText("地址："+PublicData.dataHotelData.get(position).getHotelArea());
		 hotel_level.setText("酒店级别："+PublicData.dataHotelData.get(position).getHotelLevel());
		 ImageUrls=getImageUrlList(position);
//		 System.out.println("ImageUrls.size()="+ImageUrls.size());
		 if(ImageUrls.size()>0){
		 ImageAdapter imgadapter = new ImageAdapter();
			gallery.setAdapter(imgadapter);
		 }
	}
	// private void addUi(String path, String text) {
	// ImageView imageView = new ImageView(this);
	// imageView.setBackgroundResource(R.drawable.tt);
	// TextView textView = new TextView(this);
	// textView.setTextSize(20);
	// textView.setTextColor(Color.BLACK);
	// textView.setText(text);
	// linear.addView(imageView);
	// linear.addView(textView);
	// }

	public class ImageAdapter extends BaseAdapter {

		public int getCount() {
			return ImageUrls.size();
		}

		public Object getItem(int position) {

			return position;
		}

		/**
		 * 用来得到当前项的对象的ID
		 * 
		 * @param position
		 *            作用是第几项
		 */

		public long getItemId(int position) {

			return position;
		}

		/**
		 * 用来控制控制显示适配器中每一项的具体显示内容
		 * 
		 * @param position
		 *            具体的每一项的位置
		 * @param convertView
		 *            指代本方法中返回的View类型，此处是ImageView
		 * @param parent
		 *            指代调用此适配器的那个View ,此处指代Gallery
		 */

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(TourHotelActivity.this);
			Display display = TourHotelActivity.this.getWindowManager()
					.getDefaultDisplay();
			int w = display.getWidth();// 例如w=480
			int h = display.getHeight();// 例如h=800
//			System.out.println("w" + w + "h" + h);
			i.setPadding(0, 10, 20, 10);
			i.setLayoutParams(new GalleryFlow.LayoutParams(240, 180));// 图片规格240*180
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置图片显示的缩放类型,此处是居中显示
//			i.setBackgroundResource(R.drawable.tt);
			 Drawable	d=getImageUrl(position);
			  i.setBackgroundDrawable(d);
			return i;
		}

	}
	public Drawable getImageUrl(int position){
		  String imagename=imageurl.get(position); 
		  String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		  String  pathName=SDCardRoot + File.separator + "DaMeiTour"+ File.separator+ "zip"+ File.separator+ PublicData.tour_zip+ File.separator+imagename;
		  Drawable	d=Drawable.createFromPath(pathName);
		  return d;
	}
	public List<String> getImageUrlList(int position){
		 imageurl = new ArrayList<String>();
		 if (PublicData.dataHotelData != null&&PublicData.dataHotelData.size()>0) {
				try {
					if (PublicData.dataHotelData.get(position).getHotelPhoto() != null) {
						String imageUrl =PublicData.dataHotelData.get(position).getHotelPhoto() + "|";
						if (imageUrl != null
								&& imageUrl.indexOf("|") != -1) {
							String newUrl = imageUrl.substring(0,
									imageUrl.indexOf("|"));
							imageurl.add(newUrl);
							// 剩余URL
							String remainUrl = imageUrl.substring(
									imageUrl.indexOf("|") + 1,
									imageUrl.length());
							// 直到不存在“|”
							while (remainUrl.contains("|")) {
								if (remainUrl.length() > 0) {
									newUrl = remainUrl.substring(0,
											remainUrl.indexOf("|"));
									imageurl.add(newUrl);
								}
								if (remainUrl.length() > 0)
									remainUrl = remainUrl.substring(
											remainUrl.indexOf("|") + 1,
											remainUrl.length());
							}
						}

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			}
		return imageurl;
	}
}
