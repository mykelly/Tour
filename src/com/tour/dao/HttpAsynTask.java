package com.tour.dao;


import org.json.JSONObject;
import android.os.AsyncTask;
import com.tour.listener.HttpCallBack;
public class HttpAsynTask extends AsyncTask<String, Integer, String> {

	private int flag ;
	private String url ;
	private HttpCallBack callBack;
	public boolean isRunning;
	
	JSONObject json;
	
	public HttpAsynTask( int flag,String url,HttpCallBack httpCallBack){
		this.flag=flag;
		this.url=url;
		this.callBack=httpCallBack;
		isRunning=false;
	}
	
	@Override
	protected void onPreExecute() {
		//��һ��ִ�з���
		super.onPreExecute();
		isRunning=true;
	}
	

	@Override
	protected void onProgressUpdate(Integer... values) {
		//super.onProgressUpdate(values);
        //���������doInBackground����publishProgressʱ��������Ȼ����ʱֻ��һ������  
        //��������ȡ������һ������,����Ҫ��progesss[0]��ȡֵ  
        //��n����������progress[n]��ȡֵ 
		
		//���������������UI������ʾ
 
	}

	
	@Override
	protected String doInBackground(String... params) {
		//�ڶ���ִ�з���,onPreExecute()ִ�����ִ��
		json = HttpRequestUtil.getJsonObjectFromServer(url);
		
		return  "";
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
        //doInBackground����ʱ���������仰˵������doInBackgroundִ����󴥷�  
        //�����result��������doInBackgroundִ�к�ķ���ֵ������������"ִ�����" 
		//�����������UI�����ʾ
		if(callBack!=null){
			callBack.httpCallBack(flag,json);
		}
		isRunning=false;
		
 
	}

	//onCancelled����������ȡ��ִ���е�����ʱ����UI  
    @Override  
    protected void onCancelled() {  
     
    }  
    
 
}
