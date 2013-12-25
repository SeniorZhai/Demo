package com.zoe.unit5_linkify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final TextView myTextView = (TextView)findViewById(R.id.text_view);
		String baseUri = "content://com.paad.earthquake/earthquakes/";
		PackageManager pm = getPackageManager();
	     Intent testIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseUri));
	     boolean activityExists = testIntent.resolveActivity(pm) != null;
	    
	     if (activityExists) {
	       int flags = Pattern.CASE_INSENSITIVE;
	       Pattern p = Pattern.compile("\\bquake[\\s]?[0-9]+\\b", flags);
	       Linkify.addLinks(myTextView, p, baseUri);
	     }
	}

	class MyMatchFilter implements MatchFilter {
	    public boolean acceptMatch(CharSequence s, int start, int end) {
	      return (start == 0 || s.charAt(start-1) != '!');
	    }
	  }
	  
	  /**
	   * Listing 5-9: Using a Linkify Transform Filter
	   * 
	   */
	  class MyTransformFilter implements TransformFilter {
	    public String transformUrl(Matcher match, String url) {
	      return url.toLowerCase().replace(" ", "");
	    }
	  }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
