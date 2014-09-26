package com.tour.view;

import com.tour.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WaitDialog extends Dialog {
    private ImageView imageView ;
    private Animation animation;
	public WaitDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		animation = AnimationUtils.loadAnimation(context,R.anim.dialog_image_rotate);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogview);
		 imageView = (ImageView)findViewById(R.id.dialog_img);
		 imageView.startAnimation(animation);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(animation!=null)
		animation.cancel();
	}
}
