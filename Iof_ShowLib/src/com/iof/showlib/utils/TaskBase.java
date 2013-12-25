package com.iof.showlib.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

public class TaskBase extends AsyncTask<String, Void, Integer>
{
	private boolean mIsStop;
	private Http mHttp;
	private Context mContext;
	private Parser mParser;
	private OnTaskFinishListener mListener;
	
	/**
	 * 创建一个任务
	 * @param context
	 * @param parser
	 */
	public TaskBase(Context context, Parser parser, OnTaskFinishListener l)
	{
		mContext = context;
		mParser = parser;
		mListener = l;
	}

	/**
	 * 0失败，1成功
	 */
	@Override
	protected Integer doInBackground(String... params)
	{
		if (mParser == null)
		{
			return 0;
		}
		try
		{
			String url = mParser.getUrl();
			if (url == null || url.length() == 0)
			{
				return 0;
			}
			mHttp = new Http();
			if (mHttp.get(mContext, url, mParser.getParam()))
			{
				if (mIsStop)
				{
					return 0;
				}
				StringBuffer sBuffer = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(mHttp.mIs));
				for (String s = reader.readLine(); s != null; s = reader.readLine())
				{
					sBuffer.append(s);
				}
				//解析
				mParser.parserJson(sBuffer.toString());
				
				return 1;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	protected void onPostExecute(Integer result)
	{
		super.onPostExecute(result);
		
		if (mListener != null)
		{
			mListener.onFinish(result, mIsStop);
		}
	}
	
	/**
	 * 停止任务
	 */
	public void stop()
	{
		mIsStop = true;
	}
	
	public interface Parser
	{
		/**
		 * 得到任务的url
		 * @return
		 */
		abstract public String getUrl();
		/**
		 * 得到任务的参数列表
		 * @return
		 */
		abstract public String getParam();
		/**
		 * 解析任务结果
		 * @param json
		 * @throws JSONException json数据解析异常
		 */
		abstract public void parserJson(String json) throws JSONException;
	}
	
	public interface OnTaskFinishListener
	{
		/**
		 * 任务结束回调(该回调在主线程执行)
		 * @param result 0失败，1成功
		 * @param isCancel
		 */
		abstract public void onFinish(int result, boolean isCancel);
	}
}
