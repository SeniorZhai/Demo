package com.example.ott_demo6.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ott_demo6.R;
import com.example.ott_demo6.data.GetDataTask;
import com.example.ott_demo6.data.MainData;

public class Fragament_lift extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragament_lift, container, false);

		return view;
	}
}
