package com.iof.showlib.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.LinkedList;

import android.content.Context;

public class ImageDownload implements Runnable
{
	private Context mContext;
	private LinkedList<DownFile> mDownFiles = new LinkedList<DownFile>();

	private boolean mIsRun;
	private boolean mIsPause;

	public ImageDownload(Context context)
	{
		mContext = context;
	}
	
	/**
	 * 添加下载任务
	 * @param bFirst
	 * @param file
	 */
	public void addFile(boolean bFirst, DownFile file)
	{
		//System.out.println("-------ImageDownload--addFile()=" + file.getUrl());
		synchronized (mDownFiles)
		{
			if (bFirst)
			{
				mDownFiles.addFirst(file);
			}
			else
			{
				mDownFiles.add(file);
			}
			mDownFiles.notify();
		}
	}
	
	public void delFile(DownFile file)
	{
		synchronized (mDownFiles)
		{
			mDownFiles.remove(file);
		}
	}

	public void start()
	{
		if (!mIsRun)
		{
			mIsRun = true;
			new Thread(this).start();
		}
	}

	public void stop()
	{
		mIsRun = false;
		synchronized (mDownFiles)
		{
			mDownFiles.notify();
		}
	}
	
	public void pause()
	{
		mIsPause = true;
	}
	
	public void resume()
	{
		mIsPause = false;
		synchronized (mDownFiles)
		{
			mDownFiles.notify();
		}
	}
	
	public boolean isRun()
	{
		return mIsRun;
	}

	@Override
	public void run()
	{
		DownFile file = null;
		while (mIsRun)
		{
			//System.out.println("-------ImageDownload--run()");
			synchronized(mDownFiles)
			{
				if (mIsPause || mDownFiles.size() == 0)
				{
					try
					{
						mDownFiles.wait();
					}
					catch (Exception e)
					{
					}
					continue;
				}
				//System.out.println("-------ImageDownload--run()--111111");
				file = mDownFiles.removeFirst();
			}
			if (file == null)
			{
				continue;
			}

			file.onFinish(load(mContext, file.getUrl(), file.getPath()));
		}
	}

	public boolean load(Context context, final String urlStr, final String absPath)
	{
		if (urlStr == null || absPath == null)
		{
			return false;
		}
		
		final File file = new File(absPath);
		//System.out.println("-------ImageDownload--url=" + urlStr);

		if (file.exists())
		{
			return false;
		}

		Util.openOrCreatDir(file.getParent());

		try
		{
			Http http = new Http();
			HttpURLConnection urlConn = http.getConnection(context, urlStr, null);

			if (null == urlConn)
			{
				return false;
			}

			InputStream inputStream = urlConn.getInputStream();

			final int BUFSIZE = 1024 * 4;
			byte[] buffer = new byte[BUFSIZE];

			OutputStream output = new FileOutputStream(file);
			int readed = 0;

			while (mIsRun && (readed = inputStream.read(buffer)) != -1)
			{
				output.write(buffer, 0, readed);
			}

			output.flush();
			output.close();

			int resCode = urlConn.getResponseCode();
			urlConn.disconnect();

			if (!mIsRun)
			{
				file.delete();
			}
			else if (200 == resCode)
			{
				//TODO 生成缩略图
				return true;
			}
			else
			{
				file.delete();
			}
		}
		catch (IOException e)
		{
			return false;
		}

		return false;
	}
	
	public interface DownFile
	{
		/**
		 * 获取文件下载地址
		 * @return
		 */
		public String getUrl();
		/**
		 * 获取文件保存路径
		 * @return
		 */
		public String getPath();
		/**
		 * 文件下载结束回调(注意：该回调不在主线程)
		 * @param isOk
		 */
		public void onFinish(boolean isOk);
	}
}
