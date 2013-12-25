package com.example.demo_location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LocationDemo extends Activity {

	private TextView longitude;
	private TextView latitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		longitude = (TextView) findViewById(R.id.longitude);
		latitude = (TextView) findViewById(R.id.latitude);

		Location mLocation = getLocation(this);
		Log.d("GPS", ""+mLocation.getLongitude());
		longitude.setText("Longitude: " + mLocation.getLongitude());
		latitude.setText("Latitude: " + mLocation.getLatitude());
	}

	// Get the Location by GPS or WIFI
	public Location getLocation(Context context) {
		LocationManager locMan = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Location location = locMan
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locMan
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
	}
}