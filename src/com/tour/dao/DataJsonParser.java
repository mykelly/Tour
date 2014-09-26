package com.tour.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * 实现对JSON的通用解析
 * 
 * <p>
 * 必须使用List[HashMap[String, List[HashMap[String, String]]]]存放数据
 * 
 * <p>
 * 取数据的方法如下：
 * 
 * <b> 2维数据：list.get(0).get("exam_begin").get(0).get("exam_begin")</b>
 * <p>
 * <b> 3维数据：list.get(0).get("list").get(position).get("标签名")</b>
 * 
 * @author wl
 * 
 * @version 2013.11.18
 * 
 */
public class DataJsonParser {
	List<HashMap<String, List<HashMap<String, String>>>> listAll = 
			new ArrayList<HashMap<String, List<HashMap<String, String>>>>();

	public List<HashMap<String, List<HashMap<String, String>>>> getDataList(
			String json) throws Exception {

		JSONObject jsonObject = new JSONObject(json);
		HashMap<String, List<HashMap<String, String>>> outMap = 
				new HashMap<String, List<HashMap<String, String>>>();

		@SuppressWarnings("unchecked")
		Iterator<String> outKey = jsonObject.keys();// 获得键名
		String key1, value1;
		while (outKey.hasNext()) {
			key1 = (String) outKey.next();
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

			if (key1.equals("list")) {// 发现2维数组
				JSONArray jsonArray = jsonObject.getJSONArray(key1);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					HashMap<String, String> inMapList = new HashMap<String, String>();

					@SuppressWarnings("unchecked")
					Iterator<String> inKey = object.keys();
					String key2, value2;
					while (inKey.hasNext()) {
						key2 = (String) inKey.next();
						value2 = object.getString(key2);
						inMapList.put(key2, value2);// 存入最内层数据
					}
					list.add(inMapList);// 上一级数据，多个
				}
				outMap.put(key1, list);// 囊括之前 解析的数据
			} else {// 否则1维数组作2维存储
				value1 = jsonObject.getString(key1);
				HashMap<String, String> inMapValue = new HashMap<String, String>();
				inMapValue.put(key1, value1);
				list.add(inMapValue);
				outMap.put(key1, list);// 囊括解析的数据
			}
		}
		listAll.add(outMap);// 存入完整的数据

		return listAll;
	}

}
