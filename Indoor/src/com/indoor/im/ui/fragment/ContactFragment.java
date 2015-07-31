package com.indoor.im.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.indoor.im.R;
import com.indoor.im.CustomApplcation;
import com.indoor.im.adapter.UserFriendAdapter;
import com.indoor.im.bean.User;
import com.indoor.im.ui.AddFriendActivity;
import com.indoor.im.ui.FriendsMapActivity;
import com.indoor.im.ui.FriendsStatusActivity;
import com.indoor.im.ui.LivesActivity;
import com.indoor.im.ui.NearPeopleActivity;
import com.indoor.im.ui.NewFriendActivity;
import com.indoor.im.ui.SetMyInfoActivity;
import com.indoor.im.util.CharacterParser;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.util.PinyinComparator;
import com.indoor.im.view.ClearEditText;
import com.indoor.im.view.MyLetterView;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;
import com.indoor.im.view.MyLetterView.OnTouchingLetterChangedListener;
import com.indoor.more.FindMoreActivity;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

/**
 * ��ϵ��
 * @ClassName: ContactFragment
 * @Description: TODO
 * @author smile
 * @date 2014-6-7 ����1:02:05
 */
@SuppressLint({ "DefaultLocale", "InflateParams", "ClickableViewAccessibility" })
public class ContactFragment extends FragmentBase implements OnItemClickListener,OnItemLongClickListener{

	ClearEditText mClearEditText;

	TextView dialog;
	CircleProgressBar progress;
	ListView list_friends;
	MyLetterView right_letter;

	private UserFriendAdapter userAdapter;// ����

	List<User> friends = new ArrayList<User>();

	private InputMethodManager inputMethodManager;
	
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_contacts, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		init();
	}

	private void init() {
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		initTopBarForRight("��ϵ��", R.drawable.base_action_bar_add_bg_selector,
				new onRightImageButtonClickListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						startAnimActivity(AddFriendActivity.class);
					}
				});
		initListView();
		initRightLetterView();
		initEditText();
	}

	private void initEditText() {
		mClearEditText = (ClearEditText)findViewById(R.id.et_msg_search);
		// �������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// ������������ֵΪ�գ�����Ϊԭ�����б�������Ϊ���������б�
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}

	/**
	 * ����������е�ֵ���������ݲ�����ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<User> filterDateList = new ArrayList<User>();
		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = friends;
		} else {
			filterDateList.clear();
			for (User sortModel : friends) {
				String name = sortModel.getUsername();
				if (name != null) {
					if (name.indexOf(filterStr.toString()) != -1
							|| characterParser.getSelling(name).startsWith(
									filterStr.toString())) {
						filterDateList.add(sortModel);
					}
				}
			}
		}
		// ����a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		userAdapter.updateListView(filterDateList);
	}

	/**
	 * ΪListView�������
	 * @param date
	 * @return
	 */
	private void filledData(List<BmobChatUser> datas) {
		friends.clear();
		int total = datas.size();
		for (int i = 0; i < total; i++) {
			BmobChatUser user = datas.get(i);
			User sortModel = new User();
			sortModel.setAvatar(user.getAvatar());
			sortModel.setNick(user.getNick());
			sortModel.setUsername(user.getUsername());
			sortModel.setObjectId(user.getObjectId());
			sortModel.setContacts(user.getContacts());
			// ����ת����ƴ��
			String username = sortModel.getUsername();
			// ��û��username
			if (username != null) {
				String pinyin = characterParser.getSelling(sortModel.getUsername());
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// �������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}
			} else {
				sortModel.setSortLetters("#");
			}
			friends.add(sortModel);
		}
		// ����a-z��������
		Collections.sort(friends, pinyinComparator);
	}
	
	
	ImageView iv_msg_tips;
	TextView tv_new_name;
	LinearLayout layout_new;//������
	LinearLayout layout_near;//��������
	LinearLayout layout_map;//���ѷֲ�
	LinearLayout layout_friendsstatus;//����Ȧ
	LinearLayout layout_lives;//����Ȧ
	LinearLayout layout_findmore;//����
	
	private void initListView() {
		list_friends= (ListView)findViewById(R.id.list_friends);
		RelativeLayout headView = (RelativeLayout) mInflater.inflate(R.layout.include_new_friend, null);
		iv_msg_tips = (ImageView)headView.findViewById(R.id.iv_msg_tips);
		layout_new =(LinearLayout)headView.findViewById(R.id.layout_new);
		layout_near =(LinearLayout)headView.findViewById(R.id.layout_near);
		layout_map =(LinearLayout)headView.findViewById(R.id.layout_map);
		layout_friendsstatus =(LinearLayout)headView.findViewById(R.id.layout_friendsstatus);
		layout_lives =(LinearLayout)headView.findViewById(R.id.layout_lives);
		layout_findmore =(LinearLayout)headView.findViewById(R.id.layout_findmore);
		progress=(CircleProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		layout_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), NewFriendActivity.class);
				intent.putExtra("from", "contact");
				startAnimActivity(intent);
			}
		});
		layout_near.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), NearPeopleActivity.class);
				startAnimActivity(intent);
			}
		});
        layout_map.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), FriendsMapActivity.class);
				startAnimActivity(intent);
			}
		});
        layout_friendsstatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), FriendsStatusActivity.class);
				startAnimActivity(intent);
			}
		});
        layout_lives.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), LivesActivity.class);
				startAnimActivity(intent);
			}
		});
        layout_findmore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), FindMoreActivity.class);
				startAnimActivity(intent);
			}
		});
		
		list_friends.addHeaderView(headView);
	    userAdapter = new UserFriendAdapter(getActivity(), friends);
		list_friends.setAdapter(userAdapter);
		list_friends.setOnItemClickListener(this);
		list_friends.setOnItemLongClickListener(this);
		
		list_friends.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// ����������
				if (getActivity().getWindow().getAttributes().softInputMode != WindowManager
						.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getActivity().getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
		
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		if (isVisibleToUser) {
			queryMyfriends();
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	private void initRightLetterView() {
		right_letter = (MyLetterView)findViewById(R.id.right_letter);
		dialog = (TextView)findViewById(R.id.dialog);
		right_letter.setTextView(dialog);
		right_letter.setOnTouchingLetterChangedListener(new LetterListViewListener());
	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(String s) {
			// ����ĸ�״γ��ֵ�λ��
			int position = userAdapter.getPositionForSection(s.charAt(0));
			if (position != -1) {
				list_friends.setSelection(position);
			}
		}
	}

	/** ��ȡ�����б�
	  * queryMyfriends
	  * @return void
	  * @throws
	  */
	private void queryMyfriends() {
		//�Ƿ����µĺ�������
		if(BmobDB.create(getActivity()).hasNewInvite()){
			iv_msg_tips.setVisibility(View.VISIBLE);
		}else{
			iv_msg_tips.setVisibility(View.GONE);
		}
		//����������һ�α��صĺ������ݿ�ļ�飬��Ϊ�˱��غ������ݿ����Ѿ������˶Է������ǽ���ȴû����ʾ����������
		// �����������ڴ��б���ĺ����б�
		CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(BmobDB
				.create(getActivity()).getContactList()));
	
		Map<String,BmobChatUser> users = CustomApplcation.getInstance().getContactList();
		//��װ�µ�User
		filledData(CollectionUtils.map2list(users));
		if(userAdapter==null){
			userAdapter = new UserFriendAdapter(getActivity(), friends);
			list_friends.setAdapter(userAdapter);
		}else{
			userAdapter.notifyDataSetChanged();
		}

	}
	
	private boolean hidden;
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if(!hidden){
			refresh();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(!hidden){
			refresh();
		}
	}
	
	public void refresh(){
		try {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					queryMyfriends();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		User user = (User) userAdapter.getItem(position-1);
		//�Ƚ�����ѵ���ϸ����ҳ��
		Intent intent =new Intent(getActivity(),SetMyInfoActivity.class);
		intent.putExtra("from", "other");
		intent.putExtra("username", user.getUsername());
		startAnimActivity(intent);
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		User user = (User) userAdapter.getItem(position-1);
		showDeleteDialog(user);
		return true;
	}
	
	public void showDeleteDialog(final User user) {
		final NiftyDialogBuilder dialogBuilder=new NiftyDialogBuilder(getActivity(),R.style.dialog_untran);
        dialogBuilder
                .withTitle(user.getUsername())                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("ɾ����ϵ��")                             //.withMessage(null)  no Msg
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
                    	deleteContact(user);
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
	
	 /** ɾ����ϵ��
	  * deleteContact
	  * @return void
	  * @throws
	  */
	private void deleteContact(final User user){
		progress.setVisibility(View.VISIBLE);
		userManager.deleteContact(user.getObjectId(), new UpdateListener() {
			
			@Override
			public void onSuccess() {
				//ɾ���ڴ�
				CustomApplcation.getInstance().getContactList().remove(user.getUsername());
				//���½���
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						progress.setVisibility(View.GONE);
						userAdapter.remove(user);
					}
				});
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(getActivity().getApplicationContext(), "ɾ��ʧ�ܣ�"+arg1, Toast.LENGTH_SHORT).show();
				progress.setVisibility(View.GONE);
			}
		});
	}

}