package com.example.ott_demo6.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.ott_demo6.ImageAdapter;
import com.example.ott_demo6.R;
import com.example.ott_demo6.data.GetDataTask;
import com.example.ott_demo6.data.MainData;

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
				Toast.makeText(getActivity(), data.dataList.get(arg2).getTitle(),
						Toast.LENGTH_SHORT).show();		
			}		
		});
		return view;
	}
}
