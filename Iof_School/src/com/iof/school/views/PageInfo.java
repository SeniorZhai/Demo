package com.iof.school.views;

import com.iof.school.R;
import com.iof.school.parser.ParserResInfo;
import com.iof.school.utils.Const;
import com.iof.showlib.obj.Res;
import com.iof.showlib.utils.TaskBase;
import com.iof.showlib.utils.Util;
import com.iof.showlib.utils.TaskBase.OnTaskFinishListener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PageInfo extends PageBase
{
	private ImageView mViewLogo;
	private TextView mViewName, mViewAge, mViewPriceLab, mViewPrice, mViewIntro, mBtnPlay;
	private View mBtnPlaySelect, mBtnBackSelect;
	private LinearLayout mOtherLayout;
	
	private Res mRes;
	private ParserResInfo mParserResInfo = new ParserResInfo();
	private int mBtnIndex;
	
	public PageInfo(ViewHome home)
	{
		super(home);
		((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.page_info, this);
		
		init();
	}
	
	private void init()
	{
		mViewLogo = (ImageView)findViewById(R.id.page_info_logo);
		mViewName = (TextView)findViewById(R.id.page_info_name);
		mViewAge = (TextView)findViewById(R.id.page_info_age);
		mViewPriceLab = (TextView)findViewById(R.id.page_info_price_lab);
		mViewPrice = (TextView)findViewById(R.id.page_info_price);
		mViewIntro = (TextView)findViewById(R.id.page_info_intro);
		mBtnPlay = (TextView)findViewById(R.id.page_info_btn_play);
		mBtnPlaySelect = findViewById(R.id.page_info_btn_play_select);
		mBtnBackSelect = findViewById(R.id.page_info_btn_back_select);
		mOtherLayout = (LinearLayout)findViewById(R.id.page_info_other_layout);
	}
	
	/**
	 * @param arg1 >0，表示从播放界面返回
	 * @param arg2
	 * @param obj 显示的资源
	 */
	protected void enter(int arg1, int arg2, Object obj)
	{
		if (arg1 == 0)
		{
			mBtnIndex = 0;
			mOtherLayout.removeAllViews();
			updateRes((Res)obj);
			if (mRes.mIntro == null)
			{
				mParserResInfo.setInfo(mRes);
				new TaskBase(getContext(), mParserResInfo, mFinishListener).execute();
			}
		}
	}
	
	@Override
	public boolean handleMessage(Message msg)
	{
		if (msg.what == Const.HD_ID_BUY_OVER)
		{
			Util.ShowToast(getContext(), "购买成功！");
			mRes.mHasPay = true;
			mViewPriceLab.setText("已购买");
			mViewPrice.setText("");
			mBtnPlay.setText("播   放");
			mBtnPlay.setBackgroundResource(R.drawable.info_btn4);
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			mBtnIndex = 1 - mBtnIndex;
			updateUI();
			break;
			
		case KeyEvent.KEYCODE_DPAD_UP:
			break;
			
		case KeyEvent.KEYCODE_DPAD_DOWN:
			break;
			
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (mBtnIndex == 0)
			{
				if (mRes.isFree() || mRes.mHasPay)
				{
					mHome.gotoRes(mRes);
				}
				else
				{
					//TODO 购买
					mHome.mHandler.sendEmptyMessageDelayed(Const.HD_ID_BUY_OVER, 400);
				}
			}
			else if (mBtnIndex == 1)
			{
				mHome.changePage(ViewHome.PAGE_RES, -1, 0, null);
			}
			break;
			
		case KeyEvent.KEYCODE_BACK:
			mHome.changePage(ViewHome.PAGE_RES, -1, 0, null);
			break;
		}
		return true;
	}
	
	private InfoViewBase getInfoView(Res res)
	{
		if (res.mType == Res.TYPE_VIDEO)
		{
			return new InfoViewVedio(getContext());
		}
		return null;
	}
	
	private void updateRes(Res res)
	{
		mRes = res;
		
		mViewName.setText(res.mName);
		mViewAge.setText(res.getAge());
		Drawable drawable = mHome.getLogo(res.mId);
		if (drawable == null)
		{
			mViewLogo.setImageResource(R.drawable.res_default);
		}
		else
		{
			mViewLogo.setImageDrawable(drawable);
		}
		if (res.isFree())
		{
			mViewPriceLab.setText("免费");
			mViewPrice.setText("");
		}
		else if (res.mHasPay)
		{
			mViewPriceLab.setText("已购买");
			mViewPrice.setText("");
		}
		else
		{
			mViewPriceLab.setText("价格：");
			mViewPrice.setText("¥" + res.mPrice);
		}
		if (res.mIntro == null)
		{
			mViewIntro.setText("");
		}
		else
		{
			mViewIntro.setText(res.mIntro);
		}
		InfoViewBase view = getInfoView(res);
		if (view != null)
		{
			view.initInfo(res);
			mOtherLayout.addView(view);
		}
		
		updateUI();
	}

	private void updateUI()
	{
		if (mRes.isFree() || mRes.mHasPay)
		{
			mBtnPlay.setText("播   放");
			mBtnPlay.setBackgroundResource(R.drawable.info_btn4);
		}
		else
		{
			mBtnPlay.setText("购   买");
			mBtnPlay.setBackgroundResource(R.drawable.info_btn3);
		}
		mBtnPlaySelect.setVisibility(INVISIBLE);
		mBtnBackSelect.setVisibility(INVISIBLE);
		
		if (mBtnIndex == 0)
		{
			mBtnPlaySelect.setVisibility(VISIBLE);
		}
		else
		{
			mBtnBackSelect.setVisibility(VISIBLE);
		}
	}
	
	private OnTaskFinishListener mFinishListener = new OnTaskFinishListener()
	{
		@Override
		public void onFinish(int result, boolean isCancel)
		{
			if (result == 1)
			{
				if (mRes.mIntro == null)
				{
					mViewIntro.setText("");
				}
				else
				{
					mViewIntro.setText(mRes.mIntro);
				}
				try
				{
					if (mOtherLayout.getChildCount() > 0)
					{
						((InfoViewBase)mOtherLayout.getChildAt(0)).initInfo(mRes);
					}
				}
				catch (Exception e)
				{
				}
			}
		}
	};
}
