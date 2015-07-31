package com.indoor.im.ui;

import com.indoor.im.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class LivesActivity extends ActivityBase {
	
	private ImageView eat;
	private ImageView buy; 
	private ImageView hotle; 
	private ImageView movie;
	private ImageView sport;
	private ImageView hospital;
	private ImageView park; 
	private ImageView gongce; 
	private ImageView fun;
	private ImageView atm;
	private ImageView bank;
	private ImageView book; 
	private ImageView fengjing; 
	private ImageView zhengfu;
	private ImageView bus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lives);
		init();
		eat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","��������");  
				startActivityForResult(intent,0);
			}
		});
		buy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","�������");  
				startActivityForResult(intent,0);
			}
		});
		hotle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","ס�޷���");  
				startActivityForResult(intent,0);
			}
		});
		movie.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","Ӱ��Ժ");  
				startActivityForResult(intent,0);
			}
		});
		sport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","�˶�����");  
				startActivityForResult(intent,0);
			}
		});
		hospital.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","ҽ�Ʊ���");  
				startActivityForResult(intent,0);
			}
		});
		park.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","ͣ����");  
				startActivityForResult(intent,0);
			}
		});
		gongce.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","��������");  
				startActivityForResult(intent,0);
			}
		});
		fun.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","���ֳ���");  
				startActivityForResult(intent,0);
			}
		});
		atm.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","�Զ�����");  
				startActivityForResult(intent,0);
		    }
		});
		bank.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","����");  
				startActivityForResult(intent,0);
		    }
		});
		book.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","�ƽ��Ļ�����");  
				startActivityForResult(intent,0);
		    }
		});
		fengjing.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","�羰��ʤ");  
				startActivityForResult(intent,0);
		    }
		});
		zhengfu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","��������");  
				startActivityForResult(intent,0);
			}
		});
        bus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LivesActivity.this,NavigatorActivity.class);
				//����Ŀ���ݸ�mainactivity
				intent.putExtra("Cloum","������վ");  
				startActivityForResult(intent,0);
			}
		});
	}
	
	private void init() {
		initTopBarForLeft("�����ܱ�");
		eat=(ImageView) findViewById(R.id.eat);
		buy=(ImageView) findViewById(R.id.buy);
		hotle=(ImageView) findViewById(R.id.hotal);
		movie=(ImageView) findViewById(R.id.movie);
		sport=(ImageView) findViewById(R.id.sport);
		hospital=(ImageView) findViewById(R.id.hospita);
		park=(ImageView) findViewById(R.id.park);
		gongce=(ImageView) findViewById(R.id.gongce);
		fun=(ImageView) findViewById(R.id.fun);
		atm=(ImageView) findViewById(R.id.atm);
		bank=(ImageView) findViewById(R.id.bank);
		book=(ImageView) findViewById(R.id.book);
		fengjing=(ImageView) findViewById(R.id.fengjing);
		zhengfu=(ImageView) findViewById(R.id.zhengfu);
		bus=(ImageView) findViewById(R.id.bus);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		startAnimActivity(MainActivity.class);
	}
}
