package com.iof.showlib.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.iof.showlib.BaseActivity;
import com.iof.showlib.R;
import com.iof.showlib.obj.Res;
import com.iof.showlib.obj.ResVideo;
import com.iof.showlib.utils.Util;

public class ViewVideo extends ViewBase
{
	private BaseActivity mAct;
	private VideoView mVideoView;
	private View mViewPause;
	private TextView mViewTimePlayed, mViewTimeTotal;
	private ProgressBar mProgressBar;
	
	private boolean mPause;
	
	public ViewVideo(BaseActivity act)
	{
		super(act);
		mAct = act;
		((LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_video, this);
		
		mVideoView = (VideoView)findViewById(R.id.view_video_videoview);
		mViewPause = findViewById(R.id.view_video_pause);
		mViewTimePlayed = (TextView)findViewById(R.id.view_video_time_played);
		mViewTimeTotal = (TextView)findViewById(R.id.view_video_time_total);
		mProgressBar = (ProgressBar)findViewById(R.id.view_video_progressbar);
		
		mVideoView.setOnErrorListener(mErrorListener);
		mVideoView.setOnCompletionListener(mCompletionListener);
	}

	@Override
	public String enter(int arg1, int arg2, Object obj)
	{
		if (((ResVideo)mRes).mUrl == null || ((ResVideo)mRes).mUrl.length() < 10)
		{
			return "播放失败";
		}
		mVideoView.setVideoPath(((ResVideo)mRes).mUrl);
		mVideoView.requestFocus();
		mVideoView.start();
		
		ctrlShow(false);
		mPause = false;
		
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (mVideoView.isPlaying())
			{
				mVideoView.pause();
				ctrlShow(true);
			}
			else
			{
				mVideoView.start();
				ctrlShow(false);
			}
			break;
			
		case KeyEvent.KEYCODE_BACK:
			goBack();
			break;
		}
		return true;
	}

	@Override
	public void release()
	{
	}

	@Override
	public int getResType()
	{
		return Res.TYPE_VIDEO;
	}

	@Override
	public void onPause()
	{
		if (mVideoView.isPlaying())
		{
			mVideoView.pause();
			ctrlShow(true);
			mPause = true;
		}
	}

	@Override
	public void onResume()
	{
		if (mPause)
		{
			mPause = false;
			mVideoView.start();
			ctrlShow(false);
		}
	}
	
	private void goBack()
	{
		try
		{
			mVideoView.stopPlayback();
		}
		catch (Exception e)
		{
		}
		mAct.showView(null, 0, 0, null);
	}
	
	private void ctrlShow(boolean bShow)
	{
		int v = GONE;
		if (bShow)
		{
			v = VISIBLE;
		}
		mViewPause.setVisibility(v);
		mViewTimePlayed.setVisibility(v);
		mViewTimeTotal.setVisibility(v);
		mProgressBar.setVisibility(v);
		
		if (bShow)
		{
			int time = mVideoView.getDuration() / 1000;
			mViewTimeTotal.setText(String.format("%02d:%02d:%02d", time / 3600, time % 3600 / 60, time % 60));
			mProgressBar.setMax(time);

			time = mVideoView.getCurrentPosition() / 1000;
			mViewTimePlayed.setText(String.format("%02d:%02d:%02d", time / 3600, time % 3600 / 60, time % 60));
			mProgressBar.setProgress(time);
		}
	}
	
	private OnErrorListener mErrorListener = new OnErrorListener()
	{
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra)
		{
			Util.ShowToast(mAct, "播放出错");
			goBack();
			return true;
		}
	};
	
	private OnCompletionListener mCompletionListener = new OnCompletionListener()
	{
		@Override
		public void onCompletion(MediaPlayer mp)
		{
			goBack();
		}
	};
}
