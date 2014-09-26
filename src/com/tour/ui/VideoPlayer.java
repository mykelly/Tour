package com.tour.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import com.tour.R;
import com.tour.util.NetWorkStatus;
import com.tour.util.VideoDownloader;
import com.tour.util.VideoDownloader.OnVideoDownloadErrorListener;

//import android.annotation.SuppressLint;
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
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

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
//@SuppressLint("DefaultLocale")
public class VideoPlayer extends NotTitleActivity {
	SurfaceHolder surfaceHolder;
	public static final String VIDEO_URL = "VideoUrl";
	public static final String VIDEO_PATH = "VideoPath";
	public static final String VIDEO_PLAY_ACTION = "zqkj.intent.action.video";
	public static final String VIDEO_PLAY_COMPLETE = "isPlayComplete";
	// 起始缓冲大小,当前表示3%
	private final int READY_BUFF = 10;
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

	private ProgressBar progressBar;
	private SeekBar seekBar;
	private SurfaceView surfaceView;
	// 播放控制器
	private LinearLayout playControl;
	private ImageButton playBtn;
	private TextView currentTime, totalTime;
	// 视频网络地址
	private String videoUrl = null;
	// 视频名称
	private String videoName = null;
	// 本地视频存放路径
	private String localPath = null;

	private MediaPlayer videoPlayer = null;
	private IntentFilter videoFilter = null;
	private VideoHandler mHandler = null;
	private VideoDownloader downloader = null;
	// 视频总大小
	private long videoTotalSize = 0;
	// 视频当前的已缓冲大小
	private long videoCacheSize = 0;
	// 准备播放视频出现的异常次数统计
	private int handlerPrepareExceptionTimes = 0;
	// 已经下载进度
	private int cachePercent = 0;
	// 视频总时间
	private int duration = 0;
	// 是否准备完成
	private boolean isReady = false;
	// 是否需要等待加载完成
	private boolean isLoading = false;
	// 是否暂停
	private boolean isPaused = false;
	// 是否拖动过
	private boolean isSeeked = false;
	// 是否已结束页面
	private boolean isFinish = false;
	// 是否允许seekBar触摸滑动
	private boolean isSeekBarCanTouch = false;
	// 文件是否存在
	private boolean isFileExist = false;
	// 数据库是否有该视频记录
	private boolean isDBInfoExist = false;
	// 是否播放完毕
	private boolean isPlayComplete = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 不锁屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.videoplayer);
		
		if (getIntent().getExtras() != null
				&& !getIntent().getExtras().isEmpty()) {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				videoUrl = getIntent().getExtras().getString(VIDEO_URL);
				
				if (videoUrl != null) {
					// 获得文件名
					videoName = videoUrl.substring(
							videoUrl.lastIndexOf("/") + 1, videoUrl.length());
				}
				String path = getIntent().getExtras().getString(VIDEO_PATH);
				if (path != null) {
					// 获得存储路径
					localPath = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + path + videoName;
				}
				 File  dir = new File(path);					       
				    if(!dir.exists())
				        dir.mkdirs();  //如果不存在则创建
			} else {
				Toast.makeText(getApplicationContext(), "您的手机未安装SD卡，无法播放视频！",
						Toast.LENGTH_LONG).show();
				finish();
			}
//			localPath="/storage/sdcard0/DaMeiTour/video/13031403002735tq.mp4";
//			System.out.println("在线播放视频videoUrl="+videoUrl);
//			System.out.println("localPath="+localPath);		

			mHandler = new VideoHandler(this);
			// 初始化配置
			init();
				
//			File file = new File(localPath);
//			 if(file.exists()){
//				playlocalVideo();//文件存在播放本地视频
//			}else{
				// 播放视频
				playVideo();
//			}
			TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			manager.listen(videoPhoneStateListener,
					PhoneStateListener.LISTEN_CALL_STATE);

			videoFilter = new IntentFilter();
			// 监听home键
			videoFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			// 监听电源键
			videoFilter.addAction(Intent.ACTION_SCREEN_OFF);
			registerReceiver(systemStateReceiver, videoFilter);
		} else {
			Toast.makeText(getApplicationContext(), "下载地址异常，无法播放视频！",Toast.LENGTH_LONG).show();//没有传入播放地址或者该地址没有视频
			finish();
		}
		if (videoUrl == null || localPath == null) {
			finish();
			return;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		isFinish = true;
		// 断掉下载线程
		isReady = true;
		removeVideoUpadteMsg();
		removeVideoErrorMsg();

		if (downloader != null) {
			downloader.cancelDownload(isSeeked, videoCacheSize);
		}
		if (videoFilter != null) {
			unregisterReceiver(systemStateReceiver);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化视频播放组件
	 */
	private void init() {
		// TODO Auto-generated method stub
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		playControl = (LinearLayout) findViewById(R.id.playControl);
		playBtn = (ImageButton) findViewById(R.id.playBtn);
		currentTime = (TextView) findViewById(R.id.currentTime);
		totalTime = (TextView) findViewById(R.id.totalTime);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		progressBar = (ProgressBar) findViewById(R.id.loadingBar);

		currentTime.setText("00:00:00");
		totalTime.setText("00:00:00");
		videoPlayer = new MediaPlayer();
		downloader = new VideoDownloader(this, mHandler, videoUrl, localPath,
				videoName);
		 
		  surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	
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
				return !isSeekBarCanTouch;
			}
		});
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			boolean theFirst = true;
			// 记录断点开关
			boolean setCurrentCache = true;
			// 记录断点位置，以后不再发生更改
			long currentCachePercent = 0;

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				showLoading();

				final int seekPosition = seekBar.getProgress();
				if (setCurrentCache) {
					currentCachePercent = cachePercent;
				}
				// 用户拖动的情况只记录拖动前的缓存
				if (seekPosition > currentCachePercent) {
					if (theFirst) {
						theFirst = false;
						isSeeked = true;
						setCurrentCache = false;
						downloader.seekVideoDBInfo(videoCacheSize);
					}
					downloader.seekLoadVideo(seekPosition / 1000);
				} else {
					// 设置进度值，保证seekBar的进度更新(在已缓存进度范围内生效)
					videoPlayer.seekTo(seekPosition);
					int next5sec = seekPosition + 5 * 1000;
					if (next5sec > duration) {
						next5sec = duration;
					}
					// 判断5秒后的视频缓冲是否完成
					if (downloader.isInitComplete()
							&& !downloader.checkIsBuffered(next5sec / 1000)) {
						// 从未完成处开始下载
						downloader.seekLoadVideo(cachePercent / 1000);
					}
				}

				isPaused = false;
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

		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isReady || isLoading) {
					return;
				}

				if (videoPlayer.isPlaying()) {
					playBtn.setImageResource(R.drawable.video_btn_play);
					videoPlayer.pause();
					isPaused = true;
				} else {
					playBtn.setImageResource(R.drawable.video_btn_pause);
					videoPlayer.start();
					isPaused = false;
				}
			}
		});

		downloader.setOnVideoDownloadErrorListener(new OnVideoDownloadErrorListener() {

					public void onError() {
						// TODO Auto-generated method stub
						downloader.setOnVideoDownloadErrorListener(null);
						if (videoPlayer != null && videoPlayer.isPlaying()) {
							videoPlayer.stop();
						}
						sendErrorMsg("网速不给力啊，请稍后重试！");
					}
				});
		
		
	}
	private void playlocalVideo(){
//		if (!isNetWork) {//播放本地视频			 
//		System.out.println("播放本地视频localPath="+localPath);	
		if (videoPlayer == null) {
			videoPlayer = new MediaPlayer();
		}
	     if(videoPlayer.isPlaying()==true){
	    		videoPlayer.reset();
	        }
//		videoPlayer.setDisplay(surfaceHolder);
		 
		try {					 
			videoPlayer.setDataSource(localPath);
//			videoPlayer.prepareAsync();
			videoPlayer.prepare();   
			videoPlayer.start();
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
			
//	}
	}
	/**
	 * 开始视频播放
	 */
	private void playVideo() {
		// TODO Auto-generated method stub
	
		if (!URLUtil.isNetworkUrl(videoUrl)) {
			try {
				// 若该地址非标准网络地址，如可能为本地路径等，则直接播放视频
				videoPlayer.setDataSource(videoUrl);
				videoPlayer.prepareAsync();
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
			return;
		}

		showLoading();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					prepareVideo();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					sendErrorMsg("网络异常，请稍后重试！");
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 开始播放的准备阶段
	 * 
	 * @throws IOException
	 */
	private void prepareVideo() throws Exception {
		// TODO Auto-generated catch block
		long videoInfo[] = downloader.queryVideoDBInfo(videoName);
		isDBInfoExist = videoInfo[0] == 1 ? true : false;
		File cacheFile = new File(localPath);
		isFileExist = cacheFile.exists();

		if (isFileExist) {
			if (isDBInfoExist) {
				// 数据库记录的缓存大小
				videoCacheSize = videoInfo[1];
				// 数据库记录的总大小
				videoTotalSize = videoInfo[2];
				mHandler.sendEmptyMessageDelayed(VIDEO_CACHE_READY, 1000);
				// 数据库有记录并且文件存在直接播放
				return;
			} else {
				// 文件存在但无数据库记录删除
				cacheFile.delete();
				// 状态修改为不存在
				isFileExist = false;
			}
		}

		URL url = new URL(videoUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(20000);
		conn.setRequestProperty("RANGE", "bytes=" + 0 + "-");

		InputStream is = conn.getInputStream();
		videoTotalSize = conn.getContentLength();
		if (videoTotalSize == -1) {
			return;
		}

		cacheFile.getParentFile().mkdirs();
		// 创建该文件及目录
		cacheFile.createNewFile();
		RandomAccessFile raf = new RandomAccessFile(cacheFile, "rws");
		raf.setLength(videoTotalSize);
		raf.seek(0);

		videoCacheSize = 0;
		byte buf[] = new byte[10 * 1024];
		int len = 0;
		boolean b = true;
		// 计算该视频需要缓冲的大小
		final long cache = (videoTotalSize * READY_BUFF) / 100;

		while ((len = is.read(buf)) != -1 && (!isReady)) {
			try {
				raf.write(buf, 0, len);
				videoCacheSize += len;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (b && videoCacheSize > cache) {
				// 达到条件只须发送一次消息
				b = false;
				mHandler.sendEmptyMessage(VIDEO_CACHE_READY);
				 
			}
		}

		if (!isReady) {
			mHandler.sendEmptyMessage(VIDEO_CACHE_READY);
		}

		is.close();
		raf.close();
	}

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
		
		// 播放器准备完成
		videoPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if (!isReady) {
					isReady = true;
					// 发送隐藏播放控制器消息
					sendHideControlMsg();
                
					// 初始化下载
					downloader.initVideoDownloader(videoCacheSize,
							videoTotalSize, isFileExist, isDBInfoExist);

					duration = videoPlayer.getDuration();
					seekBar.setMax(duration);
					// 视频总时间
					totalTime.setText(transTimeToStr(duration));
					// 更新UI状态
					mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
				
				}
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

//		File file = new File(localPath);
//		 if(file.exists()){
//			playlocalVideo();//文件存在播放本地视频
//		} 
		
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
						showLoading();
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (!isSeeked) {
									// 未发生超过当前已缓存大小的拖动情况，更新数据库
									downloader.seekVideoDBInfo(videoCacheSize);
								}
								long videoInfo[] = downloader
										.queryVideoDBInfo(videoName);
								// 数据库记录的缓存大小
								videoCacheSize = videoInfo[1];
								// 可以重播说明文件已经存在
								isFileExist = true;
								// 可以重播说明数据库已经有记录
								isDBInfoExist = true;
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
		Intent intent = new Intent(VIDEO_PLAY_ACTION);
		intent.putExtra(VIDEO_PLAY_COMPLETE, isPlayComplete);
		// 发送广播用以外部处理
		sendBroadcast(intent);

		if (videoPlayer != null) {
			videoPlayer.stop();
		}
		VideoPlayer.this.finish();
	}

	/**
	 * 显示等待框
	 */
	private void showLoading() {
		// TODO Auto-generated method stub
		isLoading = true;
		progressBar.setVisibility(View.VISIBLE);
		playBtn.setImageResource(R.drawable.video_btn_play);
	}

	/**
	 * 隐藏等待框
	 */
	private void hideLoading() {
		// TODO Auto-generated method stub
		isLoading = false;
		progressBar.setVisibility(View.GONE);
		playBtn.setImageResource(R.drawable.video_btn_pause);
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
	 * 移除错误信息
	 */
	private void removeVideoErrorMsg() {
		if (mHandler != null && mHandler.hasMessages(VIDEO_DOWNLOAD_ERROR)) {
			mHandler.removeMessages(VIDEO_DOWNLOAD_ERROR);
		}
	}

	/**
	 * 将进度值换算为时间
	 * 
	 * @param time
	 *            进度值
	 * @return 时间
	 */
	private String transTimeToStr(int time) {
		// TODO Auto-generated method stub
		int i = time / 1000;
		int hour = i / (60 * 60);
		int minute = i / 60 % 60;
		int second = i % 60;

		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	/**
	 * 控制视频的播放和暂停，及缓存机制
	 */
	public void handlerVideoStartUpadte() {
		// TODO Auto-generated method stub
		if (videoPlayer == null) {
			return;
		}

		int position = videoPlayer.getCurrentPosition();
		final int seekPosition = seekBar.getProgress();
		// 有拖动情况未超过已缓存值取进度条进度值，未拖动过或者超过已缓存值取播放的当前值
		position = position > seekPosition ? position : seekPosition;

		cachePercent = (int) ((videoCacheSize * duration) / (videoTotalSize == 0 ? 1
				: videoTotalSize));
		seekBar.setSecondaryProgress(cachePercent);

		if (videoPlayer.isPlaying()) {
			// 当前播放时间
			currentTime.setText(transTimeToStr(position));
			// 当前播放进度
			seekBar.setProgress(position);

			// 检测2秒之后的数据是否有缓冲完成
			int next1sec = position + 1 * 1000;
			if (next1sec > duration) {
				next1sec = duration;
			}
			if (downloader.isInitComplete()
					&& !downloader.checkIsBuffered(next1sec / 1000)) {// 缓冲未完成
				// 从未完成处开始下载
				downloader.seekLoadVideo(cachePercent / 1000);
				// 暂停播放并且提示等待
				videoPlayer.pause();
				showLoading();
			}
		} else {
			if (!isPaused && isLoading) {
				// 检测5秒之后的数据是否有缓冲完成
				int next5sec = position + 5 * 1000;
				if (next5sec > duration) {
					next5sec = duration;
				}
			 
				if (downloader.isInitComplete()&& downloader.checkIsBuffered(next5sec / 1000)) {
					videoPlayer.seekTo(position);
					// 缓存完成可以播放视频
					videoPlayer.start();
					hideLoading();
					// 播放器播放后才允许seekBar拖动
					isSeekBarCanTouch = true;
				}
			}
		}
		mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
	}

	/**
	 * 缓存完成播放下载到本地的视频
	 */
	private void handlerVideoCacheReady() {
		// TODO Auto-generated method stub
		
		try {
			videoPlayer.setDataSource(localPath);
			videoPlayer.prepareAsync();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 
			if (handlerPrepareExceptionTimes < 5) {
				// 重新发送消息
				mHandler.sendEmptyMessageDelayed(VIDEO_CACHE_READY, 1500);
			} else {
				// 出现异常的次数超过指定的则需要停掉相关联的线程
				isReady = true;
			}
			handlerPrepareExceptionTimes++;
			e.printStackTrace();
		}
	}

	/**
	 * 完成下载的操作
	 */
	private void handlerDownloadFinish() {
		// TODO Auto-generated method stub
		videoCacheSize = videoTotalSize;
	}

	/**
	 * 下载过程中需要的更新操作
	 */
	private void handlerDownloadUpdate(Message msg) {
		// TODO Auto-generated method stub
		final Long lobj = (Long) msg.obj;
		if (lobj > videoCacheSize) {
			videoCacheSize = lobj;
		}
	}

	/**
	 * 视频重新播放操作
	 */
	public void handlerVideoRestart() {
		// TODO Auto-generated method stub
		videoPlayer.seekTo(0);
		seekBar.setProgress(0);
	
		// 初始化下载
		downloader.initVideoDownloader(videoCacheSize, videoTotalSize,
				isFileExist, isDBInfoExist);
		
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

	private static class VideoHandler extends Handler {

		private WeakReference<VideoPlayer> mReference;

		public VideoHandler(VideoPlayer activity) {
			// TODO Auto-generated constructor stub
			mReference = new WeakReference<VideoPlayer>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (mReference.get() != null) {
				VideoPlayer theActivity = mReference.get();
				switch (msg.what) {
				case VIDEO_CACHE_READY:
					// 缓存操作
					theActivity.handlerVideoCacheReady();
					break;
				case VIDEO_STATE_UPDATE:
					// 播放操作
					theActivity.handlerVideoStartUpadte();
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
					theActivity.showErrorDialog(msg.obj.toString());
					break;
				case VideoDownloader.MSG_DOWNLOAD_FINISH:
					// 下载完成操作
					theActivity.handlerDownloadFinish();
					break;
				case VideoDownloader.MSG_DOWNLOAD_UPDATE:
					// 处于下载过程中的操作
					theActivity.handlerDownloadUpdate(msg);
					break;
				}
			}
		}
	}

}
