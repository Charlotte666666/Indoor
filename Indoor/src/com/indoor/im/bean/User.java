package com.indoor.im.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/** ����BmobChatUser����������������Ҫ���ӵ����Կ��ڴ����
  * @ClassName: TextUser
  * @Description: TODO
  * @author smile
  * @date 2014-5-29 ����6:15:45
  */
public class User extends BmobChatUser {

	private static final long serialVersionUID = 1L;
	
	/**
	 * //��ʾ����ƴ��������ĸ
	 */
	private String sortLetters;
	
	/**
	 * //�Ա�-true-��
	 */
	private Boolean sex;
	
	/**
	 * ��������
	 */
	private BmobGeoPoint location;//
	
	private String wallpaperUri;
	
	private String backgroundUri;
	
	public BmobGeoPoint getLocation() {
		return location;
	}
	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}
	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public String getWallpaperUri() {
		return wallpaperUri;
	}
	public void setWallpaperUri(String wallpaperUri) {
		this.wallpaperUri = wallpaperUri;
	}
	public String getBackgroundUri() {
		return backgroundUri;
	}
	public void setBackgroundUri(String backgroundUri) {
		this.backgroundUri = backgroundUri;
	}
	
}
