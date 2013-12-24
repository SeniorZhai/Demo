package com.example.ott_demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity{
	VideoView videoView;
	MediaController mediaController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getWindow().setFormat(PixelFormat.TRANSLUCENT);
			Intent intent =  getIntent();
			String url = intent.getStringExtra("url");
			setContentView(R.layout.video_layout);
			Log.d("TAG",url);
			videoView =(VideoView)findViewById(R.id.videoView);
			mediaController = new MediaController(this);
			videoView.setVideoURI(Uri.parse("rtsp://192.168.1.67/1/dianying_x10001_SayYes.mp4"));
			videoView.start();
			videoView.seekTo(videoView.getDuration()/4);
			videoView.setMediaController(mediaController);
			videoView.requestFocus();
	}
}
