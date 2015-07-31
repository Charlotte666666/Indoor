package com.indoor.im.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.adapter.base.BaseListAdapter;
import com.indoor.im.adapter.base.ViewHolder;
import com.indoor.im.bean.Blog;
import com.indoor.im.bean.User;
import com.indoor.im.util.ImageLoadOptions;
import com.indoor.im.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/** �����ǵĶ�̬
  * @ClassName: FriendsStatusAdapter
  * @Description: TODO
  * @author left
  * @date 2015-6-1 ����22:36:17
  */
public class FriendsStatusAdapter extends BaseListAdapter<Blog> {

	public FriendsStatusAdapter(Context context, List<Blog> list) {
		super(context, list);
	}
	
	private String headuri;
	private int likesnum=0;
	
	@Override
	public View bindView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_friendsstatus, null);
		}
		
		final String[] islike=new String[1];//������¼�Ƿ��޹�����΢��
		islike[0]="false";
		final Blog blog = getList().get(arg0);
	    final Context context=convertView.getContext();
		TextView brief = ViewHolder.get(convertView, R.id.edittext);
		TextView time = ViewHolder.get(convertView, R.id.time);
		final TextView name = ViewHolder.get(convertView, R.id.name);
		final TextView num = ViewHolder.get(convertView, R.id.num);
		final RoundImageView head=ViewHolder.get(convertView, R.id.head);
		ImageView photo = ViewHolder.get(convertView, R.id.photo);
		final ImageView like = ViewHolder.get(convertView, R.id.like);
		final User user = BmobUser.getCurrentUser(context, User.class);
		like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(islike[0].equals("false")){
					//����ǰ�û���ӵ�Blog���е�likes�ֶ�ֵ�У�������ǰ�û�ϲ��������
					BmobRelation relation = new BmobRelation();
					//����ǰ�û���ӵ���Զ������
					relation.add(user);
					//��Զ����ָ��`blog`��`likes`�ֶ�
					blog.setLikes(relation);
					blog.update(context, new UpdateListener() {

					    @Override
					    public void onSuccess() {
					    	likesnum++;
					    	islike[0]="true";
					    	num.setText(likesnum+"");
					    }

					    @Override
					    public void onFailure(int arg0, String arg1) {
					       
					    }
					});
				}
				else{
					showTag("�����޹�������̬��", Effects.standard, R.id.friends_status);
				}
			}
		});
		
		String photouri = blog.getPhoto().getFileUrl(convertView.getContext());
		if (photouri != null && !photouri.equals("")) {
			ImageLoader.getInstance().displayImage(photouri, photo, ImageLoadOptions.getOptions());
		} else {
			photo.setImageResource(R.drawable.defalutimage);
		}
		
		//��Ϊ�޷�ֱ��get��user��ͷ��������Ҫ������ȥ��
		String uID = blog.getAuthor().getObjectId();
		
		BmobQuery<User> query = new BmobQuery<User>();
		query.getObject(convertView.getContext(), uID, new GetListener<User>() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				
			}
			
			@Override
			public void onSuccess(User arg0) {
				headuri=arg0.getAvatar();
				if (headuri != null && !headuri.equals("")) {
					ImageLoader.getInstance().displayImage(headuri, head, ImageLoadOptions.getOptions());
				} else {
					head.setImageResource(R.drawable.default_head);
				}
				name.setText(arg0.getUsername());
			}
		});
		// ��ѯϲ��������ӵ������û�����˲�ѯ�����û���
		BmobQuery<User> query2 = new BmobQuery<User>();
		//likes��Blog���е��ֶΣ������洢����ϲ�������ӵ��û�
		query2.addWhereRelatedTo("likes", new BmobPointer(blog));    
		query2.findObjects(convertView.getContext(), new FindListener<User>() {

		    @Override
		    public void onSuccess(List<User> object) {
		    	likesnum=object.size();
		    	num.setText(likesnum+"");
		    	for(int i=0;i<object.size();i++){
		    		if(object.get(i).getObjectId().equals(user.getObjectId())){
			    		islike[0]="true";
			    		like.setImageResource(R.drawable.youlike);
			    	}
		    	}
		    }

		    @Override
		    public void onError(int code, String msg) {
		       
		    }
		});
		brief.setText(blog.getBrief());
		time.setText(blog.getCreatedAt());
		return convertView;
	}

}
