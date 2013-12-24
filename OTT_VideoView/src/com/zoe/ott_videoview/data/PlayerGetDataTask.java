package com.zoe.ott_videoview.data;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class PlayerGetDataTask extends AsyncTask<String, Integer, Void> {
	private MovieJsonData data;
	private VideoView videoView;
	private String str;

	public PlayerGetDataTask(MovieJsonData data, VideoView videoView) {
		this.data = data;
		this.videoView = videoView;
	}

	@Override
	protected Void doInBackground(String... params) {
		str = GetData.getData(params[0]);
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONObject data = (JSONObject)jsonParser.nextValue();
			String ip_server = data.getString("ip_server");
			String sites = data.getString("sites");
			str = ip_server+sites;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		videoView.requestFocus();
		videoView.setVideoURI(Uri
				.parse(str));
		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				Log.d("TAG", "º”‘ÿÕÍ≥…");		
				if (!videoView.isPlaying())
					Log.d("TAG", "Play-------");
					videoView.start();
			}
		});	
	}
}
