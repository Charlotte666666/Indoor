package com.indoor.im.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.indoor.im.CustomApplcation;
import com.indoor.im.R;
import com.indoor.im.adapter.FriendsStatusAdapter;
import com.indoor.im.bean.Blog;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

@SuppressLint("SimpleDateFormat") public class FriendsStatusActivity extends ActivityBase implements OnRefreshListener2<ListView>{

	private ImageView gotomystatus;
	private PullToRefreshListView mExpandList;
    private LinkedList<Blog> mListItems;
    private FriendsStatusAdapter mAdapter;  
    private int blognum=0;
    private ArrayList<String> friendsnames;
    private int flag=0;//�����ж�������ˢ�»����������� ,PullDown:1;PullUp:2
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_status);
		initTopBarForBoth("����Ȧ", R.drawable.base_action_bar_add_bg_selector,new onRightImageButtonClickListener() {
			
			@Override
			public void onClick() {
				startActivity(new Intent(FriendsStatusActivity.this, PublishStatusActivity.class));
			}
		} );
		gotomystatus=(ImageView) findViewById(R.id.gotomystatus);
		gotomystatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(FriendsStatusActivity.this, MyStatusActivity.class));
			}
		});
		
		//һ��Ҫͨ�����ַ�ʽ��ȡ�����б�
		friendsnames=new ArrayList<String>();
		Map<String,BmobChatUser> users = CustomApplcation.getInstance().getContactList();
		List<BmobChatUser> friends=CollectionUtils.map2list(users);
		for(int i=0;i<friends.size();i++){
			friendsnames.add(friends.get(i).getObjectId());
		}
		
		mExpandList = (PullToRefreshListView)findViewById(R.id.expand_list); 
		mExpandList.setMode(Mode.BOTH);  
		mExpandList.setOnRefreshListener(this);
		mListItems=new LinkedList<Blog>();
		mAdapter = new FriendsStatusAdapter(this, mListItems);
        mExpandList.setAdapter(mAdapter); 
        mExpandList.getLoadingLayoutProxy(false, true).setPullLabel("���ظ���...");  
		mExpandList.getLoadingLayoutProxy(false, true).setRefreshingLabel("��������..."); 
	    mExpandList.getLoadingLayoutProxy(false, true).setReleaseLabel("�ſ���ˢ��...");    
	    mExpandList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				
			}
		});
	    if(friendsnames.size()==0){
	    	 showTag("�㻹û���κκ��ѣ���ȥ��Ӻ��Ѱɣ�", Effects.standard, R.id.friends_status);
	    }
	    else{
	    	 updateList();
	    }
	}
	
	//���غ����ǵĶ�̬
	private void updateList() {
		BmobQuery<Blog> query = new BmobQuery<Blog>();
		query.addWhereContainedIn("author", friendsnames);    // ��ѯ���к��ѵ�blog
		
		if(flag==1){
			String dateString= mListItems.get(0).getCreatedAt();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			Date date=null;
		    try {
				date=sdf.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    query.order("createdAt");
		    //��ѯ�������ݣ���date֮�������
			query.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(date));
			//��Ϊ�ܻ����ͷ������Ҳ�������������������
			query.setSkip(1);
		}
		else{
			query.order("-createdAt");
			query.setLimit(20);
			query.setSkip(mListItems.size());
		}	
		query.findObjects(this, new FindListener<Blog>() {
		    @Override
		    public void onSuccess(List<Blog> object) {
		    	if(blognum==0 && (object.size()==0 || object==null)){
		    		 showTag("��ĺ����ǻ�û�з����κζ�̬�����������Ƿ�����̬�ɣ�", Effects.standard, R.id.friends_status);
		    	}
		    	else if(blognum!=0 && (object.size()==0 || object==null)){
		    		showTag("������к��Ѷ�̬��ȫ�������꣡", Effects.standard, R.id.friends_status);
		    	}
		    	else{
		    		if(flag==1){
		    			for(int i=0;i<object.size();i++){
		    				mListItems.addFirst(object.get(i));
				    	} 
		    		}
		    		else{
		    			for(int i=0;i<object.size();i++){
				    		mListItems.addLast(object.get(i));
				    	} 
		    		}
		    		blognum=mListItems.size();
			    	//֪ͨ�������ݼ��Ѿ��ı䣬�������֪ͨ����ô������ˢ��mListItems�ļ���  
		            mAdapter.notifyDataSetChanged();  
		    	}
	            // Call onRefreshComplete when the list has been refreshed.  
	            mExpandList.onRefreshComplete();  
		    }

		    @Override
		    public void onError(int code, String msg) {
		       showTag("��ȷ�������Ƿ����ӣ�", Effects.standard, R.id.friends_status);
		       mExpandList.onRefreshComplete();  
		    }
		});
	}
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		 String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),  
	                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
	     refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
	     flag=1;
         new GetDataTask().execute();  
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		 String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),  
	                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
         refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
         flag=2;
         new GetDataTask().execute(); 
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String> {  
		  
	        //��̨������  
	        @Override  
	        protected String doInBackground(Void... params) {  
	            try {  
	                Thread.sleep(1000); 
	                updateList();
	            } catch (InterruptedException e) {  
	            }  
	            String str="Added after refresh...I add";   
	            return str;  
	        }  
	  
	        //�����Ƕ�ˢ�µ���Ӧ����������addFirst������addLast()�������¼ӵ����ݼӵ�LISTView��  
	        //����AsyncTask��ԭ��onPostExecute���result��ֵ����doInBackground()�ķ���ֵ  
	        @Override  
	        protected void onPostExecute(String result) {  
	        	 
	            super.onPostExecute(result);  
	        }   
	}
}
