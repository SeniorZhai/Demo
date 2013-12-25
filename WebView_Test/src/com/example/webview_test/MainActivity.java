package com.example.webview_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends Activity {
	WebView wv;
	ProgressDialog pd;
	Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        init();//ִ�г�ʼ������
        loadurl(wv,"file:///android_asset/index.html");
//        loadurl(wv,"http://love-calculator.zeemind.com/");
        handler=new Handler(){
        	public void handleMessage(Message msg)
    	    {//����һ��Handler�����ڴ��������߳���UI��ͨѶ
    	      if (!Thread.currentThread().isInterrupted())
    	      {
    	        switch (msg.what)
    	        {
    	        case 0:
    	        	pd.show();//��ʾ���ȶԻ���        	
    	        	break;
    	        case 1:
    	        	pd.hide();//���ؽ��ȶԻ��򣬲���ʹ��dismiss()��cancel(),�����ٴε���show()ʱ����ʾ�ĶԻ���СԲȦ���ᶯ��
    	        	break;
    	        }
    	      }
    	      super.handleMessage(msg);
    	    }
        };
    }
    public void init(){//��ʼ��
    	wv=(WebView)findViewById(R.id.wv);
    	wv.getSettings().setUserAgentString("MicroMessager");
    	//wv.setInitialScale(110); 
        wv.getSettings().setJavaScriptEnabled(true);//����JS
        wv.setScrollBarStyle(0);//���������Ϊ0���ǲ������������ռ䣬��������������ҳ��
        wv.setWebViewClient(new WebViewClient(){   
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            	loadurl(view,url);//������ҳ
                return true;   
            }//��д�������,��webview����
 
        });
        wv.setWebChromeClient(new WebChromeClient(){
        	public void onProgressChanged(WebView view,int progress){//������ȸı������ 
             	if(progress==100){
            		handler.sendEmptyMessage(1);//���ȫ������,���ؽ��ȶԻ���
            	}   
                super.onProgressChanged(view, progress);   
            }   
        });
 
    	pd=new ProgressDialog(MainActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("���������У����Ժ�");
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {//��׽���ؼ�
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {   
            //wv.goBack(); 
        	ConfirmExit();
            return true;   
        }else if(keyCode == KeyEvent.KEYCODE_BACK){
        	ConfirmExit();//���˷��ؼ������Ѿ����ܷ��أ���ִ���˳�ȷ��
        	return true; 
        }   
        return super.onKeyDown(keyCode, event);   
    }
    public void ConfirmExit(){//�˳�ȷ��
    	AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
    	ad.setTitle("�˳�");
    	ad.setMessage("�Ƿ��˳����?");
    	ad.setPositiveButton("��", new DialogInterface.OnClickListener() {//�˳���ť
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();//�ر�activity
			}
		});
    	ad.setNegativeButton("��",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
				//���˳�����ִ���κβ���
			}
		});
    	ad.show();//��ʾ�Ի���
    }
    public void loadurl(final WebView view,final String url){
    	new Thread(){
        	public void run(){
        		handler.sendEmptyMessage(0);
        		view.loadUrl(url);//������ҳ
        	}
        }.start();
    }
}

