package com.iof.school.views;

import java.util.Calendar;

import com.iof.school.R;
import com.iof.school.obj.Child;
import com.iof.school.parser.ParserChild;
import com.iof.school.utils.Const;
import com.iof.showlib.utils.TaskBase;
import com.iof.showlib.utils.Util;
import com.iof.showlib.utils.TaskBase.OnTaskFinishListener;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PageChild extends PageBase
{
	private EditText mEditName;
	private TextView mTextYear, mTextMonth, mTextDay;//, mBtnOk, mBtnCancel;
	private View mBtnOkSelect, mBtnCancelSelect;
	private ImageView mBtnHead, mBtnSex;
	private ImageView mArrowYear1, mArrowYear2, mArrowMonth1, mArrowMonth2, mArrowDay1, mArrowDay2;
	private int mIndexRow, mIndexCol;
	private boolean mIsSubmit;
	private boolean mIsBoy;
	private boolean mIsChangeDay;
	private int mYear, mMonth, mDay;
	private int mMaxY, mMaxM, mMaxD; //可设置的最大日期
	
	private Child mChild;
	private ParserChild mParserChild = new ParserChild();
	
	public PageChild(ViewHome home)
	{
		super(home);
		((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.page_child, this);
		
		init();
	}
	
	private void init()
	{
		mEditName = (EditText)findViewById(R.id.page_child_name);
		mTextYear = (TextView)findViewById(R.id.page_child_year);
		mTextMonth = (TextView)findViewById(R.id.page_child_month);
		mTextDay = (TextView)findViewById(R.id.page_child_day);
		mBtnHead = (ImageView)findViewById(R.id.page_child_head);
		mBtnSex = (ImageView)findViewById(R.id.page_child_sex);
		mBtnOkSelect = findViewById(R.id.page_child_ok_select);
		mBtnCancelSelect = findViewById(R.id.page_child_cancel_select);
		mArrowYear1 = (ImageView)findViewById(R.id.page_child_year_a1);
		mArrowYear2 = (ImageView)findViewById(R.id.page_child_year_a2);
		mArrowMonth1 = (ImageView)findViewById(R.id.page_child_month_a1);
		mArrowMonth2 = (ImageView)findViewById(R.id.page_child_month_a2);
		mArrowDay1 = (ImageView)findViewById(R.id.page_child_day_a1);
		mArrowDay2 = (ImageView)findViewById(R.id.page_child_day_a2);
	}
	
	protected void enter(int arg1, int arg2, Object obj)
	{
		mIndexRow = mIndexCol = 0;
		mIsSubmit = mIsChangeDay = false;
		getMaxDate();
		updateChild((Child)obj);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (mIndexRow == 2)
			{
				mIndexCol--;
				if (mIndexCol < 0)
				{
					mIndexCol = 2;
				}
				mIsChangeDay = false;
				updateUI();
			}
			else if (mIndexRow == 3)
			{
				mIndexCol = 1 - mIndexCol;
				updateUI();
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (mIndexRow == 2)
			{
				mIndexCol++;
				if (mIndexCol > 2)
				{
					mIndexCol = 0;
				}
				mIsChangeDay = false;
				updateUI();
			}
			else if (mIndexRow == 3)
			{
				mIndexCol = 1 - mIndexCol;
				updateUI();
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_UP:
			if (mIsChangeDay)
			{
				if (mIndexCol == 0)
				{
					mYear++;
				}
				else if (mIndexCol == 1)
				{
					mMonth++;
					if (mMonth > 12)
					{
						mMonth = 1;
					}
					if (mDay > Const.DAY_IN_MONTH[mMonth])
					{
						mDay = Const.DAY_IN_MONTH[mMonth];
					}
				}
				else if (mIndexCol == 2)
				{
					mDay++;
					if (mDay > Const.DAY_IN_MONTH[mMonth])
					{
						mDay = 1;
					}
				}
				checkMaxDate();
			}
			else
			{
				mIndexRow--;
				mIndexCol = 0;
				if (mIndexRow < 0)
				{
					mIndexRow = 3;
				}
			}
			updateUI();
			break;
			
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (mIsChangeDay)
			{
				if (mIndexCol == 0)
				{
					if (mYear > 0)
					{
						mYear--;
					}
				}
				else if (mIndexCol == 1)
				{
					mMonth--;
					if (mMonth < 1)
					{
						mMonth = 12;
					}
					if (mDay > Const.DAY_IN_MONTH[mMonth])
					{
						mDay = Const.DAY_IN_MONTH[mMonth];
					}
				}
				else if (mIndexCol == 2)
				{
					mDay--;
					if (mDay < 1)
					{
						mDay = Const.DAY_IN_MONTH[mMonth];
					}
				}
				checkMaxDate();
			}
			else
			{
				mIndexRow++;
				mIndexCol = 0;
				if (mIndexRow > 3)
				{
					mIndexRow = 0;
				}
			}
			updateUI();
			break;
			
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (mIsSubmit)
			{
				break;
			}
			if (mIndexRow == 0)
			{
				showInputBoard(mEditName);
			}
			else if (mIndexRow == 1)
			{
				mIsBoy = !mIsBoy;
			}
			else if (mIndexRow == 2)
			{
				mIsChangeDay = !mIsChangeDay;
			}
			else if (mIndexRow == 3)
			{
				if (mIndexCol == 0)
				{
					String txt = mEditName.getText().toString().trim();
					if (txt.length() == 0)
					{
						Util.ShowToast(getContext(), "名字必须填写");
						break;
					}
					mIsSubmit = true;
					Child child = new Child();
					checkChildModify(child);
					mParserChild.setInfo(child);
					new TaskBase(getContext(), mParserChild, mFinishListener).execute();
				}
				else
				{
					mHome.changePage(ViewHome.PAGE_CTRL, 0, 0, null);
				}
			}
			updateUI();
			break;
			
		case KeyEvent.KEYCODE_BACK:
			if (mIsChangeDay)
			{
				mIsChangeDay = false;
				updateUI();
			}
			else
			{
				mHome.changePage(ViewHome.PAGE_CTRL, 0, 0, null);
			}
			break;
		}
		return true;
	}
	
	private void checkChildModify(Child child)
	{
		if (mChild != null)
		{
			child.mId = mChild.mId;
			String txt = mEditName.getText().toString().trim();
			if (mChild.mName != null && !mChild.mName.equals(txt))
			{
				child.mName = txt;
			}
			int sex = mIsBoy ? 2 : 1;
			if (mChild.mSex != sex)
			{
				child.mSex = sex;
			}
			if (mChild.mYear != mYear || mChild.mMonth != mMonth || mChild.mDay != mDay)
			{
				child.mYear = mYear;
				child.mMonth = mMonth;
				child.mDay = mDay;
			}
		}
		else
		{
			String txt = mEditName.getText().toString().trim();
			if (txt.length() > 0)
			{
				child.mName = txt;
			}
			child.mSex = mIsBoy ? 2 : 1;
			child.mYear = mYear;
			child.mMonth = mMonth;
			child.mDay = mDay;
		}
	}
	
	private void getMaxDate()
	{
		Calendar calendar = Calendar.getInstance();
		mMaxY = calendar.get(Calendar.YEAR);
		mMaxM =  calendar.get(Calendar.MONTH) + 1 + 10;
		mMaxD =  calendar.get(Calendar.DAY_OF_MONTH);
		
		if (mMaxM > 12)
		{
			mMaxM -= 12;
			mMaxY += 1;
		}
		if (mMaxD > Const.DAY_IN_MONTH[mMaxM])
		{
			mMaxD = Const.DAY_IN_MONTH[mMaxM];
		}
	}
	
	private void checkMaxDate()
	{
		if (mYear >= mMaxY)
		{
			if (mYear > mMaxY)
			{
				mYear = mMaxY;
			}
			if (mMonth > mMaxM)
			{
				mMonth = mMaxM;
				mDay = 31;
			}
			if (mMonth == mMaxM && mDay > mMaxD)
			{
				mDay = mMaxD;
			}
		}
		mTextYear.setText(mYear + "");
		mTextMonth.setText(String.format("%02d", mMonth));
		mTextDay.setText(String.format("%02d", mDay));
	}
	
	private void updateChild(Child child)
	{
		mChild = child;
		if (mChild != null)
		{
			if (mChild.mResId > 0)
			{
				mBtnHead.setBackgroundResource(child.mResId);
			}
			else
			{
				mBtnHead.setBackgroundResource(R.drawable.girl);
			}
			if (mChild.mName != null)
			{
				mEditName.setText(mChild.mName);
			}
			mIsBoy = mChild.mSex == 2;
			if (mChild.mYear == 0)
			{
				mYear = 2012;
			}
			else
			{
				mYear = mChild.mYear;
			}
			if (mChild.mMonth == 0)
			{
				mMonth = 1;
			}
			else
			{
				mMonth = mChild.mMonth;
			}
			if (mChild.mDay == 0)
			{
				mDay = 1;
			}
			else
			{
				mDay = mChild.mDay;
			}
		}
		else
		{
			mBtnHead.setBackgroundResource(R.drawable.girl);
			mEditName.setText("");
			mYear = 2012;
			mMonth = 1;
			mDay = 1;
			mIsBoy = false;
		}
		checkMaxDate();
		
		updateUI();
	}

	private void updateUI()
	{
		mEditName.setBackgroundResource(R.drawable.input_box1);
		mEditName.setFocusable(false);
		mTextYear.setBackgroundResource(R.drawable.input_box1);
		mTextMonth.setBackgroundResource(R.drawable.input_box1);
		mTextDay.setBackgroundResource(R.drawable.input_box1);
		mArrowYear1.setVisibility(INVISIBLE);
		mArrowYear2.setVisibility(INVISIBLE);
		mArrowMonth1.setVisibility(INVISIBLE);
		mArrowMonth2.setVisibility(INVISIBLE);
		mArrowDay1.setVisibility(INVISIBLE);
		mArrowDay2.setVisibility(INVISIBLE);
		if (mIsBoy)
		{
			mBtnSex.setImageResource(R.drawable.info_boy);
			mBtnHead.setBackgroundResource(R.drawable.boy);
		}
		else
		{
			mBtnSex.setImageResource(R.drawable.info_girl);
			mBtnHead.setBackgroundResource(R.drawable.girl);
		}
		mBtnSex.setBackgroundDrawable(null);
//		mBtnOk.setBackgroundResource(R.drawable.info_btn1);
//		mBtnCancel.setBackgroundResource(R.drawable.info_btn2);
		mBtnOkSelect.setVisibility(INVISIBLE);
		mBtnCancelSelect.setVisibility(INVISIBLE);
		
		if (mIndexRow == 0)
		{
			mEditName.setBackgroundResource(R.drawable.input_box2);
			mEditName.setFocusable(true);
			mEditName.setFocusableInTouchMode(true);
			mEditName.requestFocus();
			if (mEditName.getText().length() == 0)
			{
				showInputBoard(mEditName);
			}
		}
		else if (mIndexRow == 1)
		{
			mBtnSex.setBackgroundResource(R.drawable.input_box2);
			mBtnSex.setPadding(4, 4, 4, 4);
		}
		else if (mIndexRow == 2)
		{
			if (mIndexCol == 0)
			{
				mTextYear.setBackgroundResource(R.drawable.input_box2);
				if (mIsChangeDay)
				{
					mArrowYear1.setVisibility(VISIBLE);
					mArrowYear2.setVisibility(VISIBLE);
				}
			}
			else if (mIndexCol == 1)
			{
				mTextMonth.setBackgroundResource(R.drawable.input_box2);
				if (mIsChangeDay)
				{
					mArrowMonth1.setVisibility(VISIBLE);
					mArrowMonth2.setVisibility(VISIBLE);
				}
			}
			if (mIndexCol == 2)
			{
				mTextDay.setBackgroundResource(R.drawable.input_box2);
				if (mIsChangeDay)
				{
					mArrowDay1.setVisibility(VISIBLE);
					mArrowDay2.setVisibility(VISIBLE);
				}
			}
		}
		else if (mIndexRow == 3)
		{
			if (mIndexCol == 0)
			{
				mBtnOkSelect.setVisibility(VISIBLE);
			}
			else
			{
				mBtnCancelSelect.setVisibility(VISIBLE);
			}
		}
	}
	
	private OnTaskFinishListener mFinishListener = new OnTaskFinishListener()
	{
		@Override
		public void onFinish(int result, boolean isCancel)
		{
			mIsSubmit = false;
			if (result == 1 && mParserChild.mResult == 0)
			{
				if (mChild == null)
				{
					mHome.mRoles.add(mParserChild.mChild);
					Util.ShowToast(getContext(), "添加成功");
				}
				else
				{
					mChild.clone(mParserChild.mChild);
					Util.ShowToast(getContext(), "修改成功");
				}
				mHome.changePage(ViewHome.PAGE_CTRL, 0, 0, null);
			}
			else
			{
				Util.ShowToast(getContext(), "添加失败");
			}
		}
	};
}
