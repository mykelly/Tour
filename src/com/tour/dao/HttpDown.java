package com.tour.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.Environment;
import android.util.Log;
public class HttpDown {
	public static int n=1;
	public int isdown=-1; 
	private URL url;
	String SDRoot=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	
	public String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			// 创建一个URL对象
			url = new URL(urlStr);
			// 创建一个Http连接
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			// 使用IO流读取数据
			buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while ((buffer!=null)&&((line = buffer.readLine()) != null)) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(buffer!=null)
					buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	public File downLoadUrl(String fileName, String url) throws IOException {
      int temp=0;
		File file = null;
		OutputStream output = null;
		InputStream input = null;
		//music_download=false;
		try {
			createDir2();
			file = createSDFile2(fileName);
			input = getInputStreamByUrl(url);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];		
			while ((temp = input.read(buffer)) != -1) {  
				output.write(buffer, 0, temp);    
				
			//	System.out.println("正在下载");
			}
			n++;
           System.out.println("n==="+n+"下载完毕");						
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("queue", "异常");
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;

	}
	public static InputStream getInputStreamByUrl(String url) throws MalformedURLException, IOException {

		String htUrl = getHtpUrl(url);
		URLConnection connection = new URL(htUrl).openConnection();

		return connection.getInputStream();
	}
	public static String getHtpUrl(String url) throws UnsupportedEncodingException {
		String htpUrl = "";
		String a;
		for (int i = 0; i < url.length(); i++) {
			char c = url.charAt(i);
			if (c != '/' && c != ':' && c!=' ') {
				a = String.valueOf(c);

				htpUrl = htpUrl + URLEncoder.encode(a, "gbk");

			} else if(c==' '){
				htpUrl = htpUrl + "%20";
			}
			else {
				htpUrl=htpUrl+c;
			}
		}
		return htpUrl;
	}
	private File createDir2() {
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Fashion3g" + File.separator + "lrcName");
		file.mkdirs();

		return file;
	}

	private File createSDFile2(String lrcName) throws IOException {
		createDir2();
		File file = new File(SDRoot + "Fashion3g" + File.separator + "lrcName" + File.separator + lrcName+".png");
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	//下载文件
 	public void downFile(final String url,final String path,final String fileName) {	
		Thread t =new Thread(){			
			public void run() {
				 synchronized (this) { 
			          
				HttpGet httpRequest = null;
				HttpClient httpclient=null;
				HttpResponse httpResponse = null;
				InputStream is=null;
//				long length=0;
				
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
					return;
				}  		
				
				try {
					   
					//获得当前外部储存设备的目录
				    String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;   							   				    								    				  			    
				     File  dir = new File(SDCardRoot + path + File.separator);					       
				    if(!dir.exists())
				        dir.mkdirs();  //如果不存在则创建、
				    httpRequest = new HttpGet(url);
					httpclient = new DefaultHttpClient();
					httpResponse = httpclient.execute(httpRequest);
					if(httpResponse!=null){
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							HttpEntity	 entity = httpResponse.getEntity();
//							length = entity.getContentLength();		 
							is = entity.getContent();
						}
					}
					 FileOutputStream fileOutputStream = null;
					 if(is != null){	    	 
				 		File file = new File(Environment.getExternalStorageDirectory()+path, fileName);	
						if(!file.exists()){		
							fileOutputStream = new FileOutputStream(file);//如果不存在则创建		      
						    byte[] buf = new byte[1024];
						    int ch = -1;
						    while ((ch = is.read(buf)) != -1) {
							   fileOutputStream.write(buf, 0, ch);							   							  
						    }
						    System.out.println("下载完成");
						    fileOutputStream.flush();
						}
					 }
					 if (fileOutputStream != null) {						
						 fileOutputStream.close();
					 }
				} catch (ClientProtocolException e) {		 
					e.printStackTrace();
				} catch (IOException e) {		
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				} 
				
			    //通知所有在此对象上等待的线程 
                notifyAll(); 				  
	        } 
			}

		};
		t.start();
        synchronized (t){
        	  try { 
//                  System.out.println("等待线程t完成。。。"); 
                  t.wait(); 
              } catch (InterruptedException e) { 
                  e.printStackTrace(); 
              } 
//              System.out.println("t线程完成"); 
        }
	}
}
