package com.iof.school.obj;

import android.os.Handler;
import android.os.Message;

import com.iof.school.utils.Const;
import com.iof.showlib.obj.Res;
import com.iof.showlib.utils.ImageDownload.DownFile;

public class ResLogoFile implements DownFile
{
	private Res mRes;
	private Handler mHandler;
	private int mIndex;
	
	public ResLogoFile(Res res, Handler handler, int index)
	{
		mRes = res;
		mHandler = handler;
		mIndex = index;
	}

	@Override
	public String getUrl()
	{
		if (mRes == null)
		{
			return null;
		}
		return Const.URL + mRes.mLogoUrl;
	}

	@Override
	public String getPath()
	{
		return Const.PATH_LOGO + mRes.mId + ".png";
	}

	@Override
	public void onFinish(boolean isOk)
	{
		Message msg = new Message();
		msg.what = Const.HD_ID_LOGO_DOWN;
		msg.arg1 = isOk ? 1 : 0;
		msg.arg2 = mIndex;
		msg.obj = mRes;
		mHandler.sendMessage(msg);
	}
}
