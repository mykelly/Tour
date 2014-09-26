package com.tour.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.tour.info.DataCustomerInfo;

public class FileUtil {

	public static JSONObject readJsonData(File cacheFile) {
		JSONObject jsonObject = null;
		if (cacheFile == null) {
			return jsonObject;
		}
		try {
			InputStream fIS = new FileInputStream(cacheFile);
			BufferedInputStream bin = new BufferedInputStream(fIS);
			byte bts[] = new byte[bin.available()];
			bin.read(bts);
			bin.close();
			fIS.close();
			jsonObject = new JSONObject(new String(bts, "UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return jsonObject;

	}
	public static List<DataCustomerInfo> getArrayList(){  
		File file = new File(PublicData.AppDir.LISTPATH);
		if (!file.exists()) {
			return null;
		}
		List<DataCustomerInfo> savedArrayList=null;
		FileInputStream fileInputStream=null;
		ObjectInputStream objectInputStream=null;
		try {

			//取出数据
			fileInputStream = new FileInputStream(file.toString());
			objectInputStream = new ObjectInputStream(fileInputStream);
			List<DataCustomerInfo> tt =(List<DataCustomerInfo>) objectInputStream.readObject();
			savedArrayList =tt;
		 

		} catch (Exception e) {
			// TODO: handle exception
		}finally{

			if (objectInputStream!=null) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileInputStream!=null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return savedArrayList;
	}
	public static void saveArrayList(List<DataCustomerInfo> paramList){
		if (paramList!=null&&paramList.size()>0) {


			FileOutputStream fileOutputStream=null;
			ObjectOutputStream objectOutputStream=null;
			try {
				File file = new File(PublicData.AppDir.LISTPATH);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				if (!file.exists()) {
					file.createNewFile();
				}

				fileOutputStream= new FileOutputStream(file.toString());
				objectOutputStream= new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(paramList);
				fileOutputStream.close();
				objectOutputStream.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}if (fileOutputStream!=null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (objectOutputStream!=null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
