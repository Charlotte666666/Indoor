package com.indoor.im.bean;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class DailyArticle extends BmobObject{

	private String essay; // ��������

	public String getEssay() {
		return essay;
	}

	public void setEssay(String essay) {
		this.essay = essay;
	}

}
