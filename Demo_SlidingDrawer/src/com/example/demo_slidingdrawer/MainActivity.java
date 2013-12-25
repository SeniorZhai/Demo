package com.example.demo_slidingdrawer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	private SlidingDrawer drawer;
	private Button bn;
	@SuppressWarnings("unused")
	private boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bn = (Button) findViewById(R.id.handle);
		drawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
		drawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				flag = true;
				bn.setBackgroundColor(Color.RED);
			}
		});
		drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				flag = false;
				bn.setBackgroundColor(Color.GREEN);
			}
		});
		drawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {

			@Override
			public void onScrollEnded() {
				Log.d("TAG", "结束拖动");
			}

			@Override
			public void onScrollStarted() {
				Log.d("TAG", "开始拖动");
			}

		});
	}

}
