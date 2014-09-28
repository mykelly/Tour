package com.tour.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipException;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tour.R;
import com.tour.SQLite.DBTour;
import com.tour.SQLite.TourData;
import com.tour.adpater.RollListAdpater;
import com.tour.dao.DataJsonParser;
import com.tour.dao.HttpAsynTask;
import com.tour.encoder.PostUtil;
import com.tour.listener.HttpCallBack;
import com.tour.util.DownloadZip;
import com.tour.util.PublicData;
import com.tour.util.SaveDataClass;
import com.tour.util.UnZipFile;
import com.tour.view.WaitDialog;

/**
 * 
 * @author ly 团列表
 * @param <RollListAdpater>
 */
public class RollListActivity extends NotTitleActivity implements HttpCallBack{
	private Button bt_next;
	private ImageButton ib_fresh,ib_exit;
	private TextView mTitle;
	private WaitDialog dialog;// “等待”对话框
	private ListView rollList;
	private RollListAdpater rollAdapter;
	private HttpAsynTask httpAsynTask;
	public static Handler mHandler;
	private String uid="1",tip="";
	private	List<HashMap<String, List<HashMap<String, String>>>> datalist = new ArrayList<HashMap<String, List<HashMap<String, String>>>>();
	private RelativeLayout relyt_download;
	private LinearLayout lilyt_download;
	private	TextView tv_tip; // 提示
	private	TextView tv_mDate; // 日期
	private	TextView tv_mTitle; // 团名
	private	TextView tv_mNumber;
	private	TextView tv_mDowanLoad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tour_roll_list);
		creatUi();
		setData();
    
		
		
		 mHandler=new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					switch (msg.what) {
					case 0:
						Toast.makeText(getApplicationContext(), tip, 1000).show();
						break;

					case 1:
						PublicData.tour_id=datalist.get(0).get("tour_id").get(0).get("tour_id");
						PublicData.tour_title=datalist.get(0).get("tour_title").get(0).get("tour_title");
						PublicData.tour_zip=datalist.get(0).get("tour_zip").get(0).get("tour_zip");
						PublicData.tour_no=datalist.get(0).get("tour_no").get(0).get("tour_no");
						PublicData.tour_date=datalist.get(0).get("tour_date").get(0).get("tour_date");
						PublicData.zip_url=datalist.get(0).get("url").get(0).get("url");
						Intent intent=new Intent(RollListActivity.this,TourTabActivity.class);
					    startActivity(intent);
					    finish();
						break;
					case 2:
						SaveDataClass saveDataClass=new SaveDataClass();
						saveDataClass.saveLastUpDataTime(getApplicationContext());//保存更新数据时间
						String zipPath="";
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {					 
							zipPath =Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DaMeiTour" + File.separator + "zip"+File.separator;
						} else {
							zipPath = Environment.getExternalStorageDirectory().getPath();
//							System.out.println("没有sd卡下载路径apkPath="+apkPath);
							for (int i = 0; i < 4; i++) {
								String temp = zipPath + i;
								File f = new File(temp);
								if (f.exists()) {
									zipPath = temp + File.separator+ "DaMeiTour" + File.separator + "zip"+File.separator;
									break;
								}
							}
						}
						String zipName=PublicData.tour_zip+".zip";
						File zipFile=new File(zipPath, zipName);
						String folderPath=zipPath+PublicData.tour_zip+File.separator;
						createDir(folderPath);
						UnZipFile unZipFile=new UnZipFile();
						try {
							unZipFile.upZipFile(zipFile, folderPath);//解压缩包
						} catch (ZipException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Intent intent2=new Intent(RollListActivity.this,TourTabActivity.class);
					    startActivity(intent2);
					    finish();
						break;
					}
				}
			};
	}

	

	private void creatUi() {
		ib_fresh = (ImageButton) findViewById(R.id.tour_top_fresh);
		ib_fresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog=new WaitDialog(RollListActivity.this, android.R.style.Theme_Translucent);
				dialog.show();
//				httpAsynTask=new HttpAsynTask(0, url,this);
//				httpAsynTask.execute(null,null);
				GetDataAsyncTask getDataAsyncTask=new GetDataAsyncTask();
				getDataAsyncTask.execute();
			}
		});
		ib_exit = (ImageButton) findViewById(R.id.tour_top_exit);
		ib_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mTitle=(TextView)findViewById(R.id.tour_top_title);
		mTitle.setText(getResources().getString(R.string.tour_list));
		rollList = (ListView) findViewById(R.id.roolList);
		rollList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(RollListActivity.this,TourTabActivity.class);
			    startActivity(intent);
				
			    
			}
		});
		relyt_download=(RelativeLayout)findViewById(R.id.roll_list_item_relyt);
		lilyt_download=(LinearLayout)findViewById(R.id.roll_list_item_lilyt);
		tv_tip=(TextView) findViewById(R.id.tour_roll_list_tip);
		tv_mDate=(TextView) findViewById(R.id.roll_list_item_formdate);
		tv_mTitle=(TextView) findViewById(R.id.roll_list_item_title);
		tv_mNumber=(TextView) findViewById(R.id.roll_list_item_number);
		tv_mDowanLoad=(TextView) findViewById(R.id.roll_list_item_down);
		//下载压缩包（下载地址为:PublicData.zip_url）,下载完压缩包自动跳转到团信息页面
//		tv_mDowanLoad.setOnClickListener(new OnClickListener() {
		lilyt_download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				if("1".equals(PublicData.isnew)){//有新压缩包需要更新
					relyt_download.setVisibility(View.VISIBLE);
					tv_tip.setVisibility(View.GONE);
					SaveDataClass saveDataClass=new SaveDataClass();	
					String last_updata_time=saveDataClass.getLastUpDataTime(getApplicationContext());
					if(!"".equals(last_updata_time)){
						if(!last_updata_time.equals(PublicData.tour_update_time)){
							 TipDialog tipDialog=new TipDialog(RollListActivity.this, R.style.exchange_dialog);
							 tipDialog.show();
						} 
					} 
					
				}
//			    DownloadZip downloadZip =new DownloadZip();
//			    String filename=PublicData.tour_zip+".zip";
//			    downloadZip.downloadzip(RollListActivity.this, filename, PublicData.zip_url);
			}
		});
		bt_next=(Button)findViewById(R.id.tour_roll_next);
		bt_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 PublicData.isUpgrade=false;
				Intent intent=new Intent(RollListActivity.this,TourTabActivity.class);
			    startActivity(intent);
			    finish();
			}
		});
		if("1".equals(PublicData.isnew)){//有新压缩包需要更新
			relyt_download.setVisibility(View.VISIBLE);
			tv_tip.setVisibility(View.GONE);
			SaveDataClass saveDataClass=new SaveDataClass();	
			String last_updata_time=saveDataClass.getLastUpDataTime(getApplicationContext());
			if(!"".equals(last_updata_time)){
				if(!last_updata_time.equals(PublicData.tour_update_time)){
					 bt_next.setVisibility(View.VISIBLE);//显示跳过按钮（可以不更新进入当前的团）
				}else{
					 bt_next.setVisibility(View.GONE);
				}
			}else{
				 bt_next.setVisibility(View.GONE);
			}
			
		}else{
			relyt_download.setVisibility(View.GONE);
			tv_tip.setVisibility(View.VISIBLE);
		}
	}
	private void setData() {
		// TODO Auto-generated method stub
		tv_mDate.setText(PublicData.tour_date);
		tv_mTitle.setText(PublicData.tour_title);
		tv_mNumber.setText(PublicData.tour_no);
	
	}
	 
	private File createDir(String Path) {
		File file = new File(Path);
		file.mkdirs();
		return file;
	}
	/**
	 * 获取团信息接口
	 * @author wl
	 *
	 */
	class GetDataAsyncTask extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			dataPost();
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
			if(datalist.size()>0&&datalist.get(0).get("status").get(0).get("status")!=null){
				if("1".equals(datalist.get(0).get("status").get(0).get("status"))){
					mHandler.sendEmptyMessage(1);
					
				}else{
					tip=datalist.get(0).get("tips").get(0).get("tips");
					mHandler.sendEmptyMessage(0);
				} 
			}else{
				tip="网络异常！";
				mHandler.sendEmptyMessage(0);
			}
		}
		}
	private void dataPost(){
		String resultStr = new String("");
		PostUtil pu = new PostUtil();
		resultStr = pu.getData(new String[]{"act","get_user_tour","uid",PublicData.uid});
//		System.out.println("返回数据"+resultStr);
//		{"tour_id":"75","tour_title":"abc","tour_zip":"t75-pf4edp","tour_no":"007","tour_date":"2014-04-30","url":"http:\/\/192.168.1.10:7777\/public\/upload\/zip\/t75-pf4edp.zip","status":"1"}
		DataJsonParser jsonParser=new DataJsonParser();
		try {
			datalist=jsonParser.getDataList(resultStr);
//			System.out.println("返回数据"+datalist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	@Override
	public void httpCallBack(int flag, JSONObject json) {
		// TODO Auto-generated method stub
		if(json!=null){
			rollAdapter=new RollListAdpater(rollList.getContext(), null);
			rollList.setAdapter(rollAdapter);
		}
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}
	
	public class  TipDialog extends Dialog {
		private Handler mHandler;
		private Context mContext;
		private TextView tv_tips;
		private Button bt_support_confirm,bt_support_cancel;// 确定和取消
		public TipDialog(Context context, int theme) {
			super(context, theme);
			// TODO Auto-generated constructor stub
			mContext = context;
		}
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.tip_dialog);
			initDialog();

		}
		public void initDialog() {
			// TODO Auto-generated method stub
			tv_tips=(TextView)findViewById(R.id.tip_dialog_tips);

			bt_support_confirm = (Button) findViewById(R.id.tip_dialog_confirm);
			bt_support_cancel=(Button)findViewById(R.id.tip_dialog_cancel);
			bt_support_confirm.setOnClickListener(clickListener);
			bt_support_cancel.setOnClickListener(clickListener);
			tv_tips.setText("下载该团信息将清除之前团的数据！可直接进入当前的团。");
		
		}
		private View.OnClickListener clickListener=new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.tip_dialog_confirm:
					    DownloadZip downloadZip =new DownloadZip();
					    String filename=PublicData.tour_zip+".zip";
					    downloadZip.downloadzip(RollListActivity.this, filename, PublicData.zip_url);
					dismiss();
					break;

				case R.id.tip_dialog_cancel:
					dismiss();
					break;
				}
			
			}
		};
	}
	
	
}
