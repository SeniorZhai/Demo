package com.zoe.unit5_intents;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button bn1, bn2, bn3, bn4;
	private static final int SHOW_SUBACTIVTY = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		bn1 = (Button) findViewById(R.id.bn1);
		bn2 = (Button) findViewById(R.id.bn2);
		bn3 = (Button) findViewById(R.id.bn3);
		bn4 = (Button) findViewById(R.id.bn4);

		bn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						MyOtherActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
		bn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri
						.parse("tel:555-2368"));
				MainActivity.this.startActivity(intent);
			}
		});
		bn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri
						.parse("tel:555-2368"));
				PackageManager pm = getPackageManager();
				ComponentName cn = intent.resolveActivity(pm);
				if (cn == null) {
					Uri marketUri = Uri
							.parse("market://search?q=pname:com.myapp.packagename");
					Intent marketIntent = new Intent(Intent.ACTION_VIEW)
							.setData(marketUri);
					if (marketIntent.resolveActivity(pm) != null) {
						startActivity(marketIntent);
					} else
						Log.d("TAG", "Market client not available");
				} else
					startActivity(intent);
			}
		});
		bn4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,MyOtherActivity.class);
				startActivityForResult(intent,SHOW_SUBACTIVTY);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SHOW_SUBACTIVTY:
			if(resultCode == Activity.RESULT_CANCELED)
				Log.d("TAG","∑µªÿ¡À£°£°£°");
			break;

		default:
			break;
		}
	}

}
