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
 * ���Ǵ��View��������Ҳ�ǿ�����
 * 
 */

public class SlidingMenu extends RelativeLayout implements GestureDetector.OnGestureListener, OnTouchListener {
	public static final String TAG = "COSlidingMenu";
	private SlidingView coSlidingView;
	private boolean hasMeasured;
	private ViewGroup leftView;
	private ViewGroup rightView;
	private View centerView;
	private int leftViewWidth;// ��߲˵��Ŀ��
	private int rightViewWidth;// �ұ߲˵��Ŀ��
	private int screenWidth;// ��Ļ�Ŀ��
	private final static int SPEED = 30;// ����
	private int currentSpeed;// ��ǰ������
	private GestureDetector mGestureDetector;
	private View currentOnTouchView;// ��ǰ������view
	private boolean isScrolling;// �Ƿ����ڹ���
	private int mScrollX;// ���������X
	private SlidingState currentUIState;// �´εĽ���״̬

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		coSlidingView = new SlidingView(context, attrs);
		addView(coSlidingView);

		ViewTreeObserver viewTreeObserver = coSlidingView.getViewTreeObserver();
		// ��ȡ�ؼ����
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
	 * ��ʼ��
	 */
	protected void initView() {
		mGestureDetector = new GestureDetector(this);
		leftView.setOnTouchListener(this);
		rightView.setOnTouchListener(this);
		centerView.setOnTouchListener(this);
		// ��Ӽ����¼�
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
	 * ������ߵ�view
	 */
	public void setLeftView(ViewGroup view, int leftWidth) {
		leftView = view;
		RelativeLayout.LayoutParams lp = new LayoutParams(leftWidth, LayoutParams.FILL_PARENT);
		addView(view, lp);

	}

	/**
	 * �����ұߵ�view
	 */
	public void setRightView(ViewGroup view, int rightWidth) {
		rightView = view;
		RelativeLayout.LayoutParams lp = new LayoutParams(rightWidth, LayoutParams.FILL_PARENT);
		addView(view, lp);
	}

	/**
	 * �����м��view
	 */
	public void setCenterView(View view) {
		centerView = view;
		RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		addView(view, lp);
	}

	/**
	 * ��ȡ��ǰ�����״̬
	 */
	public SlidingState getCurrentUIState() {
		log(currentUIState.getDesc());
		return currentUIState;
	}

	/**
	 * ���ݽ���״̬��ʾ����
	 */
	public void showViewState(SlidingState nextState) {
		if (currentUIState == nextState) {
			return;
		}
		RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
		int centerMargin = centerLayoutParams.leftMargin;
		switch (nextState) {
		case SHOWLEFT:
			// �����ұ߲˵���һ������3��������2
			currentSpeed = SPEED;
			break;
		case SHOWCENTER:
			// �����ǰ��״̬����߻��ұߵĲ˵���ʾ�Ļ�������ʾΪ��ҳ
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
	 * ������ҳ��leftMargin�������Ҳ˵���margin
	 */
	private void setMenusLatyouParamsWithCenterLayoutParam(LayoutParams centerLayoutParams) {
		// �����
		RelativeLayout.LayoutParams rightLayoutParams = (RelativeLayout.LayoutParams) rightView.getLayoutParams();
		RelativeLayout.LayoutParams leftLayoutParams = (RelativeLayout.LayoutParams) leftView.getLayoutParams();
		// ����������߲˵���rightMargin����Ȼ�����
		rightLayoutParams.leftMargin = screenWidth + centerLayoutParams.leftMargin;
		rightLayoutParams.rightMargin = -rightLayoutParams.leftMargin;

		// �����������������ʱ����ҳ�����
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
	 * ��־
	 */
	private void log(String msg) {
		Log.d(TAG, msg);
	}

	/**
	 * �첽�ƶ�
	 */
	protected void asynMove() {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
		// ����ȥ
		int centerMargin = layoutParams.leftMargin;
		// ���Ŀǰ����߳��ֻ��ұ߳��֣���ֻ�� ��ҳ���֣��򲻼���ִ��
		if (centerMargin == -rightViewWidth || centerMargin == 0 || centerMargin == leftViewWidth) {
			return;
		}
		// ���� ��߲˵���һ������1��
		// �����ұ߲˵���һ������3��������2
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
	 * listview ���ڻ���ʱִ��.
	 */
	void doScrolling(float distanceX) {
		if (!isScrolling) {
			isScrolling = true;
		}
		mScrollX += distanceX;// distanceX:����Ϊ������Ϊ��

		RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
		centerLayoutParams.leftMargin -= mScrollX;
		log("distanceX=" + distanceX + "|centerLayoutParams.leftMargin =" + centerLayoutParams.leftMargin);
		if (centerLayoutParams.leftMargin >= leftViewWidth) {
			isScrolling = false;// �Ϲ�ͷ�˲���Ҫ��ִ��AsynMove��
			// ����ϵ�ͷ��
			currentUIState = SlidingState.SHOWLEFT;
			log("�������Ϲ�ͷ��");
			centerLayoutParams.leftMargin = leftViewWidth;

		} else if (centerLayoutParams.leftMargin <= -rightViewWidth) {
			// �Ϲ�ͷ�˲���Ҫ��ִ��AsynMove��
			currentUIState = SlidingState.SHOWRIGHT;
			isScrolling = false;
			log("�ұ�����Ϲ�ͷ��");
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
		// ��֮��Ϊtrue���Żᴫ�ݸ�onSingleTapUp,��Ȼ�¼��������´���.
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
		// ִ�л���.
		doScrolling(distanceX);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.d(TAG, "onSingleTapUp=" + currentOnTouchView.getClass().getSimpleName());
		// ��������ƽʱ�ؼ��İ����¼�
		if (currentOnTouchView.isClickable()) {
			currentOnTouchView.performClick();
		}
		onclickViewById(currentOnTouchView.getId(), e);
		return false;
	}

	/**
	 * �����ĵ���¼���������
	 */
	private void onclickViewById(int id, MotionEvent e) {

		if (centerView.getId() == id) {
			log("�����centerView");
			showViewState(SlidingState.SHOWCENTER);
		} else {
			log("���������" + currentOnTouchView.getClass().getSimpleName());
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		currentOnTouchView = v;// ��¼����Ŀؼ�

		// �ɿ���ʱ��Ҫ�жϣ������������Ļλ��������ȥ��
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
			//  ����time
			int moveX = 0;
			RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();

			int currentCenterMarginLeft = centerLayoutParams.leftMargin;
			switch (currentUIState) {
			// ���������
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
			log("�����Ĵ�����" + times + "|�ƶ��ľ�����moveX = " + moveX + "|currentCenterMarginLeft=" + currentCenterMarginLeft);
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

			// ���ƶ�
			// ����ֻ������center��leftMargin�������ĸ��ݸ�ֵ����
			switch (currentUIState) {
			case SHOWLEFT:
				// �������
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
