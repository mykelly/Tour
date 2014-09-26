package com.tour.deskclock;

import java.io.IOException;
import java.util.HashMap;

import com.tour.R;
import com.tour.SQLite.DBTour;
import com.tour.SQLite.TourData;
import com.tour.util.PublicData;
import com.tour.util.TTLog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AlarmTipActivity extends Activity implements OnClickListener{
	private SoundPool sndPool;
	private HashMap<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();
	private MediaPlayer mediaPlayer=null;
	private int id;
	private TextView tipTv,cancelTv,sureTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_dialog);
		Intent intent=getIntent();
		id=intent.getIntExtra("id", -1);
		tipTv=(TextView)findViewById(R.id.tv_update_content);
		cancelTv=(TextView)findViewById(R.id.tv_update_cancel);
		cancelTv.setOnClickListener(this);
		sureTv=(TextView)findViewById(R.id.tv_update_suer);
		sureTv.setOnClickListener(this);
		loadSound();

	}
	private void loadSound() {
//		sndPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
//		new Thread() {
//			public void run() {
//				try {
//					soundPoolMap.put(	0,	sndPool.load(getAssets().openFd("sound/Scandium.ogg"), 1));
//					TTLog.s("soundPoolMap.get(0)"+soundPoolMap.size());
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
//		sndPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
//			
//			@Override
//			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//				// TODO Auto-generated method stub
//				sndPool.play(soundPoolMap.get(0), (float) 1, (float) 1, 0, 10,(float) 1.2);
//			}
//		});
		String tip=TourData.queryAlarmTip(id,this);
		if(tip!=null){
			tipTv.setText(tip);
		}
		mediaPlayer=PublicData.getInitMediaPlayer();
//		TTLog.s("回收数据=============="+(mediaPlayer!=null));
		if(mediaPlayer!=null){
			mediaPlayer.stop();
			mediaPlayer.release();
//			TTLog.s("回收数据==============");
		} 
		mediaPlayer=new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		PublicData.setMediaPlayer(mediaPlayer);
		try {
			AssetFileDescriptor fileDescriptor = getAssets().openFd("sound/Scandium.ogg");
			mediaPlayer = new MediaPlayer();
			    mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
			                              fileDescriptor.getStartOffset(),
			                              fileDescriptor.getLength());
			    mediaPlayer.prepareAsync();
			   
			    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			        @Override
			        public void onPrepared(MediaPlayer mediaPlayer) {
			        	if(mediaPlayer!=null){
			        		mediaPlayer.start();
			        	}
			        }
			    });
			    
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		TTLog.s("============stop=======");
		if (mediaPlayer!=null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_update_cancel:
			finish();
			break;
		case R.id.tv_update_suer:
			Intent intent=new Intent(AlarmTipActivity.this,AlarmInitReceiver.class);  
			intent.putExtra("id", Integer.valueOf(id));
			PendingIntent pi = PendingIntent.getBroadcast(AlarmTipActivity.this, Integer.valueOf(id) ,intent, PendingIntent.FLAG_CANCEL_CURRENT); //通过getBroadcast第二个参数区分闹钟，将查询得到的note的ID值作为第二个参数。
			AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE); 
			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5*60000, pi);//设置闹铃 
			finish();
			break;
		default:
			break;
		}
	}
}
