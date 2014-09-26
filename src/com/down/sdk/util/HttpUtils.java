package com.down.sdk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.conn.ConnectTimeoutException;
public class HttpUtils {
	/**
	 * ��װ����ͷ��Ϣ
	 * @param urls
	 * @return
	 */
	public static URLConnection headMethod(String urls) {
		URL url = null;
		URLConnection urlConn = null;
		try {
			url = new URL(urls);

			urlConn = (HttpURLConnection) url.openConnection();

		} catch (MalformedURLException e1) {
			url = null;
			urlConn = null;
			e1.printStackTrace();
		} catch (java.net.UnknownHostException e2) {
//			LogUtils.e("������", "����������");
			url = null;
			urlConn = null;
			e2.printStackTrace();
		} catch (IOException e) {
			url = null;
			urlConn = null;
			e.printStackTrace();
		}
		if (url != null && urlConn != null) {
			urlConn.setConnectTimeout(20000);
			urlConn.setReadTimeout(20000);
		}
		return urlConn;
	}
	/*********************************************** GET *****************************************************/
	
	/**
	 * GET�õ�HTTP����
	 * urls������url
	 * flag��0������װ��Ϣ 1 �ƶ�KJAVA 2�ƶ�KJAVA��������
	 */
	public static HttpURLConnection getMethodDown(String urls) {
		HttpURLConnection urlConn = null;
		urlConn = (HttpURLConnection) headMethod(urls);
		if (urlConn != null) {
			try {
				// ������GET��ʽ
				urlConn.setRequestMethod("GET");
				// �Ƿ�����ض���
				urlConn.setInstanceFollowRedirects(true);
				
				
				
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return urlConn;
	}
	/**
	 * GET�õ�HTTP���� urls������url 
	 */
	public static String getMethod(String urls) {
		HttpURLConnection urlConn = null;
		StringBuffer buffer = new StringBuffer();
		urlConn = (HttpURLConnection) headMethod(urls);
		if (urlConn != null) {
			try {
				// ������GET��ʽ
				urlConn.setRequestMethod("GET");
				// �Ƿ�����ض���
				urlConn.setInstanceFollowRedirects(true);

				if (urlConn.getResponseCode() == 200) {
					String temp = null;
					InputStream in = urlConn.getInputStream();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in, "utf-8"));
					while ((temp = br.readLine()) != null) {
						buffer.append(temp);
					}
					br.close();
					in.close();
				}

			} catch (ConnectTimeoutException e) {// ���ӳ�ʱ
				e.printStackTrace();
				return "exception ConnectTimeout";
			} catch (SocketTimeoutException e) {// ��ȡ��ʱ
				e.printStackTrace();
				return "exception SocketTimeout";
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return "exception MalformedURL";
			} catch (IOException e) {
				e.printStackTrace();
				return "exception IO";
			}
//			LogUtils.s("End Http Request");
		}
		return buffer.toString();
	}
	/**
	 * ��ȡurl���ļ���
	 * 
	 * @param urls
	 * @return
	 */
	public static String getUrlFileName(String urls) {
		String name = urls;
		if (name.contains("/")) {
			String[] arr = urls.split("/");
			name = arr[arr.length - 1];
		}
		if (name.contains("?")) {
			name = name.split("?")[0];
		}
		return name;
	}
	
	

   
}
