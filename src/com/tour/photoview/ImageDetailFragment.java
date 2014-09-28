package com.tour.photoview;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.tour.R;
import com.tour.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.tour.util.PublicData;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;


public class ImageDetailFragment extends Fragment {
	private String TAG="ImageDetailFragment";
	private String mImageUrl;
	public ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;



	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//		imageLoader.displayImage(mImageUrl, mImageView, options,new SimpleImageLoadingListener() {
		Bitmap bitmap=getImageUrl(mImageUrl);
		mImageView.setImageBitmap(bitmap);
	}
	public Bitmap getImageUrl(String urlPath){
//		  String imagename=imageurl.get(position); 
		 
		  String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
//		  String  pathName=SDCardRoot + File.separator + "DaMeiTour"+ File.separator+ "zip"+ File.separator+ PublicData.tour_zip+ File.separator+imagename;
		  String  pathName=SDCardRoot + File.separator + "DaMeiTour"+ File.separator+ urlPath;
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

}
