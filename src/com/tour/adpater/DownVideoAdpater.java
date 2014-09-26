package com.tour.adpater;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.down.sdk.service.DowenLoadService;
import com.down.sdk.util.Dao;
import com.down.sdk.util.DownCallBack;
import com.down.sdk.util.DownMain;
import com.down.sdk.util.FileUtils;
import com.tour.R;
import com.tour.ui.TourVideoActivity;
import com.tour.ui.VideoPlayer;
import com.tour.util.PublicData;
import com.tour.util.TTLog;

public class DownVideoAdpater extends BaseAdapter {
	private Context mContext;
	private DownMain downMain;
	private Dao dao;
	public DownVideoAdpater(Context mContext,DownCallBack callBack){
		this.mContext=mContext;
		dao=new Dao(mContext);
		FileUtils.ExistSDCard(mContext);
		downMain=new DownMain(mContext);
		downMain.setDownCallBack(callBack);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PublicData.moviephone.size();
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
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater minflater = LayoutInflater.from(mContext);
			convertView = minflater.inflate(R.layout.downvideoelection,null);
			holder = new ViewHolder();
			holder.videotitle = (TextView) convertView.findViewById(R.id.videotitle);
			holder.videokey = (TextView) convertView.findViewById(R.id.videokey);
			holder.videoplay = (Button) convertView.findViewById(R.id.playvideo);
			holder.videodown = (Button) convertView.findViewById(R.id.downvideo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.videodown.setTag(PublicData.moviephone.get(position).getId()+"");
		holder.videotitle.setText(PublicData.moviephone.get(position).getTitle());
		if(!"".equals(PublicData.moviephone.get(position).getKey())){
			holder.videokey.setText("("+PublicData.moviephone.get(position).getKey()+")");
		}else{
			holder.videokey.setText("");
		}
		if(isDownCom(position)){
			holder.videodown.setText("已下载");
		}else{
			String id=PublicData.moviephone.get(position).getId()+"";
			TTLog.s("xxxxxxxx========="+id);
			if(DowenLoadService.downloaders!=null&&DowenLoadService.downloaders.get(id)!=null&&DowenLoadService.downloaders.get(id).isdownloading()){
				holder.videodown.setText("正在下载");
			}else{
				holder.videodown.setText("下载视频");
			}
			
		}
		holder.videokey.setText(PublicData.moviephone.get(position).getKey());
		holder.videodown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//				if (DowenLoadService.downloaders.get(PublicData.moviephone.get(position).getId()+"").isdownloading())
				if(PublicData.isNetWork){//联网		
					downMain.DownloadStart(position);
					Button btn= (Button)v.findViewById(R.id.downvideo);
					btn.setText("正在下载");

				}else{
					Toast.makeText(mContext, "网络已断开，无法下载视频！",Toast.LENGTH_LONG).show();  
				}
				//				else{
				//					downMain.DownloadPause(position);
				//				}
			}
		});
		holder.videoplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isDownCom(position)){
					String videoUrl=PublicData.moviephone.get(position).getMovieUrl();
					Intent intent=new Intent();				
					//					if(PublicData.isNetWork){//联网
					//						 intent.setClass(mContext, VideoPlayer.class);//播放网络视频
					//					}else{
					intent.setClass(mContext, TourVideoActivity.class);	//播放本地视频
					//					}					
					intent.putExtra("VideoUrl", videoUrl);
					intent.putExtra("VideoPath","/DaMeiTour/video/");
					mContext.startActivity(intent);
				}else{
					Toast toast=Toast.makeText(mContext, "下载完，才能播放",Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				



			}
		});
		return convertView;
	}
	private boolean isDownCom(int position){
		String downUrl=PublicData.moviephone.get(position).getMovieUrl();
		int downSize=dao.getInfos(downUrl, 0);
		int compeleteSize =0;
		String filename = downUrl.substring(downUrl.lastIndexOf("/") + 1, downUrl.length());
		File file_size = new File(FileUtils.jointPath(filename));
		if(file_size.exists()){
		    compeleteSize = (int) file_size.length();
		}
		TTLog.s(compeleteSize+"===downSize======="+downSize);
		if(downSize!=0&&(compeleteSize==downSize)){
			return true;
		}
		return false;
	}

}

class ViewHolder {
	TextView videotitle,videokey;
	Button videodown;
	Button videoplay;
}

