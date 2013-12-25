package com.iof.school.views;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.iof.school.R;
import com.iof.school.SchoolActivity;
import com.iof.school.obj.Child;
import com.iof.school.obj.TimeLimit;
import com.iof.school.obj.TimeRecord;
import com.iof.school.obj.TimeValue;
import com.iof.school.parser.ParserCategory;
import com.iof.school.utils.Const;
import com.iof.showlib.obj.Category;
import com.iof.showlib.obj.Res;
import com.iof.showlib.utils.TaskBase;
import com.iof.showlib.utils.Util;
import com.iof.showlib.utils.TaskBase.OnTaskFinishListener;
import com.iof.showlib.views.ViewBase;

public class ViewHome extends ViewBase implements Handler.Callback
{
	public static final int PAGE_ROLE  = 0;//角色选择
	public static final int PAGE_CTRL  = 1;//家长控制主页
	public static final int PAGE_RES   = 2;//资源页
	public static final int PAGE_CHILD = 3;//孩子信息页（包括修改和查看）
	public static final int PAGE_INFO  = 4;//资源详细页
	public static final int PAGE_SET   = 5;//设置页
	
	private SchoolActivity mAct;
	private PageBase[] mPages = new PageBase[6];
	public ArrayList<Child> mRoles = new ArrayList<Child>();
	public ArrayList<Category> mCategorys = new ArrayList<Category>();
	public ParserCategory mParserCategory = new ParserCategory();
	public ArrayList<TimeRecord> mTimeRecords;
	public TimeRecord mCurRecord;
	
	public PswInputView mPswInputView;
	
	private HashMap<String, SoftReference<Drawable>> mIamgeCache = new HashMap<String, SoftReference<Drawable>>();
	
	/**
	 * 消息的id根据页面指定，每个页面100个（0-99,100-199,...）
	 */
	public Handler mHandler;
	
	public String mPsw;
	
	public int mCurPage;
	public int mRole;
	
	public boolean mHasPsw;
	
	@SuppressWarnings("unchecked")
	public ViewHome(SchoolActivity act)
	{
		super(act);
		
		mAct = act;
		mHandler = new Handler(this);
		
		setBackgroundResource(R.drawable.all_bg);
		
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPages[0] = new PageRole(this);
		mPages[1] = new PageCtrl(this);
		mPages[2] = new PageRes(this);
		mPages[3] = new PageChild(this);
		mPages[4] = new PageInfo(this);
		mPages[5] = new PageSet(this);
		addView(mPages[0], params);
		addView(mPages[1], params);
		addView(mPages[2], params);
		addView(mPages[3], params);
		addView(mPages[4], params);
		addView(mPages[5], params);
		
		mPages[1].setVisibility(GONE);
		mPages[2].setVisibility(GONE);
		mPages[3].setVisibility(GONE);
		mPages[4].setVisibility(GONE);
		mPages[5].setVisibility(GONE);
		
		//加入密码输入层
		mPswInputView = new PswInputView(mAct);
		addView(mPswInputView, params);
		mPswInputView.setVisibility(GONE);
		
		//取得时间数据
		mTimeRecords = (ArrayList<TimeRecord>)Util.readObjectFile(Const.PATH_RECORD);
		if (mTimeRecords == null)
		{
			mTimeRecords = new ArrayList<TimeRecord>();
		}
		else
		{
			checkTimeRecord();
		}
		
		new TaskBase(getContext(), mParserCategory, mCateFinishListener).execute();
	}
	
	protected void changePage(int index, int arg1, int arg2, Object obj)
	{
		if (mCurPage == index)
		{
			return;
		}
		mPswInputView.setVisibility(GONE);
		mPages[mCurPage].setVisibility(GONE);
		mCurPage = index;
		mPages[mCurPage].enter(arg1, arg2, obj);
		mPages[mCurPage].setVisibility(VISIBLE);
	}
	
	public Child findRole(int id)
	{
		for (int i = 0; i < mRoles.size(); i++)
		{
			if (mRoles.get(i).mId == id)
			{
				return mRoles.get(i);
			}
		}
		return null;
	}
	
	public void selectRole(int index)
	{
		if (index == -1)
		{
			changePage(PAGE_CTRL, 0, 0, null);
		}
		else if (index >= 0 && index < mRoles.size())
		{
			changePage(PAGE_RES, index, 0, mRoles.get(index));
			startRecord(mRoles.get(index).mId);
		}
	}
	
	/**
	 * 返回剩余时间：-1表示无限时，0表示没有剩余，正数表示剩余分钟数
	 * @param child
	 * @return
	 */
	public int checkChildTime(Child child)
	{
		if (child.mTime.mMode == TimeLimit.LIMIT_MODE_NONE)
		{
			return -1;
		}
		TimeValue time = child.mTime.getTimeValue();
		if (time.mType == 0)
		{
			return -1;
		}
		if (time.mType == 1)
		{
			int total = 0; //秒
			TimeRecord record;
			for (int i = 0; i < mTimeRecords.size(); i++)
			{
				record = mTimeRecords.get(i);
				if (record.mChildId == child.mId)
				{
					total += (int)(record.mTimeEnd - record.mTimeStart) / 1000;
				}
			}
			total /= 60; //转成分钟
			if (total >= time.mValue1)
			{
				return 0;
			}
			else
			{
				return time.mValue1 - total;
			}
		}
		else if (time.mType == 2)
		{
			return time.hasTime();
		}
		
		return -1;
	}
	
	private void checkTimeRecord()
	{
		TimeRecord record;
		Iterator<TimeRecord> itr = mTimeRecords.iterator();
		while (itr.hasNext())
		{
			record = itr.next();
			int day = getDay(System.currentTimeMillis() + TimeValue.HOUR_8);
			if (record == null || record.mChildId == -1 || record.mTimeStart == -1|| record.mTimeEnd == -1
					|| getDay(record.mTimeEnd) != day)
			{
				itr.remove();
			}
			else if (getDay(record.mTimeStart) != day)
			{
				record.mTimeStart = day * 1000L * 3600 * 24;
			}
		}
	}
	
	private int getDay(long ms)
	{
		return (int)(ms / 1000 / 3600 / 24);
	}
	
	public void startRecord(int id)
	{
		if (mCurRecord == null)
		{
			mCurRecord = new TimeRecord();
			mTimeRecords.add(mCurRecord);
		}
		mCurRecord.mChildId = id;
		mCurRecord.mTimeStart = System.currentTimeMillis() + TimeValue.HOUR_8;
	}
	
	public void saveTimeRecord()
	{
		if (mCurRecord != null)
		{
			mCurRecord.mTimeEnd = System.currentTimeMillis() + TimeValue.HOUR_8;
		}
		mCurRecord = null;
		checkTimeRecord();
		Util.saveFile(mTimeRecords, Const.PATH_RECORD);
	}
	
	public Drawable getLogo(int id)
	{
		String absPath = Const.PATH_LOGO + id + ".png";
		SoftReference<Drawable> refBmp = mIamgeCache.get(absPath);
		if (refBmp != null && refBmp.get() != null)
		{
			return refBmp.get();
		}

		File file = new File(absPath);
		if (file.exists())
		{
			if (file.length() > 0)
			{
				try
				{
					Drawable drawable = new BitmapDrawable(BitmapFactory.decodeFile(absPath));
					mIamgeCache.put(absPath, new SoftReference<Drawable>(drawable));
					return drawable;
				}
				catch (OutOfMemoryError e)
				{
				}
				catch (Exception e)
				{
				}
			}
		}

		return null;
	}
	
	public void gotoRes(Res res)
	{
		mAct.showView(res, 0, 0, null);
	}
	
	public void exit()
	{
		mAct.exit();
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		if (msg.what > 10000)
		{
			switch (msg.what)
			{
			case Const.HD_ID_TIME_OVER:
				changePage(PAGE_ROLE, 0, 0, null);
				mAct.showView(null, 0, 0, null);
				Util.ShowToast(mAct, "你今天的时间已经到了");
				saveTimeRecord();
				break;

			}
			return true;
		}
		int index = msg.what / 100;
		if (index >= 0 && index < mPages.length)
		{
			mPages[index].handleMessage(msg);
		}
		return true;
	}

	@Override
	public String enter(int arg1, int arg2, Object obj)
	{
		return null;
	}

	@Override
	public void release()
	{
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return mPages[mCurPage].onKeyDown(keyCode, event);
	}

	@Override
	public void onPause()
	{
		mPages[mCurPage].onPause();
	}

	@Override
	public void onResume()
	{
		mPages[mCurPage].onResume();
	}
	
	private OnTaskFinishListener mCateFinishListener = new OnTaskFinishListener()
	{
		@Override
		public void onFinish(int result, boolean isCancel)
		{
			mCategorys.addAll(mParserCategory.mCates);
		}
	};
}
