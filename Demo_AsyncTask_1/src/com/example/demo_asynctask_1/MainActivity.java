package com.example.demo_asynctask_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button execute, cancel;
	private ProgressBar progressBar;
	private TextView textView;
	private MyTask myTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		execute = (Button) findViewById(R.id.execute);
		cancel = (Button) findViewById(R.id.cancel);
		textView = (TextView) findViewById(R.id.text_view);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		execute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myTask = new MyTask();
				myTask.execute("http://192.168.1.62/dianying");
				execute.setEnabled(false);
				cancel.setEnabled(true);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myTask.cancel(true);
			}
		});
	}

	private class MyTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			textView.setText("loading....");
		}

		@Override
		protected void onPostExecute(String result) {
			textView.setText(result);
			execute.setEnabled(true);
			cancel.setEnabled(false);
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				StringBuffer html = new StringBuffer();
				URL url = new URL(params[0]);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();// 返回一个
				// URLConnection对象，它表示到URL所引用的远程对象的连接。
				// conn.setConnectTimeout(5000);
				InputStreamReader isr = new InputStreamReader(
						conn.getInputStream(), "utf-8");// 返回从此打开的连接读取的输入流。
				BufferedReader br = new BufferedReader(isr);// 创建一个使用默认大小输入缓冲区的缓冲字符输入流。
				String temp;
				while ((temp = br.readLine()) != null) { // 按行读取输出流
					html.append(temp);
				}
				br.close(); // 关闭
				isr.close(); // 关闭
				Log.d("TGA", html.toString());
				return html.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
			textView.setText("loading..." + values[0]);
		}

		@Override
		protected void onCancelled() {
			textView.setText("cancelled");
			progressBar.setProgress(0);

			execute.setEnabled(true);
			cancel.setEnabled(false);
		}
	}
}
