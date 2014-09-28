package com.tour.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.tour.R;
import com.tour.SQLite.TourData;
import com.tour.ui.TourHotelActivity.ImageAdapter;
import com.tour.util.PublicData;
import com.tour.view.GalleryFlow;
import com.tour.view.GalleryimageFlow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 * 显示美食/酒店详情
 */
public class TourDetailsActivity extends NotTitleActivity {
	private Gallery gallery;
	private ImageButton videobutton,ib_back;
	private TextView tv_title,tv_name,tv_addr,tv_classify,tv_introduction;
	private	TextView hotel_contact; // 联系人
	private	TextView hotel_phone; // 联系电话
	private	TextView hotel_fax; // 传真
	private	TextView hotel_level; // 酒店级别
	private	LinearLayout lilyt_addr,lilyt_hotel;
    private String type="";
    private int id=0,position=0;
	private	List<String> ImageUrls = new ArrayList<String>();
	private	ArrayList<String> imageurl = new ArrayList<String>();
	private	List<String> imagedsc = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tourdetalils);
		type=this.getIntent().getStringExtra("type");
		id=this.getIntent().getIntExtra("id", 0);
		position=this.getIntent().getIntExtra("position", 0);
		init();
		if("hotel".equals(type)){
			   Thread thread=new Thread(new Runnable() {				
					@Override
					public void run() {
			// TODO Auto-generated method stub
			  PublicData.dataHotelData=TourData.getHotelData(TourDetailsActivity.this, id);
			// tourHotel=TourData.getHotelData(hotel_id);
			   if (PublicData.dataHotelData != null&&PublicData.dataHotelData.size()>0) {
//				   System.out.println("PublicData.dataHotelData.size()="+PublicData.dataHotelData.size());
//			   mHandler.sendEmptyMessage(1);
				   setdata();
			   }
					}
					}) ;
				thread.start();			  
		}else{
			setdata();
		}
		
		
	}

	

	private void init() {
		ib_back=(ImageButton)findViewById(R.id.tourdetalils_back);
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_title=(TextView)findViewById(R.id.tourdetalils_title);
		tv_name=(TextView)findViewById(R.id.tourdetalils_name);
		tv_addr=(TextView)findViewById(R.id.tourdetalils_address);
		tv_classify = (TextView) findViewById(R.id.tourdetalils_classify);
		tv_introduction=(TextView)findViewById(R.id.tourdetalils_introduction);
		lilyt_addr=(LinearLayout)findViewById(R.id.tourdetalils_addr_lilyt);
		lilyt_hotel=(LinearLayout)findViewById(R.id.tourdetalils_hotel_lilyt);
		hotel_contact = (TextView) findViewById(R.id.tourdetalils_hotel_contact);
		hotel_phone = (TextView) findViewById(R.id.tourdetalils_hotel_phone);
		hotel_fax = (TextView) findViewById(R.id.tourdetalils_hotel_fax);		
		hotel_level= (TextView) findViewById(R.id.tourdetalils_hotel_level);
		gallery = (Gallery) findViewById(R.id.tourdetalils_gy);// 使用Gallery显示商品详情图
		gallery.setSpacing(5);// 图片间距
		gallery.setSelection(4);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800p）
//		System.out.println("screenWidth" + screenWidth + "screenHeight"+ screenHeight);
		gallery.setMinimumHeight(185);
		gallery.setMinimumWidth(screenWidth * 100 / 100);
 
	}
	private void setdata() {
		// TODO Auto-generated method stub
		if("food".equals(type)){
			tv_title.setText("餐厅详情");	
			tv_name.setText(PublicData.tourFood.get(position).getFoodTitle());	
			tv_addr.setText("地址："+PublicData.tourFood.get(position).getFoodArea());	
			 tv_classify.setText("分类："+PublicData.tourFood.get(position).getFoodType());
			 if(!"".equals(PublicData.tourFood.get(position).getFoodContent())&&!"null".equals(PublicData.tourFood.get(position).getFoodContent())){
			tv_introduction.setText("简介："+PublicData.tourFood.get(position).getFoodContent());
			 }else{
				 tv_introduction.setVisibility(View.GONE);
			 }
			lilyt_hotel.setVisibility(View.VISIBLE);
			 hotel_contact.setText("联系人    ："+PublicData.tourFood.get(position).getFoodContact());
			 hotel_phone.setText("电话号码："+PublicData.tourFood.get(position).getFoodPhone());
			 hotel_fax.setVisibility(View.GONE);
			 hotel_level.setVisibility(View.GONE);
		}else if("hotel".equals(type)){
			tv_title.setText("酒店详情");	
			tv_name.setText(PublicData.dataHotelData.get(position).getHotelTitle());	
			tv_addr.setText("地址："+PublicData.dataHotelData.get(position).getHotelArea());	
			 tv_classify.setText("");
			
			 if(!"".equals(PublicData.dataHotelData.get(position).getHotelContent())&&!"null".equals(PublicData.dataHotelData.get(position).getHotelContent())){
				 tv_introduction.setText("简介："+PublicData.dataHotelData.get(position).getHotelContent());
					 }else{
						 tv_introduction.setVisibility(View.GONE);
					 }
			lilyt_hotel.setVisibility(View.VISIBLE);
			 hotel_contact.setText("联系人    ："+PublicData.dataHotelData.get(position).getHotelContact());
			 hotel_phone.setText("电话号码："+PublicData.dataHotelData.get(position).getHotelPhone());
			 hotel_fax.setText("传真号码："+PublicData.dataHotelData.get(position).getHotelFax());
			 hotel_level.setText("酒店级别："+PublicData.dataHotelData.get(position).getHotelLevel());
		}else if("scenic".equals(type)){
			tv_title.setText("景点详情");	
			tv_name.setText(PublicData.tourScenic.get(position).getPlaceTitle());	
			tv_addr.setText("地址："+PublicData.tourScenic.get(position).getPlaceArea());	
			 tv_classify.setText("");
		 
			 if(!"".equals(PublicData.tourScenic.get(position).getPlaceContent())&&!"null".equals(PublicData.tourScenic.get(position).getPlaceContent())){
				 tv_introduction.setText("简介："+PublicData.tourScenic.get(position).getPlaceContent());
					 }else{
						 tv_introduction.setVisibility(View.GONE);
					 }
			lilyt_hotel.setVisibility(View.VISIBLE);
			 hotel_contact.setText("联系人    ："+PublicData.tourScenic.get(position).getPlaceContact());
			 hotel_phone.setText("电话号码："+PublicData.tourScenic.get(position).getPlacePhone());
			 hotel_fax.setVisibility(View.GONE);
			 hotel_level.setVisibility(View.GONE);
		}
		
		 ImageUrls=getImageUrlList(position);
		 if(ImageUrls.size()>0){
		 ImageAdapter imgadapter = new ImageAdapter(TourDetailsActivity.this);
			gallery.setAdapter(imgadapter);
		 }
	}
	public class ImageAdapter extends BaseAdapter {
		LayoutInflater inflater;
		 public ImageAdapter(Context mContext){
			 inflater = LayoutInflater.from(mContext); 
		 }
		public int getCount() {
			return ImageUrls.size();
		}

		public Object getItem(int position) {

			return position;
		}

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

		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.tourgridview_spac, null);
			 ImageView	 imageView=(ImageView)convertView.findViewById(R.id.scenic_imageView);
			 imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置图片显示的缩放类型,此处是居中显示
			 TextView scenic_title=(TextView)convertView.findViewById(R.id.scenic_title);

			 Bitmap	d=getImageUrl(position);
			   imageView.setImageBitmap(d);
			 try {
				 String title=imagedsc.get(position);
				 scenic_title.setText(title);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			    
			
			 convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					BigImageDialog buyLifeDialog=new BigImageDialog(TourDetailsActivity.this, R.style.exchange_dialog,position);
//					buyLifeDialog.show();
					Intent intent=new Intent(TourDetailsActivity.this,ImagePagerActivity.class);
					intent.putStringArrayListExtra(ImagePagerActivity.DATA, imageurl);
					startActivity(intent);
				}
			});
			 return convertView;
			 
//			ImageView i = new ImageView(TourDetailsActivity.this);
//			i.setPadding(0, 20, 20, 20);
//			Display display = TourDetailsActivity.this.getWindowManager()
//					.getDefaultDisplay();
//			int w = display.getWidth();// 例如w=480
//			int h = display.getHeight();// 例如h=800
////			System.out.println("w" + w + "h" + h);
//			i.setLayoutParams(new GalleryFlow.LayoutParams(240, 180));// 图片规格240*180
//			i.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置图片显示的缩放类型,此处是居中显示
////			i.setBackgroundResource(R.drawable.tt);
//			Bitmap	d=getImageUrl(position);
//			  i.setImageBitmap(d);
//			return i;

			// return imageViewList.get(position);
		}

	}
	public Bitmap getImageUrl(int position){
		  String imagename=imageurl.get(position); 
		  String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
		  String  pathName=SDCardRoot + File.separator + "DaMeiTour"+ File.separator+ "zip"+ File.separator+ PublicData.tour_zip+ File.separator+imagename;
//		  Bitmap	d=BitmapFactory.decodeFile(pathName);
		  Bitmap d = null;					 
		  try {
			  File f=new File(pathName);		
			  FileInputStream  fis = new FileInputStream(f);
		
		// 压缩的图片（2的整数倍），数值越小，压缩率越小，图片越清晰
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 4;//原图缩小4倍
			// 将InputStream变为Bitmap，內存溢出拋异常
			try {
				
				d = BitmapFactory.decodeStream(fis, null,opts);
			} catch (OutOfMemoryError outOfMemoryError) {
				// TODO Auto-generated catch block
				outOfMemoryError.printStackTrace();
			}
			
		  } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		  return d;
	}
	public List<String> getImageUrlList(int position){
		 imageurl = new ArrayList<String>();
		 if("food".equals(type)){
			 if (PublicData.tourFood != null&&PublicData.tourFood.size()>0) {
					try {
						if (PublicData.tourFood.get(position).getFoodPhoto() != null) {
							String imageUrl =PublicData.tourFood.get(position).getFoodPhoto() + "|";
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
						if (PublicData.tourFood.get(position).getFoodPhotoDsc() != null) {
							String imageUrl =PublicData.tourFood.get(position).getFoodPhotoDsc() + "|";
							if (imageUrl != null
									&& imageUrl.indexOf("|") != -1) {
								String newUrl = imageUrl.substring(0,
										imageUrl.indexOf("|"));
								imagedsc.add(newUrl);
								// 剩余URL
								String remainUrl = imageUrl.substring(
										imageUrl.indexOf("|") + 1,
										imageUrl.length());
								// 直到不存在“|”
								while (remainUrl.contains("|")) {
									if (remainUrl.length() > 0) {
										newUrl = remainUrl.substring(0,
												remainUrl.indexOf("|"));
										imagedsc.add(newUrl);
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
			}else if("hotel".equals(type)){
			 
				 if (PublicData.dataHotelData != null&&PublicData.dataHotelData.size()>0) {
						try {
							if (PublicData.dataHotelData.get(position).getHotelPhoto()!= null) {
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
//							System.out.println("酒店图片描述="+PublicData.dataHotelData.get(position).getHotelPhotoDsc());
							if (PublicData.dataHotelData.get(position).getHotelPhotoDsc() != null) {
								String imageUrl =PublicData.dataHotelData.get(position).getHotelPhotoDsc() + "|";
								if (imageUrl != null
										&& imageUrl.indexOf("|") != -1) {
									String newUrl = imageUrl.substring(0,
											imageUrl.indexOf("|"));
									imagedsc.add(newUrl);
									// 剩余URL
									String remainUrl = imageUrl.substring(
											imageUrl.indexOf("|") + 1,
											imageUrl.length());
									// 直到不存在“|”
									while (remainUrl.contains("|")) {
										if (remainUrl.length() > 0) {
											newUrl = remainUrl.substring(0,
													remainUrl.indexOf("|"));
											imagedsc.add(newUrl);
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
			}else if("scenic".equals(type)){
				 
				 if (PublicData.tourScenic != null&&PublicData.tourScenic.size()>0) {
						try {
							if (PublicData.tourScenic.get(position).getPlacePhoto() != null) {
								String imageUrl =PublicData.tourScenic.get(position).getPlacePhoto() + "|";
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
						
							if (PublicData.tourScenic.get(position).getPlacePhotoDsc() != null) {
								String imageUrl =PublicData.tourScenic.get(position).getPlacePhotoDsc() + "|";
								if (imageUrl != null
										&& imageUrl.indexOf("|") != -1) {
									String newUrl = imageUrl.substring(0,
											imageUrl.indexOf("|"));
									imagedsc.add(newUrl);
									// 剩余URL
									String remainUrl = imageUrl.substring(
											imageUrl.indexOf("|") + 1,
											imageUrl.length());
									// 直到不存在“|”
									while (remainUrl.contains("|")) {
										if (remainUrl.length() > 0) {
											newUrl = remainUrl.substring(0,
													remainUrl.indexOf("|"));
											imagedsc.add(newUrl);
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
				
			}
		
		return imageurl;
	}
	public class  BigImageDialog extends Dialog {
		private Handler mHandler;
		private Context mContext;
		private TextView tv_tips;
		private Button bt_support_confirm,bt_support_cancel;// 确定和取消
		private int position=0;
		public BigImageDialog(Context context, int theme,int i) {
			super(context, theme);
			// TODO Auto-generated constructor stub
			mContext = context;
			position=i;
		}
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.tourdetails_dialog);
			initDialog();

		}
		public void initDialog() {
			// TODO Auto-generated method stub
			 ImageView	 imageView=(ImageView)findViewById(R.id.tourdetails_dialog_imageView);
//			 imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置图片显示的缩放类型,此处是居中显示
			 TextView scenic_title=(TextView)findViewById(R.id.tourdetails_dialog_title);
			 Bitmap	d=getImageUrl(position);
			   imageView.setImageBitmap(d);
			   try {
					 String title=imagedsc.get(position);
					 scenic_title.setText(title);
				} catch (Exception e) {
					// TODO: handle exception
				}
			Button btn_cancel=(Button)findViewById(R.id.tourdetails_dialog_cancel);
			btn_cancel.setOnClickListener(clickListener);
		

		}
		private View.OnClickListener clickListener=new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
			 
				case R.id.tourdetails_dialog_cancel:
					dismiss();
					break;
				}
			
			}
		};
		public Bitmap getImageUrl(int position){
			  String imagename=imageurl.get(position); 
			  String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
			  String  pathName=SDCardRoot + File.separator + "DaMeiTour"+ File.separator+ "zip"+ File.separator+ PublicData.tour_zip+ File.separator+imagename;
//			  Bitmap	d=BitmapFactory.decodeFile(pathName);
			  Bitmap d = null;					 
			  try {
				  File f=new File(pathName);		
				  FileInputStream  fis = new FileInputStream(f);
			
			// 压缩的图片（2的整数倍），数值越小，压缩率越小，图片越清晰
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 2;//原图缩小2倍
				// 将InputStream变为Bitmap，內存溢出拋异常
				try {
					
					d = BitmapFactory.decodeStream(fis, null,opts);
				} catch (OutOfMemoryError outOfMemoryError) {
					// TODO Auto-generated catch block
					outOfMemoryError.printStackTrace();
				}
				
			  } catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			  return d;
		}
	}
}
