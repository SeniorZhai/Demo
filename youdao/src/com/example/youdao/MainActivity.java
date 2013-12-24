package com.example.youdao;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button myButton01,myButton02;
	private EditText myEditText01;
	private WebView webView01;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myButton01 = (Button)findViewById(R.id.myButton01);
	    myButton02 = (Button) findViewById(R.id.myButton02);
	    myEditText01 = (EditText) findViewById(R.id.myEditText1);
	    webView01 = (WebView) findViewById(R.id.myWebView1);
	  //查询按钮添加事件
	    myButton01.setOnClickListener(new Button.OnClickListener()
	    {
	      public void onClick(View arg0)
	        {
	          String strURI = (myEditText01.getText().toString());
	          strURI = strURI.trim();
	          //如果查询内容为空提示
	          if (strURI.length() == 0)
	          {
	            Toast.makeText(MainActivity.this, "查询内容不能为空!", Toast.LENGTH_LONG)
	                .show();
	          }
	          //否则则以参数的形式从http://dict.youdao.com/m取得数据，加载到WebView里.
	          else
	          {
	            String strURL = "http://dict.youdao.com/m/search?keyfrom=dict.mindex&q="
	                + strURI;
	            webView01.loadUrl(strURL);
	          }
	        }
	    });
	 
	    //清空按钮添加事件，将EditText置空
	    myButton02.setOnClickListener(new Button.OnClickListener()
	    {
	      public void onClick(View v)
	      {
	        myEditText01.setText("");
	      }
	    });
	  }

	    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
