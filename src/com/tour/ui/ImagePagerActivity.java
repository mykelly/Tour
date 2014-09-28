package com.tour.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.tour.R;
import com.tour.photoview.HackyViewPager;
import com.tour.photoview.ImageDetailFragment;
import com.tour.util.TTLog;



public class ImagePagerActivity extends FragmentActivity  {
	private static final String STATE_POSITION = "STATE_POSITION";
	 
	public static final String COUNT = "count";
	private HackyViewPager mPager;
	private int pagerPosition = 0;
	private TextView indicator;

	private ImageView backImag;
	List<String> imagList;


	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.image_detail_pager);
		imagList=new ArrayList<String>();
		imagList.add("650328690.jpg");
		imagList.add("-193159278.jpg");
		mPager = (HackyViewPager) findViewById(R.id.pager);
		indicator = (TextView) findViewById(R.id.indicator);
		indicator.setText("");
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), imagList);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(pagerPosition);
		CharSequence text = getString(R.string.viewpager_indicator,
				pagerPosition + 1, mPager.getAdapter().getCount());
		indicator.setText(text);

		backImag = (ImageView) findViewById(R.id.img_imagepager_back);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				pagerPosition = arg0;
				TTLog.s("pagerPosition==arg0==" + pagerPosition);
				CharSequence text = getString(R.string.viewpager_indicator,
						pagerPosition + 1, mPager.getAdapter().getCount());
				indicator.setText(text);

			}

		});


	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public List<String> fileList;

		public ImagePagerAdapter(FragmentManager fm, List<String> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position);
			return ImageDetailFragment.newInstance(url);
		}

	}
	
	public void back(View view){
		finish();
	}
}