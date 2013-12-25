package com.iof.school.views;

import com.iof.school.R;
import com.iof.school.obj.Child;
import com.iof.school.parser.ParserLoginChild;
import com.iof.school.parser.ParserLoginParent;
import com.iof.school.parser.ParserRoles;
import com.iof.school.utils.Const;
import com.iof.showlib.utils.TaskBase;
import com.iof.showlib.utils.Util;
import com.iof.showlib.utils.TaskBase.OnTaskFinishListener;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PageRole extends PageBase
{
	private ImageView mParentHead;
	private RelativeLayout[] mHeadLayout = new RelativeLayout[3];
	private ImageView[] mHeads = new ImageView[3];
	private TextView[] mNames = new TextView[3];
	
	private boolean mIsInputPsw;
	private boolean mIsLogin;
	private boolean mIsParent;
	private int mIndex;
	private int mChildCount;
	
	private ParserRoles mParserRoles = new ParserRoles();
	private ParserLoginParent mParserLoginParent = new ParserLoginParent();
	private ParserLoginChild mParserLoginChild = new ParserLoginChild();
	private boolean mIsInited;
	
	public PageRole(ViewHome home)
	{
		super(home);
		((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.page_role, this);
		
		mParentHead = (ImageView)findViewById(R.id.parent_head);
		mHeadLayout[0] = (RelativeLayout)findViewById(R.id.child_head_layout1);
		mHeadLayout[1] = (RelativeLayout)findViewById(R.id.child_head_layout2);
		mHeadLayout[2] = (RelativeLayout)findViewById(R.id.child_head_layout3);
		mHeads[0] = (ImageView)findViewById(R.id.child_head1);
		mHeads[1] = (ImageView)findViewById(R.id.child_head2);
		mHeads[2] = (ImageView)findViewById(R.id.child_head3);
		mNames[0] = (TextView)findViewById(R.id.child_name1);
		mNames[1] = (TextView)findViewById(R.id.child_name2);
		mNames[2] = (TextView)findViewById(R.id.child_name3);
		
		updateChild();

		new TaskBase(getContext(), mParserRoles, mRolesFinishListener).execute();
	}
	
	protected void enter(int arg1, int arg2, Object obj)
	{
		mIsInputPsw = false;
		updateChild();
		
		mHome.mHandler.removeMessages(Const.HD_ID_TIME_OVER);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (mIsLogin)
		{
			Util.ShowToast(getContext(), "正在登录，请稍候...");
			return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (mIsInputPsw)
			{
				hidePsw();
			}
			else
			{
				mHome.exit();
			}
			return true;
		}
		
		if (!mIsInited)
		{
			return true;
		}
		
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_LEFT);
			}
			else if (mIsParent) //家长
			{
			}
			else if (mHome.mRoles.size() > 0)
			{
				mIndex--;
				if (mIndex < 0)
				{
					mIndex = mHome.mRoles.size() - 1;
				}
				updateUI();
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_RIGHT);
			}
			else if (mIsParent) //家长
			{
			}
			else if (mHome.mRoles.size() > 0)
			{
				mIndex++;
				if (mIndex >= mHome.mRoles.size())
				{
					mIndex = 0;
				}
				updateUI();
			}
			break;

		case KeyEvent.KEYCODE_DPAD_UP:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_UP);
			}
			else if (mHome.mRoles.size() > 0)
			{
				mIsParent = !mIsParent;
				updateUI();
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_DOWN);
			}
			else if (mHome.mRoles.size() > 0)
			{
				mIsParent = !mIsParent;
				updateUI();
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (mIsInputPsw) //输入密码
			{
				login();
				break;
			}
			else if (mIsParent)
			{
				if (mHome.mHasPsw)
				{
					needPsw();
					break;
				}
			}
			else
			{
				Child child = mHome.mRoles.get(mIndex);
				int time = mHome.checkChildTime(child);
				if (time == 0)
				{
					Util.ShowToast(getContext(), "时间受限，不能进入");
					return true;
				}
				else if (time > 0)
				{
					//TODO 
					Util.ShowToast(getContext(), "还可以使用" + time + "分钟");
					mHome.mHandler.sendEmptyMessageDelayed(Const.HD_ID_TIME_OVER, time * 60000);
				}
				if (child.mHasPsw)
				{
					needPsw();
					break;
				}
			}
			selectRole();
			break;
		}
		return true;
	}
	
	private void needPsw()
	{
		mIsInputPsw = true;
		mHome.mPswInputView.show(false);
	}
	
	private void hidePsw()
	{
		mIsInputPsw = false;
		mHome.mPswInputView.hide();
	}
	
	private void login()
	{
		String psw = mHome.mPswInputView.getPsw();
		if (psw == null)
		{
			return;
		}
		mIsLogin = true;
		if (mIsParent)
		{
			mParserLoginParent.setInfo(psw);
			new TaskBase(getContext(), mParserLoginParent, mLoginFinishListener).execute();
		}
		else
		{
			mParserLoginChild.setInfo(mHome.mRoles.get(mIndex).mId, psw);
			new TaskBase(getContext(), mParserLoginChild, mLoginFinishListener).execute();
		}
	}
	
	private void selectRole()
	{
		int index = -1;
		if (!mIsParent)
		{
			index = mIndex;
		}
		mHome.selectRole(index);
	}
	
	private void updateChild()
	{
		mChildCount = mHome.mRoles.size();
		if (mChildCount == 0)
		{
			for (int i = 0; i < mHeadLayout.length; i++)
			{
				mHeadLayout[i].setVisibility(GONE);
			}
			mIsParent = true;
		}
		else
		{
			for (int i = 0; i < 3; i++)
			{
				if (i < mChildCount)
				{
					mHeadLayout[i].setVisibility(VISIBLE);
					setHead(mHeads[i], mHome.mRoles.get(i));
					mNames[i].setText(mHome.mRoles.get(i).mName);
				}
				else
				{
					mHeadLayout[i].setVisibility(GONE);
				}
			}
			
			if (mIndex >= mChildCount)
			{
				mIndex = 0;
			}
		}
		updateUI();
	}

	private void updateUI()
	{
		if (mIsParent)
		{
			mParentHead.setImageResource(R.drawable.head_bg2);
		}
		else
		{
			mParentHead.setImageResource(R.drawable.head_bg1);
		}
		
		if (mChildCount == 0)
		{
			return;
		}
		
		for (int i = 0; i < mChildCount; i++)
		{
			if (!mIsParent && i == mIndex)
			{
				mHeads[i].setImageResource(R.drawable.head_bg2);
			}
			else
			{
				mHeads[i].setImageResource(R.drawable.head_bg1);
			}
		}
	}
	
	private OnTaskFinishListener mRolesFinishListener = new OnTaskFinishListener()
	{
		@Override
		public void onFinish(int result, boolean isCancel)
		{
			mIsInited = true;
			if (result == 1)
			{
				mHome.mRoles.clear();
				mHome.mRoles.addAll(mParserRoles.mRoles);
				updateChild();
				mHome.mHasPsw = mParserRoles.mHasPsw;
			}
		}
	};
	
	private OnTaskFinishListener mLoginFinishListener = new OnTaskFinishListener()
	{
		@Override
		public void onFinish(int result, boolean isCancel)
		{
			mIsLogin = false;
			int code;
			if (mIsParent)
			{
				code = mParserLoginParent.mResult;
			}
			else
			{
				code = mParserLoginChild.mResult;
			}
			if (result == 1)
			{
				if (code == 0)
				{
					hidePsw();
					selectRole();
					if (mIsParent)
					{
						mHome.mPsw = mParserLoginParent.mPsw;
					}
					return;
				}
				else if (code == 2)
				{
					Util.ShowToast(getContext(), "密码错误");
				}
			}
			else
			{
				Util.ShowToast(getContext(), "登录失败");
			}
			mHome.mPswInputView.reset();
		}
	};
}
