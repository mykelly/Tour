package com.tour.adpater;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tour.R;
import com.tour.info.CallNameInfo;
import com.tour.ui.CallNameActivity;
import com.tour.util.PublicData;

public class CallNameAdpater extends BaseAdapter {
	LayoutInflater inflater;
	private Handler mHandler;
	public CallNameAdpater(Context mContext,Handler mHandler){
		 inflater = LayoutInflater.from(mContext);
		 this.mHandler=mHandler;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return tourScenic.size();
		return PublicData.dataCustomerInfos!=null?PublicData.dataCustomerInfos.size():0;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
		    convertView = inflater.inflate(R.layout.call_name_item, null);
		    holder.rootLayout=(LinearLayout)convertView.findViewById(R.id.ll_call_name_item);
		    holder.seatPosition=(TextView)convertView.findViewById(R.id.tv_call_name_item_number);
		    holder.team=(TextView)convertView.findViewById(R.id.tv_call_name_item_team);
		    holder.place=(TextView)convertView.findViewById(R.id.tv_call_name_item_place);
		    holder.phone=(TextView)convertView.findViewById(R.id.tv_call_name_item_phone);
		    holder.name=(TextView)convertView.findViewById(R.id.tv_call_name_item_name);
		    holder.status=(ImageView)convertView.findViewById(R.id.iv_call_name_item_status);
		    convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if( PublicData.dataCustomerInfos.get(position).isAbsent()){
			 holder.status.setImageResource(R.drawable.tg_arrive_focus);
		}else{
			 holder.status.setImageResource(R.drawable.tg_absent_default);
		}
		holder.name.setText(PublicData.dataCustomerInfos.get(position).getCtTitle());
		holder.seatPosition.setText(PublicData.dataCustomerInfos.get(position).getCtSeat());
		holder.team.setText(PublicData.dataCustomerInfos.get(position).getCtTeam());
		holder.place.setText(PublicData.dataCustomerInfos.get(position).getCtPlace());
		holder.phone.setText(PublicData.dataCustomerInfos.get(position).getCtPhone());
		holder.rootLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    String id=PublicData.dataCustomerInfos.get(position).getCtId()+"";
				boolean isstatus=PublicData.dataCustomerInfos.get(position).isAbsent();
				if(isstatus){
					//È±Ï¯
                     mHandler.sendEmptyMessage(0);
                     CallNameActivity.callname(id, false);
				}else{
					//ÒÑµ½
					mHandler.sendEmptyMessage(1);
					 CallNameActivity.callname(id, true);
				}
				
				PublicData.dataCustomerInfos.get(position).setAbsent(!isstatus);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
 public static class ViewHolder{
	 LinearLayout rootLayout;
	TextView seatPosition,team,place,phone;
	TextView name;
	ImageView status;
}
}
