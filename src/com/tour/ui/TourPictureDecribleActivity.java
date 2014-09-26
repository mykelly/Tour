package com.tour.ui;

import com.tour.R;
import android.app.Activity;
import android.os.Bundle;

public class TourPictureDecribleActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tour_picture_descible);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("Õº∆¨√Ë ˆΩ· ¯");
		finish();
	}
}
