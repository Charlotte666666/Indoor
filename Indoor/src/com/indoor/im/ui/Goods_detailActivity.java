package com.indoor.im.ui;

import com.indoor.im.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


@SuppressLint({ "HandlerLeak", "SetJavaScriptEnabled" }) public class Goods_detailActivity extends ActivityBase {
	
	WebView edit_describe;
	String describe = "";      // ��Ʒ����
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_goods_detail);
	    edit_describe = (WebView) findViewById(R.id.edit_describe);
	    initTopBarForLeft("��Ʒ����");
	    initData();
	}	
	
	public void initData() {
		describe = getIntent().getStringExtra("details");
		//���ظ��ı�
		edit_describe.loadDataWithBaseURL("", describe, "text/html", "UTF-8",""); 
		edit_describe.setHorizontalScrollBarEnabled(false);//ˮƽ����ʾ������
		edit_describe.setVerticalScrollBarEnabled(false); //��ֱ����ʾ������
		//ʹ��JavaScript
		edit_describe.getSettings().setJavaScriptEnabled(true);
		//ʹ��html�е����ӵ��������ϵͳ�����������������������ת�������������
		edit_describe.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                    return false;        
            }
        });
	} 
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		startAnimActivity(MainActivity.class);
	}
}
