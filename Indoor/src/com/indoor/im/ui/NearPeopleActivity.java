package com.indoor.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.im.task.BRequest;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.adapter.NearPeopleAdapter;
import com.indoor.im.bean.User;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;
import com.indoor.im.view.xlist.XListView;
import com.indoor.im.view.xlist.XListView.IXListViewListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

/**
 * ���������б�
 * 
 * @ClassName: NewFriendActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-6 ����4:28:09
 */
public class NearPeopleActivity extends ActivityBase implements IXListViewListener,OnItemClickListener {

	XListView mListView;
	NearPeopleAdapter adapter;
	String from = "";
	CircleProgressBar progress;
	private CharSequence[] items = {"ȫ��","ֻ����", "ֻ��Ů","ȫ�������������ѣ�","ֻ���У����������ѣ�", "ֻ��Ů�����������ѣ�" }; 
	private int itemnum=0;//�����ж���������
	boolean isshowfriend=true;
	String Property=null;
	Object isman=null;
	List<User> nears = new ArrayList<User>();

	private double QUERY_KILOMETERS = 10;//Ĭ�ϲ�ѯ10���ﷶΧ�ڵ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_people);
		initView();
	}

	private void initView() {
		initTopBarForBoth("��������", R.drawable.base_action_bar_more_bg_n, new onRightImageButtonClickListener() {
			
			@Override
			public void onClick() {
				AlertDialog.Builder builder = new AlertDialog.Builder(NearPeopleActivity.this);
				builder.setTitle("����ɸѡ����");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	itemnum=item;
				    	if(itemnum==0){
							isshowfriend=true;
							Property=null;
							isman=null;
						}
						else if(itemnum==1){
							isshowfriend=true;
							Property="sex";
							isman=true;
						}
						else if(itemnum==2){
							isshowfriend=true;
							Property="sex";
							isman=false;
						}
						else if(itemnum==3){
							isshowfriend=false;
							Property=null;
							isman=null;
						}
						else if(itemnum==4){
							isshowfriend=false;
							Property="sex";
							isman=true;
						}
						else if(itemnum==5){
							isshowfriend=false;
							Property="sex";
							isman=false;
						}
				    	nears.clear();
				    	initNearByList(false);
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		initXListView();
	}

	private void initXListView() {
		mListView = (XListView) findViewById(R.id.list_near);
		progress=(CircleProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		mListView.setOnItemClickListener(this);
		// ���Ȳ�������ظ���
		mListView.setPullLoadEnable(false);
		// ��������
		mListView.setPullRefreshEnable(true);
		// ���ü�����
		mListView.setXListViewListener(this);
		//
		mListView.pullRefreshing();
		
		adapter = new NearPeopleAdapter(this, nears);
		mListView.setAdapter(adapter);
		initNearByList(false);
	}

	
	int curPage = 0;
	private void initNearByList(final boolean isUpdate){
		if(!isUpdate){
			progress.setVisibility(View.VISIBLE);
		}
		
		if(!mApplication.getLatitude().equals("")&&!mApplication.getLongtitude().equals("")){
			double latitude = Double.parseDouble(mApplication.getLatitude());
			double longtitude = Double.parseDouble(mApplication.getLongtitude());
			//��װ�Ĳ�ѯ�������������ҳ��ʱ isUpdateΪfalse��������ˢ�µ�ʱ������Ϊtrue���С�
			//�˷���Ĭ��ÿҳ��ѯ10������,�����ѯ����10�������ڲ�ѯ֮ǰ����BRequest.QUERY_LIMIT_COUNT���磺BRequest.QUERY_LIMIT_COUNT=20
			// �˷����������Ĳ�ѯָ��10�����ڵ��û��б�Ĭ�ϰ��������б�
			userManager.queryKiloMetersListByPage(isUpdate,0,"location", longtitude, latitude, isshowfriend,QUERY_KILOMETERS,Property,isman,new FindListener<User>() {

				@Override
				public void onSuccess(List<User> arg0) {
					if (CollectionUtils.isNotNull(arg0)) {
						if(isUpdate){
							nears.clear();
						}
						adapter.addAll(arg0);
						if(arg0.size()<BRequest.QUERY_LIMIT_COUNT){
							mListView.setPullLoadEnable(false);
							showTag("���������������!",Effects.thumbSlider,R.id.nearpeople);
						}else{
							mListView.setPullLoadEnable(true);
						}
					}else{
						showTag("���޸�������!",Effects.thumbSlider,R.id.nearpeople);
					}
					
					if(!isUpdate){
						progress.setVisibility(View.GONE);
					}else{
						refreshPull();
					}
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					showTag("���޸�������!",Effects.thumbSlider,R.id.nearpeople);
					mListView.setPullLoadEnable(false);
					if(!isUpdate){
						progress.setVisibility(View.GONE);
					}else{
						refreshPull();
					}
				}

			});
		}else{
			showTag("���޸�������!",Effects.thumbSlider,R.id.nearpeople);
			progress.setVisibility(View.GONE);
			refreshPull();
		}
		
	}
	
	/** ��ѯ����
	  * @Title: queryMoreNearList
	  * @Description: TODO
	  * @param @param page 
	  * @return void
	  * @throws
	  */
	private void queryMoreNearList(int page){
		double latitude = Double.parseDouble(mApplication.getLatitude());
		double longtitude = Double.parseDouble(mApplication.getLongtitude());
		userManager.queryKiloMetersListByPage(true,page,"location", longtitude, latitude, isshowfriend,QUERY_KILOMETERS,Property,isman,new FindListener<User>() {
	
			@Override
			public void onSuccess(List<User> arg0) {
				if (CollectionUtils.isNotNull(arg0)) {
					adapter.addAll(arg0);
				}
				refreshLoad();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				ShowLog("��ѯ���฽�����˳���:"+arg1);
				mListView.setPullLoadEnable(false);
				refreshLoad();
			}

		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		User user = (User) adapter.getItem(position-1);
		Intent intent =new Intent(this,SetMyInfoActivity.class);
		intent.putExtra("from", "add");
		intent.putExtra("username", user.getUsername());
		startAnimActivity(intent);		
	}

	@Override
	public void onRefresh() {
		initNearByList(true);
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
	@Override
	public void onLoadMore() {
		double latitude = Double.parseDouble(mApplication.getLatitude());
		double longtitude = Double.parseDouble(mApplication.getLongtitude());
		userManager.queryKiloMetersTotalCount(User.class, "location", longtitude, latitude, isshowfriend,QUERY_KILOMETERS,Property,isman,new CountListener() {

			@Override
			public void onSuccess(int arg0) {
				if(arg0 >nears.size()){
					curPage++;
					queryMoreNearList(curPage);
				}else{
					showTag("���ݼ������",Effects.thumbSlider,R.id.nearpeople);
					mListView.setPullLoadEnable(false);
					refreshLoad();
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowLog("��ѯ������������ʧ��"+arg1);
				refreshLoad();
			}
		});
	}
}
