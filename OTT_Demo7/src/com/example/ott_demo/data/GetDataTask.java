package com.example.ott_demo.data;

import android.os.AsyncTask;
import android.widget.TextView;

public class GetDataTask extends AsyncTask<Void, Integer, Void> {
	private MainData data;
	
	public GetDataTask(MainData data) {
		this.data = data;
	}

	@Override
	protected Void doInBackground(Void... params) {
		String str = GetData.getData();
		Analyse.fun(str, data);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
	
	}
}
