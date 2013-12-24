package com.example.demo_layoutinflater;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = (Button) findViewById(R.id.bn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
	}
	private void showDialog()
	{
		AlertDialog.Builder builder;  
        AlertDialog alertDialog; 
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.cunstom_dialog, null);
		TextView textView = (TextView)layout.findViewById(R.id.text);
		textView.setText("Hello,Welcome to My Z's text");
		builder = new AlertDialog.Builder(this);  
        builder.setView(layout);  
        alertDialog = builder.create();  
        alertDialog.show();  
	}
}
