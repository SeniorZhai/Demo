package com.zoe.jnitest;

public class Nadd {
	static {
		System.loadLibrary("hello_jni");
	}
	public native String nadd();
}
