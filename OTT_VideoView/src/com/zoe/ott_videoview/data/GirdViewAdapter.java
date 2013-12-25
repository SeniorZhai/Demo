package com.zoe.ott_videoview.data;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoe.ott_videoview.R;

public class GirdViewAdapter extends BaseAdapter {

	private MovieData data;
	private LayoutInflater inflater;
	//GirdView��������
	public GirdViewAdapter(MovieData data, Context context) {
		this.data = data;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout gridView = null;
		if (convertView == null) {
			gridView = (RelativeLayout) inflater.inflate(R.layout.grid_item,
					null);
		} else {
			gridView = (RelativeLayout) convertView;
		}
		ImageView imageView = (ImageView) gridView.findViewById(R.id.ItemImage);
		new LoadingTask(imageView).execute(data.dataList.get(position)
				.getPoster_url());
		TextView textView = (TextView) gridView.findViewById(R.id.ItemText);
		textView.setText(data.dataList.get(position).getTitle());
		return gridView;
	}

	class LoadingTask extends AsyncTask<String, Integer, Bitmap> {
		private ImageView imageView;

		public LoadingTask(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			try {
				URL url = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream inputStream = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
				inputStream.close();
			} catch (Exception e) {
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap result) {
			if (result == null) {
				imageView.setImageResource(R.drawable.weineng);
			} else
				imageView.setImageBitmap(result);
		}
	}
}