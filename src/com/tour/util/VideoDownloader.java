package com.tour.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.coremedia.iso.IsoFile;
import com.tour.video.dao.VideoDao;

/**
 * ��Ƶ����
 * 
 * @author wl
 * 
 * @version  20140415
 * 
 */
public class VideoDownloader {

	/* ��5sΪ�ָ�������Ƶ�ֶ� */
	private final int SEP_SECOND = 5;

	public static final int MSG_DOWNLOAD_UPDATE = 0x11;
	public static final int MSG_DOWNLOAD_FINISH = 0x12;

	/* ���صļ���״̬ */
	private final int NOTSTART = 0;
	private final int DOWNLOADING = 1;
	private final int FINISH = 2;

	private Handler mHandler = null;
	private VideoDao db = null;
	private OnVideoDownloadErrorListener errorListener = null;

	private String url, filePath, name;
	// �Ƿ��ʼ�����
	private boolean isInitComplete = false;
	// �Ƿ��һ�ο�������
	private boolean isDownloadStart = false;
	// �������̽�������
	private boolean shutDown = false;
	private int downloadIndex = -1;
	private int seekIndex = -1;

	private final ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
	private final ExecutorService executorService = Executors.newFixedThreadPool(5);

	public VideoDownloader(Context context, Handler handler, String url,
			String localFilePath, String videoName) {
		// TODO Auto-generated constructor stub
		this.db = new VideoDao(context);
		this.mHandler = handler;
		this.url = url;
		this.filePath = localFilePath;
		this.name = videoName;
	}

	/**
	 * ��ʼ���������񣬻�ȡ��Ƶ֡��Ϣ����VideoInfo����5s�ֶ�
	 * 
	 * @param startOffset
	 *            ��Ƶ��ʼλ��
	 * @param totalSize
	 *            ��Ƶ�ܴ�С
	 */
	public void initVideoDownloader(final long startOffset,
			final long totalSize, final boolean isFileExist,
			final boolean isDBInfoExist) {
		// TODO Auto-generated method stub
		shutDown = false;
		isInitComplete = false;

		executorService.submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
			 
				IsoFile isoFile = null;
				try {
					 
					isoFile = new IsoFile(new RandomAccessFile(filePath, "r")
							.getChannel());
					 
				
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
				 
					e.printStackTrace();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
				 
					e.printStackTrace();
					
				}

				if (isoFile == null) {
					 
					return;
				}
				 
				CareyMp4Parser mp4Parser = new CareyMp4Parser(isoFile);
				// mp4Parser.printInfo();
				VideoInfo vi = null;
				videoList.clear();
				for (int i = 0; i < mp4Parser.syncSamples.length; i++) {
					if (vi == null) {
						vi = new VideoInfo();
						vi.timeEnd = mp4Parser.timeOfSyncSamples[i];
						vi.offsetStart = mp4Parser.syncSamplesOffset[i];
					}

					if (mp4Parser.timeOfSyncSamples[i] < (videoList.size() + 1)
							* SEP_SECOND) {
						// δ�ﵽ5��ķֶ��������жϵ���ѭ��
						continue;
					}
					// ��¼�ö���Ƶ������
					vi.offsetEnd = mp4Parser.syncSamplesOffset[i];
					// ���¸ö�
					videoList.add(vi);
					vi = null;
					// ���¸ô�ѭ�������ⶪ֡
					i--;
				}

				if (vi != null) {
					// ��Ƶ�ܴ�С
					vi.offsetEnd = totalSize;
					videoList.add(vi);
					vi = null;
				}

				isInitComplete = true;
				 
				downloadVideo(startOffset, totalSize, isFileExist,isDBInfoExist);
				
			}
		});
	}

	/**
	 * ����ʼ���Ƿ����
	 * 
	 * @return true:���,false:δ���
	 */
	public boolean isInitComplete() {
		// TODO Auto-generated method stub
		return isInitComplete;
	}

	/**
	 * �ж����ز���
	 */
	public void cancelDownload(boolean isSeeked, long videoCacheSize) {
		// TODO Auto-generated method stub
		this.shutDown = true;
		this.executorService.shutdown();
		if (db != null) {
			// δ�϶����ڽ���ʱ��¼����
			if (!isSeeked && videoCacheSize > 0) {
				seekVideoDBInfo(videoCacheSize);
			}
			// �ر����ݿ�
			db.close();
		}
	}

	/**
	 * ������mp4�ļ�ͷ������λ�ô���ʼ������Ƶ
	 * 
	 * @param startOffset
	 *            mp4�ļ�ͷ������ʼλ��
	 */
	private void downloadVideo(long startOffset, long totalSize,
			boolean isFileExist, boolean isDBInfoExist) {
		// TODO Auto-generated method stub
		downloadIndex = 0;

		for (VideoInfo vi : videoList) {
			if (vi.offsetEnd > startOffset) {
				// ���ݽ��������Ƶ�Ͻ���λ�ô��ڼ�¼�Ļ���λ�ã�˵���ö�δ���ػ������ز�����
				break;
			}
			// ���������
			vi.status = FINISH;
			downloadIndex++;
		}

		if (!isFileExist) {
			// ���ݿⲻ���ڼ�¼��������¼
			if (!isDBInfoExist) {
				db.add(name, startOffset, totalSize);
			}
			// �ļ������ڲ���Ҫ����
			download();
		}
	}

	/**
	 * �����ļ�
	 */
	private void download() {
		// TODO Auto-generated method stub
		// ��������
		isDownloadStart = true;
		while (!isAllFinished()) {
			// �ж���Ƶֹͣ����
			if (shutDown || downloadIndex >= videoList.size()) {
			 
				break;
			}

			/*
			 * ���Ҫ�Զ������������ļ��ø��㷨[downloadIndex %= videoList.size()]
			 * ���㷨�Ǵ�startOffset��һֱ��������Ϊֹ
			 */
			VideoInfo vi = this.videoList.get(downloadIndex);
			if (vi.status == NOTSTART) {
				try {
					vi.status = DOWNLOADING;
					downloadByVideoInfo(vi);
					vi.status = FINISH;
					downloadIndex++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					vi.status = NOTSTART;
					if (errorListener != null) {
						errorListener.onError();
					}
					e.printStackTrace();
				}
			}
		}
		mHandler.sendEmptyMessage(MSG_DOWNLOAD_FINISH);
		// �������
		isDownloadStart = false;
	}

	/**
	 * ����Ƿ�ȫ����Ƶģ�鶼���������
	 */
	private boolean isAllFinished() {
		// TODO Auto-generated method stub
		for (VideoInfo vi : videoList) {
			if (vi.status != FINISH) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ���ָ��ʱ�����Ƶ�Ƿ��Ѿ��������
	 * 
	 * @param time
	 *            ָ����ʱ��
	 * @return {�Ƿ񻺴����,�Ƿ��Ǵ�������״̬}
	 */
	public boolean checkIsBuffered(long time) {
		// �����±��Ǵ�0��ʼ��
		int index = -1;

		for (VideoInfo vi : videoList) {
			if (vi.timeEnd > time) {
				break;
			}
			index++;
		}

		if (index < 0 || index >= videoList.size()) {
			return true;
		}

		final VideoInfo vi = videoList.get(index);
		if (vi.status == FINISH) {
			return true;
		} else if (vi.status == NOTSTART) {
			return false;
		} else if (vi.status == DOWNLOADING) {
			// return (vi.downloadSize * 100 / (vi.offsetEnd - vi.offsetStart))
			// > (((time - vi.timeStart) * 100 / SEP_SECOND));
			return false;
		}

		return true;
	}

	/**
	 * ����VideoInfo����������Ƶ�����ظö���Ƶ
	 * 
	 * @param vi
	 * @throws IOException
	 */
	private void downloadByVideoInfo(VideoInfo vi) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("download -> " + vi.toString());

		URL url = new URL(this.url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(20000);
		conn.setRequestProperty("Range", "bytes=" + vi.offsetStart + "-"
				+ vi.offsetEnd);

		RandomAccessFile raf = new RandomAccessFile(new File(filePath), "rws");
		raf.seek(vi.offsetStart);

		InputStream is = conn.getInputStream();
		vi.downloadSize = 0;

		byte[] buf = new byte[1024 * 10];
		int len;
		while ((len = is.read(buf)) != -1) {
			if (shutDown) {
				break;
			}
			raf.write(buf, 0, len);
			vi.downloadSize += len;
			Message msg = mHandler.obtainMessage();
			msg.what = MSG_DOWNLOAD_UPDATE;
			msg.obj = vi.offsetStart + vi.downloadSize;
			mHandler.sendMessage(msg);
		}

		is.close();
		raf.close();
	}

	/**
	 * ���ص�time�뿪ʼ��������
	 */
	public synchronized void seekLoadVideo(long time) {
		// �����±��Ǵ�0��ʼ��
		seekIndex = -1;

		for (VideoInfo vi : videoList) {
			if (vi.timeEnd > time) {
				break;
			}
			seekIndex++;
		}
		if (seekIndex < 0 || seekIndex >= videoList.size()) {
			return;
		}

		final VideoInfo vi = videoList.get(seekIndex);
		if (vi.status == NOTSTART) {
			executorService.submit(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						vi.status = DOWNLOADING;
						downloadByVideoInfo(vi);
						vi.status = FINISH;

						downloadIndex = seekIndex;
						if (!isDownloadStart) {
							// ��time�����ڵĶ��Ѿ�������ɣ����¶ο�ʼ����
							downloadIndex++;
							download();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						vi.status = NOTSTART;
						if (errorListener != null) {
							errorListener.onError();
						}
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * ��ѯ��Ƶ�ѻ�����Ϣ
	 * 
	 * @param videoName
	 *            ��Ƶ����
	 * @return {�Ƿ��иü�¼(0��ʾû�У�1��ʾ��),�����С,�ܴ�С,�ѻ��洦��Ӧ��ʱ��
	 */
	public long[] queryVideoDBInfo(String videoName) {
		return db.query(videoName);
	}

	/**
	 * ������Ƶ������Ϣ
	 * 
	 * @param videoCacheSize
	 *            ��ǰ����
	 */
	public void seekVideoDBInfo(long videoCacheSize) {
		db.update(name, videoCacheSize);
	}

	/**
	 * ע����Ƶ���ش���ӿ�
	 * 
	 * @param listener
	 *            VideoDownloadErrorListener
	 */
	public void setOnVideoDownloadErrorListener(
			OnVideoDownloadErrorListener listener) {
		this.errorListener = listener;
	}

	/**
	 * ��ȡ��Ƶ��Ϣ
	 */
	class VideoInfo {
		// ��Ƶ����ʱ��
		double timeEnd;
		// ��Ƶ��ʼλ��
		long offsetStart;
		// ��Ƶ����λ��
		long offsetEnd;
		// ��Ƶ�����ظöϵĴ�С
		long downloadSize;
		// ��Ƶ����״̬
		int status;

		public VideoInfo() {
			status = NOTSTART;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			String s = "EndTime: <" + timeEnd + ">, fileOffset(" + offsetStart
					+ " -> " + offsetEnd + "), isFinish: " + status;

			return s;
		}
	}

	/**
	 * ��Ƶ���ش���ӿ�
	 */
	public interface OnVideoDownloadErrorListener {
		void onError();
	}
}
