package com.zoe.ott_videoview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.zoe.ott_videoview.data.GirdViewAdapter;
import com.zoe.ott_videoview.data.MainGetDataTask;
import com.zoe.ott_videoview.data.MovieData;

public class MainActivity extends Activity {
	MovieData data;
	private Handler handler;
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		gridView = (GridView) findViewById(R.id.gridview);
		data = new MovieData();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x1111) {
					gridView.setAdapter(new GirdViewAdapter(data,
							MainActivity.this));
				}
			}
		};
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MainActivity.this,PlayerActivity.class);
				intent.putExtra("url", data.dataList.get(arg2).getJson_url());
				startActivity(intent);
			}
		});
		getTask(data, handler);
	}

	private void getTask(MovieData data, Handler handler) {
		MainGetDataTask task = new MainGetDataTask(data, handler);
		task.execute();
	}
}
