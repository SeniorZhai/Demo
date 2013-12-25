package com.example.demo_videoview;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
	VideoView videoView;
	MediaController mediaController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		videoView = (VideoView) findViewById(R.id.videoView);
		mediaController = new MediaController(this);
		videoView.requestFocus();
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);

		videoView.setVideoURI(Uri
				.parse("rtsp://192.168.1.67/1/dianying_x10001_SayYes.mp4"));
		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				Log.d("TAG", "onPrepared-------");
				if (!videoView.isPlaying())
					Log.d("TAG", "Play-------");
					videoView.start();
			}
		});
		Button button = (Button) findViewById(R.id.bn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int temp = videoView.getDuration();
				Log.d("TAG",temp+"");
				videoView.seekTo(temp/3);
				if (!videoView.isPlaying())
					Log.d("TAG","play");
					videoView.start();			
			}
		});
	}

}
