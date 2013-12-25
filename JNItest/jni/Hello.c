#include <stdio.h>
#include <jni.h>
#include "com.zoe.jnitest.Nadd.h"

JNIEXPORT jstring JNICALL Java_com_zoe_jnitest_Nadd
(JNIEnv * env ,jobject obj){
	 return (*(*env).NewStringUTF(env,"Hello JNI!"));
}
