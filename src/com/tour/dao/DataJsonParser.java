package com.tour.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * ʵ�ֶ�JSON��ͨ�ý���
 * 
 * <p>
 * ����ʹ��List[HashMap[String, List[HashMap[String, String]]]]�������
 * 
 * <p>
 * ȡ���ݵķ������£�
 * 
 * <b> 2ά���ݣ�list.get(0).get("exam_begin").get(0).get("exam_begin")</b>
 * <p>
 * <b> 3ά���ݣ�list.get(0).get("list").get(position).get("��ǩ��")</b>
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
		Iterator<String> outKey = jsonObject.keys();// ��ü���
		String key1, value1;
		while (outKey.hasNext()) {
			key1 = (String) outKey.next();
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

			if (key1.equals("list")) {// ����2ά����
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
						inMapList.put(key2, value2);// �������ڲ�����
					}
					list.add(inMapList);// ��һ�����ݣ����
				}
				outMap.put(key1, list);// ����֮ǰ ����������
			} else {// ����1ά������2ά�洢
				value1 = jsonObject.getString(key1);
				HashMap<String, String> inMapValue = new HashMap<String, String>();
				inMapValue.put(key1, value1);
				list.add(inMapValue);
				outMap.put(key1, list);// ��������������
			}
		}
		listAll.add(outMap);// ��������������

		return listAll;
	}

}
