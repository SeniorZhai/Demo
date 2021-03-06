package com.example.selectadapter;

import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gridView = (GridView) findViewById(R.id.select_view);
		gridView.setAdapter(new Adapter(8, this));

	}

	class Adapter extends BaseAdapter {
		private int size;
		private LayoutInflater inflater;

		public Adapter(int size, Context context) {
			this.size = size;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return size;
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
			LinearLayout gridView = null;
			if (convertView == null) {
				gridView = (LinearLayout) inflater.inflate(R.layout.gird_item,
						null);
			} else {
				gridView = (LinearLayout) convertView;
			}

			TextView textView = (TextView) gridView.findViewById(R.id.text);
			textView.setText(position + 1+"");
			return gridView;
		}

	}

}
