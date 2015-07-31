package com.indoor.im.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Goods extends BmobObject{
	private static final long serialVersionUID = 1L;
	private String name;                // ��Ʒ��
	private String price;               // ��Ʒ�۸�
	private String describe;            // ��Ʒ����
	private BmobFile photo;             // ��Ʒ��ͼ
	private String[] category;          //��Ʒ������Ŀ
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public BmobFile getPhoto() {
		return photo;
	}
	public void setPhoto(BmobFile photo) {
		this.photo = photo;
	}
	public String[] getCategory() {
		return category;
	}
	public void setCategory(String[] category) {
		this.category = category;
	}
}
