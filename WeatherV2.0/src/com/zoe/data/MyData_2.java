package com.zoe.data;

public class MyData_2 {
	public MyData_2() {
		weatherinfo = new Weatherinfo();
	}

	public Weatherinfo weatherinfo;

	public class Weatherinfo {
		public String city = " ";
		public String temp1 = " ";
		public String temp2 = " ";
		public String weather = " ";
		public String ptime = " ";
	}
}
