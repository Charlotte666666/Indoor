package com.indoor.im.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Blog extends BmobObject {

	private static final long serialVersionUID = 1L;

	private String brief;
	
	private User author;//���ӵķ����ߣ��������ֵ���һ��һ�Ĺ�ϵ��һ���û�����һ������

    private BmobRelation likes;//��Զ��ϵ�����ڴ洢ϲ�������ӵ������û�
	
	private BmobFile photo; //����ͼƬ

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public BmobFile getPhoto() {
		return photo;
	}

	public void setPhoto(BmobFile photo) {
		this.photo = photo;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public BmobRelation getLikes() {
		return likes;
	}

	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}

}
