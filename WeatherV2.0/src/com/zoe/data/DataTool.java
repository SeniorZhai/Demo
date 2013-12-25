package com.zoe.data;

import com.google.gson.Gson;

public class DataTool {
	public static MyData_3 formJson_3(String str) {
		Gson gson = new Gson();
		MyData_3 model = gson.fromJson(str, MyData_3.class);
		return model;
	}
	public static MyData_2 formJson_2(String str) {
		Gson gson = new Gson();
		MyData_2 model = gson.fromJson(str, MyData_2.class);
		return model;
	}
	public static MyData_1 formJson_1(String str) {
		Gson gson = new Gson();
		MyData_1 model = gson.fromJson(str, MyData_1.class);
		return model;
	}
}
