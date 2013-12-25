package com.example.demo_rtsp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends Activity {
	private String uri= "rtsp://192.168.1.67/1/dianying_x10001_SayYes.mp4";
	private Button paly, pause, stop;
	private VideoView videoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		paly = (Button) findViewById(R.id.play);
		paly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("TAG", "----button-play-----");
				PlayRtspStream(uri);
			}
		});
		videoView = (VideoView) findViewById(R.id.video);
	}

	private void PlayRtspStream(String uri) {
		videoView.setVideoURI(Uri.parse(uri));
		videoView.requestFocus();
		videoView.start();
		Log.d("TAG", "-----video-start----");
	}
}
