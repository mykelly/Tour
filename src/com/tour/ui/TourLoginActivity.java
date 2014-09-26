package com.tour.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.tour.R;
import com.tour.SQLite.DBTour;
import com.tour.SQLite.TourData;
import com.tour.dao.DataJsonParser;
import com.tour.dao.HttpAsynTask;
import com.tour.encoder.PostUtil;
import com.tour.listener.HttpCallBack;
import com.tour.util.NetWorkStatus;
import com.tour.util.PublicData;
import com.tour.util.SaveDataClass;
import com.tour.util.ShowToast;
import com.tour.util.VersionUpdate;
import com.tour.view.WaitDialog;

public class TourLoginActivity extends NotTitleActivity implements HttpCallBack {
	private String status = ""; 
	 NetWorkStatus networkstatus;
//	private boolean NetWorkStatus = false;
	private SharedPreferences user_Password;
	private Button login;
	private EditText editAccount, editPassword;
	private String mAccount, mPassword,tip="";
//    private ShowToast toast;
	private HttpAsynTask httpAsynTask;
	private WaitDialog dialog;// ���ȴ����Ի���
	private	List<HashMap<String, List<HashMap<String, String>>>> datalist = new ArrayList<HashMap<String, List<HashMap<String, String>>>>();
	public  Handler mHandler;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
		   networkstatus=new NetWorkStatus();
		 PublicData.isNetWork=networkstatus.isNetWork(getApplicationContext());//����������
		if(PublicData.isNetWork){
		VersionUpdate	updates = new VersionUpdate();//���°汾
		updates.asyncUpdate(TourLoginActivity.this, true);
		}
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
						
						DBTour dbTour=new DBTour(getApplicationContext());//�������ݿ�
						try {
							PublicData.isnew = datalist.get(0).get("isnew").get(0).get("isnew");//1 �з��������ݣ�0û��
					    PublicData.username = editAccount.getText().toString();
						PublicData.password = editPassword.getText().toString();
						PublicData.truename = datalist.get(0).get("admin_true_name").get(0).get("admin_true_name");
						PublicData.uid = datalist.get(0).get("admin_id").get(0).get("admin_id");
						PublicData.gid = datalist.get(0).get("admin_gid").get(0).get("admin_gid");
						PublicData.islogin = datalist.get(0).get("admin_check").get(0).get("admin_check");	
						SaveDataClass saveDataClass=new SaveDataClass();	
						saveDataClass.saveAccountPassword(getApplicationContext());//�����û��˺�����
						if ("1".equals(PublicData.isnew)) {
						
												
							PublicData.tour_id=datalist.get(0).get("tour_id").get(0).get("tour_id");
							PublicData.tour_title=datalist.get(0).get("tour_title").get(0).get("tour_title");
							PublicData.tour_zip=datalist.get(0).get("tour_zip").get(0).get("tour_zip");
							PublicData.tour_no=datalist.get(0).get("tour_no").get(0).get("tour_no");
							PublicData.tour_date=datalist.get(0).get("tour_date").get(0).get("tour_date");
							PublicData.tour_update_time=datalist.get(0).get("tour_update_time").get(0).get("tour_update_time");
							PublicData.zip_url=datalist.get(0).get("url").get(0).get("url");
									
										
						String last_updata_time=saveDataClass.getLastUpDataTime(getApplicationContext());
						if(!last_updata_time.equals(PublicData.tour_update_time)){//����ѹ������Ҫ����
							PublicData.isUpgrade=true;
						
							Intent intent = new Intent();
							intent.setClass(TourLoginActivity.this, RollListActivity.class);//��ת������ѹ����ҳ��
							startActivity(intent); 
							
							
						}else{
							  PublicData.isUpgrade=false;
							Intent intent=new Intent(TourLoginActivity.this,TourTabActivity.class);//ֱ����ת������Ϣҳ��
						    startActivity(intent);
						  
						}
						finish();
						
						}else{
							PublicData.isUpgrade=false;
							 Toast. makeText(TourLoginActivity.this, "��û������Ϣ",Toast.LENGTH_LONG).show();	
							 PublicData.tour_id=TourData.queryByUser(TourLoginActivity.this, PublicData.uid);	
							 if(!"".equals(PublicData.tour_id)){							 
									Intent intent=new Intent(TourLoginActivity.this,TourTabActivity.class);//ֱ����ת������Ϣҳ��
								    startActivity(intent);
							 }else{
								    Intent intent = new Intent();
									intent.setClass(TourLoginActivity.this, RollListActivity.class);//��ת������ѹ����ҳ��
									startActivity(intent); 
									
//									Intent intent=new Intent(TourLoginActivity.this,TourTabActivity.class);
//								    startActivity(intent);
							 }
							 finish();
						}
					 
						
						} catch (Exception e) {
							// TODO: handle exception
							tip="�����쳣";
							Toast.makeText(getApplicationContext(), tip, 1000).show();
						}								
						break;
					 
					}
				}
			};
	}

	private void init() {
		login = (Button) findViewById(R.id.btn_login);
		login.setOnClickListener(listener);
		editAccount = (EditText) findViewById(R.id.edit_login_account);
		editPassword = (EditText) findViewById(R.id.edit_login_password);
		editAccount.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					v.setBackgroundResource(R.drawable.login_edt_account_focus);
				} else {
					v.setBackgroundResource(R.drawable.login_edt_account_defaulet);
				}
			}
		});

		editPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					v.setBackgroundResource(R.drawable.login_edt_pwd_focus);
				} else {
					v.setBackgroundResource(R.drawable.login_edt_pwd_defaulet);
				}
			}
		});
//		toast=new ShowToast();
	
//		if (!PublicData.isNetWork) {//����ʱ
			SaveDataClass saveDataClass=new SaveDataClass();
			saveDataClass.getAccountPassword(TourLoginActivity.this);
			if(!"".equals(PublicData.username)){
			editAccount.setText(PublicData.username);
			}
			if(!"".equals(PublicData.password)){
			editPassword.setText(PublicData.password);
			}
//		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mAccount=editAccount.getText().toString();
			mPassword=editPassword.getText().toString();
 
			 PublicData.isNetWork=networkstatus.isNetWork(getApplicationContext());//����������
				 		
				if (PublicData.isNetWork) {//����
					if(!"".equals(mAccount)&&!"".equals(mPassword)){
	                dialog=new WaitDialog(TourLoginActivity.this, android.R.style.Theme_Translucent);
					dialog.show();
					LoginAsyncTask loginAsyncTask=new LoginAsyncTask();
					loginAsyncTask.execute();
					}else{
						if("".equals(mAccount)&&"".equals(mPassword)){
							 Toast. makeText(TourLoginActivity.this, getResources().getString(R.string.login_null_tip),Toast.LENGTH_LONG).show();
						
						}else{
						   if("".equals(mAccount)){
							 Toast. makeText(TourLoginActivity.this, getResources().getString(R.string.login_user_tip),Toast.LENGTH_LONG).show();
							
						    }else if("".equals(mPassword)){
						     Toast. makeText(TourLoginActivity.this, getResources().getString(R.string.login_password_tip),Toast.LENGTH_LONG).show();
						
						    }
					    }
					}
				} else {//����
 
					SaveDataClass saveDataClass=new SaveDataClass();
					saveDataClass.getAccountPassword(TourLoginActivity.this);//��ȡ�����˺�����
//					saveDataClass.getTourId(getApplicationContext());//��ȡ��id
					if(!"".equals(mAccount)&&!"".equals(mPassword)){
						if(mAccount.equals(PublicData.username)&&mPassword.equals(PublicData.password)){
							 PublicData.tour_id=TourData.queryByUser(TourLoginActivity.this, PublicData.uid);	
							 if(!"".equals(PublicData.tour_id)){							 
									Intent intent=new Intent(TourLoginActivity.this,TourTabActivity.class);//ֱ����ת������Ϣҳ��
								    startActivity(intent);
							 }else{
								    PublicData.isnew="0";
								    Intent intent = new Intent();
									intent.setClass(TourLoginActivity.this, RollListActivity.class);//��ת������ѹ����ҳ��
									startActivity(intent); 
							 }
							 finish();
						}else{
							if(!mAccount.equals(PublicData.username)&&!mPassword.equals(PublicData.password)){
								Toast. makeText(TourLoginActivity.this, getResources().getString(R.string.login_error_tip),Toast.LENGTH_LONG).show();
							}else {
								if(!mAccount.equals(PublicData.username)){
								Toast. makeText(TourLoginActivity.this, getResources().getString(R.string.login_user_errortip),Toast.LENGTH_LONG).show();
								
							    }else  if(!mPassword.equals(PublicData.password)){
							    Toast. makeText(TourLoginActivity.this, getResources().getString(R.string.login_password_errortip),Toast.LENGTH_LONG).show();
								
							    }	
							}
						}
						
					}else {
						if("".equals(mAccount)&&"".equals(mPassword)){
							 Toast. makeText(TourLoginActivity.this, getResources().getString(R.string.login_null_tip),Toast.LENGTH_LONG).show();
						
						}else{
						   if("".equals(mAccount)){
							 Toast. makeText(TourLoginActivity.this, getResources().getString(R.string.login_user_tip),Toast.LENGTH_LONG).show();
						
						    }else if("".equals(mPassword)){
						     Toast. makeText(TourLoginActivity.this, getResources().getString(R.string.login_password_tip),Toast.LENGTH_LONG).show();
						 
						    }
					    }
					}
				}
			}
//		}
	};
	/**
	 * ��¼�ӿ�
	 * @author wl
	 *
	 */
	class LoginAsyncTask extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			loginPost();
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
//					tip="��¼�ɹ���";
					mHandler.sendEmptyMessage(1);
				
				}else{
					tip=datalist.get(0).get("tips").get(0).get("tips");
					mHandler.sendEmptyMessage(0);
				} 
			}else{
				tip="�����쳣��";
				mHandler.sendEmptyMessage(0);
			}
		}
		}
	private void loginPost(){
		String resultStr = new String("");
		PostUtil pu = new PostUtil();
		resultStr = pu.getData(new String[]{"act","user_login","name",mAccount,"pwd",mPassword
//				,"imei",PublicDataClass.Imei
				});
//		System.out.println("��¼��������"+resultStr);
		DataJsonParser jsonParser=new DataJsonParser();
		try {
			datalist=jsonParser.getDataList(resultStr);
//			System.out.println("��������"+datalist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// �ж�����״̬����������
	private boolean NetWorkStatus() {
		boolean netSataus = false;
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		cwjManager.getActiveNetworkInfo();
		if (cwjManager.getActiveNetworkInfo() != null) {
			netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
		}
		return netSataus;
	}

	@Override
	public void httpCallBack(int flag, JSONObject json) {
		// TODO Auto-generated method stub
		if(json!=null){
			Intent intent = new Intent();
			intent.putExtra("NetWorkStatus", PublicData.isNetWork);
			intent.setClass(TourLoginActivity.this, RollListActivity.class);
			startActivity(intent); 
		}
	}

	 

	 
}