package com.iof.showlib.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iof.showlib.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Util
{
	public static String mIMEI;
	private static Toast mToast;
	
	private static BufferedWriter mWriter;

	public static void ShowToast(Context context, String content)
	{
		if (mToast != null)
		{
			mToast.cancel();
			mToast = null;
		}
		
		View root = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.toast, null);
		TextView text = (TextView)root.findViewById(R.id.toast_text);
		text.setText(content);
		
		mToast = new Toast(context);
		mToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 540);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.setView(root);
		mToast.show();
	}
	
	/**
	 * 从文件读取对象
	 * @param path
	 * @return
	 */
	public static Object readObjectFile(String path)
	{
		Object obj = null; 
		try
		{ 
			File f = new File(path); 
			if(!f.exists())
				return null;
			
			FileInputStream is = new FileInputStream(path); 
			ObjectInputStream ois = new ObjectInputStream(is); 
			obj = ois.readObject();
		}
		catch (Exception e) 
		{ 
			e.printStackTrace();
		} 
	
		return obj; 
	}
	
	/**
	 * 将对象保存到文件
	 * @param object
	 * @param absPath
	 * @return
	 */
	public static boolean saveFile(Object object, String absPath) 
	{ 
		if(null == object || null == absPath || absPath.length() <= 0)
		{
			return false;
		}
		
		File file = new File(absPath);
		String parentPath = file.getParent();
				
		if(null != parentPath && !openOrCreatDir(parentPath))
			return false;
		
		FileOutputStream os = null;
		ObjectOutputStream oos = null;
		
		try 
		{
			os = new FileOutputStream(absPath);
			
			oos = new ObjectOutputStream(os);
			oos.writeObject(object);
			
			oos.close();
			os.close();
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 打开目录，如果不存在则创建
	 * @param path
	 * @return
	 */
	public static boolean openOrCreatDir(String path)
	{
		File file = new File(path);

		if (!file.exists())
		{
			if (!file.mkdirs())
			{
				return false;
			}
			try
			{
				String command = "chmod 777 " + file;
				Runtime runtime = Runtime.getRuntime();
				runtime.exec(command);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return true;
		}

		return file.isDirectory();
	}
	
	/**
	 * 删除目录（包括里面的文件）
	 * @param file
	 */
	public static void deleteDir(File file)
	{
		try
		{
			if (file.isDirectory())
			{
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++)
				{
					if (files[i].isFile())
					{
						files[i].delete();
					}
					else
					{
						deleteDir(files[i]);
					}
				}
			}
			file.delete();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 安装软件
	 */
	public static void installPak(Context context, String file)
	{
		if (context == null || file == null || file.length() == 0)
		{
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	/**
	 * 软件是否安装
	 * @param pkg
	 * @return
	 */
	public static String isInstalled(Context context, String pkg)
	{
		try
		{
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkg);
			if (intent != null)
			{
				return intent.getComponent().getClassName();
			}
		}
		catch (Exception e)
		{
		}
		return null;
	}
	
	/**
	 * 运行app
	 * @param context
	 * @param pkg
	 * @param cls
	 */
	public static void runApp(Context context, String pkg, String cls)
	{
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		intent.setComponent(new ComponentName(pkg, cls));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivity(intent);
	}
	
	/**
	 * 获取文件大小的字符串形式
	 * @param size
	 * @return
	 */
	public static String getSizeString(int size)
	{
		StringBuffer sb = new StringBuffer();
		
		if (size < 1024)
		{
			sb.append(size).append('B');
		}
		else if(size < 1048576)
		{
			int di = (size % 1024) * 100 / 1024;
			if (di == 0)
			{
				sb.append(size / 1024).append("KB");
			}
			else
			{
				sb.append(size / 1024).append('.').append(di).append("KB");
			}
		}
		else
		{
			int di = (size % 1048576) * 100 / 1048576;
			if (di == 0)
			{
				sb.append(size / 1048576).append("MB");
			}
			else
			{
				sb.append(size / 1048576).append('.').append(di).append("MB");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 获取手机串号
	 */
	public static void GetPhoneInfo(Context context)
	{
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		mIMEI = manager.getDeviceId();
	}
	
	public static String Match(String content, String reg)
	{
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(content);
		String value = "";
		if(matcher.find())
		{
			value = matcher.group();
		}
		return value;
	}
	public static boolean bitmapToFile(Bitmap bitmap, String absPath, CompressFormat format)
	{
		if (null == bitmap || null == absPath)
			return false;

		File file = new File(absPath);
		String parent = file.getParent();
		if (null != parent && !openOrCreatDir(parent))
		{
			return false;
		}

		try
		{
			if (!file.createNewFile())
				return false;
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
			return false;
		}

		try
		{
			FileOutputStream fos = new FileOutputStream(file);

			bitmap.compress(format, 75, fos);

			fos.flush();
			fos.close();

		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static void testTrace()
	{
		try
		{
			int[] t = new int[3];
			t[5] = 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void startDebug()
	{
		stopDebug();
		try
		{
			mWriter = new BufferedWriter(new FileWriter("/sdcard/debug.txt"));
		}
		catch (Exception e)
		{
		}
	}
	
	public static void debug(String msg)
	{
		try
		{
			if (mWriter != null)
			{
				mWriter.write(msg);
				mWriter.write('\n');
				mWriter.flush();
			}
		}
		catch (Exception e)
		{
		}
	}
	
	public static void stopDebug()
	{
		if (mWriter != null)
		{
			try
			{
				mWriter.close();
				mWriter = null;
			}
			catch (Exception e)
			{
			}
		}
	}
}
