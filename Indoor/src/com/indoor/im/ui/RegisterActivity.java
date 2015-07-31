package com.indoor.im.ui;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.bean.User;
import com.indoor.im.config.BmobConstants;
import com.indoor.im.util.CommonUtils;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class RegisterActivity extends BaseActivity {

	Button btn_register;
	EditText et_username, et_password, et_email;
	BmobChatUser currentUser;
	CircleProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initTopBarForLeft("ע��");

		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_email = (EditText) findViewById(R.id.et_email);
		progress=(CircleProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				register();
			}
		});
		//checkUser();
	}
	
	
	private void checkUser(){
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("username", et_username.getText().toString());
		query.findObjects(this, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(List<User> arg0) {
				if(arg0!=null && arg0.size()>0){
					User user = arg0.get(0);
					user.setPassword(et_email.getText().toString());
					user.update(RegisterActivity.this, new UpdateListener() {
						
						@Override
						public void onSuccess() {
							userManager.login(et_username.getText().toString(), et_email.getText().toString(), new SaveListener() {
								
								@Override
								public void onSuccess() {
									Log.i("smile", "��½�ɹ�");
								}
								
								@Override
								public void onFailure(int code, String msg) {
									Log.i("smile", "��½ʧ�ܣ�"+code+".msg = "+msg);
								}
							});
						}
						
						@Override
						public void onFailure(int code, String msg) {
						}
					});
				}
			}
		});
	}
	
	private void register(){
		String name = et_username.getText().toString();
		String password = et_password.getText().toString();
		String pwd_again = et_email.getText().toString();
		
		if (TextUtils.isEmpty(name)) 
		{
			showTag("�û�������Ϊ��",Effects.thumbSlider,R.id.register);
			return;
		}

		if (TextUtils.isEmpty(password)) {
			showTag("���벻��Ϊ��",Effects.thumbSlider,R.id.register);
			return;
		}
		if (!pwd_again.equals(password)) {
			showTag("������������벻һ��",Effects.thumbSlider,R.id.register);
			return;
		}
		
		boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
		if(!isNetConnected){
			showTag("��ǰ���粻����,������������!",Effects.thumbSlider,R.id.register);
			return;
		}
		
		progress.setVisibility(View.VISIBLE);
		//����ÿ��Ӧ�õ�ע����������϶���һ������IM sdkδ�ṩע�᷽�����û��ɰ���bmod SDK��ע�᷽ʽ����ע�ᡣ
		//ע���ʱ����Ҫע�����㣺1��User���а��豸id��type��2���豸���а�username�ֶ�
		final User bu = new User();
		bu.setUsername(name);
		bu.setPassword(password);
		//��user���豸id���а�aa
		bu.setSex(true);
		bu.setDeviceType("android");
		bu.setInstallId(BmobInstallation.getInstallationId(this));
		bu.signUp(RegisterActivity.this, new SaveListener() {

			@Override
			public void onSuccess() {
				progress.setVisibility(View.GONE);
				showTag("ע��ɹ�",Effects.thumbSlider,R.id.register);
				// ���豸��username���а�
				userManager.bindInstallationForRegister(bu.getUsername());
				//���µ���λ����Ϣ
				updateUserLocation();
				
				//���㲥֪ͨ��½ҳ���˳�
				sendBroadcast(new Intent(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH));
				// ������ҳ
				Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				BmobLog.i(arg1);
				showTag("ע��ʧ��:"+ arg1,Effects.thumbSlider,R.id.register);
				progress.setVisibility(View.GONE);
			}
		});
	}

}
