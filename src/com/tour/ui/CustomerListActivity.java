package com.tour.ui;

import com.tour.R;
import com.tour.adpater.CustomerAdapter;
import com.tour.adpater.DownVideoAdpater;
import com.tour.util.PublicData;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class CustomerListActivity extends NotTitleActivity {
	private ImageButton  ib_back;
	private	ListView lv_customer;
	/**
	 * @param wl ¡°ÂÃ¿ÍÏêÏ¸ÐÅÏ¢¡±
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_detail);
         init();
 	}
	private void init() {
		// TODO Auto-generated method stub
		ib_back=(ImageButton)findViewById(R.id.customer_detail_back);
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		lv_customer = (ListView) findViewById(R.id.customer_detail_list);
		if(PublicData.dataCustomerInfos.size()>0){
			lv_customer.setAdapter(new CustomerAdapter(getApplication()));
		}
	}

}
