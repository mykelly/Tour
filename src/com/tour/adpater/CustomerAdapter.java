package com.tour.adpater;

import com.tour.R;
import com.tour.adpater.CallNameAdpater.ViewHolder;
import com.tour.util.PublicData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomerAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	public CustomerAdapter (Context mContext){
		this.mContext=mContext;
		mInflater=LayoutInflater.from(mContext);	
		}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
		    convertView = mInflater.inflate(R.layout.customer_item, null);
		    holder.tv_name=(TextView)convertView.findViewById(R.id.customer_item_name);
		    holder.tv_seat=(TextView)convertView.findViewById(R.id.customer_item_seat);
		    holder.tv_team=(TextView)convertView.findViewById(R.id.customer_item_team);
		    holder.tv_type=(TextView)convertView.findViewById(R.id.customer_item_type);
		    holder.tv_typeremark=(TextView)convertView.findViewById(R.id.customer_item_typeremark);
		    holder.tv_sex=(TextView)convertView.findViewById(R.id.customer_item_sex);
		    holder.tv_age=(TextView)convertView.findViewById(R.id.customer_item_age);
		    holder.tv_phone=(TextView)convertView.findViewById(R.id.customer_item_phone);
		    holder.tv_idno=(TextView)convertView.findViewById(R.id.customer_item_idno);
		    holder.tv_place=(TextView)convertView.findViewById(R.id.customer_item_place);		 
		    holder.tv_remark=(TextView)convertView.findViewById(R.id.customer_item_remark);
		    convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		try {
			holder.tv_name.setText(PublicData.dataCustomerInfos.get(position).getCtTitle());
			holder.tv_seat.setText(PublicData.dataCustomerInfos.get(position).getCtSeat());
			holder.tv_team.setText(PublicData.dataCustomerInfos.get(position).getCtTeam());
			holder.tv_type.setText(PublicData.dataCustomerInfos.get(position).getCtType());
			holder.tv_typeremark.setText(PublicData.dataCustomerInfos.get(position).getCtType_remark());
			holder.tv_sex.setText(PublicData.dataCustomerInfos.get(position).getCtSex());
			holder.tv_age.setText(PublicData.dataCustomerInfos.get(position).getCtAge());
			holder.tv_phone.setText(PublicData.dataCustomerInfos.get(position).getCtPhone());
			holder.tv_idno.setText(PublicData.dataCustomerInfos.get(position).getCtIdno());
			holder.tv_place.setText(PublicData.dataCustomerInfos.get(position).getCtPlace());		
			holder.tv_remark.setText(PublicData.dataCustomerInfos.get(position).getCtRemark());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return convertView;
	}
	public  class ViewHolder{
		TextView tv_name,tv_seat,tv_type,tv_typeremark,tv_sex,tv_age,tv_phone,tv_idno,tv_place,tv_team,tv_remark;
		 
	}
}
