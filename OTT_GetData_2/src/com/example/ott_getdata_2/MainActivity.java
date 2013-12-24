package com.example.ott_getdata_2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView textView;
	private SingleData data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView)findViewById(R.id.textView);
		data = new SingleData();
		GetDataTask task = new GetDataTask();
		task.execute();

	}
	protected class GetDataTask extends AsyncTask<Void, Integer, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			String str = GetData.getData();
			Analyse.fun(str, data);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			SmallData link;
			for (int i = 0; i < data.dataList.size(); i++) {
				link = data.dataList.get(i);
				textView.append("第"+(i+1)+"部电影：\n"+"\t片名:\t"+link.getTitle()+"\n\t海报地址"+link.getPoster_url()+"\n\t电影地址"+link.getJson_url()+"\n");
			}
		}
	}
}
