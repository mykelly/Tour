package com.tour.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import com.tour.R;
import com.tour.util.VideoDownloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.SeekBar.OnSeekBarChangeListener;
/**
 * 视频播放类 </p>
 * 
 * 跳转请务必携带视频地址和视频SD卡路径参数：</br>
 * <b>intent.putExtra("VideoPlayer.VIDEO_URL",url=???);</b>
 * <b>intent.putExtra("VideoPlayer.VIDEO_PATH",path=???);</b></p>
 * 
 * @author wl
 * 
 * @version 2014.04.15  
 * 
 */
public class TourVideoActivity extends NotTitleActivity {
	SurfaceHolder surfaceHolder;
	private ProgressBar progressBar;
	private static SeekBar seekBar;
	private SurfaceView surfaceView;
	// 播放控制器
	private LinearLayout playControl;
	private ImageButton playBtn;
	private static TextView currentTime;
	private TextView totalTime;
	VideoView videoView = null;
	Button pause;
	Button play;
	static MediaPlayer videoPlayer;
	// 是否暂停
	private static boolean isPaused = false;
	// 是否已结束页面
	private boolean isFinish = false;
	// 是否播放完毕
	private boolean isPlayComplete = false;
	// 自动隐藏播放控制器的时间
	private final int HIDE_CONTROL_TIME = 5000;
	// 视频更新状态
	private static final int VIDEO_STATE_UPDATE = 0;
	// 视频缓冲完成
	private static final int VIDEO_CACHE_READY = 1;
	// 显示播放控制器
	private static final int VIDEO_SHOW_CONTROL = 2;
	// 重新播放视频
	private static final int VIDEO_RESTART = 3;
	// 重新播放视频
	private static final int VIDEO_DOWNLOAD_ERROR = 4;
	private IntentFilter videoFilter = null;
	// 视频总时间
		private int duration = 0;
	private static VideoHandler mHandler = null;
	String videoUrl ="",localPath="",videoName="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tourvideo);
	

		
		if (getIntent().getExtras() != null&& !getIntent().getExtras().isEmpty()) {
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						   videoUrl = getIntent().getExtras().getString("VideoUrl");
								if (videoUrl != null) {
									// 获得文件名
									videoName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.length());
								}
						String path = getIntent().getExtras().getString("VideoPath");
								if (path != null) {
									// 获得存储路径
									localPath = Environment.getExternalStorageDirectory().getAbsolutePath() + path + videoName;
								}

								File file = new File(localPath);
								 if(file.exists()){
								mHandler = new VideoHandler(this);
								init();
								TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
								manager.listen(videoPhoneStateListener,
										PhoneStateListener.LISTEN_CALL_STATE);
						
								videoFilter = new IntentFilter();
								// 监听home键
								videoFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
								// 监听电源键
								videoFilter.addAction(Intent.ACTION_SCREEN_OFF);
								registerReceiver(systemStateReceiver, videoFilter);
								 }else{
										Toast.makeText(getApplicationContext(), "视频文件不存在，无法播放视频！",Toast.LENGTH_LONG).show();
										finish();
								 }
					} else {
						Toast.makeText(getApplicationContext(), "您的手机未安装SD卡，无法播放视频！",Toast.LENGTH_LONG).show();
						finish();
					}
		} else {
			Toast.makeText(getApplicationContext(), "下载地址异常，无法播放视频！",Toast.LENGTH_LONG).show();//没有传入播放地址或者该地址没有视频
			finish();
		}
		if (videoUrl == null || localPath == null) {
			finish();
			return;
		}
	}

	private void init() {
		surfaceView = (SurfaceView) findViewById(R.id.tourvideo_surfaceView);
		playControl = (LinearLayout) findViewById(R.id.tourvideo_playControl);
		playBtn = (ImageButton) findViewById(R.id.tourvideo_playBtn);
		playBtn.setOnClickListener(lisetener);
		currentTime = (TextView) findViewById(R.id.tourvideo_currentTime);
		totalTime = (TextView) findViewById(R.id.tourvideo_totalTime);
		seekBar = (SeekBar) findViewById(R.id.tourvideo_seekBar);
//		progressBar = (ProgressBar) findViewById(R.id.loadingBar);
		currentTime.setText("00:00:00");
		totalTime.setText("00:00:00");
		videoView = (VideoView) findViewById(R.id.videoView);
		pause = (Button) findViewById(R.id.pause);
		play = (Button) findViewById(R.id.play);
		pause.setOnClickListener(lisetener);
		play.setOnClickListener(lisetener);
	/*	videoView.setVideoPath(videopath);
		videoView.getWidth();
		videoView.getHeight();
		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				System.out.println("开始播放视频");
				videoPlayer.start();
				duration = videoPlayer.getDuration();
				seekBar.setMax(duration);
				// 视频总时间
				totalTime.setText(transTimeToStr(duration));
				mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
			}
		});
		videoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				System.out.println("视频錯誤");
				return false;
			}
		});
		videoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				System.out.println("视频播放完成");
			}
		});*/
		  surfaceHolder = surfaceView.getHolder();
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			surfaceHolder.setKeepScreenOn(true);
			surfaceHolder.addCallback(new Callback() {

				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					// TODO Auto-generated method stub
					destroyVideoPlayer();
				}

				@Override
				public void surfaceCreated(SurfaceHolder holder) {
					// TODO Auto-generated method stub
					createVideoPlayer(holder);
				}

				@Override
				public void surfaceChanged(SurfaceHolder holder, int format,
						int width, int height) {
					// TODO Auto-generated method stub
					videoPlayer.setDisplay(holder);
				}
			});
		seekBar.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// 初始不允许滑动：true不允许滑动，false允许
				return false;
			}
		});
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

		 

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			 

				final int seekPosition = seekBar.getProgress();
			
				isPaused = false;
				videoPlayer.seekTo(seekPosition);
				// 发送隐藏播放控制器消息
				sendHideControlMsg();
				// 更新UI状态
				mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				playBtn.setImageResource(R.drawable.video_btn_play);
				videoPlayer.pause();
				isPaused = true;
				// 移除隐藏播放控制器的操作
				removeHideControlMsg();
				// 停止更新UI
				removeVideoUpadteMsg();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (isPaused && fromUser) {
					currentTime.setText(transTimeToStr(progress));
				}
			}
		});

		surfaceView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				final int action = event.getAction();
				switch (action) {
				// 手指按下事件
				case MotionEvent.ACTION_DOWN:
					// 移除隐藏播放控制器的操作
					removeHideControlMsg();
					// 显示播放控制器
					showPlayControl();
					return true;
					// 手指离开事件
				case MotionEvent.ACTION_UP:
					// 发送隐藏播放控制器消息
					sendHideControlMsg();
					return true;
				}

				return false;
			}
		});

		playControl.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				final int action = event.getAction();
				switch (action) {
				// 手指按下事件
				case MotionEvent.ACTION_DOWN:
					// 移除隐藏播放控制器的操作
					removeHideControlMsg();
					return true;
					// 手指离开事件
				case MotionEvent.ACTION_UP:
					// 发送隐藏播放控制器消息
					sendHideControlMsg();
					return true;
				}

				return false;
			}
		});
	}

	private OnClickListener lisetener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tourvideo_playBtn:
				if (videoPlayer.isPlaying()) {
					playBtn.setImageResource(R.drawable.video_btn_play);
					videoPlayer.pause();
					isPaused = true;
				} else {
					playBtn.setImageResource(R.drawable.video_btn_pause);
					videoPlayer.start();
					isPaused = false;
				}
				break;
			case R.id.pause:
				videoPlayer.pause();
				System.out.println("视频播放暂停");
				break;
			case R.id.play:
				videoPlayer.start();
				System.out.println("视频播放开始");
				break;
			default:
				break;
			}
		}
	};
	private void destroyVideoPlayer() {
		// TODO Auto-generated method stub
		if (videoPlayer != null) {
			videoPlayer.stop();
			videoPlayer.setDisplay(null);
			videoPlayer.release();
			videoPlayer = null;
		}
	}

	/**
	 * 创建视频播放器
	 */
	private void createVideoPlayer(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (videoPlayer == null) {
			videoPlayer = new MediaPlayer();
		}

		videoPlayer.reset();
		videoPlayer.setDisplay(holder);
		try {					 
			videoPlayer.setDataSource(localPath);
			videoPlayer.prepare();   
//			videoPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 播放器准备完成
		videoPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				 
					 
					// 发送隐藏播放控制器消息
					sendHideControlMsg();
                					 
					duration = videoPlayer.getDuration();
					seekBar.setMax(duration);
					// 视频总时间
					totalTime.setText(transTimeToStr(duration));
					// 更新UI状态
					mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
					videoPlayer.start();
				 
			}
		});

		// 播放器播放完成
		videoPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.pause();
				// 先移除隐藏播放控制器的操作
				removeHideControlMsg();
				// 显示播放控制器
				showPlayControl();

				final int duration = videoPlayer.getDuration();
				currentTime.setText(transTimeToStr(duration));
				// 播放完毕设置最后的时间
				seekBar.setProgress(duration);
				isPlayComplete = true;
				showDialog();
			}
		});

		// 播发器播放出错
		videoPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				// 停止更新UI
				removeVideoUpadteMsg();
				// 提示出错
				sendErrorMsg("抱歉，视频播放出错！");
				mp.stop();
				mp.reset();
				return true;
			}
		});
//		playlocalVideo();
	}

	/**
	 * 发送出错信息
	 * 
	 * @param msg
	 *            出错信息
	 */
	private void sendErrorMsg(String msg) {
		Message message = mHandler.obtainMessage(VIDEO_DOWNLOAD_ERROR, msg);
		mHandler.sendMessage(message);
	}

	/**
	 * 播放完毕对话框
	 */
	private void showDialog() {
		// TODO Auto-generated method stub
		removeVideoUpadteMsg();
		if (isFinish) {
			return;
		}
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setMessage("视频播放完毕")
				.setPositiveButton("重播", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					 
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
							 
								mHandler.sendEmptyMessage(VIDEO_RESTART);
							}
						}).start();
						dialog.dismiss();
					}
				})
				.setNegativeButton("关闭", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						finishActivity();
					}
				}).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}

	/**
	 * 播放出错对话框
	 */
	private void showErrorDialog(String msg) {
		// TODO Auto-generated method stub
		if (isFinish) {
			return;
		}
		Dialog alertDialog = new AlertDialog.Builder(this).setMessage(msg)
				.setNegativeButton("关闭", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						finishActivity();
					}
				}).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}

	/**
	 * 结束页面</p> 发送视频播放状态广播</p>
	 * intent.getBooleanExtra(<b>VIDEO_PLAY_COMPLETE</b>)获得<b>isPlayComplete
	 * </b>是否播放完毕的值
	 * 
	 */
	private void finishActivity() {
	 
		if (videoPlayer != null) {
			videoPlayer.stop();
		}
		 finish();
	}

	/**
	 * 显示播放控制器
	 */
	private void showPlayControl() {
		// TODO Auto-generated method stub
		if (!playControl.isShown()) {
			playControl.setVisibility(View.VISIBLE);
			TranslateAnimation animation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			animation.setDuration(300);
			playControl.startAnimation(animation);
		}
	}

	/**
	 * 隐藏播放控制器
	 */
	private void hidePlayControl() {
		// TODO Auto-generated method stub
		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f);
		animation.setDuration(300);
		playControl.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				playControl.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 发送消息显示播放控制台
	 */
	private void sendHideControlMsg() {
		Message msg = mHandler.obtainMessage(VIDEO_SHOW_CONTROL);
		mHandler.sendMessageDelayed(msg, HIDE_CONTROL_TIME);
	}

	/**
	 * 移除因为延时而尚未执行的隐藏播放控制台任务的消息
	 */
	private void removeHideControlMsg() {
		// 先检查是否存在该消息
		if (mHandler.hasMessages(VIDEO_SHOW_CONTROL)) {
			mHandler.removeMessages(VIDEO_SHOW_CONTROL);
		}
	}
	/**
	 * 停止更新UI，如时间、进度条等
	 */
	private void removeVideoUpadteMsg() {
		if (mHandler != null && mHandler.hasMessages(VIDEO_STATE_UPDATE)) {
			mHandler.removeMessages(VIDEO_STATE_UPDATE);
		}
	}
	/**
	 * 将进度值换算为时间
	 * 
	 * @param time
	 *            进度值
	 * @return 时间
	 */
	private static String transTimeToStr(int time) {
		// TODO Auto-generated method stub
		int i = time / 1000;
		int hour = i / (60 * 60);
		int minute = i / 60 % 60;
		int second = i % 60;

		return String.format("%02d:%02d:%02d", hour, minute, second);
	}
	
	private static class VideoHandler extends Handler {

		private WeakReference<TourVideoActivity> mReference;

		public VideoHandler(TourVideoActivity activity) {
			// TODO Auto-generated constructor stub
			mReference = new WeakReference<TourVideoActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (mReference.get() != null) {
				TourVideoActivity theActivity = mReference.get();
				switch (msg.what) {
				case VIDEO_CACHE_READY:
					// 缓存操作
					 
					break;
				case VIDEO_STATE_UPDATE:
					// 播放操作
					int position = videoPlayer.getCurrentPosition();
					final int seekPosition = seekBar.getProgress();
					// 有拖动情况未超过已缓存值取进度条进度值，未拖动过或者超过已缓存值取播放的当前值
					position = position > seekPosition ? position : seekPosition;
					if (videoPlayer.isPlaying()) {
						// 当前播放时间
						currentTime.setText(transTimeToStr(position));
						// 当前播放进度
						seekBar.setProgress(position);						 
					} else {
						if (!isPaused ) {
						videoPlayer.seekTo(position);
						// 缓存完成可以播放视频
						videoPlayer.start();
						}
						}
					mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
					break;
				case VIDEO_SHOW_CONTROL:
					// 执行隐藏播放控制台操作
					theActivity.hidePlayControl();
					break;
				case VIDEO_RESTART:
					// 视频重新播放操作
					theActivity.handlerVideoRestart();
					break;
				case VIDEO_DOWNLOAD_ERROR:
					// 视频下载出错操作
//					theActivity.showErrorDialog(msg.obj.toString());
					break;
				case VideoDownloader.MSG_DOWNLOAD_FINISH:
					// 下载完成操作
//					theActivity.handlerDownloadFinish();
					break;
				case VideoDownloader.MSG_DOWNLOAD_UPDATE:
					// 处于下载过程中的操作
//					theActivity.handlerDownloadUpdate(msg);
					break;
				}
			}
		}
	}
	/**
	 * 视频重新播放操作
	 */
	public void handlerVideoRestart() {
		// TODO Auto-generated method stub
		videoPlayer.seekTo(0);
		seekBar.setProgress(0);
	 
		// 发送隐藏播放控制器消息
		sendHideControlMsg();
		// 更新UI状态
		mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
	}
	// 来电监听
		private final PhoneStateListener videoPhoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				// TODO Auto-generated method stub
				if (videoPlayer != null) {
					if (state == TelephonyManager.CALL_STATE_RINGING) {
						// 响铃时
						finishActivity();
					}
				}
			}
		};

		private final BroadcastReceiver systemStateReceiver = new BroadcastReceiver() {

			final String SYSTEM_DIALOG_REASON_KEY = "reason";
			final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				final String action = intent.getAction();
				if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
					String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
					if (reason != null
							&& reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
						finishActivity();
					}
				} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
					finishActivity();
				}
			}
		};
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			isFinish = true;
			removeVideoUpadteMsg(); 
			if (videoFilter != null) {
				unregisterReceiver(systemStateReceiver);
			}
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				removeVideoUpadteMsg(); 
				finishActivity();
			}
			return super.onKeyDown(keyCode, event);
		}
	
}
