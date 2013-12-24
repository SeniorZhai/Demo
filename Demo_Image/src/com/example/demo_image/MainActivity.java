package com.example.demo_image;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView imageView01 = (ImageView) findViewById(R.id.image01);
		ImageView imageView02 = (ImageView) findViewById(R.id.image02);

		// Drawable drawable = getWallpaper();
		Resources resources = getResources();
		Drawable drawable = resources.getDrawable(R.drawable.mnbz);
		
		Bitmap bitmap = ImageUtil.drawableToBitmap(drawable);
		Bitmap bitmap01 = ImageUtil.getRoundedCornerBitmap(bitmap, 10);
		Bitmap reflectBitmap = ImageUtil
				.createReflectionImageWithOrigin(bitmap01);
		imageView02.setImageBitmap(reflectBitmap);

	}
}
