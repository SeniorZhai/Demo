package com.zoe.unit5_broadcastintents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

public class MyBroadcast extends BroadcastReceiver {
	public final static String EXTRA_NAME = "EXTRA_NAME";
	public final static String EXTRA_LATITUDE = "EXTRA_LATITUDE";
	public final static String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";

	public static final String ACTION_BURN = "com.zoe.action.BURN_IT_WITH_FIRE";
	public static final String NEW_LIFEFORM = "com.zoe.action.NEW_LIFEFORM";

	@Override
	public void onReceive(Context context, Intent intent) {
		Uri data = intent.getData();
		String type = intent.getStringExtra(EXTRA_NAME);
		double lat = intent.getDoubleExtra(EXTRA_LATITUDE, 0);
		double lng = intent.getDoubleExtra(EXTRA_LONGITUDE, 0);
		Location loc = new Location("gps");
		loc.setLatitude(lat);
		loc.setLongitude(lng);

		if (type.equals("facehugger")) {
			Intent startIntent = new Intent(ACTION_BURN, data);
			startIntent.putExtra(EXTRA_LATITUDE, lat);
			startIntent.putExtra(EXTRA_LONGITUDE, lng);

			context.startService(startIntent);
		}
	}

}
