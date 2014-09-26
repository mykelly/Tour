package com.tour.dao;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;


import org.json.JSONObject;

import com.tour.util.TTLog;


public class HttpRequestUtil {
	
	private final static int CONNECTION_TIMEOUT = 30 * 1000;
	private final static int SO_TIMEOUT = 30 * 1000;
	
	
	/**
	 * special for articles, medias etc with pages
	 * */
	public static JSONObject getJsonObjectFromServer(String link) {
		//link = "http://192.168.0.50:8090/share/index.php/read/get_article_list?showtime=1357971243&access_token=62818fd16919a9e6771108af195ba711";
		
		TTLog.e("getJsonObjectFromServer", link);
		JSONObject jsonObject = null;
		HttpClient client = new DefaultHttpClient();
		
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT * 2);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT * 2);
		
		StringBuilder builder = new StringBuilder();

		try {
			HttpGet myget = new HttpGet(link);
			HttpResponse response = client.execute(myget);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
		 
            TTLog.s("result----"+builder.toString());
			jsonObject = new JSONObject(builder.toString());
			//SDLog.e("data", ((JSONArray) jsonObject.get("user")).length() + "");
		} catch (Exception e) {
			// Log.v("url response", "false");
			e.printStackTrace();
		}

		return jsonObject;
	}
	
	/**
	 * 
	 * */
	public static JSONObject getJsonObjectFromServerTimeLimit(String link) {
		TTLog.e("getJsonObjectFromServer", link);
		JSONObject jsonObject = null;
		HttpClient client = new DefaultHttpClient();
		
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
		StringBuilder builder = new StringBuilder();
		HttpGet myget = new HttpGet(link);
		try {
			HttpResponse response = client.execute(myget);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
 
			jsonObject = new JSONObject(builder.toString());
			//SDLog.e("data", ((JSONArray) jsonObject.get("user")).length() + "");
		} catch (Exception e) {
			// Log.v("url response", "false");
			e.printStackTrace();
		}

		return jsonObject;
	}
	
	
	public static void getFileFromUrl(String url, File outFile) {
		if (outFile == null) {
			return;
		}
		
		URL m;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		
		//Drawable d = null;
		byte[] buf = new byte[4 * 1024 * 10];
		try {
			m = new URL(url);
			bis = new BufferedInputStream((InputStream) m.getContent());
			fos = new FileOutputStream(outFile);
			int len;
			while ((len = bis.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			
			bis.close();
			fos.flush();
			fos.close();
			
			//d = Drawable.createFromStream(is, "src");
			
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bis = null;
			fos = null;
		}
		
		TTLog.e("getFileFromUrl", outFile.getPath());
		
	}
	
	
	public static String getWebDataFromServer(String link) {
		//link = "http://192.168.0.50:8090/share/index.php/read/get_article_list?showtime=1357971243&access_token=62818fd16919a9e6771108af195ba711";
		
		TTLog.e("getWebDataFromServer", link);
		HttpClient client = new DefaultHttpClient();
		
		
		//client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
		//client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
		
		StringBuilder builder = new StringBuilder();

		HttpGet myget = new HttpGet(link);
		try {
			HttpResponse response = client.execute(myget);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			// SDLog.e("data", builder.toString());
			
			
			// charset check here
			/*byte[] buf = builder.toString().getBytes();
			
			UniversalDetector detector = new UniversalDetector(null);
			
			detector.handleData(buf, 0, buf.length);
			
			detector.dataEnd();
		    
			String charset = detector.getDetectedCharset();
			
			charset = (charset == null) ? "utf-8" : charset;
		    SDLog.e("UniversalDetector", detector.getDetectedCharset());*/

			//jsonObject = new JSONObject(builder.toString());
			//SDLog.e("data", ((JSONArray) jsonObject.get("user")).length() + "");
		} catch (Exception e) {
			// Log.v("url response", "false");
			e.printStackTrace();
		}

		return builder.toString();
	}
	
	
	/*
	 * 
	 *  corresponding to the server php code 
	 	<?php  
			$target_path  = "./upload/";//receiver file dir
			$target_path = $target_path . basename( $_FILES['uploadedfile']['name']);  
			if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {  
			   echo "The file ".  basename( $_FILES['uploadedfile']['name']). " has been uploaded";  
			}  else{  
			   echo "There was an error uploading the file, please try again!" . $_FILES['uploadedfile']['error'];  
			}  
		?> */ 
	
	public static void uploadFile(File file, String uploadUrl) {
		
		TTLog.e("uploadFile", uploadUrl);
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			
			// restrictions on every time the transmit bytes size, prevent from out of memory
			// under we don't know the content size , it is useful 
			httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
			// permits inputstream & outputstream
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			// use post
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
					+ file.getName() + "\"" + end);
			dos.writeBytes(end);

			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			// read file
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			TTLog.e("uploadFile", result);
			// Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			dos.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
			TTLog.e("uploadFile", "error");
			// setTitle(e.getMessage());
		}
	}  
	
	
}
