package com.zoe.ott_videoview.data;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class MainGetDataTask extends AsyncTask<Void, Integer, Void> {
	private MovieData data;
	private Handler handler;
	private String uri = "http://192.168.1.162/dianying";
	public MainGetDataTask(MovieData data,Handler handler) {
		this.data = data;
		this.handler = handler;
	}

	@Override
	protected Void doInBackground(Void... params) {
		String str = GetData.getData(uri);
		XMLAnalyse.fun(str, data);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		Message message = Message.obtain();
		message.what = 0x1111;
		handler.sendMessage(message);
	}
}
