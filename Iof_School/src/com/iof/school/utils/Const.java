package com.iof.school.utils;

import android.os.Environment;

public class Const
{
	public static final String URL = "http://192.168.1.220/opetv-kudzu";
	public static final String URL_ROLES = URL + "/api/householder/load.do";
	public static final String URL_CHILD = URL + "/api/householder/editChild.do";
	public static final String URL_DEL_CHILD = URL + "/api/householder/deleteChild.do";
	public static final String URL_CATEGORY = URL + "/api/category/list.do";
	public static final String URL_RES_LIST = URL + "/api/resource/list.do";
	public static final String URL_RES_INFO = URL + "/api/resource/info.do";
	public static final String URL_LOGIN_PARENT = URL + "/api/householder/login.do";
	public static final String URL_LOGIN_CHILD = URL + "/api/child/login.do";
	public static final String URL_SET_TIME_LIMIT = URL + "/api/householder/authorizeChildTime.do";
	public static final String URL_PARENT_PSW = URL + "/api/householder/modifyPassword.do";
	
	public static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/Iof_School/";
	public static final String PATH_LOGO = PATH + "icon/";
	public static final String PATH_RECORD = PATH + "record/time.dat";
	
	/**
	 * 月份12、1-12
	 */
	public static final int[] DAY_IN_MONTH = {31, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	//消息ID
	public static final int HD_ID_LOGO_DOWN = 200;
	public static final int HD_ID_BUY_OVER = 400;
	public static final int HD_ID_TIME_OVER = 10001;
}
