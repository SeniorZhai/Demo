package com.zoe.ott_videoview.data;

import java.util.ArrayList;

public class MovieData {
	public String cur_page, max_page;
	public ArrayList<SingleMovieData> dataList;

	public MovieData() {
		dataList = new ArrayList<SingleMovieData>();
	}
}
