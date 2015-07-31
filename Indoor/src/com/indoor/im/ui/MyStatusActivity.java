package com.indoor.im.ui;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.indoor.im.R;
import com.indoor.im.adapter.MyStatusAdapter;
import com.indoor.im.bean.Blog;
import com.indoor.im.bean.User;
import com.indoor.im.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyStatusActivity extends ActivityBase implements OnRefreshListener2<ListView>{
	
	private PullToRefreshListView mExpandList;
    private List<Blog> mListItems;
    private MyStatusAdapter mAdapter;  
    private int blognum=0;
    private TextView name;
    private ImageView head;
    private ImageView background;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_status);
		initTopBarForLeft("�ҵĿռ�");
		mExpandList = (PullToRefreshListView)findViewById(R.id.expand_list); 
		name=(TextView) findViewById(R.id.name);
		head=(ImageView) findViewById(R.id.headimage);
		background=(ImageView) findViewById(R.id.background);
		User user = BmobUser.getCurrentUser(this, User.class);
		name.setText(user.getUsername());
		if(user.getAvatar()!=null && !user.getAvatar().equals("")){
			ImageLoader.getInstance().displayImage(user.getAvatar(), head,
					ImageLoadOptions.getOptions());
		}
		if(user.getBackgroundUri()!=null && !user.getBackgroundUri().equals("")){
			ImageLoader.getInstance().displayImage(user.getBackgroundUri(), background,
					ImageLoadOptions.getOptions());
		}
		mExpandList.setMode(Mode.PULL_UP_TO_REFRESH);  
		mExpandList.setOnRefreshListener(this);
		mListItems=new ArrayList<Blog>();
		mAdapter = new MyStatusAdapter(this, mListItems);
        mExpandList.setAdapter(mAdapter); 
        mExpandList.getLoadingLayoutProxy(false, true).setPullLabel("���ظ���...");  
		mExpandList.getLoadingLayoutProxy(false, true).setRefreshingLabel("��������..."); 
	    mExpandList.getLoadingLayoutProxy(false, true).setReleaseLabel("�ſ���ˢ��..."); 
	    mExpandList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				final NiftyDialogBuilder dialogBuilder=new NiftyDialogBuilder(MyStatusActivity.this,R.style.dialog_untran);
		        dialogBuilder
		                .withTitle("��ʾ")                                 
		                .withTitleColor("#FFFFFF")                              
		                .withDividerColor("#11000000")                             
		                .withMessage("ɾ��������̬��")                        
		                .withMessageColor("#FFFFFF")                               
		                .withIcon(getResources().getDrawable(R.drawable.icon))
		                .isCancelableOnTouchOutside(true)                       
		                .withDuration(700)                                     
		                .withEffect(Effectstype.Fliph)                        
		                .withButton1Text("ȷ��")                            
		                .withButton2Text("ȡ��")                              
		                .setButton1Click(new View.OnClickListener() {
		                    @Override
		                    public void onClick(View v) {
		                    	String blogId=mListItems.get(arg2-1).getObjectId();
		                    	mListItems.remove(arg2-1);
		                    	blognum--;
		    		            mAdapter.notifyDataSetChanged();  
		    	                mExpandList.onRefreshComplete(); 
		    	                Blog p = new Blog();
		    	                p.remove("author");
		    	                p.update(MyStatusActivity.this, blogId, new UpdateListener() {

		    	                    @Override
		    	                    public void onSuccess() {
		    	                       
		    	                    }

		    	                    @Override
		    	                    public void onFailure(int code, String msg) {
		    	                       
		    	                    }
		    	                });
		        				dialogBuilder.dismiss();
		                    }
		                })
		                .setButton2Click(new View.OnClickListener() {
		                    @Override
		                    public void onClick(View v) {
		                    	dialogBuilder.dismiss();
		                    }
		                })
		                .show();
			}
		});
	    updateList();
	}
	
	//�����ҵĶ�̬
	private void updateList() {
		User user = BmobUser.getCurrentUser(this, User.class);
		BmobQuery<Blog> query = new BmobQuery<Blog>();
		query.addWhereEqualTo("author", user);    // ��ѯ��ǰ�û�������blog
		query.order("-updatedAt");
		query.setLimit(20);
		query.setSkip(mListItems.size());
		query.findObjects(this, new FindListener<Blog>() {
		    @Override
		    public void onSuccess(List<Blog> object) {
		    	if(blognum==0 && (object.size()==0 || object==null)){
		    		 showTag("�㻹û�з����κζ�̬����ȥ������̬�ɣ�", Effects.standard, R.id.mystatustoast);
		    	}
		    	else if(blognum!=0 && (object.size()==0 || object==null)){
		    		showTag("������ж�̬��ȫ�������꣡", Effects.standard, R.id.mystatustoast);
		    	}
		    	else{
		    		for(int i=0;i<object.size();i++){
			    		mListItems.add(object.get(i));
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
		       showTag("��ȷ�������Ƿ����ӣ�", Effects.standard, R.id.mystatustoast);
		    }
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) { 
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		 String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),  
	                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
	        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
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
