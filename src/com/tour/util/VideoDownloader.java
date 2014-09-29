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
 * 视频下载
 * 
 * @author wl
 * 
 * @version  20140415
 * 
 */
public class VideoDownloader {

	/* 以5s为分割点进行视频分段 */
	private final int SEP_SECOND = 5;

	public static final int MSG_DOWNLOAD_UPDATE = 0x11;
	public static final int MSG_DOWNLOAD_FINISH = 0x12;

	/* 下载的几种状态 */
	private final int NOTSTART = 0;
	private final int DOWNLOADING = 1;
	private final int FINISH = 2;

	private Handler mHandler = null;
	private VideoDao db = null;
	private OnVideoDownloadErrorListener errorListener = null;

	private String url, filePath, name;
	// 是否初始化完成
	private boolean isInitComplete = false;
	// 是否第一次开启下载
	private boolean isDownloadStart = false;
	// 允许立刻结束下载
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
	 * 初始化下载任务，获取视频帧信息存入VideoInfo，以5s分段
	 * 
	 * @param startOffset
	 *            视频起始位置
	 * @param totalSize
	 *            视频总大小
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
						// 未达到5秒的分段条件，中断当次循环
						continue;
					}
					// 记录该段视频结束点
					vi.offsetEnd = mp4Parser.syncSamplesOffset[i];
					// 存下该段
					videoList.add(vi);
					vi = null;
					// 重新该次循环，以免丢帧
					i--;
				}

				if (vi != null) {
					// 视频总大小
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
	 * 检测初始化是否完成
	 * 
	 * @return true:完成,false:未完成
	 */
	public boolean isInitComplete() {
		// TODO Auto-generated method stub
		return isInitComplete;
	}

	/**
	 * 中断下载操作
	 */
	public void cancelDownload(boolean isSeeked, long videoCacheSize) {
		// TODO Auto-generated method stub
		this.shutDown = true;
		this.executorService.shutdown();
		if (db != null) {
			// 未拖动过在结束时记录缓存
			if (!isSeeked && videoCacheSize > 0) {
				seekVideoDBInfo(videoCacheSize);
			}
			// 关闭数据库
			db.close();
		}
	}

	/**
	 * 下载完mp4文件头部数据位置处开始下载视频
	 * 
	 * @param startOffset
	 *            mp4文件头处的起始位置
	 */
	private void downloadVideo(long startOffset, long totalSize,
			boolean isFileExist, boolean isDBInfoExist) {
		// TODO Auto-generated method stub
		downloadIndex = 0;

		for (VideoInfo vi : videoList) {
			if (vi.offsetEnd > startOffset) {
				// 依据解析后该视频断结束位置大于记录的缓存位置，说明该段未下载或者下载不完整
				break;
			}
			// 标记已下载
			vi.status = FINISH;
			downloadIndex++;
		}

		if (!isFileExist) {
			// 数据库不存在记录则新增记录
			if (!isDBInfoExist) {
				db.add(name, startOffset, totalSize);
			}
			// 文件不存在才需要下载
			download();
		}
	}

	/**
	 * 下载文件
	 */
	private void download() {
		// TODO Auto-generated method stub
		// 开启下载
		isDownloadStart = true;
		while (!isAllFinished()) {
			// 中断视频停止下载
			if (shutDown || downloadIndex >= videoList.size()) {
			 
				break;
			}

			/*
			 * 如果要自动下载完整的文件用该算法[downloadIndex %= videoList.size()]
			 * 该算法是从startOffset后一直到下载完为止
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
		// 下载完成
		isDownloadStart = false;
	}

	/**
	 * 检测是否全部视频模块都已下载完毕
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
	 * 检测指定时间的视频是否已经缓存好了
	 * 
	 * @param time
	 *            指定的时间
	 * @return {是否缓存完成,是否是处于下载状态}
	 */
	public boolean checkIsBuffered(long time) {
		// 数组下标是从0开始的
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
	 * 根据VideoInfo所解析的视频段下载该段视频
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
	 * 加载第time秒开始的数据流
	 */
	public synchronized void seekLoadVideo(long time) {
		// 数组下标是从0开始的
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
							// 第time秒所在的段已经下载完成，从下段开始下载
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
	 * 查询视频已缓存信息
	 * 
	 * @param videoName
	 *            视频名称
	 * @return {是否有该记录(0表示没有，1表示有),缓存大小,总大小,已缓存处对应的时间
	 */
	public long[] queryVideoDBInfo(String videoName) {
		return db.query(videoName);
	}

	/**
	 * 插入视频缓存信息
	 * 
	 * @param videoCacheSize
	 *            当前缓存
	 */
	public void seekVideoDBInfo(long videoCacheSize) {
		db.update(name, videoCacheSize);
	}

	/**
	 * 注册视频下载错误接口
	 * 
	 * @param listener
	 *            VideoDownloadErrorListener
	 */
	public void setOnVideoDownloadErrorListener(
			OnVideoDownloadErrorListener listener) {
		this.errorListener = listener;
	}

	/**
	 * 获取视频信息
	 */
	class VideoInfo {
		// 视频结束时间
		double timeEnd;
		// 视频起始位置
		long offsetStart;
		// 视频结束位置
		long offsetEnd;
		// 视频已下载该断的大小
		long downloadSize;
		// 视频下载状态
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
	 * 视频下载错误接口
	 */
	public interface OnVideoDownloadErrorListener {
		void onError();
	}
}
