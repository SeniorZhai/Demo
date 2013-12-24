package com.zoe.weather;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyDapter extends BaseAdapter {
	private ArrayList<String> data;
	private Handler handler;
	private Context context;

	public MyDapter(Context context, List<String> data,
			Handler handler) {
		this.context = context;
		this.handler = handler;
		this.data = (ArrayList<String>) data;
	}

	class ViewHolder {
		TextView textView;
		ImageButton button;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		final ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_2, null);
			holder.textView = (TextView) convertView
					.findViewById(R.id.tv_item_2);
			holder.button = (ImageButton) convertView.findViewById(R.id.bn_item_2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView.setText(data.get(position));
		holder.textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.what = 0x998;
				message.arg1 = position;
				handler.sendMessage(message);		
			}
		});
		holder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.what = 0x999;
				message.arg1 = position;
				handler.sendMessage(message);
			}
		});
		return convertView;
	}

	@Override
	public int getCount() {
		return null != data ? data.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return null != data ? data.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return null != data ? position : 0;
	}
}
