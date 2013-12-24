package com.iof.showlib.views;

import com.iof.showlib.obj.Res;

import android.content.Context;
import android.widget.RelativeLayout;

public abstract class ViewBase extends RelativeLayout
{
	protected Res mRes;
	
	public ViewBase(Context context)
	{
		super(context);
	}
	
	/**
	 * 设置资源
	 * @param res
	 */
	public void setRes(Res res)
	{
		mRes = res;
	}
	
	/**
	 * 获取组件对应的资源类型
	 * @return
	 */
	public int getResType()
	{
		return Res.TYPE_NONE;
	}
	
	/**
	 * 隐藏
	 */
	public void onPause()
	{
	}
	
	/**
	 * 恢复
	 */
	public void onResume()
	{
	}
	
	/**
	 * 显示组件时调用
	 * @param arg1
	 * @param arg2
	 * @param obj
	 */
	abstract public String enter(int arg1, int arg2, Object obj);
	/**
	 * 清理组件
	 */
	abstract public void release();
}
