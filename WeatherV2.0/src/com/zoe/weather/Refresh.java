package com.zoe.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import com.zoe.data.DataTool;

public class Refresh extends Thread {
	private String path_1 = "http://www.weather.com.cn/data/sk/";
	private String path_2 = "http://www.weather.com.cn/data/cityinfo/";
	private String path_3 = "http://m.weather.com.cn/data/";
	private MyApplication application;

	private boolean flag;

	public Refresh(MyApplication application) {
		this.application = application;
		flag = false;
	}

	@Override
	public synchronized void run() {
		while (true) {
			try {
				if (flag) {
					wait();
				} else {
					application.myData_1 = DataTool.formJson_1(getData(path_1));
					application.myData_2 = DataTool.formJson_2(getData(path_2));
					application.myData_3 = DataTool.formJson_3(getData(path_3));
					Log.d("msg", "ing>>>>>>....");
					flag = true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void setFlag() {
		flag = false;
		notify();
	}

	private String getData(String path) {
		try {
			StringBuffer html = new StringBuffer();
			URL url = new URL(path + application.city_id + ".html"); // 根据String表示形式创建URL
			// 对象。
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 返回一个
																				// URLConnection对象，它表示到URL所引用的远程对象的连接。
			conn.setConnectTimeout(5000);

			InputStreamReader isr = new InputStreamReader(conn.getInputStream());// 返回从此打开的连接读取的输入流。
			BufferedReader br = new BufferedReader(isr);// 创建一个使用默认大小输入缓冲区的缓冲字符输入流。
			String temp;
			while ((temp = br.readLine()) != null) { // 按行读取输出流
				html.append(temp).append("\n"); // 读完每行后换行
			}
			br.close(); // 关闭
			isr.close(); // 关闭
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}