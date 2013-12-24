package com.zoe.unit5_broadcastintents;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends Activity {
	
	private IntentFilter filter = new IntentFilter(MyBroadcast.NEW_LIFEFORM);
	
	private MyBroadcast receiver = new MyBroadcast();
	
	@Override
	protected void onResume() {
			super.onResume();
			registerReceiver(receiver, filter);
	}
	@Override
	protected void onPause() {
		unregisterReceiver(receiver);
		super.onPause();
	}
	 private void detectedLifeform(String detectedLifeform, double currentLongitude, double currentLatitude) {
		    Intent intent = new Intent(MyBroadcast.NEW_LIFEFORM);
		    intent.putExtra(MyBroadcast.EXTRA_NAME,
		                    detectedLifeform);
		    intent.putExtra(MyBroadcast.EXTRA_LONGITUDE,
		                    currentLongitude);
		    intent.putExtra(MyBroadcast.EXTRA_LATITUDE,
		                    currentLatitude);

		    sendBroadcast(intent);
		  }
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_main);
	  }
}
