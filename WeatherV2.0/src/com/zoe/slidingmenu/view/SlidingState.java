package com.zoe.slidingmenu.view;

public enum SlidingState {
	SHOWLEFT("��ʾ���"),//��ʾ��� 
	SHOWCENTER("��ʾ�м�"),//��ʾ�м�
	SHOWRIGHT("��ʾ�ұ�");//��ʾ�ұ�
	
	private final String desc;
	private SlidingState(String desc) {
		this.desc = desc;
	}
	
	public String getDesc(){
		return desc;
	}
}
