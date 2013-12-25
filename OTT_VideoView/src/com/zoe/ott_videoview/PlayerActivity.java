package com.zoe.ott_videoview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.zoe.ott_videoview.data.MovieJsonData;
import com.zoe.ott_videoview.data.PlayerGetDataTask;

public class PlayerActivity extends Activity {
	private static final String TAG = "---TAG---PlayerActivity---";
	private VideoView videoView;
	private MediaController mediaController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_layout);
		mediaController = new MediaController(this);
		videoView = (VideoView) findViewById(R.id.videoView);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);

		Intent intent = getIntent();
		String str = intent.getStringExtra("url");
		MovieJsonData data = new MovieJsonData();
		new PlayerGetDataTask(data, videoView).execute(str);

	}
}
