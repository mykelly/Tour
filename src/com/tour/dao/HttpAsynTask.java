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
		//第一个执行方法
		super.onPreExecute();
		isRunning=true;
	}
	

	@Override
	protected void onProgressUpdate(Integer... values) {
		//super.onProgressUpdate(values);
        //这个函数在doInBackground调用publishProgress时触发，虽然调用时只有一个参数  
        //但是这里取到的是一个数组,所以要用progesss[0]来取值  
        //第n个参数就用progress[n]来取值 
		
		//这里可以用来进行UI更新提示
 
	}

	
	@Override
	protected String doInBackground(String... params) {
		//第二个执行方法,onPreExecute()执行完后执行
		json = HttpRequestUtil.getJsonObjectFromServer(url);
		
		return  "";
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
        //doInBackground返回时触发，换句话说，就是doInBackground执行完后触发  
        //这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕" 
		//这里可以用来UI结果显示
		if(callBack!=null){
			callBack.httpCallBack(flag,json);
		}
		isRunning=false;
		
 
	}

	//onCancelled方法用于在取消执行中的任务时更改UI  
    @Override  
    protected void onCancelled() {  
     
    }  
    
 
}
