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
			URL url = new URL(path + application.city_id + ".html"); // ����String��ʾ��ʽ����URL
			// ����
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����һ��
																				// URLConnection��������ʾ��URL�����õ�Զ�̶�������ӡ�
			conn.setConnectTimeout(5000);

			InputStreamReader isr = new InputStreamReader(conn.getInputStream());// ���شӴ˴򿪵����Ӷ�ȡ����������
			BufferedReader br = new BufferedReader(isr);// ����һ��ʹ��Ĭ�ϴ�С���뻺�����Ļ����ַ���������
			String temp;
			while ((temp = br.readLine()) != null) { // ���ж�ȡ�����
				html.append(temp).append("\n"); // ����ÿ�к���
			}
			br.close(); // �ر�
			isr.close(); // �ر�
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}