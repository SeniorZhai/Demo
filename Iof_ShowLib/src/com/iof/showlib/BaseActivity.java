package com.iof.showlib;

import com.iof.showlib.obj.Res;
import com.iof.showlib.utils.Util;
import com.iof.showlib.views.ViewBase;
import com.iof.showlib.views.ViewVideo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ViewFlipper;

/**
 * 基础窗口类
 * </br>里面第一个界面为资源列表界面(包含列表、资源信息等)
 * </br>其它界面为资源显示界面
 */
public abstract class BaseActivity extends Activity
{
	private ViewFlipper mFlipper;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mFlipper = (ViewFlipper)findViewById(R.id.viewflipper);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		try
		{
			((ViewBase)mFlipper.getChildAt(0)).onPause();
			if (mFlipper.getDisplayedChild() != 0)
			{
				((ViewBase)mFlipper.getChildAt(mFlipper.getDisplayedChild())).onPause();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		try
		{
			((ViewBase)mFlipper.getChildAt(0)).onResume();
			if (mFlipper.getDisplayedChild() != 0)
			{
				((ViewBase)mFlipper.getChildAt(mFlipper.getDisplayedChild())).onResume();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		try
		{
			((ViewBase)mFlipper.getChildAt(mFlipper.getDisplayedChild())).onKeyDown(keyCode, event);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 找到ViewFlipper中指定的指定界面
	 * @param type 资源类型
	 * @return
	 */
	private int findViewInFlipper(int type)
	{
		for (int i = 0; i < mFlipper.getChildCount(); i++)
		{
			if (((ViewBase)mFlipper.getChildAt(i)).getResType() == type)
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 生成指定界面
	 * @param type
	 * @return
	 */
	private ViewBase createView(int type)
	{
		switch (type)
		{
		case Res.TYPE_VIDEO:
			return new ViewVideo(this);
		}
		
		return null;
	}
	
	/**
	 * 切换到指定的界面。
	 * </br>如果界面已经创建，则显示改界面，否则会先创建界面。
	 * @param res 要加载的资源，如果为空则认为是返回资源列表界面
	 * @param arg1 保留参数，相关子类自己定义
	 * @param arg2 保留参数，相关子类自己定义
	 * @param obj 保留参数，相关子类自己定义
	 */
	final public void showView(Res res, int arg1, int arg2, Object obj)
	{
		if (res == null) //显示资源列表
		{
			ViewBase view = (ViewBase)mFlipper.getChildAt(0);
			view.enter(arg1, arg2, obj);
			mFlipper.setDisplayedChild(0);
			return;
		}
		
		if (mFlipper.getChildCount() == 0)
		{
			throw new IllegalArgumentException("Please call showResListView() first!");
		}
		
		int index = findViewInFlipper(res.mType);
		if (index > 0)
		{
			ViewBase view = (ViewBase)mFlipper.getChildAt(index);
			view.setRes(res);
			String result = view.enter(arg1, arg2, obj);
			if (result == null)
			{
				mFlipper.setDisplayedChild(index);
			}
			else
			{
				Util.ShowToast(this, result);
			}
			return;
		}
		
		try
		{
			ViewBase view = createView(res.mType);
			if (view != null)
			{
				view.setRes(res);
				String result = view.enter(arg1, arg2, obj);
				view.enter(arg1, arg2, obj);
				mFlipper.addView(view);
				if (result == null)
				{
					mFlipper.setDisplayedChild(mFlipper.getChildCount() - 1);
				}
				else
				{
					Util.ShowToast(this, result);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 显示资源列表界面，只能调用一次，且在{@link #showView}方法之前调用
	 * @param view
	 */
	final public void showHomeView(ViewBase view)
	{
		if (mFlipper.getChildCount() > 0)
		{
			return;
		}
		mFlipper.addView(view);
		mFlipper.setDisplayedChild(0);
	}
	
	/**
	 * 销毁指定界面
	 * @param view
	 */
	final public void dropView(ViewBase view)
	{
		if (view == null)
		{
			return;
		}
		
		mFlipper.removeView(view);
		view.release();
	}
	
	/**
	 * 退出程序
	 */
	public void exit()
	{
		finish();
		System.exit(0);
	}
}