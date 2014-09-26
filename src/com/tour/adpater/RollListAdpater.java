package com.tour.adpater;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tour.R;
import com.tour.info.RollListInfo;

public class RollListAdpater extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<RollListInfo> tourList;

	public RollListAdpater(Context mContext, List<RollListInfo> tourList) {
		this.tourList = tourList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		return tourDatasList != null ? tourDatasList.size() : 0;
		return  2;
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.roll_list_item, null);

			holder.mDate=(TextView)convertView.findViewById(R.id.roll_list_item_formdate);
			holder.mTitle=(TextView)convertView.findViewById(R.id.roll_list_item_title);
			holder.mNumber=(TextView)convertView.findViewById(R.id.roll_list_item_number);
			holder.mDowanLoad=(TextView)convertView.findViewById(R.id.roll_list_item_down);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		

		
		holder.mDowanLoad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		TextView mDate; // 日期
		TextView mTitle; // 团名
		TextView mNumber;
		TextView mDowanLoad;
	}
}
