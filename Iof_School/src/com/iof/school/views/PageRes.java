package com.iof.school.views;

import java.util.ArrayList;

import com.iof.school.R;
import com.iof.school.obj.Child;
import com.iof.school.obj.ResLogoFile;
import com.iof.school.parser.ParserResList;
import com.iof.school.utils.Const;
import com.iof.showlib.obj.Res;
import com.iof.showlib.utils.ImageDownload;
import com.iof.showlib.utils.TaskBase;
import com.iof.showlib.utils.Util;
import com.iof.showlib.utils.TaskBase.OnTaskFinishListener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PageRes extends PageBase
{
	private static final int PAGE_SIZE = 8;
	
	private ResView[] mResViews = new ResView[PAGE_SIZE];
	private LinearLayout mProgressLayout, mCateLayout;
	private View mListSelect;
	private TextView mPageView;
	
	private ArrayList<Res> mRes = new ArrayList<Res>();
	private ParserResList mParserResList = new ParserResList();
	private TaskBase mTask;
	private Child mChild;
	private ImageDownload mDownload = new ImageDownload(getContext());
	
	private int mIndex = 100;
	private int mLastCate;
	private int mChildIndex;
	private int mStartIndex;
	
	private boolean mIsPage;
	private boolean mIsGetRes;
	
	public PageRes(ViewHome home)
	{
		super(home);
		((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.page_res, this);
		
		init();
	}
	
	private void init()
	{
		mProgressLayout = (LinearLayout)findViewById(R.id.page_res_progress_layout);
		mCateLayout = (LinearLayout)findViewById(R.id.page_res_type_layout);
		mListSelect = findViewById(R.id.page_res_list_select);
		mResViews[0] = new ResView((LinearLayout)findViewById(R.id.item_res_1));
		mResViews[1] = new ResView((LinearLayout)findViewById(R.id.item_res_2));
		mResViews[2] = new ResView((LinearLayout)findViewById(R.id.item_res_3));
		mResViews[3] = new ResView((LinearLayout)findViewById(R.id.item_res_4));
		mResViews[4] = new ResView((LinearLayout)findViewById(R.id.item_res_5));
		mResViews[5] = new ResView((LinearLayout)findViewById(R.id.item_res_6));
		mResViews[6] = new ResView((LinearLayout)findViewById(R.id.item_res_7));
		mResViews[7] = new ResView((LinearLayout)findViewById(R.id.item_res_8));
		mPageView = (TextView)findViewById(R.id.item_res_page);
	}
	
	/**
	 * @param arg1 进入的孩子的序号，100表示家长，-1表示从资源信息界面返回
	 * @param arg2
	 * @param obj 孩子信息
	 */
	protected void enter(int arg1, int arg2, Object obj)
	{
		if (arg1 == -1)
		{
			updateRes();
		}
		else
		{
			mChild = (Child)obj;
			mChildIndex = arg1;
			mIsPage = false;
			mLastCate = mIndex = 100;
			mStartIndex = 0;
			if (mCateLayout.getChildCount() == 0)
			{
				updateType();
			}
			getRes(true);
		}
		updateUI();
		if (!mDownload.isRun())
		{
			mDownload.start();
		}
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		if (msg.what == Const.HD_ID_LOGO_DOWN)
		{
			if (msg.arg1 == 1 && msg.arg2 >= 0 && msg.arg2 < PAGE_SIZE && mStartIndex + msg.arg2 < mRes.size()
					&& mRes.get(mStartIndex + msg.arg2).mId == ((Res)msg.obj).mId)
			{
				Drawable drawable = mHome.getLogo(((Res)msg.obj).mId);
				if (drawable == null)
				{
					mResViews[msg.arg2].mLogo.setBackgroundResource(R.drawable.res_default);
				}
				else
				{
					mResViews[msg.arg2].mLogo.setBackgroundDrawable(drawable);
				}
			}
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (mIsPage)
			{
				prePage();
			}
			else if (mIndex < 100)
			{
				if (mRes.size() > 0)
				{
					if (mIndex % 4 == 0)
					{
						mIndex += 3;
					}
					else
					{
						mIndex--;
					}
				}
			}
			else
			{
				if (mIndex == 100)
				{
					mIndex = 100 + mCateLayout.getChildCount() - 1;
				}
				else
				{
					mIndex--;
				}
				mLastCate = mIndex;
				mStartIndex = 0;
				getRes(true);
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (mIsPage)
			{
				nextPage();
			}
			else if (mIndex < 100)
			{
				if (mRes.size() > 0)
				{
					if (mIndex % 4 == 3)
					{
						mIndex -= 3;
					}
					else
					{
						mIndex++;
					}
				}
			}
			else
			{
				if (mIndex == 100 + mCateLayout.getChildCount() - 1)
				{
					mIndex = 100;
				}
				else
				{
					mIndex++;
				}
				mLastCate = mIndex;
				mStartIndex = 0;
				getRes(true);
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_UP:
			if (mIsPage)
			{
				mIsPage = false;
				mIndex = mLastCate;
			}
			else if (mIndex < 100)
			{
				if (mIndex < 4)
				{
					if (mStartIndex + PAGE_SIZE <= mRes.size())
					{
						mIndex += 4;
					}
					else
					{
						mIndex = mIndex + (mRes.size() - mStartIndex - 1 - mIndex) / 4 * 4;
					}
				}
				else
				{
					mIndex -= 4;
				}
			}
			else if (mRes.size() > 0 && !mIsGetRes)
			{
				mIsPage = true;
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (mIsPage)
			{
				mIsPage = false;
				mIndex = mLastCate;
			}
			else if (mIndex < 100)
			{
				if (mIndex < 4)
				{
					if (mStartIndex + PAGE_SIZE <= mRes.size())
					{
						mIndex += 4;
					}
					else
					{
						mIndex = mIndex + (mRes.size() - mStartIndex - 1 - mIndex) / 4 * 4;
					}
				}
				else
				{
					mIndex -= 4;
				}
			}
			else if (mRes.size() > 0 && !mIsGetRes)
			{
				mIsPage = true;
			}
			break;
			
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (mIsPage)
			{
				mIsPage = false;
				mIndex = 0;
			}
			else if (mIndex < 100)
			{
				mHome.changePage(ViewHome.PAGE_INFO, 0, 0, mRes.get(mStartIndex + mIndex));
				return true;
			}
			else
			{
				mIsPage = true;
			}
			break;
			
		case KeyEvent.KEYCODE_BACK:
			if (!mIsPage && mIndex < 100)
			{
				mIsPage = true;
			}
			else if (mIsPage)
			{
				mIsPage = false;
				mIndex = mLastCate;
			}
			else if (mChildIndex < mHome.mRoles.size())
			{
				mHome.changePage(ViewHome.PAGE_ROLE, 0, 0, null);
				mHome.saveTimeRecord();
			}
			else
			{
				mHome.changePage(ViewHome.PAGE_SET, -1, 0, null);
			}
			break;
		}
		
		updateUI();
		return true;
	}
	
	@Override
	public void onPause()
	{
		if (mChildIndex != 100)
		{
			mHome.saveTimeRecord();
		}
	}

	@Override
	public void onResume()
	{
		if (mChildIndex != 100)
		{
			mHome.startRecord(mChild.mId);
		}
	}
	
	private void prePage()
	{
		if (mStartIndex == 0)
		{
			Util.ShowToast(getContext(), "已经是第一页");
		}
		else
		{
			mStartIndex -= PAGE_SIZE;
			updateRes();
		}
	}
	
	private void nextPage()
	{
		int index = mStartIndex + PAGE_SIZE;
		if (index < mRes.size())
		{
			mStartIndex = index;
			updateRes();
		}
		else if (index < mParserResList.mTotal)
		{
			mStartIndex = index;
			getRes(false);
		}
		else
		{
			Util.ShowToast(getContext(), "已经是最后一页");
		}
	}
	
	private TextView getCateView(String text)
	{
		LayoutParams params = new LayoutParams(215, 97);
		
		TextView view = new TextView(getContext());
		view.setLayoutParams(params);
		view.setText(text);
		view.setTextSize(32);
		view.setTextColor(0xFF286060);
		view.setPadding(0, 18, 0, 0);
		view.setGravity(Gravity.CENTER);
		
		return view;
	}
	
	private void getRes(boolean clear)
	{
		mIsGetRes = true;
		mProgressLayout.setVisibility(View.VISIBLE);
		if (mTask != null)
		{
			mTask.stop();
			mTask = null;
		}
		if (clear)
		{
			mParserResList.setInfo(mHome.mCategorys.get(mLastCate - 100).mId, 1);
		}
		else
		{
			mParserResList.nextPage();
		}
		mTask = new TaskBase(getContext(), mParserResList, mFinishListener);
		mTask.execute();
	}
	
	public void updateType()
	{
		mCateLayout.removeAllViews();
		for (int i = 0; i < mHome.mCategorys.size(); i++)
		{
			mCateLayout.addView(getCateView(mHome.mCategorys.get(i).mName));
		}
	}
	
	private void updateRes()
	{
		Res res;
		Drawable drawable;
		int index;
		for (int i = 0; i < mResViews.length; i++)
		{
			index = mStartIndex + i;
			if (index < mRes.size())
			{
				mResViews[i].mLayout.setVisibility(VISIBLE);
				res = mRes.get(index);
				mResViews[i].mName.setText(res.mName);
				mResViews[i].mAge.setText(res.getAge());
				if (res.isFree())
				{
					mResViews[i].mPrice.setTextColor(0xFF6e4333);
					mResViews[i].mPrice.setText("免费");
				}
				else if (res.mHasPay)
				{
					mResViews[i].mPrice.setTextColor(0xFF6e4333);
					mResViews[i].mPrice.setText("已购买");
				}
				else
				{
					mResViews[i].mPrice.setTextColor(0xFFc63131);
					mResViews[i].mPrice.setText("¥" + res.mPrice);
				}
				drawable = mHome.getLogo(res.mId);
				if (drawable == null)
				{
					mResViews[i].mLogo.setBackgroundResource(R.drawable.res_default);
					mDownload.addFile(false, new ResLogoFile(res, mHome.mHandler, i));
				}
				else
				{
					mResViews[i].mLogo.setBackgroundDrawable(drawable);
				}
			}
			else
			{
				mResViews[i].mLayout.setVisibility(INVISIBLE);
			}
		}
		int page;
		if (mParserResList.mTotal == 0)
		{
			page = 0;
		}
		else
		{
			page = (mParserResList.mTotal - 1) / PAGE_SIZE + 1;
		}
		mPageView.setText("第" + (mStartIndex / PAGE_SIZE + 1) + "页/共" + page + "页");
	}

	private void updateUI()
	{
		for (int i = 0; i < mResViews.length; i++)
		{
			mResViews[i].mLayout.setBackgroundResource(R.drawable.item_res_bg1);
		}
		for (int i = 0; i < mCateLayout.getChildCount(); i++)
		{
			if (i == mLastCate - 100)
			{
				mCateLayout.getChildAt(i).setBackgroundResource(R.drawable.cate_bg2);
			}
			else
			{
				mCateLayout.getChildAt(i).setBackgroundDrawable(null);
			}
			((TextView)mCateLayout.getChildAt(i)).setTextColor(0xFF286060);
		}
		mListSelect.setVisibility(View.GONE);
		
		if (mIsPage)
		{
			mListSelect.setVisibility(View.VISIBLE);
		}
		else if (mIndex < 100)
		{
			if (mIndex >= mRes.size())
			{
				mIndex = mRes.size() - 1;
			}
			mResViews[mIndex].mLayout.setBackgroundResource(R.drawable.item_res_bg2);
		}
		else
		{
			mCateLayout.getChildAt(mIndex - 100).setBackgroundResource(R.drawable.cate_bg3);
		}
		((TextView)mCateLayout.getChildAt(mLastCate - 100)).setTextColor(0xFFffffff);
	}
	
	private OnTaskFinishListener mFinishListener = new OnTaskFinishListener()
	{
		@Override
		public void onFinish(int result, boolean isCancel)
		{
			mIsGetRes = false;
			mProgressLayout.setVisibility(View.GONE);
			if (result == 0 || isCancel)
			{
				//Util.ShowToast(getContext(), "获取资源失败");
				return;
			}
			if (mParserResList.mPage == 1)
			{
				mRes.clear();
			}
			mRes.addAll(mParserResList.mRes);
			updateRes();
			updateUI();
		}
	};
	
	private class ResView
	{
		public LinearLayout mLayout;
		public ImageView mLogo;
		public TextView mName, mAge, mPrice;
		
		public ResView(LinearLayout layout)
		{
			mLayout = layout;
			mLogo = (ImageView)mLayout.findViewById(R.id.item_res_logo);
			mName = (TextView)mLayout.findViewById(R.id.item_res_name);
			mAge = (TextView)mLayout.findViewById(R.id.item_res_age);
			mPrice = (TextView)mLayout.findViewById(R.id.item_res_price);
		}
	}
}
