package com.tour.util;

//import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class ShowToast {

	public Toast toast;
	public Context mContext;
	 
//	@SuppressLint("ShowToast")
	public  Toast getToast(Context context,String text){
		if(mContext==context){
			toast.setText(text);
		}else{
			this.mContext=context;
			Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		return toast;
	}
}
