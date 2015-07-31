package com.indoor.im.view;


import com.indoor.im.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("InflateParams") public class Mark extends LinearLayout{
	
	private View MarkView;//MarkView�Ĳ���
	 
	public Mark(Context context) {
		super(context);
		initView(context);
	}

	public Mark(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	@SuppressLint("NewApi") public Mark(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	//��ʼ�����棬��Ӳ����ļ���Mark��
	private void initView(Context context){
		LayoutInflater inflater=LayoutInflater.from(context);
		MarkView=inflater.inflate(R.layout.mark_view,null);
		this.addView(MarkView);			
	}
	
	public void setUsername(String name) {
		TextView textView=(TextView) findViewById(R.id.username);
		textView.setText(name);
	}
}
