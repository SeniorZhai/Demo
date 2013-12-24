package com.example.demo_mediaplay;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
	VideoView videoView;
	MediaController mediaController;
	Button bn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		videoView = (VideoView) findViewById(R.id.videoView);
		mediaController = new MediaController(this);
		bn = (Button) findViewById(R.id.bn);
		videoView.setVideoURI(Uri
				.parse("rtsp://192.168.1.67/1/dianying_x10001_SayYes.mp4"));
		// videoView.setVideoURI(Uri.parse("rtsp://192.168.1.67/1/1.mp4"));
		// videoView.setVideoURI(Uri.parse("rtsp://192.168.1.67/1/dianying_x10000_Special26.mp4"));
		// videoView.setVideoURI(Uri.parse("rtsp://192.168.1.67/1/dianying_x10009_BorderRun.mp4"));
		//
		videoView.requestFocus();
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				Log.d("TAG", "onPrepared");
				videoView.start();
			}
		});
		//
		// videoView.start();
		// videoView.seekTo(videoView.getDuration()/4);
		// videoView.setMediaController(mediaController);
		// videoView.requestFocus();

		// File video = new File("/mnt/sdcard/dianshi.mp4");
		// Log.d("TAG", video.exists() + "");
		// if (video.exists()) {
		// videoView.setVideoPath(video.getAbsolutePath());
		// videoView.setMediaController(mediaController);
		// mediaController.setMediaPlayer(videoView);
		// videoView.requestFocus();
		// }
		// bn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Log.d("TGA", videoView.getDuration() + "");
		// videoView.seekTo(videoView.getDuration() / 2);
		// }
		// });

	}

}
