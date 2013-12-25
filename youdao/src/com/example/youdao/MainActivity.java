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
	  //��ѯ��ť����¼�
	    myButton01.setOnClickListener(new Button.OnClickListener()
	    {
	      public void onClick(View arg0)
	        {
	          String strURI = (myEditText01.getText().toString());
	          strURI = strURI.trim();
	          //�����ѯ����Ϊ����ʾ
	          if (strURI.length() == 0)
	          {
	            Toast.makeText(MainActivity.this, "��ѯ���ݲ���Ϊ��!", Toast.LENGTH_LONG)
	                .show();
	          }
	          //�������Բ�������ʽ��http://dict.youdao.com/mȡ�����ݣ����ص�WebView��.
	          else
	          {
	            String strURL = "http://dict.youdao.com/m/search?keyfrom=dict.mindex&q="
	                + strURI;
	            webView01.loadUrl(strURL);
	          }
	        }
	    });
	 
	    //��հ�ť����¼�����EditText�ÿ�
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
