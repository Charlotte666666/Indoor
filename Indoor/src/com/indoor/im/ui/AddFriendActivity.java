package com.indoor.im.ui;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.task.BRequest;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.adapter.AddFriendAdapter;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.xlist.XListView;
import com.indoor.im.view.xlist.XListView.IXListViewListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

/** ���Ӻ���
  * @ClassName: AddFriendActivity
  * @Description: TODO
  * @author smile
  * @date 2014-6-5 ����5:26:41
  */
public class AddFriendActivity extends ActivityBase implements OnClickListener,
IXListViewListener,OnItemClickListener{
	
	EditText et_find_name;
	Button btn_search;
	
	List<BmobChatUser> users = new ArrayList<BmobChatUser>();
	XListView mListView;
	AddFriendAdapter adapter;
	CircleProgressBar progress ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		initView();
	}
	
	private void initView(){
		initTopBarForLeft("���Һ���");
		et_find_name = (EditText)findViewById(R.id.et_find_name);
		btn_search = (Button)findViewById(R.id.btn_search);
		progress=(CircleProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		btn_search.setOnClickListener(this);
		initXListView();
	}

	private void initXListView() {
		mListView = (XListView) findViewById(R.id.list_search);
		// ���Ȳ��������ظ���
		mListView.setPullLoadEnable(false);
		// ����������
		mListView.setPullRefreshEnable(false);
		// ���ü�����
		mListView.setXListViewListener(this);
		//
		mListView.pullRefreshing();
		
		adapter = new AddFriendAdapter(this, users);
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(this);
	}
	
	int curPage = 0;
	private void initSearchList(final boolean isUpdate){
		if(!isUpdate){
			progress.setVisibility(View.VISIBLE);
		}
		userManager.queryUserByPage(isUpdate, 0, searchName, new FindListener<BmobChatUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				BmobLog.i("��ѯ����:"+arg1);
				if(users!=null){
					users.clear();
				}
				showTag("�û�������",Effects.jelly,R.id.addcontact);
				mListView.setPullLoadEnable(false);
				refreshPull();
				//�����ܱ�֤ÿ�β�ѯ���Ǵ�ͷ��ʼ
				curPage = 0;
			}

			@Override
			public void onSuccess(List<BmobChatUser> arg0) {
				// TODO Auto-generated method stub
				if (CollectionUtils.isNotNull(arg0)) {
					if(isUpdate){
						users.clear();
					}
					adapter.addAll(arg0);
					if(arg0.size()<BRequest.QUERY_LIMIT_COUNT){
						mListView.setPullLoadEnable(false);
						showTag("�û��������!",Effects.jelly,R.id.addcontact);
					}else{
						mListView.setPullLoadEnable(true);
					}
				}else{
					BmobLog.i("��ѯ�ɹ�:�޷���ֵ");
					if(users!=null){
						users.clear();
					}
					showTag("�û�������",Effects.jelly,R.id.addcontact);
				}
				if(!isUpdate){
					progress.setVisibility(View.GONE);
				}else{
					refreshPull();
				}
				//�����ܱ�֤ÿ�β�ѯ���Ǵ�ͷ��ʼ
				curPage = 0;
			}
		});
		
	}
	
	/** ��ѯ����
	  * @Title: queryMoreNearList
	  * @Description: TODO
	  * @param @param page 
	  * @return void
	  * @throws
	  */
	private void queryMoreSearchList(int page){
		userManager.queryUserByPage(true, page, searchName, new FindListener<BmobChatUser>() {

			@Override
			public void onSuccess(List<BmobChatUser> arg0) {
				// TODO Auto-generated method stub
				if (CollectionUtils.isNotNull(arg0)) {
					adapter.addAll(arg0);
				}
				refreshLoad();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowLog("���������û�����:"+arg1);
				mListView.setPullLoadEnable(false);
				refreshLoad();
			}

		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		BmobChatUser user = (BmobChatUser) adapter.getItem(position-1);
		Intent intent =new Intent(this,SetMyInfoActivity.class);
		intent.putExtra("from", "add");
		intent.putExtra("username", user.getUsername());
		startAnimActivity(intent);		
	}
	
	String searchName ="";
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_search://����
			users.clear();
			searchName = et_find_name.getText().toString();
			if(searchName!=null && !searchName.equals("")){
				initSearchList(false);
			}else{
				showTag("�������û���",Effects.jelly,R.id.addcontact);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		userManager.querySearchTotalCount(searchName, new CountListener() {
			
			@Override
			public void onSuccess(int arg0) {
				// TODO Auto-generated method stub
				if(arg0 >users.size()){
					curPage++;
					queryMoreSearchList(curPage);
				}else{
					showTag("���ݼ������",Effects.jelly,R.id.addcontact);
					mListView.setPullLoadEnable(false);
					refreshLoad();
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowLog("��ѯ������������ʧ��"+arg1);
				refreshLoad();
			}
		});
	}
	
	private void refreshLoad(){
		if (mListView.getPullLoading()) {
			mListView.stopLoadMore();
		}
	}
	
	private void refreshPull(){
		if (mListView.getPullRefreshing()) {
			mListView.stopRefresh();
		}
	}
	

}