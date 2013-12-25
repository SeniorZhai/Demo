package com.example.ott_demo.layout;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.ott_demo.ImageAdapter;
import com.example.ott_demo.R;
import com.example.ott_demo.VideoActivity;
import com.example.ott_demo.data.GetDataTask;
import com.example.ott_demo.data.MainData;

public class Fragament_right extends Fragment{
	private GridView gridView;
	private MainData data;
	private ImageAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragament_right, container, false);
		data = new MainData();
		gridView = (GridView) view.findViewById(R.id.gridview);
		new GetDataTask(data).execute();
		adapter = new ImageAdapter(data, getActivity());
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),VideoActivity.class);
				intent.putExtra("url", data.dataList.get(arg2).getTitle());
				startActivity(intent);
			}		
		});
		return view;
	}
}
