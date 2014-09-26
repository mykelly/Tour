package com.tour.adpater;

import java.util.List;

import com.tour.R;
import com.tour.info.DataCustomerInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TourMemberAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private   List<DataCustomerInfo> dataCustomerInfos;
	public TourMemberAdapter(Context mContext,List<DataCustomerInfo> dataCustomerInfos){
		inflater=LayoutInflater.from(mContext);
		this.dataCustomerInfos=dataCustomerInfos;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataCustomerInfos.size();
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
			convertView=inflater.inflate(R.layout.tour_member_item, null);
			holder.memberName=(TextView)convertView.findViewById(R.id.tv_tour_member_item_name);
			holder.memberNo=(TextView)convertView.findViewById(R.id.tv_tour_member_item_no);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.memberName.setText(dataCustomerInfos.get(position).getCtTitle());
		holder.memberNo.setText(dataCustomerInfos.get(position).getCtSeat());
		return convertView;
	}
 public static class ViewHolder{
	 TextView memberNo;
	 TextView memberName;
 }
}
