package com.example.ott_demo.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class GetData {
	public static String getData() {
		try {
			StringBuffer html = new StringBuffer();
			URL url = new URL("http://192.168.1.62/dianying");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 返回一个
			conn.setConnectTimeout(3000);
			InputStreamReader isr = new InputStreamReader(
					conn.getInputStream(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				html.append(temp);
			}
			br.close(); // 关闭
			isr.close(); // 关闭
			return html.toString();
		} catch (Exception e) {
			Log.d("ERR", e.getMessage());
			return "";
		}
	}
}