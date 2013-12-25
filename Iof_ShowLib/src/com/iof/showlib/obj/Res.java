package com.iof.showlib.obj;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Res implements Serializable
{
	private static final long serialVersionUID = 8809776954460913764L;
	//类型
	public static final int TYPE_NONE = 0;
	public static final int TYPE_VIDEO = 1;
	public static final int TYPE_AUDIO = 2;
	public static final int TYPE_FLASH = 3;
	public static final int TYPE_PIC = 4;
	public static final int TYPE_TXT = 5;
	public static final int TYPE_TXT_PIC = 6;
	public static final int TYPE_APK = 100;
	//收费类型
	public static final int CHAEGE_TYPE_FREE = 0;
	public static final int CHAEGE_TYPE_ONCE = 2;
	public static final int CHAEGE_TYPE_PART = 3;
	public static final int CHAEGE_TYPE_TRYOUT = 4;
	public static final int CHAEGE_TYPE_TIME = 5;
	public static final int CHAEGE_TYPE_TIMES = 6;
	public static final int CHAEGE_TYPE_PROP = 100;
	
	/**
	 * id
	 */
	public int mId;
	/**
	 * 类型：1-视频；2-音频；3-flash；4-图片；5-文本；6-图文混排；100-apk
	 */
	public int mType;
	/**
	 * 收费类型：0-免费；2-收费开通；3-部分免费；4-免费试用；5-时间收费；6-按次收费；100-道具收费
	 */
	public int mChargeType;
	/**
	 * 价格
	 */
	public float mPrice;
	/**
	 * 名字
	 */
	public String mName;
	/**
	 * 图标下载地址
	 */
	public String mLogoUrl;
	/**
	 * 描述
	 */
	public String mIntro;
	/**
	 * 适合年龄段
	 */
	private int mMinAge, mMaxAge;
	/**
	 * 是否已经购买
	 */
	public boolean mHasPay;
	
	public static Res getInstance(int type)
	{
		switch (type)
		{
		case TYPE_VIDEO:
			return new ResVideo();
		}
		
		return null;
	}
	
	/**
	 * 解析json文件
	 * @param jObject json数据
	 * @param isBase 是否为基础数据，即不包含类型的自有数据
	 * @throws JSONException
	 */
	public void parserJson(JSONObject jObject, boolean isBase) throws JSONException
	{
		mId = jObject.getInt("id");
		mType = jObject.getInt("type");
		mChargeType = jObject.getInt("free");
		mName = jObject.getString("name");
		mLogoUrl = jObject.getString("logo");
		mHasPay = jObject.getInt("purchaseState") == 1;
		
		int tmp = jObject.getInt("minSuitAgeLimit");
		if (tmp == 0)
		{
			mMinAge = -1;
		}
		else
		{
			mMinAge = jObject.getInt("minSuitYear") * 12 + jObject.getInt("minSuitMonth");
		}
		tmp = jObject.getInt("maxSuitAgeLimit");
		if (tmp == 0)
		{
			mMaxAge = -1;
		}
		else
		{
			mMaxAge = jObject.getInt("maxSuitYear") * 12 + jObject.getInt("maxSuitMonth");
		}
		
		if (!isFree())
		{
			mPrice = (float)jObject.getDouble("price");
		}
		
		if (!isBase)
		{
			mIntro = jObject.getString("description");
		}
	}
	
	public boolean isFree()
	{
		return mChargeType == CHAEGE_TYPE_FREE;
	}
	
	public String getAge()
	{
		StringBuffer sb = new StringBuffer();
		if (mMinAge <= 0 && mMaxAge <= 0)
		{
			return "";
		}
		int tmp;
		boolean mHasLine = false;
		if (mMinAge <= 0)
		{
			sb.append("小于");
		}
		else if (mMaxAge <= 0)
		{
			sb.append("大于");
		}
		else
		{
			mHasLine = true;
		}
		if (mMinAge > 0)
		{
			tmp = mMinAge / 12;
			if (tmp > 0)
			{
				sb.append(tmp);
				sb.append('岁');
			}
			tmp = mMinAge % 12;
			if (tmp > 0)
			{
				sb.append(tmp);
				sb.append("个月");
			}
		}
		if (mHasLine)
		{
			sb.append('~');
		}
		if (mMaxAge > 0)
		{
			tmp = mMaxAge / 12;
			if (tmp > 0)
			{
				sb.append(tmp);
				sb.append('岁');
			}
			tmp = mMaxAge % 12;
			if (tmp > 0)
			{
				sb.append(tmp);
				sb.append("个月");
			}
		}
		return sb.toString();
	}
}
