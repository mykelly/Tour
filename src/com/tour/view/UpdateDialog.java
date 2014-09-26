package com.tour.view;

import com.tour.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
 

public class UpdateDialog extends Dialog {
    public  Context mContext;
    public Handler mHandler;
    
    public TextView mSure;
    public TextView mCencel;
    public TextView mNotUpdate;
    public TextView mUpdateContent;
    public TextView mTip;
    
    LinearLayout mBottom;
    private int flag;
    private String content;
	public UpdateDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public UpdateDialog(Context context, int theme,int flag,Handler mHandler) {
		// TODO Auto-generated constructor stub
		super(context, theme);
		this.mContext=context;
		this.mHandler=mHandler;
		this.flag=flag;
	}
	public UpdateDialog(Context context, int theme,int flag,String content,Handler mHandler) {
		// TODO Auto-generated constructor stub
		super(context, theme);
		this.mContext=context;
		this.mHandler=mHandler;
		this.flag=flag;
		this.content=content;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);
        mUpdateContent=(TextView)findViewById(R.id.tv_update_content);
        mTip=(TextView)findViewById(R.id.tv_update_tip);
        mSure=(TextView)findViewById(R.id.tv_update_suer);
        mCencel=(TextView)findViewById(R.id.tv_update_cancel);
        mNotUpdate=(TextView)findViewById(R.id.tv_no_update_suer);
        mSure.setOnClickListener(listener);
        mCencel.setOnClickListener(listener);
        mNotUpdate.setOnClickListener(listener);
        mBottom=(LinearLayout)findViewById(R.id.ll_update_bottom);
 
	}
	private android.view.View.OnClickListener listener=new android.view.View. OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			 
			case R.id.tv_update_cancel:
				dismissDialog();
				break;
			case R.id.tv_no_update_suer:
				dismissDialog();
				break;
			default:
				break;
			}
		}
	};
	
	private void dismissDialog(){
		super.dismiss();
	}
}
