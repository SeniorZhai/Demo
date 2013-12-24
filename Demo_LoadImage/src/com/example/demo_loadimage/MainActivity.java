package com.example.demo_loadimage;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView imageView;
	private Button button;
	int index = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final String pragma[] = {
				"http://192.168.1.62/static/1517A3F7A4ABC96260E2641698A4D173.jpg",
				"http://192.168.1.62/static/0C1D17A3C5608CF94307BE161ACA003F.jpg",
				"http://192.168.1.62/static/C46C2BCC17A233F8BFDF9346CF50C5BB.jpg",
				"http://192.168.1.62/static/4C1DE43223AE1C6B46A91D366D077B49.jpg",
				"http://192.168.1.62/static/5F46E70D219543ECC70B7D25B4E3D760.jpg"};
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button);
		imageView = (ImageView) findViewById(R.id.imageView);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				index++;
				LoadingTask task = new LoadingTask(imageView);
				if(index>=pragma.length)index=0;
				task.execute(pragma[index]);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
