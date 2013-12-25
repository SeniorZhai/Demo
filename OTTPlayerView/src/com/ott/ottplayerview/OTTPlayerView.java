package com.ott.ottplayerview;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

public class OTTPlayerView extends SurfaceView implements
		SurfaceHolder.Callback, OnCompletionListener, OnPreparedListener,
		OnBufferingUpdateListener {
	private static final String TAG = "OttPlayerView";
	int bufferd;
	private Context context;
	int currentPositon;
	int duration;
	float hasBufferd;
	float hasSee;
	public Handler handler;
	boolean isBuffering;
	boolean isPlaying;
	private MediaPlayer mediaPlayer;
	private SeekBar seekBar;
	protected SurfaceHolder surfaceHolder;
	private int totalTime = -1;
	private String videoUrl;

	// 构造函数
	public OTTPlayerView(Context context) {
		super(context);
		this.context = context;
		initVideoView();
	}

	public OTTPlayerView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;
		initVideoView();
	}

	public OTTPlayerView(Context context, AttributeSet attributeSet,
			int paramInt) {
		super(context, attributeSet, paramInt);
		this.context = context;
		initVideoView();
	}

	// 初始化函数
	private void initVideoView() {
		surfaceHolder = getHolder();
		surfaceHolder.setType(3);
		surfaceHolder.addCallback(this);
	}

	public int getCurrentPositon() {
		if (mediaPlayer != null)
			return mediaPlayer.getCurrentPosition();
		else
			return 0;
	}

	public int getDuration() {
		if (mediaPlayer != null) {
			duration = mediaPlayer.getDuration();
		} else {
			return 0;
		}
		return duration;
	}

	public boolean isBuffer() {
		return isBuffering;
	}

	public boolean isPlaying() {
		if (mediaPlayer != null)
			return mediaPlayer.isPlaying();
		return false;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (percent >= 100)
			;
		for (isBuffering = false;; isBuffering = true)
			return;

	}

	private void onCompletion() {
		Log.v(TAG, "00");
		if ((this.handler != null) && (mediaPlayer.getDuration() > 0)) {
			Message message = Message.obtain();
			message.what = 100;
			handler.sendMessage(message);
		}

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mediaPlayer.start();
		if (totalTime < 0) {
			totalTime = mediaPlayer.getDuration();
		}

	}

	public void openVideo() {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.reset();
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.setAudioStreamType(3);
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnCompletionListener(this);
		} catch (Exception e) {
			while (true)
				Log.e(TAG, e.getMessage());
		}
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public void release(boolean paramBoolean) {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public void seekBy(int paramInt) {
		seekTo(getCurrentPositon() + paramInt);
		Log.d(TAG, getCurrentPositon() + "");
	}

	public void seekTo(int paramInt) {
		if (paramInt >= 0) {
			int i = getDuration();
			if (paramInt <= i)
				mediaPlayer.seekTo(paramInt);
		} else if (paramInt < 0)
			mediaPlayer.seekTo(0);
		int j = getDuration();
		if (paramInt > j) {
			mediaPlayer.seekTo(getDuration());
		}
		if (!isPlaying) {
			start();
		}
	}

	public void setSeekBar(SeekBar paramSeekBar) {
		this.seekBar = paramSeekBar;
	}

	public void setVideoURL(String paramString) {
		this.videoUrl = paramString;
	}

	public void start() {
		if ((this.mediaPlayer != null) && (!this.mediaPlayer.isPlaying()))
			this.mediaPlayer.start();
		while (true) {
			openVideo();
			return;

		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		openVideo();
		int i = Log.i(TAG, "OTTPlayerView has been Created");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		int i = Log.i(TAG, "OTTPlayerView has been destroyed");

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

}
