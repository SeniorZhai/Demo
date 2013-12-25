package com.pest.box.view;

public abstract interface ShadowCallBack {
	public abstract void destroy();

	public abstract void initListener();

	public abstract void initView();

	public abstract void updateData();
}