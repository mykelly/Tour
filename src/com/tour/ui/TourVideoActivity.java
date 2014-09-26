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
public class TourVideoActivity extends NotTitleActivity {
	SurfaceHolder surfaceHolder;
	private ProgressBar progressBar;
	private static SeekBar seekBar;
	private SurfaceView surfaceView;
	// ���ſ�����
	private LinearLayout playControl;
	private ImageButton playBtn;
	private static TextView currentTime;
	private TextView totalTime;
	VideoView videoView = null;
	Button pause;
	Button play;
	static MediaPlayer videoPlayer;
	// �Ƿ���ͣ
	private static boolean isPaused = false;
	// �Ƿ��ѽ���ҳ��
	private boolean isFinish = false;
	// �Ƿ񲥷����
	private boolean isPlayComplete = false;
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
	private IntentFilter videoFilter = null;
	// ��Ƶ��ʱ��
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
									// ����ļ���
									videoName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.length());
								}
						String path = getIntent().getExtras().getString("VideoPath");
								if (path != null) {
									// ��ô洢·��
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
								// ����home��
								videoFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
								// ������Դ��
								videoFilter.addAction(Intent.ACTION_SCREEN_OFF);
								registerReceiver(systemStateReceiver, videoFilter);
								 }else{
										Toast.makeText(getApplicationContext(), "��Ƶ�ļ������ڣ��޷�������Ƶ��",Toast.LENGTH_LONG).show();
										finish();
								 }
					} else {
						Toast.makeText(getApplicationContext(), "�����ֻ�δ��װSD�����޷�������Ƶ��",Toast.LENGTH_LONG).show();
						finish();
					}
		} else {
			Toast.makeText(getApplicationContext(), "���ص�ַ�쳣���޷�������Ƶ��",Toast.LENGTH_LONG).show();//û�д��벥�ŵ�ַ���߸õ�ַû����Ƶ
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
				System.out.println("��ʼ������Ƶ");
				videoPlayer.start();
				duration = videoPlayer.getDuration();
				seekBar.setMax(duration);
				// ��Ƶ��ʱ��
				totalTime.setText(transTimeToStr(duration));
				mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
			}
		});
		videoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				System.out.println("��Ƶ�e�`");
				return false;
			}
		});
		videoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				System.out.println("��Ƶ�������");
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
				// ��ʼ����������true����������false����
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
				System.out.println("��Ƶ������ͣ");
				break;
			case R.id.play:
				videoPlayer.start();
				System.out.println("��Ƶ���ſ�ʼ");
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
	 * ������Ƶ������
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
		// ������׼�����
		videoPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				 
					 
					// �������ز��ſ�������Ϣ
					sendHideControlMsg();
                					 
					duration = videoPlayer.getDuration();
					seekBar.setMax(duration);
					// ��Ƶ��ʱ��
					totalTime.setText(transTimeToStr(duration));
					// ����UI״̬
					mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
					videoPlayer.start();
				 
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
//		playlocalVideo();
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
	 
		if (videoPlayer != null) {
			videoPlayer.stop();
		}
		 finish();
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
	 * ������ֵ����Ϊʱ��
	 * 
	 * @param time
	 *            ����ֵ
	 * @return ʱ��
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
					// �������
					 
					break;
				case VIDEO_STATE_UPDATE:
					// ���Ų���
					int position = videoPlayer.getCurrentPosition();
					final int seekPosition = seekBar.getProgress();
					// ���϶����δ�����ѻ���ֵȡ����������ֵ��δ�϶������߳����ѻ���ֵȡ���ŵĵ�ǰֵ
					position = position > seekPosition ? position : seekPosition;
					if (videoPlayer.isPlaying()) {
						// ��ǰ����ʱ��
						currentTime.setText(transTimeToStr(position));
						// ��ǰ���Ž���
						seekBar.setProgress(position);						 
					} else {
						if (!isPaused ) {
						videoPlayer.seekTo(position);
						// ������ɿ��Բ�����Ƶ
						videoPlayer.start();
						}
						}
					mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
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
//					theActivity.showErrorDialog(msg.obj.toString());
					break;
				case VideoDownloader.MSG_DOWNLOAD_FINISH:
					// ������ɲ���
//					theActivity.handlerDownloadFinish();
					break;
				case VideoDownloader.MSG_DOWNLOAD_UPDATE:
					// �������ع����еĲ���
//					theActivity.handlerDownloadUpdate(msg);
					break;
				}
			}
		}
	}
	/**
	 * ��Ƶ���²��Ų���
	 */
	public void handlerVideoRestart() {
		// TODO Auto-generated method stub
		videoPlayer.seekTo(0);
		seekBar.setProgress(0);
	 
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
