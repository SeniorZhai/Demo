package com.iof.showlib.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * http连接类
 */
public class Http
{
	public InputStream mIs;
	public int mErr = 0; //0-正常；1-没有网络；2-连接失败；3-网络异常
	
	/**
	 * 打开连接
	 */
	public HttpURLConnection getConnection(Context context, String url, String params)
	{
		if(!CheckNetwork(context))
		{
			return null;
		}
		
		if (params == null || params.length() == 0)
		{
			url += "?";
		}
		else
		{
			url += "?" + params + "&";
		}
		url += "deviceSerial=" + Util.mIMEI;
		
		System.out.println("------------url=" + url);
		HttpURLConnection conn;

		try
		{
			String proxyHost = android.net.Proxy.getDefaultHost();
			if (proxyHost != null)
			{
				java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(
						android.net.Proxy.getDefaultHost(), android.net.Proxy.getDefaultPort()));
				conn = (HttpURLConnection) new URL(url).openConnection(p);
			}
			else
			{
				conn = (HttpURLConnection) new URL(url).openConnection();
			}
			
			return conn;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		mErr = 2;
		return null;
	}
	
	public boolean get(Context context, String url, String params)
	{
		HttpURLConnection conn = getConnection(context, url, params);
		
		if(null == conn)
		{
			return false;
		}
		
		int connCount = 0;  //增加如果连接失败，则重连一次
		
		while(connCount < 2)
		{
			try
			{			
				conn.setDoInput(true); 
				conn.setDoOutput(true);
				
				conn.setConnectTimeout(15000);
		        conn.setReadTimeout(15000);
		        
		        conn.setInstanceFollowRedirects(true);  
		        
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
				conn.setRequestProperty("Accept-Language", "zh-CN");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");			
				conn.setRequestProperty("Connection", "Keep-Alive");
				
				mIs = conn.getInputStream();
		        
		        break;
			}
			catch(Exception e)
			{
				mErr = 3;
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 检测网络
	 * @param context
	 * @return
	 */
	public boolean CheckNetwork(Context context)
	{
		if(null == context)
		{
			mErr = 1;
			return false;
		}
		
		ConnectivityManager cwjManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);    
		NetworkInfo info = cwjManager.getActiveNetworkInfo(); 
		if (info != null && info.isAvailable())	//有联网
		{
			mErr = 0;
	        return true;
		}
		mErr = 1;
		return false;
	}
}
