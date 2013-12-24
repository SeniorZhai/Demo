package com.iof.school.views;

import com.iof.school.R;
import com.iof.school.obj.Child;
import com.iof.school.obj.TimeLimit;
import com.iof.school.obj.TimeValue;
import com.iof.school.parser.ParserChild;
import com.iof.school.parser.ParserLimitTime;
import com.iof.showlib.utils.TaskBase;
import com.iof.showlib.utils.Util;
import com.iof.showlib.utils.TaskBase.OnTaskFinishListener;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageSet extends PageBase
{
	private TimeView[] mTimeView = new TimeView[2];
	private View[] mTitleBtn = new View[3];
	private View[] mModeBtn = new View[2];
	private View[] mLineMode = new View[2];
	private View[] mBtn = new View[2];
	private Child mChild;
	
	private TimeLimit mLimit;
	private ParserLimitTime mParserLimitTime = new ParserLimitTime();
	private ParserChild mParserChild = new ParserChild();

	private int mRowIndex, mColIndex;
	private boolean mIsChangeNum;
	private boolean mIsInputPsw;
	private boolean mIsSubmit;
	private boolean mIsSetPsw;
	
	private String mPsw;
	
	public PageSet(ViewHome home)
	{
		super(home);
		((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.page_set, this);
		
		init();
	}
	
	private void init()
	{
		mTimeView[0] = new TimeView((ViewGroup)findViewById(R.id.page_set_time_layout1));
		mTimeView[1] = new TimeView((ViewGroup)findViewById(R.id.page_set_time_layout2));
		mTitleBtn[0] = findViewById(R.id.page_set_time_select);
		mTitleBtn[1] = findViewById(R.id.page_set_psw_select);
		mTitleBtn[2] = findViewById(R.id.page_set_res_select);
		mModeBtn[0] = findViewById(R.id.page_set_mode1_select);
		mModeBtn[1] = findViewById(R.id.page_set_mode2_select);
		mLineMode[0] = findViewById(R.id.page_set_line_mode1);
		mLineMode[1] = findViewById(R.id.page_set_line_mode2);
		mBtn[0] = findViewById(R.id.page_set_ok_select);
		mBtn[1] = findViewById(R.id.page_set_cancel_select);
		
		mTimeView[1].mLab.setText("周末可用时间：");
	}

	/**
	 * @param arg1 -1表示从资源列表界面返回
	 * @param arg2
	 * @param obj 孩子
	 */
	protected void enter(int arg1, int arg2, Object obj)
	{
		if (arg1 == -1)
		{
			return;
		}
		
		mChild = (Child)obj;
		if (mLimit == null)
		{
			mLimit = new TimeLimit();
		}
		mRowIndex = 0;
		mColIndex = 0;
		mIsChangeNum = mIsInputPsw = false;
		updateChild();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (mIsSetPsw)
		{
			return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (mIsInputPsw) //输入密码
			{
				mIsInputPsw = false;
				mHome.mPswInputView.hide();
			}
			else if (mIsChangeNum)
			{
				mIsChangeNum = false;
				updateUI();
			}
			else
			{
				mHome.changePage(ViewHome.PAGE_CTRL, -1, 0, null);
			}
			return true;
		}
		
		if (mIsSubmit)
		{
			Util.ShowToast(getContext(), "正在提交，请稍候...");
			return true;
		}
		
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_LEFT);
			}
			else if (mIsChangeNum)
			{
				mIsChangeNum = false;
				if (mRowIndex == 3 || mRowIndex == 5)
				{
					mColIndex = 1 - mColIndex;
				}
				updateUI();
			}
			else if (mRowIndex == 0)
			{
				if (mColIndex == 0)
				{
					mColIndex = 2;
				}
				else
				{
					mColIndex--;
				}
				updateUI();
			}
			else if (mRowIndex == 1 || mRowIndex == 3 || mRowIndex == 5 || mRowIndex == 6)
			{
				mColIndex = 1 - mColIndex;
				updateUI();
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_RIGHT);
			}
			else if (mIsChangeNum)
			{
				mIsChangeNum = false;
				if (mRowIndex == 3 || mRowIndex == 5)
				{
					mColIndex = 1 - mColIndex;
				}
				updateUI();
			}
			else if (mRowIndex == 0)
			{
				if (mColIndex == 2)
				{
					mColIndex = 0;
				}
				else
				{
					mColIndex++;
				}
				updateUI();
			}
			else if (mRowIndex == 1 || mRowIndex == 3 || mRowIndex == 5 || mRowIndex == 6)
			{
				mColIndex = 1 - mColIndex;
				updateUI();
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_UP:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_UP);
			}
			else if (mIsChangeNum)
			{
				if (mRowIndex == 2 || mRowIndex == 3)
				{
					mTimeView[0].keyDown(true);
				}
				else
				{
					mTimeView[1].keyDown(true);
				}
			}
			else 
			{
				mColIndex = 0;
				if (mRowIndex == 0)
				{
					mRowIndex = 6;
				}
				else
				{
					mRowIndex--;
					if (mRowIndex == 1)
					{
						mColIndex = mLimit.mMode - 1;
					}
					else if (mRowIndex == 5 && mLimit.mMode == TimeLimit.LIMIT_MODE_EVERY)
					{
						mRowIndex = 3;
					}
				}
			}
			updateUI();
			break;
			
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_DOWN);
			}
			else if (mIsChangeNum)
			{
				if (mRowIndex == 2 || mRowIndex == 3)
				{
					mTimeView[0].keyDown(false);
				}
				else
				{
					mTimeView[1].keyDown(false);
				}
			}
			else 
			{
				mColIndex = 0;
				if (mRowIndex == 6)
				{
					mRowIndex = 0;
				}
				else
				{
					mRowIndex++;
					if (mRowIndex == 1)
					{
						mColIndex = mLimit.mMode - 1;
					}
					else if (mRowIndex == 4 && mLimit.mMode == TimeLimit.LIMIT_MODE_EVERY)
					{
						mRowIndex = 6;
					}
				}
			}
			updateUI();
			break;
			
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (mIsInputPsw) //输入密码
			{
				String psw = mHome.mPswInputView.getPsw();
				if (psw == null)
				{
					return true;
				}
				if (mPsw == null)
				{
					mPsw = psw;
					mHome.mPswInputView.reset();
					Util.ShowToast(getContext(), "请再次输入密码确认");
					return true;
				}
				if (!mPsw.equals(psw))
				{
					mPsw = null;
					mHome.mPswInputView.reset();
					Util.ShowToast(getContext(), "两次密码不一致，请重新输入");
					return true;
				}

				mIsSetPsw = true;
				Child child = new Child();
				child.mId = mChild.mId;
				child.mPsw = mPsw;
				mParserChild.setInfo(child);
				new TaskBase(getContext(), mParserChild, mSetPswFinishListener).execute();
			}
			else if (mIsChangeNum)
			{
				mIsChangeNum = false;
				updateUI();
			}
			else if (mRowIndex == 0)
			{
				if (mColIndex == 1)
				{
					mIsInputPsw = true;
					mPsw = null;
					mHome.mPswInputView.show(true);
				}
				else if (mColIndex == 2)
				{
					mHome.changePage(ViewHome.PAGE_RES, 100, 0, mChild);
				}
			}
			else if (mRowIndex == 1)
			{
				if (mLimit.mMode != mColIndex + 1)
				{
					mLimit.mMode = mColIndex + 1;
					updateUI();
				}
			}
			else if (mRowIndex == 2 || mRowIndex == 3)
			{
				mIsChangeNum = true;
				if (mRowIndex == 2)
				{
					mLimit.mTime1.mType = 1;
				}
				else
				{
					mLimit.mTime1.mType = 2;
				}
				updateUI();
			}
			else if (mRowIndex == 4 || mRowIndex == 5)
			{
				mIsChangeNum = true;
				if (mRowIndex == 4)
				{
					mLimit.mTime2.mType = 1;
				}
				else
				{
					mLimit.mTime2.mType = 2;
				}
				updateUI();
			}
			else if (mRowIndex == 6)
			{
				if (mColIndex == 0)
				{
					mIsSubmit = true;
					setLimit();
					mParserLimitTime.setInfo(mChild, mLimit);
					new TaskBase(getContext(), mParserLimitTime, mLimitTimeFinishListener).execute();
				}
				else
				{
					mHome.changePage(ViewHome.PAGE_CTRL, -1, 0, null);
				}
			}
			break;
		}
		return true;
	}
	
	private int getTimeNumIndex()
	{
		if (mRowIndex == 2 || mRowIndex == 4)
		{
			return 0;
		}
		if (mColIndex == 0)
		{
			return 1;
		}
		return 2;
	}
	
	private void updateChild()
	{
		if (mChild.mTime != null)
		{
			mLimit.clone(mChild.mTime);
		}
		if (mLimit.mMode == TimeLimit.LIMIT_MODE_NONE)
		{
			mLimit.mMode = TimeLimit.LIMIT_MODE_EVERY;
		}
		if (mLimit.mTime1 == null)
		{
			mLimit.mTime1 = new TimeValue();
		}
		mTimeView[0].init(mLimit.mTime1);
		mTimeView[1].init(mLimit.mTime2);
		updateUI();
	}

	private void updateUI()
	{
		mTitleBtn[0].setVisibility(INVISIBLE);
		mTitleBtn[1].setVisibility(INVISIBLE);
		mTitleBtn[2].setVisibility(INVISIBLE);
		mModeBtn[0].setVisibility(INVISIBLE);
		mModeBtn[1].setVisibility(INVISIBLE);
		mLineMode[0].setVisibility(INVISIBLE);
		mLineMode[1].setVisibility(INVISIBLE);
		mBtn[0].setVisibility(INVISIBLE);
		mBtn[1].setVisibility(INVISIBLE);
		if (mRowIndex == 0)
		{
			mTitleBtn[mColIndex].setVisibility(VISIBLE);
		}
		else if (mRowIndex == 1)
		{
			mModeBtn[mColIndex].setVisibility(VISIBLE);
		}
		else if (mRowIndex == 6)
		{
			mBtn[mColIndex].setVisibility(VISIBLE);
		}
		if (mLimit.mMode == TimeLimit.LIMIT_MODE_WORK)
		{
			mLineMode[1].setVisibility(VISIBLE);
		}
		else
		{
			mLineMode[0].setVisibility(VISIBLE);
		}
		
		if (mRowIndex == 2 || mRowIndex == 3)
		{
			mTimeView[0].updateUI(true, mLimit.mTime1);
		}
		else
		{
			mTimeView[0].updateUI(false, mLimit.mTime1);
		}
		if (mLimit.mMode == TimeLimit.LIMIT_MODE_WORK)
		{
			if (mLimit.mTime2 == null)
			{
				mLimit.mTime2 = new TimeValue();
			}
			mTimeView[0].mLab.setText("平时可用时间：");
			mTimeView[1].mLayout.setVisibility(VISIBLE);
			if (mRowIndex == 4 || mRowIndex == 5)
			{
				mTimeView[1].updateUI(true, mLimit.mTime2);
			}
			else
			{
				mTimeView[1].updateUI(false, mLimit.mTime2);
			}
		}
		else
		{
			mTimeView[0].mLab.setText("每天可用时间：");
			mTimeView[1].mLayout.setVisibility(INVISIBLE);
		}
	}
	
	private void setLimit()
	{
		mTimeView[0].setLimet(mLimit.mTime1);
		if (mLimit.mMode == TimeLimit.LIMIT_MODE_WORK)
		{
			mTimeView[1].setLimet(mLimit.mTime2);
		}
	}
	
	private OnTaskFinishListener mLimitTimeFinishListener = new OnTaskFinishListener()
	{
		@Override
		public void onFinish(int result, boolean isCancel)
		{
			mIsSubmit = false;
			if (mParserLimitTime.mResult == 0 && !isCancel)
			{
				if (mParserLimitTime.mChild.mId == mChild.mId)
				{
					Util.ShowToast(getContext(), "设置成功");
					mHome.changePage(ViewHome.PAGE_CTRL, -1, 0, null);
				}
			}
			else
			{
				Util.ShowToast(getContext(), "设置失败");
			}
		}
	};
	
	private OnTaskFinishListener mSetPswFinishListener = new OnTaskFinishListener()
	{
		@Override
		public void onFinish(int result, boolean isCancel)
		{
			mIsSetPsw = false;
			if (mParserChild.mResult == 0 && !isCancel)
			{
				Util.ShowToast(getContext(), "密码设置成功");
				mChild.mHasPsw = mParserChild.mChild.mHasPsw;
				mIsInputPsw = false;
				mHome.mPswInputView.hide();
			}
			else
			{
				mPsw = null;
				mHome.mPswInputView.reset();
				Util.ShowToast(getContext(), "设置失败");
			}
		}
	};
	
	private class TimeView
	{
		public ViewGroup mLayout;
		public TextView mLab;
		public View mSelect1, mSelect2;
		public NumView[] mNumViews = new NumView[3];

		public TimeView(ViewGroup layout)
		{
			mLayout = layout;
			mLab = (TextView)layout.findViewById(R.id.item_time_lab);
			mSelect1 = layout.findViewById(R.id.item_time_select1);
			mSelect2 = layout.findViewById(R.id.item_time_select2);
			mNumViews[0] = new NumView((ViewGroup)layout.findViewById(R.id.item_time_num1));
			mNumViews[1] = new NumView((ViewGroup)layout.findViewById(R.id.item_time_num2));
			mNumViews[2] = new NumView((ViewGroup)layout.findViewById(R.id.item_time_num3));
		}
		
		public void init(TimeValue value)
		{
			if (value == null)
			{
				mNumViews[0].mNum = 2;
				mNumViews[1].mNum = 19;
				mNumViews[2].mNum = 21;
				return;
			}
			
			if (value.mType == 1)
			{
				mNumViews[0].mNum = value.mValue1 / 60;
				mNumViews[1].mNum = 19;
				mNumViews[2].mNum = 21;
			}
			else
			{
				mNumViews[0].mNum = 2;
				mNumViews[1].mNum = value.mValue1 / 60;
				mNumViews[2].mNum = value.mValue2 / 60;
			}
		}
		
		public void updateUI(boolean isMe, TimeValue value)
		{
			mNumViews[0].setState(0);
			mNumViews[1].setState(0);
			mNumViews[2].setState(0);
			mSelect1.setBackgroundResource(R.drawable.set_select1);
			mSelect2.setBackgroundResource(R.drawable.set_select1);
			if (value.mType == 1)
			{
				mSelect1.setBackgroundResource(R.drawable.set_select2);
			}
			else
			{
				mSelect2.setBackgroundResource(R.drawable.set_select2);
			}
			if (!isMe)
			{
				return;
			}

			mNumViews[getTimeNumIndex()].setState(mIsChangeNum ? 2 : 1);
		}
		
		public void setLimet(TimeValue value)
		{
			if (value.mType == 0)
			{
				
			}
			else if (value.mType == 2)
			{
				value.mValue1 = mNumViews[1].mNum * 60;
				value.mValue2 = mNumViews[2].mNum * 60;
			}
			else
			{
				value.mValue1 = mNumViews[0].mNum * 60;
			}
		}
		
		public void keyDown(boolean bUp)
		{
			if (mRowIndex == 2 || mRowIndex == 4)
			{
				mNumViews[0].keyDown(bUp, 0, 24);
			}
			else if (mColIndex == 0)
			{
				mNumViews[1].keyDown(bUp, 0, mNumViews[2].mNum);
			}
			else
			{
				mNumViews[2].keyDown(bUp, mNumViews[1].mNum, 24);
			}
		}
	}
	
	private class NumView
	{
		public TextView mTxt;
		public View mA1, mA2;
		public int mNum;
		
		public NumView(ViewGroup layout)
		{
			mTxt = (TextView)layout.findViewById(R.id.item_num_text);
			mA1 = layout.findViewById(R.id.item_num_a1);
			mA2 = layout.findViewById(R.id.item_num_a2);
		}
		
		public void setState(int state)
		{
			if (mNum > 9)
			{
				mTxt.setText("" + mNum);
			}
			else
			{
				mTxt.setText("0" + mNum);
			}
			if (state == 0)
			{
				mTxt.setBackgroundResource(R.drawable.set_num_bg);
				mA1.setVisibility(INVISIBLE);
				mA2.setVisibility(INVISIBLE);
			}
			else if (state == 1)
			{
				mTxt.setBackgroundResource(R.drawable.set_num_bg_select);
				mA1.setVisibility(INVISIBLE);
				mA2.setVisibility(INVISIBLE);
			}
			else
			{
				mTxt.setBackgroundResource(R.drawable.set_num_bg_select);
				mA1.setVisibility(VISIBLE);
				mA2.setVisibility(VISIBLE);
			}
		}
		
		public void keyDown(boolean bUp, int min, int max)
		{
			if (bUp)
			{
				if (mNum == max)
				{
					mNum = min;
				}
				else
				{
					mNum++;
				}
			}
			else
			{
				if (mNum == min)
				{
					mNum = max;
				}
				else
				{
					mNum--;
				}
			}
		}
	}
}
