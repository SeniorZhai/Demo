package com.pest.box;

import com.pest.box.view.LancherLayout;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 仿小米盒子UI
 * 
 * @author 李小斌 364643658@qq.com
 * */
public class MainActivity extends Activity {

	private LinearLayout layout_item;

	private LancherLayout lancherView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lancher);

		addView();
	}

	private void addView() {
		layout_item = (LinearLayout) findViewById(R.id.layout_item);
		lancherView = new LancherLayout(this);
		lancherView.initListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					// MyApp.playSound("top_float"); 放了一个音效
					Toast.makeText(MainActivity.this, v.getId()+"",	 Toast.LENGTH_SHORT).show();
				}
			}
		});

		lancherView.initListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Activity跳转动画
				overridePendingTransition(R.anim.zoout, R.anim.zoin);
				
				// MyApp.playSound("comfire"); 放了一个音效
			}
		});
		layout_item.addView(lancherView);
	}

}
