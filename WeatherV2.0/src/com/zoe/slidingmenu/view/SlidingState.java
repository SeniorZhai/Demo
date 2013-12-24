package com.zoe.slidingmenu.view;

public enum SlidingState {
	SHOWLEFT("显示左边"),//显示左边 
	SHOWCENTER("显示中间"),//显示中间
	SHOWRIGHT("显示右边");//显示右边
	
	private final String desc;
	private SlidingState(String desc) {
		this.desc = desc;
	}
	
	public String getDesc(){
		return desc;
	}
}
