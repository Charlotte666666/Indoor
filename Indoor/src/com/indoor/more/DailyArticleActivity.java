package com.indoor.more;

import java.util.List;
import java.util.Random;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.bean.DailyArticle;
import com.indoor.im.ui.BaseActivity;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;
import com.indoor.more.util.GestureListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class DailyArticleActivity extends BaseActivity {
	
	private WebView article;
	private CircleProgressBar progress;
	private String describe = "";   //����
	private int skipnum=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_article);
		initTopBarForBoth("ÿ��һ��", R.drawable.base_action_bar_more_bg_n, new onRightImageButtonClickListener() {
			
			@Override
			public void onClick() {
				showTag("���һƪ", Effects.flip, R.id.essay);
				getCount();
			}
		});
		article = (WebView) findViewById(R.id.edit_describe);
		progress=(CircleProgressBar) findViewById(R.id.progress);
		 //setLongClickable�Ǳ����  
        article.setLongClickable(true);  
        article.setOnTouchListener(new MyGestureListener(this));  
		initData();
	}
	
	public void initData() {
		BmobQuery<DailyArticle> query = new BmobQuery<DailyArticle>();
		if(skipnum<0){
		   showTag("��������һƪ��", Effects.flip, R.id.essay);
		   skipnum=0;
		}
		else{
			progress.setVisibility(View.VISIBLE);
			query.setSkip(skipnum); 
			query.order("-createdAt");
			//ִ�в�ѯ����
			query.findObjects(this, new FindListener<DailyArticle>() {
			        @Override
			        public void onSuccess(List<DailyArticle> essay) {
			        	if(essay.size()>0){
			        		describe=essay.get(0).getEssay();
					         //���ظ��ı�
					   		article.loadDataWithBaseURL("", describe, "text/html", "UTF-8",""); 
					   		article.setHorizontalScrollBarEnabled(false);//ˮƽ����ʾ������
					   		progress.setVisibility(View.GONE);
			        	}
			        	else{
			        		showTag("û�и��������ˣ�", Effects.flip, R.id.essay);
			        		skipnum--;
			        		progress.setVisibility(View.GONE);
			        	}
			           
			        }
			        @Override
			        public void onError(int code, String msg) {
			           showTag("��ȷ�������Ƿ����ӣ�", Effects.flip, R.id.essay);
			           progress.setVisibility(View.GONE);
			        }
			});
		}	
	} 
	
	/** 
     * �̳�GestureListener����дleft��right���� 
     */  
    private class MyGestureListener extends GestureListener {  
        public MyGestureListener(Context context) {  
            super(context);  
        }  
  
        @Override  
        public boolean left() {  
        	//�����
        	skipnum--;
        	initData();  
            return super.left();  
        }  
  
        @Override  
        public boolean right() {  
        	//��ѯ��ʷ���£���ǰ��
        	skipnum++;
        	initData(); 
            return super.right();  
        }  
    }  
    
    private void getCount(){
		String bql = "select count(*),* from DailyArticle";//��ѯDailyArticle�����ܼ�¼�����������м�¼��Ϣ
		new BmobQuery<DailyArticle>().doSQLQuery(this,bql, new SQLQueryListener<DailyArticle>(){
			
			@Override
			public void done(BmobQueryResult<DailyArticle> result, BmobException e) {
			   if(e==null){
		            Random random = new Random();
		    		//��Ϊ��Ŵ�1��ʼ��������Ҫ+1
		            skipnum=random.nextInt(result.getCount())+1;
		        	initData();
		         }else{
		        	  showTag("���������ȷ�������Ƿ���ͨ��", Effects.flip, R.id.essay);
		        }
			}
		});
	}
}
