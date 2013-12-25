package com.zoe.slidingmenu.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zoe.slidingmenu.view.FixListViewBugLinearLayout.OnScrollListener;

/**
 * 这是存放View的容器，也是控制类
 * 
 */

public class SlidingMenu extends RelativeLayout implements GestureDetector.OnGestureListener, OnTouchListener {
	public static final String TAG = "COSlidingMenu";
	private SlidingView coSlidingView;
	private boolean hasMeasured;
	private ViewGroup leftView;
	private ViewGroup rightView;
	private View centerView;
	private int leftViewWidth;// 左边菜单的宽度
	private int rightViewWidth;// 右边菜单的宽度
	private int screenWidth;// 屏幕的宽度
	private final static int SPEED = 30;// 速率
	private int currentSpeed;// 当前的速率
	private GestureDetector mGestureDetector;
	private View currentOnTouchView;// 当前触摸的view
	private boolean isScrolling;// 是否正在滚动
	private int mScrollX;// 横向滚动的X
	private SlidingState currentUIState;// 下次的界面状态

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		coSlidingView = new SlidingView(context, attrs);
		addView(coSlidingView);

		ViewTreeObserver viewTreeObserver = coSlidingView.getViewTreeObserver();
		// 获取控件宽度
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					screenWidth = centerView.getWidth();
					rightViewWidth = rightView.getWidth();
					leftViewWidth = leftView.getWidth();
					log("screenWidth=" + screenWidth + "|rightViewWidth=" + rightViewWidth + "|leftViewWidth=" + leftViewWidth);

					RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
					centerLayoutParams.leftMargin = 0;
					setMenusLatyouParamsWithCenterLayoutParam(centerLayoutParams);
					hasMeasured = true;
					currentUIState = SlidingState.SHOWCENTER;
					initView();
				}
				return true;
			}
		});

	}

	/**
	 * 初始化
	 */
	protected void initView() {
		mGestureDetector = new GestureDetector(this);
		leftView.setOnTouchListener(this);
		rightView.setOnTouchListener(this);
		centerView.setOnTouchListener(this);
		// 添加监听事件
		int leftViewCount = leftView.getChildCount();
		for (int i = 0; i < leftViewCount; i++) {
			View eachChildView = leftView.getChildAt(i);
			if (eachChildView instanceof FixListViewBugLinearLayout) {
				((FixListViewBugLinearLayout) eachChildView).setOnScrollListener(onScrollListener);
			}
		}
		int rightViewCount = rightView.getChildCount();
		for (int i = 0; i < rightViewCount; i++) {
			View eachChildView = rightView.getChildAt(i);
			if (eachChildView instanceof FixListViewBugLinearLayout) {
				((FixListViewBugLinearLayout) eachChildView).setOnScrollListener(onScrollListener);
			}
		}
		// View[] leftViewChilds = leftView.
	}

	private OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void doScroll(float distanceX) {
			doScrolling(distanceX);
		}

		@Override
		public void doLoosen() {
			asynMove();
		}
	};

	/**
	 * 设置左边的view
	 */
	public void setLeftView(ViewGroup view, int leftWidth) {
		leftView = view;
		RelativeLayout.LayoutParams lp = new LayoutParams(leftWidth, LayoutParams.FILL_PARENT);
		addView(view, lp);

	}

	/**
	 * 设置右边的view
	 */
	public void setRightView(ViewGroup view, int rightWidth) {
		rightView = view;
		RelativeLayout.LayoutParams lp = new LayoutParams(rightWidth, LayoutParams.FILL_PARENT);
		addView(view, lp);
	}

	/**
	 * 设置中间的view
	 */
	public void setCenterView(View view) {
		centerView = view;
		RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		addView(view, lp);
	}

	/**
	 * 获取当前界面的状态
	 */
	public SlidingState getCurrentUIState() {
		log(currentUIState.getDesc());
		return currentUIState;
	}

	/**
	 * 根据界面状态显示界面
	 */
	public void showViewState(SlidingState nextState) {
		if (currentUIState == nextState) {
			return;
		}
		RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
		int centerMargin = centerLayoutParams.leftMargin;
		switch (nextState) {
		case SHOWLEFT:
			// 大于右边菜单的一半则是3，其他是2
			currentSpeed = SPEED;
			break;
		case SHOWCENTER:
			// 如果当前的状态是左边或右边的菜单显示的话，则显示为首页
			if (centerMargin == -rightViewWidth) {
				currentSpeed = SPEED;
			} else if (centerMargin == leftViewWidth) {
				currentSpeed = -SPEED;
			}

			break;
		case SHOWRIGHT:
			currentSpeed = -SPEED;
			break;

		default:
			break;
		}
		currentUIState = nextState;
		new AsynMove().execute();
	}

	/**
	 * 根据首页的leftMargin调整左右菜单的margin
	 */
	private void setMenusLatyouParamsWithCenterLayoutParam(LayoutParams centerLayoutParams) {
		// 如果是
		RelativeLayout.LayoutParams rightLayoutParams = (RelativeLayout.LayoutParams) rightView.getLayoutParams();
		RelativeLayout.LayoutParams leftLayoutParams = (RelativeLayout.LayoutParams) leftView.getLayoutParams();
		// 重新设置左边菜单的rightMargin，不然会变形
		rightLayoutParams.leftMargin = screenWidth + centerLayoutParams.leftMargin;
		rightLayoutParams.rightMargin = -rightLayoutParams.leftMargin;

		// 不加这个，往右拉的时候首页会变形
		centerLayoutParams.rightMargin = -centerLayoutParams.leftMargin;

		leftLayoutParams.leftMargin = -leftViewWidth + centerLayoutParams.leftMargin;
		leftLayoutParams.rightMargin = -rightViewWidth - leftLayoutParams.leftMargin;

		// log("centerLayoutParams.leftMargin=" + centerLayoutParams.leftMargin
		// + ",rightLayoutParams.leftMargin =" + rightLayoutParams.leftMargin
		// + ",leftLayoutParams.leftMargin =" + leftLayoutParams.leftMargin);
		centerView.setLayoutParams(centerLayoutParams);
		rightView.setLayoutParams(rightLayoutParams);
		leftView.setLayoutParams(leftLayoutParams);

	}

	/**
	 * 日志
	 */
	private void log(String msg) {
		Log.d(TAG, msg);
	}

	/**
	 * 异步移动
	 */
	protected void asynMove() {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
		// 缩回去
		int centerMargin = layoutParams.leftMargin;
		// 如果目前是左边出现或右边出现，或只是 首页出现，则不继续执行
		if (centerMargin == -rightViewWidth || centerMargin == 0 || centerMargin == leftViewWidth) {
			return;
		}
		// 大于 左边菜单的一半则是1，
		// 大于右边菜单的一半则是3，其他是2
		int seed = 0;
		if (centerMargin < -rightViewWidth / 2) {
			currentUIState = SlidingState.SHOWRIGHT;
			seed = -SPEED;
		} else if (centerMargin > leftViewWidth / 2) {
			currentUIState = SlidingState.SHOWLEFT;
			seed = SPEED;
		} else {
			seed = centerMargin > 0 ? -SPEED : SPEED;
			currentUIState = SlidingState.SHOWCENTER;
		}
		Log.d(TAG, "state=" + currentUIState + "|seed=" + seed);
		currentSpeed = seed;
		new AsynMove().execute();
	}

	/***
	 * listview 正在滑动时执行.
	 */
	void doScrolling(float distanceX) {
		if (!isScrolling) {
			isScrolling = true;
		}
		mScrollX += distanceX;// distanceX:向左为正，右为负

		RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
		centerLayoutParams.leftMargin -= mScrollX;
		log("distanceX=" + distanceX + "|centerLayoutParams.leftMargin =" + centerLayoutParams.leftMargin);
		if (centerLayoutParams.leftMargin >= leftViewWidth) {
			isScrolling = false;// 拖过头了不需要再执行AsynMove了
			// 左边拖到头了
			currentUIState = SlidingState.SHOWLEFT;
			log("左边真的拖过头了");
			centerLayoutParams.leftMargin = leftViewWidth;

		} else if (centerLayoutParams.leftMargin <= -rightViewWidth) {
			// 拖过头了不需要再执行AsynMove了
			currentUIState = SlidingState.SHOWRIGHT;
			isScrolling = false;
			log("右边真的拖过头了");
			centerLayoutParams.leftMargin = -leftViewWidth;
		}

		setMenusLatyouParamsWithCenterLayoutParam(centerLayoutParams);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		if (null != currentOnTouchView && (currentOnTouchView instanceof ListView)) {
			ListView listView = (ListView) currentOnTouchView;

			int position = listView.pointToPosition((int) e.getX(), (int) e.getY());
			if (position != ListView.INVALID_POSITION) {
				View child = listView.getChildAt(position - listView.getFirstVisiblePosition());
				if (child != null)
					child.setPressed(true);

			}
		}
		mScrollX = 0;
		isScrolling = false;
		// 将之改为true，才会传递给onSingleTapUp,不然事件不会向下传递.
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// 执行滑动.
		doScrolling(distanceX);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.d(TAG, "onSingleTapUp=" + currentOnTouchView.getClass().getSimpleName());
		// 在这里做平时控件的按键事件
		if (currentOnTouchView.isClickable()) {
			currentOnTouchView.performClick();
		}
		onclickViewById(currentOnTouchView.getId(), e);
		return false;
	}

	/**
	 * 基本的点击事件放在这里
	 */
	private void onclickViewById(int id, MotionEvent e) {

		if (centerView.getId() == id) {
			log("点击了centerView");
			showViewState(SlidingState.SHOWCENTER);
		} else {
			log("点击了其他" + currentOnTouchView.getClass().getSimpleName());
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		currentOnTouchView = v;// 记录点击的控件

		// 松开的时候要判断，如果不到半屏幕位子则缩回去，
		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			asynMove();
			return false;
		}
		// Log.d(TAG, "onTouch view=" + v.getClass().getSimpleName());
		return mGestureDetector.onTouchEvent(event);

	}

	class AsynMove extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			//  计算time
			int moveX = 0;
			RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();

			int currentCenterMarginLeft = centerLayoutParams.leftMargin;
			switch (currentUIState) {
			// 如果是往左
			case SHOWLEFT:
				moveX = leftViewWidth - currentCenterMarginLeft ;
				break;
			case SHOWCENTER:
				moveX = Math.abs(currentCenterMarginLeft);
				break;
			case SHOWRIGHT:
				moveX = currentCenterMarginLeft +rightViewWidth;
			default:
				break;
			}

			int times = moveX % SPEED > 0 ? moveX / SPEED + 1 : moveX / SPEED;
			// getCurrentUIState();
			log("动画的次数是" + times + "|移动的距离是moveX = " + moveX + "|currentCenterMarginLeft=" + currentCenterMarginLeft);
			for (int i = 0; i < times; i++) {
				publishProgress(0);
				// publishProgress(values)
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;

		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();

			// 右移动
			// 这里只是设置center的leftMargin，其他的根据该值调整
			switch (currentUIState) {
			case SHOWLEFT:
				// 往左边移
				centerLayoutParams.leftMargin = Math.min(centerLayoutParams.leftMargin + currentSpeed, leftViewWidth);

				break;
			case SHOWCENTER:
				if (currentSpeed > 0) {
					centerLayoutParams.leftMargin = Math.min(centerLayoutParams.leftMargin + currentSpeed, 0);
				} else {
					centerLayoutParams.leftMargin = Math.max(centerLayoutParams.leftMargin + currentSpeed, 0);
				}

				break;
			case SHOWRIGHT:
				centerLayoutParams.leftMargin = Math.max(centerLayoutParams.leftMargin + currentSpeed, -rightViewWidth);

				break;

			default:
				break;
			}

			setMenusLatyouParamsWithCenterLayoutParam(centerLayoutParams);

		}

	}

}
