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
 * ��Ƶ������ </p>
 * 
 * ��ת�����Я����Ƶ��ַ����ƵSD��·��������</br>
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
	// ��ʼ�����С,��ǰ��ʾ3%
	private final int READY_BUFF = 10;
	// �Զ����ز��ſ�������ʱ��
	private final int HIDE_CONTROL_TIME = 5000;
	// ��Ƶ����״̬
	private static final int VIDEO_STATE_UPDATE = 0;
	// ��Ƶ�������
	private static final int VIDEO_CACHE_READY = 1;
	// ��ʾ���ſ�����
	private static final int VIDEO_SHOW_CONTROL = 2;
	// ���²�����Ƶ
	private static final int VIDEO_RESTART = 3;
	// ���²�����Ƶ
	private static final int VIDEO_DOWNLOAD_ERROR = 4;

	private ProgressBar progressBar;
	private SeekBar seekBar;
	private SurfaceView surfaceView;
	// ���ſ�����
	private LinearLayout playControl;
	private ImageButton playBtn;
	private TextView currentTime, totalTime;
	// ��Ƶ�����ַ
	private String videoUrl = null;
	// ��Ƶ����
	private String videoName = null;
	// ������Ƶ���·��
	private String localPath = null;

	private MediaPlayer videoPlayer = null;
	private IntentFilter videoFilter = null;
	private VideoHandler mHandler = null;
	private VideoDownloader downloader = null;
	// ��Ƶ�ܴ�С
	private long videoTotalSize = 0;
	// ��Ƶ��ǰ���ѻ����С
	private long videoCacheSize = 0;
	// ׼��������Ƶ���ֵ��쳣����ͳ��
	private int handlerPrepareExceptionTimes = 0;
	// �Ѿ����ؽ���
	private int cachePercent = 0;
	// ��Ƶ��ʱ��
	private int duration = 0;
	// �Ƿ�׼�����
	private boolean isReady = false;
	// �Ƿ���Ҫ�ȴ��������
	private boolean isLoading = false;
	// �Ƿ���ͣ
	private boolean isPaused = false;
	// �Ƿ��϶���
	private boolean isSeeked = false;
	// �Ƿ��ѽ���ҳ��
	private boolean isFinish = false;
	// �Ƿ�����seekBar��������
	private boolean isSeekBarCanTouch = false;
	// �ļ��Ƿ����
	private boolean isFileExist = false;
	// ���ݿ��Ƿ��и���Ƶ��¼
	private boolean isDBInfoExist = false;
	// �Ƿ񲥷����
	private boolean isPlayComplete = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ������
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.videoplayer);
		
		if (getIntent().getExtras() != null
				&& !getIntent().getExtras().isEmpty()) {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				videoUrl = getIntent().getExtras().getString(VIDEO_URL);
				
				if (videoUrl != null) {
					// ����ļ���
					videoName = videoUrl.substring(
							videoUrl.lastIndexOf("/") + 1, videoUrl.length());
				}
				String path = getIntent().getExtras().getString(VIDEO_PATH);
				if (path != null) {
					// ��ô洢·��
					localPath = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + path + videoName;
				}
				 File  dir = new File(path);					       
				    if(!dir.exists())
				        dir.mkdirs();  //����������򴴽�
			} else {
				Toast.makeText(getApplicationContext(), "�����ֻ�δ��װSD�����޷�������Ƶ��",
						Toast.LENGTH_LONG).show();
				finish();
			}
//			localPath="/storage/sdcard0/DaMeiTour/video/13031403002735tq.mp4";
//			System.out.println("���߲�����ƵvideoUrl="+videoUrl);
//			System.out.println("localPath="+localPath);		

			mHandler = new VideoHandler(this);
			// ��ʼ������
			init();
				
//			File file = new File(localPath);
//			 if(file.exists()){
//				playlocalVideo();//�ļ����ڲ��ű�����Ƶ
//			}else{
				// ������Ƶ
				playVideo();
//			}
			TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			manager.listen(videoPhoneStateListener,
					PhoneStateListener.LISTEN_CALL_STATE);

			videoFilter = new IntentFilter();
			// ����home��
			videoFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			// ������Դ��
			videoFilter.addAction(Intent.ACTION_SCREEN_OFF);
			registerReceiver(systemStateReceiver, videoFilter);
		} else {
			Toast.makeText(getApplicationContext(), "���ص�ַ�쳣���޷�������Ƶ��",Toast.LENGTH_LONG).show();//û�д��벥�ŵ�ַ���߸õ�ַû����Ƶ
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
		// �ϵ������߳�
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
	 * ��ʼ����Ƶ�������
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
				// ��ʼ����������true����������false����
				return !isSeekBarCanTouch;
			}
		});
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			boolean theFirst = true;
			// ��¼�ϵ㿪��
			boolean setCurrentCache = true;
			// ��¼�ϵ�λ�ã��Ժ��ٷ�������
			long currentCachePercent = 0;

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				showLoading();

				final int seekPosition = seekBar.getProgress();
				if (setCurrentCache) {
					currentCachePercent = cachePercent;
				}
				// �û��϶������ֻ��¼�϶�ǰ�Ļ���
				if (seekPosition > currentCachePercent) {
					if (theFirst) {
						theFirst = false;
						isSeeked = true;
						setCurrentCache = false;
						downloader.seekVideoDBInfo(videoCacheSize);
					}
					downloader.seekLoadVideo(seekPosition / 1000);
				} else {
					// ���ý���ֵ����֤seekBar�Ľ��ȸ���(���ѻ�����ȷ�Χ����Ч)
					videoPlayer.seekTo(seekPosition);
					int next5sec = seekPosition + 5 * 1000;
					if (next5sec > duration) {
						next5sec = duration;
					}
					// �ж�5������Ƶ�����Ƿ����
					if (downloader.isInitComplete()
							&& !downloader.checkIsBuffered(next5sec / 1000)) {
						// ��δ��ɴ���ʼ����
						downloader.seekLoadVideo(cachePercent / 1000);
					}
				}

				isPaused = false;
				// �������ز��ſ�������Ϣ
				sendHideControlMsg();
				// ����UI״̬
				mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				playBtn.setImageResource(R.drawable.video_btn_play);
				videoPlayer.pause();
				isPaused = true;
				// �Ƴ����ز��ſ������Ĳ���
				removeHideControlMsg();
				// ֹͣ����UI
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
				// ��ָ�����¼�
				case MotionEvent.ACTION_DOWN:
					// �Ƴ����ز��ſ������Ĳ���
					removeHideControlMsg();
					// ��ʾ���ſ�����
					showPlayControl();
					return true;
					// ��ָ�뿪�¼�
				case MotionEvent.ACTION_UP:
					// �������ز��ſ�������Ϣ
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
				// ��ָ�����¼�
				case MotionEvent.ACTION_DOWN:
					// �Ƴ����ز��ſ������Ĳ���
					removeHideControlMsg();
					return true;
					// ��ָ�뿪�¼�
				case MotionEvent.ACTION_UP:
					// �������ز��ſ�������Ϣ
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
						sendErrorMsg("���ٲ������������Ժ����ԣ�");
					}
				});
		
		
	}
	private void playlocalVideo(){
//		if (!isNetWork) {//���ű�����Ƶ			 
//		System.out.println("���ű�����ƵlocalPath="+localPath);	
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
	 * ��ʼ��Ƶ����
	 */
	private void playVideo() {
		// TODO Auto-generated method stub
	
		if (!URLUtil.isNetworkUrl(videoUrl)) {
			try {
				// ���õ�ַ�Ǳ�׼�����ַ�������Ϊ����·���ȣ���ֱ�Ӳ�����Ƶ
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
					sendErrorMsg("�����쳣�����Ժ����ԣ�");
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * ��ʼ���ŵ�׼���׶�
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
				// ���ݿ��¼�Ļ����С
				videoCacheSize = videoInfo[1];
				// ���ݿ��¼���ܴ�С
				videoTotalSize = videoInfo[2];
				mHandler.sendEmptyMessageDelayed(VIDEO_CACHE_READY, 1000);
				// ���ݿ��м�¼�����ļ�����ֱ�Ӳ���
				return;
			} else {
				// �ļ����ڵ������ݿ��¼ɾ��
				cacheFile.delete();
				// ״̬�޸�Ϊ������
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
		// �������ļ���Ŀ¼
		cacheFile.createNewFile();
		RandomAccessFile raf = new RandomAccessFile(cacheFile, "rws");
		raf.setLength(videoTotalSize);
		raf.seek(0);

		videoCacheSize = 0;
		byte buf[] = new byte[10 * 1024];
		int len = 0;
		boolean b = true;
		// �������Ƶ��Ҫ����Ĵ�С
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
				// �ﵽ����ֻ�뷢��һ����Ϣ
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
	 * ������Ƶ������
	 */
	private void createVideoPlayer(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (videoPlayer == null) {
			videoPlayer = new MediaPlayer();
		}

		videoPlayer.reset();
		videoPlayer.setDisplay(holder);
		
		// ������׼�����
		videoPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if (!isReady) {
					isReady = true;
					// �������ز��ſ�������Ϣ
					sendHideControlMsg();
                
					// ��ʼ������
					downloader.initVideoDownloader(videoCacheSize,
							videoTotalSize, isFileExist, isDBInfoExist);

					duration = videoPlayer.getDuration();
					seekBar.setMax(duration);
					// ��Ƶ��ʱ��
					totalTime.setText(transTimeToStr(duration));
					// ����UI״̬
					mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
				
				}
			}
		});

		// �������������
		videoPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.pause();
				// ���Ƴ����ز��ſ������Ĳ���
				removeHideControlMsg();
				// ��ʾ���ſ�����
				showPlayControl();

				final int duration = videoPlayer.getDuration();
				currentTime.setText(transTimeToStr(duration));
				// ���������������ʱ��
				seekBar.setProgress(duration);
				isPlayComplete = true;
				showDialog();
			}
		});

		// ���������ų���
		videoPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				// ֹͣ����UI
				removeVideoUpadteMsg();
				// ��ʾ����
				sendErrorMsg("��Ǹ����Ƶ���ų���");
				mp.stop();
				mp.reset();
				return true;
			}
		});

//		File file = new File(localPath);
//		 if(file.exists()){
//			playlocalVideo();//�ļ����ڲ��ű�����Ƶ
//		} 
		
	}

	/**
	 * ���ͳ�����Ϣ
	 * 
	 * @param msg
	 *            ������Ϣ
	 */
	private void sendErrorMsg(String msg) {
		Message message = mHandler.obtainMessage(VIDEO_DOWNLOAD_ERROR, msg);
		mHandler.sendMessage(message);
	}

	/**
	 * ������϶Ի���
	 */
	private void showDialog() {
		// TODO Auto-generated method stub
		removeVideoUpadteMsg();
		if (isFinish) {
			return;
		}
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setMessage("��Ƶ�������")
				.setPositiveButton("�ز�", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						showLoading();
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (!isSeeked) {
									// δ����������ǰ�ѻ����С���϶�������������ݿ�
									downloader.seekVideoDBInfo(videoCacheSize);
								}
								long videoInfo[] = downloader
										.queryVideoDBInfo(videoName);
								// ���ݿ��¼�Ļ����С
								videoCacheSize = videoInfo[1];
								// �����ز�˵���ļ��Ѿ�����
								isFileExist = true;
								// �����ز�˵�����ݿ��Ѿ��м�¼
								isDBInfoExist = true;
								mHandler.sendEmptyMessage(VIDEO_RESTART);
							}
						}).start();
						dialog.dismiss();
					}
				})
				.setNegativeButton("�ر�", new DialogInterface.OnClickListener() {

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
	 * ���ų���Ի���
	 */
	private void showErrorDialog(String msg) {
		// TODO Auto-generated method stub
		if (isFinish) {
			return;
		}
		Dialog alertDialog = new AlertDialog.Builder(this).setMessage(msg)
				.setNegativeButton("�ر�", new DialogInterface.OnClickListener() {

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
	 * ����ҳ��</p> ������Ƶ����״̬�㲥</p>
	 * intent.getBooleanExtra(<b>VIDEO_PLAY_COMPLETE</b>)���<b>isPlayComplete
	 * </b>�Ƿ񲥷���ϵ�ֵ
	 * 
	 */
	private void finishActivity() {
		Intent intent = new Intent(VIDEO_PLAY_ACTION);
		intent.putExtra(VIDEO_PLAY_COMPLETE, isPlayComplete);
		// ���͹㲥�����ⲿ����
		sendBroadcast(intent);

		if (videoPlayer != null) {
			videoPlayer.stop();
		}
		VideoPlayer.this.finish();
	}

	/**
	 * ��ʾ�ȴ���
	 */
	private void showLoading() {
		// TODO Auto-generated method stub
		isLoading = true;
		progressBar.setVisibility(View.VISIBLE);
		playBtn.setImageResource(R.drawable.video_btn_play);
	}

	/**
	 * ���صȴ���
	 */
	private void hideLoading() {
		// TODO Auto-generated method stub
		isLoading = false;
		progressBar.setVisibility(View.GONE);
		playBtn.setImageResource(R.drawable.video_btn_pause);
	}

	/**
	 * ��ʾ���ſ�����
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
	 * ���ز��ſ�����
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
	 * ������Ϣ��ʾ���ſ���̨
	 */
	private void sendHideControlMsg() {
		Message msg = mHandler.obtainMessage(VIDEO_SHOW_CONTROL);
		mHandler.sendMessageDelayed(msg, HIDE_CONTROL_TIME);
	}

	/**
	 * �Ƴ���Ϊ��ʱ����δִ�е����ز��ſ���̨�������Ϣ
	 */
	private void removeHideControlMsg() {
		// �ȼ���Ƿ���ڸ���Ϣ
		if (mHandler.hasMessages(VIDEO_SHOW_CONTROL)) {
			mHandler.removeMessages(VIDEO_SHOW_CONTROL);
		}
	}

	/**
	 * ֹͣ����UI����ʱ�䡢��������
	 */
	private void removeVideoUpadteMsg() {
		if (mHandler != null && mHandler.hasMessages(VIDEO_STATE_UPDATE)) {
			mHandler.removeMessages(VIDEO_STATE_UPDATE);
		}
	}

	/**
	 * �Ƴ�������Ϣ
	 */
	private void removeVideoErrorMsg() {
		if (mHandler != null && mHandler.hasMessages(VIDEO_DOWNLOAD_ERROR)) {
			mHandler.removeMessages(VIDEO_DOWNLOAD_ERROR);
		}
	}

	/**
	 * ������ֵ����Ϊʱ��
	 * 
	 * @param time
	 *            ����ֵ
	 * @return ʱ��
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
	 * ������Ƶ�Ĳ��ź���ͣ�����������
	 */
	public void handlerVideoStartUpadte() {
		// TODO Auto-generated method stub
		if (videoPlayer == null) {
			return;
		}

		int position = videoPlayer.getCurrentPosition();
		final int seekPosition = seekBar.getProgress();
		// ���϶����δ�����ѻ���ֵȡ����������ֵ��δ�϶������߳����ѻ���ֵȡ���ŵĵ�ǰֵ
		position = position > seekPosition ? position : seekPosition;

		cachePercent = (int) ((videoCacheSize * duration) / (videoTotalSize == 0 ? 1
				: videoTotalSize));
		seekBar.setSecondaryProgress(cachePercent);

		if (videoPlayer.isPlaying()) {
			// ��ǰ����ʱ��
			currentTime.setText(transTimeToStr(position));
			// ��ǰ���Ž���
			seekBar.setProgress(position);

			// ���2��֮��������Ƿ��л������
			int next1sec = position + 1 * 1000;
			if (next1sec > duration) {
				next1sec = duration;
			}
			if (downloader.isInitComplete()
					&& !downloader.checkIsBuffered(next1sec / 1000)) {// ����δ���
				// ��δ��ɴ���ʼ����
				downloader.seekLoadVideo(cachePercent / 1000);
				// ��ͣ���Ų�����ʾ�ȴ�
				videoPlayer.pause();
				showLoading();
			}
		} else {
			if (!isPaused && isLoading) {
				// ���5��֮��������Ƿ��л������
				int next5sec = position + 5 * 1000;
				if (next5sec > duration) {
					next5sec = duration;
				}
			 
				if (downloader.isInitComplete()&& downloader.checkIsBuffered(next5sec / 1000)) {
					videoPlayer.seekTo(position);
					// ������ɿ��Բ�����Ƶ
					videoPlayer.start();
					hideLoading();
					// ���������ź������seekBar�϶�
					isSeekBarCanTouch = true;
				}
			}
		}
		mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
	}

	/**
	 * ������ɲ������ص����ص���Ƶ
	 */
	private void handlerVideoCacheReady() {
		// TODO Auto-generated method stub
		
		try {
			videoPlayer.setDataSource(localPath);
			videoPlayer.prepareAsync();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 
			if (handlerPrepareExceptionTimes < 5) {
				// ���·�����Ϣ
				mHandler.sendEmptyMessageDelayed(VIDEO_CACHE_READY, 1500);
			} else {
				// �����쳣�Ĵ�������ָ��������Ҫͣ����������߳�
				isReady = true;
			}
			handlerPrepareExceptionTimes++;
			e.printStackTrace();
		}
	}

	/**
	 * ������صĲ���
	 */
	private void handlerDownloadFinish() {
		// TODO Auto-generated method stub
		videoCacheSize = videoTotalSize;
	}

	/**
	 * ���ع�������Ҫ�ĸ��²���
	 */
	private void handlerDownloadUpdate(Message msg) {
		// TODO Auto-generated method stub
		final Long lobj = (Long) msg.obj;
		if (lobj > videoCacheSize) {
			videoCacheSize = lobj;
		}
	}

	/**
	 * ��Ƶ���²��Ų���
	 */
	public void handlerVideoRestart() {
		// TODO Auto-generated method stub
		videoPlayer.seekTo(0);
		seekBar.setProgress(0);
	
		// ��ʼ������
		downloader.initVideoDownloader(videoCacheSize, videoTotalSize,
				isFileExist, isDBInfoExist);
		
		// �������ز��ſ�������Ϣ
		sendHideControlMsg();
		// ����UI״̬
		mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
	}

	// �������
	private final PhoneStateListener videoPhoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			if (videoPlayer != null) {
				if (state == TelephonyManager.CALL_STATE_RINGING) {
					// ����ʱ
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
					// �������
					theActivity.handlerVideoCacheReady();
					break;
				case VIDEO_STATE_UPDATE:
					// ���Ų���
					theActivity.handlerVideoStartUpadte();
					break;
				case VIDEO_SHOW_CONTROL:
					// ִ�����ز��ſ���̨����
					theActivity.hidePlayControl();
					break;
				case VIDEO_RESTART:
					// ��Ƶ���²��Ų���
					theActivity.handlerVideoRestart();
					break;
				case VIDEO_DOWNLOAD_ERROR:
					// ��Ƶ���س������
					theActivity.showErrorDialog(msg.obj.toString());
					break;
				case VideoDownloader.MSG_DOWNLOAD_FINISH:
					// ������ɲ���
					theActivity.handlerDownloadFinish();
					break;
				case VideoDownloader.MSG_DOWNLOAD_UPDATE:
					// �������ع����еĲ���
					theActivity.handlerDownloadUpdate(msg);
					break;
				}
			}
		}
	}

}
