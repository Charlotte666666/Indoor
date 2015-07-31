package com.indoor.im.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.indoor.im.R;
import com.indoor.im.CustomApplcation;
import com.indoor.im.adapter.BlackListAdapter;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.HeaderLayout;

/**
 * �������б�
 * 
 * @ClassName: BlackListActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-24 ����5:17:50
 */
public class BlackListActivity extends ActivityBase implements OnItemClickListener {

	ListView listview;
	BlackListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacklist);
		initView();
	}

	private void initView() {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		initTopBarForLeft("������");
		adapter = new BlackListAdapter(this, BmobDB.create(this).getBlackList());
		listview = (ListView) findViewById(R.id.list_blacklist);
		listview.setOnItemClickListener(this);
		listview.setAdapter(adapter);
	}

	/** ��ʾ�Ƴ��������Ի���
	  * @Title: showRemoveBlackDialog
	  * @Description: TODO
	  * @param @param position
	  * @param @param invite 
	  * @return void
	  * @throws
	  */
	public void showRemoveBlackDialog(final int position, final BmobChatUser user) {
		final NiftyDialogBuilder dialogBuilder=new NiftyDialogBuilder(this,R.style.dialog_untran);
        dialogBuilder
                .withTitle("�Ƴ�������")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("��ȷ����"+user.getUsername()+"�Ƴ���������?")                             //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFF")                                //def
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(Effectstype.Flipv)                                         //def Effectstype.Slidetop
                .withButton1Text("ȷ��")                                      //def gone
                .withButton2Text("ȡ��")                                  //def gone
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	adapter.remove(position);
        				userManager.removeBlack(user.getUsername(),new UpdateListener() {
        					
        					@Override
        					public void onSuccess() {
        						showTag("�Ƴ��������ɹ�",Effects.scale,R.id.blacklist);
        						//�����������ڴ��б���ĺ����б�
        						CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(getApplicationContext()).getContactList()));	
        						dialogBuilder.dismiss();
        					}
        					
        					@Override
        					public void onFailure(int arg0, String arg1) {
        						showTag("�Ƴ�������ʧ��:"+arg1,Effects.scale,R.id.blacklist);
        						dialogBuilder.dismiss();
        					}
        				});	
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		BmobChatUser invite = (BmobChatUser) adapter.getItem(arg2);
		showRemoveBlackDialog(arg2,invite);
	}


}
