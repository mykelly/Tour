package com.tour.adpater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tour.R;
import com.tour.info.DataPlaceInfo;
import com.tour.util.PublicData;

public class ScenicGidAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<DataPlaceInfo> tourScenic;
	private List<String> imageurl = new ArrayList<String>();// 存贮图片
	public ScenicGidAdapter(Context mContext,List<DataPlaceInfo> tourScenic){
		this.tourScenic=tourScenic;
		 inflater = LayoutInflater.from(mContext);
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return tourScenic.size();
		return tourScenic.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		convertView = inflater.inflate(R.layout.tourgridview_spac, null);
		 LinearLayout
		 scenic_background=(LinearLayout)convertView.findViewById(R.id.scenic_background);
		 ImageView
		 scenic_imageView=(ImageView)convertView.findViewById(R.id.scenic_imageView);
		 TextView
		 scenic_title=(TextView)convertView.findViewById(R.id.scenic_title);

		 Bitmap	d=getImageUrl(position);
		  scenic_imageView.setImageBitmap(d);
		 String title=tourScenic.get(position).getPlaceTitle();
//		 scenic_imageView.setBackgroundResource(R.drawable.rr);
		 scenic_title.setText(title);
		return convertView;
	}
	public Bitmap getImageUrl(int position){
		 if (tourScenic != null&&tourScenic.size()>0) {
			 imageurl = new ArrayList<String>();
				try {
					if (tourScenic.get(position).getPlacePhoto() != null) {
						String imageUrl =tourScenic.get(position).getPlacePhoto() + "|";
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
//		  String imagename="1212181717374p3y.jpg";
		  String imagename=imageurl.get(0);//默认选第一个图片做封面
//		  System.out.println("position="+position+"      imagename="+imagename);
		  String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		  String  pathName=SDCardRoot + File.separator + "DaMeiTour"+ File.separator+ "zip"+ File.separator+ PublicData.tour_zip+ File.separator+imagename;
//		  Bitmap	d=BitmapFactory.decodeFile(pathName);
		  Bitmap d = null;					 
		  try {
			  File f=new File(pathName);		
			  FileInputStream  fis = new FileInputStream(f);
		
		// 压缩的图片（2的整数倍），数值越小，压缩率越小，图片越清晰
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 2;
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
