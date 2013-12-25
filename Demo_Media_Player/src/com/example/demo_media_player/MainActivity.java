package com.example.demo_media_player;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.MediaController.MediaPlayerControl;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
	private MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mediaPlayer = new MediaPlayer();
		final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceView.setKeepScreenOn(true);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(this);
//		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.setFixedSize(480, 300);
		Button playButton = (Button) findViewById(R.id.play);
		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mediaPlayer.start();
			}
		});
		Button skipButton = (Button) findViewById(R.id.skip);
		skipButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mediaPlayer.seekTo(mediaPlayer.getDuration()/7);
			}
		});
		
		Button pauseButton = (Button)findViewById(R.id.pause);
	    pauseButton.setOnClickListener(new OnClickListener() {
	      public void onClick(View v) {
	        mediaPlayer.pause();        
	      }
	    });
	     MediaController mediaController = new MediaController(this);
		    mediaController.setMediaPlayer(new MediaPlayerControl() {
		      public boolean canPause() {
		        return true;
		      }

		      public boolean canSeekBackward() {
		        return true;
		      }

		      public boolean canSeekForward() {
		        return true;
		      }

		      public int getBufferPercentage() {
		        return 0;
		      }

		      public int getCurrentPosition() {
		        return mediaPlayer.getCurrentPosition();
		      }

		      public int getDuration() {
		        return mediaPlayer.getDuration();
		      }

		      public boolean isPlaying() {
		        return mediaPlayer.isPlaying();
		      }

		      public void pause() {
		        mediaPlayer.pause();
		      }

		      public void seekTo(int pos) {
		        mediaPlayer.seekTo(pos);
		      }

		      public void start() {
		        mediaPlayer.start();
		      }
			@Override
			public int getAudioSessionId() {
				// TODO Auto-generated method stub
				return 0;
			}
		    });    
		    mediaController.show();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {


	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mediaPlayer.setDisplay(holder);
			mediaPlayer.setDataSource(this, Uri
					.parse("rtsp://192.168.1.67/1/dianying_x10001_SayYes.mp4"));
			mediaPlayer.prepare();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mediaPlayer.release();
	}
}