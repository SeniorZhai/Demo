package com.iof.showlib.obj;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Res implements Serializable
{
	private static final long serialVersionUID = 8809776954460913764L;
	//����
	public static final int TYPE_NONE = 0;
	public static final int TYPE_VIDEO = 1;
	public static final int TYPE_AUDIO = 2;
	public static final int TYPE_FLASH = 3;
	public static final int TYPE_PIC = 4;
	public static final int TYPE_TXT = 5;
	public static final int TYPE_TXT_PIC = 6;
	public static final int TYPE_APK = 100;
	//�շ�����
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
	 * ���ͣ�1-��Ƶ��2-��Ƶ��3-flash��4-ͼƬ��5-�ı���6-ͼ�Ļ��ţ�100-apk
	 */
	public int mType;
	/**
	 * �շ����ͣ�0-��ѣ�2-�շѿ�ͨ��3-������ѣ�4-������ã�5-ʱ���շѣ�6-�����շѣ�100-�����շ�
	 */
	public int mChargeType;
	/**
	 * �۸�
	 */
	public float mPrice;
	/**
	 * ����
	 */
	public String mName;
	/**
	 * ͼ�����ص�ַ
	 */
	public String mLogoUrl;
	/**
	 * ����
	 */
	public String mIntro;
	/**
	 * �ʺ������
	 */
	private int mMinAge, mMaxAge;
	/**
	 * �Ƿ��Ѿ�����
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
	 * ����json�ļ�
	 * @param jObject json����
	 * @param isBase �Ƿ�Ϊ�������ݣ������������͵���������
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
			sb.append("С��");
		}
		else if (mMaxAge <= 0)
		{
			sb.append("����");
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
				sb.append('��');
			}
			tmp = mMinAge % 12;
			if (tmp > 0)
			{
				sb.append(tmp);
				sb.append("����");
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
				sb.append('��');
			}
			tmp = mMaxAge % 12;
			if (tmp > 0)
			{
				sb.append(tmp);
				sb.append("����");
			}
		}
		return sb.toString();
	}
}
