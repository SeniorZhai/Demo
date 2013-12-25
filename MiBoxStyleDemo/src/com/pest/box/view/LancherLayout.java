package com.pest.box.view;

import java.util.Random;

import com.pest.box.R;
import com.pest.util.DensityUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * UI界面
 * 
 * @author 李小斌 364643658@qq.com
 * */
public class LancherLayout extends LinearLayout implements ShadowCallBack, View.OnClickListener, View.OnFocusChangeListener {

	private String TAG = "SettingLayout";

	private Context context;

	private ScaleAnimEffect animEffect;
	private ImageView[] shadowBackgrounds = new ImageView[4];
	private FrameLayout[] frameLayoutViews = new FrameLayout[4];
	private ImageView[] imageViews = new ImageView[4];

	private ImageView whiteBorder;// 白色框

	private int randomTemp = -1;

	public LancherLayout(Context paramContext) {
		super(paramContext);
		this.context = paramContext;
		this.animEffect = new ScaleAnimEffect();

		width = DensityUtil.dip2px(context, 247);// 放大前的宽
		height = DensityUtil.dip2px(context, 357);// 放大前的高

		setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		setGravity(Gravity.CENTER_HORIZONTAL);
		setOrientation(LinearLayout.VERTICAL);
		addView(LayoutInflater.from(this.context).inflate(R.layout.layout_lancher, null));
		initView();
	}

	/**
	 * 随机变化item的背景色
	 * */
	private int createRandom(int paramInt) {
		Random localRandom = new Random();
		for (int i = localRandom.nextInt(paramInt);; i = localRandom.nextInt(paramInt)) {
			if (i != this.randomTemp) {
				this.randomTemp = i;
				return i;
			}
		}
	}

	/**
	 * 白色焦点框飞动、移动、变大
	 * 
	 * @param width
	 *                白色框的宽(非放大后的)
	 * @param height
	 *                白色框的高(非放大后的)
	 * @param paramFloat1
	 *                x坐标偏移量，相对于初始的白色框的中心点
	 * @param paramFloat2
	 *                y坐标偏移量，相对于初始的白色框的中心点
	 * */
	private void flyWhiteBorder(int width, int height, float paramFloat1, float paramFloat2) {
		if ((this.whiteBorder != null)) {
			this.whiteBorder.setVisibility(View.VISIBLE);
			int mWidth = this.whiteBorder.getWidth();
			int mHeight = this.whiteBorder.getHeight();
			if (mWidth == 0 || mHeight == 0) {
				mWidth = 1;
				mHeight = 1;
			}
			ViewPropertyAnimator localViewPropertyAnimator = this.whiteBorder.animate();
			localViewPropertyAnimator.setDuration(150L);
			localViewPropertyAnimator.scaleX((float) (width * 1.105) / (float) mWidth);
			localViewPropertyAnimator.scaleY((float) (height * 1.105) / (float) mHeight);
			localViewPropertyAnimator.x(paramFloat1);
			localViewPropertyAnimator.y(paramFloat2);
			localViewPropertyAnimator.start();
		}
	}

	/**
	 * 失去焦点的的动画动作
	 * 
	 * @param paramInt
	 *                失去焦点的item
	 * */
	private void showLooseFocusAinimation(int paramInt) {
		this.animEffect.setAttributs(1.105F, 1.0F, 1.105F, 1.0F, 100L);
		this.imageViews[paramInt].startAnimation(this.animEffect.createAnimation());
		this.shadowBackgrounds[paramInt].setVisibility(View.GONE);
	}

	/**
	 * 获得焦点的item的动画动作
	 * 
	 * @param paramInt
	 *                获得焦点的item
	 * */
	private void showOnFocusAnimation(final int paramInt) {
		this.frameLayoutViews[paramInt].bringToFront();
		this.animEffect.setAttributs(1.0F, 1.105F, 1.0F, 1.105F, 100L);
		Animation localAnimation = this.animEffect.createAnimation();
		localAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				shadowBackgrounds[paramInt].startAnimation(animEffect.alphaAnimation(0.0F, 1.0F, 150L, 0L));
				shadowBackgrounds[paramInt].setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}
		});
		this.imageViews[paramInt].startAnimation(localAnimation);
	}

	private OnClickListener onClickListener;

	public void destroy() {
	}

	/**
	 * 注册OnClickListener监听
	 * */
	public void initListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public void initListener() {
	}

	/**
	 * 初始化视图
	 * */
	public void initView() {
		int[] arrayOfInt = new int[7];
		arrayOfInt[0] = R.drawable.blue_no_shadow;
		arrayOfInt[1] = R.drawable.dark_no_shadow;
		arrayOfInt[2] = R.drawable.green_no_shadow;
		arrayOfInt[3] = R.drawable.orange_no_shadow;
		arrayOfInt[4] = R.drawable.pink_no_shadow;
		arrayOfInt[5] = R.drawable.red_no_shadow;
		arrayOfInt[6] = R.drawable.yellow_no_shadow;

		int i = arrayOfInt.length;
		this.frameLayoutViews[0] = ((FrameLayout) findViewById(R.id.layout_app));
		this.frameLayoutViews[1] = ((FrameLayout) findViewById(R.id.layout_game));
		this.frameLayoutViews[2] = ((FrameLayout) findViewById(R.id.layout_setting));
		this.frameLayoutViews[3] = ((FrameLayout) findViewById(R.id.layout_code));

		this.shadowBackgrounds[0] = ((ImageView) findViewById(R.id.app_shadow));
		this.shadowBackgrounds[1] = ((ImageView) findViewById(R.id.game_shadow));
		this.shadowBackgrounds[2] = ((ImageView) findViewById(R.id.setting_shadow));
		this.shadowBackgrounds[3] = ((ImageView) findViewById(R.id.code_shadow));

		this.imageViews[0] = ((ImageView) findViewById(R.id.app));
		this.imageViews[1] = ((ImageView) findViewById(R.id.game));
		this.imageViews[2] = ((ImageView) findViewById(R.id.setting));
		this.imageViews[3] = ((ImageView) findViewById(R.id.code));

		ImageView[] arrayOfImageView = new ImageView[4];// 倒影图
		arrayOfImageView[0] = ((ImageView) findViewById(R.id.set_refimg_1));
		arrayOfImageView[1] = ((ImageView) findViewById(R.id.set_refimg_2));
		arrayOfImageView[2] = ((ImageView) findViewById(R.id.set_refimg_3));
		arrayOfImageView[3] = ((ImageView) findViewById(R.id.set_refimg_4));

		this.whiteBorder = ((ImageView) findViewById(R.id.white_boder));

		FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(220, 220);
		layoutparams.leftMargin = 168;
		layoutparams.topMargin = 20;
		whiteBorder.setLayoutParams(layoutparams);

		for (int j = 0; j < 4; j++) {
			this.shadowBackgrounds[j].setVisibility(View.GONE);
			// 随机产生背景色
			// imageViews[j].setBackgroundResource(arrayOfInt[createRandom(i)]);
			imageViews[j].setOnFocusChangeListener(this);
			imageViews[j].setOnClickListener(this);
			arrayOfImageView[j].setImageBitmap(ImageReflect.createCutReflectedImage(ImageReflect.convertViewToBitmap(frameLayoutViews[j]), 0));
		}
	}

	public void onClick(View paramView) {
		if (onClickListener != null) {
			onClickListener.onClick(paramView);
		}
	}

	private float x = 0.0F;
	private float y = 0.0F;
	private int width = 0;// 放大前的宽
	private int height = 0;// 放大前的高

	private OnFocusChangeListener onFocusChangeListener;

	/**
	 * 注册焦点监听的动作
	 * */
	public void initListener(OnFocusChangeListener onFocusChangeListener) {
		this.onFocusChangeListener = onFocusChangeListener;
	}

	public void onFocusChange(View paramView, boolean paramBoolean) {
		if (onFocusChangeListener != null) {
			onFocusChangeListener.onFocusChange(paramView, paramBoolean);
		}
		int i = -1;
		switch (paramView.getId()) {
		case R.id.app:
			i = 0;
			// 此处设置不同大小的item的长宽 ,下面同理省去，同样大小，就未设置,直接初始
			// width = DensityUtil.dip2px(context, 247);// 放大前的宽
			// height = DensityUtil.dip2px(context, 357);// 放大前的高
			x = DensityUtil.dip2px(context, 118);
			y = DensityUtil.dip2px(context, 96);
			// 此处可封装出数学算法，计算偏移量的x,y值。我懒得算了，直接设置了
			break;
		case R.id.game:
			i = 1;
			x = DensityUtil.dip2px(context, 374);
			y = DensityUtil.dip2px(context, 96);
			break;
		case R.id.setting:
			i = 2;
			x = DensityUtil.dip2px(context, 634);
			y = DensityUtil.dip2px(context, 96);
			break;
		case R.id.code:
			i = 3;
			x = DensityUtil.dip2px(context, 892);
			y = DensityUtil.dip2px(context, 96);
			break;
		}
		if (paramBoolean) {
			showOnFocusAnimation(i);
			Message message = new Message();
			message.what = 0;
			mHandler.sendMessage(message);
		} else {
			showLooseFocusAinimation(i);
			this.whiteBorder.setVisibility(View.INVISIBLE);
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			flyWhiteBorder(width, height, x, y);
		}
	};

	public void updateData() {
	}
}