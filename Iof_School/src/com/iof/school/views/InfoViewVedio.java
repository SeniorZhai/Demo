package com.iof.school.views;

import com.iof.school.R;
import com.iof.showlib.obj.Res;
import com.iof.showlib.obj.ResVideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

public class InfoViewVedio extends InfoViewBase
{
	private TextView mViewDuration;

	public InfoViewVedio(Context context)
	{
		super(context);
		((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.info_view_vedio, this);
		
		mViewDuration = (TextView)findViewById(R.id.info_vedio_duration);
	}

	@Override
	public void initInfo(Res res)
	{
		try
		{
			ResVideo resVideo = (ResVideo)res;
			
			if (resVideo.mDuration > 0)
			{
				mViewDuration.setText(String.format("时长：%02d:%02d", resVideo.mDuration / 60, resVideo.mDuration % 60));
			}
			else
			{
				mViewDuration.setText("时长：未知");
			}
		}
		catch (Exception e)
		{
		}
	}
}
