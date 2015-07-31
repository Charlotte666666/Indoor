package com.indoor.im.ui;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.im.task.BRequest;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.indoor.im.R;
import com.indoor.im.adapter.WallPaperAdapter;
import com.indoor.im.bean.Blog;
import com.indoor.im.bean.User;
import com.indoor.im.bean.WallPapers;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.xlist.XListView;
import com.indoor.im.view.xlist.XListView.IXListViewListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SetWallpaperActivity extends ActivityBase implements IXListViewListener,OnItemClickListener{
	
	XListView mListView;
	WallPaperAdapter adapter;
	List<WallPapers> wallpapers = new ArrayList<WallPapers>();
	List<String> wallpaperUris = new ArrayList<String>();
	CircleProgressBar progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_wallpaper);
		initView();
	}

	private void initView() {
		initTopBarForLeft("��������");
		initXListView();
	}
	
	private void initXListView() {
		progress=(CircleProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		mListView = (XListView) findViewById(R.id.list_wallpaper);
		mListView.setOnItemClickListener(this);
		// ���Ȳ�������ظ���
		mListView.setPullLoadEnable(false);
		// ��������
		mListView.setPullRefreshEnable(true);
		// ���ü�����
		mListView.setXListViewListener(this);
		mListView.pullRefreshing();
		adapter = new WallPaperAdapter(this, wallpapers);
		mListView.setAdapter(adapter);
		initWallPaperByList(false);
	}
	
	int curPage = 0;
	private void initWallPaperByList(final boolean isUpdate){
		if(!isUpdate){
			progress.setVisibility(View.VISIBLE);
		}
		BmobQuery<WallPapers> query = new BmobQuery<WallPapers>();
		query.order("-updatedAt");// ��ʱ��˳������
		query.findObjects(SetWallpaperActivity.this, new FindListener<WallPapers>() {

			@Override
			public void onSuccess(List<WallPapers> losts) {
				if (CollectionUtils.isNotNull(losts)) {
					if(isUpdate){
						wallpapers.clear();
					}
					adapter.addAll(losts);
					for(int i=0;i<losts.size();i++){
						wallpaperUris.add(losts.get(i).getPicture().getFileUrl(SetWallpaperActivity.this));
					}
					if(losts.size()<BRequest.QUERY_LIMIT_COUNT){
						mListView.setPullLoadEnable(false);
					}else{
						mListView.setPullLoadEnable(true);
					}
				}else{
					showTag("û���κα�ֽ!",Effects.flip,R.id.setwall);
					
				}
				if(!isUpdate){
					progress.setVisibility(View.GONE);
				}else{
					refreshPull();
				}
			}

			@Override
			public void onError(int code, String arg0) {
				mListView.setPullLoadEnable(false);
				if(!isUpdate){
					progress.setVisibility(View.GONE);
				}else{
					refreshPull();
				}
			}
		});
	}
	
	/** ��ѯ����
	  * @Title: queryMoreWallPaperList
	  * @Description: TODO
	  * @param @param page 
	  * @return void
	  * @throws
	  */
	private void queryMoreWallPaperList(int page){
		BmobQuery<WallPapers> query = new BmobQuery<WallPapers>();
		query.order("-updatedAt");// ��ʱ��˳������
		query.setSkip(page*10);
		query.findObjects(SetWallpaperActivity.this, new FindListener<WallPapers>() {

			@Override
			public void onSuccess(List<WallPapers> arg0) {
				// TODO Auto-generated method stub
				if (CollectionUtils.isNotNull(arg0)) {
					adapter.addAll(arg0);
					for(int i=0;i<arg0.size();i++){
						wallpaperUris.add(arg0.get(i).getPicture().getFileUrl(SetWallpaperActivity.this));
					}
				}
				refreshLoad();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowLog("��ѯ�����ֽ����:"+arg1);
				mListView.setPullLoadEnable(false);
				refreshLoad();
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
		final NiftyDialogBuilder dialogBuilder=new NiftyDialogBuilder(SetWallpaperActivity.this,R.style.dialog_untran);
        dialogBuilder
                .withTitle("��ʾ")                                 
                .withTitleColor("#FFFFFF")                              
                .withDividerColor("#11000000")                             
                .withMessage("���뽫�˱�ֽ��Ϊ��")                        
                .withMessageColor("#FFFFFF")                               
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(true)                       
                .withDuration(700)                                     
                .withEffect(Effectstype.Fliph)                        
                .withButton1Text("���챳��")                            
                .withButton2Text("�ռ䱳��")                              
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	User user=BmobUser.getCurrentUser(SetWallpaperActivity.this,User.class);
                		user.setWallpaperUri(wallpaperUris.get(arg2-1));
                		user.update(SetWallpaperActivity.this, user.getObjectId(), new UpdateListener() {
                			
                			@Override
                			public void onSuccess() {
                				showTag("�ѽ����챳������Ϊ����ѡ���ͼƬ",Effects.flip,R.id.setwall);
                			}
                			
                			@Override
                			public void onFailure(int arg0, String arg1) {
                				showTag("�������ó���"+arg1,Effects.flip,R.id.setwall);
                			}
                		});
        				dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	User user=BmobUser.getCurrentUser(SetWallpaperActivity.this,User.class);
                		user.setBackgroundUri(wallpaperUris.get(arg2-1));
                		user.update(SetWallpaperActivity.this, user.getObjectId(), new UpdateListener() {
                			
                			@Override
                			public void onSuccess() {
                				showTag("�ѽ��ռ䱳������Ϊ����ѡ���ͼƬ",Effects.flip,R.id.setwall);
                			}
                			
                			@Override
                			public void onFailure(int arg0, String arg1) {
                				showTag("�������ó���"+arg1,Effects.flip,R.id.setwall);
                			}
                		});
                    	dialogBuilder.dismiss();
                    }
                })
                .show();
	}

	@Override
	public void onRefresh() {
		initWallPaperByList(true);
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
		BmobQuery<WallPapers> query = new BmobQuery<WallPapers>();
		query.order("-updatedAt");// ��ʱ��˳������
		query.setLimit(1000);
		query.findObjects(SetWallpaperActivity.this, new FindListener<WallPapers>() {
			
			@Override
			public void onSuccess(List<WallPapers> arg0) {
				if(arg0.size() >wallpapers.size()){
					curPage++;
					queryMoreWallPaperList(curPage);
				}else{
					showTag("û�и����ֽ��",Effects.flip,R.id.setwall);
					mListView.setPullLoadEnable(false);
					refreshLoad();
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				ShowLog("��ѯ���б�ֽʧ��"+arg1);
				refreshLoad();
			}
		});
	}
	
	
}
