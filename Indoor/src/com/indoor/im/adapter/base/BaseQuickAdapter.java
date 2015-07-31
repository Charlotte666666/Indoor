package com.indoor.im.adapter.base;

import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public abstract class BaseQuickAdapter<T, H extends BaseAdapterHelper> extends BaseAdapter {

	protected static final String TAG = BaseQuickAdapter.class.getSimpleName();
    protected final Context context;
	protected final int layoutResId;
	public final List<T> data; // �б�
	protected boolean displayIndeterminateProgress = false;
	
	public BaseQuickAdapter(Context context, int layoutResId) {
	this(context, layoutResId, null);
	}
	
	public BaseQuickAdapter(Context context, int layoutResId, List<T> data) {
	this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
	this.context = context;
	this.layoutResId = layoutResId;
	}
	
	@Override
	public int getCount() { // �õ�������
	int extra = displayIndeterminateProgress ? 1 : 0;
	return data.size() + extra;
	}
	
	@Override
	public T getItem(int position) { // �õ�������
	if (position >= data.size())
		return null;
	return data.get(position);
	}
	
	@Override
	// �õ�λ��
	public long getItemId(int position) {
	return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	if (getItemViewType(position) == 0) {
		final H helper = getAdapterHelper(position, convertView, parent);
		convert(helper, getItem(position));
		return helper.getView();
	}
	
	return createIndeterminateProgressView(convertView, parent);
	}
	
	private View createIndeterminateProgressView(View convertView,
		ViewGroup parent) {
	if (convertView == null) {
		FrameLayout container = new FrameLayout(context);
		container.setForegroundGravity(Gravity.CENTER);
		ProgressBar progress = new ProgressBar(context);
		container.addView(progress);
		convertView = container;
	}
	return convertView;
	}
	
	// ���������
	public void add(T elem) {
	data.add(elem);
	notifyDataSetChanged();
	}
	
	// ���һ���б��е�����������
	public void addAll(List<T> elem) {
	data.addAll(elem);
	notifyDataSetChanged();
	}
	
	// ɾ��������
	public void remove(T elem) {
	data.remove(elem);
	notifyDataSetChanged();
	}
	
	// ɾ��������
	public void remove(int index) {
	data.remove(index);
	notifyDataSetChanged();
	}
	
	// ����б�
	public void clear() {
	data.clear();
	notifyDataSetChanged();
	}
	
	protected abstract void convert(H helper, T item);
	
	protected abstract H getAdapterHelper(int position, View convertView,
		ViewGroup parent);

}

