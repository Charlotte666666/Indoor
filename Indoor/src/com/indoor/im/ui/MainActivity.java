package com.indoor.im.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.CustomApplcation;
import com.indoor.im.MyMessageReceiver;
import com.indoor.im.R;
import com.indoor.im.ui.fragment.ContactFragment;
import com.indoor.im.ui.fragment.NewsmediaFragment;
import com.indoor.im.ui.fragment.Online_shoppingFragment;
import com.indoor.im.ui.fragment.RecentFragment;
import com.indoor.im.ui.fragment.SettingsFragment;

/**
 * ��½
 * @ClassName: MainActivity
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 ����2:45:35
 */
public class MainActivity extends ActivityBase implements EventListener{

	private Button[] mTabs;
	private ContactFragment contactFragment;
	private RecentFragment recentFragment;
	private NewsmediaFragment newsmediaFragment;
	private Online_shoppingFragment online_shoppingFragment;
	private SettingsFragment settingFragment;
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	
	ImageView iv_recent_tips,iv_contact_tips;//��Ϣ��ʾ
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//������ʱ�����񣨵�λΪ�룩-���������̨�Ƿ���δ������Ϣ���еĻ���ȡ����
		//�������ü�����ȽϺ������͵�������Ҳ����ȥ����仰-ͬʱ����onDestory���������stopPollService����
        //BmobChat.getInstance(this).startPollService(30);
		//�����㲥������
		initNewMessageBroadCast();
		initTagMessageBroadCast();
		initView();
		initTab();
	}

	private void initView(){
		mTabs = new Button[5];
		mTabs[0] = (Button) findViewById(R.id.btn_message);
		mTabs[1] = (Button) findViewById(R.id.btn_contract);
		mTabs[2] = (Button) findViewById(R.id.btn_newsmedia);
		mTabs[3] = (Button) findViewById(R.id.btn_shopping);
		mTabs[4] = (Button) findViewById(R.id.btn_set);
		iv_recent_tips = (ImageView)findViewById(R.id.iv_recent_tips);
		iv_contact_tips = (ImageView)findViewById(R.id.iv_contact_tips);
		//�ѵ�һ��tab��Ϊѡ��״̬
		mTabs[0].setSelected(true);
	}
	
	private void initTab(){
		recentFragment = new RecentFragment();
		contactFragment = new ContactFragment();
		newsmediaFragment=new NewsmediaFragment();
		online_shoppingFragment=new Online_shoppingFragment();
		settingFragment = new SettingsFragment();
		fragments = new Fragment[] {recentFragment,contactFragment,newsmediaFragment,online_shoppingFragment,settingFragment };
		// ������ʾ��һ��fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, recentFragment).
			add(R.id.fragment_container, contactFragment).hide(contactFragment).show(recentFragment).commit();
	}
	
	
	/**
	 * button����¼�
	 * @param view
	 */
	public void onTabSelect(View view) {
		switch (view.getId()) {
		case R.id.btn_message:
			index = 0;
			break;
		case R.id.btn_contract:
			index = 1;
			break;
		case R.id.btn_newsmedia:
			index = 2;
			break;	
		case R.id.btn_shopping:
			index = 3;
			break;	
		case R.id.btn_set:
			index = 4;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		//�ѵ�ǰtab��Ϊѡ��״̬
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	@Override
	protected void onResume() {
		super.onResume();
		//СԲ����ʾ
		if(BmobDB.create(this).hasUnReadMsg()){
			iv_recent_tips.setVisibility(View.VISIBLE);
		}else{
			iv_recent_tips.setVisibility(View.GONE);
		}
		if(BmobDB.create(this).hasNewInvite()){
			iv_contact_tips.setVisibility(View.VISIBLE);
		}else{
			iv_contact_tips.setVisibility(View.GONE);
		}
		MyMessageReceiver.ehList.add(this);// �������͵���Ϣ
		//���
		MyMessageReceiver.mNewNum=0;	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MyMessageReceiver.ehList.remove(this);// ȡ���������͵���Ϣ
	}
	
	@Override
	public void onMessage(BmobMsg message) {
		refreshNewMsg(message);
	}
	
	
	/** ˢ�½���
	  * @Title: refreshNewMsg
	  * @Description: TODO
	  * @param @param message 
	  * @return void
	  * @throws
	  */
	private void refreshNewMsg(BmobMsg message){
		// ������ʾ
		boolean isAllow = CustomApplcation.getInstance().getSpUtil().isAllowVoice();
		if(isAllow){
			CustomApplcation.getInstance().getMediaPlayer().start();
		}
		iv_recent_tips.setVisibility(View.VISIBLE);
		//ҲҪ�洢����
		if(message!=null){
			BmobChatManager.getInstance(MainActivity.this).saveReceiveMessage(true,message);
		}
		if(currentTabIndex==0){
			//��ǰҳ�����Ϊ�Ựҳ�棬ˢ�´�ҳ��
			if(recentFragment != null){
				recentFragment.refresh();
			}
		}
	}
	
	NewBroadcastReceiver  newReceiver;
	
	private void initNewMessageBroadCast(){
		// ע�������Ϣ�㲥
		newReceiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
		//���ȼ�Ҫ����ChatActivity
		intentFilter.setPriority(3);
		registerReceiver(newReceiver, intentFilter);
	}
	
	/**
	 * ����Ϣ�㲥������
	 * 
	 */
	private class NewBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//ˢ�½���
			refreshNewMsg(null);
			// �ǵðѹ㲥���ս��
			abortBroadcast();
		}
	}
	
	TagBroadcastReceiver  userReceiver;
	
	private void initTagMessageBroadCast(){
		// ע�������Ϣ�㲥
		userReceiver = new TagBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_ADD_USER_MESSAGE);
		//���ȼ�Ҫ����ChatActivity
		intentFilter.setPriority(3);
		registerReceiver(userReceiver, intentFilter);
	}
	
	/**
	 * ��ǩ��Ϣ�㲥������
	 */
	private class TagBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			BmobInvitation message = (BmobInvitation) intent.getSerializableExtra("invite");
			refreshInvite(message);
			// �ǵðѹ㲥���ս��
			abortBroadcast();
		}
	}
	
	@Override
	public void onNetChange(boolean isNetConnected) {
		if(isNetConnected){
			showTag("��ǰ���粻����,������������!",Effects.standard,R.id.main_bottom);
		}
	}

	@Override
	public void onAddUser(BmobInvitation message) {
		refreshInvite(message);
	}
	
	/** ˢ�º�������
	  * @Title: notifyAddUser
	  * @Description: TODO
	  * @param @param message 
	  * @return void
	  * @throws
	  */
	private void refreshInvite(BmobInvitation message){
		boolean isAllow = CustomApplcation.getInstance().getSpUtil().isAllowVoice();
		if(isAllow){
			CustomApplcation.getInstance().getMediaPlayer().start();
		}
		iv_contact_tips.setVisibility(View.VISIBLE);
		if(currentTabIndex==1){
			if(contactFragment != null){
				contactFragment.refresh();
			}
		}else{
			//ͬʱ����֪ͨ
			String tickerText = message.getFromname()+"�������Ӻ���";
			boolean isAllowVibrate = CustomApplcation.getInstance().getSpUtil().isAllowVibrate();
			BmobNotifyManager.getInstance(this).showNotify(isAllow,isAllowVibrate,R.drawable.ic_launcher,
					tickerText, message.getFromname(), tickerText.toString(),NewFriendActivity.class);
		}
	}

	@Override
	public void onOffline() {
		showOfflineDialog(this);
	}
	
	@Override
	public void onReaded(String conversionId, String msgTime) {
	}
	
	
	private static long firstTime;
	/**
	 * ���������η��ؼ����˳�
	 */
	@Override
	public void onBackPressed() {
		if (firstTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			showTag("�ٰ�һ���˳�����",Effects.standard,R.id.main_bottom);
		}
		firstTime = System.currentTimeMillis();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(newReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(userReceiver);
		} catch (Exception e) {
		}
		//ȡ����ʱ������
//		BmobChat.getInstance(this).stopPollService();
	}
	
}