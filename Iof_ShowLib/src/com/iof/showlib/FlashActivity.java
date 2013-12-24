package com.iof.showlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.iof.showlib.utils.Http;
import com.iof.showlib.utils.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FlashActivity extends Activity implements Handler.Callback
{
	private ProgressBar mProgressBar;
	private TextView mTextView;
	private Button mInstall;
	
	private Handler mHandler;

	private String mFile;
	private int mVersion;
	private int mFileSize;
	private int mDownSize;
	private int mState; //0-下载中；1-停止；2-完成
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash);
		
		init();
	}

	/**
	 * 初始化其它信息
	 */
	private void init()
	{
		mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
		mTextView = (TextView)findViewById(R.id.textview);
		mInstall = (Button)findViewById(R.id.install);
		mHandler = new Handler(this);
		
		try
		{
			mVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Util.GetPhoneInfo(this);
		
		new MainTask(this, getResources().getString(R.string.update_url)).execute("");
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		if (msg.what == 1000)
		{
			try
			{
				mProgressBar.setMax(mFileSize);
				mProgressBar.setProgress(mDownSize);
				
				int percent = 0;
				if (mFileSize > 0)
				{
					if (mFileSize <= mDownSize)
					{
						percent = 100;
					}
					else
					{
						percent = mDownSize * 100 / mFileSize;
					}
				}
				
				if (percent == 100)
				{
					mTextView.setText("大小：" + Util.getSizeString(mFileSize) + "   已下载完成");
					mInstall.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Util.installPak(FlashActivity.this, mFile);
						}
					});
					mInstall.setVisibility(View.VISIBLE);
				}
				else
				{
					mTextView.setText("大小：" + Util.getSizeString(mFileSize) + "   已下载：" + percent + "%");
				}
				
				return true;
			}
			catch (Exception e)
			{
			}
		}
		return false;
	}

	/**
	 * 进入大厅
	 */
	private void enterCenter()
	{
		try
		{
			Intent intent = new Intent(this, Class.forName(getResources().getString(R.string.main_act)));
			startActivity(intent);
			finish();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//TODO MainTask
	/**
	 * 检测版本操作
	 * @author Administrator
	 */
	private class MainTask extends AsyncTask<String, Void, Integer>
	{
		private Context mContext;
		private String mUrl; //检测升级地址
		private String mDownUrl;
		
		public MainTask(Context context, String url)
		{
			mContext = context;
			mUrl = url;
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			super.onPostExecute(result);
			
			if (result > 0)
			{
				mProgressBar.setVisibility(View.VISIBLE);
				mTextView.setText("开始下载升级程序");
				
				new DownTask(mContext, mDownUrl).execute("");
			}
			else if (result < 0)
			{
				Util.ShowToast(mContext, "连接服务器失败！");
			}
			else
			{
				enterCenter();
			}
		}

		/**
		 * 返回大于0，表示要升级，小于0表示失败，0表示为最新版本
		 */
		@Override
		protected Integer doInBackground(String... params)
		{
			Http http = new Http();
			if (http.get(mContext, mUrl, "version=" + mVersion))
			{
				try
				{
					StringBuffer sBuffer = new StringBuffer();
					BufferedReader reader = new BufferedReader(new InputStreamReader(http.mIs));
					for (String s = reader.readLine(); s != null; s = reader.readLine())
					{
						sBuffer.append(s);
					}
					JSONTokener jTokener = new JSONTokener(sBuffer.toString());
					JSONObject jObject;
					if ((jObject = (JSONObject)jTokener.nextValue()) != null)
					{
						if ((jObject = jObject.getJSONObject("response")) != null)
						{
							int ver = jObject.getInt("version");
							mDownUrl = jObject.getString("download");
							//TODO test
							//ver = 1;
							//test
							if (mVersion < ver)
							{
								return 1;
							}
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return -1;
				}
			}
			else
			{
				return -1;
			}
			return 0;
		}
	}
	
	//TODO DownTask
	/**
	 * 下载apk包
	 * @author Administrator
	 */
	private class DownTask extends AsyncTask<String, Integer, Void>
	{
		private Context mContext;
		private Http mHttp;
		private String mUrl;
		
		public DownTask(Context context, String url)
		{
			mContext = context;
			mUrl = url;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			
			if (mState == 2)
			{
				Util.installPak(mContext, mFile);
			}
			else
			{
				Util.ShowToast(mContext, "下载出错，请稍后再试");
			}
		}

		@Override
		protected Void doInBackground(String... params)
		{
			mFile = Environment.getExternalStorageDirectory().getPath()
					+ getResources().getString(R.string.update_apk_file);
			File file1 = new File(mFile);
			if (file1.isFile() && file1.exists())
			{
				file1.delete();
			}
			
			int startPos = 0;
			mDownSize = 0;

			RandomAccessFile file;
			InputStream inStream;
			mHttp = new Http();
			try
			{
				HttpURLConnection http = getDownConnection(startPos);
				if (http == null)
				{
					mState = 1;
					return null;
				}
				http.connect();

				if (http.getResponseCode() == 206 || http.getResponseCode() == 200)
				{
					mFileSize =  http.getContentLength() + startPos;
					inStream = http.getInputStream();

					Util.openOrCreatDir(file1.getParent());
					file = new RandomAccessFile(mFile, "rwd");
					try
					{
						String command = "chmod 777 " + mFile;
						Runtime runtime = Runtime.getRuntime();
						runtime.exec(command);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					file.seek(startPos);

					byte[] buffer = new byte[4096];
					int offset = 0;

					while ((offset = inStream.read(buffer)) != -1)
					{
						file.write(buffer, 0, offset);
						mDownSize += offset;

						sendMsg();
					}

					// 下载完成
					if (mDownSize >= mFileSize)
					{
						mState = 2;
					}
					else
					{
						mState = 1;
					}
					file.close();
					inStream.close();
				}
				else
				{
					throw new Exception();
				}

			}
			catch (SocketException e)
			{
				e.printStackTrace();
				mState = 1;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				mState = 1;
			}
			
			return null;
		}

		/**
		 * 打开下载连接
		 * @param startPos
		 * @return
		 */
		private HttpURLConnection getDownConnection(int startPos)
		{
			if (!mHttp.CheckNetwork(mContext))
			{
				this.publishProgress(1);
				mState = 1;
				return null;
			}
			HttpURLConnection conn = null;

			try
			{
				String proxyHost = android.net.Proxy.getDefaultHost();
				if (proxyHost != null)
				{
					java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(
							android.net.Proxy.getDefaultHost(), android.net.Proxy.getDefaultPort()));
					conn = (HttpURLConnection) new URL(mUrl).openConnection(p);
				}
				else
				{
					conn = (HttpURLConnection) new URL(mUrl).openConnection();
				}
			}
			catch (Exception e)
			{
				return null;
			}
			conn.setConnectTimeout(20 * 1000);
			conn.setRequestProperty("Range", "bytes=" + startPos + "-");
			return conn;
		}
		
		private void sendMsg()
		{
			mHandler.sendEmptyMessage(1000);
		}
	}
}