package com.iof.school.views;

import com.iof.school.R;
import com.iof.school.obj.Child;
import com.iof.school.parser.ParserDelChild;
import com.iof.school.parser.ParserParentPsw;
import com.iof.showlib.utils.TaskBase;
import com.iof.showlib.utils.Util;
import com.iof.showlib.utils.TaskBase.OnTaskFinishListener;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PageCtrl extends PageBase
{
	private ChildView[] mChildViews = new ChildView[3];
	private ImageView mBtnAdd, mBtnPsw;
	private View mAddNotice, mPswNotice;
	private int mIndex;
	private int mTitleIndex;
	private boolean mIsInTitle;
	private boolean mIsDel;
	
	private boolean mIsInputPsw;
	private boolean mIsModifyPsw;
	
	private String mPswOld, mPswNew;
	
	private ParserDelChild mParserDelChild = new ParserDelChild();
	private ParserParentPsw mParserParentPsw = new ParserParentPsw();
	
	public PageCtrl(ViewHome home)
	{
		super(home);
		((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.page_ctrl, this);
		
		init();
	}
	
	private void init()
	{
		mBtnAdd = (ImageView)findViewById(R.id.page_ctrl_add);
		mBtnPsw = (ImageView)findViewById(R.id.page_ctrl_psw);
		mAddNotice = findViewById(R.id.page_ctrl_add_notice);
		mPswNotice = findViewById(R.id.page_ctrl_psw_notice);
		mChildViews[0] = new ChildView((ViewGroup)findViewById(R.id.item_child_1));
		mChildViews[1] = new ChildView((ViewGroup)findViewById(R.id.item_child_2));
		mChildViews[2] = new ChildView((ViewGroup)findViewById(R.id.item_child_3));
	}
	
	@Override
	protected void enter(int arg1, int arg2, Object obj)
	{
		if (arg1 == -1) //从设置界面返回
		{
			return;
		}
		mIsInTitle = mIsDel = false;
		mIndex = mTitleIndex = 0;
		updateChild();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (mIsModifyPsw)
		{
			Util.ShowToast(getContext(), "正在登录，请稍候...");
			return true;
		}
		
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_LEFT);
			}
			else if (mIsInTitle)
			{
				mTitleIndex = 1 - mTitleIndex;
			}
			else if (mHome.mRoles.size() > 0)
			{
				if (mIndex < 100)
				{
					mIndex += 300;
				}
				else
				{
					mIndex -= 100;
					//TODO test
					if (mIndex >= 100 && mIndex < 200)
					{
						mIndex -= 100;
					}
					//test
				}
			}
			updateUI();
			break;
			
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_RIGHT);
			}
			else if (mIsInTitle)
			{
				mTitleIndex = 1 - mTitleIndex;
			}
			else if (mHome.mRoles.size() > 0)
			{
				if (mIndex >= 300)
				{
					mIndex -= 300;
				}
				else
				{
					mIndex += 100;
					//TODO test
					if (mIndex >= 100 && mIndex < 200)
					{
						mIndex += 100;
					}
					//test
				}
			}
			updateUI();
			break;
			
		case KeyEvent.KEYCODE_DPAD_UP:
			if (mIsInputPsw) //输入密码
			{
				mHome.mPswInputView.input(PswInputView.PSW_KEY_UP);
			}
			else if (mHome.mRoles.size() > 0)
			{
				if (mIsInTitle)
				{
					mIsInTitle = false;
					mIndex = 0;
				}
				else if (mIndex % 100 == 0)
				{
					mIsInTitle = true;
					mTitleIndex = 0;
				}
				else
				{
					mIndex = mIndex  - 1;
				}
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
				if (mIsInTitle)
				{
					mIsInTitle = false;
					mIndex = 0;
				}
				else if (mIndex % 100 == mHome.mRoles.size() - 1)
				{
					mIndex = mIndex / 100 * 100;
				}
				else
				{
					mIndex = mIndex  + 1;
				}
				updateUI();
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (mIsInputPsw) //输入密码
			{
				//TODO
				String psw = mHome.mPswInputView.getPsw();
				if (mPswOld == null)
				{
					mPswOld = psw;
					mHome.mPswInputView.reset();
					Util.ShowToast(getContext(), "请输入新密码");
					return true;
				}
				if (mPswNew == null)
				{
					mPswNew = psw;
					mHome.mPswInputView.reset();
					Util.ShowToast(getContext(), "请再次输入密码确认");
					return true;
				}
				if (!mPswNew.equals(psw))
				{
					mPswNew = null;
					mHome.mPswInputView.reset();
					Util.ShowToast(getContext(), "两次密码不一致，请重新输入");
					return true;
				}
				
				mIsModifyPsw = true;
				mParserParentPsw.setInfo(mPswOld, mPswNew);
				new TaskBase(getContext(), mParserParentPsw, mParentPswFinishListener).execute();
			}
			else if (mIsInTitle)
			{
				if (mTitleIndex == 0)
				{
					if (mHome.mRoles.size() >= 3)
					{
						Util.ShowToast(getContext(), "最多添加3个孩子");
						break;
					}
					mHome.changePage(ViewHome.PAGE_CHILD, 0, 0, null);
				}
				else
				{
					mIsInputPsw = true;
					mPswOld = null;
					mPswNew = null;
					mHome.mPswInputView.show(true);
					if (mHome.mHasPsw)
					{
						Util.ShowToast(getContext(), "请输入原密码");
					}
					else
					{
						Util.ShowToast(getContext(), "请输入新密码");
						mPswOld = "";
					}
				}
			}
			else
			{
				if (mIndex < 100) //修改信息
				{
					mHome.changePage(ViewHome.PAGE_CHILD, 0, 0, mHome.mRoles.get(mIndex));
				}
				else if (mIndex < 200) //家长指导
				{
					Util.ShowToast(getContext(), "功能暂无，敬请期待");
				}
				else if (mIndex < 300) //设置
				{
					mHome.changePage(ViewHome.PAGE_SET, 0, 0, mHome.mRoles.get(mIndex - 200));
				}
				else if (mIndex < 400) //删除孩子
				{
					if (mIsDel)
					{
						Util.ShowToast(getContext(), "正在删除，请稍候...");
					}
					else
					{
						mIsDel = true;
						mParserDelChild.setInfo(mHome.mRoles.get(mIndex - 300).mId, mHome.mPsw);
						new TaskBase(getContext(), mParserDelChild, mDelChildFinishListener).execute();
					}
				}
			}
			break;
			
		case KeyEvent.KEYCODE_BACK:
			if (mIsInputPsw)
			{
				mIsInputPsw = false;
				mHome.mPswInputView.hide();
			}
			else
			{
				mHome.changePage(ViewHome.PAGE_ROLE, 0, 0, null);
			}
			break;
		}
		return true;
	}
	
	private void updateChild()
	{
		if (mHome.mRoles.size() == 0)
		{
			mIsInTitle = true;
		}
		Child role;
		for (int i = 0; i < 3; i++)
		{
			if (i < mHome.mRoles.size())
			{
				mChildViews[i].mLayout.setVisibility(VISIBLE);
				role = mHome.mRoles.get(i);
				setHead(mChildViews[i].mHead, role);
				mChildViews[i].mName.setText(role.mName);
				mChildViews[i].mAge.setText(getAge(role));
			}
			else
			{
				mChildViews[i].mLayout.setVisibility(INVISIBLE);
			}
		}
		
		updateUI();
	}

	private void updateUI()
	{
		for (int i = 0; i < mHome.mRoles.size(); i++)
		{
			mChildViews[i].mHead.setImageResource(R.drawable.head_bg1);
			mChildViews[i].mBtnGuide.setImageDrawable(null);
			mChildViews[i].mBtnRes.setImageDrawable(null);
			mChildViews[i].mBtnDel.setImageDrawable(null);
		}
		mBtnAdd.setImageDrawable(null);
		mBtnPsw.setImageDrawable(null);
		mAddNotice.setVisibility(GONE);
		mPswNotice.setVisibility(GONE);
		
		if (mIsInTitle)
		{
			if (mTitleIndex == 0)
			{
				mBtnAdd.setImageResource(R.drawable.ctrl_title_select);
				if (mHome.mRoles.size() == 0)
				{
					mAddNotice.setVisibility(VISIBLE);
				}
			}
			else
			{
				mBtnPsw.setImageResource(R.drawable.ctrl_title_select);
				if (mHome.mRoles.size() == 0)
				{
					mPswNotice.setVisibility(VISIBLE);
				}
			}
		}
		else
		{
			if (mIndex < 100)
			{
				mChildViews[mIndex].mHead.setImageResource(R.drawable.head_bg2);
			}
			else if (mIndex < 200)
			{
				mChildViews[mIndex - 100].mBtnGuide.setImageResource(R.drawable.ctrl_child_select);
			}
			else if (mIndex < 300)
			{
				mChildViews[mIndex - 200].mBtnRes.setImageResource(R.drawable.ctrl_child_select);
			}
			else if (mIndex < 400)
			{
				mChildViews[mIndex - 300].mBtnDel.setImageResource(R.drawable.ctrl_child_select);
			}
		}
	}
	
	private OnTaskFinishListener mDelChildFinishListener = new OnTaskFinishListener()
	{
		@Override
		public void onFinish(int result, boolean isCancel)
		{
			if (result == 1 && mParserDelChild.mResult == 0)
			{
				mHome.mRoles.remove(mHome.findRole(mParserDelChild.mId));
				Util.ShowToast(getContext(), "删除成功");
				mIndex = 0;
				updateChild();
			}
			else
			{
				Util.ShowToast(getContext(), "删除失败");
			}
			mIsDel = false;
		}
	};
	
	private OnTaskFinishListener mParentPswFinishListener = new OnTaskFinishListener()
	{
		@Override
		public void onFinish(int result, boolean isCancel)
		{
			mIsModifyPsw = false;
			if (result == 1 && mParserParentPsw.mResult == 0)
			{
				Util.ShowToast(getContext(), "密码设置成功");
				mHome.mHasPsw = true;
				mIsInputPsw = false;
				mHome.mPswInputView.hide();
			}
			else
			{
				Util.ShowToast(getContext(), "密码设置失败，请重新输入");
				mPswOld = null;
				mPswNew = null;
				if (!mHome.mHasPsw)
				{
					mPswOld = "";
				}
				mHome.mPswInputView.reset();
			}
		}
	};
	
	private class ChildView
	{
		public ViewGroup mLayout;
		public ImageView mHead;
		public TextView mName, mAge;
		public ImageView mBtnGuide, mBtnRes, mBtnDel;
		
		public ChildView(ViewGroup layout)
		{
			mLayout = layout;
			mHead = (ImageView)mLayout.findViewById(R.id.item_child_head);
			mName = (TextView)mLayout.findViewById(R.id.item_child_name);
			mAge = (TextView)mLayout.findViewById(R.id.item_child_age);
			mBtnGuide = (ImageView)mLayout.findViewById(R.id.item_child_guide);
			mBtnRes = (ImageView)mLayout.findViewById(R.id.item_child_res);
			mBtnDel = (ImageView)mLayout.findViewById(R.id.item_child_del);
		}
	}
}
