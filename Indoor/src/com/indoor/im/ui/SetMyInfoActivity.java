package com.indoor.im.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.indoor.im.R;
import com.indoor.im.CustomApplcation;
import com.indoor.im.bean.User;
import com.indoor.im.config.BmobConstants;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.util.ImageLoadOptions;
import com.indoor.im.util.PhotoUtil;
import com.indoor.im.view.RoundImageView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * ��������ҳ��
 * 
 * @ClassName: SetMyInfoActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-10 ����2:55:19
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint({ "SimpleDateFormat", "ClickableViewAccessibility", "InflateParams" })
public class SetMyInfoActivity extends ActivityBase implements OnClickListener {

	TextView tv_set_name, tv_set_nick, tv_set_gender;
	RoundImageView iv_set_avator;
	ImageView iv_arraw, iv_nickarraw;
	LinearLayout layout_all;
	CircleProgressBar progress;
	Button btn_chat, btn_back, btn_add_friend;
	RelativeLayout layout_head, layout_nick, layout_gender, layout_black_tips;

	String from = "";
	String username = "";
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��Ϊ�����ֻ���������������ĵ�����ť����Ҫ�������ص�����Ȼ���ڵ����պ����������ť������setContentView֮ǰ���ò�����Ч
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 14) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
		setContentView(R.layout.activity_set_info);
		from = getIntent().getStringExtra("from");//me add other
		username = getIntent().getStringExtra("username");
		initView();
	}

	private void initView() {
		layout_all = (LinearLayout) findViewById(R.id.layout_all);
		iv_set_avator = (RoundImageView) findViewById(R.id.iv_set_avator);
		iv_arraw = (ImageView) findViewById(R.id.iv_arraw);
		iv_nickarraw = (ImageView) findViewById(R.id.iv_nickarraw);
		tv_set_name = (TextView) findViewById(R.id.tv_set_name);
		tv_set_nick = (TextView) findViewById(R.id.tv_set_nick);
		layout_head = (RelativeLayout) findViewById(R.id.layout_head);
		layout_nick = (RelativeLayout) findViewById(R.id.layout_nick);
		layout_gender = (RelativeLayout) findViewById(R.id.layout_gender);
		// ��������ʾ��
		layout_black_tips = (RelativeLayout) findViewById(R.id.layout_black_tips);
		tv_set_gender = (TextView) findViewById(R.id.tv_set_gender);
		btn_chat = (Button) findViewById(R.id.btn_chat);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
		btn_add_friend.setEnabled(false);
		btn_chat.setEnabled(false);
		btn_back.setEnabled(false);
		progress=(CircleProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		if (from.equals("me")) {
			initTopBarForLeft("��������");
			layout_head.setOnClickListener(this);
			layout_nick.setOnClickListener(this);
			layout_gender.setOnClickListener(this);
			iv_nickarraw.setVisibility(View.VISIBLE);
			iv_arraw.setVisibility(View.VISIBLE);
			btn_back.setVisibility(View.GONE);
			btn_chat.setVisibility(View.GONE);
			btn_add_friend.setVisibility(View.GONE);
		} else {
			initTopBarForLeft("��ϸ����");
			iv_nickarraw.setVisibility(View.INVISIBLE);
			iv_arraw.setVisibility(View.INVISIBLE);
			//���ܶԷ��ǲ�����ĺ��ѣ������Է�����Ϣ--BmobIM_V1.1.2�޸�
			btn_chat.setVisibility(View.VISIBLE);
			btn_chat.setOnClickListener(this);
			if (from.equals("add")) {// �Ӹ��������б���Ӻ���--��Ϊ��ȡ�������˵ķ����������Ƿ���ʾ���ѵ�����������������Ҫ�ж�������û��Ƿ����Լ��ĺ���
				if (mApplication.getContactList().containsKey(username)) {// �Ǻ���
//					btn_chat.setVisibility(View.VISIBLE);
//					btn_chat.setOnClickListener(this);
					btn_back.setVisibility(View.VISIBLE);
					btn_back.setOnClickListener(this);
				} else {
//					btn_chat.setVisibility(View.GONE);
					btn_back.setVisibility(View.GONE);
					btn_add_friend.setVisibility(View.VISIBLE);
					btn_add_friend.setOnClickListener(this);
				}
			} else {// �鿴����
//				btn_chat.setVisibility(View.VISIBLE);
//				btn_chat.setOnClickListener(this);
				btn_back.setVisibility(View.VISIBLE);
				btn_back.setOnClickListener(this);
			}
			initOtherData(username);
		}
	}

	private void initMeData() {
		User user = userManager.getCurrentUser(User.class);
		//BmobLog.i("hight = "+user.getHight()+",sex= "+user.getSex());
		initOtherData(user.getUsername());
	}

	private void initOtherData(String name) {
		userManager.queryUser(name, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowLog("onError onError:" + arg1);
			}

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null && arg0.size() > 0) {
					user = arg0.get(0);
					btn_chat.setEnabled(true);
					btn_back.setEnabled(true);
					btn_add_friend.setEnabled(true);
					updateUser(user);
				} else {
					ShowLog("onSuccess ���޴���");
				}
			}
		});
	}

	private void updateUser(User user) {
		// ����
		refreshAvatar(user.getAvatar());
		tv_set_name.setText(user.getUsername());
		tv_set_nick.setText(user.getNick());
		tv_set_gender.setText(user.getSex() == true ? "��" : "Ů");
		// ����Ƿ�Ϊ�������û�
		if (from.equals("other")) {
			if (BmobDB.create(this).isBlackUser(user.getUsername())) {
				btn_back.setVisibility(View.GONE);
				layout_black_tips.setVisibility(View.VISIBLE);
			} else {
				btn_back.setVisibility(View.VISIBLE);
				layout_black_tips.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * ����ͷ�� refreshAvatar
	 * 
	 * @return void
	 * @throws
	 */
	private void refreshAvatar(String avatar) {
		if (avatar != null && !avatar.equals("")) {
			ImageLoader.getInstance().displayImage(avatar, iv_set_avator,
					ImageLoadOptions.getOptions());
		} else {
			iv_set_avator.setImageResource(R.drawable.default_head);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (from.equals("me")) {
			initMeData();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_chat:// ��������
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra("user", user);
			startAnimActivity(intent);
			finish();
			break;
		case R.id.layout_head:
			showAvatarPop();
			break;
		case R.id.layout_nick:
			startAnimActivity(UpdateInfoActivity.class);
//			addBlog();
			break;
		case R.id.layout_gender:// �Ա�
			showSexChooseDialog();
			break;
		case R.id.btn_back:// ������
			showBlackDialog(user.getUsername());
			break;
		case R.id.btn_add_friend://��Ӻ���
			addFriend();
			break;
		}
	}
	
	String[] sexs = new String[]{ "��", "Ů" };
	private void showSexChooseDialog() {
		final NiftyDialogBuilder dialogBuilder=new NiftyDialogBuilder(this,R.style.dialog_untran);
        dialogBuilder
                .withTitle("ѡ�������Ա�")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(Effectstype.RotateLeft)                                         //def Effectstype.Slidetop
                .withButton1Text("��")                                      //def gone
                .withButton2Text("Ů") 
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	updateInfo(0);
                    	dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	updateInfo(1);
                    	dialogBuilder.dismiss();
                    }
                })
                .show();
        
	}

	
	/** �޸�����
	  * updateInfo
	  * @Title: updateInfo
	  * @return void
	  * @throws
	  */
	private void updateInfo(int which) {
		final User u = new User();
		if(which==0){
			u.setSex(true);
		}else{
			u.setSex(false);
		}
		updateUserData(u,new UpdateListener() {

			@Override
			public void onSuccess() {
				showTag("�޸ĳɹ�",Effects.flip,R.id.layout_head);
				tv_set_gender.setText(u.getSex() == true ? "��" : "Ů");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				showTag("onFailure:" + arg1,Effects.flip,R.id.layout_head);
			}
		});
	}
	/**
	 * ��Ӻ�������
	 * 
	 * @Title: addFriend
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void addFriend() {
		progress.setVisibility(View.VISIBLE);
		// ����tag����
		BmobChatManager.getInstance(this).sendTagMessage(BmobConfig.TAG_ADD_CONTACT,
				user.getObjectId(), new PushListener() {

					@Override
					public void onSuccess() {
						progress.setVisibility(View.GONE);
						showTag("��������ɹ����ȴ��Է���֤��",Effects.flip,R.id.layout_head);
					}

					@Override
					public void onFailure(int arg0, final String arg1) {
						progress.setVisibility(View.GONE);
						ShowLog("��������ʧ��:" + arg1);
					}
				});
	}

	/**
	 * ��ʾ��������ʾ��
	 * 
	 * @Title: showBlackDialog
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void showBlackDialog(final String username) {
		final NiftyDialogBuilder dialogBuilder=new NiftyDialogBuilder(this,R.style.dialog_untran);
        dialogBuilder
                .withTitle("���������")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("��������������������յ��Է�����Ϣ��ȷ��Ҫ������")                             //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFF")                                //def
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(Effectstype.Shake)                                         //def Effectstype.Slidetop
                .withButton1Text("ȷ��")                                      //def gone
                .withButton2Text("ȡ��")                                  //def gone
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	// ��ӵ��������б�
        				userManager.addBlack(username, new UpdateListener() {

        					@Override
        					public void onSuccess() {
        						showTag("��������ӳɹ�!",Effects.flip,R.id.layout_head);
        						btn_back.setVisibility(View.GONE);
        						layout_black_tips.setVisibility(View.VISIBLE);
        						// �����������ڴ��б���ĺ����б�
        						CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(BmobDB
        								.create(SetMyInfoActivity.this).getContactList()));
        						dialogBuilder.dismiss();
        					}

        					@Override
        					public void onFailure(int arg0, String arg1) {
        						showTag("���������ʧ��:" + arg1,Effects.flip,R.id.layout_head);
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

	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	PopupWindow avatorPop;

	public String filePath = "";

	@SuppressWarnings("deprecation")
	private void showAvatarPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_showavator,
				null);
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowLog("�������");
				// TODO Auto-generated method stub
				layout_choose.setBackgroundColor(getResources().getColor(
						R.color.base_color_text_white));
				layout_photo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				File dir = new File(BmobConstants.MyAvatarDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// ԭͼ
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()));
				filePath = file.getAbsolutePath();// ��ȡ��Ƭ�ı���·��
				Uri imageUri = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA);
			}
		});
		layout_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ShowLog("������");
				layout_photo.setBackgroundColor(getResources().getColor(
						R.color.base_color_text_white));
				layout_choose.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION);
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
		avatorPop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});

		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchable(true);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// ����Ч�� �ӵײ�����
		avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
		avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
	}

	/**
	 * @Title: startImageAction
	 * @return void
	 * @throws
	 */
	private void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	Bitmap newBitmap;
	boolean isFromCamera = false;// ����������ת
	int degree = 0;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA:// �����޸�ͷ��
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					showTag("SD��������",Effects.flip,R.id.layout_head);
					return;
				}
				isFromCamera = true;
				File file = new File(filePath);
				degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
				Log.i("life", "���պ�ĽǶȣ�" + degree);
				startImageAction(Uri.fromFile(file), 200, 200,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			}
			break;
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION:// �����޸�ͷ��
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			Uri uri = null;
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					showTag("SD��������",Effects.flip,R.id.layout_head);
					return;
				}
				isFromCamera = false;
				uri = data.getData();
				startImageAction(uri, 200, 200,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			} else {
				showTag("��Ƭ��ȡʧ��",Effects.flip,R.id.layout_head);
			}

			break;
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP:// �ü�ͷ�񷵻�
			// TODO sent to crop
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			if (data == null) {
				// Toast.makeText(this, "ȡ��ѡ��", Toast.LENGTH_SHORT).show();
				return;
			} else {
				saveCropAvator(data);
			}
			// ��ʼ���ļ�·��
			filePath = "";
			// �ϴ�ͷ��
			uploadAvatar();
			break;
		default:
			break;

		}
	}

	private void uploadAvatar() {
		BmobLog.i("ͷ���ַ��" + path);
		final BmobFile bmobFile = new BmobFile(new File(path));
		bmobFile.upload(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				String url = bmobFile.getFileUrl(SetMyInfoActivity.this);
				// ����BmobUser����
				updateUserAvatar(url);
			}

			@Override
			public void onProgress(Integer arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int arg0, String msg) {
				showTag("ͷ���ϴ�ʧ�ܣ�" + msg,Effects.flip,R.id.layout_head);
			}
		});
	}

	private void updateUserAvatar(final String url) {
		User  u =new User();
		u.setAvatar(url);
		updateUserData(u,new UpdateListener() {
			@Override
			public void onSuccess() {
				showTag("ͷ����³ɹ���",Effects.flip,R.id.layout_head);
				// ����ͷ��
				refreshAvatar(url);
			}

			@Override
			public void onFailure(int code, String msg) {
				showTag("ͷ�����ʧ�ܣ�" + msg,Effects.flip,R.id.layout_head);
			}
		});
	}

	String path;

	/**
	 * ����ü���ͷ��
	 * 
	 * @param data
	 */
	private void saveCropAvator(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
				if (isFromCamera && degree != 0) {
					bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
				}
				iv_set_avator.setImageBitmap(bitmap);
				// ����ͼƬ
				String filename = new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date())+".png";
				path = BmobConstants.MyAvatarDir + filename;
				PhotoUtil.saveBitmap(BmobConstants.MyAvatarDir, filename,
						bitmap, true);
				// �ϴ�ͷ��
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}
	
	private void updateUserData(User user,UpdateListener listener){
		User current = (User) userManager.getCurrentUser(User.class);
		user.setObjectId(current.getObjectId());
		user.update(this, listener);
	}

}
