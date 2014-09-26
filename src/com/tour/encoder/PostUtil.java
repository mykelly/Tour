package com.tour.encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.tour.util.PublicData;


public class PostUtil {
 
	private String postData = "";
	private final static String KEY = "KeyforTm";// 密钥

	public String getData(String[] array) {
		String jsonStr = new String();
		try {

			LinkedHashMap<String, String> temp = new LinkedHashMap<String, String>();
			String key = "";
			String value = "";
			for (int i = 0; i < array.length; i += 2) {
				key = array[i].toString();
				value = array[i + 1].toString();
				temp.put(key, value);				
			}
			// 倒序
//			int i = array.length;
//			while (i >= 0) {
//				if (i >= 2) {
//					key = array[i - 2].toString();
//					value = array[i - 1].toString();
//					System.out.println("i==="+i+"-------key="+key+"-------value="+value);
//					temp.put(key, value);
//					System.out.println("temp=" + temp);
//					i -= 2;
//				} else {
//					i = -1;
//				}
//			}
			
			Gson gson = new Gson();
			String toJson = gson.toJson(temp);
//			System.out.println("发送请求数据=" + toJson);

			jsonStr = encrypt(gson.toJson(toJson));

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpPost httpRequest = new HttpPost(PublicData.postUrl);
		// Post运作传送变数必须用NameValuePair[]阵列储存
		// 传参数 服务端获取的方法为request.getParameter("name")
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("data", jsonStr));

		try {
			// 发出HTTP request
			httpRequest.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			// 取得HTTP response
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strReturn = EntityUtils.toString(httpResponse
						.getEntity());
//				System.out.println("返回数据：" + strReturn);
				postData = decrypt(strReturn);
//				System.out.println("解密数据：" + postData);

				// Gson gson = new Gson();
				// String toJson = gson.toJson(desStr);
				// String fromJson[]=gson.fromJson(toJson,
				// String[].class);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return postData;
	}
	
	/**
	 * 加密
	 * 
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	public static byte[] desEncrypt(byte[] plainText) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(KEY.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);
		byte data[] = plainText;
		byte encryptedData[] = cipher.doFinal(data);
		return encryptedData;
	}

	/**
	 * 解密
	 * 
	 * @param encryptText
	 * @return
	 * @throws Exception
	 */
	public static byte[] desDecrypt(byte[] encryptText) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(KEY.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key, sr);
		byte encryptedData[] = encryptText;
		byte decryptedData[] = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	public static String encrypt(String input) throws Exception {
		return base64Encode(desEncrypt(input.getBytes()));
	}

	public static String decrypt(String input) throws Exception {
		byte[] result = base64Decode(input);
		return new String(desDecrypt(result));
	}

	public static String base64Encode(byte[] s) {
		if (s == null)
			return null;
		BASE64Encoder b = new BASE64Encoder();
		return b.encode(s);
	}

	public static byte[] base64Decode(String s) throws IOException {
		if (s == null) {
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(s);
		return b;
	}

}
